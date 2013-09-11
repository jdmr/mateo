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
package mx.edu.um.mateo.general.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author jdmr
 */
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("user")
@Entity 
@Table(name = "usuarios")
public class Usuario  implements Serializable, UserDetails, TipoUsuario {
    protected final transient Logger log = LoggerFactory.getLogger(getClass());

    /**
     *
     */
    private static final long serialVersionUID = 3866041221691925369L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @NotEmpty
    @Column(unique = true, nullable = false)
    protected String username;
    @Column(nullable = false)
    protected String password;
    @Column(nullable = true, name = "open_id")
    private String openId;
    @Column(nullable = false)
    private Boolean enabled = true;
    @Column(nullable = false, name = "account_expired")
    private Boolean accountExpired = false;
    @Column(nullable = false, name = "account_locked")
    private Boolean accountLocked = false;
    @Column(nullable = false, name = "credentials_expired")
    private Boolean credentialsExpired = false;
    @NotEmpty
    @Column(nullable = false)
    private String nombre;
    @NotEmpty
    @Column(nullable = false)
    protected String apPaterno;
    @NotEmpty
    @Column(nullable = false)
    protected String apMaterno;
    
    @Email
    @NotEmpty
    @Column(nullable = false, name = "correo")
    protected String correo;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuarios_roles", joinColumns = {
        @JoinColumn(name = "usuario_id")}, inverseJoinColumns =
    @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();
    @ManyToOne(optional = false)
    private Empresa empresa;
    @ManyToOne(optional = false)
    private Almacen almacen;
    @ManyToOne
    private Ejercicio ejercicio;
    @ManyToMany
    private Set<CentroCosto> centrosDeCosto;
    @ManyToOne(optional = true)
    private Asociacion asociacion;

    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }

    public Usuario() {
        this.setRoles(new HashSet());
    }
      public Usuario( String nombre, String apPaterno, String apMaterno) {
          this();
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        
    }

    public Usuario(String username, String password, String nombre,
            String apPaterno, String apMaterno) {
        this();
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.correo = "test@test.com";
    }

    public Usuario(String username, String password, String nombre,
            String apPaterno,String apMaterno, String correo) {
        this();
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        
        this.correo = correo;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the openId
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * @param openId the openId to set
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * @return the enabled
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
  
    
    /**
     * @return the correo
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * @param correo the correo to set
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Agrega un rol a la lista de roles
     *
     * @param rol
     */
    public void addRol(Rol rol) {
        this.getRoles().add(rol);
    }

    /**
     * @return los roles
     */
    public Set<Rol> getRoles() {
        return roles;
    }

    /**
     * @param roles Los roles a asignar
     */
    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the almacen
     */
    public Almacen getAlmacen() {
        return almacen;
    }

    /**
     * @param almacen the almacen to set
     */
    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        authorities.addAll(roles);
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    /**
     * @param apPaterno the apPaterno to get
     */
       public String getApPaterno() {
        return apPaterno;
    }
       /**
     * @param apPaterno the ejercicio to set
     */
    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }
    /**
     * @param apMaterno the ejercicio to get
     */
    public String getApMaterno() {
        return apMaterno;
    }
    /**
     * @param apMaterno the apMaterno to set
     */
    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }
    
    /**
     * @return the ejercicio
     */
    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the centrosDeCosto
     */
    public Set<CentroCosto> getCentrosDeCosto() {
        return centrosDeCosto;
    }

    /**
     * @param centrosDeCosto the centrosDeCosto to set
     */
    public void setCentrosDeCosto(Set<CentroCosto> centrosDeCosto) {
        this.centrosDeCosto = centrosDeCosto;
    }
    
    public String getNombreCompleto() {
        return nombre+" "+ apPaterno+" "+apMaterno;
    }
    

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.version);
        hash = 97 * hash + Objects.hashCode(this.username);
        return hash;
    }

 

    @Override
    public String toString() {
        return "Usuario{" + "username=" + username + ", nombre=" + nombre
                + ", apPaterno=" + apPaterno + ", apMaterno=" + apMaterno + '}';
    }

    @Override
    public Boolean isTipoUsuario() {
        return true;
    }

    @Override
    public Boolean isTipoAsociado() {
        return false;
    }

    @Override
    public Boolean isTipoColportor() {
        return false;
    }
    
    
}
