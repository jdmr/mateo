/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.service;

import java.util.Map;
import mx.edu.um.mateo.colportor.model.InformeMensual;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;

/**
 *
 * @author osoto
 */
public interface InformeMensualDetalleManager {
    /**
     * Regresa la lista de informes de colportor, ya totalizados por semana
     * @param params
     * @return 
     */
    public Map<String, Object> lista(Map<String, Object> params);
    /**
     * Regresa los detalles del informe
     * @param id
     * @return 
     */
    public InformeMensualDetalle obtiene(Long id);
    /**
     * Regresa los detalles del informe
     * @param informe
     * @param id
     * @return 
     */
    public InformeMensualDetalle obtiene(InformeMensual informe,Long id);
    /**
     * Crea un nuevo detalle de informe
     * @param detalle
     * @return 
     */
    public InformeMensualDetalle crea(InformeMensualDetalle detalle);
    /**
     * Elimina un detalle del informe
     * @param id
     * @return 
     */
    public String elimina(Long id);
    
}
