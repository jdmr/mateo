/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Empresa;

/**
 *
 * @author develop
 */
@Entity
@Table(name = "tiposBecas")
public class TiposBecas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Version
    private Integer version;
    @Column(nullable = false, length = 120)
    private String descripcion;
    @Column( length = 2)
    private String status;
    @Column(nullable = false)
    private Boolean diezma; //true si este tipo de beca debe diezmar
    @Column(nullable = false)
    private Integer numHoras;
    @Column(nullable = false)
    private BigDecimal porcentaje;
    @Column
    private BigDecimal tope;
    @Column
    private Boolean perteneceAlumno; //true si la beca es del alumno, y no de la plaza de un departamento
    @Column
    private Boolean soloPostgrado; //true si la beca es solo para alumnos de postgrado
     @ManyToOne(optional = false)
    private Empresa empresa;

    public TiposBecas() {
    }

    
    public TiposBecas(String descripcion, Boolean diezma ,BigDecimal porcentaje, BigDecimal tope, Boolean perteneceAlumno, Boolean soloPostgrado, Integer numHoras ){
        this.descripcion=descripcion;
        this.diezma=diezma;
        this.porcentaje=porcentaje;
        this.tope=tope;
        this.numHoras=numHoras;
        this.perteneceAlumno=perteneceAlumno;
        this.soloPostgrado=soloPostgrado;
}

   

//    private CentroCosto centroCosto;
//    private CtaMayor ctaMayor;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     *
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     *
     * @return the diezma
     */
    public Boolean getDiezma() {
        return diezma;
    }

    /**
     * @param diezma the diezma to set
     */
    public void setDiezma(Boolean diezma) {
        this.diezma = diezma;
    }

    /**
     *
     * @return the numHoras
     */
    public Integer getNumHoras() {
        return numHoras;
    }

    /**
     * @param numHoras the numHoras to set
     */
    public void setNumHoras(Integer numHoras) {
        this.numHoras = numHoras;
    }

    /**
     * @return the porcentaje
     */
    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    /**
     * @param porcentaje the porcentaje to set
     */
    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }

    /**
     *
     * @return the perteneceAlumno
     */
    public Boolean getPerteneceAlumno() {
        return perteneceAlumno;
    }

    /**
     * @param perteneceAlumno the perteneceAlumno to set
     */
    public void setPerteneceAlumno(Boolean perteneceAlumno) {
        this.perteneceAlumno = perteneceAlumno;
    }

    /**
     * @return the soloPostgrado
     *
     */
    public Boolean getSoloPostgrado() {
        return soloPostgrado;
    }

    /**
     * @param soloPostgrado the soloPostgrado to set
     */
    public void setSoloPostgrado(Boolean soloPostgrado) {
        this.soloPostgrado = soloPostgrado;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.version);
        hash = 97 * hash + Objects.hashCode(this.descripcion);
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
        final TiposBecas other = (TiposBecas) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.descripcion, other.descripcion)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoAFEBeca{" + "id=" + id + ", version=" + version + ", descripcion=" + descripcion + ", diezma=" + diezma + ", numHoras=" + numHoras + ", porcentaje=" + porcentaje + ", perteneceAlumno=" + perteneceAlumno + ", soloPostgrado=" + soloPostgrado + '}';
    }

//    /**
//     * @return the centroCosto
//     */
//    public CentroCosto getCentroCosto() {
//        return centroCosto;
//    }
//
//    /**
//     * @param centroCosto the centroCosto to set
//     */
//    public void setCentroCosto(CentroCosto centroCosto) {
//        this.centroCosto = centroCosto;
//    }
//
//    /**
//     * @return the ctaMayor
//     */
//    public CtaMayor getCtaMayor() {
//        return ctaMayor;
//    }
//
//    /**
//     * @param ctaMayor the ctaMayor to set
//     */
//    public void setCtaMayor(CtaMayor ctaMayor) {
//        this.ctaMayor = ctaMayor;
//    }
    /**
     * @return the tope
     */
    public BigDecimal getTope() {
        return tope;
    }

    /**
     * @param tope the tope to set
     */
    public void setTope(BigDecimal tope) {
        this.tope = tope;
    }

    /**
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
}
