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
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Relacion {
	private Ejercicio ejercicio;
	private CtaMayor idCtaMayor;
	private CCosto idCCosto;
	private Auxiliar idAuxiliar;
	private String nombre;
	private String naturaleza;
	private String status;
	private Connection conn;
	/**
	 * @param ejercicio
	 * @param idCtaMayor
	 * @param idCCosto
	 */
	public Relacion(Ejercicio ejercicio, CtaMayor idCtaMayor, CCosto idCCosto) {
		super();
		this.ejercicio = ejercicio;
		this.idCtaMayor = idCtaMayor;
		this.idCCosto = idCCosto;
	}
	/**
	 * 
	 */
	public Relacion() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param idEjercicio
	 * @param idCtaMayor
	 * @param idCCosto
	 * @param idAuxiliar
	 * @param nombre
	 * @param naturaleza
	 */
	public Relacion(Ejercicio idEjercicio, CtaMayor idCtaMayor,
			CCosto idCCosto, Auxiliar idAuxiliar, String nombre,
			String naturaleza, String status) {
		super();
		this.ejercicio = idEjercicio;
		this.idCtaMayor = idCtaMayor;
		this.idCCosto = idCCosto;
		this.idAuxiliar = idAuxiliar;
		this.nombre = nombre;
		this.naturaleza = naturaleza;
		this.status = status;
	}
	/**
	 * @return Returns the idAuxiliar.
	 */
	public Auxiliar getAuxiliar() {
		return idAuxiliar;
	}
	/**
	 * @param idAuxiliar The idAuxiliar to set.
	 */
	public void setIdAuxiliar(Auxiliar idAuxiliar) {
		this.idAuxiliar = idAuxiliar;
	}
	/**
	 * @return Returns the idCCosto.
	 */
	public CCosto getIdCCosto() {
		return idCCosto;
	}
	/**
	 * @param idCCosto The idCCosto to set.
	 */
	public void setIdCCosto(CCosto idCCosto) {
		this.idCCosto = idCCosto;
	}
	/**
	 * @return Returns the idCtaMayor.
	 */
	public CtaMayor getIdCtaMayor() {
		return idCtaMayor;
	}
	/**
	 * @param idCtaMayor The idCtaMayor to set.
	 */
	public void setIdCtaMayor(CtaMayor idCtaMayor) {
		this.idCtaMayor = idCtaMayor;
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
	 * @return Returns the naturaleza.
	 */
	public String getNaturaleza() {
		return naturaleza;
	}
	/**
	 * @param naturaleza The naturaleza to set.
	 */
	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
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
	 * @return Returns the status.
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status The status to set.
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * 
	 * @return Map
	 * @throws Exception
	 */
	public Map getRelaciones(Ejercicio ejercicio) throws Exception {
		Map mRelaciones = new TreeMap();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try{
			if(this.conn == null || this.conn.isClosed())
			conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "SELECT ID_CTAMAYOR, ID_CCOSTO, ID_AUXILIAR, NOMBRE, " +
					"NATURALEZA, STATUS " +
					"FROM CONT_RELACION " +
					"WHERE ID_EJERCICIO = ? ";					
			
			pstmt = this.conn.prepareStatement(COMANDO);
			pstmt.setString(1, ejercicio.getIdEjercicio());
			rset = pstmt.executeQuery();
			
			while(rset.next()){
				mRelaciones.put(ejercicio.getIdEjercicio()+rset.getString("Id_CtaMayor")+rset.getString("Id_CCosto")+rset.getString("Id_Auxiliar"), 
						new Relacion(ejercicio, new CtaMayor(ejercicio,rset.getString("Id_CtaMayor")), 
							new CCosto(ejercicio, rset.getString("Id_CCosto")), new Auxiliar(ejercicio, rset.getString("Id_Auxiliar")), 
							rset.getString("Nombre"), rset.getString("Naturaleza"), rset.getString("Status")));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener las cuentas contables "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		
		return mRelaciones;
	}
	/**
	 * Crea una nueva cuenta en la base de datos
	 * @param Relacion
	 * @param conn La conexion se debe pasar porque este metodo puede formar parte de un proceso con transaccion
	 * @throws Exception
	 */
	public void setRelacion(Relacion relacion, Connection conn) throws Exception{
		PreparedStatement pstmt = null;		
		
		try{
			if(conn == null || conn.isClosed())
			conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "INSERT INTO CONT_RELACION " +
					"(ID_EJERCICIO, ID_CTAMAYOR, ID_CCOSTO, ID_AUXILIAR, NOMBRE, NATURALEZA, STATUS, " +
					"TIPO_CUENTA, ID_EJERCICIO2, ID_EJERCICIO3 ) " +
					"VALUES " +
					"(?,?,?,?,?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, relacion.getEjercicio().getIdEjercicio());
			pstmt.setString(2, relacion.getIdCtaMayor().getIdCtaMayor());
			pstmt.setString(3, relacion.getIdCCosto().getIdCCosto());
			pstmt.setString(4, relacion.getAuxiliar().getIdAuxiliar());
			pstmt.setString(5, relacion.getNombre());
			pstmt.setString(6, relacion.getNaturaleza());
			pstmt.setString(7, relacion.getStatus());
			pstmt.setString(8, relacion.getIdCtaMayor ().getTipoCuenta ());
			pstmt.setString(9, relacion.getEjercicio().getIdEjercicio());
			pstmt.setString(10, relacion.getEjercicio().getIdEjercicio());
			pstmt.execute();
			pstmt.close();
			
		}catch(Exception e){
			throw new Error("Error al insertar cuenta contable "+relacion.getIdCtaMayor().getIdCtaMayor()+"@"+relacion.getIdCCosto().getIdCCosto()+"@"+relacion.getAuxiliar().getIdAuxiliar()+" "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}			
		}
	}
	


}
