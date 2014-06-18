/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author osoto
 */
@Entity
@Table(name="informe_mensual_asociado", 
        uniqueConstraints = 
         @UniqueConstraint(columnNames = {"asociado_id", "mes_informe"}))
public class InformeMensualAsociado implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Colportor colportor;
    @Min(0)
    @Column(name = "horas_trabajadas",nullable = false)
    private Integer horasTrabajadas;
    @Min(0)
    @Column(name = "libros_vendidos",nullable = false)
    private Integer librosVendidos;
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
    private Integer estudiosBiblicos;
    @Min(0)
    @Column(name = "bautizados",nullable = false)
    private Integer bautizados;
    @Min(0)
    @Column(name = "diezmos",nullable = false)
    private BigDecimal diezmos;
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern=Constantes.DATE_SHORT_HUMAN_PATTERN)
    @Column(name = "mes_informe")
    private Date fechaRegistro;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Asociado asociado;
    @Column(length = 2)
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Colportor getColportor() {
        return colportor;
    }

    public void setColportor(Colportor colportor) {
        this.colportor = colportor;
    }

    public Integer getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(Integer horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public Integer getLibrosVendidos() {
        return librosVendidos;
    }

    public void setLibrosVendidos(Integer librosVendidos) {
        this.librosVendidos = librosVendidos;
    }

    public BigDecimal getTotalPedidos() {
        return totalPedidos;
    }

    public void setTotalPedidos(BigDecimal totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    public BigDecimal getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(BigDecimal totalVentas) {
        this.totalVentas = totalVentas;
    }

    public Integer getLiteraturaGratis() {
        return literaturaGratis;
    }

    public void setLiteraturaGratis(Integer literaturaGratis) {
        this.literaturaGratis = literaturaGratis;
    }

    public Integer getOracionesOfrecidas() {
        return oracionesOfrecidas;
    }

    public void setOracionesOfrecidas(Integer oracionesOfrecidas) {
        this.oracionesOfrecidas = oracionesOfrecidas;
    }

    public Integer getCasasVisitadas() {
        return casasVisitadas;
    }

    public void setCasasVisitadas(Integer casasVisitadas) {
        this.casasVisitadas = casasVisitadas;
    }

    public Integer getEstudiosBiblicos() {
        return estudiosBiblicos;
    }

    public void setEstudiosBiblicos(Integer estudiosBiblicos) {
        this.estudiosBiblicos = estudiosBiblicos;
    }

    public Integer getBautizados() {
        return bautizados;
    }

    public void setBautizados(Integer bautizados) {
        this.bautizados = bautizados;
    }

    public BigDecimal getDiezmos() {
        return diezmos;
    }

    public void setDiezmos(BigDecimal diezmos) {
        this.diezmos = diezmos;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Asociado getAsociado() {
        return asociado;
    }

    public void setAsociado(Asociado asociado) {
        this.asociado = asociado;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
        hash = 11 * hash + Objects.hashCode(this.version);
        hash = 11 * hash + Objects.hashCode(this.asociado.getId());
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
        final InformeMensualAsociado other = (InformeMensualAsociado) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.version, other.version)) {
            return false;
        }
        if (!Objects.equals(this.asociado, other.asociado.getId())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "InformeMensualAsociado{" + "id=" + id + ", version=" + version + ", colportor=" + colportor.getId() + ", horasTrabajadas=" + horasTrabajadas + ", librosVendidos=" + librosVendidos + ", totalPedidos=" + totalPedidos + ", totalVentas=" + totalVentas + ", literaturaGratis=" + literaturaGratis + ", oracionesOfrecidas=" + oracionesOfrecidas + ", casasVisitadas=" + casasVisitadas + ", estudiosBiblicos=" + estudiosBiblicos + ", bautizados=" + bautizados + ", diezmos=" + diezmos + ", fechaRegistro=" + fechaRegistro + ", asociado=" + asociado.getId() + ", status=" + status + '}';
    }
    
    
    
}
