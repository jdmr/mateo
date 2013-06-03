/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.CobroCampo;
import mx.edu.um.mateo.inscripciones.model.Institucion;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.Prorroga;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class CobroCampoDaoTest extends BaseDaoTest {

    @Autowired
    private CobroCampoDao instance;

    /**
     * Test of lista method, of class CobroCampoDao.
     */
    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(institucion);
        assertNotNull(institucion.getId());

        CobroCampo cobroCampo = null;
        for (int i = 0; i < 20; i++) {
            cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
            cobroCampo.setEmpresa(usuario.getEmpresa());
            cobroCampo.setFechaAlta(new Date());
            cobroCampo.setFechaModificacion(new Date());
            cobroCampo.setStatus("A");
            cobroCampo.setUsuarioAlta(usuario);
            cobroCampo.setUsuarioModificacion(usuario);
            instance.graba(cobroCampo, usuario);
            assertNotNull(cobroCampo.getId());
        }
        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_COBROSCAMPOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<CobroCampo>) result.get(Constantes.CONTAINSKEY_COBROSCAMPOS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of obtiene method, of class CobroCampoDao.
     */
    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(institucion);
        assertNotNull(institucion.getId());

        CobroCampo cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(usuario.getEmpresa());
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());

        CobroCampo cobroCampo1 = instance.obtiene(cobroCampo.getId());
        assertEquals(cobroCampo.getMatricula(), cobroCampo1.getMatricula());

    }

    /**
     * Test of graba method, of class CobroCampoDao.
     */
    @Test
    public void testGraba() {
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(institucion);
        assertNotNull(institucion.getId());

        CobroCampo cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(usuario.getEmpresa());
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());

        CobroCampo cobroCampo1 = instance.obtiene(cobroCampo.getId());
        assertEquals(cobroCampo.getMatricula(), cobroCampo1.getMatricula());
    }

    /**
     * Prueba el proceso de actualizacion
     */
    @Test
    public void testActualiza() {
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(institucion);
        assertNotNull(institucion.getId());

        CobroCampo cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(usuario.getEmpresa());
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());

        CobroCampo cobroCampo1 = instance.obtiene(cobroCampo.getId());
        assertEquals(cobroCampo.getMatricula(), cobroCampo1.getMatricula());

        cobroCampo1.setMatricula("1110476");
        instance.graba(cobroCampo1, usuario);

        currentSession().refresh(cobroCampo);
        assertEquals("1110476", cobroCampo.getMatricula());
    }

    /**
     * Test of elimina method, of class CobroCampoDao.
     */
    @Test
    public void testElimina() {
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Nombre-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        currentSession().save(institucion);
        assertNotNull(institucion.getId());

        CobroCampo cobroCampo = new CobroCampo("1110475", institucion, new Double("8.00"), new Double("8.00"), new Double("8.00"));
        cobroCampo.setEmpresa(usuario.getEmpresa());
        cobroCampo.setFechaAlta(new Date());
        cobroCampo.setFechaModificacion(new Date());
        cobroCampo.setStatus("A");
        cobroCampo.setUsuarioAlta(usuario);
        cobroCampo.setUsuarioModificacion(usuario);
        instance.graba(cobroCampo, usuario);
        assertNotNull(cobroCampo.getId());
        String matricula = instance.elimina(cobroCampo.getId());
        assertEquals(matricula, cobroCampo.getMatricula());
        CobroCampo cobroCampo1 = instance.obtiene(cobroCampo.getId());
        if ("A".equals(cobroCampo.getStatus())) {
            fail("Se encontro cobroCampo " + cobroCampo1);
        } else {
            log.debug("Se elimino con exito el Cobro Campo {}", matricula);
        }


    }
}
