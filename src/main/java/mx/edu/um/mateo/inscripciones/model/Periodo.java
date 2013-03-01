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
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Empresa;

@Entity
@Table(name = "afe_cat_periodo")
public class Periodo implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@Column(nullable = false)
    //private Carga carga;
    @Column(nullable = false, length = 10)
    private String clave;
    @Column(nullable = false, length = 100)
    private String descripcion;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaInicial;
    @Column(nullable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date fechaFinal; 
    @Column()
    private String incluye; //Facultades que afecta este periodo...  Si esta vacio, indica que incluye todas las carreras
    @Column()
    private String excluye; //Facultades que no afecta este periodo...
    @Column(nullable = false, length = 2)
    private String status;
    @Version
    private Integer version;
    @ManyToOne(optional = false)
    private Empresa empresa;
  

    public Periodo() {
    }

    public Periodo(String descripcion, String status, String clave, Date fechaInicial, Date fechaFinal) {
        this.descripcion = descripcion;
        this.status = status;
        this.clave = clave;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Carga getCarga() {
//        return carga;
//    }
//
//    public void setCarga(Carga carga) {
//        this.carga = carga;
//    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getIncluye() {
        return incluye;
    }

    public void setIncluye(String incluye) {
        this.incluye = incluye;
    }

    public String getExcluye() {
        return excluye;
    }

    public void setExcluye(String excluye) {
        this.excluye = excluye;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.clave);
        hash = 53 * hash + Objects.hashCode(this.version);
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
        final Periodo other = (Periodo) obj;
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
        return "Periodo{" + "id=" + id + /*", carga=" + carga +*/ ", clave=" + clave + ", descripcion=" + descripcion + ", fechaInicial=" + fechaInicial + ", fechaFinal=" + fechaFinal + ", status=" + status + ", version=" + version + '}';
    }  
    
}
