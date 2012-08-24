/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Reporte;
import mx.edu.um.mateo.general.model.Usuario;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * @author jdmr
 */
@Entity
@Table(name = "almacenes", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "empresa_id", "codigo" }),
		@UniqueConstraint(columnNames = { "empresa_id", "nombre" }) })
public class Almacen implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1822281411455233523L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Version
	private Integer version;
	@NotBlank
	@Column(nullable = false, length = 6)
	private String codigo;
	@NotBlank
	@Column(nullable = false, length = 128)
	private String nombre;
	@ManyToOne(optional = false)
	private Empresa empresa;
	@OneToMany(mappedBy = "almacen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Usuario> usuarios = new ArrayList<>();
	@OneToMany(mappedBy = "almacen", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TipoProducto> tiposDeProducto = new ArrayList<>();
	@ManyToMany
	@JoinTable(name = "almacenes_reportes", joinColumns = { @JoinColumn(name = "almacen_id") }, inverseJoinColumns = { @JoinColumn(name = "reporte_id") })
	private List<Reporte> reportes = new ArrayList<>();

	public Almacen() {
	}

	public Almacen(String codigo, String nombre, Empresa empresa) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.empresa = empresa;
	}

	public Almacen(String nombre) {
		this.nombre = nombre;
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
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
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
	 * @return the empresa
	 */
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            the empresa to set
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return the usuarios
	 */
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	/**
	 * @param usuarios
	 *            the usuarios to set
	 */
	public void setUsuarios(List<Usuario> usuarios) {
		this.setUsuarios(usuarios);
	}

	/**
	 * @return the tiposDeProducto
	 */
	public List<TipoProducto> getTiposDeProducto() {
		return tiposDeProducto;
	}

	/**
	 * @param tiposDeProducto
	 *            the tiposDeProducto to set
	 */
	public void setTiposDeProducto(List<TipoProducto> tiposDeProducto) {
		this.tiposDeProducto = tiposDeProducto;
	}

	/**
	 * Obtiene el nombre con los nombres tanto de empresa y organizacion
	 * 
	 * @return nombre El nombre completo del almacen
	 *         (organizacion|empresa|almacen)
	 */
	public String getNombreCompleto() {
		StringBuilder sb = new StringBuilder();
		sb.append(getEmpresa().getOrganizacion().getNombre());
		sb.append(" | ");
		sb.append(getEmpresa().getNombre());
		sb.append(" | ");
		sb.append(getNombre());
		return sb.toString();
	}

	/**
	 * No hace nada. NO USAR.
	 * 
	 * @param nombreCompleto
	 */
	public void setNombreCompleto(String nombreCompleto) {
		// no hace nada
	}

	/**
	 * @return the reportes
	 */
	public List<Reporte> getReportes() {
		return reportes;
	}

	/**
	 * @param reportes
	 *            the reportes to set
	 */
	public void setReportes(List<Reporte> reportes) {
		this.reportes = reportes;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Almacen other = (Almacen) obj;
		if (!Objects.equals(this.nombre, other.nombre)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 67 * hash + Objects.hashCode(this.id);
		hash = 67 * hash + Objects.hashCode(this.version);
		hash = 67 * hash + Objects.hashCode(this.nombre);
		return hash;
	}

	@Override
	public String toString() {
		return "Almacen{" + "nombre=" + nombre + '}';
	}
}
