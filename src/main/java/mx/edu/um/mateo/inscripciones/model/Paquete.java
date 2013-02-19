/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
    @Column(nullable = false, length = 50)
    private String nombre;
    @Column(nullable = false, length = 250)
    private String descripcion;
    @Column(nullable = false, length = 8)
    private Double matricula;
    @Column(nullable = false, length = 8)
    private Double ensenanza;
    @Column(nullable = false, length = 8)
    private Double internado;
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
        this.matricula = new Double(0);
        this.ensenanza = new Double(0);
        this.internado = new Double(0);

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
    public Double getEnsenanza() {
        return ensenanza;
    }

    /**
     * @param ensenanza The ensenanza to set.
     */
    public void setEnsenanza(Double ensenanza) {
        this.ensenanza = ensenanza;
    }

    /**
     * @return Returns the internado.
     */
    public Double getInternado() {
        return internado;
    }

    /**
     * @param internado The internado to set.
     */
    public void setInternado(Double internado) {
        this.internado = internado;
    }

    /**
     * @return Returns the matricula.
     */
    public Double getMatricula() {
        return matricula;
    }

    /**
     * @param matricula The matricula to set.
     */
    public void setMatricula(Double matricula) {
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
    
}
