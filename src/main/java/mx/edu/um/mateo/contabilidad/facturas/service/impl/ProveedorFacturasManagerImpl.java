/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service.impl;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.dao.ProveedorFacturasDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.contabilidad.facturas.service.ProveedorFacturasManager;
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
public class ProveedorFacturasManagerImpl implements ProveedorFacturasManager {

    @Autowired
    private ProveedorFacturasDao dao;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public ProveedorFacturas obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    @Override
    public ProveedorFacturas obtiene(String rfc) {
        return dao.obtiene(rfc);
    }

    @Override
    public void graba(ProveedorFacturas proveedorFacturas, Usuario usuario) {
        dao.crea(proveedorFacturas, usuario);
    }

    @Override
    public void actualiza(ProveedorFacturas proveedorFacturas, Usuario usuario) {
        dao.actualiza(proveedorFacturas, usuario);
    }

    @Override
    public String elimina(final Long id) {
        ProveedorFacturas proveedorFacturas = dao.obtiene(id);
        dao.elimina(new Long(id));
        return proveedorFacturas.getRfc();
    }
}
