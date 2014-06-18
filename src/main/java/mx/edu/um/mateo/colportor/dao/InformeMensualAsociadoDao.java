 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.dao;
import java.util.*;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.InformeMensualAsociado;
import mx.edu.um.mateo.colportor.utils.UltimoException;

/**
 *
 * @author osoto
 */
public interface InformeMensualAsociadoDao {
    /**
     * Regresa una lista de informes mensuales del asociado de acuerdo a los parametros enviados en params
     * @param params
     * @return 
     */
    public Map<String, Object> lista(Map<String, Object> params);
    /**
     * Regresa un informe mensual en base al id
     * @param id
     * @return 
     */
    public InformeMensualAsociado obtiene(Long id);
    /**
     * Regresa el informe mensual en base al id y asociado
     * @param clp
     * @param id
     * @return 
     */
    public InformeMensualAsociado obtiene(Asociado asoc, Long id);
    /**
     * Regresa un informe mensual en base a la fecha
     * @param colportor
     * @param fecha
     * @return 
     */
    public InformeMensualAsociado obtiene(Asociado asoc, Date fecha);
    /**
     * Registra un informe mensual en la base de datos
     * @param informeMensual
     * @return 
     */
    public InformeMensualAsociado crea(InformeMensualAsociado informeMensual);
    
    /**
     * Elimina una informe mensual
     * @param colportor
     * @param id
     * @return
     * @throws UltimoException 
     */
    public String elimina(Asociado asoc, Long id);
    
    
}