/*
 * Created on Jun 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package mx.edu.um.mateo.inscripciones.model.ccobro;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import mx.edu.um.mateo.inscripciones.model.ccobro.comparators.NoCompara;
import mx.edu.um.mateo.inscripciones.model.ccobro.cuenta.Auxiliar;
import mx.edu.um.mateo.inscripciones.model.ccobro.cuenta.CCosto;
import mx.edu.um.mateo.inscripciones.model.ccobro.cuenta.CtaMayor;
import mx.edu.um.mateo.inscripciones.model.ccobro.cuenta.Ejercicio;
import mx.edu.um.mateo.inscripciones.model.ccobro.cuenta.Relacion;
import mx.edu.um.mateo.inscripciones.model.ccobro.poliza.Metodos1;
import mx.edu.um.mateo.inscripciones.model.ccobro.dinscribir.Carga;
import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;
import mx.edu.um.mateo.inscripciones.model.ccobro.Movimiento;
import mx.edu.um.mateo.inscripciones.model.ccobro.comparators.NoCompara;
import mx.edu.um.mateo.inscripciones.model.ccobro.Pagare;
import mx.edu.um.mateo.inscripciones.model.ccobro.cuenta.Relacion;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Alumno implements Constant {
    private Integer id;
    private String matricula;
    private String carga_id;
    private Integer bloque;
    private String nombre;
    private String id_ccosto;
    private String idCtaMayor;
    private Integer modalidad_id;
    private String modalidad;
    private Integer tAlumno_id;
    private String tAlumno;
    private Integer semestre;
    private String fecha;
    private String formaPago;
    private String religion;
    private String nacionalidad;
    private String residencia;
    private String facultad_id;
    private String facultad;
    private String carrera_id;
    private String carrera;
    private String plan_id;
    private String nombre_plan;
    private Integer grado;
    private String hoStatus;
    private Double factorInternado;
    private Double matriculaImporte;
    private Double ensenanzaImporte;
    private Double internadoImporte;
    private Double pagoMinimoMatricula;
    private Double pagoMinimoEnsenanza;
    private Double pagoMinimoInternado;
    private Double manejoPagare;
    private Double descuento_Beca_E;
    private Double descuento_Beca_I;
    private Double descuento_Beca_M;
    private Boolean inscrito;
    private String institucion;
    private Integer idInstitucion;
    private Double semanasInternado;
    private String folio;
    private Boolean graduando;
    private Connection conn;
    private Connection conn_noe;
    private Connection conn_enoc;
    private Connection conn_mateo;
    
    private Integer nivel_id;
    
    Locale local = new java.util.Locale("es", "MX", "Traditional_WIN");
    SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy", local);
    DecimalFormat df = (DecimalFormat)NumberFormat.getCurrencyInstance (local);
    private Logger log = LoggerFactory.getLogger(this.getClass());
    
    /**
     *
     */
    public Alumno() {
        super();
        
        this.fecha = sdFormat.format(new Date());
        
        this.id = null;
        this.matricula = null;
        this.carga_id = null;
        this.bloque = new Integer(0);
        this.nombre = null;
        this.id_ccosto = null;
        this.idCtaMayor = null;
        this.modalidad_id = new Integer(0);
        this.modalidad = null;
        this.tAlumno_id = new Integer(0);
        this.tAlumno = null;
        this.semestre = new Integer(0);
        
        this.formaPago = null;
        this.religion = null;
        this.nacionalidad = null;
        this.residencia = null;
        this.facultad_id = null;
        this.facultad = null;
        this.carrera_id = null;
        this.carrera = null;
        this.plan_id = null;
        this.nombre_plan = null;
        this.grado = new Integer(0);
        this.nivel_id = 0;
        
        this.hoStatus = null;
        
        this.factorInternado = new Double(0);
        this.matriculaImporte = new Double(0);
        this.ensenanzaImporte = new Double(0);
        this.internadoImporte = new Double(0);
        this.pagoMinimoMatricula = new Double(0);
        this.pagoMinimoEnsenanza = new Double(0);
        this.pagoMinimoInternado = new Double(0);
        this.manejoPagare = new Double(0);
        this.descuento_Beca_E = new Double(0);
        this.descuento_Beca_I = new Double(0);
        this.descuento_Beca_M = new Double(0);
        this.inscrito = new Boolean(false);
        this.graduando = new Boolean(false);
        this.semanasInternado = new Double(0);
        this.folio = "";
    }
    
    /**
     * @param matricula
     */
    public Alumno(String matricula) {
        super();
        this.matricula = matricula;
    }
    /**
     * 
     * @param matricula
     * @param carga_id
     * @param bloque
     * @param nombre
     * @param id_ccosto
     * @param modalidad_id
     * @param modalidad
     * @param tAlumno_id
     * @param tAlumno
     * @param semestre
     * @param fecha
     * @param formaPago
     * @param religion
     * @param nacionalidad
     * @param residencia
     * @param facultad_id
     * @param facultad
     * @param carrera_id
     * @param carrera
     * @param plan_id
     * @param nombre_plan
     * @param grado
     * @param hoStatus
     * @param inscrito
     * @param institucion
     * @param semanasInternado
     * @param folio
     * @param factorInternado
     */
    public Alumno(String matricula, String carga_id, Integer bloque,
            String nombre, String id_ccosto, Integer modalidad_id,
            String modalidad, Integer tAlumno_id, String tAlumno, Integer semestre,
            String fecha, String formaPago, String religion, String nacionalidad,
            String residencia, String facultad_id, String facultad,
            String carrera_id, String carrera, String plan_id, String nombre_plan,
            Integer grado, String hoStatus, Boolean inscrito, String institucion, Double semanasInternado,
            String folio, Double factorInternado, Boolean graduando) {
        super();
        this.matricula = matricula;
        this.carga_id = carga_id;
        this.bloque = bloque;
        this.nombre = nombre;
        this.id_ccosto = id_ccosto;
        this.modalidad_id = modalidad_id;
        this.modalidad = modalidad;
        this.tAlumno_id = tAlumno_id;
        this.tAlumno = tAlumno;
        this.semestre = semestre;
        this.fecha = fecha;
        this.formaPago = formaPago;
        this.religion = religion;
        this.nacionalidad = nacionalidad;
        this.residencia = residencia;
        this.facultad_id = facultad_id;
        this.facultad = facultad;
        this.carrera_id = carrera_id;
        this.carrera = carrera;
        this.plan_id = plan_id;
        this.nombre_plan = nombre_plan;
        this.grado = grado;
        this.hoStatus = hoStatus;
        this.inscrito = inscrito;
        this.institucion = institucion;
        this.semanasInternado = semanasInternado;
        this.folio = folio;
        this.factorInternado = factorInternado;
        this.graduando = graduando;
    }
    
    
    /**
     * @param matricula
     * @param nombre
     * @param modalidad_id
     * @param alumno_id
     * @param religion
     * @param nacionalidad
     * @param residencia
     */
    public Alumno(String matricula, String nombre, Integer modalidad_id,
            Integer tAlumno_id, String tAlumno, String religion, String nacionalidad,
            String residencia) {
        super();
        this.matricula = matricula;
        this.nombre = nombre;
        this.modalidad_id = modalidad_id;
        this.tAlumno_id = tAlumno_id;
        this.tAlumno = tAlumno;
        this.religion = religion;
        this.nacionalidad = nacionalidad;
        this.residencia = residencia;
    }
    public Integer getId(){
        return this.id;
    }
    public void setId(Integer id){
        this.id = id;
    }
    /**
     * @return Returns the idCtaMayor.
     */
    public String getIdCtaMayor() {
        return idCtaMayor;
    }
    
    /**
     * @param idCtaMayor The idCtaMayor to set.
     */
    public void setIdCtaMayor(String idCtaMayor) {
        this.idCtaMayor = idCtaMayor;
    }
    
    /**
     * @return Returns the bloque.
     */
    public Integer getBloque() {
        return bloque;
    }
    
    /**
     * @param bloque The bloque to set.
     */
    public void setBloque(Integer bloque) {
        this.bloque = bloque;
    }
    
    /**
     * @return Returns the carga_id.
     */
    public String getCarga_id() {
        return carga_id;
    }
    
    /**
     * @param carga_id The carga_id to set.
     */
    public void setCarga_id(String carga_id) {
        this.carga_id = carga_id;
    }
    
    /**
     * @return Returns the carrera.
     */
    public String getCarrera() {
        return carrera;
    }
    
    /**
     * @param carrera The carrera to set.
     */
    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
    
    /**
     * @return Returns the carrera_id.
     */
    public String getCarrera_id() {
        return carrera_id;
    }
    
    /**
     * @param carrera_id The carrera_id to set.
     */
    public void setCarrera_id(String carrera_id) {
        this.carrera_id = carrera_id;
    }
    
    /**
     * @return Returns the facultad.
     */
    public String getFacultad() {
        return facultad;
    }
    
    /**
     * @param facultad The facultad to set.
     */
    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }
    
    /**
     * @return Returns the facultad_id.
     */
    public String getFacultad_id() {
        return facultad_id;
    }
    
    /**
     * @param facultad_id The facultad_id to set.
     */
    public void setFacultad_id(String facultad_id) {
        this.facultad_id = facultad_id;
    }
    
    /**
     * @return Returns the fecha.
     */
    public String getFecha() {
        return fecha;
    }
    
    /**
     * @return Returns the idInstitucion.
     */
    public Integer getIdInstitucion() {
        return idInstitucion;
    }
    
    /**
     * @param idInstitucion The idInstitucion to set.
     */
    public void setIdInstitucion(Integer idInstitucion) {
        this.idInstitucion = idInstitucion;
    }
    
    /**
     * @return Returns the fecha.
     */
    public String getFechaF() throws Exception{
        java.util.Locale local = new java.util.Locale("es", "MX",
                "Traditional_WIN");
        SimpleDateFormat sdFormat = new SimpleDateFormat("dd-MMMM-yyyy", local);
        SimpleDateFormat sdFormat2 = new SimpleDateFormat("dd/MM/yyyy", local);
        String fecha = null;
        
        try {
            fecha = sdFormat.format(sdFormat2.parse(this.fecha));
        } catch (Exception e) {
            throw new UMException("Formato incorrecto de la fecha " + this.fecha);
        }
        
        return fecha;
    }
    
    /**
     * @param fecha The fecha to set.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    
    /**
     * @return Returns the formaPago.
     */
    public String getFormaPago() {
        return formaPago;
    }
    
    /**
     * @param formaPago The formaPago to set.
     */
    public void setFormaPago(String formaPago) throws Exception {
        if ((formaPago != null) &&
                !formaPago.toUpperCase().substring(0, 1).equals("C") &&
                !formaPago.toUpperCase().substring(0, 1).equals("P")) {
            throw new UMException("La forma de pago " + formaPago + " no es valida");
        }
        
        this.formaPago = formaPago;
    }
    
    /**
     * @return Returns the grado.
     */
    public Integer getGrado() {
        return grado;
    }
    
    /**
     * @param grado The grado to set.
     */
    public void setGrado(Integer grado) {
        this.grado = grado;
    }
    
    public String getHOStatus() {
        return hoStatus;
    }
    
    public void setHOStatus(String hoStatus) {
        this.hoStatus = hoStatus;
    }
        
    public Double getSemanasInternado () {
        return this.semanasInternado;
    }
    
    public void setSemanasInternado (Double semanasInternado) {
        //log.debug("{}",semanasInternado);
        this.semanasInternado = semanasInternado;
    }
    
    /**
     * @return Returns the inscrito.
     */
    public Boolean getInscrito() {
        return inscrito;
    }
    
    /**
     * @param inscrito The inscrito to set.
     */
    public void setInscrito(Boolean inscrito) {
        this.inscrito = inscrito;
    }
    
    /**
     * @return Returns the inscrito.
     */
    public Boolean isInscrito() {
        return this.inscrito;
    }


    public Boolean getGraduando() {
        return graduando;
    }


    public void setGraduando(Boolean graduando) {
        this.graduando = graduando;
    }


    public Boolean isGraduando() {
        return this.graduando;
    }
    
    public String getFolio() {
        return folio;
    }
    
    public void setFolio(String folio) {
        this.folio = folio;
    }
    
    /**
     * @return Returns the matricula.
     */
    public String getMatricula() {
        return matricula;
    }
    
    /**
     * @param matricula The matricula to set.
     */
    public void setMatricula(String matricula) throws Exception {
        if (matricula.length() != 7) {
            throw new UMException("La matricula " + matricula +
                    " tiene una longitud invalida");
        }
        
//        if (!matricula.substring(0, 1).equals("0") &&
//                !matricula.substring(0, 1).equals("1")) {
//            throw new UMException("La matricula " + matricula +
//                    " debe comenzar con 0 o con 1");
//        }
        
        this.matricula = matricula;
    }
    
    /**
     * @return Returns the modalidad.
     */
    public String getModalidad() {
        return modalidad;
    }
    
    /**
     * @param modalidad The modalidad to set.
     */
    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }
    
    /**
     * @return Returns the modalidad_id.
     */
    public Integer getModalidad_id() {
        return modalidad_id;
    }
    
    /**
     * @param modalidad_id The modalidad_id to set.
     */
    public void setModalidad_id(Integer modalidad_id) {
        this.modalidad_id = modalidad_id;
    }
    
    /**
     * @return Returns the nacionalidad.
     */
    public String getNacionalidad() {
        return nacionalidad;
    }
    /**
     * Regresa el nivel de la carrera del alumno [licenciatura, posgrado...]
     * @return 
     */
    public Integer getNivelId() {
        return nivel_id;
    }
    
    /**
     * @param nacionalidad The nacionalidad to set.
     */
    public void setNacionalidad(String nacionalidad) {
        this.nacionalidad = nacionalidad;
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
     * @return Returns the nombre_plan.
     */
    public String getNombre_plan() {
        return nombre_plan;
    }
    
    /**
     * @param nombre_plan The nombre_plan to set.
     */
    public void setNombre_plan(String nombre_plan) {
        this.nombre_plan = nombre_plan;
    }
    
    /**
     * @return Returns the plan_id.
     */
    public String getPlan_id() {
        return plan_id;
    }
    
    /**
     * @param plan_id The plan_id to set.
     */
    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }
    
    /**
     * @return Returns the religion.
     */
    public String getReligion() {
        return religion;
    }
    
    /**
     * @param religion The religion to set.
     */
    public void setReligion(String religion) {
        this.religion = religion;
    }

