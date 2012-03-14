/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.general.web;

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
import mx.edu.um.mateo.general.dao.EmpresaDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.UltimoException;
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
 * @author jdmr
 */
@Controller
@RequestMapping("/admin/empresa")
public class EmpresaController extends BaseController {

    @Autowired
    private EmpresaDao empresaDao;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de empresas");
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
            params = empresaDao.lista(params);
            try {
                generaReporte(tipo, (List<Empresa>) params.get("empresas"), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = empresaDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Empresa>) params.get("empresas"), request);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("empresa.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = empresaDao.lista(params);
        modelo.addAttribute("empresas", params.get("empresas"));

        // inicia paginado
        Long cantidad = (Long) params.get("cantidad");
        Integer max = (Integer) params.get("max");
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<Empresa> empresas = (List<Empresa>) params.get("empresas");
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (empresas.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute("paginacion", paginacion);
        modelo.addAttribute("paginas", paginas);
        // termina paginado

        return "admin/empresa/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando empresa {}", id);
        Empresa empresa = empresaDao.obtiene(id);

        modelo.addAttribute("empresa", empresa);

        return "admin/empresa/ver";
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nueva empresa");
        Empresa empresa = new Empresa();
        modelo.addAttribute("empresa", empresa);
        return "admin/empresa/nueva";
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Empresa empresa, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "admin/empresa/nueva";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            empresa = empresaDao.crea(empresa, usuario);

            ambiente.actualizaSesion(request, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al empresa", e);
            errors.rejectValue("codigo", "campo.duplicado.message", new String[]{"codigo"}, null);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "admin/empresa/nueva";
        }

        redirectAttributes.addFlashAttribute("message", "empresa.creada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{empresa.getNombre()});

        return "redirect:/admin/empresa/ver/" + empresa.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita empresa {}", id);
        Empresa empresa = empresaDao.obtiene(id);
        modelo.addAttribute("empresa", empresa);
        return "admin/empresa/edita";
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Empresa empresa, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "admin/empresa/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            empresa = empresaDao.actualiza(empresa, usuario);

            ambiente.actualizaSesion(request, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la empresa", e);
            errors.rejectValue("codigo", "campo.duplicado.message", new String[]{"codigo"}, null);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "admin/empresa/nueva";
        }

        redirectAttributes.addFlashAttribute("message", "empresa.actualizada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{empresa.getNombre()});

        return "redirect:/admin/empresa/ver/" + empresa.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Empresa empresa, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina empresa");
        try {
            String nombre = empresaDao.elimina(id);

            ambiente.actualizaSesion(request);

            redirectAttributes.addFlashAttribute("message", "empresa.eliminada.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (UltimoException e) {
            log.error("No se pudo eliminar el empresa " + id, e);
            bindingResult.addError(new ObjectError("empresa", new String[]{"ultima.empresa.no.eliminada.message"}, null, null));
            return "admin/empresa/ver";
        } catch (Exception e) {
            log.error("No se pudo eliminar la empresa " + id, e);
            bindingResult.addError(new ObjectError("empresa", new String[]{"empresa.no.eliminada.message"}, null, null));
            return "admin/empresa/ver";
        }

        return "redirect:/admin/empresa";
    }

    private void generaReporte(String tipo, List<Empresa> empresas, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(empresas);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=empresas.pdf");
                break;
            case "CSV":
                archivo = generaCsv(empresas);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=empresas.csv");
                break;
            case "XLS":
                archivo = generaXls(empresas);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=empresas.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<Empresa> empresas, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(empresas);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(empresas);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(empresas);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("empresa.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List lista) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/empresas.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(lista));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List lista) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/empresas.jrxml"));
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
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/empresas.jrxml"));
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
