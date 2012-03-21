/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import mx.edu.um.mateo.general.model.Organizacion;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author semdariobarbaamaya
 */


@Entity
@Table(name = "cuentaresultado")

public class CuentaResultado implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
   // @ManyToOne(optional = false)
    //private Ejercicio ejercicio;
   //@NotBlank
    //@Column(nullable = false, length = 24)
    //private String tipoCuenta;
 
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombreFiscal;
    @ManyToOne
    private Organizacion oraganizacion;
    /**
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
    private String idCuentaResultado;
    */
    
    public CuentaResultado() {
    }
    
    /**
     public CuentaResultado(Ejercicio ejercicio, String tipoCuenta, String nombre, String idCuentaResultado) {
         this.ejercicio = ejercicio;
         this.tipoCuenta = tipoCuenta;
         this.nombre = nombre;
         this.idCuentaResultado = idCuentaResultado;
    }
     */
    
     public CuentaResultado(String nombre, String nombreFiscal) {
         this.nombre = nombre;
         this.nombreFiscal = nombreFiscal;
    }
     
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the oraganizacion
     */
    public Organizacion getOraganizacion() {
        return oraganizacion;
    }

    /**
     * @param oraganizacion the oraganizacion to set
     */
    public void setOraganizacion(Organizacion oraganizacion) {
        this.oraganizacion = oraganizacion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CuentaResultado other = (CuentaResultado) obj;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.version);
        hash = 79 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public String toString() {
        return "CuentaResultado{nombre=" + nombre + ", nombreFiscal=" + nombreFiscal + '}';
    }
    
}
