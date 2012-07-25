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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inventario.dao.AlmacenDao;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/inventario/almacen")
public class AlmacenController extends BaseController {

    @Autowired
    private AlmacenDao almacenDao;

    public AlmacenController() {
        log.info("Se ha creado una nueva instancia de AlmacenController");
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
        log.debug("Mostrando lista de almacenes");
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
            params = almacenDao.lista(params);
            try {
                generaReporte(tipo, (List<Almacen>) params.get("almacenes"), response, "almacenes", Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = almacenDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Almacen>) params.get("almacenes"), request, "almacenes", Constantes.EMP, empresaId);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("almacen.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = almacenDao.lista(params);
        modelo.addAttribute("almacenes", params.get("almacenes"));

        this.pagina(params, modelo, "almacenes", pagina);

        return "inventario/almacen/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando almacen {}", id);
        Almacen almacen = almacenDao.obtiene(id);

        modelo.addAttribute("almacen", almacen);

        return "inventario/almacen/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nueva almacen");
        Almacen almacen = new Almacen();
        modelo.addAttribute("almacen", almacen);
        return "inventario/almacen/nuevo";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpSession session, @Valid Almacen almacen, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "inventario/almacen/nuevo";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            almacen = almacenDao.crea(almacen, usuario);

            ambiente.actualizaSesion(session, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al almacen", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "inventario/almacen/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "almacen.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{almacen.getNombre()});

        return "redirect:/inventario/almacen/ver/" + almacen.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita almacen {}", id);
        Almacen almacen = almacenDao.obtiene(id);
        modelo.addAttribute("almacen", almacen);
        return "inventario/almacen/edita";
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpSession session, @Valid Almacen almacen, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return "inventario/almacen/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            almacen = almacenDao.actualiza(almacen, usuario);

            ambiente.actualizaSesion(session, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la almacen", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "inventario/almacen/edita";
        }

        redirectAttributes.addFlashAttribute("message", "almacen.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{almacen.getNombre()});

        return "redirect:/inventario/almacen/ver/" + almacen.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpSession session, Model modelo, @ModelAttribute Almacen almacen, BindingResult bindingResult, RedirectAttributes redirectAttributes, @RequestParam Long id) {
        log.debug("Elimina almacen");
        try {
            Long empresaId = (Long) session.getAttribute("empresaId");
            String nombre = almacenDao.elimina(id, empresaId);

            ambiente.actualizaSesion(session);

            redirectAttributes.addFlashAttribute("message", "almacen.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (UltimoException e) {
            log.error("No se pudo eliminar el almacen " + id, e);
            bindingResult.addError(new ObjectError("almacen", new String[]{"ultimo.almacen.no.eliminado.message"}, null, null));
            return "inventario/almacen/ver";
        } catch (Exception e) {
            log.error("No se pudo eliminar la almacen " + id, e);
            bindingResult.addError(new ObjectError("almacen", new String[]{"almacen.no.eliminado.message"}, null, null));
            return "inventario/almacen/ver";
        }

        return "redirect:/inventario/almacen";
    }
}
