/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author osoto
 */
@Entity
@Table(name="informe_mensual_detalle")
public class InformeMensualDetalleVO implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private InformeMensual informeMensual;
    
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern=Constantes.DATE_SHORT_HUMAN_PATTERN)
    @Column(name = "dia_informe")
    private Date fecha;
    @Min(0)
    @Column(name = "horas_trabajadas",nullable = false)
    private Double hrsTrabajadas;
    @Min(0)
    @Column(name = "libros_vendidos",nullable = false)
    private Integer literaturaVendida; //y revistas
    @Min(0)
    @Column(name = "total_pedidos",nullable = false)
    private BigDecimal totalPedidos;
    @Min(0)
    @Column(name = "libros_ventas",nullable = false)
    private BigDecimal totalVentas;
    @Min(0)
    @Column(name = "literatura_gratis",nullable = false)
    private Integer literaturaGratis;
    @Min(0)
    @Column(name = "oraciones_ofrecidas",nullable = false)
    private Integer oracionesOfrecidas;
    @Min(0)
    @Column(name = "casas_visitadas",nullable = false)
    private Integer casasVisitadas;
    @Min(0)
    @Column(name = "estudios_biblicos",nullable = false)
    private Integer contactosEstudiosBiblicos;
    @Min(0)
    @Column(name = "bautizados",nullable = false)
    private Integer bautizados;
    @Column(name = "diezmo",nullable = false)
    private BigDecimal diezmo;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuario capturo; //quien capturo
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern=Constantes.DATE_SHORT_HUMAN_PATTERN)
    @Column(name = "fecha_captura")
    private Date fechaCaptura; //fecha captura
    
    //Este dato solo se usa para reportes
    private String mesInforme;

    public InformeMensualDetalleVO() {
        this.informeMensual = new InformeMensual("A");
        this.hrsTrabajadas = 0.0;
        this.literaturaVendida = 0;
        this.totalPedidos = BigDecimal.ZERO;
        this.totalVentas = BigDecimal.ZERO;
        this.literaturaGratis = 0;
        this.oracionesOfrecidas = 0;
        this.casasVisitadas = 0;
        this.contactosEstudiosBiblicos = 0;
        this.bautizados = 0;
        this.diezmo = BigDecimal.ZERO;
        this.fechaCaptura = new Date();
    }
    
    public InformeMensualDetalleVO(InformeMensual informe, Date fecha, Double hrsTrabajadas, Integer librosRegalados, BigDecimal totalPedidos, BigDecimal totalVentas, Integer literaturaGratis, Integer oracionesOfrecidas, Integer casasVisitadas, Integer contactosEstudiosBiblicos, Integer bautizados, BigDecimal diezmo, Usuario capturo, Date cuando) {
        this.informeMensual = informe;
        this.fecha = fecha;
        this.hrsTrabajadas = hrsTrabajadas;
        this.literaturaVendida = librosRegalados;
        this.totalPedidos = totalPedidos;
        this.totalVentas = totalVentas;
        this.literaturaGratis = literaturaGratis;
        this.oracionesOfrecidas = oracionesOfrecidas;
        this.casasVisitadas = casasVisitadas;
        this.contactosEstudiosBiblicos = contactosEstudiosBiblicos;
        this.bautizados = bautizados;
        this.diezmo = diezmo;
        this.capturo = capturo;
        this.fechaCaptura = cuando;
    }
    
    

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    /**
     * @return the informeMensual
     */
    public InformeMensual getInformeMensual() {
        return informeMensual;
    }

    /**
     * @param informeMensual the informeMensual to set
     */
    public void setInformeMensual(InformeMensual informeMensual) {
        this.informeMensual = informeMensual;
    }

    /**
     * @return the fecha
     */
    public Date getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the hrsTrabajadas
     */
    public Double getHrsTrabajadas() {
        return hrsTrabajadas;
    }

    /**
     * @param hrsTrabajadas the hrsTrabajadas to set
     */
    public void setHrsTrabajadas(Double hrsTrabajadas) {
        this.hrsTrabajadas = hrsTrabajadas;
    }

    /**
     * @return the literaturaVendida
     */
    public Integer getLiteraturaVendida() {
        return literaturaVendida;
    }

    /**
     * @param literaturaVendida the literaturaVendida to set
     */
    public void setLiteraturaVendida(Integer librosRegalados) {
        this.literaturaVendida = librosRegalados;
    }

    /**
     * @return the totalPedidos
     */
    public BigDecimal getTotalPedidos() {
        return totalPedidos;
    }

    /**
     * @param totalPedidos the totalPedidos to set
     */
    public void setTotalPedidos(BigDecimal totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    /**
     * @return the totalVentas
     */
    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    /**
     * @param totalVentas the totalVentas to set
     */
    public void setTotalVentas(BigDecimal totalVentas) {
        this.totalVentas = totalVentas;
    }

    /**
     * @return the literaturaGratis
     */
    public Integer getLiteraturaGratis() {
        return literaturaGratis;
    }

    /**
     * @param literaturaGratis the literaturaGratis to set
     */
    public void setLiteraturaGratis(Integer literaturaGratis) {
        this.literaturaGratis = literaturaGratis;
    }

    /**
     * @return the oracionesOfrecidas
     */
    public Integer getOracionesOfrecidas() {
        return oracionesOfrecidas;
    }

    /**
     * @param oracionesOfrecidas the oracionesOfrecidas to set
     */
    public void setOracionesOfrecidas(Integer oracionesOfrecidas) {
        this.oracionesOfrecidas = oracionesOfrecidas;
    }

    /**
     * @return the casasVisitadas
     */
    public Integer getCasasVisitadas() {
        return casasVisitadas;
    }

    /**
     * @param casasVisitadas the casasVisitadas to set
     */
    public void setCasasVisitadas(Integer casasVisitadas) {
        this.casasVisitadas = casasVisitadas;
    }

    /**
     * @return the contactosEstudiosBiblicos
     */
    public Integer getContactosEstudiosBiblicos() {
        return contactosEstudiosBiblicos;
    }

    /**
     * @param contactosEstudiosBiblicos the contactosEstudiosBiblicos to set
     */
    public void setContactosEstudiosBiblicos(Integer contactosEstudiosBiblicos) {
        this.contactosEstudiosBiblicos = contactosEstudiosBiblicos;
    }

    /**
     * @return the bautizados
     */
    public Integer getBautizados() {
        return bautizados;
    }

    /**
     * @param bautizados the bautizados to set
     */
    public void setBautizados(Integer bautizados) {
        this.bautizados = bautizados;
    }

    public BigDecimal getDiezmo() {
        return diezmo;
    }

    public void setDiezmo(BigDecimal diezmo) {
        this.diezmo = diezmo;
    }
    
    

    /**
     * @return the capturo
     */
    public Usuario getCapturo() {
        return capturo;
    }

    /**
     * @param capturo the capturo to set
     */
    public void setCapturo(Usuario capturo) {
        this.capturo = capturo;
    }

    /**
     * @return the fechaCaptura
     */
    public Date getFechaCaptura() {
        return fechaCaptura;
    }

    /**
     * @param fechaCaptura the fechaCaptura to set
     */
    public void setFechaCaptura(Date fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public String getMesInforme() {
        return mesInforme;
    }

    public void setMesInforme(String mesInforme) {
        this.mesInforme = mesInforme;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 73 * hash + Objects.hashCode(this.id);
        hash = 73 * hash + Objects.hashCode(this.version);
        hash = 73 * hash + Objects.hashCode(this.informeMensual);
        hash = 73 * hash + Objects.hashCode(this.fecha);
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
        final InformeMensualDetalleVO other = (InformeMensualDetalleVO) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.informeMensual.getId(), other.informeMensual.getId())) {
            return false;
        }
        if (!Objects.equals(this.fecha, other.fecha)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InformeMensualDetalle{" + ", fecha=" + fecha + ", hrsTrabajadas=" + hrsTrabajadas + ", literaturaVendida=" + literaturaVendida + ", totalPedidos=" + totalPedidos + ", totalVentas=" + totalVentas + ", literaturaGratis=" + literaturaGratis + ", oracionesOfrecidas=" + oracionesOfrecidas + ", casasVisitadas=" + casasVisitadas + ", contactosEstudiosBiblicos=" + contactosEstudiosBiblicos + ", bautizados=" + bautizados + ", diezmo=" + diezmo + "}";
    }

    
    
}
