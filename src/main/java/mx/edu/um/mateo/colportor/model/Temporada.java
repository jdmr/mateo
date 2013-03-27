/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.*;

/**
 *
 * @author gibrandemetrioo
 */
@Entity
@Table(name="temporadas")
public class Temporada implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(length = 64)
    private String nombre;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, name = "fecha_inicio")
    private Date fechaInicio;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, name = "fecha_final")
    private Date fechaFinal;
    @ManyToOne(optional = false)
    private Asociacion asociacion;

    
    public Temporada(){
        
    }
    public Temporada(String nombre){
        this.nombre = nombre;
        Date fecha = new Date();
        this.fechaInicio = fecha;
        this.fechaFinal = fecha;
                
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public Asociacion getAsociacion() {
        return asociacion;
    }

    public void setAsociacion(Asociacion asociacion) {
        this.asociacion = asociacion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Temporada other = (Temporada) obj;
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public String toString() {
        return "Temporada{" + "id=" + id + ", version=" + version + ", nombre=" + nombre + ", fechaInicio=" + fechaInicio + ", fechaFinal=" + fechaFinal + ", asociacion=" + asociacion + '}';
    }

    
    
}
