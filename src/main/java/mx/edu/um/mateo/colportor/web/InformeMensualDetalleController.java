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
import java.util.Date;
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
import mx.edu.um.mateo.colportor.dao.InformeMensualDao;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao;
import mx.edu.um.mateo.colportor.model.InformeMensual;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;
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
@RequestMapping("/colportaje/informeMensualDetalle")
public class InformeMensualDetalleController {

    private static final Logger log = (Logger) LoggerFactory.getLogger(InformeMensualDetalleController.class);
    @Autowired
    private InformeMensualDao informeMensualDao;
    @Autowired
    private InformeMensualDetalleDao informeMensualDetalleDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;    
    @Autowired
    private Ambiente ambiente;

    @RequestMapping({"/lista"})
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de InformeMensualDetalle");
        
        InformeMensual informe = null;
                
        if(request.getParameter("id") == null){
            informe = (InformeMensual)request.getSession().getAttribute(Constantes.INFORMEMENSUAL);
        }
        else{
            informe = informeMensualDao.obtiene(Long.parseLong(request.getParameter("id")));
            request.getSession().setAttribute(Constantes.INFORMEMENSUAL, informe);
            
        }
        
                
        if(informe == null){
            return null;
        }        
        
        Map<String, Object> params = new HashMap<>();
        
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params.put("informe", informe.getId());
        
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
            params = informeMensualDetalleDao.lista(params);
            try {
                generaReporte(tipo, (List<InformeMensualDetalle>) params.get(Constantes.INFORMEMENSUAL_DETALLE_LIST), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }
        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = informeMensualDetalleDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<InformeMensualDetalle>) params.get(Constantes.INFORMEMENSUAL_DETALLE_LIST), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeMensualDetalle.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("informeMensualDetalle.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = informeMensualDetalleDao.lista(params);
        modelo.addAttribute(Constantes.INFORMEMENSUAL_DETALLE_LIST, params.get(Constantes.INFORMEMENSUAL_DETALLE_LIST));
        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<InformeMensualDetalle> informeMensualDetalle = (List<InformeMensualDetalle>) params.get(Constantes.INFORMEMENSUAL_DETALLE_LIST);
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (informeMensualDetalle.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        return Constantes.INFORMEMENSUAL_DETALLE_PATH_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando InformeMensualDetalle {}", id);
        InformeMensualDetalle informeMensualDetalle = informeMensualDetalleDao.obtiene(id);
        modelo.addAttribute(Constantes.INFORMEMENSUAL_DETALLE, informeMensualDetalle);
        return Constantes.INFORMEMENSUAL_DETALLE_PATH_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nueva InformeMensualDetalle");
        InformeMensualDetalle informeMensualDetalle = new InformeMensualDetalle();

        modelo.addAttribute(Constantes.INFORMEMENSUAL_DETALLE, informeMensualDetalle);
        return Constantes.INFORMEMENSUAL_DETALLE_PATH_NUEVO;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid InformeMensualDetalle informeMensualDetalle, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.INFORMEMENSUAL_DETALLE_PATH_NUEVO;
        }
        try {
            informeMensualDetalle.setInformeMensual(informeMensualDao.obtiene(((InformeMensual)request.getSession().getAttribute(Constantes.INFORMEMENSUAL)).getId()));
            informeMensualDetalle.setCapturo(ambiente.obtieneUsuario());
            informeMensualDetalle.setCuando(new Date());
            informeMensualDetalle = informeMensualDetalleDao.crea(informeMensualDetalle);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el InformeMensualDetalle", e);
            return Constantes.INFORMEMENSUAL_DETALLE_PATH_NUEVO;
        }
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeMensualDetalle.creado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{informeMensualDetalle.getFecha().toString()});
        return "redirect:" + Constantes.INFORMEMENSUAL_DETALLE_PATH_LISTA;
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita InformeMensualDetalle {}", id);
        InformeMensualDetalle informeMensualDetalle = informeMensualDetalleDao.obtiene(id);
        modelo.addAttribute(Constantes.INFORMEMENSUAL_DETALLE, informeMensualDetalle);
        return Constantes.INFORMEMENSUAL_DETALLE_PATH_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid InformeMensualDetalle informeMensualDetalle, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.INFORMEMENSUAL_DETALLE_PATH_EDITA;
        }
        try {
            informeMensualDetalle.setInformeMensual(informeMensualDao.obtiene(((InformeMensual)request.getSession().getAttribute(Constantes.INFORMEMENSUAL)).getId()));
            informeMensualDetalle.setCapturo(ambiente.obtieneUsuario());
            informeMensualDetalle.setCuando(new Date());
            informeMensualDetalle = informeMensualDetalleDao.crea(informeMensualDetalle);
        } catch (org.hibernate.exception.ConstraintViolationException e) {
            log.error("No se pudo crear el InformeMensualDetalle", e);
            return Constantes.INFORMEMENSUAL_DETALLE_PATH_EDITA;
        }
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeMensualDetalle.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{informeMensualDetalle.getFecha().toString()});
        return "redirect:" + Constantes.INFORMEMENSUAL_DETALLE_PATH_LISTA;
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute InformeMensualDetalle informeMensualDetalle, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina Pais");
        try {
            String nombre = informeMensualDetalleDao.elimina(id);
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeMensualDetalle.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el pais " + id, e);
            bindingResult.addError(new ObjectError(Constantes.PAIS_LIST, new String[]{"informeMensualDetalle.no.elimina.message"}, null, null));
            return Constantes.INFORMEMENSUAL_DETALLE_PATH_VER;
        }
        return "redirect:" + Constantes.INFORMEMENSUAL_DETALLE_PATH_LISTA;
    }

    private void generaReporte(String tipo, List<InformeMensualDetalle> informeMensualDetalle, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(informeMensualDetalle);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=informeMensualDetallees.pdf");
                break;
            case "CSV":
                archivo = generaCsv(informeMensualDetalle);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=informeMensualDetallees.csv");
                break;
            case "XLS":
                archivo = generaXls(informeMensualDetalle);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=informeMensualDetallees.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<InformeMensualDetalle> informeMensualDetalle, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(informeMensualDetalle);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(informeMensualDetalle);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(informeMensualDetalle);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("informeMensualDetalle.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List informeMensualDetalle) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/informeMensualDetallees.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(informeMensualDetalle));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List informeMensualDetalle) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/informeMensualDetallees.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(informeMensualDetalle));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List informeMensualDetalle) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/informeMensualDetallees.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(informeMensualDetalle));
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
