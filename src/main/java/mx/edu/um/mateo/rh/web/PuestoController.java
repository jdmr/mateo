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
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.rh.model.Puesto;
import mx.edu.um.mateo.rh.model.Seccion;
import mx.edu.um.mateo.rh.service.PuestoManager;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author osoto
 */
@Controller
@RequestMapping("/rh/catalogo/puestos")
public class PuestoController extends BaseController {

    @Autowired
    private PuestoManager mgr;
    @Autowired
    private SeccionManager SeccionManager;

    @SuppressWarnings("unchecked")
    @RequestMapping
    public String lista(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort, Usuario usuario,
            Errors errors, Model modelo) {
        log.debug("Mostrando puestos {}");

        Map<String, Object> params = new HashMap<>();

        Long empresaId = (Long) request.getSession().getAttribute(
                "empresaId");
        params.put("empresa", empresaId);
//
//        Long seccionId = (Long) request.getSession().getAttribute(
//                "seccionId");
//        params.put("seccion", seccionId);

        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (pagina != null) {
            params.put("pagina", pagina);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = mgr.lista(params);
            try {
                generaReporte(tipo,
                        (List<Puesto>) params.get(Constantes.PUESTO_LIST),
                        response, Constantes.PUESTO_LIST,
                        Constantes.ORG,
                        empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                // errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = mgr.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo,
                        (List<CuentaMayor>) params.get(Constantes.PUESTO_LIST),
                        request, Constantes.PUESTO_LIST,
                        Constantes.ORG,
                        empresaId);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE,
                        "lista.enviada.message");
                modelo.addAttribute(
                        Constantes.CONTAINSKEY_MESSAGE_ATTRS,
                        new String[]{
                            messageSource.getMessage("puesto.lista.label",
                                    null, request.getLocale()),
                            ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }

        params = mgr.lista(params);

        modelo.addAttribute(Constantes.PUESTO_LIST, params.get(Constantes.PUESTO_LIST));

        this.pagina(params, modelo, Constantes.PUESTO_LIST, pagina);

        log.debug("params {}", params.toString());
        return "/rh/catalogo/puestos/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando puesto {}", id);

        Puesto puesto = mgr.obtiene(id);

        modelo.addAttribute(Constantes.PUESTO_KEY, puesto);

        return "/rh/catalogo/puestos/ver";
    }

    @RequestMapping("/nuevo")
    public String nueva(Model modelo) {
        log.debug("Nuevo puesto");
        Map<String, Object> params = new HashMap<>();
        Puesto puesto = new Puesto();
        modelo.addAttribute(Constantes.PUESTO_KEY, puesto);
        params = SeccionManager.Lista(params);
        //modelo.addAttribute(Constantes.SECCION_LIST, params.get(Constantes.SECCION_LIST));
        List<Seccion> listaSeccion = (List) params.get(Constantes.CONTAINSKEY_SECCIONES);
        log.debug("Secciones***" + listaSeccion.size());
        modelo.addAttribute(Constantes.CONTAINSKEY_SECCIONES, listaSeccion);
        return "/rh/catalogo/puestos/nuevo";

    }

    @Transactional
    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request,
            HttpServletResponse response, @Valid Puesto puesto,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes) throws ObjectRetrievalFailureException {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            Map<String, Object> params = new HashMap<>();
            params = SeccionManager.Lista(params);
            //modelo.addAttribute(Constantes.SECCION_LIST, params.get(Constantes.SECCION_LIST));
            List<Seccion> listaSeccion = (List) params.get(Constantes.CONTAINSKEY_SECCIONES);
            log.debug("Secciones***" + listaSeccion.size());
            modelo.addAttribute(Constantes.CONTAINSKEY_SECCIONES, listaSeccion);

            return "/rh/catalogos/puestos/nuevo";
        }

        try {
            Map<String, Object> params = new HashMap<>();
            params = SeccionManager.Lista(params);
            //modelo.addAttribute(Constantes.SECCION_LIST, params.get(Constantes.SECCION_LIST));
            List<Seccion> listaSeccion = (List) params.get(Constantes.CONTAINSKEY_SECCIONES);
            log.debug("Secciones***" + listaSeccion.size());
            modelo.addAttribute(Constantes.CONTAINSKEY_SECCIONES, listaSeccion);
            puesto.setSeccion(SeccionManager.Obtiene(puesto.getSeccion().getId()));
            Usuario usuario = ambiente.obtieneUsuario();
            mgr.graba(puesto, usuario);

        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el puesto", e);
            return "/rh/catalogo/puestos/nuevo";
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
                "puesto.creado.message");
        redirectAttributes.addFlashAttribute(
                Constantes.CONTAINSKEY_MESSAGE_ATTRS,
                new String[]{puesto.getDescripcion()});

        return "redirect:" + "/rh/catalogo/puestos";
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar el puesto {}", id);
        Map<String, Object> params = new HashMap<>();
        params = SeccionManager.Lista(params);
        //modelo.addAttribute(Constantes.SECCION_LIST, params.get(Constantes.SECCION_LIST));
        List<Seccion> listaSeccion = (List) params.get(Constantes.CONTAINSKEY_SECCIONES);
        log.debug("Secciones***" + listaSeccion.size());
        modelo.addAttribute(Constantes.CONTAINSKEY_SECCIONES, listaSeccion);
        Puesto puesto = mgr.obtiene(id);

        modelo.addAttribute(Constantes.PUESTO_KEY, puesto);

        return "/rh/catalogo/puestos/edita";
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id,
            Model modelo, @ModelAttribute Puesto puesto,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina puesto");
        try {
            mgr.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
                    "puesto.eliminado.message");
            redirectAttributes.addFlashAttribute(
                    Constantes.CONTAINSKEY_MESSAGE_ATTRS,
                    new String[]{puesto.getDescripcion()});
        } catch (Exception e) {
            log.error("No se pudo eliminar el puesto " + id, e);
            bindingResult.addError(new ObjectError("puesto",
                    new String[]{"puesto.no.eliminado.message"},
                    null, null));
            return "/rh/puestos/ver";
        }

        return "redirect:" + "/rh/catalogo/puestos";
    }
}
