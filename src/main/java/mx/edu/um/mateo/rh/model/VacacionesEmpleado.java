/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Empresa;

/**
 *
 * @author develop
 */
@Entity
public class VacacionesEmpleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    private String descripcion;
    private String signo;
    private Integer numDias;
    @ManyToOne
    private Empresa empresa;
    @ManyToOne
    private Empleado empleado;

    public VacacionesEmpleado() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getNumDias() {
        return numDias;
    }

    public void setNumDias(Integer numDias) {
        this.numDias = numDias;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public String getSigno() {
        return signo;
    }

    public void setSigno(String signo) {
        this.signo = signo;
    }

    @Override
    public String toString() {
        return "VacacionesEmpleado{" + "id=" + id + ", version=" + version + ", descripcion=" + descripcion + ", signo=" + signo
                + ", numDias=" + numDias + ", empresa=" + empresa + ", empleado=" + empleado + '}';
    }

}
