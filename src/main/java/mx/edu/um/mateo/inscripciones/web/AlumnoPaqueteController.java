package mx.edu.um.mateo.inscripciones.web;

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
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.AlumnoPaquete;
import mx.edu.um.mateo.inscripciones.model.Institucion;
import mx.edu.um.mateo.inscripciones.service.AlumnoPaqueteManager;
import mx.edu.um.mateo.inscripciones.service.PaqueteManager;
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
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @author semdariobarbaamaya
 */
@Controller
@RequestMapping(Constantes.PATH_ALUMNOPAQUETE)
public class AlumnoPaqueteController {

    private static final Logger log = LoggerFactory.getLogger(AlumnoPaqueteController.class);
    @Autowired
    private AlumnoPaqueteManager alumnoPaqueteManager;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private Ambiente ambiente;
    @Autowired
    private PaqueteManager paqueteManager;

    @RequestMapping({"", "/lista"})
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
        log.debug("Mostrando lista de tipos de Becas");
        Map<String, Object> params = new HashMap<>();
        Long empresaId = (Long) request.getSession().getAttribute("empresaId");
        params.put("empresa", empresaId);
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
            params = alumnoPaqueteManager.lista(params);
            try {
                generaReporte(tipo, (List<AlumnoPaquete>) params.get(Constantes.CONTAINSKEY_ALUMNOPAQUETES), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = alumnoPaqueteManager.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<AlumnoPaquete>) params.get(Constantes.CONTAINSKEY_ALUMNOPAQUETES), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("alumnoPaquete.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = alumnoPaqueteManager.lista(params);
        log.debug("params{}", params.get(Constantes.CONTAINSKEY_ALUMNOPAQUETES));
        modelo.addAttribute(Constantes.CONTAINSKEY_ALUMNOPAQUETES, params.get(Constantes.CONTAINSKEY_ALUMNOPAQUETES));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<AlumnoPaquete> alumnoPaquetes = (List<AlumnoPaquete>) params.get(Constantes.CONTAINSKEY_ALUMNOPAQUETES);
        Long primero = ((pagina - 1) * max) + 1;
        log.debug("primero {}", primero);
        Long ultimo = primero + (alumnoPaquetes.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        log.debug("Paginacion{}", paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        log.debug("paginas{}", paginas);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        log.debug("Pagina{}", pagina);
        // termina paginado

        return Constantes.PATH_ALUMNOPAQUETE_LISTA;
    }
    
     @RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando alumnoPaquete {}", id);
                AlumnoPaquete alumnoPaquete = alumnoPaqueteManager.obtiene(id);

		modelo.addAttribute("alumnoPaquete", alumnoPaquete);

		return Constantes.PATH_ALUMNOPAQUETE_VER;
	}

    @RequestMapping("/nuevo")
    public String nueva(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo tipo de Beca");
        AlumnoPaquete alumnoPaquete = new AlumnoPaquete();
        modelo.addAttribute("tipoBeca", alumnoPaquete);
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession()
                .getAttribute("empresaId"));
        params.put("reporte", true);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ALUMNOPAQUETE, alumnoPaquete);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAQUETES, paqueteManager.getPaquetes(params).get(Constantes.CONTAINSKEY_PAQUETES));
        return Constantes.PATH_ALUMNOPAQUETE_NUEVO;
    }

    @Transactional
    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid AlumnoPaquete alumnoPaquete, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String matricula : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", matricula, request.getParameterMap().get(matricula));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession()
                    .getAttribute("empresaId"));
            return Constantes.PATH_ALUMNOPAQUETE_NUEVO;
        }

        try {
            AlumnoPaquete tmp = null;
             Usuario usuario = ambiente.obtieneUsuario();
            
            if(alumnoPaquete.getId() != null){
                tmp = alumnoPaqueteManager.obtiene(alumnoPaquete.getId());
                tmp.setPaquete(paqueteManager.getPaquete(alumnoPaquete.getPaquete().getId().toString()));
                tmp.setMatricula(alumnoPaquete.getMatricula());
                tmp.setStatus(alumnoPaquete.getStatus());
                alumnoPaqueteManager.graba(tmp, usuario);
                redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "alumnoPaquete.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{alumnoPaquete.getMatricula()});
            }else{
                  alumnoPaquete.setPaquete(paqueteManager.getPaquete(alumnoPaquete.getPaquete().getId().toString()));
                  alumnoPaqueteManager.graba(alumnoPaquete, usuario);
                  redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "alumnoPaquete.creado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{alumnoPaquete.getMatricula()});
            }
           
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el tipo de Beca", e);
            return Constantes.PATH_ALUMNOPAQUETE_NUEVO;
        }

        

        return "redirect:/" + Constantes.PATH_ALUMNOPAQUETE_LISTA + "/";
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo,HttpServletRequest request) {
        log.debug("Editar cuenta de tipos de becas {}", id);
          Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession()
                .getAttribute("empresaId"));
        params.put("reporte", true);
        AlumnoPaquete alumnoPaquete = alumnoPaqueteManager.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ALUMNOPAQUETE, alumnoPaquete);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAQUETES, paqueteManager.getPaquetes(params).get(Constantes.CONTAINSKEY_PAQUETES));
        return Constantes.PATH_ALUMNOPAQUETE_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute AlumnoPaquete alumnoPaquete, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina cuenta de tipos de becas");
        try {
            alumnoPaqueteManager.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "alumnoPaquete.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{alumnoPaquete.getMatricula()});
        } catch (Exception e) {
            log.error("No se pudo eliminar el tipo de alumnoPaquete " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_ALUMNOPAQUETE, new String[]{"alumnoPaquete.no.elimina.message"}, null, null));
            return Constantes.PATH_ALUMNOPAQUETE_VER;
        }

        return "redirect:/" + Constantes.PATH_ALUMNOPAQUETE_LISTA;
    }

    private void generaReporte(String tipo, List<AlumnoPaquete> alumnoPaquetes, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(alumnoPaquetes);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=alumnoPaquetes.pdf");
                break;
            case "CSV":
                archivo = generaCsv(alumnoPaquetes);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=alumnoPaquetes.csv");
                break;
            case "XLS":
                archivo = generaXls(alumnoPaquetes);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=alumnoPaquetes.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }
    }

    private void enviaCorreo(String tipo, List<AlumnoPaquete> alumnoPaquetes, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(alumnoPaquetes);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(alumnoPaquetes);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(alumnoPaquetes);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("alumnoPaquete.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List alumnoPaquetes) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/tiposBecas.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(alumnoPaquetes));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List alumnoPaquetes) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/tiposBecas.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(alumnoPaquetes));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List alumnoPaquetes) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/tiposBecas.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(alumnoPaquetes));
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
