/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;


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
    private Integer version;
    private Map <Integer,NivelEstudios> niveles;

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + Objects.hashCode(this.id);
        hash = 29 * hash + Objects.hashCode(this.nombre);
        hash = 29 * hash + Objects.hashCode(this.version);
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
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NivelEstudios{" + "id=" + id + ", nombre=" + nombre + ", version=" + version + '}';
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
    
}
