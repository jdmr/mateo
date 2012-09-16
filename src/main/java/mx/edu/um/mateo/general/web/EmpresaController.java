/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
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
package mx.edu.um.mateo.general.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import mx.edu.um.mateo.contabilidad.dao.CentroCostoDao;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.general.dao.EmpresaDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.utils.UltimoException;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author jdmr
 */
@Controller
@RequestMapping("/admin/empresa")
public class EmpresaController extends BaseController {

    @Autowired
    private EmpresaDao empresaDao;
    @Autowired
    private CentroCostoDao centroCostoDao;

    @SuppressWarnings("unchecked")
    @RequestMapping
    public String lista(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort, Model modelo) {
        log.debug("Mostrando lista de empresas");
        Map<String, Object> params = this.convierteParams(request
                .getParameterMap());
        Long organizacionId = (Long) request.getSession().getAttribute(
                "organizacionId");
        params.put("organizacion", organizacionId);

        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = empresaDao.lista(params);
            try {
                generaReporte(tipo, (List<Empresa>) params.get("empresas"),
                        response, "empresas", Constantes.ORG, organizacionId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = empresaDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Empresa>) params.get("empresas"),
                        request, "empresas", Constantes.ORG, organizacionId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute(
                        "messageAttrs",
                        new String[]{
                            messageSource.getMessage("empresa.lista.label",
                            null, request.getLocale()),
                            ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = empresaDao.lista(params);
        modelo.addAttribute("empresas", params.get("empresas"));

        this.pagina(params, modelo, "empresas", pagina);

        return "admin/empresa/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando empresa {}", id);
        Empresa empresa = empresaDao.obtiene(id);

        modelo.addAttribute("empresa", empresa);

        return "admin/empresa/ver";
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nueva empresa");
        Empresa empresa = new Empresa();
        modelo.addAttribute("empresa", empresa);
        return "admin/empresa/nueva";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpSession session, @Valid Empresa empresa,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "cuentaId", required = false) String centroDeCostoId) {
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "admin/empresa/nueva";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (StringUtils.isNotBlank(centroDeCostoId)) {
                CentroCosto centroCosto = centroCostoDao.obtieneCentroDeCosto(centroDeCostoId, usuario);
                empresa.setCentroCosto(centroCosto);
            }
            
            empresa = empresaDao.crea(empresa, usuario);

            ambiente.actualizaSesion(session, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al empresa", e);
            errors.rejectValue("codigo", "campo.duplicado.message",
                    new String[]{"codigo"}, null);
            errors.rejectValue("nombre", "campo.duplicado.message",
                    new String[]{"nombre"}, null);
            return "admin/empresa/nueva";
        }

        redirectAttributes.addFlashAttribute("message",
                "empresa.creada.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{empresa.getNombre()});

        return "redirect:/admin/empresa/ver/" + empresa.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita empresa {}", id);
        Empresa empresa = empresaDao.obtiene(id);
        modelo.addAttribute("empresa", empresa);
        return "admin/empresa/edita";
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpSession session, @Valid Empresa empresa,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "cuentaId", required = false) String centroDeCostoId) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "admin/empresa/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (StringUtils.isNotBlank(centroDeCostoId)) {
                CentroCosto centroCosto = centroCostoDao.obtieneCentroDeCosto(centroDeCostoId, usuario);
                empresa.setCentroCosto(centroCosto);
            }
            
            empresa = empresaDao.actualiza(empresa, usuario);

            ambiente.actualizaSesion(session, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la empresa", e);
            errors.rejectValue("codigo", "campo.duplicado.message",
                    new String[]{"codigo"}, null);
            errors.rejectValue("nombre", "campo.duplicado.message",
                    new String[]{"nombre"}, null);
            return "admin/empresa/nueva";
        }

        redirectAttributes.addFlashAttribute("message",
                "empresa.actualizada.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{empresa.getNombre()});

        return "redirect:/admin/empresa/ver/" + empresa.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpSession session, @RequestParam Long id,
            Model modelo, @ModelAttribute Empresa empresa,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina empresa");
        try {
            String nombre = empresaDao.elimina(id);

            ambiente.actualizaSesion(session);

            redirectAttributes.addFlashAttribute("message",
                    "empresa.eliminada.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{nombre});
        } catch (UltimoException e) {
            log.error("No se pudo eliminar el empresa " + id, e);
            bindingResult.addError(new ObjectError("empresa",
                    new String[]{"ultima.empresa.no.eliminada.message"},
                    null, null));
            return "admin/empresa/ver";
        } catch (Exception e) {
            log.error("No se pudo eliminar la empresa " + id, e);
            bindingResult
                    .addError(new ObjectError("empresa",
                    new String[]{"empresa.no.eliminada.message"},
                    null, null));
            return "admin/empresa/ver";
        }

        return "redirect:/admin/empresa";
    }

    @RequestMapping(value = "/centrosDeCosto", params = "term", produces = "application/json")
    public @ResponseBody
    List<Map<String, String>> centrosDeCosto(HttpServletRequest request,
            @RequestParam("term") String filtro) {
        log.debug("Buscando Centros de Costo por {}", filtro);
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }

        List<CentroCosto> centrosDeCosto = centroCostoDao.buscaCentrosDeCostoPorOrganizacion(filtro, ambiente.obtieneUsuario());
        List<Map<String, String>> resultados = new ArrayList<>();
        for (CentroCosto centroCosto : centrosDeCosto) {
            Map<String, String> map = new HashMap<>();
            map.put("id", centroCosto.getId().getIdCosto());
            map.put("value", centroCosto.getNombreCompleto());
            resultados.add(map);
        }

        return resultados;
    }
}
