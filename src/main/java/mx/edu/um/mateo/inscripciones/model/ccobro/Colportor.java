/*
 * Colportor.java
 *
 * Created on January 24, 2008, 11:52 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package mx.edu.um.mateo.inscripciones.model.ccobro;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Constants;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;

/**
 *
 * @author osoto
 */
public class Colportor
{
    private Integer id;
    private Alumno alumno;
    private Double importe;
    private Date fecha;
    
    private SimpleDateFormat sdf;
    private Connection conn_noe;
    
    /** Creates a new instance of Colportor */
    public Colportor ()
    {
        this.id = new Integer(0);
        this.setAlumno (getAlumno ());
        this.setImporte (new Double (0));
        this.setFecha (new Date ());
        this.sdf = new SimpleDateFormat (Constants.DATE_SHORT_HUMAN_PATTERN);
    }
    
    public Integer getId(){
        return this.id;
    }
    
    public void setId(Integer id){
        this.id = id;
    }
    
    public Alumno getAlumno ()
    {
        return alumno;
    }
    
    public void setAlumno (Alumno alumno)
    {
        this.alumno = alumno;
    }
    
    public Double getImporte ()
    {
        return importe;
    }
    
    public void setImporte (Double importe)
    {
        this.importe = importe;
    }
    
    public Date getFecha ()
    {
        return fecha;
    }
    
    public void setFecha (Date fecha)
    {
        this.fecha = fecha;
    }
    
    public Map getColportores () throws Exception
    {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        Map mColportores = new TreeMap ();
        
        try
        {
            if(conn_noe == null || conn_noe.isClosed ())
                conn_noe = new Conexion ().getConexionNoe (new Boolean (false));
            
            
            String COMANDO = "SELECT ID, ALUMNO_ID MATRICULA, IMPORTE, TO_CHAR(FECHA,'DD/MM/YYYY') FECHA " +
                    "FROM NOE.FES_ALUMNO_COLPORTOR " +
                    "WHERE STATUS = 'A' ";
            
            pstmt = conn_noe.prepareStatement (COMANDO);
            rset = pstmt.executeQuery ();
            
            while(rset.next ())
            {
                Colportor colportor = new Colportor ();
                colportor.setId (new Integer(rset.getInt ("Id")));
                colportor.setAlumno (new Alumno (rset.getString ("Matricula")));
                colportor.setImporte (new Double (rset.getDouble ("Importe")));
                colportor.setFecha (sdf.parse (rset.getString ("Fecha")));
                
                mColportores.put (colportor.getAlumno ().getMatricula (), colportor);
            }
            rset.close ();
            pstmt.close ();
        }
        catch(Exception e)
        {
            throw new Error ("Error al obtener los descuentos de materias <br>"+e);
        }
        finally
        {
            if(pstmt != null)
            {pstmt.close (); pstmt = null;}
            if(rset != null)
            {rset.close (); rset = null;}
            if(!conn_noe.isClosed ())
            {conn_noe.close (); conn_noe = null;}
        }
        
        return mColportores;
    }
    
    public static void desactivaDocumentosColportor (String matricula, String cargaId, Integer bloque, Connection conn_noe) throws Exception
    {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try
        {
            String COMANDO = "UPDATE NOE.ALUMNO_COLPORTOR_DOCUMENTO " +
                    "SET STATUS = ?, FECHA_MODIFICA = SYSDATE " +
                    "WHERE STATUS = 'C' " +
                    "and matricula in " +
                    "( " +
                        "select matricula " +
                        "from noe.temporada_colportor_relacion " +
                        "where matricula = ? " +
                        "and status = 'A' " +
                    ") ";
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setString(1, Constants.STATUS_INSCRITO);
            pstmt.setString(2, matricula);
            pstmt.execute();
            pstmt.close();
            
        }
        catch(Exception e)
        {
            throw new UMException ("Error al cambiar estatus de colportor "+matricula+"<br>"+e);
        }
        finally
        {
            if(pstmt != null)
            {pstmt.close (); pstmt = null;}
            if(rset != null)
            {rset.close (); rset = null;}
        }
                
    }
}

