/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.dao.AlumnoPaqueteDao;
import mx.edu.um.mateo.inscripciones.model.AlumnoPaquete;
import mx.edu.um.mateo.inscripciones.service.AlumnoPaqueteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author semdariobarbaamaya
 */
@Transactional
@Service
public class AlumnoPaqueteManagerImpl implements AlumnoPaqueteManager{
    
    @Autowired
    private AlumnoPaqueteDao dao;
    
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public AlumnoPaquete obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    @Override
    public void graba(AlumnoPaquete alumnoPaquete, Usuario usuario) {
        dao.graba(alumnoPaquete, usuario);
    }
    
    @Override
    public AlumnoPaquete actualiza(AlumnoPaquete alumnoPaquete){
        return dao.actualiza(alumnoPaquete);
    }

    @Override
    public void elimina(final Long id) {
        dao.elimina(new Long(id));
    }

}
