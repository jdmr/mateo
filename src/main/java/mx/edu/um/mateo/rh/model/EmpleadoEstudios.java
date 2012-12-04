/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author AMDA
 */

@Entity
@Table(name = "empleado_estudios")
public class EmpleadoEstudios implements Comparable, Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false, length = 75)
    private String nombreEstudios;    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false,name = "nivel_estudios")
    private NivelEstudios nivelEstudios; 
    //@NotBlank
    private Boolean titulado;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false,name="fecha_titulacion")
    private Date fechaTitulacion;
    @Length(max=2)
    @Column(nullable = false, length = 2)
    private String status;
    
    private Usuario userCaptura;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false,name="fecha_captura")
    private Date fechaCaptura;
    @Version
    private Integer version;
    
    @ManyToOne(optional=false)
    private Empleado empleado;
    
    public EmpleadoEstudios(){
    }

    public EmpleadoEstudios(Long id, String nombreEstudios, NivelEstudios nivelEstudios, Boolean titulado, Date fechaTitulacion, String status, Usuario userCaptura, Date fechaCaptura, Integer version, Empleado empleado) {
        this.id = id;
        this.nombreEstudios = nombreEstudios;
        this.nivelEstudios = nivelEstudios;
        this.titulado = titulado;
        this.fechaTitulacion = fechaTitulacion;
        this.status = status;
        this.userCaptura = userCaptura;
        this.fechaCaptura = fechaCaptura;
        this.version = version;
        this.empleado = empleado;
        
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public Date getFechaTitulacion() {
        return fechaTitulacion;
    }

    public void setFechaTitulacion(Date fechaTitulacion) {
        this.fechaTitulacion = fechaTitulacion;
    }

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreEstudios() {
        return nombreEstudios;
    }

    public void setNombreEstudios(String nombreEstudios) {
        this.nombreEstudios = nombreEstudios;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getTitulado() {
        return titulado;
    }

    public void setTitulado(Boolean titulado) {
        this.titulado = titulado;
    }

    public Usuario getUserCaptura() {
        return userCaptura;
    }

    public void setUserCaptura(Usuario userCaptura) {
        this.userCaptura = userCaptura;
    }

    public Integer getVersion() {
        return version;
    }

    public NivelEstudios getNivelEstudios() {
        return nivelEstudios;
    }

    public void setNivelEstudios(NivelEstudios nivelEstudios) {
        this.nivelEstudios = nivelEstudios;
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
        final EmpleadoEstudios other = (EmpleadoEstudios) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nombreEstudios, other.nombreEstudios)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.id);
        hash = 67 * hash + Objects.hashCode(this.nombreEstudios);
        hash = 67 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public String toString() {
        return "EmpleadoEstudios{" + "id=" + id +", Empleado" + ", nombreEstudios=" + nombreEstudios + ", titulado=" + titulado + ", fechaTitulacion=" + fechaTitulacion +", nivelEstudios=" + nivelEstudios + ", status=" + status + ", userCaptura=" + userCaptura + ", fechaCaptura=" + fechaCaptura  + ", version=" + version + '}';
    }
    
    @Override
    public int compareTo(Object o) {
        if (o instanceof EmpleadoEstudios) {
            EmpleadoEstudios d = (EmpleadoEstudios) o;
            return this.getId().compareTo(d.getId());
        }
        return -1;
    }
    
    
}
