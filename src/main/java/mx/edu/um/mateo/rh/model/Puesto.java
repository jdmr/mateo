/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * @author osoto
 */
@Entity
@Table(name = "puesto")
public class Puesto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8291747354060833386L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@NotBlank
	@Column(nullable = false, length = 150, unique=true)
	private String descripcion;
        @NotBlank
	@Column(nullable = false)
        private Integer categoria;
        @NotBlank
	@Column(nullable = false)
        private Integer seccion;
        @NotBlank
	@Column(nullable = false)
        private Integer minima;
        @NotBlank
	@Column(nullable = false)
        private Integer maxima;
        @Column(nullable = false, length = 2)
        private String status;
        @Column
        private Double rangoAcademico;
	@Version
	private Integer version;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getCategoria() {
        return categoria;
    }

    public void setCategoria(Integer categoria) {
        this.categoria = categoria;
    }

    public Integer getSeccion() {
        return seccion;
    }

    public void setSeccion(Integer seccion) {
        this.seccion = seccion;
    }

    public Integer getMinima() {
        return minima;
    }

    public void setMinima(Integer minima) {
        this.minima = minima;
    }

    public Integer getMaxima() {
        return maxima;
    }

    public void setMaxima(Integer maxima) {
        this.maxima = maxima;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getRangoAcademico() {
        return rangoAcademico;
    }

    public void setRangoAcademico(Double rangoAcademico) {
        this.rangoAcademico = rangoAcademico;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

	

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Puesto other = (Puesto) obj;
		if (this.id != other.id
				&& (this.id == null || !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 47 * hash + (this.id != null ? this.id.hashCode() : 0);
		hash = 47 * hash + (this.version != null ? this.version.hashCode() : 0);
		return hash;
	}

    @Override
    public String toString() {
        return "Puesto{" + "id=" + id + ", descripcion=" + descripcion + ", categoria=" + categoria + ", seccion=" + seccion + ", minima=" + minima + ", maxima=" + maxima + ", status=" + status + ", rangoAcademico=" + rangoAcademico + ", version=" + version + '}';
    }

	

}
