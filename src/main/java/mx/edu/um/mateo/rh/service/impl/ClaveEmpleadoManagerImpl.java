/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.service.BaseManager;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.rh.dao.ClaveEmpleadoDao;
import mx.edu.um.mateo.rh.model.ClaveEmpleado;
import mx.edu.um.mateo.rh.service.ClaveEmpleadoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.org.mozilla.javascript.ScriptRuntime;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class ClaveEmpleadoManagerImpl extends BaseManager implements ClaveEmpleadoManager {

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

    @Override
    public ClaveEmpleado nuevaClave(Usuario usuario, String filtro) {
        log.debug("entrando a nueva clave");
        Map<String, Object> params = new HashMap<>();
        params.put("filtro", filtro);
        params = dao.lista(params);
        List<ClaveEmpleado> claves = (List) params.get(Constantes.CONTAINSKEY_CLAVEEMPLEADO);
        ClaveEmpleado claveAnterior = null;
        ClaveEmpleado nueva = new ClaveEmpleado();
        int anterior = 0, actual;
        String hueco = null;
        Boolean huboHueco = false;
        if (claves.isEmpty()) {
            log.debug("no habia claves");
            String c = filtro + "0001";
            nueva.setClave(c);
            nueva.setStatus(Constantes.STATUS_ACTIVO);
            nueva.setFecha(new Date());
            nueva.setFechaAlta(new Date());
            nueva.setUsuarioAlta(usuario);
            return nueva;
        }
        for (ClaveEmpleado claveActual : claves) {
            actual = Integer.parseInt(claveActual.getClave());
            log.debug("claveactual{}", actual);
            if (claveAnterior != null) {
                anterior = Integer.parseInt(claveAnterior.getClave());
                log.debug("anterior{}", anterior);
            }
            if (claveAnterior != null && actual - anterior > 1) {
                log.debug("suma....{}", actual - anterior);
                log.debug("habia hueco");
                hueco = String.valueOf(anterior + 1);
                log.debug("hueco{}", hueco);
                if (dao.noExisteClave(hueco)) {
                    nueva.setClave(hueco);
                    nueva.setStatus(Constantes.STATUS_ACTIVO);
                    nueva.setFecha(new Date());
                    nueva.setFechaAlta(new Date());
                    nueva.setUsuarioAlta(usuario);
                    huboHueco = true;
                    break;
                }

            }
            claveAnterior = claveActual;
        }

        if (!huboHueco) {
            log.debug("creando nueva clave");
            int c = Integer.parseInt(claveAnterior.getClave());
            String cs = String.valueOf(c + 1);
            log.debug("cs-_{}", cs);
            if (dao.noExisteClave(cs)) {
                nueva.setClave(cs);
                nueva.setStatus(Constantes.STATUS_ACTIVO);
                nueva.setFecha(new Date());
                nueva.setFechaAlta(new Date());
                nueva.setUsuarioAlta(usuario);
            }
        }
        return nueva;
    }
}
