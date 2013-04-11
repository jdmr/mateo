/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Usuario;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author develop
 */
@Entity
@Table(name = "ALUMNO_INSTITUCION")
public class CobroCampo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @Column(name = "matricula")
    @NotNull
    private String matricula;
    @Column(name = "status")
    private String status;
    @ManyToOne(optional = false)
    private Institucion institucion;
    @Column(nullable = false, length = 8, name = "importe_matricula")
    private Double importeMatricula;
    @Column(nullable = false, length = 8, name = "importe_enzenanza")
    private Double importeEnsenanza;
    @Column(nullable = false, length = 8, name = "importe_internado")
    private Double importeInternado;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha")
    private Date fechaAlta;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaModificacion;
    @ManyToOne
    private Usuario usuarioAlta;
    @ManyToOne
    private Usuario usuarioModificacion;
    @ManyToOne(optional = false)
    private Empresa empresa;

    public CobroCampo() {
    }

    public CobroCampo(String matricula, Institucion institucion, Double importeMatricula,
            Double importeEnsenanza, Double importeInternado) {
        this.matricula = matricula;
        this.institucion = institucion;
        this.importeMatricula = importeMatricula;
        this.importeEnsenanza = importeEnsenanza;
        this.importeInternado = importeInternado;
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

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Institucion getInstitucion() {
        return institucion;
    }

    public void setInstitucion(Institucion institucion) {
        this.institucion = institucion;
    }

    public Double getImporteMatricula() {
        return importeMatricula;
    }

    public void setImporteMatricula(Double importeMatricula) {
        this.importeMatricula = importeMatricula;
    }

    public Double getImporteEnsenanza() {
        return importeEnsenanza;
    }

    public void setImporteEnsenanza(Double importeEnsenanza) {
        this.importeEnsenanza = importeEnsenanza;
    }

    public Double getImporteInternado() {
        return importeInternado;
    }

    public void setImporteInternado(Double importeInternado) {
        this.importeInternado = importeInternado;
    }

    public Date getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(Date fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public Date getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public Usuario getUsuarioAlta() {
        return usuarioAlta;
    }

    public void setUsuarioAlta(Usuario usuarioAlta) {
        this.usuarioAlta = usuarioAlta;
    }

    public Usuario getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(Usuario usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CobroCampos{" + "id=" + id + ", version=" + version + ", matricula=" + matricula + ", institucion=" + institucion
                + ", importeMatricula=" + importeMatricula + ", importeEnsenanza=" + importeEnsenanza
                + ", importeInternado=" + importeInternado + ", fechaAlta=" + fechaAlta + ", fechaModificacion=" + fechaModificacion
                + ", usuarioAlta=" + usuarioAlta + ", usuarioModificacion=" + usuarioModificacion + ", empresa=" + empresa + '}';
    }
}
