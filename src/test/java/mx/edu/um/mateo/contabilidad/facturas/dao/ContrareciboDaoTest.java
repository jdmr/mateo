/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.contabilidad.facturas.model.Contrarecibo;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
public class ContrareciboDaoTest extends BaseDaoTest {

    @Autowired
    private ContrareciboDao dao;

    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        Contrarecibo contrarecibo = null;
        for (int i = 0; i < 20; i++) {
            contrarecibo = new Contrarecibo();
            contrarecibo.setFechaPago(new Date());
            dao.crea(contrarecibo, usuario);
            assertNotNull(contrarecibo.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = dao.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_CONTRARECIBOS));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<InformeEmpleado>) result.get(Constantes.CONTAINSKEY_CONTRARECIBOS)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void testCrea() {
        Usuario usuario = obtieneUsuario();
        Contrarecibo contrarecibo = new Contrarecibo();
        contrarecibo.setFechaPago(new Date());
        dao.crea(contrarecibo, usuario);
        assertNotNull(contrarecibo.getId());
    }
}
