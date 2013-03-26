/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model.ccobro;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;

/**
 *
 * @author osoto
 */
public class CobroClima {

    private Integer id;
    private String matricula;
    private String status;

    public Map getCobrosClima() throws Exception {
        Map <String, CobroClima> cobros = new TreeMap <String, CobroClima> ();
        CobroClima cClima = null;

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        Connection conn_noe = null;

        try {
            if ((conn_noe == null) || conn_noe.isClosed()) {
                conn_noe = new Conexion().getConexionNoe(new Boolean(false));
            }

            String COMANDO = "SELECT ID, MATRICULA, STATUS " +
                    "FROM NOE.FES_COBRO_CLIMA " +
                    "WHERE STATUS = 'A' ";
            pstmt = conn_noe.prepareStatement(COMANDO);
            rset = pstmt.executeQuery();
            
            while(rset.next()) {
                cClima = new CobroClima();
                cClima.id = rset.getInt("id");
                cClima.matricula = rset.getString("matricula");
                cClima.status = rset.getString("status");
                cobros.put(cClima.matricula, cClima);
            }
        } catch (Exception e) {
            throw new UMException("CobroClima: Error al obtener los alumnos <br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (!conn_noe.isClosed()) {
                conn_noe.close();
                conn_noe = null;
            }
        }

        return cobros;
    }

    public void changeStatus(Connection conn_noe, String matricula) throws Exception {
        PreparedStatement pstmt = null;
        
        try{
            String COMANDO = "UPDATE NOE.FES_COBRO_CLIMA " +
                    "SET STATUS = 'I' " +
                    "WHERE MATRICULA = ? ";
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.execute();
        }catch(Exception e) {
            throw new UMException("CobroClima: Error al intentar desactivar el cobro de clima del alumno "+matricula+"<br>"+e);
        }finally {
            pstmt.close();
        }
    }
}
