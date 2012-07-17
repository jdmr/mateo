/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
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
package mx.edu.um.mateo.contabilidad.web;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.contabilidad.dao.EjercicioDao;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.contabilidad.model.EjercicioPK;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.*;
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
@RequestMapping("/contabilidad/ejercicio")
public class EjercicioController extends BaseController {

    @Autowired
    private EjercicioDao ejercicioDao;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de ejercicios");
        Map<String, Object> params = this.convierteParams(request.getParameterMap());
        Long organizacionId = (Long) request.getSession().getAttribute("organizacionId");
        params.put("organizacion", organizacionId);
        
        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = ejercicioDao.lista(params);
            try {
                generaReporte(tipo, (List<Ejercicio>) params.get("ejercicios"), response, "ejercicios", Constantes.ORG, organizacionId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = ejercicioDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Ejercicio>) params.get("ejercicios"), request, "ejercicios", Constantes.ORG, organizacionId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("ejercicio.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = ejercicioDao.lista(params);
        modelo.addAttribute("ejercicios", params.get("ejercicios"));

        this.pagina(params, modelo, "ejercicios", pagina);

        return "contabilidad/ejercicio/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable String id, Model modelo) {
        log.debug("Mostrando ejercicio {}", id);
        EjercicioPK key = new EjercicioPK(id, ambiente.obtieneUsuario().getEmpresa().getOrganizacion());
        Ejercicio ejercicio = ejercicioDao.obtiene(key);

        modelo.addAttribute("ejercicio", ejercicio);

        return "contabilidad/ejercicio/ver";
    }

    @RequestMapping("/nuevo")
    public String nueva(Model modelo) {
        log.debug("Nueva ejercicio");
        Ejercicio ejercicio = new Ejercicio();
        modelo.addAttribute("ejercicio", ejercicio);
        return "contabilidad/ejercicio/nuevo";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Ejercicio ejercicio, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "admin/ejercicio/nueva";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            ejercicio = ejercicioDao.crea(ejercicio, usuario);

            ambiente.actualizaSesion(request, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al ejercicio", e);
            errors.rejectValue("id", "campo.duplicado.message", new String[]{"id"}, null);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "contabilidad/ejercicio/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "ejercicio.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{ejercicio.getNombre()});

        return "redirect:/contabilidad/ejercicio/ver/" + ejercicio.getId().getIdEjercicio();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable String id, Model modelo) {
        log.debug("Edita ejercicio {}", id);
        EjercicioPK key = new EjercicioPK(id, ambiente.obtieneUsuario().getEmpresa().getOrganizacion());
        Ejercicio ejercicio = ejercicioDao.obtiene(key);
        modelo.addAttribute("ejercicio", ejercicio);
        return "contabilidad/ejercicio/edita";
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Ejercicio ejercicio, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "contabilidad/ejercicio/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            ejercicio = ejercicioDao.actualiza(ejercicio, usuario);

            ambiente.actualizaSesion(request, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la ejercicio", e);
            errors.rejectValue("id", "campo.duplicado.message", new String[]{"id"}, null);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "contabilidad/ejercicio/edita";
        }

        redirectAttributes.addFlashAttribute("message", "ejercicio.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{ejercicio.getNombre()});

        return "redirect:/contabilidad/ejercicio/ver/" + ejercicio.getId().getIdEjercicio();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam String id, Model modelo, @ModelAttribute Ejercicio ejercicio, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina ejercicio");
        try {
            EjercicioPK key = new EjercicioPK(id, ambiente.obtieneUsuario().getEmpresa().getOrganizacion());
            String nombre = ejercicioDao.elimina(key);

            ambiente.actualizaSesion(request);

            redirectAttributes.addFlashAttribute("message", "ejercicio.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la ejercicio " + id, e);
            bindingResult.addError(new ObjectError("ejercicio", new String[]{"ejercicio.no.eliminado.message"}, null, null));
            return "contabilidad/ejercicio/ver/"+id;
        }

        return "redirect:/contabilidad/ejercicio";
    }

}
