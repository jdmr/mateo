/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.model.Concepto;


/**
 *
 * @author AMDA
 */
public interface ConceptoManager {
    
    /**
     * Regresa un lista de conceptos
     * @param params
     * @return  Map<String, Object>
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Regresa un concepto
     * @param id
     * @return 
     * @throws ObjectRetrievalFailureException 
     */
    public Concepto obtiene(final Long id) throws ObjectRetrievalFailureException;

    /**
     * Graba un concepto
     * @param concepto 
     */    
    public void graba(Concepto concepto, Usuario usuario);
    
    /**
     * Elimina el concepto
     * @param id
     * @throws ObjectRetrievalFailureException 
     */
    public String elimina(final Long id) throws ObjectRetrievalFailureException ;
    
}
