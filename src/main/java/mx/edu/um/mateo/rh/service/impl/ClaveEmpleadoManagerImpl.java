/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Date;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.ClaveEmpleadoDao;
import mx.edu.um.mateo.rh.model.ClaveEmpleado;
import mx.edu.um.mateo.rh.service.ClaveEmpleadoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class ClaveEmpleadoManagerImpl implements ClaveEmpleadoManager {

    @Autowired
    private ClaveEmpleadoDao dao;

    /**
     * Regresa una lista de claveEmpleadoes.
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * Obtiene una claveEmpleado
     *
     * @param id
     * @return
     */
    @Override
    public ClaveEmpleado obtiene(final Long id) {
        return dao.obtiene(id);
    }

    /**
     * graba informacion sobre una claveEmpleado
     *
     * @param claveEmpleado the object to be saved
     * @param usuario
     */
    @Override
    public void graba(ClaveEmpleado claveEmpleado, Usuario usuario) {
        claveEmpleado.setUsuarioAlta(usuario);
        claveEmpleado.setFechaAlta(new Date());
        dao.graba(claveEmpleado, usuario);
    }

    /**
     * Cambia el status de la claveEmpleado a I
     *
     * @param id el id de claveEmpleado
     * @return
     */
    @Override
    public String elimina(final Long id) {
        return dao.elimina(id);
    }

    /**
     * Obtiene la claveActiva de un empleado a travez del id del empleado y el
     * estatus de la clave
     *
     * @param idEmpleado
     * @return
     */
    @Override
    public ClaveEmpleado obtieneClaveActiva(Long idEmpleado) {
        return dao.obtieneClaveActiva(idEmpleado);
    }
}
