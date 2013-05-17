/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.colportor.dao;

import mx.edu.um.mateo.colportor.model.Colportor;
import java.util.*;
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
 *
 * @author wilbert
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class ColportorDaoTest extends BaseDaoTest{

    private static final Logger log = LoggerFactory.getLogger(ColportorDaoTest.class);
    @Autowired
    private ColportorDao colportorDao;
    
    /**
     * Test of lista method, of class ColportorDao.
     */
    @Test
    public void testLista() {
        log.debug("Debiera mostrar lista de Colportor");
        Usuario usuario = this.obtieneAsociado();
        
        for (int i = 0; i < 20; i++) {
            Colportor colportor = new Colportor("test" + i + "@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                    "8262652626", "test", "test", "10706" + i, "test", "test001", new Date());
            colportor.setEmpresa(usuario.getEmpresa());
            colportor.setAlmacen(usuario.getAlmacen());
            currentSession().save(colportor);
            assertNotNull(colportor.getId());
        }
        
        Map<String, Object> params = new HashMap();
        params.put("empresa", usuario.getEmpresa().getId());
        
        Map result = colportorDao.lista(params);
        
        assertNotNull(result.get(Constantes.COLPORTOR_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));

        assertEquals(10, ((List<Colportor>) result.get(Constantes.COLPORTOR_LIST)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    public void testLista_ProbandoFiltro() {
        log.debug("Debiera buscar un Colportor y mostrar lista de Colportores");
        Usuario usuario = obtieneAsociado();
        
        for (int i = 0; i < 10; i++) {
            Colportor colportor = new Colportor("test" + i + "@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                    "8262652626", "test", "test", "10706" + i, "test", "test001", new Date());
            colportor.setEmpresa(usuario.getEmpresa());
            colportor.setAlmacen(usuario.getAlmacen());
            
            currentSession().save(colportor);
            assertNotNull(colportor.getId());
        }
        
        Map<String, Object> params = new HashMap();
        params.put("empresa", usuario.getEmpresa().getId());        
        Map result = colportorDao.lista(params);
        
        String filtro="test2";
        params.put("filtro", filtro);
        
        assertNotNull(result.get(Constantes.COLPORTOR_LIST));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(2, ((List<Colportor>) result.get(Constantes.COLPORTOR_LIST)).size());
        assertEquals(10, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void testObtiene() {
        log.debug("Debiera obtener un Colportor");

        Usuario usuario = obtieneAsociado();
        
        Colportor colportor = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setEmpresa(usuario.getEmpresa());
        colportor.setAlmacen(usuario.getAlmacen());
        
        currentSession().save(colportor);
        
        Long id = colportor.getId();
        assertNotNull(id);

        Colportor result = colportorDao.obtiene(id);
        assertNotNull(result);
        assertEquals(result, colportor);
        assertEquals("test", result.getClave());
    }

    @Test
    public void testGraba() {
        log.debug("Deberia crear un Colportor");
        Usuario usuario = obtieneAsociado();
        
        Colportor colportor = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setEmpresa(usuario.getEmpresa());
        colportor.setAlmacen(usuario.getAlmacen());
        
        colportorDao.crea(colportor, usuario);
        Colportor colportor2 = colportorDao.obtiene(colportor.getId());
        assertNotNull(colportor2);
        assertNotNull(colportor2.getId());

        assertEquals(colportor, colportor2);
    }

    @Test
    public void testActualiza() {
        log.debug("Deberia actualizar Colportor");
        Usuario usuario = obtieneAsociado();
        
        Colportor colportor = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setEmpresa(usuario.getEmpresa());
        colportor.setAlmacen(usuario.getAlmacen());
        
        colportorDao.crea(colportor, usuario);

        String colonia = "TEST";
        colportor.setColonia(colonia);
        colportorDao.actualiza(colportor);

        Colportor colportor2 = colportorDao.obtiene(colportor.getId());
        assertNotNull(colportor2);
        assertEquals(colonia, colportor2.getColonia());
    }


    @Test
    public void testElimina() {
        log.debug("Debiera eliminar Colportor");
        Usuario usuario = obtieneAsociado();
        
        Colportor colportor = new Colportor("test@test.com", "test", "test", "test", "test", "test", Constantes.STATUS_ACTIVO,
                "8262652626", "test", "test", "10706", "test", "test001", new Date());
        colportor.setEmpresa(usuario.getEmpresa());
        colportor.setAlmacen(usuario.getAlmacen());
        
        currentSession().save(colportor);
        assertNotNull(colportor.getId());

        String clave = colportorDao.elimina(colportor.getId());
        
        colportor = colportorDao.obtiene(colportor.getId());
        assertEquals(Constantes.STATUS_INACTIVO, (colportor).getStatus());
    }
}
