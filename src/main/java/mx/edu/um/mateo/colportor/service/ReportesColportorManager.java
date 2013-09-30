/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.service;

import java.util.Map;

/**
 *
 * @author osoto
 */
public interface ReportesColportorManager {
    /**
     * Regresa el listado de censo de colportores
     * @param params
     * @return
     * @throws Exception 
     */
    public Map <String,Object> censoColportores(Map <String,Object> params)  throws Exception;
    /**
     * Regresa el listado concentrado por temporadas de un colportor
     * @param params
     * @return
     * @throws Exception 
     */
    public Map <String,Object> concentradoPorTemporadas(Map <String,Object> params)  throws Exception;
}
