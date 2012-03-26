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
package mx.edu.um.mateo.general.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.dao.OrganizacionDao;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.utils.UltimoException;
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
 * @author jdmr
 */
@Controller
@RequestMapping("/admin/organizacion")
public class OrganizacionController extends BaseController {

    @Autowired
    private OrganizacionDao organizacionDao;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de organizaciones");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(filtro)) {
            params.put("filtro", filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put("order", order);
            params.put("sort", sort);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = organizacionDao.lista(params);
            try {
                generaReporte(tipo, (List<Organizacion>) params.get("organizaciones"), response, "organizaciones", Constantes.ADMIN, null);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = organizacionDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Organizacion>) params.get("organizaciones"), request, "organizaciones", Constantes.ADMIN, null);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("organizacion.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = organizacionDao.lista(params);
        modelo.addAttribute("organizaciones", params.get("organizaciones"));

        this.pagina(params, modelo, "organizaciones", pagina);

        return "admin/organizacion/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando organizacion {}", id);
        Organizacion organizacion = organizacionDao.obtiene(id);

        modelo.addAttribute("organizacion", organizacion);

        return "admin/organizacion/ver";
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nuevo organizacion");
        Organizacion organizacion = new Organizacion();
        modelo.addAttribute("organizacion", organizacion);
        return "admin/organizacion/nueva";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Organizacion organizacion, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return "admin/organizacion/nuevo";
        }

        try {
            Usuario usuario = null;
            if (ambiente.obtieneUsuario() != null) {
                usuario = ambiente.obtieneUsuario();
            }
            organizacion = organizacionDao.crea(organizacion, usuario);

            ambiente.actualizaSesion(request, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al organizacion", e);
            errors.rejectValue("codigo", "campo.duplicado.message", new String[]{"codigo"}, null);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "admin/organizacion/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "organizacion.creada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{organizacion.getNombre()});

        return "redirect:/admin/organizacion/ver/" + organizacion.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita organizacion {}", id);
        Organizacion organizacion = organizacionDao.obtiene(id);
        modelo.addAttribute("organizacion", organizacion);
        return "admin/organizacion/edita";
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Organizacion organizacion, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "admin/organizacion/edita";
        }

        try {
            Usuario usuario = null;
            if (ambiente.obtieneUsuario() != null) {
                usuario = ambiente.obtieneUsuario();
            }
            organizacion = organizacionDao.actualiza(organizacion, usuario);

            ambiente.actualizaSesion(request, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al organizacion", e);
            errors.rejectValue("codigo", "campo.duplicado.message", new String[]{"codigo"}, null);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "admin/organizacion/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "organizacion.actualizada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{organizacion.getNombre()});

        return "redirect:/admin/organizacion/ver/" + organizacion.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Organizacion organizacion, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina organizacion");
        try {
            String nombre = organizacionDao.elimina(id);

            ambiente.actualizaSesion(request);

            redirectAttributes.addFlashAttribute("message", "organizacion.eliminada.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (UltimoException e) {
            log.error("No se pudo eliminar el organizacion " + id, e);
            bindingResult.addError(new ObjectError("organizacion", new String[]{"ultima.organizacion.no.eliminada.message"}, null, null));
            return "admin/organizacion/ver";
        } catch (Exception e) {
            log.error("No se pudo eliminar el organizacion " + id, e);
            bindingResult.addError(new ObjectError("organizacion", new String[]{"organizacion.no.eliminada.message"}, null, null));
            return "admin/organizacion/ver";
        }

        return "redirect:/admin/organizacion";
    }

}
