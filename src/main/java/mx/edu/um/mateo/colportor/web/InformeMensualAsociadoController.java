/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.util.List;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.InformeMensualAsociado;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;
import mx.edu.um.mateo.colportor.service.InformeMensualAsociadoManager;
import mx.edu.um.mateo.general.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author osoto
 *
 */
@Controller
@RequestMapping(Constantes.PATH_RPT_CLP_INFORMEMENSUALASOCIADO)
public class InformeMensualAsociadoController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(InformeMensualAsociadoController.class);
    
    @Autowired
    private InformeMensualAsociadoManager informeMensualAsocMgr;
/*
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
        params.put("asociado", ambiente.obtieneUsuario().getId());
        
        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = informeMensualAsociadoDao.lista(params);
            
            try {
                generaReporte(tipo, (List<InformeMensualAsociado>) params.get(Constantes.INFORMEMENSUALASOCIADO_LIST), response, Constantes.INFORMEMENSUALASOCIADO_LIST, Constantes.ASO, null);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }
        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = informeMensualAsociadoDao.lista(params);
            
            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<InformeMensualAsociado>) params.get(Constantes.INFORMEMENSUALASOCIADO_LIST), request, Constantes.INFORMEMENSUALASOCIADO_LIST, Constantes.ASO, null);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("informeMensualAsociado.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = informeMensualAsociadoDao.lista(params);
        
        log.debug("Proyecto" + ((List) params.get(Constantes.INFORMEMENSUALASOCIADO_LIST)).size());
        modelo.addAttribute(Constantes.INFORMEMENSUALASOCIADO_LIST, params.get(Constantes.INFORMEMENSUALASOCIADO_LIST));

        this.pagina(params, modelo, Constantes.INFORMEMENSUALASOCIADO_LIST, pagina);

        return Constantes.INFORMEMENSUALASOCIADO_PATH_LISTA;
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo proyecto");
        InformeMensualAsociado informeMensualAsociado = new InformeMensualAsociado();
        modelo.addAttribute(Constantes.INFORMEMENSUALASOCIADO, informeMensualAsociado);
        return Constantes.INFORMEMENSUALASOCIADO_PATH_NUEVO;
    }
*/
    @Transactional
    @RequestMapping(value = "/finalizar", method = RequestMethod.GET)
    public String crea(HttpServletRequest request, 
            HttpServletResponse response,             
            Model modelo, 
            RedirectAttributes redirectAttributes
            ) {
        
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        
        if(request.getSession().getAttribute(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO) == null){
            return Constantes.PATH_RPT_CLP_INFORMEMENSUALASOCIADO;
        }
        
        List<InformeMensualDetalle> detalles = (List<InformeMensualDetalle>)request.getSession().getAttribute(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO);
        
        InformeMensualAsociado informeMensualAsociado = new InformeMensualAsociado();
        try {
            informeMensualAsociado = informeMensualAsocMgr.finalizaInforme(detalles, (Asociado)ambiente.obtieneUsuario());
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(InformeMensualAsociadoController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeMensualAsociado.creado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{sdf.format(informeMensualAsociado.getFechaRegistro())});
        return "redirect:/colportaje/informes/informeMensualAsociado";
        
    }
/*
    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {

        log.debug("Mostrando proyecto {}", id);
        InformeMensualAsociado informeMensualAsociado = informeMensualAsociadoDao.obtiene(id);

        modelo.addAttribute(Constantes.INFORMEMENSUALASOCIADO, informeMensualAsociado);

        return Constantes.INFORMEMENSUALASOCIADO_PATH_VER;
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {

        log.debug("Edita proyecto {}", id);
        InformeMensualAsociado informeMensualAsociado = informeMensualAsociadoDao.obtiene(id);
        modelo.addAttribute(Constantes.INFORMEMENSUALASOCIADO, informeMensualAsociado);
        return Constantes.INFORMEMENSUALASOCIADO_PATH_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, 
            @Valid InformeMensualAsociado informeMensualAsociado, 
            BindingResult bindingResult, 
            Errors errors, 
            Model modelo, 
            RedirectAttributes redirectAttributes) {
        
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.INFORMEMENSUALASOCIADO_PATH_EDITA;
        }
        try {
            informeMensualAsociado.setAsociado((Asociado)ambiente.obtieneUsuario());
            informeMensualAsociado = informeMensualAsociadoDao.crea(informeMensualAsociado);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear el proyecto", e);
            return Constantes.INFORMEMENSUALASOCIADO_PATH_NUEVO;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeMensualAsociado.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{sdf.format(informeMensualAsociado.getFechaRegistro())});

        return "redirect:" + Constantes.INFORMEMENSUALASOCIADO_PATH_VER + "/" + informeMensualAsociado.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, 
            @RequestParam Long id,
            Model modelo, 
            @ModelAttribute InformeMensualAsociado informeMensualAsociados, 
            BindingResult bindingResult, 
            RedirectAttributes redirectAttributes) {
        log.debug("Elimina proyecto");
        
        try {
            String nombre = informeMensualAsociadoDao.elimina((Asociado)ambiente.obtieneUsuario(),id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "informeMensualAsociado.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar el proyecto " + id, e);
            bindingResult.addError(new ObjectError(Constantes.INFORMEMENSUALASOCIADO_LIST, new String[]{"informeMensualAsociado.no.eliminado.message"}, null, null));
            return Constantes.INFORMEMENSUALASOCIADO_PATH_VER;
        }

        return "redirect:" + Constantes.INFORMEMENSUALASOCIADO_PATH;
    }*/
}
