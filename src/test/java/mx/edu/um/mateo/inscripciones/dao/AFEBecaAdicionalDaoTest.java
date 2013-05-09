/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.AFEBecaAdicional;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class AFEBecaAdicionalDaoTest extends BaseDaoTest {

    @Autowired
    private AFEBecaAdicionalDao instance;

    /**
     * Prueba la lista de paquetes
     */
    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        AFEBecaAdicional becaAdicional = null;
        TiposBecas tipoBeca = new TiposBecas();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(false);
        tiposBecas.setPorcentaje(new BigDecimal(320));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(350));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tipoBeca);
        for (int i = 0; i < 20; i++) {
            becaAdicional = new AFEBecaAdicional();
            becaAdicional.setEmpresa(usuario.getEmpresa());
            becaAdicional.setFechaAlta(new Date());
            becaAdicional.setImporte(new BigDecimal("20.8"));
            becaAdicional.setMatricula("1110475");
            becaAdicional.setStatus("A");
            becaAdicional.setTiposBecas(tipoBeca);
            instance.crea(becaAdicional, usuario);
            assertNotNull(becaAdicional.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_BECASADICIONALES));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<AFEBecaAdicional>) result.get(Constantes.CONTAINSKEY_BECASADICIONALES)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Prueba el obtener un paquete
     */
    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        String matricula = "1110475";

        TiposBecas tipoBeca = new TiposBecas();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(false);
        tiposBecas.setPorcentaje(new BigDecimal(320));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(350));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tipoBeca);

        AFEBecaAdicional becaAdicional = new AFEBecaAdicional();
        becaAdicional.setEmpresa(usuario.getEmpresa());
        becaAdicional.setFechaAlta(new Date());
        becaAdicional.setImporte(new BigDecimal("20.8"));
        becaAdicional.setMatricula(matricula);
        becaAdicional.setStatus("A");
        becaAdicional.setTiposBecas(tipoBeca);
        instance.crea(becaAdicional, usuario);
        assertNotNull(becaAdicional.getId());

        AFEBecaAdicional becaAdicional1 = instance.obtiene(becaAdicional.getId());
        assertEquals(becaAdicional.getMatricula(), becaAdicional1.getMatricula());

    }

    /**
     * Prueba el proceso de creacion del paquete
     */
    @Test
    public void testCrea() {
        Usuario usuario = obtieneUsuario();
        String matricula = "1110475";

        TiposBecas tipoBeca = new TiposBecas();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(false);
        tiposBecas.setPorcentaje(new BigDecimal(320));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(350));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tipoBeca);

        AFEBecaAdicional becaAdicional = new AFEBecaAdicional();
        becaAdicional.setEmpresa(usuario.getEmpresa());
        becaAdicional.setFechaAlta(new Date());
        becaAdicional.setImporte(new BigDecimal("20.8"));
        becaAdicional.setMatricula(matricula);
        becaAdicional.setStatus("A");
        becaAdicional.setTiposBecas(tipoBeca);
        instance.crea(becaAdicional, usuario);
        assertNotNull(becaAdicional.getId());

        AFEBecaAdicional becaAdicional1 = instance.obtiene(becaAdicional.getId());
        assertEquals(becaAdicional.getMatricula(), becaAdicional1.getMatricula());
    }

    /**
     * Prueba el proceso de actualizacion
     */
    @Test
    public void testActualiza() {
        Usuario usuario = obtieneUsuario();
        String matricula = "1110475";

        TiposBecas tipoBeca = new TiposBecas();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(false);
        tiposBecas.setPorcentaje(new BigDecimal(320));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(350));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tipoBeca);

        AFEBecaAdicional becaAdicional = new AFEBecaAdicional();
        becaAdicional.setEmpresa(usuario.getEmpresa());
        becaAdicional.setFechaAlta(new Date());
        becaAdicional.setImporte(new BigDecimal("20.8"));
        becaAdicional.setMatricula(matricula);
        becaAdicional.setStatus("A");
        becaAdicional.setTiposBecas(tipoBeca);
        instance.crea(becaAdicional, usuario);
        assertNotNull(becaAdicional.getId());

        AFEBecaAdicional becaAdicional1 = instance.obtiene(becaAdicional.getId());
        assertEquals(becaAdicional.getMatricula(), becaAdicional1.getMatricula());

        becaAdicional1.setMatricula("1110476");
        instance.actualiza(becaAdicional1, usuario);

        currentSession().refresh(becaAdicional);
        assertEquals("1110475", becaAdicional.getMatricula());
    }

    /**
     * Prueba el eliminar el paquete
     */
    @Test
    public void testElimina() {
        Usuario usuario = obtieneUsuario();

        TiposBecas tipoBeca = new TiposBecas();
        TiposBecas tiposBecas = new TiposBecas();
        tiposBecas.setDescripcion("test");
        tiposBecas.setDiezma(true);
        tiposBecas.setNumHoras(320);
        tiposBecas.setPerteneceAlumno(false);
        tiposBecas.setPorcentaje(new BigDecimal(320));
        tiposBecas.setSoloPostgrado(false);
        tiposBecas.setStatus("a");
        tiposBecas.setTope(new BigDecimal(350));
        tiposBecas.setEmpresa(usuario.getEmpresa());
        currentSession().save(tipoBeca);

        AFEBecaAdicional becaAdicional = new AFEBecaAdicional();
        becaAdicional.setEmpresa(usuario.getEmpresa());
        becaAdicional.setFechaAlta(new Date());
        becaAdicional.setImporte(new BigDecimal("20.8"));
        becaAdicional.setMatricula("1110475");
        becaAdicional.setStatus("A");
        becaAdicional.setTiposBecas(tipoBeca);
        instance.crea(becaAdicional, usuario);
        assertNotNull(becaAdicional.getId());

        String matricula = instance.elimina(becaAdicional.getId());
        assertEquals(becaAdicional.getMatricula(), matricula);

        if ("A".equals(becaAdicional.getStatus())) {
            fail("Se encontro cobroCampo " + becaAdicional);
        } else {
            log.debug("Se elimino con exito el Cobro Campo {}", matricula);
        }
    }
}
