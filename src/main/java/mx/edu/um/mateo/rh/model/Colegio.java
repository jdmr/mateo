/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import javax.persistence.*;

/**
 *
 * @author develop
 */
@Entity
@Table(name = "colegio")
public class Colegio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column
    private String nombre;
    @Column
    private String status;

    public Colegio() {
    }

    
    
    public Colegio(Long id, Integer version, String nombre, String status) {
        this.id = id;
        this.version = version;
        this.nombre = nombre;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Colegio{" + "id=" + id + ", version=" + version + '}';
    }
    
    
    
    
}
