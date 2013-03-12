/*
 * Created on Dec 21, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.cuenta;

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
public class CCosto {
	/**
	 * @param ejercicio
	 * @param idCCosto
	 */
	public CCosto(Ejercicio ejercicio, String idCCosto) {
		super();
		this.ejercicio = ejercicio;
		this.idCCosto = idCCosto;
	}
	private Ejercicio ejercicio;
	private String idCCosto;
	private String nombre;
	private String detalle;
	private Connection conn;
	/**
	 * 
	 */
	public CCosto() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param idEjercicio
	 * @param idCCosto
	 * @param nombre
	 * @param detalle
	 */
	public CCosto(Ejercicio idEjercicio, String idCCosto, String nombre,
			String detalle) {
		super();
		this.ejercicio = idEjercicio;
		this.idCCosto = idCCosto;
		this.nombre = nombre;
		this.detalle = detalle;
	}
	/**
	 * @return Returns the detalle.
	 */
	public String getDetalle() {
		return detalle;
	}
	/**
	 * @param detalle The detalle to set.
	 */
	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	/**
	 * @return Returns the idCCosto.
	 */
	public String getIdCCosto() {
		return idCCosto;
	}
	/**
	 * @param idCCosto The idCCosto to set.
	 */
	public void setIdCCosto(String idCCosto) {
		this.idCCosto = idCCosto;
	}
	/**
	 * @return Returns the idEjercicio.
	 */
	public Ejercicio getEjercicio() {
		return ejercicio;
	}
	/**
	 * @param idEjercicio The idEjercicio to set.
	 */
	public void setEjercicio(Ejercicio idEjercicio) {
		this.ejercicio = idEjercicio;
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
	 * 
	 * @param ejercicio
	 * @param idCCosto
	 * @return
	 * @throws Exception
	 */
	public CCosto getCCosto(Ejercicio ejercicio, String idCCosto) throws Exception {		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		CCosto ccosto = null;
		
		try{
			if(this.conn == null || this.conn.isClosed())
			conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "SELECT ID_CCOSTO, NOMBRE, DETALLE " +
					"FROM CONT_CCOSTO " +
					"WHERE ID_EJERCICIO = ? " +
					"AND ID_CCOSTO = ? ";
			
			pstmt = this.conn.prepareStatement(COMANDO);
			pstmt.setString(1, ejercicio.getIdEjercicio());
			pstmt.setString(2, idCCosto);
			rset = pstmt.executeQuery();
			
			if(rset.next()){
				ccosto = new CCosto(ejercicio, rset.getString("ID_CCosto"), rset.getString("Nombre"),
						rset.getString("Detalle"));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener el centros de costo "+idCCosto+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		
		return ccosto;
	}
	/**
	 * 
	 * @return Map
	 * @throws Exception
	 */
	public Map getCCostos(Ejercicio ejercicio) throws Exception {
		Map mCCostos = new TreeMap();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try{
			if(this.conn == null || this.conn.isClosed())
			conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "SELECT ID_CCOSTO, NOMBRE, DETALLE " +
					"FROM CONT_CCOSTO " +
					"WHERE ID_EJERCICIO = ? ";
			
			pstmt = this.conn.prepareStatement(COMANDO);
			pstmt.setString(1, ejercicio.getIdEjercicio());
			rset = pstmt.executeQuery();
			
			while(rset.next()){
				mCCostos.put(ejercicio.getIdEjercicio()+rset.getString("Id_CCosto"), 
						new CCosto(ejercicio, rset.getString("ID_CCosto"), rset.getString("Nombre"),
						rset.getString("Detalle")));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener los centros de costo "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		
		return mCCostos;
	}
	/**
	 * Crea un nuevo centro de costo en la base de datos
	 * @param cCosto
	 * @param conn La conexion se debe pasar porque este metodo puede formar parte de un proceso con transaccion
	 * @throws Exception
	 */
	public void setCCosto(CCosto cCosto, Connection conn) throws Exception{
		PreparedStatement pstmt = null;		
		
		try{
			if(conn == null || conn.isClosed())
			conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "INSERT INTO CONT_CCOSTO " +
					"(ID_EJERCICIO, ID_CCOSTO, NOMBRE, DETALLE) " +
					"VALUES " +
					"(?,?,?,?) ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, cCosto.getEjercicio().getIdEjercicio());
			pstmt.setString(2, cCosto.getIdCCosto());
			pstmt.setString(3, cCosto.getNombre());
			pstmt.setString(4, cCosto.getDetalle());			
			pstmt.execute();
			pstmt.close();
			
		}catch(Exception e){
			throw new Error("Error al insertar el centro de costo "+cCosto.getIdCCosto()+" "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}			
		}
	}
}
