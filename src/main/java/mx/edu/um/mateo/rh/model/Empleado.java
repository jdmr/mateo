/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import mx.edu.um.mateo.general.model.Empresa;
import org.hibernate.validator.constraints.Length;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 *
 * @author develop
 */
@Entity
@Table(name = "empleados",
uniqueConstraints = {
    @UniqueConstraint(columnNames = {"clave"})})
public class Empleado implements Serializable, Validator {

    private static final long serialVersionUID = 6001011125338853446L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne
    private Empresa empresa;
    @NotBlank(message = "La clave del empleado es un campo requerido")
    @Size(min = 7, max = 7, message = "La clave del empleado debe contener una longitud de 7 caracteres")
    @Column(nullable = false, length = 7)
    private String clave;
    @NotBlank
    @Column(nullable = false, length = 100)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 100)
    private String apPaterno;
    @NotBlank
    @Column(nullable = false, length = 100)
    private String apMaterno;
    @NotBlank
    @Column(nullable = false, length = 1)
    private String genero;
    @NotBlank
    @Column(nullable = false, length = 200)
    private String direccion;
    @NotBlank
    @Column(nullable = false, length = 2)
    private String status;
    @DateTimeFormat(pattern="dd/MM/yyyy",iso=ISO.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date fechaNacimiento;
    @NotBlank
    @Column(nullable = false, length = 30)
    private String curp;
    @NotBlank
    @Column(nullable = false, length = 15)
    private String rfc;
    @Column(nullable = false, length = 16)
    private String cuenta;
    @NotBlank
    @Column(length = 15)
    private String imms;
    @Digits(integer=3,fraction=0,message="El escalafon debe ser un numero de minimo 2 digitos y maximo 3!")
    @Column(nullable = false)
    private Integer escalafon;
    @Range(min=0, max=100, message="El turno no puede ser mayor al 100%!")
    @Column(nullable = false)
    private Integer turno;
    @DateTimeFormat(pattern="dd/MM/yyyy",iso=ISO.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date fechaAlta;
    @DateTimeFormat(pattern="dd/MM/yyyy",iso=ISO.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date fechaBaja;
    @Column(name = "experiencia_fuera_um")
    private BigDecimal experienciaFueraUm;
    @NotBlank
    @Column(length = 2, nullable = false)
    private String modalidad;
    @Length(max = 15)
    @Column(length = 15)
    private String ife;
    @NotBlank
    @Length(max = 2)
    @Column(length = 2, nullable = false)
    private String rango;
    @Column(nullable = false)
    private Boolean adventista;
    @Length(max = 100)
    @Column(length = 100, nullable = false)
    private String padre;
    @Length(max = 100)
    @Column(length = 100, nullable = false)
    private String madre;
    @Length(max = 2)
    @Column(length = 2, nullable = false)
    private String estadoCivil;
    @Length(max = 100)
    @Column(length = 100)
    private String conyuge;
    @DateTimeFormat(pattern="dd/MM/yyyy",iso=ISO.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date fechaMatrimonio;
    @Column(name = "finado_padre", nullable = false)
    private Boolean finadoPadre;
    @Column(name = "finado_madre", nullable = false)
    private Boolean finadoMadre;
    @Length(max = 100)
    @Column(length = 100, nullable = false)
    private String iglesia;
    @Length(max = 100)
    @Column(length = 100, nullable = false)
    private String responsabilidad;

    public Empleado() {
    }

    public Empleado(String clave, String nombre, String apPaterno,
            String apMaterno, String genero, String direccion, String status,
            String curp, String rfc, String cuenta, String imms,
            Integer escalafon, Integer turno, BigDecimal experienciaFueraUm,
            String modalidad, String ife, String rango, Boolean adventista,
            String padre, String madre, String estadoCivil, String conyuge,
            Boolean finadoPadre, Boolean finadoMadre, String iglesia,
            String responsabilidad) {
        this.clave = clave;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.genero = genero;
        this.fechaNacimiento = new Date();
        this.direccion = direccion;
        this.status = status;
        this.curp = curp;
        this.rfc = rfc;
        this.cuenta = cuenta;
        this.imms = imms;
        this.escalafon = escalafon;
        this.turno = turno;
        this.fechaAlta = new Date();
        this.fechaBaja = new Date();
        this.experienciaFueraUm = experienciaFueraUm;
        this.modalidad = modalidad;
        this.ife = ife;
        this.rango = rango;
        this.adventista = adventista;
        this.padre = padre;
        this.madre = madre;
        this.estadoCivil = estadoCivil;
        this.conyuge = conyuge;
        this.fechaMatrimonio = new Date();
        this.finadoPadre = finadoPadre;
        this.finadoMadre = finadoMadre;
        this.iglesia = iglesia;
        this.responsabilidad = responsabilidad;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Boolean getAdventista() {
        return adventista;
    }

    public void setAdventista(Boolean adventista) {
        this.adventista = adventista;
    }

    public String getConyuge() {
        return conyuge;
    }

    public void setConyuge(String conyuge) {
        this.conyuge = conyuge;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getCurp() {
        return curp;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public Integer getEscalafon() {
        return escalafon;
    }

    public void setEscalafon(Integer escalafon) {
        this.escalafon = escalafon;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public BigDecimal getExperienciaFueraUm() {
        return experienciaFueraUm;
    }

    public void setExperienciaFueraUm(BigDecimal experienciaFueraUm) {
        this.experienciaFueraUm = experienciaFueraUm;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Date getFechaMatrimonio() {
        return fechaMatrimonio;
    }

    public void setFechaMatrimonio(Date fechaMatrimonio) {
        this.fechaMatrimonio = fechaMatrimonio;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Boolean getFinadoMadre() {
        return finadoMadre;
    }

    public void setFinadoMadre(Boolean finadoMadre) {
        this.finadoMadre = finadoMadre;
    }

    public Boolean getFinadoPadre() {
        return finadoPadre;
    }

    public void setFinadoPadre(Boolean finadoPadre) {
        this.finadoPadre = finadoPadre;
    }

    public String getIfe() {
        return ife;
    }

    public void setIfe(String ife) {
        this.ife = ife;
    }

    public String getIglesia() {
        return iglesia;
    }

    public void setIglesia(String iglesia) {
        this.iglesia = iglesia;
    }

    public String getImms() {
        return imms;
    }

    public void setImms(String imms) {
        this.imms = imms;
    }

    public String getMadre() {
        return madre;
    }

    public void setMadre(String madre) {
        this.madre = madre;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getResponsabilidad() {
        return responsabilidad;
    }

    public void setResponsabilidad(String responsabilidad) {
        this.responsabilidad = responsabilidad;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public Integer getTurno() {
        return turno;
    }

    public void setTurno(Integer turno) {
        this.turno = turno;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Empleado{" + "id=" + id + ", version=" + version + ", clave=" + clave + ", nombre=" + nombre + ", "
                + "apPaterno=" + apPaterno + ", apMaterno=" + apMaterno + ", genero=" + genero + ", direccion=" 
                + direccion + ", status=" + status + ", fechaNacimiento=" + fechaNacimiento + ", curp=" + curp 
                + ", rfc=" + rfc + ", cuenta=" + cuenta + ", imms=" + imms + ", escalafon=" + escalafon + ", turno=" 
                + turno + ", fechaAlta=" + fechaAlta + ", fechaBaja=" + fechaBaja + ", experienciaFueraUm=" 
                + experienciaFueraUm + ", modalidad=" + modalidad + ", ife=" + ife + ", rango=" + rango + ", adventista=" 
                + adventista + ", padre=" + padre + ", madre=" + madre + ", estadoCivil=" + estadoCivil + ", conyuge=" 
                + conyuge + ", fechaMatrimonio=" + fechaMatrimonio + ", finadoPadre=" + finadoPadre + ", finadoMadre=" 
                + finadoMadre + ", iglesia=" + iglesia + ", responsabilidad=" + responsabilidad + '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Empleado other = (Empleado) obj;
        if (this.id != other.id
                && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.clave == null) ? (other.clave != null) : !this.clave
                .equals(other.clave)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 13 * hash + (this.version != null ? this.version.hashCode() : 0);
        hash = 13 * hash + (this.clave != null ? this.clave.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean supports(Class<?> type) {
        return Empleado.class.equals(type);
    }

    @Override
    public void validate(Object o, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
        Empleado emp = (Empleado) o;
        if (emp.getEscalafon() < 0) {
            e.rejectValue("escalafon", "negativeValue");
        } else if (emp.getEscalafon() > 200) {
            e.rejectValue("escalafon", "exageratedValue");
        }
    }
}
