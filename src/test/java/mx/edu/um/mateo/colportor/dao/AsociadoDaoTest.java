/*
 * No Paso Prueba,problemas con type long: asociado
 * 
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.*;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
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
public class AsociadoDaoTest extends BaseDaoTest {

    private static final Logger log = LoggerFactory.getLogger(AsociadoDao.class);
    @Autowired
    private AsociadoDao instance;
    
    /**
     * Test of lista method, of class AsociadoDao.
     */
    @Test
    public void testLista() {
        log.debug("Debiera mostrar lista de Asociado");
        Usuario usuario = obtieneAsociado();
        
        for (int i = 0; i < 20; i++) {
            Asociado asociado = new Asociado("test" + i + "@test.com", "test", "test", "test", "test",
                    Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE, Constantes.COLONIA,
                    Constantes.MUNICIPIO);
            
            asociado.setEmpresa(usuario.getEmpresa());
            asociado.setAlmacen(usuario.getAlmacen());
            currentSession().save(asociado);
            assertNotNull(asociado.getId());
        }
        Map<String, Object> params = new HashMap();
        params.put("empresa", usuario.getEmpresa().getId());
        
        Map result = instance.lista(params);
        
        assertNotNull(result.get(Constantes.ASOCIADO_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Asociado>) result.get(Constantes.ASOCIADO_LIST)).size());
        //assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void testObtiene() {
        log.debug("Debiera obtener asociado");
        Usuario usuario = obtieneAsociado();
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(usuario.getEmpresa());
        asociado.setAlmacen(usuario.getAlmacen());
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        Long id = asociado.getId();
        assertNotNull(id);

        Asociado result = instance.obtiene(id);
        assertNotNull(result);
        assertEquals(result, asociado);
    }

    @Test
    public void testCrea() {
        log.debug("Deberia crear asociado");

        Usuario usuario = obtieneAsociado();
        
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(usuario.getEmpresa());
        asociado.setAlmacen(usuario.getAlmacen());
        instance.crea(asociado, usuario);
        assertNotNull(asociado.getId());

        Asociado asociado2 = instance.obtiene(asociado.getId());

        assertNotNull(asociado2);
        assertNotNull(asociado2.getId());
    }

    @Test
    public void testActualiza() {
        log.debug("Deberia actualizar asociado");
        Usuario usuario = obtieneAsociado();
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(usuario.getEmpresa());
        asociado.setAlmacen(usuario.getAlmacen());
        currentSession().save(asociado);
        assertNotNull(asociado.getId());
        
        String nombre = "colonia";
        asociado.setColonia(nombre);

        Asociado asociado2 = instance.actualiza(asociado);
        assertNotNull(asociado2.getId());
        assertEquals(nombre, asociado.getColonia());

    }

    @Test
    public void testElimina() {
        log.debug("Debiera eliminar Asociado");

        String nom = Constantes.CLAVE;
        Usuario usuario = obtieneAsociado();
        
        Asociado asociado = new Asociado("test@test.com", "test", "test", "test", "test",
                Constantes.STATUS_ACTIVO, Constantes.CLAVE, Constantes.TELEFONO, Constantes.CALLE,
                Constantes.COLONIA, Constantes.MUNICIPIO);
        asociado.setEmpresa(usuario.getEmpresa());
        asociado.setAlmacen(usuario.getAlmacen());
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
