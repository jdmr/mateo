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
    /**
     * Regresa el listado concentrado general por temporadas de todos los colportores
     * @param params
     * @return
     * @throws Exception 
     */
    public Map <String,Object> concentradoGralPorTemporadas(Map <String,Object> params)  throws Exception;
    /**
     * Regresa el listado de TODOS los colportores con sus ventas concentradas en determinada temporada
     * @param params
     * @return
     * @throws Exception 
     */
    public Map<String, Object> concentradoVentas(Map<String, Object> params) throws Exception;
    /**
     * Regresa el listado mensual de oracion
     * @param params
     * @return
     * @throws Exception 
     */
    public Map<String, Object> planMensualOracion(Map<String, Object> params) throws Exception;
    /**
     * Regresa el listado diario de oracion
     * @param params
     * @return
     * @throws Exception 
     */
    public Map<String, Object> planDiarioOracion(Map<String, Object> params) throws Exception;
    /**
     * Regresa el informe mensual del Asociado
     * @param params
     * @return
     * @throws Exception 
     */
    public Map<String, Object> informeMensualAsociado(Map<String, Object> params) throws Exception;
    /**
     * Regresa el concentrado de los informes por asociados de una asociacion
     * @param params
     * @return
     * @throws Exception 
     */
    public Map<String, Object> informeConcentradoAsociadosAsociacion(Map<String, Object> params) throws Exception;
    /**
     * Regresa el concentrado de los informes del colportor de un anno
     * @param params
     * @return
     * @throws Exception 
     */
    public Map<String, Object> concentradoInformesColportor(Map<String, Object> params) throws Exception;
    /**
     * Regresa el concentrado de los informes anuales del colportor 
     * @param params
     * @return
     * @throws Exception 
     */
    public Map<String, Object> concentradoInformesAnualesColportor(Map<String, Object> params) throws Exception;
}
