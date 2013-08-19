/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service.impl;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.dao.ContrareciboDao;
import mx.edu.um.mateo.contabilidad.facturas.model.Contrarecibo;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.service.ContrareciboManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.AutorizacionCCPlInvalidoException;
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
}
