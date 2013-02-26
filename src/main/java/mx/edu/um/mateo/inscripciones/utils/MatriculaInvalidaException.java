/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.utils;

/**
 *
 * @author zorch
 */
public class MatriculaInvalidaException extends Exception {

    /**
     * Creates a new instance of
     * <code>MatriculaInvalidaException</code> without detail message.
     */
    public MatriculaInvalidaException() {
    }

    /**
     * Constructs an instance of
     * <code>MatriculaInvalidaException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public MatriculaInvalidaException(String msg) {
        super(msg);
    }
}
