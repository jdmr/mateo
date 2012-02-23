/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import com.google.inject.internal.Errors;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ui.Model;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.rh.dao.CtaAuxiliarDao;
import mx.edu.um.mateo.rh.model.CtaAuxiliar;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author semdariobarbaamaya
 */

@Controller
@RequestMapping("/rh/ctaAuxiliar")
public class CtaAuxiliarController {
  
     private static final Logger log = LoggerFactory.getLogger(CtaAuxiliarController.class);
    @Autowired
    private CtaAuxiliarDao ctaAuxiliarDao;
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
        log.debug("Mostrando lista de ctaAuxiliares");
        Map<String, Object> params = new HashMap<>();
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
            params = ctaAuxiliarDao.lista(params);
            try {
                generaReporte(tipo, (List<CtaAuxiliar>) params.get("ctaAuxiliares"), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = ctaAuxiliarDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<CtaAuxiliar>) params.get("ctaAuxiliares"), request);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("ctaAuxiliar.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = ctaAuxiliarDao.lista(params);
        modelo.addAttribute("ctaAuxiliares", params.get("ctaAuxiliares"));

        // inicia paginado
        Long cantidad = (Long) params.get("cantidad");
        Integer max = (Integer) params.get("max");
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<CtaAuxiliar> ctaAuxiliares = (List<CtaAuxiliar>) params.get("ctaAuxiliares");
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (ctaAuxiliares.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute("paginacion", paginacion);
        modelo.addAttribute("paginas", paginas);
        // termina paginado

        return "rh/ctaAuxiliar/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando ctaAuxiliar {}", id);
        CtaAuxiliar ctaAuxiliar = ctaAuxiliarDao.obtiene(id);

        modelo.addAttribute("ctaAuxiliar", ctaAuxiliar);

        return "rh/ctaAuxiliar/ver";
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nuevo ctaAuxiliar");
        CtaAuxiliar ctaAuxiliar = new CtaAuxiliar();
        modelo.addAttribute("ctaAuxiliar", ctaAuxiliar);
        return "rh/ctaAuxiliar/nueva";
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid CtaAuxiliar ctaAuxiliar, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return "rh/ctaMayor/nuevo";
        }

        try {
            ctaAuxiliar = ctaAuxiliarDao.crea(ctaAuxiliar);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al ctaAuxiliar", e);
            return "rh/ctaAuxiliar/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "ctaAuxiliar.creada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{ctaAuxiliar.getNombre()});

        return "redirect:/rh/ctaAuxiliar/ver/" + ctaAuxiliar.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita ctaAuxiliar {}", id);
        CtaAuxiliar ctaAuxiliar = ctaAuxiliarDao.obtiene(id);
        modelo.addAttribute("ctaAuxiliar", ctaAuxiliar);
        return "rh/ctaAuxiliar/edita";
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid CtaAuxiliar ctaAuxiliar, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "rh/ctaAuxiliar/edita";
        }
        try {
            ctaAuxiliar = ctaAuxiliarDao.actualiza(ctaAuxiliar);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al ctaAuxiliar", e);
            return "rh/ctaAuxiliar/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "ctaAuxiliar.actualizada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{ctaAuxiliar.getNombre()});

        return "redirect:/rh/ctaAuxiliar/ver/" + ctaAuxiliar.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute CtaAuxiliar ctaAuxiliar, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina ctaAuxiliar");
        try {
            String nombre = ctaAuxiliarDao.elimina(id);

            redirectAttributes.addFlashAttribute("message", "ctaAuxiliar.eliminada.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el ctaAuxiliar " + id, e);
            bindingResult.addError(new ObjectError("ctaAuxiliar", new String[]{"ctaAuxiliar.no.eliminada.message"}, null, null));
            return "rh/ctaAuxiliar/ver";
        }

        return "redirect:/rh/ctaAuxiliar";
    }

    private void generaReporte(String tipo, List<CtaAuxiliar> ctaAuxiliares, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(ctaAuxiliares);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=ctaAuxiliares.pdf");
                break;
            case "CSV":
                archivo = generaCsv(ctaAuxiliares);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=ctaAuxiliares.csv");
                break;
            case "XLS":
                archivo = generaXls(ctaAuxiliares);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=ctaAuxiliares.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<CtaAuxiliar> ctaAuxiliares, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(ctaAuxiliares);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(ctaAuxiliares);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(ctaAuxiliares);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("ctaAuxiliar.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List ctaAuxiliares) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/ctaAuxiliares.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(ctaAuxiliares));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List ctaAuxiliares) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/ctaAuxiliares.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(ctaAuxiliares));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List ctaAuxiliares) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/ctaAuxiliares.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(ctaAuxiliares));
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
