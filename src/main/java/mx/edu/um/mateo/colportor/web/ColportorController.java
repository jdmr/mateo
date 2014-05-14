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
import javax.validation.Valid;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.ColportorDao;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.LabelValueBean;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
 * @author osoto
 */
@Controller
@RequestMapping(Constantes.PATH_COLPORTOR)
public class ColportorController extends BaseController {

    
    @Autowired
    private ColportorDao colportorDao;
    @Autowired
    private RolDao rolDao;
    /*
     * DE AQUI @InitBinder public void initBinder(WebDataBinder binder) {
     *
     * binder.registerCustomEditor(TipoColportor.class, new
     * EnumEditor(TipoColportor.class)); }
     */

    @PreAuthorize("hasRole('ROLE_ASOC')")
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
        log.debug("Mostrando lista de Colportor");
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());

        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = colportorDao.lista(params);
            
            try {
                generaReporte(tipo, (List<Colportor>) params.get("colportores"), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = colportorDao.lista(params);
            
            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Colportor>) params.get("colportores"), request);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("colportor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = colportorDao.lista(params);
        
        modelo.addAttribute(Constantes.COLPORTOR_LIST, params.get(Constantes.COLPORTOR_LIST));
        this.pagina(params, modelo, Constantes.COLPORTOR_LIST, pagina);

        return Constantes.PATH_COLPORTOR_LISTA;
    }
    
    @PreAuthorize("hasRole('ROLE_ASOC')")
    @RequestMapping(value="/get_colportor_list", method = RequestMethod.GET, headers="Accept=*/*", produces = "application/json")    
    public @ResponseBody 
    List <LabelValueBean> getColportorList(@RequestParam("term") String filtro, HttpServletResponse response){
        log.debug("Buscando colportores por {}", filtro);
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params.put("filtro", filtro);
        colportorDao.lista(params);
        
        List <LabelValueBean> rValues = new ArrayList<>();
        List <Colportor> clps = (List <Colportor>) params.get(Constantes.COLPORTOR_LIST);
        for(Colportor c : clps){
            log.debug("Colportor {}", c.getClave());
            StringBuilder sb = new StringBuilder();
            sb.append(c.getClave());
            sb.append(" | ");
            sb.append(c.getNombreCompleto());   
            //Por alguna razon, el jQuery toma el valor del attr value por default.
            //Asi que en el constructor invertimos los valores: como value va el string, y como nombre la clave
            rValues.add(new LabelValueBean(c.getId(), sb.toString(), c.getClave()));
        }        
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        return rValues;        
    }

    @PreAuthorize("hasRole('ROLE_ASOC')")
    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando colportor {}", id);
        Colportor colportor = colportorDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_COLPORTOR, colportor);

        return Constantes.PATH_COLPORTOR_VER;
    }

    @PreAuthorize("hasRole('ROLE_ASOC')")
    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo colportor");
        Colportor colportores = new Colportor();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_COLPORTOR, colportores);
        return Constantes.PATH_COLPORTOR_NUEVO;
    }

    /**
     * TODO
     *
     * @param request
     * @param response
     * @param colportor
     * @param bindingResult
     * @param errors
     * @param modelo
     * @param redirectAttributes
     * @return
     * @throws ParseException
     */
    @PreAuthorize("hasRole('ROLE_ASOC')")
    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Colportor colportor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        log.debug("Entrando al metodo 'crea'");
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        log.debug("bindingResult");
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma ");
            utils.despliegaBindingResultErrors(bindingResult);
            return Constantes.PATH_COLPORTOR_NUEVO;
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            colportor = colportorDao.crea(colportor, usuario);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.creado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{colportor.getNombre()});
            
            return "redirect:" + Constantes.PATH_COLPORTOR_VER + "/" + colportor.getId();
        } catch (Exception e) {
            log.error("No se pudo crear el colportor", e);
            return Constantes.PATH_COLPORTOR_NUEVO;
        }

    }

    @PreAuthorize("hasRole('ROLE_ASOC')")
    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable Long id, Model modelo) {
        log.debug("Editar colportor {}", id);
        Rol roles = rolDao.obtiene("ROLE_COL");
        Colportor colportor = colportorDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_COLPORTOR, colportor);
        request.getSession().setAttribute(Constantes.COLPORTOR, colportor);
        return Constantes.PATH_COLPORTOR_EDITA;
    }

    @PreAuthorize("hasRole('ROLE_ASOC')")
    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Colportor colportor, BindingResult bindingResult, 
            Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            utils.despliegaBindingResultErrors(bindingResult);
            return Constantes.PATH_COLPORTOR_EDITA;
        }

        try {
            log.debug("Colportor FechaDeNacimiento" + colportor.getFechaDeNacimiento());
            Colportor clp = (Colportor)request.getSession().getAttribute(Constantes.COLPORTOR);
            colportor.setEjercicio(clp.getEjercicio());
            colportor.setEmpresa(clp.getEmpresa());
            colportor.setAlmacen(clp.getAlmacen());
            colportor.setCentrosDeCosto(clp.getCentrosDeCosto());
            colportor.setEnabled(clp.getEnabled());
            colportor.setPassword(clp.getPassword());
            colportor = colportorDao.actualiza(colportor);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la colportor", e);
            return Constantes.PATH_COLPORTOR_EDITA;
        }finally{
            request.getSession().removeAttribute(Constantes.COLPORTOR);
        }        

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{colportor.getNombre()});

        return "redirect:" + Constantes.PATH_COLPORTOR_VER + "/" + colportor.getId();
    }

    @PreAuthorize("hasRole('ROLE_ASOC')")
    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Colportor colportores, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina colportor");
        try {
            String nombre = colportorDao.elimina(id);
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "colportor.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});

        } catch (Exception e) {
            log.error("No se pudo eliminar el colportor " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_COLPORTOR, new String[]{"colportor.no.eliminado.message"}, null, null));
            return Constantes.PATH_COLPORTOR_VER;
        }

        return "redirect:" + Constantes.PATH_COLPORTOR;
    }

    @PreAuthorize("hasRole('ROLE_ASOC')")
    private void generaReporte(String tipo, List<Colportor> colportores, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(colportores);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=Colportores.pdf");
                break;
            case "CSV":
                archivo = generaCsv(colportores);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=Colportores.csv");
                break;
            case "XLS":
                archivo = generaXls(colportores);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=Colportores.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    @PreAuthorize("hasRole('ROLE_ASOC')")
    private void enviaCorreo(String tipo, List<Colportor> colportores, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(colportores);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(colportores);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(colportores);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("colportor.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    @PreAuthorize("hasRole('ROLE_ASOC')")
    private byte[] generaPdf(List colportores) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colportores.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(colportores));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    @PreAuthorize("hasRole('ROLE_ASOC')")
    private byte[] generaCsv(List colportores) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colportores.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(colportores));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    @PreAuthorize("hasRole('ROLE_ASOC')")
    private byte[] generaXls(List colportores) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/colportores.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(colportores));
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