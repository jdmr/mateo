/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;
import mx.edu.um.mateo.colportor.service.ReportesColportorManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author gibrandemetrioo
 */
@Controller
@RequestMapping("/colportaje/informes")
public class InformesController extends BaseController  {
    @Autowired
    private ReportesColportorManager rclpMgr;
    
    @RequestMapping({"/colportaje/informes"})
    public String index() {
        log.debug("Mostrando menu de colportaje/informes");
        return "colportaje/informes/index";
    }

    @RequestMapping({"informeMensualAsociado"})
    public String informeMensualAsociado(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Integer mes, //Mes a consultar
            @RequestParam(required = false) Integer year, //Year a consultar
            Usuario usuario,
            Errors errors,
            Model modelo,  
            BindingResult bindingResult, 
            RedirectAttributes redirectAttributes) {
        log.debug("Mostrando Informe Mensual del Asociado");
        log.debug("mes {}",mes);
        log.debug("year {}",year);
        
        if(mes == null || year == null){
            this.getMeses(modelo);
            modelo.addAttribute("mesElegido", 0);
            return Constantes.PATH_RPT_CLP_INFORMEMENSUALASOCIADO;
        }
        
        Map<String, Object> params = new HashMap<>();
        
        params.put("asociado", ambiente.obtieneUsuario().getId());
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params.put("organizacion", ambiente.obtieneUsuario().getEmpresa().getOrganizacion().getId());
        
        if(mes.compareTo(0) < 0 || mes.compareTo(11) > 0){
            log.error("Error al intentar obtener el informe mensual del asociado: mes {} invalido ", mes);
//            errors.rejectValue("mes", "informeMensualAsociado.error.mesInvalido",
//                    new String[]{"mes"}, null);
            return "redirect:/colportaje/reportes";
        }
        
        params.put("mes", mes);
        params.put("year", year);
        
        try {
            params = rclpMgr.informeMensualAsociado(params);
        } catch (Exception ex) {
            log.error("Error al intentar obtener el informe mensual del asociado {}", ex);
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "error.generar.reporte");
            return "redirect:/colportaje/reportes";
        }

        modelo.addAttribute(Constantes.ASOCIADO_COLPORTOR, ambiente.obtieneUsuario());
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO, params.get(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO));
        
        InformeMensualDetalle detalle = (InformeMensualDetalle)params.get(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO_TOTALES);
        Calendar gcFecha = Calendar.getInstance(TimeZone.getTimeZone("America/Monterrey"));
        gcFecha.set(year, mes, 01);
        detalle.setFecha(gcFecha.getTime());
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO_TOTALES, detalle);
        
        this.getMeses(modelo);
        modelo.addAttribute("mesElegido", mes);
                
        return Constantes.PATH_RPT_CLP_INFORMEMENSUALASOCIADO;
    }
    
    @RequestMapping({"informeConcentradoMensualAsociados"})
    public String informeConcentradoMensualAsociados(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Integer mes, //Mes a consultar
            @RequestParam(required = false) Integer year, //Year a consultar
            Usuario usuario,
            Errors errors,
            Model modelo,  
            BindingResult bindingResult, 
            RedirectAttributes redirectAttributes) {
        log.debug("Mostrando Informe Concentrado de Asociados");
        log.debug("mes {}",mes);
        log.debug("year {}",year);
        
        if(mes == null || year == null){
            this.getMeses(modelo);
            modelo.addAttribute("mesElegido", 0);
            return Constantes.PATH_RPT_CLP_INFORMECONCENTRADOASOCIADO;
        }
        
        Map<String, Object> params = new HashMap<>();
        
        params.put("asociado", ambiente.obtieneUsuario().getId());
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params.put("organizacion", ambiente.obtieneUsuario().getEmpresa().getOrganizacion().getId());
        
        if(mes.compareTo(0) < 0 || mes.compareTo(11) > 0){
            log.error("Error al intentar obtener el informe concentrado de asociados: mes {} invalido ", mes);
//            errors.rejectValue("mes", "informeMensualAsociado.error.mesInvalido",
//                    new String[]{"mes"}, null);
            return "redirect:/colportaje/reportes";
        }
        
        params.put("mes", mes);
        params.put("year", year);
        
        try {
            params = rclpMgr.informeConcentradoAsociadosAsociacion(params);
        } catch (Exception ex) {
            log.error("Error al intentar obtener el informe concentrado de asociados {}", ex);
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "error.generar.reporte");
            return "redirect:/colportaje/reportes";
        }

        modelo.addAttribute(Constantes.ASOCIADO_COLPORTOR, ambiente.obtieneUsuario());
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO, params.get(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO));
        
        InformeMensualDetalle detalle = (InformeMensualDetalle)params.get(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO_TOTALES);
        Calendar gcFecha = Calendar.getInstance(TimeZone.getTimeZone("America/Monterrey"));
        gcFecha.set(year, mes, 01);
        detalle.setFecha(gcFecha.getTime());
        modelo.addAttribute(Constantes.CONTAINSKEY_INFORMECONCENTRADOASOCIADO_TOTALES, detalle);
        
        this.getMeses(modelo);
        modelo.addAttribute("mesElegido", mes);
                
        return Constantes.PATH_RPT_CLP_INFORMECONCENTRADOASOCIADO;
    }
    
    private void getMeses(Model modelo){
        Map <Integer, String> mMeses = new TreeMap<>();
        mMeses.put(0,"Enero");
        mMeses.put(1,"Febrero");
        mMeses.put(2,"Marzo");
        mMeses.put(3,"Abril");
        mMeses.put(4,"Mayo");
        mMeses.put(5,"Junio");
        mMeses.put(6,"Julio");
        mMeses.put(7,"Agosto");
        mMeses.put(8,"Septiembre");
        mMeses.put(9,"Octubre");
        mMeses.put(10,"Noviembre");
        mMeses.put(11,"Diciembre");

        modelo.addAttribute("meses", mMeses);
    }
}
