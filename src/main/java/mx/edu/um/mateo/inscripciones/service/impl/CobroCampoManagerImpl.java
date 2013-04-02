/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.dao.CobroCampoDao;
import mx.edu.um.mateo.inscripciones.dao.ProrrogaDao;
import mx.edu.um.mateo.inscripciones.model.CobroCampo;
import mx.edu.um.mateo.inscripciones.model.Prorroga;
import mx.edu.um.mateo.inscripciones.service.CobroCampoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class CobroCampoManagerImpl  implements CobroCampoManager{
    
    @Autowired
    private CobroCampoDao dao;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public CobroCampo obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    @Override
    public void graba(CobroCampo cobroCampo, Usuario usuario) {
        dao.graba(cobroCampo, usuario);
    }

    @Override
    public String elimina(final Long id) {
        CobroCampo cobroCampo = dao.obtiene(id);
        dao.elimina(new Long(id));
        return cobroCampo.getMatricula();
    }
}
