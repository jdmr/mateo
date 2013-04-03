/*
 * Created on Jun 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package mx.edu.um.mateo.inscripciones.model.ccobro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AutorizaComida {

    private String matricula;
    private String carga_id;
    private Integer bloque;
    private Integer comidas;
    private String tipoComida;
    private String cliente;
    private String paquete;
    private Date fechaInicio;
    private Date fechaFinal;
    private Connection conn_noe;

    /**
     * @param matricula
     * @param carga_id
     * @param bloque
     */
    public AutorizaComida(String matricula, String carga_id, Integer bloque, Date fechaInicial, Date fechaFinal) {
        this.matricula = matricula;
        this.carga_id = carga_id;
        this.bloque = bloque;
        this.comidas = new Integer(3);
        this.tipoComida = "111";
        this.cliente = "ALUMNO";
        this.paquete = "M";
        this.fechaInicio = fechaInicial;
        this.fechaFinal = fechaFinal;
    }

    /**
     * @param matricula
     * @param carga_id
     * @param bloque
     * @param comidas
     * @param tipoComida
     * @param cliente
     * @param paquete
     */
    public AutorizaComida(String matricula, String carga_id, Integer bloque,
            Integer comidas, String tipoComida, String cliente, String paquete,
            Date fechaInicio, Date fechaFinal) {
        super();
        this.matricula = matricula;
        this.carga_id = carga_id;
        this.bloque = bloque;
        this.comidas = comidas;
        this.tipoComida = tipoComida;
        this.cliente = cliente;
        this.paquete = paquete;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }

    /**
     * 
     */
    public AutorizaComida() {
        super();
        // TODO Auto-generated constructor stub
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
     * @return Returns the cliente.
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * @param cliente The cliente to set.
     */
    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    /**
     * @return Returns the comidas.
     */
    public Integer getComidas() {
        return comidas;
    }

    /**
     * @param comidas The comidas to set.
     */
    public void setComidas(Integer comidas) {
        this.comidas = comidas;
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
     * @return Returns the paquete.
     */
    public String getPaquete() {
        return paquete;
    }

    /**
     * @param paquete The paquete to set.
     */
    public void setPaquete(String paquete) {
        this.paquete = paquete;
    }

    /**
     * @return Returns the tipoComida.
     */
    public String getTipoComida() {
        return tipoComida;
    }

    /**
     * @param tipoComida The tipoComida to set.
     */
    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
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

    public static void limpiaTabla(Connection conn_noe, String matricula, String carga_id, Integer bloque) throws Exception {
        PreparedStatement pstmt = null;

        try {
            String COMANDO = "DELETE "
                    + "FROM NOE.COM_AUTORIZACION "
                    + "WHERE MATRICULA = ? "
                    + "AND CARGA_ID = ? "
                    + "AND COALESCE(INSCRITO,'N') = 'N' "
                    + "AND USUARIO = 'CCOBRO' ";

            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setString(2, carga_id);
            pstmt.execute();
            pstmt.close();
        } catch (Exception e) {
            throw new UMException("Error al inicializar los datos del comedor del alumno " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }
    }

    /*Grabar movimientos del alumno en la base de datos*/
    public static void grabaTabla(Connection conn_noe, Map mComedor, String matricula, String carga_id) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        //System.out.println("Comedor - GrabaTablaFechas ");
        try {
            Iterator iComedor = mComedor.keySet().iterator();
            while (iComedor.hasNext()) {
                AutorizaComida comedor = (AutorizaComida) mComedor.get((String) iComedor.next());

                if (comedor.getMatricula().equals(matricula) && comedor.getCarga_id().equals(carga_id)) {
                    int intNReg = 0;
                    //System.out.println("GrabaTabla " + comedor.getMatricula() + ", " + comedor.getCarga_id() + ", " + comedor.getBloque());
                    String COMANDO = "SELECT COUNT(*) NREG ";
                    COMANDO += "FROM noe.COM_AUTORIZACION ";
                    COMANDO += "WHERE MATRICULA = ? ";
                    COMANDO += "AND CARGA_ID = ? ";
                    COMANDO += "AND BLOQUE = ?";
                    pstmt = conn_noe.prepareStatement(COMANDO);
                    pstmt.setString(1, matricula);
                    pstmt.setString(2, carga_id);
                    pstmt.setInt(3, comedor.getBloque().intValue());
                    rset = pstmt.executeQuery();

                    if (rset.next()) {
                        intNReg = rset.getInt("NReg");
                    }
                    pstmt.close();
                    rset.close();

                    if (intNReg == 0) {
                        COMANDO = "INSERT INTO noe.COM_AUTORIZACION ";
                        COMANDO += "(MATRICULA, CARGA_ID, BLOQUE, NUM_COMIDAS, TIPO_COMIDA, FECHA_INICIAL, FECHA_FINAL, USUARIO, CLIENTE, PAQUETE, INSCRITO) ";
                        COMANDO += "VALUES ";
                        COMANDO += "(?, ?, ?, 3, '111', SYSDATE, SYSDATE, ?, 'ALUMNO', 'M', 'N') ";
                        pstmt = conn_noe.prepareStatement(COMANDO);
                        pstmt.setString(1, comedor.getMatricula());
                        pstmt.setString(2, comedor.getCarga_id());
                        pstmt.setInt(3, comedor.getBloque().intValue());
                        pstmt.setString(4, "CCOBRO");
                        pstmt.execute();
                        pstmt.close();
                    }
                }
            }
        } catch (Exception e) {
            throw new UMException("Error al insertar autorizacion del comedor para el alumno " + matricula + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
        }
    }

    /*Grabar movimientos del alumno en la base de datos*/
    public static void grabaTabla(Connection conn_noe, List<AutorizaComida> comidas, String matricula, String carga_id) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        //System.out.println("GrabaTabla 1" + comidas);
        if (comidas == null) {
            comidas = new ArrayList();
        }

        try {
            Locale local = new java.util.Locale("es", "MX", "Traditional_WIN");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", local);
            //System.out.println("GrabaTabla 2" + comidas);

            for (AutorizaComida comida : comidas) {
                //System.out.println("GrabaTabla 3" + comida);

                int intNReg = 0;

                String COMANDO = "SELECT COUNT(*) NREG ";
                COMANDO += "FROM NOE.COM_AUTORIZACION ";
                COMANDO += "WHERE MATRICULA = ? ";
                COMANDO += "AND CARGA_ID = ? ";
                COMANDO += "AND INSCRITO ='N' ";
                COMANDO += "AND USUARIO = 'CCOBRO' ";
                pstmt = conn_noe.prepareStatement(COMANDO);
                pstmt.setString(1, matricula);
                pstmt.setString(2, carga_id);

                rset = pstmt.executeQuery();

                if (rset.next()) {
                    intNReg = rset.getInt("NReg");
                }
                pstmt.close();
                rset.close();

                //System.out.println("GrabaTabla 4" + intNReg);

                if (intNReg == 0) {
                    COMANDO = "INSERT INTO NOE.COM_AUTORIZACION ";
                    COMANDO += "(MATRICULA, CARGA_ID, BLOQUE, NUM_COMIDAS, TIPO_COMIDA, FECHA_INICIAL, FECHA_FINAL, USUARIO, CLIENTE, PAQUETE) ";
                    COMANDO += "VALUES ";
                    COMANDO += "(?, ?, ?, ?, ?, TO_DATE(?,'DD/MM/YY'), TO_DATE(?,'DD/MM/YY'), ?, 'ALUMNO', 'M') ";
                    pstmt = conn_noe.prepareStatement(COMANDO);
                    pstmt.setString(1, comida.getMatricula());
                    pstmt.setString(2, comida.getCarga_id());
                    pstmt.setInt(3, comida.getBloque().intValue());
                    pstmt.setInt(4, comida.getComidas());
                    pstmt.setString(5, comida.getTipoComida());
                    pstmt.setString(6, sdf.format(comida.getFechaInicio()));
                    pstmt.setString(7, sdf.format(comida.getFechaFinal()));
                    pstmt.setString(8, "CCOBRO");
                    pstmt.execute();
                    pstmt.close();
                }

            }
        } catch (Exception e) {
            throw new UMException("Error al insertar autorizacion del comedor para el alumno " + matricula + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
        }
    }

    /*Obtiene comidas de una alumno y las regresa en un map*/
    public Map<String, List> getComidas(String carga_id) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        Map<String, List> comidas = new TreeMap<String, List>();
        List<AutorizaComida> lista = null;

        try {
            if (conn_noe == null || conn_noe.isClosed()) {
                conn_noe = new Conexion().getConexionNoe(new Boolean(false));
            }

            String COMANDO = "SELECT MATRICULA, CARGA_ID, BLOQUE, "
                    + "NUM_COMIDAS, CLIENTE, TIPO_COMIDA, PAQUETE, FECHA_INICIAL, FECHA_FINAL "
                    + "FROM noe.COM_AUTORIZACION "
                    + "WHERE CARGA_ID = ? "
                    + "AND INSCRITO IS NULL ";
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setString(1, carga_id);

            rset = pstmt.executeQuery();

            while (rset.next()) {
                String matricula = rset.getString("matricula");
                if (rset.wasNull()) {
                    throw new UMException("Error al obtener las autorizaciones de comidas");
                }

                String cargaId = rset.getString("carga_id");
                if (rset.wasNull()) {
                    throw new UMException("Error al obtener las autorizaciones de comidas del alumno " + matricula + " no tiene una carga valida");
                }

                Integer bloque = new Integer(rset.getInt("bloque"));
                if (rset.wasNull()) {
                    throw new UMException("Error al obtener las autorizaciones de comidas del alumno " + matricula + " no tiene un bloque valido");
                }

                Calendar fechaI = new GregorianCalendar();
                fechaI.setTimeInMillis(rset.getDate("fecha_inicial").getTime());

                Calendar fechaF = new GregorianCalendar();
                fechaF.setTimeInMillis(rset.getDate("fecha_final").getTime());

                AutorizaComida autorizaComida = new AutorizaComida(matricula, cargaId,
                        bloque, new Integer(rset.getString("Num_Comidas")), rset.getString("Tipo_Comida"),
                        rset.getString("Cliente"), rset.getString("Paquete"), fechaI.getTime(), fechaF.getTime());

                ////System.out.println(matricula + "@" + cargaId + "@" + bloque);
                /*
                if(autorizaComida.getComidas().compareTo(new Integer(0)) == 0)
                throw new UMException("El numero de comidas asignado al alumno "+matricula+" es de cero");
                
                if(autorizaComida.getTipoComida() == null)
                throw new UMException("El alumno "+matricula+" no tiene comidas asignadas");
                
                if(autorizaComida.getTipoComida().equals("111") && autorizaComida.getComidas().compareTo(new Integer(3)) != 0)
                throw new UMException("El numero de comidas no coincide con el tipo de comidas asignadas al alumno "+matricula);
                else if((autorizaComida.getTipoComida().equals("100") || autorizaComida.getTipoComida().equals("010") || autorizaComida.getTipoComida().equals("001"))
                && autorizaComida.getComidas().compareTo(new Integer(1)) != 0)
                throw new UMException("El numero de comidas no coincide con el tipo de comidas asignadas al alumno "+matricula);
                else if(autorizaComida.getComidas().compareTo(new Integer(2)) == 0)
                throw new UMException("El numero de comidas no coincide con el tipo de comidas asignadas al alumno "+matricula);
                 */

                if (!comidas.containsKey(matricula)) {
                    lista = new ArrayList<AutorizaComida>();
                    comidas.put(matricula, lista);
                } else {
                    lista = comidas.get(matricula);
                }

                lista.add(autorizaComida);
            }
        } catch (Exception e) {
            throw new UMException("Error al obtener las autorizaciones de comidas " + "<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
            if (!conn_noe.isClosed()) {
                conn_noe.close();
                conn_noe = null;
            }
        }
        return comidas;
    }

    /*Indica que el alumno se inscribio*/
    public static void setInscrito(Connection conn_noe, List<AutorizaComida> comidas, String matricula, String carga_id, Date fechaI, Date fechaF) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        //System.out.println("Comedor - setInscrito " + fechaI + ", " + fechaF);

        if (comidas == null) {
            comidas = new ArrayList();
        }

        try {
            Locale local = new java.util.Locale("es", "MX", "Traditional_WIN");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", local);

            for (AutorizaComida comida : comidas) {

                int intNReg = 0;
                //System.out.println("setInscrito " + comida.getMatricula() + ", " + comida.getCarga_id() + ", " + comida.getBloque());
                String COMANDO = "SELECT COUNT(*) NREG ";
                COMANDO += "FROM NOE.COM_AUTORIZACION ";
                COMANDO += "WHERE MATRICULA = ? ";
                COMANDO += "AND CARGA_ID = ? ";
                COMANDO += "AND TO_DATE(FECHA_INICIAL,'DD/MM/YY') = TO_DATE(?,'DD/MM/YY') ";
                COMANDO += "AND TO_DATE(FECHA_FINAL,'DD/MM/YY') = TO_DATE(?,'DD/MM/YY') ";
                pstmt = conn_noe.prepareStatement(COMANDO);
                pstmt.setString(1, matricula);
                pstmt.setString(2, carga_id);
                pstmt.setString(3, sdf.format(comida.getFechaInicio()));
                pstmt.setString(4, sdf.format(comida.getFechaFinal()));
                rset = pstmt.executeQuery();

                if (rset.next()) {
                    intNReg = rset.getInt("NReg");
                }
                pstmt.close();
                rset.close();

                //System.out.println("setInscrito " + intNReg);

                if (intNReg > 0) {
                    COMANDO = "UPDATE NOE.COM_AUTORIZACION ";
                    COMANDO += "SET INSCRITO = 'S' ";
                    COMANDO += "WHERE MATRICULA = ? ";
                    COMANDO += "AND CARGA_ID = ? ";
                    COMANDO += "AND BLOQUE = ?";
                    pstmt = conn_noe.prepareStatement(COMANDO);
                    pstmt.setString(1, matricula);
                    pstmt.setString(2, carga_id);
                    pstmt.setInt(3, comida.getBloque().intValue());
                    pstmt.execute();
                    pstmt.close();
                }

            }
        } catch (Exception e) {
            throw new UMException("Error al insertar autorizacion del comedor para el alumno " + matricula + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            if (rset != null) {
                rset.close();
                rset = null;
            }
        }
    }

    public String toString() {
        return new ToStringBuilder(this).append("matricula", this.matricula).append("carga_id", this.carga_id).append("bloque", this.bloque).append("comidas", this.comidas).append("tipoComida", this.tipoComida).append("paquete", this.paquete).append("fechaInicio", this.fechaInicio).append("fechaFinal", this.fechaFinal).toString();
    }
}
