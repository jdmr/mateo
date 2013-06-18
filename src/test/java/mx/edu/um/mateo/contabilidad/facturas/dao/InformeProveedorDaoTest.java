/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
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
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class InformeProveedorDaoTest extends BaseDaoTest {

    @Autowired
    private InformeProveedorDao dao;

    public void testLista() {
        Usuario usuario = obtieneUsuario();
        InformeProveedor informeProveedor = null;
        for (int i = 0; i < 20; i++) {
            informeProveedor = new InformeProveedor();
            informeProveedor.setEmpresa(usuario.getEmpresa());
            informeProveedor.setFechaInforme(new Date());
            informeProveedor.setNombreProveedor("LAla");
            informeProveedor.setStatus("A");
            dao.crea(informeProveedor, usuario);
            assertNotNull(informeProveedor.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = dao.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<InformeEmpleado>) result.get(Constantes.CONTAINSKEY_INFORMESPROVEEDOR)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        dao.crea(informeProveedor, usuario);
        assertNotNull(informeProveedor.getId());
        InformeProveedor informeEmpleado1 = dao.obtiene(informeProveedor.getId());
        assertEquals(informeEmpleado1.getNombreProveedor(), informeProveedor.getNombreProveedor());
    }

    @Test
    public void testCrea() {
        Usuario usuario = obtieneUsuario();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        dao.crea(informeProveedor, usuario);
        assertNotNull(informeProveedor.getId());
        InformeProveedor informeProveedor1 = dao.obtiene(informeProveedor.getId());
        assertEquals(informeProveedor1.getNombreProveedor(), informeProveedor.getNombreProveedor());
    }

    @Test
    public void testActualiza() {
        Usuario usuario = obtieneUsuario();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        dao.crea(informeProveedor, usuario);
        assertNotNull(informeProveedor.getId());

        InformeProveedor informeProveedor1 = dao.obtiene(informeProveedor.getId());
        assertEquals(informeProveedor1.getNombreProveedor(), informeProveedor.getNombreProveedor());

        informeProveedor1.setNombreProveedor("Samuel");
        dao.actualiza(informeProveedor1, usuario);

        currentSession().refresh(informeProveedor);
        assertEquals("Samuel", informeProveedor.getNombreProveedor());
    }

    @Test
    public void testElimina() {
        Usuario usuario = obtieneUsuario();
        InformeProveedor informeProveedor = new InformeProveedor();
        informeProveedor.setEmpresa(usuario.getEmpresa());
        informeProveedor.setFechaInforme(new Date());
        informeProveedor.setNombreProveedor("LAla");
        informeProveedor.setStatus("A");
        dao.crea(informeProveedor, usuario);
        assertNotNull(informeProveedor.getId());

        String proveedor = dao.elimina(informeProveedor.getId());
        assertEquals(proveedor, informeProveedor.getNombreProveedor());
        InformeProveedor informeProveedor1 = dao.obtiene(informeProveedor.getId());

        if ("A".equals(informeProveedor.getStatus())) {
            fail("Se encontro el informe " + informeProveedor1);
        } else {
            log.debug("Se elimino con exito el informe con nomina: {}", proveedor);
        }
    }
}
