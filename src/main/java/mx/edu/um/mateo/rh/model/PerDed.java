/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import java.util.Objects;
import javax.management.relation.Role;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.general.model.Empresa;

/**
 *
 * @author semdariobarbaamaya
 */
@Entity
@Table(name = "perdeds")
public class PerDed implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 6, unique = true)
    private String clave;
    @Column(nullable = false, length = 50, unique = true)
    private String nombre;
    @Column(nullable = false, length = 1)
    private String naturaleza;
     /**
     * Este atributo, indica que rol tiene acceso a editar la
     *    percepcion/deduccion.
     * Este atributo sera configurado por RH
     */
    //private Role roleEdit;
    /**
     * Este atributo indica la frecuencia de pago.  Es decir, en que quincena se
     *    ha de calcular su valor y verse reflejado en la nomina del empleado.
     */
    @Column
    private String frecuenciaPago;
    //private CentroCosto contabilidad;
    //private CuentaMayor cuentaMayor;
    //private Concepto concepto;
    @Column
    private String status;
    /**
     * Lista de atributos que configuran la percepcion/deduccion
     */
    
//    private Set <Atributo>atributos;
//    private Set formulas;
    @Column
    private Integer version;
    
     @ManyToOne(optional=false)
    private Empresa empresa;

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

//    public Role getRoleEdit() {
//        return roleEdit;
//    }
//
//    public void setRoleEdit(Role roleEdit) {
//        this.roleEdit = roleEdit;
//    }

    public String getFrecuenciaPago() {
        return frecuenciaPago;
    }

    public void setFrecuenciaPago(String frecuenciaPago) {
        this.frecuenciaPago = frecuenciaPago;
    }

//    public CentroCosto getContabilidad() {
//        return contabilidad;
//    }
//
//    public void setContabilidad(CentroCosto contabilidad) {
//        this.contabilidad = contabilidad;
//    }
//
//    public CuentaMayor getCtaMayor() {
//        return cuentaMayor;
//    }
//
//    public void setCtaMayor(CuentaMayor cuentaMayor) {
//        this.cuentaMayor = cuentaMayor;
//    }
//
//    public Concepto getConcepto() {
//        return concepto;
//    }
//
//    public void setConcepto(Concepto concepto) {
//        this.concepto = concepto;
//    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public Set getAtributos() {
//        return atributos;
//    }
//
//    public void setAtributos(Set atributos) {
//        this.atributos = atributos;
//    }
//
//    public Set getFormulas() {
//        return formulas;
//    }
//
//    public void setFormulas(Set formulas) {
//        this.formulas = formulas;
//    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.clave);
        hash = 17 * hash + Objects.hashCode(this.version);
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
        final PerDed other = (PerDed) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.clave, other.clave)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "PerDed{" + "id=" + id + ", clave=" + clave + ", nombre=" + nombre + ", naturaleza=" + naturaleza + ", frecuenciaPago=" + frecuenciaPago + ", status=" + status + ", version=" + version + '}';
    }
    
}
