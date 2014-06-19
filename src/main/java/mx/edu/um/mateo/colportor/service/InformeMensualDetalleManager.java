/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.service;

import java.util.Map;
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
    public InformeMensualDetalle obtiene(Long id);
    public InformeMensualDetalle crea(InformeMensualDetalle detalle);
    public String elimina(Long id);
    
}
