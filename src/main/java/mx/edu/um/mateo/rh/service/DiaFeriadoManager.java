/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.DiaFeriado;

/**
 *
 * @author develop
 */
public interface DiaFeriadoManager {

    /**
     * Regresa una lista de diaFeriadoes.
     *
     * @param params
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una diaFeriado
     *
     * @param id
     * @return
     */
    public DiaFeriado obtiene(final Long id);

    /**
     * graba informacion sobre una diaFeriado
     *
     * @param diaFeriado the object to be saved
     * @param usuario
     */
    public void graba(DiaFeriado diaFeriado, Usuario usuario);

    /**
     * Cambia el status de la diaFeriado a I
     *
     * @param id el id de diaFeriado
     * @return regresa el nombre del dia eliminado
     */
    public String elimina(final Long id);
}
