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
public class Empleado {
	private String matricula;
	private String codigo_personal;
	private Integer creditos_autorizados;
	private String fecha;
	private String status;
	
	private Connection conn_noe;
	
	/**
	 * @return Returns the codigo_personal.
	 */
	public String getCodigo_personal() {
		return codigo_personal;
	}
	/**
	 * @param codigo_personal The codigo_personal to set.
	 */
	public void setCodigo_personal(String codigo_personal) {
		this.codigo_personal = codigo_personal;
	}
	/**
	 * @return Returns the creditos_autorizados.
	 */
	public Integer getCreditos_autorizados() {
		return creditos_autorizados;
	}
	/**
	 * @param creditos_autorizados The creditos_autorizados to set.
	 */
	public void setCreditos_autorizados(Integer creditos_autorizados) {
		this.creditos_autorizados = creditos_autorizados;
	}
	/**
	 * @return Returns the fecha.
	 */
	public String getFecha() {
		return fecha;
	}
	/**
	 * @param fecha The fecha to set.
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	/**
	 * @return Returns the matricula.
	 */
	public String getMatricula() {
		return matricula;
	}
	/**
	 * @param matricula The matricula to set.
	 */
	public void setMatricula(String matricula) {
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
	
	/*Obtener los alumnos que son Empleado UM y que tienen autorizados creditos para estudiar*/
	public Map getAutorizacionEmp () throws Exception
	{		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		Map mAEmpleados = new TreeMap();
		
		try{
			if(conn_noe == null || conn_noe.isClosed())
				conn_noe = new Conexion().getConexionNoe(new Boolean(false));

			String COMANDO = "SELECT MATRICULA, CODIGO_PERSONAL, CREDITOS_AUTORIZADOS, ";
			COMANDO += "TO_CHAR(FECHA,'DD/MM/YYYY') FECHA, STATUS ";
			COMANDO += "FROM PER_EMPESTUDIOS ";
			COMANDO += "WHERE STATUS = 'A' ";
			
			pstmt = conn_noe.prepareStatement(COMANDO);
			
			rset = pstmt.executeQuery();

			while (rset.next())
			{
				 Empleado empleado = new Empleado(rset.getString("Matricula"), rset.getString("Codigo_Personal"),
				 		new Integer(rset.getString("Creditos_Autorizados")), rset.getString("Fecha"), rset.getString("Status"));
				 
				 mAEmpleados.put(rset.getString("Matricula"), empleado);
			}
			rset.close();
			pstmt.close();			
		}catch(Exception e){
			throw new UMException("Error al obtener los creditos excentos del alumno "+this.matricula+"<br> "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn_noe.isClosed()) {conn_noe.close(); conn_noe = null;}
		}

		return mAEmpleados;
	}
	/**
	 * @param matricula
	 * @param codigo_personal
	 * @param creditos_autorizados
	 * @param fecha
	 * @param status
	 * @param conn_noe
	 */
	public Empleado(String matricula, String codigo_personal,
			Integer creditos_autorizados, String fecha, String status) {
		this.matricula = matricula;
		this.codigo_personal = codigo_personal;
		this.creditos_autorizados = creditos_autorizados;
		this.fecha = fecha;
		this.status = status;
		
	}
	/**
	 * 
	 */
	public Empleado() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * Se cambia el status a T, indicando que ya el alumno se inscribio pero que todavia no se ha contabilizado
	 * el descuento
	 * @param matricula
	 * @param conn_noe
	 * @throws Exception
	 */
	public void desactivaAutorizacion(String matricula, Connection conn_noe) throws Exception{
		PreparedStatement pstmt = null;		
		
		try{		
			String COMANDO="UPDATE NOE.PER_EMPESTUDIOS " +
					"SET STATUS = 'T' " +
					"WHERE MATRICULA = ? " +
					"AND STATUS = 'A' ";
			
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, matricula);
			pstmt.executeUpdate();
			pstmt.close();
		}catch(Exception e){
			throw new UMException("Error al desactivar la autorizacion de estudio del alumno "+matricula+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}
	
	/**
	 * Se cambia el status a I, indicando que ya el alumno se inscribio y se contabilizo 
	 * @param matricula
	 * @param conn_noe
	 * @throws Exception
	 */
	public void desactivaDefinitivamenteAutorizacion(String matricula, Connection conn_noe) throws Exception{
		PreparedStatement pstmt = null;		
		
		try{		
			String COMANDO="UPDATE PER_EMPESTUDIOS " +
					"SET STATUS = 'I' " +
					"WHERE MATRICULA = ? " +
					"AND STATUS = 'T' ";
			
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, matricula);
			pstmt.executeUpdate();
			pstmt.close();
		}catch(Exception e){
			throw new UMException("Error al desactivar la autorizacion de estudio del alumno "+matricula+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}
	
	/*Obtener ayudas de empleados inactivas*/
	public Map getAutorizacionEmp (String fInicio, String fFinal) throws Exception
	{		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		Map mAEmpleados = new TreeMap();
		
		try{
			if(conn_noe == null || conn_noe.isClosed())
				conn_noe = new Conexion().getConexionNoe(new Boolean(false));

			String COMANDO = "SELECT MATRICULA, CODIGO_PERSONAL, CREDITOS_AUTORIZADOS, ";
			COMANDO += "TO_CHAR(FECHA,'DD/MM/YYYY') FECHA, STATUS ";
			COMANDO += "FROM PER_EMPESTUDIOS ";
			COMANDO += "WHERE STATUS = 'I' ";
			COMANDO += "AND FECHA BETWEEN TO_DATE(?,'DD/MM/YY') AND TO_DATE(?,'DD/MM/YY') ";
			
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, fInicio);
			pstmt.setString(2, fFinal);
			rset = pstmt.executeQuery();

			while (rset.next())
			{
				 Empleado empleado = new Empleado(rset.getString("Matricula"), rset.getString("Codigo_Personal"),
				 		new Integer(rset.getString("Creditos_Autorizados")), rset.getString("Fecha"), rset.getString("Status"));
				 
				 mAEmpleados.put(rset.getString("Matricula"), empleado);
			}
			rset.close();
			pstmt.close();			
		}catch(Exception e){
			throw new UMException("Error al obtener los creditos excentos del alumno "+this.matricula+"<br> "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn_noe.isClosed()) {conn_noe.close(); conn_noe = null;}
		}

		return mAEmpleados;
	}
	
	public void activaAutorizacion(String matricula, String fechaInicio, String fechaFinal, Connection conn_noe) throws Exception{
		PreparedStatement pstmt = null;		
		
		try{		
			String COMANDO="UPDATE PER_EMPESTUDIOS " +
					"SET STATUS = 'A' " +
					"WHERE MATRICULA = ? " +
					"AND STATUS = 'I' " +			
					"AND FECHA BETWEEN TO_DATE(?,'DD/MM/YY') AND TO_DATE(?,'DD/MM/YY') ";
			
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, matricula);
			pstmt.setString(2, fechaInicio);
			pstmt.setString(3, fechaFinal);
			pstmt.executeUpdate();
			pstmt.close();
		}catch(Exception e){
			throw new UMException("Error al desactivar la autorizacion de estudio del alumno "+matricula+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}
}
