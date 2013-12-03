/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import mx.edu.um.mateo.contabilidad.facturas.dao.CCPDao;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeProveedorDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeEmpleadoManager;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeProveedorManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.service.BaseManager;
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
@Service("informeProveedorManager")
public class InformeProveedorManagerImpl extends BaseManager implements InformeProveedorManager, Serializable {

    @Autowired
    private InformeProveedorDao dao;
    @Autowired
    private CCPDao ccpDao;

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public Map<String, Object> revisar(Map<String, Object> params) {
        return dao.revisar(params);
    }

    @Override
    public InformeProveedor obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    @Override
    public void graba(InformeProveedor informeProveedor, Usuario usuario) throws AutorizacionCCPlInvalidoException {
        String CCP = informeProveedor.getCcp();
        if (CCP != null && !CCP.isEmpty()) {
            String listaCcps = informeProveedor.getCcp();
            String[] ccps = listaCcps.split(",");
            for (String ccp : ccps) {
                if (!ccpDao.obtiene(ccp)) {
                    throw new AutorizacionCCPlInvalidoException(ccp);
                }
            }
        }
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

    @Override
    public void finaliza(InformeProveedor informeProveedor, Usuario usuario) {
        log.debug("informe...**manager {}", informeProveedor);
        informeProveedor.setFechaPago(new Date());
        informeProveedor.setContraRecibo(String.valueOf(new Date().getTime()));
        informeProveedor.setStatus(Constantes.STATUS_FINALIZADO);
        log.debug("informe...**manager2 {}", informeProveedor);
        dao.actualiza(informeProveedor, usuario);

    }

    @Override
    public void autorizar(InformeProveedor informeProveedor, Usuario usuario) {
        informeProveedor.setStatus(Constantes.STATUS_AUTORIZADO);
        dao.actualiza(informeProveedor, usuario);
    }

    @Override
    public void rechazar(InformeProveedor informeProveedor, Usuario usuario) {
        informeProveedor.setStatus(Constantes.STATUS_RECHAZADO);
        dao.actualiza(informeProveedor, usuario);
    }

    @Override
    public List<InformeProveedor> getInformes(Long empresaId) {
        log.debug("Entro a getinformes");
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", empresaId);
        return (List) lista(params).get("informes");
    }

    @Override
    public void crea(InformeProveedor informe, Usuario usuario) {
        log.debug("Entrando a crea{}, usuario{}", informe, usuario);
        try {
            graba(informe, usuario);
        } catch (AutorizacionCCPlInvalidoException ex) {
            log.debug("CPP invalido");
        }
    }
}
