/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 * @author develop
 */
@XmlRootElement(name = "certificado")
@XmlType(propOrder = {"validezObligaciones", "estatusCertificado", "noCertificado", "fechaFinal", "fechaInicio"})
public class Certificado {

    private String validezObligaciones;
    private String estatusCertificado;
    private String noCertificado;
    private String fechaFinal;
    private String fechaInicio;

    @XmlAttribute
    public String getValidezObligaciones() {
        return validezObligaciones;
    }

    public void setValidezObligaciones(String ValidezObligaciones) {
        this.validezObligaciones = ValidezObligaciones;
    }

    @XmlAttribute
    public String getEstatusCertificado() {
        return estatusCertificado;
    }

    public void setEstatusCertificado(String EstatusCertificado) {
        this.estatusCertificado = EstatusCertificado;
    }

    @XmlAttribute
    public String getNoCertificado() {
        return noCertificado;
    }

    public void setNoCertificado(String noCertificado) {
        this.noCertificado = noCertificado;
    }

    @XmlAttribute
    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String FechaFinal) {
        this.fechaFinal = FechaFinal;
    }

    @XmlAttribute
    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String FechaInicio) {
        this.fechaInicio = FechaInicio;
    }

}
