/*
 * Created on 30/10/2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.poliza;

import mx.edu.um.mateo.inscripciones.model.ccobro.ccp.Metodos;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSession;

import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Constants;
import mx.edu.um.mateo.inscripciones.model.ccobro.cuenta.CtaMayor;
import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;

/**
 * @author Alberto
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Metodos1 extends Conexion{
	
//	Atributos de clase de la p?liza
	  String atrstrIDEjercicio = null;
	  String atrstrIDLibro = null;
	  String atrstrFolio = null;
	  String atrstrFecha = null;
	  String atrstrDescripcion = null;
	  String atrstrIDUsuario = null;
	  String atrstrRevisadoPor = null;
	  String atrstrStatus = null;
	  String atrstrStatusInd = null;
	  
	  metodos2 metodos2;
	  metodos3 metodos3;

	  //Atributos de clase del movimiento
	  String atrstrIDEjercicioMov = null;
	  String atrstrIDLibroMov = null;
	  String atrstrFolioMov = null;
	  Integer atrintNumMov = null;
	  String atrstrFechaMov = null;
	  String atrstrDescripcionMov = null;
	  String atrstrNaturalezaMov = null;
	  Double atrdblImporteMov = null;
	  String atrstrReferenciaMov = null;
	  String atrstrReferencia2Mov = null;
	  String atrstrCtaMayorMov = null;
	  String atrstrCCostoMov = null;
	  String atrstrAuxiliarMov = null;
	  String atrstrStatusMov = null;

      Metodos metsCCP;
	  
	  public Metodos1(){
		
	  	
	  }
	  
	  public Metodos1(Connection conn){
		this.conn=conn;
		metodos3 = new metodos3(conn);
		metodos2 = new metodos2(conn);
        metsCCP = new Metodos(conn);
	  }
	  
	  public void initMetodos(){
		metodos3 = new metodos3(conn);
		metodos2 = new metodos2(conn);
	  }
//	M?todos p?blicos, para el manejo de p?lizas
	  //Se espera que ya exista la variable conn

	  //M?todos del caso de uso Alta de Encabezado de P?liza

	  public void creaEncabezadoPoliza
		  (
		   String strFecha, String strDescripcion, HttpSession session
		  ) throws SQLException, Exception
	  {
		  //El encabezado debe tener los siguientes campos:
		  //Ejercicio contable, libro contable, folio de p?liza,
		  //fecha, descripci?n, usuario
		  String COMANDO = null;
		  PreparedStatement pstmt = null;
 
		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  Long idUsuario = ((Long)session.getAttribute(Constants.SESSION_USER_ID));
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDLibro = (String)session.getAttribute("id_libro");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  //Validar que el usuario, tenga derechos de creaci?n de p?lizas
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("Usuario sin derechos a crear p?lizas");
		  }

		  //Obtener c_poliza
		  String strFolio = getFolioPolizaActual(session);

		  //Validar que no exista alguna poliza con el folio = c_poliza
		  if (getExistePolizaActual(session))
			  throw new Error("Poliza con folio "+ strFolio +", ya existe!");

		  //Registrar el encabezado de la p?liza
		  COMANDO = "INSERT INTO MATEO.CONT_POLIZA ";
		  COMANDO += " (ID_EJERCICIO, ID_LIBRO, ID_CCOSTO, FOLIO, FECHA, DESCRIPCION, ID_USUARIO, STATUS, ID_EJERCICIO2) ";
		  COMANDO += " VALUES ";
		  COMANDO += " (?, ?, ?, ?, TO_DATE(?, 'dd-mm-yy'), ?, ?, 'A',?) ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDLibro);
		  pstmt.setString(3, strIDCCosto);
		  pstmt.setString(4, strFolio);
		  pstmt.setString(5, strFecha);
		  pstmt.setString(6, strDescripcion);
		  pstmt.setLong(7, idUsuario);
		  pstmt.setString(8, strIDEjercicio);
		  
		  pstmt.execute();
		  pstmt.close();
	  }
	  
//	M?todos del caso de uso Modifica Encabezado de P?liza

	  public void modificaEncabezadoPoliza
		  (
		   String strFolio,
		   String strDescripcion, String strFecha, boolean blnModificaMovimientos,
		   HttpSession session
		  ) throws SQLException, Exception
	  {
		  //Se modifica la Fecha de la p?liza, y a su vez se modifican la fecha
		  //de los movimientos de la misma p?liza si ModificaMovimientos es true
		  //Se pasan los campos de la llave primaria para identificar la p?liza
		  String COMANDO = null;
		  PreparedStatement pstmt = null;

		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDLibro = (String)session.getAttribute("id_libro");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strStatus = null;

		  //Validar que el usuario, tenga derechos de acceso a p?lizas
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("Usuario sin accesos a p?lizas");
		  }
		  //Solo los usuarios:
		  //Capturista de p?lizas, Revisa P?lizas, Director de Contabilidad
		  //Validar en base al status de la p?liza si el usuario puede modificar
		  //la p?liza

		  getDatosPoliza(strIDEjercicio, strIDLibro, strIDCCosto, strFolio);
		  strStatus = getStatusPolizaInd();

		  if (strStatus.equals("A"))
		  {//Si la p?liza est? abierta, los tres usuarios pueden modificarla
			  //Valdar que sean uno de los tres usuarios que est?n entrando
			  if (!(metodos3.esTipoUsuarioValido(session, "04") || metodos3.esTipoUsuarioValido(session, "05") || metodos3.esTipoUsuarioValido(session, "06")))
			  {
				  throw new Error("Usuario sin derechos de modificar p?liza");
			  }

			  COMANDO = "UPDATE CONT_POLIZA ";
			  COMANDO += " SET FECHA = TO_DATE(?, 'DD/MM/YY'), ";
			  COMANDO += " DESCRIPCION = ? ";
			  COMANDO += " WHERE ID_EJERCICIO = ? ";
			  COMANDO += " AND ID_LIBRO = ? ";
			  COMANDO += " AND ID_CCOSTO = ? ";
			  COMANDO += " AND FOLIO = ? ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strFecha);
			  pstmt.setString(2, strDescripcion);
			  pstmt.setString(3, strIDEjercicio);
			  pstmt.setString(4, strIDLibro);
			  pstmt.setString(5, strIDCCosto);
			  pstmt.setString(6, strFolio);
			  pstmt.execute();
			  pstmt.close();

			  //Si Modifica Movimientos es true
			  if (blnModificaMovimientos)
			  {
				  COMANDO = "UPDATE CONT_MOVIMIENTO ";
				  COMANDO += "SET FECHA = TO_DATE(?, 'DD/MM/YY') ";
				  COMANDO += "WHERE ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_LIBRO = ? ";
				  COMANDO += "AND ID_CCOSTO = ? ";
				  COMANDO += "AND FOLIO = ? ";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strFecha);
				  pstmt.setString(2, strIDEjercicio);
				  pstmt.setString(3, strIDLibro);
				  pstmt.setString(4, strIDCCosto);
				  pstmt.setString(5, strFolio);
				  pstmt.execute();
				  pstmt.close();
			  }
		  }
		  else if (strStatus.equals("R"))
		  {//si la p?liza est? en revisi?n, solo el revisa p?lizas y
		   //el director de contabilidad pueden modificar la p?liza
			  if (!(metodos3.esTipoUsuarioValido(session, "05") || metodos3.esTipoUsuarioValido(session, "06")))
			  {
				  throw new Error("Usuario sin derechos de modificar p?liza");
			  }
			  //Verificar que el usuario no sea capturista de p?lizas
			  COMANDO = "UPDATE CONT_POLIZA ";
			  COMANDO += " SET FECHA = TO_DATE(?, 'DD/MM/YY'), ";
			  COMANDO += " DESCRIPCION = ? ";
			  COMANDO += " WHERE ID_EJERCICIO = ? ";
			  COMANDO += " AND ID_LIBRO = ? ";
			  COMANDO += " AND ID_CCOSTO = ? ";
			  COMANDO += " AND FOLIO = ? ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strFecha);
			  pstmt.setString(2, strDescripcion);
			  pstmt.setString(3, strIDEjercicio);
			  pstmt.setString(4, strIDLibro);
			  pstmt.setString(5, strIDCCosto);
			  pstmt.setString(6, strFolio);
			  pstmt.execute();
			  pstmt.close();

			  //Si Modifica Movimientos es true
			  if (blnModificaMovimientos)
			  {
				  COMANDO = "UPDATE CONT_MOVIMIENTO ";
				  COMANDO += "SET FECHA = TO_DATE(?, 'DD/MM/YY') ";
				  COMANDO += "WHERE ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_LIBRO = ? ";
				  COMANDO += "AND ID_CCOSTO = ? ";
				  COMANDO += "AND FOLIO = ? ";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strFecha);
				  pstmt.setString(2, strIDEjercicio);
				  pstmt.setString(3, strIDLibro);
				  pstmt.setString(4, strIDCCosto);
				  pstmt.setString(5, strFolio);
				  pstmt.execute();
				  pstmt.close();
			  }
		  }
		  else if (strStatus.equals("C"))
		  {//Si la p?liza est? cerrada, ning?n usuario puede modificar lo p?liza
		   throw new Error("Esta p?liza solo puede ser modifica si el Director de Contabilidad la abre");
		  }
		  else if (strStatus.equals("D"))
		  {//Si la p?liza est? cerrada definitivamente,
		   //ning?n usuario puede modificar lo p?liza
		   throw new Error("Esta p?liza ya no puede ser modificada bajo ninguna circunstancia");
		  }
		  else if (strStatus.equals("I"))
		  {//Si la p?liza est? cancelada, ning?n usuario puede modificar lo p?liza
		   throw new Error("Esta p?liza ya no puede ser modificada bajo ninguna circunstancia");
		  }
	  }
	  
//	M?todos del caso de uso Abrir Encabezado de P?liza
	  public void abrirEncabezado
		  (
		   String strIDEjercicio, String strIDLibro, String strFolio,
		   HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo abre el encabezado de la p?liza modificando el status del mismo
	   //en base al usuario de contabilidad que genera la operaci?n.
	   //Usuario Director de Contabilidad, puede abrir una p?liza Cerrada o en Revisi?n
	   //Usuario Revisa P?lizas, puede abrir una p?liza en Revisi?n
	   //Usuario Capturista de P?lizas, no puede abrir p?lizas

		  PreparedStatement pstmt = null;
		  String COMANDO = null;

		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strStatus = null;

		  //Validar usuario, solo puede ser Director de Contabilidad, Revisa P?lizas,
		  //Capturista P?lizas
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("Usuario sin derechos para abrir una p?liza");
		  }

		  //Solo los usuarios:
		  //Capturista de p?lizas, Revisa P?lizas, Director de Contabilidad
		  //Validar en base al status de la p?liza si el usuario puede modificar
		  //la p?liza

		  getDatosPoliza(strIDEjercicio, strIDLibro, strIDCCosto, strFolio);
		  strStatus = getStatusPolizaInd();

		  if (strStatus.equals("A"))
		  {
		   //Si la p?liza est? abierta, no hay nada que hacer
			  throw new Error("P?liza previamente abierta.");
		  }
		  else if (strStatus.equals("R"))
		  {
		   //Si la p?liza est? en revisi?n, solo el Director de Contabilidad
		   //y el Revisa P?lizas pueden abrir la p?liza, pasando esta a status A
			  //Validar que usuario sea Director de Contabilidad o Revisa P?lizas
			  if (!(metodos3.esTipoUsuarioValido(session, "05") || metodos3.esTipoUsuarioValido(session, "06")))
			  {
				  throw new Error("Usuario sin derechos para abrir p?liza en revisi?n");
			  }

			  //Modificar status de la p?liza
			  COMANDO = "UPDATE CONT_POLIZA ";
			  COMANDO += "SET STATUS = 'A' ";
			  COMANDO += "WHERE ID_EJERCICIO = ? ";
			  COMANDO += "AND ID_LIBRO = ? ";
			  COMANDO += "AND ID_CCOSTO = ? ";
			  COMANDO += "AND FOLIO = ? ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strIDEjercicio);
			  pstmt.setString(2, strIDLibro);
			  pstmt.setString(3, strIDCCosto);
			  pstmt.setString(4, strFolio);
			  pstmt.execute();
			  pstmt.close();
		  }
		  else if (strStatus.equals("C"))
		  {
		   //Si la p?liza est? cerrada, solo el Director de Contabilidad puede abrir
		   //la p?liza, pasando esta a status 'R'
			  //Validar que usuario sea Director de Contabilidad
			  if (!metodos3.esTipoUsuarioValido(session, "06"))
			  {
				  throw new Error("Usuario sin derechos para abrir un p?liza cerrada");
			  }

			  //Modificar status de la p?liza
			  COMANDO = "UPDATE CONT_POLIZA ";
			  COMANDO += "SET STATUS = 'R' ";
			  COMANDO += "WHERE ID_EJERCICIO = ? ";
			  COMANDO += "AND ID_LIBRO = ? ";
			  COMANDO += "AND ID_CCOSTO = ? ";
			  COMANDO += "AND FOLIO = ? ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strIDEjercicio);
			  pstmt.setString(2, strIDLibro);
			  pstmt.setString(3, strIDCCosto);
			  pstmt.setString(4, strFolio);
			  pstmt.execute();
			  pstmt.close();
		  }
		  else if (strStatus.equals("D"))
		  {
		   //Si la ,p?liza est? cerrada definitivamente, no se puede abrir
			  throw new Error("No se puede abrir una p?liza que est? cerrada de manera definitiva");
		  }
		  else if (strStatus.equals("I"))
		  {
		   //Si la p?liza est? cancelada, no se puede abrir
			  throw new Error("No se puede abrir una p?liza que est? cancelada");
		  }
	  }
	  
//	M?todos del caso de uso Cerrar Encabezado de P?liza
	  public void cerrarEncabezado
		  (
		   String strIDEjercicio, String strIDLibro, String strFolio,
		   HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo cierra el encabezado de la p?liza modificando el status del mismo
	   //en base al usuario de contabilidad que genera la operaci?n.
	   //Usuario Director de Contabilidad, puede cerrar una p?liza Abierta o en Revisi?n
	   //Usuario Revisa P?lizas, puede cerrar una p?liza en Revisi?n o Abierta
	   //Usuario Capturista de P?lizas puede cerrar una p?liza Abierta
	   //Una p?liza solo puede ser cerrada si tiene movimientos y estos cuadran

		  PreparedStatement pstmt = null;
		  String COMANDO = null;

		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strStatus = null;


		  //Validar usuario, solo puede ser Director de Contabilidad, Revisa P?lizas,
		  //Capturista P?lizas
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("Usuario sin derechos para cerrar una p?liza");
		  }

		  getDatosPoliza(strIDEjercicio, strIDLibro, strIDCCosto, strFolio);
		  strStatus = getStatusPolizaInd();

		  //Validar que la p?liza no tenga ning?n movimiento en espera de ser cancelado
		  //por auditor?a (Status T)
		  if (polizaConMovsT(strIDEjercicio, strIDLibro, strIDCCosto, strFolio))
			  throw new Error("P?liza imposible de cerrar, ya que tiene movimientos <br> marcados para cancelar por Auditoria");

		  if (strStatus.equals("A"))
		  {
		   //Si la p?liza est? abierta, cualquiera de los tres usuarios v?lidos puede
			  if (polizaCuadra(strIDEjercicio, strIDLibro, strIDCCosto, strFolio))
			  {//Si la p?liza cuadra, cerrar la p?liza para su revisi?n
				  if (!(metodos3.esTipoUsuarioValido(session, "04") || metodos3.esTipoUsuarioValido(session, "05")
					  || metodos3.esTipoUsuarioValido(session, "06") || metodos3.esTipoUsuarioValido(session, "01")
					  || metodos3.esTipoUsuarioValido(session, "02")))
				  {
					  throw new Error("Usuario sin derechos de cerrar p?liza");
				  }

				  COMANDO = "UPDATE MATEO.CONT_POLIZA ";
				  COMANDO += "SET STATUS = 'D' ";
				  COMANDO += "WHERE ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_LIBRO = ? ";
				  COMANDO += "AND ID_CCOSTO = ? ";
				  COMANDO += "AND FOLIO = ? ";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strIDEjercicio);
				  pstmt.setString(2, strIDLibro);
				  pstmt.setString(3, strIDCCosto);
				  pstmt.setString(4, strFolio);
				  pstmt.execute();
				  pstmt.close();

				  //Modificar c_poliza
				  polizaModificaActual(getFolioPoliza(strIDLibro, session),strIDLibro, session);
			  }
			  else
			  {
				  throw new Error("La poliza "+strFolio+" del usuario "+strUsuario+" no cuadra! <br>  Imposible cerrarla");
			  }
		  }		  
		  else if (strStatus.equals("D"))
		  {
		   //Si la p?liza est? cerrada definitivamente, no se puede cerrar
			  throw new Error("P?liza previamente cerrada de manera definitiva");
		  }
		  else if (strStatus.equals("I"))
		  {
		   //Si la p?liza est? cancelada, no se puede cerrar
			  throw new Error("No se puede cerrar una p?liza que est? cancelada");
		  }
	  }
	  
//	M?todos del caso de uso Cancela Encabezado P?liza
	  public void cancelaEncabezado
		  (
		   String strFolio, HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo cancela una p?liza.
	   //Si la p?liza tiene status A, esta acci?n la borra f?sicamente.
	   //Si la p?liza tiene status R o C, esta acci?n le modifica el status a I,
	   //modificando la descripci?n de la p?liza y los movimientos, as? como
	   //modificar el importe a cero de los movimientos.
	   //El usuario Capturista de P?lizas, solo puede cancelar p?lizas abiertas
	   //El usuario Revisa P?lizas, puede cancelar p?lizas en revisi?n y abiertas
	   //El usuario Director de Contabilidad, puede cancelar p?lizas cerradas,
	   //en revisi?n y abiertas

		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;

		  Long idUsuario = (Long)session.getAttribute(Constants.SESSION_USER_ID);
		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDLibro = (String)session.getAttribute("id_libro");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strStatus = null;

		  //Validar usuario, solo puede ser Director de Contabilidad, Revisa P?lizas,
		  //Capturista P?lizas
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("Usuario sin derechos para cancelar una p?liza");
		  }

		  getDatosPoliza(strIDEjercicio, strIDLibro, strIDCCosto, strFolio);
		  strStatus = getStatusPolizaInd();

		  if (strStatus.equals("A"))
		  {
		   //Si la p?liza est? abierta, cualquiera de los tres usuarios v?lidos pueden
		   //cancelar la p?liza
			  if (!(metodos3.esTipoUsuarioValido(session, "04") || metodos3.esTipoUsuarioValido(session, "05") || metodos3.esTipoUsuarioValido(session, "06")))
			  {
				  throw new Error("Usuario sin derechos de cancelar p?liza");
			  }

			  //Si quien quiere borrar la p?liza no es alquien que revisa las p?lizas, ni el director de contabilidad
			  if (!metodos3.esTipoUsuarioValido(session,"05") && !metodos3.esTipoUsuarioValido(session, "06"))
			  {
				  //Verificar si es el creador de la p?liza que pretende borrar
				  COMANDO = "SELECT COUNT(*) NReg ";
				  COMANDO += "FROM  CONT_POLIZA ";
				  COMANDO += "WHERE ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_LIBRO = ? ";
				  COMANDO += "AND ID_CCOSTO = ? ";
				  COMANDO += "AND FOLIO = ? ";
				  COMANDO += "AND ID_USUARIO = ? ";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strIDEjercicio);
				  pstmt.setString(2, strIDLibro);
				  pstmt.setString(3, strIDCCosto);
				  pstmt.setString(4, strFolio);
				  pstmt.setLong(5, idUsuario);
				  rset = pstmt.executeQuery();

				  int intNReg = 0;

				  if (rset.next())
				  {
					  intNReg = rset.getInt("NReg");
				  }
				  rset.close();
				  pstmt.close();

				  if (intNReg == 0)
					  throw new Error("Imposible borrar una p?liza no creada por "+strUsuario);
			  }

			  //Si la p?liza tiene movimientos se borran los movimientos
			  if (polizaConMovimientos(strIDEjercicio, strIDLibro, strIDCCosto, strFolio))
			  {
				  COMANDO = "DELETE ";
				  COMANDO += "FROM  CONT_MOVIMIENTO ";
				  COMANDO += "WHERE ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_LIBRO = ? ";
				  COMANDO += "AND ID_CCOSTO = ? ";
				  COMANDO += "AND FOLIO = ? ";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strIDEjercicio);
				  pstmt.setString(2, strIDLibro);
				  pstmt.setString(3, strIDCCosto);
				  pstmt.setString(4, strFolio);
				  pstmt.execute();
				  pstmt.close();
			  }
			  //Borrar encabezado de la p?liza
			  COMANDO = "DELETE ";
			  COMANDO += "FROM  CONT_POLIZA ";
			  COMANDO += "WHERE ID_EJERCICIO = ? ";
			  COMANDO += "AND ID_LIBRO = ? ";
			  COMANDO += "AND ID_CCOSTO = ? ";
			  COMANDO += "AND FOLIO = ? ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strIDEjercicio);
			  pstmt.setString(2, strIDLibro);
			  pstmt.setString(3, strIDCCosto);
			  pstmt.setString(4, strFolio);
			  pstmt.execute();
			  pstmt.close();
		  }
		  else if (strStatus.equals("R"))
		  {
		   //Si la p?liza est? en revisi?n, solo el Director de Contabilidad
		   //y el Revisa P?lizas pueden cancelar la p?liza
			  if (!(metodos3.esTipoUsuarioValido(session, "05") || metodos3.esTipoUsuarioValido(session, "06")))
			  {
				  throw new Error("Usuario sin derechos de cancelar p?liza");
			  }

			  //Si la p?liza tiene movimientos se modifican
			  if (polizaConMovimientos(strIDEjercicio, strIDLibro, strIDCCosto, strFolio))
			  {
								  String strDescripcion = "MOVIMIENTO CANCELADO POR "+strUsuario+" EN ";
				  COMANDO = "UPDATE CONT_MOVIMIENTO ";
				  COMANDO += "SET STATUS = 'I', ";
				  COMANDO += "IMPORTE = 0, ";
				  COMANDO += "DESCRIPCION = ? || TO_CHAR(SYSDATE, 'DD/MM/YY') ";
				  COMANDO += "WHERE ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_LIBRO = ? ";
				  COMANDO += "AND ID_CCOSTO = ? ";
				  COMANDO += "AND FOLIO = ? ";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strDescripcion);
								  pstmt.setString(2, strIDEjercicio);
				  pstmt.setString(3, strIDLibro);
				  pstmt.setString(4, strIDCCosto);
				  pstmt.setString(5, strFolio);
				  pstmt.execute();
				  pstmt.close();
			  }

			  //Cancelar encabezado de la p?liza
						  String strDescripcion = "POLIZA CANCELADA POR "+strUsuario+" EN ";
			  COMANDO = "UPDATE CONT_POLIZA ";
			  COMANDO += "SET STATUS = 'I', ";
			  COMANDO += "DESCRIPCION = ? || TO_CHAR(SYSDATE, 'DD/MM/YY') ";
			  COMANDO += "WHERE ID_EJERCICIO = ? ";
			  COMANDO += "AND ID_LIBRO = ? ";
			  COMANDO += "AND ID_CCOSTO = ? ";
			  COMANDO += "AND FOLIO = ? ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strDescripcion);
						  pstmt.setString(2, strIDEjercicio);
			  pstmt.setString(3, strIDLibro);
			  pstmt.setString(4, strIDCCosto);
			  pstmt.setString(5, strFolio);
			  pstmt.execute();
			  pstmt.close();
		  }
		  else if (strStatus.equals("C"))
		  {
		   //Si la p?liza est? cerrada y el usuario es el Director de Contabilidad
			  //Validar el usuario
			  if (!metodos3.esTipoUsuarioValido(session, "06"))
			  {
				  throw new Error("El usuario no tiene derechos de cancelar una p?liza cerrada");
			  }

			  //Si la p?liza tiene movimientos se modifican
			  if (polizaConMovimientos(strIDEjercicio, strIDLibro, strIDCCosto, strFolio))
			  {
				  COMANDO = "UPDATE CONT_MOVIMIENTO ";
				  COMANDO += "SET STATUS = 'I', ";
				  COMANDO += "IMPORTE = 0, ";
				  COMANDO += "DESCRIPCION = ";
				  COMANDO += "'MOVIMIENTO CANCELADO POR ' || strUsuario || ' EN ' || TO_CHAR(SYSDATE, 'DD/MM/YY') ";
				  COMANDO += "WHERE ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_LIBRO = ? ";
				  COMANDO += "AND ID_CCOSTO = ? ";
				  COMANDO += "AND FOLIO = ? ";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strIDEjercicio);
				  pstmt.setString(2, strIDLibro);
				  pstmt.setString(3, strIDCCosto);
				  pstmt.setString(4, strFolio);
				  pstmt.execute();
				  pstmt.close();
			  }
			  //Cancelar encabezado de la p?liza
			  COMANDO = "UPDATE CONT_POLIZA ";
			  COMANDO += "SET STATUS = 'I', ";
			  COMANDO += "DESCRIPCION = ";
			  COMANDO += "'POLIZA CANCELADA POR ' || strUsuario || 'EN' || TO_CHAR(SYSDATE, 'DD/MM/YY') ";
			  COMANDO += "WHERE ID_EJERCICIO = ? ";
			  COMANDO += "AND ID_LIBRO = ? ";
			  COMANDO += "AND ID_CCOSTO = ? ";
			  COMANDO += "AND FOLIO = ? ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strIDEjercicio);
			  pstmt.setString(2, strIDLibro);
			  pstmt.setString(3, strIDCCosto);
			  pstmt.setString(4, strFolio);
			  pstmt.execute();
			  pstmt.close();
		  }
		  else if (strStatus.equals("D"))
		  {
		   //Si la p?liza est? cerrada definitivamente, no se puede cerrar
			  throw new Error("P?liza est? cerrada de manera definitiva.  No se puede cancelar");
		  }
		  else if (strStatus.equals("I"))
		  {
		   //Si la p?liza est? cancelada, no se puede cerrar
			  throw new Error("P?liza previamente cancelada.");
		  }
	  }
	  
//	M?todos del caso de uso Alta Movimiento
	  public void movimientoAlta
		  (
		   String strFolio, Integer concepto,
		   String strDescripcion, double dblImporte, String strNaturaleza,
		   String strReferencia, String strReferencia2, String strIDCtaMayor,
		   String strIDCCostoMov, String strIDAuxiliar, HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo permite dar de alta un movimiento de una p?liza
		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
		  
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDLibro = (String)session.getAttribute("id_libro");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strStatus = null;
		  
		  //Validar usuario
		  //Solo los usuarios registrados en el sistema de contabilidad pueden capturar
		  //movimientos, entre ellos otros sistemas
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no tiene derecho de crear movimientos");
		  }
		 
		  //Validar que la entidad del movimiento sea la misma que la entidad de la p?liza
		  if (!strIDCCosto.equals(getEntidadValida(strIDEjercicio, strIDCCostoMov)))
			  throw new Error("Entidad de movimiento distinta a entidad de poliza! "+strIDCCosto+" "+strIDEjercicio+" "+strIDCCostoMov+" "+strIDAuxiliar);

		  //Validar si la cuenta existe en la tabla de cont_relacion
		  if (!metodos2.rctaExisteCuenta(strIDCtaMayor, strIDCCostoMov, strIDAuxiliar, session).booleanValue())
			  throw new Error("Puede que la cuenta no este tecleada correctamente, ya que no existe!<br>"+strIDCtaMayor+"-"+strIDCCostoMov+"-"+strIDAuxiliar);

		  //Validar el status de la p?liza
		  getDatosPoliza(strIDEjercicio, strIDLibro, strIDCCosto, strFolio);
		  strStatus = getStatusPolizaInd();

		  //la obtencion y modifcacion del recibo actual se hara en grabaCaja.jsp
		  //porque se insertan dos movimientos

		  //Validar si la cuenta de mayor tiene su auxiliar, si es que lo debe llevar
		  tieneCtaMayor_Auxiliar(strIDCtaMayor, strIDAuxiliar, session);

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  if (strStatus.equals("A"))
		  {//Cualquier usuario de contabilidad puede agregar movimientos
						  Integer intNMov = new Integer(0);

						  //Obtener el numero de movimiento
						  COMANDO = "SELECT (COALESCE(MAX(NUMMOVTO),0)+1) NUMMOVTO ";
						  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
						  COMANDO += "WHERE ID_EJERCICIO = ? ";
						  COMANDO += "AND ID_LIBRO = ? ";
						  COMANDO += "AND ID_CCOSTO = ? ";
						  COMANDO += "AND FOLIO = ? ";

						  pstmt = conn.prepareStatement(COMANDO);
						  pstmt.setString(1, strIDEjercicio);
			  pstmt.setString(2, strIDLibro);
			  pstmt.setString(3, strIDCCosto);
			  pstmt.setString(4, strFolio);
						  rset = pstmt.executeQuery();

						  if (rset.next())
						  {
								  intNMov = new Integer(rset.getInt("NumMovto"));
						  }
						  rset.close();
						  pstmt.close();

						  //El campo Fecha del movimiento, se asignan
			  //en el trigger de inserci?n de cont_movimiento
			  COMANDO = "INSERT INTO MATEO.CONT_MOVIMIENTO ";
			  COMANDO += "(ID_EJERCICIO, ID_LIBRO, ID_CCOSTO, FOLIO,  NUMMOVTO, FECHA, CONCEPTO_ID, DESCRIPCION, ";
			  COMANDO += "IMPORTE, NATURALEZA, REFERENCIA, REFERENCIA2, ID_CTAMAYORM, ";
			  COMANDO += "ID_CCOSTOM, ID_AUXILIARM, ID_EJERCICIOM, ID_EJERCICIOM2, ID_EJERCICIOM3, ";
			  COMANDO += "TIPO_CUENTA, ID_EJERCICIO2 ) ";
			  COMANDO += "VALUES ";
			  COMANDO += "(?, ?, ?, ?, ?, to_date(?,'dd/mm/yy'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strIDEjercicio);
			  pstmt.setString(2, strIDLibro);
			  pstmt.setString(3, strIDCCosto);
			  pstmt.setString(4, strFolio);
			  pstmt.setInt(5, intNMov.intValue());
              pstmt.setString(6, getFechaPoliza());
              pstmt.setInt (7, concepto.intValue ());
			  pstmt.setString(8, strDescripcion);
			  pstmt.setDouble(9, dblImporte);
			  pstmt.setString(10, strNaturaleza);
			  pstmt.setString(11, strReferencia);
			  pstmt.setString(12, strReferencia2);
			  pstmt.setString(13, strIDCtaMayor);
			  pstmt.setString(14, strIDCCostoMov);
			  pstmt.setString(15, strIDAuxiliar);
			  pstmt.setString(16, strIDEjercicio);
			  pstmt.setString(17, strIDEjercicio);
			  pstmt.setString(18, strIDEjercicio);
			  pstmt.setString(19, new CtaMayor().getTipoCuenta(strIDEjercicio, strIDCtaMayor));
			  pstmt.setString(20, strIDEjercicio);
			  pstmt.execute();
			  pstmt.close();

						  if (strReferencia2 != null && strReferencia2.length() > 2)
                        {
                                if (strReferencia2.substring(0,2).equals("99"))
                                {
                                        String strFecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

                                        try{
                                            //Verificar si el dato capturado en Referencia2 es un control presupuestal
                                            metsCCP.ccpDescomprometerAut(strIDLibro, strFolio, intNMov, strFecha, strIDCtaMayor, strIDCCostoMov, strIDAuxiliar,
                                                    new Double(dblImporte), strDescripcion, strReferencia2, session);
                                        }catch(Exception e){
                                            e.printStackTrace();
                                            movimientoCancela(strFolio, intNMov, session);
                                            throw new  Exception("Error al intentar registar el movimiento <br> "+e);
                                        }
                                }
                        }
		  }
		  
		  else if (strStatus.equals("C"))
		  {//Si la p?liza est? cerrada no se pueden agregar movimientos
			  throw new Error("No se puede crear movimiento en p?liza cerrada "+strFolio);
		  }
		  else if (getStatusPolizaInd().equals("D"))
		  {//Si la p?liza est? cerrada definitivamente, no se pueden agregar movimientos
			  throw new Error("No se puede crear movimiento en p?liza cerrada definitivamente");
		  }
		  else if (getStatusPolizaInd().equals("I"))
		  {//Si la p?liza est? cancelada, no se pueden agregar movimientos
			  throw new Error("No se puede crear movimiento en p?liza cancelada");
		  }
	  }
	  
	  
//	M?todos del caso de uso Alta Movimiento Acumulando importe de cuentas iguales
	  public void movimientoAltaAcumulado
		  (
		   String strFolio, Integer concepto,
		   String strDescripcion, double dblImporte, String strNaturaleza,
		   String strReferencia, String strReferencia2, String strIDCtaMayor,
		   String strIDCCostoMov, String strIDAuxiliar, HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo permite dar de alta un movimiento de una p?liza
		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
		  int intNReg = 0;
          Integer intNMov = new Integer(0);

		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDLibro = (String)session.getAttribute("id_libro");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strStatus = null;

		  //Validar usuario
		  //Solo los usuarios registrados en el sistema de contabilidad pueden capturar
		  //movimientos, entre ellos otros sistemas
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no tiene derecho de crear movimientos");
		  }

		  //Validar que la entidad del movimiento sea la misma que la entidad de la p?liza
		  if (!strIDCCosto.equals(getEntidadValida(strIDEjercicio, strIDCCostoMov)))
			  throw new Error("Entidad de movimiento distinta a entidad de poliza!");

		  //Validar si la cuenta existe en la tabla de cont_relacion
		  if (!metodos2.rctaExisteCuenta(strIDCtaMayor, strIDCCostoMov, strIDAuxiliar, session).booleanValue())
			  throw new Error("Puede que la cuenta no este tecleada correctamente, ya que no existe!<br>"+strIDCtaMayor+"-"+strIDCCostoMov+"-"+strIDAuxiliar);

		  //Validar el status de la p?liza
		  getDatosPoliza(strIDEjercicio, strIDLibro, strIDCCosto, strFolio);
		  strStatus = getStatusPolizaInd();

		  //la obtencion y modifcacion del recibo actual se hara en grabaCaja.jsp
		  //porque se insertan dos movimientos

		  //Validar si la cuenta de mayor tiene su auxiliar, si es que lo debe llevar
		  tieneCtaMayor_Auxiliar(strIDCtaMayor, strIDAuxiliar, session);

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  if (strStatus.equals("A"))
		  {//Cualquier usuario de contabilidad puede agregar movimientos
						  //Validar si ya existe un movimiento con esta cuenta y naturaleza
						  COMANDO = "SELECT COALESCE(NUMMOVTO,0) NREG ";
						  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
						  COMANDO += "WHERE ID_EJERCICIO = ? ";
						  COMANDO += "AND ID_LIBRO = ? ";
						  COMANDO += "AND ID_CCOSTO = ? ";
						  COMANDO += "AND FOLIO = ? ";
						  COMANDO += "AND ID_CTAMAYORM = ? ";
						  COMANDO += "AND ID_CCOSTOM = ? ";
			  COMANDO += "AND NATURALEZA = ? ";
						  COMANDO += "AND ID_AUXILIARM = ? ";
						  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strIDEjercicio);
			  pstmt.setString(2, strIDLibro);
			  pstmt.setString(3, strIDCCosto);
			  pstmt.setString(4, strFolio);
			  pstmt.setString(5, strIDCtaMayor);
			  pstmt.setString(6, strIDCCostoMov);
			  pstmt.setString(7, strNaturaleza);
			  pstmt.setString(8, strIDAuxiliar);
						  rset = pstmt.executeQuery();

						  if (rset.next())
						  {
								  intNReg = rset.getInt("NReg");
						  }
						  pstmt.close();
						  rset.close();

						  if (intNReg == 0)
						  {
								  

								  //Obtener el numero de movimiento
								  COMANDO = "SELECT (COALESCE(MAX(NUMMOVTO),0)+1) NUMMOVTO ";
								  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
								  COMANDO += "WHERE ID_EJERCICIO = ? ";
								  COMANDO += "AND ID_LIBRO = ? ";
								  COMANDO += "AND ID_CCOSTO = ? ";
								  COMANDO += "AND FOLIO = ? ";

								  pstmt = conn.prepareStatement(COMANDO);
								  pstmt.setString(1, strIDEjercicio);
								  pstmt.setString(2, strIDLibro);
								  pstmt.setString(3, strIDCCosto);
								  pstmt.setString(4, strFolio);
								  rset = pstmt.executeQuery();

								  if (rset.next())
								  {
										  intNMov = new Integer(rset.getInt("NumMovto"));
								  }
								  rset.close();
								  pstmt.close();

								  //Los campos NumMovto y Fecha del movimiento, se asignan
								  //en el trigger de inserci?n de cont_movimiento
								  COMANDO = "INSERT INTO MATEO.CONT_MOVIMIENTO ";
								  COMANDO += "(ID_EJERCICIO, ID_LIBRO, ID_CCOSTO, FOLIO, NUMMOVTO, CONCEPTO_ID, DESCRIPCION, IMPORTE, ";
								  COMANDO += " NATURALEZA, REFERENCIA, REFERENCIA2, ID_CTAMAYORM, ID_CCOSTOM, ";
								  COMANDO += " ID_AUXILIARM, ID_EJERCICIOM, ID_EJERCICIOM2, ID_EJERCICIOM3, ";
								  COMANDO += " TIPO_CUENTA, ID_EJERCICIO2 )";
								  COMANDO += " VALUES ";
								  COMANDO += "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
								  pstmt = conn.prepareStatement(COMANDO);
								  pstmt.setString(1, strIDEjercicio);
								  pstmt.setString(2, strIDLibro);
								  pstmt.setString(3, strIDCCosto);
								  pstmt.setString(4, strFolio);
								  pstmt.setInt(5, intNMov.intValue());
                                                                  pstmt.setInt(6, concepto.intValue ());
								  pstmt.setString(7, strDescripcion);
								  pstmt.setDouble(8, dblImporte);
								  pstmt.setString(9, strNaturaleza);
								  pstmt.setString(10, strReferencia);
								  pstmt.setString(11,strReferencia2);
								  pstmt.setString(12, strIDCtaMayor);
								  pstmt.setString(13, strIDCCostoMov);
								  pstmt.setString(14, strIDAuxiliar);
								  pstmt.setString(15, strIDEjercicio);
								  pstmt.setString(16, strIDEjercicio);
								  pstmt.setString(17, strIDEjercicio);
								  pstmt.setString(18, new CtaMayor().getTipoCuenta(strIDEjercicio, strIDCtaMayor));
								  pstmt.setString(19, strIDEjercicio);
								  pstmt.execute();
								  pstmt.close();
						  }
						  else
						  {
                                  intNMov = intNReg;
                                  
								  COMANDO = "UPDATE MATEO.CONT_MOVIMIENTO ";
								  COMANDO += "SET IMPORTE = IMPORTE + ? ";
								  COMANDO += "WHERE ID_EJERCICIO = ? ";
								  COMANDO += "AND ID_LIBRO = ? ";
								  COMANDO += "AND ID_CCOSTO = ? ";
								  COMANDO += "AND FOLIO = ? ";
								  COMANDO += "AND ID_CTAMAYORM = ? ";
								  COMANDO += "AND ID_CCOSTOM = ? ";
								  COMANDO += "AND NATURALEZA = ? ";
								  COMANDO += "AND ID_AUXILIARM = ? ";
								  pstmt = conn.prepareStatement(COMANDO);
								  pstmt.setDouble(1, dblImporte);
								  pstmt.setString(2, strIDEjercicio);
								  pstmt.setString(3, strIDLibro);
								  pstmt.setString(4, strIDCCosto);
								  pstmt.setString(5, strFolio);
								  pstmt.setString(6, strIDCtaMayor);
								  pstmt.setString(7, strIDCCostoMov);
								  pstmt.setString(8, strNaturaleza);
				  pstmt.setString(9, strIDAuxiliar);
								  pstmt.execute();
								  pstmt.close();
						  }

                          if (strReferencia2 != null && strReferencia2.length() > 2)
                        {
                                if (strReferencia2.substring(0,2).equals("99"))
                                {
                                        String strFecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

                                        try{
                                            //Verificar si el dato capturado en Referencia2 es un control presupuestal
                                            metsCCP.ccpDescomprometerAut(strIDLibro, strFolio, intNMov, strFecha, strIDCtaMayor, strIDCCostoMov, strIDAuxiliar,
                                                    new Double(dblImporte), strDescripcion, strReferencia2, session);
                                        }catch(Exception e){
                                            e.printStackTrace();
                                            movimientoCancela(strFolio, intNMov, session);
                                            throw new  Exception("Error al intentar registar el movimiento <br> "+e);
                                        }
                                }
                        }
		  }
		  
		  else if (strStatus.equals("C"))
		  {//Si la p?liza est? cerrada no se pueden agregar movimientos
			  throw new Error("No se puede crear movimiento en p?liza cerrada");
		  }
		  else if (getStatusPolizaInd().equals("D"))
		  {//Si la p?liza est? cerrada definitivamente, no se pueden agregar movimientos
			  throw new Error("No se puede crear movimiento en p?liza cerrada definitivamente");
		  }
		  else if (getStatusPolizaInd().equals("I"))
		  {//Si la p?liza est? cancelada, no se pueden agregar movimientos
			  throw new Error("No se puede crear movimiento en p?liza cancelada");
		  }
	  }
	  
//	M?todos del caso de uso Alta Movimiento Acumulando importe de cuentas iguales
		  //tomando en cuenta la naturaleza y descripcion del movimiento.
		  //Se utiliza en el caso de quere acumular importes en cuentas iguales pero bajo
		  //conceptos diferentes.
	  public void movimientoAltaAcumuladoD
		  (
		   String strFolio, Integer concepto,
		   String strDescripcion, double dblImporte, String strNaturaleza,
		   String strReferencia, String strReferencia2, String strIDCtaMayor,
		   String strIDCCostoMov, String strIDAuxiliar, HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo permite dar de alta un movimiento de una p?liza
		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
				  int intNReg = 0;
          Integer intNMov = new Integer(0);

		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDLibro = (String)session.getAttribute("id_libro");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strStatus = null;

		  //Validar usuario
		  //Solo los usuarios registrados en el sistema de contabilidad pueden capturar
		  //movimientos, entre ellos otros sistemas
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no tiene derecho de crear movimientos");
		  }

		  //Validar que la entidad del movimiento sea la misma que la entidad de la p?liza
		  if (!strIDCCosto.equals(getEntidadValida(strIDEjercicio, strIDCCostoMov)))
			  throw new Error("Entidad de movimiento distinta a entidad de poliza!");

		  //Validar si la cuenta existe en la tabla de cont_relacion
		  if (!metodos2.rctaExisteCuenta(strIDCtaMayor, strIDCCostoMov, strIDAuxiliar, session).booleanValue())
			  throw new Error("Puede que la cuenta no este tecleada correctamente, ya que no existe!<br>"+strIDCtaMayor+"-"+strIDCCostoMov+"-"+strIDAuxiliar);

		  //Validar el status de la p?liza
		  getDatosPoliza(strIDEjercicio, strIDLibro, strIDCCosto, strFolio);
		  strStatus = getStatusPolizaInd();

		  //la obtencion y modifcacion del recibo actual se hara en grabaCaja.jsp
		  //porque se insertan dos movimientos

		  //Validar si la cuenta de mayor tiene su auxiliar, si es que lo debe llevar
		  tieneCtaMayor_Auxiliar(strIDCtaMayor, strIDAuxiliar, session);

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  if (strStatus.equals("A"))
		  {


                      //Cualquier usuario de contabilidad puede agregar movimientos
						  //Validar si ya existe un movimiento con esta cuenta y naturaleza
						  COMANDO = "SELECT COALESCE(NUMMOVTO,0) NREG ";
						  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
						  COMANDO += "WHERE ID_EJERCICIO = ? ";
						  COMANDO += "AND ID_LIBRO = ? ";
						  COMANDO += "AND ID_CCOSTO = ? ";
						  COMANDO += "AND FOLIO = ? ";
						  COMANDO += "AND ID_CTAMAYORM = ? ";
						  COMANDO += "AND ID_CCOSTOM = ? ";
			  COMANDO += "AND NATURALEZA = ? ";
						  COMANDO += "AND DESCRIPCION = ? ";
						  COMANDO += "AND ID_AUXILIARM = ? ";
						  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strIDEjercicio);
			  pstmt.setString(2, strIDLibro);
			  pstmt.setString(3, strIDCCosto);
			  pstmt.setString(4, strFolio);
			  pstmt.setString(5, strIDCtaMayor);
			  pstmt.setString(6, strIDCCostoMov);
			  pstmt.setString(7, strNaturaleza);
						  pstmt.setString(8, strDescripcion);
			  pstmt.setString(9, strIDAuxiliar);
						  rset = pstmt.executeQuery();

						  if (rset.next())
						  {
								  intNReg = rset.getInt("NReg");
						  }
						  pstmt.close();
						  rset.close();

						  if (intNReg == 0)
						  {
								  

								  //Obtener el numero de movimiento
								  COMANDO = "SELECT (COALESCE(MAX(NUMMOVTO),0)+1) NUMMOVTO ";
								  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
								  COMANDO += "WHERE ID_EJERCICIO = ? ";
								  COMANDO += "AND ID_LIBRO = ? ";
								  COMANDO += "AND ID_CCOSTO = ? ";
								  COMANDO += "AND FOLIO = ? ";

								  pstmt = conn.prepareStatement(COMANDO);
								  pstmt.setString(1, strIDEjercicio);
								  pstmt.setString(2, strIDLibro);
								  pstmt.setString(3, strIDCCosto);
								  pstmt.setString(4, strFolio);
								  rset = pstmt.executeQuery();

								  if (rset.next())
								  {
										  intNMov = new Integer(rset.getInt("NumMovto"));
								  }
								  rset.close();
								  pstmt.close();

                                                                  if (strReferencia2 != null && strReferencia2.length() > 2) {
                                                                      if (strReferencia2.substring(0, 2).equals("99")) {
                                                                          String strFecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

                                                                          try {
                                                                              //Verificar si el dato capturado en Referencia2 es un control presupuestal
                                                                              metsCCP.ccpDescomprometerAut(strIDLibro, strFolio, intNMov, strFecha, strIDCtaMayor, strIDCCostoMov, strIDAuxiliar,
                                                                                      new Double(dblImporte), strDescripcion, strReferencia2, session);
                                                                          } catch (Exception e) {
                                                                              e.printStackTrace();
                                                                              //movimientoCancela(strFolio, intNMov, session);
                                                                              throw new Exception("Error al intentar registar el movimiento <br> " + e);
                                                                          }
                                                                      }
                                                                  }


								  //Los campos NumMovto y Fecha del movimiento, se asignan
								  //en el trigger de inserci?n de cont_movimiento
								  COMANDO = "INSERT INTO MATEO.CONT_MOVIMIENTO ";
								  COMANDO += "(ID_EJERCICIO, ID_LIBRO, ID_CCOSTO, FOLIO, NUMMOVTO, CONCEPTO_ID, DESCRIPCION, IMPORTE, ";
								  COMANDO += " NATURALEZA, REFERENCIA, REFERENCIA2, ID_CTAMAYORM, ID_CCOSTOM, ";
								  COMANDO += " ID_AUXILIARM, ID_EJERCICIOM, ID_EJERCICIOM2, ID_EJERCICIOM3, ";
								  COMANDO += " TIPO_CUENTA, ID_EJERCICIO2 )";
								  COMANDO += " VALUES ";
								  COMANDO += "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
								  pstmt = conn.prepareStatement(COMANDO);
								  pstmt.setString(1, strIDEjercicio);
								  pstmt.setString(2, strIDLibro);
								  pstmt.setString(3, strIDCCosto);
								  pstmt.setString(4, strFolio);
								  pstmt.setInt(5, intNMov.intValue());
                                                                  pstmt.setInt (6, concepto.intValue ());
								  pstmt.setString(7, strDescripcion);
								  pstmt.setDouble(8, dblImporte);
								  pstmt.setString(9, strNaturaleza);
								  pstmt.setString(10, strReferencia);
								  pstmt.setString(11, strReferencia2);
								  pstmt.setString(12, strIDCtaMayor);
								  pstmt.setString(13, strIDCCostoMov);
								  pstmt.setString(14, strIDAuxiliar);
								  pstmt.setString(15, strIDEjercicio);
								  pstmt.setString(16, strIDEjercicio);
								  pstmt.setString(17, strIDEjercicio);
								  pstmt.setString(18, new CtaMayor().getTipoCuenta(strIDEjercicio, strIDCtaMayor));
								  pstmt.setString(19, strIDEjercicio);
								  pstmt.execute();
								  pstmt.close();

						  }
						  else
						  {
                              intNMov = intNReg;

                                                                if (strReferencia2 != null && strReferencia2.length() > 2) {
                                                                      if (strReferencia2.substring(0, 2).equals("99")) {
                                                                          String strFecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

                                                                          try {
                                                                              //Verificar si el dato capturado en Referencia2 es un control presupuestal
                                                                              metsCCP.ccpDescomprometerAut(strIDLibro, strFolio, intNMov, strFecha, strIDCtaMayor, strIDCCostoMov, strIDAuxiliar,
                                                                                      new Double(dblImporte), strDescripcion, strReferencia2, session);
                                                                          } catch (Exception e) {
                                                                              e.printStackTrace();
                                                                              //movimientoCancela(strFolio, intNMov, session);
                                                                              throw new Exception("Error al intentar registar el movimiento <br> " + e);
                                                                          }
                                                                      }
                                                                  }

								  COMANDO = "UPDATE MATEO.CONT_MOVIMIENTO ";
								  COMANDO += "SET IMPORTE = IMPORTE + ? ";
								  COMANDO += "WHERE ID_EJERCICIO = ? ";
								  COMANDO += "AND ID_LIBRO = ? ";
								  COMANDO += "AND ID_CCOSTO = ? ";
								  COMANDO += "AND FOLIO = ? ";
								  COMANDO += "AND ID_CTAMAYORM = ? ";
								  COMANDO += "AND ID_CCOSTOM = ? ";
								  COMANDO += "AND NATURALEZA = ? ";
								  COMANDO += "AND DESCRIPCION = ? ";
					  COMANDO += "AND ID_AUXILIARM = ? ";
								  pstmt = conn.prepareStatement(COMANDO);
								  pstmt.setDouble(1, dblImporte);
								  pstmt.setString(2, strIDEjercicio);
								  pstmt.setString(3, strIDLibro);
								  pstmt.setString(4, strIDCCosto);
								  pstmt.setString(5, strFolio);
								  pstmt.setString(6, strIDCtaMayor);
								  pstmt.setString(7, strIDCCostoMov);
								  pstmt.setString(8, strNaturaleza);
								  pstmt.setString(9, strDescripcion);
					  pstmt.setString(10, strIDAuxiliar);
								  pstmt.execute();
								  pstmt.close();
						  }

                        
		  }
		  
		  else if (strStatus.equals("C"))
		  {//Si la p?liza est? cerrada no se pueden agregar movimientos
			  throw new Error("No se puede crear movimiento en p?liza cerrada");
		  }
		  else if (getStatusPolizaInd().equals("D"))
		  {//Si la p?liza est? cerrada definitivamente, no se pueden agregar movimientos
			  throw new Error("No se puede crear movimiento en p?liza cerrada definitivamente");
		  }
		  else if (getStatusPolizaInd().equals("I"))
		  {//Si la p?liza est? cancelada, no se pueden agregar movimientos
			  throw new Error("No se puede crear movimiento en p?liza cancelada");
		  }
	  }
	  
//	M?todos del caso de uso Modifica Movimiento
	  public void movimientoModifica
		  (
		   String strFolio, int intNumMovto,
		   String strFecha, Integer concepto, String strDescripcion, double dblImporte,
		   String strNaturaleza,  String strReferencia, String strReferencia2,
		   String strIDCtaMayor,  String strIDCCostoMov, String strIDAuxiliar,
		   HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo permite modificar un movimiento de una p?liza
		  PreparedStatement pstmt = null;
		  String COMANDO = null;

		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDLibro = (String)session.getAttribute("id_libro");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strStatus = null;

		  //Validar usuario
		  //Solo los usuarios capturista de p?lizas, revisa p?lizas y director de contabilidad
		  //pueden modificar una p?liza
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no tiene derecho de modificar movimientos");
		  }

		  //Validar que la entidad del movimiento sea la misma que la entidad de la p?liza
		  if (!strIDCCosto.equals(getEntidadValida(strIDEjercicio, strIDCCostoMov)))
			  throw new Error("Entidad de movimiento distinta a entidad de poliza!");

		  //Validar si la cuenta existe en la tabla de cont_relacion
		  if (!metodos2.rctaExisteCuenta(strIDCtaMayor, strIDCCostoMov, strIDAuxiliar, session).booleanValue())
			  throw new Error("Puede que la cuenta no este tecleada correctamente, ya que no existe!");

		  //Validar el status de la p?liza
		  getDatosPoliza(strIDEjercicio, strIDLibro, strIDCCosto, strFolio);
		  strStatus = getStatusPolizaInd();

		  //Validar si la cuenta de mayor tiene su auxiliar, si es que lo debe llevar
		  tieneCtaMayor_Auxiliar(strIDCtaMayor, strIDAuxiliar, session);

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  if (strStatus.equals("A"))
		  {//Cualquiera de los tres usuarios puede modificar un movimiento
			  if (!(metodos3.esTipoUsuarioValido(session, "04") || metodos3.esTipoUsuarioValido(session, "05") || metodos3.esTipoUsuarioValido(session, "06")))
			  {
				  throw new Error("Usuario sin derechos de modificar movimientos en una poliza");
			  }

			  COMANDO = "UPDATE MATEO.CONT_MOVIMIENTO ";
			  COMANDO += "SET FECHA = TO_DATE(?, 'dd-mm-yyyy'),  ";
                          COMANDO += "CONCEPTO_ID = ? ";
			  COMANDO += "DESCRIPCION = ?, IMPORTE = ?, ";
			  COMANDO += "NATURALEZA = ?, REFERENCIA = ?, ";
			  COMANDO += "REFERENCIA2 = ?, ID_CTAMAYORM = ?, ";
			  COMANDO += "ID_CCOSTOM = ?, ID_AUXILIARM = ? ";
			  COMANDO += "WHERE ID_EJERCICIO = ? ";
			  COMANDO += "AND ID_LIBRO = ? ";
			  COMANDO += "AND ID_CCOSTO = ? ";
			  COMANDO += "AND FOLIO = ? ";
			  COMANDO += "AND NUMMOVTO = ?";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strFecha);
                          pstmt.setInt(2, concepto.intValue ());
			  pstmt.setString(3, strDescripcion);
			  pstmt.setDouble(4, dblImporte);
			  pstmt.setString(5, strNaturaleza);
			  pstmt.setString(6, strReferencia);
			  pstmt.setString(7, strReferencia2);
			  pstmt.setString(8, strIDCtaMayor);
			  pstmt.setString(9, strIDCCostoMov);
			  pstmt.setString(10, strIDAuxiliar);
			  pstmt.setString(11, strIDEjercicio);
			  pstmt.setString(12, strIDLibro);
			  pstmt.setString(13, strIDCCosto);
			  pstmt.setString(14, strFolio);
			  pstmt.setInt(15, intNumMovto);
			  pstmt.execute();
			  pstmt.close();

						  //Borrar cualquier movimiento de descomprometido en control presupuestal
						  //ccpDescomprometerBorraAut(strIDLibro, strFolio, new Integer(intNumMovto), session);

						  if (strReferencia2 != null && strReferencia2.length() > 2)
						  {
								  if (strReferencia2.substring(0,2).equals("99"))
								  {
										  strFecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

										  //Verificar si el dato capturado en Referencia2 es un control presupuestal
										  //ccpDescomprometerAut(strIDLibro, strFolio, new Integer(intNumMovto), strFecha, strIDCtaMayor, strIDCCostoMov, strIDAuxiliar,
												  //new Double(dblImporte), strDescripcion, strReferencia2, session);
								  }
						  }
		  }
		  else if (strStatus.equals("R"))
		  {//Solo el revisa p?lizas y el director de contabilidad pueden modificar movimientos
			  //Validar usuario
			  if (!(metodos3.esTipoUsuarioValido(session, "05") || metodos3.esTipoUsuarioValido(session, "06")))
			  {
				  throw new Error("Usuario sin derechos para modificar movimientos en p?liza en revisi?n");
			  }

			  COMANDO = "UPDATE MATEO.CONT_MOVIMIENTO ";
			  COMANDO += "SET FECHA = TO_DATE(?, 'dd-mm-yyyy'), ";
			  COMANDO += "DESCRIPCION = ?, IMPORTE = ?, ";
			  COMANDO += "NATURALEZA = ?, REFERENCIA = ?, ";
			  COMANDO += "REFERENCIA2 = ?, ID_CTAMAYOR = ?, ";
			  COMANDO += "ID_CCOSTOM = ?, ID_AUXILIAR = ? ";
			  COMANDO += "WHERE ID_EJERCICIO = ? ";
			  COMANDO += "AND ID_LIBRO = ? ";
			  COMANDO += "AND ID_CCOSTO = ? ";
			  COMANDO += "AND FOLIO = ? ";
			  COMANDO += "AND NUMMOVTO = ?";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strFecha);
			  pstmt.setString(2, strDescripcion);
			  pstmt.setDouble(3, dblImporte);
			  pstmt.setString(4, strNaturaleza);
			  pstmt.setString(5, strReferencia);
			  pstmt.setString(6, strReferencia2);
			  pstmt.setString(7, strIDCtaMayor);
			  pstmt.setString(8, strIDCCostoMov);
			  pstmt.setString(9, strIDAuxiliar);
			  pstmt.setString(10, strIDEjercicio);
			  pstmt.setString(11, strIDLibro);
			  pstmt.setString(12, strIDCCosto);
			  pstmt.setString(13, strFolio);
			  pstmt.setInt(14, intNumMovto);
			  pstmt.execute();
			  pstmt.close();

						  //Borrar cualquier movimiento de descomprometido en control presupuestal
						  //ccpDescomprometerBorraAut(strIDLibro, strFolio, new Integer(intNumMovto), session);

						  if (strReferencia2 != null && strReferencia2.length() > 2)
						  {
								  if (strReferencia2.substring(0,2).equals("99"))
								  {
										  strFecha = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());

										  //Verificar si el dato capturado en Referencia2 es un control presupuestal
										  //ccpDescomprometerAut(strIDLibro, strFolio, new Integer(intNumMovto), strFecha, strIDCtaMayor, strIDCCostoMov, strIDAuxiliar,
												  //new Double(dblImporte), strDescripcion, strReferencia2, session);
								  }
						  }

		  }
		  else if (strStatus.equals("C"))
		  {//Si la p?liza est? cerrada no se pueden modificar movimientos
			  throw new Error("No se puede modificar movimiento en p?liza cerrada");
		  }
		  else if (strStatus.equals("D"))
		  {//Si la p?liza est? cerrada definitivamente, no se pueden modificar movimientos
			  throw new Error("No se puede modificar movimiento en p?liza cerrada definitivamente");
		  }
		  else if (strStatus.equals("I"))
		  {//Si la p?liza est? cancelada, no se pueden modificar movimientos
			  throw new Error("No se puede modificar movimiento en p?liza cancelada");
		  }
	  }
	  
//	M?todos del caso de uso Cancela Movimiento
	  public void movimientoCancela
		  (
		   String strFolio, int intNumMovto, HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo permite cancelar o borrar un movimiento dependiendo del status de la p?liza
		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;

		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDLibro = (String)session.getAttribute("id_libro");
		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  Long idUsuario = (Long)session.getAttribute(Constants.SESSION_USER_ID);
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strStatus = null;

		  //Validar usuario
		  //Solo los usuarios capturista de p?lizas, revisa p?lizas y director de contabilidad
		  //pueden modificar una p?liza
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no tiene derecho de cancelar movimientos");
		  }

		  //Validar el status de la p?liza
		  getDatosPoliza(strIDEjercicio, strIDLibro, strIDCCosto, strFolio);
		  strStatus = getStatusPolizaInd();

		  if (strStatus.equals("A"))	//si el usuario no es de caja se borra el movimiento
		  {//Cualquiera de los tres usuarios puede BORRAR un movimiento
			  if (!(metodos3.esTipoUsuarioValido(session, "04") || metodos3.esTipoUsuarioValido(session, "05") || metodos3.esTipoUsuarioValido(session, "06")
			  || metodos3.esTipoUsuarioValido(session, "01") || metodos3.esTipoUsuarioValido(session, "00")))
			  {
				  throw new Error("Usuario sin derechos de borrar movimientos");
			  }
			  if (metodos3.esTipoUsuarioValido(session, "01")) //si el usuario es de caja se cancela el movimiento
			  {
				  COMANDO = "UPDATE MATEO.CONT_MOVIMIENTO ";
				  COMANDO += "SET STATUS = 'T' ";
				  COMANDO += "WHERE ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_LIBRO = ? ";
				  COMANDO += "AND ID_CCOSTO = ? ";
				  COMANDO += "AND FOLIO = ? ";
				  COMANDO += "AND NUMMOVTO = ?";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strIDEjercicio);
				  pstmt.setString(2, strIDLibro);
				  pstmt.setString(3, strIDCCosto);
				  pstmt.setString(4, strFolio);
				  pstmt.setInt(5, intNumMovto);
				  pstmt.execute();
				  pstmt.close();
			  }
			  else if (metodos3.esTipoUsuarioValido(session, "00")) //si el usuario es de auditoria se cancela el movimiento
			  {
				  String strDescripcion = "Cancelado por "+ strUsuario +" en "+ (new java.util.Date()).toString();
				  COMANDO = "UPDATE MATEO.CONT_MOVIMIENTO ";
				  COMANDO += "SET STATUS = 'I', ";
				  COMANDO += "IMPORTE = 0, ";
				  COMANDO += "DESCRIPCION = ? ";
				  COMANDO += "WHERE ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_LIBRO = ? ";
				  COMANDO += "AND ID_CCOSTO = ? ";
				  COMANDO += "AND FOLIO = ? ";
				  COMANDO += "AND NUMMOVTO = ?";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strDescripcion);
				  pstmt.setString(2, strIDEjercicio);
				  pstmt.setString(3, strIDLibro);
				  pstmt.setString(4, strIDCCosto);
				  pstmt.setString(5, strFolio);
				  pstmt.setInt(6, intNumMovto);
				  pstmt.execute();
				  pstmt.close();
			  }
			  else
			  {
				  //Si quien quiere borrar la p?liza no es alquien que revisa las p?lizas, ni el director de contabilidad
				  if (!metodos3.esTipoUsuarioValido(session,"05") && !metodos3.esTipoUsuarioValido(session, "06"))
				  {
					  //Verificar si es el creador de la p?liza que pretende borrar
					  COMANDO = "SELECT COUNT(*) NReg ";
					  COMANDO += "FROM  CONT_POLIZA ";
					  COMANDO += "WHERE ID_EJERCICIO = ? ";
					  COMANDO += "AND ID_LIBRO = ? ";
					  COMANDO += "AND ID_CCOSTO = ? ";
					  COMANDO += "AND FOLIO = ? ";
					  COMANDO += "AND ID_USUARIO = ? ";
					  pstmt = conn.prepareStatement(COMANDO);
					  pstmt.setString(1, strIDEjercicio);
					  pstmt.setString(2, strIDLibro);
					  pstmt.setString(3, strIDCCosto);
					  pstmt.setString(4, strFolio);
					  pstmt.setLong(5, idUsuario);
					  rset = pstmt.executeQuery();

					  int intNReg = 0;

					  if (rset.next())
					  {
						  intNReg = rset.getInt("NReg");
					  }
					  rset.close();
					  pstmt.close();

					  if (intNReg == 0)
						  throw new Error("Imposible borrar una p?liza no creada por "+strUsuario);
				  }

								  //Borrar cualquier movimiento de descomprometido en control presupuestal
								  metsCCP.ccpDescomprometerBorraAut(strIDLibro, strFolio, new Integer(intNumMovto), session);

								  //Cuando la p?liza est? abierta, la acci?n de cancelar movimiento
				  //ocasiona que se borre f?sicamente el movimiento
				  COMANDO = "DELETE ";
				  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
				  COMANDO += "WHERE ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_LIBRO = ? ";
				  COMANDO += "AND ID_CCOSTO = ? ";
				  COMANDO += "AND FOLIO = ? ";
				  COMANDO += "AND NUMMOVTO = ?";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strIDEjercicio);
				  pstmt.setString(2, strIDLibro);
				  pstmt.setString(3, strIDCCosto);
				  pstmt.setString(4, strFolio);
				  pstmt.setInt(5, intNumMovto);
				  pstmt.execute();
				  pstmt.close();
			  }
		  }
		  else if (strStatus.equals("R"))
		  {//Solo el revisa p?lizas y el director de contabilidad pueden CANCELAR movimientos
		   //Cuando la p?liza est? en revisi?n, los movimientos se cancelan, no se borran
			  //Validar usuario
			  if (!(metodos3.esTipoUsuarioValido(session, "04") || metodos3.esTipoUsuarioValido(session, "05") || metodos3.esTipoUsuarioValido(session, "06")))
			  {
				  throw new Error("Usuario sin derechos para cancelar movimientos en p?liza en revisi?n");
			  }

			  COMANDO = "UPDATE MATEO.CONT_MOVIMIENTO ";
			  COMANDO += "SET STATUS = 'I' ";
			  COMANDO += "WHERE ID_EJERCICIO = ? ";
			  COMANDO += "AND ID_LIBRO = ? ";
			  COMANDO += "AND ID_CCOSTO = ? ";
			  COMANDO += "AND FOLIO = ? ";
			  COMANDO += "AND NUMMOVTO = ?";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strIDEjercicio);
			  pstmt.setString(2, strIDLibro);
			  pstmt.setString(3, strIDCCosto);
			  pstmt.setString(4, strFolio);
			  pstmt.setInt(5, intNumMovto);
			  pstmt.execute();
			  pstmt.close();

						  //Borrar cualquier movimiento de descomprometido en control presupuestal
						  //ccpDescomprometerBorraAut(strIDLibro, strFolio, new Integer(intNumMovto), session);

		  }
		  else if (strStatus.equals("C"))
		  {//Si la p?liza est? cerrada no se pueden modificar movimientos
			  throw new Error("No se puede cancelar movimiento en p?liza cerrada");
		  }
		  else if (strStatus.equals("D"))
		  {//Si la p?liza est? cerrada definitivamente, no se pueden modificar movimientos
			  throw new Error("No se puede cancelar movimiento en p?liza cerrada definitivamente");
		  }
		  else if (strStatus.equals("I"))
		  {//Si la p?liza est? cancelada, no se pueden modificar movimientos
			  throw new Error("P?liza previamente cancelada");
		  }
	  }
/*
	public ArrayList getLibros(String login, String ejercicio, String cCosto) throws SQLException{
		//Funcion que te devuelve los libros de un usuario
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		StringBuffer query = new StringBuffer();
		ArrayList libros= new ArrayList();
		DiferentesDatosVO libro;

		query.append("SELECT F.ID_LIBRO , L.NOMBRE FROM MATEO.CONT_FOLIO F,MATEO.CONT_LIBRO L ");
		query.append("WHERE F.ID_LIBRO=L.ID_LIBRO AND F.ID_EJERCICIO =L.ID_EJERCICIO AND "); 
		query.append("F.LOGIN=? AND F.ID_EJERCICIO=? AND ID_CCOSTO=? ");


		 try{
			 ps=conn.prepareStatement(query.toString());
			 ps.setString(1,login);
			 ps.setString(2,ejercicio);
			 ps.setString(3,cCosto);
			 rs = ps.executeQuery();


			 while(rs.next()){
			 	libro=new DiferentesDatosVO();
			 	libro.setId(rs.getString("ID_LIBRO"));
			 	libro.setDato(rs.getString("NOMBRE"));
			 	libros.add(libro);

			 }

		 }finally {
			 if (rs != null) { try { rs.close(); } catch(Exception e) {}}
			 if (ps != null) { try { ps.close(); } catch(Exception e) {}}
		 }
		 return libros;
	 }
*/
	  
