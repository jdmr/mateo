/*
 * Created on Dec 23, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.academico;

import mx.edu.um.mateo.inscripciones.model.ccobro.Alumno;
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
public class Carga {
	/**
	 * @param cargaId
	 */
	public Carga(String cargaId) {
		super();
		this.cargaId = cargaId;
	}
	private String cargaId;
	private String nombreCarga;
	private String fCreada;
	private String periodo;
	private Integer ciclo;
	private String fInicio;
	private String fFinal;
	private String fExtra;
	private Integer numCursos;
	private String estado;
	private Connection conn;
	/**
	 * @param cargaId
	 * @param nombreCarga
	 */
	public Carga(String cargaId, String nombreCarga) {
		super();
		this.cargaId = cargaId;
		this.nombreCarga = nombreCarga;
	}
	/**
	 * @param cargaId
	 * @param nombreCarga
	 * @param creada
	 * @param periodo
	 * @param ciclo
	 * @param inicio
	 * @param final1
	 * @param extra
	 * @param numCursos
	 * @param estado
	 */
	public Carga(String cargaId, String nombreCarga, String creada,
			String periodo, Integer ciclo, String inicio, String final1,
			String extra, Integer numCursos, String estado) {
		super();
		this.cargaId = cargaId;
		this.nombreCarga = nombreCarga;
		fCreada = creada;
		this.periodo = periodo;
		this.ciclo = ciclo;
		fInicio = inicio;
		fFinal = final1;
		fExtra = extra;
		this.numCursos = numCursos;
		this.estado = estado;
	}
	/**
	 * 
	 */
	public Carga() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return Returns the cargaId.
	 */
	public String getCargaId() {
		return cargaId;
	}
	/**
	 * @param cargaId The cargaId to set.
	 */
	public void setCargaId(String cargaId) {
		this.cargaId = cargaId;
	}
	/**
	 * @return Returns the ciclo.
	 */
	public Integer getCiclo() {
		return ciclo;
	}
	/**
	 * @param ciclo The ciclo to set.
	 */
	public void setCiclo(Integer ciclo) {
		this.ciclo = ciclo;
	}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	/**
	 * @return Returns the fCreada.
	 */
	public String getFCreada() {
		return fCreada;
	}
	/**
	 * @param creada The fCreada to set.
	 */
	public void setFCreada(String creada) {
		fCreada = creada;
	}
	/**
	 * @return Returns the fExtra.
	 */
	public String getFExtra() {
		return fExtra;
	}
	/**
	 * @param extra The fExtra to set.
	 */
	public void setFExtra(String extra) {
		fExtra = extra;
	}
	/**
	 * @return Returns the fFinal.
	 */
	public String getFFinal() {
		return fFinal;
	}
	/**
	 * @param final1 The fFinal to set.
	 */
	public void setFFinal(String final1) {
		fFinal = final1;
	}
	/**
	 * @return Returns the fInicio.
	 */
	public String getFInicio() {
		return fInicio;
	}
	/**
	 * @param inicio The fInicio to set.
	 */
	public void setFInicio(String inicio) {
		fInicio = inicio;
	}
	/**
	 * @return Returns the nombreCarga.
	 */
	public String getNombreCarga() {
		return nombreCarga;
	}
	/**
	 * @param nombreCarga The nombreCarga to set.
	 */
	public void setNombreCarga(String nombreCarga) {
		this.nombreCarga = nombreCarga;
	}
	/**
	 * @return Returns the numCursos.
	 */
	public Integer getNumCursos() {
		return numCursos;
	}
	/**
	 * @param numCursos The numCursos to set.
	 */
	public void setNumCursos(Integer numCursos) {
		this.numCursos = numCursos;
	}
	/**
	 * @return Returns the periodo.
	 */
	public String getPeriodo() {
		return periodo;
	}
	/**
	 * @param periodo The periodo to set.
	 */
	public void setPeriodo(String periodo) {
		this.periodo = periodo;
	}
	public Map getCargas() throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Map mCargas = new TreeMap();
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionEnoc(new Boolean(false));
			
			String COMANDO = "SELECT CARGA_ID, NOMBRE_CARGA " +
					"FROM CARGA ";
			pstmt = conn.prepareStatement(COMANDO);
			rset = pstmt.executeQuery();
			
			while(rset.next()){				
				mCargas.put(rset.getString("Carga_ID"), new Carga(rset.getString("Carga_ID"), rset.getString("Nombre_Carga")));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener las cargas academicas <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		
		return mCargas;
	}
        
        public Boolean isCargaActiva() throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Boolean flag = new Boolean(false);
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionEnoc(new Boolean(false));
			
			String COMANDO = "SELECT COUNT(*) NREG " +
					"FROM CARGA " +
                                        "WHERE SYSDATE BETWEEN F_INICIO AND F_FINAL " +
                                        "AND CARGA_ID = ? ";
			pstmt = conn.prepareStatement(COMANDO);
                        pstmt.setString (1, this.getCargaId ());
			rset = pstmt.executeQuery();
			
			if(rset.next()){				
				if(rset.getInt ("Nreg") > 0){
                                    flag = new Boolean(true);
                                }
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al validar la carga "+this.getCargaId ()+" <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		
		return flag;
	}

        public static Boolean isCargaVerano(String cargaId) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Boolean flag = new Boolean(false);
        Connection conn = null;

		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionEnoc(new Boolean(false));

			String COMANDO = "SELECT COUNT(*) NREG " +
					"FROM CARGA " +
                                        "WHERE CARGA_ID = ? " +
                                        "AND ISVERANO = 'S' ";
			pstmt = conn.prepareStatement(COMANDO);
                        pstmt.setString (1, cargaId);
			rset = pstmt.executeQuery();

			if(rset.next()){
				if(rset.getInt ("Nreg") > 0){
                                    flag = new Boolean(true);
                                }
			}
			pstmt.close();
			rset.close();

		}catch(Exception e){
			throw new Error("Error al validar la carga "+cargaId+" <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn.isClosed()) {conn.close(); conn = null;}
		}

		return flag;
	}
}
