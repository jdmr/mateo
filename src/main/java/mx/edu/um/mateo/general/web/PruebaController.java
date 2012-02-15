/*
 * The MIT License
 *
 * Copyright 2012 J. David Mendoza <jdmendoza@um.edu.mx>.
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

import java.text.NumberFormat;
import mx.edu.um.mateo.general.dao.EmpresaDao;
import mx.edu.um.mateo.general.dao.OrganizacionDao;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/prueba")
public class PruebaController {

    @Autowired
    private OrganizacionDao organizacionDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private EmpresaDao empresaDao;

    @RequestMapping
    public String inicia() {
        return "/prueba/index";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public String guarda(
            @RequestParam String username,
            @RequestParam String password) {
        Organizacion organizacion = new Organizacion("UM", "UM", "Universidad de Montemorelos");
        organizacion = organizacionDao.crea(organizacion);
        Rol rol = new Rol("ROLE_ADMIN");
        rol = rolDao.crea(rol);
        Usuario usuario = new Usuario(
                username,
                password,
                "Admin",
                "User");
        Long almacenId = 0l;
        actualizaUsuario:
        for (Empresa empresa : organizacion.getEmpresas()) {
            for (Almacen almacen : empresa.getAlmacenes()) {
                almacenId = almacen.getId();
                break actualizaUsuario;
            }
        }
        usuarioDao.crea(usuario, almacenId, new String[]{rol.getAuthority()});
        rol = new Rol("ROLE_ORG");
        rolDao.crea(rol);
        rol = new Rol("ROLE_EMP");
        rolDao.crea(rol);
        rol = new Rol("ROLE_USER");
        rolDao.crea(rol);

        organizacion = new Organizacion("TEST","TEST","TEST");
        organizacionDao.crea(organizacion, usuario);

        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(2);
        nf.setMaximumFractionDigits(0);
        nf.setGroupingUsed(false);
        for (int i = 1; i <= 30; i++) {
            String numero = nf.format(i);
            StringBuilder sb = new StringBuilder();
            sb.append("TST-").append(numero);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("TEST-").append(numero);
            Empresa empresa = new Empresa(sb.toString(), sb2.toString(), sb2.toString(), "000000000001", organizacion);
            empresaDao.crea(empresa);
        }
        
        return "redirect:/";
    }
}
