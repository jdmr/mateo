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
package mx.edu.um.mateo.activos.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.activos.dao.ActivoDao;
import mx.edu.um.mateo.activos.dao.TipoActivoDao;
import mx.edu.um.mateo.activos.model.Activo;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
@RequestMapping("/activoFijo/activo")
public class ActivoController extends BaseController {

    @Autowired
    private ActivoDao activoDao;
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
            Model modelo) throws ParseException {
        log.debug("Mostrando lista de activos");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Map<String, Object> params = this.convierteParams(request.getParameterMap());
        Long empresaId = (Long) request.getSession().getAttribute("empresaId");
        params.put("empresa", empresaId);

        if (params.containsKey("fechaIniciado")) {
            log.debug("FechaIniciado: {}", params.get("fechaIniciado"));
            params.put("fechaIniciado", sdf.parse((String) params.get("fechaIniciado")));
        }

        if (params.containsKey("fechaTerminado")) {
            params.put("fechaTerminado", sdf.parse((String) params.get("fechaTerminado")));
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = activoDao.lista(params);
            try {
                generaReporte(tipo, (List<Activo>) params.get("activos"), response, "activos", Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = activoDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Activo>) params.get("activos"), request, "activos", Constantes.EMP, empresaId);
                modelo.addAttribute("message", "lista.enviado.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("activo.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = activoDao.lista(params);
        modelo.addAttribute("activos", params.get("activos"));
        modelo.addAttribute("resumen", params.get("resumen"));

        this.pagina(params, modelo, "activos", pagina);

        return "activoFijo/activo/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando activo {}", id);
        Activo activo = activoDao.obtiene(id);

        modelo.addAttribute("activo", activo);

        return "activoFijo/activo/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo activo");
        Activo activo = new Activo();
        modelo.addAttribute("activo", activo);

        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession().getAttribute("empresaId"));
        params.put("reporte", true);
        params = tipoActivoDao.lista(params);
        modelo.addAttribute("tiposDeActivo", params.get("tiposDeActivo"));

        return "activoFijo/activo/nuevo";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Activo activo, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");

            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession().getAttribute("empresaId"));
            params.put("reporte", true);
            params = tipoActivoDao.lista(params);
            modelo.addAttribute("tiposDeActivo", params.get("tiposDeActivo"));

            return "activoFijo/activo/nuevo";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            log.debug("TipoActivo: {}", activo.getTipoActivo().getId());
            activo = activoDao.crea(activo, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al activo", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);

            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession().getAttribute("empresaId"));
            params.put("reporte", true);
            params = tipoActivoDao.lista(params);
            modelo.addAttribute("tiposDeActivo", params.get("tiposDeActivo"));

            return "activoFijo/activo/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "activo.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{activo.getFolio()});

        return "redirect:/activoFijo/activo/ver/" + activo.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable Long id, Model modelo) {
        log.debug("Edita activo {}", id);
        Activo activo = activoDao.obtiene(id);
        modelo.addAttribute("activo", activo);

        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession().getAttribute("empresaId"));
        params.put("reporte", true);
        params = tipoActivoDao.lista(params);
        modelo.addAttribute("tiposDeActivo", params.get("tiposDeActivo"));

        return "activoFijo/activo/edita";
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Activo activo, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");

            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession().getAttribute("empresaId"));
            params.put("reporte", true);
            params = tipoActivoDao.lista(params);
            modelo.addAttribute("tiposDeActivo", params.get("tiposDeActivo"));

            return "activoFijo/activo/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            activo = activoDao.actualiza(activo, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la activo", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);

            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession().getAttribute("empresaId"));
            params.put("reporte", true);
            params = tipoActivoDao.lista(params);
            modelo.addAttribute("tiposDeActivo", params.get("tiposDeActivo"));

            return "activoFijo/activo/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "activo.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{activo.getFolio()});

        return "redirect:/activoFijo/activo/ver/" + activo.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Activo activo, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina activo");
        try {
//            String nombre = activoDao.baja(id);
//
//            redirectAttributes.addFlashAttribute("message", "activo.eliminado.message");
//            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la activo " + id, e);
            bindingResult.addError(new ObjectError("activo", new String[]{"activo.no.eliminado.message"}, null, null));
            return "activoFijo/activo/ver";
        }

        return "redirect:/activoFijo/activo";
    }
}
