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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Empresa;
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
    private String RFCProveedor;
    private String folioFactura;
    private String nombreProveedor;
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
    @ManyToOne(optional = false)
    private InformeProveedor informeProveedor;
    @ManyToOne(optional = false)
    private Empresa empresa;

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

    @Override
    public String toString() {
        return "InformeProveedorDetalle{" + "id=" + id + ", version=" + version + ", RFCProveedor=" + RFCProveedor
                + ", folioFactura=" + folioFactura + ", nombreProveedor=" + nombreProveedor
                + ", subtotal=" + subtotal + ", IVA=" + IVA + ", total=" + total
                + ", status=" + status + ", fechaFactura=" + fechaFactura
                + ", pathPDF=" + pathPDF + ", pathXMl=" + pathXMl
                + ", nombrePDF=" + nombrePDF + ", nombreXMl=" + nombreXMl
                + ", informeProveedor=" + informeProveedor + ", empresa=" + empresa + '}';
    }
}
