/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeEmpleadoDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeEmpleadoManager;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service("informeEmpleadoManager")
//@Component(value = "informeEmpleadoMgr")
public class InformeEmpleadoManagerImpl extends BaseDao implements InformeEmpleadoManager, Serializable {

    @Autowired
    private InformeEmpleadoDao dao;

    @Override
    public List<InformeEmpleado> getInformes(Long empresaId) {
        log.debug("Entro a getinformes");
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", empresaId);
        return (List) lista(params).get("informes");
    }

    @Override
    public void crea(InformeEmpleado informe, Usuario usuario) {

        log.debug("Entrando a crea{}, usuario{}", informe, usuario);
        graba(informe, usuario);
    }

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public InformeEmpleado obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    @Override
    public void graba(InformeEmpleado informeEmpleado, Usuario usuario) {
        log.debug("entrando a graba informeEmpleadoManager");
        dao.crea(informeEmpleado, usuario);
    }

    @Override
    public void actualiza(InformeEmpleado informeEmpleado, Usuario usuario) {
        dao.actualiza(informeEmpleado, usuario);
    }

    @Override
    public String elimina(final Long id) {
        InformeEmpleado informeEmpleado = dao.obtiene(id);
        dao.elimina(new Long(id));
        return informeEmpleado.getNumNomina();
    }

    @Override
    public void finaliza(InformeEmpleado informeEmpleado, Usuario usuario) {
        informeEmpleado.setStatus(Constantes.STATUS_FINALIZADO);
        dao.actualiza(informeEmpleado, usuario);

    }

    @Override
    public void autorizar(InformeEmpleado informeEmpleado, Usuario usuario) {
        informeEmpleado.setStatus(Constantes.STATUS_AUTORIZADO);
        dao.actualiza(informeEmpleado, usuario);

    }

    @Override
    public void rechazar(InformeEmpleado informeEmpleado, Usuario usuario) {
        informeEmpleado.setStatus(Constantes.STATUS_RECHAZADO);
        dao.actualiza(informeEmpleado, usuario);

    }
}
