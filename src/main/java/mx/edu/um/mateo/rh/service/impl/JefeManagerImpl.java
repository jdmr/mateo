/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.JefeDao;
import mx.edu.um.mateo.rh.model.Jefe;
import mx.edu.um.mateo.rh.service.JefeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class JefeManagerImpl implements JefeManager {

    @Autowired
    private JefeDao dao;

    /**
     * Regresa una lista de jefe.
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * Obtiene una jefe
     *
     * @param id
     * @return
     */
    @Override
    public Jefe obtiene(final Long id) {
        return dao.obtiene(id);
    }

    /**
     * graba informacion sobre una jefe
     *
     * @param jefe the object to be saved
     * @param usuario
     */
    @Override
    public void graba(Jefe jefe, Usuario usuario) {
        dao.graba(jefe, usuario);
    }

    /**
     * Elimina el jefe
     *
     * @param id el id de jefe
     * @return
     */
    @Override
    public String elimina(final Long id) {
        return dao.elimina(id);
    }

}
