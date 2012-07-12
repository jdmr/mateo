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
import javax.persistence.ManyToOne;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Embeddable
public class CtaMayorPK implements Serializable {

    @ManyToOne(optional = false)
    private Ejercicio ejercicio;
    @Column(name = "id_ctamayor", length = 20, nullable = false)
    private String idCtaMayor;
    @Column(name = "tipo_cuenta", length = 1, nullable = false)
    private String tipoCuenta;

    public CtaMayorPK() {
    }

    public CtaMayorPK(Ejercicio ejercicio, String idCtaMayor, String tipoCuenta) {
        this.ejercicio = ejercicio;
        this.idCtaMayor = idCtaMayor;
        this.tipoCuenta = tipoCuenta;
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
     * @return the idCtaMayor
     */
    public String getIdCtaMayor() {
        return idCtaMayor;
    }

    /**
     * @param idCtaMayor the idCtaMayor to set
     */
    public void setIdCtaMayor(String idCtaMayor) {
        this.idCtaMayor = idCtaMayor;
    }

    /**
     * @return the tipoCuenta
     */
    public String getTipoCuenta() {
        return tipoCuenta;
    }

    /**
     * @param tipoCuenta the tipoCuenta to set
     */
    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.ejercicio);
        hash = 13 * hash + Objects.hashCode(this.idCtaMayor);
        hash = 13 * hash + Objects.hashCode(this.tipoCuenta);
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
        final CtaMayorPK other = (CtaMayorPK) obj;
        if (!Objects.equals(this.ejercicio, other.ejercicio)) {
            return false;
        }
        if (!Objects.equals(this.idCtaMayor, other.idCtaMayor)) {
            return false;
        }
        if (!Objects.equals(this.tipoCuenta, other.tipoCuenta)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CtaMayorPK{" + "ejercicio=" + ejercicio + ", idCtaMayor=" + idCtaMayor + ", tipoCuenta=" + tipoCuenta + '}';
    }
}
