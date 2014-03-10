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
import mx.edu.um.mateo.colportor.dao.ColportorDao;
import mx.edu.um.mateo.colportor.dao.TemporadaDao;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Temporada;
import mx.edu.um.mateo.colportor.service.ReportesColportorManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.web.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping(Constantes.PATH_RPT_CLP)
public class ReportesController extends BaseController {

    @Autowired
    private ReportesColportorManager rclpMgr;
    @Autowired
    private ColportorDao clpDao;
    @Autowired
    private TemporadaDao tmpDao;

    @RequestMapping({"/colportaje/reportes"})
    public String index() {
        log.debug("Mostrando menu de colportaje/reportes");
        return "colportaje/reportes/index";
    }    

    @RequestMapping("censoColportores")
    public String censoColportores(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Usuario usuario,
            Errors errors,
            Model modelo) {
        log.debug("Mostrando censo de Colportores");
        log.debug("filtro {}", filtro);
        log.debug("pagina {}", pagina);
        log.debug("tipo {}", tipo);
        log.debug("correo {}", correo);
        log.debug("order {}", order);
        log.debug("sort {}", sort);
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());

        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            log.debug("Entrando a tipo");
            params.put("reporte", true);
            try {
                params = rclpMgr.censoColportores(params);
            } catch (Exception ex) {
                log.error("Error al intentar obtener el censo de colportores");
            }
            log.debug("Obtuvo listado");
            try {
                log.debug("Generando reporte");
                generaReporte(tipo, (List<Colportor>) params.get("censoColportores"), response,
                        "censoColportores", Constantes.EMP, ambiente.obtieneUsuario().getEmpresa().getId());
                log.debug("Genero reporte");
                return null;
            } catch (Exception e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            try {
                params = rclpMgr.censoColportores(params);
            } catch (Exception ex) {
                log.error("Error al intentar obtener el censo de colportores");
            }

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Colportor>) params.get("censoColportores"), request,
                        "censoColportores", Constantes.EMP, ambiente.obtieneUsuario().getEmpresa().getId());
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("colportor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (Exception e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        try {
            params = rclpMgr.censoColportores(params);
        } catch (Exception ex) {
            log.error("Error al intentar obtener el censo de colportores");
            ex.printStackTrace();
        }

        modelo.addAttribute(Constantes.CONTAINSKEY_CENSOCOLPORTORES, params.get(Constantes.CONTAINSKEY_CENSOCOLPORTORES));

        return Constantes.PATH_RPT_CLP_CENSOCOLPORTORES;
    }
    
    @RequestMapping("concentradoPorTemporadas")
    public String concentradoPorTemporadas(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long colportorId,
            Usuario usuario,
            Errors errors,
            Model modelo,  
            RedirectAttributes redirectAttributes) {
        log.debug("Mostrando concentrado por temporadas");
        
        log.debug("filtro {}", filtro);
        log.debug("pagina {}", pagina);
        log.debug("tipo {}", tipo);
        log.debug("correo {}", correo);
        log.debug("order {}", order);
        log.debug("sort {}", sort);
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params.put("colportor", colportorId);

        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            log.debug("Entrando a tipo");
            params.put("reporte", true);
            try {
                params = rclpMgr.concentradoPorTemporadas(params);
            } catch (Exception ex) {
                log.error("Error al intentar obtener el concentrado por temporadas");
            }
            log.debug("Obtuvo listado");
            try {
                log.debug("Generando reporte");
                generaReporte(tipo, (List<Colportor>) params.get(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS), response,
                        Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS, Constantes.EMP, ambiente.obtieneUsuario().getEmpresa().getId());
                log.debug("Genero reporte");
                return null;
            } catch (Exception e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            try {
                params = rclpMgr.concentradoPorTemporadas(params);
            } catch (Exception ex) {
                log.error("Error al intentar obtener el concentrado por temporadas");
            }

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Colportor>) params.get(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS), request,
                        Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS, Constantes.EMP, ambiente.obtieneUsuario().getEmpresa().getId());
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("colportor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (Exception e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        try {
            params = rclpMgr.concentradoPorTemporadas(params);
        } catch (Exception ex) {
            log.error("Error al intentar obtener el concentrado por temporadas");
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "error.generar.reporte");
            return "redirect:/colportaje/reportes";
        }

        modelo.addAttribute(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS, params.get(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS));
        modelo.addAttribute(Constantes.COLPORTOR, clpDao.obtiene(colportorId));
                
        return Constantes.PATH_RPT_CLP_CONCENTRADOPORTEMPORADAS;
    }
    
    @RequestMapping("concentradoGeneralPorTemporadas")
    public String concentradoGeneralPorTemporadas(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String clave,
            Usuario usuario,
            Errors errors,
            Model modelo,  
            RedirectAttributes redirectAttributes) {
        log.debug("Mostrando concentrado por temporadas");
        
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        
        if(clave != null && !clave.isEmpty()){
            params.put("colportor", clpDao.obtiene(clave).getId());            
        }
        else{
            return Constantes.PATH_RPT_CLP_CONCENTRADOGRALPORTEMPORADAS;
        }
        

        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            log.debug("Entrando a tipo");
            params.put("reporte", true);
            try {
                params = rclpMgr.concentradoGralPorTemporadas(params);
            } catch (Exception ex) {
                log.error("Error al intentar obtener el concentrado por temporadas");
            }
            log.debug("Obtuvo listado");
            try {
                log.debug("Generando reporte");
                generaReporte(tipo, (List<Colportor>) params.get(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS), response,
                        Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS, Constantes.EMP, ambiente.obtieneUsuario().getEmpresa().getId());
                log.debug("Genero reporte");
                return null;
            } catch (Exception e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            try {
                params = rclpMgr.concentradoGralPorTemporadas(params);
            } catch (Exception ex) {
                log.error("Error al intentar obtener el concentrado por temporadas");
            }

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Colportor>) params.get(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS), request,
                        Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS, Constantes.EMP, ambiente.obtieneUsuario().getEmpresa().getId());
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("colportor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (Exception e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        try {
            params = rclpMgr.concentradoGralPorTemporadas(params);
        } catch (Exception ex) {
            log.error("Error al intentar obtener el concentrado por temporadas");
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "error.generar.reporte");
            return "redirect:/colportaje/reportes";
        }

        modelo.addAttribute(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS, params.get(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS));
        modelo.addAttribute(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_BOLETIN, params.get(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_BOLETIN));
        modelo.addAttribute(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_DIEZMO, params.get(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_DIEZMO));
                
        return Constantes.PATH_RPT_CLP_CONCENTRADOGRALPORTEMPORADAS;
    }
    
    @RequestMapping("concentradoVentas")
    public String concentradoVentas(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Long temporadaId,
            Usuario usuario,
            Errors errors,
            Model modelo,  
            RedirectAttributes redirectAttributes) {
        log.debug("Mostrando concentrado ventas");
        
        Map<String, Object> params = new HashMap<>();
        Temporada tmp = null;
        
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params.put("organizacion", ambiente.obtieneUsuario().getEmpresa().getOrganizacion().getId());
        
        if(temporadaId != null){
            tmp = tmpDao.obtiene(temporadaId);
            params.put("temporada", tmp.getId());
        }
        else{
            modelo.addAttribute(Constantes.TEMPORADA_LIST, tmpDao.lista(params).get(Constantes.TEMPORADA_LIST));
            return Constantes.PATH_RPT_CLP_CONCENTRADOVENTAS;
        }            

        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }
        if (StringUtils.isNotBlank(tipo)) {
            log.debug("Entrando a tipo");
            params.put("reporte", true);
            try {
                params = rclpMgr.concentradoVentas(params);
            } catch (Exception ex) {
                log.error("Error al intentar obtener el concentrado ventas");
            }
            log.debug("Obtuvo listado");
            try {
                log.debug("Generando reporte");
                generaReporte(tipo, (List<Colportor>) params.get(Constantes.CONTAINSKEY_CONCENTRADOVENTAS), response,
                        Constantes.CONTAINSKEY_CONCENTRADOVENTAS, Constantes.EMP, ambiente.obtieneUsuario().getEmpresa().getId());
                log.debug("Genero reporte");
                return null;
            } catch (Exception e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            try {
                params = rclpMgr.concentradoVentas(params);
            } catch (Exception ex) {
                log.error("Error al intentar obtener el concentrado por temporadas");
            }

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Colportor>) params.get(Constantes.CONTAINSKEY_CONCENTRADOVENTAS), request,
                        Constantes.CONTAINSKEY_CONCENTRADOVENTAS, Constantes.EMP, ambiente.obtieneUsuario().getEmpresa().getId());
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("colportor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (Exception e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        try {
            params = rclpMgr.concentradoVentas(params);
        } catch (Exception ex) {
            log.error("Error al intentar obtener el concentrado por temporadas");
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "error.generar.reporte");
            return "redirect:/colportaje/reportes";
        }

        modelo.addAttribute(Constantes.CONTAINSKEY_CONCENTRADOVENTAS, params.get(Constantes.CONTAINSKEY_CONCENTRADOVENTAS));
        modelo.addAttribute(Constantes.TEMPORADA_LIST, tmpDao.lista(params).get(Constantes.TEMPORADA_LIST));
        modelo.addAttribute(Constantes.TEMPORADA, tmp);
        
        modelo.addAttribute(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_BOLETIN, params.get(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_BOLETIN));
        modelo.addAttribute(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_DIEZMO, params.get(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_DIEZMO));
                
        return Constantes.PATH_RPT_CLP_CONCENTRADOVENTAS;
    }
    
    @RequestMapping("planMensualOracion")
    public String planMensualOracion(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Usuario usuario,
            Errors errors,
            Model modelo,  
            RedirectAttributes redirectAttributes) {
        log.debug("Mostrando Plan Mensual de Oracion");
        
        Map<String, Object> params = new HashMap<>();
        
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params.put("organizacion", ambiente.obtieneUsuario().getEmpresa().getOrganizacion().getId());
        
        try {
            params = rclpMgr.planMensualOracion(params);
        } catch (Exception ex) {
            log.error("Error al intentar obtener el plan mensual de oracion");
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "error.generar.reporte");
            return "redirect:/colportaje/reportes";
        }

        modelo.addAttribute(Constantes.CONTAINSKEY_PLANMENSUALORACION, ((Map<String, Colportor>)params.get(Constantes.CONTAINSKEY_PLANMENSUALORACION)).values());
                
        return Constantes.PATH_RPT_CLP_PLANMENSUALORACION;
    }
    
    @RequestMapping("planDiarioOracion")
    public String planDiarioOracion(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Usuario usuario,
            Errors errors,
            Model modelo,  
            RedirectAttributes redirectAttributes) {
        log.debug("Mostrando Plan Diario de Oracion");
        
        Map<String, Object> params = new HashMap<>();
        
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params.put("organizacion", ambiente.obtieneUsuario().getEmpresa().getOrganizacion().getId());
        
        try {
            params = rclpMgr.planDiarioOracion(params);
        } catch (Exception ex) {
            log.error("Error al intentar obtener el plan diario de oracion");
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "error.generar.reporte");
            return "redirect:/colportaje/reportes";
        }

        modelo.addAttribute(Constantes.CONTAINSKEY_PLANDIARIOORACION, ((Map<String, Colportor>)params.get(Constantes.CONTAINSKEY_PLANDIARIOORACION)).values());
                
        return Constantes.PATH_RPT_CLP_PLANDIARIOORACION;
    }
}