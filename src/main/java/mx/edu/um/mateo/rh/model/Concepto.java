/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author AMDA
 */
@Entity
@Table(name = "cont_concepto")
public class Concepto implements Comparable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false, length = 60, unique = true)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 120)
    private String descripcion;
    @NotBlank
    @Column(nullable = false, length = 100)
    private String tags;
    @Column(nullable = false, length = 2)
    private String status;
    @Version
    private Integer version;

    /**
     *
     */
    public Concepto() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Long getId() {
        return this.id;
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

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTags() {
        return this.tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return this.version;
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
        final Concepto other = (Concepto) obj;
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
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.nombre);
        hash = 53 * hash + Objects.hashCode(this.version);
        return hash;
    }

    @Override
    public String toString() {
        return "Concepto{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", tags=" + tags + ", status=" + status + ", version=" + version + '}';
    }
     
    public int compareTo(Object o) {
        if (o instanceof Concepto) {
            Concepto c = (Concepto) o;
            return this.getId().compareTo(c.getId());
        }
        return -1;
    }
}