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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;
import java.util.Date;
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Constants;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Pagare {
	private String matricula;
	private String carga_id;
	private Integer bloque;
	private Integer folio;
	private String fVencimiento;
	private Double importe;
	private String status;
        private String clave;
	
	private Connection conn;
	
	/**
	 * @return Returns the bloque.
	 */
	public Integer getBloque() {
		return bloque;
	}
	/**
	 * @param bloque The bloque to set.
	 */
	public void setBloque(Integer bloque) {
		this.bloque = bloque;
	}
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
	 * @return Returns the folio.
	 */
	public Integer getFolio() {
		return folio;
	}
	/**
	 * @param folio The folio to set.
	 */
	public void setFolio(Integer folio) {
		this.folio = folio;
	}
	/**
	 * @return Returns the fVencimiento.
	 */
	public String getFVencimiento() {		
		return fVencimiento;
	}
	/**
	 * @param vencimiento The fVencimiento to set.
	 */
	public void setFVencimiento(String vencimiento) {
		fVencimiento = vencimiento;
	}
	/**
	 * @return Returns the importe.
	 */
	public Double getImporte() {
		return importe;
	}
	/**
	 * @param importe The importe to set.
	 */
	public void setImporte(Double importe) {
		this.importe = importe;
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

    /**
     * @return the clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * @param clave the clave to set
     */
    public void setClave(String clave) {
        this.clave = clave;
    }
	/**
	 * 
	 */
	public Pagare() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param matricula
	 * @param carga_id
	 * @param bloque
	 * @param vencimiento
	 * @param importe
	 * @param status
	 */
	public Pagare(String matricula, String carga_id, Integer bloque,
			String vencimiento, Double importe, String status) {
		this.matricula = matricula;
		this.carga_id = carga_id;
		this.bloque = bloque;
		this.fVencimiento = vencimiento;
		this.importe = importe;
		this.status = status;
	}
	/**
	 * @param matricula
	 * @param carga_id
	 * @param bloque
	 * @param vencimiento
	 * @param importe
	 * @param status
	 */
	public Pagare(String matricula, String carga_id, Integer bloque,
			String vencimiento, Double importe, String status, String clave) {
		this.matricula = matricula;
		this.carga_id = carga_id;
		this.bloque = bloque;
		this.fVencimiento = vencimiento;
		this.importe = importe;
		this.status = status;
                this.clave = clave;
	}
	
	public static void limpiaTabla (Connection conn, String matricula, String carga_id, Integer bloque) throws  Exception {
		PreparedStatement pstmt = null;	
		
		try{			
			String	COMANDO = "DELETE " +
					"FROM MATEO.FES_CC_PAGARE_DET " +
					"WHERE MATRICULA = ? " +
					"AND CARGA_ID = ? " +
					"AND BLOQUE = ? ";
			
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, matricula);
			pstmt.setString(2, carga_id);
			pstmt.setInt(3, bloque.intValue());
			pstmt.execute();
			pstmt.close();			
		}catch(Exception e){
			throw new UMException("Error al inicializar los pagares del alumno "+matricula+" "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}
	
	/*Grabar movimientos del alumno en la base de datos*/
	public static void grabaTabla (Connection conn, Vector vPagares, Alumno alumno) throws Exception{
		PreparedStatement pstmt = null;
		
		try{
                        Locale local = new java.util.Locale (Constants.LOCALE_LANGUAGE, Constants.LOCALE_COUNTRY, Constants.LOCALE_VARIANT);
                        SimpleDateFormat sdf = new SimpleDateFormat (Constants.DATE_SHORT_HUMAN_PATTERN, local);

			Enumeration ePagares = vPagares.elements();
			while(ePagares.hasMoreElements()){
				Pagare pagare = (Pagare)ePagares.nextElement();

				if(pagare.getMatricula().equals(alumno.getMatricula ())){

                                    //En el caso de los folios, la fecha viene vacia, asi que se le asigna la fecha actual
                                    if(pagare.getFVencimiento().isEmpty()){
                                        pagare.setFVencimiento(sdf.format(new Date()));
                                    }

//					Insertar pagares en tabla de pagare_detalle
					//El folio del pagare y el status se asignan en el trigger
					String COMANDO = "INSERT INTO MATEO.FES_CC_PAGARE_DET ";
					COMANDO += "(ID, MATRICULA, CARGA_ID, BLOQUE, FVENCIMIENTO, ";
					COMANDO += "IMPORTE, STATUS, CCOBRO_ID, VERSION, CLAVE) ";
					COMANDO += "VALUES ";
					COMANDO += "((SELECT MAX(ID)+1 FROM MATEO.FES_CC_PAGARE_DET), ?, ?, ?, TO_DATE(?, 'dd-mm-yy'), ?, ?, ?, 0, ?)";
					pstmt = conn.prepareStatement(COMANDO);
					pstmt.setString(1, pagare.getMatricula());
					pstmt.setString(2, pagare.getCarga_id());
					pstmt.setInt(3, pagare.getBloque().intValue());
					pstmt.setString(4, pagare.getFVencimiento());
					pstmt.setDouble(5, pagare.getImporte().doubleValue());
					pstmt.setString(6, pagare.getStatus());
                                        pstmt.setInt (7, alumno.getId ().intValue ());
                                        pstmt.setString (8, pagare.getStatus()+alumno.getFolio()+pagare.getFVencimiento());

					pstmt.execute();
					pstmt.close();
				}							
			}			
		}catch(Exception e){
			throw new UMException("Error al insertar los pagares del calculo de cobro del alumno "+alumno.getMatricula ()+" "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}
	
	public Vector getPagaresCC (String matricula, String carga_id, Integer bloque) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Vector vctPagares = new Vector();
		Locale local = new Locale("es","MX","Traditional_WIN");
	    SimpleDateFormat sdFormat = new SimpleDateFormat("dd-MMMM-yyyy", local);	    
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "SELECT FOLIO, FVENCIMIENTO FECHA, IMPORTE, STATUS, CLAVE " +
				"FROM mateo.FES_CC_PAGARE_DET " +
				"WHERE MATRICULA = ? " +
				"AND CARGA_ID = ? " +
				"AND BLOQUE = ? " +
				//"AND STATUS IN ('A','P','C','BB','BA') " +
				"ORDER BY FOLIO ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, matricula);
			pstmt.setString(2, carga_id);
			pstmt.setInt(3, bloque.intValue());
			rset = pstmt.executeQuery();
			
			while(rset.next()){
				Pagare pagare = new Pagare(matricula, 
						carga_id, 
						bloque,  
						sdFormat.format(rset.getDate("Fecha")), 
						new Double(rset.getDouble("Importe")), 
						rset.getString("Status"), rset.getString("Clave"));
				vctPagares.add(pagare);
			}
			
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new UMException("Error al obtener los pagares del alumno "+matricula+" en la carga "+carga_id+" y el bloque "+bloque+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn.isClosed()) {conn.close(); conn = null;}
		}
		
		return vctPagares;
	}
	/**
	 * 
	 * @param fInicial
	 * @param fFinal
	 * @return ArrayList con las cargas contenidas entre la fecha inicial y la fecha final
	 */
	public ArrayList getCargas(String fInicial, String fFinal)throws Exception{
		ArrayList cargas = new ArrayList();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String COMANDO = "";
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionEnoc(new Boolean(false));
			
			if (fInicial != null && fFinal != null){
				COMANDO = "SELECT CARGA_ID FROM ENOC.CARGA " +
				"WHERE TO_DATE(?,'DD/MM/YYYY') " +
				"BETWEEN F_INICIO AND F_FINAL " +
				"UNION SELECT CARGA_ID FROM ENOC.CARGA " +
				"WHERE TO_DATE(?,'DD/MM/YYYY') " +
				"BETWEEN F_INICIO AND F_FINAL";
			
				pstmt = conn.prepareStatement(COMANDO);
				pstmt.setString(1, fInicial);
				pstmt.setString(2, fFinal);
				rset = pstmt.executeQuery();
			}else if (fInicial == null && fFinal != null){
				COMANDO = "SELECT CARGA_ID FROM ENOC.CARGA " +
				"WHERE TO_DATE(?,'DD/MM/YYYY') " +
				"BETWEEN F_INICIO AND F_FINAL ";
			
				pstmt = conn.prepareStatement(COMANDO);				
				pstmt.setString(1, fFinal);
				rset = pstmt.executeQuery();
			}else{
				COMANDO = "SELECT CARGA_ID FROM ENOC.CARGA ";
			
				pstmt = conn.prepareStatement(COMANDO);
				rset = pstmt.executeQuery();
			}
				
			while(rset.next()){ 
				cargas.add(rset.getString("CARGA_ID"));
			}
			
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new UMException("Error al obtener las cargas <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn.isClosed()) {conn.close(); conn = null;}
		}
		
		return cargas;
	}
	
	/**
	 * @author benji
	 * Se utiliza para la clase SaldoVencido y su respectivo reporte.
	 * @param cargaId
	 * @param bloque
	 * @param fechaI
	 * @param fechaF
	 * @return Map con los pagares
	 * @throws Exception
	 */
	
	public Map getPagaresCC (String cargaId, Integer bloque, String fechaI, String fechaF) throws Exception {
		Map mPagares = new TreeMap();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		String cargas = "";
		String tmpMatricula = null;
		Vector vctPagares = new Vector();
		
		Locale local = new Locale("es","MX","Traditional_WIN");
	    SimpleDateFormat sdFormat = new SimpleDateFormat("dd-MMMM-yyyy", local);	    
		
	    cargas = "";
		
	    for (Iterator i = getCargas(fechaI, fechaF).iterator();i.hasNext();){
	    	cargas +="'"+(String) i.next()+"',";
	    }
	    cargas = cargas.substring(0,cargas.length()-1);
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionMateo(new Boolean(false));
			/*Se modifica la fecha de vencimiento del pagare al primero de mes*/
			if((fechaI!=null && fechaI.trim().length()>0)&&((fechaF!=null && fechaF.trim().length()>0))){
				String COMANDO = "SELECT MATRICULA, SUM(IMPORTE) IMPORTE " +
				"FROM mateo.FES_CC_PAGARE_DET " +
				"WHERE CARGA_ID IN ("+cargas+") AND STATUS IN ('A', 'P') " +
				"AND to_date('01/'||to_char(fvencimiento,'mm')||'/'||to_char(fvencimiento,'yyyy'),'dd/mm/yy') > TO_DATE(?,'DD/MM/YYYY') " +
				"GROUP BY MATRICULA";
				
				pstmt = conn.prepareStatement(COMANDO);
				pstmt.setString(1, fechaF);
				rset = pstmt.executeQuery();
			}
			else if(fechaF!=null && fechaF.trim().length()>0){
				String COMANDO = "SELECT MATRICULA, SUM(IMPORTE) IMPORTE " +
				"FROM mateo.FES_CC_PAGARE_DET " +
				"WHERE CARGA_ID IN ("+cargas+") " +				
				"AND STATUS IN ('A') " +
				"AND to_date('01/'||to_char(fvencimiento,'mm')||'/'||to_char(fvencimiento,'yyyy'),'dd/mm/yy') > TO_DATE(?,'DD/MM/YYYY') " +
				"GROUP BY MATRICULA";
				
				pstmt = conn.prepareStatement(COMANDO);				
				pstmt.setString(1, fechaF);
				rset = pstmt.executeQuery();
				
			}else{
				String COMANDO = "SELECT MATRICULA, SUM(IMPORTE) IMPORTE " +
				"FROM mateo.FES_CC_PAGARE_DET " +
				"WHERE CARGA_ID = ? " +
				"AND BLOQUE = ? " +
				"AND STATUS = 'A' " +
				"GROUP BY MATRICULA";
				
				pstmt = conn.prepareStatement(COMANDO);
				pstmt.setString(1, cargaId);
				pstmt.setInt(2, bloque.intValue());
				rset = pstmt.executeQuery();
			}
			
			
			while(rset.next()){
				mPagares.put(rset.getString("matricula"),new Double(rset.getDouble("importe")));	
			}
			
			
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new UMException("Error al obtener los pagares del alumno "+rset.getString("matricula")+" en la carga "+cargaId+" y el bloque "+bloque+" <br> "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn.isClosed()) {conn.close(); conn = null;}
		}
		
		return mPagares;
	}
	
	public void grabaPagaresX(String matricula, Vector vPagares) throws Exception{
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionMateo(new Boolean(false));
			//Pagare.grabaTabla(conn, vPagares, matricula);
		}catch(Exception e){
			throw new UMException("Error al intentar grabar los pagares del alumno"+matricula+"<br>"+e);
		}finally{		
			if(!conn.isClosed()) {conn.close(); conn = null;}
		}
	}
}
