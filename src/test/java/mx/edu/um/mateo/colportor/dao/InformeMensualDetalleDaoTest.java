/*
 * TODO problemas con el constructor
 * 
 */
package mx.edu.um.mateo.colportor.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.InformeMensual;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;
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
public class InformeMensualDetalleDaoTest extends BaseDaoTest {

    @Autowired
    private InformeMensualDetalleDao instance;
    
    @Test
    public void testList() {
        log.debug("test lista de informeMensualDetalle");

        Usuario colportor = obtieneColportor();
        
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
                
        InformeMensualDetalle detalle = null;
        for (int i = 0; i < 20; i++) {
            //InformeMensual informe, Date fecha, Double hrsTrabajadas, Integer librosRegalados, BigDecimal totalPedidos, BigDecimal totalVentas, Integer literaturaGratis, Integer oracionesOfrecidas, Integer casasVisitadas, Integer contactosEstudiosBiblicos, Integer bautizados, Usuario capturo, Date cuando

            detalle = new InformeMensualDetalle(informe, new Date(), 2.5, 2, new BigDecimal("254"), new BigDecimal("521"), 2, 3, 1, 2, 3, colportor, new Date());
            currentSession().save(detalle);
            assertNotNull(detalle.getId());

        }

        Map<String, Object> params = new TreeMap<String, Object>();
        params.put("informe", informe.getId());
        Map result = instance.lista(params);
        
        assertNotNull(result.get(Constantes.INFORMEMENSUAL_DETALLE_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<InformeMensual>) result.get(Constantes.INFORMEMENSUAL_DETALLE_LIST)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void testObtiene() {
        log.debug("test obtener informeMensualDetalle");

        Usuario colportor = obtieneColportor();
        
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
        
        InformeMensualDetalle detalle = new InformeMensualDetalle(informe, new Date(), 2.5, 2, new BigDecimal("254"), new BigDecimal("521"), 2, 3, 1, 2, 3, colportor, new Date());
        currentSession().save(detalle);
        assertNotNull(detalle.getId());

        InformeMensualDetalle result = instance.obtiene(detalle.getId());
        assertNotNull(result);

        assertEquals(result, detalle);
    }

    @Test
    public void testCrear() {
        log.debug("test crear InformeMensualDetalle");

        Usuario colportor = obtieneColportor();
                
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
        
        InformeMensualDetalle detalle = new InformeMensualDetalle(informe, new Date(), 2.5, 2, new BigDecimal("254"), new BigDecimal("521"), 2, 3, 1, 2, 3, colportor, new Date());
        instance.crea(detalle);
        assertNotNull(detalle.getId());

    }

    @Test
    public void testActualizar() {
        log.debug("test actualizar InformeMensualDetalle");

        Usuario colportor = obtieneColportor();
                
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
        
        InformeMensualDetalle detalle = new InformeMensualDetalle(informe, new Date(), 2.5, 2, new BigDecimal("254"), new BigDecimal("521"), 2, 3, 1, 2, 3, colportor, new Date());
        instance.crea(detalle);
        assertNotNull(detalle.getId());
        
        detalle.setBautizados(100);
        instance.crea(detalle);
        
        

        assertEquals(detalle, instance.obtiene(detalle.getId()));
    }

    @Test
    public void testElimina() throws UltimoException {
        log.debug("test eliminar InformeMensualDetalle");
        
        Usuario colportor = obtieneColportor();
                
        InformeMensual informe = new InformeMensual((Colportor)colportor, new Date(), Constantes.STATUS_ACTIVO, colportor, new Date());
        currentSession().save(informe);
        assertNotNull(informe.getId());
        
        InformeMensualDetalle detalle = new InformeMensualDetalle(informe, new Date(), 2.5, 2, new BigDecimal("254"), new BigDecimal("521"), 2, 3, 1, 2, 3, colportor, new Date());
        instance.crea(detalle);
        assertNotNull(detalle.getId());
        
        String descripcion = instance.elimina(detalle.getId());

        InformeMensualDetalle prueba = instance.obtiene(detalle.getId());
        if (prueba != null) {
            fail("Fallo prueba Eliminar");
        }
    }
}
