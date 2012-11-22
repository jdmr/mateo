/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.EmpleadoPuesto;

/**
 *
 * @author semdariobarbaamaya
 */
public interface EmpleadoPuestoDao {

     /**
     * Regresa una lista de empleadopuesto.
     * @param empleadopuesto
     * @return 
     */
    public Map<String, Object> lista (Map<String, Object> params);

    /**
     * Obtiene una empleadopuesto
     * @param id
     * @return 
     */
    public EmpleadoPuesto obtiene(final Long id);

    /**
     * Guarda la informacion de empleadopuesto
     * @param empleadopuesto el objeto que sera guardado
     */    
    public void graba(EmpleadoPuesto empleadopuesto, Usuario usuario);

    /**
     * Borra la categoria de la base de datos mediante el id
     * @param id id de empleadopuesto
     */
    public String elimina(final Long id);
}
