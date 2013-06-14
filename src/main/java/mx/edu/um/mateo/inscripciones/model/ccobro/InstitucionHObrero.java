/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model.ccobro;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * Lee los datos de AlumnoInstitucion
 * @author osoto
 */
public class InstitucionHObrero {
    private String matricula;
    private BigDecimal pctEnsenanza;
    private BigDecimal pctInternado;
    private BigDecimal pctMatricula;
    private BigDecimal pctGral;

    /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the pctEnsenanza
     */
    public BigDecimal getPctEnsenanza() {
        return pctEnsenanza;
    }

    /**
     * @param pctEnsenanza the pctEnsenanza to set
     */
    public void setPctEnsenanza(BigDecimal pctEnsenanza) {
        this.pctEnsenanza = pctEnsenanza;
    }

    /**
     * @return the pctInternado
     */
    public BigDecimal getPctInternado() {
        return pctInternado;
    }

    /**
     * @param pctInternado the pctInternado to set
     */
    public void setPctInternado(BigDecimal pctInternado) {
        this.pctInternado = pctInternado;
    }

    /**
     * @return the pctMatricula
     */
    public BigDecimal getPctMatricula() {
        return pctMatricula;
    }

    /**
     * @param pctMatricula the pctMatricula to set
     */
    public void setPctMatricula(BigDecimal pctMatricula) {
        this.pctMatricula = pctMatricula;
    }

    /**
     * @return the pctGral
     */
    public BigDecimal getPctGral() {
        return pctGral;
    }

    /**
     * @param pctGral the pctGral to set
     */
    public void setPctGral(BigDecimal pctGral) {
        this.pctGral = pctGral;
    }
    
    public static InstitucionHObrero getPorcentajeInstituciones(String matricula, Connection conn) throws SQLException {
        InstitucionHObrero inst = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        StringBuilder str = new StringBuilder("select importe_matricula, importe_ensenanza, importe_internado, porcentaje from noe.alumno_institucion ");
        str.append("where matricula = ? and status = 'A' ");
        try{
            pstmt = conn.prepareStatement(str.toString());
            pstmt.setString(1, matricula);
            rset = pstmt.executeQuery();
            
            if(rset.next()){
                inst = new InstitucionHObrero();
                inst.setMatricula(matricula);
                inst.setPctMatricula(rset.getBigDecimal("importe_matricula"));
                inst.setPctEnsenanza(rset.getBigDecimal("importe_ensenanza"));
                inst.setPctInternado(rset.getBigDecimal("importe_internado"));
                inst.setPctGral(rset.getBigDecimal("porcentaje"));
            }
            
            if(rset.wasNull()){
                throw new SQLException("El alumno "+matricula+" aunque es hijo de obrero, no tiene ningun porcentaje de ayuda registrado");
            }
        }catch(Exception e){
            throw new SQLException(e);
        }finally{
            pstmt = null;
            rset = null;
            
        }
        return inst;
    }
    public static void savePorcentajeInstituciones(String matricula, Connection conn) throws SQLException {
        PreparedStatement pstmt = null;
        
        StringBuilder str = new StringBuilder("update noe.alumno_institucion set status = 'I' where matricula = ? and status = 'A' ");
        try{
            pstmt = conn.prepareStatement(str.toString());
            pstmt.setString(1, matricula);
            pstmt.executeUpdate();
            pstmt.close();
        }catch(Exception e){
        }finally{
            pstmt = null;
            
        }
    }
}
