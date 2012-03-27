package mx.edu.um.mateo.contabilidad.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.*;
import mx.edu.um.mateo.general.model.Organizacion;
import org.hibernate.validator.constraints.NotBlank;

/**
 *
 * @author nujev
 */
@Entity
@Table(name = "auxiliares")
public class CuentaAuxiliar implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @NotBlank
    @Column(nullable = false, length = 24)
    private String nombre;
    @NotBlank
    @Column(nullable = false, length = 24, name="nombre_fiscal")
    private String nombreFiscal;
    @NotBlank
    @Column(nullable = false, length = 50)
    private String clave;
    @Column(nullable = false)
    private Boolean detalle = false;
    @Column(nullable = false)
    private Boolean aviso = false;
    @Column(nullable = false)
    private Boolean auxiliar = false;
    @Column(nullable = false)
    private Boolean iva = false;
    @Column(nullable = false, scale = 2, precision = 8)
    private BigDecimal porcentajeIva = new BigDecimal("0");
    @ManyToOne
    private Organizacion organizacion;

    public CuentaAuxiliar() {
    }

    public CuentaAuxiliar(String nombre, String nombreFiscal, String clave, Organizacion organizacion) {
        this.nombre = nombre;
        this.nombreFiscal = nombreFiscal;
        this.clave = clave;
        this.organizacion = organizacion;
    }
    
    public CuentaAuxiliar(String nombre, String nombreFiscal, String clave, Boolean detalle, Boolean aviso, Boolean auxiliar, Boolean iva, BigDecimal porcentajeIva) {
        this.nombre = nombre;
        this.nombreFiscal = nombreFiscal;
        this.clave = clave;
        this.detalle = detalle;
        this.aviso = aviso;
        this.auxiliar = auxiliar;
        this.iva = iva;
        this.porcentajeIva = porcentajeIva;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreFiscal() {
        return nombreFiscal;
    }

    public void setNombreFiscal(String nombreFiscal) {
        this.nombreFiscal = nombreFiscal;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Boolean getDetalle() {
        return detalle;
    }

    public void setDetalle(Boolean detalle) {
        this.detalle = detalle;
    }

    public Boolean getAviso() {
        return aviso;
    }

    public void setAviso(Boolean aviso) {
        this.aviso = aviso;
    }

    public Boolean getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(Boolean auxiliar) {
        this.auxiliar = auxiliar;
    }

    public Boolean getIva() {
        return iva;
    }

    public void setIva(Boolean iva) {
        this.iva = iva;
    }

    public BigDecimal getPorcentajeIva() {
        return porcentajeIva;
    }

    public void setPorcentajeIva(BigDecimal porcentajeIva) {
        this.porcentajeIva = porcentajeIva;
    }

    /**
     * @return the organizacion
     */
    public Organizacion getOrganizacion() {
        return organizacion;
    }

    /**
     * @param organizacion the organizacion to set
     */
    public void setOrganizacion(Organizacion organizacion) {
        this.organizacion = organizacion;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.clave);
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
        final CuentaAuxiliar other = (CuentaAuxiliar) obj;
        if (!Objects.equals(this.clave, other.clave)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "CuentaAuxiliar{" + "nombre=" + nombre + ", nombreFiscal=" + nombreFiscal + ", clave=" + clave + ", detalle=" + detalle + ", aviso=" + aviso + ", auxiliar=" + auxiliar + ", iva=" + iva + ", porcentajeIva=" + porcentajeIva + '}';
    }
}
