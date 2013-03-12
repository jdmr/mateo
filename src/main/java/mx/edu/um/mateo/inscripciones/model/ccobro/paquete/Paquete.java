/*
 * Created on 27/06/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.paquete;

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
public class Paquete {
	private Integer id;
	private String nombre;
	private String descripcion;	
	private Double matricula;
	private Double ensenanza;
	private Double internado;
	private String acfe;
	/**
	 * 
	 */
	public Paquete() {
		this.nombre = "";
		this.descripcion = "";
		this.acfe = "1";
		this.matricula = new Double(0);
		this.ensenanza = new Double(0);
		this.internado = new Double(0);
	
	}
	public Paquete(Integer id){
		this.id = id;
	}

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
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return Returns the ensenanza.
	 */
	public Double getEnsenanza() {
		return ensenanza;
	}
	/**
	 * @param ensenanza The ensenanza to set.
	 */
	public void setEnsenanza(Double ensenanza) {
		this.ensenanza = ensenanza;
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
	 * @return Returns the acfe.
	 */
	public String getAcfe() {
		return acfe;
	}
	/**
	 * @param acfe The acfe to set.
	 */
	public void setAcfe(String acfe) {
		this.acfe = acfe;
	}
	public void setPaquete(Paquete paquete) throws Exception{
		if(paquete.getId() != null){
			//System.out.println("Actualiza Paquete");
			this.updatePaquete(paquete);			
		}
		else{
			//System.out.println("Crea Paquete");
			this.createPaquete(paquete);
		}
	}
	public void createPaquete(Paquete paquete) throws Exception{
		Conexion conx = null;
		Connection conn_noe = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Integer id = null;
		
		try{
			conx = new Conexion();
			conn_noe = conx.getConexionNoe(new Boolean(false));
			
			String COMANDO = "SELECT COALESCE(MAX(ID),0)+1 ID " +
					"FROM FES_PAQUETE " ;
			pstmt = conn_noe.prepareStatement(COMANDO);
			rset = pstmt.executeQuery();
			if(rset.next()){
				id = new Integer(rset.getInt("id"));
			}
			pstmt.close();
			rset.close();
			
			COMANDO = "INSERT INTO FES_PAQUETE " +
					"(ID, NOMBRE, DESCRIPCION, MATRICULA, ENSENANZA, INTERNADO, ACFE)" +
					"VALUES " +
					"(?,?,?,?,?,?,?)";
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setInt(1, id.intValue());
			pstmt.setString(2, paquete.getNombre());
			pstmt.setString(3, paquete.getDescripcion());
			pstmt.setDouble(4, paquete.getMatricula().doubleValue());
			pstmt.setDouble(5, paquete.getEnsenanza().doubleValue());
			pstmt.setDouble(6, paquete.getInternado().doubleValue());
			pstmt.setString(7, paquete.getAcfe());
			pstmt.execute();
			pstmt.close();
		}catch(Exception e){
			throw new Exception("Error al intentar crear un nuevo paquete "+e);
		}finally{
			if(rset != null){rset.close(); rset = null;}
			if(pstmt != null){pstmt.close(); pstmt = null;}
			if(!conn_noe.isClosed()){conn_noe.close(); conn_noe = null;}
		}
	}
	
	public void updatePaquete(Paquete paquete) throws Exception{
		Conexion conx = null;
		Connection conn_noe = null;
		PreparedStatement pstmt = null;
		try{
			conx = new Conexion();
			conn_noe = conx.getConexionNoe(new Boolean(false));
			
			String COMANDO = "UPDATE FES_PAQUETE " +
					"SET NOMBRE = ?, DESCRIPCION = ?, " +
					"MATRICULA = ?, ENSENANZA = ?, INTERNADO = ?, ACFE = ? " +
					"WHERE ID = ? ";
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setString(1, paquete.getNombre());
			pstmt.setString(2, paquete.getDescripcion());
			pstmt.setDouble(3, paquete.getMatricula().doubleValue());
			pstmt.setDouble(4, paquete.getEnsenanza().doubleValue());
			pstmt.setDouble(5, paquete.getInternado().doubleValue());
			pstmt.setString(6, paquete.getAcfe());
			pstmt.setInt(7, paquete.getId().intValue());
			pstmt.execute();
			pstmt.close();
		}catch(Exception e){
			throw new Exception("Error al intentar actualizar el paquete "+e);
		}finally{
			if(pstmt != null){pstmt.close(); pstmt = null;}
			if(!conn_noe.isClosed()){conn_noe.close(); conn_noe = null;}
		}
	}
	
	public Paquete getPaquete(Integer id) throws Exception{
		Conexion conx = null;
		Connection conn_noe = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Paquete paquete = null;
		
		try{
			conx = new Conexion();
			conn_noe = conx.getConexionNoe(new Boolean(false));
			
			String COMANDO = "SELECT ID, NOMBRE, DESCRIPCION, MATRICULA, ENSENANZA, INTERNADO, ACFE " +
					"FROM FES_PAQUETE " +
					"WHERE ID = ?" ;
			pstmt = conn_noe.prepareStatement(COMANDO);
			pstmt.setInt(1, id.intValue());
			rset = pstmt.executeQuery();
			if(rset.next()){
				paquete = new Paquete();
				paquete.setId(new Integer(rset.getInt("id")));
				paquete.setNombre(rset.getString("Nombre"));
				paquete.setDescripcion(rset.getString("Descripcion"));
				paquete.setMatricula(new Double(rset.getDouble("Matricula")));
				paquete.setEnsenanza(new Double(rset.getDouble("Ensenanza")));
				paquete.setInternado(new Double(rset.getDouble("Internado")));
				paquete.setAcfe(rset.getString("Acfe"));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Exception("Error al intentar obtener un paquete "+e);
		}finally{
			if(rset != null){rset.close(); rset = null;}
			if(pstmt != null){pstmt.close(); pstmt = null;}
			if(!conn_noe.isClosed()){conn_noe.close(); conn_noe = null;}
		}
		return paquete;
	}
	
	public Map getPaquetes() throws Exception{
		Conexion conx = null;
		Connection conn_noe = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Map mPaquetes = new TreeMap();
		
		try{
			conx = new Conexion();
			conn_noe = conx.getConexionNoe(new Boolean(false));
			
			String COMANDO = "SELECT ID, NOMBRE, DESCRIPCION, MATRICULA, ENSENANZA, INTERNADO, ACFE " +
			"FROM FES_PAQUETE " ;
			
			pstmt = conn_noe.prepareStatement(COMANDO);	
			rset = pstmt.executeQuery();
			while(rset.next()){
				Paquete paquete = new Paquete();
				paquete.setId(new Integer(rset.getInt("id")));
				paquete.setNombre(rset.getString("Nombre"));
				paquete.setDescripcion(rset.getString("Descripcion"));
				paquete.setMatricula(new Double(rset.getDouble("Matricula")));
				paquete.setEnsenanza(new Double(rset.getDouble("Ensenanza")));
				paquete.setInternado(new Double(rset.getDouble("Internado")));
				paquete.setAcfe(rset.getString("Acfe"));
				
				mPaquetes.put(paquete.getId(), paquete);
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Exception("Error al intentar obtener los paquetes "+e);
		}finally{
			if(rset != null){rset.close(); rset = null;}
			if(pstmt != null){pstmt.close(); pstmt = null;}
			if(!conn_noe.isClosed()){conn_noe.close(); conn_noe = null;}
		}
		return mPaquetes;
	}
}
