/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.dao.ProyectoColportorDao;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.colportor.model.ProyectoColportor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 *
 */
@Controller
@RequestMapping(Constantes.PROYECTOCOLPORTOR_PATH)
public class ProyectoColportorController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ProyectoColportorController.class);
    @Autowired
    private ProyectoColportorDao proyectoColportorDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UsuarioDao usuarioDao;

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
        log.debug("Mostrando lista de proyectos");
        Map<String, Object> params = new HashMap<>();
        
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params.put("usuario", ambiente.obtieneUsuario().getId());
        
        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = proyectoColportorDao.lista(params);
            
            try {
                generaReporte(tipo, (List<ProyectoColportor>) params.get(Constantes.PROYECTOCOLPORTOR_LIST), response, Constantes.PROYECTOCOLPORTOR_LIST, Constantes.ASO, null);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }
        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = proyectoColportorDao.lista(params);
            
            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<ProyectoColportor>) params.get(Constantes.PROYECTOCOLPORTOR_LIST), request, Constantes.PROYECTOCOLPORTOR_LIST, Constantes.ASO, null);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("proyectoColportor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = proyectoColportorDao.lista(params);
        
        log.debug("Proyecto" + ((List) params.get(Constantes.PROYECTOCOLPORTOR_LIST)).size());
        modelo.addAttribute(Constantes.PROYECTOCOLPORTOR_LIST, params.get(Constantes.PROYECTOCOLPORTOR_LIST));

        this.pagina(params, modelo, Constantes.PROYECTOCOLPORTOR_LIST, pagina);

        return Constantes.PROYECTOCOLPORTOR_PATH_LISTA;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo proyecto");
        ProyectoColportor proyectoColportor = new ProyectoColportor();
        modelo.addAttribute(Constantes.PROYECTOCOLPORTOR, proyectoColportor);
        return Constantes.PROYECTOCOLPORTOR_PATH_NUEVO;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, 
            HttpServletResponse response, 
            @Valid ProyectoColportor proyectoColportor, 
            BindingResult bindingResult, 
            Errors errors, 
            Model modelo, 
            RedirectAttributes redirectAttributes) {
        
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            this.despliegaBindingResultErrors(bindingResult);
            return Constantes.PROYECTOCOLPORTOR_PATH_NUEVO;
        }
        
        proyectoColportor.setStatus(Constantes.STATUS_ACTIVO);
        proyectoColportor.setUsuario(ambiente.obtieneUsuario());
        proyectoColportor.setEmpresa(ambiente.obtieneUsuario().getEmpresa());
        proyectoColportor = proyectoColportorDao.crea(proyectoColportor);
       
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "proyectoColportor.creado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{proyectoColportor.getNombre()});
        return "redirect:" + Constantes.PROYECTOCOLPORTOR_PATH_VER + "/" + proyectoColportor.getId();
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {

        log.debug("Mostrando proyecto {}", id);
        ProyectoColportor proyectoColportor = proyectoColportorDao.obtiene(id);

        modelo.addAttribute(Constantes.PROYECTOCOLPORTOR, proyectoColportor);

        return Constantes.PROYECTOCOLPORTOR_PATH_VER;
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {

        log.debug("Edita proyecto {}", id);
        ProyectoColportor proyectoColportor = proyectoColportorDao.obtiene(id);
        modelo.addAttribute(Constantes.PROYECTOCOLPORTOR, proyectoColportor);
        return Constantes.PROYECTOCOLPORTOR_PATH_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, 
            @Valid ProyectoColportor proyectoColportor, 
            BindingResult bindingResult, 
            Errors errors, 
            Model modelo, 
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PROYECTOCOLPORTOR_PATH_EDITA;
        }
        try {
            proyectoColportor.setUsuario(ambiente.obtieneUsuario());
            proyectoColportor.setEmpresa(ambiente.obtieneUsuario().getEmpresa());
            proyectoColportor = proyectoColportorDao.crea(proyectoColportor);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el proyecto", e);
            return Constantes.PROYECTOCOLPORTOR_PATH_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "proyectoColportor.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{proyectoColportor.getNombre()});

        return "redirect:" + Constantes.PROYECTOCOLPORTOR_PATH_VER + "/" + proyectoColportor.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, 
            @RequestParam Long id,
            Model modelo, 
            @ModelAttribute ProyectoColportor proyectoColportors, 
            BindingResult bindingResult, 
            RedirectAttributes redirectAttributes) {
        log.debug("Elimina proyecto");
        try {
            String nombre = proyectoColportorDao.elimina(ambiente.obtieneUsuario(),id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "proyectoColportor.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el proyecto " + id, e);
            bindingResult.addError(new ObjectError(Constantes.PROYECTOCOLPORTOR_LIST, new String[]{"proyectoColportor.no.eliminado.message"}, null, null));
            return Constantes.PROYECTOCOLPORTOR_PATH_VER;
        }

        return "redirect:" + Constantes.PROYECTOCOLPORTOR_PATH;
    }
}
