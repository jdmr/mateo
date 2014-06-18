/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao;
import mx.edu.um.mateo.rh.model.VacacionesEmpleado;
import mx.edu.um.mateo.rh.service.VacacionesEmpleadoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class VacacionesEmpleadoManagerImpl implements VacacionesEmpleadoManager {

    @Autowired
    private VacacionesEmpleadoDao dao;

    /**
     * Regresa una lista de vacacionesEmpleadoes.
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * Obtiene una vacacionesEmpleado
     *
     * @param id
     * @return
     */
    @Override
    public VacacionesEmpleado obtiene(final Long id) {
        return dao.obtiene(id);
    }

    /**
     * graba informacion sobre una vacacionesEmpleado
     *
     * @param vacacionesEmpleado the object to be saved
     * @param usuario
     */
    @Override
    public void graba(VacacionesEmpleado vacacionesEmpleado, Usuario usuario) {
        dao.graba(vacacionesEmpleado, usuario);
    }

    /**
     * Elimina las vacaciones
     *
     * @param id el id de vacacionesEmpleado
     * @return
     */
    @Override
    public String elimina(final Long id) {
        return dao.elimina(id);
    }

    /**
     *
     * @return
     */
    @Override
    public Integer totalDias() {
        return dao.totalDias();
    }
}
