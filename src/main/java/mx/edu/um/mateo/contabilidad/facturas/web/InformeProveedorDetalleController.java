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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.contabilidad.facturas.model.Contrarecibo;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.contabilidad.facturas.service.ContrareciboManager;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorDetalleManager;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorManager;
import mx.edu.um.mateo.general.dao.ProveedorDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.BancoNoCoincideException;
import mx.edu.um.mateo.general.utils.ClabeNoCoincideException;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.CuentaChequeNoCoincideException;
import mx.edu.um.mateo.general.utils.FormaPagoNoCoincideException;
import mx.edu.um.mateo.general.utils.ProveedorNoCoincideException;
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
@RequestMapping(Constantes.PATH_INFORMEPROVEEDOR_DETALLE)
public class InformeProveedorDetalleController extends BaseController {

    @Autowired
    private InformeProveedorDetalleManager manager;
    @Autowired
    private InformeProveedorManager managerInforme;
    @Autowired
    private ProveedorDao proveedorDao;
    @Autowired
    private ContrareciboManager contrareciboManager;

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
        InformeProveedor informeId = (InformeProveedor) request.getSession().getAttribute("informeId");
        params.put("informeProveedor", informeId.getId());
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
                generaReporte(tipo, (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE), response);
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
                enviaCorreo(correo, (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("detalle.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = manager.lista(params);
        log.debug("params{}", params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE));
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE, params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<InformeProveedorDetalle> detalles = (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE);
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

        return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTA;
    }

    @RequestMapping("/listaContrarecibo")
    public String listaContrarecibos(HttpServletRequest request, HttpServletResponse response,
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
        Contrarecibo contrareciboId = (Contrarecibo) request.getSession().getAttribute("contrareciboId");
        params.put("contrarecibo", contrareciboId.getId());

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
            params = manager.contrarecibo(params);
            try {
                generaReporte(tipo, (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = manager.contrarecibo(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("detalle.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = manager.contrarecibo(params);
        log.debug("params{}", params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE));
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE, params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<InformeProveedorDetalle> detalles = (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE);
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

        return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTACONTRARECIBOS;
    }

    @RequestMapping("/contrarecibos")
    public String contrarecibos(HttpServletRequest request, HttpServletResponse response,
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
            params = contrareciboManager.lista(params);
            try {
                generaReporte(tipo, (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_CONTRARECIBOS), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = contrareciboManager.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_CONTRARECIBOS), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("detalle.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = contrareciboManager.lista(params);
        log.debug("params{}", params.get(Constantes.CONTAINSKEY_CONTRARECIBOS));
        modelo.addAttribute(Constantes.CONTAINSKEY_CONTRARECIBOS, params.get(Constantes.CONTAINSKEY_CONTRARECIBOS));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<Contrarecibo> contrarecibos = (List<Contrarecibo>) params.get(Constantes.CONTAINSKEY_CONTRARECIBOS);
        Long primero = ((pagina - 1) * max) + 1;
        log.debug("primero {}", primero);
        log.debug("detalles {}", contrarecibos.size());
        Long ultimo = primero + (contrarecibos.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINACION, paginacion);
        log.debug("Paginacion{}", paginacion);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINAS, paginas);
        log.debug("paginas{}", paginas);
        modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        log.debug("Pagina{}", pagina);
        // termina paginado

        return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_CONTRARECIBOS;
    }

    @RequestMapping({"/contrarecibo"})
    public String contrarecibo(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Usuario usuario,
            Errors errors,
            Model modelo) {
        log.debug("Entrando a contrarecibo..**..");
        Map<String, Object> params = new HashMap<>();
        Long empresaId = (Long) request.getSession().getAttribute("empresaId");
        params.put("empresa", empresaId);
        InformeProveedor informeId = (InformeProveedor) request.getSession().getAttribute("informeId");
        params.put("informeProveedor", informeId.getId());
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
                generaReporte(tipo, (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE), response);
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
                enviaCorreo(correo, (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("detalle.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = manager.lista(params);
        log.debug("params{}", params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE));
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE, params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<InformeProveedorDetalle> detalles = (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE);
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

        return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_CONTRARECIBO;
    }

    @RequestMapping("/revisar")
    public String revisar(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Usuario usuario,
            Errors errors,
            Model modelo) {
        log.debug("Entrando a contrarecibo..**..");
        Map<String, Object> params = new HashMap<>();
        Long empresaId = (Long) request.getSession().getAttribute("empresaId");
        params.put("empresa", empresaId);
//        InformeProveedor informeId = (InformeProveedor) request.getSession().getAttribute("informeId");
//        params.put("informeProveedor", informeId.getId());
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
                generaReporte(tipo, (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE), response);
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
                enviaCorreo(correo, (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE), request);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("detalle.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = manager.revisar(params);
        log.debug("params{}", params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE));
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE, params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE));

        // inicia paginado
        Long cantidad = (Long) params.get(Constantes.CONTAINSKEY_CANTIDAD);
        Integer max = (Integer) params.get(Constantes.CONTAINSKEY_MAX);
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<InformeProveedorDetalle> detalles = (List<InformeProveedorDetalle>) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR_DETALLE);
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

        return "/factura/revisaProveedor/detalles";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando paquete {}", id);
        InformeProveedorDetalle detalle = manager.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR_DETALLE, detalle);

        return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_VER;
    }

    @RequestMapping("/verContrarecibo/{id}")
    public String verContrarecibo(HttpServletRequest request, @PathVariable Long id, Model modelo) {
        log.debug("Mostrando paquete {}", id);
        Contrarecibo contrarecibo = contrareciboManager.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_CONTRARECIBO, contrarecibo);
        request.getSession().setAttribute("contrareciboId", contrarecibo);

        return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTACONTRARECIBOS;
    }

    @RequestMapping("/nuevo")
    public String nueva(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo paquete");
        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) request.getSession().getAttribute("proveedorFacturas");
        modelo.addAttribute("proveedorFacturas", proveedorFacturas);
        Map<String, Object> params = new HashMap<>();


        InformeProveedorDetalle detalle = new InformeProveedorDetalle();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR_DETALLE, detalle);
        params.put("empresa", request.getSession()
                .getAttribute("empresaId"));
        params.put("reporte", true);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR_DETALLE, detalle);
        return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_NUEVO;
    }

