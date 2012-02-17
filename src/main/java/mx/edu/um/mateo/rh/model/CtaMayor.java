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
//    private Ejercicio ejercicio;
//    private String idCtaMayor;
//    private String tipoCuenta;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombreFiscal;
//    private Boolean detalle;
//    private Boolean aviso;
//    private Boolean auxiliar;
//    private Boolean iva;
//    private Double pctIva;
//    private Boolean detalleR;

    public CtaMayor() {
    }

    public CtaMayor(String nombre, String nombreFiscal) {
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
        return "CtaMayor{nombre=" + nombre + ", nombreFiscal=" + nombreFiscal + '}';
    }
}
