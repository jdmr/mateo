/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.rh.model;

import mx.edu.um.mateo.nomina.model.PerDed;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @Entity
 * @Table(name = "empleado_perded")
 * @author osoto
 */
public class EmpleadoPerDed {
    private static final long serialVersionUID = 3866051221691915360L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne(optional = false)
    private Empleado empleado;
    @ManyToOne(optional = false)
    private PerDed perDed;
    @NotEmpty
    @Column(nullable=false)
    private String atributos;
    @NotEmpty
    @Column(nullable=false)
    private String status;
    @NotBlank
    @Column(nullable=false)
    private BigDecimal importe;
    @Column(nullable=false)
    private Boolean porcentaje;
    @NotBlank
    @Column(nullable=false)
    private Integer frecuenciaPago; //Semanal, 1a. Quincena. 2a. Quincena, Mensual - Ver EnumFrecuenciaPago

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

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public PerDed getPerDed() {
        return perDed;
    }

    public void setPerDed(PerDed perDed) {
        this.perDed = perDed;
    }

    public String getAtributos() {
        return atributos;
    }

    public void setAtributos(String atributos) {
        this.atributos = atributos;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    public Boolean isPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(Boolean porcentaje) {
        this.porcentaje = porcentaje;
    }

    public Integer getFrecuenciaPago() {
        return frecuenciaPago;
    }

    public void setFrecuenciaPago(Integer frecuenciaPago) {
        this.frecuenciaPago = frecuenciaPago;
    }
    
    
    
}
