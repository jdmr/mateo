/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
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
public class ClaveEmpleado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    private String clave;
    private String status;
    private String observaciones;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fecha;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioAlta;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "fechaAlta")
    private Date fechaAlta;
    @ManyToOne(optional = false)
    private Empresa empresa;

    public ClaveEmpleado() {
    }

    public ClaveEmpleado(String clave, String status, String observaciones, Date fecha) {
        this.clave = clave;
        this.status = status;
        this.observaciones = observaciones;
        this.fecha = fecha;
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

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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

    @Override
    public String toString() {
        return "ClaveEmpleado{" + "id=" + id + ", version=" + version + ", clave=" + clave + ", status=" + status
                + ", observaciones=" + observaciones + ", fecha=" + fecha + ", usuarioAlta=" + usuarioAlta
                + ", fechaAlta=" + fechaAlta + ", empresa=" + empresa + '}';
    }

}
