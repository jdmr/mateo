/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * @author osoto
 */
@Entity
@Table(name = "seccion")
public class Seccion implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8291747354060833386L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotBlank
	@Column(nullable = false, length = 50)
	private String nombre;
        @Column(nullable = false, length = 50)
        private String categoriaId;
        @Column (nullable = false, length = 24)
        private Float rangoAcademico;
        @Column (nullable = false, length = 6, precision= 2)
        private Float maximo;
        @Column (nullable = false, length = 6, precision= 2)
        private Float minimo;
	@Version
	private Long version;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

        public String getCategoriaId() {
            return categoriaId;
        }

        public void setCategoriaId(String categoriaId) {
            this.categoriaId = categoriaId;
        }

        public Float getMaximo() {
            return maximo;
        }

        public void setMaximo(Float maximo) {
            this.maximo = maximo;
        }

        public Float getMinimo() {
            return minimo;
        }

        public void setMinimo(Float minimo) {
            this.minimo = minimo;
        }

        public Float getRangoAcademico() {
            return rangoAcademico;
        }

        public void setRangoAcademico(Float rangoAcademico) {
            this.rangoAcademico = rangoAcademico;
        }

	/**
	 * @return the version
	 */
	public Long getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Long version) {
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
		final Seccion other = (Seccion) obj;
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
		return "Seccion{" + "id=" + id + ", nombre=" + nombre + ", version="
				+ version + '}';
	}

}
