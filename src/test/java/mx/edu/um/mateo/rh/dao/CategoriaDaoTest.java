/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.rh.model.Categoria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
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
 * @author Zorch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mateo.xml", "classpath:security.xml"})
@Transactional
public class CategoriaDaoTest {
    @Autowired
    private CategoriaDao instance;
    private static final Logger log= LoggerFactory.getLogger(CategoriaDaoTest.class);
    
    @Autowired
    private SessionFactory sessionFactory;
    
    private Session currentSession(){
        return sessionFactory.getCurrentSession();
    }

    @Test
     public void testObtenerListaDeCategorias() {     
        log.debug("Muestra lista de categorias");
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Organizacion organizacion = new Organizacion("codigo", "nombre", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("test", "alamcen", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        for(int i=0; i<5; i++){
            
            
            Categoria categoria=new Categoria();
            
            
            categoria.setNombre("Categoria"+i);
            categoria.setStatus("A");
            categoria.setEmpresa(empresa);
            instance.graba(categoria, usuario);
           assertNotNull(categoria.getId());
        }
         Map<String, Object> params= new HashMap<>();
         params.put("empresa",empresa.getId());
         Map<String, Object> result=instance.lista(params);
         assertEquals(5,result.size());
        
        
    }

     
    @Test
    public void testObtiene() {
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Organizacion organizacion = new Organizacion("codigo", "nombre", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("test", "alamcen", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        Categoria categoria= new Categoria();
        categoria.setNombre("Test1");
        categoria.setStatus("AC");
        categoria.setEmpresa(empresa);
        instance.graba(categoria, usuario);
        
        Categoria categoria1= instance.obtiene(categoria.getId());
        assertEquals(categoria.getId(),categoria1.getId());
        
       
    }
    @Test
    public void testGraba() throws Exception {
        Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Organizacion organizacion = new Organizacion("codigo", "nombre", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("test", "alamcen", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id);
        Categoria categoria= new Categoria();
        categoria.setNombre("Test1");
        categoria.setStatus("AC");
        categoria.setEmpresa(empresa);
        instance.graba(categoria, usuario);
        
        assertNotNull(categoria.getId());
        assertEquals(categoria.getNombre(),"Test1");
        assertEquals(categoria.getStatus(), "AC");
        
        
        
    }
    @Test
    public void testElimina() throws Exception {
       Rol rol = new Rol(Constantes.ROLE_COL);
        currentSession().save(rol);
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        Organizacion organizacion = new Organizacion("codigo", "nombre", "Organizacion");
        currentSession().save(organizacion);
        Empresa empresa = new Empresa("codigo", "empresa", "Empresa", "123456789123", organizacion);
        currentSession().save(empresa);
        Almacen almacen = new Almacen("test", "alamcen", empresa);
        currentSession().save(almacen);
        Usuario usuario = new Usuario("bugs@um.edu.mx", "apPaterno","apMaterno", "TEST-01", "TEST-01");
        usuario.setEmpresa(empresa);
        usuario.setAlmacen(almacen);
        usuario.setRoles(roles);
        currentSession().save(usuario);
        Long id = usuario.getId();
        assertNotNull(id); 
        Categoria categoria= new Categoria();
        categoria.setNombre("Test1");
        categoria.setStatus("AC");
        categoria.setEmpresa(empresa);
        instance.graba(categoria, usuario);
        instance.elimina(categoria.getId());
        
        assertNotNull(categoria.getId());
        assertEquals(categoria.getNombre(),"Test1");
        assertEquals(categoria.getStatus(), "AC");
        
    }
}

