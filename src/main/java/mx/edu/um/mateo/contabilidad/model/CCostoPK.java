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
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Embeddable
public class CCostoPK implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3235516203373513172L;
    @ManyToOne(optional = false)
    @JoinColumns({
        @JoinColumn(name = "id_ejercicio"),
        @JoinColumn(name = "id_organizacion")})
    private Ejercicio ejercicio;
    @Column(name = "id_ccosto", length = 20, nullable = false)
    private String idCosto;

    public CCostoPK() {
    }

    public CCostoPK(Ejercicio ejercicio, String idCosto) {
        this.ejercicio = ejercicio;
        this.idCosto = idCosto;
    }

    /**
     * @return the ejercicio
     */
    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    /**
     * @param ejercicio the ejercicio to set
     */
    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    /**
     * @return the idCosto
     */
    public String getIdCosto() {
        return idCosto;
    }

    /**
     * @param idCosto the idCosto to set
     */
    public void setIdCosto(String idCosto) {
        this.idCosto = idCosto;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.ejercicio);
        hash = 97 * hash + Objects.hashCode(this.idCosto);
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
        final CCostoPK other = (CCostoPK) obj;
        if (!Objects.equals(this.ejercicio, other.ejercicio)) {
            return false;
        }
        if (!Objects.equals(this.idCosto, other.idCosto)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CCostoPK{" + "ejercicio=" + ejercicio + ", idCosto=" + idCosto
                + '}';
    }
}
