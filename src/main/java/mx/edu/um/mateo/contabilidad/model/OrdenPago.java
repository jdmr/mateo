/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.model;

import java.util.Date;
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
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;

/**
 *
 * @author osoto
 */
@Entity
@Table(name = "cont_orden_pago")
public class OrdenPago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 150, nullable = false)
    private String descripcion;
    @Column(nullable = false)
    private Boolean cheque;
    @Column(name = "fecha_pago", nullable = false)
    private Date fechaPago;
    @ManyToOne(optional = false)
    private Empresa empresa;
    @Column(length = 2, nullable = false)
    private String status;
    @ManyToOne(optional = false)
    private Usuario userCaptura;
    @Column(name = "fecha_captura", nullable = false)
    private Date fechaCaptura;
    @Column(length = 2, nullable = false, name = "status_interno")
    private String statusInterno;
    @Version
    private Integer version;

    public OrdenPago() {
        statusInterno = Constantes.STATUS_ACTIVO;
    }

    public OrdenPago(String descripcion, Boolean cheque, Date fechaPago, Empresa empresa, String status, Usuario userCaptura, Date fechaCaptura, String statusInterno) {
        this.descripcion = descripcion;
        this.cheque = cheque;
        this.fechaPago = fechaPago;
        this.empresa = empresa;
        this.status = status;
        this.userCaptura = userCaptura;
        this.fechaCaptura = fechaCaptura;
        this.statusInterno = statusInterno;
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
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the cheque
     */
    public Boolean getCheque() {
        return cheque;
    }

    /**
     * @param cheque the cheque to set
     */
    public void setCheque(Boolean cheque) {
        this.cheque = cheque;
    }

    /**
     * @return the fechaPago
     */
    public Date getFechaPago() {
        return fechaPago;
    }

    /**
     * @param fechaPago the fechaPago to set
     */
    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the userCaptura
     */
    public Usuario getUserCaptura() {
        return userCaptura;
    }

    /**
     * @param userCaptura the userCaptura to set
     */
    public void setUserCaptura(Usuario userCaptura) {
        this.userCaptura = userCaptura;
    }

    /**
     * @return the fechaCaptura
     */
    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    /**
     * @param fechaCaptura the fechaCaptura to set
     */
    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    /**
     * @return the statusInterno
     */
    public String getStatusInterno() {
        return statusInterno;
    }

    /**
     * @param statusInterno the statusInterno to set
     */
    public void setStatusInterno(String statusInterno) {
        this.statusInterno = statusInterno;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.fechaCaptura);
        hash = 79 * hash + Objects.hashCode(this.version);
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
        final OrdenPago other = (OrdenPago) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.fechaCaptura, other.fechaCaptura)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "OrdenPago{" + "id=" + id + ", descripcion=" + descripcion + ", cheque=" + cheque + ", fechaPago=" + fechaPago + ", status=" + status + ", userCaptura=" + userCaptura + ", fechaCaptura=" + fechaCaptura + ", version=" + version + '}';
    }
    
    
    
}
