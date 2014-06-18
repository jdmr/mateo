/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.model;

import java.util.Date;
import mx.edu.um.mateo.general.model.Usuario;

/**
 * Esta clase se utiliza para pasar los valores a los reportes de ventas 
 * @author osoto
 */
public class PedidoColportorVO {
    private String numPedido;
    private Date fechaPedido;
    private Date fechaEntrega;
    private String formaPago;
    private ClienteColportor cliente;
    private Usuario colportor;
    private PedidoColportorItem item;

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public Date getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public ClienteColportor getCliente() {
        return cliente;
    }

    public void setCliente(ClienteColportor cliente) {
        this.cliente = cliente;
    }

    public Usuario getColportor() {
        return colportor;
    }

    public void setColportor(Usuario colportor) {
        this.colportor = colportor;
    }

    public PedidoColportorItem getItem() {
        return item;
    }

    public void setItem(PedidoColportorItem item) {
        this.item = item;
    }
    
    
}
