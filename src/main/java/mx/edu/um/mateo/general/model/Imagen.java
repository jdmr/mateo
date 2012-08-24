/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.general.model;

import java.io.Serializable;
import java.util.Objects;

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
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Entity
@Table(name = "imagenes")
public class Imagen implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3387974275938400076L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Version
	private Integer version;
	@NotBlank
	@Column(nullable = false, length = 64)
	private String nombre;
	@NotBlank
	@Column(nullable = false, length = 128, name = "tipo_contenido")
	private String tipoContenido;
	@Column(nullable = false)
	private Long tamano;
	@Column(nullable = false)
	private byte[] archivo;

	public Imagen() {
	}

	public Imagen(String nombre, String tipoContenido, Long tamano,
			byte[] archivo) {
		this.nombre = nombre;
		this.tipoContenido = tipoContenido;
		this.tamano = tamano;
		this.archivo = archivo;
	}

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

	/**
	 * @return the tipoContenido
	 */
	public String getTipoContenido() {
		return tipoContenido;
	}

	/**
	 * @param tipoContenido
	 *            the tipoContenido to set
	 */
	public void setTipoContenido(String tipoContenido) {
		this.tipoContenido = tipoContenido;
	}

	/**
	 * @return the tamano
	 */
	public Long getTamano() {
		return tamano;
	}

	/**
	 * @param tamano
	 *            the tamano to set
	 */
	public void setTamano(Long tamano) {
		this.tamano = tamano;
	}

	/**
	 * @return the archivo
	 */
	public byte[] getArchivo() {
		return archivo;
	}

	/**
	 * @param archivo
	 *            the archivo to set
	 */
	public void setArchivo(byte[] archivo) {
		this.archivo = archivo;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Imagen other = (Imagen) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 97 * hash + Objects.hashCode(this.id);
		hash = 97 * hash + Objects.hashCode(this.version);
		hash = 97 * hash + Objects.hashCode(this.nombre);
		return hash;
	}

	@Override
	public String toString() {
		return "Imagen{" + "nombre=" + nombre + ", tipoContenido="
				+ tipoContenido + '}';
	}
}
