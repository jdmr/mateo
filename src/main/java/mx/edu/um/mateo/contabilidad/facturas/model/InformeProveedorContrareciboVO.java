/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.model;

import java.io.Serializable;

/**
 * Esta clase se creo para generar el reporte de contrarecibo
 * @author osoto
 */
public class InformeProveedorContrareciboVO implements Serializable{
    private Contrarecibo contrarecibo;
    private InformeProveedorDetalle detalle;

    public Contrarecibo getContrarecibo() {
        return contrarecibo;
    }

    public void setContrarecibo(Contrarecibo contrarecibo) {
        this.contrarecibo = contrarecibo;
    }

    public InformeProveedorDetalle getDetalle() {
        return detalle;
    }

    public void setDetalle(InformeProveedorDetalle detalle) {
        this.detalle = detalle;
    }
    
    
}
