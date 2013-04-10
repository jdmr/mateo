/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;

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
import mx.edu.um.mateo.general.model.Empresa;

/**
 *
 * @author zorch
 */
@Entity
@Table(name="afe_convenio")
public class AFEConvenio implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Integer version;
    @Column
    String matricula;    
    @Column( length = 2)
    private String status;
    private Alumno alumno;
    @ManyToOne(optional=false)
    private Empresa empresa;
    @ManyToOne(optional=false)
    private TiposBecas tipoBeca;
    @Column
    private BigDecimal importe;
    @Column(name="num_horas")
    private Integer numHoras;
    @Column
    private Boolean diezma;
    
    public AFEConvenio(){
    }
    
    public AFEConvenio(String status , Alumno alumno, Empresa empresa, TiposBecas tipoBeca, BigDecimal importe, Integer numHoras, Boolean diezma, String matricula ){
        this.alumno=alumno;
        this.empresa=empresa;
        this.tipoBeca=tipoBeca;
        this.importe=importe;
        this.numHoras=numHoras;
        this.matricula=matricula;
        this.diezma=diezma;
        this.status=status;
    
}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getNumHoras() {
        return numHoras;
    }

    public void setNumHoras(Integer numHoras) {
        this.numHoras = numHoras;
    }

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Alumno getAlumno() {
        return alumno;
    }

    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    public TiposBecas getTipoBeca() {
        return tipoBeca;
    }

    public void setTipoBeca(TiposBecas tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    
    public Boolean getDiezma() {
        return diezma;
    }

    public void setDiezma(Boolean diezma) {
        this.diezma = diezma;
    }

    @Override
    public String toString() {
        return "AFEConvenio{" + "id=" + id + ", version=" + version + ", matricula=" + matricula + ", status=" + status + ", alumno=" + alumno + ", empresa=" + empresa + ", tipoBeca=" + tipoBeca+ ", importe=" + importe + ", numHoras=" + numHoras + ", diezma=" + diezma + '}';
    }

   
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.version);
        hash = 79 * hash + Objects.hashCode(this.status);
        hash = 79 * hash + Objects.hashCode(this.matricula);
        hash = 79 * hash + Objects.hashCode(this.empresa);
        hash = 79 * hash + Objects.hashCode(this.tipoBeca);
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
        final AFEConvenio other = (AFEConvenio) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.matricula, other.matricula)) {
            return false;
        }
        if (!Objects.equals(this.empresa, other.empresa)) {
            return false;
        }
        if (!Objects.equals(this.tipoBeca, other.tipoBeca)) {
            return false;
        }
        
        return true;
    }

   
    
    
}
