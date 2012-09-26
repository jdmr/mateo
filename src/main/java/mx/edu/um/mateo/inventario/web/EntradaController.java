/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
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
package mx.edu.um.mateo.inventario.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.dao.ProveedorDao;
import mx.edu.um.mateo.general.model.Proveedor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.LabelValueBean;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inventario.dao.EntradaDao;
import mx.edu.um.mateo.inventario.dao.ProductoDao;
import mx.edu.um.mateo.inventario.model.Cancelacion;
import mx.edu.um.mateo.inventario.model.Entrada;
import mx.edu.um.mateo.inventario.model.LoteEntrada;
import mx.edu.um.mateo.inventario.model.Producto;
import mx.edu.um.mateo.inventario.utils.NoCuadraException;
import mx.edu.um.mateo.inventario.utils.NoEstaAbiertaException;
import mx.edu.um.mateo.inventario.utils.NoEstaCerradaException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarEnCeroException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarException;
import mx.edu.um.mateo.inventario.utils.ProductoNoSoportaFraccionException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/inventario/entrada")
public class EntradaController extends BaseController {

    @Autowired
    private EntradaDao entradaDao;
    @Autowired
    private ProveedorDao proveedorDao;
    @Autowired
    private ProductoDao productoDao;

    @SuppressWarnings("unchecked")
    @RequestMapping
    public String lista(HttpServletRequest request,
            HttpServletResponse response, Usuario usuario, Errors errors,
            Model modelo) throws ParseException {
        log.debug("Mostrando lista de tipos de entradas");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Map<String, Object> params = this.convierteParams(request
                .getParameterMap());
        Long almacenId = (Long) request.getSession().getAttribute("almacenId");
        params.put("almacen", almacenId);

        if (params.containsKey("fechaIniciado")) {
            log.debug("FechaIniciado: {}", params.get("fechaIniciado"));
            params.put("fechaIniciado",
                    sdf.parse((String) params.get("fechaIniciado")));
        }

        if (params.containsKey("fechaTerminado")) {
            params.put("fechaTerminado",
                    sdf.parse((String) params.get("fechaTerminado")));
        }

        if (params.containsKey("tipo")
                && StringUtils.isNotBlank((String) params.get("tipo"))) {
            params.put("reporte", true);
            params = entradaDao.lista(params);
            try {
                generaReporte((String) params.get("tipo"),
                        (List<Entrada>) params.get("entradas"), response,
                        "entradas", Constantes.ALM, almacenId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove("reporte");
                errors.reject("error.generar.reporte");
            }
        }

        if (params.containsKey("correo")
                && StringUtils.isNotBlank((String) params.get("correo"))) {
            params.put("reporte", true);
            params = entradaDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo((String) params.get("correo"),
                        (List<Entrada>) params.get("entradas"), request,
                        "entradas", Constantes.ALM, almacenId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute(
                        "messageAttrs",
                        new String[]{
                            messageSource.getMessage("entrada.lista.label",
                            null, request.getLocale()),
                            ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = entradaDao.lista(params);
        modelo.addAttribute("entradas", params.get("entradas"));

        Long pagina = 1l;
        if (params.containsKey("pagina")) {
            pagina = (Long) params.get("pagina");
        }
        this.pagina(params, modelo, "entradas", pagina);

        if (params.containsKey(Constantes.ABIERTA)
                || params.containsKey(Constantes.CERRADA)
                || params.containsKey(Constantes.PENDIENTE)
                || params.containsKey(Constantes.FACTURADA)
                || params.containsKey(Constantes.CANCELADA)
                || params.containsKey(Constantes.DEVOLUCION)) {
            modelo.addAttribute("estatus", Boolean.TRUE);
        } else {
            modelo.addAttribute("estatus", Boolean.FALSE);
        }

        return "inventario/entrada/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando entrada {}", id);
        Entrada entrada = entradaDao.obtiene(id);
        switch (entrada.getEstatus().getNombre()) {
            case Constantes.ABIERTA:
                modelo.addAttribute("puedeEditar", true);
                modelo.addAttribute("puedeEliminar", true);
                modelo.addAttribute("puedeCerrar", true);
                modelo.addAttribute("puedePendiente", true);
                break;
            case Constantes.PENDIENTE:
                modelo.addAttribute("puedeEditarPendiente", true);
                break;
            case Constantes.CERRADA:
                modelo.addAttribute("puedeCancelar", true);
                break;
        }

        modelo.addAttribute("entrada", entrada);

        BigDecimal subtotal = new BigDecimal("0").setScale(2,
                RoundingMode.HALF_UP);
        BigDecimal iva = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
        for (LoteEntrada lote : entrada.getLotes()) {
            subtotal = subtotal.add(lote.getPrecioUnitario().multiply(
                    lote.getCantidad()));
            iva = iva.add(lote.getIva());
        }
        BigDecimal total = subtotal.add(iva);
        modelo.addAttribute("subtotal",
                subtotal.setScale(2, RoundingMode.HALF_UP));
        modelo.addAttribute("iva", iva);
        modelo.addAttribute("total", total.setScale(2, RoundingMode.HALF_UP));
        if (iva.compareTo(entrada.getIva()) == 0
                && total.compareTo(entrada.getTotal()) == 0) {
            modelo.addAttribute("estiloTotales", "label label-success");
        } else {
            BigDecimal variacion = new BigDecimal("0.05");
            BigDecimal topeIva = entrada.getIva().multiply(variacion);
            BigDecimal topeTotal = entrada.getTotal().multiply(variacion);
            log.debug("Estilos {} {} {} {} {} {}",
                    new Object[]{iva, entrada.getIva(), topeIva, total,
                        entrada.getTotal(), topeTotal});
            if (iva.compareTo(entrada.getIva()) < 0
                    || total.compareTo(entrada.getTotal()) < 0) {
                log.debug("La diferencia es menor");
                if (iva.compareTo(entrada.getIva().subtract(topeIva)) >= 0
                        && total.compareTo(entrada.getTotal().subtract(
                        topeTotal)) >= 0) {
                    modelo.addAttribute("estiloTotales", "label label-warning");
                } else {
                    modelo.addAttribute("estiloTotales",
                            "label label-important");
                }
            } else {
                log.debug(
                        "La diferencia es mayor {} {}",
                        new Object[]{
                            iva.compareTo(entrada.getIva().add(topeIva)),
                            total.compareTo(entrada.getTotal().add(
                            topeTotal))});
                if (iva.compareTo(entrada.getIva().add(topeIva)) <= 0
                        && total.compareTo(entrada.getTotal().add(topeTotal)) <= 0) {
                    log.debug("estilo warning");
                    modelo.addAttribute("estiloTotales", "label label-warning");
                } else {
                    log.debug("estilo error");
                    modelo.addAttribute("estiloTotales",
                            "label label-important");
                }
            }
        }

        return "inventario/entrada/ver";
    }

    @RequestMapping("/nueva")
    public String nueva(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo entrada");
        Entrada entrada = new Entrada();
        modelo.addAttribute("entrada", entrada);

        return "inventario/entrada/nueva";
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request,
            HttpServletResponse response, @Valid Entrada entrada,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return "inventario/entrada/nueva";
        }

        if (entrada.getProveedor() == null
                || entrada.getProveedor().getId() == null) {
            log.warn("No introdujo un proveedor correcto, regresando");
            errors.rejectValue("proveedor",
                    "entrada.no.eligio.proveedor.message", null, null);
            return "inventario/entrada/nueva";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (request.getParameter("proveedor.id") == null) {
                log.warn("No se puede crear la entrada si no ha seleccionado un proveedor");
                errors.rejectValue("proveedor", "entrada.sin.proveedor.message");
                return "inventario/entrada/nueva";
            }
            Proveedor proveedor = proveedorDao.obtiene(new Long(request
                    .getParameter("proveedor.id")));
            entrada.setProveedor(proveedor);
            entrada = entradaDao.crea(entrada, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la entrada", e);
            errors.rejectValue("factura", "campo.duplicado.message",
                    new String[]{"factura"}, null);

            return "inventario/entrada/nueva";
        }

        redirectAttributes.addFlashAttribute("message",
                "entrada.creada.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{entrada.getFolio()});

        return "redirect:/inventario/entrada/ver/" + entrada.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable Long id,
            Model modelo) {
        log.debug("Edita entrada {}", id);
        Entrada entrada = entradaDao.obtiene(id);
        modelo.addAttribute("entrada", entrada);

        return "inventario/entrada/edita";
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Entrada entrada,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "inventario/entrada/edita";
        }

        if (entrada.getProveedor() == null
                || entrada.getProveedor().getId() == null) {
            log.warn("No introdujo un proveedor correcto, regresando");
            errors.rejectValue("proveedor",
                    "entrada.no.eligio.proveedor.message", null, null);
            return "inventario/entrada/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (request.getParameter("proveedor.id") == null) {
                log.warn("No se puede crear la entrada si no ha seleccionado un proveedor");
                errors.rejectValue("proveedor", "entrada.sin.proveedor.message");
                return "inventario/entrada/nueva";
            }
            Proveedor proveedor = proveedorDao.obtiene(new Long(request
                    .getParameter("proveedor.id")));
            entrada.setProveedor(proveedor);
            entrada = entradaDao.actualiza(entrada, usuario);
        } catch (NoEstaAbiertaException e) {
            log.error("No se pudo actualizar la entrada", e);
            modelo.addAttribute("message",
                    "entrada.intento.modificar.cerrada.message");
            modelo.addAttribute("messageStyle", "alert-error");
            modelo.addAttribute("messageAttrs",
                    new String[]{entrada.getFolio()});
            return "inventario/entrada/nueva";
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la entrada", e);
            errors.rejectValue("factura", "campo.duplicado.message",
                    new String[]{"factura"}, null);

            return "inventario/entrada/nueva";
        }

        redirectAttributes.addFlashAttribute("message",
                "entrada.actualizada.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{entrada.getFolio()});

        return "redirect:/inventario/entrada/ver/" + entrada.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id,
            Model modelo, @ModelAttribute Entrada entrada,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina entrada");
        try {
            String nombre = entradaDao.elimina(id, ambiente.obtieneUsuario());

            redirectAttributes.addFlashAttribute("message",
                    "entrada.eliminada.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la entrada " + id, e);
            bindingResult
                    .addError(new ObjectError("entrada",
                    new String[]{"entrada.no.eliminada.message"},
                    null, null));
            return "inventario/entrada/ver";
        }

        return "redirect:/inventario/entrada";
    }

    @RequestMapping("/cerrar/{id}")
    public String cerrar(HttpServletRequest request, @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        log.debug("Cierra entrada {}", id);
        try {
            String folio = entradaDao.cierra(id, ambiente.obtieneUsuario());
            redirectAttributes.addFlashAttribute("message",
                    "entrada.cerrada.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{folio});
        } catch (NoEstaAbiertaException e) {
            log.error("No se pudo cerrar la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.intento.modificar.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{""});
        } catch (NoSePuedeCerrarEnCeroException e) {
            log.error("No se pudo cerrar la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.no.cerrada.en.cero.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
        } catch (NoCuadraException e) {
            log.error("No se pudo cerrar la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.no.cuadra.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
        } catch (NoSePuedeCerrarException e) {
            log.error("No se pudo cerrar la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.no.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
        }

        return "redirect:/inventario/entrada/ver/" + id;
    }

    @RequestMapping(value = "/pendiente/cerrar", method = RequestMethod.POST)
    public String cerrarPendiente(HttpServletRequest request,
            @ModelAttribute Entrada entrada,
            RedirectAttributes redirectAttributes) {
        log.debug("Cierra entrada {}", entrada.getFolio());
        try {
            entrada = entradaDao.cierraPendiente(entrada,
                    ambiente.obtieneUsuario());
            redirectAttributes.addFlashAttribute("message",
                    "entrada.cerrada.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{entrada.getFolio()});
        } catch (NoSePuedeCerrarException e) {
            log.error("No se pudo cerrar la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.no.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
        }

        return "redirect:/inventario/entrada/ver/" + entrada.getId();
    }

    @RequestMapping("/pendiente/{id}")
    public String pendiente(HttpServletRequest request, @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        log.debug("Poniendo pendiente a entrada {}", id);
        try {
            String folio = entradaDao.pendiente(id, ambiente.obtieneUsuario());
            redirectAttributes.addFlashAttribute("message",
                    "entrada.pendiente.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{folio});
        } catch (NoEstaAbiertaException e) {
            log.error("No se pudo cerrar la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.intento.modificar.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{""});
        } catch (NoSePuedeCerrarEnCeroException e) {
            log.error("No se pudo poner en pendiente la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.no.cerrada.en.cero.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
        } catch (NoCuadraException e) {
            log.error("No se pudo poner en pendiente la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.no.cuadra.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
        } catch (NoSePuedeCerrarException e) {
            log.error("No se pudo poner en pendiente la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.no.pendiente.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
        }

        return "redirect:/inventario/entrada/ver/" + id;
    }

    @RequestMapping("/pendiente/edita/{id}")
    public String editaPendiente(HttpServletRequest request,
            @PathVariable Long id, Model modelo,
            RedirectAttributes redirectAttributes) {
        log.debug("Editando entrada pendiente {}", id);
        Entrada entrada = entradaDao.obtiene(id);
        if (entrada.getEstatus().getNombre().equals(Constantes.PENDIENTE)) {
            modelo.addAttribute("entrada", entrada);
            return "inventario/entrada/pendiente";
        } else {
            redirectAttributes.addFlashAttribute("message",
                    "entrada.no.pendiente.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            return "redirect:/inventario/entrada/ver/" + id;
        }

    }

    @RequestMapping("/cancela/{id}")
    public String cancela(@PathVariable Long id, Model modelo,
            RedirectAttributes redirectAttributes) {
        try {
            log.debug("Cancela entrada {}", id);
            Map<String, Object> resultado = entradaDao.preCancelacion(id,
                    ambiente.obtieneUsuario());
            modelo.addAttribute("entrada", resultado.get("entrada"));
            modelo.addAttribute("productos", resultado.get("productos"));
            modelo.addAttribute("entradas", resultado.get("entradas"));
            modelo.addAttribute("salidas", resultado.get("salidas"));
            modelo.addAttribute("productosCancelados",
                    resultado.get("productosCancelados"));
            modelo.addAttribute("productosSinHistoria",
                    resultado.get("productosSinHistoria"));

            return "inventario/entrada/cancela";
        } catch (NoEstaCerradaException e) {
            log.error("No se puede cancelar la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.no.cerrada.para.cancelar.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{e.getEntrada().getFolio()});
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            return "redirect:/inventario/entrada/ver/" + id;
        }
    }

    @RequestMapping(value = "/cancelar", method = RequestMethod.POST)
    public String cancelar(@RequestParam Long id,
            @RequestParam String comentarios, Model modelo,
            RedirectAttributes redirectAttributes) {
        try {
            log.debug("Cancelando entrada {}", id);
            Cancelacion cancelacion = entradaDao.cancelar(id,
                    ambiente.obtieneUsuario(), comentarios);
            modelo.addAttribute("cancelacion", cancelacion);

            modelo.addAttribute("message", "entrada.cancelada.message");
            modelo.addAttribute("messageAttrs", new String[]{cancelacion
                        .getEntrada().getFolio()});
            modelo.addAttribute("messageStyle", "alert-success");
            return "/inventario/entrada/cancelada";
        } catch (NoEstaCerradaException e) {
            log.error("No se puede cancelar la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.no.cerrada.para.cancelar.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{e.getSalida().getFolio()});
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            return "redirect:/inventario/entrada/ver/" + id;
        }
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/proveedores", params = "term", produces = "application/json")
    public @ResponseBody
    List<LabelValueBean> proveedores(HttpServletRequest request,
            @RequestParam("term") String filtro) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession().getAttribute("empresaId"));
        params.put("filtro", filtro);
        params = proveedorDao.lista(params);
        List<LabelValueBean> valores = new ArrayList<>();
        List<Proveedor> proveedores = (List<Proveedor>) params
                .get("proveedores");
        for (Proveedor proveedor : proveedores) {
            StringBuilder sb = new StringBuilder();
            sb.append(proveedor.getNombre());
            sb.append(" | ");
            sb.append(proveedor.getRfc());
            sb.append(" | ");
            sb.append(proveedor.getNombreCompleto());
            valores.add(new LabelValueBean(proveedor.getId(), sb.toString(),
                    proveedor.getNombre()));
        }
        return valores;
    }

    @RequestMapping(value = "/productos", params = "term", produces = "application/json")
    public @ResponseBody
    List<LabelValueBean> productos(HttpServletRequest request,
            @RequestParam("term") String filtro) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("almacen", request.getSession().getAttribute("almacenId"));
        params.put("filtro", filtro);
        params = productoDao.lista(params);
        List<LabelValueBean> valores = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Producto> productos = (List<Producto>) params.get("productos");
        for (Producto producto : productos) {
            StringBuilder sb = new StringBuilder();
            sb.append(producto.getSku());
            sb.append(" | ");
            sb.append(producto.getNombre());
            sb.append(" | ");
            sb.append(producto.getDescripcion());
            sb.append(" | ");
            sb.append(producto.getExistencia()).append(" ")
                    .append(producto.getUnidadMedida());
            sb.append(" | ");
            sb.append(producto.getPrecioUnitario());
            valores.add(new LabelValueBean(producto.getId(), sb.toString()));
        }
        return valores;
    }

    @RequestMapping("/lote/{id}")
    public String nuevoLote(@PathVariable Long id, Model modelo) {
        log.debug("Nuevo lote para entrada {}", id);
        Entrada entrada = entradaDao.carga(id);
        LoteEntrada lote = new LoteEntrada(entrada);

        modelo.addAttribute("lote", lote);

        return "inventario/entrada/lote";
    }

    @RequestMapping(value = "/lote/crea", method = RequestMethod.POST)
    public String creaLote(HttpServletRequest request, @Valid LoteEntrada lote,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "inventario/entrada/lote/"
                    + request.getParameter("entrada.id");
        }

        try {
            if (StringUtils.isBlank(request.getParameter("producto.id"))) {
                log.warn("No se puede crear la entrada si no ha seleccionado un proveedor");
                modelo.addAttribute("lote", lote);
                modelo.addAttribute("message", "lote.sin.producto.message");
                return "inventario/entrada/lote";
            }
            lote = entradaDao.creaLote(lote);
        } catch (NoEstaAbiertaException e) {
            log.error("No se pudo cerrar la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.intento.modificar.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{""});
        } catch (ProductoNoSoportaFraccionException e) {
            log.error(
                    "No se pudo crear la entrada porque no se encontro el producto",
                    e);
            modelo.addAttribute("message", "lote.sin.producto.message");
            modelo.addAttribute("lote", lote);
            return "inventario/entrada/lote";
        }

        redirectAttributes.addFlashAttribute("message", "lote.creado.message");
        redirectAttributes.addFlashAttribute(
                "messageAttrs",
                new String[]{
                    lote.getProducto().getNombre(),
                    lote.getCantidad().toString(),
                    lote.getPrecioUnitario().toString(),
                    lote.getProducto().getUnidadMedida(),
                    lote.getIva()
                    .add(lote.getPrecioUnitario().multiply(
                    lote.getCantidad())).toString()});

        return "redirect:/inventario/entrada/ver/" + lote.getEntrada().getId();
    }

    @RequestMapping("/lote/elimina/{id}")
    public String eliminaLote(@PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        log.debug("Eliminando lote {}", id);
        try {
            id = entradaDao.eliminaLote(id);
            redirectAttributes.addFlashAttribute("message",
                    "lote.eliminado.message");
        } catch (NoEstaAbiertaException e) {
            log.error("No se pudo cerrar la entrada", e);
            redirectAttributes.addFlashAttribute("message",
                    "entrada.intento.modificar.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{""});
        }

        return "redirect:/inventario/entrada/ver/" + id;
    }
}
