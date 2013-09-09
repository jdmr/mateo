/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.model;

import java.io.Serializable;

/**
 *
 * @author osoto
 */
public class ReporteColportorVO implements Serializable{
    private Colportor colportor;
    private Integer col1;
    private Integer col2;

    public Colportor getColportor() {
        return colportor;
    }

    public void setColportor(Colportor colportor) {
        this.colportor = colportor;
    }

    public Integer getCol1() {
        return col1;
    }

    public void setCol1(Integer col1) {
        this.col1 = col1;
    }

    public Integer getCol2() {
        return col2;
    }

    public void setCol2(Integer col2) {
        this.col2 = col2;
    }
    
    
}
