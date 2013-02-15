/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;

/**
 *
 * @author zorch
 */
@Entity
@Table(name="alum_personal", schema="enoc")


public class Alumno implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne(optional=false)
    private AlumnoAcademico academico;
    @Column(name="bautizado")
    protected String bautizado;
    @Column(name="telefono", length=30)    
    protected String telefono;
    @Column(name="email", length=50)
    protected String email;
    @Column(name="codigo_personal", length=7)
    protected String matricula;
    @Column(nullable=false, length=40, insertable=false, updatable=false)
    protected String nombre;
    @Column(name="apellido_paterno", length=40, nullable=false, insertable=false, updatable=false)
    protected String apPaterno;
    @Column(name="apellido_materno", length=40, nullable=false, insertable=false, updatable=false)
    protected String apMaterno;
    @Column(name="estado_civil", length=1, insertable=false, updatable=false)
    protected String estadoCivil;
    @Column(name="sexo", nullable=false, insertable=false, updatable=false)
    private String genero;
    @Column(name="f_nacimiento", length=7, insertable=false, updatable=false)
    @Temporal(javax.persistence.TemporalType.DATE)
    protected Date fNacimiento;

    public Alumno() {
    }
   
    
   public Alumno(String matricula, String email, String nombre, String apPaterno, String apMaterno,
			AlumnoAcademico academico) {
		this.matricula = matricula;
                this.email = email;
		this.nombre = nombre;
		this.academico = academico;
                this.apMaterno = apPaterno;
		this.apPaterno = apMaterno;
		
		
   }
   public Alumno(String matricula, String nombre, String apPaterno, String apMaterno,
			AlumnoAcademico academico) {
		this.matricula = matricula;
		this.nombre = nombre;
		this.apMaterno = apPaterno;
		this.apPaterno = apMaterno;
		this.academico = academico;
		
   }
    
    public AlumnoAcademico getAcademico() {
        return academico;
    }

    public void setAcademico(AlumnoAcademico academico) {
        this.academico = academico;
    }



    public String getBautizado() {
        return bautizado;
    }

    public void setBautizado(String bautizado) {
        this.bautizado = bautizado;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Date getfNacimiento() {
        return fNacimiento;
    }

    public void setfNacimiento(Date fNacimiento) {
        this.fNacimiento = fNacimiento;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.id);
        hash = 83 * hash + Objects.hashCode(this.version);
        hash = 83 * hash + Objects.hashCode(this.matricula);
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
        final Alumno other = (Alumno) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.matricula, other.matricula)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Alumno{" + "id=" + id + ", version=" + version + ", academico=" + academico + ", bautizado=" + bautizado + ", telefono=" + telefono + ", email=" + email + ", matricula=" + matricula + ", nombre=" + nombre + ", apPaterno=" + apPaterno + ", apMaterno=" + apMaterno + ", estadoCivil=" + estadoCivil + ", genero=" + genero + ", fNacimiento=" + fNacimiento + '}';
    }
    
    
    
}
