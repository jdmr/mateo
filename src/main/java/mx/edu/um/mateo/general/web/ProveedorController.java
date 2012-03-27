/*
 * The MIT License
 *
 * Copyright 2012 J. David Mendoza <jdmendoza@um.edu.mx>.
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
package mx.edu.um.mateo.general.web;

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
import mx.edu.um.mateo.general.utils.ReporteException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
@RequestMapping("/admin/proveedor")
public class ProveedorController extends BaseController {

    @Autowired
    private ProveedorDao proveedorDao;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de proveedores");
        Map<String, Object> params = new HashMap<>();
        Long empresaId = (Long) request.getSession().getAttribute("empresaId");
        params.put("empresa", empresaId);
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
            params = proveedorDao.lista(params);
            try {
                generaReporte(tipo, (List<Proveedor>) params.get("proveedores"), response, "proveedores", Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = proveedorDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Proveedor>) params.get("proveedores"), request, "proveedores", Constantes.EMP, empresaId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("proveedor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = proveedorDao.lista(params);
        modelo.addAttribute("proveedores", params.get("proveedores"));

        this.pagina(params, modelo, "proveedores", pagina);

        return "admin/proveedor/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando proveedor {}", id);
        Proveedor proveedor = proveedorDao.obtiene(id);

        modelo.addAttribute("proveedor", proveedor);

        return "admin/proveedor/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo proveedor");
        Proveedor proveedor = new Proveedor();
        modelo.addAttribute("proveedor", proveedor);
        return "admin/proveedor/nuevo";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Proveedor proveedor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return "admin/proveedor/nuevo";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            proveedor = proveedorDao.crea(proveedor, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al proveedor", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "admin/proveedor/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "proveedor.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{proveedor.getNombre()});

        return "redirect:/admin/proveedor/ver/" + proveedor.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita proveedor {}", id);
        Proveedor proveedor = proveedorDao.obtiene(id);
        modelo.addAttribute("proveedor", proveedor);
        return "admin/proveedor/edita";
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Proveedor proveedor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "admin/proveedor/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            proveedor = proveedorDao.actualiza(proveedor, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la proveedor", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "admin/proveedor/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "proveedor.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{proveedor.getNombre()});

        return "redirect:/admin/proveedor/ver/" + proveedor.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Proveedor proveedor, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina proveedor");
        try {
            String nombre = proveedorDao.elimina(id);

            redirectAttributes.addFlashAttribute("message", "proveedor.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la proveedor " + id, e);
            bindingResult.addError(new ObjectError("proveedor", new String[]{"proveedor.no.eliminado.message"}, null, null));
            return "admin/proveedor/ver";
        }

        return "redirect:/admin/proveedor";
    }
}
