/*
 * The MIT License
 *
 * Copyright 2012 J. David Mendoza <jdmendoza@um.edu.mx>.
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
package mx.edu.um.mateo.inventario.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.contabilidad.model.EjercicioPK;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.server.MockMvc;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.*;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GenericWebXmlContextLoader.class, locations = {
    "classpath:mateo.xml",
    "classpath:security.xml",
    "classpath:dispatcher-servlet.xml"
})
@Transactional
public class AlmacenControllerTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(AlmacenControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private SessionFactory sessionFactory;
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }
    
    @Test
    public void debieraMostrarListaDeAlmacenes() throws Exception {
        log.debug("Debiera mostrar lista de almacenes");
        this.mockMvc.perform(
                get("/inventario/almacen"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/inventario/almacen/lista.jsp"))
                .andExpect(model().attributeExists("almacenes"))
                .andExpect(model().attributeExists("paginacion"))
                .andExpect(model().attributeExists("paginas"))
                .andExpect(model().attributeExists("pagina"));
    }
    
    @Test
    public void debieraMostrarAlmacen() throws Exception {
        log.debug("Debiera mostrar almacen");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("TST", "test-01", empresa);
        currentSession().save(almacen);
        Long id = almacen.getId();
        
        this.mockMvc.perform(get("/inventario/almacen/ver/" + id))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/inventario/almacen/ver.jsp"))
                .andExpect(model().attributeExists("almacen"));
        
    }
    
    @Test
    public void debieraCrearAlmacen() throws Exception {
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        EjercicioPK ejercicioPK = new EjercicioPK("TEST", organizacion);
        Byte x = new Byte("0");
        Ejercicio ejercicio = new Ejercicio(ejercicioPK, "TEST", "A", StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, x, x);
        currentSession().save(ejercicio);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        usuario.setEjercicio(ejercicio);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList<GrantedAuthority>(usuario.getRoles()));
        
        this.mockMvc.perform(post("/inventario/almacen/crea")
                .param("codigo", "TST-01")
                .param("nombre", "TEST--01")
                )
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "almacen.creado.message"));
        
    }
    
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
}
