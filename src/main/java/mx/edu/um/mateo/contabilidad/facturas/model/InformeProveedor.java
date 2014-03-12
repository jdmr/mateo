/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.model;

import java.io.Serializable;
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
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.UtilStatus;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author develop
 */
@Entity(name = "informeProveedor")
@Table(name = "informeProveedor")
public class InformeProveedor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    private String status;
    private String cuentaCheque;
    private String clabe;
    private String formaPago;
    private String nombreProveedor;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaInforme;
    @ManyToOne(optional = false)
    private Empresa empresa;
    @ManyToOne(optional = true)
    private ProveedorFacturas proveedorFacturas;
    @ManyToOne(optional = true)
    private Usuario empleado;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaPago;
    private String contraRecibo;
    private String moneda;
    private String contabilidad;
    private String ccp;
    private String banco;
    private String tipoDocumento;

    public InformeProveedor() {
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

    public String getStatus() {
        return status;
    }

    public String getStatusTexto() {

        return UtilStatus.valueStatus(this.status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public Date getFechaInforme() {
        return fechaInforme;
    }

    public void setFechaInforme(Date fechaInforme) {
        this.fechaInforme = fechaInforme;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getContraRecibo() {
        return contraRecibo;
    }

    public void setContraRecibo(String contraRecibo) {
        this.contraRecibo = contraRecibo;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public String getCuentaCheque() {
        return cuentaCheque;
    }

    public void setCuentaCheque(String cuentaCheque) {
        this.cuentaCheque = cuentaCheque;
    }

    public String getClabe() {
        return clabe;
    }

    public void setClabe(String clabe) {
        this.clabe = clabe;
    }

    public ProveedorFacturas getProveedorFacturas() {
        return proveedorFacturas;
    }

    public void setProveedorFacturas(ProveedorFacturas proveedorFacturas) {
        this.proveedorFacturas = proveedorFacturas;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getContabilidad() {
        return contabilidad;
    }

    public void setContabilidad(String contabilidad) {
        this.contabilidad = contabilidad;
    }

    public String getCcp() {
        return ccp;
    }

    public void setCcp(String ccp) {
        this.ccp = ccp;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public Usuario getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Usuario empleado) {
        this.empleado = empleado;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public String toString() {
        return "InformeProveedor{" + "id=" + id + ", version=" + version + ", status=" + status
                + ", cuentaCheque=" + cuentaCheque + ", clabe=" + clabe + ", formaPago=" + formaPago
                + ", nombreProveedor=" + nombreProveedor + ", fechaInforme=" + fechaInforme + ", empresa=" + empresa
                + ", proveedorFacturas=" + proveedorFacturas + ", empleado=" + empleado + ", fechaPago=" + fechaPago
                + ", contraRecibo=" + contraRecibo + ", moneda=" + moneda + ", contabilidad=" + contabilidad
                + ", ccp=" + ccp + ", banco=" + banco + ", tipoDocumento=" + tipoDocumento + '}';
    }

}
