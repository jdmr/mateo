/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;

import java.io.Serializable;
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

/**
 * TODO Sustituir nombre de por id
 *
 *
 */
/**
 *
 * @author develop
 */
@Entity
@Table(name = "FES_COMPROMISOPAGO")
public class Prorroga implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Version
    private Integer version;
    @Column(nullable = false, length = 20)
    private String matricula;
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(name = "fecha_comp")
    @Temporal(TemporalType.DATE)
    private Date fechaCompromiso;
    @Column(nullable = false, length = 100)
    private String descripcion;
    @Column(nullable = false, length = 15)
    private Double saldo;
    @Column(name = "usuario", nullable = false, length = 20)
    private String userName;
    @Column(nullable = false, length = 2)
    private String status;
    @ManyToOne(optional = false)
    private Empresa empresa;
    @ManyToOne(optional = false)
    private Usuario usuario;
    @Column(nullable = false, length = 200)
    private String observaciones;

    public Prorroga() {
    }

    public Prorroga(String matricula, Date fecha,
            Date fecha_comp, String descripcion, Double saldo,
            String status) {
        this.matricula = matricula;
        this.fecha = fecha;
        this.fechaCompromiso = fecha_comp;
        this.descripcion = descripcion;
        this.saldo = saldo;
        this.status = status;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion The descripcion to set.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return Returns the fecha.
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha The fecha to set.
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return Returns the fecha_comp.
     */
    public Date getFechaCompromiso() {
        return fechaCompromiso;
    }

    /**
     * @param fecha_comp The fecha_comp to set.
     */
    public void setFechaCompromiso(Date fecha_comp) {
        this.fechaCompromiso = fecha_comp;
    }

    /**
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return Returns the matricula.
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param matricula The matricula to set.
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return Returns the saldo.
     */
    public Double getSaldo() {
        return saldo;
    }

    /**
     * @param saldo The saldo to set.
     */
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    /**
     * @return Returns the status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return Returns the usuario.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param usuario The usuario to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}