//	M?todos de uso general
	   private boolean polizaCuadra
		(
		 String strIDEjercicio, String strIDLibro, String strIDCCosto, String strFolio
		) throws SQLException, Exception{//Este m?todo verifica si cuadran los movimientos de la p?liza
			
			boolean blnSw = false;
			
			if (!polizaConMovimientos(strIDEjercicio, strIDLibro, strIDCCosto, strFolio))
			{
			throw new Error("Esta p?liza no tiene movimientos");
			}
			
			//Calcular cr?ditos
			String COMANDO = "SELECT COALESCE(SUM(IMPORTE * CASE NATURALEZA WHEN 'D' THEN -1 ELSE 1 END),-1) IMPORTE ";
			COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
			COMANDO += "WHERE ID_EJERCICIO = ? ";
			COMANDO += "AND ID_LIBRO = ? ";
			COMANDO += "AND ID_CCOSTO = ? ";
			COMANDO += "AND FOLIO = ? ";
			PreparedStatement pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, strIDEjercicio);
			pstmt.setString(2, strIDLibro);
			pstmt.setString(3, strIDCCosto);
			pstmt.setString(4, strFolio);
			ResultSet rset = pstmt.executeQuery();
			
			if (rset.next())
			{
                            //System.out.println("Poliza "+ strIDCCosto+"-"+strIDLibro+strFolio +": $"+rset.getDouble("Importe"));
                            if(new Double(rset.getDouble("Importe")).compareTo(new Double(0.5)) < 0 &&
                    		new Double(rset.getDouble("Importe")).compareTo(new Double(-0.5)) > 0)
                                    blnSw = true;

//				Se permite que exista una diferencia de medio peso
//                            if (strIDLibro.equals("20")){
//                		if(new Double(rset.getDouble("Importe")).compareTo(new Double(0.5)) < 0 &&
//                    		new Double(rset.getDouble("Importe")).compareTo(new Double(-0.5)) > 0)
//							blnSw = true;
//				}
//				else{
//					if(new Double(rset.getDouble("Importe")).compareTo(new Double(0)) == 0)
//							blnSw = true;
//				}
			}
			rset.close();
			pstmt.close();
			
			return blnSw;
	   }
	  
