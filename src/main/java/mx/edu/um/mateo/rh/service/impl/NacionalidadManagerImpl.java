/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.NacionalidadDao;
import mx.edu.um.mateo.rh.model.Nacionalidad;
import mx.edu.um.mateo.rh.service.NacionalidadManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */

@Transactional
@Service
public class NacionalidadManagerImpl implements NacionalidadManager{
    @Autowired
    private NacionalidadDao dao;

     /**
     *@see mx.edu.um.mateo.rh.service.NacionalidadManager#lista(java.util.Map)  
     */
    
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.NacionalidadManager#obtiene(java.lang.Long) 
     */
    @Override
    public Nacionalidad obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    /**
     * @see mx.edu.um.mateo.rh.service.NacionalidadManager#graba(mx.edu.um.mateo.rh.model.Nacionalidad, mx.edu.um.mateo.general.model.Usuario) 
     */
    
    @Override
    public void graba(Nacionalidad nacionalidad, Usuario usuario) {
         if(nacionalidad.getId()==null){
            nacionalidad.setStatus(Constantes.STATUS_ACTIVO);
        }
        dao.graba(nacionalidad, usuario);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.NacionalidadManager#elimina(java.lang.Long) 
     */
    
    
    @Override
    public String elimina(final Long id) {
        Nacionalidad nacionalidad= dao.obtiene(id);
        nacionalidad.setStatus(Constantes.STATUS_INACTIVO);
        dao.graba(nacionalidad, null);
        return nacionalidad.getNombre();
    }

   
}


