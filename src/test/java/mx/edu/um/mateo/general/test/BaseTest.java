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
package mx.edu.um.mateo.general.test;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.contabilidad.model.EjercicioPK;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import static org.junit.Assert.assertNotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public abstract class BaseTest {
    protected final transient Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private SessionFactory sessionFactory;
    
    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
            
    protected Authentication authenticate(UserDetails principal, String credentials, List<GrantedAuthority> authorities) {
        log.debug("Entrando al metodo 'authenticate' ***");
        Authentication authentication = new TestingAuthenticationToken(principal, credentials, authorities);
        authentication.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("Regresando autenticacion {} ***", authentication);
        return authentication;
    }
    
    protected Usuario obtieneUsuario(){
        log.debug("Entrando a 'obtieneUsuario'");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Rol rol = new Rol("ROLE_TEST");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen);
        
        //Creando Ejercicio para usuario
        EjercicioPK ejPk= new EjercicioPK();
        ejPk.setIdEjercicio("001-2013");
        ejPk.setOrganizacion(organizacion);
        Ejercicio prueba= new Ejercicio();
        prueba.setMascAuxiliar("Auxiliar");
        prueba.setStatus("A");
        prueba.setId(ejPk);
        prueba.setMascBalance("MascBalance");
        prueba.setMascCcosto("CCosto");
        prueba.setMascResultado("Resultado");
        prueba.setNivelContable(Byte.MIN_VALUE);
        prueba.setNivelTauxiliar(Byte.MIN_VALUE);
        prueba.setNombre("Nombre");
        currentSession().save(prueba);
        
        
        Usuario user = new Usuario("test", "TEST-01", "nombre", "appaterno", "apmaterno", "tset@um.edu.mx");
        user.setEmpresa(empresa);
        user.setAlmacen(almacen);
        user.setRoles(roles);
        user.setEjercicio(prueba);
        currentSession().save(user);        
        Long id = user.getId();
        assertNotNull(id);
        
        log.debug("Usuario creado {}",user);
        
        return user;
    }
    
    protected Usuario obtieneColportor(){
        log.debug("Entrando a 'obtieneColportor'");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Rol rol = new Rol("ROLE_CLP");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen);
        
        Usuario clp = new Colportor("test", "TEST-01", "test@clp.edu.mx", "nombre", "appaterno", "apmaterno", "54321", "A", "826-26-30-900", "Libertad", "Matamoros", "Montemorelos", "L", "999999", new Date());
        clp.setEmpresa(empresa);
        clp.setAlmacen(almacen);
        clp.setRoles(roles);
        currentSession().save(clp);        
        Long id = clp.getId();
        assertNotNull(id);
        
        log.debug("Colportor creado {}",clp);
        
        return clp;
    }
    
    protected Usuario obtieneAsociado(){
        log.debug("Entrando a 'obtieneAsociado'");
        Organizacion organizacion = new Organizacion("tst-01", "test-01", "test-01");
        currentSession().save(organizacion);
        assertNotNull(organizacion.getId());
        
        Empresa empresa = new Empresa("tst-01", "test-01", "test-01", "000000000001", organizacion);
        currentSession().save(empresa);
        assertNotNull(empresa.getId());
        
        Set<Rol> roles = new HashSet<>();
        
        Rol rol = new Rol("ROLE_ASOC");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        roles.add(rol);
        rol = new Rol("ROLE_CLP");
        currentSession().save(rol);
        assertNotNull(rol.getId());
        roles.add(rol);
        
        
        Almacen almacen = new Almacen("TST", "TEST", empresa);
        currentSession().save(almacen);
        assertNotNull(almacen);
        
        Usuario asoc = new Asociado("test", "TEST-01", "test@clp.edu.mx", "nombre", "appaterno", "apmaterno", "A", "54321", "8262630900", "Libertad", "Matamoros", "Montemorelos");
        asoc.setEmpresa(empresa);
        asoc.setAlmacen(almacen);
        asoc.setRoles(roles);
        currentSession().save(asoc);        
        Long id = asoc.getId();
        assertNotNull(id);
        
        log.debug("Asociado creado {}",asoc);
        
        return asoc;
    }
    
}
