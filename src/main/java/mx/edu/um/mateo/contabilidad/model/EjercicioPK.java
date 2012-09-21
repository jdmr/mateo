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
package mx.edu.um.mateo.contabilidad.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import mx.edu.um.mateo.general.model.Organizacion;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Embeddable
public class EjercicioPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8523273562997747556L;
	@Column(name = "id_ejercicio", nullable = false, length = 8)
	private String idEjercicio;
	@ManyToOne(optional = false)
	private Organizacion organizacion;

	public EjercicioPK() {
	}

	public EjercicioPK(String idEjercicio, Organizacion organizacion) {
		this.idEjercicio = idEjercicio;
		this.organizacion = organizacion;
	}

	/**
	 * @return the idEjercicio
	 */
	public String getIdEjercicio() {
		return idEjercicio;
	}

	/**
	 * @param idEjercicio
	 *            the idEjercicio to set
	 */
	public void setIdEjercicio(String idEjercicio) {
		this.idEjercicio = idEjercicio;
	}

	/**
	 * @return the organizacion
	 */
	public Organizacion getOrganizacion() {
		return organizacion;
	}

	/**
	 * @param organizacion
	 *            the organizacion to set
	 */
	public void setOrganizacion(Organizacion organizacion) {
		this.organizacion = organizacion;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 17 * hash + Objects.hashCode(this.idEjercicio);
		hash = 17 * hash + Objects.hashCode(this.organizacion);
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
		final EjercicioPK other = (EjercicioPK) obj;
		if (!Objects.equals(this.idEjercicio, other.idEjercicio)) {
			return false;
		}
		if (!Objects.equals(this.organizacion, other.organizacion)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "EjercicioPK{" + "idEjercicio=" + idEjercicio
				+ ", organizacion=" + organizacion + '}';
	}
}
