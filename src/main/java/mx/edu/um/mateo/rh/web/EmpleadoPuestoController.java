package mx.edu.um.mateo.rh.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.rh.model.Empleado;
import mx.edu.um.mateo.rh.model.EmpleadoPuesto;
import mx.edu.um.mateo.rh.service.EmpleadoPuestoManager;
import mx.edu.um.mateo.rh.service.PuestoManager;
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
 * @author semdariobarbaamaya
 */
@Controller
@RequestMapping("/rh/empleado/empleadoPuesto")
public class EmpleadoPuestoController extends BaseController {

    @Autowired
    private EmpleadoPuestoManager empleadoPuestoManager;
    @Autowired
    private PuestoManager puestoManager;

    public EmpleadoPuestoController() {
        log.info("Se ha creado una nueva instancia de EmpleadoPuestoController");
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
        log.debug("Mostrando lista de empleadoPuesto");
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
            params = empleadoPuestoManager.lista(params);
            try {
                generaReporte(tipo, (List<EmpleadoPuesto>) params.get(Constantes.EMPLEADOPUESTO_LIST), response, "empleadoPuesto", Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = empleadoPuestoManager.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<EmpleadoPuesto>) params.get(Constantes.EMPLEADOPUESTO_LIST), request, "empleadoPuesto", Constantes.EMP, empresaId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("empleadoPuesto.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = empleadoPuestoManager.lista(params);
        modelo.addAttribute(Constantes.EMPLEADOPUESTO_LIST, params.get(Constantes.EMPLEADOPUESTO_LIST));

        this.pagina(params, modelo, Constantes.EMPLEADOPUESTO_LIST, pagina);

        return Constantes.PATH_EMPLEADOPUESTO_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(HttpServletRequest request, @PathVariable Long id, Model modelo) throws ObjectRetrievalFailureException {
        log.debug("Mostrando empleadoPuesto {}", id);
        EmpleadoPuesto empleadoPuesto = empleadoPuestoManager.obtiene(id);
        modelo.addAttribute(Constantes.EMPLEADOPUESTO_KEY, empleadoPuesto);

        request.getSession().setAttribute(Constantes.EMPLEADOPUESTO_KEY, empleadoPuesto);

        return Constantes.PATH_EMPLEADOPUESTO_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo, HttpServletRequest request) {
        log.debug("Nuevo empleadoPuesto");
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession().getAttribute("empresaId"));
        modelo.addAttribute(Constantes.PUESTO_LIST, puestoManager.lista(params).get(Constantes.PUESTO_LIST));
        EmpleadoPuesto empleadoPuesto = new EmpleadoPuesto();
        modelo.addAttribute(Constantes.EMPLEADOPUESTO_KEY, empleadoPuesto);
        return Constantes.PATH_EMPLEADOPUESTO_NUEVO;
    }

    @RequestMapping(value = "/graba", method = RequestMethod.POST)
    public String graba(HttpServletRequest request, HttpServletResponse response, @Valid EmpleadoPuesto empleadoPuesto, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return Constantes.PATH_EMPLEADOPUESTO_NUEVO;
        }

        try {
            empleadoPuesto.setPuesto(puestoManager.obtiene(empleadoPuesto.getPuesto().getId()));
            Usuario usuario = ambiente.obtieneUsuario();
            empleadoPuestoManager.graba(empleadoPuesto, usuario);

            ambiente.actualizaSesion(request.getSession(), usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al empleadoPuesto", e);
            errors.rejectValue("nombre", "empleadoPuesto.errors.creado", e.toString());
            return Constantes.PATH_EMPLEADOPUESTO_NUEVO;
        }

        redirectAttributes.addFlashAttribute("message", "empleadoPuesto.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{empleadoPuesto.getPuesto().getDescripcion()});

        return "redirect:" + Constantes.PATH_EMPLEADOPUESTO;
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, HttpServletRequest request, @Valid EmpleadoPuesto empleadoPuesto, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        log.debug("Edita empleadoPuesto {}", id);
        try {
            empleadoPuesto = empleadoPuestoManager.obtiene(id);
        } catch (ObjectRetrievalFailureException ex) {
            log.error("No se pudo obtener al empleadoPuesto", ex);
            errors.rejectValue("empleadoPuesto", "registro.noEncontrado", new String[]{"empleadoPuesto"}, null);
            return Constantes.PATH_EMPLEADOPUESTO;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession().getAttribute("empresaId"));
        modelo.addAttribute(Constantes.PUESTO_LIST, puestoManager.lista(params).get(Constantes.PUESTO_LIST));
        modelo.addAttribute(Constantes.EMPLEADOPUESTO_KEY, empleadoPuesto);
        return Constantes.PATH_EMPLEADOPUESTO_EDITA;
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute EmpleadoPuesto empleadoPuesto, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina empleadoPuesto");
        try {
            Long empresaId = (Long) request.getSession().getAttribute("empresaId");
            String descripcion = empleadoPuestoManager.elimina(id);

            ambiente.actualizaSesion(request.getSession());

            redirectAttributes.addFlashAttribute("message", "empleadoPuesto.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{descripcion});
        } catch (Exception e) {
            log.error("No se pudo eliminar el empleadoPuesto " + id, e);
            bindingResult.addError(new ObjectError("empleadoPuesto", new String[]{"empleadoPuesto.no.eliminado.message"}, null, null));
            return Constantes.PATH_EMPLEADOPUESTO_VER;
        }
        return "redirect:" + Constantes.PATH_EMPLEADOPUESTO;
    }

}
