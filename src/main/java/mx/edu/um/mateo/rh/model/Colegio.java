/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import mx.edu.um.mateo.general.model.Organizacion;

/**
 * @hibernate.class table="rh_colegio" schema="aron"
 * @struts.form include-all="true" extends="BaseForm"
 * @author osoto
 */
@Entity
@Table (name = "colegio")
public class Colegio implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (nullable = false, length = 64)
    private String nombre;
    @Column (nullable = false, length = 2)
    private String status;
    @Version
    private Integer version;
    @ManyToOne
    private Organizacion organizacion;

    public Colegio() {
    }

    public Colegio(String nombre, String status) {
        this.nombre = nombre;
        this.status = status;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
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
    
     /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the organizacion
     */
    public Organizacion getOrganizacion() {
        return organizacion;
    }

    /**
     * @param organizacion the organizacion to set
     */
    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.id);
        hash = 61 * hash + Objects.hashCode(this.nombre);
        hash = 61 * hash + Objects.hashCode(this.version);
        hash = 61 * hash + Objects.hashCode(this.organizacion.getId());
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
        final Colegio other = (Colegio) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.organizacion.getId(), other.organizacion.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Colegio{" + "id=" + id + ", nombre=" + nombre + ", status=" + status + ", version=" + version + ", organizacion=" + organizacion.getId().toString() + '}';
    }

   

    
    
}
