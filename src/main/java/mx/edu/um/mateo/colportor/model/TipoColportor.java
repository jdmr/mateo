
package mx.edu.um.mateo.colportor.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "tipo_colportor")
public class TipoColportor implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version;
    
    @NotEmpty
    @Column(unique = true, nullable = false, length = 100)
    private String tipoColportor;
    @Column(nullable = false, length = 2)
    private String status;

    public TipoColportor() {
    }

    public TipoColportor(String tipoColportor, String status) {
        this.tipoColportor = tipoColportor;
        this.status = status;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the version
     */
    public Long getVersion() {
        return version;
    }

    /**
     * @return the tipoColportor
     */
    public String getTipoColportor() {
        return tipoColportor;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.id);
        hash = 23 * hash + Objects.hashCode(this.version);
        hash = 23 * hash + Objects.hashCode(this.tipoColportor);
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
        final TipoColportor other = (TipoColportor) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.tipoColportor, other.tipoColportor)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "TipoColportor{" + "id=" + id + ", version=" + version + ", tipoColportor=" + tipoColportor + ", status=" + status + '}';
    }
    
    
}