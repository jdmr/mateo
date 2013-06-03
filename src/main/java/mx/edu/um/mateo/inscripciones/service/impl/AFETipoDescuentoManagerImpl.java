/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.AFETipoDescuentoDao;
import mx.edu.um.mateo.inscripciones.model.AFETipoDescuento;
import mx.edu.um.mateo.inscripciones.service.AFETipoDescuentoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@Transactional
@Service
public class AFETipoDescuentoManagerImpl extends BaseDao implements AFETipoDescuentoManager{
    
    @Autowired
    private AFETipoDescuentoDao dao;



    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public AFETipoDescuento obtiene(final Long id) {
       return dao.obtiene(new Long(id));
    }

    @Override
    public void graba(AFETipoDescuento afeTipoDescuento, Organizacion organizacion){
     if(afeTipoDescuento.getId()==null){
            afeTipoDescuento.setStatus(Constantes.STATUS_ACTIVO);
        }
            dao.graba(afeTipoDescuento, organizacion);
    
           
       }
    

    @Override
    public String elimina( Long id) {
         return dao.elimina(id);
       
    }
}

