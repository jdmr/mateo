/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.model.EmpleadoPuesto;

/**
 *
 * @author semdariobarbaamaya
 */
public interface EmpleadoPuestoManager {
     /**
     * Regresa todos los empleadopuestos
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene los empleadopuestos basandose en su id
     * @param id el id de empleadopuesto
     * @return regresa el empleadopuesto que obtuvo mediante el id
     */
    public EmpleadoPuesto obtiene(final Long id)throws ObjectRetrievalFailureException;

    /**
     * Graba la informacion del empleadopuesto
     * @param empleadoPuesto el objeto que sera grabado
     */
    public void graba(EmpleadoPuesto empleadoPuesto, Usuario usuario);

    /**
     * Elimina empleadopuesto de la base de datos mediante su id
     * @param id el id del empleadopuesto
     */
    public String elimina(final Long id);
}
