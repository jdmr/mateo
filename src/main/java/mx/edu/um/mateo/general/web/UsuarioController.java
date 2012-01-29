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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.model.UsuarioRol;
import mx.edu.um.mateo.general.utils.UltimoException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author jdmr
 */
@Controller
@RequestMapping("/admin/usuario")
public class UsuarioController {

    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private UsuarioDao usuarioDao;

    @RequestMapping
    public String lista(Model modelo) {
        log.debug("Mostrando lista de usuarios");
        Map<String, Object> params = usuarioDao.lista(null);
        modelo.addAttribute("usuarios", params.get("usuarios"));
        return "admin/usuario/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando usuario {}", id);
        Usuario usuario = usuarioDao.obtiene(id);
        List<Rol> roles = usuarioDao.roles();
        Map<String, Boolean> seleccionados = new HashMap<>();
        for (UsuarioRol usuarioRol : usuario.getAuthorities()) {
            seleccionados.put(usuarioRol.getRol().getAuthority(), Boolean.TRUE);
        }
        modelo.addAttribute("seleccionados", seleccionados);
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("roles", roles);
        return "admin/usuario/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo usuario");
        List<Rol> roles = usuarioDao.roles();
        Usuario usuario = new Usuario();
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("roles", roles);
        return "admin/usuario/nuevo";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, @Valid Usuario usuario, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            List<Rol> roles = usuarioDao.roles();
            modelo.addAttribute("roles", roles);

            Map<String, Boolean> seleccionados = new HashMap<>();
            for (UsuarioRol usuarioRol : usuario.getAuthorities()) {
                seleccionados.put(usuarioRol.getRol().getAuthority(), Boolean.TRUE);
            }
            modelo.addAttribute("seleccionados", seleccionados);
            return "admin/usuario/nuevo";
        }

        try {
            String[] roles = request.getParameterValues("roles");
            if (roles == null || roles.length == 0) {
                roles = new String[] {"ROLE_USER"};
            }
            Long almacenId = (Long) request.getSession().getAttribute("almacenId");
            usuario = usuarioDao.crea(usuario, almacenId, roles);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al usuario", e);
            errors.rejectValue("username", "campo.duplicado.message", new String[]{"username"}, null);
            List<Rol> roles = usuarioDao.roles();
            modelo.addAttribute("roles", roles);

            Map<String, Boolean> seleccionados = new HashMap<>();
            for (UsuarioRol usuarioRol : usuario.getAuthorities()) {
                seleccionados.put(usuarioRol.getRol().getAuthority(), Boolean.TRUE);
            }
            modelo.addAttribute("seleccionados", seleccionados);
            return "admin/usuario/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "usuario.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{usuario.getUsername()});

        return "redirect:/admin/usuario/ver/" + usuario.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(@RequestParam Long id) {
        log.debug("Elimina usuario");
        try {
            usuarioDao.elimina(id);
        } catch (UltimoException e) {
            log.error("No se pudo eliminar el usuario " + id, e);
        }

        return "redirect:/admin/usuario";
    }
}
