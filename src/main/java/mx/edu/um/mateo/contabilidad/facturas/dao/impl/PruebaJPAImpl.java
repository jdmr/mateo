/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao.impl;

import java.util.Collection;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import mx.edu.um.mateo.contabilidad.facturas.dao.InformeEmpleadoDao;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.Query;

/**
 *
 * @author develop
 */
public class PruebaJPAImpl extends BaseDao implements InformeEmpleadoDao {

    @PersistenceContext
    private EntityManager em;

    public String loadProductsByCategory(String category) {
        log.debug("Entro al metodo -*-*");
        return "saliendoMetodo";
    }

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public InformeEmpleado obtiene(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void crea(InformeEmpleado informe, Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actualiza(InformeEmpleado informe, Usuario usuario) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String elimina(Long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
