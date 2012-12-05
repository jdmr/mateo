///*
// * The MIT License
// *
// * Copyright 2012 jdmr.
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package mx.edu.um.mateo.colportor.model;
//
//import java.io.Serializable;
//import java.util.*;
//import javax.persistence.*;
//import org.hibernate.validator.constraints.Email;
//import org.hibernate.validator.constraints.NotEmpty;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
///**
// *
// * @author jdmr
// */
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)
//@DiscriminatorValue("user")
//@Entity
//@Table(name = "usuarios")
//public class Usuario implements Serializable, UserDetails {
//
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////    @Version
////    private Integer version;
////    @Email
////    @NotEmpty
////    @Column(unique = true, nullable = false, length = 128)
////    private String username;
////    @Column
////    private String password;
////    @Column(nullable = true, name = "open_id")
////    private String openId;
////    @Column(nullable = false)
////    private Boolean enabled = true;
////    @Column(nullable = false, name = "account_expired")
////    private Boolean accountExpired = false;
////    @Column(nullable = false, name = "account_locked")
////    private Boolean accountLocked = false;
////    @Column(nullable = false, name = "credentials_expired")
////    private Boolean credentialsExpired = false;
////    @NotEmpty
////    @Column(nullable = false, length = 128)
////    private String nombre;
////    @NotEmpty
////    @Column(nullable = false, length = 128)
////    private String apPaterno;
////    @NotEmpty
////    @Column(nullable = false, length = 128)
////    private String apMaterno;
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable(name = "usuarios_roles", joinColumns = {
//        @JoinColumn(name = "usuario_id")}, inverseJoinColumns =
//    @JoinColumn(name = "rol_id"))
//    private Set<Rol> roles = new HashSet<>();
//    @ManyToOne(optional = false)
//    private Asociacion asociacion;
////    @ManyToOne(optional = true)
////    private Asociado asociado;
////    @ManyToOne(optional = true)
////    private Colportor colportor;
//
//    public Usuario() {
//    }
//
//    public Usuario(String username, String password, String nombre, String apPaterno, String apMaterno) {
//        this.username = username;
//        this.password = password;
//        this.nombre = nombre;
//        this.apPaterno = apPaterno;
//        this.apMaterno = apMaterno;
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public Integer getVersion() {
//        return version;
//    }
//
//    public void setVersion(Integer version) {
//        this.version = version;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getOpenId() {
//        return openId;
//    }
//
//    public void setOpenId(String openId) {
//        this.openId = openId;
//    }
//
//    public Boolean getEnabled() {
//        return enabled;
//    }
//
//    public void setEnabled(Boolean enabled) {
//        this.enabled = enabled;
//    }
//
//    public Boolean getAccountExpired() {
//        return accountExpired;
//    }
//
//    public void setAccountExpired(Boolean accountExpired) {
//        this.accountExpired = accountExpired;
//    }
//
//    public Boolean getAccountLocked() {
//        return accountLocked;
//    }
//
//    public void setAccountLocked(Boolean accountLocked) {
//        this.accountLocked = accountLocked;
//    }
//
//    public Boolean getCredentialsExpired() {
//        return credentialsExpired;
//    }
//
//    public void setCredentialsExpired(Boolean credentialsExpired) {
//        this.credentialsExpired = credentialsExpired;
//    }
//
//    public String getNombre() {
//        return nombre;
//    }
//
//    public void setNombre(String nombre) {
//        this.nombre = nombre;
//    }
//
//    public String getApPaterno() {
//        return apPaterno;
//    }
//
//    public void setApPaterno(String apellidoP) {
//        this.apPaterno = apellidoP;
//    }
//
//    public String getApMaterno() {
//        return apMaterno;
//    }
//
//    public void setApMaterno(String apellidoM) {
//        this.apMaterno = apellidoM;
//    }
//
//    /**
//     * Agrega un rol a la lista de roles
//     *
//     * @param rol
//     */
//    public void addRol(Rol rol) {
//        this.getRoles().add(rol);
//    }
//
//    /**
//     * @return los roles
//     */
//    public Set<Rol> getRoles() {
//        return roles;
//    }
//
//    /**
//     * @param roles Los roles a asignar
//     */
//    public void setRoles(Set<Rol> roles) {
//        this.roles = roles;
//    }
//
//    public Asociacion getAsociacion() {
//        return asociacion;
//    }
//
//    public void setAsociacion(Asociacion asociacion) {
//        this.asociacion = asociacion;
//    }
//
//    public Asociado getAsociado() {
//        return null;
//    }
//
//    public void setAsociado(Asociado asociado) {
//      //  this.asociado = asociado;
//    }
//
//    public Colportor getColportor() {
//        return null;
//    }
//
//    public void setColportor(Colportor colportor) {
//        //this.colportor = colportor;
//    }
////    
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
//        authorities.addAll(roles);
//        return authorities;
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return !accountExpired;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return !accountLocked;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return !credentialsExpired;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return enabled;
//    }
//
//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 59 * hash + Objects.hashCode(this.id);
//        hash = 59 * hash + Objects.hashCode(this.version);
//        hash = 59 * hash + Objects.hashCode(this.username);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final Usuario other = (Usuario) obj;
//        if (!Objects.equals(this.id, other.id)) {
//            return false;
//        }
//        if (!Objects.equals(this.version, other.version)) {
//            return false;
//        }
//        if (!Objects.equals(this.username, other.username)) {
//            return false;
//        }
//        return true;
//    }
//
//    @Override
//    public String toString() {
//        return "Usuario{" + "username=" + username + ", nombre=" + nombre + ", apellidoP=" + apPaterno + ",apellidoM=" + apMaterno + ", asociacion=" + asociacion + '}';
//    }
//}
