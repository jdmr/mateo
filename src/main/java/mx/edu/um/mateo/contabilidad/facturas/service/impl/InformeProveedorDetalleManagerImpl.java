/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service.impl;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeProveedorDao;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeProveedorDetallesDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorDetalleManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.BancoNoCoincideException;
import mx.edu.um.mateo.general.utils.ClabeNoCoincideException;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.CuentaChequeNoCoincideException;
import mx.edu.um.mateo.general.utils.ProveedorNoCoincideException;
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
    public Map<String, Object> revisar(Map<String, Object> params) {
        return dao.revisar(params);
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

    @Override
    public void autorizar(List ids) throws Exception {
        String cuentaCheque;
        String clabe;
        String banco;
        String proveedor;
        Long id = (Long) ids.get(ids.size() - 1);
        InformeProveedorDetalle detalle = dao.obtiene(id);
        cuentaCheque = detalle.getInformeProveedor().getCuentaCheque();
        clabe = detalle.getInformeProveedor().getClabe();
        banco = detalle.getInformeProveedor().getProveedorFacturas().getBanco();
        proveedor = detalle.getInformeProveedor().getNombreProveedor();
        detalle.setStatus(Constantes.STATUS_AUTORIZADO);
        for (Object id2 : ids) {
            InformeProveedorDetalle detalle2 = dao.obtiene((Long) id2);
            String cuentaCheque2 = detalle2.getInformeProveedor().getCuentaCheque();
            String clabe2 = detalle2.getInformeProveedor().getClabe();
            String banco2 = detalle2.getInformeProveedor().getProveedorFacturas().getBanco();
            String proveedor2 = detalle2.getInformeProveedor().getNombreProveedor();
            if (!cuentaCheque.equals(cuentaCheque2) || cuentaCheque2.isEmpty() || cuentaCheque2 == null) {
                throw new CuentaChequeNoCoincideException(id.toString());
            }
            if (!clabe.equals(clabe2) || clabe2.isEmpty() || clabe2 == null) {
                throw new ClabeNoCoincideException(id.toString());
            }
            if (!banco.equals(banco2) || banco2.isEmpty() || banco2 == null) {
                throw new BancoNoCoincideException(id.toString());
            }
            if (!proveedor.equals(proveedor2) || proveedor2.isEmpty() || proveedor2 == null) {
                throw new ProveedorNoCoincideException(id.toString());
            }
            detalle2.setStatus(Constantes.STATUS_AUTORIZADO);
        }
    }

    @Override
    public void rechazar(List ids) throws Exception {
        String proveedor;
        Long id = (Long) ids.get(ids.size() - 1);
        InformeProveedorDetalle detalle = dao.obtiene(id);
        proveedor = detalle.getInformeProveedor().getNombreProveedor();
        detalle.setStatus(Constantes.STATUS_RECHAZADO);
        for (Object id2 : ids) {
            InformeProveedorDetalle detalle2 = dao.obtiene((Long) id2);
            String proveedor2 = detalle2.getInformeProveedor().getNombreProveedor();
            if (!proveedor.equals(proveedor2) || proveedor2.isEmpty() || proveedor2 == null) {
                throw new ProveedorNoCoincideException(id.toString());
            }
            detalle2.setStatus(Constantes.STATUS_RECHAZADO);
        }
    }
}
