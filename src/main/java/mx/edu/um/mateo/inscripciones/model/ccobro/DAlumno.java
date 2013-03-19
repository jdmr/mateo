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
import java.util.Vector;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class DAlumno extends Descuento implements Constant{
	private String matricula;
	private Boolean contabiliza;
	private String tipo_descuento_id;
	private String tipo_descuento;
	
	private Connection conn_noe;
	
	/**
	 * @return Returns the contabiliza.
	 */
	public Boolean getContabiliza() {
		return contabiliza;
	}
	/**
	 * @param contabiliza The contabiliza to set.
	 */
	public void setContabiliza(Boolean contabiliza) {
		this.contabiliza = contabiliza;
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
	 * 
	 */
	public DAlumno() {
		super();
		// TODO Auto-generated constructor stub
	}
	
//	Obtener el descuento de la materia
	public Vector getDAlumno () throws Exception
	{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Vector vDAlumnos = new Vector();
	try{
		if(conn_noe == null || conn_noe.isClosed())
			conn_noe = new Conexion().getConexionNoe(new Boolean(false));
		
		String COMANDO = "SELECT MATRICULA, COALESCE(DESCUENTOID, '') DESCUENTOID, ";
		COMANDO += "COALESCE(IMPORTE, 0.0) IMPORTE, ";
		COMANDO += "COALESCE(CONTABILIZA, 'N') CONTABILIZA, ";
		COMANDO += "TO_CHAR(FECHA, 'DD/MM/YYYY') FECHA, USUARIO, STATUS ";
		COMANDO += "FROM noe.FES_DESCUENTOALUMNO ";
		COMANDO += "WHERE STATUS = 'A' " ;
		
		pstmt = conn_noe.prepareStatement(COMANDO);
		
		rset = pstmt.executeQuery();

		while(rset.next())
		{
			
			DAlumno dAlumno = new DAlumno(rset.getString("Matricula"), new Integer(rset.getInt("DescuentoID")), new Double(rset.getDouble("importe")), 
					rset.getString("Contabiliza").equals("S")?new Boolean(true):new Boolean(false), rset.getString("fecha"), rset.getString("Usuario"), rset.getString("status"));
			
			vDAlumnos.add(dAlumno);			
		}
		rset.close();
		pstmt.close();
	}catch(Exception e){		
		throw new UMException("Error al obtener los descuentos de alumnos <br>"+e);
	}finally{
		if(pstmt != null) {pstmt.close(); pstmt = null;}
		if(rset != null) {rset.close(); rset = null;}
		if(!conn_noe.isClosed()) {conn_noe.close(); conn_noe = null;}
	}
	
		return vDAlumnos;
	}
	/**
	 * @param matricula
	 * @param descuentoid
	 * @param importe
	 * @param contabiliza
	 * @param fecha
	 * @param usuario
	 * @param status
	 */
	public DAlumno(String matricula, Integer descuentoid, Double importe, Boolean contabiliza, String fecha, String usuario, String status) {
		this.matricula = matricula;
		this.setId(descuentoid);
		this.setImporte(importe);
		this.contabiliza = contabiliza;
		this.setFecha(fecha);
		this.setUsuario(usuario);
		this.setStatus(status);
		
		/*Determinar el tipo de descuento*/
		if(this.getId().compareTo(ccfintDMExt) == 0){
			this.tipo_descuento_id = ccfstrDesctoMExtID;
			this.tipo_descuento = ccfstrDesctoMExt;
			this.setAplica_en("T");
		}
	}
	/**
	 * @return Returns the tipo_descuento.
	 */
	public String getTipo_descuento() {
		return tipo_descuento;
	}
	/**
	 * @param tipo_descuento The tipo_descuento to set.
	 */
	public void setTipo_descuento(String tipo_descuento) {
		this.tipo_descuento = tipo_descuento;
	}
	/**
	 * @return Returns the tipo_descuento_id.
	 */
	public String getTipo_descuento_id() {
		return tipo_descuento_id;
	}
	/**
	 * @param tipo_descuento_id The tipo_descuento_id to set.
	 */
	public void setTipo_descuento_id(String tipo_descuento_id) {
		this.tipo_descuento_id = tipo_descuento_id;
	}
	
	//Desactivar descuentos del alumno
	public void desactivarDescuentos(String matricula, Connection conn_noe) throws Exception{
		PreparedStatement pstmt = null;
		try{		
			String COMANDO = "UPDATE NOE.FES_DESCUENTOALUMNO ";
			COMANDO += "SET STATUS = 'I' ";
			COMANDO += "WHERE MATRICULA = ? ";
			COMANDO += "AND STATUS = 'A' ";
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, matricula);
			pstmt.execute();
			pstmt.close();
		}catch(Exception e){
			throw new UMException("Error al desactivar los descuentos del alumno "+matricula+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}
}
