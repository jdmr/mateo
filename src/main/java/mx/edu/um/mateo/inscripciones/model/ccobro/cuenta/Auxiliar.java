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
public class Auxiliar {
	private Ejercicio ejercicio;
	private String idAuxiliar;
	private String nombre;
	private String detalle;
	private Connection conn;
	/**
	 * @param ejercicio
	 * @param idAuxiliar
	 */
	public Auxiliar(Ejercicio ejercicio, String idAuxiliar) {
		super();
		this.ejercicio = ejercicio;
		this.idAuxiliar = idAuxiliar;
	}
	/**
	 * 
	 */
	public Auxiliar() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param idEjercicio
	 * @param idAuxiliar
	 * @param nombre
	 * @param detalle
	 */
	public Auxiliar(Ejercicio idEjercicio, String idAuxiliar, String nombre,
			String detalle) {
		super();
		this.ejercicio = idEjercicio;
		this.idAuxiliar = idAuxiliar;
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
	 * @return Returns the idAuxiliar.
	 */
	public String getIdAuxiliar() {
		return idAuxiliar;
	}
	/**
	 * @param idAuxiliar The idAuxiliar to set.
	 */
	public void setIdAuxiliar(String idAuxiliar) {
		this.idAuxiliar = idAuxiliar;
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
	 * @return Map
	 * @throws Exception
	 */
	public Map getAuxiliares(Ejercicio ejercicio) throws Exception {
		Map mAuxiliares = new TreeMap();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try{
			if(this.conn == null || this.conn.isClosed())
			conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "SELECT ID_AUXILIAR, NOMBRE, DETALLE " +
					"FROM CONT_AUXILIAR " +
					"WHERE ID_EJERCICIO = ? ";
			
			pstmt = this.conn.prepareStatement(COMANDO);
			pstmt.setString(1, ejercicio.getIdEjercicio());
			rset = pstmt.executeQuery();
			
			while(rset.next()){
				mAuxiliares.put(ejercicio.getIdEjercicio()+rset.getString("Id_Auxiliar"), 
						new Auxiliar(ejercicio, rset.getString("ID_Auxiliar"), rset.getString("Nombre"),
						rset.getString("Detalle")));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener los auxiliares "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		
		return mAuxiliares;
	}
	/**
	 * Crea un nuevo auxiliar en la base de datos
	 * @param auxiliar
	 * @param conn La conexion se debe pasar porque este metodo puede formar parte de un proceso con transaccion
	 * @throws Exception
	 */
	public void setAuxiliar(Auxiliar auxiliar, Connection conn) throws Exception{
		PreparedStatement pstmt = null;		
		
		try{
			if(conn == null || conn.isClosed())
			conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "INSERT INTO CONT_AUXILIAR " +
					"(ID_EJERCICIO, ID_AUXILIAR, NOMBRE, DETALLE) " +
					"VALUES " +
					"(?,?,?,?) ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, auxiliar.getEjercicio().getIdEjercicio());
			pstmt.setString(2, auxiliar.getIdAuxiliar());
			pstmt.setString(3, auxiliar.getNombre());
			pstmt.setString(4, auxiliar.getDetalle());			
			pstmt.execute();
			pstmt.close();
			
			
		}catch(Exception e){
			throw new Error("Error al insertar el auxiliar "+auxiliar.getIdAuxiliar()+" "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}			
		}
	}
}
