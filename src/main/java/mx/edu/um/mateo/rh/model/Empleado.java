/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author develop
 */
@Entity
@Table(name = "empleados")
public class Empleado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @NotBlank
    @Column(nullable = false, length = 10)
    private String clave;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String apPaterno;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String apMaterno;
    @NotBlank
    @Column(nullable = false, length = 1)
    private String genero;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String direccion;
    @NotBlank
    @Column(nullable = false, length = 2)
    private String status;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaNacimiento;
    private String curp;
    private String rfc;
    private String cuenta;
    private String imms;
    private Integer escalafon;
    private Integer turno;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaAlta;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaBaja;
    private BigDecimal experienciaFueraUm;
    private String modalidad;
    private String ife;
    private String rango;
    private Boolean adventista;
    private String padre;
    private String madre;
    private String estadoCivil;
    private String conyuge;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaMatrimonio;
    private Boolean finadoPadre;
    private Boolean finadoMadre;
    private String iglesia;
    private String responsabilidad;

    public Empleado() {
    }

    public Empleado(String clave, String nombre, String apPaterno, String apMaterno,
            String genero, String direccion, String status,
            String curp, String rfc, String cuenta, String imms, Integer scalafon,
            Integer turno, BigDecimal experienciaFueraUm,
            String modalidad, String ife, String rango, Boolean adventista, String padre,
            String madre, String estadoCivil, String conyuge,
            Boolean finadoPadre, Boolean finadoMadre, String iglesia, String responsabilidad,
            Integer escalafon) {
        this.clave = clave;
        this.nombre = nombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.genero = genero;
        this.direccion = direccion;
        this.status = status;
        this.adventista = adventista;
        this.conyuge = conyuge;
        this.cuenta = cuenta;
        this.curp = curp;
        this.direccion = direccion;
        this.escalafon = escalafon;
        this.estadoCivil = estadoCivil;
        this.experienciaFueraUm = experienciaFueraUm;
        this.fechaAlta = new Date();
        this.fechaBaja = new Date();
        this.fechaMatrimonio = new Date();
        this.fechaNacimiento = new Date();
        this.finadoMadre = finadoMadre;
        this.finadoPadre = finadoPadre;
        this.genero = genero;
        this.ife = ife;
        this.iglesia = iglesia;
        this.imms = imms;
        this.madre = madre;
        this.modalidad = modalidad;
        this.padre = padre;
        this.rango = rango;
        this.responsabilidad = responsabilidad;
        this.rfc = rfc;
        this.turno = turno;
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

    @Override
    public String toString() {
        return "Empleado{ id= " + id + ", nombre=" + nombre + ", status=" + status + '}';
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
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        if ((this.clave == null) ? (other.clave != null) : !this.clave.equals(other.clave)) {
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
    
    
}
