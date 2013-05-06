/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;

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
import javax.persistence.TemporalType;
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Usuario;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author zorch
 */
@Entity
@Table(name= "fes_descuentoAlumno")
public class AlumnoDescuento implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;   
    @Column(nullable=false, length=2)
    private String status; 
    @Column(nullable=false, length=1)
    private String contabiliza; 
   @DateTimeFormat(pattern = "dd/MM/yyyy")
   @Temporal(TemporalType.DATE)
    private Date fecha;
   @ManyToOne(optional = false)
   private Usuario usuario;
   @ManyToOne(optional = false)
   private Descuento descuento;
   @Column(length=7)
   private String matricula;
    
   public AlumnoDescuento(){
    }
   
    public AlumnoDescuento(String matricula,Descuento descuento,Date fecha, Usuario usuario, String contabiliza, String status){
        
        this.descuento=descuento;
        this.matricula=matricula;
        this.fecha=fecha;
        this.contabiliza=contabiliza;
        this.status=status;
        this.usuario=usuario;
        
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContabiliza() {
        return contabiliza;
    }

    public void setContabiliza(String contabiliza) {
        this.contabiliza = contabiliza;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Descuento getDescuento() {
        return descuento;
    }

    public void setDescuento(Descuento descuento) {
        this.descuento = descuento;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    @Override
    public String toString() {
        return "AlumnoDescuento{" + "id=" + id + ", version=" + version + ", status=" + status + ", contabiliza=" + contabiliza + ", fecha=" + fecha + ", usuario=" + usuario.getId() + ", descuento=" + descuento + ", matricula=" + matricula + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        hash = 97 * hash + Objects.hashCode(this.version);
        hash = 97 * hash + Objects.hashCode(this.status);
        hash = 97 * hash + Objects.hashCode(this.matricula);
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
        final AlumnoDescuento other = (AlumnoDescuento) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.status, other.status)) {
            return false;
        }
        if (!Objects.equals(this.matricula, other.matricula)) {
            return false;
        }
        return true;
    }
    
}
