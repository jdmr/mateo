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
public class UtilMoneda {

    public static String valueMoneda(String moneda) {
        switch (moneda) {
            case "P": {
                return "Pesos";
            }
            case "D": {
                return "Dolares";
            }
        }
        return null;
    }

}
