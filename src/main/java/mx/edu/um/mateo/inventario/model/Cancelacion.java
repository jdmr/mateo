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
package mx.edu.um.mateo.inventario.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Entity
@Table(name = "cancelaciones_almacen")
public class Cancelacion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7861788862053997189L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Version
	private Integer version;
	@Column(nullable = false, length = 128)
	private String folio;
	@Column(length = 200)
	private String comentarios;
	@ManyToOne
	private Entrada entrada;
	@ManyToOne
	private Salida salida;
	@ManyToOne(optional = false)
	private Almacen almacen;
	@ManyToMany
	@JoinTable(name = "cancelaciones_almacen_productos", joinColumns = { @JoinColumn(name = "cancelacion_id") }, inverseJoinColumns = { @JoinColumn(name = "producto_id") })
	private Set<Producto> productos;
	@OneToMany
	@JoinTable(name = "cancelaciones_almacen_entradas", joinColumns = { @JoinColumn(name = "cancelacion_id") }, inverseJoinColumns = { @JoinColumn(name = "entrada_id") })
	private List<Entrada> entradas;
	@OneToMany
	@JoinTable(name = "cancelaciones_almacen_salidas", joinColumns = { @JoinColumn(name = "cancelacion_id") }, inverseJoinColumns = { @JoinColumn(name = "salida_id") })
	private List<Salida> salidas;
	@Column(nullable = false, length = 64)
	private String creador;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, name = "date_created")
	private Date fechaCreacion;

	public Cancelacion() {
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
	 * @return the folio
	 */
	public String getFolio() {
		return folio;
	}

	/**
	 * @param folio
	 *            the folio to set
	 */
	public void setFolio(String folio) {
		this.folio = folio;
	}

	/**
	 * @return the comentarios
	 */
	public String getComentarios() {
		return comentarios;
	}

	/**
	 * @param comentarios
	 *            the comentarios to set
	 */
	public void setComentarios(String comentarios) {
		this.comentarios = comentarios;
	}

	/**
	 * @return the entrada
	 */
	public Entrada getEntrada() {
		return entrada;
	}

	/**
	 * @param entrada
	 *            the entrada to set
	 */
	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
		this.almacen = entrada.getAlmacen();
	}

	/**
	 * @return the salida
	 */
	public Salida getSalida() {
		return salida;
	}

	/**
	 * @param salida
	 *            the salida to set
	 */
	public void setSalida(Salida salida) {
		this.salida = salida;
		this.almacen = salida.getAlmacen();
	}

	/**
	 * @return the almacen
	 */
	public Almacen getAlmacen() {
		return almacen;
	}

	/**
	 * @param almacen
	 *            the almacen to set
	 */
	public void setAlmacen(Almacen almacen) {
		this.almacen = almacen;
	}

	/**
	 * @return the productos
	 */
	public Set<Producto> getProductos() {
		return productos;
	}

	/**
	 * @param productos
	 *            the productos to set
	 */
	public void setProductos(Set<Producto> productos) {
		this.productos = productos;
	}

	/**
	 * @return the entradas
	 */
	public List<Entrada> getEntradas() {
		return entradas;
	}

	/**
	 * @param entradas
	 *            the entradas to set
	 */
	public void setEntradas(List<Entrada> entradas) {
		this.entradas = entradas;
	}

	/**
	 * @return the salidas
	 */
	public List<Salida> getSalidas() {
		return salidas;
	}

	/**
	 * @param salidas
	 *            the salidas to set
	 */
	public void setSalidas(List<Salida> salidas) {
		this.salidas = salidas;
	}

	/**
	 * @return the creador
	 */
	public String getCreador() {
		return creador;
	}

	/**
	 * @param creador
	 *            the creador to set
	 */
	public void setCreador(String creador) {
		this.creador = creador;
	}

	/**
	 * @return the fechaCreacion
	 */
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion
	 *            the fechaCreacion to set
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Cancelacion other = (Cancelacion) obj;
		if (!Objects.equals(this.folio, other.folio)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 89 * hash + Objects.hashCode(this.id);
		hash = 89 * hash + Objects.hashCode(this.version);
		hash = 89 * hash + Objects.hashCode(this.folio);
		return hash;
	}

	@Override
	public String toString() {
		return "Cancelacion{" + "folio=" + folio + '}';
	}
}
