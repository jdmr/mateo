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

import mx.edu.um.mateo.inscripciones.model.ccobro.Alumno;
import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PaqueteAlumno {
    private Integer id;
    private Alumno alumno;
    private Paquete paquete;
    private String status;
    
    
    /**
     *
     */
    public PaqueteAlumno() {
        this.alumno = new Alumno();
        this.paquete = new Paquete();
        this.status = "A";
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
     * @return Returns the alumno.
     */
    public Alumno getAlumno() {
        return alumno;
    }
    /**
     * @param alumno The alumno to set.
     */
    public void setAlumno(Alumno alumno) {
        this.alumno = alumno;
    }
    /**
     * @return Returns the paquete.
     */
    public Paquete getPaquete() {
        return paquete;
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
     * @param paquete The paquete to set.
     */
    public void setPaquete(Paquete paquete) {
        this.paquete = paquete;
    }
    public void setPaqueteAlumno(PaqueteAlumno paquete) throws Exception{
        if(paquete.getId() != null){
            this.updatePaqueteAlumno(paquete);
        } else{
            this.createPaqueteAlumno(paquete);
        }
    }
    public void createPaqueteAlumno(PaqueteAlumno paquete) throws Exception{
        Conexion conx = null;
        Connection conn_noe = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Integer id = null;
        
        try{
            //Validar que la matricula es correcta
            paquete.getAlumno().getAlumno();
            
            conx = new Conexion();
            conn_noe = conx.getConexionNoe(new Boolean(false));
            
            String COMANDO = "SELECT COALESCE(MAX(ID),0)+1 ID " +
                    "FROM noe.FES_PAQUETEALUMNO " ;
            pstmt = conn_noe.prepareStatement(COMANDO);
            rset = pstmt.executeQuery();
            if(rset.next()){
                id = new Integer(rset.getInt("id"));
            }
            pstmt.close();
            rset.close();
            
            COMANDO = "INSERT INTO noe.FES_PAQUETEALUMNO " +
                    "(ID, MATRICULA, PAQUETE_ID, STATUS)" +
                    "VALUES " +
                    "(?,?,?,?)";
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setInt(1, id.intValue());
            pstmt.setString(2, paquete.getAlumno().getMatricula());
            pstmt.setInt(3, paquete.getPaquete().getId().intValue());
            pstmt.setString(4, paquete.getStatus());
            pstmt.execute();
            pstmt.close();
        }catch(Exception e){
            throw new Exception("Error al intentar relacionar al alumno con un paquete "+e);
        }finally{
            if(rset != null){rset.close(); rset = null;}
            if(pstmt != null){pstmt.close(); pstmt = null;}
            if(!conn_noe.isClosed()){conn_noe.close(); conn_noe = null;}
        }
    }
    
    public void updatePaqueteAlumno(PaqueteAlumno paquete) throws Exception{
        Conexion conx = null;
        Connection conn_noe = null;
        PreparedStatement pstmt = null;
        try{
            conx = new Conexion();
            conn_noe = conx.getConexionNoe(new Boolean(false));
            
            String COMANDO = "UPDATE noe.FES_PAQUETEALUMNO " +
                    "SET MATRICULA = ?, PAQUETE_ID = ?, " +
                    "STATUS = ? " +
                    "WHERE ID = ? ";
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setString(1, paquete.getAlumno().getMatricula());
            pstmt.setInt(2, paquete.getPaquete().getId().intValue());
            pstmt.setString(3, paquete.getStatus());
            pstmt.setInt(4, paquete.getId().intValue());
            pstmt.execute();
            pstmt.close();
        }catch(Exception e){
            throw new Exception("Error al intentar actualizar el paquete "+e);
        }finally{
            if(pstmt != null){pstmt.close(); pstmt = null;}
            if(!conn_noe.isClosed()){conn_noe.close(); conn_noe = null;}
        }
    }
    
    public PaqueteAlumno getPaqueteAlumno(Integer id) throws Exception{
        Conexion conx = null;
        Connection conn_noe = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        PaqueteAlumno paquete = null;
        
        try{
            conx = new Conexion();
            conn_noe = conx.getConexionNoe(new Boolean(false));
            
            String COMANDO = "SELECT ID, MATRICULA, PAQUETE_ID, STATUS " +
                    "FROM noe.FES_PAQUETEALUMNO " +
                    "WHERE ID = ?" ;
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setInt(1, id.intValue());
            rset = pstmt.executeQuery();
            if(rset.next()){
                paquete = new PaqueteAlumno();
                paquete.setId(new Integer(rset.getInt("id")));
                paquete.setAlumno(new Alumno(rset.getString("matricula")));
                paquete.setPaquete(new Paquete(new Integer(rset.getInt("id"))));
                paquete.setStatus(rset.getString("Status"));
            }
            pstmt.close();
            rset.close();
            
        }catch(Exception e){
            throw new Exception("Error al intentar obtener un nuevo paquete "+e);
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
            
            String COMANDO = "SELECT ID, MATRICULA, PAQUETE_ID, STATUS " +
                    "FROM noe.FES_PAQUETEALUMNO " +
                    "WHERE STATUS = 'A' " ;
            
            pstmt = conn_noe.prepareStatement(COMANDO);
            rset = pstmt.executeQuery();
            while(rset.next()){
                PaqueteAlumno paquete = new PaqueteAlumno();
                paquete.setId(new Integer(rset.getInt("id")));
                paquete.setAlumno(new Alumno(rset.getString("matricula")));
                paquete.setPaquete(new Paquete(new Integer(rset.getInt("paquete_id"))));
                paquete.setStatus(rset.getString("Status"));
                try{
                    paquete.getAlumno().getAlumno();
                    paquete.setPaquete(new Paquete().getPaquete(new Integer(rset.getInt("paquete_id"))));
                    
                    mPaquetes.put(paquete.getAlumno().getMatricula(), paquete);
                }catch(Exception e){
                    System.err.print("PaqueteAlumno.getPaquetes() - El alumno "+paquete.getAlumno()+" marca el error "+e);
                }
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
    public void desactivaPaquete(String matricula, Connection conn_noe) throws Exception{
        PreparedStatement pstmt = null;
        try{
            String COMANDO = "UPDATE NOE.FES_PAQUETEALUMNO " +
                    "SET STATUS = 'I' " +
                    "WHERE MATRICULA = ? ";
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.execute();
            pstmt.close();
        }catch(Exception e){
            throw new Exception("Error al intentar desactivar el paquete del alumno "+e);
        }finally{
            if(pstmt!=null){pstmt.close(); pstmt = null;}
        }
    }
    public void borrarPaquete(Integer id) throws Exception{
        Conexion conx = null;
        Connection conn_noe = null;
        PreparedStatement pstmt = null;
        
        try{
            conx = new Conexion();
            conn_noe = conx.getConexionNoe(new Boolean(false));
            String COMANDO = "DELETE FROM noe.FES_PAQUETEALUMNO WHERE ID = ? ";
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setInt(1, id.intValue());
            pstmt.execute();
            pstmt.close();
        }catch(Exception e){
            throw new Exception("Error al intentar borrar el paquete del alumno "+e);
        }finally{
            if(pstmt != null){pstmt.close(); pstmt = null;}
            if(!conn_noe.isClosed()){conn_noe.close(); conn_noe = null;}
        }
    }
}
