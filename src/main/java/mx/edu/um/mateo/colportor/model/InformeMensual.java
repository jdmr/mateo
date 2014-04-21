/*
 * To change this template, choose Tools | Templates
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
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author osoto
 */
@Entity
@Table(name="informe_mensual")
public class InformeMensual implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Colportor colportor;
    
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern=Constantes.DATE_SHORT_HUMAN_PATTERN)
    @Column(name = "mes_informe")
    private Date fecha;
    @Column(length = 2)
    private String status;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario capturo; //quien capturo
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern=Constantes.DATE_SHORT_HUMAN_PATTERN)
    @Column(name = "fecha_captura")
    private Date fechaCaptura; //fecha captura

    public InformeMensual() {
        this.colportor = new Colportor();
    }

    public InformeMensual(String status) {
        this.status = status;
    }
            
    public InformeMensual(Colportor colportor, Date fecha, String status, Usuario capturo, Date fechaCaptura) {
        this.colportor = colportor;
        this.fecha = fecha;
        this.status = status;
        this.capturo = capturo;
        this.fechaCaptura = fechaCaptura;
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
     * @return the colportor
     */
    public Colportor getColportor() {
        return colportor;
    }

    /**
     * @param colportor the colportor to set
     */
    public void setColportor(Colportor colportor) {
        this.colportor = colportor;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
     * @return the capturo
     */
    public Usuario getCapturo() {
        return capturo;
    }

    /**
     * @param capturo the capturo to set
     */
    public void setCapturo(Usuario capturo) {
        this.capturo = capturo;
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
    public void setFechaCaptura(Date cuando) {
        this.fechaCaptura = cuando;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
        hash = 73 * hash + Objects.hashCode(this.version);
        hash = 73 * hash + Objects.hashCode(this.colportor);
        hash = 73 * hash + Objects.hashCode(this.fecha);
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
        final InformeMensual other = (InformeMensual) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.colportor.getId(), other.colportor.getId())) {
            return false;
        }
        if (!Objects.equals(this.fecha, other.fecha)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InformeMensual{" + "id=" + id + ", version=" + version + ", colportor=" + colportor.getId() + ", fecha=" + fecha + ", status=" + status + ", capturo=" + capturo.getId() + ", cuando=" + fechaCaptura + '}';
    }

    
    
}
