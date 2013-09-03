/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author zorch
 */
@Entity
@Table(name = "cat_modalidad")
public class Modalidad implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "nombre_modalidad")
    private String nombre;
    @Column(length = 1)
    private String enLinea;

    public Modalidad() {
    }

    public Modalidad(String nombre, String enLinea) {
        this.nombre = nombre;
        this.enLinea = enLinea;
    }

    public Modalidad(Integer id, String nombre, String enLinea) {
        this.nombre = nombre;
        this.id = id;
        this.enLinea = enLinea;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEnLinea() {
        return enLinea;
    }

    public void setEnLinea(String enLinea) {
        this.enLinea = enLinea;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.nombre);
        hash = 17 * hash + Objects.hashCode(this.enLinea);
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
        final Modalidad other = (Modalidad) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.enLinea, other.enLinea)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Modalidad{" + "id=" + id + ", nombre=" + nombre + ", enLinea=" + enLinea + '}';
    }
}
