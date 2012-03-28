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
import java.util.*;
import javax.persistence.*;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author jdmr
 */
@Entity
@Table(name = "usuarios")
public class Usuario implements Serializable, UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @NotEmpty
    @Column(unique = true, nullable = false, length = 128)
    private String username;
    @Column(nullable = false)
    private String password;
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
    @Column(nullable = false, length = 128)
    private String nombre;
    @NotEmpty
    @Column(nullable = false, length = 128)
    private String apellido;
    @Email
    @NotEmpty
    @Column(nullable = false, name = "correo")
    private String correo;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuarios_roles", joinColumns = {
        @JoinColumn(name = "usuario_id")}, inverseJoinColumns =
    @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();
    @ManyToOne(optional = false)
    private Empresa empresa;
    @ManyToOne(optional = false)
    private Almacen almacen;

    public Usuario() {
    }

    public Usuario(String username, String password, String nombre, String apellido) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = "test@test.com";
    }

    public Usuario(String username, String password, String nombre, String apellido, String correo) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
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
     * @return the apellido
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * @param apellido the apellido to set
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
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
        return "Usuario{" + "username=" + username + ", nombre=" + nombre + ", apellido=" + apellido + '}';
    }
}
