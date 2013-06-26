/*
 * TODO problemas con el constructor
 * 
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.InformeMensual;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 *
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class InformeMensualDaoTest extends BaseDaoTest {

    @Autowired
    private InformeMensualDao instance;
    
    @Test
    public void testList() {
        log.debug("test lista de informeMensual");

        Usuario colportor = obtieneColportor();
                
        InformeMensual informe = null;
        for (int i = 0; i < 20; i++) {
            informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
            currentSession().save(informe);
            assertNotNull(informe.getId());

        }

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("empresa", colportor.getEmpresa().getId());
        Map result = instance.lista(params);
        
        assertNotNull(result.get(Constantes.INFORMEMENSUAL_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<InformeMensual>) result.get(Constantes.INFORMEMENSUAL_LIST)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void testObtiene() {
        log.debug("test obtener informeMensual");

        Usuario colportor = obtieneColportor();
        
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
        Long id = informe.getId();

        InformeMensual result = instance.obtiene(id);
        assertNotNull(result);

        assertEquals(result, informe);
    }

    @Test
    public void testCrear() {
        log.debug("test crear InformeMensual");

        Usuario colportor = obtieneColportor();
                
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());

    }

    @Test
    public void testActualizar() {
        log.debug("test actualizar InformeMensual");

        Usuario colportor = obtieneColportor();
                
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());

        InformeMensual informe2 = instance.obtiene(informe.getId());
        informe2.setStatus(Constantes.STATUS_CANCELADO);

        InformeMensual informe3 = instance.crea(informe2);
        assertNotNull(informe3);

        assertEquals(informe2, informe2);
    }

    @Test
    public void testElimina() throws UltimoException {
        log.debug("test eliminar InformeMensual");
        
        Usuario colportor = obtieneColportor();
                
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
        
        String descripcion = instance.elimina(informe.getId());

        InformeMensual prueba = instance.obtiene(informe.getId());
        if (prueba != null) {
            fail("Fallo prueba Eliminar");
        }
    }
}
