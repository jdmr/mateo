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
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import mx.edu.um.mateo.general.model.Usuario;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Entity
@Table(name = "vacaciones")
public class SolicitudVacaciones implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2407891007463889865L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Version
	private Integer version;
	//@ManyToOne(optional = false)
	private Empleado empleado;
	@Column(name = "dias_vacaciones", nullable = false)
	private Integer diasVacaciones;
	@Column(name = "prima_vacacional", nullable = false)
	private Short primaVacacional;
	@Column(name = "visita_padre", nullable = false)
	private Boolean visitaPadres = false;
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_inicial", nullable = false)
	private Date fechaInicial;
	@Temporal(TemporalType.DATE)
	@Column(name = "fecha_final", nullable = false)
	private Date fechaFinal;
	@Column(nullable = false)
	private String destino;
	@Column(nullable = false)
	private Boolean nacional = true;
	private Integer kilometros;
	@Column(nullable = false)
	private String contacto;
	@Column(name = "contacto_email", nullable = false)
	private String contactoEmail;
	@Column(name = "contacto_telefono", nullable = false)
	private String contactoTelefono;
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_captura")
	private Usuario userCaptura;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_captura", nullable = false)
	private Date fechaCaptura;
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_recibe")
	private Usuario userRecibeRH;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_recibe_rh")
	private Date fechaRecibeRH;
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_autoriza_rh")
	private Usuario userAutorizaRH;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_autoriza_rh")
	private Date fechaAutorizaRH;
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_autoriza_jefe")
	private Usuario userAutorizaJefe;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_autoriza_jefe")
	private Date fechaAutorizaJefe;
	@Column(length = 2, nullable = false)
	private String status;
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "fecha_prima_vacacional")
	private Date fechaPrimaVacacional;
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_prima_vacacional")
	private Usuario userPrimaVacacional;
	@Column(length = 20)
	private String folio;
	@Column(name = "observaciones_empleado")
	private String observacionesEmpleado;
	@Column(name = "observaciones_rh")
	private String observacionesRH;
	@Column(name = "folio_pago", length = 10)
	private String folioPago;
	@Column(nullable = false)
	private Boolean furlough = false;

	public SolicitudVacaciones() {
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
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
	 * @return the empleado
	 */
	public Empleado getEmpleado() {
		return empleado;
	}

	/**
	 * @param empleado
	 *            the empleado to set
	 */
	public void setEmpleado(Empleado empleado) {
		this.empleado = empleado;
	}

	/**
	 * @return the diasVacaciones
	 */
	public Integer getDiasVacaciones() {
		return diasVacaciones;
	}

	/**
	 * @param diasVacaciones
	 *            the diasVacaciones to set
	 */
	public void setDiasVacaciones(Integer diasVacaciones) {
		this.diasVacaciones = diasVacaciones;
	}

	/**
	 * @return the primaVacacional
	 */
	public Short getPrimaVacacional() {
		return primaVacacional;
	}

	/**
	 * @param primaVacacional
	 *            the primaVacacional to set
	 */
	public void setPrimaVacacional(Short primaVacacional) {
		this.primaVacacional = primaVacacional;
	}

	/**
	 * @return the visitaPadres
	 */
	public Boolean getVisitaPadres() {
		return visitaPadres;
	}

	/**
	 * @param visitaPadres
	 *            the visitaPadres to set
	 */
	public void setVisitaPadres(Boolean visitaPadres) {
		this.visitaPadres = visitaPadres;
	}

	/**
	 * @return the fechaInicial
	 */
	public Date getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial
	 *            the fechaInicial to set
	 */
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return the fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal
	 *            the fechaFinal to set
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return the destino
	 */
	public String getDestino() {
		return destino;
	}

	/**
	 * @param destino
	 *            the destino to set
	 */
	public void setDestino(String destino) {
		this.destino = destino;
	}

	/**
	 * @return the nacional
	 */
	public Boolean getNacional() {
		return nacional;
	}

	/**
	 * @param nacional
	 *            the nacional to set
	 */
	public void setNacional(Boolean nacional) {
		this.nacional = nacional;
	}

	/**
	 * @return the kilometros
	 */
	public Integer getKilometros() {
		return kilometros;
	}

	/**
	 * @param kilometros
	 *            the kilometros to set
	 */
	public void setKilometros(Integer kilometros) {
		this.kilometros = kilometros;
	}

	/**
	 * @return the contacto
	 */
	public String getContacto() {
		return contacto;
	}

	/**
	 * @param contacto
	 *            the contacto to set
	 */
	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	/**
	 * @return the contactoEmail
	 */
	public String getContactoEmail() {
		return contactoEmail;
	}

	/**
	 * @param contactoEmail
	 *            the contactoEmail to set
	 */
	public void setContactoEmail(String contactoEmail) {
		this.contactoEmail = contactoEmail;
	}

	/**
	 * @return the contactoTelefono
	 */
	public String getContactoTelefono() {
		return contactoTelefono;
	}

	/**
	 * @param contactoTelefono
	 *            the contactoTelefono to set
	 */
	public void setContactoTelefono(String contactoTelefono) {
		this.contactoTelefono = contactoTelefono;
	}

	/**
	 * @return the userCaptura
	 */
	public Usuario getUserCaptura() {
		return userCaptura;
	}

	/**
	 * @param userCaptura
	 *            the userCaptura to set
	 */
	public void setUserCaptura(Usuario userCaptura) {
		this.userCaptura = userCaptura;
	}

	/**
	 * @return the fechaCaptura
	 */
	public Date getFechaCaptura() {
		return fechaCaptura;
	}

	/**
	 * @param fechaCaptura
	 *            the fechaCaptura to set
	 */
	public void setFechaCaptura(Date fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}

	/**
	 * @return the userRecibeRH
	 */
	public Usuario getUserRecibeRH() {
		return userRecibeRH;
	}

	/**
	 * @param userRecibeRH
	 *            the userRecibeRH to set
	 */
	public void setUserRecibeRH(Usuario userRecibeRH) {
		this.userRecibeRH = userRecibeRH;
	}

	/**
	 * @return the fechaRecibeRH
	 */
	public Date getFechaRecibeRH() {
		return fechaRecibeRH;
	}

	/**
	 * @param fechaRecibeRH
	 *            the fechaRecibeRH to set
	 */
	public void setFechaRecibeRH(Date fechaRecibeRH) {
		this.fechaRecibeRH = fechaRecibeRH;
	}

	/**
	 * @return the userAutorizaRH
	 */
	public Usuario getUserAutorizaRH() {
		return userAutorizaRH;
	}

	/**
	 * @param userAutorizaRH
	 *            the userAutorizaRH to set
	 */
	public void setUserAutorizaRH(Usuario userAutorizaRH) {
		this.userAutorizaRH = userAutorizaRH;
	}

	/**
	 * @return the fechaAutorizaRH
	 */
	public Date getFechaAutorizaRH() {
		return fechaAutorizaRH;
	}

	/**
	 * @param fechaAutorizaRH
	 *            the fechaAutorizaRH to set
	 */
	public void setFechaAutorizaRH(Date fechaAutorizaRH) {
		this.fechaAutorizaRH = fechaAutorizaRH;
	}

	/**
	 * @return the userAutorizaJefe
	 */
	public Usuario getUserAutorizaJefe() {
		return userAutorizaJefe;
	}

	/**
	 * @param userAutorizaJefe
	 *            the userAutorizaJefe to set
	 */
	public void setUserAutorizaJefe(Usuario userAutorizaJefe) {
		this.userAutorizaJefe = userAutorizaJefe;
	}

	/**
	 * @return the fechaAutorizaJefe
	 */
	public Date getFechaAutorizaJefe() {
		return fechaAutorizaJefe;
	}

	/**
	 * @param fechaAutorizaJefe
	 *            the fechaAutorizaJefe to set
	 */
	public void setFechaAutorizaJefe(Date fechaAutorizaJefe) {
		this.fechaAutorizaJefe = fechaAutorizaJefe;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the fechaPrimaVacacional
	 */
	public Date getFechaPrimaVacacional() {
		return fechaPrimaVacacional;
	}

	/**
	 * @param fechaPrimaVacacional
	 *            the fechaPrimaVacacional to set
	 */
	public void setFechaPrimaVacacional(Date fechaPrimaVacacional) {
		this.fechaPrimaVacacional = fechaPrimaVacacional;
	}

	/**
	 * @return the userPrimaVacacional
	 */
	public Usuario getUserPrimaVacacional() {
		return userPrimaVacacional;
	}

	/**
	 * @param userPrimaVacacional
	 *            the userPrimaVacacional to set
	 */
	public void setUserPrimaVacacional(Usuario userPrimaVacacional) {
		this.userPrimaVacacional = userPrimaVacacional;
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
	 * @return the observacionesEmpleado
	 */
	public String getObservacionesEmpleado() {
		return observacionesEmpleado;
	}

	/**
	 * @param observacionesEmpleado
	 *            the observacionesEmpleado to set
	 */
	public void setObservacionesEmpleado(String observacionesEmpleado) {
		this.observacionesEmpleado = observacionesEmpleado;
	}

	/**
	 * @return the observacionesRH
	 */
	public String getObservacionesRH() {
		return observacionesRH;
	}

	/**
	 * @param observacionesRH
	 *            the observacionesRH to set
	 */
	public void setObservacionesRH(String observacionesRH) {
		this.observacionesRH = observacionesRH;
	}

	/**
	 * @return the folioPago
	 */
	public String getFolioPago() {
		return folioPago;
	}

	/**
	 * @param folioPago
	 *            the folioPago to set
	 */
	public void setFolioPago(String folioPago) {
		this.folioPago = folioPago;
	}

	/**
	 * @return the furlough
	 */
	public Boolean getFurlough() {
		return furlough;
	}

	/**
	 * @param furlough
	 *            the furlough to set
	 */
	public void setFurlough(Boolean furlough) {
		this.furlough = furlough;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + Objects.hashCode(this.id);
		hash = 79 * hash + Objects.hashCode(this.version);
		hash = 79 * hash + Objects.hashCode(this.empleado);
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
		final SolicitudVacaciones other = (SolicitudVacaciones) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "SolicitudVacaciones{" + "id=" + id + ", version=" + version
				+ ", empleado=" + empleado + ", diasVacaciones="
				+ diasVacaciones + ", primaVacacional=" + primaVacacional
				+ ", visitaPadres=" + visitaPadres + ", fechaInicial="
				+ fechaInicial + ", fechaFinal=" + fechaFinal + ", destino="
				+ destino + ", nacional=" + nacional + ", kilometros="
				+ kilometros + ", contacto=" + contacto + ", contactoEmail="
				+ contactoEmail + ", contactoTelefono=" + contactoTelefono
				+ ", userCaptura=" + userCaptura + ", fechaCaptura="
				+ fechaCaptura + ", userRecibeRH=" + userRecibeRH
				+ ", fechaRecibeRH=" + fechaRecibeRH + ", userAutorizaRH="
				+ userAutorizaRH + ", fechaAutorizaRH=" + fechaAutorizaRH
				+ ", userAutorizaJefe=" + userAutorizaJefe
				+ ", fechaAutorizaJefe=" + fechaAutorizaJefe + ", status="
				+ status + ", fechaPrimaVacacional=" + fechaPrimaVacacional
				+ ", userPrimaVacacional=" + userPrimaVacacional + ", folio="
				+ folio + ", observacionesEmpleado=" + observacionesEmpleado
				+ ", observacionesRH=" + observacionesRH + ", folioPago="
				+ folioPago + ", furlough=" + furlough + '}';
	}
}
