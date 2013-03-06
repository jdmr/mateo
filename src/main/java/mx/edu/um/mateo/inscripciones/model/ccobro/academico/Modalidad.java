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
public class Modalidad {
	/**
	 * @param modalidadId
	 */
	public Modalidad(Integer modalidadId) {
		super();
		this.modalidadId = modalidadId;
	}
	private Integer modalidadId;	
	private String nombreModalidad;
	private Connection conn;
	/**
	 * @param modalidadId
	 * @param nombreModalidad
	 */
	public Modalidad(Integer modalidadId, String nombreModalidad) {
		super();
		this.modalidadId = modalidadId;
		this.nombreModalidad = nombreModalidad;
	}
	/**
	 * 
	 */
	public Modalidad() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return Returns the modalidadId.
	 */
	public Integer getModalidadId() {
		return modalidadId;
	}
	/**
	 * @param modalidadId The modalidadId to set.
	 */
	public void setModalidadId(Integer modalidadId) {
		this.modalidadId = modalidadId;
	}
	/**
	 * @return Returns the nombreModalidad.
	 */
	public String getNombreModalidad() {
		return nombreModalidad;
	}
	/**
	 * @param nombreModalidad The nombreModalidad to set.
	 */
	public void setNombreModalidad(String nombreModalidad) {
		this.nombreModalidad = nombreModalidad;
	}
	public Map getModalidades() throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Map mModalidades = new TreeMap();
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionEnoc(new Boolean(false));
			
			String COMANDO = "SELECT MODALIDAD_ID, NOMBRE_MODALIDAD " +
					"FROM CAT_MODALIDAD ";
			pstmt = conn.prepareStatement(COMANDO);
			rset = pstmt.executeQuery();
			
			while(rset.next()){				
				mModalidades.put(new Integer(rset.getString("Modalidad_ID")), new Modalidad(new Integer(rset.getInt("Modalidad_ID")), rset.getString("Nombre_Modalidad")));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener las modalidades <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		
		return mModalidades;
	}
}
