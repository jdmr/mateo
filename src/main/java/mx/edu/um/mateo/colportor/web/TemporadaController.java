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
import java.util.*;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.TemporadaDao;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.general.utils.Ambiente;
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
@RequestMapping(Constantes.PATH_TEMPORADA)
public class TemporadaController {

    private static final Logger log = LoggerFactory.getLogger(TemporadaController.class);
    @Autowired
    private TemporadaDao temporadaDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private Ambiente ambiente;

    /**
     * Regresa lista de temporadas por asociacion<br>
     * Si la asociacion no esta presente en session 
     * @param request
     * @param response
     * @param filtro
     * @param pagina
     * @param tipo
     * @param correo
     * @param order
     * @param sort
     * @param modelo
     * @return 
     */
    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de Temporada");
        //filtrar temporadas por asociacion

        Map<String, Object> params = new HashMap<>();
        params.put(Constantes.SESSION_ASOCIACION, ((Asociacion) request.getSession().getAttribute(Constantes.SESSION_ASOCIACION)));


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
            params = temporadaDao.lista(params);
            try {
                generaReporte(tipo, (List<Temporada>) params.get(Constantes.CONTAINSKEY_TEMPORADAS), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = temporadaDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<Temporada>) params.get(Constantes.CONTAINSKEY_TEMPORADAS), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, 
                        new String[]{messageSource.getMessage("temporada.lista.label", 
                        null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }


        params = temporadaDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_TEMPORADAS, params.get(Constantes.CONTAINSKEY_TEMPORADAS));

 //        inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<Temporada> temporadas = (List<Temporada>) params.get(Constantes.CONTAINSKEY_TEMPORADAS);
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (temporadas.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
       //  termina paginado

        params = temporadaDao.lista(params);

        modelo.addAttribute(Constantes.CONTAINSKEY_TEMPORADAS, params.get(Constantes.CONTAINSKEY_TEMPORADAS));

        List<Temporada> lista = (List) params.get(Constantes.CONTAINSKEY_TEMPORADAS);
        log.debug("SizeLista " + temporadas.size());
        log.debug("SizeLista " + lista.size());

        modelo.addAttribute("SizeTemporadas" , lista.size());

        return Constantes.PATH_TEMPORADA_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando Temporada {}", id);
        Temporada temporadas = temporadaDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_TEMPORADA, temporadas);
        return Constantes.PATH_TEMPORADA_VER;
    }
    

    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nueva Temporada");
        Temporada temporadas = new Temporada();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_TEMPORADA, temporadas);
        return Constantes.PATH_TEMPORADA_NUEVA;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Temporada temporada, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.PATH_TEMPORADA_NUEVA;
        }
        //try fechaInicio
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
            temporada.setFechaInicio(sdf.parse(request.getParameter("fechaInicio")));
        } catch (ConstraintViolationException e) {
            log.error("Fecha de Inicio Incorrecta", e);
            return Constantes.PATH_TEMPORADA_NUEVA;
        }
        //try fechaFinal
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
            temporada.setFechaFinal(sdf.parse(request.getParameter("fechaFinal")));
        } catch (ConstraintViolationException e) {
            log.error("Fecha de Final Incorrecta", e);
            return Constantes.PATH_TEMPORADA_NUEVA;
        }

        try {
            log.debug("Temporada FEcha Inicio" + temporada.getFechaInicio());
            temporada.setAsociacion((Asociacion)request.getSession().getAttribute(Constantes.SESSION_ASOCIACION));
            log.debug("Temporada FEcha Inicio" + temporada.getFechaFinal());
            temporada = temporadaDao.crea(temporada);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la temporada", e);
            errors.rejectValue("asociacion" , "Asociacion no encontrada");
            return Constantes.PATH_TEMPORADA_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.creada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{temporada.getNombre()});

        return "redirect:" + Constantes.PATH_TEMPORADA_VER + "/" + temporada.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita Temporada {}", id);
        Temporada temporadas = temporadaDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_TEMPORADA, temporadas);
        return Constantes.PATH_TEMPORADA_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Temporada temporada, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_TEMPORADA_EDITA;
        }
        //try fechaInicio
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
            temporada.setFechaInicio(sdf.parse(request.getParameter("fechaInicio")));
        } catch (ConstraintViolationException e) {
            log.error("Fecha de Inicio Incorrecta", e);
            return Constantes.PATH_TEMPORADA_EDITA;
        }
        //try fechaFinal
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
            temporada.setFechaFinal(sdf.parse(request.getParameter("fechaFinal")));
        } catch (ConstraintViolationException e) {
            log.error("Fecha de Final Incorrecta", e);
            return Constantes.PATH_TEMPORADA_EDITA;
        }
        try {
            log.debug("Temporada FEcha Inicio" + temporada.getFechaInicio());
            temporada.setAsociacion((Asociacion)request.getSession().getAttribute(Constantes.SESSION_ASOCIACION)); //esta
            log.debug("Temporada FEcha Inicio" + temporada.getFechaFinal());
            temporada = temporadaDao.actualiza(temporada);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al Temporada", e);
            errors.rejectValue("asociacion" , "Asociacion no encontrada");                                          //y esta
            return Constantes.PATH_TEMPORADA_EDITA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.actualizada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{temporada.getNombre()});

        return "redirect:" + Constantes.PATH_TEMPORADA_VER + "/" + temporada.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Temporada temporadas, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina Temporada");
        try {
            String nombre = temporadaDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "temporada.eliminada.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el temporada " + id, e);
            bindingResult.addError(new ObjectError(Constantes.CONTAINSKEY_TEMPORADAS, new String[]{"temporada.no.eliminada.message"}, null, null));
            return Constantes.PATH_TEMPORADA_VER;
        }

        return "redirect:" + Constantes.PATH_TEMPORADA;
    }

    private void generaReporte(String tipo, List<Temporada> temporadas, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case Constantes.TIPO_DOCUMENTO_PDF:
                archivo = generaPdf(temporadas);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=temporadas.pdf");
                break;
            case Constantes.TIPO_DOCUMENTO_CSV:
                archivo = generaCsv(temporadas);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=temporadas.csv");
                break;
            case Constantes.TIPO_DOCUMENTO_XLS:
                archivo = generaXls(temporadas);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=temporadas.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<Temporada> temporadas, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case Constantes.TIPO_DOCUMENTO_PDF:
                archivo = generaPdf(temporadas);
                tipoContenido = "application/pdf";
                break;
            case Constantes.TIPO_DOCUMENTO_CSV:
                archivo = generaCsv(temporadas);
                tipoContenido = "text/csv";
                break;
            case Constantes.TIPO_DOCUMENTO_XLS:
                archivo = generaXls(temporadas);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("temporada.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List temporadas) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/temporadas.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(temporadas));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List temporadas) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/temporadas.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(temporadas));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List temporadas) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/temporadas.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(temporadas));
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
