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
package mx.edu.um.mateo.inventario.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inventario.dao.CancelacionDao;
import mx.edu.um.mateo.inventario.model.Cancelacion;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/inventario/cancelacion")
public class CancelacionController extends BaseController {

    @Autowired
    private CancelacionDao cancelacionDao;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            Cancelacion cancelacion,
            Errors errors,
            Model modelo) throws ParseException {
        log.debug("Mostrando lista de cancelaciones");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Map<String, Object> params = this.convierteParams(request.getParameterMap());
        Long almacenId = (Long) request.getSession().getAttribute("almacenId");
        params.put("almacen", almacenId);

        if (params.containsKey("fechaIniciado")) {
            log.debug("FechaIniciado: {}", params.get("fechaIniciado"));
            params.put("fechaIniciado", sdf.parse((String) params.get("fechaIniciado")));
        }

        if (params.containsKey("fechaTerminado")) {
            params.put("fechaTerminado", sdf.parse((String) params.get("fechaTerminado")));
        }

        if (params.containsKey("tipo") && StringUtils.isNotBlank((String) params.get("tipo"))) {
            params.put("reporte", true);
            params = cancelacionDao.lista(params);
            try {
                generaReporte((String) params.get("tipo"), (List<Cancelacion>) params.get("cancelaciones"), response, "cancelaciones", Constantes.ALM, almacenId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove("reporte");
                errors.reject("error.generar.reporte");
            }
        }

        if (params.containsKey("correo") && StringUtils.isNotBlank((String) params.get("correo"))) {
            params.put("reporte", true);
            params = cancelacionDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo((String) params.get("correo"), (List<Cancelacion>) params.get("cancelaciones"), request, "cancelaciones", Constantes.ALM, almacenId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("cancelacion.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = cancelacionDao.lista(params);
        modelo.addAttribute("cancelaciones", params.get("cancelaciones"));

        Long pagina = 1l;
        if (params.containsKey("pagina")) {
            pagina = (Long) params.get("pagina");
        }
        this.pagina(params, modelo, "cancelaciones", pagina);

        return "inventario/cancelacion/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando cancelacion {}", id);
        Cancelacion cancelacion = cancelacionDao.obtiene(id);
        modelo.addAttribute("cancelacion", cancelacion);
        return "inventario/cancelacion/ver";
    }
}
