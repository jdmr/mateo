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
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;
import java.math.BigDecimal;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Movimiento implements Constant{
	private String matricula;
	private String carga_id;
	private Integer bloque;
	private String tipoMov;
	private String descripcion;
	private Double importe;
	private String naturaleza;
	private String contabiliza;
	private String aplica_en;
	private String id_ctaMayor;
	private String id_ccosto;
	private String id_auxiliar;
	
	private Connection conn;
	
	/**
	 * @return Returns the aplica_en.
	 */
	public String getAplica_en() {
		return aplica_en;
	}
	/**
	 * @param aplica_en The aplica_en to set.
	 */
	public void setAplica_en(String aplica_en) {
		this.aplica_en = aplica_en;
	}
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
	 * @return Returns the contabiliza.
	 */
	public String getContabiliza() {
		return contabiliza;
	}
	/**
	 * @param contabiliza The contabiliza to set.
	 */
	public void setContabiliza(String contabiliza) {
		this.contabiliza = contabiliza;
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
	 * @return Returns the id_auxiliar.
	 */
	public String getId_auxiliar() {
		return id_auxiliar;
	}
	/**
	 * @param id_auxiliar The id_auxiliar to set.
	 */
	public void setId_auxiliar(String id_auxiliar) {
		this.id_auxiliar = id_auxiliar;
	}
	/**
	 * @return Returns the id_ccosto.
	 */
	public String getId_ccosto() {
		return id_ccosto;
	}
	/**
	 * @param id_ccosto The id_ccosto to set.
	 */
	public void setId_ccosto(String id_ccosto) {
		this.id_ccosto = id_ccosto;
	}
	/**
	 * @return Returns the id_ctaMayor.
	 */
	public String getId_ctaMayor() {
		return id_ctaMayor;
	}
	/**
	 * @param id_ctaMayor The id_ctaMayor to set.
	 */
	public void setId_ctaMayor(String id_ctaMayor) {
		this.id_ctaMayor = id_ctaMayor;
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
	 * @return Returns the naturaleza.
	 */
	public String getNaturaleza() {
		return naturaleza;
	}
	/**
	 * @param naturaleza The naturaleza to set.
	 */
	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}
	/**
	 * @return Returns the tipoMov.
	 */
	public String getTipoMov() {
		return tipoMov;
	}
	/**
	 * @param tipoMov The tipoMov to set.
	 */
	public void setTipoMov(String tipoMov) {
		this.tipoMov = tipoMov;
	}
	/**
	 * @param matricula
	 * @param carga_id
	 * @param bloque
	 */
	public Movimiento(String matricula, String carga_id, Integer bloque) {
		super();
		this.matricula = matricula;
		this.carga_id = carga_id;
		this.bloque = bloque;
	}
	/**
	 * @param matricula
	 * @param carga_id
	 * @param bloque
	 * @param tipoMov
	 * @param descripcion
	 * @param importe
	 * @param naturaleza
	 * @param contabiliza
	 * @param aplica_en 
	 */
	public Movimiento(String matricula, String carga_id, Integer bloque,
			String tipoMov, String descripcion, Double importe,
			String naturaleza, String contabiliza, String aplica_en,
			String idCCosto) throws Exception {
		this.matricula = matricula;
		this.carga_id = carga_id;
		this.bloque = bloque;
		this.tipoMov = tipoMov;
		this.descripcion = descripcion;
		this.importe = importe;
		this.naturaleza = naturaleza;
		this.contabiliza = contabiliza;
		this.aplica_en = aplica_en;
		
		this.id_auxiliar = "0000000";        

        /*Cualquier movimiento cuyo importe sea cero, no contabiliza*/
        if (this.importe.compareTo(new Double(0)) == 0)
		{
			this.contabiliza = "N";
		}
        
        if(contabiliza.equals("S")){
        	if(idCCosto == null)
        		throw new UMException("Un movimiento del alumno "+matricula+" que debe contabilizar tiene contabilidad invalida "+idCCosto);
        	this.id_ccosto = idCCosto;
        }

        /*Evaluar el tipo de movimiento para asignar la cuenta respectiva*/
        /*Ya debe estar asignado el valor de la entidad del alumno*/
        if (this.tipoMov.equals(ccfstrMatriculaID))
                this.id_ctaMayor = "2.1.02.01";                

        else if (this.tipoMov.equals(ccfstrEnsenanzaID))
        	this.id_ctaMayor = "2.1.02.01";

        else if (this.tipoMov.equals(ccfstrInternadoID))
        	this.id_ctaMayor = "2.1.02.01";

        else if (this.tipoMov.equals(ccfstrManejoPagareID))
        	this.id_ctaMayor = "2.1.02.01";

        else if (this.tipoMov.equals(ccfstrMExtemporaneaID))
        	this.id_ctaMayor = "2.1.02.01";

        else if (this.tipoMov.equals(ccfstrDesctoContadoID))
        	this.id_ctaMayor = "2.1.02.05";

        else if (this.tipoMov.equals(ccfstrDesctoObreroID))
        	this.id_ctaMayor = "2.1.02.05";
        
        else if (this.tipoMov.equals(ccfstrDesctoHObreroID))
        	this.id_ctaMayor = "2.1.02.05";

        else if (this.tipoMov.equals(ccfstrBecasID))
        	this.id_ctaMayor = "2.1.02.04";

        else if (this.tipoMov.equals(ccfstrDesctoMExtID))
        	this.id_ctaMayor = "2.1.02.05";
	}
	
	/*Calcular costo bloque*/
	public Double getCostoBloque(Map mMovimientos) throws Exception
	{
		Double dblCreditos = new Double(0);
		Double dblCargos = new Double(0);

		//Leer solo aquellos movimientos del calculo de cobro que contienen
		//conceptos que se despliegan como parte del costo total del bloque.
		Iterator iMovimientos = mMovimientos.keySet().iterator();
		
		while (iMovimientos.hasNext()){
			Movimiento movimiento = (Movimiento)mMovimientos.get((String)iMovimientos.next());
			
			if(!movimiento.getAplica_en().equals("T")){
				
				if (movimiento.getNaturaleza().equals("C"))
					dblCreditos = new Double(dblCreditos.doubleValue() + movimiento.getImporte().doubleValue());
				else if (movimiento.getNaturaleza().equals("D"))
					dblCargos = new Double(dblCargos.doubleValue() + movimiento.getImporte().doubleValue());
			}
			
		}
		return new Double(dblCreditos.doubleValue() - dblCargos.doubleValue());
	}
	
	/*Este metodo permite obtener el importe acumulado de cualquier tipo de movimiento, dependiendo del atributo aplica_en*/
	public Double getImporte(Map mMovimientos, String tipo_mov, String naturaleza, String contabiliza, String aplica_en) throws Exception
	{		
		Iterator iMovimientos = mMovimientos.keySet().iterator();
		Double importe = new Double(0);
		
		while (iMovimientos.hasNext()){
			Movimiento movimiento = (Movimiento)mMovimientos.get((String)iMovimientos.next());
			
			if(movimiento.getAplica_en().equals(aplica_en)){
				if(movimiento.getContabiliza().equals(contabiliza)){				
					if (movimiento.getNaturaleza().equals(naturaleza))
						importe = new Double(importe.doubleValue() + movimiento.getImporte().doubleValue());
					else if (movimiento.getNaturaleza().equals(naturaleza))
						importe = new Double(importe.doubleValue() + movimiento.getImporte().doubleValue());
				}
			}			
		}
        
		return importe;
	}
	
	/*Este metodo permite obtener el importe acumulado de cualquier tipo de movimiento, dependiendo del atributo aplica_en*/
	public Double getImporte(Map mMovimientos, Map mTipo_mov, String aplica_en, String naturaleza) throws Exception
	{		
		Iterator iMovimientos = mMovimientos.keySet().iterator();
		Double importe = new Double(0);
		
		while (iMovimientos.hasNext()){
			Movimiento movimiento = (Movimiento)mMovimientos.get((String)iMovimientos.next());			
			Iterator iTipo_mov = mTipo_mov.keySet().iterator();
			while (iTipo_mov.hasNext()){
				String tipo_mov = (String)mTipo_mov.get((String)iTipo_mov.next());				
				if(tipo_mov.equals(movimiento.getTipoMov())){					
					if(movimiento.getAplica_en().equals(aplica_en)){						
						if (movimiento.getNaturaleza().equals(naturaleza))
							importe = new Double(importe.doubleValue() + movimiento.getImporte().doubleValue());
						else if (movimiento.getNaturaleza().equals(naturaleza))
							importe = new Double(importe.doubleValue() + movimiento.getImporte().doubleValue());
					}						
				}
			}									
		}		 
		
		return importe;
	}
	
	/**
	 * Este metodo permite obtener el importe acumulado de cualquier tipo de movimiento, dependiendo del atributo aplica_en
	 * Si negacion es true, indica que al comparar aplica_en se negara el resultado
	 * @param mMovimientos
	 * @param aplica_en
	 * @param negacion
	 * @return
	 * @throws Exception
	 */	 
	public Double getImporte(Map mMovimientos, String aplica_en, Boolean negacion) throws Exception
	{		
		Iterator iMovimientos = mMovimientos.keySet().iterator();
		Double importe = new Double(0);
		
		while (iMovimientos.hasNext()){
			Movimiento movimiento = (Movimiento)mMovimientos.get((String)iMovimientos.next());
                        
			Boolean evalua = new Boolean(movimiento.getAplica_en().equals(aplica_en));
			
			//Si se debe evaluar que el valor sea distinto 
			if(negacion){
				evalua = new Boolean(!evalua);
			}
			
			if (evalua){
                                if (movimiento.getNaturaleza().equals("C"))
					importe = new Double(importe.doubleValue() + movimiento.getImporte().doubleValue());
				else if (movimiento.getNaturaleza().equals("D"))
					importe = new Double(importe.doubleValue() - movimiento.getImporte().doubleValue());
			}					
												
		}

        //Si el importe es a favor del estudiante, en el concepto de pago inicial...
        System.out.print("Movimiento "+importe);
		if(importe.compareTo(new Double(0)) > 0){
            importe = 0.0;
        }
        System.out.print("Movimiento-after "+importe);
		
		return importe;
	}
	
	/**
	 * 
	 */
	public Movimiento() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static void limpiaTabla (Connection conn, String matricula, String carga_id, Integer bloque) throws  Exception {
		PreparedStatement pstmt = null;	
		
		try{			
			String	COMANDO = "DELETE " +
					"FROM MATEO.FES_CC_MOVIMIENTO " +
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
			throw new UMException("Error al inicializar los movimientos del alumno "+matricula+" "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}
	
	/*Grabar movimientos del alumno en la base de datos*/
	public static void grabaTabla (Connection conn, Map mMovimientos, Alumno alumno) throws Exception{
		PreparedStatement pstmt = null;
		
		try{
			Iterator iMovimientos = mMovimientos.keySet().iterator();
			while(iMovimientos.hasNext()){
				Movimiento movimiento = (Movimiento)mMovimientos.get((String)iMovimientos.next());
				
				if(movimiento.getMatricula().equals(alumno.getMatricula ())){
					/*Insertar movimiento*/
					String COMANDO = "INSERT INTO MATEO.FES_CC_MOVIMIENTO ";
					COMANDO += "(ID, MATRICULA, CARGA_ID, BLOQUE, TIPOMOV, ";
					COMANDO += "DESCRIPCION, IMPORTE, NATURALEZA, CONTABILIZA, ";
					COMANDO += "APLICA_EN, ID_CTAMAYOR, ID_CCOSTO, ID_AUXILIAR, CCOBRO_ID, VERSION) ";
					COMANDO += "VALUES ";
					COMANDO += "((SELECT COALESCE(MAX(ID),0)+1 FROM MATEO.FES_CC_MOVIMIENTO), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
                                        
					pstmt = conn.prepareStatement(COMANDO);
					pstmt.setString(1, movimiento.getMatricula());
					pstmt.setString(2, movimiento.getCarga_id());
					pstmt.setInt(3, movimiento.getBloque().intValue());
					pstmt.setString(4, movimiento.getTipoMov());
					pstmt.setString(5, movimiento.getDescripcion());
					pstmt.setDouble(6, movimiento.getImporte().doubleValue());
					pstmt.setString(7, movimiento.getNaturaleza());
					pstmt.setString(8, movimiento.getContabiliza());
					pstmt.setString(9, movimiento.getAplica_en());
					pstmt.setString(10, movimiento.getId_ctaMayor());
					pstmt.setString(11, movimiento.getId_ccosto());
					pstmt.setString(12, movimiento.getId_auxiliar());
                                        pstmt.setInt (13, alumno.getId ().intValue ());                                        
					pstmt.execute();
					pstmt.close();
				}								
			}			
		}catch(Exception e){
			throw new UMException("Error al insertar los movimientos del calculo de cobro del alumno "+alumno.getMatricula ()+" "+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
		}
	}
	
	/**
	 * Obtener los movimientos del calculo de cobro
	 * @param matricula
	 * @param carga_id
	 * @param bloque
	 * @param seccion
	 *   1. CB - Obtener los movimientos del costo del bloque
	 *   2. CI - Obtener los movimientos del costo de inscripcion
	 *   3. OT - Obtener los otros movimientos
	 *   4. TODOS - Obtener todos los movimientos
	 *   5. CONT - Obtener los movimientos que contabilizan
	 * @return
	 * @throws Exception
	 */
        public Map getMovimientosCC (String matricula, String carga_id, Integer bloque, String seccion) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Map mMovimientos = new TreeMap();
		
		try{
			if(conn == null || conn.isClosed())
				conn = new Conexion().getConexionMateo(new Boolean(false));
			
			//Obtener movimientos del costo de bloque
			if (seccion.equals("CB"))
			{
				String COMANDO = "SELECT TIPOMOV, DESCRIPCION, ";
				COMANDO += "IMPORTE * CASE NATURALEZA WHEN 'D' THEN -1 ELSE 1 END AS IMPORTE, ";
				COMANDO += "NATURALEZA, CONTABILIZA, APLICA_EN, ID_CCOSTO ";
				COMANDO += "FROM mateo.FES_CC_MOVIMIENTO ";
				COMANDO += "WHERE MATRICULA = ? ";
				COMANDO += "AND CARGA_ID = ? ";
				COMANDO += "AND BLOQUE = ? ";
				COMANDO += "AND TIPOMOV BETWEEN '01' AND '19' ";
				COMANDO += "ORDER BY TIPOMOV";
				pstmt = conn.prepareStatement(COMANDO);
				pstmt.setString(1, matricula);
				pstmt.setString(2, carga_id);
				pstmt.setInt(3, bloque.intValue());				
			}
			//Obtener movimientos de la cuota de inscripci?n
			else if (seccion.equals("CI"))
			{
				String COMANDO = "SELECT TIPOMOV, DESCRIPCION, ";
				COMANDO += "IMPORTE * CASE NATURALEZA WHEN 'D' THEN -1 ELSE 1 END AS IMPORTE, ";
				COMANDO += "NATURALEZA, CONTABILIZA, APLICA_EN, ID_CCOSTO ";
				COMANDO += "FROM mateo.FES_CC_MOVIMIENTO ";
				COMANDO += "WHERE MATRICULA = ? ";
				COMANDO += "AND CARGA_ID = ? ";
				COMANDO += "AND BLOQUE = ? ";
				COMANDO += "AND TIPOMOV BETWEEN '20' AND '39' ";
				COMANDO += "ORDER BY TIPOMOV";
				pstmt = conn.prepareStatement(COMANDO);
				pstmt.setString(1, matricula);
				pstmt.setString(2, carga_id);
				pstmt.setInt(3, bloque.intValue());				
			}
			//Obtener movimientos de los otros movimientos
			else if (seccion.equals("OT"))
			{
				String COMANDO = "SELECT TIPOMOV, DESCRIPCION, ";
				COMANDO += "IMPORTE * CASE NATURALEZA WHEN 'D' THEN -1 ELSE 1 END AS IMPORTE, ";
				COMANDO += "NATURALEZA, CONTABILIZA, APLICA_EN, ID_CCOSTO ";
				COMANDO += "FROM mateo.FES_CC_MOVIMIENTO ";
				COMANDO += "WHERE MATRICULA = ? ";
				COMANDO += "AND CARGA_ID = ? ";
				COMANDO += "AND BLOQUE = ? ";
				COMANDO += "AND TIPOMOV BETWEEN '50' AND '70' ";
				COMANDO += "ORDER BY TIPOMOV";
				pstmt = conn.prepareStatement(COMANDO);
				pstmt.setString(1, matricula);
				pstmt.setString(2, carga_id);
				pstmt.setInt(3, bloque.intValue());				
			}
			else if (seccion.equals("TODOS"))
			{
				String COMANDO = "SELECT TIPOMOV, DESCRIPCION, ";
				COMANDO += "IMPORTE,  ";
				COMANDO += "NATURALEZA, CONTABILIZA, APLICA_EN, ID_CCOSTO ";
				COMANDO += "FROM mateo.FES_CC_MOVIMIENTO ";
				COMANDO += "WHERE MATRICULA = ? ";
				COMANDO += "AND CARGA_ID = ? ";
				COMANDO += "AND BLOQUE = ? ";				
				COMANDO += "ORDER BY TIPOMOV";
				pstmt = conn.prepareStatement(COMANDO);
				pstmt.setString(1, matricula);
				pstmt.setString(2, carga_id);
				pstmt.setInt(3, bloque.intValue());
			}
			else if (seccion.equals("CONT"))
			{
				String COMANDO = "SELECT TIPOMOV, DESCRIPCION, ";
				COMANDO += "IMPORTE,  ";
				COMANDO += "NATURALEZA, CONTABILIZA, APLICA_EN, ID_CCOSTO ";
				COMANDO += "FROM mateo.FES_CC_MOVIMIENTO ";
				COMANDO += "WHERE MATRICULA = ? ";
				COMANDO += "AND CARGA_ID = ? ";
				COMANDO += "AND BLOQUE = ? ";
				COMANDO += "AND CONTABILIZA = 'S' ";
				COMANDO += "ORDER BY TIPOMOV";
				pstmt = conn.prepareStatement(COMANDO);
				pstmt.setString(1, matricula);
				pstmt.setString(2, carga_id);
				pstmt.setInt(3, bloque.intValue());
			}
			
			rset = pstmt.executeQuery();
			Integer contador = new Integer(1);
			while(rset.next()){
				Movimiento movimiento = new Movimiento(matricula, carga_id, bloque, rset.getString("TipoMov"), 
						rset.getString("Descripcion"), new Double(rset.getDouble("Importe")), rset.getString("Naturaleza"), 
						rset.getString("Contabiliza"), rset.getString("Aplica_En"), rset.getString("id_ccosto"));
				mMovimientos.put(matricula+carga_id+bloque+contador,movimiento);
				contador = new Integer(contador.intValue()+1);				
			}
			
			pstmt.close();
			rset.close();
			
		}catch(Exception e){
			throw new UMException("Error al obtener los movimientos del alumno "+matricula+" en la carga "+carga_id+" y el bloque "+bloque+"<br>"+e);
		}finally{
			if(pstmt != null) {pstmt.close(); pstmt = null;}
			if(rset != null) {rset.close(); rset = null;}
			if(!conn.isClosed()) {conn.close(); conn = null;}
		}
		
		return mMovimientos;
	}
	/**
	 * Recorre el map de movimientos y regresa el importe del movimiento solicitado
	 * En caso de que el tipo de movimiento se repita, se regresara solo el primer movimiento
	 * @param mMovimientos
	 * @param tipoMov
	 * @return Double
	 * @throws Exception
	 */
	 public Double getMovimiento(Map mMovimientos, String tipoMov) throws Exception{
	 	Double importe = null;
	 	
	 	Iterator iMovimientos = mMovimientos.keySet().iterator();
	 	while(iMovimientos.hasNext()){
	 		Movimiento movimiento = (Movimiento)mMovimientos.get(iMovimientos.next());
	 		if(movimiento.getTipoMov().equals(tipoMov)){
	 			importe = movimiento.getImporte();
	 			break;
	 		}
	 	}
	 	return importe;
	 }
         
         public static BigDecimal getCuotaInscripcion(Connection conn, String matricula, String carga_id, Integer bloque) throws Exception{
             PreparedStatement pstmt = null;
             ResultSet rset = null;
            BigDecimal returnValue = new BigDecimal("0.0");
             
         
             try{
                
                StringBuilder COMANDO = new StringBuilder();
                COMANDO.append("SELECT sum(IMPORTE * CASE NATURALEZA WHEN 'D' THEN -1 ELSE 1 END) AS IMPORTE ");
                COMANDO.append("FROM mateo.FES_CC_MOVIMIENTO ");
                COMANDO.append("WHERE MATRICULA = ? " );
                COMANDO.append("AND CARGA_ID = ? ");
                COMANDO.append("AND BLOQUE = ? ");
                COMANDO.append("AND TIPOMOV BETWEEN '20' AND '39' " );
                
                pstmt = conn.prepareStatement(COMANDO.toString());
                pstmt.setString(1, matricula);
                pstmt.setString(2, carga_id);
                pstmt.setInt(3, bloque);
                
                rset = pstmt.executeQuery();
                if(rset.next()){
                    returnValue = new BigDecimal(rset.getString("importe"));
                }
                
             }catch(Exception e){
                throw new UMException("Error al obtener los movimientos del alumno "+matricula+" en la carga "+carga_id+" y el bloque "+bloque+"<br>"+e);
             }finally{
                if(pstmt != null) {pstmt.close(); pstmt = null;}
                if(rset != null) {rset.close(); rset = null;}
            }
            return returnValue;
         }
}
