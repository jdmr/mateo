/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.JefeSeccion;

/**
 *
 * @author develop
 */
public interface JefeSeccionDao {

    /**
     * Regresa una lista de jefeSecciones.
     *
     * @param jefeSeccion
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una jefeSeccion
     *
     * @param id
     * @return
     */
    public JefeSeccion obtiene(final Long id);

    /**
     * graba informacion sobre una jefeSeccion
     *
     * @param jefeSeccion the object to be saved
     */
    public void graba(JefeSeccion jefeSeccion, Usuario usuario);

    /**
     * Cambia el status de la jefeSeccion a I
     *
     * @param id el id de jefeSeccion
     */
    public String elimina(final Long id);

    /**
     * Obtiene la claveActiva de un empleado a travez del id del empleado y el
     * estatus de la clave
     *
     * @param idEmpleado
     * @return
     */
    public JefeSeccion obtieneClaveActiva(Long idEmpleado);

    /**
     * Veridica que exista claveActiva de un empleado atravez del atributo clave
     *
     * @param clave
     * @return true o false si la clave existe o no
     */
    public Boolean noExisteClave(String clave);
}
