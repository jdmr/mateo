/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.Constants;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.rh.model.Seccion;
import mx.edu.um.mateo.rh.model.Seccion;
import mx.edu.um.mateo.rh.service.SeccionManager;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
 * @author osoto
 */
@Controller
@RequestMapping("/rh/seccion")
public class SeccionController extends BaseController {

    @Autowired
    private SeccionManager seccionManager;

//    @SuppressWarnings("unchecked")
    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de secciones");
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
            params = seccionManager.getLista(params);
            try {
                generaReporte(tipo, (List<Seccion>) params.get(mx.edu.um.mateo.Constantes.CONTAINSKEY_SECCIONES), response, "secciones", mx.edu.um.mateo.general.utils.Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = seccionManager.getLista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Seccion>) params.get(mx.edu.um.mateo.Constantes.CONTAINSKEY_SECCIONES), request, "secciones", mx.edu.um.mateo.general.utils.Constantes.EMP, empresaId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("seccion.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = seccionManager.getLista(params);

        modelo.addAttribute(mx.edu.um.mateo.Constantes.CONTAINSKEY_SECCIONES, params.get(mx.edu.um.mateo.Constantes.CONTAINSKEY_SECCIONES));

        this.pagina(params, modelo, mx.edu.um.mateo.Constantes.CONTAINSKEY_SECCIONES, pagina);

        return mx.edu.um.mateo.Constantes.PATH_SECCION_LISTA;
    }


    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) throws ObjectRetrievalFailureException {
        log.debug("Mostrando proveedor {}", id);
        Seccion seccion = seccionManager.getSeccion(id);

         modelo.addAttribute(mx.edu.um.mateo.Constantes.ADDATTRIBUTE_SECCION, seccion);

        return mx.edu.um.mateo.Constantes.PATH_SECCION_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo seccion");
        Seccion seccion = new Seccion();
        modelo.addAttribute(mx.edu.um.mateo.Constantes.ADDATTRIBUTE_SECCION, seccion);
        return mx.edu.um.mateo.Constantes.PATH_SECCION_NUEVO;
    }

    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid Seccion seccion, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return mx.edu.um.mateo.Constantes.PATH_SECCION_NUEVO;
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            //seccion = seccionManager.crea(seccion, usuario);
            seccionManager.grabaSeccion(seccion);

            ambiente.actualizaSesion(request.getSession(), usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al seccion", e);
            /**
             * TODO CORREGIR MENSAJE DE ERROR
             */
            
            errors.rejectValue("nombre", "seccion.errors.creado", e.toString());
            return mx.edu.um.mateo.Constantes.PATH_SECCION_NUEVO;
        }

        redirectAttributes.addFlashAttribute("message", "seccion.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{seccion.getNombre()});

        return "redirect:" + mx.edu.um.mateo.Constantes.PATH_SECCION;
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, HttpServletRequest request, @Valid Seccion seccion, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        log.debug("Edita seccion {}", id);
        try {
            seccion = seccionManager.getSeccion(id);
        } catch (ObjectRetrievalFailureException ex) {
            log.error("No se pudo obtener al seccion", ex);
            errors.rejectValue("seccion", "registro.noEncontrado", new String[]{"seccion"}, null);
            return mx.edu.um.mateo.Constantes.PATH_SECCION;
        }
        modelo.addAttribute(mx.edu.um.mateo.Constantes.ADDATTRIBUTE_SECCION, seccion);
        return mx.edu.um.mateo.Constantes.PATH_SECCION_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id,
            Model modelo, @ModelAttribute Seccion seccion,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina seccion");
        try {
            seccionManager.removeSeccion(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
                    "seccion.eliminado.message");
            redirectAttributes.addFlashAttribute(
                    Constantes.CONTAINSKEY_MESSAGE_ATTRS,
                    new String[]{seccion.getNombre()});
        } catch (Exception e) {
            log.error("No se pudo eliminar la seccion " + id, e);
            bindingResult.addError(new ObjectError("seccion",
                    new String[]{"seccion.no.eliminado.message"},
                    null, null));
            return "/rh/seccion/ver";
        }

        return "redirect:" + "/rh/seccion";
    }
}
