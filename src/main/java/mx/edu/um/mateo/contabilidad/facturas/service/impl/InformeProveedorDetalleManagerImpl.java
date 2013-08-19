/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service.impl;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeProveedorDao;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeProveedorDetallesDao;
import mx.edu.um.mateo.contabilidad.facturas.model.Contrarecibo;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.contabilidad.facturas.service.ContrareciboManager;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorDetalleManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.service.BaseManager;
import mx.edu.um.mateo.general.utils.BancoNoCoincideException;
import mx.edu.um.mateo.general.utils.ClabeNoCoincideException;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.CuentaChequeNoCoincideException;
import mx.edu.um.mateo.general.utils.FormaPagoNoCoincideException;
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
public class InformeProveedorDetalleManagerImpl extends BaseManager implements InformeProveedorDetalleManager {

    @Autowired
    private InformeProveedorDetallesDao dao;
    @Autowired
    private ContrareciboManager contrareciboManager;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public Map<String, Object> revisar(Map<String, Object> params) {
        return dao.revisar(params);
    }

    @Override
    public Map<String, Object> contrarecibo(Map<String, Object> params) {
        return dao.contrarecibo(params);
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
    public void autorizar(List ids, Usuario usuario) throws Exception {
        String cuentaCheque;
        String clabe;
        String banco;
        String proveedor;
        String formaPago;
        Contrarecibo contrarecibo = new Contrarecibo();
        contrareciboManager.graba(contrarecibo, usuario);
        String id = (String) ids.get(ids.size() - 1);
        InformeProveedorDetalle detalle = dao.obtiene(Long.valueOf(id));
        cuentaCheque = detalle.getInformeProveedor().getCuentaCheque();
        clabe = detalle.getInformeProveedor().getClabe();
        banco = detalle.getInformeProveedor().getBanco();
        proveedor = detalle.getInformeProveedor().getNombreProveedor();
        detalle.setContrarecibo(contrarecibo);
        detalle.setStatus(Constantes.STATUS_AUTORIZADO);
        formaPago = detalle.getInformeProveedor().getFormaPago();
        for (Object id2 : ids) {
            String ide = (String) id2;
            InformeProveedorDetalle detalle2 = dao.obtiene(Long.valueOf(ide));
            String cuentaCheque2 = detalle2.getInformeProveedor().getCuentaCheque();
            String clabe2 = detalle2.getInformeProveedor().getClabe();
            String banco2 = detalle2.getInformeProveedor().getBanco();
            String proveedor2 = detalle2.getInformeProveedor().getNombreProveedor();
            String formaPago2 = detalle2.getInformeProveedor().getFormaPago();
            if (!cuentaCheque.equals(cuentaCheque2)) {
                log.debug("las cuentas no coinciden");
                throw new CuentaChequeNoCoincideException(id.toString());
            }
            if (!clabe.equals(clabe2)) {
                log.debug("las clabes no coinciden");
                throw new ClabeNoCoincideException(id.toString());
            }
            if (!banco.equals(banco2)) {
                log.debug("los bancos no coinciden");
                throw new BancoNoCoincideException(id.toString());
            }
            if (!proveedor.equals(proveedor2)) {
                log.debug("los proveedores no coinciden");
                throw new ProveedorNoCoincideException(id.toString());
            }
            if (!formaPago.equals(formaPago2)) {
                log.debug("la forma de pago no coinciden");
                throw new FormaPagoNoCoincideException(id.toString());
            }
            detalle2.setStatus(Constantes.STATUS_AUTORIZADO);
            detalle2.setContrarecibo(contrarecibo);
        }
    }

    @Override
    public void rechazar(List ids) throws Exception {
        String proveedor;
        String id = (String) ids.get(ids.size() - 1);
        InformeProveedorDetalle detalle = dao.obtiene(Long.valueOf(id));
        proveedor = detalle.getInformeProveedor().getNombreProveedor();
        detalle.setStatus(Constantes.STATUS_RECHAZADO);
        for (Object id2 : ids) {
            String ide = (String) id2;
            InformeProveedorDetalle detalle2 = dao.obtiene(Long.valueOf(ide));
            String proveedor2 = detalle2.getInformeProveedor().getNombreProveedor();
            if (!proveedor.equals(proveedor2) || proveedor2.isEmpty() || proveedor2 == null) {
                throw new ProveedorNoCoincideException(id.toString());
            }
            detalle2.setStatus(Constantes.STATUS_RECHAZADO);
        }
    }
}
