package mx.edu.um.mateo.contabilidad.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author develop
 */
@Entity
@Table(name = "libro")
public class Libro implements Serializable {

    @Id
    private LibroPK id;
    @Version
    private Integer version;
    @Column(length = 60, nullable = false)
    private String nombre;

    public Libro() {
    }

    /**
     * @return the id
     */
    public LibroPK getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(LibroPK id) {
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
    
}