//	M?todos de uso general
	public boolean existePoliza
		(
		 String strIDEjercicio, String strIDLibro, String strIDCCosto, String strFolio
		) throws SQLException, Exception
	{//Este m?todo verifica si cuadran los movimientos de la p?liza

		int intNReg = 0;

		//Verificamos si la p?liza tiene movimientos
		String COMANDO = "SELECT COUNT(*) AS NREG ";
		COMANDO += "FROM MATEO.CONT_POLIZA ";
		COMANDO += "WHERE ID_EJERCICIO = ? ";
		COMANDO += "AND ID_LIBRO = ? ";
		COMANDO += "AND ID_CCOSTO = ? ";
		COMANDO += "AND FOLIO = ? ";
		PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		pstmt.setString(1, strIDEjercicio);
		pstmt.setString(2, strIDLibro);
		pstmt.setString(3, strIDCCosto);
		pstmt.setString(4, strFolio);
		ResultSet rset = pstmt.executeQuery();

		if (rset.next())
		{
			intNReg = rset.getInt("NReg");
		}
		rset.close();
		pstmt.close();

		if (intNReg == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	public boolean polizaConMovimientos
		(
		 String strIDEjercicio, String strIDLibro, String strIDCCosto, String strFolio
		) throws SQLException, Exception
	{//Este m?todo verifica si cuadran los movimientos de la p?liza

		int intNReg = 0;

		//Verificamos si la p?liza tiene movimientos
		String COMANDO = "SELECT COUNT(*) AS NREG ";
		COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
		COMANDO += "WHERE ID_EJERCICIO = ? ";
		COMANDO += "AND ID_LIBRO = ? ";
		COMANDO += "AND ID_CCOSTO = ? ";
		COMANDO += "AND FOLIO = ? ";
		PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		pstmt.setString(1, strIDEjercicio);
		pstmt.setString(2, strIDLibro);
		pstmt.setString(3, strIDCCosto);
		pstmt.setString(4, strFolio);
		ResultSet rset = pstmt.executeQuery();

		if (rset.next())
		{
			intNReg = rset.getInt("NReg");
		}
		rset.close();
		pstmt.close();

		if (intNReg == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	  
//	M?todos de uso general
	  public boolean polizaConMovsT
		  (
		   String strIDEjercicio, String strIDLibro, String strIDCCosto, String strFolio
		  ) throws SQLException, Exception
	  {//Este m?todo verifica si existen movimientos en status T, en espera de ser cancelados por Auditoria

		  int intNReg = 0;

		  //Leer movimientos
		  String COMANDO = "SELECT COUNT(*) AS NREG ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
		  COMANDO += "WHERE STATUS = 'T' ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  COMANDO += "AND FOLIO = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDLibro);
		  pstmt.setString(3, strIDCCosto);
		  pstmt.setString(4, strFolio);
		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  intNReg = rset.getInt("NReg");
		  }
		  rset.close();
		  pstmt.close();

		  if (intNReg == 0)
		  {
			  return false;
		  }
		  else
		  {
			  return true;
		  }
	  }
	  
//	M?todos de uso general
	  public String getFolioPoliza
		  (
		   String strIDLibro, HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo obtiene el primer folio disponible para que el usuario pueda crear una p?liza

		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
		  String strFolio = null;

		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  //Validar que usuario tenga derechos para sistema de contabilidad
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no tiene derechos para crear una p?liza");
		  }

		  //Obtener el siguiente n?mero de p?liza v?lido para el usuario
		  COMANDO = "SELECT SUBSTR(COALESCE(MAX(C_POLIZA),'00000')+100001,2,5) C_POLIZA ";
		  COMANDO += "FROM MATEO.CONT_FOLIO ";
		  COMANDO += "WHERE UPPER(LOGIN) = ? ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strUsuario.toUpperCase());
		  pstmt.setString(2, strIDEjercicio);
		  pstmt.setString(3, strIDLibro);
		  pstmt.setString(4, strIDCCosto);
		  rset = pstmt.executeQuery();
		  if (rset.next())
		  {
			  strFolio = rset.getString("C_Poliza");
		  }
		  pstmt.close();
		  rset.close();

		  if (strFolio == null)
		  {
			  throw new Error("Usuario con rango de p?lizas inv?lido");
		  }

		  return strFolio;
	  }
   

   
	  //M?todos de uso general
	  public String getFolioPolizaActual
		  (
		   HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo obtiene el folio del campo C_Poliza

		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
		  String strFolio = null;

		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strIDLibro = (String)session.getAttribute("id_libro");

		  //Validar que usuario tenga derechos para sistema de contabilidad
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no esta registrado en el sistema de contabilidad");
		  }

		  //Obtener el n?mero de p?liza actual
		  COMANDO = "SELECT COALESCE(C_POLIZA,'00000') C_POLIZA ";
		  COMANDO += "FROM MATEO.CONT_FOLIO ";
		  COMANDO += "WHERE UPPER(LOGIN) = ? ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strUsuario.toUpperCase());
		  pstmt.setString(2, strIDEjercicio);
		  pstmt.setString(3, strIDLibro);
		  pstmt.setString(4, strIDCCosto);
		  rset = pstmt.executeQuery();
		  if (rset.next())
		  {
			  strFolio = rset.getString("C_Poliza");
		  }
		  pstmt.close();
		  rset.close();

		  if (strFolio == null)
		  {
			  throw new Error("Usuario sin rango de p?lizas");
		  }

		  return strFolio;
	  }
	  
