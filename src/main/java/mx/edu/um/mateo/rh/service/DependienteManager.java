/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.rh.model.Dependiente;



/**
 *
 * @author AMDA
 */
public interface DependienteManager {
    
    public Map<String, Object> lista(Map<String, Object> params);
    
    
    public Dependiente obtiene(final String id);
    
    
    public void graba(Dependiente dependiente);
    
    
    public String elimina(final String id);
}
