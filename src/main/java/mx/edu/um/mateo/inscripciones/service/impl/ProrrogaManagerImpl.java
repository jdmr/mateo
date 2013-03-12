/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.dao.ProrrogaDao;
import mx.edu.um.mateo.inscripciones.dao.TiposBecasDao;
import mx.edu.um.mateo.inscripciones.model.Prorroga;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import mx.edu.um.mateo.inscripciones.service.ProrrogaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class ProrrogaManagerImpl implements ProrrogaManager {

    @Autowired
    private ProrrogaDao dao;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public Prorroga obtiene(final Integer id) {
        return dao.obtiene(new Integer(id));
    }

    @Override
    public void graba(Prorroga prorroga, Usuario usuario) {
        dao.graba(prorroga, usuario);
    }

    @Override
    public String elimina(final Integer id) {
        Prorroga prorroga = dao.obtiene(id);
        dao.elimina(new Integer(id));
        return prorroga.getDescripcion();
    }
}
