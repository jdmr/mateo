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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import mx.edu.um.mateo.general.model.Cliente;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Entity
@Table(name = "facturas_almacen", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"almacen_id", "folio"})
})
public class FacturaAlmacen implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(nullable = false, length = 64)
    private String folio;
    @Column(length = 128)
    private String comentarios;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @Column(scale = 2, precision = 8, nullable = false)
    private BigDecimal iva = BigDecimal.ZERO;
    @Column(scale = 2, precision = 8, nullable = false)
    private BigDecimal total = BigDecimal.ZERO;
    @ManyToOne(optional = false)
    private Estatus estatus;
    @ManyToOne(optional = false)
    private Cliente cliente;
    @ManyToOne(optional = false)
    private Almacen almacen;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "date_created")
    private Date fechaCreacion;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "last_updated")
    private Date fechaModificacion;
    @OneToMany
    @JoinTable(name = "facturas_almacen_entradas", joinColumns = {
        @JoinColumn(name = "factura_id")}, inverseJoinColumns = {
        @JoinColumn(name = "entrada_id")})
    private List<Entrada> entradas = new ArrayList<>();
    @OneToMany
    @JoinTable(name = "facturas_almacen_salidas", joinColumns = {
        @JoinColumn(name = "factura_id")}, inverseJoinColumns = {
        @JoinColumn(name = "salida_id")})
    private List<Salida> salidas = new ArrayList<>();

    public FacturaAlmacen() {
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
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
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
     * @return the cliente
     */
    public Cliente getCliente() {
        return cliente;
    }

    /**
     * @param cliente the cliente to set
     */
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
     * @return the entradas
     */
    public List<Entrada> getEntradas() {
        return entradas;
    }

    /**
     * @param entradas the entradas to set
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
     * @param salidas the salidas to set
     */
    public void setSalidas(List<Salida> salidas) {
        this.salidas = salidas;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FacturaAlmacen other = (FacturaAlmacen) obj;
        if (!Objects.equals(this.folio, other.folio)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.version);
        hash = 67 * hash + Objects.hashCode(this.folio);
        return hash;
    }

    @Override
    public String toString() {
        return "FacturaAlmacen{" + "folio=" + folio + '}';
    }
}
