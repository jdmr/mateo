/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author nujev
 */
@Entity
@Table(name = "ejercicio")
public class Ejercicio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @NotBlank
    @Column(nullable =false)
    private String idEjercicio;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 2)
    private String status;
    @NotBlank
    @Column(nullable = false, length = 2)
    private String mascBalance;
    @NotBlank
    @Column(nullable = false, length = 2)
    private String mascResultado;
    @NotBlank
    @Column(nullable = false, length = 2)
    private String mascAuxiliar;
    @NotBlank
    @Column(nullable = false, length = 2)
    private String mascCcosto;
    @NotBlank
    @Column(nullable = false, length = 2)
    private Byte nivelContable;
    @NotBlank
    @Column(nullable = false, length = 2)
    private Byte nivelTauxiliar;
    //private Set ccostos;
    //private Set ctasMayor;
    //private Set auxiliares;
    //private Set libros;

    public Ejercicio() {
    }

    public Ejercicio(String nombre, String status) {
        this.nombre = nombre;
        this.status = status;
    }

    /**
     * @return the idEjercicio
     */
    public String getIdEjercicio() {
        return idEjercicio;
    }

    /**
     * @param idEjercicio the idEjercicio to set
     */
    public void setIdEjercicio(String idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the mascBalance
     */
    public String getMascBalance() {
        return mascBalance;
    }

    /**
     * @param mascBalance the mascBalance to set
     */
    public void setMascBalance(String mascBalance) {
        this.mascBalance = mascBalance;
    }

    /**
     * @return the mascResultado
     */
    public String getMascResultado() {
        return mascResultado;
    }

    /**
     * @param mascResultado the mascResultado to set
     */
    public void setMascResultado(String mascResultado) {
        this.mascResultado = mascResultado;
    }

    /**
     * @return the mascAuxiliar
     */
    public String getMascAuxiliar() {
        return mascAuxiliar;
    }

    /**
     * @param mascAuxiliar the mascAuxiliar to set
     */
    public void setMascAuxiliar(String mascAuxiliar) {
        this.mascAuxiliar = mascAuxiliar;
    }

    /**
     * @return the mascCcosto
     */
    public String getMascCcosto() {
        return mascCcosto;
    }

    /**
     * @param mascCcosto the mascCcosto to set
     */
    public void setMascCcosto(String mascCcosto) {
        this.mascCcosto = mascCcosto;
    }

    /**
     * @return the nivelContable
     */
    public Byte getNivelContable() {
        return nivelContable;
    }

    /**
     * @param nivelContable the nivelContable to set
     */
    public void setNivelContable(Byte nivelContable) {
        this.nivelContable = nivelContable;
    }

    /**
     * @return the nivelTauxiliar
     */
    public Byte getNivelTauxiliar() {
        return nivelTauxiliar;
    }

    /**
     * @param nivelTauxiliar the nivelTauxiliar to set
     */
    public void setNivelTauxiliar(Byte nivelTauxiliar) {
        this.nivelTauxiliar = nivelTauxiliar;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ejercicio other = (Ejercicio) obj;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public String toString() {
        return "Ejercicio{" + "nombre=" + nombre + ", status=" + status + '}';
    }
}
