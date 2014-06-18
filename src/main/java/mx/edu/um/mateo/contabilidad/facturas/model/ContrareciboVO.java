/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.model;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author develop
 */
public class ContrareciboVO {

    private Contrarecibo contrarecibo;
    private ProveedorFacturas proveedor;
    private InformeProveedor informeProveedor;
    private InformeProveedorDetalle informeProveedorDetalle;
    private Contrarecibo contrarecibo2;
    private ProveedorFacturas proveedor2;
    private InformeProveedor informeProveedor2;
    private InformeProveedorDetalle informeProveedorDetalle2;

    public ProveedorFacturas getProveedor() {
        return proveedor;
    }

    public void setProveedor(ProveedorFacturas proveedor) {
        this.proveedor = proveedor;
    }

    public InformeProveedor getInformeProveedor() {
        return informeProveedor;
    }

    public void setInformeProveedor(InformeProveedor informeProveedor) {
        this.informeProveedor = informeProveedor;
    }

    public InformeProveedorDetalle getInformeProveedorDetalle() {
        return informeProveedorDetalle;
    }

    public void setInformeProveedorDetalle(InformeProveedorDetalle informeProveedorDetalle) {
        this.informeProveedorDetalle = informeProveedorDetalle;
    }

    public Contrarecibo getContrarecibo() {
        return contrarecibo;
    }

    public void setContrarecibo(Contrarecibo contrarecibo) {
        this.contrarecibo = contrarecibo;
    }

    public Contrarecibo getContrarecibo2() {
        return contrarecibo2;
    }

    public void setContrarecibo2(Contrarecibo contrarecibo2) {
        this.contrarecibo2 = contrarecibo2;
    }

    public ProveedorFacturas getProveedor2() {
        return proveedor2;
    }

    public void setProveedor2(ProveedorFacturas proveedor2) {
        this.proveedor2 = proveedor2;
    }

    public InformeProveedor getInformeProveedor2() {
        return informeProveedor2;
    }

    public void setInformeProveedor2(InformeProveedor informeProveedor2) {
        this.informeProveedor2 = informeProveedor2;
    }

    public InformeProveedorDetalle getInformeProveedorDetalle2() {
        return informeProveedorDetalle2;
    }

    public void setInformeProveedorDetalle2(InformeProveedorDetalle informeProveedorDetalle2) {
        this.informeProveedorDetalle2 = informeProveedorDetalle2;
    }
    
    
}
