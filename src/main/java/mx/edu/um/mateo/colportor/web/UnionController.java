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
import mx.edu.um.mateo.colportor.dao.UnionDao;
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
 * @author wilbert
 */
@Controller
@RequestMapping(Constantes.PATH_UNION)
public class UnionController extends BaseController {

    @Autowired
    private UnionDao unionDao;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de uniones");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = unionDao.lista(params);
            try {
                log.debug("Generando reportes por Union en " + tipo);
                generaReporte(tipo, (List<Union>) params.get(Constantes.CONTAINSKEY_UNIONES), response, Constantes.CONTAINSKEY_UNIONES, Constantes.ADMIN, null);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }
        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = unionDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<Union>) params.get(Constantes.CONTAINSKEY_UNIONES), request, Constantes.CONTAINSKEY_UNIONES, Constantes.ADMIN, null);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("union.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = unionDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_UNIONES, params.get(Constantes.CONTAINSKEY_UNIONES));

        this.pagina(params, modelo, Constantes.CONTAINSKEY_UNIONES, pagina);

        return Constantes.PATH_UNION_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando union {}", id);
        Union union = unionDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_UNION, union);

        return Constantes.PATH_UNION_VER;
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nueva union");
        Union union = new Union();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_UNION, union);
        return Constantes.PATH_UNION_NUEVA;
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Union union, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.PATH_UNION_NUEVA;
        }
        try {
            Usuario usuario = null;
            if (ambiente.obtieneUsuario() != null) {
                usuario = ambiente.obtieneUsuario();
            }
            union = unionDao.crea(union, usuario);

            ambiente.actualizaSesion(request.getSession(), usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al union", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return Constantes.PATH_UNION_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "union.creada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{union.getNombre()});

        return "redirect:" + Constantes.PATH_UNION_VER + "/" + union.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita union {}", id);
        Union union = unionDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_UNION, union);
        return Constantes.PATH_UNION_EDITA;
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Union union, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return Constantes.PATH_UNION_EDITA;
        }
        try {
            Usuario usuario = null;
            if (ambiente.obtieneUsuario() != null) {
                usuario = ambiente.obtieneUsuario();
            }
            if (union.getStatus() == "0") {
                union.setStatus(Constantes.STATUS_INACTIVO);
            } else {
                union.setStatus(Constantes.STATUS_ACTIVO);
            }
            union = unionDao.actualiza(union, usuario);
            ambiente.actualizaSesion(request.getSession(), usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al union", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return Constantes.PATH_UNION_EDITA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "union.actualizada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{union.getNombre()});

        return "redirect:" + Constantes.PATH_UNION_VER + "/" + union.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Union union, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina union");
        try {
            String nombre = unionDao.elimina(id);
            ambiente.actualizaSesion(request.getSession());

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "union.eliminada.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el union " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_UNION, new String[]{"union.no.eliminada.message"}, null, null));
            return Constantes.PATH_UNION_VER;
        }

        return "redirect:" + Constantes.PATH_UNION;
    }
}
