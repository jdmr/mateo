/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

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
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.rh.dao.CtaResultadoDao;
import mx.edu.um.mateo.rh.model.CtaResultado;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author develop
 */
@Controller
@RequestMapping("rh/ctaResultado")
public class CtaResultadoController {

 private static final Logger log = LoggerFactory.getLogger(CtaResultadoController.class);
    @Autowired
    private CtaResultadoDao ctaResultadoDao;
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
            Model modelo) {
        log.debug("Mostrando lista de ctaResultados");
        Map<String, Object> params = new HashMap<>();
        params.put("organizacion", request.getSession().getAttribute("organizacionId"));
        if (StringUtils.isNotBlank(filtro)) {
            params.put("filtro", filtro);
        }
        if (pagina != null) {
            params.put("pagina", pagina);
            modelo.addAttribute("pagina", pagina);
        } else {
            pagina = 1L;
            modelo.addAttribute("pagina", pagina);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put("order", order);
            params.put("sort", sort);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = ctaResultadoDao.lista(params);
            try {
                generaReporte(tipo, (List<CtaResultado>) params.get("ctaResultados"), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = ctaResultadoDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<CtaResultado>) params.get("ctaResultados"), request);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("ctaResultado.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = ctaResultadoDao.lista(params);
        modelo.addAttribute("ctaResultados", params.get("ctaResultados"));

        // inicia paginado
        Long cantidad = (Long) params.get("cantidad");
        Integer max = (Integer) params.get("max");
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<CtaResultado> ctaResultados = (List<CtaResultado>) params.get("ctaResultados");
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (ctaResultados.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute("paginacion", paginacion);
        modelo.addAttribute("paginas", paginas);
        // termina paginado

        return "rh/ctaResultado/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando ctaResultado {}", id);
        CtaResultado ctaResultado = ctaResultadoDao.obtiene(id);

        modelo.addAttribute("ctaResultado", ctaResultado);

        return "rh/ctaResultado/ver";
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nueva ctaResultado");
        CtaResultado ctaResultado = new CtaResultado();
        modelo.addAttribute("ctaResultado", ctaResultado);
        return "rh/ctaResultado/nueva";
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid CtaResultado ctaResultado, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "rh/ctaResultado/nueva";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            ctaResultado = ctaResultadoDao.crea(ctaResultado);

            ambiente.actualizaSesion(request, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al ctaResultado", e);
            errors.rejectValue("codigo", "campo.duplicado.message", new String[]{"codigo"}, null);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "rh/ctaResultado/nueva";
        }

        redirectAttributes.addFlashAttribute("message", "ctaResultado.creada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{ctaResultado.getNombre()});

        return "redirect:/rh/ctaResultado/ver/" + ctaResultado.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita ctaResultado {}", id);
        CtaResultado ctaResultado = ctaResultadoDao.obtiene(id);
        modelo.addAttribute("ctaResultado", ctaResultado);
        return "rh/ctaResultado/edita";
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid CtaResultado ctaResultado, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "rh/ctaResultado/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            ctaResultado = ctaResultadoDao.actualiza(ctaResultado);

            ambiente.actualizaSesion(request, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la ctaResultado", e);
            errors.rejectValue("codigo", "campo.duplicado.message", new String[]{"codigo"}, null);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "rh/ctaResultado/nueva";
        }

        redirectAttributes.addFlashAttribute("message", "ctaResultado.actualizada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{ctaResultado.getNombre()});

        return "redirect:/rh/ctaResultado/ver/" + ctaResultado.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute CtaResultado ctaResultado, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina ctaResultado");
        try {
            String nombre = ctaResultadoDao.elimina(id);

            ambiente.actualizaSesion(request);

            redirectAttributes.addFlashAttribute("message", "ctaResultado.eliminada.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (UltimoException e) {
            log.error("No se pudo eliminar el ctaResultado " + id, e);
            bindingResult.addError(new ObjectError("ctaResultado", new String[]{"ultima.ctaResultado.no.eliminada.message"}, null, null));
            return "rh/ctaResultado/ver";
        } catch (Exception e) {
            log.error("No se pudo eliminar la ctaResultado " + id, e);
            bindingResult.addError(new ObjectError("ctaResultado", new String[]{"ctaResultado.no.eliminada.message"}, null, null));
            return "rh/ctaResultado/ver";
        }

        return "redirect:/rh/ctaResultado";
    }

    private void generaReporte(String tipo, List<CtaResultado> ctaResultados, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(ctaResultados);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=ctaResultados.pdf");
                break;
            case "CSV":
                archivo = generaCsv(ctaResultados);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=ctaResultados.csv");
                break;
            case "XLS":
                archivo = generaXls(ctaResultados);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=ctaResultados.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<CtaResultado> ctaResultados, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(ctaResultados);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(ctaResultados);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(ctaResultados);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("ctaResultado.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List lista) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/ctaResultados.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(lista));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List lista) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/ctaResultados.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(lista));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List lista) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/ctaResultados.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(lista));
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

