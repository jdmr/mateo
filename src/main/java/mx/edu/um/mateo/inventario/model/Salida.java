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
import java.util.List;
import java.util.Objects;
import javax.persistence.*;
import mx.edu.um.mateo.general.model.Cliente;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Entity
@Table(name = "salida", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"almacen_id", "folio"})
})
public class Salida implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(nullable = false, length = 64)
    private String folio;
    @Column(length = 64)
    private String atendio;
    @Column(length = 64)
    private String reporte;
    @Column(length = 64)
    private String empleado;
    @Column(length = 64)
    private String departamento;
    @Column(length = 254)
    private String comentarios;
    @Column(scale = 2, precision = 8, nullable = false)
    private BigDecimal iva = new BigDecimal("0");
    @Column(scale = 2, precision = 8, nullable = false)
    private BigDecimal total = new BigDecimal("0");
    @ManyToOne(optional = false)
    private Estatus estatus;
    @ManyToOne(optional = false)
    private Cliente cliente;
    @ManyToOne(optional = false)
    private Almacen almacen;
    @OneToMany(mappedBy = "salida", cascade = CascadeType.REMOVE)
    private List<LoteSalida> lotes = new ArrayList<>();

    public Salida() {
    }

    public Salida(String atendio, String reporte, String empleado, String departamento, String comentarios, Estatus estatus, Cliente cliente, Almacen almacen) {
        this.atendio = atendio;
        this.reporte = reporte;
        this.empleado = empleado;
        this.departamento = departamento;
        this.comentarios = comentarios;
        this.estatus = estatus;
        this.cliente = cliente;
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
     * @return the atendio
     */
    public String getAtendio() {
        return atendio;
    }

    /**
     * @param atendio the atendio to set
     */
    public void setAtendio(String atendio) {
        this.atendio = atendio;
    }

    /**
     * @return the reporte
     */
    public String getReporte() {
        return reporte;
    }

    /**
     * @param reporte the reporte to set
     */
    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    /**
     * @return the empleado
     */
    public String getEmpleado() {
        return empleado;
    }

    /**
     * @param empleado the empleado to set
     */
    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }

    /**
     * @return the departamento
     */
    public String getDepartamento() {
        return departamento;
    }

    /**
     * @param departamento the departamento to set
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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
     * @return the lotes
     */
    public List<LoteSalida> getLotes() {
        return lotes;
    }

    /**
     * @param lotes the lotes to set
     */
    public void setLotes(List<LoteSalida> lotes) {
        this.lotes = lotes;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Salida other = (Salida) obj;
        if (!Objects.equals(this.folio, other.folio)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.version);
        hash = 67 * hash + Objects.hashCode(this.folio);
        return hash;
    }

    @Override
    public String toString() {
        return "Salida{" + "folio=" + folio + ", atendio=" + atendio + ", reporte=" + reporte + ", empleado=" + empleado + ", departamento=" + departamento + ", iva=" + iva + ", total=" + total + ", estatus=" + estatus + ", cliente=" + cliente + ", almacen=" + almacen + '}';
    }
}
