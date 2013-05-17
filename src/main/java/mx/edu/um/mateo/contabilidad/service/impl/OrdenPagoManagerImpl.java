/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.service.impl;

import java.util.Date;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.dao.OrdenPagoDao;
import mx.edu.um.mateo.contabilidad.model.OrdenPago;
import mx.edu.um.mateo.contabilidad.service.OrdenPagoManager;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 *
 * @author osoto
 */
@Transactional
@Service
public class OrdenPagoManagerImpl extends BaseDao implements OrdenPagoManager{
    
    @Autowired
    private OrdenPagoDao dao;

    public OrdenPagoManagerImpl(){
    
    }
    
    /**
     * @see mx.edu.um.mateo.contabilidad.service.OrdenPagoManager#lista(java.util.Map) 
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }
    
    /**
     * @see mx.edu.um.mateo.contabilidad.service.OrdenPagoManager#obtiene(java.lang.Long) 
     */
    @Override
    public OrdenPago obtiene(Long id) {
       return dao.obtiene(new Long(id));
    }

    /**
     * @see mx.edu.um.mateo.contabilidad.service.OrdenPagoManager#graba(mx.edu.um.mateo.contabilidad.model.OrdenPago, mx.edu.um.mateo.general.model.Usuario) 
     */
    @Override
    public void graba(OrdenPago ordenPago, Usuario usuario){
        
       ordenPago.setStatus(Constantes.STATUS_ACTIVO);
       ordenPago.setStatusInterno(Constantes.STATUS_ACTIVO);
        
       ordenPago.setUserCaptura(usuario);
       ordenPago.setFechaCaptura(new Date());
       log.debug("OrdenPago >>> {}", ordenPago);
       dao.crea(ordenPago, usuario);
    }
    
    /**
     * @see mx.edu.um.mateo.contabilidad.service.OrdenPagoManager#actualiza(mx.edu.um.mateo.contabilidad.model.OrdenPago, mx.edu.um.mateo.general.model.Usuario) 
     */
    @Override
    public void actualiza(OrdenPago ordenPago, Usuario usuario){
        
       ordenPago.setUserCaptura(usuario);
       ordenPago.setFechaCaptura(new Date());
       log.debug("OrdenPago >>> {}", ordenPago);
       dao.actualiza(ordenPago, usuario);
    }

    /**
     * @see mx.edu.um.mateo.contabilidad.service.OrdenPagoManager#elimina(java.lang.Long) 
     */
    @Override
    public String elimina( Long id) {
         return dao.elimina(id);       
    }
}
