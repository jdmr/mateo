/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Usuario;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author develop
 */
@Entity
public class DiaFeriado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    private String nombre;
    private String descripcion;
    private Boolean dado;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioAlta;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "fechaAlta")
    private Date fechaAlta;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @ManyToOne
    private Empresa empresa;

    public DiaFeriado() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean isDado() {
        return dado;
    }

    public Boolean getDado() {
        return dado;
    }

    public void setDado(Boolean dado) {
        this.dado = dado;
    }

    public Usuario getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuario usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "DiaFeriado{" + "id=" + id + ", version=" + version + ", nombre=" + nombre + ", descripcion=" + descripcion
                + ", dado=" + dado + ", usuarioAlta=" + usuarioAlta + ", fechaAlta=" + fechaAlta + ", fecha=" + fecha
                + ", empresa=" + empresa + '}';
    }

}
