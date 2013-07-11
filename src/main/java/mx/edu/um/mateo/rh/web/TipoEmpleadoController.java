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
package mx.edu.um.mateo.rh.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import mx.edu.um.mateo.general.model.Cliente;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.rh.model.TipoEmpleado;
import mx.edu.um.mateo.rh.service.TipoEmpleadoManager;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Omar Soto <osoto@um.edu.mx>
 */
@Controller
@RequestMapping(Constantes.PATH_TIPOEMPLEADO)
public class TipoEmpleadoController extends BaseController {

    @Autowired
    private TipoEmpleadoManager tipoEmpleadoMgr;

    @SuppressWarnings("unchecked")
    @RequestMapping
    public String lista(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort, Model modelo) {
        log.debug("Mostrando lista de tiposEmpleados");
        Map<String, Object> params = new HashMap<>();
        Long organizacionId = (Long) request.getSession().getAttribute("organizacionId");
        params.put("organizacion", organizacionId);
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
            params = tipoEmpleadoMgr.lista(params);
            try {
                generaReporte(tipo, (List<Cliente>) params.get(Constantes.TIPOEMPLEADO_LIST),
                        response, "tiposEmpleado", Constantes.EMP, organizacionId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = tipoEmpleadoMgr.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Cliente>) params.get(Constantes.TIPOEMPLEADO_LIST),
                        request, "tiposEmpleado", Constantes.EMP, organizacionId);
                modelo.addAttribute("message", "lista.enviado.message");
                modelo.addAttribute(
                        "messageAttrs",
                        new String[]{
                            messageSource.getMessage("tiposEmpleado.lista.label",
                            null, request.getLocale()),
                            ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = tipoEmpleadoMgr.lista(params);
        modelo.addAttribute(Constantes.TIPOEMPLEADO_LIST, params.get(Constantes.TIPOEMPLEADO_LIST));

        this.pagina(params, modelo, Constantes.TIPOEMPLEADO_LIST, pagina);

        return Constantes.PATH_TIPOEMPLEADO_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando tipoEmpleado {}", id);
        TipoEmpleado tipoEmpleado = tipoEmpleadoMgr.obtiene(id);

        modelo.addAttribute(Constantes.TIPOEMPLEADO_KEY, tipoEmpleado);

        return Constantes.PATH_TIPOEMPLEADO_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo tipoEmpleado");
        TipoEmpleado tipoEmpleado = new TipoEmpleado();
        modelo.addAttribute("tipoEmpleado", tipoEmpleado);

        return Constantes.PATH_TIPOEMPLEADO_NUEVO;
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request,
            HttpServletResponse response, @Valid TipoEmpleado tipoEmpleado,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");

            return Constantes.PATH_TIPOEMPLEADO_NUEVO;
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            log.debug("TipoCliente: {}", tipoEmpleado.getId());
            tipoEmpleado = tipoEmpleadoMgr.crea(tipoEmpleado, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al tipo empleado", e);
            errors.rejectValue("descripcion", "campo.duplicado.message",
                    new String[]{"descripcion"}, null);

            return Constantes.PATH_TIPOEMPLEADO_NUEVO;
        }

        redirectAttributes.addFlashAttribute("message",
                "cliente.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{tipoEmpleado.getDescripcion()});

        return "redirect:" + Constantes.PATH_TIPOEMPLEADO_VER + "/" + tipoEmpleado.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable Long id,
            Model modelo) {
        log.debug("Edita tipoEmpleado {}", id);
        TipoEmpleado tipoEmpleado = tipoEmpleadoMgr.obtiene(id);
        modelo.addAttribute("tipoEmpleado", tipoEmpleado);

        return Constantes.PATH_TIPOEMPLEADO_EDITA;
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid TipoEmpleado tipoEmpleado,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");

            return Constantes.PATH_TIPOEMPLEADO_EDITA;
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            tipoEmpleado = tipoEmpleadoMgr.crea(tipoEmpleado, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo actualizar el tipo de empleado", e);
            errors.rejectValue("descripcion", "campo.duplicado.message",
                    new String[]{"descripcion"}, null);

            return Constantes.PATH_TIPOEMPLEADO_NUEVO;
        }

        redirectAttributes.addFlashAttribute("message",
                "cliente.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{tipoEmpleado.getDescripcion()});

        return "redirect:" + Constantes.PATH_TIPOEMPLEADO_VER + "/" + tipoEmpleado.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id,
            Model modelo, @ModelAttribute TipoEmpleado tipoEmpleado,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina tipo de empleado");
        try {
            String nombre = tipoEmpleadoMgr.elimina(id);

            redirectAttributes.addFlashAttribute("message",
                    "cliente.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el tipo de empleado " + id, e);
            bindingResult
                    .addError(new ObjectError("tipoEmpleado",
                    new String[]{"tipoEmpleado.no.eliminado.message"},
                    null, null));

            return Constantes.PATH_TIPOEMPLEADO_EDITA;
        }

        return "redirect:" + Constantes.PATH_TIPOEMPLEADO;
    }
}