//	M?todos de uso general
	  public boolean getExistePolizaActual
		  (
		   HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo regresa true si hay una p?liza con el folio = c_poliza

		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
		  String strFolio = null;

		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strIDLibro = (String)session.getAttribute("id_libro");

		  //Validar que usuario tenga derechos para sistema de contabilidad
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no esta registrado en el sistema de contabilidad");
		  }

		  //Obtener el n?mero de p?liza actual
		  COMANDO = "SELECT COALESCE(C_POLIZA,'00000') C_POLIZA ";
		  COMANDO += "FROM MATEO.CONT_FOLIO ";
		  COMANDO += "WHERE UPPER(LOGIN) = ? ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strUsuario.toUpperCase());
		  pstmt.setString(2, strIDEjercicio);
		  pstmt.setString(3, strIDLibro);
		  pstmt.setString(4, strIDCCosto);
		  rset = pstmt.executeQuery();
		  if (rset.next())
		  {
			  strFolio = rset.getString("C_Poliza");
		  }
		  pstmt.close();
		  rset.close();

		  if (strFolio == null)
		  {
			  throw new Error("Usuario sin rango de p?lizas");
		  }

		  return getExistePoliza(strFolio, session);
	  }
	  
//	M?todos de uso general
	  public boolean getExistePoliza
		  (
		   String strFolio, HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo regresa true si hay una p?liza con el folio = c_poliza

		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
		  Integer intNReg = null;
		  boolean blnSw = false;

		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strIDLibro = (String)session.getAttribute("id_libro");

		  //Validar que usuario tenga derechos para sistema de contabilidad
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no esta registrado en el sistema de contabilidad");
		  }

		  COMANDO = "SELECT COUNT(*) NREG ";
		  COMANDO += "FROM MATEO.CONT_POLIZA ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  COMANDO += "AND FOLIO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDLibro);
		  pstmt.setString(3, strIDCCosto);
		  pstmt.setString(4, strFolio);
		  rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  intNReg = new Integer(rset.getInt("NReg"));
		  }
		  rset.close();
		  pstmt.close();

		  if (intNReg.compareTo(new Integer(0)) == 1)
			  blnSw = true;
		  else if (intNReg.compareTo(new Integer(0)) == 0)
			  blnSw = false;
		  else
			  throw new Error("Error critico en polizas! <br> Comunicarse a la Direccion de Sistemas");

		  return blnSw;
	  }
	  
