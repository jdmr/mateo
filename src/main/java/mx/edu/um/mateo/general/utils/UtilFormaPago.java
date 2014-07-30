/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.utils;

/**
 *
 * @author develop
 */
public class UtilFormaPago {

    public static String valueForma(String forma) {
        switch (forma) {
            case "T": {
                return "Transferencia";
            }
            case "C": {
                return "Cheque";
            }
        }
        return null;
    }

}
