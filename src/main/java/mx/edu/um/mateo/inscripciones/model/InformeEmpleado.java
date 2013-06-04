/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author develop
 */
@Entity
@Table(name = "informeEmpleado")
public class InformeEmpleado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    private String NumNomina;
    private String NombreEmpleado;
    private String RFCProveedor;
    private String FolioFactura;
    private String NombreProveedor;
    private BigDecimal subtotal;
    private BigDecimal IVA;
    private BigDecimal Total;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaInforme;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaFactura;
    @ManyToOne(optional = false)
    private Empresa empresa;
    private String pathPDF;
    private String pathXMl;

    public InformeEmpleado() {
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

    public String getNumNomina() {
        return NumNomina;
    }

    public void setNumNomina(String NumNomina) {
        this.NumNomina = NumNomina;
    }

    public String getNombreEmpleado() {
        return NombreEmpleado;
    }

    public void setNombreEmpleado(String NombreEmpleado) {
        this.NombreEmpleado = NombreEmpleado;
    }

    public String getRFCProveedor() {
        return RFCProveedor;
    }

    public void setRFCProveedor(String RFCProveedor) {
        this.RFCProveedor = RFCProveedor;
    }

    public String getFolioFactura() {
        return FolioFactura;
    }

    public void setFolioFactura(String FolioFactura) {
        this.FolioFactura = FolioFactura;
    }

    public String getNombreProveedor() {
        return NombreProveedor;
    }

    public void setNombreProveedor(String NombreProveedor) {
        this.NombreProveedor = NombreProveedor;
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
        return Total;
    }

    public void setTotal(BigDecimal Total) {
        this.Total = Total;
    }

    public Date getFechaInforme() {
        return fechaInforme;
    }

    public void setFechaInforme(Date fechaInforme) {
        this.fechaInforme = fechaInforme;
    }

    public Date getFechaFactura() {
        return fechaFactura;
    }

    public void setFechaFactura(Date fechaFactura) {
        this.fechaFactura = fechaFactura;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
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

    @Override
    public String toString() {
        return "InformeEmpleado{" + "id=" + id + ", version=" + version + ", NumNomina=" + NumNomina
                + ", NombreEmpleado=" + NombreEmpleado + ", RFCProveedor=" + RFCProveedor
                + ", FolioFactura=" + FolioFactura + ", NombreProveedor=" + NombreProveedor
                + ", subtotal=" + subtotal + ", IVA=" + IVA + ", Total=" + Total
                + ", fechaInforme=" + fechaInforme + ", fechaFactura=" + fechaFactura
                + ", empresa=" + empresa + ", pathPDF=" + pathPDF + ", pathXMl=" + pathXMl + '}';
    }
}