//	M?todos de uso general
	  public void getDatosPoliza
		  (
		   String strIDEjercicio, String strIDLibro, String strIDCCosto, String strFolio
		  ) throws SQLException, Exception
	  {//Este m?todo, obtiene los datos de la p?liza

		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
		  //Limpiar atributos de clase
		  limpiarAtributos();

		  //Obtener datos de la p?liza
		  COMANDO = "SELECT TO_CHAR(FECHA, 'dd-mm-yyyy') FECHA, ";
		  COMANDO += "DESCRIPCION, U.USERNAME ID_USUARIO, ";
		  COMANDO += "CASE P.STATUS WHEN 'A' THEN 'Abierto' WHEN 'R' THEN 'En Revisi&oacute;on' ";
		COMANDO += "WHEN 'C' THEN 'Cerrada' WHEN 'D' THEN 'Cierre Definitivo' WHEN 'I' THEN 'Cancelada' ";
		COMANDO += "ELSE 'Estatus Err&oacute;neo' END AS Status, ";
		  COMANDO += "STATUS STATUSIND, REVISADO_POR ";
		  COMANDO += "FROM MATEO.CONT_POLIZA P, NOE.APP_USER U ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  COMANDO += "AND FOLIO = ? ";
		  COMANDO += "AND P.ID_USUARIO = U.ID ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDLibro);
		  pstmt.setString(3, strIDCCosto);
		  pstmt.setString(4, strFolio);
		  rset = pstmt.executeQuery();

		  if(rset.next())
		  {
			  atrstrFecha = rset.getString("Fecha");
			  atrstrStatus = rset.getString("Status");
			  atrstrIDUsuario = rset.getString("ID_Usuario");
			  atrstrDescripcion = rset.getString("Descripcion");
			  atrstrIDEjercicio = strIDEjercicio;
			  atrstrIDLibro = strIDLibro;
			  atrstrFolio = strFolio;
			  atrstrStatusInd = rset.getString("StatusInd");
			  atrstrRevisadoPor = rset.getString("Revisado_Por");
		  }
		  rset.close();
		  pstmt.close();

		  if (atrstrStatusInd == null)
		  {
			  new Error("P?liza inv?lida! " + strIDEjercicio + " " + strIDLibro + " " + strIDCCosto + " " + strFolio);
		  }
	  }
	  
	//	M?todos de uso general
	  public String getStatusPoliza() throws Exception
	  {//Este m?todo, regresa el status de la p?liza
		  return atrstrStatus;
	  }
	  
	//M?todos de uso general
	public String getStatusPolizaInd() throws Exception
	{//Este m?todo, regresa el status de la p?liza
		return atrstrStatusInd;
	}
	
	//	M?todos de uso general
	  public String getCreadorPoliza () throws Exception
	  {//Este m?todo, regresa el creador de la p?liza
		  return atrstrIDUsuario;
	  }
	  
	//	M?todos de uso general
	  public String getRevisaPoliza () throws Exception
	  {//Este m?todo, regresa el login de quien revis? la p?liza
		  if (atrstrRevisadoPor == null)
			  atrstrRevisadoPor = "";

		  return atrstrRevisadoPor;
	  }
	  
	//	M?todos de uso general
	  public String getFechaPoliza () throws Exception
	  {//Este m?todo, regresa la fecha de la p?liza
		  return atrstrFecha;
	  }

	  //M?todos de uso general
	  public String getDescripcionPoliza() throws Exception
	  {//Este m?todo, regresa la descripcion de la p?liza
		  return atrstrDescripcion;
	  }

	  //M?todos de uso general
	  public String getIDEjercicioPoliza () throws Exception
	  {//Este m?todo, regresa el ejercicio de la p?liza
		  return atrstrIDEjercicio;
	  }
	  
	 //	M?todos de uso general
	  public String getIDLibroPoliza () throws Exception
	  {//Este m?todo, regresa libro de la p?liza
		  return atrstrIDLibro;
	  }
 
	  //M?todos de uso general
	  public String getFolioPoliza() throws Exception
	  {//Este m?todo, regresa el folio de la p?liza
		  return atrstrFolio;
	  }
	  
//	M?todos de uso general
	  public String getNombreLibro (String strIDLibro) throws SQLException, Exception
	  {
		  String COMANDO = null;
		  PreparedStatement pstmt = null;
		  ResultSet rset = null;

		  String strLibro = null;

		  COMANDO = "SELECT NOMBRE ";
		  COMANDO += "FROM CONT_LIBRO ";
		  COMANDO += "WHERE ID_LIBRO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDLibro);
		  rset = pstmt.executeQuery();

		  if(rset.next())
		  {
			  strLibro = rset.getString("Nombre");
		  }

		  if(rset.wasNull())
		  {
			  new Error("Libro inv?lido!");
		  }

		  rset.close();
		  pstmt.close();

		  return strLibro;
	  }
	  
//	M?todos de uso general
	  public void limpiarAtributos () throws Exception
	  {
		  atrstrIDEjercicio = null;
		  atrstrIDLibro = null;
		  atrstrFolio = null;
		  atrstrFecha = null;
		  atrstrDescripcion = null;
		  atrstrIDUsuario = null;
		  atrstrStatus = null;
		  atrstrStatusInd = null;
	  }
	  
//	M?todos de uso general
	  public Integer getNumeroMovimiento
		  (
		   String strIDEjercicio, String strIDLibro, String strIDCCosto, String strFolio
		  ) throws SQLException, Exception
	  {//Este m?todo, obtiene el siguiente n?mero de movimiento

		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
		  Integer intNumMovto = null;

		  //Obtener datos de la p?liza
		  COMANDO = "SELECT COALESCE(MAX(NUMMOVTO),0)+1 NUMMOVTO ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  COMANDO += "AND FOLIO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDLibro);
		  pstmt.setString(3, strIDCCosto);
		  pstmt.setString(4, strFolio);
		  rset = pstmt.executeQuery();

		  if(rset.next())
		  {
			  intNumMovto = new Integer(rset.getInt("NumMovto"));
		  }

		  if (rset.wasNull())
		  {
			  new Error("P?liza inv?lida!");
		  }

		  rset.close();
		  pstmt.close();

		  return intNumMovto;
	  }
	  
//	Limpiar atributos de movimiento
	  public void limpiarAtributosMov()
	  {
		  atrstrFechaMov = null;
		  atrstrStatusMov = null;
		  atrstrDescripcionMov = null;
		  atrstrIDEjercicioMov = null;
		  atrstrIDLibroMov = null;
		  atrstrFolioMov = null;
		  atrstrNaturalezaMov = null;
		  atrdblImporteMov = null;
		  atrstrReferenciaMov = null;
		  atrstrReferencia2Mov = null;
		  atrstrCtaMayorMov = null;
		  atrstrCCostoMov = null;
		  atrstrAuxiliarMov = null;
	  }
	  
//	M?todos de uso general
	  public void getDatosMovimiento
		  (
		   String strFolio, Integer intNumMov, HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo, obtiene los datos de la p?liza

		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDLibro = (String)session.getAttribute("id_libro");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");

		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;

		  //Limpiar atributos de clase
		  limpiarAtributosMov();

		  //Obtener datos de la p?liza
		  COMANDO = "SELECT TO_CHAR(FECHA, 'dd-mm-yyyy') FECHA, ";
		  COMANDO += "DESCRIPCION, IMPORTE, NATURALEZA, ";
		  COMANDO += "COALESCE(REFERENCIA, '-') REFERENCIA, ";
		  COMANDO += "COALESCE(REFERENCIA2, '-') REFERENCIA2, ";
		  COMANDO += "ID_CTAMAYORM, ID_CCOSTOM, ";
		  COMANDO += "COALESCE(ID_AUXILIARM, '-') ID_AUXILIARM, ";
		  COMANDO += "CASE STATUS WHEN 'A' THEN 'Abierto' WHEN 'R' THEN 'En Revisi&oacute;on' ";
		COMANDO += "WHEN 'C' THEN 'Cerrada' WHEN 'D' THEN 'Cierre Definitivo' WHEN 'I' THEN 'Cancelada' ";
		COMANDO += "ELSE 'Estatus Err&oacute;neo' END AS Status ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  COMANDO += "AND FOLIO = ? ";
		  COMANDO += "AND NUMMOVTO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDLibro);
		  pstmt.setString(3, strIDCCosto);
		  pstmt.setString(4, strFolio);
		  pstmt.setInt(5, intNumMov.intValue());
		  rset = pstmt.executeQuery();

		  if(rset.next())
		  {
			  atrstrFechaMov = rset.getString("Fecha");
			  atrstrStatusMov = rset.getString("Status");
			  atrstrDescripcionMov = rset.getString("Descripcion");
			  atrstrIDEjercicioMov = strIDEjercicio;
			  atrstrIDLibroMov = strIDLibro;
			  atrstrFolioMov = strFolio;
			  atrstrNaturalezaMov = rset.getString("Naturaleza");
			  atrdblImporteMov = new Double(rset.getDouble("Importe"));
			  atrstrReferenciaMov = rset.getString("Referencia");
			  atrstrReferencia2Mov = rset.getString("Referencia2");
			  atrstrCtaMayorMov = rset.getString("ID_CtaMayorM");
			  atrstrCCostoMov = rset.getString("ID_CCostoM");
			  atrstrAuxiliarMov = rset.getString("ID_AuxiliarM");
		  }
		  rset.close();
		  pstmt.close();

		  if (atrstrCCostoMov == null)
		  {
			  new Error("P?liza inv?lida!");
		  }
	  }
	  
//	M?todos de uso general
	  public String getFechaMov()
	  {
		  return atrstrFechaMov;
	  }
	  
//	M?todos de uso general
	  public String getDescripcionMov()
	  {
		  return atrstrDescripcionMov;
	  }
  
	  //M?todos de uso general
	  public double getImporteMov()
	  {
		  return atrdblImporteMov.doubleValue();
	  }


	  //M?todos de uso general
	  public String getNaturalezaMov()
	  {
		  return atrstrNaturalezaMov;
	  }


	  //M?todos de uso general
	  public String getReferenciaMov()
	  {
		  if (atrstrReferenciaMov.equals("-"))
			  atrstrReferenciaMov = "";
		  return atrstrReferenciaMov;
	  }
	  
//	M?todos de uso general
	  public String getReferencia2Mov()
	  {
		  if (atrstrReferencia2Mov.equals("-"))
			  atrstrReferencia2Mov = "";
		  return atrstrReferencia2Mov;
	  }


	  //M?todos de uso general
	  public String getCtaMayorMov()
	  {
		  return atrstrCtaMayorMov;
	  }


	  //M?todos de uso general
	  public String getCCostoMov()
	  {
		  return atrstrCCostoMov;
	  }


	  //M?todos de uso general
	  public String getAuxiliarMov()
	  {
		  if (atrstrAuxiliarMov.equals("-"))
			  atrstrAuxiliarMov = "";
		  return atrstrAuxiliarMov;
	  }
	  
	//M?todos de uso general
	public String getStatusMov()
	{
		return atrstrStatusMov;
	}
	
//	M?todos de uso general
	  public PreparedStatement getDatosMovimiento
		  (
		   String strIDEjercicio, String strIDLibro, String strIDCCosto, String strFolio
		  ) throws SQLException, Exception
	  {//Este m?todo, obtiene los datos de la p?liza

		  PreparedStatement pstmt = null;
		  String COMANDO = null;

		  //Obtener datos de la p?liza
		  COMANDO = "SELECT NUMMOVTO, TO_CHAR(FECHA, 'dd-mm-yyyy') FECHA, ";
		  COMANDO += "DESCRIPCION, IMPORTE, NATURALEZA, ";
		  COMANDO += "COALESCE(REFERENCIA, '-') REFERENCIA, ";
		  COMANDO += "COALESCE(REFERENCIA2, '-') REFERENCIA2, ";
		  COMANDO += "ID_CTAMAYORM, ID_CCOSTOM, ";
		  COMANDO += "COALESCE(ID_AUXILIARM, '-') ID_AUXILIARM, ";
		  COMANDO += "CASE STATUS WHEN 'A' THEN 'Abierto' WHEN 'R' THEN 'En Revisi&oacute;on' ";
		COMANDO += "WHEN 'C' THEN 'Cerrada' WHEN 'D' THEN 'Cierre Definitivo' WHEN 'I' THEN 'Cancelada' ";
		COMANDO += "ELSE 'Estatus Err&oacute;neo' END AS Status, ";
		  COMANDO += "STATUS STATUSIND ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  COMANDO += "AND FOLIO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDLibro);
		  pstmt.setString(3, strIDCCosto);
		  pstmt.setString(4, strFolio);

		  return pstmt;
	  }
	  
