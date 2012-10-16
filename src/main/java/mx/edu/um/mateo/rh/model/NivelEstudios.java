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
public class NivelEstudios implements Comparable{
    Logger log = LoggerFactory.getLogger(getClass());
    private Integer id;
    private String nombre;
    private Map <Integer,NivelEstudios> niveles;
    
    public static final Integer PRIMARIA = 1;
    public static final Integer SECUNDARIA = 2;
    public static final Integer PREPARATORIA = 3;
    public static final Integer LICENCIATURA = 4;
    public static final Integer MAESTRIA = 5;
    public static final Integer DOCTORADO = 6;

    public NivelEstudios() {
    }

    public NivelEstudios(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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



    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.nombre);
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
        final NivelEstudios other = (NivelEstudios) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NivelEstudios{" + "id=" + id + ", nombre=" + nombre + '}';
    }

    /**
     * @return the niveles
     */
    public Map <Integer,NivelEstudios> getNiveles() {
        return niveles;
    }

    /**
     * @param niveles the niveles to set
     */
    public void setNiveles(Map <Integer,NivelEstudios> niveles) {
        log.debug("asignando nivel{}", niveles.size());
        this.niveles = niveles;
    }
    
    public int compareTo(Object o) {
        if (o instanceof NivelEstudios) {
            NivelEstudios c = (NivelEstudios) o;
            return this.getId().compareTo(c.getId());
        }
        return -1;
    }
    
    /**
     * @param nivel obtiene el nivel 
     * @return 
     */
    public NivelEstudios obtener(Integer nivel){
        return niveles.get(nivel);
    }
    /**
     * @return regresa la lista
     */
    public List lista(){
        return (List)niveles.values();
    }
    
}
