/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorManager;
import mx.edu.um.mateo.contabilidad.facturas.service.ProveedorFacturasManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.AutorizacionCCPlInvalidoException;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.FaltaArchivoPDFException;
import mx.edu.um.mateo.general.utils.FaltaArchivoXMLException;
import mx.edu.um.mateo.general.utils.FormaPagoNoSeleccionadaException;
import mx.edu.um.mateo.general.utils.MonedaNoSeleccionadaException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.rh.model.Empleado;
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
        params.put(Constantes.CONTAINSKEY_REPORTE, Constantes.CONTAINSKEY_REPORTE);
        Usuario prueba = ambiente.obtieneUsuario();
        log.debug("usuarioPrueba{}", prueba);
        log.debug("usuario{}", usuario);
        if (prueba.isProveedor()) {
            ProveedorFacturas proveedorFacturas = (ProveedorFacturas) ambiente.obtieneUsuario();
            params.put("proveedorFacturas", proveedorFacturas.getId());
            log.debug("entrado como proveedor");
        }
        if (prueba.isEmpleado()) {
            Empleado empleado = (Empleado) ambiente.obtieneUsuario();
            params.put("empleado", empleado.getId());
            log.debug("entrado como empleado");
        }

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
            if (prueba.isProveedor()) {
                params = manager.lista(params);
                log.debug("entrado como proveedor");
            }
            if (prueba.isEmpleado()) {
                params = manager.listaEmpleado(params);
                log.debug("entrado como empleado");
            }
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
            if (prueba.isEmpleado()) {
                params = manager.listaEmpleado(params);
            }
            if (prueba.isProveedor()) {
                params = manager.lista(params);
                log.debug("entrado como proveedor");
            }

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<InformeProveedor>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("informeProveedor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        if (prueba.isProveedor()) {
            params = manager.lista(params);
            log.debug("entrado como proveedor");
        }
        if (prueba.isEmpleado()) {
            params = manager.listaEmpleado(params);
            log.debug("entrado como emepleado");
        }

        log.debug("params{}", params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR));
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESPROVEEDOR, params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR));

        pagina(params, modelo, Constantes.CONTAINSKEY_INFORMESPROVEEDOR, pagina);

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
        params.put(Constantes.CONTAINSKEY_REPORTE, Constantes.CONTAINSKEY_REPORTE);
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
            params = manager.revisar(params);
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
            params = manager.revisar(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<InformeProveedor>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("informeProveedor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = manager.revisar(params);
        log.debug("params{}", params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR));
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESPROVEEDOR, params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR));

        pagina(params, modelo, Constantes.CONTAINSKEY_INFORMESPROVEEDOR, pagina);
        return "/factura/revisaProveedor/encabezados";
    }

    @RequestMapping("/ver/{id}")
    public String ver(HttpServletRequest request, @PathVariable Long id, Model modelo) {
        log.debug("Mostrando informe {}", id);
        Usuario usuario = ambiente.obtieneUsuario();
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
        Usuario usuario = ambiente.obtieneUsuario();
        InformeProveedor informe = new InformeProveedor();

        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informe);
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession()
                .getAttribute("empresaId"));
        params.put("reporte", true);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, informe);
        if (usuario.isProveedor()) {
            ProveedorFacturas proveedorFacturas = (ProveedorFacturas) ambiente.obtieneUsuario();
            informe.setClabe(proveedorFacturas.getClabe());
            informe.setCuentaCheque(proveedorFacturas.getCuentaCheque());
            informe.setBanco(proveedorFacturas.getBanco());
            return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;
        }
        if (usuario.isEmpleado()) {
            return Constantes.PATH_INFORMEPROVEEDOR_NUEVOEMP;
        }
        return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;

    }

    @Transactional
    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid InformeProveedor informe,
            BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes, Locale locale) throws Exception {
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
        Map<String, Object> params = new HashMap<>();
        Usuario usuario = ambiente.obtieneUsuario();
        if (usuario.isProveedor()) {
            ProveedorFacturas proveedorFacturas = (ProveedorFacturas) ambiente.obtieneUsuario();
            informe.setNombreProveedor(usuario.getNombre());
            informe.setProveedorFacturas(proveedorFacturas);

            String formaPago = informe.getFormaPago();
//            ProveedorFacturas proveedorFacturas1 = pFacturasManager.obtiene(proveedorFacturas.getId());
            log.debug(formaPago);
            log.debug(informe.getBanco());
            log.debug(informe.getClabe());
            log.debug(informe.getCuentaCheque());

            if (informe.getBanco() != null && informe.getBanco() != proveedorFacturas.getBanco() && !informe.getBanco().isEmpty()) {
                log.debug("entrando banco");
                proveedorFacturas.setBanco(informe.getBanco());
//                pFacturasManager.actualiza(proveedorFacturas1, proveedorFacturas);
            } else if (informe.getBanco() == null || informe.getBanco().isEmpty()) {

                return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;
            }
//            switch (formaPago) {
//                case "T":
            log.debug("entrando clabe");
            if (informe.getClabe() != null && informe.getClabe() != proveedorFacturas.getClabe() && !informe.getClabe().isEmpty()) {
                proveedorFacturas.setClabe(informe.getClabe());
//                        pFacturasManager.actualiza(proveedorFacturas1, proveedorFacturas);
            }
//                case "C":
            log.debug("entrando cheque");
            if (informe.getCuentaCheque() != null && informe.getCuentaCheque() != proveedorFacturas.getCuentaCheque() && !informe.getCuentaCheque().isEmpty()) {
                proveedorFacturas.setCuentaCheque(informe.getCuentaCheque());
//                        pFacturasManager.actualiza(proveedorFacturas1, proveedorFacturas);
            }
//                    break;
//            }

            pFacturasManager.actualiza(proveedorFacturas, usuario);
            ambiente.actualizaSesion(request.getSession(), proveedorFacturas);
        }
        if (usuario.isEmpleado()) {
            Empleado empleado = (Empleado) usuario;
            informe.setNombreProveedor(usuario.getNombre());
            informe.setEmpleado(empleado);
        }
        informe.setStatus(Constantes.STATUS_ACTIVO);

        try {

            manager.graba(informe, usuario);
        } catch (AutorizacionCCPlInvalidoException e) {
            log.error("No se pudo crear el detalle", e);
            if (e != null) {
                log.debug("**Enviando mensajes....CCP no encontrado");
            }
        } catch (MonedaNoSeleccionadaException ex) {
            log.debug("Moneda no seleccionada");
//            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeproveedor.noSeleccionoMoneda");
//            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{"Monda"});
            errors.rejectValue("moneda", "informeproveedor.noSeleccionoMoneda",
                    new String[]{"moneda"}, null);
            return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;
        } catch (FormaPagoNoSeleccionadaException ex) {
            log.debug("Forma de pago no seleccionada");
//            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeproveedor.noSeleccionoFormaPago");
//            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS);
            errors.rejectValue("formaPago", "informeproveedor.noSeleccionoFormaPago",
                    new String[]{"formaPago"}, null);
            return Constantes.PATH_INFORMEPROVEEDOR_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeproveedor.formadepago");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{informe.getFormaPagoTexto(), informe.getCuentaCheque(), informe.getBanco(), informe.getMonedaTexto()});
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
    public String finaliza(HttpServletRequest request, @RequestParam Long id, Model modelo,
            @ModelAttribute InformeProveedor informeProveedor, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) throws FaltaArchivoXMLException, FaltaArchivoXMLException {
        log.debug("Finalizando informe");
        try {
            Usuario usuario = ambiente.obtieneUsuario();
            InformeProveedor informe = manager.obtiene(id);
            log.debug("informe...**controller{}", informe);
            manager.finaliza(informe, usuario);
        } catch (FaltaArchivoPDFException e) {
            log.error("No se pudo finalizar informe " + id, e);
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "faltaArchivoXML.noFinaliza");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{e.getMessage()});
//            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, new String[]{"informeProveedor.no.finaliza.message"}, null, null));
            return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_LISTA;
        } catch (FaltaArchivoXMLException e) {
            log.error("No se pudo finalizar informe " + id, e);
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "faltaArchivoPDF.noFinaliza");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{e.getMessage()});
//            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR, new String[]{"informeProveedor.no.finaliza.message"}, null, null));
            return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_LISTA;
        }

        return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_LISTA;
    }

    @Transactional
    @RequestMapping(value = "/autorizar", method = RequestMethod.POST)
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
    @RequestMapping(value = "/rechazar", method = RequestMethod.POST)
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
