/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author osoto
 */
@Entity
@Table(name = "pedido_colportor")
public class PedidoColportor implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(nullable = false, length = 20, unique = true)
    private String numPedido;
    @ManyToOne
    private ProyectoColportor proyecto;
    @NotBlank
    @Column(nullable = false, length = 120)
    private String lugar;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "fecha_pedido")
    private Date fechaPedido; //Fecha pedido
    @Column(nullable = false, name = "hora_pedido")
    private Integer horaPedido;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "fecha_entrega")
    private Date fechaEntrega;
    @Column(nullable = false, name = "hora_entrega")
    private Integer horaEntrega;
    @NotBlank
    @Column(nullable = false, name = "forma_pago", length = 3)
    private String formaPago;
    @ManyToOne
    private ClienteColportor cliente;
    @NotBlank
    @Column(nullable = false, length = 120)
    private String razonSocial; //razon social, servicio, puesto
    @Column(nullable = true, length = 250)
    private String observaciones;
    @Column(nullable = false, length = 2)
    private String status;
    @ManyToOne
    private Usuario colportor;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getNumPedido() {
        return numPedido;
    }

    public void setNumPedido(String numPedido) {
        this.numPedido = numPedido;
    }

    public ProyectoColportor getProyecto() {
        return proyecto;
    }

    public void setProyecto(ProyectoColportor proyecto) {
        this.proyecto = proyecto;
    }
    
    

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
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

    public Integer getHoraPedido() {
        return horaPedido;
    }

    public void setHoraPedido(Integer horaPedido) {
        this.horaPedido = horaPedido;
    }

    public Integer getHoraEntrega() {
        return horaEntrega;
    }

    public void setHoraEntrega(Integer horaEntrega) {
        this.horaEntrega = horaEntrega;
    }
    
    public String getFormaPagoValue() {
        return FormaPago.valueOf(formaPago).getValue();
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

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Usuario getColportor() {
        return colportor;
    }

    public void setColportor(Usuario colportor) {
        this.colportor = colportor;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.version);
        hash = 89 * hash + Objects.hashCode(this.numPedido);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PedidoColportor other = (PedidoColportor) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.numPedido, other.numPedido)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PedidoColportor{" + "id=" + id + ", version=" + version + ", numPedido=" + numPedido + ", lugar=" + lugar 
                + ", fecha=" + fechaPedido + ", cliente=" + cliente.getId() + ", razonSocial=" + razonSocial + ", observaciones=" + observaciones 
                + ", status=" + status + ", colportor=" + colportor.getId() + '}';
    }
    
    
}
