/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.dao.ConceptoDao;
import mx.edu.um.mateo.rh.model.Concepto;
import mx.edu.um.mateo.rh.service.ConceptoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author AMDA
 */

@Service
@Transactional
public class ConceptoManagerImpl implements ConceptoManager {
    
    @Autowired
    private ConceptoDao dao;

    /**
     * @see mx.edu.um.mateo.rh.service.ConceptoManager#lista(java.util.Map) 
     */
    @Override
   public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.ConceptoManager#obtiene(java.lang.Long) 
     */
    @Override
    public Concepto obtiene(final Long id) throws ObjectRetrievalFailureException {
        return dao.obtiene(id);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.ConceptoManager#graba(mx.edu.um.mateo.rh.model.Concepto, mx.edu.um.mateo.general.model.Usuario) 
     */
    @Override
    public void graba(Concepto concepto, Usuario usuario) {
        if (concepto.getId()== null){
            concepto.setStatus(Constantes.STATUS_ACTIVO);
        }
        dao.graba(concepto, usuario);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.ConceptoManager#elimina(java.lang.Long) 
     */
    @Override
   public String elimina(final Long id) throws ObjectRetrievalFailureException {
        return dao.elimina(id);
    }
    
}
