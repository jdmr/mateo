/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
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
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.general.model.Empresa;

/**
 *
 * @author semdariobarbaamaya
 */
@Entity
@Table(name = "empleado_puesto")
public class EmpleadoPuesto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne(optional = false)
    private Puesto puesto;
    @Column(nullable = false, scale = 2, precision = 6)
    private BigDecimal turno;
    @Column(nullable = false, length = 2)
    private String status;
    @ManyToOne
    private CentroCosto centroCosto;
    @ManyToOne
    private Empresa empresa;
    @ManyToOne
    private Empleado empleado;

    public EmpleadoPuesto() {
        puesto = new Puesto();
        turno = new BigDecimal("0");
        status = new String();
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

    public Puesto getPuesto() {
        return puesto;
    }

    public void setPuesto(Puesto puesto) {
        this.puesto = puesto;
    }

    public BigDecimal getTurno() {
        return turno;
    }

    public void setTurno(BigDecimal turno) {
        this.turno = turno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.id);
        hash = 61 * hash + Objects.hashCode(this.version);
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
        final EmpleadoPuesto other = (EmpleadoPuesto) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        return true;
    }

    public CentroCosto getCentroCosto() {
        return centroCosto;
    }

    public void setCentroCosto(CentroCosto centroCosto) {
        this.centroCosto = centroCosto;
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

    @Override
    public String toString() {
        return "EmpleadoPuesto{" + "id=" + id + ", version=" + version + ", puesto=" + puesto + ", turno=" + turno
                + ", status=" + status + ", centroCosto=" + centroCosto + '}';
    }

}
