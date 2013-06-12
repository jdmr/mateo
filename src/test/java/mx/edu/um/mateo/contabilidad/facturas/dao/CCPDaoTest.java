/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao;

import mx.edu.um.mateo.general.test.BaseDaoTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class CCPDaoTest extends BaseDaoTest {

    @Autowired
    private CCPDao dao;

    @Test
    public void testObtenerAlumno() {

        log.debug("Obtiene un Alumno");
        String folio = "990236";
        Boolean existe = dao.obtiene(folio);
        assertTrue(existe);
    }
}
