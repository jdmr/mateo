/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.dao.ContrareciboDao;
import mx.edu.um.mateo.contabilidad.facturas.model.Contrarecibo;
import mx.edu.um.mateo.contabilidad.facturas.model.ContrareciboVO;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedorDetalle;
import mx.edu.um.mateo.contabilidad.facturas.model.ProveedorFacturas;
import mx.edu.um.mateo.contabilidad.facturas.service.ContrareciboManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.AutorizacionCCPlInvalidoException;
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
public class ContrareciboManagerImpl implements ContrareciboManager {

    @Autowired
    private ContrareciboDao dao;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public void graba(Contrarecibo contrarecibo, Usuario usuario) {
        dao.crea(contrarecibo, usuario);
    }

    @Override
    public Contrarecibo obtiene(Long id) {
        return dao.obtiene(new Long(id));
    }

    @Override
    public void actualiza(Contrarecibo contrarecibo, Usuario usuario) {
        dao.actualiza(contrarecibo, usuario);
    }

    /**
     * Método para cargar datos al ContrarecivoVO
     *
     * @param id
     * @return
     */
    @Override
    public List ListadeContrarecibosVO(Long id) {
        Contrarecibo contrarecibo = obtiene(id);
        ContrareciboVO contrareciboVO = null;
        List<InformeProveedorDetalle> detalles = contrarecibo.getDetalles();
        List<ContrareciboVO> vos = new ArrayList<>();
        for (InformeProveedorDetalle x : detalles) {
            contrareciboVO = new ContrareciboVO();
            ProveedorFacturas proveedorFacturas = x.getInformeProveedor().getProveedorFacturas();
            InformeProveedor informeProveedor = x.getInformeProveedor();
            contrareciboVO.setContrarecibo(contrarecibo);
            contrareciboVO.setInformeProveedor(informeProveedor);
            contrareciboVO.setProveedor(proveedorFacturas);
            contrareciboVO.setInformeProveedorDetalle(x);
            vos.add(contrareciboVO);
        }

        return vos;
    }

    @Override
    public String elimina(Long id) {
        Contrarecibo contrarecibo=obtiene(id);
        List<InformeProveedorDetalle> detalles=contrarecibo.getDetalles();
        for(InformeProveedorDetalle x:detalles){
        x.setStatus(Constantes.STATUS_ACTIVO);
        }
        return dao.elimina(id);
    }
}
