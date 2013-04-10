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
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
<<<<<<< HEAD
    public Paquete obtiene(final String id) {
        return dao.obtiene(new Long(id));
=======
    public Paquete getPaquete(final String id) {
        return dao.getPaquete(new Long(id));
>>>>>>> b20b2e03d2080fc6bc589dd66140f8133a75a89e
    }

    @Override
    public void crea(Paquete paquete, Usuario usuario) {
        dao.crea(paquete, usuario);
    }

    @Override
<<<<<<< HEAD
    public void elimina(final String id) {
        dao.elimina(new Long(id));
=======
    public void removePaquete(final String id) {
        dao.removePaquete(new Long(id));
>>>>>>> b20b2e03d2080fc6bc589dd66140f8133a75a89e
    }
}
