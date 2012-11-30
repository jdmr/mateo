/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 *
 * @author osoto@um.edu.mx
 */
@Entity
@DiscriminatorValue("empleado")
public class Empleado extends Usuario {

    private static final long serialVersionUID = 6001011125338853446L;
    @NotBlank
    @Size(min = 7, max = 7, message = "La clave del empleado debe contener una longitud de 7 caracteres")
    @Column(length = 7)
    private String clave;
    @NotBlank
    @Column( length = 1)
    private String genero;
    @NotBlank
    @Column( length = 200)
    private String direccion;
    @Column(length = 2)
    private String status;
    @DateTimeFormat(pattern="dd/MM/yyyy",iso=ISO.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date fechaNacimiento;
    @NotBlank
    @Column( length = 30)
    private String curp;
    @NotBlank
    @Column( length = 15)
    private String rfc;
    @Column(length = 16)
    private String cuenta;
    @NotBlank
    @Column(length = 15)
    private String imms;
    @Digits(integer=3,fraction=0,message="El escalafon debe ser un numero de minimo 2 digitos y maximo 3!")
    @Column
    private Integer escalafon;
    @Range(min=0, max=100, message="El turno no puede ser mayor al 100%!")
    @Column
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
    @Column(length = 2)
    private String modalidad;
    @Length(max = 15)
    @Column(length = 15)
    private String ife;
    @NotBlank
    @Length(max = 2)
    @Column(length = 2)
    private String rango;
    @Column
    private Boolean adventista;
    @Length(max = 100)
    @Column(length = 100)
    private String padre;
    @Length(max = 100)
    @Column(length = 100)
    private String madre;
    @Length(max = 2)
    @Column(length = 2)
    private String estadoCivil;
    @Length(max = 100)
    @Column(length = 100)
    private String conyuge;
    @DateTimeFormat(pattern="dd/MM/yyyy",iso=ISO.DATE)
    @Temporal(TemporalType.TIMESTAMP)
    @Column
    private Date fechaMatrimonio;
    @Column(name = "finado_padre")
    private Boolean finadoPadre;
    @Column(name = "finado_madre")
    private Boolean finadoMadre;
    @Length(max = 100)
    @Column(length = 100)
    private String iglesia;
    @Length(max = 100)
    @Column(length = 100)
    private String responsabilidad;    
    @ManyToOne
    private TipoEmpleado tipoEmpleado;

    public Empleado() {
    }

    public Empleado(String nombre,String apPaterno, String apMaterno,String correo, String username, String clave, 
            Boolean adventista,String genero, String direccion, String status,
            String curp, String rfc, String cuenta, String imms, 
            Integer escalafon, Integer turno, BigDecimal experienciaFueraUm,
            String modalidad, String ife, String rango,
            String padre, String madre, String estadoCivil, String conyuge,
            Boolean finadoPadre, Boolean finadoMadre, String iglesia,
            String responsabilidad, String password, TipoEmpleado tipoEmpleado) {
        super(nombre, apPaterno, apMaterno); 
        this.clave = clave;
        this.correo=correo;
        this.username=correo;
        this.password=password;
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
        this.tipoEmpleado = tipoEmpleado;
        
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the tipoEmpleado
     */
    public TipoEmpleado getTipoEmpleado() {
        return tipoEmpleado;
    }

    /**
     * @param tipoEmpleado the tipoEmpleado to set
     */
    public void setTipoEmpleado(TipoEmpleado tipoEmpleado) {
        this.tipoEmpleado = tipoEmpleado;
    }

    @Override
    public String toString() {
        return "Empleado{" + ", clave=" + clave + ", nombre = "+ this.getNombreCompleto() +", genero=" + genero + ", direccion=" 
                + direccion + ", status=" + status + ", fechaNacimiento=" + fechaNacimiento + ", curp=" + curp 
                + ", rfc=" + rfc + ", cuenta=" + cuenta + ", imms=" + imms + ", escalafon=" + escalafon + ", turno=" 
                + turno + ", fechaAlta=" + fechaAlta + ", fechaBaja=" + fechaBaja + ", experienciaFueraUm=" 
                + experienciaFueraUm + ", modalidad=" + modalidad + ", ife=" + ife + ", rango=" + rango + ", adventista=" 
                + adventista + ", padre=" + padre + ", madre=" + madre + ", estadoCivil=" + estadoCivil + ", conyuge=" 
                + conyuge + ", fechaMatrimonio=" + fechaMatrimonio + ", finadoPadre=" + finadoPadre + ", finadoMadre=" 
                + finadoMadre + ", iglesia=" + iglesia + ", responsabilidad=" + responsabilidad + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.clave);
        hash = 59 * hash + Objects.hashCode(this.getNombreCompleto());
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
        final Empleado other = (Empleado) obj;
        if (!Objects.equals(this.clave, other.clave)) {
            return false;
        }
        if (!Objects.equals(this.curp, other.getNombreCompleto())) {
            return false;
        }
        return true;
    }
    

}
