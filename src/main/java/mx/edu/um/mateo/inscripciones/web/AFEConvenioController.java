/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inscripciones.dao.AlumnoDao;
import mx.edu.um.mateo.inscripciones.model.AFEConvenio;
import mx.edu.um.mateo.inscripciones.model.Alumno;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import mx.edu.um.mateo.inscripciones.service.AFEConvenioManager;
import mx.edu.um.mateo.inscripciones.service.TiposBecasManager;
import mx.edu.um.mateo.inscripciones.utils.MatriculaInvalidaException;
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
import org.hibernate.context.spi.CurrentSessionContext;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author zorch
 */
@Controller
@RequestMapping(Constantes.PATH_AFECONVENIO)
public class AFEConvenioController extends BaseController{
    private static final Logger log = LoggerFactory.getLogger(mx.edu.um.mateo.inscripciones.web.TiposBecasController.class);
    @Autowired
    private AFEConvenioManager afeConvenioManager;
    @Autowired
    private TiposBecasManager tiposManager;
    @Autowired
    private AlumnoDao alDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
   
    
    @RequestMapping ({"","/lista"})
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Alumno alumno,
            Errors errors,
            Model modelo) {
        log.debug("Mostrando lista de convenios");
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
            params = afeConvenioManager.lista(params);
            try {
                generaReporte(tipo, (List<AFEConvenio>) params.get(Constantes.CONTAINSKEY_AFECONVENIO), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }
        
        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = afeConvenioManager.lista(params);
            
            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<AFEConvenio>) params.get(Constantes.CONTAINSKEY_AFECONVENIO), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("afeConvenio.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = afeConvenioManager.lista(params);
        log.debug("params{}",params.get(Constantes.CONTAINSKEY_AFECONVENIO));
        modelo.addAttribute(Constantes.CONTAINSKEY_AFECONVENIO, params.get(Constantes.CONTAINSKEY_AFECONVENIO));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<AFEConvenio> afeConvenio = (List<AFEConvenio>) params.get(Constantes.CONTAINSKEY_AFECONVENIO);
        Long primero = ((pagina - 1) * max) + 1;
        log.debug("primero {}",primero);
        log.debug("Conveniossize {}",afeConvenio.size());
        Long ultimo = primero + (afeConvenio.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        log.debug("Paginacion{}", paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        log.debug("paginas{}",paginas);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        log.debug("Pagina{}",pagina);
        // termina paginado

        return Constantes.PATH_AFECONVENIO_LISTA ;
    }
    
    
    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando convenio {}", id);
        AFEConvenio afeConvenio = afeConvenioManager.obtiene(id);
        
        modelo.addAttribute(Constantes.ADDATTRIBUTE_AFECONVENIO, afeConvenio);
        
        return Constantes.PATH_AFECONVENIO_VER;
    }
    
//    Se comento porque se a creado un nuevo metodo llamando asignarConvenio
//     @RequestMapping("/nuevo")
//    public String nueva(Model modelo) {
//        log.debug("Nuevo convenio");
//        Map<String, Object>params= new HashMap<>();
//        AFEConvenio afeConvenio = new AFEConvenio();
//        params= tiposManager.getTiposBeca(params);
//        List<TiposBecas> listaTipos= (List)params.get(Constantes.CONTAINSKEY_TIPOSBECAS);
//        modelo.addAttribute(Constantes.CONTAINSKEY_TIPOSBECAS, listaTipos);
//        modelo.addAttribute(Constantes.ADDATTRIBUTE_AFECONVENIO, afeConvenio);
//        return Constantes.PATH_AFECONVENIO_NUEVO;
//    }
    
     @Transactional
    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid AFEConvenio afeConvenio, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
         log.debug("ENTRANDO al metodo graba");
         for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            despliegaBindingResultErrors(bindingResult);
            modelo.addAttribute(Constantes.ADDATTRIBUTE_AFECONVENIO, afeConvenio);
            errors.rejectValue("matricula", "error.convenio.message",
					new String[] { afeConvenio.getMatricula() }, null);
            return Constantes.PATH_AFECONVENIO_NUEVO;
        }
        
        try {
           log.debug("obteniendo usuario");
            Usuario usuario = ambiente.obtieneUsuario();
             afeConvenioManager.graba(afeConvenio, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo grabar convenio", e);
            return Constantes.PATH_AFECONVENIO_NUEVO;
        }catch(MatriculaInvalidaException ex){
            log.error("No se pudo encontrar matricula del convenio", ex);
			errors.rejectValue("matricula", "matricula.no.valida.message",
					new String[] { afeConvenio.getMatricula() }, null);
        return Constantes.PATH_AFECONVENIO_VER;
        }
        
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "afeConvenio.graba.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{afeConvenio.getMatricula()});
        
        return "redirect:" + Constantes.PATH_AFECONVENIO_LISTA  ;
    }
    
    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar convenio {}", id);
        Map<String, Object>params= new HashMap<>();
        AFEConvenio afeConvenio = afeConvenioManager.obtiene(id);
        params= tiposManager.getTiposBeca(params);
        List<TiposBecas> listaTipos= (List)params.get(Constantes.CONTAINSKEY_TIPOSBECAS);
        modelo.addAttribute(Constantes.CONTAINSKEY_TIPOSBECAS, listaTipos);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_AFECONVENIO, afeConvenio);
        return Constantes.PATH_AFECONVENIO_EDITA;
    }
    
    
    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute AFEConvenio afeConvenio, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina cuenta de convenios");
        try {
          String convenio= afeConvenioManager.elimina(id);
            
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "afeConvenio.elimina.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{convenio.toString()});
        } catch (Exception e) {
            log.error("No se pudo eliminar el convenio " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_AFECONVENIO, new String[]{"afeConvenio.no.elimina.message"}, null, null));
            return Constantes.PATH_AFECONVENIO_VER;
        }
        
        return "redirect:" + Constantes.PATH_AFECONVENIO_LISTA ;
    }
    
    @RequestMapping(value = "/asignarConvenio")
    public String asignarConvenio(HttpServletRequest request, Model modelo, @Valid AFEConvenio afeConvenio,Errors errors, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        try {
            AFEConvenio afeConvenio2 = afeConvenioManager.asignarConvenio(afeConvenio);
            modelo.addAttribute(Constantes.ADDATTRIBUTE_AFECONVENIO, afeConvenio2);
            
        } catch (MatriculaInvalidaException ex) {
            log.error("No se pudo encontrar matricula del convenio", ex);
			errors.rejectValue("matricula", "matricula.no.valida.message",
					new String[] { afeConvenio.getMatricula() }, null);
                        return Constantes.PATH_AFECONVENIO_CONVENIO;
        }
        
        return  Constantes.PATH_AFECONVENIO_CONVENIO;
    }
    
    @RequestMapping(value = "/obtenerAlumno")
    public String obtenerAlumno(HttpServletRequest request, Model modelo, @Valid AFEConvenio afeConvenio,Errors errors, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        
          Alumno alumno = alDao.obtiene(afeConvenio.getMatricula());
          afeConvenio.setAlumno(alumno);
          modelo.addAttribute(Constantes.ADDATTRIBUTE_AFECONVENIO, afeConvenio);
          
          if(alumno == null){
             errors.rejectValue("matricula", "matricula.no.valida.message",
					new String[] { afeConvenio.getMatricula() }, null);
            }
        
        return  Constantes.PATH_AFECONVENIO_CONVENIO;
    }
    
     private void generaReporte(String tipo, List<AFEConvenio> afeConvenios, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(afeConvenios);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=Convenios.pdf");
                break;
            case "CSV":
                archivo = generaCsv(afeConvenios);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=Convenios.csv");
                break;
            case "XLS":
                archivo = generaXls(afeConvenios);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=Convenios.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }
        
    }
    
    private void enviaCorreo(String tipo, List<AFEConvenio> afeConvenios, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(afeConvenios);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(afeConvenios);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(afeConvenios);
                tipoContenido = "application/vnd.ms-excel";
        }
        
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("afeConvenio.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }
    
    private byte[] generaPdf(List afeConvenios) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/afeConvenio.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(afeConvenios));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);
        
        return archivo;
    }
    
    private byte[] generaCsv(List afeConvenios) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/afeConvenios.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(afeConvenios));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();
        
        return archivo;
    }
    
    private byte[] generaXls(List afeConvenios) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/afeConvenios.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(afeConvenios));
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
