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
public class Grupo {
    Logger log = LoggerFactory.getLogger(getClass());
    private Integer id;
    private String nombre;
    private Integer maximo;
    private Integer minimo;
    private Map <Integer,Grupo> grupos;
    
    public static final Integer GRUPO_A = 1;
    public static final Integer GRUPO_B = 2;
    public static final Integer GRUPO_C = 3;
    public static final Integer GRUPO_D = 4;
    public static final Integer GRUPO_E = 5;
    
    public Grupo(){
    }

    public Grupo(Integer id, String nombre, Integer maximo, Integer minimo) {
        this.id = id;
        this.nombre = nombre;
        this.maximo = maximo;
        this.minimo = minimo;
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

    public Integer getMaximo() {
        return maximo;
    }

    public void setMaximo(Integer maximo) {
        this.maximo = maximo;
    }

    public Integer getMinimo() {
        return minimo;
    }

    public void setMinimo(Integer minimo) {
        this.minimo = minimo;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.id);
        hash = 31 * hash + Objects.hashCode(this.nombre);
        hash = 31 * hash + Objects.hashCode(this.maximo);
        hash = 31 * hash + Objects.hashCode(this.minimo);
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
        final Grupo other = (Grupo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.maximo, other.maximo)) {
            return false;
        }
        if (!Objects.equals(this.minimo, other.minimo)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Grupo{" + "id=" + id + ", nombre=" + nombre + ", maximo=" + maximo + ", minimo=" + minimo + '}';
    }
    
     /**
     * @return the grupos
     */
    public Map <Integer,Grupo> getGrupos(){
        return grupos;
    }
    /**
     * @param grupos the grupos to set
     */
    public void setGrupos(Map <Integer,Grupo> grupos) {
        log.debug("asignando grupo{}", grupos.size());
        this.grupos = grupos;
    }
    
    public int compareTo(Object o) {
        if (o instanceof Grupo) {
            Grupo c = (Grupo) o;
            return this.getId().compareTo(c.getId());
        }
        return -1;
    }
     /**
     * @param grupo obtiene el grupo 
     * @return 
     */
    public Grupo obtener(Integer grupo){
        return grupos.get(grupo);
    }
    /**
     * @return regresa la lista
     */
    public List lista(){
        return (List)grupos.values();
    }
}
