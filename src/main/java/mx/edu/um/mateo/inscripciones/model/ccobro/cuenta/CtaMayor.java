/*
 * Created on Nov 10, 2004
 *
 * Contiene funciones de alta, modificacion y baja de cuentas
 * Asi como funciones utilitarias, como identificar la naturaleza de la cuenta
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.cuenta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CtaMayor {
	/**
	 * @param idEjercicio
	 * @param idCtaMayor
	 */
	public CtaMayor(Ejercicio idEjercicio, String idCtaMayor) {
		super();
		this.ejercicio = idEjercicio;
		this.idCtaMayor = idCtaMayor;
	}
	private Ejercicio ejercicio;
	private String idCtaMayor;
	private String tipoCuenta;
	private String nombre;
	private String nombreFiscal;
	private Boolean detalle;
	private Boolean aviso;
	private Boolean auxiliar;
	private Boolean iva;
	private Double pctIva;
	private Boolean detalleR;
	private Connection conn;
		
	/**
	 * @param idEjercicio
	 * @param idCtaMayor
	 * @param tipoCuenta
	 * @param nombre
	 * @param nombreFiscal
	 * @param detalle
	 * @param aviso
	 * @param iva
	 * @param pctIva
	 * @param detalleR
	 */
	public CtaMayor(Ejercicio idEjercicio, String idCtaMayor, String tipoCuenta,
			String nombre, String nombreFiscal, Boolean detalle, Boolean aviso,
			Boolean auxiliar, Boolean iva, Double pctIva, Boolean detalleR) {
		super();
		this.ejercicio = idEjercicio;
		this.idCtaMayor = idCtaMayor;
		this.tipoCuenta = tipoCuenta;
		this.nombre = nombre;
		this.nombreFiscal = nombreFiscal;
		this.detalle = detalle;
		this.aviso = aviso;
		this.auxiliar = auxiliar;
		this.iva = iva;
		this.pctIva = pctIva;
		this.detalleR = detalleR;
	}
	public CtaMayor(){
		
	}
	
	public CtaMayor getCtaMayor(Ejercicio idEjercicio, String idCtaMayor) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		CtaMayor ctaMayor = null;
		
		try{		
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "SELECT ID_EJERCICIO, ID_CTAMAYOR, TIPO_CUENTA, NOMBRE, " +
				"NOMBREFISCAL, DETALLE, AVISO, AUXILIAR, IVA, PCTIVA, DETALLER " +
				"FROM mateo.CONT_CTAMAYOR " +
				"WHERE DETALLE = 'S' " +
				"AND ID_EJERCICIO = ? " +
				"AND ID_CTAMAYOR = ? ";
			
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, idEjercicio.getIdEjercicio());
			pstmt.setString(2, idCtaMayor);
			rset = pstmt.executeQuery();

			if (rset.next())
			{
				ctaMayor = new CtaMayor(idEjercicio, idCtaMayor, rset.getString("Tipo_Cuenta"), 
						rset.getString("Nombre"), rset.getString("NombreFiscal"), 
						rset.getString("Detalle").equals("S")?new Boolean(true):new Boolean(false), 
						rset.getString("Aviso").equals("S")?new Boolean(true):new Boolean(false), 
						rset.getString("Auxiliar").equals("S")?new Boolean(true):new Boolean(false), 
						rset.getString("Iva").equals("S")?new Boolean(true):new Boolean(false),
						new Double(rset.getDouble("PctIva")), 
						rset.getString("DetalleR").equals("S")?new Boolean(true):new Boolean(false));
			}
			pstmt.close();
			rset.close();
		}catch(Exception e){
			throw new Error("Error al obtener los datos de la cuenta de mayor "+idCtaMayor+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn.isClosed()) {conn.close(); conn = null;}
		}
		return ctaMayor;
	}
	
	public String getNaturaleza() throws Exception
	{
		String naturaleza = null;
		
		//Obtener naturaleza de la cuenta
		if (this.getTipoCuenta().equals("B"))
		{
			if (this.getIdCtaMayor().substring(0,1).equals("1"))
				naturaleza = "D";
			else if (this.getIdCtaMayor().substring(0,1).equals("2") ||
				 this.getIdCtaMayor().substring(0,1).equals("3"))
				naturaleza = "C";
			else
				throw new Error("Cuenta de mayor invalida"+this.getIdCtaMayor()+"-"+this.getIdCtaMayor().substring(0,1));
		}
		else if (this.getTipoCuenta().equals("R"))
		{
			if (this.getIdCtaMayor().substring(0,1).equals("1"))
				naturaleza = "C";
			else if (this.getIdCtaMayor().substring(0,1).equals("2") ||
				 this.getIdCtaMayor().substring(0,1).equals("3"))
				naturaleza = "D";
			else
				throw new Error("Cuenta de mayor invalida "+this.getIdCtaMayor());
		}
		else
			throw new Error("Tipo de cuenta invalido! <br> No se pudo determinar una naturaleza");
		return naturaleza;
	}
	/**
	 * @return Returns the aviso.
	 */
	public Boolean getAviso() {
		return aviso;
	}
	/**
	 * @param aviso The aviso to set.
	 */
	public void setAviso(Boolean aviso) {
		this.aviso = aviso;
	}
	/**
	 * @return Returns the detalle.
	 */
	public Boolean getDetalle() {
		return detalle;
	}
	/**
	 * @param detalle The detalle to set.
	 */
	public void setDetalle(Boolean detalle) {
		this.detalle = detalle;
	}
	/**
	 * @return Returns the detalleR.
	 */
	public Boolean getDetalleR() {
		return detalleR;
	}
	/**
	 * @param detalleR The detalleR to set.
	 */
	public void setDetalleR(Boolean detalleR) {
		this.detalleR = detalleR;
	}
	/**
	 * @return Returns the idCtaMayor.
	 */
	public String getIdCtaMayor() {
		return idCtaMayor;
	}
	/**
	 * @param idCtaMayor The idCtaMayor to set.
	 */
	public void setIdCtaMayor(String idCtaMayor) {
		this.idCtaMayor = idCtaMayor;
	}
	/**
	 * @return Returns the idEjercicio.
	 */
	public Ejercicio getEjercicio() {
		return ejercicio;
	}
	/**
	 * @param idEjercicio The idEjercicio to set.
	 */
	public void setEjercicio(Ejercicio idEjercicio) {
		this.ejercicio = idEjercicio;		
	}
	/**
	 * @return Returns the iva.
	 */
	public Boolean getIva() {
		return iva;
	}
	/**
	 * @param iva The iva to set.
	 */
	public void setIva(Boolean iva) {
		this.iva = iva;
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
	 * @return Returns the nombreFiscal.
	 */
	public String getNombreFiscal() {
		return nombreFiscal;
	}
	/**
	 * @param nombreFiscal The nombreFiscal to set.
	 */
	public void setNombreFiscal(String nombreFiscal) {
		this.nombreFiscal = nombreFiscal;
	}
	/**
	 * @return Returns the pctIva.
	 */
	public Double getPctIva() {
		return pctIva;
	}
	/**
	 * @param pctIva The pctIva to set.
	 */
	public void setPctIva(Double pctIva) {
		this.pctIva = pctIva;
	}
	/**
	 * @return Returns the tipoCuenta.
	 */
	public String getTipoCuenta() {
		return tipoCuenta;
	}
	/**
	 * @param tipoCuenta The tipoCuenta to set.
	 */
	public void setTipoCuenta(String tipoCuenta) {
		this.tipoCuenta = tipoCuenta;
	}
	/**
	 * 
	 * @return Map
	 * @throws Exception
	 */
	public Map getCtasMayor(Ejercicio ejercicio) throws Exception {
		Map mCtasMayor = new TreeMap();
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		
		try{
			if(this.conn == null || this.conn.isClosed())
			conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "SELECT ID_CTAMAYOR, TIPO_CUENTA, NOMBRE, NOMBREFISCAL, " +
					"DETALLE, AVISO, AUXILIAR, IVA, COALESCE(PCTIVA,0) PCTIVA, DETALLER " +
					"FROM mateo.CONT_CTAMAYOR " +
					"WHERE ID_EJERCICIO = ? ";
								
			pstmt = this.conn.prepareStatement(COMANDO);
			pstmt.setString(1, ejercicio.getIdEjercicio());
			rset = pstmt.executeQuery();
			
			while(rset.next()){
				mCtasMayor.put(ejercicio.getIdEjercicio()+rset.getString("Id_CtaMayor"), 
						new CtaMayor(ejercicio, rset.getString("ID_CtaMayor"), rset.getString("Tipo_Cuenta"),
								rset.getString("Nombre"), rset.getString("NombreFiscal"), 
								rset.getString("Detalle").equals("S")?new Boolean(true):new Boolean(false),
								rset.getString("Aviso").equals("S")?new Boolean(true):new Boolean(false), 
								rset.getString("Auxiliar").equals("S")?new Boolean(true):new Boolean(false),
								rset.getString("Iva").equals("S")?new Boolean(true):new Boolean(false),
								new Double(rset.getString("PCTIVA")), 
								rset.getString("DetalleR").equals("S")?new Boolean(true):new Boolean(false)));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener las cuentas de mayor "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		
		return mCtasMayor;
	}
	/**
	 * Crea una nueva cuenta de mayor en la base de datos
	 * @param ctaMayor
	 * @param conn La conexion se debe pasar porque este metodo puede formar parte de un proceso con transaccion
	 * @throws Exception
	 */
	public void setCtaMayor(CtaMayor ctaMayor, Connection conn) throws Exception{
		PreparedStatement pstmt = null;		
		
		try{
			if(conn == null || conn.isClosed())
			conn = new Conexion().getConexionMateo(new Boolean(false));
			
			String COMANDO = "INSERT INTO mateo.CONT_CTAMAYOR " +
					"(ID_EJERCICIO, ID_CTAMAYOR, TIPO_CUENTA, NOMBRE, NOMBREFISCAL, " +
					"DETALLE, AVISO, AUXILIAR, IVA, PCTIVA, DETALLER) " +
					"VALUES " +
					"(?,?,?,?,?,?,?,?,?,?,?) ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, ctaMayor.getEjercicio().getIdEjercicio());
			pstmt.setString(2, ctaMayor.getIdCtaMayor());
			pstmt.setString(3, ctaMayor.getTipoCuenta());
			pstmt.setString(4, ctaMayor.getNombre());
			pstmt.setString(5, ctaMayor.getNombreFiscal());
			pstmt.setString(6, ctaMayor.getDetalle().booleanValue() ? "S":"N");
			pstmt.setString(7, ctaMayor.getAviso().booleanValue() ? "S":"N");
			pstmt.setString(8, ctaMayor.getAuxiliar().booleanValue() ? "S":"N");
			pstmt.setString(9, ctaMayor.getIva().booleanValue() ? "S":"N");
			pstmt.setDouble(10, ctaMayor.getPctIva().doubleValue());
			pstmt.setString(11, ctaMayor.getDetalleR().booleanValue() ? "S":"N");
			pstmt.execute();
			pstmt.close();
			
		}catch(Exception e){
			throw new Error("Error al insertar la cuenta de mayor "+ctaMayor.getIdCtaMayor()+" "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}			
		}
	}
	/**
	 * @return Returns the auxiliar.
	 */
	public Boolean getAuxiliar() {
		return auxiliar;
	}
	/**
	 * @param auxiliar The auxiliar to set.
	 */
	public void setAuxiliar(Boolean auxiliar) {
		this.auxiliar = auxiliar;
	}
	
//	Obtener tipo de cuenta
	  public String getTipoCuenta
	  (
		  String idEjercicio, String idCtaMayor
	  ) throws SQLException, Exception
	  {
		  String strTipoCuenta = null;
		  
		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  
		  try{		
			  if(conn == null || conn.isClosed())
				  conn = new Conexion().getConexionMateo(new Boolean(false));
			  
			  String COMANDO = "SELECT TIPO_CUENTA ";
			  COMANDO += "FROM mateo.CONT_CTAMAYOR ";
			  COMANDO += "WHERE ID_EJERCICIO = ? ";
			  COMANDO += "AND ID_CTAMAYOR = ? ";
			  COMANDO += "AND DETALLE = 'S' ";
			  
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, idEjercicio);
			  pstmt.setString(2, idCtaMayor);
			  
			  rset = pstmt.executeQuery();
			  
			  if (rset.next())
			  {
				  strTipoCuenta = rset.getString("Tipo_Cuenta");
			  }
			  rset.close();
			  pstmt.close();
			  
			  if(strTipoCuenta == null)
				  throw new Error("La cuenta "+idCtaMayor+" no existe");
			  
		  }catch(Exception e){
			  throw new Error("Error al obtener los datos de la cuenta de mayor "+idCtaMayor+"<br>"+e);
		  }finally{
			  if(pstmt != null) {pstmt.close(); pstmt = null;}
			  if(rset != null) {rset.close(); rset = null;}
			  if(!conn.isClosed()) {conn.close(); conn = null;}
		  }
		  
		  return strTipoCuenta;
	  }
}
