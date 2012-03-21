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
package mx.edu.um.mateo.activos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Entity
@Table(name = "xactivos")
public class XActivo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(nullable = false, length = 64)
    private String folio;
    @Column(length = 64)
    private String procedencia;
    @Column(length = 32)
    private String factura;
    @Column(length = 64)
    private String pedimento;
    @Column(length = 32)
    private String moneda;
    @Column(scale = 2, precision = 8)
    private BigDecimal tipoCambio;
    @Column(length = 64)
    private String condicion;
    @Column(length = 64)
    private String poliza;
    @Column(length = 64)
    private String codigo;
    @Column(length = 200)
    private String descripcion;
    @Column(length = 64)
    private String marca;
    @Column(length = 64)
    private String modelo;
    @Column(length = 64)
    private String serial;
    @Column(nullable = false, scale = 2, precision = 8)
    private BigDecimal moi = BigDecimal.ZERO;
    @Column(nullable = false, scale = 2, precision = 8)
    private BigDecimal valorRescate = BigDecimal.ONE;
    @Column(nullable = false, scale = 2, precision = 8)
    private BigDecimal inpc = BigDecimal.ZERO;
    private String ubicacion;
    @Column(length = 64)
    private Boolean inactivo = false;
    @Temporal(TemporalType.DATE)
    private Date fechaInactivo;
    @Column(nullable = false)
    private Long tipoActivoId;
    @Column(nullable = false)
    private Long proveedorId;
    @Column(nullable = false)
    private Long departamentoId;
    @Column(nullable = false)
    private Long empresaId;
    @Column(nullable = false)
    private Long activoId;
    @Column(length = 128)
    private String responsable;
    @Column(length = 32)
    private String motivo = "COMPRA";
    @Column(nullable = false)
    private Boolean garantia = false;
    @Column(nullable = false)
    private Integer mesesGarantia = 0;
    @Column(nullable = false)
    private Boolean seguro = false;
    @Column(nullable = false, scale = 2, precision = 8)
    private BigDecimal valorNeto = BigDecimal.ZERO;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fechaCreacion;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date fechaModificacion;
    @Column(nullable = false, length = 64)
    private String creador;
    @Column(nullable = false, length = 64)
    private String actividad;

    public XActivo() {
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
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
     * @param version the version to set
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
     * @param folio the folio to set
     */
    public void setFolio(String folio) {
        this.folio = folio;
    }

    /**
     * @return the procedencia
     */
    public String getProcedencia() {
        return procedencia;
    }

    /**
     * @param procedencia the procedencia to set
     */
    public void setProcedencia(String procedencia) {
        this.procedencia = procedencia;
    }

    /**
     * @return the factura
     */
    public String getFactura() {
        return factura;
    }

    /**
     * @param factura the factura to set
     */
    public void setFactura(String factura) {
        this.factura = factura;
    }

    /**
     * @return the pedimento
     */
    public String getPedimento() {
        return pedimento;
    }

    /**
     * @param pedimento the pedimento to set
     */
    public void setPedimento(String pedimento) {
        this.pedimento = pedimento;
    }

    /**
     * @return the moneda
     */
    public String getMoneda() {
        return moneda;
    }

    /**
     * @param moneda the moneda to set
     */
    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    /**
     * @return the tipoCambio
     */
    public BigDecimal getTipoCambio() {
        return tipoCambio;
    }

    /**
     * @param tipoCambio the tipoCambio to set
     */
    public void setTipoCambio(BigDecimal tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    /**
     * @return the condicion
     */
    public String getCondicion() {
        return condicion;
    }

    /**
     * @param condicion the condicion to set
     */
    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    /**
     * @return the poliza
     */
    public String getPoliza() {
        return poliza;
    }

    /**
     * @param poliza the poliza to set
     */
    public void setPoliza(String poliza) {
        this.poliza = poliza;
    }

    /**
     * @return the codigo
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return the marca
     */
    public String getMarca() {
        return marca;
    }

    /**
     * @param marca the marca to set
     */
    public void setMarca(String marca) {
        this.marca = marca;
    }

    /**
     * @return the modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * @return the serial
     */
    public String getSerial() {
        return serial;
    }

    /**
     * @param serial the serial to set
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * @return the moi
     */
    public BigDecimal getMoi() {
        return moi;
    }

    /**
     * @param moi the moi to set
     */
    public void setMoi(BigDecimal moi) {
        this.moi = moi;
    }

    /**
     * @return the valorRescate
     */
    public BigDecimal getValorRescate() {
        return valorRescate;
    }

    /**
     * @param valorRescate the valorRescate to set
     */
    public void setValorRescate(BigDecimal valorRescate) {
        this.valorRescate = valorRescate;
    }

    /**
     * @return the inpc
     */
    public BigDecimal getInpc() {
        return inpc;
    }

    /**
     * @param inpc the inpc to set
     */
    public void setInpc(BigDecimal inpc) {
        this.inpc = inpc;
    }

    /**
     * @return the ubicacion
     */
    public String getUbicacion() {
        return ubicacion;
    }

    /**
     * @param ubicacion the ubicacion to set
     */
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    /**
     * @return the inactivo
     */
    public Boolean getInactivo() {
        return inactivo;
    }

    /**
     * @param inactivo the inactivo to set
     */
    public void setInactivo(Boolean inactivo) {
        this.inactivo = inactivo;
    }

    /**
     * @return the fechaInactivo
     */
    public Date getFechaInactivo() {
        return fechaInactivo;
    }

    /**
     * @param fechaInactivo the fechaInactivo to set
     */
    public void setFechaInactivo(Date fechaInactivo) {
        this.fechaInactivo = fechaInactivo;
    }

    /**
     * @return the tipoActivoId
     */
    public Long getTipoActivoId() {
        return tipoActivoId;
    }

    /**
     * @param tipoActivoId the tipoActivoId to set
     */
    public void setTipoActivoId(Long tipoActivoId) {
        this.tipoActivoId = tipoActivoId;
    }

    /**
     * @return the proveedorId
     */
    public Long getProveedorId() {
        return proveedorId;
    }

    /**
     * @param proveedorId the proveedorId to set
     */
    public void setProveedorId(Long proveedorId) {
        this.proveedorId = proveedorId;
    }

    /**
     * @return the departamentoId
     */
    public Long getDepartamentoId() {
        return departamentoId;
    }

    /**
     * @param departamentoId the departamentoId to set
     */
    public void setDepartamentoId(Long departamentoId) {
        this.departamentoId = departamentoId;
    }

    /**
     * @return the empresaId
     */
    public Long getEmpresaId() {
        return empresaId;
    }

    /**
     * @param empresaId the empresaId to set
     */
    public void setEmpresaId(Long empresaId) {
        this.empresaId = empresaId;
    }

    /**
     * @return the activoId
     */
    public Long getActivoId() {
        return activoId;
    }

    /**
     * @param activoId the activoId to set
     */
    public void setActivoId(Long activoId) {
        this.activoId = activoId;
    }

    /**
     * @return the responsable
     */
    public String getResponsable() {
        return responsable;
    }

    /**
     * @param responsable the responsable to set
     */
    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    /**
     * @return the motivo
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * @param motivo the motivo to set
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /**
     * @return the garantia
     */
    public Boolean getGarantia() {
        return garantia;
    }

    /**
     * @param garantia the garantia to set
     */
    public void setGarantia(Boolean garantia) {
        this.garantia = garantia;
    }

    /**
     * @return the mesesGarantia
     */
    public Integer getMesesGarantia() {
        return mesesGarantia;
    }

    /**
     * @param mesesGarantia the mesesGarantia to set
     */
    public void setMesesGarantia(Integer mesesGarantia) {
        this.mesesGarantia = mesesGarantia;
    }

    /**
     * @return the seguro
     */
    public Boolean getSeguro() {
        return seguro;
    }

    /**
     * @param seguro the seguro to set
     */
    public void setSeguro(Boolean seguro) {
        this.seguro = seguro;
    }

    /**
     * @return the valorNeto
     */
    public BigDecimal getValorNeto() {
        return valorNeto;
    }

    /**
     * @param valorNeto the valorNeto to set
     */
    public void setValorNeto(BigDecimal valorNeto) {
        this.valorNeto = valorNeto;
    }

    /**
     * @return the fechaCreacion
     */
    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * @param fechaCreacion the fechaCreacion to set
     */
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * @return the fechaModificacion
     */
    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    /**
     * @param fechaModificacion the fechaModificacion to set
     */
    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    /**
     * @return the creador
     */
    public String getCreador() {
        return creador;
    }

    /**
     * @param creador the creador to set
     */
    public void setCreador(String creador) {
        this.creador = creador;
    }

    /**
     * @return the actividad
     */
    public String getActividad() {
        return actividad;
    }

    /**
     * @param actividad the actividad to set
     */
    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    @Override
    public String toString() {
        return "XActivo{" + "folio=" + folio + ", activoId=" + activoId + ", creador=" + creador + ", actividad=" + actividad + '}';
    }

}
