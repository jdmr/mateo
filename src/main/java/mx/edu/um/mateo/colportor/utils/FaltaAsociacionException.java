/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.utils;

/**
 *
 * @author develop
 */
public class FaltaAsociacionException extends Exception {

    /**
     * Creates a new instance of
     * <code>FaltaAsociacionException</code> without detail message.
     */
    public FaltaAsociacionException() {
    }

    /**
     * Constructs an instance of
     * <code>FaltaAsociacionException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public FaltaAsociacionException(String msg) {
        super(msg);
    }
    public FaltaAsociacionException(String msg, Throwable exception) {
        super(msg, exception);
    }
}
