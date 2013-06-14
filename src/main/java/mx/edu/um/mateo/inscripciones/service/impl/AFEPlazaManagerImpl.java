/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.dao.AFEConvenioDao;
import mx.edu.um.mateo.inscripciones.dao.AFEPlazaDao;
import mx.edu.um.mateo.inscripciones.model.AFEPlaza;
import mx.edu.um.mateo.inscripciones.model.Prorroga;
import mx.edu.um.mateo.inscripciones.service.AFEConvenioManager;
import mx.edu.um.mateo.inscripciones.service.AFEPlazaManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class AFEPlazaManagerImpl implements AFEPlazaManager {

    @Autowired
    private AFEPlazaDao dao;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public AFEPlaza obtiene(final Long id) {
        return dao.obtiene(id);
    }

    @Override
    public void crea(AFEPlaza afePlaza, Usuario usuario) {
        dao.crea(afePlaza, usuario);
    }

    @Override
    public String elimina(final Long id) {
        AFEPlaza afePlaza = dao.obtiene(id);
        dao.elimina(id);
        return afePlaza.getTipoPlaza();
    }

    @Override
    public void actualiza(final AFEPlaza afePlaza, Usuario usuario) {
        dao.actualiza(afePlaza, usuario);
    }
}
