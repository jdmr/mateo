/*
 * Created on 24/01/2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.afe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;
import java.util.TreeMap;

import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Constants;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Convenio implements Comparable <String>{

    private Integer id;
    private String matricula;
    private String puesto;
    private BigDecimal precioHora;
    private BigDecimal beca;
    private BigDecimal maximoHoras;
    private Date fecha;
    private String user;
    private String status;
    private Integer tipoConvenio;
    private String ejercicio;
    private String ccosto;
    private String carga;
    private Integer bloque;
    private BigDecimal totalEnsenanza;

    public Convenio() {

    }

    public Convenio(Integer id, String alumnoId, String puesto, BigDecimal precioHora, BigDecimal beca, BigDecimal maximoHoras,
            Date fecha, String userId, Integer tipoConvenio, String ejercicioId, String ccostoId,
            String cargaId, Integer bloqueId, BigDecimal totalEnsenanza) {
        this.id = id;
        this.matricula = alumnoId;
        this.puesto = puesto;
        this.precioHora = precioHora;
        this.maximoHoras = maximoHoras;
        this.beca = beca;
        this.fecha = fecha;
        this.user = userId;
        this.tipoConvenio = tipoConvenio;
        this.ejercicio = ejercicioId;
        this.ccosto = ccostoId;
        this.carga = cargaId;
        this.bloque = bloqueId;
        this.totalEnsenanza = totalEnsenanza;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public BigDecimal getPrecioHora() {
        return precioHora;
    }

    public void setPrecioHora(BigDecimal precioHora) {
        this.precioHora = precioHora;
    }

    public BigDecimal getBeca() {
        return beca;
    }

    public void setBeca(BigDecimal beca) {
        this.beca = beca;
    }

    public BigDecimal getMaximoHoras() {
        return maximoHoras;
    }

    public void setMaximoHoras(BigDecimal maximoHoras) {
        this.maximoHoras = maximoHoras;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getTipoConvenio() {
        return tipoConvenio;
    }

    public void setTipoConvenio(Integer tipoConvenio) {
        this.tipoConvenio = tipoConvenio;
    }

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getCcosto() {
        return ccosto;
    }

    public void setCcosto(String ccosto) {
        this.ccosto = ccosto;
    }

    public String getCarga() {
        return carga;
    }

    public void setCarga(String carga) {
        this.carga = carga;
    }

    public Integer getBloque() {
        return bloque;
    }

    public void setBloque(Integer bloque) {
        this.bloque = bloque;
    }

    public BigDecimal getTotalEnsenanza() {
        return totalEnsenanza;
    }

    public void setTotalEnsenanza(BigDecimal totalEnsenanza) {
        this.totalEnsenanza = totalEnsenanza;
    }

    public BigDecimal getTotalConvenio () {

        /*BigDecimal pagoHoras = this.maximoHoras.multiply(precioHora);
        BigDecimal diezmo = pagoHoras.movePointLeft(1);
        return (pagoHoras.add(this.beca)).subtract(diezmo);*/
        return this.beca;
    }

    /**
     *
     * @param obj
     * @throws Exception
     */
    public void desactivaConvenio(String matricula, String cargaId, Integer bloqueId, Connection conn_noe) throws Exception {
        PreparedStatement pstmt = null;

        try {
            String COMANDO = "UPDATE AFE_CONVENIO " +
                    "SET STATUS = ? " +
                    "WHERE MATRICULA = ? " +
                    "AND CARGA_ID = ? " +
                    "AND BLOQUE_ID = ? " +
                    "AND STATUS = ? ";
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setString(1, Constants.CONVENIO_STATUS_INACTIVO);
            pstmt.setString(2, matricula);
            pstmt.setString(3, cargaId);
            pstmt.setInt(4, bloqueId);
            pstmt.setString(5, Constants.CONVENIO_STATUS_ACTIVO);

            pstmt.execute();
            pstmt.close();

        } catch (Exception e) {
            throw new Exception("AFE-Convenio: Error al intentar desactivar convenio de alumno " + matricula + "  <br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }
    }

    /**
     *
     * @param obj
     * @throws Exception
     */
    public void eliminaConvenio(String matricula, String cargaId, Integer bloqueId, Connection conn_noe) throws Exception {
        PreparedStatement pstmt = null;

        try {
            String COMANDO = "UPDATE AFE_CONVENIO " +
                    "SET STATUS = ? " +
                    "WHERE MATRICULA = ? " +
                    "AND CARGA_ID = ? " +
                    "AND BLOQUE_ID = ? " +
                    "AND STATUS = ? ";
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setString(1, Constants.CONVENIO_STATUS_ELIMINADO);
            pstmt.setString(2, matricula);
            pstmt.setString(3, cargaId);
            pstmt.setInt(4, bloqueId);
            pstmt.setString(5, Constants.CONVENIO_STATUS_ACTIVO);

            pstmt.execute();
            pstmt.close();

        } catch (Exception e) {
            throw new Exception("AFE-Convenio: Error al intentar eliminar convenio de alumno " + matricula + "  <br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }
    }

    /**
     * Funcion utilizada por el calculo de cobro
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map getConvenios(Convenio obj) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Connection conn_noe = null;
        Map mConvenios = new TreeMap();

        Locale local = new Locale("es", "MX", "Traditional_WIN");
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_SHORT_HUMAN_PATTERN, local);

        try {
            if ((conn_noe == null) || conn_noe.isClosed()) {
                conn_noe = new Conexion().getConexionNoe(new Boolean(false));
            }

            //A la beca se le quita el 10%
            String COMANDO = "SELECT ID, MATRICULA, PUESTO, PRECIO_HORA, BECA, MAXIMO_HORAS, TO_CHAR(FECHA,'DD/MM/YY') FECHA, USER_ID, TIPO_CONVENIO_ID, EJERCICIO_ID, CCOSTO_ID, CARGA_ID, BLOQUE_ID, TOTAL_ENSENANZA " +
                    "FROM AFE_CONVENIO " +
                    "WHERE STATUS = ? ";

            if (obj == null) {
                //No se necesitan variables en el query
                pstmt = conn_noe.prepareStatement(COMANDO);
                pstmt.setString(1, Constants.CONVENIO_STATUS_ACTIVO);
            }

            rset = pstmt.executeQuery();

            while (rset.next()) {
                Convenio convenio = new Convenio(
                        new Integer(rset.getString("id")),
                        rset.getString("matricula"),
                        rset.getString("puesto"),
                        new BigDecimal(rset.getString("precio_hora")),
                        new BigDecimal(rset.getString("beca")),
                        new BigDecimal(rset.getString("maximo_horas")),
                        sdf.parse(rset.getString("fecha")),
                        rset.getString("user_id"),
                        new Integer(rset.getString("tipo_convenio_id")),
                        rset.getString("ejercicio_id"),
                        rset.getString("ccosto_id"),
                        rset.getString("carga_id"),
                        new Integer(rset.getString("bloque_id")),
                        new BigDecimal(rset.getString("total_ensenanza")));

                mConvenios.put(
                        convenio.getMatricula() + convenio.getCarga() + convenio.getBloque(),
                        convenio);
            }

            pstmt.close();
            rset.close();

        } catch (Exception e) {
            throw new Exception("AFE-Convenio: Error al obtener los convenios  <br>" + e);
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
        return mConvenios;
    }

    /**
     * Funcion utilizada por el calculo de cobro
     * @param obj
     * @return
     * @throws Exception
     */
    public static Map getConveniosByStatusInscrito() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Connection conn_noe = null;
        Map mConvenios = new TreeMap();

        Locale local = new Locale("es", "MX", "Traditional_WIN");
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_SHORT_HUMAN_PATTERN, local);

        try {
            if ((conn_noe == null) || conn_noe.isClosed()) {
                conn_noe = new Conexion().getConexionNoe(new Boolean(false));
            }

            //A la beca se le quita el 10%
            String COMANDO = "SELECT ID, MATRICULA, PUESTO, PRECIO_HORA, BECA, MAXIMO_HORAS, TO_CHAR(FECHA,'DD/MM/YY') FECHA, USER_ID, TIPO_CONVENIO_ID, EJERCICIO_ID, CCOSTO_ID, CARGA_ID, BLOQUE_ID, TOTAL_ENSENANZA " +
                    "FROM AFE_CONVENIO " +
                    "WHERE STATUS = ? ";

            //No se necesitan variables en el query
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setString(1, Constants.CONVENIO_STATUS_INACTIVO);

            rset = pstmt.executeQuery();

            while (rset.next()) {
                Convenio convenio = new Convenio(
                        new Integer(rset.getString("id")),
                        rset.getString("matricula"),
                        rset.getString("puesto"),
                        new BigDecimal(rset.getString("precio_hora")),
                        new BigDecimal(rset.getString("beca")),
                        new BigDecimal(rset.getString("maximo_horas")),
                        sdf.parse(rset.getString("fecha")),
                        rset.getString("user_id"),
                        new Integer(rset.getString("tipo_convenio_id")),
                        rset.getString("ejercicio_id"),
                        rset.getString("ccosto_id"),
                        rset.getString("carga_id"),
                        new Integer(rset.getString("bloque_id")),
                        new BigDecimal(rset.getString("total_ensenanza")));

                mConvenios.put(
                        convenio.getMatricula() + convenio.getCarga() + convenio.getBloque(),
                        convenio);
            }

            pstmt.close();
            rset.close();

        } catch (Exception e) {
            throw new Exception("AFE-Convenio: Error al obtener los convenios  <br>" + e);
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
        return mConvenios;
    }

    public int compareTo(String myClass) {

		return new CompareToBuilder()
			.append(this.matricula+this.carga+this.bloque.toString(),myClass)
			.toComparison();
	}

    /**
     *
     * @param obj
     * @throws Exception
     */
    public static void desactivaContrato(String matricula, String cargaId, Integer bloqueId, Connection conn_noe) throws Exception {
        PreparedStatement pstmt = null;

        try {
            String COMANDO = "UPDATE NOE.AFE_CONTRATO_ALUMNO " +
                    "SET STATUS = ?, CARGA_ID = ?, BLOQUE_ID = ?, FECHA_MODIFICA = SYSDATE " +
                    "WHERE MATRICULA = ? " +
                    "AND STATUS = ? ";
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setString(1, Constants.STATUS_INSCRITO);
            pstmt.setString(2, cargaId);
            pstmt.setInt(3, bloqueId);
            pstmt.setString(4, matricula);
            pstmt.setString(5, Constants.STATUS_ACTIVO);

            pstmt.execute();
            pstmt.close();

        } catch (Exception e) {
            throw new UMException("AFE-Contrato: Error al intentar desactivar el contrato de alumno " + matricula + "  <br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }
    }
}
