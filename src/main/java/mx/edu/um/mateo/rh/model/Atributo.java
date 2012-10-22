/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author semdariobarbaamaya
 */
@Component
public class Atributo {
    Logger log = LoggerFactory.getLogger(getClass());
    private Integer id;
    private String nombre;
    private String descripcion;
    private String simbolo;
    private Map <Integer,Atributo> atributos;
    
    public static final Integer DIEZMO = 1;
    public static final Integer BASICO = 2;
    public static final Integer ISPT = 3;
    public static final Integer NOMIMA = 4;
    public static final Integer SOBRESUELDO = 5;
    public static final Integer PALABRARESERVADA = 6;
    public static final Integer REVISIONSOCIAL = 7;
    public static final Integer BASENOMINA = 8;
    public static final Integer PORCENTAJETOTAL = 9;

    public Atributo() {
    }

    public Atributo(Integer id, String nombre, String descripcion, String simbolo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.simbolo = simbolo;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.nombre);
        hash = 79 * hash + Objects.hashCode(this.descripcion);
        hash = 79 * hash + Objects.hashCode(this.simbolo);
        hash = 79 * hash + Objects.hashCode(this.atributos);
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
        final Atributo other = (Atributo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        if (!Objects.equals(this.simbolo, other.simbolo)) {
            return false;
        }
        if (!Objects.equals(this.atributos, other.atributos)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Atributo{" + "id=" + id + ", nombre=" + nombre + ", descripcion=" + descripcion + ", simbolo=" + simbolo + '}';
    }
    
    /**
     * @return the atributos
     */
    public Map <Integer,Atributo> getAtributos(){
        return atributos;
    }
    /**
     * @param atributos the atributos to set
     */
    public void setAtributos(Map <Integer,Atributo> atributos) {
        log.debug("asignando atributo{}", atributos.size());
        this.atributos = atributos;
    }
    
    public int compareTo(Object o) {
        if (o instanceof Atributo) {
            Atributo c = (Atributo) o;
            return this.getId().compareTo(c.getId());
        }
        return -1;
    }
     /**
     * @param atributo obtiene el atributo 
     * @return 
     */
    public Atributo obtener(Integer atributo){
        return atributos.get(atributo);
    }
    /**
     * @return regresa la lista
     */
    public List lista(){
        return (List)atributos.values();
    }
}
