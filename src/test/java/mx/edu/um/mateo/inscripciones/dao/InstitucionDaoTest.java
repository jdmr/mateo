package mx.edu.um.mateo.inscripciones.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.Institucion;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

/**
 * @author semdariobarbaamaya
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class InstitucionDaoTest extends BaseDaoTest {

    @Autowired
    private InstitucionDao instance;

    @SuppressWarnings("unchecked")
    @Test
    public void debieraMostrarListaDeInstitucion() {
        log.debug("Debiera mostrar lista de institucion");
        Usuario usuario = obtieneUsuario();
        Institucion institucion = null;
        for (int i = 0; i < 20; i++) {
            institucion = new Institucion();
            institucion.setNombre("Nombre-test");
            institucion.setPorcentaje(new BigDecimal("123"));
            institucion.setStatus("A");
            institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
            instance.graba(institucion);
            assertNotNull(institucion.getId());
        }
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_INSTITUCION));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<Institucion>) result.get(Constantes.CONTAINSKEY_INSTITUCION)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void debieraObtenerInstitucion() {
        log.debug("Debiera obtener Institucion");
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Institucion-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        instance.graba(institucion);
        assertNotNull(institucion.getId());
        Long id = institucion.getId();
        Institucion result = instance.obtiene(id);
        assertNotNull(result.getId());
        assertEquals("Institucion-test", result.getNombre());
    }

    @Test
    public void debieraCrearInstitucion() {
        log.debug("Debiera crear institucion");
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Institucion-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        instance.graba(institucion);
        assertNotNull(institucion.getId());
        Long id = institucion.getId();

        Institucion result = instance.obtiene(id);
        assertNotNull(result.getId());
        assertEquals(institucion.getNombre(), result.getNombre());
    }

    @Test
    public void debieraActualizarInstitucion() {
        log.debug("Debiera actualizar institucion");
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Institucion-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        instance.graba(institucion);
        assertNotNull(institucion.getId());
        Long id = institucion.getId();

        Institucion result = instance.obtiene(id);
        assertNotNull(result.getId());
        assertEquals(institucion.getNombre(), result.getNombre());

        result.setNombre("PruebaNombre");
        instance.actualiza(result);

        currentSession().refresh(institucion);
        assertEquals(result.getNombre(), institucion.getNombre());
    }

    @Test
    public void debieraEliminarInstitucion() {
        log.debug("Debiera actualizar institucion");
        Usuario usuario = obtieneUsuario();
        Institucion institucion = new Institucion();
        institucion.setNombre("Institucion-test");
        institucion.setPorcentaje(new BigDecimal("123"));
        institucion.setStatus("A");
        institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
        instance.graba(institucion);
        assertNotNull(institucion.getId());
        Long id = institucion.getId();

        institucion.setNombre("PRUEBA");
        instance.graba(institucion);
        assertNotNull(institucion.getId());
        String nombre = instance.elimina(institucion.getId());
        assertEquals("PRUEBA", nombre);
        try {
            Institucion institucion1 = instance.obtiene(institucion.getId());
            fail("Se encontro paquete " + institucion1);
        } catch (ObjectRetrievalFailureException e) {
            log.debug("Se elimino con exito el paquete {}", nombre);
        }
    }
}
