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
package mx.edu.um.mateo.general.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.inventario.model.Almacen;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author jdmr
 */
@Entity
@Table(name = "empresas", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"organizacion_id", "codigo"}),
    @UniqueConstraint(columnNames = {"organizacion_id", "nombre"})})
public class Empresa implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7116598843287753824L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @NotBlank
    @Column(nullable = false, length = 6)
    private String codigo;
    @NotBlank
    @Column(nullable = false, length = 64)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 128, name = "nombre_completo")
    private String nombreCompleto;
    @NotBlank
    @Size(min = 12, max = 13)
    @Column(nullable = false, length = 13, name = "rfc")
    private String rfc;
    @ManyToOne
    private CentroCosto centroCosto;
    @ManyToOne(optional = false)
    private Organizacion organizacion;
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Almacen> almacenes = new ArrayList<>();
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Proveedor> proveedores = new ArrayList<>();
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<TipoCliente> tiposDeCliente = new ArrayList<>();
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Cliente> clientes = new ArrayList<>();
    @ManyToMany
    private List<Reporte> reportes = new ArrayList<>();

    public Empresa() {
    }

    public Empresa(String codigo, String nombre, String nombreCompleto,
            String rfc, Organizacion organizacion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.nombreCompleto = nombreCompleto;
        this.rfc = rfc;
        this.organizacion = organizacion;
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
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the nombreCompleto
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * @param nombreCompleto the nombreCompleto to set
     */
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    /**
     * @return the rfc
     */
    public String getRfc() {
        return rfc;
    }

    /**
     * @param rfc the rfc to set
     */
    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    /**
     * @return the centroCosto
     */
    public CentroCosto getCentroCosto() {
        return centroCosto;
    }

    /**
     * @param centroCosto the centroCosto to set
     */
    public void setCentroCosto(CentroCosto centroCosto) {
        this.centroCosto = centroCosto;
    }

    /**
     * @return the organizacion
     */
    public Organizacion getOrganizacion() {
        return organizacion;
    }

    /**
     * @param organizacion the organizacion to set
     */
    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    /**
     * @return the almacenes
     */
    public List<Almacen> getAlmacenes() {
        return almacenes;
    }

    /**
     * @param almacenes the almacenes to set
     */
    public void setAlmacenes(List<Almacen> almacenes) {
        this.almacenes = almacenes;
    }

    /**
     * @return the proveedores
     */
    public List<Proveedor> getProveedores() {
        return proveedores;
    }

    /**
     * @param proveedores the proveedores to set
     */
    public void setProveedores(List<Proveedor> proveedores) {
        this.proveedores = proveedores;
    }

    /**
     * @return the tiposDeCliente
     */
    public List<TipoCliente> getTiposDeCliente() {
        return tiposDeCliente;
    }

    /**
     * @param tiposDeCliente the tiposDeCliente to set
     */
    public void setTiposDeCliente(List<TipoCliente> tiposDeCliente) {
        this.tiposDeCliente = tiposDeCliente;
    }

    /**
     * @return the clientes
     */
    public List<Cliente> getClientes() {
        return clientes;
    }

    /**
     * @param clientes the clientes to set
     */
    public void setClientes(List<Cliente> clientes) {
        this.clientes = clientes;
    }

    /**
     * @return the reportes
     */
    public List<Reporte> getReportes() {
        return reportes;
    }

    /**
     * @param reportes the reportes to set
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
        final Empresa other = (Empresa) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.version);
        hash = 47 * hash + Objects.hashCode(this.codigo);
        return hash;
    }

    @Override
    public String toString() {
        return "Empresa{" + "nombre=" + nombre + '}';
    }
}
