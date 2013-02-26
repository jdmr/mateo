/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author zorch
 */
@Entity
@Table(name="alum_academico", schema="enoc")
public class AlumnoAcademico implements Serializable{
    @Id
    private String codigo_personal;
    @Column(name="modalidad_id", nullable=false)
    private Modalidad modalidad;
    @Column(name="tipo_alumno")
    private Integer tipoAlumno;

    public AlumnoAcademico() {
    }
    
    public AlumnoAcademico(Modalidad modalidad, Integer tipoAlumno) {
		this.modalidad = modalidad;
                this.tipoAlumno = tipoAlumno;
		
   }

      public AlumnoAcademico(String codigo_personal, Modalidad modalidad, Integer tipoAlumno) {
                this.codigo_personal=codigo_personal;
		this.modalidad = modalidad;
                this.tipoAlumno = tipoAlumno;
		
   }
    public String getCodigo_personal() {
        return codigo_personal;
    }

    public void setCodigo_personal(String codigo_personal) {
        this.codigo_personal = codigo_personal;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    public void setModalidad(Modalidad modalidad) {
        this.modalidad = modalidad;
    }

    public Integer getTipoAlumno() {
        return tipoAlumno;
    }

    public void setTipoAlumno(Integer tipoAlumno) {
        this.tipoAlumno = tipoAlumno;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.codigo_personal);
        hash = 97 * hash + Objects.hashCode(this.modalidad);
        hash = 97 * hash + Objects.hashCode(this.tipoAlumno);
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
        final AlumnoAcademico other = (AlumnoAcademico) obj;
        if (!Objects.equals(this.codigo_personal, other.codigo_personal)) {
            return false;
        }
        if (!Objects.equals(this.modalidad, other.modalidad)) {
            return false;
        }
        if (!Objects.equals(this.tipoAlumno, other.tipoAlumno)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "AlumnoAcademico{" + "codigo_personal=" + codigo_personal + ", modalidad=" + modalidad + ", tipoAlumno=" + tipoAlumno + '}';
    }
  
}



    