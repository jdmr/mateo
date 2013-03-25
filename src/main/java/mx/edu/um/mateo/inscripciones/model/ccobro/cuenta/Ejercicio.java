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
public class Ejercicio {
	private String idEjercicio;
	private String nombre;
	private String mascBalance;
	private String mascResultado;
	private String mascAuxiliar;
	private String mascCCosto;
	private String nivelContable;
	private String nivelAuxiliar;
	private String status;
	private static Connection conn;
	/**
	 * @param idEjercicio
	 * @param nombre
	 * @param mascBalance
	 * @param mascResultado
	 * @param mascCCosto
	 * @param nivelContable
	 * @param nivelAuxiliar
	 * @param status
	 */
	public Ejercicio(String idEjercicio, String nombre, String mascBalance,
			String mascResultado, String mascAuxiliar, String mascCCosto, 
			String nivelContable, String nivelAuxiliar, String status) {
		super();
		this.idEjercicio = idEjercicio;
		this.nombre = nombre;
		this.mascBalance = mascBalance;
		this.mascResultado = mascResultado;
		this.mascAuxiliar = mascAuxiliar;
		this.mascCCosto = mascCCosto;
		this.nivelContable = nivelContable;
		this.nivelAuxiliar = nivelAuxiliar;
		this.status = status;
	}
	/**
	 * @param idEjercicio
	 */
	public Ejercicio(String idEjercicio) {
		super();
		this.idEjercicio = idEjercicio;
	}
	/**
	 * 
	 */
	public Ejercicio() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param idEjercicio
	 * @param nombre
	 * @param status
	 */
	public Ejercicio(String idEjercicio, String nombre) {
		super();
		this.idEjercicio = idEjercicio;
		this.nombre = nombre;
		this.status = "A";
	}
	/**
	 * @return Returns the idEjercicio.
	 */
	public String getIdEjercicio() {
		return idEjercicio;
	}
	/**
	 * @param idEjercicio The idEjercicio to set.
	 */
	public void setIdEjercicio(String idEjercicio) {
		this.idEjercicio = idEjercicio;
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
	 * @return Returns the mascBalance.
	 */
	public String getMascBalance() {
		return mascBalance;
	}
	/**
	 * @param mascBalance The mascBalance to set.
	 */
	public void setMascBalance(String mascBalance) {
		this.mascBalance = mascBalance;
	}
	/**
	 * @return Returns the mascCCosto.
	 */
	public String getMascCCosto() {
		return mascCCosto;
	}
	/**
	 * @param mascCCosto The mascCCosto to set.
	 */
	public void setMascCCosto(String mascCCosto) {
		this.mascCCosto = mascCCosto;
	}
	/**
	 * @return Returns the mascResultado.
	 */
	public String getMascResultado() {
		return mascResultado;
	}
	/**
	 * @param mascResultado The mascResultado to set.
	 */
	public void setMascResultado(String mascResultado) {
		this.mascResultado = mascResultado;
	}
	/**
	 * @return Returns the nivelAuxiliar.
	 */
	public String getNivelAuxiliar() {
		return nivelAuxiliar;
	}
	/**
	 * @param nivelAuxiliar The nivelAuxiliar to set.
	 */
	public void setNivelAuxiliar(String nivelAuxiliar) {
		this.nivelAuxiliar = nivelAuxiliar;
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
	 * @return Returns the mascAuxiliar.
	 */
	public String getMascAuxiliar() {
		return mascAuxiliar;
	}
	/**
	 * @param mascAuxiliar The mascAuxiliar to set.
	 */
	public void setMascAuxiliar(String mascAuxiliar) {
		this.mascAuxiliar = mascAuxiliar;
	}
	/**
	 * Regresa un listado de todos los ejercicio, en un map
	 * @return Map Contiene todos los ejercicios activos o inactivos, siendo la llave idEjercicio
	 */
	public static Map getEjercicios() throws Exception {
		Map mEjercicios = new TreeMap();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try{
			if(conn == null || conn.isClosed())
			conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "SELECT ID_EJERCICIO, NOMBRE, MASC_BALANCE, MASC_RESULTADO, " +
					"MASC_AUXILIAR, MASC_CCOSTO, NIVEL_CONTABLE, NIVEL_TAUXILIAR, STATUS " +
					"FROM CONT_EJERCICIO " ;
			
			pstmt = conn.prepareStatement(COMANDO);
			rset = pstmt.executeQuery();
			
			while(rset.next()){
				mEjercicios.put(rset.getString("Id_Ejercicio"), new Ejercicio(rset.getString("ID_Ejercicio"), rset.getString("Nombre"),
						rset.getString("Masc_Balance"), rset.getString("Masc_Resultado"), rset.getString("Masc_Auxiliar"),
						rset.getString("Masc_CCosto"), rset.getString("Nivel_Contable"), rset.getString("Nivel_TAuxiliar"),
						rset.getString("Status")));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener los ejercicios contables "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn.isClosed()) {conn.close(); conn = null;}
		}
		
		return mEjercicios;
	}	
	/**
	 * Crea un nuevo ejercicio contable en la base de datos 
	 * @param ejercicio
	 * @param conn La conexion es necesaria ya que este metodo puede ser parte de un proceso con transaccion
	 * @throws Exception
	 */
	public void setEjercicio(Ejercicio ejercicio, Connection conn) throws Exception{
		PreparedStatement pstmt = null;		
		
		try{
			if(conn == null || conn.isClosed())
			conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "INSERT INTO CONT_EJERCICIO " +
					"(ID_EJERCICIO, NOMBRE, MASC_BALANCE, MASC_RESULTADO, " +
					"MASC_AUXILIAR, MASC_CCOSTO, NIVEL_CONTABLE, NIVEL_TAUXILIAR, STATUS)" +
					"VALUES " +
					"(?,?,?,?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, ejercicio.getIdEjercicio());
			pstmt.setString(2, ejercicio.getNombre());
			pstmt.setString(3, ejercicio.getMascBalance());
			pstmt.setString(4, ejercicio.getMascResultado());
			pstmt.setString(5, ejercicio.getMascAuxiliar());
			pstmt.setString(6, ejercicio.getMascCCosto());
			pstmt.setString(7, ejercicio.getNivelContable());
			pstmt.setString(8, ejercicio.getNivelAuxiliar());
			pstmt.setString(9, ejercicio.getStatus());
			pstmt.execute();
			pstmt.close();
			
			
		}catch(Exception e){
			throw new Error("Error al insertar el ejercicio contable "+ejercicio.getIdEjercicio()+" "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}			
		}
	}
	
}
