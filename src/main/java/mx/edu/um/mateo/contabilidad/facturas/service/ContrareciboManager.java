/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.model.Contrarecibo;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author develop
 */
public interface ContrareciboManager {

    public Map<String, Object> lista(Map<String, Object> params);

    public void graba(Contrarecibo contrarecibo, Usuario usuario);

    public Contrarecibo obtiene(Long id);

    public void actualiza(Contrarecibo contrarecibo, Usuario usuario);

    public List ListadeContrarecibosVO(Long id);

    public String elimina(Long id);
}
