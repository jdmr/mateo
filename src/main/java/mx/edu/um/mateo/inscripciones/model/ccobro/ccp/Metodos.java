/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.inscripciones.model.ccobro.ccp;

import mx.edu.um.mateo.inscripciones.model.ccobro.poliza.Metodos1;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.servlet.http.HttpSession;

/**
 *
 * @author osoto
 */
public class Metodos {
    public Connection conn = null;
    public Metodos1 metsPoliza;

    public Metodos(Connection conn) {
        this.conn = conn;
    }

	public ResultSet getCuentasConPresupuesto(String strIDEjercicio, String strCCosto)throws Exception{
		String strComando = "SELECT ID_EJERCICIO,ID_CTAMAYOR,ID_CCOSTO ";
			strComando += "FROM mateo.CCP_PRESUPUESTO ";
			strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
			strComando += "AND ID_CCOSTO = '"+strCCosto+"' ";
			strComando += "GROUP BY ID_EJERCICIO,ID_CTAMAYOR,ID_CCOSTO ";
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		return rst;
	}

	public double getPresupuestoMesActual(String strIDEjercicio, String strIDCtaMayor,
	 					String strIDCcosto, int intMes) throws Exception{
		double dblCantidad = 0;
		String strComando = "SELECT COALESCE(SUM(IMPORTE),0) AS IMPORTE FROM mateo.CCP_PRESUPUESTO ";
			strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
			strComando += "AND ID_CCOSTO = '"+strIDCcosto+"' ";
			strComando += "AND ID_CTAMAYOR = '"+strIDCtaMayor+"' ";
			strComando += "AND MES <= "+intMes;
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next()){
			dblCantidad = rst.getDouble("IMPORTE");
		}
		rst.close();
		stmt.close();
		return dblCantidad;
	}

	/*Toma en cuenta el auxiliar para el caso de los fondos asignados*/
	public double getPresupuestoMesActual(String strIDEjercicio, String strIDCtaMayor,
	 					String strIDCcosto, String strIDAuxiliar, int intMes) throws Exception{
		double dblCantidad = 0;
		String strComando = "SELECT COALESCE(SUM(IMPORTE),0) AS IMPORTE FROM mateo.CCP_PRESUPUESTO ";
			strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
			strComando += "AND ID_CCOSTO = '"+strIDCcosto+"' ";
			strComando += "AND ID_CTAMAYOR = '"+strIDCtaMayor+"' ";
			strComando += "AND ID_AUXILIAR = '"+strIDAuxiliar+"' ";
			strComando += "AND MES <= "+intMes;
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next()){
			dblCantidad = rst.getDouble("IMPORTE");
		}
		rst.close();
		stmt.close();
		return dblCantidad;
	}



	public double getGastos(String strIDEjercicio, String strIDCtaMayor, String strIDCcosto)throws Exception{
		double dblCredito = 0;
		double dblCargo = 0;
		dblCargo = metsPoliza.getTotalCargos(strIDEjercicio, strIDCtaMayor, strIDCcosto,"", "01-01-"+getAnnoActual(), getFechaActual(""));
		dblCredito = metsPoliza.getTotalCreditos(strIDEjercicio, strIDCtaMayor, strIDCcosto,"", "01-01-"+getAnnoActual(), getFechaActual(""));
		return (dblCargo - dblCredito);
	}

	/*Toma en cuenta el auxiliar en el caso de los fondos asignados*/
	public double getGastos(String strIDEjercicio, String strIDCtaMayor, String strIDCcosto, String strIDAuxiliar)throws Exception{
		double dblCredito = 0;
		double dblCargo = 0;
		dblCargo = metsPoliza.getTotalCargos(strIDEjercicio, strIDCtaMayor, strIDCcosto,strIDAuxiliar, "01-01-"+getAnnoActual(), getFechaActual(""));
		dblCredito = metsPoliza.getTotalCreditos(strIDEjercicio, strIDCtaMayor, strIDCcosto,strIDAuxiliar, "01-01-"+getAnnoActual(), getFechaActual(""));

		return (dblCargo - dblCredito);
	}


	/*Toma en cuenta el auxiliar para el caso de los fondos asignados*/
	public double getComprometido(String strIDEjercicio, String strIDCtaMayor,
	        String strIDCcosto, String strIDAuxiliar, String strFechaInicial, String strFechaFinal)throws Exception{
		double dblCantidadComprometida = 0, dblCantidadDesComprometida = 0;
		String strComando = "SELECT COALESCE(SUM(PRECIO_U * CANTIDAD),0) AS IMPORTE ";
        strComando += "FROM mateo.CCP_AUTORIZACION_DET ";
		strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
		strComando += "AND ID_AUTORIZACION IN  ";
        strComando += "( ";
        strComando += "SELECT ID_AUTORIZACION ";
        strComando += "FROM mateo.CCP_AUTORIZACION ";
        strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
        strComando += "AND ID_CCOSTO = '"+strIDCcosto+"' ";
        strComando += "AND STATUS = 'R' ";
        strComando += "AND FECHA BETWEEN TO_DATE('"+strFechaInicial+"', 'DD/MM/YY') AND TO_DATE('"+strFechaFinal+"', 'DD/MM/YY') ";
        strComando += ")";
		strComando += "AND ID_CTAMAYOR = '"+strIDCtaMayor+"' ";
		strComando += "AND ID_AUXILIAR = '"+strIDAuxiliar+"' ";

		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next()){
			dblCantidadComprometida = rst.getDouble("IMPORTE");
		}
        rst.close();

        strComando = "SELECT COALESCE(SUM(IMPORTE),0) AS IMPORTE ";
		strComando += "FROM CCP_DESCOMPROMETIDO ";
		strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
		strComando += "AND ID_AUTORIZACION IN ";
		strComando += "( ";
		strComando += "SELECT ID_AUTORIZACION ";
		strComando += "FROM CCP_AUTORIZACION_DET ";
		strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
		strComando += "AND ID_AUTORIZACION IN  ";
        strComando += "( ";
        strComando += "SELECT ID_AUTORIZACION ";
        strComando += "FROM CCP_AUTORIZACION ";
        strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
        strComando += "AND ID_CCOSTO = '"+strIDCcosto+"' ";
        strComando += "AND STATUS = 'R' ";
        strComando += "AND FECHA BETWEEN TO_DATE('"+strFechaInicial+"', 'DD/MM/YY') AND TO_DATE('"+strFechaFinal+"', 'DD/MM/YY') ";
        strComando += ") ";
		strComando += "AND ID_CTAMAYOR = '"+strIDCtaMayor+"' ";
		strComando += "AND ID_AUXILIAR = '"+strIDAuxiliar+"' " ;
        strComando += ") ";
        strComando += "AND STATUS = 'A' ";
		rst = stmt.executeQuery(strComando);
		if(rst.next()){
			dblCantidadDesComprometida = rst.getDouble("IMPORTE");
		}

		rst.close();
		stmt.close();
		return (dblCantidadComprometida - dblCantidadDesComprometida );
	}

	public double getComprometidoCC
        (
                String strIDEjercicio, String strIDCcosto, String strFechaI, String strFechaF
        )throws Exception
        {
		double dblCantidadComprometida = 0, dblCantidadDesComprometida = 0;
		String strComando = "SELECT COALESCE(SUM(PRECIO_U * CANTIDAD),0) AS IMPORTE ";
        strComando += "FROM mateo.CCP_AUTORIZACION_DET ";
		strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
        strComando += "AND ID_AUTORIZACION IN  ";
        strComando += "( ";
        strComando += "SELECT ID_AUTORIZACION ";
        strComando += "FROM mateo.CCP_AUTORIZACION ";
        strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
        strComando += "AND ID_CCOSTO = '"+strIDCcosto+"' ";
        strComando += "AND STATUS = 'R' ";
        strComando += "AND TO_DATE(FECHA, 'DD/MM/YY') ";
        strComando += "BETWEEN TO_DATE('"+strFechaI+"', 'DD/MM/YY') ";
        strComando += "AND BETWEEN TO_DATE('"+strFechaF+"', 'DD/MM/YY') ";
        strComando += ")";
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next()){
			dblCantidadComprometida = rst.getDouble("IMPORTE");
		}
        rst.close();



        //Se buscan aquellas autorizaciones con movimientos en la tabla de descomprometido
        //entre las fechas indicadas.
        //Sin embargo, se tiene que validar que las autorizaciones sean de cierto rango de fechas
        //y que los registros de descomprometido sean tambi?n de dicho rango de fechas
        strComando = "SELECT COALESCE(SUM(IMPORTE),0) AS IMPORTE FROM CCP_DESCOMPROMETIDO ";
		strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
        strComando += "AND ID_AUTORIZACION IN ";
        strComando += "( ";
        strComando += "SELECT ID_AUTORIZACION ";
        strComando += "FROM CCP_AUTORIZACION ";
        strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
        strComando += "AND ID_CCOSTO = '"+strIDCcosto+"' ";
        strComando += "AND STATUS = 'R' ";
        strComando += "AND TO_DATE(FECHA, 'DD/MM/YY') ";
        strComando += "BETWEEN TO_DATE('"+strFechaI+"', 'DD/MM/YY') ";
        strComando += "AND BETWEEN TO_DATE('"+strFechaF+"', 'DD/MM/YY') ";
        strComando += ")";
        strComando += "AND TO_DATE(FECHA, 'DD/MM/YY') ";
        strComando += "BETWEEN TO_DATE('"+strFechaI+"', 'DD/MM/YY') ";
        strComando += "AND BETWEEN TO_DATE('"+strFechaF+"', 'DD/MM/YY') ";
		rst = stmt.executeQuery(strComando);
		if(rst.next()){
			dblCantidadDesComprometida = rst.getDouble("IMPORTE");
		}

		rst.close();
		stmt.close();

		        //System.out.println("GetComprometidoCC "+"@"+strIDEjercicio+"@"+strIDCcosto+"@"+strFechaI+"@"+strFechaF+"@"+String.valueOf(dblCantidadDesComprometida));

		return (dblCantidadComprometida - dblCantidadDesComprometida );
	}

        public double getPresupuestoDisponible(String strIDEjercicio, String strIDCtaMayor,
						String strIDCcosto, int intMes)throws Exception{
		double dblPresupuesto = getPresupuestoMesActual(strIDEjercicio, strIDCtaMayor, strIDCcosto, intMes);

                java.util.Calendar gcFechaActual = new java.util.GregorianCalendar();
                gcFechaActual.setTime(new java.util.Date());
                gcFechaActual.set(java.util.Calendar.MONTH, intMes);
                gcFechaActual.getActualMaximum(java.util.Calendar.DATE);
                String strFechaActual = new java.text.SimpleDateFormat("dd/MM/yyyy").format(gcFechaActual.getTime());

                double dblComprometido = getComprometido(strIDEjercicio, strIDCtaMayor, strIDCcosto, strFechaActual);
		double dblGastos = getGastos(strIDEjercicio, strIDCtaMayor, strIDCcosto);
		return (dblPresupuesto - dblComprometido - dblGastos);

	}

    /*Esta funcion toma en cuenta el auxiliar, para el caso de los fondos asignados*/
    public double getPresupuestoDisponible(String strIDEjercicio, String strIDCtaMayor,
						String strIDCcosto, String strIDAuxiliar, int intMes)throws Exception{
		double dblPresupuesto = getPresupuestoMesActual(strIDEjercicio, strIDCtaMayor, strIDCcosto, strIDAuxiliar, intMes);

                java.util.Calendar gcFechaActual = new java.util.GregorianCalendar();
                gcFechaActual.setTime(new java.util.Date());
                gcFechaActual.set(java.util.Calendar.MONTH, intMes);
                gcFechaActual.getActualMaximum(java.util.Calendar.DATE);
                String strFechaActual = new java.text.SimpleDateFormat("dd/MM/yyyy").format(gcFechaActual.getTime());

                double dblComprometido = getComprometido(strIDEjercicio, strIDCtaMayor, strIDCcosto, strFechaActual);
		double dblGastos = getGastos(strIDEjercicio, strIDCtaMayor, strIDCcosto, strIDAuxiliar);
		return (dblPresupuesto - dblComprometido - dblGastos);

	}

	public void grabaAutorizacion(String strIDEjercicio, String strIDAutorizacion, String strIDAutorizacionDet,
					String strIDCtaMayor, String strIDCCosto, String strIDAuxiliar,
					double dblCantidad, String strDescripcion, double dblPUnitario, String strStatus, HttpSession session
					)throws Exception{

			/*Validar el centro de costo, si es una contabilidad, el parametro cuenta de mayor contiene un auxiliar*/
			/*de fondos asignados*/
                        if(((String)session.getAttribute("id_ccosto")).equals((String)session.getAttribute("id_ccosto_ccp")))
			{
				strIDAuxiliar = strIDCtaMayor;
				strIDCtaMayor = "3.1.02.01";
			}
                        //Si la ctaMayor contiene mas de 7 caracteres, indica que no es un auxiliar y mucho menos una ctaMayor
                        //Entonces se espera recibir un string compuesto por una ctaMayor y un auxiliar
                        else if(strIDCtaMayor.length() > 7) {
                            String [] splits = strIDCtaMayor.split("-");
                            strIDCtaMayor = splits[0];
                            strIDAuxiliar = splits[1];
                        }

			String strComando = "INSERT INTO mateo.CCP_AUTORIZACION_DET(ID_EJERCICIO, ";
			strComando += "ID_AUTORIZACION, ID_AUTORIZACION_DET, ID_CTAMAYOR, ID_CCOSTO, ID_AUXILIAR, ";
			strComando += "CANTIDAD, DESCRIPCION, PRECIO_U, STATUS) ";
			strComando += "VALUES('"+strIDEjercicio+"', '"+strIDAutorizacion+"', "+strIDAutorizacionDet+",";
			strComando += "'"+strIDCtaMayor+"','"+strIDCCosto+"','"+strIDAuxiliar+"', "+String.valueOf(dblCantidad)+", ";
			strComando += "'"+strDescripcion+"', "+String.valueOf(dblPUnitario)+", '"+strStatus+"') ";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(strComando);
			stmt.close();
	}

	public void grabaEncabezadoAutorizacion(String strIDEjercicio, String strIDAutorizacion, String strIDCCosto,
					String strFecha, String strMoneda, String strNoDoc, String strClaveEmpleado,
					String strProveedor, String strServicio, String strUsuarioLogin, String strStatus)throws Exception{
		String strComando = "INSERT INTO mateo.CCP_AUTORIZACION(ID_EJERCICIO, ";
			strComando += "ID_AUTORIZACION, ID_CCOSTO, FECHA, TIPO, NO_DOC, CLAVEEMPLEADO, PROVEEDOR, SERVICIO, USUARIO, STATUS, IMPRESO) ";
			strComando += "VALUES('"+strIDEjercicio+"', '"+strIDAutorizacion+"', '"+strIDCCosto+"',";
			strComando += "TO_DATE('"+strFecha+"', 'dd/mm/yy'),'"+strMoneda+"','"+strNoDoc+"','"+strClaveEmpleado+"','"+strProveedor+"',";
                        strComando += "'"+strServicio+"','"+strUsuarioLogin+"', '"+strStatus+"', 'N') ";

			PreparedStatement pstmt = conn.prepareStatement(strComando);
			pstmt.execute();
			pstmt.close();
	}

	public void modificaEncAutorizacion(HttpSession session, String strIDAutorizacion,
						String strNoDoc, String strClaveEmpleado, String strFecha, String strMoneda,
                                                String strProveedor, String strServicio)throws Exception{
		String strComando = "UPDATE mateo.CCP_AUTORIZACION ";
			strComando += "SET NO_DOC = '"+strNoDoc+"', ";
                        strComando += "FECHA = to_date('"+strFecha+"','dd/mm/yy'), ";
			strComando += "CLAVEEMPLEADO = '"+strClaveEmpleado+"', ";
			strComando += "TIPO = '"+strMoneda+"', ";
                        strComando += "PROVEEDOR = '"+strProveedor+"', ";
                        strComando += "SERVICIO = '"+strServicio+"' ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strIDAutorizacion+"'";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(strComando);
	}
	public void modificaDetAutorizacion(HttpSession session, String strIDAutorizacion, String strIDAutorizacionDet,
						double dblCantidad, String strDescripcion, double dblPrecioU,
						String strCtaMayor, String strCCosto)throws Exception{

                         String strAuxiliar = "";

                        String strComando = "UPDATE mateo.CCP_AUTORIZACION_DET ";
			strComando += "SET DESCRIPCION = '"+strDescripcion+"', CANTIDAD = "+String.valueOf(dblCantidad)+", PRECIO_U = "+String.valueOf(dblPrecioU)+", ";


                        /*Validar el centro de costo, si es una contabilidad, el parametro cuenta de mayor contiene un auxiliar*/
			/*de fondos asignados*/
                        if(((String)session.getAttribute("id_ccosto")).equals((String)session.getAttribute("id_ccosto_ccp")))
			{
				strAuxiliar = strCtaMayor;
				strCtaMayor = "3.1.02.01";
			}
                        //Si la ctaMayor contiene mas de 7 caracteres, indica que no es un auxiliar y mucho menos una ctaMayor
                        //Entonces se espera recibir un string compuesto por una ctaMayor y un auxiliar
                        else if(strCtaMayor.length() > 7) {
                            String [] splits = strCtaMayor.split("-");
                            strCtaMayor = splits[0];
                            strAuxiliar = splits[1];
                        }
                        else {
                            strAuxiliar = "0000000";
                        }

                        strComando += " ID_CTAMAYOR = '"+strCtaMayor+"', ";
                        strComando += " ID_AUXILIAR = '"+strAuxiliar+"' ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strIDAutorizacion+"' ";
			strComando += "AND ID_AUTORIZACION_DET = "+strIDAutorizacionDet;


			Statement stmt = conn.createStatement();
			stmt.executeUpdate(strComando);
	}
	//utilizades
	public int getDia(java.util.Date dtFecha)throws Exception{
		java.text.SimpleDateFormat sdfFormateer = new java.text.SimpleDateFormat("dd");
		return Integer.parseInt(sdfFormateer.format(dtFecha));
	}
	//utilizades
	public int getMes(java.util.Date dtFecha)throws Exception{
		java.text.SimpleDateFormat sdfFormateer = new java.text.SimpleDateFormat("MM");
		return Integer.parseInt(sdfFormateer.format(dtFecha));
	}
	//utilizades
	public int getAnno(java.util.Date dtFecha)throws Exception{
		java.text.SimpleDateFormat sdfFormateer = new java.text.SimpleDateFormat("yy");
		return Integer.parseInt(sdfFormateer.format(dtFecha));
	}
	//utilizades
	public int getDiaActual()throws Exception{
		java.util.Date dtFecha = new java.util.Date();
		java.text.SimpleDateFormat sdfFormateer = new java.text.SimpleDateFormat("dd");
		return Integer.parseInt(sdfFormateer.format(dtFecha));
	}
	//utilizades
	public int getMesActual()throws Exception{
		java.util.Date dtFecha = new java.util.Date();
		java.text.SimpleDateFormat sdfFormateer = new java.text.SimpleDateFormat("MM");
		return Integer.parseInt(sdfFormateer.format(dtFecha));
	}

	//utilizades
	public String getAnnoActual()throws Exception{
		java.util.Date dtFecha = new java.util.Date();
		java.text.SimpleDateFormat sdfFormateer = new java.text.SimpleDateFormat("yy");
		return sdfFormateer.format(dtFecha);
	}

	//utilizades
	public String getFechaActual(String strTemp)throws Exception{
		java.util.Date dtFecha = new java.util.Date();
		java.text.SimpleDateFormat sdfFormateer = new java.text.SimpleDateFormat("dd-MM-yy");
		return (sdfFormateer.format(dtFecha));
	}

	public java.sql.Date getFechaActual()throws Exception{
		java.util.Date dtFechaAct = new java.util.Date();
		java.sql.Date dtFecha = new java.sql.Date(dtFechaAct.getTime());
		return dtFecha;
	}

	public String getAutorizacionID(String strIDEjercicio)throws Exception{
		String strAutorizacionId = "";
		String strComando = "SELECT COALESCE(MAX(ID_AUTORIZACION)+1,'9900001') ID_AUTORIZACION ";
		strComando += "FROM mateo.CCP_AUTORIZACION ";
		strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
		strComando += "AND LENGTH(ID_AUTORIZACION) = 7 ";
		Statement stmt = conn.createStatement();

		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next())
			strAutorizacionId = rst.getString("ID_AUTORIZACION");
		rst.close();
		stmt.close();
		return strAutorizacionId;

	}

	public String getAutorizacionIdDet(String strIDEjercicio, String strIDAutorizacion)throws Exception{
		String strAutorizacionIdDet = "";
		String strComando = "SELECT COALESCE(MAX(ID_AUTORIZACION_DET),0) + 1 ID_AUTORIZACION_DET ";
		strComando += "FROM mateo.CCP_AUTORIZACION_DET ";
		strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
		strComando += "AND ID_AUTORIZACION = '"+strIDAutorizacion+"' ";
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next())
			strAutorizacionIdDet = rst.getString("ID_AUTORIZACION_DET");
		rst.close();
		stmt.close();
		return strAutorizacionIdDet;

	}

	//se utiliza para cada departamento
	public PreparedStatement getAutorizacionesEnc(HttpSession session, String strStatus)throws Exception{
		String strComando = "SELECT  * FROM mateo.CCP_AUTORIZACION ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_CCOSTO = '"+(String)session.getAttribute("id_ccosto_ccp")+"' ";
			strComando += "AND STATUS = '"+strStatus+"' ";
                        strComando += "ORDER BY ID_AUTORIZACION ";
			PreparedStatement pstmt = conn.prepareStatement(strComando);

			return pstmt;
	}

	//se utiliza para obtener autorizaciones independientemente del departamento
	public PreparedStatement getAutorizacionesEncT(HttpSession session, String strStatus)throws Exception{
		String strComando = "SELECT  * FROM mateo.CCP_AUTORIZACION ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                        strComando += "AND ID_CCOSTO = '"+(String)session.getAttribute("id_ccosto_ccp")+"' ";
			strComando += "AND STATUS = '"+strStatus+"' ";
                        strComando += "ORDER BY ID_AUTORIZACION DESC ";
			PreparedStatement pstmt = conn.prepareStatement(strComando);

			return pstmt;
	}

	//se utiliza para obtener autorizaciones independientemente del departamento
	public PreparedStatement getAutorizacionesEncTGral(HttpSession session, String strStatus)throws Exception{
		String strComando = "SELECT  * FROM CCP_AUTORIZACION ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND STATUS = '"+strStatus+"' ";
                        strComando += "ORDER BY ID_AUTORIZACION DESC ";
			PreparedStatement pstmt = conn.prepareStatement(strComando);

			return pstmt;
	}

	//se utiliza para obtener autorizaciones independientemente del departamento
    //y validando que el usuario que autoriz? sea distinto al actual
	public PreparedStatement getAutorizacionesEncTGralUser(HttpSession session, String strStatus)throws Exception{
		String strComando = "SELECT  * FROM mateo.CCP_AUTORIZACION ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND STATUS = '"+strStatus+"' ";
            strComando += "AND USUARIO_AUT_RECH != '"+(String)session.getAttribute("login")+"' ";
            strComando += "ORDER BY ID_AUTORIZACION DESC ";
			PreparedStatement pstmt = conn.prepareStatement(strComando);

			return pstmt;
	}

	//se utiliza para una sola autorizacion
	public PreparedStatement getAutorizacionEnc(HttpSession session, String strAutorizacion)throws Exception{
		String strComando = "SELECT  * FROM mateo.CCP_AUTORIZACION ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
			PreparedStatement pstmt = conn.prepareStatement(strComando);

			return pstmt;
	}

	public PreparedStatement getAutorizacionesDet(HttpSession session, String strAutorizacion,
						String strStatus)throws Exception{
		String strComando = "SELECT  * FROM mateo.CCP_AUTORIZACION_DET ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
			strComando += "AND STATUS = '"+strStatus+"'";
			Statement stmt = conn.createStatement();
                        PreparedStatement pstmt = conn.prepareStatement(strComando);

			return pstmt;
	}

	public PreparedStatement getAutorizacionesDet(HttpSession session, String strAutorizacion)throws Exception{
		String strComando = "SELECT  * FROM mateo.CCP_AUTORIZACION_DET ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"'";
			PreparedStatement pstmt = conn.prepareStatement(strComando);

			return pstmt;
	}

        public Map <String, Double> getImportesTotalContabilizados(HttpSession session)throws Exception{
                Map <String, Double> mReturn = new TreeMap <String, Double> ();
		double dblImporte = 0;
		String strComando = "SELECT  REFERENCIA2, COALESCE(SUM(IMPORTE),0) IMPORTE ";
                        strComando += "FROM MATEO.CONT_MOVIMIENTO ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                        strComando += "AND REFERENCIA2 LIKE '99%' ";
                        strComando += "GROUP BY ID_EJERCICIO, REFERENCIA2 ";
			Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(strComando);
			while(rst.next()){
				dblImporte = rst.getDouble("importe");
                                mReturn.put(rst.getString("referencia2"), dblImporte);
                        }
                        stmt.close();
                        rst.close();

                        return mReturn;
	}

        public double getImporteTotalContabilizado(HttpSession session, String strAutorizacion)throws Exception{
		double dblImporte = 0;
		String strComando = "SELECT  COALESCE(SUM(IMPORTE),0) ";
                        strComando += "FROM MATEO.CONT_MOVIMIENTO ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                        strComando += "AND REFERENCIA2 LIKE '99%' ";
			strComando += "AND REFERENCIA2 = '%"+strAutorizacion+"%'";
			Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(strComando);
			if(rst.next())
				dblImporte = rst.getDouble(1);
                        stmt.close();
                        rst.close();

                        return dblImporte;
	}

	public double getImporteTotalAutorizacion(HttpSession session, String strAutorizacion)throws Exception{
		double dblImporte = 0;
		String strComando = "SELECT  COALESCE(SUM(CANTIDAD*PRECIO_U),0) ";
                        strComando += "FROM mateo.CCP_AUTORIZACION_DET ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"'";
			Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(strComando);
			if(rst.next())
				dblImporte = rst.getDouble(1);
                        stmt.close();
                        rst.close();

                        return dblImporte;
	}

	public double getImporteTotalAutorizacionPesos(HttpSession session, String strAutorizacion)throws Exception{
		double dblImporte = 0;
		String strComando = "SELECT  COALESCE(SUM(CANTIDAD*PRECIO_U),0) ";
                        strComando += "FROM mateo.CCP_AUTORIZACION_DET ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"'";
			Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(strComando);
			if(rst.next())
				dblImporte = rst.getDouble(1);
                        stmt.close();
                        rst.close();

                        return dblImporte;
	}

	public PreparedStatement getAutorizacionDet(HttpSession session, String strAutorizacion,
						String strAutorizacionDet)throws Exception{
		String strComando = "SELECT  * FROM mateo.CCP_AUTORIZACION_DET ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
			strComando += "AND ID_AUTORIZACION_DET = '"+strAutorizacionDet+"' ";
			PreparedStatement pstmt = conn.prepareStatement(strComando);

			return pstmt;
	}


	//se utiliza para cada departamento
	public boolean getAutorizacionesEncBool(HttpSession session, String strStatus)throws Exception{
		int intNumReg = 0;
		String strComando = "SELECT  COUNT(*) FROM mateo.CCP_AUTORIZACION ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_CCOSTO = '"+(String)session.getAttribute("id_ccosto_ccp")+"' ";
			strComando += "AND STATUS = '"+strStatus+"'";
                        strComando += "ORDER BY ID_AUTORIZACION ";
			Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(strComando);
			if(rst.next())
				intNumReg = rst.getInt(1);
			rst.close();
			stmt.close();
			return (intNumReg > 0);
	}


	public boolean getAutorizacionesDetBool(HttpSession session, String strAutorizacion,
						String strStatus)throws Exception{
		int intNumReg = 0;
		String strComando = "SELECT  COUNT(*) FROM mateo.CCP_AUTORIZACION_DET ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
			strComando += "AND STATUS = '"+strStatus+"'";
			Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(strComando);
			if(rst.next())
				intNumReg = rst.getInt(1);
			rst.close();
			stmt.close();
			return (intNumReg > 0);
	}


	public void borraEncabezadoAutorizacion(HttpSession session, String strAutorizacion)throws Exception{
		String strComando = "DELETE FROM mateo.CCP_AUTORIZACION ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"'"; //en la db es numerico
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(strComando);
			stmt.close();
	}


	public void borraDetalleAutorizacion(HttpSession session, String strAutorizacion,
						String strAutorizacionDet)throws Exception{
		String strComando = "DELETE FROM mateo.CCP_AUTORIZACION_DET ";
			strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
			strComando += "AND ID_AUTORIZACION_DET = "+strAutorizacionDet; //en la db es numerico
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(strComando);
			stmt.close();
	}

	public void cambiaStatus(HttpSession session, String strEjercicio, String strAutorizacion, String strStatusAct, boolean boolAutomatico)throws Exception{
			if(boolAutomatico){
				if(strStatusAct.equals("A"))
					strStatusAct = "OK";
				else if(strStatusAct.equals("OK"))
					strStatusAct = "AT";
				else if(strStatusAct.equals("AT"))
					strStatusAct = "R";
				else if(strStatusAct.equals("R"))
					throw new Error("La autorzacion ya ha sido aceptada, no se pueden hacer cambios");
				else if(strStatusAct.equals("X"))
					throw new Error("La autorzacion ya ha sido rechazada");
				else
					throw new Error("Status desconocido");
			}

                        String strUsuario = (String)session.getAttribute("login");

                        String strTabla1 = "UPDATE mateo.CCP_AUTORIZACION ";
			String strTabla2 = "UPDATE mateo.CCP_AUTORIZACION_DET ";
			String strComando = "SET STATUS = '"+strStatusAct+"' ";
				strComando += "WHERE ID_EJERCICIO = '"+strEjercicio+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"'";
			Statement stmt = conn.createStatement();
			stmt.executeUpdate(strTabla1+strComando);
			stmt.executeUpdate(strTabla2+strComando);

                        //Actualizar dato de quien confirma
                        if (strStatusAct.equals("OK"))
                        {
                                strComando = "UPDATE CCP_AUTORIZACION ";
                                strComando += "SET USUARIO_CONFIRMA = '"+strUsuario+"', ";
                                strComando += "FECHA_CONFIRMA = SYSDATE ";
                                strComando += "WHERE ID_EJERCICIO = '"+strEjercicio+"' ";
                                strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
                                stmt.executeUpdate(strComando);
                        }

                        //Actualizar dato de quien Autoriza
                        else if (strStatusAct.equals("R"))
                        {
                                //Obtener tipo de cambio
                                Double dblTCambio = new Double(0);
                                String COMANDO = "SELECT TIPO_CAMBIO_DLL ";
                                COMANDO += "FROM mateo.CONT_TIPOCAMBIO ";
                                PreparedStatement pstmt = conn.prepareStatement(COMANDO);
                                ResultSet rset = pstmt.executeQuery();

                                if (rset.next())
                                {
                                        dblTCambio = new Double(rset.getDouble("Tipo_Cambio_Dll"));
                                }

                                pstmt.close();
                                rset.close();

                                strComando = "UPDATE CCP_AUTORIZACION ";
                                strComando += "SET USUARIO_AUT_RECH = '"+strUsuario+"', ";
                                strComando += "FECHA_AUT_RECH = SYSDATE, ";
                                strComando += "TIPO_CAMBIO_DLL = " + dblTCambio.toString() + " ";
                                strComando += "WHERE ID_EJERCICIO = '"+strEjercicio+"' ";
                                strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
                                stmt.executeUpdate(strComando);
                        }

                        //Actualizar dato de quien Autoriza
                        else if (strStatusAct.equals("X"))
                        {
                                strComando = "UPDATE CCP_AUTORIZACION ";
                                strComando += "SET USUARIO_AUT_RECH = '"+strUsuario+"', ";
                                strComando += "FECHA_AUT_RECH = SYSDATE ";
                                strComando += "WHERE ID_EJERCICIO = '"+strEjercicio+"' ";
                                strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
                                stmt.executeUpdate(strComando);
                        }

			stmt.close();

	}

        public double getLimiteAutorizacion() throws SQLException, Exception
        {
                //En caso de que no exista ningun registro, se asigna por default
                //la cantidad de $1500.00

                double dblLimite = 1500;

                String COMANDO = "SELECT COALESCE(LIMITE_AUTORIZACION, 1500) LIMITE ";
                COMANDO += "FROM mateo.CCP_PARAMGRAL ";
                PreparedStatement pstmt = conn.prepareStatement(COMANDO);
                ResultSet rset = pstmt.executeQuery();

                if (rset.next())
                {
                        dblLimite = rset.getDouble("Limite");
                }

                rset.close();
                pstmt.close();

                return dblLimite;
        }

	//funciones para descomprometido
	//esta funcion se utiliza en un procedimiento comun solo llamando las principales
	//para descomprometido manual......
	public void descomprometerMovimiento(HttpSession session, String strAutorizacion, String strAutorizacionDet, java.sql.Date dtFecha,
										String strCtaMayor, String strCCosto, String strAuxiliar, double dblImporte,
										String strDescripcion)throws Exception{

		int intDescomprometidoID = 0;
		String strComando = "";
		double dblImportePorDescomprometer = 0;

		//obtenemos la cantidad que falta por descomprometer
		dblImportePorDescomprometer = getCantidadPorDescomprometer(session, strAutorizacion, strAutorizacionDet, strCtaMayor, strCCosto, strAuxiliar);

		//verifcamos si el importe que se est? tratando de insertar es mayoyr que la cantidad disponible
		if( dblImportePorDescomprometer < dblImporte)
			throw new Error ("La cantidad es mayor al importe de la autorizacion.");

		intDescomprometidoID = getDescomprometidoIDNum();

		strComando = "INSERT INTO mateo.CCP_DESCOMPROMETIDO(ID_EJERCICIO, ID_AUTORIZACION, ID_AUTORIZACION_DET, ID_DESCOMPROMETIDO, FECHA, ";
				strComando += "ID_CTAMAYOR_M, ID_CCOSTO_M, ID_AUXILIAR_M, IMPORTE, DESCRIPCION, USUARIO, STATUS) ";
				strComando += "VALUES('"+(String)session.getAttribute("id_ejercicio")+"', '"+strAutorizacion+"', '"+strAutorizacionDet+"', "+intDescomprometidoID+", ";
				strComando += "?, '"+strCtaMayor+"', '"+strCCosto+"', '"+strAuxiliar+"', "+dblImporte+", '"+strDescripcion+"', ";
				strComando += "'"+(String)session.getAttribute("login")+"', 'A')";
				PreparedStatement pstmt = conn.prepareStatement(strComando);
				pstmt.setDate(1,dtFecha);
				pstmt.execute();
				pstmt.close();

		if (dblImportePorDescomprometer == dblImporte)	//cuando se descompromete por el total, el movimiento pasa a status D
			cambiaStatusDescomprometerDet(session, strAutorizacion, strAutorizacionDet, "D");

		//si ya no existen movimientos comprometidos se cambia el encabezado a status D

		if(!getDetallesComprometidos(session, strAutorizacion))
			cambiaStatusDescomprometerEnc(session, strAutorizacion, "D");

	}

	//comprometer movimiento
	//comprometido manual
	public void comprometerMovimiento(HttpSession session, String strAutorizacion, String strAutorizacionDet,
										String strCtaMayor, String strCCosto, String strAuxiliar)throws Exception{
		//obtener datos del detalle de autorizacion
		//cambiar el status del encabezado a R
		if(!getDetallesComprometidos(session, strAutorizacion)) //si todos los movimientos estan descomprometidos se cambia el status del encabezado
			cambiaStatusDescomprometerEnc(session, strAutorizacion, "R");
		//cambiar el status del detalle a R
		cambiaStatusDescomprometerDet(session, strAutorizacion, strAutorizacionDet, "R");
		//borrar los registros de CCP_DESCOMPROMETIDO relacionados con la autorizacion y el detalle
		borrarDescomprometido(session, strAutorizacion, strAutorizacionDet, strCtaMayor, strCCosto, strAuxiliar);
	}
	//para el procedimiento manual
	public void descomprometerEncabezado(HttpSession session, String strAutorizacion, String strDescripcion)throws Exception{
		String strAutorizacionDet = "";
		String strCtaMayor = "";
		String strCCosto = "";
		String strAuxiliar = "";
		double dblImporte = 0;
		java.sql.Date dtFecha = getFechaActual();
                PreparedStatement pstmt = getAutorizacionesDet(session, strAutorizacion, "R");
                ResultSet rst = pstmt.executeQuery();
		while(rst.next()){
			strAutorizacionDet = rst.getString("ID_AUTORIZACION_DET");
			strCtaMayor = rst.getString("ID_CTAMAYOR");
			strCCosto = rst.getString("ID_CCosto");
			strAuxiliar = rst.getString("ID_AUXILIAR");
                        dblImporte = getCantidadPorDescomprometer(session, strAutorizacion, strAutorizacionDet, strCtaMayor, strCCosto, strAuxiliar);
                        descomprometerMovimiento(session, strAutorizacion, strAutorizacionDet, dtFecha, strCtaMayor, strCCosto, strAuxiliar, dblImporte, strDescripcion);
			cambiaStatusDescomprometerDet(session, strAutorizacion, strAutorizacionDet, "D");
		}
		rst.close();
                pstmt.close();
		cambiaStatusDescomprometerEnc(session, strAutorizacion, "D");
	}

	//para el proceso manual
	public double getCantidadPorDescomprometer(HttpSession session, String strAutorizacion, String strAutorizacionDet,
	        String strCtaMayor, String strCCosto, String strAuxiliar)throws Exception{
		double dblImporte = 0;
		double dblImporteD = 0;
		String strComando = "SELECT COALESCE(CANTIDAD * PRECIO_U,0) IMPORTE ";
                                strComando += "FROM  mateo.CCP_AUTORIZACION_DET ";
				strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
				strComando += "AND ID_AUTORIZACION_DET = '"+strAutorizacionDet+"'";
				Statement stmt = conn.createStatement();

                                ResultSet rst = stmt.executeQuery(strComando);
				if(rst.next())
					dblImporte = rst.getDouble("IMPORTE");
                                rst.close();

				//ES NECESARIO QUE SE ADICIONE LA COLUMNA DE ID_AUTORIZACIONDET PARA HACER EL CALCULO CORRECTO
				strComando = "SELECT COALESCE(SUM(IMPORTE),0) FROM  CCP_DESCOMPROMETIDO ";
				strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
				strComando += "AND ID_AUTORIZACION_DET = '"+strAutorizacionDet+"' ";
				strComando += "AND ID_CCOSTO_M = '"+strCCosto+"' ";
				strComando += "AND ID_CTAMAYOR_M = '"+strCtaMayor+"' ";
				strComando += "AND ID_AUXILIAR_M = '"+strAuxiliar+"'";
				rst = stmt.executeQuery(strComando);
				if(rst.next())
					dblImporteD = rst.getDouble(1);
                                rst.close();
                                stmt.close();

				return (dblImporte - dblImporteD);
	}


	public String getDescomprometidoID()throws Exception{
		String strDescomprometidoID = "";
		String strComando = "SELECT COALESCE(MAX(ID_DESCOMPROMETIDO),0) + 1  ID_DESCOMPROMETIDO ";
		strComando += "FROM mateo.CCP_DESCOMPROMETIDO ";
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next())
			strDescomprometidoID = rst.getString("ID_DESCOMPROMETIDO");
		rst.close();
		stmt.close();
		return strDescomprometidoID;

	}

	public int getDescomprometidoIDNum()throws Exception{
		int intDescomprometidoID = 0;
		String strComando = "SELECT COALESCE(MAX(ID_DESCOMPROMETIDO),0) + 1  ID_DESCOMPROMETIDO ";
		strComando += "FROM mateo.CCP_DESCOMPROMETIDO ";
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next())
			intDescomprometidoID = rst.getInt("ID_DESCOMPROMETIDO");
		rst.close();
		stmt.close();
		return intDescomprometidoID;

	}

	public void borrarDescomprometido(HttpSession session, String strAutorizacion, String strAutorizacionDet,
										String strCtaMayor, String strCCosto, String strAuxiliar)throws Exception{
		String strComando = "DELETE FROM mateo.CCP_DESCOMPROMETIDO ";
				strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
				strComando += "AND ID_AUTORIZACION_DET = '"+strAutorizacionDet+"' ";
				strComando += "AND ID_CCOSTO_M = '"+strCCosto+"' ";
				strComando += "AND ID_CTAMAYOR_M = '"+strCtaMayor+"' ";
				strComando += "AND ID_AUXILIAR_M = '"+strAuxiliar+"'";
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(strComando);
				stmt.close();
	}

	public void cambiaStatusDescomprometerDet(HttpSession session, String strAutorizacion, String strAutorizacionDet, String strStatus)throws Exception{
                String strComando = "UPDATE mateo.CCP_AUTORIZACION_DET  SET STATUS = '"+strStatus+"' ";
				strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
				strComando += "AND ID_AUTORIZACION_DET = '"+strAutorizacionDet+"'";
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(strComando);
				stmt.close();
	}

	public void cambiaStatusDescomprometerDet(HttpSession session, String strAutorizacion, String strStatus)throws Exception{

                String strComando = "UPDATE mateo.CCP_AUTORIZACION_DET  SET STATUS = '"+strStatus+"' ";
				strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(strComando);
				stmt.close();
	}

	public void cambiaStatusDescomprometerDetDP(HttpSession session, String strAutorizacion, String strStatus)throws Exception{
                String strComando = "UPDATE mateo.CCP_AUTORIZACION_DET  SET STATUS = '"+strStatus+"' ";
				strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
                                strComando += "AND STATUS = 'DP' ";
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(strComando);
				stmt.close();
	}

	public void cambiaStatusDescomprometerEnc(HttpSession session, String strAutorizacion, String strStatus)throws SQLException, Exception{
		String strComando = "UPDATE mateo.CCP_AUTORIZACION SET STATUS = '"+strStatus+"' ";
				strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"'";
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(strComando);
				stmt.close();
	}


	public boolean getDetallesComprometidos(HttpSession session, String strAutorizacion)throws Exception{
		int intRows = 0;
		String strComando = "SELECT COUNT(*) FROM  mateo.CCP_AUTORIZACION_DET ";
				strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
				strComando += "AND STATUS = 'R'";
				Statement stmt = conn.createStatement();
				ResultSet rst = stmt.executeQuery(strComando);
				if(rst.next())
					intRows = rst.getInt(1);
                                rst.close();
                                stmt.close();
				return (intRows > 0);
	}

	//funciones para descomprometido
	//para descomprometido contabilidad
	public void descomprometerMovimiento(HttpSession session, String strIdlibro, String strCCosto, String strFolio,
	        int intNumMov, String strCCostoM, String strCtaMayorM, String strAuxiliarM,
	        double dblImporte, String strAutorizacion, String strDescripcion)throws Exception{

                int intDescomprometidoID = 0;
		String strComando = "";
		intDescomprometidoID = getDescomprometidoIDNum();

		strComando = "INSERT INTO mateo.CCP_DESCOMPROMETIDO(ID_EJERCICIO, ID_AUTORIZACION, ID_LIBRO, ID_CCOSTO, ";
		strComando += "FOLIO, NUMMOVTO, ID_DESCOMPROMETIDO, FECHA, ID_CTAMAYOR_M, ID_CCOSTO_M, ID_AUXILIAR_M, ";
		strComando += "IMPORTE, DESCRIPCION, USUARIO, STATUS) ";
		strComando += "VALUES(?, ?, ?, ?, ?, ?, ?, to_date(?, 'dd-MM-yy'), ?, ?, ?, ?, ?, ?, 'A')";
        //System.out.println("descomprometerMovimiento "+strAutorizacion);
        PreparedStatement pstmt = conn.prepareStatement(strComando);
        pstmt.setString(1, (String)session.getAttribute("id_ejercicio"));
        pstmt.setString(2, strAutorizacion);
        pstmt.setString(3, strIdlibro);
        pstmt.setString(4, strCCosto);
        pstmt.setString(5, strFolio);
        pstmt.setInt(6, intNumMov);
        pstmt.setInt(7, intDescomprometidoID);
        pstmt.setString(8, getFechaActual(""));
        pstmt.setString(9, strCtaMayorM);
        pstmt.setString(10, strCCostoM);
        pstmt.setString(11, strAuxiliarM);
        pstmt.setDouble(12, dblImporte);
        pstmt.setString(13, strDescripcion);
        pstmt.setString(14, (String)session.getAttribute("login"));
		pstmt.execute();
        pstmt.close();

	}

	//comprometer movimiento
	//para contabilidad
	public void comprometerMovimiento(HttpSession session, String strIdlibro, String strCCosto,
										String strFolio, String strAutorizacion)throws Exception{
		//obtener datos del detalle de autorizacion
		//cambiar el status del encabezado a R
		if(!getDetallesComprometidos(session, strAutorizacion)) //si todos los movimientos estan descomprometidos se cambia el status del encabezado
			cambiaStatusDescomprometerEnc(session, strAutorizacion, "R");
		//cambiar el status del detalle a R
		//cambiaStatusDescomprometerDet(session, strAutorizacion, strAutorizacionDet, "R");
		//borrar los registros de CCP_DESCOMPROMETIDO relacionados con la autorizacion y el detalle
		//borrarDescomprometido(session, strAutorizacion, strAutorizacionDet, strCtaMayor, strCCosto, strAuxiliar);
	}

	//se llama a la funcion getDatosMovimiento contenida en poliza/metodos.jsp
	public void descomprometeMovimientosPoliza(HttpSession session, String strLibro, String strCCosto, String strFolio, String strDescripcion)throws Exception{
		double dblImporteMovimiento = 0;
		double dblCantidadPorDescomprometer = 0;
		int intNumMov = 0;
		String strReferencias = "";	//se hace provision para el caso de mas de una autorizacion de  control
		String strReferencia = ""; //para una autorizacion de  control
		String strCCostoM = "";
		String strCtaMayorM = "";
		String strAuxiliarM = "";
		PreparedStatement pstmt = metsPoliza.getDatosMovimiento((String)session.getAttribute("id_ejercicio"), strLibro, strCCosto, strFolio);
		ResultSet rst = pstmt.executeQuery();
		while(rst.next()){
			int intNumReferencias = 0;
			dblCantidadPorDescomprometer = 0;
			strCCostoM = rst.getString("ID_CCOSTOM"); //ponerle a ccosto
			strCtaMayorM = rst.getString("ID_CTAMAYORM"); //ponerle lo necesario
			strAuxiliarM = rst.getString("ID_AUXILIARM"); //lo necesario
			strReferencias = rst.getString("REFERENCIA");
			dblImporteMovimiento = rst.getDouble("IMPORTE");
			intNumMov = rst.getInt("NUMMOVTO");

			java.util.StringTokenizer strtReferencias = new java.util.StringTokenizer(strReferencias, ",");
			intNumReferencias = strtReferencias.countTokens();
			//si no hay tokens verificamos que la referncia solo contega un elemento
			if(intNumReferencias == 0){
				strReferencias.trim();
				if(strReferencias.length() > 0)
					intNumReferencias = 1;
			}
			//verificar que no este vacio el campo
			if(intNumReferencias > 0){
				//si es una sola referencia
				if(intNumReferencias == 1){//verificacion de caso "una referencia"
					strReferencia = strReferencias;
					if(esAutorizacionValida(session, strReferencia)){//checar que la referencia sea una autorizacion de control
						//verificar si existe la cuenta del movimiento en la tabla de autorizaciones
						if(existeCuenta(session, strReferencia, strCCostoM, strCtaMayorM, strAuxiliarM)){
							//se procede a verificar si el importe del movimiento
							//no sobrepasa lo que falta por descomprometer del detalle de la autorizacion
							dblCantidadPorDescomprometer = getCantidadPorDescomprometer(session, strReferencia, strCtaMayorM, strCCostoM, strAuxiliarM);
							if(dblCantidadPorDescomprometer < dblImporteMovimiento)
								throw new Error("el importe del movimiento es mayor"); //falta poner todos los datos
							//si todo va bien se descompromete la autorizacion
							descomprometerMovimiento(session, strLibro, strCCosto, strFolio, intNumMov, strCCostoM, strCtaMayorM, strAuxiliarM, dblImporteMovimiento, strReferencia, strDescripcion);
						}
						else{
							//si no se encuentra la cuenta se verifica si el importe del movimiento no
							//sobrepasa lo que falta por descomprometer del total de la autorizacion
							dblCantidadPorDescomprometer = getCantidadPorDescomprometer(session, strReferencia, "", "", "");
							if(dblCantidadPorDescomprometer < dblImporteMovimiento)
								throw new Error("el del movimiento es mayor"); //falta poner todos los datos
							//si todo va bien se descompromete la autorizacion
							descomprometerMovimiento(session, strLibro, strCCosto, strFolio, intNumMov, strCCostoM, strCtaMayorM, strAuxiliarM, dblImporteMovimiento, strReferencia, strDescripcion);
						}

					}//fin if checar autorizacion
				}//fin if verificacion de caso "una referencia"
				else{//caso multiples referencias
					double dblImporteTotal = 0;
					double dblImporteReferencia = 0;
					//utilizamos un vector para almacener temporalmente las autorizaciones
					//porque el objeto StringTokenizer no es rebobinable
					//se utilizan dos vectores uno para cada autorizacion y su importe
					//el otro para almacenar los vectores individuales
					java.util.Vector vctReferencias = new java.util.Vector();
					for(int i = 0; i < intNumReferencias; i++){
						java.util.Vector vctReferencia = new java.util.Vector();
						strReferencia = strtReferencias.nextToken();
						if(esAutorizacionValida(session, strReferencia)){//checar que la referencia sea una autorizacion de control
							//sumar cantidad de todos los controles
							if(existeCuenta(session, strReferencia, strCCostoM, strCtaMayorM, strAuxiliarM))
								dblImporteReferencia = getCantidadPorDescomprometer(session, strReferencia, strCtaMayorM, strCCostoM, strAuxiliarM);
							else
								dblImporteReferencia = getCantidadPorDescomprometer(session, strReferencia, "", "", "");
							dblImporteTotal += dblImporteReferencia;
							//se adiciona la autorizacion al objeto temporal
							vctReferencia.add(strReferencia);
							vctReferencia.add(new Double(dblImporteReferencia));
							vctReferencias.add(vctReferencia);
						}//fin if checar autorizacion
					}//fin for

					//aqui se debe verificar que la suma de lso importes de las autorizaciones no sobrepasen
					//al importe de movimiento
					if(dblImporteMovimiento < dblImporteTotal)
						throw new Error("detalles de la poliza y movimientos");
					//aqui se utiliza el vector
					java.util.Vector vctReferencia = new java.util.Vector();
					for(int i = 0; i < vctReferencias.size(); i++){
						vctReferencia = (java.util.Vector)vctReferencias.get(i);
						strReferencia = (String)vctReferencia.get(0);
						dblImporteReferencia = ((Double)vctReferencia.get(1)).doubleValue();
						//si no han habido problemas proceder a descomprometer el movimiento
						descomprometerMovimiento(session, strLibro, strCCosto, strFolio, intNumMov, strCCostoM, strCtaMayorM, strAuxiliarM, dblImporteReferencia, strReferencia, strDescripcion);
					}//fin segundo for
				}//fin else para multiples referencias
			}//fin if de verificacion de existencia de datos
		}//fin while
	}


	//se utiliza esta funcion para obtener el importe que falta por descomprometer con respecto a la autorizacion
	//esta funcion no esta ligada al id_autorizacion_det acumula por autorizacion y por cuenta
	public double getCantidadPorDescomprometer(HttpSession session, String strAutorizacion, String strCtaMayor, String strCCosto, String strAuxiliar)throws Exception{
		double dblImporte = 0;
		double dblImporteD = 0;
		String strComando = "SELECT COALESCE(SUM(IMPORTE),0) IMPORTE FROM  mateo.CCP_AUTORIZACION_DET ";
				strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
				if(!strCCosto.equals("") && !strCtaMayor.equals("") && strAuxiliar.equals("")){
					strComando += "AND ID_CCOSTO = '"+strCCosto+"' ";
					strComando += "AND ID_CTAMAYOR = '"+strCtaMayor+"' ";
					strComando += "AND ID_AUXILIAR = '"+strAuxiliar+"'";
				}
				Statement stmt = conn.createStatement();
				ResultSet rst = stmt.executeQuery(strComando);
				if(rst.next())
					dblImporte = rst.getDouble("IMPORTE");

				//ES NECESARIO QUE SE ADICIONE LA COLUMNA DE ID_AUTORIZACIONDET PARA HACER EL CALCULO CORRECTO
				strComando = "SELECT COALESCE(SUM(IMPORTE),0) FROM  CCP_DESCOMPROMETIDO ";
				strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
				if(!strCCosto.equals("") && !strCtaMayor.equals("") && strAuxiliar.equals("")){
					strComando += "AND ID_CCOSTO_M = '"+strCCosto+"' ";
					strComando += "AND ID_CTAMAYOR_M = '"+strCtaMayor+"' ";
					strComando += "AND ID_AUXILIAR_M = '"+strAuxiliar+"'";
				}
				rst = stmt.executeQuery(strComando);
				if(rst.next())
					dblImporteD = rst.getDouble(1);

				return (dblImporte - dblImporteD);
	}


	public boolean esAutorizacionValida(HttpSession session, String strAutorizacion)throws Exception{
		boolean boolEsAutorizacionValida = false;
		int intMedidaString = strAutorizacion.length(); //debe ser 6
		String strSubstring = strAutorizacion.substring(0, 2);

		//se verifica que cuente con las dos pricipales condiciones
		//que mida 6 caracteres y que empieze con "99"

		//validamos que empieze con "99"
		if(strSubstring.equals("99")){
			if(intMedidaString == 6){
				String strComando = "SELECT * FROM mateo.CCP_AUTORIZACION ";
						strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
						strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"'";
				Statement stmt = conn.createStatement();
				ResultSet rst = stmt.executeQuery(strComando);
				if(rst.next())
					boolEsAutorizacionValida = true;
				rst.close();
				stmt.close();
			}
			else
				throw new Error("hey que poner descripcion de error");
		}

		return boolEsAutorizacionValida;
	}

	//se verifica la existencia de la cuenta en la tabla CCP_AUTORIZACION_DET
	//nota: se toma por hecho que previamente se ha llamado a la funcion "esAutorizacionValida(session, strAutorizacion)"
	//para validar la autorizacion
	public boolean existeCuenta(HttpSession session, String strAutorizacion, String strCCosto,
									String strCtaMayor, String strAuxiliar)throws Exception{
		boolean boolExisteCuenta = false;
		String strComando = "SELECT * FROM mateo.CCP_AUTORIZACION_DET ";
				strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
				strComando += "AND ID_CCOSTO = '"+strCCosto+"' ";
				strComando += "AND ID_CTAMAYOR = '"+strCtaMayor+"' ";
				strComando += "AND ID_AUXILIAR = '"+strAuxiliar+"'";
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next())
			boolExisteCuenta = true;
		rst.close();
		stmt.close();
		return boolExisteCuenta;
	}


