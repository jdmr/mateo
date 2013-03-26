/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.inscripciones.model.ccobro;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Constants;
//import mx.edu.um.afe.model.BecaAdicionalVO;
//import mx.edu.um.afe.model.CentroCostoAFEPuesto;
//import mx.edu.um.afe.model.ProyectoAFEPuesto;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;
//import mx.edu.um.model.User;

/**
 *
 * @author osoto
 */
public class Contrato {
    private Alumno alumno;
    private Integer contratoId;
    private Integer plazaId;
    private String clavePlaza;
    private Date fechaInicio;
    private Date fechaFinal;
    private String ejercicioId;
    private String ccostoId;
    private Integer proyectoId;
    private Integer maximoHoras;
    private BigDecimal precioHora;
    private BigDecimal diezmo;
    private BigDecimal importe;
    private BigDecimal becaAdicional;

    /**
     * @return the alumno
     */
    public Alumno getAlumno() {
        return alumno;
    }

    /**
     * @param alumno the alumno to set
     */
    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }

    /**
     * @return the contratoId
     */
    public Integer getContratoId() {
        return contratoId;
    }

    /**
     * @param contratoId the contratoId to set
     */
    public void setContratoId(Integer contratoId) {
        this.contratoId = contratoId;
    }

    /**
     * @return the plazaId
     */
    public Integer getPlazaId() {
        return plazaId;
    }

    /**
     * @param plazaId the plazaId to set
     */
    public void setPlazaId(Integer plazaId) {
        this.plazaId = plazaId;
    }

    /**
     * @return the clavePlaza
     */
    public String getClavePlaza() {
        return clavePlaza;
    }

    /**
     * @param clavePlaza the clavePlaza to set
     */
    public void setClavePlaza(String clavePlaza) {
        this.clavePlaza = clavePlaza;
    }

    /**
     * @return the fechaInicio
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * @param fechaInicio the fechaInicio to set
     */
    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * @return the fechaFinal
     */
    public Date getFechaFinal() {
        return fechaFinal;
    }

    /**
     * @param fechaFinal the fechaFinal to set
     */
    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    /**
     * @return the ejercicioId
     */
    public String getEjercicioId() {
        return ejercicioId;
    }

    /**
     * @param ejercicioId the ejercicioId to set
     */
    public void setEjercicioId(String ejercicioId) {
        this.ejercicioId = ejercicioId;
    }

    /**
     * @return the ccostoId
     */
    public String getCentroCostoId() {
        return ccostoId;
    }

    /**
     * @param ccostoId the ccostoId to set
     */
    public void setCentroCostoId(String ccostoId) {
        this.ccostoId = ccostoId;
    }

    /**
     * @return the proyectoId
     */
    public Integer getProyectoId() {
        return proyectoId;
    }

    /**
     * @param proyectoId the proyectoId to set
     */
    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
    }

    /**
     * @return the maximoHoras
     */
    public Integer getMaximoHoras() {
        return maximoHoras;
    }

    /**
     * @param maximoHoras the maximoHoras to set
     */
    public void setMaximoHoras(Integer maximoHoras) {
        this.maximoHoras = maximoHoras;
    }

    /**
     * @return the precioHora
     */
    public BigDecimal getPrecioHora() {
        return precioHora;
    }

    /**
     * @param precioHora the precioHora to set
     */
    public void setPrecioHora(BigDecimal precioHora) {
        this.precioHora = precioHora;
    }

    /**
     * @return the diezmo
     */
    public BigDecimal getDiezmo() {
        return diezmo;
    }

    /**
     * @param diezmo the diezmo to set
     */
    public void setDiezmo(BigDecimal diezmo) {
        this.diezmo = diezmo;
    }

    /**
     * @return the importe
     */
    public BigDecimal getImporte() {
        return importe;
    }

    /**
     * @param importe the importe to set
     */
    public void setImporte(BigDecimal importe) {
        this.importe = importe;
    }

    /**
     * @return the becaAdicional
     */
    public BigDecimal getBecaAdicional() {
        return becaAdicional;
    }

    /**
     * @param becaAdicional the becaAdicional to set
     */
    public void setBecaAdicional(BigDecimal becaAdicional) {
        this.becaAdicional = becaAdicional;
    }
    
    public static void limpiaTabla(String matricula, String carga_id, Integer bloque, Connection conn) throws Exception {
        //System.out.println("getcontratoInscrito");

        String COMANDO = "DELETE " +
                "FROM MATEO.FES_CC_AFE " +
                "WHERE MATRICULA = ? " +
                "AND CARGA_ID = ? " +
                "AND BLOQUE = ? ";
        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
        pstmt.setString(1, matricula);
        pstmt.setString(2, carga_id);
        pstmt.setInt(3, bloque);
        pstmt.executeQuery();
        pstmt.close();
    }


