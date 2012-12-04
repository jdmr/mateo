/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.Categoria;
import mx.edu.um.mateo.rh.model.EmpleadoEstudios;

/**
 *
 * @author AMDA
 */
public interface EmpleadoEstudiosManager {
    
     /**
     * Regresa todas las categorias
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene EmpleadoEstudios basandose en su id
     * @param id el id de EmpleadoEstudios
     * @return regresa EmpleadoEstudios que obtuvo mediante el id
     */
    public EmpleadoEstudios obtiene(final String id);

    /**
     * Graba la informacion de EmpleadoEstudios
     * @param EmpleadoEstudios el objeto que sera grabado
     */
    public void graba(EmpleadoEstudios empleadoEstudios);

    /**
     * Elimina EmpleadoEstudios de la base de datos mediante su id
     * @param id el id EmpleadoEstudios
     */
    public String elimina(final Long id);
    
}
