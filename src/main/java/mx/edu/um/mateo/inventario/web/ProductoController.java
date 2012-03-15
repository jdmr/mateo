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

import java.io.BufferedOutputStream;
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
import mx.edu.um.mateo.general.model.Imagen;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inventario.dao.ProductoDao;
import mx.edu.um.mateo.inventario.dao.TipoProductoDao;
import mx.edu.um.mateo.inventario.model.Producto;
import mx.edu.um.mateo.inventario.model.XProducto;
import net.sf.jasperreports.engine.JRException;
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
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Usuario usuario,
            Errors errors,
            Model modelo) {
        log.debug("Mostrando lista de tipos de productos");
        Map<String, Object> params = new HashMap<>();
        params.put("almacen", request.getSession().getAttribute("almacenId"));
        if (StringUtils.isNotBlank(filtro)) {
            params.put("filtro", filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put("order", order);
            params.put("sort", sort);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = productoDao.lista(params);
            try {
                generaReporte(tipo, (List<Producto>) params.get("productos"), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove("reporte");
                errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = productoDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Producto>) params.get("productos"), request);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("producto.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = productoDao.lista(params);
        modelo.addAttribute("productos", params.get("productos"));

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

    private void generaReporte(String tipo, List<Producto> productos, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = reporteUtil.generaPdf(productos, "/mx/edu/um/mateo/inventario/reportes/productos.jrxml");
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=productos.pdf");
                break;
            case "CSV":
                archivo = reporteUtil.generaCsv(productos, "/mx/edu/um/mateo/inventario/reportes/productos.jrxml");
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=productos.csv");
                break;
            case "XLS":
                archivo = reporteUtil.generaXls(productos, "/mx/edu/um/mateo/inventario/reportes/productos.jrxml");
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=productos.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<Producto> productos, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = reporteUtil.generaPdf(productos, "/mx/edu/um/mateo/inventario/reportes/productos.jrxml");
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = reporteUtil.generaCsv(productos, "/mx/edu/um/mateo/inventario/reportes/productos.jrxml");
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = reporteUtil.generaXls(productos, "/mx/edu/um/mateo/inventario/reportes/productos.jrxml");
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("producto.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

}
