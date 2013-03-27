/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.CiudadDao;
import mx.edu.um.mateo.colportor.dao.EstadoDao;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.colportor.model.Ciudad;
import mx.edu.um.mateo.colportor.model.Estado;
import mx.edu.um.mateo.general.utils.Ambiente;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.lang.StringUtils;
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
@RequestMapping("/colportaje/ciudad")
public class CiudadController {
    private static final Logger log = (Logger) LoggerFactory.getLogger(CiudadController.class);
    @Autowired
    private CiudadDao ciudadDao;
    @Autowired
    private EstadoDao estadoDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private UsuarioDao usuarioDao;
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
            Model modelo) {
        log.debug("Mostrando lista de Ciudad");
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
            params = ciudadDao.lista(params);
            try {
                generaReporte(tipo, (List<Ciudad>) params.get(Constantes.CONTAINSKEY_CIUDADES), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }
        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = ciudadDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<Ciudad>) params.get(Constantes.CONTAINSKEY_CIUDADES), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "ciudad.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("ciudad.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = ciudadDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_CIUDADES, params.get(Constantes.CONTAINSKEY_CIUDADES));
        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<Ciudad> ciudad = (List<Ciudad>) params.get(Constantes.CONTAINSKEY_CIUDADES);
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (ciudad.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        return Constantes.PATH_CIUDAD_LISTA;
        }
    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando Ciudad {}", id);
        Ciudad ciudad = ciudadDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_CIUDAD, ciudad);
        return Constantes.PATH_CIUDAD_VER;
    }
    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nueva Ciudad");
        Ciudad ciudad = new Ciudad();
        
        Map<String, Object> estados = estadoDao.lista(null);
        modelo.addAttribute(Constantes.CONTAINSKEY_ESTADOS, estados.get(Constantes.CONTAINSKEY_ESTADOS));
        modelo.addAttribute(Constantes.ADDATTRIBUTE_CIUDAD, ciudad);
        return Constantes.PATH_CIUDAD_NUEVA;
    }
    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Ciudad ciudad, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.PATH_CIUDAD_NUEVA;
        }
        try {
            Estado estado = estadoDao.obtiene(ciudad.getEstado().getId());
            ciudad.setEstado(estado);
            ciudad = ciudadDao.crea(ciudad);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el Ciudad", e);
            return Constantes.PATH_CIUDAD_NUEVA;
        }
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "ciudad.creada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{ciudad.getNombre()});
        return "redirect:" + Constantes.PATH_CIUDAD_VER + "/" + ciudad.getId();
    }
    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita Ciudad {}", id);
        Map<String, Object> estados = estadoDao.lista(null);
        modelo.addAttribute(Constantes.CONTAINSKEY_ESTADOS, estados.get(Constantes.CONTAINSKEY_ESTADOS));
        Ciudad ciudad = ciudadDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_CIUDAD, ciudad);
        return Constantes.PATH_CIUDAD_EDITA;
    }
    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Ciudad ciudad, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_CIUDAD_EDITA;
        }
        try {
            Estado estado = estadoDao.obtiene(ciudad.getEstado().getId());
            ciudad.setEstado(estado);
            ciudad = ciudadDao.actualiza(ciudad);
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            log.error("No se pudo crear el Ciudad", e);
            return Constantes.PATH_CIUDAD_EDITA;
        }
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "ciudad.actualizada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{ciudad.getNombre()});
        return "redirect:" + Constantes.PATH_CIUDAD_VER + "/" + ciudad.getId();
    }
    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Ciudad ciudad, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina Pais");
        try {
            String nombre = ciudadDao.elimina(id);
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "ciudad.eliminada.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el pais " + id, e);
            bindingResult.addError(new ObjectError(Constantes.CONTAINSKEY_PAISES, new String[]{"ciudad.no.eliminada.message"}, null, null));
            return Constantes.PATH_CIUDAD_VER;
        }
        return "redirect:" + Constantes.PATH_CIUDAD_LISTA;
    }
    private void generaReporte(String tipo, List<Ciudad> ciudad, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(ciudad);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=ciudades.pdf");
                break;
            case "CSV":
                archivo = generaCsv(ciudad);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=ciudades.csv");
                break;
            case "XLS":
                archivo = generaXls(ciudad);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=ciudades.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<Ciudad> ciudad, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(ciudad);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(ciudad);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(ciudad);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("ciudad.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List ciudad) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/ciudades.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(ciudad));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List ciudad) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/ciudades.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(ciudad));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List ciudad) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/ciudades.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(ciudad));
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
