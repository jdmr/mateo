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
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Entity
@Table(name = "xlotes_salida")
public class XLoteSalida implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(nullable = false, scale = 3, precision = 8)
    private BigDecimal cantidad;
    @Column(nullable = false, scale = 2, precision = 8, name = "precio_unitario")
    private BigDecimal precioUnitario;
    @Column(nullable = false, scale = 2, precision = 8)
    private BigDecimal iva;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "date_created")
    private Date fechaCreacion;
    @Column(nullable = false, name = "lote_salida_id")
    private Long loteSalidaId;
    @Column(nullable = false, name = "producto_id")
    private Long productoId;
    @Column(nullable = false, name = "salida_id")
    private Long salidaId;
    @Column(nullable = false, length = 32)
    private String actividad;
    @Column(nullable = false, length = 64)
    private String creador;

    public XLoteSalida() {
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
     * @return the cantidad
     */
    public BigDecimal getCantidad() {
        return cantidad;
    }

    /**
     * @param cantidad the cantidad to set
     */
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * @return the precioUnitario
     */
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * @param precioUnitario the precioUnitario to set
     */
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * @return the iva
     */
    public BigDecimal getIva() {
        return iva;
    }

    /**
     * @param iva the iva to set
     */
    public void setIva(BigDecimal iva) {
        this.iva = iva;
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
     * @return the loteSalidaId
     */
    public Long getLoteSalidaId() {
        return loteSalidaId;
    }

    /**
     * @param loteSalidaId the loteSalidaId to set
     */
    public void setLoteSalidaId(Long loteSalidaId) {
        this.loteSalidaId = loteSalidaId;
    }

    /**
     * @return the productoId
     */
    public Long getProductoId() {
        return productoId;
    }

    /**
     * @param productoId the productoId to set
     */
    public void setProductoId(Long productoId) {
        this.productoId = productoId;
    }

    /**
     * @return the salidaId
     */
    public Long getSalidaId() {
        return salidaId;
    }

    /**
     * @param salidaId the salidaId to set
     */
    public void setSalidaId(Long salidaId) {
        this.salidaId = salidaId;
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.loteSalidaId);
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
        final XLoteSalida other = (XLoteSalida) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.loteSalidaId, other.loteSalidaId)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "XLoteSalida{" + "id=" + id + ", version=" + version + ", cantidad=" + cantidad + ", precioUnitario=" + precioUnitario + ", iva=" + iva + ", fechaCreacion=" + fechaCreacion + ", loteSalidaId=" + loteSalidaId + ", productoId=" + productoId + ", salidaId=" + salidaId + ", actividad=" + actividad + ", creador=" + creador + '}';
    }
}
