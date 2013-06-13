/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.dao.ColegioDao;
import mx.edu.um.mateo.rh.model.Colegio;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author IrasemaBalderas
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ColegioDaoTest extends BaseDaoTest {
 
    @Autowired
    private ColegioDao colegioDao;
    
    public ColegioDaoTest() {
    }

    /**
     * Test of lista method, of class ColegioDaoHibernate.
     */
    @Test
    public void lista() {
        log.debug("lista");
        
        Usuario asociado = obtieneAsociado();
        
        for (int i = 0; i <= 5; i++) {
        Colegio colegio = new Colegio();
            colegio.setNombre("Nombre");
            colegio.setStatus("A"+i);
            colegio.setOrganizacion(asociado.getEmpresa().getOrganizacion());
            colegioDao.crea(colegio);
            assertNotNull(colegio.getId());
        }

        Map<String, Object> params = new TreeMap();
        params.put("organizacion", asociado.getEmpresa().getOrganizacion().getId());
        params = colegioDao.lista(params);
//        assertNotNull((List) params.get(Constantes.CONTAINSKEY_COLEGIOS));
        assertEquals(6, ((List) params.get(Constantes.CONTAINSKEY_COLEGIOS)).size());

    }

    /**
     * Test of obtiene method, of class ColegioDaoHibernate.
     */
    @Test
    public void obtiene() {
        System.out.println("obtiene");
        
        Usuario asociado = obtieneAsociado();
        
        Colegio colegio = new Colegio();
        colegio.setNombre("Nombre");
        colegio.setStatus("A");
        colegio.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(colegio);
        assertNotNull(colegio.getId());
        
        Colegio colegio1 = colegioDao.obtiene(colegio.getId());
        assertEquals(colegio1.getId(), colegio.getId()); 
    }

    /**
     * Test of saveColegio method, of class ColegioDaoHibernate.
     */
    @Test
    public void graba() {
        System.out.println("graba");
        
        Usuario asociado = obtieneAsociado();
        
        Colegio colegio = new Colegio();
        colegio.setNombre("Nombre");
        colegio.setStatus("A");
        colegio.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        colegioDao.crea(colegio);
        assertNotNull(colegio.getId());
    }

    /**
     * Test of updateColegio method, of class ColegioDaoHibernate.
     */
    @Test
    public void actualiza() {
        System.out.println("actualiza");
        
        Usuario asociado = obtieneAsociado();
        
        Colegio colegio = new Colegio();
        colegio.setNombre("Nombre");
        colegio.setStatus("A");
        colegio.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(colegio);
        assertNotNull(colegio.getId());
        
        Map<String, Object> params = new TreeMap();
        params.put("organizacion", asociado.getEmpresa().getOrganizacion().getId());
        params = colegioDao.lista(params);
        Integer items = ((List)params.get(Constantes.CONTAINSKEY_COLEGIOS)).size();
        
        String nombre="Nombre2";
        colegio.setNombre(nombre);
        colegioDao.crea(colegio);
        
        Colegio colegio2 = colegioDao.obtiene(colegio.getId());
        assertEquals(nombre, colegio2.getNombre());
        
        params = colegioDao.lista(params);
        assertEquals(items,(Integer)((List)params.get(Constantes.CONTAINSKEY_COLEGIOS)).size());
    }

    /**
     * Test of elimina method, of class ColegioDaoHibernate.
     */
    @Test
    public void elimina() {
        System.out.println("removeColegio");
        
        Usuario asociado = obtieneAsociado();
        
        Colegio colegio = new Colegio();
        colegio.setNombre("Nombre");
        colegio.setStatus("A");
        colegio.setOrganizacion(asociado.getEmpresa().getOrganizacion());
        currentSession().save(colegio);
        assertNotNull(colegio.getId());
        
        String nombre = colegioDao.elimina(colegio.getId());
        
        colegio = colegioDao.obtiene(colegio.getId());
        
        assertEquals(Constantes.STATUS_INACTIVO, colegio.getStatus());
        
    }
}
