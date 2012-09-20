/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author develop
 */
@Entity
@Table(name = "dependiente")
public class Dependiente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column
    private TipoDependiente tipoDependiente;
    @Column
    private String nombre;
    @Column
    private String apPaterno;
    @Column
    private String apMaterno;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaNac;

    public Dependiente() {
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoDependiente getTipoDependiente() {
        return tipoDependiente;
    }

    public void setTipoDependiente(TipoDependiente tipoDependiente) {
        this.tipoDependiente = tipoDependiente;
    }

  

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getApellidoM() {
        return apMaterno;
    }

    public void setApellidoM(String apellidoM) {
        this.apMaterno = apellidoM;
    }

    public String getApellidoP() {
        return apPaterno;
    }

    public void setApellidoP(String apellidoP) {
        this.apPaterno = apellidoP;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Dependiente{" + "id=" + id + ", tipoDependiente=" + tipoDependiente + ", nombre=" + nombre +
                ", apellidoP=" + apPaterno + ", apellidoM=" + apMaterno + ", fechaNac=" + fechaNac + '}';
    }
 
    
    
}
