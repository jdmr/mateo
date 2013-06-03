/*
 * Created on Jun 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package mx.edu.um.mateo.inscripciones.model.ccobro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TParametros {
	private String carga_id;
	private String clave;
	private String concepto;
	private String valor;
	private Connection conn_noe;
	/**
	 * @return Returns the carga_id.
	 */
	public String getCarga_id() {
		return carga_id;
	}
	/**
	 * @param carga_id The carga_id to set.
	 */
	public void setCarga_id(String carga_id) {
		this.carga_id = carga_id;
	}
	/**
	 * @return Returns the clave.
	 */
	public String getClave() {
		return clave;
	}
	/**
	 * @param clave The clave to set.
	 */
	public void setClave(String clave) {
		this.clave = clave;
	}
	/**
	 * @return Returns the concepto.
	 */
	public String getConcepto() {
		return concepto;
	}
	/**
	 * @param concepto The concepto to set.
	 */
	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}
	/**
	 * @return Returns the valor.
	 */
	public String getValor() {
		return valor;
	}
	/**
	 * @param valor The valor to set.
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}
	public Map getTParametros (String carga_id) throws Exception{
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		Map mTParametros = new TreeMap();
		
		try{		
		
		if(conn_noe == null || conn_noe.isClosed())
				conn_noe = new Conexion().getConexionNoe(new Boolean(false));
		
		String COMANDO = "SELECT CLAVE, CONCEPTO, VALOR ";		
		COMANDO += "FROM noe.FES_PARAMGRALDET ";
		COMANDO += "WHERE CARGA_ID = ? ";		
		
		pstmt = conn_noe.prepareStatement(COMANDO);
		pstmt.setString(1, carga_id);		
		
		rset = pstmt.executeQuery();

		while(rset.next())
		{
			TParametros tParametros = new TParametros(carga_id, rset.getString("Clave"), rset.getString("Concepto"), rset.getString("Valor"));
			
			mTParametros.put(rset.getString("Clave")+carga_id+rset.getString("Concepto"), tParametros);
		}
		
		}catch(Exception e){
			throw new UMException("Error al obtener la tabla de parametros de la carga "+carga_id+"<br>"+e);
		}finally{
			if(rset != null) {rset.close(); rset = null;}
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(!conn_noe.isClosed()) {conn_noe.close(); conn_noe = null;}
		}		
		
		return mTParametros;
	}
	/**
	 * 
	 */
	public TParametros() {
		super();
	}
	/**
	 * @param carga_id
	 * @param clave
	 * @param concepto
	 * @param valor
	 */
	public TParametros(String carga_id, String clave, String concepto,
			String valor) {
		super();
		this.carga_id = carga_id;
		this.clave = clave;
		this.concepto = concepto;
		this.valor = valor;
	}
}
