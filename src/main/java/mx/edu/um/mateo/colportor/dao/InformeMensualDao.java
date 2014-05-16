 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;
import java.util.*;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.InformeMensual;
import mx.edu.um.mateo.colportor.utils.UltimoException;

/**
 *
 * @author osoto
 */
public interface InformeMensualDao {
    /**
     * Regresa una lista de informes mensuales de colportores de acuerdo a los parametros enviados en params
     * @param params
     * @return 
     */
    public Map<String, Object> lista(Map<String, Object> params);
    /**
     * Regresa un informe mensual en base al id
     * @param id
     * @return 
     */
    public InformeMensual obtiene(Long id);
    /**
     * Regresa el informe mensual en base al id y colportor
     * @param clp
     * @param id
     * @return 
     */
    public InformeMensual obtiene(Colportor clp, Long id);
    /**
     * Regresa un informe mensual en base a la fecha
     * @param colportor
     * @param fecha
     * @return 
     */
    public InformeMensual obtiene(Colportor clp, Date fecha);
    /**
     * Registra un informe mensual en la base de datos
     * @param informeMensual
     * @return 
     */
    public InformeMensual crea(InformeMensual informeMensual);
    
    /**
     * Elimina una informe mensual
     * @param colportor
     * @param id
     * @return
     * @throws UltimoException 
     */
    public String elimina(Colportor clp, Long id);
    
    
}