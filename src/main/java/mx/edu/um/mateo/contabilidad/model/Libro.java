package mx.edu.um.mateo.contabilidad.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * 
 * @author develop
 */
@Entity
@Table(name = "cont_libro")
public class Libro implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -123680422669945690L;
	@Id
	private LibroPK id;
	@Version
	private Integer version;
	@Column(length = 60, nullable = false)
	private String nombre;

	public Libro() {
	}

	public Libro(LibroPK id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}

	/**
	 * @return the id
	 */
	public LibroPK getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(LibroPK id) {
		this.id = id;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Integer version) {
		this.version = version;
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
}
