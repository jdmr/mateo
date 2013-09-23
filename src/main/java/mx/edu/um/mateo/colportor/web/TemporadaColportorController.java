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
import javax.validation.Valid;
import mx.edu.um.mateo.colportor.dao.AsociadoDao;
import mx.edu.um.mateo.colportor.dao.ColportorDao;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.dao.TemporadaDao;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.LabelValueBean;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.rh.dao.ColegioDao;
import mx.edu.um.mateo.rh.model.Colegio;
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
@RequestMapping(Constantes.TEMPORADACOLPORTOR_PATH)
public class TemporadaColportorController extends BaseController{

    @Autowired
    private TemporadaColportorDao temporadaColportorDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private TemporadaDao temporadaDao;
    @Autowired
    private AsociadoDao asociadoDao;
    @Autowired
    private ColportorDao colportorDao;
    @Autowired
    private ColegioDao colegioDao;
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
            Model modelo) {
        log.debug("Mostrando lista de Temporada Colportor");
        
        //Por default, utilizamos ahora la busqueda por asociado
        Map<String, Object> params = new HashMap<>();
        params.put("asociado", ambiente.obtieneUsuario().getId());
        
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
            params = temporadaColportorDao.lista(params);
            try {
                generaReporte(tipo, (List<TemporadaColportor>) params.get(Constantes.TEMPORADACOLPORTOR_LIST), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = temporadaColportorDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<TemporadaColportor>) params.get(Constantes.TEMPORADACOLPORTOR_LIST), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("temporadaColportor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = temporadaColportorDao.lista(params);
        modelo.addAttribute(Constantes.TEMPORADACOLPORTOR_LIST, params.get(Constantes.TEMPORADACOLPORTOR_LIST));

        pagina = (Long) params.get("pagina");
        this.pagina(params, modelo, Constantes.TEMPORADACOLPORTOR_LIST, pagina);

        return Constantes.TEMPORADACOLPORTOR_PATH_LISTA;
    }
    
    @RequestMapping(value="/get_temporada_clp_list", method = RequestMethod.GET, headers="Accept=*/*", produces = "application/json")    
    public @ResponseBody 
    List <LabelValueBean> getTemporadaColportorList(@RequestParam("clave") String filtro, HttpServletResponse response){
        log.debug("Buscando temporadas del colportor por {}", filtro);
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params.put("asociado", ambiente.obtieneUsuario().getId());
        params.put("filtro", filtro);
        temporadaColportorDao.listadoTemporadasPorColportor(params);
        
        List <LabelValueBean> rValues = new ArrayList<>();
        List <TemporadaColportor> clps = (List <TemporadaColportor>) params.get(Constantes.TEMPORADACOLPORTOR_LIST);
        for(TemporadaColportor tc : clps){
            log.debug("Temporada Colportor {} - {}", tc.getColportor().getClave(), tc.getTemporada().getNombre());
            StringBuilder sb = new StringBuilder();
            sb.append(tc.getTemporada().getNombre()); 
            //Por alguna razon, el jQuery toma el valor del attr value por default.
            //Asi que en el constructor invertimos los valores: como value va el string, y como nombre la clave
            rValues.add(new LabelValueBean(tc.getId(), sb.toString()));
        }        
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        return rValues;        
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando Temporada Colportor {}", id);
        TemporadaColportor temporadaColportor = temporadaColportorDao.obtiene(id);
        modelo.addAttribute(Constantes.TEMPORADACOLPORTOR, temporadaColportor);
        return Constantes.TEMPORADACOLPORTOR_PATH_VER;
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo, HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        log.debug("Nueva Temporada Colportor");
        modelo.addAttribute(Constantes.TEMPORADACOLPORTOR, new TemporadaColportor());

        params.put("organizacion", ambiente.obtieneUsuario().getEmpresa().getOrganizacion().getId());
        params = temporadaDao.lista(params);        
        modelo.addAttribute(Constantes.TEMPORADA_LIST, (List) params.get(Constantes.TEMPORADA_LIST));
      
        params = colegioDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_COLEGIOS, (List) params.get(Constantes.CONTAINSKEY_COLEGIOS));
        
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params = colportorDao.lista(params);
        modelo.addAttribute(Constantes.COLPORTOR_LIST, (List) params.get(Constantes.COLPORTOR_LIST));
        
        params = asociadoDao.lista(params);
        modelo.addAttribute(Constantes.ASOCIADO_LIST, (List) params.get(Constantes.ASOCIADO_LIST));
        
        Map<String, Object> temporadas = temporadaDao.lista(null);
        params = temporadaDao.lista(params);
        modelo.addAttribute(Constantes.TEMPORADA_LIST, (List)params.get(Constantes.TEMPORADA_LIST));
        
        return Constantes.TEMPORADACOLPORTOR_PATH_NUEVA;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid TemporadaColportor temporadaColportor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        log.info("creando Temporada Colportor");
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));

        }
        
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando" + bindingResult.getAllErrors());
            despliegaBindingResultErrors(bindingResult);
            return Constantes.TEMPORADACOLPORTOR_PATH_NUEVA;
        }
        
        try {
            Usuario usuario = ambiente.obtieneUsuario();
            Asociado asociado = asociadoDao.obtiene(usuario.getId());
            temporadaColportor.setAsociado(asociado);
            log.debug("Asociado asignado a la TmpClp {}", asociado);
            
            Temporada temporada = temporadaDao.obtiene(temporadaColportor.getTemporada().getId());
            temporadaColportor.setTemporada(temporada);
            Colportor colportor = colportorDao.obtiene(temporadaColportor.getColportor().getId());
            temporadaColportor.setColportor(colportor);
            Colegio colegio = colegioDao.obtiene(temporadaColportor.getColegio().getId());
            temporadaColportor.setColegio(colegio);
            
            if(temporadaColportor.getId() == null){
                temporadaColportor.setAsociado(asociado);
                temporadaColportor.setStatus(Constantes.STATUS_ACTIVO);
                temporadaColportor.setFecha(new Date());
            }
            
            temporadaColportor = temporadaColportorDao.crea(temporadaColportor);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la temporada Colportor", e);
            return Constantes.TEMPORADACOLPORTOR_PATH_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.graba.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{temporadaColportor.getStatus()});

        return "redirect:" + Constantes.TEMPORADACOLPORTOR_PATH_VER + "/" + temporadaColportor.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita Temporada {}", id);
        
        TemporadaColportor temporadaColportor = temporadaColportorDao.obtiene(id);
        modelo.addAttribute(Constantes.TEMPORADACOLPORTOR, temporadaColportor);
        
        Map<String, Object> params = new HashMap<>();
        params.put("organizacion", ambiente.obtieneUsuario().getEmpresa().getOrganizacion().getId());
        params = temporadaDao.lista(params);        
        modelo.addAttribute(Constantes.TEMPORADA_LIST, (List) params.get(Constantes.TEMPORADA_LIST));
      
        params = colegioDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_COLEGIOS, (List) params.get(Constantes.CONTAINSKEY_COLEGIOS));
        
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params = colportorDao.lista(params);
        modelo.addAttribute(Constantes.COLPORTOR_LIST, (List) params.get(Constantes.COLPORTOR_LIST));
        
        params = asociadoDao.lista(params);
        modelo.addAttribute(Constantes.ASOCIADO_LIST, (List) params.get(Constantes.ASOCIADO_LIST));
        
        Map<String, Object> temporadas = temporadaDao.lista(null);
        params = temporadaDao.lista(params);
        modelo.addAttribute(Constantes.TEMPORADA_LIST, (List)params.get(Constantes.TEMPORADA_LIST));

        
        return Constantes.TEMPORADACOLPORTOR_PATH_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid TemporadaColportor temporadaColportor, 
        BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            despliegaBindingResultErrors(bindingResult);
            return Constantes.TEMPORADACOLPORTOR_PATH_EDITA;
        }
        //try fechaInicio
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(Constantes.DATE_SHORT_HUMAN_PATTERN);
            temporadaColportor.setFecha(sdf.parse(request.getParameter("fecha")));
        } catch (ConstraintViolationException e) {
            log.error("Fecha  Incorrecta", e);
            return Constantes.TEMPORADACOLPORTOR_PATH_NUEVA;
        }
        try {
            Usuario usuario = ambiente.obtieneUsuario();
            
            Temporada temporada = temporadaDao.obtiene(temporadaColportor.getTemporada().getId());
            temporadaColportor.setTemporada(temporada);
            Asociado asociado = asociadoDao.obtiene(ambiente.obtieneUsuario().getId());
            temporadaColportor.setAsociado(asociado);
            Colportor colportor = colportorDao.obtiene(temporadaColportor.getColportor().getId());
            temporadaColportor.setColportor(colportor);

            Colegio colegio = colegioDao.obtiene(temporadaColportor.getColegio().getId());
            temporadaColportor.setColegio(colegio);
            temporadaColportor = temporadaColportorDao.actualiza(temporadaColportor);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al Asociacion", e);
            return Constantes.TEMPORADACOLPORTOR_PATH_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.actualizada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{temporadaColportor.getObservaciones()});

        return "redirect:" + Constantes.TEMPORADACOLPORTOR_PATH_VER + "/" + temporadaColportor.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute TemporadaColportor temporadaColportor, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina Temporada Colportor");
        try {
            String nombre = temporadaColportorDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "temporadaColportor.eliminada.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el asociado " + id, e);
            bindingResult.addError(new ObjectError(Constantes.TEMPORADACOLPORTOR_LIST, new String[]{"temporadaColportor.no.eliminada.message"}, null, null));
            return Constantes.TEMPORADACOLPORTOR_PATH_VER;
        }

        return "redirect:" + Constantes.TEMPORADACOLPORTOR_PATH;
    }

    private void generaReporte(String tipo, List<TemporadaColportor> temporadaColportor, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case Constantes.TIPO_DOCUMENTO_PDF:
                archivo = generaPdf(temporadaColportor);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=temporadacolportor.pdf");
                break;
            case Constantes.TIPO_DOCUMENTO_CSV:
                archivo = generaCsv(temporadaColportor);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=temporadacolportor.csv");
                break;
            case Constantes.TIPO_DOCUMENTO_XLS:
                archivo = generaXls(temporadaColportor);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=temporadacolportor.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }
    }

    private void enviaCorreo(String tipo, List<TemporadaColportor> temporadaColportor, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case Constantes.TIPO_DOCUMENTO_PDF:
                archivo = generaPdf(temporadaColportor);
                tipoContenido = "application/pdf";
                break;
            case Constantes.TIPO_DOCUMENTO_CSV:
                archivo = generaCsv(temporadaColportor);
                tipoContenido = "text/csv";
                break;
            case Constantes.TIPO_DOCUMENTO_XLS:
                archivo = generaXls(temporadaColportor);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("temporadaColportor.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List temporadaColportor) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/temporadacolportor.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(temporadaColportor));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List temporadaColportor) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/temporadacolportor.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(temporadaColportor));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List temporadaColportor) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/temporadacolportor.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(temporadaColportor));
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
