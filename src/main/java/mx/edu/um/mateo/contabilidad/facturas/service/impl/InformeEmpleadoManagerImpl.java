/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeEmpleadoDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeEmpleadoManager;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class InformeEmpleadoManagerImpl implements InformeEmpleadoManager {

    @Autowired
    private InformeEmpleadoDao dao;

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
}
