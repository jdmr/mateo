/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.dao.PaqueteDao;
import mx.edu.um.mateo.inscripciones.model.Paquete;
import mx.edu.um.mateo.inscripciones.service.PaqueteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class PaqueteManagerImpl implements PaqueteManager {

    @Autowired
    private PaqueteDao dao;

    @Override
    public Map<String, Object> getPaquetes(Map<String, Object> params) {
        return dao.getPaquetes(params);
    }

    @Override
    public Paquete getPaquete(final String id) {
        return dao.getPaquete(new Long(id));
    }

    @Override
    public void graba(Paquete paquete, Usuario usuario) {
        dao.graba(paquete, usuario);
    }

    @Override
    public void removePaquete(final String id) {
        dao.removePaquete(new Long(id));
    }
}
