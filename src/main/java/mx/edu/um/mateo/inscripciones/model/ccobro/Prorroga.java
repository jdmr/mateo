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
import java.util.Date;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Prorroga {
	private String matricula;
	private Integer id;
	private String fecha;
	private String fecha_comp;
	private String descripcion;
	private Double saldo;
	private String usuario;
	private String status;
	
	private Connection conn_noe;
	
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * @return Returns the fecha_comp.
	 */
	public String getFecha_comp() {
		return fecha_comp;
	}
	/**
	 * @param fecha_comp The fecha_comp to set.
	 */
	public void setFecha_comp(String fecha_comp) {
		this.fecha_comp = fecha_comp;
	}
	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return Returns the saldo.
	 */
	public Double getSaldo() {
		return saldo;
	}
	/**
	 * @param saldo The saldo to set.
	 */
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
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
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}
	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	/*Obtiene pr?rrogas de pago*/
	public Map getProrrogas () throws Exception
	{
		Map mProrrogas = new TreeMap();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try{
			if(conn_noe == null || conn_noe.isClosed())
				conn_noe = new Conexion().getConexionNoe(new Boolean(false));
		
			String COMANDO = "SELECT MATRICULA, COMPROMISOPAGOID, ";
			COMANDO += "TO_CHAR(FECHA_COMP, 'dd-mm-yy') FECHA, TO_CHAR(FECHA_COMP, 'dd-mm-yy') FECHA_COMP, ";
			COMANDO += "DESCRIPCION, SALDO, USUARIO, STATUS ";
			COMANDO += "FROM FES_COMPROMISOPAGO ";
			COMANDO += "WHERE STATUS = 'A' ";
			
			pstmt = conn_noe.prepareStatement(COMANDO);
			
			rset = pstmt.executeQuery();
	
			while (rset.next())
			{
				Prorroga prorroga = new Prorroga(rset.getString("Matricula"), new Integer(rset.getInt("CompromisoPagoID")),
						rset.getString("Fecha"), rset.getString("Fecha_Comp"), rset.getString("Descripcion"),
						new Double(rset.getDouble("Saldo")), rset.getString("Usuario"), rset.getString("Status"));
				
				mProrrogas.put(rset.getString("Matricula"), prorroga);
			}
			pstmt.close();
			rset.close();
		}catch(Exception e){
			throw new UMException("Error al obtener las prorrogas de pago <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn_noe.isClosed()) {conn_noe.close(); conn_noe = null;}
		}
		
		return mProrrogas;
	}
	/**
	 * 
	 */
	public Prorroga() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param matricula
	 * @param id
	 * @param fecha
	 * @param fecha_comp
	 * @param descripcion
	 * @param saldo
	 * @param usuario
	 * @param status
	 */
	public Prorroga(String matricula, Integer id, String fecha,
			String fecha_comp, String descripcion, Double saldo,
			String usuario, String status) {
		this.matricula = matricula;
		this.id = id;
		this.fecha = fecha;
		this.fecha_comp = fecha_comp;
		this.descripcion = descripcion;
		this.saldo = saldo;
		this.usuario = usuario;
		this.status = status;
	}
	
	//Desactivar descuentos del alumno
	public void desactivarProrrogas(String matricula, Connection conn_noe) throws Exception{
		PreparedStatement pstmt = null;
		try{
			String COMANDO = "UPDATE NOE.FES_COMPROMISOPAGO ";
			COMANDO += "SET STATUS = 'I' ";
			COMANDO += "WHERE MATRICULA = ? ";
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, matricula);
			pstmt.execute();
			pstmt.close();
		}catch(Exception e){
			throw new UMException("Error al desactivar las prorrogas del alumno "+matricula+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}
	
	public Map getProrrogaInscrita(String matricula, String fechaInicio, String fechaFinal) throws Exception
	{
		Map mProrrogas = new TreeMap();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try{
			if(conn_noe == null || conn_noe.isClosed())
				conn_noe = new Conexion().getConexionNoe(new Boolean(false));
		
			String COMANDO = "SELECT COMPROMISOPAGOID, ";
			COMANDO += "TO_CHAR(FECHA_COMP, 'dd-mm-yy') FECHA, TO_CHAR(FECHA_COMP, 'dd-mm-yy') FECHA_COMP, ";
			COMANDO += "DESCRIPCION, SALDO, USUARIO, STATUS ";
			COMANDO += "FROM FES_COMPROMISOPAGO ";
			COMANDO += "WHERE STATUS = 'I' ";
			COMANDO += "AND MATRICULA = ? ";
			COMANDO += "AND FECHA BETWEEN TO_DATE(?,'DD/MM/YY') AND TO_DATE(?,'DD/MM/YY') ";
			
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, matricula);
			pstmt.setString(2, fechaInicio);
			pstmt.setString(3, fechaFinal);
			rset = pstmt.executeQuery();
	
			while (rset.next())
			{
				Prorroga prorroga = new Prorroga(rset.getString("Matricula"), new Integer(rset.getInt("CompromisoPagoID")),
						rset.getString("Fecha"), rset.getString("Fecha_Comp"), rset.getString("Descripcion"),
						new Double(rset.getDouble("Saldo")), rset.getString("Usuario"), rset.getString("Status"));
				
				mProrrogas.put(rset.getString("compromisopagoid"), prorroga);
			}
			pstmt.close();
			rset.close();
		}catch(Exception e){
			throw new UMException("Error al obtener las prorrogas de pago <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn_noe.isClosed()) {conn_noe.close(); conn_noe = null;}
		}
		
		return mProrrogas;
	}
	public Map getProrrogaInscritaByFechas(String fechaInicio, String fechaFinal) throws Exception
	{
		Map mProrrogas = new TreeMap();
		PreparedStatement pstmt = null;
		ResultSet rset = null;

		try{
			if(conn_noe == null || conn_noe.isClosed())
				conn_noe = new Conexion().getConexionNoe(new Boolean(false));
                        
                        
                        String COMANDO = "SELECT  MATRICULA, COMPROMISOPAGOID, ";
			COMANDO += "TO_CHAR(FECHA_COMP, 'dd-mm-yy') FECHA, TO_CHAR(FECHA_COMP, 'dd-mm-yy') FECHA_COMP, ";
			COMANDO += "DESCRIPCION, SALDO, USUARIO, STATUS ";
			COMANDO += "FROM FES_COMPROMISOPAGO ";
			COMANDO += "WHERE STATUS = 'I' ";
			COMANDO += "AND FECHA BETWEEN TO_DATE(?,'DD/MM/YY') AND TO_DATE(?,'DD/MM/YY') ";
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, fechaInicio);
			pstmt.setString(2, fechaFinal);
			rset = pstmt.executeQuery();
			while (rset.next())
			{
				Prorroga prorroga = new Prorroga(rset.getString("Matricula"), new Integer(rset.getInt("CompromisoPagoID")),
						rset.getString("Fecha"), rset.getString("Fecha_Comp"), rset.getString("Descripcion"),
						new Double(rset.getDouble("Saldo")), rset.getString("Usuario"), rset.getString("Status"));
				mProrrogas.put(prorroga.getMatricula(), prorroga);
			}
			pstmt.close();
			rset.close();
		}catch(Exception e){
			throw new UMException("Error al obtener las prorrogas de pago <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn_noe.isClosed()) {conn_noe.close(); conn_noe = null;}
		}
		return mProrrogas;
	}
}
