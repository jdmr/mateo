/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 
 * @author osoto
 */
@Entity
@Table(name = "estudiosEmpleado")
public class EstudiosEmpleado implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column
    private String nombreEstudios;
    @Column
    private NivelEstudios nivelEstudios;
    @Column
    private Short titulado;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaTitulacion;
    @Column
    private String status;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaCaptura;

    /**
     * @hibernate.property not-null="true"
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    
    /**
     * @hibernate.property column="nombre_estudios" length="75" not-null="true"
     * @return the nombreEstudios
     */
    public String getNombreEstudios() {
        return nombreEstudios;
    }

    /**
     * @param nombreEstudios the nombreEstudios to set
     */
    public void setNombreEstudios(String nombreEstudios) {
        this.nombreEstudios = nombreEstudios;
    }


    /**
     * @hibernate.property not-null="true"
     * @return the titulado
     */
    public Short getTitulado() {
        return titulado;
    }

    /**
     * @param titulado the titulado to set
     */
    public void setTitulado(Short titulado) {
        this.titulado = titulado;
    }

    
    /**
     * Este atributo puede ser null en el caso de no estar titulado
     * @hibernate.property column="fecha_titulacion" 
     * @return the fechaTitulacion
     */
    public Date getFechaTitulacion() {
        return fechaTitulacion;
    }

    /**
     * @param fechaTitulacion the fechaTitulacion to set
     */
    public void setFechaTitulacion(Date fechaTitulacion) {
        this.fechaTitulacion = fechaTitulacion;
    }

    /**
     * @hibernate.property length="2" not-null="true"
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
     * @hibernate.many-to-one column="nivel_estudios_id" not-null="true" outer-join="false"
     * @return the nivelEstudios
     */
    public NivelEstudios getNivelEstudios() {
        return nivelEstudios;
    }

    /**
     * @param nivelEstudios the nivelEstudios to set
     */
    public void setNivelEstudios(NivelEstudios nivelEstudios) {
        this.nivelEstudios = nivelEstudios;
    }

    /**
     * @hibernate.many-to-one column="user_captura_id" not-null="true" outer-join="false"
     * @return the userCaptura
     */


    /**
     * @hibernate.property column="fecha_captura" not-null="true"
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

    public boolean equals(Object object) {
		if (!(object instanceof EstudiosEmpleado)) {
			return false;
		}
		EstudiosEmpleado rhs = (EstudiosEmpleado) object;
		return new EqualsBuilder()
                        .append(this.id, rhs.id)
                        .isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
    @Override
	public int hashCode() {
		return new HashCodeBuilder(-1915515825, -2044322231)
                        .append(this.id)
                        .toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
    @Override
	public String toString() {
		return new ToStringBuilder(this)
                        .append("id", this.id)
                        .append("nombreEstudios", this.nombreEstudios)
                        //.append("nivel", this.nivel)
                        .append("titulado", this.titulado)
                        .append("fechaTitulacion", this.fechaTitulacion)
                        .append("status", this.status)
			.toString();
	}

    public int compareTo(Object o) {
        if(!(o instanceof EstudiosEmpleado)) {
            return -1;
        }
        EstudiosEmpleado ev = (EstudiosEmpleado)o;
        return ev.getFechaTitulacion().compareTo(this.getFechaTitulacion());
    }

}
