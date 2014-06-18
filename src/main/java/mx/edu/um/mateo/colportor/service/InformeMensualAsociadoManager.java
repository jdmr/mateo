/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.service;

import java.util.List;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.InformeMensualAsociado;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;

/**
 *
 * @author osoto
 */
public interface InformeMensualAsociadoManager {
    /**
     * Finaliza un informe del asociado, validando que no haya informes duplicados de un mismo mes
     * @param detalles
     * @param asociado
     * @throws Exception 
     */
    public InformeMensualAsociado finalizaInforme(List<InformeMensualDetalle> detalles, Asociado asociado) throws Exception;
}
