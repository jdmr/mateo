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
import java.util.TreeMap;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Facultad {
	private String facultadId;
	private String titulo;
	private String nombreFacultad;
	private String codigoPersonal;
	private Connection conn;
	/**
	 * @param facultadId
	 * @param nombreFacultad
	 */
	public Facultad(String facultadId, String nombreFacultad) {
		super();
		this.facultadId = facultadId;
		this.nombreFacultad = nombreFacultad;
	}
	/**
	 * @param facultadId
	 * @param titulo
	 * @param nombreFacultad
	 * @param codigoPersonal
	 */
	public Facultad(String facultadId, String titulo, String nombreFacultad,
			String codigoPersonal) {
		super();
		this.facultadId = facultadId;
		this.titulo = titulo;
		this.nombreFacultad = nombreFacultad;
		this.codigoPersonal = codigoPersonal;
	}
	/**
	 * 
	 */
	public Facultad() {
		super();
		// TODO Auto-generated constructor stub
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
	 * @return Returns the facultadId.
	 */
	public String getFacultadId() {
		return facultadId;
	}
	/**
	 * @param facultadId The facultadId to set.
	 */
	public void setFacultadId(String facultadId) {
		this.facultadId = facultadId;
	}
	/**
	 * @return Returns the nombreFacultad.
	 */
	public String getNombreFacultad() {
		return nombreFacultad;
	}
	/**
	 * @param nombreFacultad The nombreFacultad to set.
	 */
	public void setNombreFacultad(String nombreFacultad) {
		this.nombreFacultad = nombreFacultad;
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
	public Map getFacultades() throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Map mFacultades = new TreeMap();
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionEnoc(new Boolean(false));
			Object obj = null;
			
			String COMANDO = "SELECT FACULTAD_ID, NOMBRE_FACULTAD " +
					"FROM enoc.CAT_FACULTAD ";
			pstmt = conn.prepareStatement(COMANDO);
			rset = pstmt.executeQuery();
			
			while(rset.next()){				
				mFacultades.put(rset.getString("Facultad_ID"), new Facultad(rset.getString("Facultad_ID"), rset.getString("Nombre_Facultad")));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener las facultades <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		
		return mFacultades;
	}
}
