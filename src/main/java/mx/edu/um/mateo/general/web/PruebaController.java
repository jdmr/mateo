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

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.dao.*;
import mx.edu.um.mateo.general.model.*;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inventario.dao.AlmacenDao;
import mx.edu.um.mateo.inventario.dao.EntradaDao;
import mx.edu.um.mateo.inventario.dao.ProductoDao;
import mx.edu.um.mateo.inventario.dao.TipoProductoDao;
import mx.edu.um.mateo.inventario.model.*;
import mx.edu.um.mateo.inventario.utils.NoCuadraException;
import mx.edu.um.mateo.inventario.utils.NoEstaAbiertaException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarException;
import mx.edu.um.mateo.inventario.utils.ProductoNoSoportaFraccionException;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    private static final Logger log = LoggerFactory.getLogger(PruebaController.class);
    @Autowired
    private OrganizacionDao organizacionDao;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private EmpresaDao empresaDao;
    @Autowired
    private ProveedorDao proveedorDao;
    @Autowired
    private TipoClienteDao tipoClienteDao;
    @Autowired
    private ClienteDao clienteDao;
    @Autowired
    private AlmacenDao almacenDao;
    @Autowired
    private TipoProductoDao tipoProductoDao;
    @Autowired
    private ProductoDao productoDao;
    @Autowired
    private EntradaDao entradaDao;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    @RequestMapping
    public String inicia() {
        return "/prueba/index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String guarda(
            @RequestParam String username,
            @RequestParam String password) {

        Transaction transaction = null;
        try {
            transaction = currentSession().beginTransaction();
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
            Estatus estatus2 = new Estatus(Constantes.PENDIENTE, 200);
            currentSession().save(estatus2);
            estatus2 = new Estatus(Constantes.CERRADA, 300);
            currentSession().save(estatus2);

            organizacion = new Organizacion("TEST", "TEST", "TEST");
            organizacionDao.crea(organizacion, usuario);

            Empresa emp = usuario.getEmpresa();
            Almacen alm = usuario.getAlmacen();
            Query query = currentSession().createQuery("select tp from TipoProducto tp where almacen.id = :almacenId");
            query.setLong("almacenId", alm.getId());
            TipoProducto tp = (TipoProducto) query.uniqueResult();

            NumberFormat nf = NumberFormat.getInstance();
            nf.setMinimumIntegerDigits(2);
            nf.setMaximumFractionDigits(0);
            nf.setGroupingUsed(false);
            for (int i = 1; i <= 29; i++) {
                String numero = nf.format(i);
                StringBuilder sb = new StringBuilder();
                sb.append("TST-").append(numero);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("TEST-").append(numero);
                Empresa empresa = new Empresa(sb.toString(), sb2.toString(), sb2.toString(), "0000000000" + numero, organizacion);
                empresaDao.crea(empresa);
            }

            Proveedor prov1 = null;
            for (int i = 1; i < 20; i++) {
                String numero = nf.format(i);
                StringBuilder sb = new StringBuilder();
                sb.append("TEST-").append(numero);
                Proveedor proveedor = new Proveedor(sb.toString(), sb.toString(), "0000000000" + numero, null);
                proveedor = proveedorDao.crea(proveedor, usuario);
                if (prov1 == null) {
                    prov1 = proveedor;
                }

                TipoCliente tipoCliente = new TipoCliente(sb.toString(), sb.toString(), new BigDecimal("0.16"), null);
                tipoClienteDao.crea(tipoCliente, usuario);

                Cliente cliente = new Cliente(sb.toString(), sb.toString(), "0000000000" + numero, tipoCliente, null);
                clienteDao.crea(cliente, usuario);

                Almacen almacen = new Almacen("TST-" + numero, sb.toString(), emp);
                almacenDao.crea(almacen);

                TipoProducto tipoProducto = new TipoProducto(sb.toString(), sb.toString(), alm);
                tipoProductoDao.crea(tipoProducto);

                Producto producto = new Producto("TST-" + numero, sb.toString(), sb.toString(), sb.toString(), tp, alm);
                productoDao.crea(producto);
            }

            Map<String, Object> params = productoDao.lista(null);
            int i = 0;
            for (Producto producto : (List<Producto>) params.get("productos")) {
                Entrada entrada = new Entrada("TEST" + (++i), "TEST" + i, new Date(), estatus, prov1, alm);
                entrada.setIva(new BigDecimal("16.00"));
                entrada.setTotal(new BigDecimal("116.00"));
                entrada = entradaDao.crea(entrada, usuario);
                LoteEntrada lote = new LoteEntrada(new BigDecimal("10"), new BigDecimal("10"), producto, entrada);
                entradaDao.creaLote(lote);
                currentSession().refresh(entrada);
                entradaDao.cierra(entrada, usuario);
            }
            
            transaction.commit();

        } catch (HibernateException | ProductoNoSoportaFraccionException | NoEstaAbiertaException | NoSePuedeCerrarException | NoCuadraException e) {
            log.error("No se pudo cargar los datos de prueba", e);
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("No se pudieron cargar los datos de prueba", e);
        }

        return "redirect:/";
    }
}
