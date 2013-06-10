/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service.impl;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeEmpleadoDao;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeEmpleadoDetalleDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeEmpleadoDetalleManager;
import mx.edu.um.mateo.general.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class InformeEmpleadoDetalleManagerImpl implements InformeEmpleadoDetalleManager {

    @Autowired
    private InformeEmpleadoDetalleDao dao;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public InformeEmpleadoDetalle obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    @Override
    public void graba(InformeEmpleadoDetalle detalle, Usuario usuario) {
        dao.crea(detalle, usuario);
    }

    @Override
    public void actualiza(InformeEmpleadoDetalle detalle, Usuario usuario) {
        dao.actualiza(detalle, usuario);
    }

    @Override
    public String elimina(final Long id) {
        InformeEmpleadoDetalle detalle = dao.obtiene(id);
        dao.elimina(new Long(id));
        return detalle.getNombreProveedor();
    }
}
