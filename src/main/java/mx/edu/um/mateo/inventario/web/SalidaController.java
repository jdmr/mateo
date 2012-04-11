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
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.dao.ClienteDao;
import mx.edu.um.mateo.general.model.Cliente;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.LabelValueBean;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inventario.dao.ProductoDao;
import mx.edu.um.mateo.inventario.dao.SalidaDao;
import mx.edu.um.mateo.inventario.model.Cancelacion;
import mx.edu.um.mateo.inventario.model.LoteSalida;
import mx.edu.um.mateo.inventario.model.Producto;
import mx.edu.um.mateo.inventario.model.Salida;
import mx.edu.um.mateo.inventario.utils.*;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
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
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/inventario/salida")
public class SalidaController extends BaseController {

    @Autowired
    private SalidaDao salidaDao;
    @Autowired
    private ClienteDao clienteDao;
    @Autowired
    private ProductoDao productoDao;

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
        log.debug("Mostrando lista de salidas");
        Map<String, Object> params = new HashMap<>();
        Long almacenId = (Long) request.getSession().getAttribute("almacenId");
        params.put("almacen", almacenId);
        if (StringUtils.isNotBlank(filtro)) {
            params.put("filtro", filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put("order", order);
            params.put("sort", sort);
        }
        if (pagina != null) {
            params.put("pagina", pagina);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = salidaDao.lista(params);
            try {
                generaReporte(tipo, (List<Salida>) params.get("salidas"), response, "salidas", Constantes.ALM, almacenId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove("reporte");
                errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = salidaDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Salida>) params.get("salidas"), request, "salidas", Constantes.ALM, almacenId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("salida.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = salidaDao.lista(params);
        modelo.addAttribute("salidas", params.get("salidas"));

        this.pagina(params, modelo, "salidas", pagina);

        return "inventario/salida/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando salida {}", id);
        Salida salida = salidaDao.obtiene(id);
        switch (salida.getEstatus().getNombre()) {
            case Constantes.ABIERTA:
                modelo.addAttribute("puedeEditar", true);
                modelo.addAttribute("puedeEliminar", true);
                modelo.addAttribute("puedeCerrar", true);
                break;
            case Constantes.CERRADA:
                modelo.addAttribute("puedeCancelar", true);
                modelo.addAttribute("puedeReporte", true);
                break;
            case Constantes.CANCELADA:
                log.debug("Puede ver reporte");
                modelo.addAttribute("puedeReporte", true);
                break;
        }

        modelo.addAttribute("salida", salida);

        BigDecimal subtotal = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
        BigDecimal iva = new BigDecimal("0").setScale(2, RoundingMode.HALF_UP);
        for (LoteSalida lote : salida.getLotes()) {
            subtotal = subtotal.add(lote.getPrecioUnitario().multiply(lote.getCantidad()));
            iva = iva.add(lote.getIva());
        }
        BigDecimal total = subtotal.add(iva);
        modelo.addAttribute("subtotal", subtotal.setScale(2, RoundingMode.HALF_UP));
        modelo.addAttribute("iva", iva);
        modelo.addAttribute("total", total.setScale(2, RoundingMode.HALF_UP));
        if (iva.compareTo(salida.getIva()) == 0 && total.compareTo(salida.getTotal()) == 0) {
            modelo.addAttribute("estiloTotales", "label label-success");
        } else {
            BigDecimal variacion = new BigDecimal("0.05");
            BigDecimal topeIva = salida.getIva().multiply(variacion);
            BigDecimal topeTotal = salida.getTotal().multiply(variacion);
            log.debug("Estilos {} {} {} {} {} {}", new Object[]{iva, salida.getIva(), topeIva, total, salida.getTotal(), topeTotal});
            if (iva.compareTo(salida.getIva()) < 0 || total.compareTo(salida.getTotal()) < 0) {
                log.debug("La diferencia es menor");
                if (iva.compareTo(salida.getIva().subtract(topeIva)) >= 0 && total.compareTo(salida.getTotal().subtract(topeTotal)) >= 0) {
                    modelo.addAttribute("estiloTotales", "label label-warning");
                } else {
                    modelo.addAttribute("estiloTotales", "label label-important");
                }
            } else {
                log.debug("La diferencia es mayor {} {}", new Object[]{iva.compareTo(salida.getIva().add(topeIva)), total.compareTo(salida.getTotal().add(topeTotal))});
                if (iva.compareTo(salida.getIva().add(topeIva)) <= 0 && total.compareTo(salida.getTotal().add(topeTotal)) <= 0) {
                    log.debug("estilo warning");
                    modelo.addAttribute("estiloTotales", "label label-warning");
                } else {
                    log.debug("estilo error");
                    modelo.addAttribute("estiloTotales", "label label-important");
                }
            }
        }

        return "inventario/salida/ver";
    }

    @RequestMapping("/nueva")
    public String nueva(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo salida");
        Salida salida = new Salida();
        modelo.addAttribute("salida", salida);

        return "inventario/salida/nueva";
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Salida salida, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return "inventario/salida/nueva";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (request.getParameter("cliente.id") == null) {
                log.warn("No se puede crear la salida si no ha seleccionado un cliente");
                errors.rejectValue("cliente", "salida.sin.cliente.message");
                return "inventario/salida/nueva";
            }
            Cliente cliente = clienteDao.obtiene(new Long(request.getParameter("cliente.id")));
            salida.setCliente(cliente);
            salida.setAtendio(usuario.getApellido() + ", " + usuario.getNombre());
            salida = salidaDao.crea(salida, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la salida", e);
            errors.rejectValue("factura", "campo.duplicado.message", new String[]{"factura"}, null);

            return "inventario/salida/nueva";
        }

        redirectAttributes.addFlashAttribute("message", "salida.creada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{salida.getFolio()});

        return "redirect:/inventario/salida/ver/" + salida.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable Long id, Model modelo) {
        log.debug("Edita salida {}", id);
        Salida salida = salidaDao.obtiene(id);
        modelo.addAttribute("salida", salida);

        return "inventario/salida/edita";
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Salida salida, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "inventario/salida/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (request.getParameter("cliente.id") == null) {
                log.warn("No se puede crear la salida si no ha seleccionado un cliente");
                errors.rejectValue("cliente", "salida.sin.cliente.message");
                return "inventario/salida/nueva";
            }
            Cliente cliente = clienteDao.obtiene(new Long(request.getParameter("cliente.id")));
            salida.setCliente(cliente);
            salida.setAtendio(usuario.getApellido() + ", " + usuario.getNombre());
            salida = salidaDao.actualiza(salida, usuario);
        } catch (NoEstaAbiertaException e) {
            log.error("No se pudo actualizar la salida", e);
            modelo.addAttribute("message", "salida.intento.modificar.cerrada.message");
            modelo.addAttribute("messageStyle", "alert-error");
            modelo.addAttribute("messageAttrs", new String[]{salida.getFolio()});
            return "inventario/salida/nueva";
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la salida", e);
            errors.rejectValue("factura", "campo.duplicado.message", new String[]{"factura"}, null);

            return "inventario/salida/nueva";
        }

        redirectAttributes.addFlashAttribute("message", "salida.actualizada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{salida.getFolio()});

        return "redirect:/inventario/salida/ver/" + salida.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Salida salida, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina salida");
        try {
            String nombre = salidaDao.elimina(id);

            redirectAttributes.addFlashAttribute("message", "salida.eliminada.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la salida " + id, e);
            bindingResult.addError(new ObjectError("salida", new String[]{"salida.no.eliminada.message"}, null, null));
            return "inventario/salida/ver";
        }

        return "redirect:/inventario/salida";
    }

    @RequestMapping("/cerrar/{id}")
    public String cerrar(HttpServletRequest request, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.debug("Cierra salida {}", id);
        try {
            String folio = salidaDao.cierra(id, ambiente.obtieneUsuario());
            redirectAttributes.addFlashAttribute("message", "salida.cerrada.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{folio});
        } catch (NoHayExistenciasSuficientes e) {
            log.error("No se pudo cerrar la salida", e);
            redirectAttributes.addFlashAttribute("message", "salida.producto.sin.existencias.suficientes.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{e.getProducto().getNombre(), e.getProducto().getExistencia().toString(), e.getProducto().getUnidadMedida()});
        } catch (NoEstaAbiertaException e) {
            log.error("No se pudo cerrar la salida", e);
            redirectAttributes.addFlashAttribute("message", "salida.intento.modificar.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{""});
        } catch (NoSePuedeCerrarEnCeroException e) {
            log.error("No se pudo cerrar la salida", e);
            redirectAttributes.addFlashAttribute("message", "salida.no.cerrada.en.cero.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
        } catch (NoSePuedeCerrarException e) {
            log.error("No se pudo cerrar la salida", e);
            redirectAttributes.addFlashAttribute("message", "salida.no.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
        }

        return "redirect:/inventario/salida/ver/" + id;
    }

    @RequestMapping("/cancela/{id}")
    public String cancela(@PathVariable Long id, Model modelo, RedirectAttributes redirectAttributes) {
        try {
            log.debug("Cancela salida {}", id);
            Map<String, Object> resultado = salidaDao.preCancelacion(id, ambiente.obtieneUsuario());
            modelo.addAttribute("salida", resultado.get("salida"));
            modelo.addAttribute("productos", resultado.get("productos"));
            modelo.addAttribute("entradas", resultado.get("entradas"));
            modelo.addAttribute("salidas", resultado.get("salidas"));
            modelo.addAttribute("productosCancelados", resultado.get("productosCancelados"));
            modelo.addAttribute("productosSinHistoria", resultado.get("productosSinHistoria"));

            return "inventario/salida/cancela";
        } catch (NoEstaCerradaException e) {
            log.error("No se puede cancelar la salida", e);
            redirectAttributes.addFlashAttribute("message", "salida.no.cerrada.para.cancelar.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{e.getSalida().getFolio()});
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            return "redirect:/inventario/salida/ver/" + id;
        }
    }

    @RequestMapping(value = "/cancelar", method = RequestMethod.POST)
    public String cancelar(@RequestParam Long id, @RequestParam String comentarios, Model modelo, RedirectAttributes redirectAttributes) {
        try {
            log.debug("Cancelando salida {}", id);
            Cancelacion cancelacion = salidaDao.cancelar(id, ambiente.obtieneUsuario(), comentarios);
            modelo.addAttribute("cancelacion", cancelacion);

            modelo.addAttribute("message", "salida.cancelada.message");
            modelo.addAttribute("messageAttrs", new String[]{cancelacion.getSalida().getFolio()});
            modelo.addAttribute("messageStyle", "alert-success");
            return "/inventario/salida/cancelada";
        } catch (NoEstaCerradaException e) {
            log.error("No se puede cancelar la salida", e);
            redirectAttributes.addFlashAttribute("message", "salida.no.cerrada.para.cancelar.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{e.getSalida().getFolio()});
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            return "redirect:/inventario/salida/ver/" + id;
        }
    }

    @RequestMapping(value = "/clientes", params = "term", produces = "application/json")
    public @ResponseBody
    List<LabelValueBean> clientes(HttpServletRequest request, @RequestParam("term") String filtro) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession().getAttribute("empresaId"));
        params.put("filtro", filtro);
        params = clienteDao.lista(params);
        List<LabelValueBean> valores = new ArrayList<>();
        List<Cliente> clientes = (List<Cliente>) params.get("clientes");
        for (Cliente cliente : clientes) {
            StringBuilder sb = new StringBuilder();
            sb.append(cliente.getNombre());
            sb.append(" | ");
            sb.append(cliente.getRfc());
            sb.append(" | ");
            sb.append(cliente.getNombreCompleto());
            valores.add(new LabelValueBean(cliente.getId(), sb.toString(), cliente.getNombre()));
        }
        return valores;
    }

    @RequestMapping(value = "/productos", params = "term", produces = "application/json")
    public @ResponseBody
    List<LabelValueBean> productos(HttpServletRequest request, @RequestParam("term") String filtro) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        List<Producto> productos = productoDao.listaParaSalida(filtro, (Long) request.getSession().getAttribute("almacenId"));
        List<LabelValueBean> valores = new ArrayList<>();
        for (Producto producto : productos) {
            StringBuilder sb = new StringBuilder();
            sb.append(producto.getSku());
            sb.append(" | ");
            sb.append(producto.getNombre());
            sb.append(" | ");
            sb.append(producto.getDescripcion());
            sb.append(" | ");
            sb.append(producto.getExistencia()).append(" ").append(producto.getUnidadMedida());
            sb.append(" | ");
            sb.append(producto.getPrecioUnitario());
            valores.add(new LabelValueBean(producto.getId(), sb.toString()));
        }
        return valores;
    }

    @RequestMapping("/lote/{id}")
    public String nuevoLote(@PathVariable Long id, Model modelo) {
        log.debug("Nuevo lote para salida {}", id);
        Salida salida = salidaDao.carga(id);
        LoteSalida lote = new LoteSalida(salida);

        modelo.addAttribute("lote", lote);

        return "inventario/salida/lote";
    }

    @RequestMapping(value = "/lote/crea", method = RequestMethod.POST)
    public String creaLote(HttpServletRequest request, @Valid LoteSalida lote, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "inventario/salida/lote/" + request.getParameter("salida.id");
        }

        try {
            if (StringUtils.isBlank(request.getParameter("producto.id"))) {
                log.warn("No se puede crear la salida si no ha seleccionado un cliente");
                modelo.addAttribute("message", "lote.sin.producto.message");
                modelo.addAttribute("lote", lote);
                return "inventario/salida/lote";
            }
            Producto producto = productoDao.obtiene(new Long(request.getParameter("producto.id")));
            Salida salida = salidaDao.obtiene(new Long(request.getParameter("salida.id")));
            lote.setProducto(producto);
            lote.setSalida(salida);
            lote.setFechaCreacion(new Date());
            lote = salidaDao.creaLote(lote);
        } catch (NoEstaAbiertaException e) {
            log.error("No se pudo cerrar la salida", e);
            redirectAttributes.addFlashAttribute("message", "salida.intento.modificar.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{""});
        } catch (ProductoNoSoportaFraccionException e) {
            log.error("No se pudo crear la salida porque no se encontro el producto", e);
            modelo.addAttribute("message", "lote.sin.producto.message");
            modelo.addAttribute("lote", lote);
            return "inventario/salida/lote";
        }

        redirectAttributes.addFlashAttribute("message", "lote.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{lote.getProducto().getNombre(), lote.getCantidad().toString(), lote.getPrecioUnitario().toString(), lote.getProducto().getUnidadMedida(), lote.getIva().add(lote.getPrecioUnitario().multiply(lote.getCantidad())).toString()});

        return "redirect:/inventario/salida/ver/" + lote.getSalida().getId();
    }

    @RequestMapping("/lote/elimina/{id}")
    public String eliminaLote(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.debug("Eliminando lote {}", id);
        try {
            id = salidaDao.eliminaLote(id);
            redirectAttributes.addFlashAttribute("message", "lote.eliminado.message");
        } catch (NoEstaAbiertaException e) {
            log.error("No se pudo cerrar la salida", e);
            redirectAttributes.addFlashAttribute("message", "salida.intento.modificar.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{""});
        }

        return "redirect:/inventario/salida/ver/" + id;
    }
}
