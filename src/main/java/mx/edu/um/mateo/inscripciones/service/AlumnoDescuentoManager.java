/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.model.AlumnoDescuento;

/**
 *
 * @author zorch
 */
public interface AlumnoDescuentoManager {
    
     /**
     * 
     * @param params
     * @return regresa la lista de todos los Alumno con Descuentos
     */ 
    
    public Map<String, Object>  lista(Map<String, Object> params);

    /**
     * Obtiene la informacion del AlumnoDescuento basandose en el id
     * @param id el id del AlumnoDescuento
     
     */
    public AlumnoDescuento obtiene(final Long id);

    /**
     * Graba de un AlumnoDescuento(ya sea crear o actualizar)
     * @param AlumnoDescuento  el objeto que sera grabado
     */
    public void graba(AlumnoDescuento alumnoDescuento, Usuario usuario);

    /**
     * Cambia el status del AlumnoDescuento a I
     * @param id el id del AlumnoDescuento 
     */
    public String elimina(final Long id);
    
}
