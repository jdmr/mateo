/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Empresa;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * ClienteColportors de los colportores
 * @author osoto
 */
@Entity
@Table(name = "Cliente_colportor")
public class ClienteColportor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @NotBlank
    @Column(nullable = false, length = 120)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 120)
    private String apPaterno;
    @Column(nullable = true, length = 120)
    private String apMaterno;    
    @Column(nullable = true, length = 15)
    private String telefonoCasa;
    @Column(nullable = true, length = 15)
    private String telefonoTrabajo;
    @Column(nullable = true, length = 15)
    private String telefonoCelular;
    @Email
    @Column(nullable = true, length = 100)
    private String email;
    @NotBlank
    @Column(nullable = false, length = 200)
    private String direccion1; //Calle, numero
    @Column(nullable = true, length = 150)
    private String direccion2; //otro
    @Column(nullable = true, length = 100)
    private String colonia;
    @Column(nullable = true, length = 100)
    private String municipio;
    @Column(nullable = false, length = 2)
    private String status;
    @ManyToOne
    private Empresa empresa;
    
    @InitBinder
    public void inicializar(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }
    
    public String getNombreCompleto() {
        return nombre + " " + apPaterno + " " + apMaterno;
    }

    public String getTelefonoCasa() {
        return telefonoCasa;
    }

    public void setTelefonoCasa(String telefonoCasa) {
        this.telefonoCasa = telefonoCasa;
    }

    public String getTelefonoTrabajo() {
        return telefonoTrabajo;
    }

    public void setTelefonoTrabajo(String telefonoTrabajo) {
        this.telefonoTrabajo = telefonoTrabajo;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String telefonoCelular) {
        this.telefonoCelular = telefonoCelular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion1() {
        return direccion1;
    }

    public void setDireccion1(String direccion1) {
        this.direccion1 = direccion1;
    }

    public String getDireccion2() {
        return direccion2;
    }

    public void setDireccion2(String direccion2) {
        this.direccion2 = direccion2;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        hash = 71 * hash + Objects.hashCode(this.version);
        hash = 71 * hash + Objects.hashCode(this.nombre);
        hash = 71 * hash + Objects.hashCode(this.apPaterno);
        hash = 71 * hash + Objects.hashCode(this.apMaterno);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClienteColportor other = (ClienteColportor) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.apPaterno, other.apPaterno)) {
            return false;
        }
        if (!Objects.equals(this.apMaterno, other.apMaterno)) {
            return false;
        }
 
        return true;
    }

    @Override
    public String toString() {
        return "Cliente{" + "id=" + id + ", version=" + version + ", nombre=" + nombre + ", apPaterno=" + apPaterno + ", apMaterno=" + apMaterno 
                + ", telefonoCasa=" + telefonoCasa + ", telefonoTrabajo=" + telefonoTrabajo + ", telefonoCelular=" + telefonoCelular 
                + ", email=" + email + ", direccion1=" + direccion1 + ", direccion2=" + direccion2 + ", colonia=" + colonia 
                + ", municipio=" + municipio + ", status=" + status + ", empresa=" + empresa.getId() + '}';
    }
    
    
}
