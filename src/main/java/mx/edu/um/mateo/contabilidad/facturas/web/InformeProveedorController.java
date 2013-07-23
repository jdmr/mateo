/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web;

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
import mx.edu.um.mateo.contabilidad.facturas.dao.ProveedorFacturasDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorManager;
import mx.edu.um.mateo.contabilidad.facturas.service.ProveedorFacturasManager;
import mx.edu.um.mateo.general.model.Proveedor;
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
@RequestMapping(Constantes.PATH_INFORMEPROVEEDOR)
public class InformeProveedorController extends BaseController {

    @Autowired
    private InformeProveedorManager manager;
    @Autowired
    private ProveedorFacturasManager pFacturasManager;

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
        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) ambiente.obtieneUsuario();
        params.put("proveedorFacturas", proveedorFacturas.getId());
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
                generaReporte(tipo, (List<InformeProveedor>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR), response);
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
                enviaCorreo(correo, (List<InformeProveedor>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("informeProveedor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = manager.lista(params);
        log.debug("params{}", params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR));
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESPROVEEDOR, params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<InformeProveedor> informes = (List<InformeProveedor>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR);
        Long primero = ((pagina - 1) * max) + 1;
        log.debug("primero {}", primero);
        log.debug("informes {}", informes.size());
        Long ultimo = primero + (informes.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        log.debug("Paginacion{}", paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        log.debug("paginas{}", paginas);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        log.debug("Pagina{}", pagina);
        // termina paginado

        return Constantes.PATH_INFORMEPROVEEDOR_LISTA;
    }

    @RequestMapping("/encabezados")
    public String revisa(HttpServletRequest request, HttpServletResponse response,
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
                generaReporte(tipo, (List<InformeProveedor>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR), response);
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
                enviaCorreo(correo, (List<InformeProveedor>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("informeProveedor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = manager.lista(params);
        log.debug("params{}", params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR));
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESPROVEEDOR, params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<InformeProveedor> informes = (List<InformeProveedor>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR);
        Long primero = ((pagina - 1) * max) + 1;
        log.debug("primero {}", primero);
        log.debug("informes {}", informes.size());
        Long ultimo = primero + (informes.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        log.debug("Paginacion{}", paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        log.debug("paginas{}", paginas);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        log.debug("Pagina{}", pagina);
        // termina paginado
        return "/factura/revisaProveedor/encabezados";
    }

    @RequestMapping("/ver/{id}")
    public String ver(HttpServletRequest request, @PathVariable Long id, Model modelo) {
        log.debug("Mostrando informe {}", id);


        InformeProveedor informeProveedor = manager.obtiene(id);
        request.getSession().setAttribute("informeId", informeProveedor);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informeProveedor);
        if ("a".equals(informeProveedor.getStatus().trim()) || "A".equals(informeProveedor.getStatus().trim())) {
            return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTA;
        }
        return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_DETALLE_CONTRARECIBO;
    }

    @RequestMapping("/revisar/{id}")
    public String revisar(HttpServletRequest request, @PathVariable Long id, Model modelo) {
        log.debug("Mostrando informe {}", id);

        InformeProveedor informeProveedor = manager.obtiene(id);
        request.getSession().setAttribute("informeId", informeProveedor);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informeProveedor);
        return "redirect:/factura/informeProveedorDetalle/revisar";
    }

    @RequestMapping("/nuevo")
    public String nueva(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo informe");
        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) ambiente.obtieneUsuario();
        InformeProveedor informe = new InformeProveedor();
        informe.setClabe(proveedorFacturas.getClabe());
        informe.setCuentaCheque(proveedorFacturas.getCuentaCheque());
        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informe);
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession()
                .getAttribute("empresaId"));
        params.put("reporte", true);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informe);
        return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;
    }

    @Transactional
    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid InformeProveedor informe,
            BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession()
                    .getAttribute("empresaId"));

            this.despliegaBindingResultErrors(bindingResult);

            return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;
        }
        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) ambiente.obtieneUsuario();
        String formaPago = informe.getFormaPago();
        ProveedorFacturas proveedorFacturas1 = pFacturasManager.obtiene(proveedorFacturas.getId());
        switch (formaPago) {
            case "T":
                if (informe.getClabe() != null && informe.getClabe() != proveedorFacturas.getClabe() && !informe.getClabe().isEmpty()) {
                    proveedorFacturas1.setClabe(informe.getClabe());
                    pFacturasManager.actualiza(proveedorFacturas1, proveedorFacturas);
                } else if (informe.getClabe() == null || informe.getClabe().isEmpty()) {

                    return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;
                }
            case "C":
                if (informe.getCuentaCheque() != null && informe.getCuentaCheque() != proveedorFacturas.getCuentaCheque() && !informe.getCuentaCheque().isEmpty()) {
                    proveedorFacturas1.setCuentaCheque(informe.getCuentaCheque());
                    pFacturasManager.actualiza(proveedorFacturas1, proveedorFacturas);
                } else if (informe.getCuentaCheque() == null || informe.getCuentaCheque().isEmpty()) {

                    return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;
                }
        }



        try {

            Usuario usuario = ambiente.obtieneUsuario();
            informe.setNombreProveedor(usuario.getNombre());
            informe.setProveedorFacturas(proveedorFacturas);
            manager.graba(informe, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el tipo de Beca", e);
            return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeProveedor.graba.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{informe.getNombreProveedor()});

        return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_LISTA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, HttpServletResponse response, @Valid InformeProveedor informe, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession()
                    .getAttribute("empresaId"));

            this.despliegaBindingResultErrors(bindingResult);

            return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            log.debug("Informe {}", informe);
            manager.actualiza(informe, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el tipo de Beca", e);
            return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeProveedor.graba.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{informe.getNombreProveedor()});

        return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_LISTA;
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar cuenta de tipos de becas {}", id);
        InformeProveedor informeProveedor = manager.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informeProveedor);
        return Constantes.PATH_INFORMEPROVEEDOR_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute InformeProveedor informeProveedor, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina cuenta de tipos de becas");
        try {
            manager.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeProveedor.elimina.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{informeProveedor.getNombreProveedor()});
        } catch (Exception e) {
            log.error("No se pudo eliminar el tipo de informe " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, new String[]{"informeProveedor.no.elimina.message"}, null, null));
            return Constantes.PATH_INFORMEPROVEEDOR_VER;
        }

        return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_LISTA;
    }

    @Transactional
    @RequestMapping(value = "/finaliza", method = RequestMethod.GET)
    public String finaliza(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute InformeProveedor informeProveedor, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Finalizando informe");
        try {
            Usuario usuario = ambiente.obtieneUsuario();
            InformeProveedor informe = manager.obtiene(id);
            log.debug("informe...**controller{}", informe);
            manager.finaliza(informe, usuario);
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeProveedor.finaliza.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{informeProveedor.getNombreProveedor()});
        } catch (Exception e) {
            log.error("No se pudo finalizar informe " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, new String[]{"informeProveedor.no.finaliza.message"}, null, null));
            return Constantes.PATH_INFORMEPROVEEDOR_VER;
        }

        return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_LISTA;
    }

    @Transactional
    @RequestMapping(value = "/autorizar", method = RequestMethod.GET)
    public String autorizar(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute InformeProveedor informeProveedor, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Finalizando informe");
        try {
            Usuario usuario = ambiente.obtieneUsuario();
            InformeProveedor informe = manager.obtiene(id);
            log.debug("informe...**controller{}", informe);
            manager.autorizar(informe, usuario);
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeProveedor.finaliza.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{informeProveedor.getNombreProveedor()});
        } catch (Exception e) {
            log.error("No se pudo finalizar informe " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, new String[]{"informeProveedor.no.finaliza.message"}, null, null));
            return Constantes.PATH_INFORMEPROVEEDOR_VER;
        }

        return "redirect:/factura/informeProveedor/encabezados";
    }

    @Transactional
    @RequestMapping(value = "/rechazar", method = RequestMethod.GET)
    public String rechazar(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute InformeProveedor informeProveedor, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Finalizando informe");
        try {
            Usuario usuario = ambiente.obtieneUsuario();
            InformeProveedor informe = manager.obtiene(id);
            log.debug("informe...**controller{}", informe);
            manager.rechazar(informe, usuario);
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeProveedor.finaliza.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{informeProveedor.getNombreProveedor()});
        } catch (Exception e) {
            log.error("No se pudo finalizar informe " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, new String[]{"informeProveedor.no.finaliza.message"}, null, null));
            return Constantes.PATH_INFORMEPROVEEDOR_VER;
        }

        return "redirect:/factura/informeProveedor/encabezados";
    }

    private void generaReporte(String tipo, List<InformeProveedor> informe, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(informe);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=Informes.pdf");
                break;
            case "CSV":
                archivo = generaCsv(informe);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=Informes.csv");
                break;
            case "XLS":
                archivo = generaXls(informe);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=Informes.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<InformeProveedor> informes, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(informes);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(informes);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(informes);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("informe.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List informes) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/informesProveedor.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(informes));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List informes) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/informesProveedor.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(informes));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List informes) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/informesProveedor.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(informes));
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
