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
    public Map <String,Object> censoColportores(Map <String,Object> params)  throws Exception;
}
