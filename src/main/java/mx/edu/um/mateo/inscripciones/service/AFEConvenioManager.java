/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.AFEConvenio;
import mx.edu.um.mateo.inscripciones.model.Alumno;
import mx.edu.um.mateo.inscripciones.utils.MatriculaInvalidaException;

/**
 *
 * @author zorch
 */
public interface AFEConvenioManager {
    /**
     * 
     * @param params
     * @return regresa la lista de todos los Convenios
     */ 
    
    public Map<String, Object>  lista(Map<String, Object> params);

    /**
     * Obtiene la informacion del Convenio basandose en el id
     * @param id el id del Convenio
     
     */
    public AFEConvenio obtiene(final Long id);

    /**
     * Graba de un Convenio(ya sea crear o actualizar)
     * @param AFEConvenio  el objeto que sera grabado
     */
    public void graba(AFEConvenio afeConvenio, Usuario usuario)throws MatriculaInvalidaException;

    /**
     * Cambia el status del Convenio a I
     * @param id el id del Convenio 
     */
    public String elimina(final Long id);
    

}