/**
     * @return Returns the residencia.
     */
    public String getResidencia() {
        return residencia;
    }
    
    /**
     * @param residencia The residencia to set.
     */
    public void setResidencia(String residencia) {
        this.residencia = residencia;
    }

    /**
     * @return Returns the semestre.
     */
    public Integer getSemestre() {
        return semestre;
    }
    
    /**
     * @param semestre The semestre to set.
     */
    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }
    
    /**
     * @return Returns the tAlumno.
     */
    public String getTAlumno() {
        return tAlumno;
    }
    
    /**
     * @param alumno The tAlumno to set.
     */
    public void setTAlumno(String alumno) {
        tAlumno = alumno;
    }
    
    /**
     * @return Returns the tAlumno_id.
     */
    public Integer getTAlumno_id() {
        return tAlumno_id;
    }
    
    /**
     * @param alumno_id The tAlumno_id to set.
     */
    public void setTAlumno_id(Integer alumno_id) {
        tAlumno_id = alumno_id;
    }
    
    /**
     * @return Returns the descuento_Beca_E.
     */
    public Double getDescuento_Beca_E() {
        return descuento_Beca_E;
    }
    
    /**
     * @param descuento_Beca_E The descuento_Beca_E to set.
     */
    public void setDescuento_Beca_E(Double descuento_Beca_E) {
        this.descuento_Beca_E = descuento_Beca_E;
    }
    
    /**
     * @return Returns the descuento_Beca_I.
     */
    public Double getDescuento_Beca_I() {
        return descuento_Beca_I;
    }
    
    /**
     * @param descuento_Beca_I The descuento_Beca_I to set.
     */
    public void setDescuento_Beca_I(Double descuento_Beca_I) {
        this.descuento_Beca_I = descuento_Beca_I;
    }
    
    /**
     * @return Returns the descuento_Beca_M.
     */
    public Double getDescuento_Beca_M() {
        return descuento_Beca_M;
    }
    
    /**
     * @param descuento_Beca_M The descuento_Beca_M to set.
     */
    public void setDescuento_Beca_M(Double descuento_Beca_M) {
        this.descuento_Beca_M = descuento_Beca_M;
    }
    
    /**
     * @return Returns the ensenanzaImporte.
     */
    public Double getEnsenanzaImporte() {
        return ensenanzaImporte;
    }
    
    /**
     * @param ensenanzaImporte The ensenanzaImporte to set.
     */
    public void setEnsenanzaImporte(Double ensenanzaImporte) {
        this.ensenanzaImporte = ensenanzaImporte;
    }
    
    /**
     * @return Returns the id_ccosto.
     */
    public String getId_ccosto() {
        return id_ccosto;
    }
    
    /**
     * @param id_ccosto The id_ccosto to set.
     */
    public void setId_ccosto(String id_ccosto) {
        this.id_ccosto = id_ccosto;
    }
    
    /**
     * @return Returns the internadoImporte.
     */
    public Double getInternadoImporte() {
        return internadoImporte;
    }
    
    /**
     * @param internadoImporte The internadoImporte to set.
     */
    public void setInternadoImporte(Double internadoImporte) {
        this.internadoImporte = internadoImporte;
    }
    
    /**
     * @return Returns the manejoPagare.
     */
    public Double getManejoPagare() {
        return manejoPagare;
    }
    
    /**
     * @param manejoPagare The manejoPagare to set.
     */
    public void setManejoPagare(Double manejoPagare) {
        this.manejoPagare = manejoPagare;
    }
    
    /**
     * @return Returns the matriculaImporte.
     */
    public Double getMatriculaImporte() {
        return matriculaImporte;
    }
    
    /**
     * @param matriculaImporte The matriculaImporte to set.
     */
    public void setMatriculaImporte(Double matriculaImporte) {
        this.matriculaImporte = matriculaImporte;
    }
    
    /**
     * @return Returns the factorInternado.
     */
    public Double getFactorInternado() {
        return factorInternado;
    }
    
    /**
     * @param factorInternado The factorInternado to set.
     */
    public void setFactorInternado(Double factorInternado) {
        this.factorInternado = factorInternado;
    }
    
    /**
     * @return Returns the pagoMinimoEnsenanza.
     */
    public Double getPagoMinimoEnsenanza() {
        return pagoMinimoEnsenanza;
    }
    
    /**
     * @param pagoMinimoEnsenanza The pagoMinimoEnsenanza to set.
     */
    public void setPagoMinimoEnsenanza(Double pagoMinimoEnsenanza) {
        this.pagoMinimoEnsenanza = pagoMinimoEnsenanza;
    }
    
    /**
     * @return Returns the pagoMinimoInternado.
     */
    public Double getPagoMinimoInternado() {
        return pagoMinimoInternado;
    }
    
    /**
     * @param pagoMinimoInternado The pagoMinimoInternado to set.
     */
    public void setPagoMinimoInternado(Double pagoMinimoInternado) {
        this.pagoMinimoInternado = pagoMinimoInternado;
    }
    
    /**
     * @return Returns the pagoMinimoMatricula.
     */
    public Double getPagoMinimoMatricula() {
        return pagoMinimoMatricula;
    }
    
    /**
     * @param pagoMinimoMatricula The pagoMinimoMatricula to set.
     */
    public void setPagoMinimoMatricula(Double pagoMinimoMatricula) {
        this.pagoMinimoMatricula = pagoMinimoMatricula;
    }
    
    public void getContabilidad() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            //log.debug("{}",this.matricula);
            
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            String COMANDO = "SELECT C.CCOSTO_ID ";
            COMANDO += "FROM ALUM_PLAN P, MAPA_PLAN MP, CAT_CARRERA C ";
            COMANDO += "WHERE MP.PLAN_ID = P.PLAN_ID ";
            COMANDO += "AND C.CARRERA_ID = MP.CARRERA_ID ";
            COMANDO += "AND P.ESTADO = '1' ";
            COMANDO += "AND P.CODIGO_PERSONAL = ?";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.id_ccosto = rset.getString("ccosto_id");
            }
            
            pstmt.close();
            rset.close();
            
            if ((this.id_ccosto == null) || (this.id_ccosto.length() == 0)) {
                throw new UMException(
                        "No se puedo determinar la contabilidad correspondiente del alumno " +
                        this.matricula +
                        "<br> Puede ser que su plan de estudios sea invalido!");
            }
            
            //Validar las cuentas activas del alumno
            this.tieneMasDeUnaContabilidad();
        } catch (Exception e) {
            throw new UMException("Error al obtener la contabilidad del alumno " +
                    this.matricula + "<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
    }
    
    public void tieneMasDeUnaContabilidad() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Integer nReg = new Integer(0);
        
        try {
            if ((conn_mateo == null) || conn_mateo.isClosed()) {
                conn_mateo = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            //Puede ser necesario validar el ejercicio contable
            Calendar gcHoy = new GregorianCalendar();
            gcHoy.setTime(new Date());
            
            SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");
            
            String COMANDO = "SELECT COUNT(*) NREG ";
            COMANDO += "FROM CONT_RELACION ";
            COMANDO += "WHERE ID_EJERCICIO = ? ";
            COMANDO += "AND ID_CCOSTO IN ('1.01','2.01') ";
            COMANDO += ("AND ID_CTAMAYOR IN ('" + ccfCtaEstudiantes + "','" +
                    ccfCtaPasivos + "','" + ccfCtaIncobrables + "') ");
            COMANDO += "AND ID_AUXILIAR = ? ";
            COMANDO += "AND STATUS = 'A' ";
            
            pstmt = conn_mateo.prepareStatement(COMANDO);
            pstmt.setString(1,
                    "001" + sdFormat.format(gcHoy.getTime()).substring(6));
            pstmt.setString(2, this.matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                nReg = new Integer(rset.getString("NReg"));
            }
            
            pstmt.close();
            rset.close();
            
            if (nReg.compareTo(new Integer(0)) > 1) {
                throw new UMException("El alumno " + this.getMatricula() +
                        " tiene mas de una cuenta contable activa.  Favor de reportarlo a Finanzas Estudiantiles.");
            }
        } catch (Exception e) {
            throw new UMException(
                    "Error al verificar las cuentas activas del alumno " +
                    this.matricula + "<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_mateo.isClosed()) {
                conn_mateo.close();
                conn_mateo = null;
            }
        }
    }
    
    public void getCargaIDBloque() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            /*Obtener CargaID*/
            String COMANDO = "SELECT DISTINCT SUBSTR(KCA.CURSO_CARGA_ID,0,6) CARGA_ID ";
            COMANDO += "FROM CARGA_GRUPO_CURSO CGC, CARGA_GRUPO CG, MAPA_CURSO C, KRDX_CURSO_ACT KCA, ";
            COMANDO += "( ";
            COMANDO += "SELECT CARGA_ID ";
            COMANDO += "FROM CARGA_BLOQUE ";
            COMANDO += "WHERE TO_DATE(SYSDATE, 'dd-mm-yy') ";
            COMANDO += "BETWEEN F_INICIO AND F_CIERRE ";
            COMANDO += ") A ";
            COMANDO += "WHERE SUBSTR(KCA.CURSO_CARGA_ID,0,6) = A.CARGA_ID ";
            COMANDO += "AND CGC.CURSO_CARGA_ID = CG.CURSO_CARGA_ID ";
            COMANDO += "AND C.CURSO_ID = CGC.CURSO_ID ";
            COMANDO += "AND KCA.CURSO_ID = C.CURSO_ID ";
            COMANDO += "AND KCA.CURSO_CARGA_ID = CGC.CURSO_CARGA_ID ";
            COMANDO += "AND C.ESTADO = '1' ";
            COMANDO += "AND KCA.TIPOCAL_ID = 'M' ";
            COMANDO += "AND KCA.CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.carga_id = rset.getString("Carga_ID");
            }
            
            rset.close();
            pstmt.close();
            
            //log.debug("getCargaIDBloque - carga {}",this.carga_id);
            
            if (this.carga_id == null) {
                //Buscar la carga_id en fes_ccobro
                //Si no tiene carga de materias activa, puede ser que ya este inscrito
                COMANDO = "SELECT CARGA_ID ";
                COMANDO += "FROM FES_CCOBRO ";
                COMANDO += "WHERE MATRICULA = ? ";
                COMANDO += "AND FECHA IN ";
                COMANDO += "(SELECT MAX(FECHA) ";
                COMANDO += "FROM FES_CCOBRO ";
                COMANDO += "WHERE MATRICULA = ?) ";
                pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, this.matricula);
                pstmt.setString(2, this.matricula);
                rset = pstmt.executeQuery();
                
                if (rset.next()) {
                    this.carga_id = rset.getString("Carga_ID");
                }
                
                pstmt.close();
                rset.close();
                
                if (this.carga_id == null) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene los datos necesarios para obtener la carga");
                }
            }
            //log.debug("getCargaIDBloque - carga {}",this.carga_id);

            COMANDO = "SELECT COALESCE(BLOQUE_ID,0) BLOQUE ";
            COMANDO += "FROM CARGA_BLOQUE ";
            COMANDO += "WHERE TO_DATE(SYSDATE,'dd-mm-yy') BETWEEN F_INICIO AND F_CIERRE ";
            COMANDO += "AND CARGA_ID = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.carga_id);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.bloque = new Integer(rset.getInt("Bloque"));
            }
            
            rset.close();
            pstmt.close();

            //log.debug("getCargaIDBloque - bloque {}",this.bloque);
            
            if (this.bloque.compareTo(new Integer(0)) == 0) {
                throw new UMException("La carga " + this.carga_id +
                        " no tiene sub-bloques activos");
            }
        } catch (Exception e) {
            throw new UMException("Error al obtener la carga y el bloque del alumno " +
                    this.matricula + "<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
    }
    
    public void getUltimaCargaInscrita(String fechaI, String fechaF) throws Exception {
         PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            String COMANDO = "SELECT CARGA_ID, BLOQUE ";
            COMANDO += "FROM FES_CCOBRO ";
            COMANDO += "WHERE MATRICULA = ? ";
            COMANDO += "AND FECHA IN ";
            COMANDO += "(SELECT MAX(FECHA) ";
            COMANDO += "FROM FES_CCOBRO ";
            COMANDO += "WHERE MATRICULA = ? ";
            COMANDO += "AND FECHA BETWEEN TO_DATE(?, 'DD/MM/YY') AND TO_DATE(?, 'DD/MM/YY')  ";
            COMANDO += "AND INSCRITO = 'S' ) ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            pstmt.setString(2, this.matricula);
            pstmt.setString (3, fechaI);
            pstmt.setString (4, fechaF);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.carga_id = rset.getString("Carga_ID");
                this.bloque = new Integer(rset.getInt("Bloque"));
            }
            
            pstmt.close();
            rset.close();
            
            if (this.carga_id == null) {
                throw new UMException("El alumno " + this.matricula +
                        " no esta inscrito en ningun bloque");
            }
        } catch (Exception e) {
            throw new UMException(
                    "Error al intentar obtener la carga y bloque de la ultima inscripcion del alumno " +
                    this.matricula);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
    }


    
    public void getAlumno() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            //Obtener datos de la tabla Alumno_Academico
            String COMANDO = "SELECT MODALIDAD_ID, TIPO_ALUMNO, SEMESTRE, ";
            COMANDO += "CLAS_FIN, RESIDENCIA_ID, GRADO, HO_STATUS ";
            COMANDO += "FROM ALUM_ACADEMICO ";
            COMANDO += "WHERE CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.modalidad_id = new Integer(rset.getInt("Modalidad_ID"));
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene una modalidad asignada<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.tAlumno_id = new Integer(rset.getInt("Tipo_Alumno"));
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene un tipo de alumno asignado<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.religion = rset.getString("Clas_Fin");
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene una religion asignada<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.residencia = rset.getString("Residencia_ID");
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene un tipo de residencia asignado<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.semestre = new Integer(rset.getInt("Semestre"));
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene un semestre asignado<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.grado = new Integer(rset.getInt("Grado"));
                
                this.hoStatus = rset.getString("ho_status");
                
                if(rset.wasNull()) {
                    throw new UMException("El status de hijo de obrero del alumno "+this.matricula+
                            " es invalido!<br>Favor de contactar a la Direccion de Sistemas");
                }

            }
            
            rset.close();
            pstmt.close();
            
            /*Todos los atributos tipo String son inicializados con el valor null
             * Por lo tanto, se utilizan para validar si el ResultSet obtuvo datos*/
            if (this.religion == null) {
                throw new UMException(
                        "El alumno " + this.matricula +
                        "tiene una religion invalida! " +
                        "<br>Favor de contactar al Dpto. de Registro.");
            }

            //Obtener descripcion de modalidad
            COMANDO = "SELECT NOMBRE_MODALIDAD ";
            COMANDO += "FROM CAT_MODALIDAD ";
            COMANDO += "WHERE MODALIDAD_ID = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setInt(1, this.modalidad_id.intValue());
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.modalidad = rset.getString("Nombre_Modalidad");
            }
            
            rset.close();
            pstmt.close();
            
            if (this.modalidad == null) {
                throw new UMException("El alumno " + this.matricula +
                        " tiene la modalidad " + this.modalidad_id +
                        " la cual es invalidad<br>Favor de contactar al Dpto. de Registo");
            }
            
            //Obtener descripcion de tipo alumno
            COMANDO = "SELECT NOMBRE_TIPO ";
            COMANDO += "FROM CAT_TIPOALUMNO ";
            COMANDO += "WHERE TIPO_ID = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setInt(1, this.tAlumno_id.intValue());
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.tAlumno = rset.getString("Nombre_Tipo");
            }
            
            rset.close();
            pstmt.close();
            
            if (this.tAlumno == null) {
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " tiene el tipo de alumno" + this.tAlumno_id +
                            " el cual es invalido<br>Favor de contactar al Dpto. de Registo");
                }
            }
            
            //Obtener nacionalidad
            COMANDO = "SELECT CASE NACIONALIDAD WHEN 91 THEN 'MEX' ELSE 'EXT' END AS PAIS, ";
            COMANDO += "NOMBRE || ' ' || APELLIDO_PATERNO || ' ' || APELLIDO_MATERNO NOMBRE ";
            COMANDO += "FROM ALUM_PERSONAL ";
            COMANDO += "WHERE CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.nacionalidad = rset.getString("Pais");
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene capturado un valor de pais valido<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.nombre = rset.getString("Nombre");
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene capturado un nombre valido<br>Favor de contactar al Dpto. de Registo");
                }
            }
            
            rset.close();
            pstmt.close();
            
            if (this.nacionalidad == null) {
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene datos de nacionalidad validos<br>Favor de contactar al Dpto. de Registro");
                }
            }
            
            //Obtener Facultad y Carrera
            COMANDO = "SELECT F.FACULTAD_ID, F.NOMBRE_FACULTAD, ";
            COMANDO += "C.CARRERA_ID, C.NOMBRE_CARRERA, C.CCOSTO_ID, ";
            COMANDO += "P.PLAN_ID, MP.NOMBRE_PLAN, P.CICLO SEMESTRE, N.NIVEL_ID ";
            COMANDO += "FROM ALUM_PLAN P, MAPA_PLAN MP, ";
            COMANDO += "CAT_CARRERA C, CAT_FACULTAD F, CAT_NIVEL N ";
            COMANDO += "WHERE F.FACULTAD_ID = C.FACULTAD_ID ";
            COMANDO += "AND C.CARRERA_ID = MP.CARRERA_ID ";
            COMANDO += "AND MP.PLAN_ID = P.PLAN_ID ";
            COMANDO += "AND P.ESTADO = '1' ";
            COMANDO += "AND C.NIVEL_ID = N.NIVEL_ID ";
            COMANDO += "AND P.CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.facultad_id = rset.getString("Facultad_ID");
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no esta registrado en una facultad valida<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.facultad = rset.getString("Nombre_Facultad");
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no esta registrado en una facultad valida<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.carrera_id = rset.getString("Carrera_ID");
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no esta registrado en una carrera valida<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.carrera = rset.getString("Nombre_Carrera");
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no esta registrado en una carrera valida<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.id_ccosto = rset.getString("CCosto_ID");
                
                if (rset.wasNull()) {
                    throw new UMException("La carrera "+this.carrera+" del alumno " + this.matricula +
                            " no tiene una contabilidad registrada<br>Favor de contactar al Dpto. de Sistemas");
                }
                
                this.plan_id = rset.getString("Plan_ID");
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no esta registrado en un plan de estudios valido<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.nombre_plan = rset.getString("Nombre_Plan");
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no esta registrado en un plan de estudios valido<br>Favor de contactar al Dpto. de Registo");
                }

                this.semestre = new Integer(rset.getInt("Semestre"));

                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene un semestre asignado<br>Favor de contactar al Dpto. de Registo");
                }
                
                this.nivel_id = new Integer(rset.getInt("nivel_id"));

                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene una carrera valida. Le falta el nivel. <br>Favor de contactar al Dpto. de Registo");
                }
            }
            
            rset.close();
            pstmt.close();
            
            if (this.plan_id == null) {
                throw new UMException("El alumno " + this.matricula +
                        " no tiene un plan de estudios valido<br>Favor de contactar al Dpto. de Registo");
            }

            Integer nivel = 0;
            COMANDO = "SELECT ENOC.NIVEL_PLAN(?) AS NIVEL " +
                      " FROM ENOC.ALUM_PERSONAL " +
                      "WHERE CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1,this.plan_id);
            pstmt.setString(2, this.matricula);
            rset = pstmt.executeQuery();

            if(rset.next()){
                nivel = rset.getInt("nivel");
            }

            if(rset.wasNull()){
                throw new UMException("El alumno " + this.matricula +
                            " presenta un error al intentar obtener su nivel de estudios");
            }

            rset.close();
            pstmt.close();

            //Niveles: 1 - Bachilleres, 2 - Universitario, 3 - Maestria, 4 - Doctorado
            if(nivel.compareTo(2) <= 0){
            //Obtener el semestre y el grado correctos
            COMANDO = "SELECT ENOC.ALUM_PLAN_CICLO(?,?) AS SEMESTRE " +
                    "FROM ENOC.ALUM_PERSONAL " +
                    "WHERE CODIGO_PERSONAL = ? ";
            }
            else{
                //Obtener el semestre y el grado correctos
                COMANDO = "SELECT ENOC.ALUM_PLAN_CICLO_POSTGRADO(?,?) AS SEMESTRE " +
                    "FROM ENOC.ALUM_PERSONAL " +
                    "WHERE CODIGO_PERSONAL = ? ";
            }
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            pstmt.setString(2,this.plan_id);
            pstmt.setString(3, this.matricula);
            rset = pstmt.executeQuery();

            if(rset.next()){
                this.semestre = rset.getInt("semestre");
            }

            if(rset.wasNull()){
                throw new UMException("El alumno " + this.matricula +
                            " no tiene un semestre asignado<br>Favor de contactar al Dpto. de Registo");
            }

            rset.close();
            pstmt.close();

            //Si el alumno es de bachilleres...
            if(this.facultad_id.equals("107")){
                //... el semestre se divide entre el numero de tetramestres en el anno
                this.grado = (new BigDecimal(this.semestre.toString()).divide(new BigDecimal("3"), 2, BigDecimal.ROUND_CEILING)).intValue();
            }
            else{
                //... el semestre se divide entre el numero de semestres en el anno
                this.grado = (new BigDecimal(this.semestre.toString()).divide(new BigDecimal("2"), 2, BigDecimal.ROUND_CEILING)).intValue();
            }

            //Evaluar si el alumno es graduando
            Integer numReg = 0;
            COMANDO = "SELECT COUNT(*) AS NUMREG " +
                    "FROM ENOC.ALUM_GRADUA " +
                    "WHERE CODIGO_PERSONAL = ? " +
                    "AND TO_DATE(FECHA_GRADUACION,'dd/mm/yy') > TO_DATE(sysdate,'dd/mm/yy') ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            rset = pstmt.executeQuery();
            if(rset.next()){
                numReg = rset.getInt("numReg");
            }

            rset.close();
            pstmt.close();

            if(numReg.compareTo(0) > 0){
                this.graduando = true;
            }
            else{
                this.graduando = false;
            }

        } catch (Exception e) {
            throw new UMException("Error al obtener los datos del alumno " +
                    this.matricula + "<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
    }
    
    /*Verificar si el alumno debe pagar matricula*/
    /*Si el metodo regresa true, es que se requiere pagar matricula*/
    public Boolean isMatricula() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        Boolean blnIsMatricula = new Boolean(false);
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            Integer intIsMatricula = new Integer(0);
            
            //Verificar si la carga y bloque del alumno tienen prendida la bandera de matricula
            String COMANDO = "SELECT COUNT(*) NREG " + "FROM CARGA_BLOQUE " +
                    "WHERE CARGA_ID = ? " + "AND BLOQUE_ID = ? " +
                    "AND ISMATRICULA = 'S' ";
            
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.carga_id);
            pstmt.setInt(2, this.bloque.intValue());
            
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                intIsMatricula = new Integer(rset.getInt("NReg"));
            }
            
            pstmt.close();
            rset.close();
            
            //Si la carga y el bloque no requieren pago de matricula
            if (intIsMatricula.compareTo(new Integer(0)) == 0) {
                Integer intNReg = new Integer(0);
                
                //Se debe verificar si el alumno ya pago matricula en la carga respectiva
                //buscamos si el alumno ya se inscribio algun subloque de la carga
                COMANDO = "SELECT COUNT(*) NREG " + "FROM FES_CCOBRO " +
                        "WHERE MATRICULA = ? " + "AND CARGA_ID = ? " +
                        "AND INSCRITO = 'S' ";
                
                pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, this.matricula);
                pstmt.setString(2, this.carga_id);
                
                rset = pstmt.executeQuery();
                
                if (rset.next()) {
                    intNReg = new Integer(rset.getInt("NReg"));
                }
                
                pstmt.close();
                rset.close();
                
                //Suponemos que un alumno inscrito en un subloque anterior,
                //ya pago matricula
                if (intNReg.compareTo(new Integer(0)) > 0) {
                    blnIsMatricula = new Boolean(false);
                } else {
                    /*Verificar si en la carga actual, un bloque anterior exige pago de matricula*/
                    COMANDO = "SELECT COUNT(*) NREG ";
                    COMANDO += "FROM CARGA_BLOQUE  ";
                    COMANDO += "WHERE BLOQUE_ID < ? ";
                    COMANDO += "AND CARGA_ID = ? ";
                    COMANDO += "AND ISMATRICULA = 'S' ";
                    pstmt = conn_enoc.prepareStatement(COMANDO);
                    pstmt.setInt(1, this.bloque.intValue());
                    pstmt.setString(2, this.carga_id);
                    rset = pstmt.executeQuery();
                    
                    if (rset.next()) {
                        intNReg = new Integer(rset.getInt("NReg"));
                    }
                    
                    pstmt.close();
                    rset.close();
                    
                    /*If 2*/
                    if (intNReg.compareTo(new Integer(0)) > 0) {
                        blnIsMatricula = new Boolean(true);
                    } else {
                        String strCargaID = null;
                        
                        //Obtener la primer carga anterior a la actual con un bloque
                        //requiriendo pago de matricula
                        COMANDO = "SELECT C.CARGA_ID ";
                        COMANDO += "FROM CARGA C, CARGA_BLOQUE B ";
                        COMANDO += "WHERE B.CARGA_ID = C.CARGA_ID ";
                        COMANDO += "AND SUBSTR(C.CARGA_ID,1,5) = SUBSTR(?, 1,5) ";
                        COMANDO += "AND C.CARGA_ID < ? ";
                        COMANDO += "AND B.ISMATRICULA = 'S' ";
                        COMANDO += "ORDER BY C.F_FINAL ";
                        
                        pstmt = conn_enoc.prepareStatement(COMANDO);
                        pstmt.setString(1, this.carga_id);
                        pstmt.setString(2, this.carga_id);
                        
                        rset = pstmt.executeQuery();
                        
                        if (rset.next()) {
                            strCargaID = rset.getString("Carga_ID");
                        }
                        
                        pstmt.close();
                        rset.close();
                        
                        intNReg = new Integer(0);
                        
                        //Se debe verificar si el alumno ya pago matricula en la carga respectiva
                        //buscamos si el alumno ya se inscribio algun subloque de la carga
                        COMANDO = "SELECT COUNT(*) NREG " + "FROM FES_CCOBRO " +
                                "WHERE MATRICULA = ? " + "AND CARGA_ID = ? " +
                                "AND INSCRITO = 'S' ";
                        
                        pstmt = conn.prepareStatement(COMANDO);
                        pstmt.setString(1, this.matricula);
                        pstmt.setString(2, strCargaID);
                        
                        rset = pstmt.executeQuery();
                        
                        if (rset.next()) {
                            intNReg = new Integer(rset.getInt("NReg"));
                        }
                        
                        pstmt.close();
                        rset.close();
                        
                        //Verificar si el alumno se inscribio en alguna carga entre la
                        //obtenida anteriormente y la carga actual
                        if(intNReg.compareTo(new Integer(0)) > 0)
                            blnIsMatricula = new Boolean(false);
                        else {
                            //Nos se utiliza el BETWEEN porque es INCLUSIVE y ocasiona errores
                            COMANDO = "SELECT COUNT(*) NReg " +
                                    "FROM FES_CCOBRO " +
                                    "WHERE MATRICULA = ? " +
                                    "AND INSCRITO = 'S' " +
                                    "AND CARGA_ID > ? " +
                                    "AND CARGA_ID < ? ";
                            
                            pstmt = conn.prepareStatement(COMANDO);
                            pstmt.setString(1, this.matricula);
                            pstmt.setString(2, strCargaID);
                            pstmt.setString(3, this.carga_id);
                            
                            rset = pstmt.executeQuery();
                            
                            if(rset.next()){
                                intNReg = new Integer(rset.getInt("NReg"));
                            }
                            
                            pstmt.close();
                            rset.close();
                            
                            //Suponemos que un alumno inscrito en una carga anterior,
                            //ya pago matricula
                            if (intNReg.compareTo(new Integer(0)) > 0) {
                                blnIsMatricula = new Boolean(false);
                            } else {
                                blnIsMatricula = new Boolean(true);
                            }
                            
                        }
                        
                    } /*Fin if 2*/} /*Fin if 1*/
            }
            //Si la carga y el bloque requieren pago de matricula
            else {
                blnIsMatricula = new Boolean(true);
            }
        } catch (Exception e) {
            throw new UMException(
                    "Error al intentar determinar el pago de matricula del alumno " +
                    this.matricula + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
        
        return blnIsMatricula;
    }
    
    /*Metodo que calcula importes de Matricula, Ensenanza e Internado para*/
    /*Alumno Hijo de Obrero*/
    public void hijoObrero(Map mTParametros, Map mMovimientos,
            Double matricula, Double ensenanza, Double internado, Connection conn)
            throws Exception {
        //Si el alumno es tipo hijo de obrero
        log.debug("Hijo de obrero {} , {}",this.getMatricula(),this.hoStatus);
        if (this.tAlumno_id.compareTo(ccfintTHObrero) == 0 && this.hoStatus.equals("A")) {
            Integer intConta = new Integer(0);
            
            Double dblValor = new Double(0);
            Double dblDctoEnsenanza = new Double(0);
            Double dblDctoInternado = new Double(0);
            Double dblDctoMatricula = new Double(0);
            
            try {
                log.debug("hijoObrero - 1 {}",this.getMatricula());
                InstitucionHObrero instHO = InstitucionHObrero.getPorcentajeInstituciones(this.getMatricula(), conn);
                dblDctoMatricula = new BigDecimal(String.valueOf(matricula)).multiply(instHO.getPctMatricula()).doubleValue();
                log.debug("hijoObrero - 2 {}", dblDctoMatricula);
                
                dblDctoEnsenanza = new BigDecimal(String.valueOf(ensenanza)).multiply(instHO.getPctEnsenanza()).doubleValue();
                log.debug("hijoObrero - 2 {}", dblDctoEnsenanza);
                
                dblDctoInternado = new BigDecimal(String.valueOf(internado)).multiply(instHO.getPctInternado()).doubleValue();
                log.debug("hijoObrero - 3 {}", dblDctoInternado);
                /*Obtener los parametros de descuento para este tipo de alumno*/
                /*Descuento en Matricula*/
//                TParametros tParametros = (TParametros) mTParametros.get("HO" +
//                        this.carga_id + "M");
//                dblDctoMatricula = new Double(matricula.doubleValue() * new Double(
//                        tParametros.getValor()).doubleValue());
//                tParametros = null;
                
                /*Descuento en Ensenanza*/
//                tParametros = (TParametros) mTParametros.get("HO" +
//                            this.carga_id + "E");
//                dblDctoEnsenanza = new Double(ensenanza.doubleValue() * new Double(
//                            tParametros.getValor()).doubleValue());
                //log.debug("Descuento ensenanza {} * {} ",ensenanza,tParametros.getValor());
                
                /*Descuento en Internado*/
//                tParametros = (TParametros) mTParametros.get("HO" +
//                            this.carga_id + "I");
//                dblDctoInternado = new Double(internado.doubleValue() * new Double(
//                            tParametros.getValor()).doubleValue());
//                //log.debug("Descuento internado {} * {}",internado,tParametros.getValor());
//                tParametros = null;
                    
                /*Descuento en Ensenanza*/
                /**
                 * TODO El Sr. Randeles solicito el 11 de enero de 2006, que se haga una evaluacion
                 * aparte para el descuento de ensenanza de los hijos de obreros de la
                 * Agencia de Publicaciones, la cual tiene ID 123.
                 * Para dichos alumnos el descuento es del 50%
                 *
                 * Arodi, solicito el 15 de Agosto de 2007, que para la Agencia de Publicaciones
                 * se tengan dos registros, uno que se comporte el 80%, y otro que se comporte con
                 * 50% para matricula y ensenanza
                 * 
                 * Arodi, solicto el 26 de Agosto de 2008, que para la Agencia de Publicaciones
                 * se tenga otro registro mas, uno que se calcule el 25% para matricula y ensenanza,
                 * y el 30% para internado
                 * 
                 * Arodi, solicito el 17 de Agosto de 2012, que para la Agencia de Publicaciones se
                 * tenga otro registro mas, uno que se calcule el 30% para matricula, y el 40% para 
                 * ensenanza e internado
                 * 
                 * El 6 de setiembre de 2012, se sustituyo este procedimiento por la lectura de institucionHObrero
                 */
//                this.getDependecia();
//                //log.debug("Institucion {}",this.getIdInstitucion());
//                if(this.getIdInstitucion().compareTo(new Integer(123)) == 0){
//                    dblDctoMatricula = new Double(matricula.doubleValue() * 0.50);
//                    dblDctoEnsenanza = new Double(ensenanza.doubleValue() * 0.50);
//                    
//                    //log.debug("Descuento matricula {} dcto {}",matricula,dblDctoMatricula);
//                    //log.debug("Descuento ensenanza {} dcto {}",ensenanza,dblDctoEnsenanza);
//                    
//                    
//                }else if(this.getIdInstitucion().compareTo(new Integer(139)) == 0){
//                    dblDctoMatricula = new Double(matricula.doubleValue() * 0.25);
//                    dblDctoEnsenanza = new Double(ensenanza.doubleValue() * 0.25);
//                    dblDctoInternado = new Double(internado.doubleValue() * 0.30);
//                    
//                    //log.debug("Descuento matricula {} dcto {}",matricula,dblDctoMatricula);
//                    //log.debug("Descuento ensenanza {} dcto {}",ensenanza,dblDctoEnsenanza);
//                }else if(this.getIdInstitucion().compareTo(new Integer(150)) == 0){
//                    dblDctoMatricula = new Double(matricula.doubleValue() * 0.30);
//                    dblDctoEnsenanza = new Double(ensenanza.doubleValue() * 0.30);
//                    dblDctoInternado = new Double(internado.doubleValue() * 0.40);
//                    
//                    //log.debug("Descuento matricula {} dcto {}",matricula,dblDctoMatricula);
//                    //log.debug("Descuento ensenanza {} dcto {}",ensenanza,dblDctoEnsenanza);
//                }
                
                //log.debug("hijoObrero - 3");
                
                //Insertar descuentos en la tabla de movimientos de c?lculo de cobro
                //log.debug("hijoObrero - 4");
                Movimiento movimiento = new Movimiento(this.matricula,
                        this.carga_id, this.bloque, ccfstrDesctoHObreroID,
                        ccfstrDesctoHObrero, dblDctoEnsenanza, "C", "S", "E",
                        this.getId_ccosto());
                mMovimientos.put(this.matricula + this.carga_id + this.bloque +
                        ccfstrDesctoHObreroID + "E", movimiento);
                movimiento = null;
                
                //log.debug("hijoObrero - 5");
                movimiento = new Movimiento(this.matricula, this.carga_id,
                        this.bloque, ccfstrDesctoHObreroID,
                        ccfstrDesctoHObrero, dblDctoInternado, "C", "S", "I",
                        this.getId_ccosto());
                mMovimientos.put(this.matricula + this.carga_id + this.bloque +
                        ccfstrDesctoHObreroID + "I", movimiento);
                movimiento = null;
                
                //log.debug("hijoObrero - 6");
                movimiento = new Movimiento(this.matricula, this.carga_id,
                        this.bloque, ccfstrDesctoHObreroID,
                        ccfstrDesctoHObrero, dblDctoMatricula, "C", "S", "M",
                        this.getId_ccosto());
                mMovimientos.put(this.matricula + this.carga_id + this.bloque +
                        ccfstrDesctoHObreroID + "M", movimiento);
                movimiento = null;
            } catch (Exception e) {
                e.printStackTrace();
                throw new UMException(
                        "Error al calcular las ayudas de hijo de obrero al alumno " +
                        this.getMatricula()+"<br>"+e);
            }
        }
        else if(this.hoStatus.equals("I")){
            //Indica que este alumno debe pasar con Finanzas Estudiantiles para que le activen la ayuda de Hijo Obrero
            
        }
    }
    
    /*Metodo que calcula importes de Matricula, Ense?anza e Internado para*/
    /*Alumno Becado*/
    public void becado(Map mBecas, Map mMovimientos, Double matricula,
            Double ensenanza, Double internado) throws Exception {
        //Si el alumno es tipo Becado
        if (this.tAlumno_id.compareTo(ccfintTBecado) == 0 ||
                this.tAlumno_id.compareTo(ccfintTHijoEmpleado) == 0) {
            if (mBecas.isEmpty()) {
                if(this.tAlumno_id.compareTo(ccfintTHijoEmpleado) == 0){
                    throw new UMException("El alumno " + this.getMatricula() +
                        " es tipo de alumno HIJO DE EMPLEADO UM pero no tiene ninguna beca capturada");
                }
                else{
                    throw new UMException("El alumno " + this.getMatricula() +
                            " es tipo de alumno BECADO pero no tiene ninguna beca capturada");
                }
            }
            
            Double dblBecaMatricula = new Double(0);
            Double dblBecaEnsenanza = new Double(0);
            Double dblBecaInternado = new Double(0);
            
            Beca beca = null;
            
            if (mBecas.containsKey(this.matricula)) {
                beca = (Beca) mBecas.get(this.matricula);
                
                dblBecaMatricula = new Double(beca.getP_matricula().doubleValue() * matricula.doubleValue());
                dblBecaEnsenanza = new Double(beca.getP_ensenanza().doubleValue() * ensenanza.doubleValue());
                dblBecaInternado = new Double(beca.getP_internado().doubleValue() * internado.doubleValue());
                
                /*Insertar movimientos de becas*/
                /*Beca sobre matricula*/
                if (dblBecaMatricula.compareTo(new Double(0)) != 0) {
                    Movimiento movimiento = new Movimiento(this.matricula,
                            this.carga_id, this.bloque, ccfstrBecasID,
                            ccfstrBecas + " MATRICULA", dblBecaMatricula, "C",
                            "S", "M", this.getId_ccosto());
                    mMovimientos.put(this.matricula + this.carga_id +
                            this.bloque + ccfstrBecasID + "M", movimiento);
                    movimiento = null;
                }
                
                /*Beca sobre ensenanza*/
                if (dblBecaEnsenanza.compareTo(new Double(0)) != 0) {
                    Movimiento movimiento = new Movimiento(this.matricula,
                            this.carga_id, this.bloque, ccfstrBecasID,
                            ccfstrBecas + " ENSENANZA", dblBecaEnsenanza, "C",
                            "S", "E", this.getId_ccosto());
                    mMovimientos.put(this.matricula + this.carga_id +
                            this.bloque + ccfstrBecasID + "E", movimiento);
                    movimiento = null;
                }
                
                /*Beca sobre internado*/
                if (dblBecaInternado.compareTo(new Double(0)) != 0) {
                    Movimiento movimiento = new Movimiento(this.matricula,
                            this.carga_id, this.bloque, ccfstrBecasID,
                            ccfstrBecas + " INTERNADO", dblBecaInternado, "C",
                            "S", "I", this.getId_ccosto());
                    mMovimientos.put(this.matricula + this.carga_id +
                            this.bloque + ccfstrBecasID + "I", movimiento);
                    movimiento = null;
                }
            } else {
                throw new UMException("El alumno " + this.getMatricula() +
                        " es tipo de alumno BECADO pero no tiene ninguna beca capturada");
            }
        }
    }
    
    /*Metodo que calcula importes de Matricula, Ense?anza e Internado para*/
    /*Alumno Obrero*/
    public void Obrero(Map mMovimientos, Double ensenanza)
    throws Exception {
        //Si el alumno es tipo Obrero
        if (this.tAlumno_id.compareTo(ccfintTObrero) == 0) {
            /*Insertar movimiento de descuento de obrero*/
            Movimiento movimiento = new Movimiento(this.matricula,
                    this.carga_id, this.bloque, ccfstrDesctoObreroID,
                    ccfstrDesctoObrero, ensenanza, "C", "N", "E",
                    this.getId_ccosto());
            mMovimientos.put(this.matricula + this.carga_id + this.bloque +
                    ccfstrDesctoObreroID, movimiento);
            movimiento = null;
        }
    }
    
    /*Calcular saldo anterior del alumno*/
    public void getSaldoAnterior(Map mMovimientos, Vector mPagares) throws Exception {
        Double dblSACredito = new Double(0);
        Double dblSACargo = new Double(0);
        
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            /*Obtener ejercicio contable actual*/
            java.util.Calendar gcFecha = new java.util.GregorianCalendar();
            gcFecha.setTime(new java.util.Date());
            
            String strIDEjercicio = "001-" +
                    String.valueOf(gcFecha.get(java.util.Calendar.YEAR));
            
            /*Obtener saldo anterior de los movimientos de contabilidad*/
            /*Obtener los creditos del saldo anterior*/
            String COMANDO = "SELECT COALESCE(SUM(IMPORTE),0) SACREDITO ";
            COMANDO += "FROM CONT_MOVIMIENTO ";
            COMANDO += "WHERE ID_EJERCICIO = ? ";
            COMANDO += "AND ID_AUXILIARM = ? ";
            COMANDO += "AND NATURALEZA = 'C' ";
            COMANDO += "AND STATUS != 'I' ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, strIDEjercicio);
            pstmt.setString(2, this.matricula);
            
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                dblSACredito = new Double(rset.getDouble("SACredito"));
            }
            
            pstmt.close();
            rset.close();
            
            //Obtener los cargos del saldo anterior
            COMANDO = "SELECT COALESCE(SUM(IMPORTE),0) SACARGO ";
            COMANDO += "FROM CONT_MOVIMIENTO ";
            COMANDO += "WHERE ID_EJERCICIO = ? ";
            COMANDO += "AND NATURALEZA = 'D' ";
            COMANDO += "AND STATUS != 'I' ";
            COMANDO += "AND ID_AUXILIARM = ? ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, strIDEjercicio);
            pstmt.setString(2, this.matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                dblSACargo = new Double(rset.getDouble("SACargo"));
            }
            
            pstmt.close();
            rset.close();
            
            Double dblSAnterior = new Double(dblSACargo.doubleValue() -
                    dblSACredito.doubleValue());
            
            /*Creditos para el alumno*/
            if (dblSAnterior.compareTo(new Double(0)) > 0) {
                /*Insertar movimiento de calculo de cobro del saldo anterior*/
                Movimiento movimiento = new Movimiento(this.matricula,
                        this.carga_id, this.bloque, ccfstrSaldoAnteriorID,
                        ccfstrSaldoAnterior, dblSAnterior, "D", "N", "T",
                        this.getId_ccosto());
                mMovimientos.put(this.matricula + this.carga_id + this.bloque +
                        ccfstrSaldoAnteriorID, movimiento);
                movimiento = null;
            }
            /*Cargos para el alumno*/
            else {
                Movimiento movimiento = new Movimiento(this.matricula,
                        this.carga_id, this.bloque, ccfstrSaldoAnteriorID,
                        ccfstrSaldoAnterior,
                        new Double(new BigDecimal(dblSAnterior.doubleValue()).abs()
                        .doubleValue()),
                        "C", "N", "T", this.getId_ccosto());
                mMovimientos.put(this.matricula + this.carga_id + this.bloque +
                        ccfstrSaldoAnteriorID, movimiento);
                movimiento = null;

                //Registro del folio de saldo anterior
                Pagare pagare = new Pagare(this.matricula,this.carga_id, this.bloque,"",(new BigDecimal(dblSAnterior.doubleValue()).abs()).doubleValue(), ccfpstrSaldoAnterior);

                mPagares.add(pagare);
            }
        } catch (Exception e) {
            throw new UMException("Error al obtener el saldo anterior del alumno " +
                    this.matricula + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
    }
    
    /*
     * Limpiar tablas de calculo de cobro en la base de datos
     */
    public static void limpiaTabla(Connection conn, String matricula,
            String carga_id, Integer bloque) throws Exception {
        PreparedStatement pstmt = null;
        
        try {
            String COMANDO = "DELETE " + "FROM MATEO.FES_CCOBRO " +
                    "WHERE MATRICULA = ? " + "AND CARGA_ID = ? " +
                    "AND BLOQUE = ? ";
            
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setString(2, carga_id);
            pstmt.setInt(3, bloque.intValue());
            pstmt.execute();
            pstmt.close();
        } catch (Exception e) {
            throw new UMException(
                    "Error al inicializar el calculo de cobro del alumno " +
                    matricula + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }
    }
    
    /*Grabar datos del alumno en la base de datos*/
    public void grabaTabla(Connection conn, Boolean inscrito, Boolean online)
    throws Exception {
        PreparedStatement pstmt = null;
        
        try {
            if (this.matricula.length() != 7) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula +
                        "<br>La matricula tiene una longitud invalida");
            }
            
            
            if (this.carga_id.length() != 6) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>La carga " + this.carga_id +
                        "tiene una longitud invalida");
            }
            
            
            if (String.valueOf(this.bloque).length() > 3) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>El bloque " + this.bloque +
                        "tiene una longitud invalida");
            }
            
            
            if (this.nombre.length() > 150) {
                this.nombre = this.nombre.substring(0, 149);
            }
            
            
            if (String.valueOf(this.modalidad_id).length() > 2) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>La modalidad_id " +
                        this.modalidad_id + "tiene una longitud invalida");
            }
            
            
            if (this.modalidad.length() > 30) {
                this.modalidad = this.modalidad.substring(0, 29);
            }
            
            
            if (String.valueOf(this.tAlumno_id).length() > 2) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>El tipo de alumno_id " +
                        this.tAlumno_id + "tiene una longitud invalida");
            }
            
            
            if (this.tAlumno.length() > 40) {
                this.tAlumno = this.tAlumno.substring(0, 39);
            }
            
            
            if (String.valueOf(this.semestre).length() > 2) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>El semestre " + this.semestre +
                        "tiene una longitud invalida");
            }
            
            
            if (this.formaPago.substring(0, 1).length() > 2) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>La forma de pago " +
                        this.formaPago.substring(0, 1) +
                        "tiene una longitud invalida");
            }
            
            
            if (this.religion.length() > 2) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>La religion " + this.religion +
                        "tiene una longitud invalida");
            }
            
            
            if (this.nacionalidad.length() > 3) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>La nacionalidad " +
                        this.nacionalidad + "tiene una longitud invalida");
            }
            
            
            if (this.residencia.length() > 1) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>La residencia " + this.residencia +
                        "tiene una longitud invalida");
            }
            
            
            if (this.facultad_id.length() > 3) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>La facultad_id " + this.facultad_id +
                        "tiene una longitud invalida");
            }
            
            
            if (this.facultad.length() > 100) {
                this.facultad = this.facultad.substring(0, 99);
            }
            
            
            if (this.carrera_id.length() > 5) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>La carrera_id " + this.carrera_id +
                        "tiene una longitud invalida");
            }
            
            
            if (this.carrera.length() > 150) {
                this.carrera = this.carrera.substring(0, 149);
            }
            
            
            if (this.plan_id.length() > 12) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>El plan_id " + this.plan_id +
                        "tiene una longitud invalida");
            }
            
            
            if (this.nombre_plan.length() > 40) {
                this.nombre_plan = this.nombre_plan.substring(0, 39);
            }
            
            
            if (String.valueOf(this.grado).length() > 2) {
                throw new UMException(
                        "Error al insertar datos del calculo de cobro del alumno " +
                        this.matricula + "<br>El grado " + this.grado +
                        "tiene una longitud invalida");
            }
            
