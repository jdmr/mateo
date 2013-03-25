package mx.edu.um.mateo.inscripciones.model;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Id;

public class Carga {
    
    @Id
    @Column(length=6)
    private String carga;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private Date fechaInicio;
    @Column
    private Date fechaFinal;

    public Carga() {
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    @Override
    public String toString() {
        return "Carga{" + "carga=" + carga + ", nombre=" + nombre + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.carga);
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
        final Carga other = (Carga) obj;
        if (!Objects.equals(this.carga, other.carga)) {
            return false;
        }
        return true;
    }
    
    
    
    
}
