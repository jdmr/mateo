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

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.model.Imagen;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inventario.dao.ProductoDao;
import mx.edu.um.mateo.inventario.dao.TipoProductoDao;
import mx.edu.um.mateo.inventario.model.Entrada;
import mx.edu.um.mateo.inventario.model.Producto;
import mx.edu.um.mateo.inventario.model.Salida;
import mx.edu.um.mateo.inventario.model.XProducto;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/inventario/producto")
public class ProductoController extends BaseController {

    @Autowired
    private ProductoDao productoDao;
    @Autowired
    private TipoProductoDao tipoProductoDao;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            Usuario usuario,
            Errors errors,
            Model modelo) throws ParseException {
        log.debug("Mostrando lista de productos");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Map<String, Object> params = this.convierteParams(request.getParameterMap());
        Long almacenId = (Long) request.getSession().getAttribute("almacenId");
        params.put("almacen", almacenId);

        boolean buscarPorFecha = false;
        if (params.containsKey("fecha")) {
            log.debug("Fecha: {}", params.get("fecha"));
            params.put("fecha", sdf.parse((String) params.get("fecha")));
            buscarPorFecha = true;
        }
        
        if (params.containsKey("inactivo")) {
            params.put("inactivo", Boolean.valueOf((String) params.get("inactivo")));
        }

        if (params.containsKey("tipo") && StringUtils.isNotBlank((String) params.get("tipo"))) {
            params.put("reporte", true);
            if (!buscarPorFecha) {
                params = productoDao.lista(params);
            } else {
                log.debug("Buscando por fecha {}", params.get("fecha"));
                params = productoDao.obtieneHistorial(params);
            }
            try {
                generaReporte((String) params.get("tipo"), (List<Producto>) params.get("productos"), response, "productos", Constantes.ALM, almacenId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove("reporte");
                errors.reject("error.generar.reporte");
            }
        }

        if (params.containsKey("correo") && StringUtils.isNotBlank((String) params.get("correo"))) {
            params.put("reporte", true);
            if (!buscarPorFecha) {
                params = productoDao.lista(params);
            } else {
                log.debug("Buscando por fecha {}", params.get("fecha"));
                params = productoDao.obtieneHistorial(params);
            }

            params.remove("reporte");
            try {
                enviaCorreo((String) params.get("correo"), (List<Producto>) params.get("productos"), request, "productos", Constantes.ALM, almacenId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("producto.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        if (!buscarPorFecha) {
            params = productoDao.lista(params);
        } else {
            log.debug("Buscando por fecha {}", params.get("fecha"));
            params = productoDao.obtieneHistorial(params);
        }
        modelo.addAttribute("productos", params.get("productos"));

        Long pagina = 1l;
        if (params.containsKey("pagina")) {
            pagina = (Long) params.get("pagina");
        }
        this.pagina(params, modelo, "productos", pagina);

        return "inventario/producto/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando producto {}", id);
        Producto producto = productoDao.obtiene(id);

        modelo.addAttribute("producto", producto);

        return "inventario/producto/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo producto");
        Producto producto = new Producto();
        modelo.addAttribute("producto", producto);

        Map<String, Object> params = new HashMap<>();
        params.put("almacen", request.getSession().getAttribute("almacenId"));
        params.put("reporte", true);
        params = tipoProductoDao.lista(params);
        modelo.addAttribute("tiposDeProducto", params.get("tiposDeProducto"));

        return "inventario/producto/nuevo";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, @Valid Producto producto, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes, @RequestParam(value = "imagen", required = false) MultipartFile archivo) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return "inventario/producto/nuevo";
        }

        try {

            if (archivo != null && !archivo.isEmpty()) {
                Imagen imagen = new Imagen(
                        archivo.getOriginalFilename(),
                        archivo.getContentType(),
                        archivo.getSize(),
                        archivo.getBytes());
                producto.getImagenes().add(imagen);
            }
            Usuario usuario = ambiente.obtieneUsuario();
            producto = productoDao.crea(producto, usuario);

        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el producto", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);

            Map<String, Object> params = new HashMap<>();
            params.put("almacen", request.getSession().getAttribute("almacenId"));
            params.put("reporte", true);
            params = tipoProductoDao.lista(params);
            modelo.addAttribute("tiposDeProducto", params.get("tiposDeProducto"));

            return "inventario/producto/nuevo";
        } catch (IOException e) {
            log.error("No se pudo crear el producto", e);
            errors.rejectValue("imagenes", "problema.con.imagen.message", new String[]{archivo.getOriginalFilename()}, null);

            Map<String, Object> params = new HashMap<>();
            params.put("almacen", request.getSession().getAttribute("almacenId"));
            params.put("reporte", true);
            params = tipoProductoDao.lista(params);
            modelo.addAttribute("tiposDeProducto", params.get("tiposDeProducto"));

            return "inventario/producto/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "producto.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{producto.getNombre()});

        return "redirect:/inventario/producto/ver/" + producto.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable Long id, Model modelo) {
        log.debug("Edita producto {}", id);
        Producto producto = productoDao.obtiene(id);
        modelo.addAttribute("producto", producto);

        Map<String, Object> params = new HashMap<>();
        params.put("almacen", request.getSession().getAttribute("almacenId"));
        params.put("reporte", true);
        params = tipoProductoDao.lista(params);
        modelo.addAttribute("tiposDeProducto", params.get("tiposDeProducto"));

        return "inventario/producto/edita";
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Producto producto, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes, @RequestParam(value = "imagen", required = false) MultipartFile archivo) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "inventario/producto/edita";
        }

        try {
            if (archivo != null && !archivo.isEmpty()) {
                Imagen imagen = new Imagen(
                        archivo.getOriginalFilename(),
                        archivo.getContentType(),
                        archivo.getSize(),
                        archivo.getBytes());
                producto.getImagenes().add(imagen);
            }
            Usuario usuario = ambiente.obtieneUsuario();
            producto = productoDao.actualiza(producto, usuario);
        } catch (IOException e) {
            log.error("No se pudo actualizar el producto", e);
            errors.rejectValue("imagenes", "problema.con.imagen.message", new String[]{archivo.getOriginalFilename()}, null);

            Map<String, Object> params = new HashMap<>();
            params.put("almacen", request.getSession().getAttribute("almacenId"));
            params.put("reporte", true);
            params = tipoProductoDao.lista(params);
            modelo.addAttribute("tiposDeProducto", params.get("tiposDeProducto"));

            return "inventario/producto/edita";
        } catch (ConstraintViolationException e) {
            log.error("No se pudo actualizar el producto", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);

            Map<String, Object> params = new HashMap<>();
            params.put("almacen", request.getSession().getAttribute("almacenId"));
            params.put("reporte", true);
            params = tipoProductoDao.lista(params);
            modelo.addAttribute("tiposDeProducto", params.get("tiposDeProducto"));

            return "inventario/producto/edita";
        }

        redirectAttributes.addFlashAttribute("message", "producto.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{producto.getNombre()});

        return "redirect:/inventario/producto/ver/" + producto.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Producto producto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina producto");
        try {
            String nombre = productoDao.elimina(id);

            redirectAttributes.addFlashAttribute("message", "producto.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la producto " + id, e);
            bindingResult.addError(new ObjectError("producto", new String[]{"producto.no.eliminado.message"}, null, null));
            return "inventario/producto/ver";
        }

        return "redirect:/inventario/producto";
    }

    @RequestMapping("/historial/{id}")
    public String historial(@PathVariable Long id, @RequestParam(required = false) Long pagina, Model modelo) {
        log.debug("Mostrando historial del producto {}", id);
        Map<String, Object> params = new HashMap<>();
        params = productoDao.historial(id, params);

        modelo.addAttribute("historial", params.get("historial"));

        // inicia paginado
        Long cantidad = (Long) params.get("cantidad");
        Integer max = (Integer) params.get("max");
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<XProducto> productos = (List<XProducto>) params.get("historial");
        if (pagina == null) {
            pagina = 1l;
        }
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (productos.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute("paginacion", paginacion);
        modelo.addAttribute("paginas", paginas);
        // termina paginado

        return "inventario/producto/historial";
    }
    
    @RequestMapping("/arreglaDescripciones")
    public String arreglaDescripciones() {
        log.debug("Arreglando descripciones");
        productoDao.arreglaDescripciones();
        return "redirect:/inventario/producto";
    }
}
