/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;

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
 * @author develop
 */
@Entity
@Table(name = "FES_PAQUETES")
public class Paquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Version
    private Integer version;
    @Column(nullable = false, length = 50)
    private String nombre;
    @Column(nullable = false, length = 250)
    private String descripcion;
    @Column(nullable = false, length = 8)
    private BigDecimal matricula;
    @Column(nullable = false, length = 8)
    private BigDecimal ensenanza;
    @Column(nullable = false, length = 8)
    private BigDecimal internado;
    @Column(nullable = false, length = 1)
    private String acfe;
    @ManyToOne(optional = false)
    private Empresa empresa;

    /**
     *
     */
    public Paquete() {
        this.nombre = "";
        this.descripcion = "";
        this.acfe = "1";
        this.matricula = BigDecimal.ZERO;
        this.ensenanza = BigDecimal.ZERO;
        this.internado = BigDecimal.ZERO;

    }

    public Paquete(Integer id) {
        this.id = id;
    }

    /**
     * @return Returns the descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion The descripcion to set.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * @return Returns the id.
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return Returns the nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre The nombre to set.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return Returns the ensenanza.
     */
    public BigDecimal getEnsenanza() {
        return ensenanza;
    }

    /**
     * @param ensenanza The ensenanza to set.
     */
    public void setEnsenanza(BigDecimal ensenanza) {
        this.ensenanza = ensenanza;
    }

    /**
     * @return Returns the internado.
     */
    public BigDecimal getInternado() {
        return internado;
    }

    /**
     * @param internado The internado to set.
     */
    public void setInternado(BigDecimal internado) {
        this.internado = internado;
    }

    /**
     * @return Returns the matricula.
     */
    public BigDecimal getMatricula() {
        return matricula;
    }

    /**
     * @param matricula The matricula to set.
     */
    public void setMatricula(BigDecimal matricula) {
        this.matricula = matricula;
    }

    /**
     * @return Returns the acfe.
     */
    public String getAcfe() {
        return acfe;
    }

    /**
     * @param acfe The acfe to set.
     */
    public void setAcfe(String acfe) {
        this.acfe = acfe;
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
        hash = 17 * hash + Objects.hashCode(this.id);
        hash = 17 * hash + Objects.hashCode(this.version);
        hash = 17 * hash + Objects.hashCode(this.nombre);
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
        final Paquete other = (Paquete) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Paquete{" + "id=" + id + ", version=" + version + ", nombre=" + nombre + ", descripcion=" + descripcion + ", matricula=" + matricula + ", ensenanza=" + ensenanza + ", internado=" + internado + ", acfe=" + acfe + ", empresa=" + empresa.getId() + '}';
    }
    
    
    
}
