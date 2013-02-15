/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.inscripciones.Alumno;

/**
 *
 * @author zorch
 */
public interface AlumnoDao {
        
    public Map<String, Object> lista(Map<String, Object> params);
        
    public Alumno obtiene(String matricula);

    

}