//METODOS UTILIZADOS EN LOS REPORTES DE PRESUPUESTO
//POR BLOQUES
	public ResultSet getCuentasConPresupuestoOperativo(String strIDEjercicio, String strCCosto)throws Exception{
			String sSql = "SELECT NOMBRE, ID_CTAMAYOR, AUXILIAR ";
			sSql += "FROM mateo.CONT_CTAMAYOR ";
			sSql += "WHERE ID_EJERCICIO = ? ";
			sSql += "AND ID_CTAMAYOR IN ";
			sSql += "(SELECT ID_CTAMAYOR ";
			sSql += "FROM mateo.CONT_RELACION ";
			sSql += "WHERE ID_EJERCICIO = ? ";
			sSql += "AND ID_CCOSTO = ?) ";
			sSql += "AND (ID_CTAMAYOR like '2.4%' ";
			sSql += "OR ID_CTAMAYOR like '2.5%') ";
			sSql += "AND DETALLE = 'S' ";
			sSql += "ORDER BY TIPO_CUENTA, ID_CTAMAYOR";
			PreparedStatement pstmt = conn.prepareStatement(sSql);
			pstmt.setString(1, strIDEjercicio);
			pstmt.setString(2, strIDEjercicio);
			pstmt.setString(3, strCCosto);
			return (pstmt.executeQuery());
	}


	public double getPresupuestoRangoMeses(String strIDEjercicio, String strIDCtaMayor,
	 					String strIDCcosto, int intMesIni, int intMesFin) throws Exception{
		double dblCantidad = 0;
		String strComando = "SELECT COALESCE(SUM(IMPORTE),0) AS IMPORTE FROM mateo.CCP_PRESUPUESTO ";
			strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
			strComando += "AND ID_CTAMAYOR = '"+strIDCtaMayor+"' ";
			strComando += "AND ID_CCOSTO = '"+strIDCcosto+"' ";
			strComando += "AND MES >= "+intMesIni+" ";
			strComando += "AND MES <= "+intMesFin;
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next()){
			dblCantidad = rst.getDouble("IMPORTE");
		}
		rst.close();
		stmt.close();
		return dblCantidad;
	}

	public double getGastos(String strIDEjercicio, String strIDCtaMayor, String strIDCcosto, String strFechaInicial, String strFechaFinal)throws Exception{
		double dblCredito = 0;
		double dblCargo = 0;
		dblCargo = metsPoliza.getTotalCargos(strIDEjercicio, strIDCtaMayor, strIDCcosto, "0000000", strFechaInicial, strFechaFinal);
		dblCredito = metsPoliza.getTotalCreditos(strIDEjercicio, strIDCtaMayor, strIDCcosto, "0000000", strFechaInicial, strFechaFinal);
		return (dblCargo - dblCredito);
	}

