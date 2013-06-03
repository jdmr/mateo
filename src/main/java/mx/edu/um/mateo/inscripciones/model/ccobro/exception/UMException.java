/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.inscripciones.model.ccobro.exception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author osoto
 */
public class UMException extends Exception {
    private String msg;
    private List values;
    
    public UMException () {
        values = new ArrayList();
    }
    
    public UMException (String msg) {
        super(msg);
        this.setMsg(msg);
        values = new ArrayList();
    }
    public UMException (String msg, Throwable cause) {
        super(msg, cause);
    }

    public UMException (String msg, Object ...values) {
        super(msg);
        this.setMsg(msg);
        this.values = Arrays.asList(values);
    }
    
    public UMException (Exception e) {
        super(e);
        this.setMsg(e.getMessage());
        values = new ArrayList();
    }
    
    public UMException (String msg, Exception e) {
        super(msg, e);
        this.setMsg(msg+"\n"+e.getMessage());
        values = new ArrayList();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;

    }

    /**
     * @return the values
     */
    public List getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(List values) {
        this.values = values;
    }

    public void setValue(Object obj) {
        this.values.add(obj);
    }
    
    
}
