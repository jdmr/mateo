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
import mx.edu.um.mateo.rh.model.Colegio;
import mx.edu.um.mateo.rh.service.ColegioManager;
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
@RequestMapping("/rh/colegio")
public class ColegioController extends BaseController {

    @Autowired
    private ColegioManager colegioManager;

    public ColegioController() {
        log.info("Se ha creado una nueva instancia de ColegioController");
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
        log.debug("Mostrando lista de colegios");
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
            params = colegioManager.lista(params);
            try {
                generaReporte(tipo, (List<Colegio>) params.get(mx.edu.um.mateo.Constantes.CONTAINSKEY_COLEGIOS), response, "colegios", Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = colegioManager.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Colegio>) params.get(mx.edu.um.mateo.Constantes.CONTAINSKEY_COLEGIOS), request, "colegios", Constantes.EMP, empresaId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("colegio.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = colegioManager.lista(params);
        modelo.addAttribute(mx.edu.um.mateo.Constantes.CONTAINSKEY_COLEGIOS, params.get(mx.edu.um.mateo.Constantes.CONTAINSKEY_COLEGIOS));

        this.pagina(params, modelo, mx.edu.um.mateo.Constantes.CONTAINSKEY_COLEGIOS, pagina);

        return mx.edu.um.mateo.Constantes.PATH_COLEGIO_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) throws ObjectRetrievalFailureException {
        log.debug("Mostrando proveedor {}", id);
        Colegio colegio = colegioManager.obtiene(id);

         modelo.addAttribute(mx.edu.um.mateo.Constantes.ADDATTRIBUTE_COLEGIO, colegio);

        return mx.edu.um.mateo.Constantes.PATH_COLEGIO_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo colegio");
        Colegio colegio = new Colegio();
        modelo.addAttribute(mx.edu.um.mateo.Constantes.ADDATTRIBUTE_COLEGIO, colegio);
        return mx.edu.um.mateo.Constantes.PATH_COLEGIO_NUEVO;
    }

    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid Colegio colegio, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return mx.edu.um.mateo.Constantes.PATH_COLEGIO_NUEVO;
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            //colegio = colegioManager.crea(colegio, usuario);
            colegioManager.saveColegio(colegio);

            ambiente.actualizaSesion(request.getSession(), usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al colegio", e);
            /**
             * TODO CORREGIR MENSAJE DE ERROR
             */
            
            errors.rejectValue("nombre", "colegio.errors.creado", e.toString());
            return mx.edu.um.mateo.Constantes.PATH_COLEGIO_NUEVO;
        }

        redirectAttributes.addFlashAttribute("message", "colegio.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{colegio.getNombre()});

        return "redirect:" + mx.edu.um.mateo.Constantes.PATH_COLEGIO;
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, HttpServletRequest request, @Valid Colegio colegio, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        log.debug("Edita colegio {}", id);
        try {
            colegio = colegioManager.obtiene(id);
        } catch (ObjectRetrievalFailureException ex) {
            log.error("No se pudo obtener al colegio", ex);
            errors.rejectValue("colegio", "registro.noEncontrado", new String[]{"colegio"}, null);
            return mx.edu.um.mateo.Constantes.PATH_COLEGIO;
        }
        modelo.addAttribute(mx.edu.um.mateo.Constantes.ADDATTRIBUTE_COLEGIO, colegio);
        return mx.edu.um.mateo.Constantes.PATH_COLEGIO_EDITA;
    }

//    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
//    public String actualiza(HttpServletRequest request, @Valid Colegio colegio, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
//        if (bindingResult.hasErrors()) {
//            log.error("Hubo algun error en la forma, regresando");
//            for (ObjectError error : bindingResult.getAllErrors()) {
//                log.debug("Error: {}", error);
//            }
//            return "rh/colegio/edita";
//        }
//
//        try {
//            Usuario usuario = ambiente.obtieneUsuario();
//            //colegio = colegioManager.saveColegio(colegio, usuario);
//            colegioManager.updateColegio(colegio);
//           
//            ambiente.actualizaSesion(request.getSession(), usuario);
//        } catch (ConstraintViolationException e) {
//            log.error("No se pudo crear el colegio", e);
//            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
//            return "rh/colegio/edita";
//        }
//
//        redirectAttributes.addFlashAttribute("message", "colegio.actualizado.message");
//        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{colegio.getNombre()});
//
//        return "redirect:/rh/colegio/ver/" + colegio.getId();
//    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Colegio colegio, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina colegio");
        try {
            Long empresaId = (Long) request.getSession().getAttribute("empresaId");
            String nombre = colegioManager.removeColegio(id);

            ambiente.actualizaSesion(request.getSession());

            redirectAttributes.addFlashAttribute("message", "colegio.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        }
         catch (Exception e) {
            log.error("No se pudo eliminar el colegio " + id, e);
            bindingResult.addError(new ObjectError("colegio", new String[]{"colegio.no.eliminado.message"}, null, null));
            return mx.edu.um.mateo.Constantes.PATH_COLEGIO_VER;
        }

        return "redirect:" + mx.edu.um.mateo.Constantes.PATH_COLEGIO;
    }
}