//    public static void grabaTabla(String matricula, String cargaId, Integer bloque, BecaAdicionalVO beca, Connection conn) throws Exception{
//        //System.out.println("Graba contrato");
//        Locale local = new java.util.Locale (Constants.LOCALE_LANGUAGE, Constants.LOCALE_COUNTRY, Constants.LOCALE_VARIANT);
//        SimpleDateFormat sdf = new SimpleDateFormat (Constants.DATE_SHORT_HUMAN_PATTERN, local);
//
//        PreparedStatement pstmt = null;
//
//        String sqlCols = "(MATRICULA, CARGA_ID, BLOQUE ";
//        String sqlValues = "('"+matricula+"', '"+cargaId+"', "+bloque+" ";
//        if(beca.getContrato() != null){
//            sqlCols += ",CONTRATO_ID, PLAZA_ID, FECHA_INICIO, FECHA_FINAL ";
//            sqlValues += " ,"+beca.getContrato().getId()+","+beca.getContrato().getPlaza().getId()+",to_date('"+sdf.format(beca.getContrato().getFechaInicio())+"','dd/mm/yy'), to_date('"+sdf.format(beca.getContrato().getFechaFinal())+"','dd/mm/yy')";
//            if(beca.getContrato().getPlaza() instanceof CentroCostoAFEPuesto){
//                CentroCostoAFEPuesto puesto = (CentroCostoAFEPuesto)beca.getContrato().getPlaza();
//                sqlCols += ",EJERCICIO_ID, CCOSTO_ID, MAXIMO_HORAS, IMPORTE, CLAVE_PLAZA, PRECIO_HORA, DIEZMO ";
//                sqlValues += ",'"+puesto.getCentroCosto().getEjercicio().getIdEjercicio()+"','"+puesto.getCentroCosto().getIdCCosto()+"',"+beca.getContrato().getNumeroHoras()+","+beca.getBecaBasica()+", " +
//                        puesto.getClave()+", "+beca.getContrato().getPlaza().getPrecioPorHora(beca.getContrato())+", "+beca.getContrato().getDiezmo()+" ";
//            }
//            else if(beca.getContrato().getPlaza() instanceof ProyectoAFEPuesto){
//                ProyectoAFEPuesto proyecto = (ProyectoAFEPuesto)beca.getContrato().getPlaza();
//                sqlCols += ",PROYECTO_ID, IMPORTE ";
//                sqlValues += ","+proyecto.getProyecto().getId()+","+beca.getContrato().getImporteAPagar()+" ";
//            }
//            if(beca.getContrato().getTipoBeca() != null){
//                //Tiene una beca adicional - de las nuevas
//                sqlCols += ", TIPOBECA_ID, TIPOBECA, NUM_HORAS_ADIC, IMPORTE_ADIC, PORCENTAJE, DIEZMA, TOTAL_BECA_ADIC ";
//                sqlValues += ", '"+beca.getContrato().getTipoBeca().getId()+"', '"+beca.getContrato().getTipoBeca().getDescripcion()+"', '"+beca.getContrato().getNumeroHorasAdicional()+"', "
//                        + " '"+beca.getContrato().getImporte()+"', '"+(beca.getContrato().getPorcentaje()?'1':'0')+"', '"+(beca.getContrato().getDiezma()?'1':'0')+"', '"+beca.getContrato().getTotalBecaAdic()+"' ";
//            }
//        }
//        if(beca.getBecaAdicional() != null){
//            sqlCols += ",BECA_ADICIONAL ";
//            sqlValues += ","+beca.getBecaAdicional()+" ";
//        }
//
//        String COMANDO = "INSERT INTO MATEO.FES_CC_AFE " +
//                sqlCols+" ) " +
//                "VALUES " +
//                sqlValues+" )";
//        //System.out.println(COMANDO);
//        try{
//            pstmt = conn.prepareStatement(COMANDO);
//            
//            pstmt.execute();
//
//        }catch(Exception e){
//            e.printStackTrace();
//            throw new UMException("Error al intentar grabar los datos del contrato en el Calculo de Cobro <br>"+e);
//        }finally{
//            pstmt.close();
//        }
//
//    }

    public static Contrato getContratoInscrito(String matricula, String carga_id, Integer bloque, Alumno alumno, Connection conn) throws Exception {
        //System.out.println("getcontratoInscrito");
        Contrato contrato = null;

        String COMANDO = "SELECT CONTRATO_ID, CLAVE_PLAZA, PLAZA_ID, FECHA_INICIO, FECHA_FINAL, EJERCICIO_ID, CCOSTO_ID, PROYECTO_ID, " +
                "MAXIMO_HORAS, PRECIO_HORA, DIEZMO, IMPORTE, BECA_ADICIONAL " +
                "FROM MATEO.FES_CC_AFE " +
                "WHERE MATRICULA = ? " +
                "AND CARGA_ID = ? " +
                "AND BLOQUE = ? ";
        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
        pstmt.setString(1, matricula);
        pstmt.setString(2, carga_id);
        pstmt.setInt(3, bloque);
        ResultSet rset = pstmt.executeQuery();

        if(rset.next()){
            contrato = new Contrato();
            contrato.setAlumno(alumno);
            contrato.setContratoId(rset.getInt("contrato_id"));
            contrato.setFechaInicio(rset.getDate("fecha_inicio"));
            contrato.setFechaFinal(rset.getDate("fecha_final"));

            //System.out.println("Evaluado si es plaza de contrato o de ccosto");
            rset.getInt("proyecto_id");
            if(rset.wasNull()){
                //System.out.println("Plaza de CCosto");
                contrato.setPlazaId(rset.getInt("plaza_id"));
                contrato.setClavePlaza(rset.getString("clave_plaza"));
                contrato.setEjercicioId(rset.getString("ejercicio_id"));
                contrato.setCentroCostoId(rset.getString("ccosto_id"));
                contrato.setPrecioHora(new BigDecimal(rset.getString("precio_hora")));
                contrato.setMaximoHoras(rset.getInt("maximo_horas"));
                contrato.setImporte(new BigDecimal(rset.getString("importe")));
                contrato.setDiezmo(new BigDecimal(rset.getString("diezmo")));
            }
            else{
                contrato.setProyectoId(rset.getInt("proyecto_id"));
                contrato.setImporte(new BigDecimal(rset.getString("importe")));
                contrato.setDiezmo(new BigDecimal(rset.getString("diezmo")));
            }
            contrato.setBecaAdicional(new BigDecimal(rset.getString("beca_adicional")));

        }

        return contrato;
    }
    
    public static Boolean estaConvenioFirmado(String matricula, Alumno alumno, Connection conn) throws Exception {
        //System.out.println("getcontratoActivo");

        String COMANDO = "SELECT COUNT(*) AS ROWCOUNT "
                + "FROM NOE.AFE_CONTRATO_ALUMNO "
                + "WHERE MATRICULA = ? "
                + "AND STATUS = 'A' "
                + "AND ENTREGADO = ? ";
        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
        pstmt.setString(1, matricula);
        pstmt.setString(2, Constants.STATUS_ENTREGADO);
        ResultSet rset = pstmt.executeQuery();

        if(rset.next()){
            if(rset.getInt("rowCount") == 1){
                return Boolean.TRUE;
            }
            else{
                return Boolean.FALSE;
            }
        }
        return Boolean.FALSE;
    }
    public static void registraBecaExcedenteInstitucional(String matricula, String carga, Integer bloque, BigDecimal excedente, Long user, Connection conn) throws Exception {
        //System.out.println("registraBecaExcedente");

        String COMANDO = "SELECT COUNT(*) AS ROWCOUNT "
                + "FROM NOE.ALUMNO_BECA "
                + "WHERE MATRICULA = ? "
                + "AND STATUS = 'A' ";
        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
        pstmt.setString(1, matricula);
        ResultSet rset = pstmt.executeQuery();

        if(rset.next()){
            if(rset.getInt("rowCount") == 0){
                //Insertar registro
                COMANDO = "INSERT INTO NOE.ALUMNO_BECA "
                        + "(ID, VERSION, MATRICULA, BECA, STATUS, USER_ALTA_ID, FECHA_ALTA, USER_MODIFICA_ID, FECHA_MODIFICA, CARGA_ID, BLOQUE_ID, BECA1, BECA2, BECA3) "
                        + "VALUES "
                        + "((SELECT MAX(ID)+1 FROM NOE.ALUMNO_BECA), 0, ?, 0, 'A', ?, sysdate, null, null, ?, ?, 0, ?, 0)";
                PreparedStatement pstmt2 = conn.prepareStatement(COMANDO);
                pstmt2.setString(1, matricula);
                pstmt2.setLong(2, user);
                pstmt2.setString(3, carga);
                pstmt2.setInt(4, bloque);
                pstmt2.setBigDecimal(5, excedente);
                pstmt2.executeUpdate();
                pstmt2.close();
            }
            else{
                COMANDO = "UPDATE NOE.ALUMNO_BECA "
                        + "SET BECA2 = ?, CARGA_ID = ?, BLOQUE_ID = ? "
                        + "WHERE MATRICULA = ? "
                        + "AND STATUS = 'A' ";
                PreparedStatement pstmt2 = conn.prepareStatement(COMANDO);
                pstmt2.setBigDecimal(1, excedente);
                pstmt2.setString(2, carga);
                pstmt2.setInt(3, bloque);
                pstmt2.setString(4, matricula);
                pstmt2.executeUpdate();
                pstmt2.close();
            }
        }
        rset.close();
        pstmt.close();
    }
    
}
