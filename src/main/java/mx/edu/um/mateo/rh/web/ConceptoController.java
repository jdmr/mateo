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
package mx.edu.um.mateo.rh.web;

import mx.edu.um.mateo.inventario.web.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.model.Proveedor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inventario.dao.AlmacenDao;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.rh.model.Concepto;
import mx.edu.um.mateo.rh.service.ConceptoManager;
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
 * @author AMDA
 */
@Controller
@RequestMapping("/rh/concepto")
public class ConceptoController extends BaseController {

    @Autowired
    private ConceptoManager conceptoManager;

    public ConceptoController() {
        log.info("Se ha creado una nueva instancia de ConceptoController");
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
        log.debug("Mostrando lista de conceptos");
        Map<String, Object> params = new HashMap<>();
        Long empresaId = (Long) request.getSession().getAttribute("empresaId");
//        params.put("empresa", empresaId);
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
            params = conceptoManager.lista(params);
            try {
                generaReporte(tipo, (List<Concepto>) params.get(mx.edu.um.mateo.Constantes.CONCEPTO_LIST), response, "conceptos", Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = conceptoManager.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Concepto>) params.get(mx.edu.um.mateo.Constantes.CONCEPTO_LIST), request, "conceptos", Constantes.EMP, empresaId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("concepto.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = conceptoManager.lista(params);
        modelo.addAttribute(mx.edu.um.mateo.Constantes.CONCEPTO_LIST, params.get(mx.edu.um.mateo.Constantes.CONCEPTO_LIST));

        this.pagina(params, modelo, mx.edu.um.mateo.Constantes.CONCEPTO_LIST, pagina);

        return "rh/concepto/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) throws ObjectRetrievalFailureException {
        log.debug("Mostrando proveedor {}", id);
        Concepto concepto = conceptoManager.obtiene(id);

         modelo.addAttribute(mx.edu.um.mateo.Constantes.CONCEPTO_KEY, concepto);

        return "rh/concepto/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo concepto");
        Concepto concepto = new Concepto();
        modelo.addAttribute(mx.edu.um.mateo.Constantes.CONCEPTO_KEY, concepto);
        return "rh/concepto/nuevo";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Concepto concepto, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "rh/concepto/nuevo";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            //concepto = conceptoManager.crea(concepto, usuario);
            conceptoManager.graba(concepto);

            ambiente.actualizaSesion(request, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al concepto", e);
            /**
             * TODO CORREGIR MENSAJE DE ERROR
             */
            
            errors.rejectValue("nombre", "concepto.errors.creado", e.toString());
            return "rh/concepto/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "concepto.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{concepto.getNombre()});

        return "redirect:/rh/concepto/ver/" + concepto.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo, Errors errors) {
        log.debug("Edita concepto {}", id);
        Concepto concepto;
        try {
            concepto = conceptoManager.obtiene(id);
        } catch (ObjectRetrievalFailureException ex) {
            log.error("No se pudo obtener al concepto", ex);
            errors.rejectValue("concepto", "registro.noEncontrado", new String[]{"concepto"}, null);
            return "rh/concepto";
        }
        modelo.addAttribute(mx.edu.um.mateo.Constantes.CONCEPTO_KEY, concepto);
        return "rh/concepto/edita";
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Concepto concepto, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "rh/concepto/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            //concepto = conceptoManager.graba(concepto, usuario);
            conceptoManager.graba(concepto);

            ambiente.actualizaSesion(request, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el concepto", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "rh/concepto/edita";
        }

        redirectAttributes.addFlashAttribute("message", "concepto.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{concepto.getNombre()});

        return "redirect:/rh/concepto/ver/" + concepto.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Concepto concepto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina concepto");
        try {
            Long empresaId = (Long) request.getSession().getAttribute("empresaId");
            //String nombre = conceptoManager.elimina(id, empresaId);
             String nombre = conceptoManager.elimina(id);

            ambiente.actualizaSesion(request);

            redirectAttributes.addFlashAttribute("message", "concepto.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        }
         catch (Exception e) {
            log.error("No se pudo eliminar el concepto " + id, e);
            bindingResult.addError(new ObjectError("concepto", new String[]{"concepto.no.eliminado.message"}, null, null));
            return "rh/concepto/ver";
        }

        return "redirect:/rh/concepto";
    }
}
