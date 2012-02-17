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
package mx.edu.um.mateo.general.web;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import mx.edu.um.mateo.general.dao.EmpresaDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.test.BaseTest;
import mx.edu.um.mateo.general.test.GenericWebXmlContextLoader;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EmpresaControllerTest extends BaseTest {

    private static final Logger log = LoggerFactory.getLogger(EmpresaControllerTest.class);
    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;
    @Autowired
    private EmpresaDao empresaDao;
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webApplicationContextSetup(wac).build();
    }

    @Test
    public void debieraMostrarListaDeEmpresas() throws Exception {
        log.debug("Debiera mostrar lista de empresas");
        this.mockMvc.perform(
                get("/admin/empresa"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/admin/empresa/lista.jsp"))
                .andExpect(model().attributeExists("empresas"))
                .andExpect(model().attributeExists("paginacion"))
                .andExpect(model().attributeExists("paginas"))
                .andExpect(model().attributeExists("pagina"));
    }
    
    @Test
    public void debieraMostrarEmpresa() throws Exception {
        log.debug("Debiera mostrar empresa");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
        currentSession().save(empresa);
        Long id = empresa.getId();

        this.mockMvc.perform(get("/admin/empresa/ver/" + id))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/WEB-INF/jsp/admin/empresa/ver.jsp"))
                .andExpect(model().attributeExists("empresa"));
    }
    
    @Test
    public void debieraCrearEmpresa() throws Exception {
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Empresa otraEmpresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
        currentSession().save(otraEmpresa);
        Almacen almacen = new Almacen("TEST01",otraEmpresa);
        currentSession().save(almacen);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        usuario.setEmpresa(otraEmpresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));
        
        this.mockMvc.perform(post("/admin/empresa/crea")
                .param("codigo", "tst-02")
                .param("nombre", "TEST--02")
                .param("nombreCompleto", "TEST--02"))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "empresa.creada.message"));
    }
    
    @Test
    public void debieraActualizarEmpresa() throws Exception {
        Organizacion organizacion = new Organizacion("TEST01", "TEST01", "TEST01");
        currentSession().save(organizacion);
        Empresa otraEmpresa = new Empresa("tst-01", "test-01", "test-01", organizacion);
        currentSession().save(otraEmpresa);
        Almacen almacen = new Almacen("TEST01",otraEmpresa);
        currentSession().save(almacen);
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Usuario usuario = new Usuario("test-01@test.com", "test-01", "TEST1", "TEST");
        usuario.setEmpresa(otraEmpresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        
        this.authenticate(usuario, usuario.getPassword(), new ArrayList(usuario.getAuthorities()));
        
        Empresa empresa = otraEmpresa;
        
        this.mockMvc.perform(post("/admin/empresa/actualiza")
                .param("id", empresa.getId().toString())
                .param("version", empresa.getVersion().toString())
                .param("codigo", "PRUEBA")
                .param("nombre", empresa.getNombre())
                .param("nombreCompleto", empresa.getNombreCompleto()))
                .andExpect(status().isOk())
                .andExpect(flash().attributeExists("message"))
                .andExpect(flash().attribute("message", "empresa.actualizada.message"));
        
        currentSession().refresh(empresa);
        assertEquals("PRUEBA", empresa.getCodigo());
    }
}
