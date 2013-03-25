/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.financiero;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author osoto
 */
public enum TipoOperacionCaja {

    UNKNOWN(0, "TODOS"),
    CALCULO_COBRO(1, "CALCULO COBRO"),
    PAGARE(2, "PAGARE"),
    PRORROGA_PAGO(3, "PRORROGA"),
    SALDO_VENCIDO(4, "SALDO VENCIDO"),
    OTRO(5, "OTRO");
    private Integer id;
    private String descripcion;

    TipoOperacionCaja(Integer id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public static TipoOperacionCaja valueOf(Integer id) {
        switch (id) {
            case 1:
                return CALCULO_COBRO;
            case 2:
                return PAGARE;
            case 3:
                return PRORROGA_PAGO;
            case 4:
                return SALDO_VENCIDO;
            case 5:
                return OTRO;
            default:
                return UNKNOWN;
        }
    }

    public static List<TipoOperacionCaja> getTipos() {
        List list = new ArrayList();
        list.add(TipoOperacionCaja.valueOf(0));
        list.add(TipoOperacionCaja.valueOf(1));
        list.add(TipoOperacionCaja.valueOf(2));
        list.add(TipoOperacionCaja.valueOf(3));
        list.add(TipoOperacionCaja.valueOf(4));
        list.add(TipoOperacionCaja.valueOf(5));
        return list;
    }
}
