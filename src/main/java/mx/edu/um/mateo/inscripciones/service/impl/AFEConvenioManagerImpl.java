/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.inscripciones.dao.AFEConvenioDao;
import mx.edu.um.mateo.inscripciones.dao.AlumnoDao;
import mx.edu.um.mateo.inscripciones.model.AFEConvenio;
import mx.edu.um.mateo.inscripciones.service.AFEConvenioManager;
import mx.edu.um.mateo.inscripciones.utils.MatriculaInvalidaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@Transactional
@Service
public class AFEConvenioManagerImpl extends BaseDao implements AFEConvenioManager{
    
    @Autowired
    private AFEConvenioDao dao;

    @Autowired
    private AlumnoDao alDao;
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public AFEConvenio obtiene(Long id) {
       return dao.obtiene(new Long(id));
    }

    @Override
    public void graba(AFEConvenio afeConvenio)throws MatriculaInvalidaException{
       if(alDao.obtiene(afeConvenio.getMatricula())!= null){
           dao.graba(afeConvenio);
           
       }else{
           
           throw new MatriculaInvalidaException() ;
       }
        
        
        
    }
    

    @Override
    public String elimina( Long id) {
         return dao.elimina(id);
       
    }
}
