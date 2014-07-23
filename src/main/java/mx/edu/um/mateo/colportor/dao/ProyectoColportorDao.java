 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;
import java.util.*;
import mx.edu.um.mateo.colportor.model.ProyectoColportor;
import mx.edu.um.mateo.colportor.model.ProyectoColportor;
import mx.edu.um.mateo.colportor.utils.UltimoException;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author osoto
 */
public interface ProyectoColportorDao {
    /**
     * Regresa una lista de proyectos de acuerdo a los parametros enviados en params
     * @param params
     * @return 
     */
    public Map<String, Object> lista(Map<String, Object> params);
    /**
     * Regresa un proyecto en base al id
     * @param id
     * @return 
     */
    public ProyectoColportor obtiene(Long id);
    /**
     * Regresa un proyecto en base al id y usuario
     * @param clp
     * @param id
     * @return 
     */
    public ProyectoColportor obtiene(Usuario user, Long id);
    /**
     * Registra un proyecto en la base de datos
     * @param ProyectoColportor
     * @return 
     */
    public ProyectoColportor crea(ProyectoColportor proyectoColportor);
    
    /**
     * Elimina un proyecto
     * @param colportor
     * @param id
     * @return
     * @throws UltimoException 
     */
    public String elimina(Usuario user, Long id);
    
    public ProyectoColportor obtiene(String codigo);
    
    
}