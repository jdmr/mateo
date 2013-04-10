/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inscripciones.dao.InstitucionDao;
import mx.edu.um.mateo.inscripciones.model.CobroCampo;
import mx.edu.um.mateo.inscripciones.model.Institucion;
import mx.edu.um.mateo.inscripciones.service.CobroCampoManager;
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
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author develop
 */
@Controller
@RequestMapping(Constantes.PATH_COBROCAMPO)
public class CobroCampoController extends BaseController {

    @Autowired
    private CobroCampoManager manager;
    @Autowired
    private InstitucionDao institucionDao;
    @Autowired
    private UsuarioDao usuarioDao;

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
        log.debug("Mostrando lista de Cobros a Campos");
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
            params = manager.lista(params);
            try {
                generaReporte(tipo, (List<CobroCampo>) params.get(Constantes.CONTAINSKEY_COBROSCAMPOS), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = manager.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<CobroCampo>) params.get(Constantes.CONTAINSKEY_COBROSCAMPOS), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("cobroCampo.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = manager.lista(params);
        log.debug("params{}", params.get(Constantes.CONTAINSKEY_COBROSCAMPOS));
        modelo.addAttribute(Constantes.CONTAINSKEY_COBROSCAMPOS, params.get(Constantes.CONTAINSKEY_COBROSCAMPOS));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<CobroCampo> cobroCampos = (List<CobroCampo>) params.get(Constantes.CONTAINSKEY_COBROSCAMPOS);
        Long primero = ((pagina - 1) * max) + 1;
        log.debug("primero {}", primero);
        log.debug("cobroCampos size {}", cobroCampos.size());
        Long ultimo = primero + (cobroCampos.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        log.debug("Paginacion{}", paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        log.debug("paginas{}", paginas);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        log.debug("Pagina{}", pagina);
        // termina paginado

        return Constantes.PATH_COBROCAMPO_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando Cobro a Campo {}", id);
        CobroCampo cobroCampo = manager.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_COBROCAMPO, cobroCampo);

        return Constantes.PATH_COBROCAMPO_VER;
    }

    @RequestMapping("/nuevo")
    public String nueva(HttpServletRequest request, Model modelo) {
        Map<String, Object> params = new HashMap<>();
        log.debug("Nueva Cobro a Campo");
        params = institucionDao.lista(params);
        List<Institucion> instituciones = (List) params.get(Constantes.CONTAINSKEY_INSTITUCION);
        modelo.addAttribute(Constantes.CONTAINSKEY_INSTITUCION, instituciones);
        CobroCampo cobroCampo = new CobroCampo();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_COBROCAMPO, cobroCampo);
        params.put("empresa", request.getSession()
                .getAttribute("empresaId"));
        params.put("reporte", true);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_COBROCAMPO, cobroCampo);
        return Constantes.PATH_COBROCAMPO_NUEVO;



    }

    @Transactional
    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid CobroCampo cobroCampo, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession()
                    .getAttribute("empresaId"));
            return Constantes.PATH_COBROCAMPO_NUEVO;
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (cobroCampo.getId() == null) {
                cobroCampo.setUsuarioAlta(usuario);
                cobroCampo.setFechaAlta(new Date());
                Institucion institucion = institucionDao.obtiene(cobroCampo.getInstitucion().getId());
                cobroCampo.setInstitucion(institucion);
                log.debug("cobro campo {}", cobroCampo);
                manager.graba(cobroCampo, usuario);
            } else {
                CobroCampo cobroCampoTmp = manager.obtiene(cobroCampo.getId());
                cobroCampoTmp.setFechaAlta(cobroCampo.getFechaAlta());
                Usuario usuarioAlta = usuarioDao.obtiene(cobroCampo.getUsuarioAlta().getId());
                cobroCampoTmp.setUsuarioAlta(usuarioAlta);
                cobroCampoTmp.setUsuarioModificacion(usuario);
                cobroCampoTmp.setImporteEnsenanza(cobroCampo.getImporteEnsenanza());
                cobroCampoTmp.setImporteInternado(cobroCampo.getImporteInternado());
                cobroCampoTmp.setImporteMatricula(cobroCampo.getImporteMatricula());
                cobroCampoTmp.setStatus(cobroCampo.getStatus());
                cobroCampoTmp.setFechaModificacion(new Date());
                cobroCampoTmp.setMatricula(cobroCampo.getMatricula());
                Institucion institucion = institucionDao.obtiene(cobroCampo.getInstitucion().getId());
                cobroCampoTmp.setInstitucion(institucion);
                log.debug("cobro campo {}", cobroCampo);
                manager.graba(cobroCampoTmp, usuario);
            }
//if (cobroCampo.getId() != null)

        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el cobro a campo", e);
            return Constantes.PATH_COBROCAMPO_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "cobroCampo.graba.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{cobroCampo.getMatricula()});

        return "redirect:" + Constantes.PATH_COBROCAMPO_LISTA + "/";
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar cuenta de tipos de becas {}", id);
        Map<String, Object> params = new HashMap<>();
        params = institucionDao.lista(params);
        List<Institucion> instituciones = (List) params.get(Constantes.CONTAINSKEY_INSTITUCION);
        modelo.addAttribute(Constantes.CONTAINSKEY_INSTITUCION, instituciones);
        CobroCampo cobroCampo = manager.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_COBROCAMPO, cobroCampo);
        return Constantes.PATH_COBROCAMPO_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute CobroCampo cobroCampo, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina cuenta de prorrogas");
        try {
            manager.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "cobroCampo.elimina.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{cobroCampo.getMatricula()});
        } catch (Exception e) {
            log.error("No se pudo eliminar el cobro a campo " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_COBROCAMPO, new String[]{"cobroCampo.no.elimina.message"}, null, null));
            return Constantes.PATH_COBROCAMPO_VER;
        }

        return "redirect:" + Constantes.PATH_COBROCAMPO_LISTA;
    }

    private void generaReporte(String tipo, List<CobroCampo> cobroCampos, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(cobroCampos);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=CobroCampos.pdf");
                break;
            case "CSV":
                archivo = generaCsv(cobroCampos);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=CobroCampos.csv");
                break;
            case "XLS":
                archivo = generaXls(cobroCampos);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=CobroCampos.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<CobroCampo> cobroCampos, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(cobroCampos);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(cobroCampos);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(cobroCampos);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("cobroCampo.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List cobroCampo) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/cobroCampo.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(cobroCampo));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List cobroCampo) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/cobroCampo.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(cobroCampo));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List cobroCampo) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/cobroCampo.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(cobroCampo));
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
