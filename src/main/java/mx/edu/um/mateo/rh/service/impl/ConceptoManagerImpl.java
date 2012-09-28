/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.dao.ConceptoDao;
import mx.edu.um.mateo.rh.model.Concepto;
import mx.edu.um.mateo.rh.service.ConceptoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author AMDA
 */

@Component
@Transactional
public class ConceptoManagerImpl implements ConceptoManager {
    
    @Autowired
    private ConceptoDao dao;

    /**
     * @see mx.edu.um.service.ConceptoManager#getConceptos(mx.edu.um.nomina.model.Concepto)
     */
   public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * @see mx.edu.um.service.ConceptoManager#getConcepto(String id)
     */
    public Concepto obtiene(final Long id) throws ObjectRetrievalFailureException {
        return dao.obtiene(id);
    }

    /**
     * @see mx.edu.um.service.ConceptoManager#saveConcepto(Concepto concepto)
     */
    public void graba(Concepto concepto) {
        if (concepto.getId()== null){
            concepto.setStatus(Constantes.STATUS_ACTIVO);
        }
        dao.graba(concepto);
    }

    /**
     * @see mx.edu.um.service.ConceptoManager#removeConcepto(String id)
     */
   public String elimina(final Long id) throws ObjectRetrievalFailureException {
        return dao.elimina(id);
    }
    
}
