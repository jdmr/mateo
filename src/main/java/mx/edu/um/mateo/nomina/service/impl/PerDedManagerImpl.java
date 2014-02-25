/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.nomina.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.nomina.dao.PerDedDao;
import mx.edu.um.mateo.nomina.model.PerDed;
import mx.edu.um.mateo.nomina.service.PerDedManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author semdariobarbaamaya
 */
@Transactional
@Service
public class PerDedManagerImpl extends BaseDao implements PerDedManager{
     @Autowired
    private PerDedDao dao;

    /**
     *  @see mx.edu.um.mateo.rh.service.PerDedManager#lista(java.util.Map) 
     */
    
    @Override
     public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     *  @see mx.edu.um.mateo.rh.service.PerDedManager#obtiene(java.lang.String) 
     */
    
    @Override
    public PerDed obtiene(final String id) {
        return dao.obtiene(new Long(id));
    }

    /**
     * @see mx.edu.um.mateo.rh.service.PerDedaManager#graba(mx.edu.um.mateo.rh.model.PerDed, mx.edu.um.mateo.general.model.Usuario) 
     */
    @Override
    public void graba(PerDed perded, Usuario usuario) {
        if(perded.getId()==null){
            perded.setStatus(Constantes.STATUS_ACTIVO);
        }
        dao.graba(perded, usuario);
    }

    /**
     *  @see mx.edu.um.mateo.rh.service.PerDedManager#elimina(java.lang.String) 
     */
    @Override
    public String elimina(final Long id) {
        PerDed perded = dao.obtiene(new Long(id));
        perded.setStatus(Constantes.STATUS_INACTIVO);
         return perded.getNombre();
    }
}
