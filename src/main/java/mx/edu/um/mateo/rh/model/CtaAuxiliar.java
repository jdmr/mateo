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
 * @author semdariobarbaamaya
 */

@Entity
@Table(name = "ctaauxiliar")

public class CtaAuxiliar implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombreFiscal;

    public CtaAuxiliar() {
        
    }

    public CtaAuxiliar(String nombre, String nombreFiscal) {
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

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CtaAuxiliar other = (CtaAuxiliar) obj;
        return true;
    }

    public int hashCode() {
        int hash = id.intValue();
        return hash;
    }

    public String toString() {
        return "CtaAuxiliar{id=" + id + ", version=" + version + ", nombre=" + nombre + ", nombreFiscal=" + nombreFiscal + '}';
    }
    
}