//	M?todos de uso general
	  public PreparedStatement getDatosMovimientoConAvisos
		  (
		   String strIDEjercicio, String strIDLibro, String strIDCCosto, String strFolio
		  ) throws SQLException, Exception
	  {//Este m?todo, obtiene los datos de la p?liza

		  PreparedStatement pstmt = null;
		  String COMANDO = null;

		  //Obtener datos de la p?liza
		  COMANDO = "SELECT M.NUMMOVTO, TO_CHAR(M.FECHA, 'dd-mm-yyyy') FECHA, ";
		  COMANDO += "M.DESCRIPCION, M.IMPORTE, M.NATURALEZA, ";
		  COMANDO += "COALESCE(M.REFERENCIA, '-') REFERENCIA, ";
		  COMANDO += "COALESCE(M.REFERENCIA2, '-') REFERENCIA2, ";
		  COMANDO += "M.ID_CTAMAYORM, M.ID_CCOSTOM, ";
		  COMANDO += "COALESCE(M.ID_AUXILIARM, '-') ID_AUXILIARM, ";
		  COMANDO += "CASE M.STATUS WHEN 'A' THEN 'Abierto' WHEN 'R' THEN 'En Revisi&oacute;on' ";
		COMANDO += "WHEN 'C' THEN 'Cerrada' WHEN 'D' THEN 'Cierre Definitivo' WHEN 'I' THEN 'Cancelada' ";
		COMANDO += "ELSE 'Estatus Err&oacute;neo' END AS Status ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO M, CONT_CTAMAYOR C ";
		  COMANDO += "WHERE C.ID_EJERCICIO = M.ID_EJERCICIO ";
		  COMANDO += "AND C.ID_CTAMAYOR = M.ID_CTAMAYORM ";
		  COMANDO += "AND C.AVISO = 'S' ";
		  COMANDO += "AND C.DETALLE = 'S' ";
		  COMANDO += "AND M.ID_EJERCICIO = ? ";
		  COMANDO += "AND M.ID_LIBRO = ? ";
		  COMANDO += "AND M.ID_CCOSTO = ? ";
		  COMANDO += "AND M.FOLIO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDLibro);
		  pstmt.setString(3, strIDCCosto);
		  pstmt.setString(4, strFolio);

		  return pstmt;
	  }
	  
//	M?todos de uso general
	  public PreparedStatement getMovimientos
		  (
		   String strIDEjercicio, String strIDCtaMayor, String strIDCCosto,
		   String strIDAuxiliar, String strTipo, String strFechaI,
		   String strFechaF
		  ) throws SQLException, Exception
	  {//Este m?todo, obtiene los movimientos de la cuenta dependiendo del tipo
	   //El tipo, indica si se requieren movimientos de p?lizas abiertas y en revisi?n,
	   //o de p?lizas cerradas, o de cualquier p?liza.
	   //Validando el rango de fechas

		  PreparedStatement pstmt = null;
		  String COMANDO = null;

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  //Obtener datos de la p?liza
		  COMANDO = "SELECT M.ID_LIBRO, M.FOLIO, M.NUMMOVTO, TO_CHAR(M.FECHA, 'dd-mm-yyyy') FECHA, ";
		  COMANDO += "M.DESCRIPCION, M.IMPORTE, M.NATURALEZA, ";
		  COMANDO += "COALESCE(M.REFERENCIA, '-') REFERENCIA, ";
		  COMANDO += "COALESCE(M.REFERENCIA2, '-') REFERENCIA2, ";
		  COMANDO += "CASE M.STATUS WHEN 'A' THEN 'Abierto' WHEN 'R' THEN 'En Revisi&oacute;on' ";
		COMANDO += "WHEN 'C' THEN 'Cerrada' WHEN 'D' THEN 'Cierre Definitivo' WHEN 'I' THEN 'Cancelada' ";
		COMANDO += "ELSE 'Estatus Err&oacute;neo' END AS Status ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO M, CONT_POLIZA P ";
		  COMANDO += "WHERE M.ID_EJERCICIO = P.ID_EJERCICIO ";
		  COMANDO += "AND M.ID_LIBRO = P.ID_LIBRO ";
		  COMANDO += "AND M.ID_CCOSTO = P.ID_CCOSTO ";
		  COMANDO += "AND M.FOLIO = P.FOLIO ";

		  if (strTipo.equals("T"))
		  {
			  //Todos los movimientos, incluyendo polizas abiertas y cerradas
		  }
		  else if (strTipo.equals("P"))
		  {
			  //Movimientos por afectar, solo polizas abiertas y en revision
			  COMANDO += "AND P.STATUS IN ('A', 'R') ";
		  }
		  else if (strTipo.equals("A"))
		  {
			  //Movimientos que ya afectaron, solo polizas cerradas y definitivamente cerradas
			  COMANDO += "AND P.STATUS IN ('C', 'D') ";
		  }
		  else
			  throw new Error("Tipo de busqueda invalido");

		  COMANDO += "AND TO_DATE(M.FECHA, 'dd-mm-yy') BETWEEN TO_DATE(?, 'dd-mm-yy') AND TO_DATE(?, 'dd-mm-yy') ";
		  COMANDO += "AND M.ID_EJERCICIO = ? ";
		  COMANDO += "AND M.ID_CTAMAYORM = ? ";
		  COMANDO += "AND M.ID_CCOSTOM = ? ";

		  if (strIDAuxiliar.equals("0000000"))
		  {
			  COMANDO += "ORDER BY TO_DATE(FECHA,'dd-mm-yy'), FOLIO, NUMMOVTO ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strFechaI);
			  pstmt.setString(2, strFechaF);
			  pstmt.setString(3, strIDEjercicio);
			  pstmt.setString(4, strIDCtaMayor);
			  pstmt.setString(5, strIDCCosto);
		  }
		  else
		  {
			  COMANDO += "AND M.ID_AUXILIARM = ? ";
			  COMANDO += "ORDER BY TO_DATE(FECHA,'dd-mm-yy'), FOLIO, NUMMOVTO ";

			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strFechaI);
			  pstmt.setString(2, strFechaF);
			  pstmt.setString(3, strIDEjercicio);
			  pstmt.setString(4, strIDCtaMayor);
			  pstmt.setString(5, strIDCCosto);
			  pstmt.setString(6, strIDAuxiliar);
		  }

		  return pstmt;
	  }
	  
//	M?todos de uso general
	  public String getFolioRecibo
		  (
		   String strIDLibro, HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo obtiene el primer folio disponible para que el usuario pueda crear un recibo

		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
		  String strFolio = null;

		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");

		  //Validar que usuario tenga derechos para sistema de contabilidad
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no tiene derechos para crear una recibo");
		  }

		  //Obtener el siguiente n?mero de p?liza v?lido para el usuario
		  COMANDO = "SELECT SUBSTR(COALESCE(MAX(C_RECIBO),'00000')+1000001,2,6) C_RECIBO ";
		  COMANDO += "FROM CONT_FOLIO ";
		  COMANDO += "WHERE UPPER(LOGIN) = ? ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strUsuario.toUpperCase());
		  pstmt.setString(2, strIDEjercicio);
		  pstmt.setString(3, strIDLibro);
		  pstmt.setString(4, strIDCCosto);
		  rset = pstmt.executeQuery();
		  if (rset.next())
		  {
			  strFolio = rset.getString("C_Recibo");
		  }
		  pstmt.close();
		  rset.close();

		  if (strFolio == null)
		  {
			  throw new Error("Usuario con rango de recibos inv?lido");
		  }

		  return strFolio;
	  }
	  
//	M?todos de uso general
	  public String getFolioReciboActual
		  (
		   HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo obtiene el primer folio disponible para que el usuario pueda crear un recibo

		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
		  String strFolio = null;

		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strIDLibro = (String)session.getAttribute("id_libro");

		  //Validar que usuario tenga derechos para sistema de contabilidad
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no tiene derechos para crear una recibo");
		  }

		  //Obtener el siguiente n?mero de p?liza v?lido para el usuario
		  COMANDO = "SELECT COALESCE(C_RECIBO,'000000') C_RECIBO ";
		  COMANDO += "FROM CONT_FOLIO ";
		  COMANDO += "WHERE UPPER(LOGIN) = ? ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strUsuario.toUpperCase());
		  pstmt.setString(2, strIDEjercicio);
		  pstmt.setString(3, strIDLibro);
		  pstmt.setString(4, strIDCCosto);
		  rset = pstmt.executeQuery();
		  if (rset.next())
		  {
			  strFolio = rset.getString("C_Recibo");
		  }
		  pstmt.close();
		  rset.close();

		  if (strFolio == null)
		  {
			  throw new Error("Usuario con rango de recibos inv?lido");
		  }

		  return strFolio;
	  }
	  
//	Cambiar el numero de recibo actual
	  public void reciboModificaActual
		  (
		   String strRecibo, String strIDLibro, HttpSession session
		  ) throws Exception
	  {//Este modifica el recibo actual

		  PreparedStatement pstmt = null;
		  String COMANDO = null;

		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");

		  //modificar el recibo actual
		  COMANDO = "UPDATE CONT_FOLIO ";
		  COMANDO += "SET C_RECIBO = ? ";
		  COMANDO += "WHERE UPPER(LOGIN) = ? ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strRecibo);
		  pstmt.setString(2, strUsuario.toUpperCase());
		  pstmt.setString(3, strIDEjercicio);
		  pstmt.setString(4, strIDLibro);
		  pstmt.setString(5, strIDCCosto);
		  pstmt.executeUpdate();
		  pstmt.close();
	  }
	  
//	Cambiar el numero de recibo actual
	  public void reciboModificaActual
		  (
		   String strRecibo, HttpSession session
		  ) throws Exception
	  {//Este modifica el recibo actual

		  PreparedStatement pstmt = null;
		  String COMANDO = null;

		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strIDLibro = (String)session.getAttribute("id_libro");

		  //modificar el recibo actual
		  COMANDO = "UPDATE CONT_FOLIO ";
		  COMANDO += "SET C_RECIBO = ? ";
		  COMANDO += "WHERE UPPER(LOGIN) = ? ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strRecibo);
		  pstmt.setString(2, strUsuario.toUpperCase());
		  pstmt.setString(3, strIDEjercicio);
		  pstmt.setString(4, strIDLibro);
		  pstmt.setString(5, strIDCCosto);
		  pstmt.executeUpdate();
		  pstmt.close();
	  }
	  
	//Cambiar el numero de poliza actual
	public void polizaModificaActual
		(
		 String strPoliza, String strIDLibro, HttpSession session
		) throws SQLException, Exception
	{//Este modifica la poliza actual

		PreparedStatement pstmt = null;
		String COMANDO = null;

		String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		String strIDCCosto = (String)session.getAttribute("id_ccosto");

		//modificar el recibo actual
		COMANDO = "UPDATE MATEO.CONT_FOLIO ";
		COMANDO += "SET C_POLIZA = ? ";
		COMANDO += "WHERE UPPER(LOGIN) = ? ";
		COMANDO += "AND ID_EJERCICIO = ? ";
		COMANDO += "AND ID_LIBRO = ? ";
		COMANDO += "AND ID_CCOSTO = ? ";
		pstmt = conn.prepareStatement(COMANDO);
		pstmt.setString(1, strPoliza);
		pstmt.setString(2, strUsuario.toUpperCase());
		pstmt.setString(3, strIDEjercicio);
		pstmt.setString(4, strIDLibro);
		pstmt.setString(5, strIDCCosto);
		pstmt.execute();
		pstmt.close();
	}
	
//	Cambiar el numero de poliza actual
	  public void polizaModificaActual
		  (
		   String strPoliza, HttpSession session
		  ) throws SQLException, Exception
	  {//Este modifica la poliza actual

		  PreparedStatement pstmt = null;
		  String COMANDO = null;

		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");
		  String strIDLibro = (String)session.getAttribute("id_libro");

		  //modificar el recibo actual
		  COMANDO = "UPDATE CONT_FOLIO ";
		  COMANDO += "SET C_POLIZA = ? ";
		  COMANDO += "WHERE UPPER(LOGIN) = ? ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strPoliza);
		  pstmt.setString(2, strUsuario.toUpperCase());
		  pstmt.setString(3, strIDEjercicio);
		  pstmt.setString(4, strIDLibro);
		  pstmt.setString(5, strIDCCosto);
		  pstmt.execute();
		  pstmt.close();
	  }
	  
//	M?todos de uso general
	  public String getFolioPolizaC
		  (
		   String strIDLibro, HttpSession session
		  ) throws SQLException, Exception
	  {//Este m?todo obtiene el folio actual de la poliza

		  PreparedStatement pstmt = null;
		  ResultSet rset = null;
		  String COMANDO = null;
		  String strFolio = null;

		  String strUsuario = ((String)session.getAttribute("login")).toUpperCase();
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
		  String strIDCCosto = (String)session.getAttribute("id_ccosto");

		  //Validar que usuario tenga derechos para sistema de contabilidad
		  if (!metodos3.esUsuarioValido(session))
		  {
			  throw new Error("El usuario no tiene derechos para crear una p?liza");
		  }

		  //Obtener el siguiente n?mero de p?liza v?lido para el usuario
		  COMANDO = "SELECT SUBSTR(COALESCE(MAX(C_POLIZA),'00000')+100000,2,5) C_POLIZA ";
		  COMANDO += "FROM CONT_FOLIO ";
		  COMANDO += "WHERE UPPER(LOGIN) = ? ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strUsuario.toUpperCase());
		  pstmt.setString(2, strIDEjercicio);
		  pstmt.setString(3, strIDLibro);
		  pstmt.setString(4, strIDCCosto);
		  rset = pstmt.executeQuery();
		  if (rset.next())
		  {
			  strFolio = rset.getString("C_Poliza");
		  }
		  pstmt.close();
		  rset.close();

		  if (strFolio == null)
		  {
			  throw new Error("No existe la poliza o los datos son incorrectos");
		  }

		  return strFolio;
	  }
	  
//	M?todos de uso general
	  public String getEntidadValida
	  (
		  String strIDEjercicio, String strIDCCosto
	  ) throws SQLException, Exception
	  {
		  //Obtiene el nivel contable del ejercicio actual,
		  //y obtiene la entidad del CCosto
		  Integer intNContable = null;
		  String COMANDO = "SELECT NIVEL_CONTABLE ";
		  COMANDO += "FROM MATEO.CONT_EJERCICIO ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  ResultSet rset = pstmt.executeQuery();
		  if (rset.next())
		  {
			  intNContable = new Integer(rset.getInt("Nivel_Contable"));
		  }
		  if (rset.wasNull())
		  {
			  throw new Error("Ejercicio contable inv?lido");
		  }
		  pstmt.close();
		  rset.close();

		  String strEntidad = "";
		 
		  try{
			  StringTokenizer strTkn = new StringTokenizer(strIDCCosto, ".");
			  int intCont = 1;

			  while (strTkn.hasMoreTokens() &&
				  intCont <= intNContable.intValue())
			  {
				  strEntidad += strTkn.nextToken()+".";
				  intCont += 1;
			  }
			  strEntidad = strEntidad.substring(0,strEntidad.length()-1);
		  }catch (NoSuchElementException e)
		  {
			  throw new Error("Contabilidad invalida! <br> No cumple con el nivel contable");
		  }

		  return strEntidad;
	  }
	  
//	M?todos de uso general
	  public String getTAuxiliarValido
	  (
		  String strIDEjercicio, String strIDAuxiliar
	  ) throws SQLException, Exception
	  {
		  //Obtiene el nivel de tipo de auxiliar del ejercicio actual,
		  //y obtiene el tipo de auxiliar del auxiliar
		  Integer intTAuxiliar = null;

		  String COMANDO = "SELECT NIVEL_TAUXILIAR ";
		  COMANDO += "FROM CONT_EJERCICIO ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  ResultSet rset = pstmt.executeQuery();
		  if (rset.next())
		  {
			  intTAuxiliar = new Integer(rset.getInt("Nivel_TAuxiliar"));
		  }
		  pstmt.close();
		  rset.close();

		  if (intTAuxiliar == null)
		  {
			  throw new Error("Ejercicio contable invalido <br> No tiene tipo de auxiliar");
		  }

		  String strAuxiliar = "";

		  try{
			  StringTokenizer strTkn = new StringTokenizer(strIDAuxiliar, ".");
			  int intCont = 1;

			  while (strTkn.hasMoreTokens() &&
				  intCont <= intTAuxiliar.intValue())
			  {
				  strAuxiliar += strTkn.nextToken()+".";
				  intCont += 1;
			  }
			  strAuxiliar = strAuxiliar.substring(0,strAuxiliar.length()-1);
		  }catch (NoSuchElementException e)
		  {
			  throw new Error("Auxiliar invalido! <br> No cumple con el tipo de auxiliar");
		  }

		  return strAuxiliar;
	  }
	  
//	Obtener Entidad de una p?liza
	  public String getEntidadPoliza
	  (
		  String strIDEjercicio, String strIDLibro, String strFolioPoliza,
		  String strUsuario
	  ) throws SQLException, Exception
	  {
		  String strIDCCosto = null;
		  String COMANDO = "SELECT ID_CCOSTO ";
		  COMANDO += "FROM CONT_FOLIO ";
		  COMANDO += "WHERE LOGIN = ? ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND C_POLIZA = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strUsuario);
		  pstmt.setString(2, strIDEjercicio);
		  pstmt.setString(3, strIDLibro);
		  pstmt.setString(4, strFolioPoliza);
		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  strIDCCosto = rset.getString("ID_CCosto");
		  }
		  if (strIDCCosto == null)
			  throw new Error("Poliza Actual "+ strFolioPoliza +" invalida para el usuario "+ strUsuario);
		  pstmt.close();
		  rset.close();

		  return getEntidadValida(strIDEjercicio, strIDCCosto);
	  }
	  
	public void guardarDatosArchivo(String strArchivo, String strLibro, String strPoliza, HttpSession session )throws Exception{
			java.io.BufferedWriter bw = new java.io.BufferedWriter(new java.io.FileWriter(strArchivo, true));
			bw.write((String)session.getAttribute("id_ejercicio"));
			bw.write('\t');
			bw.write(strLibro);
			bw.write('\t');
			bw.write(strPoliza);
			bw.write('\t');
			bw.write((String)session.getAttribute("strFechaFormat"));
			bw.write('\t');
			bw.write((String)session.getAttribute("strDescripcion"));
			bw.write('\t');
			bw.write((String)session.getAttribute("dblImporte"));
			bw.write('\t');
			bw.write((String)session.getAttribute("strNumRecibo"));
			bw.write('\t');
			bw.write((String)session.getAttribute("strAuxiliar"));
			bw.write('\t');
			bw.write((String)session.getAttribute("strCTAMayor"));
			bw.write('\t');
			bw.write((String)session.getAttribute("strCCosto"));
			bw.write('\t');
			bw.write((String)session.getAttribute("strAuxiliar"));
			bw.newLine();
			bw.close();
		}
		
