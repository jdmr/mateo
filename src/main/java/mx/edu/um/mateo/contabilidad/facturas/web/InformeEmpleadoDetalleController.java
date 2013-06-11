/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeEmpleadoDetalleManager;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeEmpleadoManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inscripciones.model.FileUploadForm;
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
import org.apache.commons.io.IOUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author develop
 */
@Controller
@RequestMapping(Constantes.PATH_INFORMEEMPLEADODETALLE)
public class InformeEmpleadoDetalleController extends BaseController {

    @Autowired
    private InformeEmpleadoDetalleManager manager;
    @Autowired
    private InformeEmpleadoManager managerInforme;

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
        log.debug("Mostrando lista de informes");
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
                generaReporte(tipo, (List<InformeEmpleadoDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESDETALLES), response);
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
                enviaCorreo(correo, (List<InformeEmpleadoDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESDETALLES), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("detalle.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = manager.lista(params);
        log.debug("params{}", params.get(Constantes.CONTAINSKEY_INFORMESDETALLES));
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESDETALLES, params.get(Constantes.CONTAINSKEY_INFORMESDETALLES));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<InformeEmpleadoDetalle> detalles = (List<InformeEmpleadoDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESDETALLES);
        Long primero = ((pagina - 1) * max) + 1;
        log.debug("primero {}", primero);
        log.debug("detalles {}", detalles.size());
        Long ultimo = primero + (detalles.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        log.debug("Paginacion{}", paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        log.debug("paginas{}", paginas);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        log.debug("Pagina{}", pagina);
        // termina paginado

        return Constantes.PATH_INFORMEEMPLEADODETALLE_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando paquete {}", id);
        InformeEmpleadoDetalle detalle = manager.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEEMPLEADODETALLE, detalle);

        return Constantes.PATH_INFORMEEMPLEADODETALLE_VER;
    }

    @RequestMapping("/nuevo")
    public String nueva(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo paquete");
        Map<String, Object> params = new HashMap<>();
        params = managerInforme.lista(params);
        List<InformeEmpleado> informes = (List) params.get(Constantes.CONTAINSKEY_INFORMESEMPLEADO);
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESEMPLEADO, informes);

        InformeEmpleadoDetalle detalle = new InformeEmpleadoDetalle();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEEMPLEADODETALLE, detalle);
        params.put("empresa", request.getSession()
                .getAttribute("empresaId"));
        params.put("reporte", true);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEEMPLEADODETALLE, detalle);
        return Constantes.PATH_INFORMEEMPLEADODETALLE_NUEVO;
    }

    @Transactional
    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid InformeEmpleadoDetalle detalle,
            BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes,
            @ModelAttribute("uploadForm") FileUploadForm uploadForm) throws Exception {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession()
                    .getAttribute("empresaId"));

            this.despliegaBindingResultErrors(bindingResult);

            return Constantes.PATH_INFORMEEMPLEADODETALLE_NUEVO;
        }

        try {
            Map<String, Object> params = new HashMap<>();
            //Subir archivos
            List<MultipartFile> files = uploadForm.getFiles();

            List<String> fileNames = new ArrayList<String>();

            if (null != files && files.size() > 0) {
                for (MultipartFile multipartFile : files) {
                    String fileName = multipartFile.getOriginalFilename();
                    fileNames.add(fileName);
                    String uploadDir = "/home/develop/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename();
                    File dirPath = new File(uploadDir);
                    if (!dirPath.exists()) {
                        dirPath.mkdirs();
                    }
                    multipartFile.transferTo(new File("/home/develop/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename()));
                    if (multipartFile.getOriginalFilename().contains(".pdf")) {
                        detalle.setPathPDF("/home/develop/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename());
                        detalle.setNombrePDF(multipartFile.getOriginalFilename());
                    }
                    if (multipartFile.getOriginalFilename().contains(".xml")) {
                        detalle.setPathXMl("/home/develop/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename());
                        detalle.setNombreXMl(multipartFile.getOriginalFilename());
                    }
                }
            }
            ////Subir archivos\\\
            InformeEmpleado informe = managerInforme.obtiene(detalle.getInformeEmpleado().getId());
            detalle.setInformeEmpleado(informe);
            Usuario usuario = ambiente.obtieneUsuario();
            manager.graba(detalle, usuario);
            request.getSession().setAttribute("detalleId", detalle.getId());

        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el detalle", e);
            return Constantes.PATH_INFORMEEMPLEADODETALLE_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "detalle.graba.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{detalle.getNombreProveedor()});

        return "redirect:" + Constantes.PATH_INFORMEEMPLEADODETALLE_LISTA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, HttpServletResponse response, @Valid InformeEmpleadoDetalle detalle,
            BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes,
            @ModelAttribute("uploadForm") FileUploadForm uploadForm) throws Exception {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        utils.despliegaBindingResultErrors(bindingResult);
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession()
                    .getAttribute("empresaId"));

            this.despliegaBindingResultErrors(bindingResult);

            return Constantes.PATH_INFORMEEMPLEADODETALLE_NUEVO;
        }

        try {
            InformeEmpleado informe = managerInforme.obtiene(detalle.getInformeEmpleado().getId());
            detalle.setInformeEmpleado(informe);
            InformeEmpleadoDetalle detalleTmp = manager.obtiene(detalle.getId());
            detalle.setNombrePDF(detalleTmp.getNombrePDF());
            detalle.setNombreXMl(detalleTmp.getNombreXMl());
            detalle.setPathPDF(detalleTmp.getPathPDF());
            detalle.setPathXMl(detalleTmp.getPathXMl());
            Usuario usuario = ambiente.obtieneUsuario();
            log.debug("Paquete {}", detalle);
            manager.actualiza(detalle, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el detalle", e);
            return Constantes.PATH_INFORMEEMPLEADODETALLE_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "detalle.graba.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{detalle.getNombreProveedor()});

        return "redirect:" + Constantes.PATH_INFORMEEMPLEADODETALLE_LISTA;
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar cuenta de detalles{}", id);
        Map<String, Object> params = new HashMap<>();
        params = managerInforme.lista(params);
        List<InformeEmpleado> informes = (List) params.get(Constantes.CONTAINSKEY_INFORMESEMPLEADO);
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESEMPLEADO, informes);
        InformeEmpleadoDetalle detalle = manager.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEEMPLEADODETALLE, detalle);
        return Constantes.PATH_INFORMEEMPLEADODETALLE_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo,
            @ModelAttribute InformeEmpleadoDetalle detalle, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina cuenta de detalles");
        try {
            manager.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "detalle.elimina.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{detalle.getNombreProveedor()});
        } catch (Exception e) {
            log.error("No se pudo eliminar el tipo de paquete " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_INFORMEEMPLEADODETALLE, new String[]{"detalle.no.elimina.message"}, null, null));
            return Constantes.PATH_INFORMEEMPLEADODETALLE_VER;
        }

        return "redirect:" + Constantes.PATH_INFORMEEMPLEADODETALLE_LISTA;
    }

    @RequestMapping(value = "/descargarPdf/{id}", method = RequestMethod.GET)
    public ModelAndView handleRequestPDF(@PathVariable Long id, Model modelo, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
//        Long detalleId = (Long) (request.getSession().getAttribute("detalleId"));
//        InformeEmpleadoDetalle detalle = manager.obtiene(detalleId);
        InformeEmpleadoDetalle detalle = manager.obtiene(id);
        try {
            // Suponemos que es un zip lo que se quiere descargar el usuario.
            // Aqui se hace a piñón fijo, pero podría obtenerse el fichero
            // pedido por el usuario a partir de algún parámetro del request
            // o de la URL con la que nos han llamado.
            String nombreFichero = detalle.getNombrePDF();
            String unPath = detalle.getPathPDF();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreFichero + "\"");

            InputStream is = new FileInputStream(unPath);

            IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();

        } catch (IOException ex) {
            // Sacar log de error.
            throw ex;
        }
        return null;
    }

    @RequestMapping(value = "/descargarXML/{id}", method = RequestMethod.GET)
    public ModelAndView handleRequestXML(@PathVariable Long id, Model modelo, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InformeEmpleadoDetalle detalle = manager.obtiene(id);
        try {
            // Suponemos que es un zip lo que se quiere descargar el usuario.
            // Aqui se hace a piñón fijo, pero podría obtenerse el fichero
            // pedido por el usuario a partir de algún parámetro del request
            // o de la URL con la que nos han llamado.
            String nombreFichero = detalle.getNombreXMl();
            String unPath = detalle.getPathXMl();

            response.setContentType("application/xml");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreFichero + "\"");

            InputStream is = new FileInputStream(unPath);

            IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();

        } catch (IOException ex) {
            // Sacar log de error.
            throw ex;
        }
        return null;
    }

    private void generaReporte(String tipo, List<InformeEmpleadoDetalle> detalle,
            HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(detalle);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=InformeDetalles.pdf");
                break;
            case "CSV":
                archivo = generaCsv(detalle);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=InformeDetalles.csv");
                break;
            case "XLS":
                archivo = generaXls(detalle);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=InformeDetalles.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }
    }

    private void enviaCorreo(String tipo, List<InformeEmpleadoDetalle> detalle, HttpServletRequest request)
            throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(detalle);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(detalle);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(detalle);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("detalle.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List detalles) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/detalles.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(detalles));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List detalles) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/detalles.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(detalles));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List detalles) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/detalles.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(detalles));
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
