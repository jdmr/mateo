/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Date;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.DiaFeriado;

/**
 *
 * @author develop
 */
public interface DiaFeriadoDao {

    /**
     * Regresa una lista de diaFeriadoes.
     *
     * @param diaFeriado
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
     */
    public void graba(DiaFeriado diaFeriado, Usuario usuario);

    /**
     * Verifica si una fecha dada esta en el catalogo de días feriados
     *
     * @param fecha
     * @return Si esta en el catalogo regresa true, si no false;
     */
    public Boolean esFeriado(Date fecha);

    /**
     * Elimina el dia feriado
     *
     * @param id el id de diaFeriado
     */
    public String elimina(final Long id);
}
