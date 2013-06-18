/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service.impl;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeProveedorDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeEmpleadoManager;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorManager;
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
public class InformeProveedorManagerImpl implements InformeProveedorManager {

    @Autowired
    private InformeProveedorDao dao;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public InformeProveedor obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    @Override
    public void graba(InformeProveedor informeProveedor, Usuario usuario) {
        dao.crea(informeProveedor, usuario);
    }

    @Override
    public void actualiza(InformeProveedor informeProveedor, Usuario usuario) {
        dao.actualiza(informeProveedor, usuario);
    }

    @Override
    public String elimina(final Long id) {
        InformeProveedor informeProveedor = dao.obtiene(id);
        dao.elimina(new Long(id));
        return informeProveedor.getNombreProveedor();
    }
}
