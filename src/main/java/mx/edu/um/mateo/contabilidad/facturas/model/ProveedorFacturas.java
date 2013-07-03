/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author develop
 */
@Entity
@DiscriminatorValue("proveedor")
public class ProveedorFacturas extends Usuario {

    private String razonSocial;
    private String rfc;
    private String idFiscal;
    private String CURP;
    private String direccion;
    private String telefono;
    private String tipoTercero;
    private String clabe; //clave interbancaria
    private String banco;
    private String status;
    private String cuentaCheque;

    public ProveedorFacturas() {
    }

    public ProveedorFacturas(String username, String password, String nombre, String apPaterno, String apMaterno, String correo,
            String razonSocial, String rfc, String idFiscal, String CURP, String direccion, String telefono,
            String tipoTercero, String clabe, String banco, String status, String cuentaCheque) {
        super(username, password, nombre, apPaterno, apMaterno, correo);
        this.razonSocial = razonSocial;
        this.rfc = rfc;
        this.idFiscal = idFiscal;
        this.CURP = CURP;
        this.direccion = direccion;
        this.telefono = telefono;
        this.tipoTercero = tipoTercero;
        this.clabe = clabe;
        this.banco = banco;
        this.status = status;
        this.cuentaCheque = cuentaCheque;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getIdFiscal() {
        return idFiscal;
    }

    public void setIdFiscal(String idFiscal) {
        this.idFiscal = idFiscal;
    }

    public String getCURP() {
        return CURP;
    }

    public void setCURP(String CURP) {
        this.CURP = CURP;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getTipoTercero() {
        return tipoTercero;
    }

    public void setTipoTercero(String tipoTercero) {
        this.tipoTercero = tipoTercero;
    }

    public String getClabe() {
        return clabe;
    }

    public void setClabe(String clabe) {
        this.clabe = clabe;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCuentaCheque() {
        return cuentaCheque;
    }

    public void setCuentaCheque(String cuentaCheque) {
        this.cuentaCheque = cuentaCheque;
    }

    @Override
    public String toString() {
        return "ProveedorFacturas{" + "razonSocial=" + razonSocial + ", rfc=" + rfc + ", idFiscal=" + idFiscal + ", CURP=" + CURP
                + ", direccion=" + direccion + ", telefono=" + telefono + ", tipoTercero=" + tipoTercero + ", clabe=" + clabe
                + ", banco=" + banco + ", status=" + status + ", cuentaCheque=" + cuentaCheque + '}';
    }
}
