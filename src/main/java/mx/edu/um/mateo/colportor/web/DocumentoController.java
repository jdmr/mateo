/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.ColportorDao;
import mx.edu.um.mateo.colportor.dao.DocumentoDao;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.dao.TemporadaDao;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Documento;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.utils.Ambiente;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author wilbert S
 */
@Controller
@RequestMapping(Constantes.PATH_DOCUMENTO)
public class DocumentoController {

    private static final Logger log = LoggerFactory.getLogger(DocumentoController.class);
    @Autowired
    private DocumentoDao DocumentoDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private Ambiente ambiente;
    @Autowired
    private TemporadaColportorDao temporadaColportorDao;
    @Autowired
    private ColportorDao colportorDao;
    @Autowired
    private TemporadaDao temporadaDao;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    /*
     * DE AQUI @InitBinder public void initBinder(WebDataBinder binder) {
     *
     * binder.registerCustomEditor(TipoDocumento.class, new
     * EnumEditor(TipoDocumento.class)); }
     */

    @RequestMapping({"/lista", ""})
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String clave,
            Usuario usuario,
            Errors errors,
            Model modelo) {
        log.debug("Mostrando lista de documentos");
        Map<String, Object> params = new HashMap<>();
        
        Integer max = 100;
        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (pagina != null) {
            params.put(Constantes.CONTAINSKEY_PAGINA, pagina);
            modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        } else {
            pagina = 1L;
            modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        }
        
        params.put(Constantes.CONTAINSKEY_MAX, 100);
        
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        
        log.debug("esAsociado {}, esColportor {}, colportorTmp {}", new Object [] {ambiente.esAsociado(), ambiente.esColportor(), request.getSession().getAttribute("colportorTmp")});
        TemporadaColportor temporadaColportorTmp = null;
        if (ambiente.esAsociado() && request.getSession().getAttribute("colportorTmp") == null) {
            log.debug("Entrando a Documentos como Asociado sin Colportor");
            log.debug("clave" + clave);

            if (clave != null && !clave.isEmpty()) {
                log.debug("clave" + colportorDao.obtiene(clave));
                Colportor colportor = colportorDao.obtiene(clave);
                request.getSession().setAttribute("colportorTmp", colportor);
                log.debug("colportor" + colportor);
                TemporadaColportor temporadaColportor = temporadaColportorDao.obtiene(colportor);
                request.getSession().setAttribute("temporadaColportorTmp", temporadaColportor);
                params.put("temporadaColportor", temporadaColportorTmp);
                log.debug("temporadaColportor" + temporadaColportor);
            } else {
                return Constantes.PATH_DOCUMENTO_LISTA;
            }
        }

        if (ambiente.esAsociado()) {
            log.debug("Entrando a Documentos como Asociado con un Colportor");

            temporadaColportorTmp = temporadaColportorDao.obtiene(((Colportor) request.getSession().getAttribute("colportorTmp")));
            params.put("temporadaColportor", temporadaColportorTmp);
        }

        if (ambiente.esColportor()) {
            log.debug("Entrando a Documentos como Colportor");

            Colportor colportor = colportorDao.obtiene(ambiente.obtieneUsuario().getId());
            log.debug("Usuario {}",ambiente.obtieneUsuario());
            log.debug("Colportor {}",colportor);
            
            if (request.getParameter("temporadaId") == null) {
                log.debug("Entrando a Documentos como Colportor {} con Temporada Activa", colportor);

                temporadaColportorTmp = temporadaColportorDao.obtiene(colportor);
                params.put("temporadaColportor", temporadaColportorTmp);
            } else {
                log.debug("Entrando a Documentos como Colportor con Temporada Inactiva");
                Temporada temporada = temporadaDao.obtiene(Long.parseLong(request.getParameter("temporadaId")));
                log.debug("temporada" + temporada.getId());
                temporadaColportorTmp = temporadaColportorDao.obtiene(colportor, temporada);
                params.put("temporadaColportor", temporadaColportorTmp);
            }
//          Codigo para validar prueba

            log.debug("temporadaColportorTmp" + temporadaColportorTmp.getId());
            request.setAttribute("temporadaColportorTmp", temporadaColportorTmp);
            modelo.addAttribute("temporadaColportorTmp", temporadaColportorTmp);
            log.debug("temporadaColportorTmpId" + temporadaColportorTmp.getId());
            modelo.addAttribute("temporadaColportorPrueba", temporadaColportorTmp.getId().toString());
//            params.put("temporadaColportor", temporadaColportorTmp);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = DocumentoDao.lista(params);
            try {
                generaReporte(tipo, (List<Documento>) params.get(Constantes.CONTAINSKEY_DOCUMENTOS), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = DocumentoDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<Documento>) params.get(Constantes.CONTAINSKEY_DOCUMENTOS), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("documento.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }

        params = DocumentoDao.lista(params);
        params = temporadaDao.lista(params);
//        Codigo para Valdiar Pruebas

        modelo.addAttribute(Constantes.CONTAINSKEY_DOCUMENTOS, params.get(Constantes.CONTAINSKEY_DOCUMENTOS));
        modelo.addAttribute(Constantes.CONTAINSKEY_TEMPORADAS, params.get(Constantes.CONTAINSKEY_TEMPORADAS));

        List<Documento> lista = (List) params.get(Constantes.CONTAINSKEY_DOCUMENTOS);
        log.debug("Items en lista {}", lista.size());
        Iterator<Documento> iter = lista.iterator();

        List<Temporada> listaTemporada = (List) params.get(Constantes.CONTAINSKEY_TEMPORADAS);

        Map<String, Object> temporadas = temporadaDao.lista(null);
        modelo.addAttribute(Constantes.CONTAINSKEY_TEMPORADAS, temporadas.get(Constantes.CONTAINSKEY_TEMPORADAS));

        Documento doc = null;

        BigDecimal totalBoletin = new BigDecimal("0");
        BigDecimal totalDiezmos = new BigDecimal("0");
        BigDecimal totalDepositos = new BigDecimal("0");
        BigDecimal objetivo = new BigDecimal(temporadaColportorTmp.getObjetivo());
        BigDecimal fidelidad = new BigDecimal("0");
        BigDecimal alcanzado = new BigDecimal("0");
        
        while (iter.hasNext()) {
            doc = iter.next();            
            switch (doc.getTipoDeDocumento()) {
                case Constantes.BOLETIN: {
                    log.debug("{} importe {}", doc.getId(), doc.getImporte());
                    totalBoletin = totalBoletin.add(doc.getImporte());
                    log.debug("totalBoletin {}", totalBoletin);
                    break;

                }

                case Constantes.DIEZMO: {
                    log.debug("importe {}", doc.getImporte());
                    totalDiezmos = totalDiezmos.add(doc.getImporte());
                    log.debug("totalDiezmos {}", totalDiezmos);
                    break;
                }

                case Constantes.DEPOSITO_CAJA: {
                    log.debug("importe {}", doc.getImporte());
                    totalDepositos = totalDepositos.add(doc.getImporte());
                   log.debug("totalDepositos {}", totalDepositos);
                    break;
                }

                case Constantes.DEPOSITO_BANCO: {
                    log.debug("importe {}", doc.getImporte());
                    totalDepositos = totalDepositos.add(doc.getImporte());
                   log.debug("totalDepositos {}", totalDepositos);
                    break;
                }
                case Constantes.NOTAS_DE_COMPRA: {
                    log.debug("importe {}", doc.getImporte());
                    totalDepositos = totalDepositos.add(doc.getImporte());
                    log.debug("totalDepositos {}", totalDepositos);
                    break;

                }

            }

        }

        modelo.addAttribute(Constantes.TOTALBOLETIN, totalBoletin);
        modelo.addAttribute(Constantes.TOTALDIEZMOS, totalDiezmos);
        modelo.addAttribute(Constantes.TOTALDEPOSITOS, totalDepositos);
        modelo.addAttribute(Constantes.OBJETIVO, objetivo);
        
        if (objetivo.compareTo(new BigDecimal("0")) > 0) {
            log.debug("% alcanzado = [totalBoletin {} / objetivo {}] = {}", new Object []{totalBoletin, objetivo, totalBoletin.divide(objetivo, 6, RoundingMode.HALF_EVEN).multiply(new BigDecimal("100"))});
            alcanzado = totalBoletin.divide(objetivo, 6, RoundingMode.HALF_EVEN).multiply(new BigDecimal("100"));
        }
        if (totalBoletin.compareTo(new BigDecimal("0")) > 0) {
            log.debug("fidelidad = [totalDiezmos {} / totalBoletin {}] = {}");
            fidelidad = totalDiezmos.divide(totalBoletin.movePointLeft(1), 6, RoundingMode.HALF_EVEN).multiply(new BigDecimal("100"));
        }
        modelo.addAttribute(Constantes.ALCANZADO, alcanzado.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        modelo.addAttribute(Constantes.FIDELIDAD, fidelidad.setScale(2, BigDecimal.ROUND_HALF_EVEN));
        
        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        
        List<Documento> documentos = (List<Documento>) params.get(Constantes.CONTAINSKEY_DOCUMENTOS);
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (documentos.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);        
        modelo.addAttribute("claveTmp", temporadaColportorDao.obtiene(temporadaColportorTmp.getId()).getColportor().getClave());
        
        return Constantes.PATH_DOCUMENTO_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando documento {}", id);
        Documento documentos = DocumentoDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_DOCUMENTO, documentos);

        return Constantes.PATH_DOCUMENTO_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo documento");
        Documento documentos = new Documento();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_DOCUMENTO, documentos);
        return Constantes.PATH_DOCUMENTO_NUEVO;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Documento documentos, 
        BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        Map<String, Object> params = new HashMap<>();
        for (String folio : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", folio, request.getParameterMap().get(folio));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.PATH_DOCUMENTO_NUEVO;
        }
        switch (documentos.getTipoDeDocumento()) {
            case "0":
                documentos.setTipoDeDocumento(Constantes.DEPOSITO_CAJA);
                break;
            case "1":
                documentos.setTipoDeDocumento(Constantes.DEPOSITO_BANCO);
                break;
            case "2":
                documentos.setTipoDeDocumento(Constantes.DIEZMO);
                break;
            case "3":
                documentos.setTipoDeDocumento(Constantes.NOTAS_DE_COMPRA);
                break;
            case "4":
                documentos.setTipoDeDocumento(Constantes.BOLETIN);
                break;
            case "5":
                documentos.setTipoDeDocumento(Constantes.INFORME);
                break;
        }


        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
            documentos.setFecha(sdf.parse(request.getParameter("fecha")));
        } catch (ConstraintViolationException e) {
            log.error("Fecha", e);
            return Constantes.PATH_DOCUMENTO_NUEVO;
        }

        try {
            log.debug("Documento Fecha" + documentos.getFecha());
            //294 y 305
            TemporadaColportor temporadaColportorTmp = temporadaColportorDao.obtiene((Colportor) ambiente.obtieneUsuario());
            documentos.setTemporadaColportor(temporadaColportorTmp);
            log.debug("temporadaColportorCrea" + temporadaColportorTmp.getId().toString());
            params.put("temporadaColportor", temporadaColportorTmp.getId().toString());
//            modelo.addAttribute("temporadaColportorTmp", temporadaColportorTmp.getId().toString());
//            modelo.addAttribute("claveTmp", temporadaColportorDao.obtiene(temporadaColportorTmp.getId()).getColportor().getClave());
//            Codigo para validar prueba
            request.getSession().setAttribute("temporadaColportorTmp", temporadaColportorTmp);
            request.getSession().setAttribute("temporadaColportorPrueba", temporadaColportorTmp.getId().toString());
            documentos = DocumentoDao.crea(documentos);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el documento", e);
            return Constantes.PATH_DOCUMENTO_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "documento.creado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{documentos.getFolio()});
        
        return "redirect:" + Constantes.PATH_DOCUMENTO_VER+"/"+documentos.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar documento {}", id);
        Documento documentos = DocumentoDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_DOCUMENTO, documentos);
        return Constantes.PATH_DOCUMENTO_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Documento documentos, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_DOCUMENTO_EDITA;
        }
        switch (documentos.getTipoDeDocumento()) {
            case "0":
                documentos.setTipoDeDocumento(Constantes.DEPOSITO_CAJA);
                break;
            case "1":
                documentos.setTipoDeDocumento(Constantes.DEPOSITO_BANCO);
                break;
            case "2":
                documentos.setTipoDeDocumento(Constantes.DIEZMO);
                break;
            case "3":
                documentos.setTipoDeDocumento(Constantes.NOTAS_DE_COMPRA);
                break;
            case "4":
                documentos.setTipoDeDocumento(Constantes.BOLETIN);
                break;
            case "5":
                documentos.setTipoDeDocumento(Constantes.INFORME);
                break;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
            documentos.setFecha(sdf.parse(request.getParameter("fecha")));
        } catch (ConstraintViolationException e) {
            log.error("Fecha", e);
            return Constantes.PATH_DOCUMENTO_EDITA;
        }

        try {
            documentos.setTemporadaColportor(temporadaColportorDao.obtiene((Colportor) ambiente.obtieneUsuario()));
            log.debug("Documento Fecha" + documentos.getFecha());
            documentos = DocumentoDao.actualiza(documentos);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo actualizar el documento", e);
            return Constantes.PATH_DOCUMENTO_EDITA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "documento.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{documentos.getFolio()});
        
        return "redirect:" + Constantes.PATH_DOCUMENTO_VER + "/" + documentos.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Documento documentos, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina documento");
        try {
            String folio = DocumentoDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "documento.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{folio});
        } catch (Exception e) {
            log.error("No se pudo eliminar el documento " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_DOCUMENTO, new String[]{"documento.no.eliminado.message"}, null, null));
            return Constantes.PATH_DOCUMENTO_VER;
        }

        return "redirect:" + Constantes.PATH_DOCUMENTO;
    }

    private void generaReporte(String tipo, List<Documento> documentos, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case Constantes.TIPO_DOCUMENTO_PDF:
                archivo = generaPdf(documentos);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=Documentoes.pdf");
                break;
            case Constantes.TIPO_DOCUMENTO_CSV:
                archivo = generaCsv(documentos);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=Documentoes.csv");
                break;
            case Constantes.TIPO_DOCUMENTO_XLS:
                archivo = generaXls(documentos);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=Documentoes.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<Documento> documentos, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case Constantes.TIPO_DOCUMENTO_PDF:
                archivo = generaPdf(documentos);
                tipoContenido = "application/pdf";
                break;
            case Constantes.TIPO_DOCUMENTO_CSV:
                archivo = generaCsv(documentos);
                tipoContenido = "text/csv";
                break;
            case Constantes.TIPO_DOCUMENTO_XLS:
                archivo = generaXls(documentos);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("documento.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List documentos) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/documentos.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(documentos));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List documentos) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/documentos.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(documentos));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List documentos) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/documentos.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(documentos));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }
}