//REPORTES

	public PreparedStatement getEntidadesConPresupuesto(HttpSession session)throws Exception{
		String strComando = "SELECT DISTINCT(C.ID_CCOSTO),C.NOMBRE ";
				strComando += "FROM mateo.CONT_CCOSTO C, mateo.CCP_PRESUPUESTO P ";
				strComando += "WHERE P.ID_CCOSTO = C.ID_CCOSTO ";
				strComando += "AND P.ID_EJERCICIO = C.ID_EJERCICIO ";
				strComando += "AND C.ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND C.DETALLE = 'S'";
		PreparedStatement pstmt = conn.prepareStatement(strComando);

		return pstmt;
	}

	public double getCuentasImporteAcumulado(HttpSession session, String strCCosto, int intMedidaSubstr,
												String strPatronInicial, String strPatronFinal)throws Exception{//acumulado por tipos
		double dblCantidad = 0;
		String strComando = "SELECT P.ID_EJERCICIO, COALESCE(SUM(P.IMPORTE),0) ";
				strComando += "FROM 	mateo.CCP_PRESUPUESTO P, mateo.CONT_CTAMAYOR M ";
				strComando += "WHERE 	P.ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND	M.TIPO_CUENTA = 'R' ";
				strComando += "AND	M.DETALLE = 'S' ";
				strComando += "AND	M.ID_CTAMAYOR = P.ID_CTAMAYOR ";
				strComando += "AND	P.ID_CCOSTO = '"+strCCosto+"' ";
				strComando += "AND	SUBSTR(M.ID_CTAMAYOR,0,"+intMedidaSubstr+") BETWEEN '"+strPatronInicial+"' AND '"+strPatronFinal+"' ";
				strComando += "AND	M.ID_EJERCICIO = P.ID_EJERCICIO ";
				strComando += "GROUP BY P.ID_EJERCICIO";
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next())
			dblCantidad = rst.getDouble(2);
		rst.close();
		stmt.close();
		return dblCantidad;
	}

	public double getCuentasImporteAcumulado(HttpSession session, String strCCosto, int intMedidaSubstr,
                String strPatronInicial, String strPatronFinal, String strMesI, String strMesF)throws Exception{//acumulado por tipos
		double dblCantidad = 0;
		String strComando = "SELECT P.ID_EJERCICIO, COALESCE(SUM(P.IMPORTE),0) ";
				strComando += "FROM 	mateo.CCP_PRESUPUESTO P, mateo.CONT_CTAMAYOR M ";
				strComando += "WHERE 	P.ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
				strComando += "AND	M.TIPO_CUENTA = 'R' ";
				strComando += "AND	M.DETALLE = 'S' ";
				strComando += "AND	M.ID_CTAMAYOR = P.ID_CTAMAYOR ";
				strComando += "AND	P.ID_CCOSTO = '"+strCCosto+"' ";
				strComando += "AND	SUBSTR(M.ID_CTAMAYOR,0,"+intMedidaSubstr+") BETWEEN '"+strPatronInicial+"' AND '"+strPatronFinal+"' ";
				strComando += "AND	M.ID_EJERCICIO = P.ID_EJERCICIO ";
                                strComando += "AND      P.MES BETWEEN '"+strMesI+"' AND '"+strMesF+"' ";
				strComando += "GROUP BY P.ID_EJERCICIO";
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next())
			dblCantidad = rst.getDouble(2);
		rst.close();
		stmt.close();
		return dblCantidad;
	}

	public PreparedStatement getPresupuesto(String strIDEjercicio, String strCCosto, String mes1, String mes2) throws Exception{
			String  strComando =  "select id_ccosto, ID_CTAMAYOR, id_auxiliar, COALESCE(sum(importe),0) IMPORTE from MATEO.CCP_PRESUPUESTO ";
			strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
			strComando += "AND MES BETWEEN '"+mes1+"' AND '"+mes2+"' ";
			strComando += "group by ID_CCOSTO, ID_CTAMAYOR, id_auxiliar ";
			strComando += "HAVING ID_CCOSTO = '"+strCCosto+"' ";
			strComando += "AND (ID_CTAMAYOR LIKE '2.4.%' ";
			strComando += "OR ID_CTAMAYOR LIKE '2.5.%' ";
			strComando += "OR ID_CTAMAYOR IN ('2.3.18') ";
                        //09-jul-2012 - El CP Arturo Sebastian, solicito que ahora todas las escuelas de FACSA muestren estas cuentas
                        strComando += "OR (ID_CTAMAYOR =any ('2.3.18','2.3.19','2.3.20') AND ID_CCOSTO like ('1.01.2.03%'))) ";
                        //strComando += "OR (ID_CTAMAYOR IN ('2.3.19','2.3.20') AND ID_CCOSTO = '1.01.2.03.01')) ";
			strComando += "ORDER BY ID_CTAMAYOR";

			PreparedStatement pstmt = conn.prepareStatement(strComando);

			return pstmt;
	}

	public double getGastosMesActual(String strIDEjercicio, String strIDCtaMayor, String strIDCcosto, String auxiliarId, String strFechaInicial, String strFechaFinal) throws Exception{
			double dblCantidad = 0;
			String strComando = "SELECT COALESCE(SUM(IMPORTE*CASE NATURALEZA WHEN 'C' THEN -1 ELSE 1 END),0) IMPORTE ";
			strComando += "FROM MATEO.CONT_MOVIMIENTO ";
			strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
			strComando += "AND ID_CCOSTOM = '"+strIDCcosto+"' ";
			strComando += "AND ID_CTAMAYORM = '"+strIDCtaMayor+"' ";
                        strComando += "AND ID_AUXILIARM = '"+auxiliarId+"' ";
			strComando += "AND FECHA BETWEEN to_date('"+strFechaInicial+"', 'dd/mm/yy') AND to_date('"+strFechaFinal+"', 'dd/mm/yy') ";

            Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(strComando);
			while(rst.next()){
					dblCantidad = rst.getDouble("IMPORTE");
			}
			rst.close();
			stmt.close();

			//System.out.println("GastosMesActual "+strIDEjercicio+"@"+strIDCtaMayor+"@"+strIDCcosto+"@"+strFechaInicial+"@"+strFechaFinal+"@"+dblCantidad);

			return dblCantidad;
	}

	public double getComprometido(String strIDEjercicio, String strIDCtaMayor, String strIDCcosto, String strFechaFinal)throws Exception{
			double dblCantidadComprometida = 0, dblCantidadDesComprometida = 0;
			String strComando = "SELECT COALESCE(SUM(PRECIO_U * CANTIDAD),0) AS IMPORTE ";
            strComando += "FROM mateo.CCP_AUTORIZACION_DET ";
            strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
            strComando += "AND ID_CTAMAYOR = '"+strIDCtaMayor+"' ";
			strComando += "AND ID_AUTORIZACION IN ";
            strComando += "( ";
            strComando += "SELECT ID_AUTORIZACION ";
            strComando += "FROM mateo.CCP_AUTORIZACION ";
			strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
			strComando += "AND ID_CCOSTO = '"+strIDCcosto+"' ";
            strComando += "AND STATUS = 'R' ";
			strComando += "AND FECHA <= to_date('"+strFechaFinal+"', 'dd/mm/yy') ";
            strComando += ") ";

			Statement stmt = conn.createStatement();
			ResultSet rst = stmt.executeQuery(strComando);
			if(rst.next()){
					dblCantidadComprometida = rst.getDouble("IMPORTE");
			}
            rst.close();

            strComando = "SELECT COALESCE(SUM(IMPORTE),0) AS IMPORTE FROM CCP_DESCOMPROMETIDO ";
            strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
            strComando += "AND FECHA <= to_date('"+strFechaFinal+"', 'dd/mm/yy') ";
            strComando += "AND ID_AUTORIZACION IN ";
            strComando += "( ";
            strComando += "SELECT ID_AUTORIZACION ";
            strComando += "FROM CCP_AUTORIZACION_DET ";
            strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
            strComando += "AND ID_CTAMAYOR = '"+strIDCtaMayor+"' ";
            strComando += "AND ID_AUTORIZACION IN ";
            strComando += "( ";
            strComando += "SELECT ID_AUTORIZACION ";
            strComando += "FROM CCP_AUTORIZACION ";
            strComando += "WHERE ID_EJERCICIO = '"+strIDEjercicio+"' ";
            strComando += "AND ID_CCOSTO = '"+strIDCcosto+"' ";
            strComando += "AND STATUS = 'R' ";
            strComando += "AND FECHA <= to_date('"+strFechaFinal+"', 'dd/mm/yy') ";
            strComando += ") ";
            strComando += ") ";
            strComando += "AND STATUS = 'A' ";
			rst = stmt.executeQuery(strComando);
			if(rst.next()){
					dblCantidadDesComprometida = rst.getDouble("IMPORTE");
			}

            rst.close();
            stmt.close();

			return (dblCantidadComprometida - dblCantidadDesComprometida );
	}

	public PreparedStatement getCComprometido(String strIDEjer, String strCCosto, String strFechai, String strFechaf)throws Exception{
		String strComando = "SELECT ID_AUTORIZACION, FECHA, NO_DOC, CLAVEEMPLEADO, PROVEEDOR, SERVICIO, USUARIO FROM mateo.CCP_AUTORIZACION ";
			strComando += "WHERE ID_EJERCICIO = '"+strIDEjer+"' ";
			strComando += "AND ID_CCOSTO = '"+strCCosto+"' ";
                        strComando += "AND FECHA BETWEEN TO_DATE('"+strFechai+"','dd/MM/yyyy') AND TO_DATE('"+strFechaf+"','dd/MM/yyyy') ";
			strComando += "AND STATUS = 'R'";
			PreparedStatement stmt = conn.prepareStatement(strComando);

			return stmt;
	}

	public PreparedStatement getCComprometidoDet(String strIDEjer, String strCCosto, String auxiliarId, String strAutorizacion)throws Exception
	{
	 String strComando = "SELECT * FROM mateo.CCP_AUTORIZACION_DET ";
		 	strComando += "WHERE ID_EJERCICIO = '"+strIDEjer+"' ";
			strComando += "AND ID_CCOSTO = '"+strCCosto+"' ";
                        strComando += "AND ID_AUXILIAR = '"+auxiliarId+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
//			strComando += "AND STATUS = 'R' ";

			PreparedStatement stmt = conn.prepareStatement(strComando,ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            return stmt;
	}

	public PreparedStatement getAutorizacionesDetP(String strAutorizacion, String strStatus, String strIDEjer, String strCCosto)throws Exception{
		String strComando = "SELECT  * FROM mateo.CCP_AUTORIZACION_DET ";
			strComando += "WHERE ID_EJERCICIO = '"+strIDEjer+"' ";
			strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"' ";
			strComando += "AND ID_CCOSTO = '"+strCCosto+"' ";
			strComando += "AND STATUS = '"+strStatus+"'";
			PreparedStatement pstmt = conn.prepareStatement(strComando);

			return pstmt;
	}

	public PreparedStatement getCComprometido(String strStatus, String strIDEjer, String strCCosto)throws Exception{
		String strComando = "SELECT * FROM mateo.CCP_AUTORIZACION ";
			strComando += "WHERE ID_EJERCICIO = '"+strIDEjer+"' ";
			strComando += "AND ID_CCOSTO = '"+strCCosto+"' ";
			strComando += "AND STATUS = '"+strStatus+"'";
			PreparedStatement pstmt = conn.prepareStatement(strComando);

			return pstmt;
	}

        //M?todos para el descomprometido autom?tico
        public void ccpDescomprometerAut
        (
                String strIDLibro, String strFolio, Integer intNumMovto, String strFecha,
                String strCtaMayorM, String strCCostoM, String strAuxiliarM, Double dblImporte,
                String strDescripcion, String strReferencia, HttpSession session
        ) throws SQLException, Exception
        {
                //El parametro autorizacion, puede contener varios numeros de control separados por comas
                //El centro de costo (contabilidad) y el a?o contable, se encuentran en la session
                //Los parametros terminados en M, contienen la cuenta contable afectada

                //Desmembrar autorizaciones
                java.util.StringTokenizer strTkn = new java.util.StringTokenizer(strReferencia, ",");

                //Vector para guardar folios
                java.util.Vector vctAutorizacion = new java.util.Vector();
                Integer intTkn = new Integer(strTkn.countTokens());

                while(strTkn.hasMoreTokens())
                {
                        vctAutorizacion.add(strTkn.nextToken());
                }

                //Validar que cada una de las autorizaciones sea v?lida
                if (!ccpsonAutorizacionesValidas(vctAutorizacion, session))
                {
                        throw new Exception("Folios de ?rdenes de compra/servicio no son v?lidos");
                }

                //Validar que sumatoria de controles sea menor o igual que importe
                //Obtener saldo autorizado de todos los controles
                Double dblAutorizacion = ccpgetSumatoriaAutorizacion(vctAutorizacion, session);
                //Obtener saldo descomprometido de todos los controles
                Double dblAutorizacionD = ccpgetSumatoriaAutorizacionD(vctAutorizacion, session);

                //Saldo disponible en las autorizaciones
                //Al saldo le sumamos el limite de sobregiro multiplicado por el numero de controles
                Double dblSaldo = new Double(dblAutorizacion.doubleValue() - dblAutorizacionD.doubleValue() +
                        (ccpgetLimiteSobregiro().doubleValue() * intTkn.doubleValue()));
                //System.out.println(dblSaldo+", "+dblAutorizacion+", "+dblAutorizacionD+", "+ccpgetLimiteSobregiro()+", "+intTkn);
                if (dblSaldo.compareTo(dblImporte) < 0)
                {
                        throw new Exception("Importe gastado"+dblImporte+" es mayor que importe autorizado "+dblSaldo);
                }

                //Grabar movimientos en CCP_DESCOMPROMETIDO
                ccpDescomprometidoAlta(strIDLibro, strFolio, intNumMovto, strFecha, strCtaMayorM, strCCostoM, strAuxiliarM,
                        dblImporte, strDescripcion, vctAutorizacion, session);
        }

        public boolean ccpsonAutorizacionesValidas
        (
                java.util.Vector vctAutorizacion, HttpSession session
        ) throws SQLException, Exception
        {
                java.util.Enumeration enAutorizacion = vctAutorizacion.elements();

                while (enAutorizacion.hasMoreElements())
                {
                        Integer intAutorizacion = new Integer(0);
                        String strAutorizacion = ((String)enAutorizacion.nextElement()).trim();

                        String COMANDO = "SELECT COUNT(*) AUTORIZACION ";
                        COMANDO += "FROM mateo.CCP_AUTORIZACION ";
                        COMANDO += "WHERE ID_EJERCICIO = ? ";
                        COMANDO += "AND ID_AUTORIZACION = ? ";
                        PreparedStatement pstmt = conn.prepareStatement(COMANDO);
                        pstmt.setString(1, (String)session.getAttribute("id_ejercicio"));
                        pstmt.setString(2, strAutorizacion);
                        ResultSet rset = pstmt.executeQuery();

                        if (rset.next())
                        {
                                intAutorizacion = new Integer(rset.getInt("Autorizacion"));
                        }

                        pstmt.close();
                        rset.close();

                        if (intAutorizacion.compareTo(new Integer(0)) == 0)
                        {
                                throw new Exception("Autorizacion "+strAutorizacion+" no es v?lida!");
                        }
                }

                return true;
        }

        public Double ccpgetSumatoriaAutorizacion
        (
                java.util.Vector vctAutorizacion, HttpSession session
        ) throws SQLException, Exception
        {
                java.util.Enumeration enAutorizacion = vctAutorizacion.elements();
                Double dblTotal = new Double(0);

                while(enAutorizacion.hasMoreElements())
                {
                        String strAutorizacion = ((String)enAutorizacion.nextElement()).trim();
                        dblTotal = new Double(dblTotal.doubleValue() +
                                getImporteTotalAutorizacionPesos(session, strAutorizacion));
                }
                return dblTotal;
        }

        //Obtiene sumatoria de movimientos descomprometidos
        public Double ccpgetSumatoriaAutorizacionD
        (
                java.util.Vector vctAutorizacion, HttpSession session
        ) throws SQLException, Exception
        {
                java.util.Enumeration enAutorizacion = vctAutorizacion.elements();
                Double dblTotal = new Double(0);

                while(enAutorizacion.hasMoreElements())
                {
                        String strAutorizacion = ((String)enAutorizacion.nextElement()).trim();
                        dblTotal = new Double(dblTotal.doubleValue() +
                                getImporteTotalAutorizacionD(session, strAutorizacion));
                }
                return dblTotal;
        }

        //Obtiene saldo descomprometido en una autorizacion
        public double getImporteTotalAutorizacionD
        (
                HttpSession session, String strAutorizacion
        )throws SQLException, Exception
        {
		double dblImporte = 0;
		String strComando = "SELECT  COALESCE(SUM(CANTIDAD*PRECIO_U),0) ";
                strComando += "FROM mateo.CCP_AUTORIZACION_DET ";
		strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
		strComando += "AND ID_AUTORIZACION = '"+strAutorizacion+"'";
                strComando += "AND SUBSTR(STATUS,1,1) = 'D' ";
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next())
		        dblImporte = rst.getDouble(1);
                stmt.close();
                rst.close();

                return dblImporte;
	}

        public void ccpDescomprometidoAlta
        (
                String strIDLibro, String strFolio, Integer intNumMovto, String strFecha,
                String strCtaMayorM, String strCCostoM, String strAuxiliarM, Double dblImporte,
                String strDescripcion, java.util.Vector vctAutorizacion, HttpSession session
        ) throws SQLException, Exception
        {
                //No tenemos ninguna preferencia por folio alguno, as? que los leemos en el orden
                //en que se encuentran en el vector
                java.util.Enumeration enAutorizacion = vctAutorizacion.elements();


                while(enAutorizacion.hasMoreElements() && dblImporte.compareTo(new Double(0)) >= 0)
                {
                        String strAutorizacion = ((String)enAutorizacion.nextElement()).trim();

                        //Obtener saldo disponible en autorizacion
                        Double dblTotal = new Double(getImporteTotalAutorizacion(session, strAutorizacion));
                        Double dblTotalD = new Double(getImporteTotalAutorizacionD(session, strAutorizacion));

                        //Al saldo se le suma el limite de sobregiro
                        Double dblSaldo = new Double(dblTotal.doubleValue() - dblTotalD.doubleValue() + ccpgetLimiteSobregiro().doubleValue());

                        //Evaluar qu? cantidad se descomprometer? en autorizacion
                        //Si el gasto es menor que el saldo de la orden autorizada
                        if (dblSaldo.compareTo(dblImporte) >= 0)
                        {
                                dblSaldo = new Double(dblImporte.doubleValue());
                                dblImporte = new Double(-1);
                        }
                        //Si el gasto es mayor que la orden autorizada
                        else if (dblSaldo.compareTo(dblImporte) < 0)
                        {
                                //Restamos al gasto el importe descomprometido
                                dblImporte = new Double(dblImporte.doubleValue() - dblSaldo.doubleValue());
                        }

                        //Generar registro en descomprometido
                        String strIDEjercicio = (String)session.getAttribute("id_ejercicio");

                        //Aqu? se utiliza la variable id_ccosto y no id_ccosto_ccp
                        //para obtener el dato del capturista de la poliza
                        String strIDCCosto = (String)session.getAttribute("id_ccosto");
                        descomprometerMovimiento(session, strIDLibro, strIDCCosto, strFolio,
                                intNumMovto.intValue(), strCCostoM, strCtaMayorM, strAuxiliarM,
                                dblSaldo.doubleValue(), strAutorizacion, strDescripcion);

                        //Obtener saldo disponible en autorizacion
                        dblSaldo = new Double(dblTotal.doubleValue() - dblTotalD.doubleValue() - dblSaldo.doubleValue());

                        //Si el saldo es mayor que cero, no se hace nada
                        //Si el saldo es igual a cero, se descompromete
                        //Si el saldo es menor que cero, indica que el gasto fue mayor que lo autorizado.  En tal caso,
                        //la diferencia no debe de ser mayor que el parametro limite de sobregiro
                        if (dblSaldo.compareTo(new Double(0)) == 0)
                        {
                                //Modificamos el status del encabezado
                                cambiaStatusDescomprometerEnc(session, strAutorizacion, "D");
                                //Modificamos el status del detalle, indicando la P que se modifico desde polizas
                                cambiaStatusDescomprometerDet(session, strAutorizacion, "DP");
                        }
                        //Si el saldo es negativo y la suma del saldo y el limite de sobregiro es positivo,
                        //se descompromete.
                        else if (dblSaldo.compareTo(new Double(0)) < 0 &&
                                new Double(ccpgetLimiteSobregiro().doubleValue() + dblSaldo.doubleValue()).compareTo(new Double(0)) >= 0)
                        {
                                //Modificamos el status del encabezado
                                cambiaStatusDescomprometerEnc(session, strAutorizacion, "D");
                                //Modificamos el status del detalle, indicando la P que se modifico desde polizas
                                cambiaStatusDescomprometerDet(session, strAutorizacion, "DP");
                        }
                }
        }

        public void ccpDescomprometerBorraAut
        (
                String strIDLibro, String strFolio, Integer intNumMovto, HttpSession session
        ) throws SQLException, Exception
        {
                String strAutorizacion = "";

                //Obtener folio de autorizacion
                String COMANDO = "SELECT ID_AUTORIZACION ";
                COMANDO += "FROM mateo.CCP_DESCOMPROMETIDO ";
                COMANDO += "WHERE ID_EJERCICIO = ? ";
                COMANDO += "AND ID_LIBRO = ? ";
                COMANDO += "AND ID_CCOSTO = ? ";
                COMANDO += "AND FOLIO = ? ";
                COMANDO += "AND NUMMOVTO = ? ";
                PreparedStatement pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, (String)session.getAttribute("id_ejercicio"));
                pstmt.setString(2, strIDLibro);

                //Aqu? se utiliza la variable id_ccosto y no id_ccosto_ccp,
                //para utilizar la contabilidad del capturista de la poliza
                pstmt.setString(3, (String)session.getAttribute("id_ccosto"));
                pstmt.setString(4, strFolio);
                pstmt.setInt(5, intNumMovto.intValue());
                ResultSet rset = pstmt.executeQuery();

                while (rset.next())
                {
                        strAutorizacion = rset.getString("ID_Autorizacion");

                        //Borrar movimiento de descomprometido
                        COMANDO = "UPDATE mateo.CCP_DESCOMPROMETIDO ";
                        COMANDO += "SET DESCRIPCION = 'CANCELADO POR ' || ?  || 'EN ' || TO_CHAR(SYSDATE,'DD/MM/YY'), ";
                        COMANDO += "IMPORTE = 0, STATUS = 'I' ";
                        COMANDO += "WHERE ID_EJERCICIO = ? ";
                        COMANDO += "AND ID_LIBRO = ? ";
                        COMANDO += "AND ID_CCOSTO = ? ";
                        COMANDO += "AND FOLIO = ? ";
                        COMANDO += "AND NUMMOVTO = ? ";
                        COMANDO += "AND STATUS = 'A' ";
                        pstmt = conn.prepareStatement(COMANDO);
                        pstmt.setString(1, (String)session.getAttribute("login"));
                        pstmt.setString(2, (String)session.getAttribute("id_ejercicio"));
                        pstmt.setString(3, strIDLibro);

                        //Aqu? se utiliza id_ccosto y no id_ccosto_ccp,
                        //para obtener la contabilidad del capturista
                        pstmt.setString(4, (String)session.getAttribute("id_ccosto"));
                        pstmt.setString(5, strFolio);
                        pstmt.setInt(6, intNumMovto.intValue());
                        pstmt.execute();
                        pstmt.close();

                        //Modificar el status de la autorizacion a R, ya que si ten?a D indicando
                        //que ya estaba totalmente descomprometida, ahora se desajusto
                        cambiaStatusDescomprometerEnc(session, strAutorizacion, "R");
                        cambiaStatusDescomprometerDetDP(session, strAutorizacion, "R");
                }
                pstmt.close();
                rset.close();
        }

        public Double ccpgetLimiteSobregiro() throws SQLException
        {
                Double dblLimiteSobregiro = new Double(0);

                String COMANDO = "SELECT LIMITE_SOBREGIRO ";
                COMANDO += "FROM mateo.CCP_PARAMGRAL ";
                PreparedStatement pstmt = conn.prepareStatement(COMANDO);
                ResultSet rset = pstmt.executeQuery();

                if (rset.next())
                {
                        dblLimiteSobregiro = new Double(rset.getDouble("Limite_Sobregiro"));
                }
                pstmt.close();
                rset.close();

                return dblLimiteSobregiro;
        }

        //Metodo que regresa los centros de costo a los que tiene acceso un empleado
        public PreparedStatement ccpgetCCostosEmp
        (
                HttpSession session
        ) throws SQLException, Exception
        {
                String strComando = "SELECT ID_EJERCICIO, ID_CCOSTO FROM mateo.CCP_EMPLEADO_CCOSTO ";
                strComando += "WHERE CLAVEEMPLEADO IN ";
                strComando += "(SELECT CODIGO_PERSONAL FROM NOE.DATOS_PERSONALES ";
                strComando += " WHERE LOGIN = '"+((String)session.getAttribute("login")).toUpperCase()+"')";
                strComando += " AND ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                PreparedStatement pstmt = conn.prepareStatement(strComando);
                return pstmt;
        }

	public PreparedStatement getCComprometidoDet(String strIDEjer, String strCCosto, String strIDAutorizacion, String strFechai, String strFechaf)throws Exception
	{
                String strComando = "SELECT D.ID_AUTORIZACION, D.ID_CTAMAYOR, D.ID_CCOSTO, D.CANTIDAD, D.DESCRIPCION, D.PRECIO_U, D.STATUS, A.FECHA, A.NO_DOC, A.PROVEEDOR ";
	        strComando += "FROM mateo.CCP_AUTORIZACION_DET D, mateo.ccp_autorizacion A ";
	        strComando += "WHERE A.ID_EJERCICIO = D.ID_EJERCICIO ";
		strComando += "AND A.ID_AUTORIZACION = D.ID_AUTORIZACION ";
		strComando += "AND D.ID_EJERCICIO = '"+strIDEjer+"' ";
		strComando += "AND D.ID_CCOSTO = '"+strCCosto+"' ";
                strComando += "AND A.ID_AUTORIZACION = '"+strIDAutorizacion+"' ";
		strComando += "AND A.FECHA BETWEEN TO_DATE('"+strFechai+"', 'DD/MM/YYYY') AND TO_DATE('"+strFechaf+"', 'DD/MM/YYYY') ";

		PreparedStatement pstmt = conn.prepareStatement(strComando);
		return pstmt;
	}

	public double getCDescomprometido(String strIDEjer, String strCCosto, String strFechai, String strFechaf)throws Exception
	{
	        double dblCantidad=0;
                String strComando = "SELECT COALESCE(SUM(IMPORTE),0) as IMPORTE ";
	        strComando += "from MATEO.CCP_DESCOMPROMETIDO ";
                strComando += "WHERE ID_EJERCICIO = '"+strIDEjer+"' ";
		strComando += "AND ID_CCOSTO = '"+strCCosto+"' ";
		strComando += "AND FECHA BETWEEN TO_DATE('"+strFechai+"', 'DD/MM/YYYY') AND TO_DATE('"+strFechaf+"', 'DD/MM/YYYY') ";
		strComando += "AND STATUS = 'A'";
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next()){
			dblCantidad = rst.getDouble("IMPORTE");
		}
		rst.close();
		stmt.close();
		return dblCantidad;
	}

        //Obtiene autorizaciones descomprometidas parcialmente
        public PreparedStatement ccpgetAutorizacionesDP
        (
                HttpSession session, String strFechaInicial, String strFechaFinal
        )throws SQLException, Exception
        {
                String strComando = "SELECT * ";
                strComando += "FROM mateo.CCP_AUTORIZACION ";
		strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                strComando += "AND ID_CCOSTO = '"+(String)session.getAttribute("id_ccosto_ccp")+"' ";
                strComando += "AND STATUS = 'R' ";
                strComando += "AND ID_AUTORIZACION IN ";
                strComando += "( ";
                strComando += "SELECT ID_AUTORIZACION ";
                strComando += "FROM mateo.CCP_DESCOMPROMETIDO ";
                strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                strComando += "AND FECHA BETWEEN to_date('"+strFechaInicial+"', 'dd/mm/yy') ";
                strComando += "AND to_date('"+strFechaFinal+"', 'dd/mm/yy') ";
                strComando += "AND STATUS = 'A' ";
                strComando += "AND ID_AUTORIZACION IN ";
                strComando += "( ";
                strComando += "SELECT ID_AUTORIZACION ";
                strComando += "FROM mateo.CCP_AUTORIZACION ";
	        strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
	        strComando += "AND ID_CCOSTO = '"+(String)session.getAttribute("id_ccosto_ccp")+"' ";
                strComando += "AND STATUS = 'R' ";
                strComando += ") ";
                strComando += ") ";
                strComando += " ORDER BY ID_AUTORIZACION ";
		PreparedStatement pstmt = conn.prepareStatement(strComando);
                return pstmt;
	}

        //Obtiene autorizaciones sin importe alguno descomprometido
        public PreparedStatement ccpgetAutorizacionesNoDP
        (
                HttpSession session, String strFechaInicial, String strFechaFinal, String strStatus
        )throws SQLException, Exception
        {
                PreparedStatement pstmt = null;

                //Si se quieren las ordenes autorizadas, entonces se tiene que validar
                //que no tengan descomprometido ning?n impote.
                //O si se quieren ordenes autorizadas no por el usuario actual

                if (strStatus.equals("R") || strStatus.equals("NU"))
                {
                        String strComando = "SELECT * ";
                        strComando += "FROM mateo.CCP_AUTORIZACION ";
                        strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                        strComando += "AND ID_CCOSTO = '"+(String)session.getAttribute("id_ccosto_ccp")+"' ";
                        strComando += "AND STATUS = 'R' ";

                        //Si quieren ordenes autorizadas no por el usuario actual
                        if (strStatus.equals("NU"))
                        {
                                strComando += "AND USUARIO_AUT_RECH != '"+(String)session.getAttribute("login")+"' ";
                        }

                        strComando += "AND ID_AUTORIZACION NOT IN ";
                        strComando += "( ";
                        strComando += "SELECT ID_AUTORIZACION ";
                        strComando += "FROM mateo.CCP_DESCOMPROMETIDO ";
                        strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                        strComando += "AND FECHA BETWEEN to_date('"+strFechaInicial+"', 'dd/mm/yy') ";
                        strComando += "AND to_date('"+strFechaFinal+"', 'dd/mm/yy') ";
                        strComando += "AND STATUS = 'A' ";
                        strComando += "AND ID_AUTORIZACION IN ";
                        strComando += "( ";
                        strComando += "SELECT ID_AUTORIZACION ";
                        strComando += "FROM mateo.CCP_AUTORIZACION ";
                        strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                        strComando += "AND ID_CCOSTO = '"+(String)session.getAttribute("id_ccosto_ccp")+"' ";
                        strComando += "AND STATUS = 'R' ";

                        //Si quieren ordenes autorizadas no por el usuario actual
                        if (strStatus.equals("NU"))
                        {
                                strComando += "AND USUARIO_AUT_RECH != '"+(String)session.getAttribute("login")+"' ";
                        }

                        strComando += ") ";
                        strComando += ") ";
                        strComando += " ORDER BY ID_AUTORIZACION ";

                        pstmt = conn.prepareStatement(strComando);
                }
                else
                {
                        pstmt = getAutorizacionesEnc(session, strStatus);
                }

                return pstmt;
	}

        public Map <String, Double> ccpgetDescomprometidoAutomatico(HttpSession session) throws SQLException
        {
                double dblComprometido = 0;
                Map<String, Double>  mReturn = new TreeMap <String, Double> ();

                String strComando = "SELECT ID_AUTORIZACION, COALESCE(SUM(IMPORTE),0) AS IMPORTE FROM mateo.CCP_DESCOMPROMETIDO ";
                strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                strComando += "AND STATUS = 'A' ";
                strComando += "AND FOLIO IS NOT NULL ";
                strComando += "GROUP BY ID_AUTORIZACION ";
                Statement stmt = conn.createStatement();
                ResultSet rst = stmt.executeQuery(strComando);
                while(rst.next())
                {
                        dblComprometido = rst.getDouble("Importe");
                        mReturn.put(rst.getString("id_autorizacion"), dblComprometido);
                }
                stmt.close();
                rst.close();

                return mReturn;
        }

        public Map <String, Double> ccpgetDescomprometidoManual(HttpSession session) throws SQLException
        {
                double dblComprometido = 0;
                Map<String, Double>  mReturn = new TreeMap <String, Double> ();

                String strComando = "SELECT ID_AUTORIZACION, COALESCE(SUM(IMPORTE),0) AS IMPORTE FROM mateo.CCP_DESCOMPROMETIDO ";
                strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                strComando += "AND STATUS = 'A' ";
                strComando += "AND FOLIO IS NULL ";
                strComando += "GROUP BY ID_AUTORIZACION ";
                Statement stmt = conn.createStatement();
                ResultSet rst = stmt.executeQuery(strComando);
                while(rst.next())
                {
                        dblComprometido = rst.getDouble("Importe");
                        mReturn.put(rst.getString("id_autorizacion"), dblComprometido);
                }
                stmt.close();
                rst.close();

                return mReturn;
        }

        public double ccpgetDescomprometido(HttpSession session, String strAutorizacionID) throws SQLException
        {
                double dblComprometido = 0;

                String strComando = "SELECT COALESCE(SUM(IMPORTE),0) AS IMPORTE FROM mateo.CCP_DESCOMPROMETIDO ";
                strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                strComando += "AND STATUS = 'A' ";
                strComando += "AND ID_AUTORIZACION = '"+strAutorizacionID+"' ";
                Statement stmt = conn.createStatement();
                ResultSet rst = stmt.executeQuery(strComando);
                if(rst.next())
                {
                        dblComprometido = rst.getDouble("Importe");
                }
                stmt.close();
                rst.close();

                return dblComprometido;
        }

        public double ccpgetDescomprometido(HttpSession session, String strAutorizacionID, String strFechaInicial, String strFechaFinal) throws SQLException
        {
                double dblComprometido = 0;

                String strComando = "SELECT COALESCE(SUM(IMPORTE),0) AS IMPORTE FROM mateo.CCP_DESCOMPROMETIDO ";
                strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                strComando += "AND FECHA BETWEEN to_date('"+strFechaInicial+"', 'dd/mm/yy') ";
                strComando += "AND to_date('"+strFechaFinal+"', 'dd/mm/yy') ";
                strComando += "AND STATUS = 'A' ";
                strComando += "AND ID_AUTORIZACION = '"+strAutorizacionID+"' ";
                Statement stmt = conn.createStatement();
                ResultSet rst = stmt.executeQuery(strComando);
                if(rst.next())
                {
                        dblComprometido = rst.getDouble("Importe");
                }
                stmt.close();
                rst.close();

                return dblComprometido;
        }

        public double ccpgetDescomprometido(HttpSession session, String strAutorizacionID, String strIDCtaMayor, String auxiliarId, String strFechaInicial, String strFechaFinal) throws SQLException
        {
                double dblComprometido = 0;

                String strComando = "SELECT COALESCE(SUM(IMPORTE),0) AS IMPORTE FROM mateo.CCP_DESCOMPROMETIDO ";
                strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
                strComando += "AND FECHA BETWEEN to_date('"+strFechaInicial+"', 'dd/mm/yy') ";
                strComando += "AND to_date('"+strFechaFinal+"', 'dd/mm/yy') ";
                strComando += "AND STATUS = 'A' ";
                strComando += "AND ID_AUTORIZACION = '"+strAutorizacionID+"' ";
                strComando += "AND ID_CTAMAYOR_M = '"+strIDCtaMayor+"' ";
                strComando += "AND ID_AUXILIAR_M = '"+auxiliarId+"' ";
                Statement stmt = conn.createStatement();
                ResultSet rst = stmt.executeQuery(strComando);
                if(rst.next())
                {
                        dblComprometido = rst.getDouble("Importe");
                }
                stmt.close();
                rst.close();
                stmt = null;
                rst = null;

                return dblComprometido;
        }

        //Funciones para obtener el presupuesto total de un departamento
        //Para obtener el presupuesto acumulado, se utiliza la funcion
	public double ccgetPresupuestoCC
        (
                String strIDCcosto, HttpSession session
        ) throws Exception
        {
		double dblCantidad = 0;

                java.util.Calendar gcHoy = new java.util.GregorianCalendar();
                gcHoy.setTime(new java.util.Date());
		String strComando = "SELECT COALESCE(SUM(IMPORTE),0) AS IMPORTE FROM mateo.CCP_PRESUPUESTO ";
		strComando += "WHERE ID_EJERCICIO = '"+(String)session.getAttribute("id_ejercicio")+"' ";
		strComando += "AND ID_CCOSTO = '"+strIDCcosto+"' ";
		strComando += "AND MES <= "+String.valueOf(gcHoy.get(java.util.Calendar.MONTH)+1);
                strComando += "AND (ID_CTAMAYOR LIKE '2.4.%' ";
                strComando += "OR ID_CTAMAYOR LIKE '2.5.%' ";
                strComando += "OR ID_CTAMAYOR IN ('2.3.18') ";
                //09-jul-2012 - El CP Arturo Sebastian, solicito que ahora todas las escuelas de FACSA muestren estas cuentas
                strComando += "OR (ID_CTAMAYOR =any ('2.3.18','2.3.19','2.3.20') AND ID_CCOSTO like ('1.01.2.03%'))) ";
                //strComando += "OR (ID_CTAMAYOR IN ('2.3.19','2.3.20') AND ID_CCOSTO = '1.01.2.03.01')) ";
		Statement stmt = conn.createStatement();
		ResultSet rst = stmt.executeQuery(strComando);
		if(rst.next())
                {
			dblCantidad = rst.getDouble("IMPORTE");
		}
		rst.close();
		stmt.close();
		return dblCantidad;
	}

        //Funcion para calcular las Autorizaciones hasta hoy
        public double ccgetComprometidoCC
        (
                String strIDCCosto, HttpSession session
        ) throws SQLException, Exception
        {
                double dblComprometido = 0;

                String COMANDO = "SELECT COALESCE(SUM(PRECIO_U * CANTIDAD),0) IMPORTE ";
                COMANDO += "FROM mateo.CCP_AUTORIZACION_DET ";
                COMANDO += "WHERE ID_EJERCICIO = ? ";
                COMANDO += "AND ID_CCOSTO = ? ";
                COMANDO += "AND ID_EJERCICIO || ID_AUTORIZACION IN ";
                COMANDO += "(SELECT ID_EJERCICIO || ID_AUTORIZACION ";
                COMANDO += "FROM mateo.CCP_AUTORIZACION ";
                COMANDO += "WHERE ID_EJERCICIO = ? ";
                COMANDO += "AND ID_CCOSTO = ? ";
                COMANDO += "AND STATUS = 'R' ";
                COMANDO += "AND TO_DATE(FECHA,'DD/MM/YY') <= TO_DATE(SYSDATE,'DD/MM/YY') ) ";
                PreparedStatement pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, (String)session.getAttribute("id_ejercicio"));
                pstmt.setString(2, strIDCCosto);
                pstmt.setString(3, (String)session.getAttribute("id_ejercicio"));
                pstmt.setString(4, strIDCCosto);
                ResultSet rset = pstmt.executeQuery();

                if (rset.next())
                {
                        dblComprometido = rset.getDouble("Importe");
                }
                pstmt.close();
                rset.close();

                return dblComprometido;
        }

        //Funcion para obtener el descomprometido hasta hoy,
        //SIN TOMAR EN CUENTA AQUELLOS REGISTROS HECHOS POR CONTABILIDAD
        //YA QUE AL CALCULAR LOS GASTOS SE DUPLICARIAN
        public double ccgetDescomprometidoCC
        (
                String strIDCCosto, HttpSession session
        ) throws SQLException, Exception
        {
                double dblDescomprometido = 0;

/**************************Query Optimizado*****************************************/
                String COMANDO = "SELECT COALESCE(SUM(IMPORTE),0) IMPORTE ";
                COMANDO += "FROM mateo.CCP_DESCOMPROMETIDO D, mateo.CCP_AUTORIZACION A ";
                COMANDO += "WHERE A.ID_EJERCICIO = ? ";
                COMANDO += "AND A.ID_CCOSTO = ? ";
                //COMANDO += "AND A.STATUS = 'R' ";
                COMANDO += "AND D.STATUS = 'A' " ;
                COMANDO += "AND A.ID_EJERCICIO = D.ID_EJERCICIO ";
                COMANDO += "AND A.ID_AUTORIZACION = D.ID_AUTORIZACION ";
                PreparedStatement pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, (String)session.getAttribute("id_ejercicio"));
                pstmt.setString(2, strIDCCosto);

                ResultSet rset = pstmt.executeQuery();

                if (rset.next())
                {
                        dblDescomprometido = rset.getDouble("Importe");
                }
                pstmt.close();
                rset.close();

                return dblDescomprometido;
        }

        //Funcion para obtener los gastos de un centro de costo
        public double ccgetGastosCC
        (
                String strIDCCosto, HttpSession session
        ) throws SQLException, Exception
        {
                double dblGastos = 0;

                String COMANDO = "SELECT COALESCE(SUM(IMPORTE * CASE NATURALEZA WHEN 'C' THEN -1 ELSE 1 END),0) IMPORTE ";
                COMANDO += "FROM mateo.CONT_MOVIMIENTO ";
                COMANDO += "WHERE ID_EJERCICIO = ? ";
                COMANDO += "AND ID_CCOSTOM = ? ";
                COMANDO += "AND (ID_CTAMAYORM LIKE '2.4.%' ";
                COMANDO += "OR ID_CTAMAYORM LIKE '2.5.%' ";
                COMANDO += "OR ID_CTAMAYORM IN ('2.3.18') ";
                //09-jul-2012 - El CP Arturo Sebastian, solicito que ahora todas las escuelas de FACSA muestren estas cuentas
                COMANDO += "OR (ID_CTAMAYOR =any ('2.3.18','2.3.19','2.3.20') AND ID_CCOSTO like ('1.01.2.03%'))) "+
                //COMANDO += "OR (ID_CTAMAYORM IN ('2.3.19','2.3.20') AND ID_CCOSTOM = '1.01.2.03.01')) " +
                        "AND ID_EJERCICIO || ID_CTAMAYORM IN " +
                        "( " +
                        "SELECT ID_EJERCICIO || ID_CTAMAYOR " +
                        "FROM mateo.CCP_PRESUPUESTO " +
                        "WHERE ID_EJERCICIO = ? " +
                        "AND ID_CCOSTO = ? " +
                        "AND (ID_CTAMAYOR LIKE '2.4.%'  " +
                        "OR ID_CTAMAYOR LIKE '2.5.%'  " +
                        "OR ID_CTAMAYOR IN ('2.3.18')  " +
                        //09-jul-2012 - El CP Arturo Sebastian, solicito que ahora todas las escuelas de FACSA muestren estas cuentas
                        "OR (ID_CTAMAYOR =any ('2.3.18','2.3.19','2.3.20') AND ID_CCOSTO like ('1.01.2.03%'))) "+
                        //"OR (ID_CTAMAYOR IN ('2.3.19','2.3.20') AND ID_CCOSTO = '1.01.2.03.01')) " +
                        ") ";
                PreparedStatement pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, (String)session.getAttribute("id_ejercicio"));
                pstmt.setString(2, strIDCCosto);
                pstmt.setString(3, (String)session.getAttribute("id_ejercicio"));
                pstmt.setString(4, strIDCCosto);
                ResultSet rset = pstmt.executeQuery();

                if (rset.next())
                {
                        dblGastos = rset.getDouble("Importe");
                }
                pstmt.close();
                rset.close();

                return dblGastos;
        }

        //Funcion que regresa el presupuesto disponible
        public double ccgetPresupuestoDispCC
        (
                String strIDCCosto, HttpSession session
        ) throws Exception
        {
                return ccgetPresupuestoCC(strIDCCosto, session) -
                ((ccgetComprometidoCC(strIDCCosto, session) ) +
                //El hecho de disminuir los descomprometido ocasiona que se incremente el saldo disponible
                //- ccgetDescomprometidoCC(strIDCCosto, session)) +
                ccgetGastosCC(strIDCCosto, session));
        }

        //Funcion que asigna al campo impreso el status S
        public void ccpsetImpreso
        (
                String strAutorizacionID, HttpSession session
        ) throws Exception
        {
                String COMANDO = "UPDATE mateo.CCP_AUTORIZACION ";
                COMANDO += "SET IMPRESO = 'S', ";
                COMANDO += "USUARIO_IMPRESO = ?, ";
                COMANDO += "FECHA_IMPRESO = SYSDATE ";
                COMANDO += "WHERE ID_EJERCICIO = ? ";
                COMANDO += "AND ID_AUTORIZACION = ? ";
                PreparedStatement pstmt = conn.prepareStatement(COMANDO);
                pstmt.setString(1, (String)session.getAttribute("login"));
                pstmt.setString(2, (String)session.getAttribute("id_ejercicio"));
                pstmt.setString(3, strAutorizacionID);
                pstmt.executeUpdate();
                pstmt.close();
        }
        //Funci?n que determina el bloque actual, y obtiene el presupuesto anterior
        //si es que el bloque actual es el bloque 2
        //Para determinar el bloque en el cual ha de buscar el presupuesto,
        //evalua el rango de las fechas recibidas, el cual es el rango de busqueda actual.
        public double ccpgetPresupuestoDisponible
        (
                String strIDCtaMayor, String auxiliarId, String fechaInicial, String fechaFinal, HttpSession session
        ) throws SQLException, Exception
        {
                String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
                String strIDCCosto = (String)session.getAttribute("id_ccosto_ccp");

                java.util.Calendar gcFechaInicial = new java.util.GregorianCalendar();
                java.util.Calendar gcFechaActual = new java.util.GregorianCalendar();
                java.util.Calendar gcFechaFinal = new java.util.GregorianCalendar();

                gcFechaActual.setTime(new java.text.SimpleDateFormat("dd/MM/yyyy").parse(fechaInicial));

                //System.out.println(fechaInicial+"@"+fechaFinal);

                if (gcFechaActual.get(java.util.Calendar.MONTH)+1 >= 1 &&
                        gcFechaActual.get(java.util.Calendar.MONTH)+1 < 7)
                {
                        //Obtener presupuesto del Bloque 2 ya que la fecha Inicial del rango de busqueda actual
                        //esta entre los meses correspondientes al bloque 1
                        gcFechaInicial.setTime(gcFechaActual.getTime());
                        gcFechaInicial.set(java.util.Calendar.DATE, 1);
                        gcFechaInicial.set(java.util.Calendar.MONTH, 6);

                        gcFechaFinal.setTime(gcFechaActual.getTime());
                        gcFechaFinal.set(java.util.Calendar.MONTH, 11);
                        int intDias = gcFechaFinal.getActualMaximum(java.util.Calendar.DATE);
                        gcFechaFinal.set(java.util.Calendar.DATE, intDias);
                }
                else if (gcFechaActual.get(java.util.Calendar.MONTH)+1 >= 7 &&
                        gcFechaActual.get(java.util.Calendar.MONTH)+1 <= 12)
                {
                        //Obtener presupuesto del Bloque 1 ya que la fecha Inicial del rango de busqueda actual
                        //esta entre los meses correspondientes al bloque 2
                        gcFechaInicial.setTime(gcFechaActual.getTime());
                        gcFechaInicial.set(java.util.Calendar.DATE, 1);
                        gcFechaInicial.set(java.util.Calendar.MONTH, 0);

                        gcFechaFinal.setTime(gcFechaActual.getTime());
                        gcFechaFinal.set(java.util.Calendar.MONTH, 5);
                        int intDias = gcFechaFinal.getActualMaximum(java.util.Calendar.DATE);
                        gcFechaFinal.set(java.util.Calendar.DATE, intDias);
                }


                //Obtener presupuesto
                double dblPresupuesto = getPresupuestoRangoMeses(strIDEjercicio, strIDCtaMayor, strIDCCosto,
                        gcFechaInicial.get(java.util.Calendar.MONTH)+1,
                        gcFechaFinal.get(java.util.Calendar.MONTH)+1);

                //Obtener gastos del mes actual
                String strFechaInicial = new java.text.SimpleDateFormat("dd/MM/yyyy").format(gcFechaInicial.getTime());
                String strFechaFinal = new java.text.SimpleDateFormat("dd/MM/yyyy").format(gcFechaFinal.getTime());

                double dblGastos = getGastosMesActual(strIDEjercicio, strIDCtaMayor, strIDCCosto, auxiliarId, strFechaInicial, strFechaFinal);

                //Obtener comprometido
                double dblComprometido = getComprometido(strIDEjercicio, strIDCtaMayor, strIDCCosto, auxiliarId, strFechaInicial, strFechaFinal);

                return dblPresupuesto - (dblGastos + dblComprometido);
        }
	/*Funcion que regresa un rango de centros de costo*/
	public java.util.Vector ccpgetRangoCCostos
	(
		String strCCostoI, String strCCostoF, HttpSession session
	) throws Exception
	{
		java.util.Vector vctCCostos = new java.util.Vector();

		String strIDEjercicio = (String)session.getAttribute("id_ejercicio");

		String COMANDO = "SELECT ID_CCOSTO ";
		COMANDO += "FROM mateo.CONT_CCOSTO ";
		COMANDO += "WHERE ID_EJERCICIO = ? ";
		COMANDO += "AND ID_CCOSTO BETWEEN ? AND ? ";
		COMANDO += "AND DETALLE = 'S' ";

		PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		pstmt.setString(1, strIDEjercicio);
		pstmt.setString(2, strCCostoI);
		pstmt.setString(3, strCCostoF);

		ResultSet rset = pstmt.executeQuery();

		while(rset.next())
		{
			vctCCostos.add(rset.getString("ID_CCosto"));
		}

		pstmt.close();
		rset.close();

		return vctCCostos;
	}
	//Obtener listado de fondos
	public Vector getFondos(HttpSession session) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rset = null;
		Vector vctFondos = new Vector();

		String COMANDO = "SELECT A.ID_AUXILIAR, A.NOMBRE ";
		COMANDO += "FROM mateo.CONT_AUXILIAR A, CONT_RELACION R ";
		COMANDO += "WHERE A.ID_EJERCICIO = R.ID_EJERCICIO ";
		COMANDO += "AND A.ID_AUXILIAR = R.ID_AUXILIAR ";
		COMANDO += "AND R.ID_EJERCICIO = ? ";
		COMANDO += "AND R.ID_CCOSTO = ? ";
		COMANDO += "AND R.ID_CTAMAYOR = '3.1.02.01' ";
		COMANDO += "ORDER BY A.ID_AUXILIAR ";

		pstmt = conn.prepareStatement(COMANDO);
		pstmt.setString(1, (String)session.getAttribute("id_ejercicio"));
		pstmt.setString(2, (String)session.getAttribute("id_ccosto_ccp"));

		rset = pstmt.executeQuery();

		while(rset.next()){
			vctFondos.add(rset.getString("ID_Auxiliar")+"@"+rset.getString("Nombre"));
		}

		rset.close();
		pstmt.close();

		return vctFondos;
	}
	//Obtiene todas las autorizaciones de compra, menos las canceladas,
	//con su saldo autorizado y su saldo descomprometido
	public PreparedStatement ccpgetAutorizacionesTodas(HttpSession session, String fechaInicial, String fechaFinal) throws Exception {
		String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		String strIDCCosto = (String)session.getAttribute("id_ccosto_ccp");

		String COMANDO = "SELECT Z.ID_AUTORIZACION, TO_CHAR(Z.FECHA,'DD/MM/YYYY') FECHA, COALESCE(A.IMPORTE,0) AUTORIZADO, COALESCE(D.IMPORTE,0) DESCOMPROMETIDO ";
		COMANDO += "FROM mateo.CCP_AUTORIZACION Z, ";
		COMANDO += "(SELECT ID_EJERCICIO, ID_AUTORIZACION, COALESCE(SUM(precio_u*cantidad),0) IMPORTE ";
		COMANDO += "FROM mateo.CCP_AUTORIZACION_DET ";
		COMANDO += "WHERE ID_EJERCICIO = ? ";
		COMANDO += "AND STATUS != 'X' ";
		COMANDO += "GROUP BY ID_EJERCICIO, ID_AUTORIZACION) A, ";
		COMANDO += "(SELECT ID_EJERCICIO, ID_AUTORIZACION, coalesce(SUM(IMPORTE),0) IMPORTE ";
		COMANDO += "FROM mateo.CCP_DESCOMPROMETIDO ";
		COMANDO += "WHERE ID_EJERCICIO = ? ";
		COMANDO += "GROUP BY ID_EJERCICIO, ID_AUTORIZACION) D ";
		COMANDO += "WHERE A.ID_EJERCICIO = D.ID_EJERCICIO(+) ";
		COMANDO += "AND A.ID_AUTORIZACION = D.ID_AUTORIZACION(+) ";
		COMANDO += "AND A.ID_EJERCICIO = Z.ID_EJERCICIO ";
		COMANDO += "AND A.ID_AUTORIZACION = Z.ID_AUTORIZACION ";
		COMANDO += "AND Z.ID_EJERCICIO = ? ";
		COMANDO += "AND Z.ID_CCOSTO = ? ";
		COMANDO += "AND Z.FECHA BETWEEN TO_DATE(?,'DD/MM/YY') AND TO_DATE(?,'DD/MM/YY') ";
		COMANDO += "ORDER BY Z.ID_AUTORIZACION ";

		PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		pstmt.setString(1, strIDEjercicio);
		pstmt.setString(2, strIDEjercicio);
		pstmt.setString(3, strIDEjercicio);
		pstmt.setString(4, strIDCCosto);
		pstmt.setString(5, fechaInicial);
		pstmt.setString(6, fechaFinal);

		return pstmt;
	}

}
