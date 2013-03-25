/*
 * No Paso Prueba,problemas con type long: asociado
 * 
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.*;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.colportor.utils.FaltaAsociacionException;
import mx.edu.um.mateo.colportor.utils.UltimoException;
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
 * @author gibrandemetrioo
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class AsociadoDaoTest {

    private static final Logger log = LoggerFactory.getLogger(AsociadoDao.class);
    @Autowired
    private AsociadoDao instance;
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Test of lista method, of class AsociadoDao.
     */
    @Test
    public void debieraMostrarListaDeAsociado() {
        log.debug("Debiera mostrar lista de Asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        for (int i = 0; i < 20; i++) {
            Asociado asociado = new Asociado("test" + i + "@test.com", "test", "test", "test", "test",
                    Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                    Constantes.MUNICIPIO);
            
            asociado.setEmpresa(empresa);
            asociado.setAlmacen(almacen);
            asociado.setAsociacion(asociacion);
            currentSession().save(asociado);
            assertNotNull(asociado.getId());
        }
        Map<String, Object> params = new HashMap();
        params.put(Constantes.ADDATTRIBUTE_ASOCIACION, asociacion);
        Map result=null;
        try {
            result = instance.lista(params);
        } catch (FaltaAsociacionException ex) {
            log.error("Falta asociacion", ex);
        }
        assertNotNull(result.get(Constantes.CONTAINSKEY_ASOCIADOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Asociado>) result.get(Constantes.CONTAINSKEY_ASOCIADOS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerAsociado() {
        log.debug("Debiera obtener asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol("ROLE_ASO");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        String nombre = Constantes.MUNICIPIO;
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        Long id = asociado.getId();
        assertNotNull(id);

        Asociado result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(nombre, result.getMunicipio());
        assertEquals(result, asociado);
    }

    @Test
    public void deberiaCrearAsociado() {
        log.debug("Deberia crear asociado");

        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);

        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());

        Asociado asociado2 = instance.obtiene(asociado.getId());

        assertNotNull(asociado2);
        assertNotNull(asociado2.getId());
    }

    @Test
    public void deberiaActualizarAsociado() {
        log.debug("Deberia actualizar asociado");
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        currentSession().save(asociado);

        String nombre = "colonia";
        asociado.setColonia(nombre);

        Asociado asociado2 = instance.actualiza(asociado);
        assertNotNull(asociado2.getId());
        assertEquals(nombre, asociado.getColonia());

        assertEquals(asociado, asociado2);
    }

    @Test
    public void deberiaEliminarAsociado() throws UltimoException {
        log.debug("Debiera eliminar Asociado");

        String nom = Constantes.CLAVE;
        Union union = new Union("test");
        union.setStatus(Constantes.STATUS_ACTIVO);
        currentSession().save(union);
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Rol rol = new Rol(Constantes.ROLE_ASO);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Asociacion asociacion = new Asociacion("TEST01", Constantes.STATUS_ACTIVO, union);
        currentSession().save(asociacion);
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(empresa);
        asociado.setAlmacen(almacen);
        asociado.setAsociacion(asociacion);
        currentSession().save(asociado);
        assertNotNull(asociado.getId());

        String clave = instance.elimina(asociado.getId());
        assertEquals(nom, clave);

        Asociado prueba = instance.obtiene(asociado.getId());

        if (prueba.getStatus() != Constantes.STATUS_INACTIVO) {
            fail("Fallo prueba Eliminar");
        }
    }
}
