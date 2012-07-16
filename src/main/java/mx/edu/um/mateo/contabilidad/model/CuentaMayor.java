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
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Entity
@Table(name = "cont_ctamayor")
public class CtaMayor implements Serializable {

    @Id
    private CtaMayorPK id;
    @Version
    private Integer version;
    @NotBlank
    @Column(length = 60, nullable = false)
    private String nombre;
    @NotBlank
    @Column(name = "nombrefiscal", length = 60, nullable = false)
    private String nombreFiscal;
    @Column(length = 2, nullable = false)
    private Boolean detalle;
    @NotBlank
    @Column(length = 1, nullable = false)
    private String aviso;
    @NotBlank
    @Column(length = 1, nullable = false)
    private String auxiliar;
    @NotBlank
    @Column(length = 2, nullable = false)
    private Boolean iva;
    @NotBlank
    @Column(name = "pctiva", nullable = false)
    private Long pctIVA;
    @Column(length = 2, nullable = false)
    private Boolean detaller;

    public CtaMayor() {
    }

    /**
     * @return the id
     */
    public CtaMayorPK getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(CtaMayorPK id) {
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
     * @return the nombreFiscal
     */
    public String getNombreFiscal() {
        return nombreFiscal;
    }

    /**
     * @param nombreFiscal the nombreFiscal to set
     */
    public void setNombreFiscal(String nombreFiscal) {
        this.nombreFiscal = nombreFiscal;
    }

    /**
     * @return the detalle
     */
    public Boolean getDetalle() {
        return detalle;
    }

    /**
     * @param detalle the detalle to set
     */
    public void setDetalle(Boolean detalle) {
        this.detalle = detalle;
    }

    /**
     * @return the aviso
     */
    public String getAviso() {
        return aviso;
    }

    /**
     * @param aviso the aviso to set
     */
    public void setAviso(String aviso) {
        this.aviso = aviso;
    }

    /**
     * @return the auxiliar
     */
    public String getAuxiliar() {
        return auxiliar;
    }

    /**
     * @param auxiliar the auxiliar to set
     */
    public void setAuxiliar(String auxiliar) {
        this.auxiliar = auxiliar;
    }

    /**
     * @return the iva
     */
    public Boolean getIva() {
        return iva;
    }

    /**
     * @param iva the iva to set
     */
    public void setIva(Boolean iva) {
        this.iva = iva;
    }

    /**
     * @return the pctIVA
     */
    public Long getPctIVA() {
        return pctIVA;
    }

    /**
     * @param pctIVA the pctIVA to set
     */
    public void setPctIVA(Long pctIVA) {
        this.pctIVA = pctIVA;
    }

    /**
     * @return the detaller
     */
    public Boolean getDetaller() {
        return detaller;
    }

    /**
     * @param detaller the detaller to set
     */
    public void setDetaller(Boolean detaller) {
        this.detaller = detaller;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
        hash = 73 * hash + Objects.hashCode(this.version);
        hash = 73 * hash + Objects.hashCode(this.nombre);
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
        final CtaMayor other = (CtaMayor) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CtaMayor{" + "id=" + id + ", version=" + version + ", nombre=" + nombre + ", nombreFiscal=" + nombreFiscal + ", detalle=" + detalle + ", aviso=" + aviso + ", auxiliar=" + auxiliar + ", iva=" + iva + ", pctIVA=" + pctIVA + ", detaller=" + detaller + '}';
    }
}
