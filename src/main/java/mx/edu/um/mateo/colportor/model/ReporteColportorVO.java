/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author osoto
 */
public class ReporteColportorVO implements Serializable{
    private Colportor colportor;
    private Integer col1;
    private Integer col2;
    
    private TemporadaColportor temporadaColportor;
    private BigDecimal acumuladoBoletin;
    private BigDecimal acumuladoDiezmo;

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

    public TemporadaColportor getTemporadaColportor() {
        return temporadaColportor;
    }

    public void setTemporadaColportor(TemporadaColportor temporadaColportor) {
        this.temporadaColportor = temporadaColportor;
    }

    public BigDecimal getAcumuladoBoletin() {
        return acumuladoBoletin;
    }

    public void setAcumuladoBoletin(BigDecimal acumuladoBoletin) {
        this.acumuladoBoletin = acumuladoBoletin;
    }

    public BigDecimal getAcumuladoDiezmo() {
        return acumuladoDiezmo;
    }

    public void setAcumuladoDiezmo(BigDecimal acumuladoDiezmo) {
        this.acumuladoDiezmo = acumuladoDiezmo;
    }

    
}
