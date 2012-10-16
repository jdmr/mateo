/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author AMDA
 */

@Entity
@Table(name = "cont_dependiente")
public class Dependiente implements Comparable, Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false, length = 60, unique = true)
    private String nombre;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false,name="BDAY")
    private Date fechaNacimiento;
//    @NotBlank
//    @Column(nullable = false, name="relacion_id")
//    private TipoDependiente tipoDependiente;
//    @NotBlank
//    @Column(nullable = false, name="colegio_id")
//    private Colegio colegio;
    @NotBlank
    @Column(nullable = false, length = 100, unique = true)
    private String estudios;
    @NotNull
    @Column
    private Integer grado;
    @Version
    private Integer version;
    @Length(max=2)
    @Column(nullable = false, length = 2)
    private String status;
    

    public String getEstudios() {
        return estudios;
    }

    public void setEstudios(String estudios) {
        this.estudios = estudios;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getGrado() {
        return grado;
    }

    public void setGrado(Integer grado) {
        this.grado = grado;
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
        final Dependiente other = (Dependiente) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
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
        hash = 67 * hash + Objects.hashCode(this.nombre);
        hash = 67 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public String toString() {
        return "Dependiente{" + "id=" + id + ", nombre=" + nombre + ", fechaNacimiento=" + fechaNacimiento + ", estudios=" + estudios + ", grado=" + grado +  ", status=" + status + ", version=" + version + '}';
    }
    
    public int compareTo(Object o) {
        if (o instanceof Dependiente) {
            Dependiente d = (Dependiente) o;
            return this.getId().compareTo(d.getId());
        }
        return -1;
    }
    
}
