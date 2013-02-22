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
package mx.edu.um.mateo.colportor.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.AsociacionDao;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
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
 * @author gibrandemetrioo
 */
@Controller
@RequestMapping(Constantes.PATH_ASOCIACION)
public class AsociacionController extends BaseController {

    @Autowired
    private AsociacionDao asociacionDao;

    public AsociacionController() {
        log.info("Se ha creado una nueva instancia de AsociacionController");
    }

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de Asociaciones");
        Map<String, Object> params = new HashMap<>();
        Long unionId = (Long) request.getSession().getAttribute(Constantes.SESSION_UNION);
        params.put(Constantes.ADDATTRIBUTE_UNION, unionId);
        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = asociacionDao.lista(params);
            try {
                generaReporte(tipo, (List<Asociacion>) params.get(Constantes.CONTAINSKEY_ASOCIACIONES), response, Constantes.CONTAINSKEY_ASOCIACIONES, Constantes.UNI, null);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }
        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = asociacionDao.lista(params);
            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<Asociacion>) params.get(Constantes.CONTAINSKEY_ASOCIACIONES), request, Constantes.CONTAINSKEY_ASOCIACIONES, Constantes.UNI, null);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("asociacion.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = asociacionDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_ASOCIACIONES, params.get(Constantes.CONTAINSKEY_ASOCIACIONES));

        this.pagina(params, modelo, Constantes.CONTAINSKEY_ASOCIACIONES, pagina);

        return Constantes.PATH_ASOCIACION_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando Asociacion {}", id);
        Asociacion asociacion = asociacionDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ASOCIACION, asociacion);
        return Constantes.PATH_ASOCIACION_VER;
    }

    @RequestMapping("/nueva")
    public String nuevo(Model modelo) {
        log.debug("Nueva Asociacion");
        Asociacion asociacion = new Asociacion();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ASOCIACION, asociacion);
        return Constantes.PATH_ASOCIACION_NUEVA;
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Asociacion asociacion, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return Constantes.PATH_ASOCIACION_NUEVA;
        }
        try {
            Usuario usuario = null;
            if (ambiente.obtieneUsuario() != null) {
                usuario = ambiente.obtieneUsuario();
            }
            asociacion = asociacionDao.crea(asociacion, usuario);
            ambiente.actualizaSesion(request.getSession(), usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al Asociacion", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return Constantes.PATH_ASOCIACION_NUEVA;
        }
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "asociacion.creada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{asociacion.getNombre()});
        return "redirect:" + Constantes.PATH_ASOCIACION_VER + "/" + asociacion.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita Asociacion {}", id);
        Asociacion asociacion = asociacionDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_ASOCIACION, asociacion);
        return Constantes.PATH_ASOCIACION_EDITA;
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Asociacion asociacion, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return Constantes.PATH_ASOCIACION_EDITA;
        }
        try {
            Usuario usuario = null;
            if (ambiente.obtieneUsuario() != null) {
                usuario = ambiente.obtieneUsuario();
            }
            if (asociacion.getStatus() == "0") {
                asociacion.setStatus(Constantes.STATUS_INACTIVO);
            } else {
                asociacion.setStatus(Constantes.STATUS_ACTIVO);
            }
            asociacion = asociacionDao.actualiza(asociacion, usuario);
            ambiente.actualizaSesion(request.getSession(), usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la Asociacion", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return Constantes.PATH_ASOCIACION_EDITA;
        }
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "asociacion.actualizada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{asociacion.getNombre()});
        return "redirect:" + Constantes.PATH_ASOCIACION_VER + "/" + asociacion.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Asociacion Asociacion, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina Asociacion");
        try {
            Long unionId = ((Union) request.getSession().getAttribute(Constantes.SESSION_UNION)).getId();
            String nombre = asociacionDao.elimina(id, unionId);
            ambiente.actualizaSesion(request.getSession());
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "asociacion.eliminada.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la Asociacion " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_ASOCIACION, new String[]{"asociacion.no.eliminada.message"}, null, null));
            return Constantes.PATH_ASOCIACION_VER;
        }
        return "redirect:" + Constantes.PATH_ASOCIACION;
    }
}
