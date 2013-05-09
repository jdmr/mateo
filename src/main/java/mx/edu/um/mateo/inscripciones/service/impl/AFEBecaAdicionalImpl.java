/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Date;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.dao.AFEBecaAdicionalDao;
import mx.edu.um.mateo.inscripciones.model.AFEBecaAdicional;
import mx.edu.um.mateo.inscripciones.service.AFEBecaAdicionalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class AFEBecaAdicionalImpl implements AFEBecaAdicionalManager {

    @Autowired
    private AFEBecaAdicionalDao dao;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public AFEBecaAdicional obtiene(final Long id) {
        return dao.obtiene(id);
    }

    @Override
    public void crea(AFEBecaAdicional becaAdicional, Usuario usuario) {
        becaAdicional.setFechaAlta(new Date());
        dao.crea(becaAdicional, usuario);
    }

    @Override
    public String elimina(final Long id) {
        AFEBecaAdicional becaAdicional = dao.obtiene(id);
        dao.elimina(id);
        return becaAdicional.getMatricula();
    }

    @Override
    public void actualiza(final AFEBecaAdicional becaAdicional, Usuario usuario) {
        dao.actualiza(becaAdicional, usuario);
    }
}
