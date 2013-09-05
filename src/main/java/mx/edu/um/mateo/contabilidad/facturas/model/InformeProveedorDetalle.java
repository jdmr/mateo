/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author develop
 */
@Entity
@Table(name = "informeReporteDetalle")
public class InformeProveedorDetalle implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    //*****En el caso del empleado, estos atributos contienen los datos del proveedor que expidio la factura
    private String RFCProveedor;
    private String folioFactura;
    private String nombreProveedor;
    //**************************
    private BigDecimal subtotal;
    private BigDecimal IVA;
    private BigDecimal total;
    private String status;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaFactura;
    private String pathPDF;
    private String pathXMl;
    private String nombrePDF;
    private String nombreXMl;
    @ManyToOne
    private Contrarecibo contrarecibo;
    @ManyToOne(optional = false)
    private InformeProveedor informeProveedor;
    @ManyToOne(optional = false)
    private Empresa empresa;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioAlta;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioMOdificacion;
    @ManyToOne(fetch = FetchType.LAZY)
    private Usuario usuarioAutRech;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaCaptura;
    @Temporal(TemporalType.DATE)
    private Date fechaAutRech;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;

    public InformeProveedorDetalle() {
    }

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

    public String getRFCProveedor() {
        return RFCProveedor;
    }

    public void setRFCProveedor(String RFCProveedor) {
        this.RFCProveedor = RFCProveedor;
    }

    public String getFolioFactura() {
        return folioFactura;
    }

    public void setFolioFactura(String folioFactura) {
        this.folioFactura = folioFactura;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getIVA() {
        return IVA;
    }

    public void setIVA(BigDecimal IVA) {
        this.IVA = IVA;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public String getPathPDF() {
        return pathPDF;
    }

    public void setPathPDF(String pathPDF) {
        this.pathPDF = pathPDF;
    }

    public String getPathXMl() {
        return pathXMl;
    }

    public void setPathXMl(String pathXMl) {
        this.pathXMl = pathXMl;
    }

    public String getNombrePDF() {
        return nombrePDF;
    }

    public void setNombrePDF(String nombrePDF) {
        this.nombrePDF = nombrePDF;
    }

    public String getNombreXMl() {
        return nombreXMl;
    }

    public void setNombreXMl(String nombreXMl) {
        this.nombreXMl = nombreXMl;
    }

    public InformeProveedor getInformeProveedor() {
        return informeProveedor;
    }

    public void setInformeProveedor(InformeProveedor informeProveedor) {
        this.informeProveedor = informeProveedor;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Contrarecibo getContrarecibo() {
        return contrarecibo;
    }

    public void setContrarecibo(Contrarecibo contrarecibo) {
        this.contrarecibo = contrarecibo;
    }

    public Usuario getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuario usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuario getUsuarioMOdificacion() {
        return usuarioMOdificacion;
    }

    public void setUsuarioMOdificacion(Usuario usuarioMOdificacion) {
        this.usuarioMOdificacion = usuarioMOdificacion;
    }

    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Usuario getUsuarioAutRech() {
        return usuarioAutRech;
    }

    public void setUsuarioAutRech(Usuario usuarioAutRech) {
        this.usuarioAutRech = usuarioAutRech;
    }

    public Date getFechaAutRech() {
        return fechaAutRech;
    }

    public void setFechaAutRech(Date fechaAutRech) {
        this.fechaAutRech = fechaAutRech;
    }

    @Override
    public String toString() {
        return "InformeProveedorDetalle{" + "id=" + id + ", version=" + version + ", RFCProveedor=" + RFCProveedor + ", folioFactura=" + folioFactura
                + ", nombreProveedor=" + nombreProveedor + ", subtotal=" + subtotal + ", IVA=" + IVA + ", total=" + total + ", status=" + status
                + ", fechaFactura=" + fechaFactura + ", pathPDF=" + pathPDF + ", pathXMl=" + pathXMl + ", nombrePDF=" + nombrePDF
                + ", nombreXMl=" + nombreXMl + ", contrarecibo=" + contrarecibo + ", informeProveedor=" + informeProveedor
                + ", empresa=" + empresa + ", usuarioAlta=" + usuarioAlta + ", usuarioMOdificacion=" + usuarioMOdificacion
                + ", usuarioAutRech=" + usuarioAutRech + ", fechaCaptura=" + fechaCaptura + ", fechaAutRech=" + fechaAutRech
                + ", fechaModificacion=" + fechaModificacion + '}';
    }
}