//            if (this.getResidencia().equals("I") && this.getSemanasInternado().compareTo(new Double(0)) <= 0) {
//                throw new UMException(
//                        "Error al insertar datos del calculo de cobro del alumno " +
//                        this.matricula + "<br>El numero de semanas de internado "+this.getSemanasInternado()+
//                        "es invalido");
//            }
            
            System.out.print ("Paso1");
            /*Generar folio unico por calculo de cobro de alumnos inscritos*/
            if(inscrito.booleanValue()){
                String COMANDO = "SELECT SUBSTR(TO_CHAR(COALESCE(MAX(SUBSTR(FOLIO,8)),'00000000')+10001),2) FOLIO " +
                        "FROM MATEO.FES_CCOBRO " +
                        "WHERE CARGA_ID = ? " +
                        "AND INSCRITO = 'S' ";
                pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, this.carga_id);
                
                ResultSet rset = pstmt.executeQuery();
                
                if (rset.next()) {
                    this.folio = this.carga_id+this.bloque+rset.getString("Folio");
                }
                
            } else
                this.folio = null;
            
            System.out.print ("Paso2");
            String COMANDO = "SELECT MAX(ID)+1 NREG FROM MATEO.FES_CCOBRO ";
            pstmt = conn.prepareStatement (COMANDO);
            ResultSet rset = pstmt.executeQuery ();
            
            while(rset.next ()){
                this.id = new Integer(rset.getInt ("nreg"));
            }
            rset.close();
            
            System.out.print ("Paso3");
            COMANDO = "INSERT INTO MATEO.FES_CCOBRO ";
            COMANDO += "(ID, MATRICULA, CARGA_ID, BLOQUE, MODALIDAD_ID, MODALIDAD, ";
            COMANDO += "TALUMNO_ID, TALUMNO, SEMESTRE, FECHA, FORMAPAGO, RELIGION, ";
            COMANDO += "NACIONALIDAD, RESIDENCIA, FACULTAD_ID, FACULTAD, ";
            COMANDO += "CARRERA_ID, CARRERA, PLAN_ID, NOMBRE_PLAN, GRADO, SEMANAS_INTERNADO, ";
            COMANDO += "NOMBRE, INSCRITO, FOLIO, VERSION, GRADUANDO, ON_LINE) ";
            COMANDO += "VALUES ";
            COMANDO += "(?, ?, ?, ?, ?, ?, ?, ?, ?, to_date(?,'dd/mm/yy'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, ?) ";
            
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setInt (1, this.id.intValue ());
            pstmt.setString(2, this.matricula);
            pstmt.setString(3, this.carga_id);
            pstmt.setInt(4, this.bloque.intValue());
            pstmt.setInt(5, this.modalidad_id.intValue());
            pstmt.setString(6, this.modalidad);
            pstmt.setInt(7, this.tAlumno_id.intValue());
            pstmt.setString(8, this.tAlumno);
            pstmt.setString(9, this.semestre.toString());
            pstmt.setString(10, this.fecha);
            pstmt.setString(11, this.formaPago.substring(0, 1));
            pstmt.setString(12, this.religion);
            pstmt.setString(13, this.nacionalidad);
            pstmt.setString(14, this.residencia);
            pstmt.setString(15, this.facultad_id);
            pstmt.setString(16, this.facultad);
            pstmt.setString(17, this.carrera_id);
            pstmt.setString(18, this.carrera);
            pstmt.setString(19, this.plan_id);
            pstmt.setString(20, this.nombre_plan);
            pstmt.setString(21, this.grado.toString());
            pstmt.setDouble(22, this.semanasInternado);
            pstmt.setString(23, this.nombre);
            pstmt.setString(24, (!inscrito.booleanValue()) ? "N" : "S");
            pstmt.setString(25, this.folio);
            pstmt.setString(26, (!graduando.booleanValue()) ? "N" : "S");
            pstmt.setString(27, (!online.booleanValue()) ? "N" : "S");
            
            pstmt.execute();
            pstmt.close();
        } catch (Exception e) {
            throw new UMException(
                    "Error al insertar datos del calculo de cobro del alumno " +
                    this.matricula + "<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }
    }
    public void modificaHOStatus(Connection conn_enoc) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
                String COMANDO = "UPDATE ENOC.ALUM_ACADEMICO ";
                COMANDO += "SET HO_STATUS = 'I' ";
                COMANDO += "WHERE CODIGO_PERSONAL = ? ";
                
                pstmt = conn_enoc.prepareStatement(COMANDO);
                pstmt.setString(1, this.getMatricula());

                pstmt.execute();
                pstmt.close();

                //log.debug("Alumno.modificaHOStatus {}",this.getMatricula());
        } catch (Exception e) {
            throw new UMException("Error al cambiar el status de hijo obrero al alumno " +
                    this.getMatricula() + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
        }
    }
    
    public void activaHOStatus() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
                if ((conn == null) || conn.isClosed()) {
                    conn = new Conexion().getConexionEnoc(new Boolean(false));
                }
                String COMANDO = "UPDATE ALUM_ACADEMICO ";
                COMANDO += "SET HO_STATUS = 'A' ";
                COMANDO += "WHERE CODIGO_PERSONAL = ? ";
                
                pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, this.getMatricula());
                
                pstmt.execute();
                pstmt.close();

                //log.debug("Alumno.modificaHOStatus {}",this.getMatricula());
        } catch (Exception e) {
            throw new UMException("Error al cambiar el status de hijo obrero al alumno " +
                    this.getMatricula() + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            if (conn != null) {
                conn.close();
                conn = null;
            }
        }
    }
    /**
     * Es necesario, al querer reiniciar el estatus de inscrito del alumno,
     * verificar si la carga y el bloque existen ya que puede darse el caso de
     * que al alumno se le asignan las materias en un bloque anterior y el
     * alumno se inscribe en un bloque posterior
     */
    public void borraStatus(Connection conn_enoc) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Integer nReg = new Integer(0);
        
        try {
            String COMANDO = "SELECT COUNT(*)  NREG " +
                    "FROM ALUM_ESTADO " +
                    "WHERE CODIGO_PERSONAL = ? " +
                    "AND CARGA_ID = ? " +
                    "AND BLOQUE_ID = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.getMatricula());
            pstmt.setString(2, this.getCarga_id());
            pstmt.setInt(3, this.getBloque().intValue());
            rset = pstmt.executeQuery();
            
            if(rset != null){
                nReg = new Integer(rset.getInt("NReg"));
            }
            rset.close();
            pstmt.close();
            
            if(nReg.compareTo(0) > 0){
                
                COMANDO = "UPDATE ALUM_ESTADO ";
                COMANDO += "SET ESTADO = 'A' ";
                COMANDO += "WHERE CODIGO_PERSONAL = ? ";
                COMANDO += "AND CARGA_ID = ? ";
                COMANDO += "AND BLOQUE_ID = ? ";
                
                pstmt = conn_enoc.prepareStatement(COMANDO);
                pstmt.setString(1, this.getMatricula());
                pstmt.setString(2, this.getCarga_id());
                pstmt.setInt(3, this.getBloque().intValue());
            } else{
                COMANDO = "INSERT INTO ALUM_ESTADO " +
                        "(CODIGO_PERSONAL, CARGA_ID, BLOQUE_ID, ESTADO) " +
                        "VALUE " +
                        "(?, ?, ?, 'A') ";
                
                pstmt = conn_enoc.prepareStatement(COMANDO);
                pstmt.setString(1, this.getMatricula());
                pstmt.setString(2, this.getCarga_id());
                pstmt.setInt(3, this.getBloque());
            }
            pstmt.execute();
            pstmt.close();
        } catch (Exception e) {
            throw new UMException("Error al borrar el status de inscrito al alumno " +
                    this.getMatricula() + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
        }
    }
    
    public void modificaStatus(Connection conn_enoc) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Integer nReg = new Integer(0);
        
        try {
            String COMANDO = "SELECT COUNT(*)  NREG " +
                    "FROM ENOC.ALUM_ESTADO " +
                    "WHERE CODIGO_PERSONAL = ? " +
                    "AND CARGA_ID = ? " +
                    "AND BLOQUE_ID = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.getMatricula());
            pstmt.setString(2, this.getCarga_id());
            pstmt.setInt(3, this.getBloque());
            rset = pstmt.executeQuery();
            
            if(rset.next()){
                nReg = new Integer(rset.getInt("NReg"));
            }
            rset.close();
            pstmt.close();
            
            if(nReg.compareTo(0) > 0){
                
                COMANDO = "UPDATE ENOC.ALUM_ESTADO ";
                COMANDO += "SET ESTADO = 'I' ";
                COMANDO += "WHERE CODIGO_PERSONAL = ? ";
                COMANDO += "AND CARGA_ID = ? ";
                COMANDO += "AND BLOQUE_ID = ? ";
                
                pstmt = conn_enoc.prepareStatement(COMANDO);
                pstmt.setString(1, this.getMatricula());
                pstmt.setString(2, this.getCarga_id());
                pstmt.setInt(3, this.getBloque());
            } else{
                COMANDO = "INSERT INTO ENOC.ALUM_ESTADO " +
                        "(CODIGO_PERSONAL, CARGA_ID, BLOQUE_ID, ESTADO) " +
                        "VALUES " +
                        "(?, ?, ?, 'I') ";
                
                pstmt = conn_enoc.prepareStatement(COMANDO);
                pstmt.setString(1, this.getMatricula());
                pstmt.setString(2, this.getCarga_id());
                pstmt.setInt(3, this.getBloque());
            }
            pstmt.execute();
            pstmt.close();
            
            //log.debug("Alumno.modificaStatus {}, {}, {}", new String [] {this.getMatricula(),this.getCarga_id(),this.getBloque().toString()});
            //log.debug("Alumno.modificaGrado {}, {}, {}",new String [] {this.getMatricula(),this.grado.toString(),this.semestre.toString()});

            COMANDO = "UPDATE ENOC.ALUM_PLAN " +
                    "SET GRADO = ?, CICLO = ? " +
                    "WHERE CODIGO_PERSONAL = ? " +
                    "AND PLAN_ID = ?";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setInt(1, this.grado);
            pstmt.setInt(2, this.semestre);
            pstmt.setString(3, this.matricula);
            pstmt.setString(4, this.plan_id);
            pstmt.executeUpdate();
            pstmt.close();

        } catch (Exception e) {
            throw new UMException("Error al cambiar el status de inscrito al alumno " +
                    this.getMatricula() + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
        }
    }
    
    public void getDatosCCobro()
    throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            String COMANDO =
                    "SELECT SEMANAS_INTERNADO " +
                    "FROM FES_CCOBRO " +
                    "WHERE MATRICULA = ? " + "AND CARGA_ID = ? " +
                    "AND BLOQUE = ? ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setString(2, carga_id);
            pstmt.setInt(3, bloque.intValue());
            
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                
                this.setSemanasInternado(rset.getDouble("semanas_internado"));
            }
            
            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new UMException(
                    "Error al obtener los datos del CCobro del alumno " + matricula +
                    " en la carga " + carga_id + " y el bloque " + bloque + "<br>" +
                    e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
    }
    
    public void getAlumnoCC(String matricula, String carga_id, Integer bloque)
    throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        //log.debug("getAlumnoCC ");

        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            String COMANDO =
                    "SELECT NOMBRE, MODALIDAD_ID, MODALIDAD, TALUMNO_ID, TALUMNO, " +
                    "SEMESTRE, TO_CHAR(FECHA,'DD/MM/YYYY') FECHA, FORMAPAGO, RELIGION, NACIONALIDAD, " +
                    "RESIDENCIA, FACULTAD_ID, FACULTAD, CARRERA_ID, CARRERA, PLAN_ID, NOMBRE_PLAN, " +
                    "GRADO, SEMANAS_INTERNADO, INSCRITO, FOLIO, GRADUANDO " + "FROM FES_CCOBRO " +
                    "WHERE MATRICULA = ? " + "AND CARGA_ID = ? " +
                    "AND BLOQUE = ? ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setString(2, carga_id);
            pstmt.setInt(3, bloque.intValue());
            //log.debug("getAlumnoCC {}",matricula);
            //log.debug("getAlumnoCC {}",carga_id);
            //log.debug("getAlumnoCC {}",bloque);
            
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.setMatricula(matricula);
                this.setCarga_id(carga_id);
                this.setBloque(bloque);
                this.setNombre(rset.getString("Nombre"));
                this.setModalidad_id(new Integer(rset.getInt("Modalidad_ID")));
                this.setModalidad(rset.getString("Modalidad"));
                this.setTAlumno_id(new Integer(rset.getInt("TAlumno_ID")));
                this.setTAlumno(rset.getString("TAlumno"));
                this.setSemestre(new Integer(rset.getInt("Semestre")));
                this.setFecha(rset.getString("Fecha"));
                this.setFormaPago(rset.getString("FormaPago"));
                this.setReligion(rset.getString("Religion"));
                this.setNacionalidad(rset.getString("Nacionalidad"));
                this.setResidencia(rset.getString("Residencia"));
                this.setFacultad_id(rset.getString("Facultad_ID"));
                this.setFacultad(rset.getString("Facultad"));
                this.setCarrera_id(rset.getString("Carrera_ID"));
                this.setCarrera(rset.getString("Carrera"));
                this.setPlan_id(rset.getString("Plan_ID"));
                this.setNombre_plan(rset.getString("Nombre_Plan"));
                this.setGrado(new Integer(rset.getString("Grado")));
                this.setSemanasInternado(rset.getDouble("semanas_internado"));
                this.setInscrito(new Boolean(rset.getString("Inscrito").equals("S")
                ? true : false));
                this.setGraduando(new Boolean(rset.getString("Graduando").equals("S")
                ? true : false));
                this.setFolio(rset.getString("Folio"));
                this.setFactorInternado(new Double(0));
                
                //Evaluar el valor de residencia, formapago y religion
                if (this.getResidencia().equals("E")) {
                    this.setResidencia("Externo");
                } else {
                    this.setResidencia("Interno");
                }
                
                if (this.getFormaPago().equals("C")) {
                    this.setFormaPago("Contado");
                } else {
                    this.setFormaPago("Pagare");
                }
                
                if (this.getReligion().equals("1")) {
                    this.setReligion("ASD");
                } else {
                    this.setReligion("No ASD");
                }
                
                this.getDependecia();
            }else{
                /*Si no leyo ningun dato*/
                throw new UMException("El alumno "+matricula+" no tiene ningun calculo de cobro estimado en la carga "+carga_id+" y el bloque "+bloque);
            }
            
            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new UMException(
                    "Error al obtener los datos generales del alumno " + matricula +
                    " en la carga " + carga_id + " y el bloque " + bloque + "<br>" +
                    e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
    }
    
    /**
     *
     * @param cargaId
     * @param bloque
     * @param facultadId
     * @param carreraId
     * @param modalidad
     * @param fechaI
     * @param fechaF
     * @return
     * @throws Exception
     */
    public Map getAlumnoCC(String cargaId, Integer bloque, String facultadId,
            String carreraId, String modalidad, String fechaI, String fechaF)
            throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Map mAlumnos = new TreeMap(new NoCompara());
        String COMANDO = null;
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            COMANDO = "SELECT MATRICULA, NOMBRE, MODALIDAD_ID, MODALIDAD, TALUMNO_ID, TALUMNO, " +
                    "SEMESTRE, TO_CHAR(FECHA,'DD/MM/YYYY') FECHA, FORMAPAGO, RELIGION, NACIONALIDAD, " +
                    "RESIDENCIA, FACULTAD_ID, FACULTAD, CARRERA_ID, CARRERA, PLAN_ID, NOMBRE_PLAN, " +
                    "GRADO, SEMANAS_INTERNADO, INSCRITO, FOLIO, GRADUANDO " + "FROM FES_CCOBRO " +
                    "WHERE INSCRITO = 'S' ";
            
            //Si cargaid y bloque son nulos, entonces debe existir un rango de fechas
            if ((cargaId != null) && (bloque != null)) {
                COMANDO += "AND CARGA_ID = ? AND BLOQUE = ? ";
            } else if ((fechaI != null) && (fechaF != null)) {
                COMANDO += "AND FECHA BETWEEN TO_DATE(?,'DD/MM/YYYY') AND TO_DATE(?,'DD/MM/YYYY') ";
            } else if (((cargaId != null) && (bloque == null)) ||
                    ((cargaId == null) && (bloque != null))) {
                throw new UMException("Error: Los valores de la carga " + cargaId +
                        " y el valor del bloque " + bloque +
                        ", ambos, deben tener un valor valido.");
            } else if (((fechaI == null) && (fechaF != null)) ||
                    ((fechaI != null) && (fechaF == null))) {
                throw new UMException("Error: El dato Fecha Inicial = " + fechaI +
                        " y Fecha Final = " + fechaF +
                        ".  Ambos campos deben tener una fecha valida.");
            } else {
                throw new UMException("Error: El dato cargaId = " + cargaId +
                        " y bloque = " + bloque +
                        ". Debe proporcionar un valor para la carga y el bloque, o de lo contrario proporcionar un rango de fechas.");
            }
            
            if (facultadId != null) {
                COMANDO += "AND FACULTAD_ID = ? ";
                
                if (carreraId != null) {
                    COMANDO += "AND CARRERA_ID = ? ";
                }
            }
            
            if ((facultadId == null) && (carreraId != null)) {
                throw new UMException("Error: El dato carreraId = " + carreraId +
                        " y facultadId = " + facultadId +
                        ". Carrera no puede tener ningun valor si facultadId es 'null'.");
            }
            pstmt = conn.prepareStatement(COMANDO);
            
            if ((cargaId != null) && (bloque != null)) {
                pstmt.setString(1, cargaId);
                pstmt.setInt(2, bloque.intValue());
            } else if ((fechaI != null) && (fechaF != null)) {
                pstmt.setString(1, fechaI);
                pstmt.setString(2, fechaF);
            }
            
            if (facultadId != null) {
                pstmt.setString(3, facultadId);
                
                if (carreraId != null) {
                    pstmt.setString(4, carreraId);
                }
            }
            
            rset = pstmt.executeQuery();
            
            //log.debug("{}",COMANDO);
            
            while (rset.next()) {
                this.matricula = rset.getString("matricula");
                Alumno alumno = new Alumno(this.matricula, cargaId, bloque,
                        rset.getString("nombre"), this.getId_ccosto(),
                        new Integer(rset.getInt("modalidad_id")),
                        rset.getString("modalidad"),
                        new Integer(rset.getInt("talumno_id")),
                        rset.getString("talumno"),
                        new Integer(rset.getInt("semestre")),
                        rset.getString("fecha"), rset.getString("formapago"),
                        rset.getString("religion"),
                        rset.getString("nacionalidad"),
                        rset.getString("residencia"),
                        rset.getString("facultad_id"),
                        rset.getString("facultad"),
                        rset.getString("carrera_id"),
                        rset.getString("carrera"), rset.getString("plan_id"),
                        rset.getString("nombre_plan"),
                        new Integer(rset.getInt("grado")),
                        new String(rset.getInt("talumno_id") == 2 ? "S":"N"),
                        new Boolean(rset.getString("inscrito").equals("S")
                        ? true : false), "",
                        rset.getDouble("semanas_internado"),
                        rset.getString("Folio"),
                        new Double(0),
                        new Boolean(rset.getString("graduando").equals("S")
                        ? true : false));
                
                mAlumnos.put(this.matricula, alumno);
            }
            
            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new UMException(
                    "Error al obtener los datos generales del alumno " +
                    this.matricula + " en la carga " + cargaId + " y el bloque " +
                    bloque + "<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
        
        return mAlumnos;
    }
    
    private void getDependecia() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            String COMANDO = "SELECT INSTITUCION_ID, COALESCE(I.NOMBRE_INSTITUCION,'') NOMBRE, A.TIPO_ALUMNO ";
            COMANDO += "FROM ALUM_ACADEMICO A, CAT_INSTITUCION I ";
            COMANDO += "WHERE A.OBRERO_INSTITUCION = I.INSTITUCION_ID ";
            COMANDO += "AND A.CODIGO_PERSONAL = ? ";
            COMANDO += "AND A.TIPO_ALUMNO IN (2,6) ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.getMatricula());
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.setIdInstitucion(new Integer(rset.getInt("Institucion_ID")));
                this.setInstitucion(rset.getString("Nombre"));
            }
            
            rset.close();
            pstmt.close();
            
            if (((this.getTAlumno_id().compareTo(new Integer(2)) == 0) ||
                    (this.getTAlumno_id().compareTo(new Integer(6)) == 0)) &&
                    (this.getInstitucion().length() == 0)) {
                throw new UMException("Alumno " + this.getMatricula() +
                        " es hijo de obrero o un obrero ACFE " +
                        "y no tiene una dependencia." +
                        "<br>Favor de contactar a Finanzas Estudiantiles.");
            }
        } catch (Exception e) {
            throw new UMException(
                    "Error al obtener la dependencia del obrero para el alumno " +
                    this.getMatricula() + "<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
    }
    
    /**
     * @return Returns the institucion.
     */
    public String getInstitucion() {
        if (this.institucion == null) {
            this.institucion = "";
        }
        
        return this.institucion;
    }
    
    /**
     * @param institucion The institucion to set.
     */
    public void setInstitucion(String institucion) {
        this.institucion = institucion;
    }
    
    public void desInscribir(String matricula, String cargaId, Integer bloque,
            Connection conn, Connection conn_enoc) throws Exception {
        PreparedStatement pstmt = null;
        
        try {
            String COMANDO = "UPDATE FES_CCOBRO " + "SET INSCRITO = '?' " +
                    "WHERE MATRICULA = ? " + "AND CARGA_ID = ? " +
                    "AND BLOQUE = ? ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setString(2, cargaId);
            pstmt.setInt(3, bloque.intValue());
            pstmt.execute();
            pstmt.close();
            
            COMANDO = "UPDATE FES_CCOBRO " + "SET INSCRITO = 'N' " +
                    "WHERE MATRICULA = ? " + "AND CARGA_ID = ? " +
                    "AND BLOQUE = ? ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setString(2, cargaId);
            pstmt.setInt(3, bloque.intValue());
            pstmt.execute();
            pstmt.close();
            
            COMANDO = "UPDATE ALUM_ESTADO " + "SET ESTADO = 'M' " +
                    "WHERE CODIGO_PERSONAL = ? " + "AND CARGA_ID = ? " +
                    "AND BLOQUE_ID = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setString(2, cargaId);
            pstmt.setInt(3, bloque.intValue());
            pstmt.execute();
            pstmt.close();
        } catch (Exception e) {
            throw new UMException("Error al tratar de desinscribir al alumno " +
                    matricula + " en el calculo de cobro <br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }
    }
    
    public Map getAlumnosCC(String cargaId, Integer bloque)
    throws Exception {
        Map mAlumnos = new TreeMap();
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String COMANDO = "";
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            COMANDO = "SELECT MATRICULA, CARGA_ID, BLOQUE, NOMBRE, MODALIDAD_ID, MODALIDAD, " +
                    "TALUMNO_ID, TALUMNO, SEMESTRE, FECHA, FORMAPAGO, RELIGION, NACIONALIDAD, " +
                    "RESIDENCIA, FACULTAD_ID, FACULTAD, CARRERA_ID, CARRERA, PLAN_ID, NOMBRE_PLAN," +
                    "GRADO, INSCRITO, SEMANAS_INTERNADO, FOLIO, GRADUANDO " + "FROM FES_CCOBRO " +
                    "WHERE CARGA_ID = ? " + "AND BLOQUE = ? ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, cargaId);
            pstmt.setInt(2, bloque.intValue());
            rset = pstmt.executeQuery();
            
            while (rset.next()) {
                this.matricula = rset.getString("matricula");
                
                /*Obtener contabilidad del alumno*/
                this.getContabilidad();
                
                Alumno alumno = new Alumno(this.matricula, cargaId, bloque,
                        rset.getString("nombre"), this.getId_ccosto(),
                        new Integer(rset.getInt("modalidad_id")),
                        rset.getString("modalidad"),
                        new Integer(rset.getInt("talumno_id")),
                        rset.getString("talumno"),
                        new Integer(rset.getInt("semestre")),
                        rset.getString("fecha"), rset.getString("formapago"),
                        rset.getString("religion"),
                        rset.getString("nacionalidad"),
                        rset.getString("residencia"),
                        rset.getString("facultad_id"),
                        rset.getString("facultad"),
                        rset.getString("carrera_id"),
                        rset.getString("carrera"), rset.getString("plan_id"),
                        rset.getString("nombre_plan"),
                        new Integer(rset.getInt("grado")),
                        rset.getString("ho_status"),
                        new Boolean(rset.getString("inscrito").equals("S")
                        ? true : false), "",
                        rset.getDouble("semanas_internado"),
                        rset.getString("Folio"),
                        new Double(0),
                        new Boolean(rset.getString("graduando").equals("S")
                        ? true : false));
                
                mAlumnos.put(this.matricula, alumno);
            }
            
            pstmt.close();
            rset.close();
        } catch (Exception e) {
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
        
        return mAlumnos;
    }
    
    public Vector getAlumnoCC(String fechaI, String fechaF)
    throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Vector vAlumnos = new Vector();
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            String COMANDO =
                    "SELECT MATRICULA, CARGA_ID, BLOQUE, NOMBRE, MODALIDAD_ID, MODALIDAD, TALUMNO_ID, TALUMNO, " +
                    "SEMESTRE, TO_CHAR(FECHA,'DD/MM/YYYY') FECHA, FORMAPAGO, RELIGION, NACIONALIDAD, " +
                    "RESIDENCIA, FACULTAD_ID, FACULTAD, CARRERA_ID, CARRERA, PLAN_ID, NOMBRE_PLAN, " +
                    "GRADO, SEMANAS_INTERNADO, INSCRITO, FOLIO, GRADUANDO " + "FROM FES_CCOBRO " +
                    "WHERE FECHA BETWEEN TO_DATE(?,'DD/MM/YY') AND TO_DATE(?,'DD/MM/YY') " +
                    "AND INSCRITO = 'S' ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, fechaI);
            pstmt.setString(2, fechaF);
            
            rset = pstmt.executeQuery();
            
            while (rset.next()) {
                Alumno alumno = new Alumno(rset.getString("Matricula"),
                        rset.getString("Carga_id"),
                        new Integer(rset.getInt("bloque")),
                        rset.getString("Nombre"), "",
                        new Integer(rset.getInt("Modalidad_ID")),
                        rset.getString("Modalidad"),
                        new Integer(rset.getInt("TAlumno_ID")),
                        rset.getString("TAlumno"),
                        new Integer(rset.getInt("Semestre")),
                        rset.getString("Fecha"), rset.getString("FormaPago"),
                        rset.getString("Religion"),
                        rset.getString("Nacionalidad"),
                        rset.getString("Residencia"),
                        rset.getString("Facultad_ID"),
                        rset.getString("Facultad"),
                        rset.getString("Carrera_ID"),
                        rset.getString("Carrera"), rset.getString("Plan_ID"),
                        rset.getString("Nombre_Plan"),
                        new Integer(rset.getString("Grado")),
                        rset.getString("ho_status"),
                        new Boolean(rset.getString("Inscrito").equals("S")
                        ? true : false), "",
                        rset.getDouble("semanas_internado"),
                        rset.getString("Folio"),
                        new Double(0),
                        new Boolean(rset.getString("graduando").equals("S")
                        ? true : false));
                
                vAlumnos.add(alumno);
            }
            
            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new UMException(
                    "Error al obtener los datos de los alumnos inscritos <br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
        
        return vAlumnos;
    }
    
    /**
     * Obtener datos del alumno, pero no del calculo de cobro, ya que en el estado de cuenta pueden consultar
     * datos de cualquier alumno aunque no este inscrito actualmente
     * @param String matricula
     * @author osoto
     *
     * TODO To change the template for this generated type comment go to
     * Window - Preferences - Java - Code Style - Code Templates
     */
    public void getAlumno(String matricula) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            //En el estado de cuenta pueden consultar alumnos que por un largo periodo
            //no han estudiado, por lo que ni siquiera tengan datos academicos
            String COMANDO = "SELECT CASE NACIONALIDAD WHEN 91 THEN 'MEX' ELSE 'EXT' END AS PAIS, ";
            COMANDO += "NOMBRE || ' ' || APELLIDO_PATERNO || ' ' || APELLIDO_MATERNO NOMBRE ";
            COMANDO += "FROM ALUM_PERSONAL ";
            COMANDO += "WHERE CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.nacionalidad = rset.getString("Pais");
                
                if (rset.wasNull()) {
                    this.nacionalidad = "-";
                }
                
                this.nombre = rset.getString("Nombre");
                
                if (rset.wasNull()) {
                    throw new UMException("El alumno " + this.matricula +
                            " no tiene capturado un valor de pais valido<br>Favor de contactar al Dpto. de Registo");
                }
            }
            
            rset.close();
            pstmt.close();
            
            //Obtener datos de la tabla Alumno_Academico
            COMANDO = "SELECT MODALIDAD_ID, TIPO_ALUMNO, SEMESTRE, ";
            COMANDO += "CLAS_FIN, RESIDENCIA_ID, GRADO ";
            COMANDO += "FROM ALUM_ACADEMICO ";
            COMANDO += "WHERE CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.modalidad_id = new Integer(rset.getInt("Modalidad_ID"));
                
                if (rset.wasNull()) {
                    this.modalidad_id = new Integer(0);
                }
                
                this.tAlumno_id = new Integer(rset.getInt("Tipo_Alumno"));
                
                if (rset.wasNull()) {
                    this.tAlumno_id = new Integer(0);
                }
                
                this.religion = rset.getString("Clas_Fin");
                
                if (rset.wasNull()) {
                    this.religion = "-";
                }
                
                this.residencia = rset.getString("Residencia_ID");
                
                if (rset.wasNull()) {
                    this.residencia = "-";
                }
                
                this.semestre = new Integer(rset.getInt("Semestre"));
                
                if (rset.wasNull()) {
                    this.semestre = new Integer(0);
                }
                
                this.grado = new Integer(rset.getInt("Grado"));
                
                if (rset.wasNull()) {
                    this.grado = new Integer(0);
                }
            }
            
            rset.close();
            pstmt.close();
            
            //Obtener descripcion de modalidad
            COMANDO = "SELECT NOMBRE_MODALIDAD ";
            COMANDO += "FROM CAT_MODALIDAD ";
            COMANDO += "WHERE MODALIDAD_ID = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setInt(1, this.modalidad_id.intValue());
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.modalidad = rset.getString("Nombre_Modalidad");
            }
            
            rset.close();
            pstmt.close();
            
            if (this.modalidad == null) {
                this.modalidad = "-";
            }
            
            //Obtener descripcion de tipo alumno
            COMANDO = "SELECT NOMBRE_TIPO ";
            COMANDO += "FROM CAT_TIPOALUMNO ";
            COMANDO += "WHERE TIPO_ID = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setInt(1, this.tAlumno_id.intValue());
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.tAlumno = rset.getString("Nombre_Tipo");
            }
            
            rset.close();
            pstmt.close();
            
            if (this.tAlumno == null) {
                if (rset.wasNull()) {
                    this.tAlumno = "-";
                }
            }
            
            //Obtener Facultad y Carrera
            COMANDO = "SELECT F.FACULTAD_ID, F.NOMBRE_FACULTAD, ";
            COMANDO += "C.CARRERA_ID, C.NOMBRE_CARRERA, ";
            COMANDO += "P.PLAN_ID, MP.NOMBRE_PLAN ";
            COMANDO += "FROM ALUM_PLAN P, MAPA_PLAN MP, ";
            COMANDO += "CAT_CARRERA C, CAT_FACULTAD F ";
            COMANDO += "WHERE F.FACULTAD_ID = C.FACULTAD_ID ";
            COMANDO += "AND C.CARRERA_ID = MP.CARRERA_ID ";
            COMANDO += "AND MP.PLAN_ID = P.PLAN_ID ";
            COMANDO += "AND P.ESTADO = '1' ";
            COMANDO += "AND P.CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.facultad_id = rset.getString("Facultad_ID");
                
                if (rset.wasNull()) {
                    this.facultad_id = "-";
                }
                
                this.facultad = rset.getString("Nombre_Facultad");
                
                if (rset.wasNull()) {
                    this.facultad = "-";
                }
                
                this.carrera_id = rset.getString("Carrera_ID");
                
                if (rset.wasNull()) {
                    this.carrera_id = "-";
                }
                
                this.carrera = rset.getString("Nombre_Carrera");
                
                if (rset.wasNull()) {
                    this.carrera = "-";
                }
                
                this.plan_id = rset.getString("Plan_ID");
                
                if (rset.wasNull()) {
                    this.plan_id = "-";
                }
                
                this.nombre_plan = rset.getString("Nombre_Plan");
                
                if (rset.wasNull()) {
                    this.nombre_plan = "-";
                }
            }
            
            rset.close();
            pstmt.close();
            
            if (this.plan_id == null) {
                this.plan_id = "-";
            }
        } catch (Exception e) {
            throw new UMException("Error al tratar de obtener los datos del alumno " +
                    matricula + "<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
    }
    
    /**
     * Obtiene el nombre del alumno, y lo asigna a la propiedad correspondiente
     * @param matricula
     * @return String
     * @throws Exception
     */
    public String getNombre(String matricula) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            String COMANDO =
                    "SELECT APELLIDO_PATERNO || ' ' || APELLIDO_MATERNO || ' ' || NOMBRE NOMBRE " +
                    "FROM ALUM_PERSONAL " + "WHERE CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.setNombre(rset.getString("Nombre"));
                
                if (rset.wasNull()) {
                    throw new UMException("El nombre del alumno " + matricula +
                            " es invalido");
                }
            }
            
            pstmt.close();
            rset.close();
            
            return this.getNombre();
        } catch (Exception e) {
            throw new UMException("Error al tratar de obtener los datos del alumno " +
                    matricula + " <br> " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
    }
    
    /**
     * Metodo que obtiene los datos del alumno, pero NO del calculo de cobro
     * Este metodo es util para la consulta del estado de cuenta de alumnos
     * Solo la carga y el bloque se obtiene del calculo de cobro
     * @param matricula
     * @throws Exception
     */
    public void getDatosAlumno(String matricula) throws Exception {
        try {
            this.setMatricula(matricula);
            
            //Obtener datos del alumno
            this.getAlumno();
            
            //Obtener carga y bloque actuales del alumno
            this.getCargaBloqueCC();
        } catch (Exception e) {
            throw new UMException("Error al tratar de obtener los datos del alumno " +
                    matricula + "<br>" + e);
        }
    }
    
    /**
     * Metodo que obtiene los datos del alumno, pero NO del calculo de cobro
     * Este metodo es util para la consulta del estado de cuenta de alumnos
     * Solo la carga y el bloque se obtiene del calculo de cobro,
     * pero sin validar si estos ultimos existen
     * @param matricula
     * @throws Exception
     */
    public void getDatosAlumnoSinValidar(String matricula)
    throws Exception {
        try{
            this.setMatricula(matricula);
            
            //Obtener datos del alumno
            this.getAlumno();
            
            //Obtener carga y bloque actuales del alumno
            this.getCargaBloqueCC();
            
        } catch (Exception e) {
            //
        }
    }
    
    /**
     * Obtener rango de alumnos obteniendo y validando que tengan una carga y un bloque
     * @param matriculaI
     * @param matriculaF
     * @return
     */
    public Vector getAlumnos(String matriculaI, String matriculaF)
    throws Exception {
        Vector vAlumnos = new Vector();
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            String COMANDO = "SELECT CODIGO_PERSONAL " + "FROM ALUM_PERSONAL " +
                    "WHERE CODIGO_PERSONAL BETWEEN ? AND ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, matriculaI);
            pstmt.setString(2, matriculaF);
            
            rset = pstmt.executeQuery();
            
            while (rset.next()) {
                Alumno alumno = new Alumno();
                alumno.getDatosAlumno(rset.getString("Codigo_Personal"));
                
                vAlumnos.add(alumno);
            }
            
            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new UMException("Error al obtener rango de alumnos<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
        
        return vAlumnos;
    }
    
    /**
     * Obtener todos los alumnos registrados en la base de datos,
     * tratando de obtener su carga y bloque, pero sin generarse un error si estos datos no existen
     * @param Carga
     * @param Map
     * @return Map
     */
    public Map getTodosAlumnos(Carga carga, Map mAlumnos) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            //La busqueda la ligamos a los kardex, para asi leer solo alumnos que hayan estado estudiando
            //ultimamente, o que ya tengan carga academica
            String COMANDO = "SELECT P.CODIGO_PERSONAL, P.NOMBRE || ' ' || P.APELLIDO_PATERNO || ' ' || P.APELLIDO_MATERNO NOMBRE_LEGAL, A.MODALIDAD_ID, A.TIPO_ALUMNO, T.NOMBRE_TIPO, P.RELIGION_ID, P.NACIONALIDAD, A.RESIDENCIA_ID " +
                    "FROM ALUM_PERSONAL P, ALUM_ACADEMICO A, CAT_TIPOALUMNO T " +
                    "WHERE A.CODIGO_PERSONAL = P.CODIGO_PERSONAL " +
                    "AND T.TIPO_ID = A.TIPO_ALUMNO " +
                    "AND P.CODIGO_PERSONAL IN " +
                    "(SELECT CODIGO_PERSONAL FROM KRDX_CURSO_ACT " +
                    "WHERE CURSO_CARGA_ID LIKE ? ) ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, carga.getCargaId()+"%");
            rset = pstmt.executeQuery();
            
            while (rset.next()) {
                Alumno alumno = new Alumno(rset.getString("Codigo_Personal"), rset.getString("Nombre_Legal"), new Integer(rset.getInt("Modalidad_ID")), new Integer(rset.getInt("Tipo_Alumno")), rset.getString("Nombre_Tipo"), rset.getString("Religion_ID"), rset.getString("Nacionalidad"), rset.getString("Residencia_ID"));
                
                mAlumnos.put(alumno.getMatricula(), alumno);
            }
            
            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new UMException("Error al obtener todos los alumnos<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
        
        return mAlumnos;
    }
    
    public void getCargaBloqueCC() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            String COMANDO = "SELECT CARGA_ID, BLOQUE ";
            COMANDO += "FROM FES_CCOBRO ";
            COMANDO += "WHERE MATRICULA = ? ";
            COMANDO += "AND FECHA IN ";
            COMANDO += "(SELECT MAX(FECHA) ";
            COMANDO += "FROM FES_CCOBRO ";
            COMANDO += "WHERE MATRICULA = ?) ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            pstmt.setString(2, this.matricula);
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.carga_id = rset.getString("Carga_ID");
                this.bloque = new Integer(rset.getInt("Bloque"));
            }
            
            pstmt.close();
            rset.close();
            
            if (this.carga_id == null) {
                throw new UMException("El alumno " + this.matricula +
                        " no esta inscrito en ningun bloque");
            }
        } catch (Exception e) {
            throw new UMException(
                    "Error al intentar obtener la carga y bloque de la ultima inscripcion del alumno " +
                    this.matricula);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
    }
    
    public void getCtaMayor(String ejercicio) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            //Obtener la cuenta de mayor del estudiante
            String COMANDO = "SELECT ID_CTAMAYOR ";
            COMANDO += "FROM CONT_RELACION ";
            COMANDO += "WHERE ID_EJERCICIO = ? ";
            COMANDO += "AND ID_AUXILIAR = ? ";
            COMANDO += "AND STATUS = 'A' ";
            
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, ejercicio);
            pstmt.setString(2, this.matricula);
            
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                this.idCtaMayor = rset.getString("ID_CtaMayor");
            }
            
            pstmt.close();
            rset.close();
            
            if (this.idCtaMayor == null) {
                throw new UMException("Matricula " + this.matricula + " invalida!");
            }
        } catch (Exception e) {
            throw new UMException(
                    "Error al intentar obtener la cuenta de mayor del estudiante");
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
    }
    
    /**
     * Obtiene a las diferentes cuentas y contabilidades registradas del alumno
     * @param idEjercicio
     * @return Map
     * @throws Exception
     */
    public Map getCuentasAlumnos(String idEjercicio, Alumno alumno)
    throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Map mCuentas = new TreeMap();
        Vector vCuentas = new Vector();
        String tmp_matricula = null;
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            //Obtener las cuentas de los alumnos
            String COMANDO = "SELECT DISTINCT ID_CCOSTO, ID_CTAMAYOR ";
            COMANDO += "FROM CONT_RELACION ";
            COMANDO += "WHERE ID_EJERCICIO = ? ";
            COMANDO += "AND ID_CCOSTO IN ('1.01','2.01') ";
            COMANDO += "AND ID_CTAMAYOR IN ('1.1.04.01','1.1.04.29','1.1.04.30') ";
            COMANDO += "AND ID_AUXILIAR = ? ";
            COMANDO += "GROUP BY ID_CCOSTO, ID_CTAMAYOR ";
            
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, idEjercicio);
            pstmt.setString(2, alumno.getMatricula());
            
            rset = pstmt.executeQuery();
            //log.debug("getCuentasAlumnos 1");
            
            while (rset.next()) {
                Ejercicio ejercicio = new Ejercicio(idEjercicio);
                //log.debug("getCuentasAlumnos 2");
                
                Relacion cuenta = new Relacion(ejercicio,
                        new CtaMayor().getCtaMayor(ejercicio,
                        rset.getString("id_CtaMayor")),
                        new CCosto().getCCosto(ejercicio,
                        rset.getString("id_CCosto")));
                //log.debug("getCuentasAlumnos 3");
                vCuentas = new Vector();
                vCuentas.add(cuenta);
                //log.debug("getCuentasAlumnos 6");
            }
            
            pstmt.close();
            rset.close();
            
            mCuentas.put(alumno.getMatricula(), vCuentas);
            //log.debug("getCuentasAlumnos 7");
        } catch (Exception e) {
            throw new UMException("Error al intentar obtener las cuentas del alumno " +
                    alumno + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
        
        return mCuentas;
    }
    
    public Double getCargosPeriodo(HttpSession session, CCosto ccosto,
            CtaMayor ctaMayor, Auxiliar auxiliar, String fechaI, String fechaF)
            throws Exception {
        Double cargos = null;
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            //Obtener los cargos del alumno en cualquier contabilidad y
            //en cualquiera de las tres cuentas de estudiantes (activos, pasivos e incobrables)
            Metodos1 m = new Metodos1(conn);
            Ejercicio ejercicio = new Ejercicio((String) session.getAttribute(
                    "id_ejercicio"));
            
            cargos = new Double(m.getTotalCargosAlumnos(
                    ejercicio.getIdEjercicio(), ctaMayor.getIdCtaMayor(),
                    ccosto.getIdCCosto(), auxiliar.getIdAuxiliar(), fechaI,
                    fechaF, this.getCarga_id(), this.getBloque()));
        } catch (Exception e) {
            throw new UMException("Error al obtener los cargos del periodo " + e);
        } finally {
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
        
        return cargos;
    }
    
    public Double getCreditosPeriodo(HttpSession session, CCosto ccosto,
            CtaMayor ctaMayor, Auxiliar auxiliar, String fechaI, String fechaF)
            throws Exception {
        Double creditos = null;
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            //Obtener los creditos del alumno en cualquier contabilidad y
            //en cualquiera de las tres cuentas de estudiantes (activos, pasivos e incobrables)
            Metodos1 m = new Metodos1(conn);
            Ejercicio ejercicio = new Ejercicio((String) session.getAttribute(
                    "id_ejercicio"));
            
            creditos = new Double(m.getTotalCreditosAlumnos(
                    ejercicio.getIdEjercicio(), ctaMayor.getIdCtaMayor(),
                    ccosto.getIdCCosto(), auxiliar.getIdAuxiliar(), fechaI,
                    fechaF, this.getCarga_id(), this.getBloque()));
        } catch (Exception e) {
            throw new UMException("Error al obtener los creditos del periodo " + e);
        } finally {
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
        
        return creditos;
    }
    
    public Double getSaldoAnteriorPeriodo(HttpSession session, CCosto ccosto,
            CtaMayor ctaMayor, Auxiliar auxiliar, String fechaI)
            throws Exception {
        Double sAnterior = null;
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            //Obtener los sAnterior del alumno en cualquier contabilidad y
            //en cualquiera de las tres cuentas de estudiantes (activos, pasivos e incobrables)
            Metodos1 m = new Metodos1(conn);
            Ejercicio ejercicio = new Ejercicio((String) session.getAttribute(
                    "id_ejercicio"));
            
            sAnterior = new Double(m.getSaldoAnterior(
                    ejercicio.getIdEjercicio(), ctaMayor.getIdCtaMayor(),
                    ccosto.getIdCCosto(), auxiliar.getIdAuxiliar(), fechaI));
        } catch (Exception e) {
            throw new UMException("Error al obtener el saldo anterior del periodo " +
                    e);
        } finally {
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
        
        return sAnterior;
    }
    
    public Boolean isContabilizado() throws Exception {
        Boolean returnV = new Boolean(false);
        PreparedStatement pstmt = null;
        Integer nReg = new Integer(0);
        ResultSet rset = null;
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            String COMANDO = "SELECT COUNT(*) NREG " + "FROM CONT_MOVIMIENTO " +
                    "WHERE ID_AUXILIARM = ? " + "AND REFERENCIA2 = ? ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, this.getMatricula());
            pstmt.setString(2, this.getCarga_id() + this.getBloque());
            
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                nReg = new Integer(rset.getInt("NReg"));
            }
            
            pstmt.close();
            rset.close();
            
            if (nReg.compareTo(new Integer(0)) > 0) {
                returnV = new Boolean(true);
            }
        } catch (Exception e) {
            throw new UMException("Error al evaluar si el alumno " +
                    this.getMatricula() +
                    " tiene los movimientos de inscripcion contabilizados " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }
        
        return returnV;
    }
    
    public List<String> getCargasActivas () throws Exception{
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        List <String> cargas = null;

        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionMateo(new Boolean(false));
            }
            String COMANDO = "SELECT CARGA_ID ";
            COMANDO += "FROM ENOC.CARGA ";
            COMANDO += "WHERE TO_DATE(SYSDATE,'dd-mm-yy') BETWEEN F_INICIO AND F_FINAL ";

            pstmt = conn_enoc.prepareStatement(COMANDO);
            rset = pstmt.executeQuery();

            cargas = new ArrayList();
            while(rset.next()){
                cargas.add(rset.getString("Carga_id"));
            }

        } catch (Exception e) {
            throw new UMException("Error al obtener las cargas activas del alumno " +
                    this.getMatricula()  + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }

            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
        
        return cargas;
    }

    public Map<String,String> getCargasActivasFormalesInMap() throws Exception{
        List <String> cargas = this.getCargasActivas();
        List <String> rCargas = new ArrayList();

        for(String c : cargas){
            //Solo se quiere leer las cargas que son de estudios formales, las cuales estan en el rango entre 1 y 3. Ej. 09101C vs 09104C
            if(c.matches("\\d{4}[1-3]?.$")){
                rCargas.add(c);
            }
        }

        Map <String, String> mCargas = new TreeMap<String,String>();
        for(String c : rCargas){
            mCargas.put(c, c);
        }

        return mCargas;
    }


    public Map<String,String> getCargasActivasParaBachilleres() throws Exception{
        Map<String,String> mCargas = this.getCargasActivasFormalesInMap();
        Map <String,String> cargasBach = new TreeMap<String,String>();

        //Las cargas de Bachilleres, tienen el formato [0..9][0..9][0..9][0..9]3[A..D]
        //Las cargas de Universidad, tienen el formato [0..9][0..9][0..9][0..9]1[A..D]
        //Las cargas de Nocturna, tienen el formato [0..9][0..9][0..9][0..9]2[A..D]
        for(Map.Entry<String,String> c : mCargas.entrySet()){
            //Solo se quiere leer las cargas que son de estudios formales, las cuales estan en el rango entre 1 y 3. Ej. 09101C vs 09104C
            if(c.getKey().matches("\\d{4}[3]?.$")){
                cargasBach.put(c.getKey(),c.getValue());
            }
        }

        return cargasBach;
    }

    public Map<String,String> getCargasActivasParaUniversidad() throws Exception{
        Map<String,String> mCargas = this.getCargasActivasFormalesInMap();
        Map <String,String> cargasUniv = new TreeMap<String,String>();

        //Las cargas de Bachilleres, tienen el formato [0..9][0..9][0..9][0..9]3[A..D]
        //Las cargas de Universidad, tienen el formato [0..9][0..9][0..9][0..9]1[A..D]
        //Las cargas de Nocturna, tienen el formato [0..9][0..9][0..9][0..9]2[A..D]
        for(Map.Entry<String,String> c : mCargas.entrySet()){
            //Solo se quiere leer las cargas que son de estudios formales, las cuales estan en el rango entre 1 y 3. Ej. 09101C vs 09104C
            if(c.getKey().matches("\\d{4}[1]?.$")){
                cargasUniv.put(c.getKey(),c.getValue());
            }
        }

        return cargasUniv;
    }

    public List<String> getUltimaCargaInscritaFormal() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List <String> cargas = new ArrayList<String>();

        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }

            String COMANDO = "SELECT CARGA_ID, BLOQUE ";
            COMANDO += "FROM MATEO.FES_CCOBRO ";
            COMANDO += "WHERE MATRICULA = ? ";
            COMANDO += "AND FECHA IN ";
            COMANDO += "(SELECT MAX(FECHA) ";
            COMANDO += "FROM MATEO.FES_CCOBRO ";
            COMANDO += "WHERE MATRICULA = ? ";
            COMANDO += "AND INSCRITO = 'S' ) ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, this.matricula);
            pstmt.setString(2, this.matricula);
            rset = pstmt.executeQuery();

            String carga = null;
            while (rset.next()) {
                carga = rset.getString("Carga_ID");

                //Evaluar si la carga es formal
                if(carga.matches("\\d{4}[1-3]?.$")){
                    cargas.add(carga);
                }
            }

            pstmt.close();
            rset.close();

            
        } catch (Exception e) {
            throw new UMException(
                    "Error al intentar obtener la carga y bloque de la ultima inscripcion del alumno " +
                    this.matricula + " "+ e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }

            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }

         return cargas;
    }
    /**
     * Si la carga actual que tiene asignada el Alumno, esta <b>activa</b> y tiene el formato de las cargas de universidad, regresa TRUE
     * @return
     * @throws SQLException
     */
    public Boolean isUniversitario() throws Exception {

        if(this.getCargasActivasParaUniversidad().containsKey(this.carga_id)){
            return true;
        }
        return false;
    }
    /**
     * Si la carga actual que tiene asignada el Alumno, esta <b>activa</b> y tiene el formato de las cargas de bachilleres, regresa TRUE
     * @return
     * @throws SQLException
     */
    public Boolean isBachilleres() throws Exception {

        if(this.getCargasActivasParaBachilleres().containsKey(this.carga_id)){
            return true;
        }
        return false;
    }

    /**
     * Obtiene la ultimaCargaInscritaFormal del alumno, y la compara con la carga que tiene asignada en este momento. Si coincide el formato de la carga
     * indica que no es de primer ingreso.
     * @return
     * @throws Exception
     */
    public Boolean isPrimerIngreso() throws Exception {
        Boolean sw = true;
        List <String> cargas = this.getUltimaCargaInscritaFormal();
        for(String c : cargas){
            //log.debug("isPrimerIngreso - cargaId {} , {}",c,"\\d{4}["+this.carga_id.substring(4, 5)+"]?.$"+" = "+(c.matches("\\d{4}["+this.carga_id.substring(4, 5)+"]?.$")));
            if(c.matches("\\d{4}["+this.carga_id.substring(4, 5)+"]?.$")){
                sw = false;
            }
        }
        return sw;
    }
    
}
