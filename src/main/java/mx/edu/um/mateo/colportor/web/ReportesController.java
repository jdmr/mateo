/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.service.ReportesColportorManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.web.BaseController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(Constantes.PATH_RPT_CLP)
public class ReportesController extends BaseController {

    @Autowired
    private ReportesColportorManager rclpMgr;
    @Autowired
    private RolDao rolDao;

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
}