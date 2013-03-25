/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.utils;

/**
 *
 * @author AMDA
 */
public class ObjectRetrievalFailureException extends Exception {

    /**
     * Creates a new instance of
     * <code>ObjectRetrievalFailureException</code> without detail message.
     */
    public ObjectRetrievalFailureException() {
    }

    /**
     * Constructs an instance of
     * <code>ObjectRetrievalFailureException</code> with the specified detail
     * message.
     *
     * @param msg the detail message.
     */
    public ObjectRetrievalFailureException(String msg) {
        super(msg);
    }

    public ObjectRetrievalFailureException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ObjectRetrievalFailureException(Class clazz, Object id) {
        super("Error en la clase" + clazz + "con el id" + id);
    }
    
    
    
}
