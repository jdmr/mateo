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
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        modelo.addAttribute("usuarios",params.get("usuarios"));
        return "admin/usuario/lista";
    }
    
    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando usuario {}",id);
        Usuario usuario = usuarioDao.obtiene(id);
        List<Rol> roles = usuarioDao.roles();
        List<Rol> rolesUsuario = usuario.getAuthorities();
        List<Rol> seleccionados = new ArrayList<>();
        for(Rol rol : roles) {
            if (rolesUsuario.contains(rol)) {
                seleccionados.add(rol);
            }
        }
        modelo.addAttribute("usuario",usuario);
        modelo.addAttribute("roles",usuarioDao.roles());
        for(Rol rol : seleccionados) {
            modelo.addAttribute(rol.getAuthority(),true);
        }
        return "admin/usuario/ver";
    }
}
