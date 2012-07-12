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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Entity
@Table(name = "cont_ejercicio")
public class Ejercicio implements Serializable {

    @Id
    private EjercicioPK id;
    @Version
    private Integer version;
    @Column(length = 60, nullable = false)
    private String nombre;
    @Column(length = 1, nullable = false)
    private String status;
    @Column(length = 15, nullable = false, name = "masc_balance")
    private String mascBalance;
    @Column(length = 15, nullable = false, name = "masc_resultado")
    private String mascResultado;
    @Column(length = 15, nullable = false, name = "masc_auxiliar")
    private String mascAuxiliar;
    @Column(length = 15, nullable = false, name = "masc_ccosto")
    private String mascCcosto;
    @Column(nullable = false, name = "nivel_contable")
    private Byte nivelContable;
    @Column(nullable = false, name = "nivel_tauxiliar")
    private Byte nivelTauxiliar;

    public Ejercicio() {
    }

    /**
     * @return the id
     */
    public EjercicioPK getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(EjercicioPK id) {
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
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the mascBalance
     */
    public String getMascBalance() {
        return mascBalance;
    }

    /**
     * @param mascBalance the mascBalance to set
     */
    public void setMascBalance(String mascBalance) {
        this.mascBalance = mascBalance;
    }

    /**
     * @return the mascResultado
     */
    public String getMascResultado() {
        return mascResultado;
    }

    /**
     * @param mascResultado the mascResultado to set
     */
    public void setMascResultado(String mascResultado) {
        this.mascResultado = mascResultado;
    }

    /**
     * @return the mascAuxiliar
     */
    public String getMascAuxiliar() {
        return mascAuxiliar;
    }

    /**
     * @param mascAuxiliar the mascAuxiliar to set
     */
    public void setMascAuxiliar(String mascAuxiliar) {
        this.mascAuxiliar = mascAuxiliar;
    }

    /**
     * @return the mascCcosto
     */
    public String getMascCcosto() {
        return mascCcosto;
    }

    /**
     * @param mascCcosto the mascCcosto to set
     */
    public void setMascCcosto(String mascCcosto) {
        this.mascCcosto = mascCcosto;
    }

    /**
     * @return the nivelContable
     */
    public Byte getNivelContable() {
        return nivelContable;
    }

    /**
     * @param nivelContable the nivelContable to set
     */
    public void setNivelContable(Byte nivelContable) {
        this.nivelContable = nivelContable;
    }

    /**
     * @return the nivelTauxiliar
     */
    public Byte getNivelTauxiliar() {
        return nivelTauxiliar;
    }

    /**
     * @param nivelTauxiliar the nivelTauxiliar to set
     */
    public void setNivelTauxiliar(Byte nivelTauxiliar) {
        this.nivelTauxiliar = nivelTauxiliar;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.version);
        hash = 23 * hash + Objects.hashCode(this.nombre);
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
        final Ejercicio other = (Ejercicio) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ejercicio{" + "id=" + id + ", version=" + version + ", nombre=" + nombre + ", status=" + status + ", mascBalance=" + mascBalance + ", mascResultado=" + mascResultado + ", mascAuxiliar=" + mascAuxiliar + ", mascCcosto=" + mascCcosto + ", nivelContable=" + nivelContable + ", nivelTauxiliar=" + nivelTauxiliar + '}';
    }
}
