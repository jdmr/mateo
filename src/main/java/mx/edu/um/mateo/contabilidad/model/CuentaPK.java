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
import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Embeddable
public class CuentaPK implements Serializable {

    @ManyToOne(optional = false)
    private Ejercicio ejercicio;
    @ManyToOne(optional = false)
    private CtaMayor ctaMayor;
    @ManyToOne(optional = false)
    private CentroCosto ccosto;
    @ManyToOne(optional = false)
    private Auxiliar auxiliar;

    public CuentaPK() {
    }

    public CuentaPK(Ejercicio ejercicio, CtaMayor ctaMayor, CentroCosto ccosto, Auxiliar auxiliar) {
        this.ejercicio = ejercicio;
        this.ctaMayor = ctaMayor;
        this.ccosto = ccosto;
        this.auxiliar = auxiliar;
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
     * @return the ctaMayor
     */
    public CtaMayor getCtaMayor() {
        return ctaMayor;
    }

    /**
     * @param ctaMayor the ctaMayor to set
     */
    public void setCtaMayor(CtaMayor ctaMayor) {
        this.ctaMayor = ctaMayor;
    }

    /**
     * @return the ccosto
     */
    public CentroCosto getCcosto() {
        return ccosto;
    }

    /**
     * @param ccosto the ccosto to set
     */
    public void setCcosto(CentroCosto ccosto) {
        this.ccosto = ccosto;
    }

    /**
     * @return the auxiliar
     */
    public Auxiliar getAuxiliar() {
        return auxiliar;
    }

    /**
     * @param auxiliar the auxiliar to set
     */
    public void setAuxiliar(Auxiliar auxiliar) {
        this.auxiliar = auxiliar;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.ejercicio);
        hash = 41 * hash + Objects.hashCode(this.ctaMayor);
        hash = 41 * hash + Objects.hashCode(this.ccosto);
        hash = 41 * hash + Objects.hashCode(this.auxiliar);
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
        final CuentaPK other = (CuentaPK) obj;
        if (!Objects.equals(this.ejercicio, other.ejercicio)) {
            return false;
        }
        if (!Objects.equals(this.ctaMayor, other.ctaMayor)) {
            return false;
        }
        if (!Objects.equals(this.ccosto, other.ccosto)) {
            return false;
        }
        if (!Objects.equals(this.auxiliar, other.auxiliar)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CuentaPK{" + "ejercicio=" + ejercicio + ", ctaMayor=" + ctaMayor + ", ccosto=" + ccosto + ", auxiliar=" + auxiliar + '}';
    }
}
