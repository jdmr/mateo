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
import mx.edu.um.mateo.contabilidad.dao.CuentaAuxiliarDao;
import mx.edu.um.mateo.contabilidad.model.CuentaAuxiliar;
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
@RequestMapping(Constantes.PATH_CUENTA_AUXILIAR)
public class CuentaAuxiliarController extends BaseController {

    @Autowired
    private CuentaAuxiliarDao cuentaAuxiliarDao;
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
        log.debug("Mostrando lista de cuentas de auxiliares");
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
            params = cuentaAuxiliarDao.lista(params);
            try {
                generaReporte(tipo, (List<CuentaAuxiliar>) params.get(Constantes.CONTAINSKEY_AUXILIARES), response, Constantes.CONTAINSKEY_AUXILIARES, mx.edu.um.mateo.general.utils.Constantes.ORG, organizacionId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = cuentaAuxiliarDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<CuentaAuxiliar>) params.get(Constantes.CONTAINSKEY_AUXILIARES), request, Constantes.CONTAINSKEY_AUXILIARES, mx.edu.um.mateo.general.utils.Constantes.ORG, organizacionId);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("auxiliares.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = cuentaAuxiliarDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_AUXILIARES, params.get(Constantes.CONTAINSKEY_AUXILIARES));

        this.pagina(params, modelo, Constantes.CONTAINSKEY_AUXILIARES, pagina);

        return Constantes.PATH_CUENTA_AUXILIAR_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando cuenta de auxiliar {}", id);
        CuentaAuxiliar auxiliar = cuentaAuxiliarDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_AUXILIAR, auxiliar);

        return Constantes.PATH_CUENTA_AUXILIAR_VER;
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nueva cuenta de auxiliar");
        CuentaAuxiliar auxiliar = new CuentaAuxiliar();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_AUXILIAR, auxiliar);
        return Constantes.PATH_CUENTA_AUXILIAR_NUEVA;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid CuentaAuxiliar auxiliar, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.PATH_CUENTA_AUXILIAR_NUEVA;
        }

        try {
            auxiliar = cuentaAuxiliarDao.crea(auxiliar, ambiente.obtieneUsuario());
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la cuenta de auxiliar", e);
            return Constantes.PATH_CUENTA_AUXILIAR_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "auxiliares.creada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{auxiliar.getNombre()});

        return "redirect:" + Constantes.PATH_CUENTA_AUXILIAR_VER + "/" + auxiliar.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar cuenta de auxiliar {}", id);
        CuentaAuxiliar auxiliar = cuentaAuxiliarDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_AUXILIAR, auxiliar);
        return Constantes.PATH_CUENTA_AUXILIAR_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid CuentaAuxiliar auxiliar, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_CUENTA_AUXILIAR_EDITA;
        }
        try {
            auxiliar = cuentaAuxiliarDao.actualiza(auxiliar, ambiente.obtieneUsuario());
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la cuenta de auxiliar", e);
            return Constantes.PATH_CUENTA_AUXILIAR_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "auxiliares.actualizada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{auxiliar.getNombre()});

        return "redirect:" + Constantes.PATH_CUENTA_AUXILIAR_VER + "/" + auxiliar.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute CuentaAuxiliar auxiliar, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina cuenta de auxiliar");
        try {
            String nombre = cuentaAuxiliarDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "auxiliares.eliminada.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la cuenta de auxiliar " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_AUXILIAR, new String[]{"auxiliares.no.eliminada.message"}, null, null));
            return Constantes.PATH_CUENTA_AUXILIAR_VER;
        }

        return "redirect:" + Constantes.PATH_CUENTA_AUXILIAR;
    }
}
