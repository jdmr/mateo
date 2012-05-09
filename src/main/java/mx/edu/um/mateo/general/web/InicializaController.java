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

import mx.edu.um.mateo.contabilidad.dao.LibroDao;
import mx.edu.um.mateo.contabilidad.model.Libro;
import mx.edu.um.mateo.general.dao.OrganizacionDao;
import mx.edu.um.mateo.general.dao.ReporteDao;
import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.inventario.model.Estatus;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author jdmr
 */
@Controller
@RequestMapping("/inicializa")
public class InicializaController {

    private static final Logger log = LoggerFactory.getLogger(InicializaController.class);
    @Autowired
    private OrganizacionDao organizacionDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private ReporteDao reporteDao;
    @Autowired
    private LibroDao libroDao;

    @RequestMapping
    public String inicia() {
        return "/inicializa/index";
    }

    @Transactional
    @RequestMapping(method = RequestMethod.POST)
    public String guarda(
            @RequestParam String username,
            @RequestParam String password) {

        Transaction transaction = null;
        try {
            transaction = currentSession().beginTransaction();
            reporteDao.inicializa();
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

            Estatus estatus = new Estatus(Constantes.ABIERTA, 100);
            currentSession().save(estatus);
            estatus = new Estatus(Constantes.PENDIENTE, 200);
            currentSession().save(estatus);
            estatus = new Estatus(Constantes.CERRADA, 300);
            currentSession().save(estatus);
            estatus = new Estatus(Constantes.FACTURADA, 400);
            currentSession().save(estatus);
            estatus = new Estatus(Constantes.CANCELADA, 500);
            currentSession().save(estatus);

            Libro libroCaja = new Libro("Caja", "20", "AC", 20, organizacion);
            libroDao.crea(libroCaja, usuario);
            currentSession().save(libroCaja);
            Libro libroChequera = new Libro("Chequera", "30", "AC", 30, organizacion);
            libroDao.crea(libroChequera, usuario);
            currentSession().save(libroChequera);
            Libro libroSaldosIniciales = new Libro("Saldos Iniciales", "40", "AC", 40, organizacion);
            libroDao.crea(libroSaldosIniciales, usuario);
            currentSession().save(libroSaldosIniciales);

            transaction.commit();


        } catch (Exception e) {
            log.error("No se pudo inicializar la aplicacion", e);
            transaction.rollback();
        }

        return "redirect:/";
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}
