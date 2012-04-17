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
import javax.persistence.*;
import mx.edu.um.mateo.general.model.Organizacion;

/**
 * NO UTILIZAR
 * 
 * Clase utilizada SOLO para migrar los activos, luego sera necesario hacer una
 * reingenieria en donde se actualice este objeto por un departamento que este
 * ligado a una CuentaMayor. Pero por ahora, migraremos con esta.
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Entity
@Table(name = "cuentas")
public class Cuenta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    @Column(nullable = false)
    private Integer version;
    @Column(nullable = false, length = 128)
    private String nombre;
    @Column(length = 128)
    private String nombre2;
    @Column(nullable = false, length = 200)
    private String descripcion;
    @Column(nullable = false, name="nivel_id")
    private Long nivel;
    @Column(nullable = false)
    private Boolean base = false;
    @Column(nullable = false, name="centro_costo")
    private Boolean centroCosto = false;
    @Column(nullable = false, name="tiene_auxiliares")
    private Boolean tieneAuxiliares = false;
    @ManyToOne(optional = false)
    private Organizacion organizacion;

    public Cuenta() {
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
     * @return the nombre2
     */
    public String getNombre2() {
        return nombre2;
    }

    /**
     * @param nombre2 the nombre2 to set
     */
    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
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
     * @return the nivel
     */
    public Long getNivel() {
        return nivel;
    }

    /**
     * @param nivel the nivel to set
     */
    public void setNivel(Long nivel) {
        this.nivel = nivel;
    }

    /**
     * @return the base
     */
    public Boolean getBase() {
        return base;
    }

    /**
     * @param base the base to set
     */
    public void setBase(Boolean base) {
        this.base = base;
    }

    /**
     * @return the centroCosto
     */
    public Boolean getCentroCosto() {
        return centroCosto;
    }

    /**
     * @param centroCosto the centroCosto to set
     */
    public void setCentroCosto(Boolean centroCosto) {
        this.centroCosto = centroCosto;
    }

    /**
     * @return the tieneAuxiliares
     */
    public Boolean getTieneAuxiliares() {
        return tieneAuxiliares;
    }

    /**
     * @param tieneAuxiliares the tieneAuxiliares to set
     */
    public void setTieneAuxiliares(Boolean tieneAuxiliares) {
        this.tieneAuxiliares = tieneAuxiliares;
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
    
    public String getNombreCompleto() {
        StringBuilder sb = new StringBuilder();
        sb.append(nombre);
        sb.append(" | ");
        sb.append(descripcion);
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Cuenta other = (Cuenta) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.version);
        hash = 53 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public String toString() {
        return "Cuenta{" + "nombre=" + nombre + '}';
    }
}