    @RequestMapping("/nueva")
    public String nuevo(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo paquete");
        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) request.getSession().getAttribute("proveedorFacturas");
        modelo.addAttribute("proveedorFacturas", proveedorFacturas);
        Map<String, Object> params = new HashMap<>();


        InformeProveedorDetalle detalle = new InformeProveedorDetalle();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR_DETALLE, detalle);
        params.put("empresa", request.getSession()
                .getAttribute("empresaId"));
        params.put("reporte", true);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR_DETALLE, detalle);
        return "/factura/informeProveedorDetalle/nueva";
    }

    @Transactional
    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid InformeProveedorDetalle detalle,
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

            return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_NUEVO;
        }


        Map<String, Object> params = new HashMap<>();
        //Subir archivos
        List<MultipartFile> files = uploadForm.getFiles();

        List<String> fileNames = new ArrayList<String>();

        if (null != files && files.size() > 0) {
            for (MultipartFile multipartFile : files) {
                String fileName = multipartFile.getOriginalFilename();
                fileNames.add(fileName);
                String uploadDir = "/home/facturas/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename();
                File dirPath = new File(uploadDir);
                if (!dirPath.exists()) {
                    dirPath.mkdirs();
                }
                multipartFile.transferTo(new File("/home/facturas/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename()));
                if (multipartFile.getOriginalFilename().contains(".pdf")) {
                    detalle.setPathPDF("/home/facturas/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename());
                    detalle.setNombrePDF(multipartFile.getOriginalFilename());
                }
                if (multipartFile.getOriginalFilename().contains(".xml")) {
                    detalle.setPathXMl("/home/facturas/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename());
                    detalle.setNombreXMl(multipartFile.getOriginalFilename());
                }
            }
        }
        ////Subir archivos\\\


        InformeProveedor informe = (InformeProveedor) request.getSession().getAttribute("informeId");
        detalle.setInformeProveedor(informe);
        Usuario usuario = ambiente.obtieneUsuario();

        ProveedorFacturas proveedorFacturas = (ProveedorFacturas) ambiente.obtieneUsuario();
        detalle.setNombreProveedor(proveedorFacturas.getNombre());
        detalle.setRFCProveedor(proveedorFacturas.getRfc());
        try {
            manager.graba(detalle, usuario);
            request.getSession().setAttribute("detalleId", detalle.getId());

        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el detalle", e);
            return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "detalle.graba.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{detalle.getNombreProveedor()});

        return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, HttpServletResponse response, @Valid InformeProveedorDetalle detalle,
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

            return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_NUEVO;
        }

        try {
            InformeProveedor informe = (InformeProveedor) request.getSession().getAttribute("informeId");
            detalle.setInformeProveedor(informe);
            InformeProveedorDetalle detalleTmp = manager.obtiene(detalle.getId());
            detalle.setNombrePDF(detalleTmp.getNombrePDF());
            detalle.setNombreXMl(detalleTmp.getNombreXMl());
            detalle.setPathPDF(detalleTmp.getPathPDF());
            detalle.setPathXMl(detalleTmp.getPathXMl());
            Usuario usuario = ambiente.obtieneUsuario();
            log.debug("Paquete {}", detalle);
            manager.actualiza(detalle, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el detalle", e);
            return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "detalle.graba.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{detalle.getNombreProveedor()});

        return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTA;
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar cuenta de detalles{}", id);
        Map<String, Object> params = new HashMap<>();
        params = managerInforme.lista(params);
        List<InformeProveedor> informes = (List) params.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR);
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMESPROVEEDOR, informes);
        InformeProveedorDetalle detalle = manager.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR_DETALLE, detalle);
        return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo,
            @ModelAttribute InformeProveedorDetalle detalle, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina cuenta de detalles");
        try {
            manager.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "detalle.elimina.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{detalle.getNombreProveedor()});
        } catch (Exception e) {
            log.error("No se pudo eliminar el tipo de paquete " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_INFORMEPROVEEDOR_DETALLE, new String[]{"detalle.no.elimina.message"}, null, null));
            return Constantes.PATH_INFORMEPROVEEDOR_DETALLE_VER;
        }

        return "redirect:" + Constantes.PATH_INFORMEPROVEEDOR_DETALLE_LISTA;
    }

    @RequestMapping(value = "/descargarPdf/{id}", method = RequestMethod.GET)
    public ModelAndView handleRequestPDF(@PathVariable Long id, Model modelo, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InformeProveedorDetalle detalle = manager.obtiene(id);
        try {
            String nombreFichero = detalle.getNombrePDF();
            String unPath = detalle.getPathPDF();

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreFichero + "\"");

            InputStream is = new FileInputStream(unPath);

            IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();

        } catch (IOException ex) {
            throw ex;
        }
        return null;
    }

    @RequestMapping(value = "/descargarXML/{id}", method = RequestMethod.GET)
    public ModelAndView handleRequestXML(@PathVariable Long id, Model modelo, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        InformeProveedorDetalle detalle = manager.obtiene(id);
        try {
            String nombreFichero = detalle.getNombreXMl();
            String unPath = detalle.getPathXMl();

            response.setContentType("application/xml");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreFichero + "\"");

            InputStream is = new FileInputStream(unPath);

            IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();

        } catch (IOException ex) {
            throw ex;
        }
        return null;
    }

    @Transactional
    @RequestMapping(value = "/autorizar", method = RequestMethod.GET)
    public String autorizar(HttpServletRequest request, Model modelo, RedirectAttributes redirectAttributes) throws Exception {
        log.debug("Entrando a Autorizar informe");
        String checks = request.getParameter("checkFacturasid");

        log.debug("map {}", request.getParameterMap().toString());
        log.debug("names {}", request.getParameterNames().toString());
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.get("key");
        Boolean autorizar = false;
        Boolean rechazar = false;
        ArrayList ids = new ArrayList();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String nombre = parameterNames.nextElement();
            if (nombre.startsWith("botonAut")) {
                autorizar = true;
            }
            if (nombre.startsWith("botonRe")) {
                rechazar = true;
            }

            if (nombre.startsWith("checkFac")) {
                String[] id = nombre.split("-");
                log.debug("id ={}", id[1]);
                ids.add(id[1]);
            }
        }
        Usuario usuario = ambiente.obtieneUsuario();
        if (autorizar) {
            log.debug("enviando al metodo para autorizar");
            try {
                manager.autorizar(ids, usuario);
            } catch (ClabeNoCoincideException e) {
                log.debug("la clabe de la factura con id= {} no coincide", e);
                if (e != null) {
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "clabe.no.coincide");
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{e.getMessage()});
                }
                return "/factura/revisaProveedor/detalles";
            } catch (ProveedorNoCoincideException e) {
                log.debug("el proveedor de la factura con id= {} no coincide", e);
                if (e != null) {
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "proveedor.no.coincide");
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{e.getMessage()});
                }
                return "/factura/revisaProveedor/detalles";
            } catch (BancoNoCoincideException e) {
                log.debug("el banco de la factura con id= {} no coincide", e);
                if (e != null) {
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "banco.no.coincide");
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{e.getMessage()});
                }
                return "/factura/revisaProveedor/detalles";
            } catch (CuentaChequeNoCoincideException e) {
                log.debug("la cuenta de la factura con id= {} no coincide", e);
                if (e != null) {
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "cuenta.no.coincide");
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{e.getMessage()});
                }
                return "/factura/revisaProveedor/detalles";

            } catch (FormaPagoNoCoincideException e) {
                log.debug("la forma de pago de la factura con id= {} no coincide", e);
                if (e != null) {
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "cuenta.no.coincide");
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{e.getMessage()});
                }
                return "/factura/revisaProveedor/detalles";
            }
        }
        if (rechazar) {
            log.debug("enviando al metodo para rechazar");
            try {
                manager.rechazar(ids);
            } catch (ProveedorNoCoincideException e) {
                log.debug("el banco de la factura con id= {} no coincide", e);
                if (e != null) {
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "banco.no.coincide");
                    redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{e.getMessage()});
                }
                return "/factura/revisaProveedor/detalles";
            }
        }

        log.debug("check{}", checks);
//            InformeProveedor informe = manager.obtiene(id);
//            log.debug("informe...**controller{}", informe);
//            manager.autorizar(informe, usuario);
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeProveedor.finaliza.message");
//            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{informeProveedor.getNombreProveedor()});


        return "/factura/revisaProveedor/detalles";
    }

    private void generaReporte(String tipo, List<InformeProveedorDetalle> detalle,
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

    private void enviaCorreo(String tipo, List<InformeProveedorDetalle> detalle, HttpServletRequest request)
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
