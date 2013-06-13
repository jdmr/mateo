/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.ColegioColportorDao;
import mx.edu.um.mateo.colportor.model.ColegioColportor;
import mx.edu.um.mateo.general.model.Usuario;
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
 * @author wilbert
 */
@Controller
@RequestMapping(Constantes.PATH_COLEGIO_COLPORTOR)
public class ColegioColportorController {

    private static final Logger log = LoggerFactory.getLogger(ColegioColportorController.class);
    @Autowired
    private ColegioColportorDao ColegioDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
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
            Errors errors,
            Model modelo) {
        log.debug("Mostrando lista de colegios");
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
            params = ColegioDao.lista(params);
            try {
                generaReporte(tipo, (List<ColegioColportor>) params.get(Constantes.CONTAINSKEY_COLEGIOS), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = ColegioDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<ColegioColportor>) params.get(Constantes.CONTAINSKEY_COLEGIOS), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("colegio.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = ColegioDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_COLEGIOS, params.get(Constantes.CONTAINSKEY_COLEGIOS));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<ColegioColportor> colegios = (List<ColegioColportor>) params.get(Constantes.CONTAINSKEY_COLEGIOS);
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (colegios.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        // termina paginado

        return Constantes.PATH_COLEGIO_COLPORTOR_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando colegio {}", id);
        ColegioColportor colegios = ColegioDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_COLEGIO, colegios);

        return Constantes.PATH_COLEGIO_COLPORTOR_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo colegio");
        ColegioColportor colegios = new ColegioColportor();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_COLEGIO, colegios);
        return Constantes.PATH_COLEGIO_COLPORTOR_NUEVO;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid ColegioColportor colegios, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.PATH_COLEGIO_COLPORTOR_NUEVO;
        }
        try {
            colegios = ColegioDao.crea(colegios);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la colegio", e);
            return Constantes.PATH_COLEGIO_COLPORTOR_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.creado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{colegios.getNombre()});

        return "redirect:" + Constantes.PATH_COLEGIO_COLPORTOR_VER + "/" + colegios.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar colegio {}", id);
        ColegioColportor colegios = ColegioDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_COLEGIO, colegios);
        return Constantes.PATH_COLEGIO_COLPORTOR_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid ColegioColportor colegios, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_COLEGIO_COLPORTOR_EDITA;
        }
        try {
            colegios = ColegioDao.actualiza(colegios);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la colegio", e);
            return Constantes.PATH_COLEGIO_COLPORTOR_NUEVO;
        }
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{colegios.getNombre()});

        return "redirect:" + Constantes.PATH_COLEGIO_COLPORTOR_VER + "/" + colegios.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute ColegioColportor colegios, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina colegio");
        try {
            String nombre = ColegioDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "colegio.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la colegio " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_COLEGIO, new String[]{"colegio.no.eliminado.message"}, null, null));
            return Constantes.PATH_COLEGIO_COLPORTOR_VER;
        }

        return "redirect:" + Constantes.PATH_COLEGIO_COLPORTOR;
    }

    private void generaReporte(String tipo, List<ColegioColportor> colegios, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(colegios);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=Colegioes.pdf");
                break;
            case "CSV":
                archivo = generaCsv(colegios);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=Colegioes.csv");
                break;
            case "XLS":
                archivo = generaXls(colegios);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=Colegioes.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<ColegioColportor> colegios, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(colegios);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(colegios);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(colegios);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("colegio.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List colegios) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colegios.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(colegios));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List colegios) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colegios.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(colegios));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List colegios) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colegios.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(colegios));
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
