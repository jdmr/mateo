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
package mx.edu.um.mateo.colportor.dao;

import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Asociacion;
import java.util.*;
import mx.edu.um.mateo.colportor.utils.FaltaAsociacionException;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 *
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ColportorDaoTest {

    private static final Logger log = LoggerFactory.getLogger(ColportorDaoTest.class);
    @Autowired
    private ColportorDao colportorDao;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class ColportorDao.
     */
    @Test
    public void debieraMostrarListaDeColportor() {
        log.debug("Debiera mostrar lista de Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        for (int i = 0; i < 20; i++) {

            Colportor colportor = new Colportor("test" + i + "@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                    "8262652626", "test", "test", "10706" + i, "test", "test001", new Date());
            colportor.setEmpresa(empresa);
            colportor.setAlmacen(almacen);
            colportor.setAsociacion(asociacion);
            currentSession().save(colportor);
            assertNotNull(colportor.getId());



        }
        Map<String, Object> params = new HashMap();
        params.put(Constantes.ADDATTRIBUTE_ASOCIACION, asociacion);
        Map result=null;
        try {
            result = colportorDao.lista(params);
        } catch (FaltaAsociacionException ex) {
            log.error("Falta asociacion", ex);
        }
        assertNotNull(result.get(Constantes.CONTAINSKEY_COLPORTORES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Colportor>) result.get(Constantes.CONTAINSKEY_COLPORTORES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    public void debieraBuscarUnColportorYMostrarLaListaDeColportores() {
        log.debug("Debiera buscar un Colportor y mostrar lista de Colportores");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        for (int i = 0; i < 10; i++) {

            Colportor colportor = new Colportor("test" + i + "@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                    "8262652626", "test", "test", "10706" + i, "test", "test001", new Date());
            colportor.setEmpresa(empresa);
            colportor.setAlmacen(almacen);
            colportor.setAsociacion(asociacion);
            currentSession().save(colportor);
            assertNotNull(colportor.getId());



        }
        String filtro="test2";
        Map<String, Object> params = new HashMap();
        params.put(Constantes.ADDATTRIBUTE_ASOCIACION, asociacion);
        Map result=null;
        try {
            result = colportorDao.lista(params);
        } catch (FaltaAsociacionException ex) {
            log.error("Falta asociacion", ex);
        }
        params.put("filtro", filtro);
        
        assertNotNull(result.get(Constantes.CONTAINSKEY_COLPORTORES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(2, ((List<Colportor>) result.get(Constantes.CONTAINSKEY_COLPORTORES)).size());
        assertEquals(10, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Cuadno no hay una asociacion en session
     */
    @Test    
    public void debieraMostrarListaVasiaDeColportor() {
        log.debug("Debiera mostrar lista vasia de Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        for (int i = 0; i < 10; i++) {

            Colportor colportor = new Colportor("test" + i + "@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                    "8262652626", "test", "test", "10706" + i, "test", "test001", new Date());
            colportor.setEmpresa(empresa);
            colportor.setAlmacen(almacen);
            colportor.setAsociacion(asociacion);
            currentSession().save(colportor);
            assertNotNull(colportor.getId());



        }
        Map<String, Object> params = null;
        Map result=null;
        try {
            result = colportorDao.lista(params);
            fail("La prueba debio haber regresado al excepcion de Falta Asociacion");
        } catch (FaltaAsociacionException ex) {
            log.error("Paso con exito", ex);
        }}

    @Test
    public void debieraObtenerColportor() {
        log.debug("Debiera obtener un Colportor");

        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Colportor colportor = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setEmpresa(empresa);
        colportor.setAlmacen(almacen);
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);
        Long id = colportor.getId();
        assertNotNull(id);

        Colportor result = colportorDao.obtiene(id);
        assertNotNull(result);
        assertEquals(result, colportor);
        assertEquals("test", result.getClave());
    }
//

    @Test
    public void deberiaCrearColportor() {
        log.debug("Deberia crear un Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol("ROLE_CLP"); //Rol Colportor
        currentSession().save(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Colportor colportor = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setEmpresa(empresa);
        colportor.setAlmacen(almacen);
        colportor.setAsociacion(asociacion);
        colportorDao.crea(colportor, null);
        Colportor colportor2 = colportorDao.obtiene(colportor.getId());
        assertNotNull(colportor2);
        assertNotNull(colportor2.getId());

        assertEquals(colportor, colportor2);
    }
//

    @Test
    public void deberiaActualizarColportor() {
        log.debug("Deberia actualizar Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Colportor colportor = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setEmpresa(empresa);
        colportor.setAlmacen(almacen);
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);

        String colonia = Constantes.COLONIA;
        colportor.setColonia(colonia);

        Colportor colportor2 = colportorDao.actualiza(colportor);
        assertNotNull(colportor2);
        assertEquals(colonia, colportor.getColonia());

        assertEquals(colportor, colportor2);
    }
//

    @Test
    public void deberiaEliminarColportor() {
        log.debug("Debiera eliminar Colportor");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Colportor colportor = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setEmpresa(empresa);
        colportor.setAlmacen(almacen);
        colportor.setAsociacion(asociacion);
        currentSession().save(colportor);
        assertNotNull(colportor.getId());

        String clave = colportorDao.elimina(colportor.getId());
        assertEquals(colportor.getClave(), clave);

        colportor = colportorDao.obtiene(colportor.getId());

        assertEquals(Constantes.STATUS_INACTIVO, (colportor).getStatus());
 
        if((colportor).getStatus() != Constantes.STATUS_INACTIVO){
            fail("Fallo prueba Eliminar");
        }
    }
}
