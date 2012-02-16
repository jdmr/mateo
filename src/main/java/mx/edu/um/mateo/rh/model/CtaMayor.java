/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author nujev
 */
@Entity
@Table(name = "ctamayor")
public class CtaMayor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @NotBlank
    @Column(nullable = false)
    private Ejercicio ejercicio;
    @Column(nullable = true)
    private String idCtaMayor;
    @Column(nullable = true)
    private String tipoCuenta;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombreFiscal;
    @Column(nullable = true)
    private Boolean detalle;
    @Column(nullable = true)
    private Boolean aviso;
    @Column(nullable = true)
    private Boolean auxiliar;
    @Column(nullable = true)
    private Boolean iva;
    @Column(nullable = true)
    private Double pctIva;
    @Column(nullable = true)
    private Boolean detalleR;

    public CtaMayor() {
    }

    public CtaMayor(Ejercicio ejercicio, String nombre, String nombreFiscal) {
        this.ejercicio = ejercicio;
        this.nombre = nombre;
        this.nombreFiscal = nombreFiscal;
    }

    public Boolean getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(Boolean auxiliar) {
        this.auxiliar = auxiliar;
    }

    public Boolean getAviso() {
        return aviso;
    }

    public void setAviso(Boolean aviso) {
        this.aviso = aviso;
    }

    public Boolean getDetalle() {
        return detalle;
    }

    public void setDetalle(Boolean detalle) {
        this.detalle = detalle;
    }

    public Boolean getDetalleR() {
        return detalleR;
    }

    public void setDetalleR(Boolean detalleR) {
        this.detalleR = detalleR;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdCtaMayor() {
        return idCtaMayor;
    }

    public void setIdCtaMayor(String idCtaMayor) {
        this.idCtaMayor = idCtaMayor;
    }

    public Boolean getIva() {
        return iva;
    }

    public void setIva(Boolean iva) {
        this.iva = iva;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreFiscal() {
        return nombreFiscal;
    }

    public void setNombreFiscal(String nombreFiscal) {
        this.nombreFiscal = nombreFiscal;
    }

    public Double getPctIva() {
        return pctIva;
    }

    public void setPctIva(Double pctIva) {
        this.pctIva = pctIva;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public String toString() {
        return "CtaMayor{" + "ejercicio=" + ejercicio + ", tipoCuenta=" + tipoCuenta + ", nombre=" + nombre + ", nombreFiscal=" + nombreFiscal + '}';
    }
}
