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
package mx.edu.um.mateo.contabilidad.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.dao.CuentaMayorDao;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
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
 * @author jdmr
 */
@Controller
@RequestMapping(Constantes.PATH_CUENTA_MAYOR)
public class CuentaMayorController extends BaseController {

    @Autowired
    private CuentaMayorDao cuentaMayorDao;
    @Autowired
    private Ambiente ambiente;

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
        log.debug("Mostrando lista de cuentas de mayores");
        Map<String, Object> params = new HashMap<>();
        Long organizacionId = (Long) request.getSession().getAttribute("organizacionId");
        params.put("organizacion", organizacionId);
        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = cuentaMayorDao.lista(params);
            try {
                generaReporte(tipo, (List<CuentaMayor>) params.get(Constantes.CONTAINSKEY_MAYORES), response, Constantes.CONTAINSKEY_MAYORES, mx.edu.um.mateo.general.utils.Constantes.ORG, organizacionId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = cuentaMayorDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<CuentaMayor>) params.get(Constantes.CONTAINSKEY_MAYORES), request, Constantes.CONTAINSKEY_MAYORES, mx.edu.um.mateo.general.utils.Constantes.ORG, organizacionId);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("mayores.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = cuentaMayorDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_MAYORES, params.get(Constantes.CONTAINSKEY_MAYORES));

        this.pagina(params, modelo, Constantes.CONTAINSKEY_MAYORES, pagina);

        return Constantes.PATH_CUENTA_MAYOR_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando cuenta de mayor {}", id);
        CuentaMayor mayor = cuentaMayorDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_MAYOR, mayor);

        return Constantes.PATH_CUENTA_MAYOR_VER;
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nueva cuenta de mayor");
        CuentaMayor mayor = new CuentaMayor();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_MAYOR, mayor);
        return Constantes.PATH_CUENTA_MAYOR_NUEVA;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid CuentaMayor mayor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.PATH_CUENTA_MAYOR_NUEVA;
        }

        try {
            mayor = cuentaMayorDao.crea(mayor, ambiente.obtieneUsuario());
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la cuenta de mayor", e);
            return Constantes.PATH_CUENTA_MAYOR_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "mayores.creada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{mayor.getNombre()});

        return "redirect:" + Constantes.PATH_CUENTA_MAYOR_VER + "/" + mayor.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar cuenta de mayor {}", id);
        CuentaMayor mayor = cuentaMayorDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_MAYOR, mayor);
        return Constantes.PATH_CUENTA_MAYOR_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid CuentaMayor mayor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_CUENTA_MAYOR_EDITA;
        }
        try {
            mayor = cuentaMayorDao.actualiza(mayor, ambiente.obtieneUsuario());
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la cuenta de mayor", e);
            return Constantes.PATH_CUENTA_MAYOR_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "mayores.actualizada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{mayor.getNombre()});

        return "redirect:" + Constantes.PATH_CUENTA_MAYOR_VER + "/" + mayor.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute CuentaMayor mayor, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina cuenta de mayor");
        try {
            String nombre = cuentaMayorDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "mayores.eliminada.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la cuenta de mayor " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_MAYOR, new String[]{"mayores.no.eliminada.message"}, null, null));
            return Constantes.PATH_CUENTA_MAYOR_VER;
        }

        return "redirect:" + Constantes.PATH_CUENTA_MAYOR;
    }
}
