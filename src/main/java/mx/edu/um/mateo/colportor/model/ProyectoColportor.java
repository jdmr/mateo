/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author osoto
 */
@Entity
@Table(name = "proyecto_colportor", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"codigo", "empresa_id"})})
public class ProyectoColportor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(name = "codigo",nullable = false)
    private String codigo; //Este codigo sera unico por empresa.
    @Column(name = "nombre",nullable = false)
    private String nombre;
    @Column(name = "descripcion",nullable = false)
    private String descripcion;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern=Constantes.DATE_SHORT_HUMAN_PATTERN)
    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Empresa empresa;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario usuario;
    @Column(name = "status",nullable = false)
    private String status;

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

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.id);
        hash = 61 * hash + Objects.hashCode(this.version);
        hash = 61 * hash + Objects.hashCode(this.codigo);
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
        final ProyectoColportor other = (ProyectoColportor) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ProyectoColportor{" + "id=" + id + ", version=" + version + ", codigo=" + codigo + ", nombre=" + nombre + ", descripcion=" + descripcion + ", fechaInicio=" + fechaInicio + ", empresa=" + empresa.getId() + ", usuario=" + usuario.getId() + ", status=" + status + '}';
    }
    
    
}
