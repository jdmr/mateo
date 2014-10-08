/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.contabilidad.facturas.service.ProveedorFacturasManager;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.web.BaseController;
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
import org.springframework.security.crypto.keygen.KeyGenerators;
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
@RequestMapping(Constantes.PATH_PROVEEDORFACTURAS)
public class ProveedorFacturasController extends BaseController {

    @Autowired
    private ProveedorFacturasManager manager;
    @Autowired
    private RolDao rolDao;

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
        log.debug("Mostrando lista de Colportor");
        Map<String, Object> params = new HashMap<>();
        Long empresaId = (Long) request.getSession().getAttribute("empresaId");
        params.put("empresa", empresaId);
        log.debug("paginacion{}", pagina);
        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = manager.lista(params);

            try {
                generaReporte(tipo, (List<ProveedorFacturas>) params.get(Constantes.CONTAINSKEY_PROVEEDORESFACTURAS), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }
        log.debug("paramsini{}", params);
        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = manager.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<ProveedorFacturas>) params.get(Constantes.CONTAINSKEY_PROVEEDORESFACTURAS), request);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("proveedor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = manager.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_PROVEEDORESFACTURAS, params.get(Constantes.CONTAINSKEY_PROVEEDORESFACTURAS));
        pagina(params, modelo, Constantes.CONTAINSKEY_PROVEEDORESFACTURAS, pagina);
        log.debug("paramsfin{}", params);

        return Constantes.PATH_PROVEEDORFACTURAS_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando colportor {}", id);
        ProveedorFacturas proveedorFacturas = manager.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_PROVEEDORFACTURAS, proveedorFacturas);

        return Constantes.PATH_PROVEEDORFACTURAS_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo colportor");
        ProveedorFacturas proveedorFacturas = new ProveedorFacturas();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_PROVEEDORFACTURAS, proveedorFacturas);
        return Constantes.PATH_PROVEEDORFACTURAS_NUEVO;
    }

    @Transactional
    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid ProveedorFacturas proveedorFacturas,
            BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        log.debug("Entrando al metodo 'crea'");
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        log.debug("bindingResult");
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma ");
            utils.despliegaBindingResultErrors(bindingResult);
            return Constantes.PATH_PROVEEDORFACTURAS_NUEVO;
        }

//        String password = null;
//        password = KeyGenerators.string().generateKey();
//        log.debug("passwordColportor" + password);
        try {
//            proveedorFacturas.setPassword(password);
            Usuario usuario = ambiente.obtieneUsuario();
            proveedorFacturas.setEjercicio(usuario.getEjercicio());
            log.debug("usuario logeado ...{}" + usuario.getEjercicio());
            proveedorFacturas.setPassword("prueba");
            manager.graba(proveedorFacturas, usuario);
            log.debug("usuario logeado enviado{}" + usuario.getEjercicio());

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "proveedor.creado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{proveedorFacturas.getNombre()});

            return "redirect:" + Constantes.PATH_PROVEEDORFACTURAS_LISTA;
        } catch (Exception e) {
            log.error("No se pudo crear el colportor", e);
            return Constantes.PATH_PROVEEDORFACTURAS_NUEVO;
        }
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar colportor {}", id);
        Rol roles = rolDao.obtiene("ROLE_PRV");
        ProveedorFacturas proveedorFacturas = manager.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_PROVEEDORFACTURAS, proveedorFacturas);
        modelo.addAttribute("roles", roles);
        return Constantes.PATH_PROVEEDORFACTURAS_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid ProveedorFacturas proveedorFacturas, BindingResult bindingResult,
            Errors errors, Model modelo, RedirectAttributes redirectAttributes) throws ParseException {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            utils.despliegaBindingResultErrors(bindingResult);
            return Constantes.PATH_PROVEEDORFACTURAS_EDITA;
        }

        try {
            String[] roles = request.getParameterValues("roles");
            log.debug("Asignando ROLE_PRV por defecto");
            roles = new String[]{"ROLE_PRV"};
            modelo.addAttribute("roles", roles);
            manager.actualiza(proveedorFacturas, ambiente.obtieneUsuario());
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la colportor", e);
            return Constantes.PATH_PROVEEDORFACTURAS_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "proveedor.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{proveedorFacturas.getNombre()});

        return "redirect:" + Constantes.PATH_PROVEEDORFACTURAS_LISTA;
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo,
            @ModelAttribute ProveedorFacturas proveedorFacturas, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina colportor");
        try {
            String rfc = manager.elimina(id);
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "proveedor.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{rfc});

        } catch (Exception e) {
            log.error("No se pudo eliminar el colportor " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_PROVEEDORFACTURAS, new String[]{"proveedor.no.eliminado.message"}, null, null));
            return Constantes.PATH_PROVEEDORFACTURAS_VER;
        }

        return "redirect:" + Constantes.PATH_PROVEEDORFACTURAS_LISTA;
    }

    private void generaReporte(String tipo, List<ProveedorFacturas> proveedorFacturas, HttpServletResponse response)
            throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(proveedorFacturas);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=proveedorFacturas.pdf");
                break;
            case "CSV":
                archivo = generaCsv(proveedorFacturas);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=proveedorFacturas.csv");
                break;
            case "XLS":
                archivo = generaXls(proveedorFacturas);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=proveedorFacturas.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<ProveedorFacturas> proveedorFacturas, HttpServletRequest request)
            throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(proveedorFacturas);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(proveedorFacturas);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(proveedorFacturas);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getCorreo());
        String titulo = messageSource.getMessage("proveedor.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List proveedorFacturas) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/proveedorFacturas.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(proveedorFacturas));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List proveedorFacturas) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/proveedorFacturas.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(proveedorFacturas));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List proveedorFacturas) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/proveedorFacturas.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(proveedorFacturas));
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
