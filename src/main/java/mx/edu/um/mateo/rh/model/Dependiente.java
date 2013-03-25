/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author zorch
 */
@Entity
@Table(name = "dependiente")
public class Dependiente implements Serializable {

   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false, length=2)
    private String status;
    @Version
    private Integer version;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private TipoDependiente tipoDependiente;
    @NotBlank
    @Column(nullable= false, length=200)
    private String nombre;
    @Column
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaNac;
     @ManyToOne(optional=false)
    private Empleado empleado;

    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
     
     @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.version);
        hash = 23 * hash + Objects.hashCode(this.nombre);
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
        final Dependiente other = (Dependiente) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }
     
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
                ", fechaNac=" + fechaNac +", status=" + status + ", empleado="+empleado.getId()+'}';
    }

   
 
    
    
}
