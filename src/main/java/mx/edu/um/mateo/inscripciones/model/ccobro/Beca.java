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
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Constants;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Beca {
	private String matricula;
	private String fecha;
	private String tipo_beca;
	private Double p_matricula;
	private Double p_ensenanza;
	private Double p_internado;
	private String status;

	private Connection conn_noe;

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
	 * @return Returns the p_ensenanza.
	 */
	public Double getP_ensenanza() {
		return p_ensenanza;
	}
	/**
	 * @param p_ensenanza The p_ensenanza to set.
	 */
	public void setP_ensenanza(Double p_ensenanza) {
		this.p_ensenanza = p_ensenanza;
	}
	/**
	 * @return Returns the p_internado.
	 */
	public Double getP_internado() {
		return p_internado;
	}
	/**
	 * @param p_internado The p_internado to set.
	 */
	public void setP_internado(Double p_internado) {
		this.p_internado = p_internado;
	}
	/**
	 * @return Returns the p_matricula.
	 */
	public Double getP_matricula() {
		return p_matricula;
	}
	/**
	 * @param p_matricula The p_matricula to set.
	 */
	public void setP_matricula(Double p_matricula) {
		this.p_matricula = p_matricula;
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
	 * @return Returns the tipo_bea.
	 */
	public String getTipo_beca() {
		return tipo_beca;
	}
	/**
	 * @param tipo_bea The tipo_bea to set.
	 */
	public void setTipo_beca(String tipo_beca) {
		this.tipo_beca = tipo_beca;
	}

	public Map getBecas() throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Map mBecas = new TreeMap();

		try{
			if(conn_noe == null || conn_noe.isClosed())
				conn_noe = new Conexion().getConexionNoe(new Boolean(false));

			String COMANDO = " SELECT MATRICULA, TIPO_BECA, ";
			COMANDO += "COALESCE(p_matricula,0)/100 as BMatricula, ";
	        COMANDO += "COALESCE(p_ensenanza,0)/100 as BEnsenanza, ";
	        COMANDO += "COALESCE(p_internado,0)/100 as BInternado ";
	        COMANDO += "FROM bec_alumnos b ";
	        COMANDO += "WHERE STATUS = 'A' ";

	        pstmt = conn_noe.prepareStatement(COMANDO);

	        rset = pstmt.executeQuery();

	        while (rset.next()) {

	        	Beca beca = new Beca(rset.getString("Matricula"), rset.getString("Tipo_Beca"),new Double(rset.getDouble("BMatricula")), new Double(rset.getDouble("BEnsenanza")), new Double(rset.getDouble("BInternado")));

	        	if(!mBecas.containsKey(rset.getString("Matricula")))
	        		mBecas.put(rset.getString("Matricula"), beca);
	        }
	        pstmt.close();
	        rset.close();
		}catch(Exception e) {
			throw new UMException("Error al obtener las becas <br>" + e);
		}finally {
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn_noe.isClosed()) {conn_noe.close(); conn_noe = null;}
		}
		return mBecas;
	}

	/**
	 * @param matricula
	 * @param tipo_beca
	 * @param p_matricula
	 * @param p_ensenanza
	 * @param p_internado
	 */
	public Beca(String matricula, String tipo_beca, Double p_matricula,
			Double p_ensenanza, Double p_internado) {
		this.matricula = matricula;
		this.tipo_beca = tipo_beca;
		this.p_matricula = p_matricula;
		this.p_ensenanza = p_ensenanza;
		this.p_internado = p_internado;
	}
	/**
	 *
	 */
	public Beca() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void desactivaBeca(String matricula, Connection conn_noe) throws Exception{
		PreparedStatement pstmt = null;

		try{
			String COMANDO="UPDATE NOE.BEC_ALUMNOS " +
					"SET STATUS = 'I' " +
					"WHERE MATRICULA = ? " +
					"AND STATUS = 'A' ";

			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, matricula);
			pstmt.executeUpdate();
			pstmt.close();
		}catch(Exception e){
			throw new UMException("Error al desactivar la beca del alumno "+matricula+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}

    public static void desactivaBecaAdicional(String matricula, String cargaId, Integer bloque, Connection conn_noe) throws Exception{
		PreparedStatement pstmt = null;

		try{
			String COMANDO="UPDATE NOE.ALUMNO_BECA " +
					"SET STATUS = ?, CARGA_ID = ?, BLOQUE_ID = ? , FECHA_MODIFICA = SYSDATE " +
					"WHERE MATRICULA = ? " +
					"AND STATUS = ? ";

			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, Constants.STATUS_INSCRITO);
            pstmt.setString(2, cargaId);
            pstmt.setInt(3, bloque);
            pstmt.setString(4, matricula);
            pstmt.setString(5, Constants.STATUS_ACTIVO);
			pstmt.executeUpdate();
			pstmt.close();
		}catch(Exception e){
			throw new UMException("Error al desactivar la beca del alumno "+matricula+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}

	public Map getBecas(String fInicio, String fFinal) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Map mBecas = new TreeMap();

		try{
			if(conn_noe == null || conn_noe.isClosed())
				conn_noe = new Conexion().getConexionNoe(new Boolean(false));

			String COMANDO = " SELECT MATRICULA, TIPO_BECA, ";
			COMANDO += "COALESCE(p_matricula,0)/100 as BMatricula, ";
	        COMANDO += "COALESCE(p_ensenanza,0)/100 as BEnsenanza, ";
	        COMANDO += "COALESCE(p_internado,0)/100 as BInternado ";
	        COMANDO += "FROM bec_alumnos b ";
	        COMANDO += "WHERE STATUS = 'I' ";
	        COMANDO += "AND FECHA BETWEEN TO_DATE(?,'DD/MM/YY') AND TO_DATE(?,'DD/MM/YY') ";

	        pstmt = conn_noe.prepareStatement(COMANDO);
	        pstmt.setString(1, fInicio);
	        pstmt.setString(2, fFinal);

	        rset = pstmt.executeQuery();

	        while (rset.next()) {

	        	Beca beca = new Beca(rset.getString("Matricula"), rset.getString("Tipo_Beca"),new Double(rset.getDouble("BMatricula")), new Double(rset.getDouble("BEnsenanza")), new Double(rset.getDouble("BInternado")));

	        	if(!mBecas.containsKey(rset.getString("Matricula")))
	        		mBecas.put(rset.getString("Matricula"), beca);
	        }
	        pstmt.close();
	        rset.close();
		}catch(Exception e) {
			throw new UMException("Error al obtener las becas <br>" + e);
		}finally {
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn_noe.isClosed()) {conn_noe.close(); conn_noe = null;}
		}
		return mBecas;
	}

	public void activaBeca(String matricula, String fechaInicio, String fechaFinal, Connection conn_noe) throws Exception{
		PreparedStatement pstmt = null;

		try{
			String COMANDO="UPDATE BEC_ALUMNOS " +
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
			throw new UMException("Error al activar la beca del alumno "+matricula+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}
}
