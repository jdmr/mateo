/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.AFEPlaza;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class AfePlazaDaoTest extends BaseDaoTest {

    @Autowired
    private AFEPlazaDao instance;

    /**
     * Prueba la lista de paquetes
     */
    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        AFEPlaza afePlaza = null;
        for (int i = 0; i < 20; i++) {
            afePlaza = new AFEPlaza();
            afePlaza.setClave("1110475");
            afePlaza.setDias("23");
            afePlaza.setEmail("samuel.9401@gmail.com");
            afePlaza.setEmpresa(usuario.getEmpresa());
            afePlaza.setFechaAlta(new Date());
            afePlaza.setFechaModificacion(new Date());
            afePlaza.setPrimerIngreso(false);
            afePlaza.setRequisitos("Estudiante");
            afePlaza.setTipoPlaza("Ayudante General");
            afePlaza.setTurno("Matutino");
            afePlaza.setIndustrial(true);
            afePlaza.setObservaciones("prueba");
            afePlaza.setUsuarioAlta(usuario);
            afePlaza.setUsuarioModificacion(usuario);
            instance.crea(afePlaza, usuario);
            assertNotNull(afePlaza.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_AFEPLAZAS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<TiposBecas>) result.get(Constantes.CONTAINSKEY_AFEPLAZAS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Prueba el obtener un paquete
     */
    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        AFEPlaza afePlaza = new AFEPlaza();
        afePlaza.setClave("1110475");
        afePlaza.setDias("23");
        afePlaza.setEmail("samuel.9401@gmail.com");
        afePlaza.setEmpresa(usuario.getEmpresa());
        afePlaza.setFechaAlta(new Date());
        afePlaza.setFechaModificacion(new Date());
        afePlaza.setPrimerIngreso(false);
        afePlaza.setRequisitos("Estudiante");
        afePlaza.setTipoPlaza("Ayudante General");
        afePlaza.setTurno("Matutino");
        afePlaza.setIndustrial(true);
        afePlaza.setObservaciones("prueba");
        afePlaza.setUsuarioAlta(usuario);
        afePlaza.setUsuarioModificacion(usuario);
        instance.crea(afePlaza, usuario);
        assertNotNull(afePlaza.getId());

        AFEPlaza afePlaza1 = instance.obtiene(afePlaza.getId());
        assertEquals(afePlaza.getClave(), afePlaza1.getClave());

    }

    /**
     * Prueba el proceso de creacion del paquete
     */
    @Test
    public void testCrea() {
        Usuario usuario = obtieneUsuario();
        AFEPlaza afePlaza = new AFEPlaza();
        afePlaza.setClave("1110475");
        afePlaza.setDias("23");
        afePlaza.setIndustrial(true);
        afePlaza.setObservaciones("prueba");
        afePlaza.setEmail("samuel.9401@gmail.com");
        afePlaza.setEmpresa(usuario.getEmpresa());
        afePlaza.setFechaAlta(new Date());
        afePlaza.setFechaModificacion(new Date());
        afePlaza.setPrimerIngreso(false);
        afePlaza.setRequisitos("Estudiante");
        afePlaza.setTipoPlaza("Ayudante General");
        afePlaza.setTurno("Matutino");
        afePlaza.setUsuarioAlta(usuario);
        afePlaza.setUsuarioModificacion(usuario);
        instance.crea(afePlaza, usuario);
        assertNotNull(afePlaza.getId());

        AFEPlaza afePlaza1 = instance.obtiene(afePlaza.getId());
        assertEquals(afePlaza.getClave(), afePlaza1.getClave());
    }

    /**
     * Prueba el proceso de actualizacion
     */
    @Test
    public void testActualiza() {
        Usuario usuario = obtieneUsuario();
        AFEPlaza afePlaza = new AFEPlaza();
        afePlaza.setClave("1110475");
        afePlaza.setDias("23");
        afePlaza.setEmail("samuel.9401@gmail.com");
        afePlaza.setEmpresa(usuario.getEmpresa());
        afePlaza.setFechaAlta(new Date());
        afePlaza.setFechaModificacion(new Date());
        afePlaza.setPrimerIngreso(false);
        afePlaza.setRequisitos("Estudiante");
        afePlaza.setTipoPlaza("Ayudante General");
        afePlaza.setTurno("Matutino");
        afePlaza.setUsuarioAlta(usuario);
        afePlaza.setUsuarioModificacion(usuario);
        instance.crea(afePlaza, usuario);
        assertNotNull(afePlaza.getId());

        AFEPlaza afePlaza1 = instance.obtiene(afePlaza.getId());
        assertEquals(afePlaza.getClave(), afePlaza1.getClave());

        afePlaza1.setObservaciones("test2");
        instance.actualiza(afePlaza1, usuario);

        currentSession().refresh(afePlaza);
        assertEquals("test2", afePlaza.getObservaciones());
    }

    /**
     * Prueba el eliminar el paquete
     */
    @Test
    public void testElimina() {
        Usuario usuario = obtieneUsuario();
        AFEPlaza afePlaza = new AFEPlaza();
        afePlaza.setClave("1110475");
        afePlaza.setDias("23");
        afePlaza.setEmail("samuel.9401@gmail.com");
        afePlaza.setEmpresa(usuario.getEmpresa());
        afePlaza.setFechaAlta(new Date());
        afePlaza.setFechaModificacion(new Date());
        afePlaza.setPrimerIngreso(false);
        afePlaza.setRequisitos("Estudiante");
        afePlaza.setTipoPlaza("Ayudante General");
        afePlaza.setTurno("Matutino");
        afePlaza.setUsuarioAlta(usuario);
        afePlaza.setUsuarioModificacion(usuario);
        instance.crea(afePlaza, usuario);
        assertNotNull(afePlaza.getId());

        String tipoPlaza = instance.elimina(afePlaza.getId());
        assertEquals(afePlaza.getTipoPlaza(), tipoPlaza);

        try {
            AFEPlaza afePlaza1 = instance.obtiene(afePlaza.getId());
            if ("A".equals(afePlaza1.getStatus())) {
                fail("Se encontro afePlaza " + afePlaza1);
            }
        } catch (ObjectRetrievalFailureException e) {
            log.debug("Se elimino con exito la Plaza {}", tipoPlaza);
        }
    }
}
