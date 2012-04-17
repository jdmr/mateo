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
package mx.edu.um.mateo.activos.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.activos.dao.TipoActivoDao;
import mx.edu.um.mateo.activos.model.TipoActivo;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
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
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/activoFijo/tipoActivo")
public class TipoActivoController extends BaseController {

    @Autowired
    private TipoActivoDao tipoActivoDao;

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
        log.debug("Mostrando lista de tipos de activos");
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
            params = tipoActivoDao.lista(params);
            try {
                generaReporte(tipo, (List<TipoActivo>) params.get("tiposDeActivo"), response, "tiposDeActivo", Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove("reporte");
                errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = tipoActivoDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<TipoActivo>) params.get("tiposDeActivo"), request, "tiposDeActivo", Constantes.EMP, empresaId);
                modelo.addAttribute("message", "lista.enviado.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("tipoActivo.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = tipoActivoDao.lista(params);
        modelo.addAttribute("tiposDeActivo", params.get("tiposDeActivo"));

        this.pagina(params, modelo, "tiposDeActivo", pagina);

        return "activoFijo/tipoActivo/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando tipoActivo {}", id);
        TipoActivo tipoActivo = tipoActivoDao.obtiene(id);

        modelo.addAttribute("tipoActivo", tipoActivo);

        return "activoFijo/tipoActivo/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo tipoActivo");
        TipoActivo tipoActivo = new TipoActivo();
        modelo.addAttribute("tipoActivo", tipoActivo);
        return "activoFijo/tipoActivo/nuevo";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid TipoActivo tipoActivo, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return "activoFijo/tipoActivo/nuevo";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            tipoActivo = tipoActivoDao.crea(tipoActivo, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al tipoActivo", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "activoFijo/tipoActivo/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "tipoActivo.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{tipoActivo.getNombre()});

        return "redirect:/activoFijo/tipoActivo/ver/" + tipoActivo.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita tipoActivo {}", id);
        TipoActivo tipoActivo = tipoActivoDao.obtiene(id);
        modelo.addAttribute("tipoActivo", tipoActivo);
        return "activoFijo/tipoActivo/edita";
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid TipoActivo tipoActivo, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "activoFijo/tipoActivo/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            tipoActivo = tipoActivoDao.actualiza(tipoActivo, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la tipoActivo", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "activoFijo/tipoActivo/edita";
        }

        redirectAttributes.addFlashAttribute("message", "tipoActivo.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{tipoActivo.getNombre()});

        return "redirect:/activoFijo/tipoActivo/ver/" + tipoActivo.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute TipoActivo tipoActivo, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina tipoActivo");
        try {
            String nombre = tipoActivoDao.elimina(id);

            redirectAttributes.addFlashAttribute("message", "tipoActivo.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la tipoActivo " + id, e);
            bindingResult.addError(new ObjectError("tipoActivo", new String[]{"tipoActivo.no.eliminado.message"}, null, null));
            return "activoFijo/tipoActivo/ver";
        }

        return "redirect:/activoFijo/tipoActivo";
    }
}
