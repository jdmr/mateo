/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.utils;

/**
 *
 * @author develop
 */
public class UtilStatus {

    public static String valueStatus(String status) {
        switch (status) {
            case Constantes.STATUS_ACTIVO: {
                return "Activo";
            }
            case Constantes.STATUS_FINALIZADO: {
                return "Finalizado";
            }
            case Constantes.STATUS_AUTORIZADO: {
                return "Autorizado";
            }
        }
        return null;
    }
}
