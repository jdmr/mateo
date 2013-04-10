/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.DescuentoDao;
import mx.edu.um.mateo.inscripciones.model.Descuento;
import mx.edu.um.mateo.inscripciones.service.DescuentoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@Transactional
@Service
public class DescuentoManagerImpl extends BaseDao implements DescuentoManager{
    @Autowired
    private DescuentoDao descuentoDao;
      /**
     *  @see mx.edu.um.mateo.rh.service.DescuentoManager#lista(java.util.Map) 
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return descuentoDao.lista(params);
    }
    
    /**
     *  @see mx.edu.um.mateo.rh.service.DescuentoManager#obtiene(java.lang.String) 
        */
    
    @Override
    public Descuento obtiene(final Long id) {
       return descuentoDao.obtiene(new Long(id));
    }

    /**
     * @see mx.edu.um.mateo.rh.service.DescuentoManager#graba(mx.edu.um.mateo.rh.model.Categoria, mx.edu.um.mateo.general.model.Usuario) 
     */
    
    @Override
    public void graba(Descuento descuento, Organizacion organizacion){
    if(descuento.getId()==null){
            descuento.setStatus(Constantes.STATUS_ACTIVO);
        }
        descuentoDao.graba(descuento, organizacion);
    }
    

    /**
     *  @see mx.edu.um.mateo.rh.service.DescuentoManager#elimina(java.lang.String) 
     */
    
    @Override
    public String elimina(final Long id) {
         return descuentoDao.elimina(id);
       
    }
}
