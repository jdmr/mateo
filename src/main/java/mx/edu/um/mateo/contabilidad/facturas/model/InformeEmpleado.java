/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.model;

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
@Entity(name = "informeEmpleado")
@Table(name = "informeEmpleado")
public class InformeEmpleado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    private String numNomina;
    private String nombreEmpleado;
    private String status;
    private Boolean informe;
    private Boolean pesos;
    private Boolean dolares;
    private Boolean reembolso;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaInforme;
    @ManyToOne(optional = false)
    private Empresa empresa;

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
        return numNomina;
    }

    public void setNumNomina(String NumNomina) {
        this.numNomina = NumNomina;
    }

    public String getNombreEmpleado() {
        return nombreEmpleado;
    }

    public void setNombreEmpleado(String NombreEmpleado) {
        this.nombreEmpleado = NombreEmpleado;
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

    public Boolean getInforme() {
        return informe;
    }

    public void setInforme(Boolean informe) {
        this.informe = informe;
    }

    public Boolean getPesos() {
        return pesos;
    }

    public void setPesos(Boolean pesos) {
        this.pesos = pesos;
    }

    public Boolean getDolares() {
        return dolares;
    }

    public void setDolares(Boolean dolares) {
        this.dolares = dolares;
    }

    public Boolean getReembolso() {
        return reembolso;
    }

    public void setReembolso(Boolean reembolso) {
        this.reembolso = reembolso;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "InformeEmpleado{" + "id=" + id + ", version=" + version + ", numNomina=" + numNomina
                + ", nombreEmpleado=" + nombreEmpleado + ", status=" + status
                + ", informe=" + informe + ", pesos=" + pesos
                + ", dolares=" + dolares + ", reembolso=" + reembolso
                + ", fechaInforme=" + fechaInforme + ", empresa=" + empresa + '}';
    }
}
