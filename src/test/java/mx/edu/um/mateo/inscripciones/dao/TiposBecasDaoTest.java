/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TiposBecasDaoTest extends BaseDaoTest{

    @Autowired
    private TiposBecasDao instance;
    /**
     * Test del metodo de lista,de la clase nacionalidadDao.
     */
    @Test
    public void testObtenerListaDeTiposBecas() {
        Usuario usuario = obtieneUsuario();
        for (int i = 0; i < 20; i++) {
            Empresa empresa= usuario.getEmpresa();
            TiposBecas tiposBecas = new TiposBecas("test", Boolean.TRUE, new BigDecimal(12.3),new BigDecimal(12.3), Boolean.TRUE, Boolean.FALSE, 230 , empresa);
            instance.graba(tiposBecas, usuario);
        }
        Map<String, Object> params = null;
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get("tiposBecas"));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<TiposBecas>) result.get("tiposBecas")).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());


    }

    /**
     * Test of obtiene method, of class NacionalidadDao.
     */
    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        Empresa empresa= usuario.getEmpresa();
        TiposBecas tiposBecas = new TiposBecas("test", Boolean.TRUE, new BigDecimal(12.3),new BigDecimal(12.3), Boolean.TRUE, Boolean.FALSE, 230 , empresa);
        instance.graba(tiposBecas, usuario);
        assertNotNull(tiposBecas.getId());
        TiposBecas tiposBeca2 = instance.obtiene(tiposBecas.getId());
        assertEquals(tiposBeca2.getDescripcion(), tiposBecas.getDescripcion());
    }

    /**
     * Test of crea method, of class NacionalidadDao.
     */
    @Test
    public void testCrea() {
        Usuario usuario=obtieneUsuario();
        Empresa empresa= usuario.getEmpresa();
        TiposBecas tiposBecas = new TiposBecas("test", Boolean.TRUE, new BigDecimal(12.3),new BigDecimal(12.3), Boolean.TRUE, Boolean.FALSE, 230 , empresa);
        instance.graba(tiposBecas, usuario);
        assertNotNull(tiposBecas.getId());
    }

    /**
     * Test of actualiza method, of class NacionalidadDao.
     */
    @Test
    public void testActualiza() {
        Usuario usuario=obtieneUsuario();
        Empresa empresa= usuario.getEmpresa();
        TiposBecas tiposBecas = new TiposBecas("test", Boolean.TRUE, new BigDecimal(12.3),new BigDecimal(12.3), Boolean.TRUE, Boolean.FALSE, 230 , empresa);
        currentSession().save(tiposBecas);
        assertNotNull(tiposBecas.getId());
        String descripcion = "prueba";
        tiposBecas.setDescripcion(descripcion);
        instance.graba(tiposBecas, usuario);
        assertEquals("prueba", tiposBecas.getDescripcion());
    }

    /**
     * Test of elimina method, of class NacionalidadDao.
     */
    @Test
    public void testElimina() throws Exception {
        Usuario usuario=obtieneUsuario();
        Empresa empresa= usuario.getEmpresa();
        TiposBecas tiposBecas = new TiposBecas("test", Boolean.TRUE, new BigDecimal(12.3),new BigDecimal(12.3), Boolean.TRUE, Boolean.FALSE, 230 , empresa);
        instance.graba(tiposBecas, usuario);
        assertNotNull(tiposBecas.getId());
        String descripcion = instance.elimina(tiposBecas.getId());
        assertEquals(descripcion, tiposBecas.getDescripcion());
    }
}
