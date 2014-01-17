/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.JefeSeccionDao;
import mx.edu.um.mateo.rh.model.JefeSeccion;
import mx.edu.um.mateo.rh.service.JefeSeccionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class JefeSeccionManagerImpl implements JefeSeccionManager {

    @Autowired
    private JefeSeccionDao dao;

    /**
     * Regresa una lista de jefeSecciones.
     *
     * @param jefeSeccion
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * Obtiene una jefeSeccion
     *
     * @param id
     * @return
     */
    public JefeSeccion obtiene(final Long id) {
        return dao.obtiene(id);
    }

    /**
     * graba informacion sobre una jefeSeccion
     *
     * @param jefeSeccion the object to be saved
     */
    public void graba(JefeSeccion jefeSeccion, Usuario usuario) {
        dao.graba(jefeSeccion, usuario);
    }

    /**
     * Cambia el status de la jefeSeccion a I
     *
     * @param id el id de jefeSeccion
     */
    public String elimina(final Long id) {
        return dao.elimina(id);
    }

}
