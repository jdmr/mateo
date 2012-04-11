package mx.edu.um.mateo.contabilidad.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import mx.edu.um.mateo.general.model.Organizacion;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author develop
 */
@Entity
@Table(name = "libro")
public class Libro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(nullable = false, length = 24)
    String nombre;
    @NotBlank
    @Column(nullable = false, length = 3)
    String clave;
    @NotBlank
    @Column(nullable = false, length = 2)
    String status;
    @Column
    int codigo;
    @ManyToOne
    private Organizacion organizacion;

    public Libro() {
    }

    public Libro(String nombre, String clave, String status, int codigo) {
        this.nombre = nombre;
        this.clave = clave;
        this.status = status;
        this.codigo = codigo;

    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public Organizacion getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Libro other = (Libro) obj;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.clave);
        hash = 79 * hash + Objects.hashCode(this.nombre);
        hash = 79 * hash + Objects.hashCode(this.status);
        return hash;
    }

    @Override
    public String toString() {
        return "Libro{nombre=" + nombre + ", clave=" + clave + ", status" + status + '}';
    }
}
