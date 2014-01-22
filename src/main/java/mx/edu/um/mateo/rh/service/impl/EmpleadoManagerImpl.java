/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.service.BaseManager;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.dao.EmpleadoDao;
import mx.edu.um.mateo.rh.model.ClaveEmpleado;
import mx.edu.um.mateo.rh.model.Empleado;
import mx.edu.um.mateo.rh.service.EmpleadoManager;
import mx.edu.um.mateo.rh.service.TipoEmpleadoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author AMDA
 */
@Component
@Transactional
public class EmpleadoManagerImpl extends BaseManager implements EmpleadoManager {

    @Autowired
    private EmpleadoDao dao;
    @Autowired
    private TipoEmpleadoManager teMgr;

    /**
     * @see
     * mx.edu.um.mateo.mateo.rh.service.EmpleadoManager#lista(java.util.Map)
     */
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * @see mx.edu.um.mateo.mateo.rh.service.EmpleadoManager#
     */
    public Empleado obtiene(Long id) throws ObjectRetrievalFailureException {
        return dao.obtiene(id);
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.service.EmpleadoManager#getEmpleados(mx.edu.um.rh.model.Empleado)
     */
    public List getEmpleados(final Empleado empleado) {
        return dao.searchEmpleado(empleado);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.EmpleadoManager#getEmpleado(String id)
     */
    public Empleado getEmpleado(final Empleado empleado) {
        return dao.getEmpleado(empleado);
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.service.EmpleadoManager#saveEmpleado(mx.edu.um.mateo.rh.model.Empleado,
     * mx.edu.um.mateo.general.model.Usuario)
     */
    public void saveEmpleado(Empleado empleado, Usuario usuario, ClaveEmpleado clave) {
        log.debug("saveEmpleado ");
        dao.saveEmpleado(empleado, usuario, clave);
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.service.EmpleadoManager#removeEmpleado(mx.edu.um.mateo.rh.model.Empleado)
     */
    public void removeEmpleado(final Empleado empleado) {
        //dao.removeEmpleado(empleado);

    }

    /**
     * @see mx.edu.um.mateo.rh.service.EmpleadoManager#elimina(java.lang.Long)
     */
    public String elimina(Long id) {
        return "";
    }

    public List searchEmpleado(Empleado empleado) {
        return dao.searchEmpleado(empleado);
    }

    public List searchEmpleadoByClaveOrApPaterno(Empleado empleado) {
        return dao.searchEmpleadoByClaveOrApPaterno(empleado);
    }

    public Object getEmpleadosBday(Empleado empleado) {
        return dao.getEmpleadosBday(empleado);
    }

    public Empleado getEmpleadoByClave(final Empleado empleado) {
        return dao.getEmpleadoClave(empleado);
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.service.SolicitudSalidaManager#getDiasVacacionesActuales(SolicitudSalida
     * solicitudSalida)throwsException
     */
    public void saveDiasVacacionesActuales(Empleado empleado, User user) throws Exception {

        Integer dias = 0;
        Boolean sw = false;

    }

    @Override
    public void actualizaEmpleado(Empleado empleado) {
        dao.actualiza(empleado);
    }

}
