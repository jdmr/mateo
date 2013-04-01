/*
 * Created on Aug 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.dinscribir;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.Map;
import java.util.TreeMap;


/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Carga {
    private static Connection conn_enoc;
    private String cargaId;
    private String nombre;
    private String fInicio;
    private String fFinal;

    /**
     *
     */
    public Carga() {
        super();

        // TODO Auto-generated constructor stub
    }

	/**
	 * @param cargaId
	 */
	public Carga(String cargaId) {
		super();
		this.cargaId = cargaId;
	}
    /**
     * Se obtienen las cargas academicas que estan incluidas en el rango de fechas especificado
     *
     * @param fechaI
     * @param fechaF
     * @return Map
     * @throws Exception
     */
    public static Map getCargas(String hoy)
        throws Exception {
        Map mCargas = new TreeMap();

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
            	
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));

                String COMANDO = "SELECT CARGA_ID, NOMBRE_CARGA NOMBRE, TO_CHAR(F_INICIO,'DD/MM/YYYY') F_INICIO, TO_CHAR(F_FINAL,'DD/MM/YYYY') F_FINAL  " + 
				" FROM enoc.CARGA " +
				"WHERE  TO_DATE(?,'DD/MM/YY') BETWEEN F_INICIO AND F_FINAL ";
                pstmt = conn_enoc.prepareStatement(COMANDO);
                pstmt.setString(1, hoy);
                
                //System.out.println(COMANDO);
                rset = pstmt.executeQuery();
                
                while (rset.next()) {
                	Carga carga = new Carga(rset.getString("Carga_ID"));
                        carga.setNombre(rset.getString("Nombre"));
                        carga.setFInicio(rset.getString("F_Inicio"));
                        carga.setFFinal(rset.getString("F_Final"));
                	mCargas.put(carga.getCargaId(), carga);                	
                }
                
                pstmt.close();
                rset.close();
            }
        } catch (Exception e) {
            throw new Exception(
                "Error al obtener las cargas incluidas en la fecha " +
                hoy + " " + e+"<br>");
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }

            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }

        return mCargas;
    }
    
    public static Boolean isVerano(String cargaId) throws Exception {
    Boolean flag = new Boolean(false);
    PreparedStatement pstmt = null;
    ResultSet rset = null;

    try {
        if ((conn_enoc == null) || conn_enoc.isClosed()) {
        	
            conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            
            String COMANDO = "SELECT TIPOCARGA " + 
			" FROM enoc.CARGA " +
			"WHERE  CARGA_ID = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, cargaId);
            
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
            	flag = new Boolean(rset.getString("tipocarga").equals("V")?true:false);                	
            }
            
            pstmt.close();
            rset.close();
        }
    } catch (Exception e) {
        throw new Exception(
            "Error al verificar si la carga " + cargaId + " corresponde al bloque de verano"
            + " " + e+"<br>");
    } finally {
        if (pstmt != null) {
            pstmt.close();
            pstmt = null;
        }

        if (rset != null) {
            rset.close();
            rset = null;
        }

        if (!conn_enoc.isClosed()) {
            conn_enoc.close();
            conn_enoc = null;
        }
    }

    return flag;
}
    
	public String getCargaId() {
		return cargaId;
	}
	public void setCargaId(String cargaId) {
		this.cargaId = cargaId;
	}
	public String getFFinal() {
		return fFinal;
	}
	public void setFFinal(String final1) {
		fFinal = final1;
	}
	public String getFInicio() {
		return fInicio;
	}
	public void setFInicio(String inicio) {
		fInicio = inicio;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}
