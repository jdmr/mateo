/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import mx.edu.um.mateo.general.model.Empresa;
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
	private Long id;
	@NotBlank
	@Column(nullable = false, length = 150, unique=true)
	private String descripcion;
        @NotNull
	@Column(nullable = false)
        private Integer categoria;
        
        @NotNull
	@Column(nullable = false)
        private Integer minimo;
        @NotNull
	@Column(nullable = false)
        private Integer maximo;
        @Column(nullable = false, length = 2)
        private String status;
        @Column
        private Double rangoAcademico;
	@Version
	private Long version;
        @ManyToOne(optional=false)
        private Empresa empresa;
        @ManyToOne(optional=false)
        private Seccion seccion;

    public Puesto() {
    }

    public Puesto(String descripcion, Integer categoria, Seccion seccion, Integer minimo, Integer maximo, String status, Empresa empresa) {
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.seccion = seccion;
        this.minimo = minimo;
        this.maximo = maximo;
        this.status = status;
        this.empresa = empresa;
    }

        
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

    public void setId(Long id) {
        this.id = id;
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

    public Seccion getSeccion() {
        return seccion;
    }

    public void setSeccion(Seccion seccion) {
        this.seccion = seccion;
    }

    public Integer getMinimo() {
        return minimo;
    }

    public void setMinimo(Integer minimo) {
        this.minimo = minimo;
    }

    public Integer getMaximo() {
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
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
        return "Puesto{" + "id=" + id + ", descripcion=" + descripcion + ", categoria=" + categoria + ", seccion=" + seccion + ", minima=" + minimo + ", maxima=" + maximo + ", status=" + status + ", rangoAcademico=" + rangoAcademico + ", version=" + version + '}';
    }

	

}
