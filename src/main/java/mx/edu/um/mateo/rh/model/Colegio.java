/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.util.Objects;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @hibernate.class table="colegios" schema="aron"
 * @struts.form include-all="true" extends="BaseForm"
 * @author osoto
 */
@Entity
@Table (name = "Colegio")
public class Colegio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Column (nullable = false, length = 24)
    private String nombre;
    @Column (nullable = false, length = 2)
    private String status;
    @Version
    private Integer version;

    public Colegio() {
    }

    public Colegio(String nombre, String status, Integer version) {
        this.nombre = nombre;
        this.status = status;
        this.version = version;
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the nombre
     */

    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the status
     */

    public String getStatus() {
        return status;
    }


    /**
     * @param status the status to set
     */

    public void setStatus(String status) {
        this.status = status;
    }


    /**
     * @return the version
     */

    public Integer getVersion() {
        return version;
    }


    @Override
    public String toString() {
        return "Colegio{" + "id=" + id + ", nombre=" + nombre + ", status=" + status + ", version=" + version + '}';
    }

    /**
     * @param version the version to set
     */

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
        final Colegio other = (Colegio) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 29 * hash + Objects.hashCode(this.nombre);
        hash = 29 * hash + Objects.hashCode(this.status);
        hash = 29 * hash + Objects.hashCode(this.version);
        return hash;
    }

    
}