//	Obtener nombre de centro de costo
	  public String getNombreCCosto
	  (
		  String strIDEjercicio, String strIDCCosto
	  ) throws SQLException, Exception
	  {
		  String strNombre = null;
		  String COMANDO = "SELECT NOMBRE ";
		  COMANDO += "FROM CONT_CCOSTO ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDCCosto);
		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  strNombre = rset.getString("Nombre");
		  }
		  rset.close();
		  pstmt.close();

		  if(strNombre == null)
			  throw new Error("El centro de costo no existe");

		  return strNombre;
	  }
	  
//	Obtener nombre de cuenta de mayor
	  public String getNombreCtaMayor
	  (
		  String strIDEjercicio, String strIDCtaMayor
	  ) throws SQLException, Exception
	  {
		  String strNombre = null;
		  String COMANDO = "SELECT NOMBRE ";
		  COMANDO += "FROM CONT_CTAMAYOR ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_CTAMAYOR = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDCtaMayor);
		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  strNombre = rset.getString("Nombre");
		  }
		  rset.close();
		  pstmt.close();

		  if(strNombre == null)
			  throw new Error("La cuenta de mayor "+strIDCtaMayor+" no existe");

		  return strNombre;
	  }
	  
//	Obtener nombre de centro de auxiliar
	  public String getNombreAuxiliar
	  (
		  String strIDEjercicio, String strIDAuxiliar
	  ) throws SQLException, Exception
	  {
		  String strNombre = null;
		  String COMANDO = "SELECT NOMBRE ";
		  COMANDO += "FROM CONT_AUXILIAR ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_AUXILIAR = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDAuxiliar);
		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  strNombre = rset.getString("Nombre");
		  }
		  rset.close();
		  pstmt.close();

		  if(strNombre == null)
			  throw new Error("La cuenta de auxiliar no existe");

		  return strNombre;
	  }
	  
//	Obtener nombre de cuenta
	  public String getNombreCuenta
	  (
		  String strIDEjercicio, String strIDCtaMayor, String strIDCCosto,
		  String strIDAuxiliar
	  ) throws SQLException, Exception
	  {
		  String strNombre = null;

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  String COMANDO = "SELECT NOMBRE ";
		  COMANDO += "FROM CONT_RELACION ";
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

		  if (rset.next())
		  {
			  strNombre = rset.getString("Nombre");
		  }
		  rset.close();
		  pstmt.close();

		  if(strNombre == null)
			  throw new Error("La cuenta "+strIDCtaMayor+"-"+strIDCCosto+"-"+strIDAuxiliar+" no existe");

		  return strNombre;
	  }
	  
	  
//	Obtener el atributo Auxiliar de la cuenta de mayor
	  public Boolean getLlevaAuxiliar
	  (
		  String strIDCtaMayor, HttpSession session
	  ) throws SQLException, Exception
	  {
		  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");

		  String strLlevaAuxiliar = null;

		  String COMANDO = "SELECT AUXILIAR ";
		  COMANDO += "FROM MATEO.CONT_CTAMAYOR ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_CTAMAYOR = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDCtaMayor);
		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  strLlevaAuxiliar = rset.getString("Auxiliar");
		  }
		  pstmt.close();
		  rset.close();

		  if (strLlevaAuxiliar == null)
			  throw new Error("Cuenta de mayor invalida");

		  if (strLlevaAuxiliar.equals("S"))
			  return new Boolean(true);
		  else if (strLlevaAuxiliar.equals("N"))
			  return new Boolean("false");
		  else return null;
	  }
	  
//	Validar si la cuenta de mayor tiene su auxiliar, si es que lo debe tener
	  public void tieneCtaMayor_Auxiliar
	  (
		  String strIDCtaMayor, String strIDAuxiliar, HttpSession session
	  ) throws SQLException, Exception
	  {
		  if (strIDAuxiliar == null || strIDAuxiliar.equals("0000000") || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "";

		  //Validar que si la cuenta de mayor tiene su auxiliar, si es que lo debe llevar
		  if (getLlevaAuxiliar(strIDCtaMayor, session).booleanValue()
			  && strIDAuxiliar.length() == 0)
			  throw new Error("La cuenta de mayor debe tener un auxiliar <br> "+
				  "Imposible grabar movimiento sin auxiliar");

		  //De lo contrario
		  if (!getLlevaAuxiliar(strIDCtaMayor, session).booleanValue()
			  && strIDAuxiliar.length() > 0)
			  throw new Error("La cuenta de mayor no debe tener un auxiliar <br> "+
				  "Imposible grabar movimiento con auxiliar");
	  }
	  
//	Obtener atributo Status
	  public double getPolizaImporte
	  (
		  String strIDEjercicio, String strIDLibro, String strIDCCosto, String strFolio
	  ) throws SQLException, Exception
	  {
		  double dblImporte = 0;

		  String COMANDO = "SELECT COALESCE(SUM(IMPORTE*CASE NATURALEZA WHEN 'D' THEN -1 ELSE 1 END),0) IMPORTE ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_LIBRO = ? ";
		  COMANDO += "AND ID_CCOSTO = ? ";
		  COMANDO += "AND FOLIO = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  pstmt.setString(2, strIDLibro);
		  pstmt.setString(3, strIDCCosto);
		  pstmt.setString(4, strFolio);
		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  dblImporte = rset.getDouble("Importe");
		  }
		  rset.close();
		  pstmt.close();

		  if (dblImporte == 0)
			  throw new Error("Numero de poliza invalido");

		  return dblImporte;
	  }
	  
//	Obtener Saldo anterior de la cuenta indicada
	  public double getEdoCtaSaldoAnterior
	  (
		  String strIDEjercicio, String strIDCtaMayor, String strIDCCosto,
		  String strIDAuxiliar, String strFechaInicial, String strTipo
	  ) throws SQLException, Exception
	  {
		  double dblImporte = 0;

		  PreparedStatement pstmt = null;

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  String COMANDO = "SELECT COALESCE(SUM(M.IMPORTE*CASE M.NATURALEZA WHEN 'D' THEN -1 ELSE 1 END ),0) AS IMPORTE ";
				  COMANDO += "FROM MATEO.CONT_MOVIMIENTO M, CONT_POLIZA P ";
				  COMANDO += "WHERE M.ID_EJERCICIO = P.ID_EJERCICIO ";
				  COMANDO += "AND M.ID_LIBRO = P.ID_LIBRO ";
				  COMANDO += "AND M.ID_CCOSTO = P.ID_CCOSTO ";
				  COMANDO += "AND M.FOLIO = P.FOLIO ";

		  if (strTipo.equals("T"))
		  {

		  }
		  else if (strTipo.equals("P"))
		  {
			  //Movimientos que no han afectado, que son de polizas abiertas o en revision
			  COMANDO += "AND P.STATUS IN ('A', 'R') ";
		  }
		  else if (strTipo.equals("A"))
		  {
			  //Movimientos que ya afectaron, solo polizas cerradas y definitivamente cerradas
			  COMANDO += "AND P.STATUS IN ('C', 'D') ";
		  }
		  else
			  throw new Error("Tipo de busqueda invalido");

		  COMANDO += "AND M.FECHA < TO_DATE(?, 'dd-mm-yy') ";
		  COMANDO += "AND M.ID_EJERCICIO = ? ";
		  COMANDO += "AND M.ID_CTAMAYORM = ? ";
		  COMANDO += "AND M.ID_CCOSTOM = ? ";

		  if (strIDAuxiliar.equals("0000000"))
		  {
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strFechaInicial);
			  pstmt.setString(2, strIDEjercicio);
			  pstmt.setString(3, strIDCtaMayor);
			  pstmt.setString(4, strIDCCosto);
		  }
		  else
		  {
			  COMANDO += "AND M.ID_AUXILIARM = ? ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strFechaInicial);
			  pstmt.setString(2, strIDEjercicio);
			  pstmt.setString(3, strIDCtaMayor);
			  pstmt.setString(4, strIDCCosto);
			  pstmt.setString(5, strIDAuxiliar);
		  }

		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  dblImporte = rset.getDouble("Importe");
		  }
		  rset.close();
		  pstmt.close();

		  return dblImporte;
	  }
	  
//	Obtener importe subtotal de la cuenta indicada
	  public double getEdoCtaSubTotal
	  (
		  String strIDEjercicio, String strIDCtaMayor, String strIDCCosto,
		  String strIDAuxiliar, String strFechaInicial, String strFechaFinal,
		  String strTipo
	  ) throws SQLException, Exception
	  {
		  double dblImporte = 0;

		  PreparedStatement pstmt = null;

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  String COMANDO = "SELECT COALESCE(SUM(IMPORTE*CASE NATURALEZA WHEN 'D' THEN -1 ELSE 1 END),0) IMPORTE ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";

		  if (strTipo.equals("T"))
		  {
			  //Todos los movimientos, incluyendo polizas abiertas y cerradas
			  COMANDO += "WHERE ID_EJERCICIO || ID_LIBRO || ID_CCOSTO || FOLIO IN ";
			  COMANDO += "(SELECT ID_EJERCICIO || ID_LIBRO || ID_CCOSTO || FOLIO ";
			  COMANDO += " FROM MATEO.CONT_POLIZA ";
			  COMANDO += " WHERE STATUS IN ('A', 'R', 'C', 'D')) ";
		  }
		  else if (strTipo.equals("P"))
		  {
			  //Movimientos por afectar, solo polizas abiertas y en revision
			  COMANDO += "WHERE ID_EJERCICIO || ID_LIBRO || ID_CCOSTO || FOLIO IN ";
			  COMANDO += "(SELECT ID_EJERCICIO || ID_LIBRO || ID_CCOSTO || FOLIO ";
			  COMANDO += " FROM MATEO.CONT_POLIZA ";
			  COMANDO += " WHERE STATUS IN ('A', 'R')) ";
		  }
		  else if (strTipo.equals("A"))
		  {
			  //Movimientos que ya afectaron, solo polizas cerradas y definitivamente cerradas
			  COMANDO += "WHERE ID_EJERCICIO || ID_LIBRO || ID_CCOSTO || FOLIO IN ";
			  COMANDO += "(SELECT ID_EJERCICIO || ID_LIBRO || ID_CCOSTO || FOLIO ";
			  COMANDO += " FROM MATEO.CONT_POLIZA ";
			  COMANDO += " WHERE STATUS IN ('C', 'D')) ";
		  }
		  else
			  throw new Error("Tipo de busqueda invalido");

		  COMANDO += "AND FECHA BETWEEN TO_DATE(?, 'dd-mm-yy') AND TO_DATE(?, 'dd-mm-yy') ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_CTAMAYORM = ? ";
		  COMANDO += "AND ID_CCOSTOM = ? ";

		  if (strIDAuxiliar.equals("0000000"))
		  {
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strFechaInicial);
			  pstmt.setString(2, strFechaFinal);
			  pstmt.setString(3, strIDEjercicio);
			  pstmt.setString(4, strIDCtaMayor);
			  pstmt.setString(5, strIDCCosto);
		  }
		  else
		  {
			  COMANDO += "AND ID_AUXILIARM = ? ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strFechaInicial);
			  pstmt.setString(2, strFechaFinal);
			  pstmt.setString(3, strIDEjercicio);
			  pstmt.setString(4, strIDCtaMayor);
			  pstmt.setString(5, strIDCCosto);
			  pstmt.setString(6, strIDAuxiliar);
		  }

		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  dblImporte = rset.getDouble("Importe");
		  }
		  rset.close();
		  pstmt.close();

		  return dblImporte;
	  }
	  
//	Obtener importe total de una cuenta
	  public double getImporteTotal
	  (
		  String strIDEjercicio, String strIDCtaMayor, String strIDCCosto,
		  String strIDAuxiliar, String strFechaI, String strFechaF
	  ) throws SQLException, Exception
	  {
		  double dblImporte = 0;
		  double dblSAnterior = 0;

		  PreparedStatement pstmt = null;

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  //Obtener Saldo Anterior
		  String COMANDO = "SELECT COALESCE(SUM(IMPORTE*CASE NATURALEZA WHEN 'D' THEN -1 ELSE 1 END),0) IMPORTE ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
		  COMANDO += "WHERE FECHA < TO_DATE(?, 'dd-mm-yy') ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_CTAMAYORM = ? ";
		  COMANDO += "AND ID_CCOSTOM = ? ";
		  COMANDO += "AND ID_AUXILIARM = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strFechaI);
		  pstmt.setString(2, strIDEjercicio);
		  pstmt.setString(3, strIDCtaMayor);
		  pstmt.setString(4, strIDCCosto);
		  pstmt.setString(5, strIDAuxiliar);

		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  dblSAnterior = rset.getDouble("Importe");
		  }
		  rset.close();
		  pstmt.close();

		  //Obtener Importe del rango
		  COMANDO = "SELECT COALESCE(SUM(IMPORTE*CASE NATURALEZA WHEN 'D' THEN -1 ELSE 1 END),0) IMPORTE ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
		  COMANDO += "WHERE FECHA BETWEEN TO_DATE(?, 'dd-mm-yy') AND TO_DATE(?, 'dd-mm-yy') ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_CTAMAYORM = ? ";
		  COMANDO += "AND ID_CCOSTOM = ? ";
		  COMANDO += "AND ID_AUXILIARM = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strFechaI);
		  pstmt.setString(2, strFechaF);
		  pstmt.setString(3, strIDEjercicio);
		  pstmt.setString(4, strIDCtaMayor);
		  pstmt.setString(5, strIDCCosto);
		  pstmt.setString(6, strIDAuxiliar);

		  rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  dblImporte = rset.getDouble("Importe");
		  }
		  rset.close();
		  pstmt.close();

		  return dblSAnterior + dblImporte;
	  }
	  
//	Obtener importe total de creditos de una cuenta
	  public double getTotalCreditos
	  (
		  String strIDEjercicio, String strIDCtaMayor, String strIDCCosto,
		  String strIDAuxiliar, String strFechaI, String strFechaF
	  ) throws SQLException, Exception
	  {
		  double dblImporte = 0;

		  PreparedStatement pstmt = null;

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  //Obtener Importe del rango
		  String COMANDO = "SELECT COALESCE(SUM(IMPORTE),0) IMPORTE ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
		  COMANDO += "WHERE NATURALEZA = 'C' ";
		  COMANDO += "AND FECHA BETWEEN TO_DATE(?, 'dd-mm-yy') AND TO_DATE(?, 'dd-mm-yy') ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_CTAMAYORM = ? ";
		  COMANDO += "AND ID_CCOSTOM = ? ";
		  COMANDO += "AND ID_AUXILIARM = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strFechaI);
		  pstmt.setString(2, strFechaF);
		  pstmt.setString(3, strIDEjercicio);
		  pstmt.setString(4, strIDCtaMayor);
		  pstmt.setString(5, strIDCCosto);
		  pstmt.setString(6, strIDAuxiliar);

		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  dblImporte = rset.getDouble("Importe");
		  }
		  rset.close();
		  pstmt.close();

		  return dblImporte;
	  }
	  
//	Obtener importe total de cargos de una cuenta
	  public double getTotalCargos
	  (
		  String strIDEjercicio, String strIDCtaMayor, String strIDCCosto,
		  String strIDAuxiliar, String strFechaI, String strFechaF
	  ) throws SQLException, Exception
	  {
		  double dblImporte = 0;

		  PreparedStatement pstmt = null;

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  //Obtener Importe del rango
		  String COMANDO = "SELECT COALESCE(SUM(IMPORTE),0) IMPORTE ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
		  COMANDO += "WHERE NATURALEZA = 'D' ";
		  COMANDO += "AND FECHA BETWEEN TO_DATE(?, 'dd-mm-yy') AND TO_DATE(?, 'dd-mm-yy') ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_CTAMAYORM = ? ";
		  COMANDO += "AND ID_CCOSTOM = ? ";
		  COMANDO += "AND ID_AUXILIARM = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strFechaI);
		  pstmt.setString(2, strFechaF);
		  pstmt.setString(3, strIDEjercicio);
		  pstmt.setString(4, strIDCtaMayor);
		  pstmt.setString(5, strIDCCosto);
		  pstmt.setString(6, strIDAuxiliar);

		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  dblImporte = rset.getDouble("Importe");
		  }
		  rset.close();
		  pstmt.close();

		  return dblImporte;
	  }
	  
//		Obtener importe total de cargos de una cuenta
	  public double getSaldoAnterior
	  (
		  String strIDEjercicio, String strIDCtaMayor, String strIDCCosto,
		  String strIDAuxiliar, String strFechaI
	  ) throws SQLException, Exception
	  {
		  double dblImporte = 0;

		  PreparedStatement pstmt = null;

		  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
			  strIDAuxiliar = "0000000";

		  //Obtener Importe del rango
		  String COMANDO = "SELECT COALESCE(SUM(IMPORTE),0) IMPORTE ";
		  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
		  COMANDO += "WHERE NATURALEZA = 'D' ";
		  COMANDO += "AND FECHA < TO_DATE(?, 'dd-mm-yy') ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  COMANDO += "AND ID_CTAMAYORM = ? ";
		  COMANDO += "AND ID_CCOSTOM = ? ";
		  COMANDO += "AND ID_AUXILIARM = ? ";
		  pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strFechaI);		  
		  pstmt.setString(2, strIDEjercicio);
		  pstmt.setString(3, strIDCtaMayor);
		  pstmt.setString(4, strIDCCosto);
		  pstmt.setString(5, strIDAuxiliar);

		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  dblImporte = rset.getDouble("Importe");
		  }
		  rset.close();
		  pstmt.close();

		  return dblImporte;
	  }
	  
