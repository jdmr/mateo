/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
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
package mx.edu.um.mateo.rh.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.rh.dao.EstudiosEmpleadoDao;
import mx.edu.um.mateo.rh.model.EstudiosEmpleado;
import mx.edu.um.mateo.rh.model.NivelEstudios;
import net.sf.jasperreports.engine.*;
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
@RequestMapping(Constantes.PATH_ESTUDIOSEMPLEADO)
public class EstudiosEmpleadoController {
    
    private static final Logger log = LoggerFactory.getLogger(mx.edu.um.mateo.rh.web.EstudiosEmpleadoController.class);
    @Autowired
    private EstudiosEmpleadoDao estudiosEmpleadoDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
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
            Errors errors,
            Model modelo) {
        log.debug("Mostrando lista de estudiosEmpleados");
        Map<String, Object> params = new HashMap<>();
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
            params = estudiosEmpleadoDao.lista(params);
            try {
                generaReporte(tipo, (List<EstudiosEmpleado>) params.get(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = estudiosEmpleadoDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<EstudiosEmpleado>) params.get(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("estudiosEmpleado.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        List nivelesEstudios = Arrays.asList(NivelEstudios.values());
        modelo.addAttribute(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO, nivelesEstudios);
        params = estudiosEmpleadoDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO, params.get(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<EstudiosEmpleado> estudiosEmpleados = (List<EstudiosEmpleado>) params.get(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO);
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (estudiosEmpleados.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        // termina paginado

        return Constantes.PATH_ESTUDIOSEMPLEADO_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando estudiosEmpleado {}", id);
        EstudiosEmpleado estudiosEmpleado = estudiosEmpleadoDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_ESTUDIOSEMPLEADO, estudiosEmpleado);

        return Constantes.PATH_ESTUDIOSEMPLEADO_VER;
    }

    @RequestMapping("/nuevo")
    public String nueva(Model modelo) {
        log.debug("Nuevo estudiosEmpleado");
        EstudiosEmpleado estudiosEmpleado = new EstudiosEmpleado();
        List nivelesEstudios = Arrays.asList(NivelEstudios.values());
        modelo.addAttribute(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO, nivelesEstudios);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ESTUDIOSEMPLEADO, estudiosEmpleado);
        return Constantes.PATH_ESTUDIOSEMPLEADO_NUEVO;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid EstudiosEmpleado estudiosEmpleado, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.PATH_ESTUDIOSEMPLEADO_NUEVO;
        }

        try {
            List nivelesEstudios = Arrays.asList(NivelEstudios.values());
            modelo.addAttribute(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO, nivelesEstudios);
            log.debug("estudiosEmpleado "+ estudiosEmpleado.toString());
            estudiosEmpleado = estudiosEmpleadoDao.crea(estudiosEmpleado);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear estudiosEmpleado", e);
            return Constantes.PATH_ESTUDIOSEMPLEADO_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "estudiosEmpleado.creado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{estudiosEmpleado.getNombreEstudios().toString()});

        return "redirect:" + Constantes.PATH_ESTUDIOSEMPLEADO_VER + "/" + estudiosEmpleado.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar cuenta de estudiosEmpleado {}", id);
        EstudiosEmpleado estudiosEmpleado = estudiosEmpleadoDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ESTUDIOSEMPLEADO, estudiosEmpleado);
        return Constantes.PATH_ESTUDIOSEMPLEADO_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid EstudiosEmpleado estudiosEmpleado, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_ESTUDIOSEMPLEADO_EDITA;
        }
        try {
            List nivelesEstudios = Arrays.asList(NivelEstudios.values());
            modelo.addAttribute(Constantes.CONTAINSKEY_ESTUDIOSEMPLEADO, nivelesEstudios);
            estudiosEmpleado = estudiosEmpleadoDao.actualiza(estudiosEmpleado);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la cuenta de estudiosEmpleado", e);
            return Constantes.PATH_ESTUDIOSEMPLEADO_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "estudiosEmpleado.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{estudiosEmpleado.getNombreEstudios().toString()});

        return "redirect:" + Constantes.PATH_ESTUDIOSEMPLEADO_VER + "/" + estudiosEmpleado.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute EstudiosEmpleado estudiosEmpleado, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina cuenta de estudiosEmpleado");
        try {
            String nombre = estudiosEmpleadoDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "estudiosEmpleado.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar estudiosEmpleado " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_ESTUDIOSEMPLEADO, new String[]{"estudiosEmpleado.no.eliminado.message"}, null, null));
            return Constantes.PATH_ESTUDIOSEMPLEADO_VER;
        }

        return "redirect:" + Constantes.PATH_ESTUDIOSEMPLEADO;
    }

    private void generaReporte(String tipo, List<EstudiosEmpleado> estudiosEmpleados, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(estudiosEmpleados);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=EstudiosEmpleado.pdf");
                break;
            case "CSV":
                archivo = generaCsv(estudiosEmpleados);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=EstudiosEmpleado.csv");
                break;
            case "XLS":
                archivo = generaXls(estudiosEmpleados);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=EstudiosEmpleado.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<EstudiosEmpleado> estudiosEmpleados, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(estudiosEmpleados);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(estudiosEmpleados);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(estudiosEmpleados);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("estudiosEmpleado.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List estudiosEmpleados) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/estudiosEmpleados.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(estudiosEmpleados));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List estudiosEmpleados) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/estudiosEmpleados.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(estudiosEmpleados));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List estudiosEmpleados) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/estudiosEmpleados.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(estudiosEmpleados));
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
