/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name = "AFE_PLAZA")
public class AFEPlaza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    private String tipoPlaza;
    private String clave;
    private String observaciones;
    private Boolean primerIngreso;
    private Boolean industrial;
    private String turno;
    private String dias;
    private String requisitos;
    private String email;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fechaAlta;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @ManyToOne
    private Usuario usuarioAlta;
    @ManyToOne
    private Usuario usuarioModificacion;
    @ManyToOne(optional = false)
    private Empresa empresa;

    public AFEPlaza() {
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

    public String getTipoPlaza() {
        return tipoPlaza;
    }

    public void setTipoPlaza(String tipoPlaza) {
        this.tipoPlaza = tipoPlaza;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getPrimerIngreso() {
        return primerIngreso;
    }

    public void setPrimerIngreso(Boolean primerIngreso) {
        this.primerIngreso = primerIngreso;
    }

    public String getTurno() {
        return turno;
    }

    public void setTurno(String turno) {
        this.turno = turno;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getRequisitos() {
        return requisitos;
    }

    public void setRequisitos(String requisitos) {
        this.requisitos = requisitos;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Usuario getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuario usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuario getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Usuario usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
     
    
    @Override
    public String toString() {
        return "AfePlaza{" + "id=" + id + ", version=" + version + ", tipoPlaza=" + tipoPlaza
                + ", clave=" + clave + ", primerIngreso=" + primerIngreso + ", turno=" + turno
                + ", dias=" + dias + ", requisitos=" + requisitos + ", email=" + email
                + ", fechaAlta=" + fechaAlta + ", fechaModificacion=" + fechaModificacion
                + ", usuarioAlta=" + usuarioAlta + ", usuarioModificacion=" + usuarioModificacion
                + ", empresa=" + empresa + '}';
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getIndustrial() {
        return industrial;
    }

    public void setIndustrial(Boolean industrial) {
        this.industrial = industrial;
    }
}
