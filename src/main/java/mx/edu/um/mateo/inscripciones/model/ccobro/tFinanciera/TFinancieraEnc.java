/*
 * Created on Dec 23, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.tFinanciera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;

import mx.edu.um.mateo.inscripciones.model.ccobro.academico.Carga;
import mx.edu.um.mateo.inscripciones.model.ccobro.academico.Clasificacion;
import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TFinancieraEnc {
	private Integer tFinancieraId;
	private Carga carga;
	private Map clasificacion;	
	private Double matricula;
	private Double tLegales;
	private Double internado;	
	private String status;
	private Connection conn;
	
	/**
	 * @param financieraId
	 * @param carga
	 * @param clasificacion
	 * @param matricula
	 * @param legales
	 * @param internado
	 * @param status
	 */
	public TFinancieraEnc(Integer financieraId, Carga carga, Map clasificacion,
			Double matricula, Double legales, Double internado, String status) {
		super();
		tFinancieraId = financieraId;
		this.carga = carga;
		this.clasificacion = clasificacion;
		this.matricula = matricula;
		tLegales = legales;
		this.internado = internado;
		this.status = status;
	}
	public TFinancieraEnc(Integer financieraId, Carga carga, 
			Double matricula, Double legales, Double internado, String status) {
		super();
		tFinancieraId = financieraId;
		this.carga = carga;		
		this.matricula = matricula;
		tLegales = legales;
		this.internado = internado;
		this.status = status;
	}
	/**
	 * 
	 */
	public TFinancieraEnc() {
		super();		
	}
	/**
	 * @return Returns the carga.
	 */
	public Carga getCarga() {
		return carga;
	}
	/**
	 * @param carga The carga to set.
	 */
	public void setCarga(Carga carga) {
		this.carga = carga;
	}
	/**
	 * @return Returns the clasificacion.
	 */
	public Map getClasificacion() {
		return clasificacion;
	}
	/**
	 * @param clasificacion The clasificacion to set.
	 */
	public void setClasificacion(Map clasificacion) {
		this.clasificacion = clasificacion;
	}
	/**
	 * @return Returns the internado.
	 */
	public Double getInternado() {
		return internado;
	}
	/**
	 * @param internado The internado to set.
	 */
	public void setInternado(Double internado) {
		this.internado = internado;
	}
	/**
	 * @return Returns the matricula.
	 */
	public Double getMatricula() {
		return matricula;
	}
	/**
	 * @param matricula The matricula to set.
	 */
	public void setMatricula(Double matricula) {
		this.matricula = matricula;
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
	 * @return Returns the tFinancieraId.
	 */
	public Integer getTFinancieraId() {
		return tFinancieraId;
	}
	/**
	 * @param financieraId The tFinancieraId to set.
	 */
	public void setTFinancieraId(Integer financieraId) {
		tFinancieraId = financieraId;
	}
	/**
	 * @return Returns the tLegales.
	 */
	public Double getTLegales() {
		return tLegales;
	}
	/**
	 * @param legales The tLegales to set.
	 */
	public void setTLegales(Double legales) {
		tLegales = legales;
	}
	public TFinancieraEnc getEncabezado(Carga carga) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		TFinancieraEnc encabezado = null;
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionNoe(new Boolean(false));
			
			String COMANDO = "SELECT TFINANCIERA_ID, MATRICULA, TLEGALES, INTERNADO, STATUS " +
					"FROM noe.FES_TFINANCIERA_ENC " +
					"WHERE CARGA_ID = ? ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, carga.getCargaId());
			rset = pstmt.executeQuery();
			
			if(rset.next()){				
				Map mClasificaciones = null;
				encabezado = new TFinancieraEnc(new Integer(rset.getInt("TFinanciera_ID")), carga, mClasificaciones, 
						new Double(rset.getDouble("Matricula")), new Double(rset.getDouble("TLegales")), 
						new Double(rset.getDouble("Internado")), rset.getString("Status"));
				mClasificaciones = new Clasificacion().getClasificaciones(encabezado);
				encabezado.setClasificacion(mClasificaciones);				
			}
			pstmt.close();
			rset.close();			
			
		}catch(Exception e){
			throw new Error("Error al obtener el encabezado de la tabla financiera de "+carga.getCargaId()+" <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		return encabezado;
	}
	public void setEncabezado(TFinancieraEnc encabezado) throws Exception{
		PreparedStatement pstmt = null;		
		String COMANDO = null;
		String paso = "1";
		
		TFinancieraEnc dbEncabezado = this.getEncabezado(encabezado.getCarga());
				
		try{
			if(conn == null || conn.isClosed()){
				conn = new Conexion().getConexionNoe(new Boolean(true));
				if(conn == null)
					throw new Error("Error al intentar obtener una conexion");
			}
			paso = "2";
			if(dbEncabezado != null){
				encabezado.setTFinancieraId(dbEncabezado.getTFinancieraId());
				paso = "3";				
				COMANDO = "UPDATE noe.FES_TFINANCIERA_ENC " +
				"SET MATRICULA = ?, " +
				"TLEGALES = ?, " +
				"INTERNADO = ?," +
				"STATUS = ? " +				
				"WHERE TFINANCIERA_ID = ? " +
				"AND CARGA_ID = ? ";				
				pstmt = conn.prepareStatement(COMANDO);
				pstmt.setDouble(1, encabezado.getMatricula().doubleValue());
				pstmt.setDouble(2, encabezado.getTLegales().doubleValue());
				pstmt.setDouble(3, encabezado.getInternado().doubleValue());
				pstmt.setString(4, encabezado.getStatus());
				pstmt.setInt(5, encabezado.getTFinancieraId().intValue());
				pstmt.setString(6, encabezado.getCarga().getCargaId());
				paso = "4";
			}			
			else{
				paso = "5";
				//Obtener el folio de la tabla financiera
				encabezado.setTFinancieraId(this.getTFinancieraId(conn));
				
				COMANDO = "INSERT INTO noe.FES_TFINANCIERA_ENC " +
				"(TFINANCIERA_ID, CARGA_ID, MATRICULA, TLEGALES, INTERNADO, STATUS) " +
				"VALUES " +
				"(?,?,?,?,?,?) ";
				pstmt = conn.prepareStatement(COMANDO);				
				pstmt.setInt(1, encabezado.getTFinancieraId().intValue());
				pstmt.setString(2, encabezado.getCarga().getCargaId());
				pstmt.setDouble(3, encabezado.getMatricula().doubleValue());
				pstmt.setDouble(4, encabezado.getTLegales().doubleValue());
				pstmt.setDouble(5, encabezado.getInternado().doubleValue());
				pstmt.setString(6, encabezado.getStatus());
				paso = "6";					
			}
					
			pstmt.execute();
			paso = "7";
			pstmt.close();			
			conn.commit();
			
			new Clasificacion().setClasificacion(encabezado, encabezado.getClasificacion());
		}catch(Exception e){
			conn.rollback();
			throw new Error("Error al modificar los datos del encabezado de la tabla financiera "+encabezado.getCarga().getCargaId()+"<br>"+e+"<br>"+paso);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}			
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
	}
	/**
	 *  
	 * Obtiene un nuevo folio para la tabla financiera
	 * @param conn
	 * @return El maximo id de la tabla financiera correspondiente a una carga academica
	 * En caso de que no exista tabla financiera de la carga indicada, regresara 1 
	 * @throws Exception
	 */
	public Integer getTFinancieraId(Connection conn) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String COMANDO = null;		
		Integer tFinancieraId = new Integer(1);
		
		try{
			COMANDO = "SELECT COALESCE(MAX(TFINANCIERA_ID),0)+1 ID " +
			"FROM noe.FES_TFINANCIERA_ENC " ;				
			pstmt = conn.prepareStatement(COMANDO);							
			rset = pstmt.executeQuery();
			
			if(rset.next())
				tFinancieraId = new Integer(rset.getInt("ID"));
			
			pstmt.close();
			rset.close();
		}catch(Exception e){			
			throw new Error("Error al obtener el folio de la tabla financiera "+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
		}
		return tFinancieraId;
	}
	/**
	 *  
	 * Obtiene el folio de la tabla financiera para la carga academica especificada
	 * @param conn
	 * @param carga
	 * @return El id de la tabla financiera correspondiente a una carga academica
	 * En caso de que no exista tabla financiera de la carga indicada, regresara 0 
	 * @throws Exception
	 */
	public Integer getTFinancieraId(Connection conn, Carga carga) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String COMANDO = null;		
		Integer tFinancieraId = new Integer(1);
		
		try{
			COMANDO = "SELECT COALESCE(MAX(TFINANCIERA_ID),0)+1 ID " +
			"FROM noe.FES_TFINANCIERA_ENC " +
			"WHERE CARGA_ID = ? " ;				
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, carga.getCargaId());
			rset = pstmt.executeQuery();
			
			if(rset.next())
				tFinancieraId = new Integer(rset.getInt("ID"));
			
			pstmt.close();
			rset.close();
		}catch(Exception e){			
			throw new Error("Error al obtener el folio de la tabla financiera para la carga academica "+carga.getCargaId()+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
		}
		return tFinancieraId;
	}
	public Map getEncabezados() throws Exception{
		Map mEncabezados = new TreeMap();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try{
			if(conn == null || conn.isClosed()){
				conn = new Conexion().getConexionNoe(new Boolean(true));
				if(conn == null)
					throw new Error("Error al intentar obtener una conexion");
			}
			
			String COMANDO = "SELECT TFINANCIERA_ID, CARGA_ID, MATRICULA, TLEGALES, INTERNADO, STATUS " +
					"FROM noe.FES_TFINANCIERA_ENC ";
			pstmt = conn.prepareStatement(COMANDO);
			rset = pstmt.executeQuery();
			
			while(rset.next()){				
				TFinancieraEnc encabezado = new TFinancieraEnc(new Integer(rset.getInt("TFinanciera_ID")),
						new Carga(rset.getString("Carga_ID")), new Double(rset.getDouble("Matricula")),
						new Double(rset.getDouble("TLegales")),new Double(rset.getDouble("Internado")), 
						rset.getString("Status"));
				
				//Obtener clasificacion
				encabezado.setClasificacion(new Clasificacion().getClasificaciones(encabezado));
				
				mEncabezados.put(encabezado.getCarga(),encabezado);
			}
			
			pstmt.close();
			rset.close();
					
		}catch(Exception e){
			throw new Error("Error al intentar obtener todas las tablas financieras existentes");
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
		}
		return mEncabezados;
	}
}