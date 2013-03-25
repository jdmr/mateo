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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import mx.edu.um.mateo.inventario.dao.EntradaDao;
import mx.edu.um.mateo.inventario.dao.FacturaAlmacenDao;
import mx.edu.um.mateo.inventario.dao.SalidaDao;
import mx.edu.um.mateo.inventario.model.Entrada;
import mx.edu.um.mateo.inventario.model.FacturaAlmacen;
import mx.edu.um.mateo.inventario.model.Salida;
import mx.edu.um.mateo.inventario.utils.NoEstaAbiertaException;
import mx.edu.um.mateo.inventario.utils.NoEstaCerradaException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCancelarException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarEnCeroException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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
@RequestMapping("/inventario/factura")
public class FacturaAlmacenController extends BaseController {

    @Autowired
    private FacturaAlmacenDao facturaDao;
    @Autowired
    private EntradaDao entradaDao;
    @Autowired
    private SalidaDao salidaDao;
    @Autowired
    private ClienteDao clienteDao;

    @SuppressWarnings("unchecked")
    @RequestMapping
    public String lista(HttpServletRequest request,
            HttpServletResponse response, Usuario usuario, Errors errors,
            Model modelo) throws ParseException {
        log.debug("Mostrando lista de facturas");
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
            params = facturaDao.lista(params);
            try {
                generaReporte((String) params.get("tipo"),
                        (List<FacturaAlmacen>) params.get("facturas"),
                        response, "facturasAlmacen", Constantes.ALM, almacenId);
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
            params = facturaDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo((String) params.get("correo"),
                        (List<FacturaAlmacen>) params.get("facturas"), request,
                        "facturasAlmacen", Constantes.ALM, almacenId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute(
                        "messageAttrs",
                        new String[]{
                            messageSource.getMessage(
                            "facturaAlmacen.lista.label", null,
                            request.getLocale()),
                            ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = facturaDao.lista(params);
        modelo.addAttribute("facturas", params.get("facturas"));

        Long pagina = 1l;
        if (params.containsKey("pagina")) {
            pagina = (Long) params.get("pagina");
        }
        this.pagina(params, modelo, "facturas", pagina);

        return "inventario/factura/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando factura {}", id);
        FacturaAlmacen factura = facturaDao.obtiene(id);
        switch (factura.getEstatus().getNombre()) {
            case Constantes.ABIERTA:
                modelo.addAttribute("puedeEditar", true);
                modelo.addAttribute("puedeEliminar", true);
                modelo.addAttribute("puedeCerrar", true);
                break;
            case Constantes.CERRADA:
                modelo.addAttribute("puedeCancelar", true);
                modelo.addAttribute("puedeReporte", true);
                break;
        }

        modelo.addAttribute("factura", factura);

        BigDecimal salidasTotal = BigDecimal.ZERO;
        BigDecimal salidasIva = BigDecimal.ZERO;
        for (Salida salida : factura.getSalidas()) {
            salidasTotal = salidasTotal.add(salida.getTotal());
            salidasIva = salidasIva.add(salida.getIva());
        }
        modelo.addAttribute("salidasTotal", salidasTotal);
        modelo.addAttribute("salidasIva", salidasIva);
        modelo.addAttribute("salidasSubtotal",
                salidasTotal.subtract(salidasIva));

        BigDecimal entradasTotal = BigDecimal.ZERO;
        BigDecimal entradasIva = BigDecimal.ZERO;
        for (Entrada entrada : factura.getEntradas()) {
            entradasTotal = entradasTotal.add(entrada.getTotal());
            entradasIva = entradasIva.add(entrada.getIva());
        }
        modelo.addAttribute("entradasTotal", entradasTotal);
        modelo.addAttribute("entradasIva", entradasIva);
        modelo.addAttribute("entradasSubtotal",
                entradasTotal.subtract(entradasIva));

        return "inventario/factura/ver";
    }

    @RequestMapping("/nueva")
    public String nueva(HttpServletRequest request, Model modelo) {
        log.debug("Nueva factura");
        FacturaAlmacen factura = new FacturaAlmacen();
        modelo.addAttribute("factura", factura);

        return "inventario/factura/nueva";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request,
            HttpServletResponse response, @Valid FacturaAlmacen factura,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (request.getParameter("cliente.id") == null) {
                log.warn("No se puede crear la factura si no ha seleccionado un cliente");
                errors.rejectValue("cliente",
                        "facturaAlmacen.sin.cliente.message");
                return "inventario/factura/nueva";
            }
            Cliente cliente = clienteDao.obtiene(new Long(request
                    .getParameter("cliente.id")));
            factura.setCliente(cliente);
            factura = facturaDao.crea(factura, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la factura", e);
            errors.rejectValue("factura", "campo.duplicado.message",
                    new String[]{"factura"}, null);

            return "inventario/factura/nueva";
        }

        redirectAttributes.addFlashAttribute("message",
                "facturaAlmacen.creada.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{factura.getFolio()});

        return "redirect:/inventario/factura/ver/" + factura.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable Long id,
            Model modelo) {
        log.debug("Edita factura {}", id);
        FacturaAlmacen factura = facturaDao.obtiene(id);
        modelo.addAttribute("factura", factura);

        return "inventario/factura/edita";
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request,
            @Valid FacturaAlmacen factura, BindingResult bindingResult,
            Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "inventario/factura/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (request.getParameter("cliente.id") == null) {
                log.warn("No se puede crear la factura si no ha seleccionado un cliente");
                errors.rejectValue("cliente",
                        "facturaAlmacen.sin.cliente.message");
                return "inventario/factura/nueva";
            }
            Cliente cliente = clienteDao.obtiene(new Long(request
                    .getParameter("cliente.id")));
            factura.setCliente(cliente);
            factura = facturaDao.actualiza(factura, usuario);
        } catch (NoEstaAbiertaException e) {
            log.error("No se pudo actualizar la factura", e);
            modelo.addAttribute("message",
                    "facturaAlmacen.intento.modificar.cerrada.message");
            modelo.addAttribute("messageStyle", "alert-error");
            modelo.addAttribute("messageAttrs",
                    new String[]{factura.getFolio()});
            return "inventario/factura/nueva";
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la factura", e);
            errors.rejectValue("factura", "campo.duplicado.message",
                    new String[]{"factura"}, null);

            return "inventario/factura/nueva";
        }

        redirectAttributes.addFlashAttribute("message",
                "facturaAlmacen.actualizada.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{factura.getFolio()});

        return "redirect:/inventario/factura/ver/" + factura.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id,
            Model modelo, @ModelAttribute FacturaAlmacen factura,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina factura");
        try {
            String nombre = facturaDao.elimina(id);

            redirectAttributes.addFlashAttribute("message",
                    "facturaAlmacen.eliminada.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la factura " + id, e);
            redirectAttributes.addFlashAttribute("message",
                    "facturaAlmacen.no.eliminada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{id.toString()});
            return "redirect:/inventario/factura/ver/" + id;
        }

        return "redirect:/inventario/factura";
    }

    @RequestMapping("/cerrar/{id}")
    public String cerrar(HttpServletRequest request, @PathVariable Long id,
            RedirectAttributes redirectAttributes) {
        log.debug("Cierra factura {}", id);
        try {
            String folio = facturaDao.cierra(id, ambiente.obtieneUsuario());
            redirectAttributes.addFlashAttribute("message",
                    "facturaAlmacen.cerrada.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{folio});
        } catch (NoEstaAbiertaException e) {
            log.error("No se pudo cerrar la factura", e);
            redirectAttributes.addFlashAttribute("message",
                    "facturaAlmacen.intento.modificar.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{""});
        } catch (NoSePuedeCerrarEnCeroException e) {
            log.error("No se pudo cerrar la factura", e);
            redirectAttributes.addFlashAttribute("message",
                    "facturaAlmacen.no.cerrada.en.cero.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
        } catch (NoSePuedeCerrarException e) {
            log.error("No se pudo cerrar la factura", e);
            redirectAttributes.addFlashAttribute("message",
                    "facturaAlmacen.no.cerrada.message");
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
        }

        return "redirect:/inventario/factura/ver/" + id;
    }

    @RequestMapping(value = "/cancela/{id}")
    public String cancela(@PathVariable Long id, Model modelo,
            RedirectAttributes redirectAttributes) {
        try {
            log.debug("Cancelando factura {}", id);
            FacturaAlmacen factura = facturaDao.cancelar(id,
                    ambiente.obtieneUsuario());

            redirectAttributes.addFlashAttribute("message",
                    "facturaAlmacen.cancelada.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{factura.getFolio()});
            redirectAttributes.addFlashAttribute("messageStyle",
                    "alert-success");
            return "redirect:/inventario/factura/ver/" + id;
        } catch (NoEstaCerradaException e) {
            log.error("No se puede cancelar la factura", e);
            redirectAttributes.addFlashAttribute("message",
                    "facturaAlmacen.no.cerrada.para.cancelar.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{e.getFactura().getFolio()});
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            return "redirect:/inventario/factura/ver/" + id;
        } catch (NoSePuedeCancelarException e) {
            log.error("No se puede cancelar la factura", e);
            redirectAttributes.addFlashAttribute("message",
                    "facturaAlmacen.no.cancelada.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{e.getFactura().getFolio()});
            redirectAttributes.addFlashAttribute("messageStyle", "alert-error");
            return "redirect:/inventario/factura/ver/" + id;
        }
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/clientes", params = "term", produces = "application/json")
    public @ResponseBody
    List<LabelValueBean> clientes(HttpServletRequest request,
            @RequestParam("term") String filtro) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
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
            valores.add(new LabelValueBean(cliente.getId(), sb.toString(),
                    cliente.getNombre()));
        }
        return valores;
    }

    @RequestMapping(value = "/buscaSalida", params = "term", produces = "application/json")
    public @ResponseBody
    List<LabelValueBean> buscaSalida(
            HttpServletRequest request,
            @RequestParam("term") String filtro,
            @RequestParam("facturaId") Long facturaId) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("almacen", request.getSession().getAttribute("almacenId"));
        params.put("filtro", filtro);
        params.put("facturaId", facturaId);
        List<Salida> salidas = salidaDao.buscaSalidasParaFactura(params);
        List<LabelValueBean> valores = new ArrayList<>();
        for (Salida salida : salidas) {
            StringBuilder sb = new StringBuilder();
            sb.append(salida.getFolio());
            sb.append(" | ");
            sb.append(salida.getCliente().getNombre());
            valores.add(new LabelValueBean(salida.getId(), sb.toString(),
                    salida.getFolio()));
        }
        return valores;
    }

    @RequestMapping(value = "/buscaEntrada", params = "term", produces = "application/json")
    public @ResponseBody
    List<LabelValueBean> buscaEntrada(HttpServletRequest request,
            @RequestParam("term") String filtro,
            @RequestParam("facturaId") Long facturaId) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("almacen", request.getSession().getAttribute("almacenId"));
        params.put("filtro", filtro);
        params.put("facturaId", facturaId);
        List<Entrada> entradas = entradaDao.buscaEntradasParaFactura(params);
        List<LabelValueBean> valores = new ArrayList<>();
        for (Entrada entrada : entradas) {
            StringBuilder sb = new StringBuilder();
            sb.append(entrada.getFolio());
            sb.append(" | ");
            sb.append(entrada.getProveedor().getNombre());
            valores.add(new LabelValueBean(entrada.getId(), sb.toString(),
                    entrada.getFolio()));
        }
        return valores;
    }

    @RequestMapping(value = "/salida/nueva", method = RequestMethod.POST)
    public String nuevaSalida(@RequestParam Long id,
            @RequestParam Long salidaId, Model modelo,
            RedirectAttributes redirectAttributes) throws NoEstaAbiertaException {
        log.debug("Nueva salida para factura {}", id);
        FacturaAlmacen factura = facturaDao.agregaSalida(id, salidaId);

        modelo.addAttribute("message", "facturaAlmacen.agrega.salida.message");
        modelo.addAttribute("messageAttrs", new String[]{factura.getFolio()});
        modelo.addAttribute("messageStyle", "alert-success");
        return "redirect:/inventario/factura/ver/" + id;
    }

    @RequestMapping(value = "/entrada/nueva", method = RequestMethod.POST)
    public String nuevaEntrada(@RequestParam Long id,
            @RequestParam Long entradaId, Model modelo,
            RedirectAttributes redirectAttributes) throws NoEstaAbiertaException {
        log.debug("Nueva entrada para factura {}", id);
        FacturaAlmacen factura = facturaDao.agregaEntrada(id, entradaId);

        modelo.addAttribute("message", "facturaAlmacen.agrega.entrada.message");
        modelo.addAttribute("messageAttrs", new String[]{factura.getFolio()});
        modelo.addAttribute("messageStyle", "alert-success");
        return "redirect:/inventario/factura/ver/" + id;
    }

    @RequestMapping(value = "/salida/elimina/{id}/{salidaId}")
    public String eliminaSalida(@PathVariable Long id,
            @PathVariable Long salidaId, Model modelo,
            RedirectAttributes redirectAttributes) throws NoEstaAbiertaException {
        log.debug("Eliminando salida {} de factura {}", salidaId, id);
        FacturaAlmacen factura = facturaDao.eliminaSalida(id, salidaId);

        redirectAttributes.addFlashAttribute("message",
                "facturaAlmacen.elimina.salida.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{factura.getFolio()});
        redirectAttributes.addFlashAttribute("messageStyle", "alert-success");
        return "redirect:/inventario/factura/ver/" + id;
    }

    @RequestMapping(value = "/entrada/elimina/{id}/{entradaId}")
    public String eliminaEntrada(@PathVariable Long id,
            @PathVariable Long entradaId, Model modelo,
            RedirectAttributes redirectAttributes) throws NoEstaAbiertaException {
        log.debug("Eliminando entrada {} de factura {}", entradaId, id);
        FacturaAlmacen factura = facturaDao.eliminaEntrada(id, entradaId);

        redirectAttributes.addFlashAttribute("message",
                "facturaAlmacen.elimina.entrada.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{factura.getFolio()});
        redirectAttributes.addFlashAttribute("messageStyle", "alert-success");
        return "redirect:/inventario/factura/ver/" + id;
    }
}
