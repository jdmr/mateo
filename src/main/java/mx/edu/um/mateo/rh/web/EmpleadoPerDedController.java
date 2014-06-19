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
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.LabelValueBean;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.nomina.model.EnumFrecuenciaPago;
import mx.edu.um.mateo.nomina.model.PerDed;
import mx.edu.um.mateo.nomina.service.PerDedManager;
import mx.edu.um.mateo.rh.model.Empleado;
import mx.edu.um.mateo.rh.model.EmpleadoPerDed;
import mx.edu.um.mateo.rh.service.EmpleadoPerDedManager;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
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
 * @author semdariobarbaamaya
 */
@Controller
@RequestMapping(Constantes.PATH_EMPLEADOPERDED)
public class EmpleadoPerDedController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(mx.edu.um.mateo.nomina.web.PerDedController.class);
    @Autowired
    private PerDedManager pdManager;
    @Autowired
    private EmpleadoPerDedManager empleadoPDManager;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private Ambiente ambiente;

    @RequestMapping({"", "/lista"})
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
        log.debug("Mostrando lista de perdeds");
        
        Map<String, Object> params = new HashMap<>();
        Long empresaId = (Long) request.getSession().getAttribute("empresaId");
        params.put("empresa", empresaId);
        params.put("empleadoId", ((Empleado)request.getSession().getAttribute(Constantes.EMPLEADO_KEY)).getId());
        
        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (pagina != null) {
            params.put(Constantes.CONTAINSKEY_PAGINA, pagina);
            modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        } else {
            pagina = 1L;
            modelo.addAttribute(Constantes.CONTAINSKEY_PAGINA, pagina);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = empleadoPDManager.lista(params);
            try {
                generaReporte(tipo, (List<PerDed>) params.get(Constantes.EMPLEADOPERDED_LIST), response, "empleadoPerDed", Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = empleadoPDManager.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<PerDed>) params.get(Constantes.EMPLEADOPERDED_LIST), request, "empleadoPerDed", Constantes.EMP, empresaId);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("empleadoPerDed.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = empleadoPDManager.lista(params);
        log.debug("params{}", params.get(Constantes.EMPLEADOPERDED_LIST));
        modelo.addAttribute(Constantes.EMPLEADOPERDED_LIST, params.get(Constantes.EMPLEADOPERDED_LIST));

        this.pagina(params, modelo, Constantes.EMPLEADOPERDED_LIST, pagina);

        return Constantes.PATH_EMPLEADOPERDED_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable String id, Model modelo) {
        log.debug("Mostrando perded {}", id);
        EmpleadoPerDed perded = empleadoPDManager.obtiene(id);

        modelo.addAttribute(Constantes.EMPLEADOPERDED_KEY, perded);

        return Constantes.PATH_EMPLEADOPERDED_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo perded");
        EmpleadoPerDed perded = new EmpleadoPerDed();
        modelo.addAttribute(Constantes.EMPLEADOPERDED_KEY, perded);
        modelo.addAttribute(Constantes.FRECUENCIAPAGO_LIST, EnumFrecuenciaPago.values());
        return Constantes.PATH_EMPLEADOPERDED_NUEVO;
    }

    @Transactional
    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid EmpleadoPerDed perded, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            despliegaBindingResultErrors(bindingResult);
            perded = new EmpleadoPerDed();
            modelo.addAttribute(Constantes.EMPLEADOPERDED_KEY, perded);
            return Constantes.PATH_EMPLEADOPERDED_NUEVO;
        }

        try {
            perded.setPerDed(pdManager.obtiene(perded.getPerDed().getId().toString()));
            perded.setEmpleado((Empleado)request.getSession().getAttribute(Constantes.EMPLEADO));
            empleadoPDManager.graba(perded);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo grabar perded", e);
            perded = new EmpleadoPerDed();
            modelo.addAttribute(Constantes.EMPLEADOPERDED_KEY, perded);
            return Constantes.PATH_EMPLEADOPERDED_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "empleadoPerDed.graba.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{perded.getPerDed().getNombre()});

        return "redirect:" + Constantes.PATH_EMPLEADOPERDED_LISTA + "/";
    }

    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable String id, Model modelo) {
        log.debug("Editar cuenta de perded {}", id);
        EmpleadoPerDed perded = empleadoPDManager.obtiene(id);
        modelo.addAttribute(Constantes.EMPLEADOPERDED_KEY, perded);
        modelo.addAttribute(Constantes.FRECUENCIAPAGO_LIST, EnumFrecuenciaPago.values());
        
        //obtener perded
        Map<String, Object> params = new HashMap<>();
        Long empresaId = (Long) request.getSession().getAttribute("empresaId");
        params.put("empresa", empresaId);        
        params.put("reporte", "");        
        
        params = pdManager.getPerDedList(params);
        List <LabelValueBean> rValues = (List<LabelValueBean>)params.get(Constantes.PERDED_LIST);
        for(LabelValueBean b : rValues){
            if(b.getId().equals(perded.getPerDed().getId())){
                modelo.addAttribute(Constantes.PERDED_KEY, b.getLabel());
            }
        }
        
        return Constantes.PATH_EMPLEADOPERDED_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute EmpleadoPerDed perded, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina cuenta de perded");
        try {
            empleadoPDManager.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "empleadoPerDed.elimina.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{perded.getPerDed().getNombre()});
        } catch (Exception e) {
            log.error("No se pudo eliminar perded " + id, e);
            bindingResult.addError(new ObjectError(Constantes.EMPLEADOPERDED_KEY, new String[]{"empleadoPerDed.no.elimina.message"}, null, null));
            return Constantes.PATH_EMPLEADOPERDED_VER;
        }

        return "redirect:" + Constantes.PATH_EMPLEADOPERDED_LISTA;
    }

}
