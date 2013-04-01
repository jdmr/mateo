/*
 * Created on Jun 9, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.estudiantes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import mx.edu.um.mateo.inscripciones.model.ccobro.cuenta.CtaMayor;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Aviso {

    private Connection conn;
    private Connection conn_enoc;

    private String getEntidadValida(
            String strIDEjercicio, String strIDCCosto, Connection conn) throws SQLException, Exception {
        //Obtiene el nivel contable del ejercicio actual,
        //y obtiene la entidad del CCosto
        Integer intNContable = null;
        String COMANDO = "SELECT NIVEL_CONTABLE ";
        COMANDO += "FROM MATEO.CONT_EJERCICIO ";
        COMANDO += "WHERE ID_EJERCICIO = ? ";
        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
        pstmt.setString(1, strIDEjercicio);
        ResultSet rset = pstmt.executeQuery();
        if (rset.next()) {
            intNContable = new Integer(rset.getInt("Nivel_Contable"));
        }

        if (rset.wasNull()) {
            throw new Error("Ejercicio contable invalido");
        }
        pstmt.close();
        rset.close();

        String strEntidad = "";

        try {
            StringTokenizer strTkn = new StringTokenizer(strIDCCosto, ".");
            int intCont = 1;

            while (strTkn.hasMoreTokens()
                    && intCont <= intNContable.intValue()) {
                strEntidad += strTkn.nextToken() + ".";
                intCont += 1;
            }
            strEntidad = strEntidad.substring(0, strEntidad.length() - 1);
        } catch (NoSuchElementException e) {
            throw new Error("Contabilidad invalida! <br> No cumple con el nivel contable");
        }

        return strEntidad;
    }

    private String getEntidadValida(
            String strIDEjercicio, String strIDCCosto) throws SQLException, Exception {
        //Obtiene el nivel contable del ejercicio actual,
        //y obtiene la entidad del CCosto
        Integer intNContable = null;
        String COMANDO = "SELECT NIVEL_CONTABLE ";
        COMANDO += "FROM mateo.CONT_EJERCICIO ";
        COMANDO += "WHERE ID_EJERCICIO = ? ";
        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
        pstmt.setString(1, strIDEjercicio);
        ResultSet rset = pstmt.executeQuery();
        if (rset.next()) {
            intNContable = new Integer(rset.getInt("Nivel_Contable"));
        }

        if (rset.wasNull()) {
            throw new Error("Ejercicio contable invalido");
        }
        pstmt.close();
        rset.close();

        String strEntidad = "";

        try {
            StringTokenizer strTkn = new StringTokenizer(strIDCCosto, ".");
            int intCont = 1;

            while (strTkn.hasMoreTokens()
                    && intCont <= intNContable.intValue()) {
                strEntidad += strTkn.nextToken() + ".";
                intCont += 1;
            }
            strEntidad = strEntidad.substring(0, strEntidad.length() - 1);
        } catch (NoSuchElementException e) {
            throw new Error("Contabilidad invalida! <br> No cumple con el nivel contable");
        }

        return strEntidad;
    }

    //Obtener nombre de centro de costo
    private String getNombreCCosto(
            String strIDEjercicio, String strIDCCosto) throws SQLException, Exception {
        String strNombre = null;
        String COMANDO = "SELECT NOMBRE ";
        COMANDO += "FROM mateo.CONT_CCOSTO ";
        COMANDO += "WHERE ID_EJERCICIO = ? ";
        COMANDO += "AND ID_CCOSTO = ? ";
        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
        pstmt.setString(1, strIDEjercicio);
        pstmt.setString(2, strIDCCosto);
        ResultSet rset = pstmt.executeQuery();

        if (rset.next()) {
            strNombre = rset.getString("Nombre");
        }
        rset.close();
        pstmt.close();

        if (strNombre == null) {
            throw new Error("El centro de costo no existe");
        }

        return strNombre;
    }

    //Obtener nombre de cuenta de mayor
    private String getNombreCtaMayor(
            String strIDEjercicio, String strIDCtaMayor) throws SQLException, Exception {
        String strNombre = null;
        String COMANDO = "SELECT NOMBRE ";
        COMANDO += "FROM mateo.CONT_CTAMAYOR ";
        COMANDO += "WHERE ID_EJERCICIO = ? ";
        COMANDO += "AND ID_CTAMAYOR = ? ";
        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
        pstmt.setString(1, strIDEjercicio);
        pstmt.setString(2, strIDCtaMayor);
        ResultSet rset = pstmt.executeQuery();

        if (rset.next()) {
            strNombre = rset.getString("Nombre");
        }
        rset.close();
        pstmt.close();

        if (strNombre == null) {
            throw new Error("La cuenta de mayor " + strIDCtaMayor + " no existe");
        }

        return strNombre;
    }

    //Obtener nombre de centro de auxiliar
    private String getNombreAuxiliar(
            String strIDEjercicio, String strIDAuxiliar) throws SQLException, Exception {
        String strNombre = null;
        String COMANDO = "SELECT NOMBRE ";
        COMANDO += "FROM mateo.CONT_AUXILIAR ";
        COMANDO += "WHERE ID_EJERCICIO = ? ";
        COMANDO += "AND ID_AUXILIAR = ? ";
        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
        pstmt.setString(1, strIDEjercicio);
        pstmt.setString(2, strIDAuxiliar);
        ResultSet rset = pstmt.executeQuery();

        if (rset.next()) {
            strNombre = rset.getString("Nombre");
        }
        rset.close();
        pstmt.close();

        if (strNombre == null) {
            throw new Error("La cuenta de auxiliar no existe");
        }

        return strNombre;
    }

    //Obtener nombre de cuenta
    private String getNombreCuenta(
            String strIDEjercicio, String strIDCtaMayor, String strIDCCosto,
            String strIDAuxiliar) throws SQLException, Exception {
        String strNombre = null;

        if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-")) {
            strIDAuxiliar = "0000000";
        }

        String COMANDO = "SELECT NOMBRE ";
        COMANDO += "FROM mateo.CONT_RELACION ";
        COMANDO += "WHERE ID_EJERCICIO = ? ";
        COMANDO += "AND ID_CTAMAYOR = ? ";
        COMANDO += "AND ID_CCOSTO = ? ";
        COMANDO += "AND ID_AUXILIAR = ? ";
        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
        pstmt.setString(1, strIDEjercicio);
        pstmt.setString(2, strIDCtaMayor);
        pstmt.setString(3, strIDCCosto);
        pstmt.setString(4, strIDAuxiliar);
        ResultSet rset = pstmt.executeQuery();

        if (rset.next()) {
            strNombre = rset.getString("Nombre");
        }
        rset.close();
        pstmt.close();

        if (strNombre == null) {
            throw new Error("La cuenta " + strIDCtaMayor + "-" + strIDCCosto + "-" + strIDAuxiliar + " no existe");
        }

        return strNombre;
    }

    //Obtener nombre del ejercicio contable
    private String getNombreEjercicio(
            String strIDEjercicio) throws SQLException, Exception {
        String strNombre = null;

        String COMANDO = "SELECT NOMBRE ";
        COMANDO += "FROM mateo.CONT_EJERCICIO ";
        COMANDO += "WHERE STATUS = 'A' ";
        COMANDO += "AND ID_EJERCICIO = ? ";
        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
        pstmt.setString(1, strIDEjercicio);
        ResultSet rset = pstmt.executeQuery();

        if (rset.next()) {
            strNombre = rset.getString("Nombre");
        }
        rset.close();
        pstmt.close();

        if (strNombre == null) {
            throw new Error("El ejercicio no existe " + strIDEjercicio);
        }

        return strNombre;
    }


    /*Dar de alta el encabezado de un aviso*/
    public Integer mavi_AltaEncabezado(
            String strFecha, String strDescripcion, String strTipoAviso, Integer intPrioridad, String strLogin) throws Exception {
        PreparedStatement pstmt = null;
        Integer intFolio = null;

        try {


            intFolio = mavi_getNuevoId("Id_Aviso", "mateo.Cont_Aviso");
            
            //System.out.println("Aviso id "+intFolio);

            String COMANDO = "INSERT INTO mateo.CONT_AVISO ";
            COMANDO += "VALUES ";
            COMANDO += "(?, to_date(?, 'dd/mm/yy'), ?, ?, 'A', ?, ?)";

            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setInt(1, intFolio.intValue());
            pstmt.setString(2, strFecha);
            pstmt.setString(3, strDescripcion);
            pstmt.setString(4, strLogin);
            pstmt.setString(5, strTipoAviso);
            pstmt.setInt(6, intPrioridad.intValue());
            pstmt.execute();

        } catch (SQLException e) {
            throw new Error("mavi_AltaAviso - Imposible dar de alta aviso " + strDescripcion + " " + e);
        } finally {
            pstmt.close();
            pstmt = null;
        }
        return intFolio;
    }

    /*Dar de alta el encabezado de un aviso*/
    public Integer mavi_AltaEncabezado(
            String strFecha, String strDescripcion, String strTipoAviso, Integer intPrioridad, String strLogin, Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        Integer intFolio = null;

        try {


            intFolio = mavi_getNuevoId("Id_Aviso", "mateo.Cont_Aviso", conn);

            String COMANDO = "INSERT INTO MATEO.CONT_AVISO ";
            COMANDO += "VALUES ";
            COMANDO += "(?, to_date(?, 'dd/mm/yy'), ?, ?, 'A', ?, ?)";

            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setInt(1, intFolio.intValue());
            pstmt.setString(2, strFecha);
            pstmt.setString(3, strDescripcion);
            pstmt.setString(4, strLogin);
            pstmt.setString(5, strTipoAviso);
            pstmt.setInt(6, intPrioridad.intValue());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("mavi_AltaAviso - Imposible dar de alta aviso " + strDescripcion + " " + e);
        } finally {
            pstmt.close();
            pstmt = null;
        }
        return intFolio;
    }


    /*Dar de alta el encabezado de un aviso, sin pasar la session*/
    public Integer mavi_AltaEncabezado(
            String strFecha, String strDescripcion, String strLogin) throws Exception {
        PreparedStatement pstmt = null;

        Integer intFolio = null;

        try {

            intFolio = mavi_getNuevoId("Id_Aviso", "mateo.Cont_Aviso");

            String COMANDO = "INSERT INTO mateo.CONT_AVISO ";
            COMANDO += "VALUES ";
            COMANDO += "(?, to_date(?, 'dd/mm/yy'), ?, ?, 'A')";

            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setInt(1, intFolio.intValue());
            pstmt.setString(2, strFecha);
            pstmt.setString(3, strDescripcion);
            pstmt.setString(4, strLogin);
            pstmt.execute();

        } catch (SQLException e) {
            throw new Error("mavi_AltaAviso - Imposible dar de alta aviso " + strDescripcion + " " + e);
        } finally {
            pstmt.close();
            pstmt = null;
        }

        return intFolio;
    }


    /*Dar de alta el detalle de un aviso*/
    public void mavi_AltaDetalle(
            Integer intFolio, String strIDEjercicio, String strIDCCosto, String strIDCtaMayor, String strIDAuxiliar,
            String strNaturaleza, Boolean blnActivar) throws Exception {
        PreparedStatement pstmt = null;

        try {

            Integer intFolio_Det = mavi_getNuevoId("Id_Aviso_Det", "mateo.Cont_Aviso_Det", "Id_Aviso", intFolio);

            String COMANDO = "INSERT INTO mateo.CONT_AVISO_DET ";
            COMANDO += "VALUES ";
            COMANDO += "(?,?,?,?,?,?,?,?)";

            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setInt(1, intFolio.intValue());
            pstmt.setInt(2, intFolio_Det.intValue());
            pstmt.setString(3, strIDEjercicio);
            pstmt.setString(4, strIDCCosto);
            pstmt.setString(5, strIDCtaMayor);
            pstmt.setString(6, strIDAuxiliar);
            pstmt.setString(7, strNaturaleza);
            pstmt.setBoolean(8, blnActivar.booleanValue());
            pstmt.execute();

        } catch (SQLException e) {
            throw new Error("mavi_AltaAviso - Imposible dar de alta aviso " + e);
        } finally {
            pstmt.close();
            pstmt = null;
        }
    }

    /*Dar de alta el detalle de un aviso*/
    public void mavi_AltaDetalle(
            Integer intFolio, String strIDEjercicio, String strIDCCosto, String strIDCtaMayor, String strIDAuxiliar,
            String strNaturaleza, Boolean blnActivar, Connection conn) throws Exception {
        PreparedStatement pstmt = null;

        try {

            Integer intFolio_Det = mavi_getNuevoId("Id_Aviso_Det", "mateo.Cont_Aviso_Det", "Id_Aviso", intFolio, conn);

            String COMANDO = "INSERT INTO MATEO.CONT_AVISO_DET ";
            COMANDO += "VALUES ";
            COMANDO += "(?,?,?,?,?,?,?,?)";

            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setInt(1, intFolio.intValue());
            pstmt.setInt(2, intFolio_Det.intValue());
            pstmt.setString(3, strIDEjercicio);
            pstmt.setString(4, strIDCCosto);
            pstmt.setString(5, strIDCtaMayor);
            pstmt.setString(6, strIDAuxiliar);
            pstmt.setString(7, strNaturaleza);
            pstmt.setBoolean(8, blnActivar.booleanValue());
            pstmt.execute();

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("mavi_AltaAviso - Imposible dar de alta aviso " + e);
        } finally {
            pstmt.close();
            pstmt = null;
        }
    }


    /*Permite generar el aviso de cambio de plan de estudios en un alumno*/
    /*Es indispensable que se ejecute antes de que se grabe el nuevo plan de estudios*/
    /*y que el nuevo plan de estudios se pase como parametro*/
    public void mavi_SetCambioPlan(
            String strMatricula, String strPlanID, String strLogin) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        /*Obtener entidad contable actual del alumno*/
        String strCCostoID = null;
        String strCCostoIDPlanNuevo = null;


        try {
            /*Obtener ejercicio actual*/
            String strIDEjercicio = "";
            String COMANDO = "SELECT MAX(ID_EJERCICIO) ID_EJERCICIO ";
            COMANDO += "FROM mateo.CONT_EJERCICIO ";
            COMANDO += "WHERE STATUS = 'A' ";
            pstmt = conn.prepareStatement(COMANDO);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                strIDEjercicio = rset.getString("ID_Ejercicio");
            }
            rset.close();
            pstmt.close();

            /*Validar que la matricula contenga un valor valido*/
            if (strMatricula.length() != 7) {
                throw new Error("Matricula invalida!");
            }

            Integer intNReg = new Integer(0);

            COMANDO = "SELECT COUNT(*) NREG ";
            COMANDO += "FROM enoc.ALUM_PERSONAL ";
            COMANDO += "WHERE CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, strMatricula);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                intNReg = new Integer(rset.getInt("NReg"));
            }
            pstmt.close();
            rset.close();

            if (intNReg.compareTo(new Integer(0)) == 0) {
                throw new Error("Alumno no dado de alta en el sistema");
            }

            //Obtener nombre del alumno
            String strNombre = null;

            COMANDO = "SELECT NOMBRE_LEGAL NOMBRE ";
            COMANDO += "FROM enoc.ALUM_PERSONAL ";
            COMANDO += "WHERE CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, strMatricula);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                strNombre = rset.getString("Nombre").toUpperCase();
            }
            pstmt.close();
            rset.close();

            COMANDO = "SELECT COUNT(*) NREG ";
            COMANDO += "FROM mateo.CONT_AUXILIAR ";
            COMANDO += "WHERE ID_EJERCICIO = ? ";
            COMANDO += "AND ID_AUXILIAR = ? ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, strIDEjercicio);
            pstmt.setString(2, strMatricula);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                intNReg = new Integer(rset.getInt("NReg"));
            }
            pstmt.close();
            rset.close();

            /*Si el alumno no esta dado de alta en el catalogo de auxiliares,*/
            /*indica que es un alumno nuevo*/
            if (intNReg.compareTo(new Integer(0)) == 0) {
                /*Insertar auxiliar*/
                COMANDO = "INSERT INTO mateo.CONT_AUXILIAR ";
                COMANDO += "(ID_EJERCICIO, ID_AUXILIAR, NOMBRE, DETALLE) ";
                COMANDO += "VALUES ";
                COMANDO += "(?, ?, ?, 'S')";
                pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, strIDEjercicio);
                pstmt.setString(2, strMatricula);
                pstmt.setString(3, strNombre);
                pstmt.execute();
                pstmt.close();

                COMANDO = "SELECT C.CCOSTO_ID ";
                COMANDO += "FROM enoc.ALUM_PLAN AP, enoc.MAPA_PLAN P, enoc.CAT_CARRERA C ";
                COMANDO += "WHERE P.PLAN_ID = AP.PLAN_ID ";
                COMANDO += "AND C.CARRERA_ID = P.CARRERA_ID ";
                COMANDO += "AND AP.ESTADO = '1' ";
                COMANDO += "AND AP.CODIGO_PERSONAL = ? ";
                pstmt = conn_enoc.prepareStatement(COMANDO);
                pstmt.setString(1, strMatricula);
                rset = pstmt.executeQuery();

                if (rset.next()) {
                    strCCostoID = getEntidadValida(strIDEjercicio, rset.getString("ID_Nivel"));
                }
                pstmt.close();
                rset.close();

                if (strCCostoID == null) {
                    //Si el alumno no tiene plan de estudios activo
                    //agarrar el primer plan disponible
                    COMANDO = "SELECT C.CCOSTO_ID ";
                    COMANDO += "FROM enoc.ALUM_PLAN AP, enoc.MAPA_PLAN P, enoc.CAT_CARRERA C ";
                    COMANDO += "WHERE P.PLAN_ID = AP.PLAN_ID ";
                    COMANDO += "AND C.CARRERA_ID = P.CARRERA_ID ";
                    COMANDO += "AND AP.CODIGO_PERSONAL = ? ";
                    pstmt = conn_enoc.prepareStatement(COMANDO);
                    pstmt.setString(1, strMatricula);
                    rset = pstmt.executeQuery();

                    if (rset.next()) {
                        strCCostoID = getEntidadValida(strIDEjercicio, rset.getString("ID_Nivel"));
                    }
                    pstmt.close();
                    rset.close();

                    if (strCCostoID == null) {
                        throw new Error("Plan del alumno, no es valido!");
                    }
                }

                strNombre = getNombreCCosto(strIDEjercicio, strCCostoID) + " ESTUDIANTES " + strNombre;

                //Verificar si existe la cuenta contable, en la contabilidad actual del alumno
                //aun cuando est? desactivada

                COMANDO = "SELECT COUNT(*) NREG ";
                COMANDO += "FROM mateo.CONT_RELACION ";
                COMANDO += "WHERE ID_EJERCICIO = ? ";
                COMANDO += "AND ID_CTAMAYOR = '1.1.04.01' ";
                COMANDO += "AND ID_CCOSTO = ? ";
                COMANDO += "AND ID_AUXILIAR = ? ";
                pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, strIDEjercicio);
                pstmt.setString(2, strCCostoID);
                pstmt.setString(3, strMatricula);
                rset = pstmt.executeQuery();

                if (rset.next()) {
                    intNReg = new Integer(rset.getInt("NReg"));
                }
                pstmt.close();
                rset.close();
                
                
                if (intNReg.compareTo(new Integer(0)) == 0) {
                    COMANDO = "INSERT INTO mateo.CONT_RELACION ";
                    COMANDO += "(ID_EJERCICIO, ID_CTAMAYOR, ID_CCOSTO, ID_AUXILIAR, NOMBRE, STATUS, NATURALEZA, TIPO_CUENTA, ID_EJERCICIO2, ID_EJERCICIO3) ";
                    COMANDO += "VALUES ";
                    COMANDO += "(?, '1.1.04.01', ?, ?, ?, 'A', 'D',?,?,?) ";
                    pstmt = conn.prepareStatement(COMANDO);
                    pstmt.setString(1, strIDEjercicio);
                    pstmt.setString(2, strCCostoID);
                    pstmt.setString(3, strMatricula);
                    pstmt.setString(4, strNombre);
                    pstmt.setString(5, new CtaMayor().getTipoCuenta(strIDEjercicio, "1.1.04.01"));
                    pstmt.setString(6, strIDEjercicio);
                    pstmt.setString(7, strIDEjercicio);
                    pstmt.execute();
                    pstmt.close();
                }
            } else /*No es un alumno nuevo, por lo que verificamos si el cambio de plan afecta a mas de una contabilidad
             * y si es asi se genera un aviso*/ {
                /*Obtener la fecha actual*/
                java.util.Date dtHoy = new java.util.Date();
                java.text.SimpleDateFormat sdFormat = new java.text.SimpleDateFormat("dd-MM-yyyy");
                String strFecha = sdFormat.format(dtHoy);

                /*Obtener contabilidad del nuevo plan de estudios*/
                COMANDO = "SELECT C.CCOSTO_ID ";
                COMANDO += "FROM enoc.ALUM_PLAN AP, enoc.MAPA_PLAN P, enoc.CAT_CARRERA C ";
                COMANDO += "WHERE P.PLAN_ID = AP.PLAN_ID ";
                COMANDO += "AND C.CARRERA_ID = P.CARRERA_ID ";
                COMANDO += "AND AP.PLAN_ID = ? ";
                COMANDO += "AND AP.CODIGO_PERSONAL = ? ";
                pstmt = conn_enoc.prepareStatement(COMANDO);
                pstmt.setString(1, strPlanID);
                pstmt.setString(2, strMatricula);
                rset = pstmt.executeQuery();

                if (rset.next()) {
                    strCCostoIDPlanNuevo = getEntidadValida(strIDEjercicio, rset.getString("ID_Nivel"));
                }
                pstmt.close();
                rset.close();


                /*Verificar si hubo un cambio de contabilidad*/
                if (!strCCostoID.equals(strCCostoIDPlanNuevo)) {
                    /*Graba encabezado*/
                    Integer intFolio = mavi_AltaEncabezado(strFecha, "Cambio de Plan de Estudios", "CPL", new Integer(3), strLogin);

                    /*Grabar detalles*/
                    String strIDCtaMayor = "1.1.04.01";

                    mavi_AltaDetalle(intFolio, strIDEjercicio, strCCostoID, strIDCtaMayor, strMatricula, "D", new Boolean(false));
                    mavi_AltaDetalle(intFolio, strIDEjercicio, strCCostoIDPlanNuevo, strIDCtaMayor, strMatricula, "C", new Boolean(true));

                }
            }

        } catch (Exception e) {
            throw new Error("Imposible grabar aviso de cambio de plan de estudios de " + strMatricula + " " + e);
        } finally {
            pstmt.close();
            pstmt = null;
            rset.close();
            rset = null;
        }
    }


    /*Permite generar el aviso de activacion del estado de un alumno*/
    /*Es indispensable que se ejecute antes de que se grabe la actualizacion*/
    public void mavi_SetActivaAlumno(
            String strMatricula, String strLogin, Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            /*Obtener ejercicio actual*/
            String strIDEjercicio = "";
            String COMANDO = "SELECT MAX(ID_EJERCICIO) ID_EJERCICIO ";
            COMANDO += "FROM MATEO.CONT_EJERCICIO ";
            COMANDO += "WHERE STATUS = 'A' ";
            pstmt = conn.prepareStatement(COMANDO);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                strIDEjercicio = rset.getString("ID_Ejercicio");
            }
            rset.close();
            pstmt.close();

            /*Obtener la fecha actual*/
            java.util.Date dtHoy = new java.util.Date();
            java.text.SimpleDateFormat sdFormat = new java.text.SimpleDateFormat("dd-MM-yyyy");
            String strFecha = sdFormat.format(dtHoy);

            /*Obtener entidad contable actual del alumno*/
            String strCCostoID = null;
            String strCCostoIDPlanNuevo = null;

            COMANDO = "SELECT C.CCOSTO_ID ";
            COMANDO += "FROM ENOC.ALUM_PLAN AP, ENOC.MAPA_PLAN P, ENOC.CAT_CARRERA C ";
            COMANDO += "WHERE P.PLAN_ID = AP.PLAN_ID ";
            COMANDO += "AND C.CARRERA_ID = P.CARRERA_ID ";
            COMANDO += "AND AP.ESTADO = '1' ";
            COMANDO += "AND AP.CODIGO_PERSONAL = ? ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, strMatricula);
            rset = pstmt.executeQuery();



            if (rset.next()) {
                strCCostoID = getEntidadValida(strIDEjercicio, rset.getString("CCosto_ID"), conn);
            }
            pstmt.close();
            rset.close();

            /*Evaluar si el alumno esta cambiando de status incobrable/pasivo a activo*/
            Integer intNReg = new Integer(0);
            COMANDO = "SELECT COUNT(*) NREG "
                    + "FROM MATEO.CONT_RELACION "
                    + "WHERE ID_EJERCICIO = ? "
                    + "AND ID_CCOSTO = ? "
                    + "AND ID_CTAMAYOR IN ('1.1.04.29','1.1.04.30') "
                    + "AND ID_AUXILIAR = ? "
                    + "AND STATUS = 'A' ";

            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, strIDEjercicio);
            pstmt.setString(2, strCCostoID);
            pstmt.setString(3, strMatricula);

            rset = pstmt.executeQuery();
            if (rset.next()) {
                intNReg = new Integer(rset.getInt("NReg"));
            }

            pstmt.close();
            rset.close();

            /*Si el alumno no tiene una cuenta de pasivos o incobrables activa
             * indica que es un alumno activo, por lo tanto no entra al siguiente 
             * codigo*/
            if (intNReg.compareTo(new Integer(0)) > 0) {
                /*Graba encabezado*/
                Integer intFolio = mavi_AltaEncabezado(strFecha, "Cambio de Estatus de Alumno", "INC", new Integer(1), strLogin, conn);

                /*Obtener cuenta activa del estudiante(incobrable, pasivo, activo)*/
                String strIDCtaMayor = null;

                COMANDO = "SELECT ID_CTAMAYOR ";
                COMANDO += "FROM MATEO.CONT_RELACION ";
                COMANDO += "WHERE ID_EJERCICIO = ? ";
                COMANDO += "AND ID_CCOSTO = ? ";
                COMANDO += "AND ID_CTAMAYOR IN ('1.1.04.01','1.1.04.29','1.1.04.30') ";
                COMANDO += "AND ID_AUXILIAR = ? ";
                COMANDO += "AND STATUS = 'A' ";

                pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, strIDEjercicio);
                pstmt.setString(2, strCCostoID);
                pstmt.setString(3, strMatricula);

                rset = pstmt.executeQuery();

                if (rset.next()) {
                    strIDCtaMayor = rset.getString("ID_CtaMayor");
                    //System.out.println("Cuenta de Mayor -" + strIDCtaMayor);
                }

                pstmt.close();
                rset.close();

                /*Grabar detalles*/

                mavi_AltaDetalle(intFolio, strIDEjercicio, strCCostoID, strIDCtaMayor, strMatricula, "D", new Boolean(false), conn);

                /*Si la cuenta de mayor, no es la de estudiantes activos (1.1.04.01), indica que un alumno se ha activado*/
                /*Si la cuenta de mayor es la de estudiantes activos (1.1.04.01) indica que el alumnos se convertira en pasivo*/
                /*Verificar el proceso de cambio de estatus  de alumnos en Escolar*/
                if (strIDCtaMayor.equals("1.1.04.01")) {
                    mavi_AltaDetalle(intFolio, strIDEjercicio, strCCostoID, "1.1.04.29", strMatricula, "C", new Boolean(true), conn);
                } else {
                    mavi_AltaDetalle(intFolio, strIDEjercicio, strCCostoID, "1.1.04.01", strMatricula, "C", new Boolean(true), conn);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Imposible dar de alta el aviso de actualizacion de status " + e.getMessage());

        } finally {
            pstmt.close();
            pstmt = null;
            rset.close();
            rset = null;
        }
    }


    /*Obtener nuevo id de determinado campo de determinada tabla*/
    public Integer mavi_getNuevoId(
            String strCampo, String strTabla) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        Integer intFolio = null;

        try {

            /*Obtener id*/
            String COMANDO = "SELECT COALESCE(MAX(" + strCampo + "),0) + 1 FOLIO ";
            COMANDO += "FROM mateo." + strTabla + " ";

            pstmt = conn.prepareStatement(COMANDO);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                intFolio = new Integer(rset.getInt("Folio"));
            }

        } catch (Exception e) {
            throw new Error("mavi_getNuevoId - Imposible obtener nuevo folio " + e);
        } finally {
            pstmt.close();
            pstmt = null;
            rset.close();
            rset = null;
        }

        return intFolio;
    }

    /*Obtener nuevo id de determinado campo de determinada tabla*/
    public Integer mavi_getNuevoId(
            String strCampo, String strTabla, Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        Integer intFolio = null;

        try {

            /*Obtener id*/
            String COMANDO = "SELECT COALESCE(MAX(" + strCampo + "),0) + 1 FOLIO ";
            COMANDO += "FROM mateo." + strTabla + " ";
            //System.out.println(COMANDO);
            pstmt = conn.prepareStatement(COMANDO);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                intFolio = new Integer(rset.getInt("Folio"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("mavi_getNuevoId - Imposible obtener nuevo folio " + e);
        } finally {
            pstmt.close();
            pstmt = null;
            rset.close();
            rset = null;
        }

        return intFolio;
    }


    /*Obtener nuevo id de determinado campo de determinada tabla en base a otro campo*/
    public Integer mavi_getNuevoId(
            String strCampo, String strTabla, String strCampoBase, Object objValue) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        Integer intFolio = null;

        try {

            /*Obtener id*/
            String COMANDO = "SELECT COALESCE(MAX(" + strCampo + "),0) + 1 FOLIO ";
            COMANDO += "FROM mateo." + strTabla + " ";
            COMANDO += "WHERE " + strCampoBase + "  = ? ";

            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setInt(1, ((Integer) objValue).intValue());
            rset = pstmt.executeQuery();

            if (rset.next()) {
                intFolio = new Integer(rset.getInt("Folio"));
            }
        } catch (Exception e) {
            throw new Error("mavi_getNuevoId - Imposible obtener nuevo folio " + e);
        } finally {
            pstmt.close();
            pstmt = null;
            rset.close();
            rset = null;
        }

        return intFolio;
    }

    /*Obtener nuevo id de determinado campo de determinada tabla en base a otro campo*/
    public Integer mavi_getNuevoId(
            String strCampo, String strTabla, String strCampoBase, Object objValue, Connection conn) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        Integer intFolio = null;

        try {

            /*Obtener id*/
            String COMANDO = "SELECT COALESCE(MAX(" + strCampo + "),0) + 1 FOLIO ";
            COMANDO += "FROM mateo." + strTabla + " ";
            COMANDO += "WHERE " + strCampoBase + "  = ? ";

            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setInt(1, ((Integer) objValue).intValue());
            rset = pstmt.executeQuery();

            if (rset.next()) {
                intFolio = new Integer(rset.getInt("Folio"));
            }
        } catch (Exception e) {
            throw new Error("mavi_getNuevoId - Imposible obtener nuevo folio " + e);
        } finally {
            pstmt.close();
            pstmt = null;
            rset.close();
            rset = null;
        }

        return intFolio;
    }

    public Connection getConnectionMateo() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/conn_mateo");
            conn = ds.getConnection();
        } catch (NamingException e) {
            try {
                //no existe un datasource
                Class.forName("oracle.jdbc.driver.OracleDriver");
                conn = DriverManager.getConnection("jdbc:oracle:thin:@rigel.um.edu.mx:1521:ora1", "mateo", "jgrjwjiewm");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return conn;
    }

    public Connection getConnectionNoe() {
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            DataSource ds = (DataSource) envContext.lookup("jdbc/conn_mateo");
            conn = ds.getConnection();
        } catch (NamingException e) {
            try {
                //no existe un datasource
                Class.forName("oracle.jdbc.driver.OracleDriver");
                conn = DriverManager.getConnection("jdbc:oracle:thin:@rigel.um.edu.mx:1521:ora1", "noe", "arcopacto");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return conn;
    }

    //Metodo para crear la cuenta de un alumno
    private void crearCuentaAlumno(
            String strMatricula) throws SQLException, Exception {
        //Obtener Ejercicio            
        String strIDEjercicio = "";
        String FE_COMANDO = "SELECT MAX(ID_EJERCICIO) ID_EJERCICIO ";
        FE_COMANDO += "FROM mateo.CONT_EJERCICIO ";
        FE_COMANDO += "WHERE STATUS = 'A' ";
        PreparedStatement pstmt = conn.prepareStatement(FE_COMANDO);
        ResultSet fe_rset = pstmt.executeQuery();

        if (fe_rset.next()) {
            strIDEjercicio = fe_rset.getString("ID_Ejercicio");
        }
        fe_rset.close();
        pstmt.close();

        int intNReg = 0;
        //Validar que la matricula contenga un valor v?lido
        if (strMatricula.length() != 7) {
            throw new Error("Matricula invalida!");
        }

        FE_COMANDO = "SELECT COUNT(*) NREG ";
        FE_COMANDO += "FROM enoc.ALUMNO_PERSONAL ";
        FE_COMANDO += "WHERE CODIGO_PERSONAL = ? ";
        pstmt = conn_enoc.prepareStatement(FE_COMANDO);
        pstmt.setString(1, strMatricula);
        fe_rset = pstmt.executeQuery();

        if (fe_rset.next()) {
            intNReg = fe_rset.getInt("NReg");
        }
        pstmt.close();
        fe_rset.close();

        if (intNReg == 0) {
            throw new Error("Alumno no dado de alta en el sistema");
        }

        //Obtener nombre del alumno
        String strNombre = null;

        FE_COMANDO = "SELECT NOMBRE_LEGAL NOMBRE ";
        FE_COMANDO += "FROM enoc.ALUMNO_PERSONAL ";
        FE_COMANDO += "WHERE CODIGO_PERSONAL = ? ";
        pstmt = conn_enoc.prepareStatement(FE_COMANDO);
        pstmt.setString(1, strMatricula);
        fe_rset = pstmt.executeQuery();

        if (fe_rset.next()) {
            strNombre = fe_rset.getString("Nombre").toUpperCase();
        }
        pstmt.close();
        fe_rset.close();

        //Verificar si existe la cuenta de auxiliar
        //Que sucedera cuando exista el auxiliar, pero en otro ejercicio contable??
        intNReg = 0;

        FE_COMANDO = "SELECT COUNT(*) NREG ";
        FE_COMANDO += "FROM mateo.CONT_AUXILIAR ";
        FE_COMANDO += "WHERE ID_EJERCICIO = ? ";
        FE_COMANDO += "AND ID_AUXILIAR = ? ";
        pstmt = conn.prepareStatement(FE_COMANDO);
        pstmt.setString(1, strIDEjercicio);
        pstmt.setString(2, strMatricula);
        fe_rset = pstmt.executeQuery();

        if (fe_rset.next()) {
            intNReg = fe_rset.getInt("NReg");
        }
        pstmt.close();
        fe_rset.close();

        /*Si el alumno no esta dado de alta en el catalogo de auxiliares,*/
        /*indica que es un alumno nuevo*/
        if (intNReg == 0) {
            //Insertar auxiliar
            FE_COMANDO = "INSERT INTO mateo.CONT_AUXILIAR ";
            FE_COMANDO += "(ID_EJERCICIO, ID_AUXILIAR, NOMBRE, DETALLE) ";
            FE_COMANDO += "VALUES ";
            FE_COMANDO += "(?, ?, ?, 'S')";
            pstmt = conn.prepareStatement(FE_COMANDO);
            pstmt.setString(1, strIDEjercicio);
            pstmt.setString(2, strMatricula);
            pstmt.setString(3, strNombre);
            pstmt.execute();
            pstmt.close();


            //Obtener contabilidad a la que pertenece el alumno
            //en base al plan de estudios activo
            String strCCostoID = null;

            FE_COMANDO = "SELECT C.CCOSTO_ID ";
            FE_COMANDO += "FROM enoc.ALUM_PLAN AP, enoc.MAPA_PLAN P, enoc.CAT_CARRERA C ";
            FE_COMANDO += "WHERE P.PLAN_ID = AP.PLAN_ID ";
            FE_COMANDO += "AND C.CARRERA_ID = P.CARRERA_ID ";
            FE_COMANDO += "AND AP.ESTADO = '1' ";
            FE_COMANDO += "AND AP.CODIGO_PERSONAL = ? ";
            pstmt = conn_enoc.prepareStatement(FE_COMANDO);
            pstmt.setString(1, strMatricula);
            fe_rset = pstmt.executeQuery();

            if (fe_rset.next()) {
                strCCostoID = getEntidadValida(strIDEjercicio, fe_rset.getString("ID_Nivel"));
            }
            pstmt.close();
            fe_rset.close();

            if (strCCostoID == null) {
                //Si el alumno no tiene plan de estudios activo
                //agarrar el primer plan disponible
                FE_COMANDO = "SELECT C.CCOSTO_ID ";
                FE_COMANDO += "FROM enoc.ALUM_PLAN AP, enoc.MAPA_PLAN P, enoc.CAT_CARRERA C ";
                FE_COMANDO += "WHERE P.PLAN_ID = AP.PLAN_ID ";
                FE_COMANDO += "AND S.CARRERA_ID = P.CARRERA_ID ";
                FE_COMANDO += "AND AP.CODIGO_PERSONAL = ? ";
                pstmt = conn_enoc.prepareStatement(FE_COMANDO);
                pstmt.setString(1, strMatricula);
                fe_rset = pstmt.executeQuery();

                if (fe_rset.next()) {
                    strCCostoID = getEntidadValida(strIDEjercicio, fe_rset.getString("ID_Nivel"));
                }
                pstmt.close();
                fe_rset.close();

                if (strCCostoID == null) {
                    throw new Error("Plan del alumno, no es valido!");
                }
            }

            strNombre = getNombreCCosto(strIDEjercicio, strCCostoID) + " ESTUDIANTES " + strNombre;

            //Verificar si existe la cuenta contable, en la contabilidad actual del alumno
            //aun cuando est? desactivada

            //Que sucedera cuando exista el auxiliar, pero en otro ejercicio contable??
            intNReg = 0;

            FE_COMANDO = "SELECT COUNT(*) NREG ";
            FE_COMANDO += "FROM mateo.CONT_RELACION ";
            FE_COMANDO += "WHERE ID_EJERCICIO = ? ";
            FE_COMANDO += "AND ID_CTAMAYOR = '1.1.04.01' ";
            FE_COMANDO += "AND ID_CCOSTO = ? ";
            FE_COMANDO += "AND ID_AUXILIAR = ? ";
            pstmt = conn.prepareStatement(FE_COMANDO);
            pstmt.setString(1, strIDEjercicio);
            pstmt.setString(2, strCCostoID);
            pstmt.setString(3, strMatricula);
            fe_rset = pstmt.executeQuery();

            if (fe_rset.next()) {
                intNReg = fe_rset.getInt("NReg");
            }
            pstmt.close();
            fe_rset.close();

            if (intNReg == 0) {
                FE_COMANDO = "INSERT INTO mateo.CONT_RELACION ";
                FE_COMANDO += "(ID_EJERCICIO, ID_CTAMAYOR, ID_CCOSTO, ID_AUXILIAR, NOMBRE, STATUS, NATURALEZA,TIPO_CUENTA,ID_EJERCICIO2,ID_EJERCICIO3) ";
                FE_COMANDO += "VALUES ";
                FE_COMANDO += "(?, '1.1.04.01', ?, ?, ?, 'A', 'D',?,?,?) ";
                pstmt = conn.prepareStatement(FE_COMANDO);
                pstmt.setString(1, strIDEjercicio);
                pstmt.setString(2, strCCostoID);
                pstmt.setString(3, strMatricula);
                pstmt.setString(4, strNombre);
                pstmt.setString(5, new CtaMayor().getTipoCuenta(strIDEjercicio, "1.1.04.01"));
                pstmt.setString(6, strIDEjercicio);
                pstmt.setString(7, strIDEjercicio);
                pstmt.execute();
                pstmt.close();
            }
        }

    }
}
