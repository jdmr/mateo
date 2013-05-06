/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.service.BaseManager;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.AlumnoDescuentoDao;
import mx.edu.um.mateo.inscripciones.model.AlumnoDescuento;
import mx.edu.um.mateo.inscripciones.service.AlumnoDescuentoManager;
import org.hibernate.context.spi.CurrentSessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author zorch
 */
@Service
public class AlumnoDescuentoManagerImpl extends BaseManager implements AlumnoDescuentoManager{
      
    @Autowired
    private AlumnoDescuentoDao dao;
    
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public AlumnoDescuento obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    @Override
    public void graba(AlumnoDescuento alumnoDescuento, Usuario usuario) {
        log.debug("{}",dao);
        log.debug("aqui es");
        dao.graba(alumnoDescuento, usuario);
    }
    

    @Override
    public String elimina(final Long id) {
       AlumnoDescuento alumnoDescuento = dao.obtiene(id);
       String result = null;
       //En este caso es un descuento de un alumno Inscrito 
        if("A".equals(alumnoDescuento.getStatus())){
            result= dao.elimina(new Long(id));
       }else{
            alumnoDescuento.setStatus(Constantes.STATUS_CANCELADO);
            dao.graba(alumnoDescuento, alumnoDescuento.getUsuario() );
            result= alumnoDescuento.getMatricula();
        }
        return result;
        
        
    }

    
}
