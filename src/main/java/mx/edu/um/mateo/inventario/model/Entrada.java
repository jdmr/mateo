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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import mx.edu.um.mateo.general.model.Proveedor;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Entity
@Table(name = "entradas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"almacen_id", "folio"}),
    @UniqueConstraint(columnNames = {"almacen_id", "factura"})
})
public class Entrada implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(nullable = false, length = 64)
    private String folio;
    @NotBlank
    @Column(nullable = false, length = 64)
    private String factura;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "fecha_factura")
    private Date fechaFactura;
    @Column(length = 128)
    private String comentarios;
    @Column(scale = 2, precision = 8)
    private BigDecimal tipoCambio;
    @Column(scale = 2, precision = 8, nullable = false)
    private BigDecimal iva = new BigDecimal("0");
    @Column(scale = 2, precision = 8, nullable = false)
    private BigDecimal total = new BigDecimal("0");
    private Boolean devolucion = false;
    @ManyToOne(optional = false)
    private Estatus estatus;
    @ManyToOne(optional = false)
    private Proveedor proveedor;
    @ManyToOne(optional = false)
    private Almacen almacen;
    @OneToMany(mappedBy = "entrada")
    private List<LoteEntrada> lotes = new ArrayList<>();

    public Entrada() {
    }

    public Entrada(String folio, String factura, Date fechaFactura, Estatus estatus, Proveedor proveedor, Almacen almacen) {
        this.folio = folio;
        this.factura = factura;
        this.fechaFactura = fechaFactura;
        this.estatus = estatus;
        this.proveedor = proveedor;
        this.almacen = almacen;
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
     * @return the fechaFactura
     */
    public Date getFechaFactura() {
        return fechaFactura;
    }

    /**
     * @param fechaFactura the fechaFactura to set
     */
    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    /**
     * @return the comentarios
     */
    public String getComentarios() {
        return comentarios;
    }

    /**
     * @param comentarios the comentarios to set
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
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
     * @return the total
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @param total the total to set
     */
    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    /**
     * @return the devolucion
     */
    public Boolean getDevolucion() {
        return devolucion;
    }

    /**
     * @param devolucion the devolucion to set
     */
    public void setDevolucion(Boolean devolucion) {
        this.devolucion = devolucion;
    }

    /**
     * @return the estatus
     */
    public Estatus getEstatus() {
        return estatus;
    }

    /**
     * @param estatus the estatus to set
     */
    public void setEstatus(Estatus estatus) {
        this.estatus = estatus;
    }

    /**
     * @return the proveedor
     */
    public Proveedor getProveedor() {
        return proveedor;
    }

    /**
     * @param proveedor the proveedor to set
     */
    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * @return the almacen
     */
    public Almacen getAlmacen() {
        return almacen;
    }

    /**
     * @param almacen the almacen to set
     */
    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    /**
     * @return the lotes
     */
    public List<LoteEntrada> getLotes() {
        return lotes;
    }

    /**
     * @param lotes the lotes to set
     */
    public void setLotes(List<LoteEntrada> lotes) {
        this.lotes = lotes;
    }

    public BigDecimal getSubtotal() {
        BigDecimal subtotal = total.subtract(iva).setScale(2, RoundingMode.HALF_UP);
        return subtotal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Entrada other = (Entrada) obj;
        if (!Objects.equals(this.folio, other.folio)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.version);
        hash = 23 * hash + Objects.hashCode(this.folio);
        return hash;
    }

    @Override
    public String toString() {
        return "Entrada{" + "folio=" + folio + ", factura=" + factura + ", total=" + total + '}';
    }
}