//	Obtener nombre del ejercicio contable
	  public String getNombreEjercicio
	  (
		  String strIDEjercicio
	  ) throws SQLException, Exception
	  {
		  String strNombre = null;

		  String COMANDO = "SELECT NOMBRE ";
		  COMANDO += "FROM CONT_EJERCICIO ";
		  COMANDO += "WHERE STATUS = 'A' ";
		  COMANDO += "AND ID_EJERCICIO = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, strIDEjercicio);
		  ResultSet rset = pstmt.executeQuery();

		  if (rset.next())
		  {
			  strNombre = rset.getString("Nombre");
		  }
		  rset.close();
		  pstmt.close();

		  if(strNombre == null)
			  throw new Error("El ejercicio no existe "+strIDEjercicio);

		  return strNombre;
	  }
	  
//	Obtener listado de folios dependiendo los datos de la session
	  public PreparedStatement getFoliosSession
	  (
		  HttpSession session
	  ) throws SQLException, Exception
	  {
                  String COMANDO = "SELECT ID_EJERCICIO, ID_CCOSTO, ID_LIBRO ";
		  COMANDO += "FROM CONT_FOLIO ";
		  COMANDO += "WHERE LOGIN = ? ";
				  COMANDO += "AND ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_CCOSTO = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, (String)session.getAttribute("login"));
				  pstmt.setString(2, (String)session.getAttribute("id_ejercicio"));
				  pstmt.setString(3, (String)session.getAttribute("id_ccosto"));

		  return pstmt;
	  }

      //Obtener listado de ejercicios y contabilidades activos del usuario
	  public PreparedStatement getContabilidades
	  (
		  HttpSession session
	  ) throws SQLException, Exception
	  {
		  String COMANDO = "SELECT DISTINCT ID_CCOSTO ";
		  COMANDO += "FROM CONT_FOLIO ";
		  COMANDO += "WHERE LOGIN = ? ";
          COMANDO += "AND ID_EJERCICIO = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, (String)session.getAttribute("login"));
				  pstmt.setString(2, (String)session.getAttribute("ejercicio"));

		  return pstmt;
	  }

	  //Obtener listado de ejercicios y contabilidades activos del usuario
	  public PreparedStatement getContabilidades
	  (
		  String ejercicio, HttpSession session
	  ) throws SQLException, Exception
	  {
		  String COMANDO = "SELECT DISTINCT ID_CCOSTO ";
		  COMANDO += "FROM CONT_FOLIO ";
		  COMANDO += "WHERE LOGIN = ? ";
				  COMANDO += "AND ID_EJERCICIO = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, (String)session.getAttribute("login"));
				  pstmt.setString(2, ejercicio);

		  return pstmt;
	  }
	  
//	Obtener listado de ejercicios y contabilidades activos del usuario
	  public PreparedStatement getContabilidades
	  (
		  String ejercicio
	  ) throws SQLException, Exception
	  {
		  String COMANDO = "SELECT DISTINCT ID_CCOSTO ";
		  COMANDO += "FROM CONT_FOLIO ";
		  COMANDO += "WHERE ID_EJERCICIO = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, ejercicio);

		  return pstmt;
	  }
	  
//	Obtener listado de ejercicios y contabilidades activos del usuario
	  public PreparedStatement getEjercicios
	  (
		  HttpSession session
	  ) throws SQLException, Exception
	  {
		  String COMANDO = "SELECT DISTINCT ID_EJERCICIO ";
		  COMANDO += "FROM CONT_FOLIO ";
		  COMANDO += "WHERE LOGIN = ? ";
		  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
		  pstmt.setString(1, (String)session.getAttribute("login"));

		  return pstmt;
	  }
	  
//		Validar si la cuenta tiene longitud valida
		  public boolean getEsCuentaLengthValida(String strCtaMayor, String strCCosto, String strAuxiliar,HttpSession session) throws SQLException, Exception
		  {
			  String strTipoCta = null;
			  String strMascCtaMayor = null;
			  String strMascCCosto = null;
			  String strMascAuxiliar = null;
			  String strNivelContable = null;

			  boolean blnSw = true;

			  //Obtener ejercicio de la session
			  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");

			  //Obtener tipo de cuenta de mayor (B,R)
			  String COMANDO = "SELECT TIPO_CUENTA ";
			  COMANDO += "FROM MATEO.CONT_CTAMAYOR ";
			  COMANDO += "WHERE DETALLE = 'S' ";
			  COMANDO += "AND ID_EJERCICIO = ? ";
			  COMANDO += "AND ID_CTAMAYOR = ? ";
			  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strIDEjercicio);
			  pstmt.setString(2, strCtaMayor);
			  ResultSet rset = pstmt.executeQuery();

			  if (rset.next())
				  strTipoCta = rset.getString("Tipo_Cuenta");
			  else
				  throw new Error("Cta. de mayor invalida!");
			  pstmt.close();
			  rset.close();

			  //Obtener mascara de la cuenta de mayor
			  //Si tipo de cuenta es Balance
			  if (strTipoCta.equals("B"))
			  {
				  //Obtener la mascara de la cuenta de balance
				  COMANDO = "SELECT MASC_BALANCE MASC ";
			  }
			  //Si tipo de cuenta es de resultados
			  else if (strTipoCta.equals("R"))
			  {
				  //Obtener la mascara de la cuenta de resultados
				  COMANDO = "SELECT MASC_RESULTADO MASC ";
			  }

			  COMANDO += "FROM CONT_EJERCICIO ";
			  COMANDO += "WHERE ID_EJERCICIO = ? ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strIDEjercicio);
			  rset = pstmt.executeQuery();

			  if (rset.next())
				  strMascCtaMayor = rset.getString("Masc");
			  else
				  throw new Error("Ejercicio invalido");
			  pstmt.close();
			  rset.close();

			  //Obtener mascara del centro de costo
			  COMANDO = "SELECT MASC_CCOSTO MASC, NIVEL_CONTABLE ";
			  COMANDO += "FROM CONT_EJERCICIO ";
			  COMANDO += "WHERE ID_EJERCICIO = ? ";
			  pstmt = conn.prepareStatement(COMANDO);
			  pstmt.setString(1, strIDEjercicio);
			  rset = pstmt.executeQuery();

			  if (rset.next())
			  {
				  strMascCCosto = rset.getString("Masc");
				  strNivelContable = rset.getString("Nivel_Contable");
			  }
			  else
				  throw new Error("Ejercicio invalido");
			  pstmt.close();
			  rset.close();

			  //Si la cuenta de mayor debe llevar auxiliar
			  if (metodos2.rctaLlevaAuxiliar(strCtaMayor, session).booleanValue())
			  {
				  //Por ahora la mascara del auxiliar quedara fija a 7
				  strMascAuxiliar = "7";
			  }

			  //Obtener longitud que debe tener el centro de costo
			  if (getLongitudMascara(strCtaMayor, strMascCtaMayor))
				  throw new Error("Cuenta de Mayor es incorrecta, favor de volverla a teclear");

			  if (strTipoCta.equals("B"))
			  {
				  if (getLongitudMascara(strCCosto, strMascCCosto, strNivelContable))
					  throw new Error("Contabilidad es incorrecta, favor de volverla a teclear");
			  }
			  else if (strTipoCta.equals("R"))
			  {
				  if (getLongitudMascara(strCCosto, strMascCCosto))
					  throw new Error("Cuenta de centro de costo es incorrecta, favor de volverla a teclear");
			  }
			  else
				  throw new Error("Tipo de cuenta de mayor invalido");

			  if (getLongitudMascara(strAuxiliar, strMascAuxiliar))
				  throw new Error("Cuenta de auxiliar es incorrecta, favor de volverla a teclear");

			  return blnSw;
		  }
		  
//		Validar si la longitud de la cuenta coincide con la longitud especificada en la mascara de la cuenta
		  public boolean getLongitudMascara
		  (
			  String strCuenta, String strMascara
		  ) throws SQLException, Exception
		  {
			  //Obtener la longitud de la mascara
			  Integer intConta = new Integer(0);
			  Integer intAcum = new Integer(0);

			  while (intConta.compareTo(new Integer(strMascara.length())) < 0 )
			  {
				  //Obtener un digito de la mascara y acumularlo
				  intAcum = new Integer(intAcum.intValue() + new Integer(strMascara.substring(intConta.intValue(), 1)).intValue());
				  intConta = new Integer(intConta.intValue() + 1);
			  }

			  //Agregar al acumulado, la logitud de la mascara - 1, para sustituir los puntos
			  intAcum = new Integer(intAcum.intValue() + strMascara.length() - 1);

			  //Regresar el valor boolean que se obtiene de comparar la longitud de la cuenta con el valor acumulado
			  return (intAcum.compareTo(new Integer(strCuenta.length())) == 0 );
		  }
		  
//		Validar si la longitud de la cuenta coincide con la longitud especificada en la mascara de la cuenta
		  //hasta el nivel contable
		  public boolean getLongitudMascara
		  (
			  String strCuenta, String strMascara, String strNivelContable
		  ) throws SQLException, Exception
		  {
			  //Obtener la longitud de la mascara
			  Integer intConta = new Integer(0);
			  Integer intAcum = new Integer(0);

			  //Validamos contra el nivel contable, para obtener solo el numero de digitos especificado por este
			  while (intConta.compareTo(new Integer(strNivelContable)) < 0 )
			  {
				  //Obtener un digito de la mascara y acumularlo
				  intAcum = new Integer(intAcum.intValue() + new Integer(strMascara.substring(intConta.intValue(), 1)).intValue());
				  intConta = new Integer(intConta.intValue() + 1);
			  }

			  //Agregar al acumulado, la logitud de la mascara - 1, para sustituir los puntos
			  intAcum = new Integer(intAcum.intValue() + strMascara.length() - 1);


			  //Regresar el valor boolean que se obtiene de comparar la longitud de la cuenta con el valor acumulado
			  return (intAcum.compareTo(new Integer(strCuenta.length())) == 0 );
		  }
		  
		//Generar listado de polizas que no cuadran
			  public java.util.Vector getPolizasNoCuadradas
			  (
					  HttpSession session
			  ) throws SQLException, Exception
			  {
					  String strIDEjercicio = (String)session.getAttribute("id_ejercicio");

					  java.util.Vector vctPolizas = new java.util.Vector();

					  String COMANDO = "SELECT ID_LIBRO, ID_CCOSTO, FOLIO, U.USERNAME ID_USUARIO, ";
					  COMANDO += "COALESCE(REVISADO_POR,'.') REVISADO_POR, STATUS, TO_DATE(FECHA,'DD/MM/YY') FECHA ";
					  COMANDO += "FROM MATEO.CONT_POLIZA P, NOE.APP_USER U ";
					  COMANDO += "WHERE ID_EJERCICIO = ? ";
					  COMANDO += "AND P.ID_USUARIO = U.ID ";
					  COMANDO += "ORDER BY TO_NUMBER(ID_LIBRO), ID_CCOSTO, TO_NUMBER(FOLIO), STATUS ";

					  PreparedStatement pstmt = conn.prepareStatement(COMANDO);
					  pstmt.setString(1, strIDEjercicio);

					  ResultSet rset = pstmt.executeQuery();

					  while (rset.next())
					  {
							  String strFolio = rset.getString("Folio");
							  String strIDLibro = rset.getString("ID_Libro");
							  String strIDCCosto = rset.getString("ID_CCosto");
							  String strStatus = rset.getString("Status");
							  String strFecha = rset.getString("Fecha");
							  String strIDUsuario = rset.getString("ID_Usuario");
							  String strRevisadoPor = rset.getString("Revisado_Por");

							  COMANDO = "SELECT COALESCE(SUM(IMPORTE),0) IMPORTE ";
							  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
							  COMANDO += "WHERE ID_EJERCICIO = ? ";
							  COMANDO += "AND ID_LIBRO = ? ";
							  COMANDO += "AND ID_CCOSTO = ? ";
							  COMANDO += "AND FOLIO = ? ";
							  COMANDO += "AND NATURALEZA = 'C' ";

							  double dblCreditos = 0;
							  PreparedStatement pstmt2 = conn.prepareStatement(COMANDO);
							  pstmt2.setString(1, strIDEjercicio);
							  pstmt2.setString(2, strIDLibro);
							  pstmt2.setString(3, strIDCCosto);
							  pstmt2.setString(4, strFolio);
							  ResultSet rset2 = pstmt2.executeQuery();

							  if (rset2.next())
									  dblCreditos = rset2.getDouble("Importe");

							  pstmt2.close();
							  rset2.close();

							  double dblCargos = 0;
							  COMANDO = "SELECT COALESCE(SUM(IMPORTE),0) IMPORTE ";
							  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
							  COMANDO += "WHERE ID_EJERCICIO = ? ";
							  COMANDO += "AND ID_LIBRO = ? ";
							  COMANDO += "AND ID_CCOSTO = ? ";
							  COMANDO += "AND FOLIO = ? ";
							  COMANDO += "AND NATURALEZA = 'D' ";

							  pstmt2 = conn.prepareStatement(COMANDO);
							  pstmt2.setString(1, strIDEjercicio);
							  pstmt2.setString(2, strIDLibro);
							  pstmt2.setString(3, strIDCCosto);
							  pstmt2.setString(4, strFolio);
							  rset2 = pstmt2.executeQuery();

							  if (rset2.next())
									  dblCargos = rset2.getDouble("Importe");

							  pstmt2.close();
							  rset2.close();

							  if (dblCargos != dblCreditos)
							  {
									  vctPolizas.add(strIDEjercicio+"@"+
											  strIDLibro+"@"+
											  strIDCCosto+"@"+
											  strFolio+"@"+
											  strStatus+"@"+
											  strFecha+"@"+
											  strIDUsuario+"@"+
											  strRevisadoPor+"@"+
											  String.valueOf(dblCargos)+"@"+
											  String.valueOf(dblCreditos));
							  }
					  }

					  pstmt.close();
					  rset.close();

					  return vctPolizas;
			  }
			  /**
			   * Obtiene los creditos de una cuenta de alumno, ya que valida que las polizas leidas no sean de inscripcion.
			   * Esta validacion la realiza mediante el campo referencia2
			   * @param strIDEjercicio
			   * @param strIDCtaMayor
			   * @param strIDCCosto
			   * @param strIDAuxiliar
			   * @param strFechaI
			   * @param strFechaF
			   * @param strCargaId
			   * @param bloque
			   * @return
			   * @throws SQLException
			   * @throws Exception
			   */
			  public double getTotalCreditosAlumnos
			  (
				  String strIDEjercicio, String strIDCtaMayor, String strIDCCosto,
				  String strIDAuxiliar, String strFechaI, String strFechaF,
				  String strCargaId, Integer bloque
			  ) throws SQLException, Exception
			  {
				  double dblImporte = 0;

				  PreparedStatement pstmt = null;

				  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
					  strIDAuxiliar = "0000000";

				  //Obtener Importe del rango
				  String COMANDO = "SELECT COALESCE(SUM(IMPORTE),0) IMPORTE ";
				  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
				  COMANDO += "WHERE NATURALEZA = 'C' ";
				  COMANDO += "AND FECHA BETWEEN TO_DATE(?, 'dd-mm-yy') AND TO_DATE(?, 'dd-mm-yy') ";
				  COMANDO += "AND ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_CTAMAYORM = ? ";
				  COMANDO += "AND ID_CCOSTOM = ? ";
				  COMANDO += "AND ID_AUXILIARM = ? ";
				  COMANDO += "AND COALESCE(REFERENCIA2,'-') != ? ";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strFechaI);
				  pstmt.setString(2, strFechaF);
				  pstmt.setString(3, strIDEjercicio);
				  pstmt.setString(4, strIDCtaMayor);
				  pstmt.setString(5, strIDCCosto);
				  pstmt.setString(6, strIDAuxiliar);
				  pstmt.setString(7, strCargaId+bloque);

				  ResultSet rset = pstmt.executeQuery();

				  if (rset.next())
				  {
					  dblImporte = rset.getDouble("Importe");
				  }
				  rset.close();
				  pstmt.close();

				  return dblImporte;
			  }
			  /**
			   * Obtiene los cargos de una cuenta de alumno, ya que valida que las polizas leidas no sean de inscripcion.
			   * Esta validacion la realiza mediante el campo referencia2
			   * @param strIDEjercicio
			   * @param strIDCtaMayor
			   * @param strIDCCosto
			   * @param strIDAuxiliar
			   * @param strFechaI
			   * @param strFechaF
			   * @param strCargaId
			   * @param bloque
			   * @return
			   * @throws SQLException
			   * @throws Exception
			   */
			  public double getTotalCargosAlumnos
			  (
				  String strIDEjercicio, String strIDCtaMayor, String strIDCCosto,
				  String strIDAuxiliar, String strFechaI, String strFechaF,
				  String strCargaId, Integer bloque
			  ) throws SQLException, Exception
			  {
				  double dblImporte = 0;

				  PreparedStatement pstmt = null;

				  if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
					  strIDAuxiliar = "0000000";

				  //Obtener Importe del rango
				  String COMANDO = "SELECT COALESCE(SUM(IMPORTE),0) IMPORTE ";
				  COMANDO += "FROM MATEO.CONT_MOVIMIENTO ";
				  COMANDO += "WHERE NATURALEZA = 'D' ";
				  COMANDO += "AND FECHA BETWEEN TO_DATE(?, 'dd-mm-yy') AND TO_DATE(?, 'dd-mm-yy') ";
				  COMANDO += "AND ID_EJERCICIO = ? ";
				  COMANDO += "AND ID_CTAMAYORM = ? ";
				  COMANDO += "AND ID_CCOSTOM = ? ";
				  COMANDO += "AND ID_AUXILIARM = ? ";
				  COMANDO += "AND COALESCE(REFERENCIA2,'-') != ? ";
				  pstmt = conn.prepareStatement(COMANDO);
				  pstmt.setString(1, strFechaI);
				  pstmt.setString(2, strFechaF);
				  pstmt.setString(3, strIDEjercicio);
				  pstmt.setString(4, strIDCtaMayor);
				  pstmt.setString(5, strIDCCosto);
				  pstmt.setString(6, strIDAuxiliar);
				  pstmt.setString(7, strCargaId+bloque);

				  ResultSet rset = pstmt.executeQuery();

				  if (rset.next())
				  {
					  dblImporte = rset.getDouble("Importe");
				  }
				  rset.close();
				  pstmt.close();

				  return dblImporte;
			  }
}

