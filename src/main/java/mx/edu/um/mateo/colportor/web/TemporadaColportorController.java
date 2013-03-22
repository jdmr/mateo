/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.colportor.dao.AsociadoDao;
import mx.edu.um.mateo.colportor.dao.ColegioColportorDao;
import mx.edu.um.mateo.colportor.dao.ColportorDao;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.dao.TemporadaDao;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.ColegioColportor;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Documento;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.dao.*;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.colportor.utils.FaltaAsociacionException;
import mx.edu.um.mateo.rh.model.Colegio;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author gibrandemetrioo
 */
@Controller
@RequestMapping(Constantes.PATH_TEMPORADACOLPORTOR)
public class TemporadaColportorController {

    private static final Logger log = LoggerFactory.getLogger(TemporadaColportorController.class);
    @Autowired
    private TemporadaColportorDao temporadaColportorDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private TemporadaDao temporadaDao;
    @Autowired
    private AsociadoDao asociadoDao;
    @Autowired
    private ColportorDao colportorDao;
    @Autowired
    private ColegioColportorDao colegioDao;
    @Autowired
    private Ambiente ambiente;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Usuario usuario,
            Model modelo) {
        log.debug("Mostrando lista de Temporada Colportor");
        Map<String, Object> params = new HashMap<>();
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
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = temporadaColportorDao.lista(params);
            try {
                generaReporte(tipo, (List<TemporadaColportor>) params.get(Constantes.CONTAINSKEY_TEMPORADACOLPORTORES), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = temporadaColportorDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<TemporadaColportor>) params.get(Constantes.CONTAINSKEY_TEMPORADACOLPORTORES), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("temporadaColportor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = temporadaColportorDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_TEMPORADACOLPORTORES, params.get(Constantes.CONTAINSKEY_TEMPORADACOLPORTORES));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<TemporadaColportor> temporadaColportores = (List<TemporadaColportor>) params.get(Constantes.CONTAINSKEY_TEMPORADACOLPORTORES);
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (temporadaColportores.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        // termina paginado

        return Constantes.PATH_TEMPORADACOLPORTOR_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando Temporada Colportor {}", id);
        TemporadaColportor temporadaColportor = temporadaColportorDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_TEMPORADACOLPORTOR, temporadaColportor);
        return Constantes.PATH_TEMPORADACOLPORTOR_VER;
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        log.debug("Nueva Temporada Colportor");
        TemporadaColportor temporadaColportor = new TemporadaColportor();
        params.put(Constantes.ADDATTRIBUTE_ASOCIACION, ((Asociacion) request.getSession().getAttribute(Constantes.SESSION_ASOCIACION)).getId());
        params.put(Constantes.SESSION_ASOCIACION, request.getSession().getAttribute(Constantes.SESSION_ASOCIACION));
        log.debug("asocicacion***" + request.getSession().getAttribute(Constantes.SESSION_ASOCIACION));

        params = temporadaDao.lista(params);
        List<Temporada> listaTemporadas = (List) params.get(Constantes.CONTAINSKEY_TEMPORADAS);
        log.debug("Temporadas***" + listaTemporadas.size());
        modelo.addAttribute(Constantes.CONTAINSKEY_TEMPORADAS, listaTemporadas);
      
        params = colegioDao.lista(params);
        List<Documento> listaColegios = (List) params.get(Constantes.CONTAINSKEY_COLEGIOS);
        modelo.addAttribute(Constantes.CONTAINSKEY_COLEGIOS, listaColegios);
            log.debug("listaColegios***" + listaColegios.size());
        try {
            params = colportorDao.lista(params);
        } catch (FaltaAsociacionException ex) {
            log.error("Falta asociacion", ex);
        }
        List<Colportor> listaColportores = (List) params.get(Constantes.CONTAINSKEY_COLPORTORES);
        log.debug("listaColportores***" + listaColportores.size());
        modelo.addAttribute(Constantes.CONTAINSKEY_COLPORTORES, listaColportores);
        
        try {
            params = asociadoDao.lista(params);
        } catch (FaltaAsociacionException ex) {
            log.error("Falta asociacion", ex);
        }
        List<Asociado> listaAsociados = (List) params.get(Constantes.CONTAINSKEY_ASOCIADOS);
        log.debug("listaAsociados" + listaAsociados.size());
        modelo.addAttribute(Constantes.CONTAINSKEY_ASOCIADOS, listaAsociados);
//        Map<String, Object> temporadas = temporadaDao.lista(null);
//        params = temporadaDao.lista(null);
//        modelo.addAttribute(Constantes.CONTAINSKEY_TEMPORADAS, temporadas.get(Constantes.CONTAINSKEY_TEMPORADAS));
//        modelo.addAttribute(Constantes.CONTAINSKEY_TEMPORADAS, params.get(Constantes.CONTAINSKEY_TEMPORADAS));
//
//        Map<String, Object> asociados = null;
//        try {
//            asociados = asociadoDao.lista(null);
//            params = asociadoDao.lista(null);
//        } catch (FaltaAsociacionException ex) {
//            log.error("Falta asociacion", ex);
//        }
//        List<Asociado> lista = (List) asociados.get(Constantes.CONTAINSKEY_ASOCIADOS);
//        log.debug("asociados" + lista.size());
//        modelo.addAttribute(Constantes.CONTAINSKEY_ASOCIADOS, lista);
//        modelo.addAttribute(Constantes.CONTAINSKEY_ASOCIADOS, asociados.get(Constantes.CONTAINSKEY_ASOCIADOS));
//        modelo.addAttribute(Constantes.CONTAINSKEY_ASOCIADOS, params.get(Constantes.CONTAINSKEY_ASOCIADOS));
//
//        Map<String, Object> colportores = null;
//        try {
//            colportores = colportorDao.lista(null);
//            params = colportorDao.lista(null);
//        } catch (FaltaAsociacionException ex) {
//            log.error("Falta asociacion", ex);
//        }
//        modelo.addAttribute(Constantes.CONTAINSKEY_COLPORTORES, colportores.get(Constantes.CONTAINSKEY_COLPORTORES));
//        modelo.addAttribute(Constantes.CONTAINSKEY_COLPORTORES, params.get(Constantes.CONTAINSKEY_COLPORTORES));
//
//        Map<String, Object> colegios = colegioDao.lista(null);
//        params = colegioDao.lista(null);
//        modelo.addAttribute(Constantes.CONTAINSKEY_COLEGIOS, colegios.get(Constantes.CONTAINSKEY_COLEGIOS));
//        modelo.addAttribute(Constantes.CONTAINSKEY_COLEGIOS, params.get(Constantes.CONTAINSKEY_COLEGIOS));
        modelo.addAttribute(Constantes.ADDATTRIBUTE_TEMPORADACOLPORTOR, temporadaColportor);

        modelo.addAttribute("sizeTemporada", listaTemporadas.size());
        modelo.addAttribute("sizeColportor", listaAsociados.size());
        modelo.addAttribute("sizeAsociado", listaColportores.size());
        modelo.addAttribute("sizeColegios", listaColegios.size());

        return Constantes.PATH_TEMPORADACOLPORTOR_NUEVA;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid TemporadaColportor temporadaColportor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        log.info("creando TC");
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));

        }
        log.debug("temoporadaColportor" + modelo);
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando" + bindingResult.getAllErrors());
            return Constantes.PATH_TEMPORADACOLPORTOR_NUEVA;
        }
        //try fechaInicio
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
            temporadaColportor.setFecha(sdf.parse(request.getParameter("fecha")));
        } catch (ConstraintViolationException e) {
            log.error("Fecha  Incorrecta", e);
            return Constantes.PATH_TEMPORADACOLPORTOR_NUEVA;
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            temporadaColportor.setUnion(usuario.getAsociacion().getUnion());
            temporadaColportor.setAsociacion(usuario.getAsociacion());

            Temporada temporada = temporadaDao.obtiene(temporadaColportor.getTemporada().getId());
            temporadaColportor.setTemporada(temporada);
            Colportor colportor = colportorDao.obtiene(temporadaColportor.getColportor().getId());
            temporadaColportor.setColportor(colportor);
            Asociado asociado = asociadoDao.obtiene(ambiente.obtieneUsuario().getId());
            temporadaColportor.setAsociado(asociado);
            ColegioColportor colegio = colegioDao.obtiene(temporadaColportor.getColegioColportor().getId());
            temporadaColportor.setColegioColportor(colegio);
            temporadaColportor = temporadaColportorDao.crea(temporadaColportor);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la temporada Colportor", e);
            return Constantes.PATH_TEMPORADACOLPORTOR_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.creada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{temporadaColportor.getStatus()});

        return "redirect:" + Constantes.PATH_TEMPORADACOLPORTOR_VER + "/" + temporadaColportor.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita Temporada {}", id);
        TemporadaColportor temporadaColportor = temporadaColportorDao.obtiene(id);
        Map<String, Object> temporadas = temporadaDao.lista(null);
        modelo.addAttribute(Constantes.CONTAINSKEY_TEMPORADAS, temporadas.get(Constantes.CONTAINSKEY_TEMPORADAS));
        Map<String, Object> asociados = null;
        try {
            asociados = asociadoDao.lista(null);
        } catch (FaltaAsociacionException ex) {
            log.error("Falta asociacion", ex);
        }
        modelo.addAttribute(Constantes.CONTAINSKEY_ASOCIADOS, asociados.get(Constantes.CONTAINSKEY_ASOCIADOS));
        Map<String, Object> colportores = null;
        try {
            colportores = colportorDao.lista(null);
        } catch (FaltaAsociacionException ex) {
            log.error("Falta asociacion", ex);
        }
        modelo.addAttribute(Constantes.CONTAINSKEY_COLPORTORES, colportores.get(Constantes.CONTAINSKEY_COLPORTORES));
        Map<String, Object> colegios = colegioDao.lista(null);
        modelo.addAttribute(Constantes.CONTAINSKEY_COLEGIOS, colegios.get(Constantes.CONTAINSKEY_COLEGIOS));

        modelo.addAttribute(Constantes.ADDATTRIBUTE_TEMPORADACOLPORTOR, temporadaColportor);
        modelo.addAttribute("sizeTemporada", temporadas.size());
        modelo.addAttribute("sizeColportor", asociados.size());
        modelo.addAttribute("sizeAsociado", colportores.size());
        modelo.addAttribute("sizeColegio", colegios.size());
        return Constantes.PATH_TEMPORADACOLPORTOR_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid TemporadaColportor temporadaColportor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_TEMPORADACOLPORTOR_EDITA;
        }
        //try fechaInicio
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
            temporadaColportor.setFecha(sdf.parse(request.getParameter("fecha")));
        } catch (ConstraintViolationException e) {
            log.error("Fecha  Incorrecta", e);
            return Constantes.PATH_TEMPORADACOLPORTOR_NUEVA;
        }
        try {
            Usuario usuario = ambiente.obtieneUsuario();
            temporadaColportor.setUnion(usuario.getAsociacion().getUnion());
            temporadaColportor.setAsociacion(usuario.getAsociacion());

            Temporada temporada = temporadaDao.obtiene(temporadaColportor.getTemporada().getId());
            temporadaColportor.setTemporada(temporada);
            Asociado asociado = asociadoDao.obtiene(ambiente.obtieneUsuario().getId());
            temporadaColportor.setAsociado(asociado);
            Colportor colportor = colportorDao.obtiene(temporadaColportor.getColportor().getId());
            temporadaColportor.setColportor(colportor);

            ColegioColportor colegio = colegioDao.obtiene(temporadaColportor.getColegioColportor().getId());
            temporadaColportor.setColegioColportor(colegio);
            temporadaColportor = temporadaColportorDao.actualiza(temporadaColportor);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al Asociacion", e);
            return Constantes.PATH_TEMPORADACOLPORTOR_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.actualizada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{temporadaColportor.getObservaciones()});

        return "redirect:" + Constantes.PATH_TEMPORADACOLPORTOR_VER + "/" + temporadaColportor.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute TemporadaColportor temporadaColportor, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina Temporada Colportor");
        try {
            String nombre = temporadaColportorDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.eliminada.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el asociado " + id, e);
            bindingResult.addError(new ObjectError(Constantes.CONTAINSKEY_TEMPORADACOLPORTORES, new String[]{"temporadaColportor.no.eliminada.message"}, null, null));
            return Constantes.PATH_TEMPORADACOLPORTOR_VER;
        }

        return "redirect:" + Constantes.PATH_TEMPORADACOLPORTOR;
    }

    private void generaReporte(String tipo, List<TemporadaColportor> temporadaColportor, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case Constantes.TIPO_DOCUMENTO_PDF:
                archivo = generaPdf(temporadaColportor);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=temporadacolportor.pdf");
                break;
            case Constantes.TIPO_DOCUMENTO_CSV:
                archivo = generaCsv(temporadaColportor);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=temporadacolportor.csv");
                break;
            case Constantes.TIPO_DOCUMENTO_XLS:
                archivo = generaXls(temporadaColportor);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=temporadacolportor.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }
    }

    private void enviaCorreo(String tipo, List<TemporadaColportor> temporadaColportor, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case Constantes.TIPO_DOCUMENTO_PDF:
                archivo = generaPdf(temporadaColportor);
                tipoContenido = "application/pdf";
                break;
            case Constantes.TIPO_DOCUMENTO_CSV:
                archivo = generaCsv(temporadaColportor);
                tipoContenido = "text/csv";
                break;
            case Constantes.TIPO_DOCUMENTO_XLS:
                archivo = generaXls(temporadaColportor);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("temporadaColportor.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List temporadaColportor) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/temporadacolportor.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(temporadaColportor));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List temporadaColportor) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/temporadacolportor.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(temporadaColportor));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List temporadaColportor) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/temporadacolportor.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(temporadaColportor));
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
