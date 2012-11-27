/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Organizacion;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;



/**
 *
 * @author osoto
 */
@Entity
//@Table(name="tipo_empleado", uniqueConstraints = {
//    @UniqueConstraint(columnNames = {"prefijo"})}))
public class TipoEmpleado {
    private static final long serialVersionUID = 6001011125338853446L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne
    private Organizacion organizacion;
    @NotBlank
    @Length(min=3, max=3)
    @Column(nullable=false,unique=true)
    private String prefijo;
    @NotBlank
    @Length(max=128)
    @Column(nullable=false)
    private String descripcion;

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

    /**
     * @return the prefijo
     */
    public String getPrefijo() {
        return prefijo;
    }

    /**
     * @param prefijo the prefijo to set
     */
    public void setPrefijo(String prefijo) {
        this.prefijo = prefijo;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.version);
        hash = 97 * hash + Objects.hashCode(this.prefijo);
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
        final TipoEmpleado other = (TipoEmpleado) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.prefijo, other.prefijo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoEmpleado{" + "id=" + id + ", version=" + version + ", organizacion=" + organizacion + ", prefijo=" + prefijo + ", descripcion=" + descripcion + '}';
    }
    
    
    
}
