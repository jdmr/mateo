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
public class DMateria extends Descuento {
	private String curso_plan_id;
	
	Connection conn_noe;
	
	/**
	 * @return Returns the curso_plan_id.
	 */
	public String getCurso_plan_id() {
		return curso_plan_id;
	}
	/**
	 * @param curso_plan_id The curso_plan_id to set.
	 */
	public void setCurso_plan_id(String curso_plan_id) {
		this.curso_plan_id = curso_plan_id;
	}
	/**
	 * 
	 */
	public DMateria() {
		//super();
		// TODO Auto-generated constructor stub
	}
	
	//Obtener el descuento de la materia
	public Map getDMateria () throws Exception
	{
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Map mDMateria = new TreeMap();
	try{
		
		if(conn_noe == null || conn_noe.isClosed())
			conn_noe = new Conexion().getConexionNoe(new Boolean(false));
		
		
		String COMANDO = "SELECT CURSO_PLAN_ID, COALESCE(IMPORTE,0.0) AS IMPORTE, ";
		COMANDO += "TO_CHAR(FECHA,'DD/MM/YYYY') FECHA, USUARIO, STATUS ";
		COMANDO += "FROM FES_DESCUENTOMATERIA ";
		COMANDO += "WHERE STATUS = 'A' ";
		
		pstmt = conn_noe.prepareStatement(COMANDO);
		rset = pstmt.executeQuery();
		
		

		while(rset.next())
		{			
                        DMateria dMateria = new DMateria(rset.getString("Curso_Plan_ID"), new Double(rset.getDouble("importe")), 
					rset.getString("fecha"), rset.getString("Usuario"), rset.getString("status"));
                        
			mDMateria.put(rset.getString("Curso_Plan_ID"), dMateria);			
		}
		rset.close();
		pstmt.close();
	}catch(Exception e){
		throw new UMException("Error al obtener los descuentos de materias <br>"+e);
	}finally{
		if(pstmt != null) {pstmt.close(); pstmt = null;}
		if(rset != null) {rset.close(); rset = null;}
		if(!conn_noe.isClosed()) {conn_noe.close(); conn_noe = null;}
	}
	
		return mDMateria;
	}
	/**
	 * @param curso_plan_id
	 * @param importe
	 * @param fecha
	 * @param usuario
	 * @param status
	 */
	public DMateria(String curso_plan_id, Double importe, String fecha, String usuario, String status) {
		this.curso_plan_id = curso_plan_id;
		this.setImporte(importe);
		this.setFecha(fecha);
		this.setUsuario(usuario);
		this.setStatus(status);		
	}
}
