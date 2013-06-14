/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.colportor.dao.TipoColportorDao;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.TipoColportor;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.web.BaseController;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(Constantes.PATH_TIPO_COLPORTOR)
public class TipoColportorController extends BaseController {
    
    @Autowired
    private TipoColportorDao tipoColportorDao;
    
    /*
     * DE AQUI @InitBinder public void initBinder(WebDataBinder binder) {
     *
     * binder.registerCustomEditor(TipoColportor.class, new
     * EnumEditor(TipoColportor.class)); }
     */

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
        log.debug("Mostrando lista de TipoColportor");
        Map<String, Object> params = new HashMap<>();
        params.put(Constantes.ADDATTRIBUTE_ASOCIACION, ((Asociacion) request.getSession().getAttribute(Constantes.SESSION_ASOCIACION)));

        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = tipoColportorDao.lista(params);
            
            try {
                generaReporte(tipo, (List<TipoColportor>) params.get("colportores"), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            
            params = tipoColportorDao.lista(params);
            

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<TipoColportor>) params.get("colportores"), request);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("tipoColportor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = tipoColportorDao.lista(params);
        
        modelo.addAttribute(Constantes.CONTAINSKEY_TIPO_COLPORTOR, params.get(Constantes.CONTAINSKEY_TIPO_COLPORTOR));
        this.pagina(params, modelo, Constantes.CONTAINSKEY_TIPO_COLPORTOR, pagina);

        return Constantes.PATH_TIPO_COLPORTOR_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando tipoColportor {}", id);
        TipoColportor tipoColportor = tipoColportorDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_TIPO_COLPORTOR, tipoColportor);

        return Constantes.PATH_TIPO_COLPORTOR_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo tipoColportor");
        TipoColportor colportor = new TipoColportor();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_TIPO_COLPORTOR, colportor);
        return Constantes.PATH_TIPO_COLPORTOR_NUEVO;
    }

    /**
     * TODO
     *
     * @param request
     * @param response
     * @param tipoColportor
     * @param bindingResult
     * @param errors
     * @param modelo
     * @param redirectAttributes
     * @return
     * @throws ParseException
     */
    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid TipoColportor tipoColportor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        log.debug("Entrando al metodo 'crea'");
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        log.debug("bindingResult");
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma ");
            utils.despliegaBindingResultErrors(bindingResult);
            return Constantes.PATH_TIPO_COLPORTOR_NUEVO;
        }
        
        try {
            
            tipoColportor = tipoColportorDao.crea(tipoColportor);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "tipoColportor.creado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{tipoColportor.getTipoColportor()});
            
            return "redirect:" + Constantes.PATH_TIPO_COLPORTOR_VER + "/" + tipoColportor.getId();
        } catch (Exception e) {
            log.error("No se pudo crear el tipoColportor", e);
            return Constantes.PATH_TIPO_COLPORTOR_NUEVO;
        }

    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar tipoColportor {}", id);
        TipoColportor colportor = tipoColportorDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_TIPO_COLPORTOR, colportor);
        
        return Constantes.PATH_TIPO_COLPORTOR_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid TipoColportor tipoColportor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            utils.despliegaBindingResultErrors(bindingResult);
            return Constantes.PATH_TIPO_COLPORTOR_EDITA;
        }

        try {
            tipoColportor = tipoColportorDao.actualiza(tipoColportor);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el tipoColportor", e);
            return Constantes.PATH_TIPO_COLPORTOR_EDITA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "tipoColportor.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{tipoColportor.getTipoColportor()});

        return "redirect:" + Constantes.PATH_TIPO_COLPORTOR_VER + "/" + tipoColportor.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute TipoColportor tipoColportor, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina tipoColportor");
        try {
            String nombre = tipoColportorDao.elimina(id);
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "tipoColportor.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});

        } catch (Exception e) {
            log.error("No se pudo eliminar el tipoColportor " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_TIPO_COLPORTOR, new String[]{"tipoColportor.no.eliminado.message"}, null, null));
            return Constantes.PATH_TIPO_COLPORTOR_VER;
        }

        return "redirect:" + Constantes.PATH_TIPO_COLPORTOR;
    }

    private void generaReporte(String tipo, List<TipoColportor> tiposColportor, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(tiposColportor);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=TiposColportor.pdf");
                break;
            case "CSV":
                archivo = generaCsv(tiposColportor);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=TiposColportor.csv");
                break;
            case "XLS":
                archivo = generaXls(tiposColportor);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=TiposColportor.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<TipoColportor> tiposColportor, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(tiposColportor);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(tiposColportor);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(tiposColportor);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("tipoColportor.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List tiposColportor) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colportores.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(tiposColportor));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List tiposColportor) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colportores.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(tiposColportor));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List tiposColportor) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colportores.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(tiposColportor));
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