 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;
import java.util.*;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;
import mx.edu.um.mateo.colportor.utils.UltimoException;

/**
 *
 * @author osoto
 */
public interface InformeMensualDetalleDao {
    /**
     * Regresa una lista de detalles de un informe mensual de colportores de acuerdo a los parametros enviados en params
     * @param params
     * @return 
     */
    public Map<String, Object> lista(Map<String, Object> params);
    /**
     * Regresa un detalle de un informe mensual en base al id
     * @param id
     * @return 
     */
    public InformeMensualDetalle obtiene(Long id);
    /**
     * Registra un detalle de un informe mensual en la base de datos
     * @param informeMensualDetalle
     * @return 
     */
    public InformeMensualDetalle crea(InformeMensualDetalle detalle);
    
    /**
     * Elimina un detalle de un informe mensual
     * @param id
     * @return
     * @throws UltimoException 
     */
    public String elimina(Long id);
    
    
}