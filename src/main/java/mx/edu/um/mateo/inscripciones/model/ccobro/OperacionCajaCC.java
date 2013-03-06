/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model.ccobro;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Constants;
import mx.edu.um.mateo.inscripciones.model.ccobro.financiero.TipoOperacionCaja;

/**
 *
 * @author osoto
 */
public class OperacionCajaCC {
    public static void limpiaTabla(DataSource ds, String matricula, TipoOperacionCaja tipoOperacion) throws Exception{
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try{
            conn = ds.getConnection();
            String COMANDO = "DELETE FROM MATEO.CONT_OPERACION_CAJA WHERE MATRICULA = ? AND TIPO_OPERACION_ID = ? AND STATUS = ? ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setInt(2, tipoOperacion.getId());
            pstmt.setString(3, Constants.STATUS_ACTIVO);
            pstmt.execute();
            
        }catch (Exception e){
            throw new Exception("Error al intenter borrar las operaciones de caja "+e);
        }finally{
            if(pstmt != null)pstmt.close();
        }
    }
    
    
    public static void grabaTabla(DataSource ds, String matricula, TipoOperacionCaja tipoOperacion, BigDecimal importe, Integer folio, Long user) throws Exception{
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        if(importe.compareTo(new BigDecimal("0.00")) < 0){
        
            try{
                conn = ds.getConnection();
                StringBuilder COMANDO = new StringBuilder();
                COMANDO.append("INSERT INTO MATEO.CONT_OPERACION_CAJA  ");
                COMANDO.append(" (ID, VERSION, MATRICULA, TIPO_OPERACION_ID, IMPORTE, REFERENCIA, FOLIO_DOC, STATUS, FECHA, USER_ID) ");
                COMANDO.append(" VALUES ");
                COMANDO.append(" ((SELECT COALESCE(MAX(ID),0)+1 FROM MATEO.CONT_OPERACION_CAJA), 0, ?, ?, ?, NULL, ?, 'A', ?, ?) ");
                pstmt = conn.prepareStatement(COMANDO.toString());
                pstmt.setString(1, matricula);
                pstmt.setInt(2, tipoOperacion.getId());
                pstmt.setBigDecimal(3, importe);
                pstmt.setInt(4, folio);
                pstmt.setDate(5, new java.sql.Date(new java.util.Date().getTime()));
                pstmt.setLong(6, user);
                pstmt.execute();

            }catch (Exception e){
                throw new Exception("Error al intenter insertar las operaciones de caja "+e);
            }finally{
                pstmt.close();
            }
        }
    }
    
    public static void desactivaOperacion(Connection conn, Integer id, String idEjercicio, String idLibro, String idCCosto, String folio, String referencia) throws Exception{
        PreparedStatement pstmt = null;
        
        try{
            String COMANDO = "UPDATE MATEO.CONT_OPERACION_CAJA SET STATUS = 'I', ID_EJERCICIO = ?, ID_LIBRO = ?, "
                    + " ID_EJERCICIO2 = ?, ID_CCOSTO = ?, FOLIO = ?, REFERENCIA = ? "
                    + " WHERE ID = ? ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, idEjercicio);
            pstmt.setString(2, idLibro);
            pstmt.setString(3, idEjercicio);
            pstmt.setString(4, idCCosto);
            pstmt.setString(5, folio);
            pstmt.setString(6, referencia);
            pstmt.setInt(7, id);
            pstmt.executeUpdate();
            
        }catch (Exception e){
            throw new Exception("Error al intenter actualizar la operacion de caja "+id+"--"+e);
        }finally{
            if(pstmt!=null)pstmt.close();
        }
    }
    
    public static void creaOperacionPorDiferencia(Connection conn, Integer id, String matricula, Integer tipoOperacionId, 
            BigDecimal importe, String referencia, Integer folioDoc, Long userId,
            String idEjercicio, String idLibro, String idCCosto, String folio) throws Exception {
        PreparedStatement pstmt = null;
        
        try{
            OperacionCajaCC.desactivaOperacion(conn, id, idEjercicio, idLibro, idCCosto, folio, referencia);
            
            StringBuilder COMANDO = new StringBuilder();
            COMANDO.append("INSERT INTO MATEO.CONT_OPERACION_CAJA  ");
            COMANDO.append(" (ID, VERSION, MATRICULA, TIPO_OPERACION_ID, IMPORTE, REFERENCIA, FOLIO_DOC, STATUS, FECHA, USER_ID) ");
            COMANDO.append(" VALUES ");
            COMANDO.append(" ((SELECT COALESCE(MAX(ID),0)+1 FROM MATEO.CONT_OPERACION_CAJA), 0, ?, ?, ?, ?, ?, 'A', ?, ?) ");
            pstmt = conn.prepareStatement(COMANDO.toString());
            pstmt.setString(1, matricula);
            pstmt.setInt(2, tipoOperacionId);
            pstmt.setBigDecimal(3, importe);
            pstmt.setString(4, null);
            pstmt.setInt(5, folioDoc);
            pstmt.setDate(6, new java.sql.Date(new java.util.Date().getTime()));
            pstmt.setLong(7, userId);
            pstmt.execute();
            
        }catch (Exception e){
            throw new Exception("Error al intenter crear nueva operacion de caja "+e);
        }finally{
            if(pstmt!=null)pstmt.close();
        }
    }
}
