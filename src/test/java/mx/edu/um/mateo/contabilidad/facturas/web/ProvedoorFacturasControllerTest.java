/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.general.model.Proveedor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseControllerTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.general.utils.Constantes;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.redirectedUrl;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class ProvedoorFacturasControllerTest extends BaseControllerTest {

    @Test
    public void testLista() throws Exception {
        log.debug("Debiera mostrar lista de paquetes");

        Usuario usuario = this.obtieneUsuario();
        ProveedorFacturas proveedorFacturas = null;
        for (int i = 0; i < 20; i++) {
            proveedorFacturas = new ProveedorFacturas("testA" + i, "TEST-01", "nombre", "appaterno", "apmaterno", "test" + i + "@prv.edu.mx",
                    "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01",
                    "TEST-01", "TEST-01", "TEST-01", "a", "TEST-01");
            proveedorFacturas.setAlmacen(usuario.getAlmacen());
            proveedorFacturas.setEmpresa(usuario.getEmpresa());
            currentSession().save(proveedorFacturas);
            assertNotNull(proveedorFacturas.getId());
        }


        this.mockMvc.perform(get(Constantes.PATH_PROVEEDORFACTURAS)).
                andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PROVEEDORFACTURAS_LISTA + ".jsp")).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PROVEEDORESFACTURAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINACION)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINAS)).
                andExpect(model().attributeExists(Constantes.CONTAINSKEY_PAGINA));
    }

    @Test
    public void testNuevo() throws Exception {
        log.debug("Test 'nuevo'");


        this.mockMvc.perform(get(Constantes.PATH_PROVEEDORFACTURAS_NUEVO))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PROVEEDORFACTURAS_NUEVO + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PROVEEDORFACTURAS));
    }

    @Test
    public void testEdita() throws Exception {

        Usuario usuario = this.obtieneUsuario();
        ProveedorFacturas proveedorFacturas = new ProveedorFacturas("testA", "TEST-01", "nombre", "appaterno", "apmaterno", "test@prv.edu.mx",
                "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01",
                "TEST-01", "TEST-01", "TEST-01", "a", "TEST-01");
        proveedorFacturas.setEmpresa(usuario.getEmpresa());
        proveedorFacturas.setAlmacen(usuario.getAlmacen());
        currentSession().save(proveedorFacturas);
        assertNotNull(proveedorFacturas.getId());

        this.mockMvc.perform(get(Constantes.PATH_PROVEEDORFACTURAS_EDITA + "/" + proveedorFacturas.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PROVEEDORFACTURAS_EDITA + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PROVEEDORFACTURAS))
                .andExpect(model().attribute(Constantes.ADDATTRIBUTE_PROVEEDORFACTURAS, proveedorFacturas));
    }

    @Test
    public void testVer() throws Exception {

        Usuario usuario = this.obtieneUsuario();
        ProveedorFacturas proveedorFacturas = new ProveedorFacturas("testA", "TEST-01", "nombre", "appaterno", "apmaterno", "test@prv.edu.mx",
                "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01",
                "TEST-01", "TEST-01", "TEST-01", "a", "TEST-01");
        proveedorFacturas.setAlmacen(usuario.getAlmacen());
        proveedorFacturas.setEmpresa(usuario.getEmpresa());
        currentSession().save(proveedorFacturas);
        assertNotNull(proveedorFacturas.getId());

        this.mockMvc.perform(get(Constantes.PATH_PROVEEDORFACTURAS_VER + "/" + proveedorFacturas.getId()))
                .andExpect(forwardedUrl("/WEB-INF/jsp/" + Constantes.PATH_PROVEEDORFACTURAS_VER + ".jsp"))
                .andExpect(model().attributeExists(Constantes.ADDATTRIBUTE_PROVEEDORFACTURAS));
    }

    @Test
    public void testGraba() throws Exception {
        log.debug("Test Graba");

        Usuario proveedorFacturas = obtieneProveedor();
        this.authenticate(proveedorFacturas, proveedorFacturas.getPassword(), new ArrayList<GrantedAuthority>(proveedorFacturas.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_PROVEEDORFACTURAS_GRABA)
                //                .param("almacen", proveedorFacturas.getAlmacen().getId().toString())
                //                .param("empresa", proveedorFacturas.getEmpresa().getId().toString())
                .param("username", "testC1")
                .param("username", "testC1")
                .param("correo", "test@test.com")
                .param("password", "test")
                .param("nombre", "testC1")
                .param("apPaterno", "test")
                .param("apMaterno", "test")
                .param("status", Constantes.STATUS_ACTIVO)
                .param("telefono", Constantes.TELEFONO)
                .param("clabe", Constantes.CLAVE)
                .param("razonSocial", "lkjlakjd")
                .param("rfc", "kjasdkjasd")
                .param("idFiscal", "kjasdkjasd")
                .param("CURP", "kjasdkjasd")
                .param("banco", "kjasdkjasd")
                .param("direccion", "kjasdkjasd")
                .param("tipoTercero", "kjasdkjasd")
                .param("cuentaCheque", "05052010"))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "proveedor.creado.message"));
    }

    @Test
    public void testActualiza() throws Exception {
        Usuario usuario = this.obtieneUsuario();
        ProveedorFacturas proveedorFacturas = new ProveedorFacturas("testA", "TEST-01", "nombre", "appaterno", "apmaterno", "test@prv.edu.mx",
                "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01",
                "TEST-01", "TEST-01", "TEST-01", "a", "TEST-01");
        proveedorFacturas.setAlmacen(usuario.getAlmacen());
        proveedorFacturas.setEmpresa(usuario.getEmpresa());
        currentSession().save(proveedorFacturas);
        assertNotNull(proveedorFacturas.getId());
        this.authenticate(proveedorFacturas, proveedorFacturas.getPassword(), new ArrayList<GrantedAuthority>(proveedorFacturas.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_PROVEEDORFACTURAS_ACTUALIZA)
                .param("version", proveedorFacturas.getVersion().toString())
                .param("id", proveedorFacturas.getId().toString())
                .param("username", "testC1")
                .param("correo", "test@test.com")
                .param("password", "test")
                .param("nombre", "testC12")
                .param("apPaterno", "test")
                .param("apMaterno", "test")
                .param("status", Constantes.STATUS_ACTIVO)
                .param("telefono", Constantes.TELEFONO)
                .param("clabe", Constantes.CLAVE)
                .param("razonSocial", "lkjlakjd")
                .param("rfc", "kjasdkjasd")
                .param("idFiscal", "kjasdkjasd")
                .param("CURP", "kjasdkjasd")
                .param("banco", "kjasdkjasd")
                .param("direccion", "kjasdkjasd")
                .param("tipoTercero", "kjasdkjasd")
                .param("cuentaCheque", "05052010"))
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "proveedor.actualizado.message"))
                .andExpect(redirectedUrl(Constantes.PATH_PROVEEDORFACTURAS_LISTA));
        currentSession().refresh(proveedorFacturas);
        log.debug("{}", proveedorFacturas);
        assertEquals("testC12", proveedorFacturas.getNombre());
    }

    @Test
    public void testElimina() throws Exception {
        Usuario usuario = obtieneUsuario();
        ProveedorFacturas proveedorFacturas = new ProveedorFacturas("testA", "TEST-01", "nombre", "appaterno", "apmaterno", "test@prv.edu.mx",
                "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01", "TEST-01",
                "TEST-01", "TEST-01", "TEST-01", "a", "TEST-01");
        proveedorFacturas.setAlmacen(usuario.getAlmacen());
        proveedorFacturas.setEmpresa(usuario.getEmpresa());
        currentSession().save(proveedorFacturas);
        assertNotNull(proveedorFacturas.getId());
        this.authenticate(proveedorFacturas, proveedorFacturas.getPassword(), new ArrayList<GrantedAuthority>(proveedorFacturas.getRoles()));
        this.mockMvc.perform(post(Constantes.PATH_PROVEEDORFACTURAS_ELIMINA)
                .param("id", proveedorFacturas.getId().toString()))
                .andExpect(flash().attributeExists(Constantes.CONTAINSKEY_MESSAGE))
                .andExpect(flash().attribute(Constantes.CONTAINSKEY_MESSAGE, "proveedor.eliminado.message"))
                .andExpect(redirectedUrl(Constantes.PATH_PROVEEDORFACTURAS_LISTA));
    }
}
