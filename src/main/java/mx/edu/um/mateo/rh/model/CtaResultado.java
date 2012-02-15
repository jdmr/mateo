/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author semdariobarbaamaya
 */


@Entity
@Table(name = "ctaresultados")

public class CtaResultado implements Serializable{
    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne(optional = false)
    private Ejercicio ejercicio;
   @NotBlank
    @Column(nullable = false, length = 24)
    private String tipoCuenta;
   @NotBlank
    @Column(nullable = false, length = 24)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombreFiscal;
   @NotBlank
    @Column(nullable = false)
    private Boolean detalle;
    @NotBlank
    @Column(nullable = false)
    private Boolean aviso;
    @NotBlank
    @Column(nullable = false)
    private Boolean auxiliar;
    @NotBlank
    @Column(nullable = false)
    private Boolean iva;
    @NotBlank
    @Column(nullable = false)
    private Boolean detalleR;
    @NotBlank
    @Column(nullable = false, length = 24)
    private Double pctIva;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String idCtaResultado;
    
    
    public CtaResultado() {
    }
    
    
     public CtaResultado(Ejercicio ejercicio,String tipoCuenta, String nombre, String idCtaResultado) {
         
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

    public Ejercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(Ejercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getIdCtaResultado() {
        return idCtaResultado;
    }

    public void setIdCtaResultado(String idCtaResultado) {
        this.idCtaResultado = idCtaResultado;
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

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CtaResultado other = (CtaResultado) obj;
        return true;
    }

    public int hashCode() {
        int hash = 3;
        return hash;
    }

    public String toString() {
        return "CtaResultado{" + "ejercicio=" + ejercicio + ", nombre=" + nombre + '}';
    }
    
}
