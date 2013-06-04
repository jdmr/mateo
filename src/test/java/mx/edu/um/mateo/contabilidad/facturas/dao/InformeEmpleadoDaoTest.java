/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao;

import mx.edu.um.mateo.contabilidad.facturas.dao.InformeEmpleadoDao;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseDaoTest;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.CobroCampo;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import org.junit.Test;
import static org.junit.Assert.*;
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
public class InformeEmpleadoDaoTest extends BaseDaoTest {

    @Autowired
    private InformeEmpleadoDao instance;

    /**
     * Test of lista method, of class InformeEmpleadoDao.
     */
    @Test
    public void testLista() {
        Usuario usuario = obtieneUsuario();
        InformeEmpleado informe = null;
        for (int i = 0; i < 20; i++) {
            informe = new InformeEmpleado();
            informe.setEmpresa(usuario.getEmpresa());
            informe.setNumNomina("054");
            informe.setNombreEmpleado("Sam");
            informe.setFechaInforme(new Date());
            informe.setDolares(Boolean.TRUE);
            informe.setInforme(Boolean.TRUE);
            informe.setPesos(Boolean.TRUE);
            informe.setReembolso(Boolean.TRUE);
            informe.setStatus("a");
//            informe.setFechaFactura(new Date());
//            informe.setFolioFactura("1110475");
//            informe.setIVA(new BigDecimal(".16"));
//            informe.setNombreProveedor("Lala");
//            informe.setPathPDF("prueba.pdf");
//            informe.setPathXMl("prueba.xml");
//            informe.setRFCProveedor("1147hgas40q");
//            informe.setSubtotal(new BigDecimal("223"));
//            informe.setTotal(new BigDecimal("250"));
            instance.crea(informe, usuario);
            assertNotNull(informe.getId());
        }

        Map<String, Object> params;
        params = new TreeMap<>();
        params.put("empresa", usuario.getEmpresa().getId());
        Map<String, Object> result = instance.lista(params);
        assertNotNull(result.get(Constantes.CONTAINSKEY_INFORMESEMPLEADO));
        assertNotNull(result.get(Constantes.CONTAINSKEY_CANTIDAD));
        assertEquals(10, ((List<InformeEmpleado>) result.get(Constantes.CONTAINSKEY_INFORMESEMPLEADO)).size());
        assertEquals(20, ((Long) result.get(Constantes.CONTAINSKEY_CANTIDAD)).intValue());
    }

    /**
     * Test of obtiene method, of class InformeEmpleadoDao.
     */
    @Test
    public void testObtiene() {
        Usuario usuario = obtieneUsuario();
        InformeEmpleado informe = new InformeEmpleado();
        informe.setEmpresa(usuario.getEmpresa());
        informe.setNumNomina("054");
        informe.setNombreEmpleado("Sam");
        informe.setFechaInforme(new Date());
        informe.setDolares(Boolean.TRUE);
        informe.setInforme(Boolean.TRUE);
        informe.setPesos(Boolean.TRUE);
        informe.setReembolso(Boolean.TRUE);
        informe.setStatus("a");
        instance.crea(informe, usuario);
        assertNotNull(informe.getId());
        InformeEmpleado informeEmpleado = instance.obtiene(informe.getId());
        assertEquals(informe.getNombreEmpleado(), informeEmpleado.getNombreEmpleado());
    }

    /**
     * Test of crea method, of class InformeEmpleadoDao.
     */
    @Test
    public void testCrea() {
        Usuario usuario = obtieneUsuario();
        InformeEmpleado informe = new InformeEmpleado();
        informe.setEmpresa(usuario.getEmpresa());
        informe.setNumNomina("054");
        informe.setNombreEmpleado("Sam");
        informe.setFechaInforme(new Date());
        informe.setDolares(Boolean.TRUE);
        informe.setInforme(Boolean.TRUE);
        informe.setPesos(Boolean.TRUE);
        informe.setReembolso(Boolean.TRUE);
        informe.setStatus("a");
        instance.crea(informe, usuario);
        assertNotNull(informe.getId());
        InformeEmpleado informeEmpleado = instance.obtiene(informe.getId());
        assertEquals(informe.getNombreEmpleado(), informeEmpleado.getNombreEmpleado());
    }

    /**
     * Test of actualiza method, of class InformeEmpleadoDao.
     */
    @Test
    public void testActualiza() {
        Usuario usuario = obtieneUsuario();
        InformeEmpleado informe = new InformeEmpleado();
        informe.setEmpresa(usuario.getEmpresa());
        informe.setNumNomina("054");
        informe.setNombreEmpleado("Sam");
        informe.setFechaInforme(new Date());
        informe.setDolares(Boolean.TRUE);
        informe.setInforme(Boolean.TRUE);
        informe.setPesos(Boolean.TRUE);
        informe.setReembolso(Boolean.TRUE);
        informe.setStatus("a");
        instance.crea(informe, usuario);
        assertNotNull(informe.getId());

        InformeEmpleado informeEmpleado = instance.obtiene(informe.getId());
        assertEquals(informe.getNombreEmpleado(), informeEmpleado.getNombreEmpleado());

        informeEmpleado.setNombreEmpleado("Samuel");
        instance.actualiza(informeEmpleado, usuario);

        currentSession().refresh(informe);
        assertEquals("Samuel", informe.getNombreEmpleado());
    }

    /**
     * Test of elimina method, of class InformeEmpleadoDao.
     */
    @Test
    public void testElimina() {
        Usuario usuario = obtieneUsuario();
        InformeEmpleado informe = new InformeEmpleado();
        informe.setEmpresa(usuario.getEmpresa());
        informe.setNumNomina("054");
        informe.setNombreEmpleado("Sam");
        informe.setFechaInforme(new Date());
        informe.setDolares(Boolean.TRUE);
        informe.setInforme(Boolean.TRUE);
        informe.setPesos(Boolean.TRUE);
        informe.setReembolso(Boolean.TRUE);
        informe.setStatus("a");
        instance.crea(informe, usuario);
        assertNotNull(informe.getId());

        String nomina = instance.elimina(informe.getId());
        assertEquals(nomina, informe.getNumNomina());
        InformeEmpleado informeEmpleado = instance.obtiene(informe.getId());

        if ("A".equals(informe.getStatus())) {
            fail("Se encontro el informe " + informeEmpleado);
        } else {
            log.debug("Se elimino con exito el informe con nomina: {}", nomina);
        }
    }
}