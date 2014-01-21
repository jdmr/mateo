/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.service.BaseManager;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.dao.EmpleadoDao;
import mx.edu.um.mateo.rh.dao.JefeDao;
import mx.edu.um.mateo.rh.dao.JefeSeccionDao;
import mx.edu.um.mateo.rh.model.Empleado;
import mx.edu.um.mateo.rh.model.Jefe;
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
public class JefeSeccionManagerImpl extends BaseManager implements JefeSeccionManager {

    @Autowired
    private JefeSeccionDao dao;
    @Autowired
    private EmpleadoDao empleadoDao;
    @Autowired
    private JefeDao jefeDao;

    /**
     * Regresa una lista de jefeSecciones.
     *
     * @param params
     * @return
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * Obtiene una jefeSeccion
     *
     * @param id
     * @return
     */
    @Override
    public JefeSeccion obtiene(final Long id) {
        return dao.obtiene(id);
    }

    /**
     * graba informacion sobre una jefeSeccion
     *
     * @param jefeSeccion the object to be saved
     * @param usuario
     * @param ids
     */
    public void graba(JefeSeccion jefeSeccion, Usuario usuario, List ids) {
        Empleado empleado = empleadoDao.getEmpleado(jefeSeccion.getJefeSeccion().getNombre());
        log.debug("empleadoManager{}", empleado.getNombre());
        for (Object id : ids) {
            log.debug("entrandoaforgraba");
            String idJefe = String.valueOf(id);
            Jefe jefe = jefeDao.obtiene(Long.valueOf(idJefe));

            JefeSeccion jefeSeccion1 = new JefeSeccion();
            jefeSeccion1.setFechaAlta(new Date());
            jefeSeccion1.setJefeSeccion(empleado);
            jefeSeccion1.setJefe(jefe);
            jefeSeccion1.setStatus(Constantes.STATUS_ACTIVO);
            jefeSeccion1.setUsuarioAlta(usuario);
            log.debug("grabando");
            dao.graba(jefeSeccion1, usuario);
        }
    }

    /**
     * Cambia el status de la jefeSeccion a I
     *
     * @param id el id de jefeSeccion
     * @return
     */
    @Override
    public String elimina(final Long id) {
        return dao.elimina(id);
    }

}
