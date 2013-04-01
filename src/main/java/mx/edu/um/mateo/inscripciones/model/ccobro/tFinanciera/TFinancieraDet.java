/*
 * Created on Dec 23, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.tFinanciera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import mx.edu.um.mateo.inscripciones.model.ccobro.academico.Carrera;
import mx.edu.um.mateo.inscripciones.model.ccobro.academico.Clasificacion;
import mx.edu.um.mateo.inscripciones.model.ccobro.academico.Modalidad;
import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TFinancieraDet {
	private Integer tFinancieraID;
	private Carrera carrera;
	private Modalidad modalidad;
	private Double pMatricula;
	private Double pTLegales;
	private Double pInternado;
	private Double pCCredito;
	private Double pCMateria;
	private Connection conn;
	/**
	 * @param carrera
	 * @param modalidad
	 * @param matricula
	 * @param legales
	 * @param internado
	 * @param credito
	 * @param materia
	 */
	public TFinancieraDet(Carrera carrera, Modalidad modalidad,
			Double matricula, Double legales, Double internado, Double credito, Double materia) {
		super();
		this.carrera = carrera;
		this.modalidad = modalidad;
		this.setPMatricula(matricula);
		this.setPTLegales(legales);
		this.setPInternado(internado);
		this.setPCCredito(credito);
		this.setPCMateria(materia);
	}
	/**
	 * @param financieraID
	 * @param carrera
	 * @param matricula
	 * @param legales
	 * @param internado
	 * @param credito
	 * @param materia
	 */
	public TFinancieraDet(Integer financieraID, Carrera carrera, Modalidad modalidad,
			Double matricula, Double legales, Double internado, Double credito, Double materia) {
		super();
		tFinancieraID = financieraID;
		this.carrera = carrera;
		this.modalidad = modalidad;
		this.setPMatricula(matricula);
		this.setPTLegales(legales);
		this.setPInternado(internado);
		this.setPCCredito(credito);
		this.setPCMateria(materia);
	}
	/**
	 * 
	 */
	public TFinancieraDet() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return Returns the carrera.
	 */
	public Carrera getCarrera() {
		return carrera;
	}
	/**
	 * @param carrera The carrera to set.
	 */
	public void setCarrera(Carrera carrera) {
		this.carrera = carrera;
	}
	/**
	 * @return Returns the modalidad.
	 */
	public Modalidad getModalidad() {
		return modalidad;
	}
	/**
	 * @param modalidad The modalidad to set.
	 */
	public void setModalidad(Modalidad modalidad) {
		this.modalidad = modalidad;
	}
	/**
	 * @return Returns the pCCredito.
	 */
	public Double getPCCredito() {
		return pCCredito;
	}
	/**
	 * @param credito The pCCredito to set.
	 */
	public void setPCCredito(Double credito) {
		pCCredito = credito;
	}
	/**
	 * @return Returns the pInternado.
	 */
	public Double getPInternado() {
		return pInternado;
	}
	/**
	 * @param internado The pInternado to set.
	 */
	public void setPInternado(Double internado) {
		pInternado = internado;
	}
	/**
	 * @return Returns the pMatricula.
	 */
	public Double getPMatricula() {
		return pMatricula;
	}
	/**
	 * @param matricula The pMatricula to set.
	 */
	public void setPMatricula(Double matricula) {
		pMatricula = matricula;
	}
	/**
	 * @return Returns the pTLegales.
	 */
	public Double getPTLegales() {
		return pTLegales;
	}
	/**
	 * @param legales The pTLegales to set.
	 */
	public void setPTLegales(Double legales) {
		pTLegales = legales;
	}	
	/**
	 * @return Returns the tFinancieraID.
	 */
	public Integer getTFinancieraID() {
		return tFinancieraID;
	}
	/**
	 * @param financieraID The tFinancieraID to set.
	 */
	public void setTFinancieraID(Integer financieraID) {
		tFinancieraID = financieraID;
	}
	public Double getPCMateria(){
		return this.pCMateria;
	}
	public void setPCMateria(Double materia){
		this.pCMateria = materia;
	}
	public Map getDetalles(TFinancieraEnc encabezado) throws Exception{
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Map mDetalles = new TreeMap();
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionNoe(new Boolean(false));
			
			String COMANDO = "SELECT CARRERA_ID, MODALIDAD_ID, PMATRICULA, PTLEGALES, PINTERNADO, PCCREDITO, PCMATERIA " +
					"FROM noe.FES_TFINANCIERA_DET " +
					"WHERE TFINANCIERA_ID = ? ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setInt(1, encabezado.getTFinancieraId().intValue());
			rset = pstmt.executeQuery();
			
			while(rset.next()){				
				mDetalles.put(rset.getString("Carrera_ID")+rset.getString("Modalidad_ID"), new TFinancieraDet(encabezado.getTFinancieraId(), new Carrera(rset.getString("Carrera_ID")), new Modalidad(new Integer(rset.getInt("Modalidad_ID"))), new Double(rset.getDouble("PMatricula")), new Double(rset.getDouble("PTLegales")), new Double(rset.getDouble("PInternado")), new Double(rset.getDouble("PCCredito")), new Double(rset.getDouble("PCMateria"))));
			}
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new Error("Error al obtener los detalles de la tabla financiera "+encabezado.getCarga().getCargaId()+" <br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
		return mDetalles;
	}
	public void setDetalles(TFinancieraEnc encabezado, Map detalles) throws Exception{
		PreparedStatement pstmt = null;		
		String COMANDO = null;
		Boolean sw = new Boolean(true);
		
		String paso = "1";
		
		Map mDetalles = this.getDetalles(encabezado);
		paso = "2";
		Iterator iDetalles = detalles.keySet().iterator();
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionNoe(new Boolean(true));
		
			while(iDetalles.hasNext()){
				paso = "3";
				TFinancieraDet detalle = (TFinancieraDet)detalles.get(iDetalles.next());
				
				//Si ya existe un registro en tabla financiera detalle, actualizar
				paso = "4";
				
				if(mDetalles.containsKey(detalle.getCarrera().getCarreraId()+detalle.getModalidad().getModalidadId())){
					
					//Al realizar el query se debe evaluar que porcentaje trae un valor mayor o igual que cero
					
					COMANDO = "UPDATE noe.FES_TFINANCIERA_DET " +
					"SET ";
					
					Clasificacion clasificacion = (Clasificacion)encabezado.getClasificacion().get(new Integer(1));
					if(clasificacion.getCCredito().compareTo(new Double(0)) > 0){					
						detalle.setPCMateria(new Double(-1));
					}
					else if(clasificacion.getCMateria().compareTo(new Double(0)) > 0){
						detalle.setPCCredito(new Double(-1));
					}
					else{
						COMANDO +="PCCREDITO = 0, PCMATERIA = 0, ";
					}
											
					if(detalle.getPMatricula().compareTo(new Double(0)) >= 0){
						COMANDO += "PMATRICULA = ?, ";
						sw = new Boolean(false);
					}
					if(detalle.getPTLegales().compareTo(new Double(0)) >= 0){
						COMANDO += "PTLEGALES = ?, ";
						sw = new Boolean(false);
					}
					if(detalle.getPCCredito().compareTo(new Double(0)) >= 0){
						COMANDO += "PCCREDITO = ?, PCMATERIA = 0, ";
						sw = new Boolean(false);
					}
					if(detalle.getPCMateria().compareTo(new Double(0)) >= 0){
						COMANDO += "PCMATERIA = ?, PCCREDITO = 0, ";
						sw = new Boolean(false);
					}
					if(detalle.getPInternado().compareTo(new Double(0)) >= 0){
						COMANDO += "PINTERNADO = ?, ";
						sw = new Boolean(false);
					}
					
					COMANDO = COMANDO.substring(0,COMANDO.length()-2)+" ";
					COMANDO += "WHERE TFINANCIERA_ID = ? " +
					"AND CARRERA_ID = ? " +
					"AND MODALIDAD_ID = ? ";
					
					//System.out.println("TFinancieroDet - Linea 272" + COMANDO);
					
					if(!sw.booleanValue()){
						pstmt = conn.prepareStatement(COMANDO);
						Integer conta = new Integer(1);
						if(detalle.getPMatricula().compareTo(new Double(0)) >= 0){
							pstmt.setDouble(conta.intValue(), detalle.getPMatricula().doubleValue()/100);
							conta = new Integer(conta.intValue() + 1);
						}
						if(detalle.getPTLegales().compareTo(new Double(0)) >= 0){
							pstmt.setDouble(conta.intValue(), detalle.getPTLegales().doubleValue()/100);
							conta = new Integer(conta.intValue() + 1);
						}
						if(detalle.getPCCredito().compareTo(new Double(0)) >= 0){
							pstmt.setDouble(conta.intValue(), detalle.getPCCredito().doubleValue()/100);
							conta = new Integer(conta.intValue() + 1);
						}
						if(detalle.getPCMateria().compareTo(new Double(0)) >= 0){
							pstmt.setDouble(conta.intValue(), detalle.getPCMateria().doubleValue()/100);
							conta = new Integer(conta.intValue() + 1);
						}
						if(detalle.getPInternado().compareTo(new Double(0)) >= 0){
							pstmt.setDouble(conta.intValue(), detalle.getPInternado().doubleValue()/100);
							conta = new Integer(conta.intValue() + 1);
						}
						
						pstmt.setInt(conta.intValue(), encabezado.getTFinancieraId().intValue());
						conta = new Integer(conta.intValue() + 1);
						pstmt.setString(conta.intValue(), detalle.getCarrera().getCarreraId());
						conta = new Integer(conta.intValue() + 1);
						pstmt.setInt(conta.intValue(), detalle.getModalidad().getModalidadId().intValue());
					}
					paso = "5";
				}			
				else{
					paso = "6";
					sw = new Boolean(false);
					COMANDO = "INSERT INTO noe.FES_TFINANCIERA_DET " +
					"(TFINANCIERA_ID, CARRERA_ID, MODALIDAD_ID, PMATRICULA, PTLEGALES, PINTERNADO, PCCREDITO, PCMATERIA) " +
					"VALUES " +
					"(?,?,?,?,?,?,?,?) ";
					
					pstmt = conn.prepareStatement(COMANDO);
					pstmt.setInt(1, encabezado.getTFinancieraId().intValue());
					pstmt.setString(2, detalle.getCarrera().getCarreraId());
					pstmt.setInt(3, detalle.getModalidad().getModalidadId().intValue());
					pstmt.setDouble(4, detalle.getPMatricula().doubleValue()/100);
					pstmt.setDouble(5, detalle.getPTLegales().doubleValue()/100);
					pstmt.setDouble(6, detalle.getPInternado().doubleValue()/100);
					pstmt.setDouble(7, detalle.getPCCredito().doubleValue()/100);
					pstmt.setDouble(8, detalle.getPCMateria().doubleValue()/100);
					paso = "7";
				}
				if(!sw.booleanValue()){
					pstmt.execute();
					pstmt.close();
				}
				sw = new Boolean(true);
			}		
			
			paso = "8";
			conn.commit();
		}catch(Exception e){
			conn.rollback();
			throw new Error("Error al ingresar los detalles de la tabla financiera "+encabezado.getCarga().getCargaId()+" <br>"+e+" "+paso);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}			
			if(!this.conn.isClosed()) {this.conn.close(); this.conn = null;}
		}
	}	
}
