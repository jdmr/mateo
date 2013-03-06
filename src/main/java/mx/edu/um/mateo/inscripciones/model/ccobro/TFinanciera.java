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
public class TFinanciera {
	private String sector_id;
	private Integer idModalidad;
	private String carga_id;
	private String clasFin;
	private Double matricula;
	private Double tLegales;
	private Double cCredito;
	private Double internado;
	
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
	/**
	 * @return Returns the clasFin.
	 */
	public String getClasFin() {
		return clasFin;
	}
	/**
	 * @param clasFin The clasFin to set.
	 */
	public void setClasFin(String clasFin) {
		this.clasFin = clasFin;
	}
	/**
	 * @return Returns the idModalidad.
	 */
	public Integer getIdModalidad() {
		return idModalidad;
	}
	/**
	 * @param idModalidad The idModalidad to set.
	 */
	public void setIdModalidad(Integer idModalidad) {
		this.idModalidad = idModalidad;
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
	 * @return Returns the sector_id.
	 */
	public String getSector_id() {
		return sector_id;
	}
	/**
	 * @param sector_id The sector_id to set.
	 */
	public void setSector_id(String sector_id) {
		this.sector_id = sector_id;
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
	
	public Map getTFinanciera (String carga_id) throws Exception{
		
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		Map mTFinanciera = new TreeMap();
		
		try{		
			if(conn_noe == null || conn_noe.isClosed())
					conn_noe = new Conexion().getConexionNoe(new Boolean(false));
			
			String COMANDO = "SELECT SECTOR_ID, IDMODALIDAD, CARGA_ID, CLASFIN, ";
			COMANDO += "COALESCE(MATRICULA, 0) AS MATRICULA, ";
			COMANDO += "COALESCE(TLEGALES,0) AS TLEGALES, ";
			COMANDO += "COALESCE(CCREDITO,0) AS CCREDITO, ";
			COMANDO += "COALESCE(INTERNADO,0) AS INTERNADO ";
			COMANDO += "FROM FES_TABLAFINANCIERA ";
			COMANDO += "WHERE CARGA_ID = ? ";		
			
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, carga_id);		
			
			rset = pstmt.executeQuery();
	
			while(rset.next())
			{
				String carrera_id = rset.getString("sector_id");
				TFinanciera tFinanciera = new TFinanciera(carrera_id,
						new Integer(rset.getInt("IdModalidad")), rset.getString("carga_id"),
						rset.getString("ClasFin"), new Double(rset.getDouble("Matricula")),
						new Double(rset.getDouble("TLegales")), new Double(rset.getDouble("CCredito")),  
						new Double(rset.getDouble("Internado")));
				
				mTFinanciera.put(carga_id+carrera_id+new Integer(rset.getInt("idModalidad"))+rset.getString("ClasFin"),tFinanciera);
			}
			
		}catch(Exception e){
			throw new UMException("Error al obtener la tabla financiera de la carga "+carga_id+"<br>"+e);
		}finally{
			if(rset != null) {rset.close(); rset = null;}
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(!conn_noe.isClosed()) {conn_noe.close(); conn_noe = null;}
		}		
		
		return mTFinanciera;
	}
	/**
	 * @param sector_id
	 * @param idModalidad
	 * @param carga_id
	 * @param clasFin
	 * @param matricula
	 * @param legales
	 * @param credito
	 * @param internado
	 */
	public TFinanciera(String sector_id, Integer idModalidad, String carga_id,
			String clasFin, Double matricula, Double legales, Double credito,
			Double internado) {
		super();
		this.sector_id = sector_id;
		this.idModalidad = idModalidad;
		this.carga_id = carga_id;
		this.clasFin = clasFin;
		this.matricula = matricula;
		tLegales = legales;
		cCredito = credito;
		this.internado = internado;
	}
	/**
	 * 
	 */
	public TFinanciera() {
		super();
	}
}
