/*
 * Created on Dec 23, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import mx.edu.um.mateo.inscripciones.model.ccobro.cuenta.CCosto;
import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Carrera {
	private Facultad facultad;
	private String carreraId;
	private Integer nivelId;
	private String titulo;
	private String nombreCarrera;
	private String nombreCorto;
	private CCosto ccosto;
	private String codigoPersonal;
	private String nivelContable;
	private Connection conn;
	/**
	 * @param carreraId
	 */
	public Carrera(String carreraId) {
		super();
		this.carreraId = carreraId;
	}
	/**
	 * @param facultad
	 * @param carreraId
	 * @param nombreCarrera
	 */
	public Carrera(Facultad facultad, String carreraId, String nombreCarrera) {
		super();
		this.facultad = facultad;
		this.carreraId = carreraId;
		this.nombreCarrera = nombreCarrera;
	}
	/**
	 * @param facultad
	 * @param carreraId
	 * @param nivelId
	 * @param titulo
	 * @param nombreCarrera
	 * @param nombreCorto
	 * @param ccosto
	 * @param codigoPersonal
	 * @param nivelContable
	 */
	public Carrera(Facultad facultad, String carreraId, Integer nivelId,
			String titulo, String nombreCarrera, String nombreCorto,
			CCosto ccosto, String codigoPersonal, String nivelContable) {
		super();
		this.facultad = facultad;
		this.carreraId = carreraId;
		this.nivelId = nivelId;
		this.titulo = titulo;
		this.nombreCarrera = nombreCarrera;
		this.nombreCorto = nombreCorto;
		this.ccosto = ccosto;
		this.codigoPersonal = codigoPersonal;
		this.nivelContable = nivelContable;
	}
	/**
	 * 
	 */
	public Carrera() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return Returns the carreraId.
	 */
	public String getCarreraId() {
		return carreraId;
	}
	/**
	 * @param carreraId The carreraId to set.
	 */
	public void setCarreraId(String carreraId) {
		this.carreraId = carreraId;
	}
	/**
	 * @return Returns the ccosto.
	 */
	public CCosto getCcosto() {
		return ccosto;
	}
	/**
	 * @param ccosto The ccosto to set.
	 */
	public void setCcosto(CCosto ccosto) {
		this.ccosto = ccosto;
	}
	/**
	 * @return Returns the codigoPersonal.
	 */
	public String getCodigoPersonal() {
		return codigoPersonal;
	}
	/**
	 * @param codigoPersonal The codigoPersonal to set.
	 */
	public void setCodigoPersonal(String codigoPersonal) {
		this.codigoPersonal = codigoPersonal;
	}
	/**
	 * @return Returns the facultad.
	 */
	public Facultad getFacultad() {
		return facultad;
	}
	/**
	 * @param facultad The facultad to set.
	 */
	public void setFacultad(Facultad facultad) {
		this.facultad = facultad;
	}
	/**
	 * @return Returns the nivelContable.
	 */
	public String getNivelContable() {
		return nivelContable;
	}
	/**
	 * @param nivelContable The nivelContable to set.
	 */
	public void setNivelContable(String nivelContable) {
		this.nivelContable = nivelContable;
	}
	/**
	 * @return Returns the nivelId.
	 */
	public Integer getNivelId() {
		return nivelId;
	}
	/**
	 * @param nivelId The nivelId to set.
	 */
	public void setNivelId(Integer nivelId) {
		this.nivelId = nivelId;
	}
	/**
	 * @return Returns the nombreCarrera.
	 */
	public String getNombreCarrera() {
		return nombreCarrera;
	}
	/**
	 * @param nombreCarrera The nombreCarrera to set.
	 */
	public void setNombreCarrera(String nombreCarrera) {
		this.nombreCarrera = nombreCarrera;
	}
	/**
	 * @return Returns the nombreCorto.
	 */
	public String getNombreCorto() {
		return nombreCorto;
	}
	/**
	 * @param nombreCorto The nombreCorto to set.
	 */
	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}
	/**
	 * @return Returns the titulo.
	 */
	public String getTitulo() {
		return titulo;
	}
	/**
	 * @param titulo The titulo to set.
	 */
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public void getCarreras(Facultad facultad, Map mCarreras) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rset = null;		
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionEnoc(new Boolean(false));
			
			String COMANDO = "SELECT CARRERA_ID, NOMBRE_CARRERA " +
					"FROM CAT_CARRERA " +
					"WHERE FACULTAD_ID = ? ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, facultad.getFacultadId());
			rset = pstmt.executeQuery();
			
			while(rset.next()){				
				mCarreras.put(rset.getString("Carrera_ID"), new Carrera(facultad, rset.getString("Carrera_ID"), rset.getString("Nombre_Carrera")));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener las carreras <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}	
	}
}
