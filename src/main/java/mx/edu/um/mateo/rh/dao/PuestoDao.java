/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.Puesto;

/**
 * 
 * @author osoto
 */
public interface PuestoDao {
	public Map<String, Object> lista(Map<String, Object> params);

	public Puesto obtiene(Long id);

	public Puesto graba(Puesto puesto, Usuario usuario);

	public String elimina(Long id);

}
