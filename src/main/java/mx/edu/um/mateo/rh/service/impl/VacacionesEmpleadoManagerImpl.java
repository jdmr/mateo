/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao;
import mx.edu.um.mateo.rh.model.VacacionesEmpleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class VacacionesEmpleadoManagerImpl extends BaseDao {

    @Autowired
    private VacacionesEmpleadoDao dao;

    /**
     * Regresa una lista de vacaciones.
     *
     * @param params
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * Obtiene una vacaciones
     *
     * @param id
     * @return
     */
    public VacacionesEmpleado obtiene(final Long id) {
        return dao.obtiene(id);
    }

    /**
     * graba informacion sobre las vacaciones de algun empleado
     *
     * @param vacaciones the object to be saved
     * @param usuario
     */
    public void graba(VacacionesEmpleado vacaciones, Usuario usuario) {
        dao.graba(vacaciones, usuario);
    }

    /**
     * Elimina las vacaciones
     *
     * @param id el id de vacaciones
     * @return
     */
    public String elimina(final Long id) {
        return dao.elimina(id);
    }

}
