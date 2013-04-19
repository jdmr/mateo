/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.dao;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.contabilidad.model.OrdenPago;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import org.apache.commons.beanutils.BeanUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author osoto
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class OrdenPagoDaoTest extends BaseDaoTest {
    private OrdenPago ordenPago = null;
    Usuario usuario = null;

    @Autowired
    private OrdenPagoDao instance;
    
    private void inserta(){
        usuario = obtieneUsuario();
        ordenPago = new OrdenPago();
        ordenPago.setDescripcion("Test");
        ordenPago.setCheque(false);
        ordenPago.setFechaPago(new Date());
        ordenPago.setEmpresa(usuario.getEmpresa());
        ordenPago.setStatus("A");
        ordenPago.setUserCaptura(usuario);
        ordenPago.setFechaCaptura(new Date());
        ordenPago.setStatusInterno("A");
        instance.crea(ordenPago, usuario);
        assertNotNull(ordenPago.getId());        
    }
    
    /**
     * Prueba la lista de ordenPagos
     */
    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        OrdenPago ordenPago=null;
        for (int i = 0; i < 20; i++) {
            ordenPago = new OrdenPago();
            ordenPago.setDescripcion("Test");
            ordenPago.setCheque(false);
            ordenPago.setFechaPago(new Date());
            ordenPago.setEmpresa(usuario.getEmpresa());
            ordenPago.setStatus("A");
            ordenPago.setUserCaptura(usuario);
            ordenPago.setFechaCaptura(new Date());
            ordenPago.setStatusInterno("A");
            instance.crea(ordenPago, usuario);
            assertNotNull(ordenPago.getId()); 
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_ORDENES_PAGO));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<OrdenPago>) result.get(Constantes.CONTAINSKEY_ORDENES_PAGO)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Prueba el obtener un ordenPago
     */
    @Test
    public void testObtiene() {
        inserta();
        
        OrdenPago ordenPago1 = instance.obtiene(ordenPago.getId());
        assertNotNull(ordenPago1);

    }

    /**
     * Prueba el proceso de creacion del ordenPago
     */
    @Test
    public void testCrea() {
        inserta();
        
        OrdenPago ordenPago1 = instance.obtiene(ordenPago.getId());
        assertNotNull(ordenPago1);
    }
    
    /**
     * Prueba el proceso de actualizacion
     */
    @Test
    public void testActualiza() {
        inserta();
        log.debug("OrdenPago {}", ordenPago);
        
        Map<String, Object> params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        List <OrdenPago> lista = (List)result.get(Constantes.CONTAINSKEY_ORDENES_PAGO);
        Integer nRows =  lista.size();
        
        OrdenPago ordenPago1 = new OrdenPago();
        try {
            BeanUtils.copyProperties(ordenPago1, ordenPago);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            log.error("Error al intentar copiar las propiedades de orden pago");
        }
        assertNotNull(ordenPago1);
        
        ordenPago1.setDescripcion("test2");
        instance.actualiza(ordenPago1, usuario);
        
        currentSession().refresh(ordenPago);
        assertEquals("Test", ordenPago.getDescripcion());
        assertEquals("test2", ordenPago1.getDescripcion());
        
        //Debe haber dos registros en la lista
        result = instance.lista(params);
        lista = (List)result.get(Constantes.CONTAINSKEY_ORDENES_PAGO);
        assertEquals(nRows+1, lista.size());
    }

    /**
     * Prueba el eliminar el ordenPago
     */
    @Test
    public void testElimina() {
        inserta();
        
        Map<String, Object> params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        List <OrdenPago> lista = (List)result.get(Constantes.CONTAINSKEY_ORDENES_PAGO);
        Integer nRows =  lista.size();
        
        String descripcion = instance.elimina(ordenPago.getId());
        assertEquals(ordenPago.getDescripcion(), descripcion);
        
        OrdenPago ordenPago1 = instance.obtiene(ordenPago.getId());        
        if(ordenPago1 != null){
            fail("Se encontro ordenPago "+ordenPago1);
        }
        else{
            log.debug("Se elimino con exito el ordenPago {}", descripcion);
        }
        
        result = instance.lista(params);
        lista = (List)result.get(Constantes.CONTAINSKEY_ORDENES_PAGO);
        assertEquals(nRows, new Integer(lista.size()));
        
    }
}
