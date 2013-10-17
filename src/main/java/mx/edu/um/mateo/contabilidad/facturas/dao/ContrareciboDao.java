/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao;

import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.model.Contrarecibo;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author develop
 */
public interface ContrareciboDao {

    public Map<String, Object> lista(Map<String, Object> params);

    public void crea(Contrarecibo contrarecibo, Usuario usuario);

    public Contrarecibo obtiene(Long id);

    public void actualiza(Contrarecibo contrarecibo, Usuario usuario);

    public String elimina(Long id);
}
