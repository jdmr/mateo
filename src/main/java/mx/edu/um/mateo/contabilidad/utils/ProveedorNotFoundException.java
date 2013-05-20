/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.utils;

/**
 *
 * @author osoto
 */
public class ProveedorNotFoundException extends Exception {

    /**
     * Creates a new instance of
     * <code>ProveedorNotFoundException</code> without detail message.
     */
    public ProveedorNotFoundException() {
    }

    /**
     * Constructs an instance of
     * <code>ProveedorNotFoundException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ProveedorNotFoundException(String msg) {
        super(msg);
    }
}
