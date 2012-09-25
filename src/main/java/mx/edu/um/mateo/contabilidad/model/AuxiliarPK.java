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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Embeddable
public class AuxiliarPK implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5812982720327226255L;
	@ManyToOne(optional = false)
	@JoinColumns({ @JoinColumn(name = "id_ejercicio"),
			@JoinColumn(name = "id_organizacion") })
	private Ejercicio ejercicio;
	@Column(nullable = false, length = 20, name = "id_auxiliar")
	private String auxiliar;

	public AuxiliarPK() {
	}

	public AuxiliarPK(Ejercicio ejercicio, String auxiliar) {
		this.ejercicio = ejercicio;
		this.auxiliar = auxiliar;
	}

	/**
	 * @return the ejercicio
	 */
	public Ejercicio getEjercicio() {
		return ejercicio;
	}

	/**
	 * @param ejercicio
	 *            the ejercicio to set
	 */
	public void setEjercicio(Ejercicio ejercicio) {
		this.ejercicio = ejercicio;
	}

	/**
	 * @return the auxiliar
	 */
	public String getAuxiliar() {
		return auxiliar;
	}

	/**
	 * @param auxiliar
	 *            the auxiliar to set
	 */
	public void setAuxiliar(String auxiliar) {
		this.auxiliar = auxiliar;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 37 * hash + Objects.hashCode(this.ejercicio);
		hash = 37 * hash + Objects.hashCode(this.auxiliar);
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
		final AuxiliarPK other = (AuxiliarPK) obj;
		if (!Objects.equals(this.ejercicio, other.ejercicio)) {
			return false;
		}
		if (!Objects.equals(this.auxiliar, other.auxiliar)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "AuxiliarPK{" + "ejercicio=" + ejercicio + ", auxiliar="
				+ auxiliar + '}';
	}
}
