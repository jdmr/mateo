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
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import mx.edu.um.mateo.inscripciones.model.ccobro.tFinanciera.TFinancieraEnc;
import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Clasificacion {
	private Integer tFinancieraId;
	private Boolean acfe;
	private Double cCredito;
	private Double cMateria;
	private Connection conn;
	/**
	 * @param acfe
	 * @param credito
	 */
	public Clasificacion(Boolean acfe, Double credito, Double materia) {
		super();
		this.acfe = acfe;
		this.cCredito = credito;
		this.cMateria = materia;
	}
	/**
	 * @param financieraId
	 * @param acfe
	 * @param credito
	 */
	public Clasificacion(Integer financieraId, Boolean acfe, Double credito, Double materia) {
		super();
		this.tFinancieraId = financieraId;
		this.acfe = acfe;
		this.cCredito = credito;
		this.cMateria = materia;
	}
	/**
	 * 
	 */
	public Clasificacion() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return Returns the acfe.
	 */
	public Boolean getAcfe() {
		return acfe;
	}
	/**
	 * @return Returns the acfe.
	 */
	public Boolean isAcfe() {
		return acfe;
	}
	/**
	 * @param acfe The acfe to set.
	 */
	public void setAcfe(Boolean acfe) {
		this.acfe = acfe;
	}
	/**
	 * @return Returns the cCredito.
	 */
	public Double getCCredito() {
		return cCredito;
	}
	/**
	 * @param credito The cCredito to set.
	 */
	public void setCCredito(Double credito) {
		cCredito = credito;
	}
	public Double getCMateria(){
		return this.cMateria;
	}
	public void setCMateria(Double materia){
		this.cMateria = materia;
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
	public Map getClasificaciones(TFinancieraEnc encabezado) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Map mClasificaciones = new TreeMap();
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionNoe(new Boolean(false));
			
			String COMANDO = "SELECT ACFE, CCREDITO, CMATERIA " +
					"FROM noe.FES_TFINANCIERA_CLAS " +
					"WHERE TFINANCIERA_ID = ? ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setInt(1, encabezado.getTFinancieraId().intValue());
			rset = pstmt.executeQuery();
			
			while(rset.next()){				
				mClasificaciones.put(rset.getString("ACFE").equals("S") ? new Integer(1) : new Integer(2), 
						new Clasificacion(encabezado.getTFinancieraId(), rset.getString("ACFE").equals("S") ? new Boolean(true) : new Boolean(false), new Double(rset.getDouble("CCredito")), new Double(rset.getDouble("CMateria"))));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener el costo por credito de la tabla financiera "+encabezado.getCarga().getCargaId()+" <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		return mClasificaciones;
	}
	public void setClasificacion(TFinancieraEnc encabezado, Map clasificaciones) throws Exception {
		PreparedStatement pstmt = null;
		String COMANDO = null;
		String paso = "1";
		
		Map mClasificaciones = this.getClasificaciones(encabezado); 
		Iterator iClasificaciones = clasificaciones.keySet().iterator();
		paso = "2";
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionNoe(new Boolean(true));
			paso = "3";
			while(iClasificaciones.hasNext()){
				Object key = iClasificaciones.next(); 
				Clasificacion clasificacion = (Clasificacion)clasificaciones.get(key);
				
				//No debe existir un costo de credito y un costo de materia al mismo tiempo
				if(clasificacion.getCCredito().compareTo(new Double(0)) > 0){
					if(clasificacion.getCMateria().compareTo(new Double(0)) > 0)
						throw new Exception("No se puede dar de alta un costo de credito y costo de materia");					
				} 
				else if(clasificacion.getCMateria().compareTo(new Double(0)) > 0){
					if(clasificacion.getCCredito().compareTo(new Double(0)) > 0)
						throw new Exception("No se puede dar de alta un costo de credito y costo de materia");					
				}
				
				paso = "4";
				if(mClasificaciones.containsKey(key)){
					paso = "5";
					COMANDO = "UPDATE noe.FES_TFINANCIERA_CLAS " +
							"SET CCREDITO = ?, CMATERIA = ? " +
							"WHERE TFINANCIERA_ID = ? " +
							"AND ACFE = ? ";
					pstmt = conn.prepareStatement(COMANDO);
					pstmt.setDouble(1, clasificacion.getCCredito().doubleValue());
					pstmt.setDouble(2, clasificacion.getCMateria().doubleValue());
					pstmt.setInt(3, encabezado.getTFinancieraId().intValue());
					pstmt.setString(4, clasificacion.getAcfe().booleanValue() ? "S" : "N");
				}
				else{
					paso = "6";
					COMANDO = "INSERT INTO noe.FES_TFINANCIERA_CLAS " +
							"(TFINANCIERA_ID, ACFE, CCREDITO, CMATERIA) " +
							"VALUES " +
							"(?,?,?,?) ";
					pstmt = conn.prepareStatement(COMANDO);
					pstmt.setInt(1, encabezado.getTFinancieraId().intValue());					
					pstmt.setString(2, clasificacion.getAcfe().booleanValue() ? "S" : "N");
					pstmt.setDouble(3, clasificacion.getCCredito().doubleValue());
					pstmt.setDouble(4, clasificacion.getCMateria().doubleValue());
				}
				paso = "7";
				pstmt.execute();
				pstmt.close();
			}
			paso = "8";				
			
			conn.commit();
		}catch(Exception e){
			conn.rollback();
			throw new Error("Error al insertar el costo por credito en la tabla financiera "+encabezado.getCarga().getCargaId()+" <br>"+e+"<br>"+paso);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}			
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
	}
}
