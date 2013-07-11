/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service.impl;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeProveedorDao;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeProveedorDetallesDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorDetalleManager;
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
public class InformeProveedorDetalleManagerImpl implements InformeProveedorDetalleManager {

    @Autowired
    private InformeProveedorDetallesDao dao;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public InformeProveedorDetalle obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    @Override
    public void graba(InformeProveedorDetalle proveedorDetalle, Usuario usuario) {
        dao.crea(proveedorDetalle, usuario);
    }

    @Override
    public void actualiza(InformeProveedorDetalle proveedorDetalle, Usuario usuario) {
        dao.actualiza(proveedorDetalle, usuario);
    }

    @Override
    public String elimina(final Long id) {
        InformeProveedorDetalle proveedorDetalle = dao.obtiene(id);
        dao.elimina(new Long(id));
        return proveedorDetalle.getNombreProveedor();
    }
}
