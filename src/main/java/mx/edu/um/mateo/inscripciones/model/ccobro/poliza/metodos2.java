/*
 * Created on 30/10/2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.poliza;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpSession;

import mx.edu.um.mateo.inscripciones.model.ccobro.cuenta.CtaMayor;
import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;

/**
 * @author Alberto
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class metodos2 extends Conexion{
	
	
	public metodos2(){
		
	}
	
	public metodos2(Connection conn){
		this.conn=conn;
	}
		//Obtener la naturaleza de la cuenta
		public String rctagetNaturaleza
		(
			String strIDCtaMayor, String strTipoCuenta
		) throws SQLException, Exception
		{
			String strNaturaleza = null;
			//Obtener naturaleza de la cuenta
			if (strTipoCuenta.equals("B"))
			{
				if (strIDCtaMayor.substring(0,1).equals("1"))
					strNaturaleza = "D";
				else if (strIDCtaMayor.substring(0,1).equals("2") ||
					 strIDCtaMayor.substring(0,1).equals("3"))
					strNaturaleza = "C";
				else
					throw new Error("Cuenta de mayor invalida"+strIDCtaMayor+"-"+strIDCtaMayor.substring(0,1));
			}
			else if (strTipoCuenta.equals("R"))
			{
				if (strIDCtaMayor.substring(0,1).equals("1"))
					strNaturaleza = "C";
				else if (strIDCtaMayor.substring(0,1).equals("2") ||
					 strIDCtaMayor.substring(0,1).equals("3"))
					strNaturaleza = "D";
				else
					throw new Error("Cuenta de mayor invalida"+strIDCtaMayor+"-"+strIDCtaMayor.substring(0,1));
			}
			else
				throw new Error("Tipo de cuenta invalido! <br> No se puedo determinar una naturaleza");
			return strNaturaleza;
		}


		//Obtener la naturaleza de la cuenta
		public String rctagetNaturaleza
		(
			String strIDEjercicio, String strIDCtaMayor,
			String strIDCCosto, String strIDAuxiliar
		) throws SQLException, Exception
		{
			String strNaturaleza = null;

			if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
				strIDAuxiliar = "0000000";

			String COMANDO = "SELECT NATURALEZA ";
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
				strNaturaleza = rset.getString("Naturaleza");
			}
			pstmt.close();
			rset.close();

			if (strNaturaleza == null)
				throw new Error("Cuenta invalida");

			return strNaturaleza;
		}


		//Obtener la naturaleza de la cuenta, regresando el importe con signo
		public Double rctagetNaturaleza
		(
			String strIDEjercicio, String strIDCtaMayor,
			Double dblImporte, String strNaturalezaImporte
		) throws SQLException, Exception
		{
			//Si el importe viene positivo, indica que su naturaleza es acreedora,
			//Si el importe viene negativo, indica que su naturaleza es deudora
			String strTipoCuenta = null;

			//Obtener el tipo de la cuenta de mayor (B,R)
			String COMANDO = "SELECT TIPO_CUENTA ";
			COMANDO += "FROM CONT_CTAMAYOR ";
			COMANDO += "WHERE DETALLE = 'S' ";
			COMANDO += "AND ID_EJERCICIO = ? ";
			COMANDO += "AND ID_CTAMAYOR = ? ";
			PreparedStatement pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, strIDEjercicio);
			pstmt.setString(2, strIDCtaMayor);
			ResultSet rset = pstmt.executeQuery();

			if (rset.next())
			{
				strTipoCuenta = rset.getString("Tipo_Cuenta");
			}
			pstmt.close();
			rset.close();

			if (strTipoCuenta == null)
				throw new Error("Cuenta invalida");

			if (rctagetNaturaleza(strIDCtaMayor, strTipoCuenta).equals("C"))
			{
				if (strNaturalezaImporte.equals("C"))
				{
					//Si la naturaleza del importe es acreedora
					//El importe queda igual (positivo)
				}
				else
				{
					//Si la naturaleza del importe es deudora
					//El importe se convierte a negativo
					dblImporte = new Double(dblImporte.doubleValue() * (-1));
					//para indicar que debe ser un cargo
				}
			}
			else
			{
				if (strNaturalezaImporte.equals("D"))
				{
					//Si la naturaleza del importe es acreedora
					//El importe queda igual (positivo)
				}
				else
				{
					//Si la naturaleza del importe es deudora
					//El importe se convierte a positivo
					dblImporte = new Double(dblImporte.doubleValue() * (-1));
					//para indicar que debe ser un credito
				}
			}

			return dblImporte;
		}


		//Crear relaci?n de centro de costo con cuenta de mayor
		public void rctaRelacionarCC_CtaMayor
		(
			String strIDCtaMayor, String strIDCCosto, String strNombre,
			String strTipoCuenta, HttpSession session
		) throws SQLException, Exception
		{
			String strIDEjercicio = (String)session.getAttribute("id_ejercicio");

			//Obtener nombre del centro de costo
			String COMANDO = "SELECT NOMBRE ";
			COMANDO += "FROM CONT_CCOSTO ";
			COMANDO += "WHERE ID_EJERCICIO = ? ";
			COMANDO += "AND ID_CCOSTO = ? ";
			PreparedStatement pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, strIDEjercicio);
			pstmt.setString(2, strIDCCosto);
			ResultSet rset = pstmt.executeQuery();

			if (rset.next())
				strNombre = rset.getString("Nombre");
			else
				throw new Error("Cuenta de centro de costo invalida");
			pstmt.close();
			rset.close();


			//Obtener nombre de la cuenta de mayor
			COMANDO = "SELECT NOMBRE ";
			COMANDO += "FROM CONT_CTAMAYOR ";
			COMANDO += "WHERE DETALLE = 'S' ";
			COMANDO += "AND ID_EJERCICIO = ? ";
			COMANDO += "AND ID_CTAMAYOR = ? ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, strIDEjercicio);
			pstmt.setString(2, strIDCtaMayor);
			rset = pstmt.executeQuery();

			if (rset.next())
				strNombre += " " + rset.getString("Nombre");
			else
				throw new Error("Cuenta de mayor invalida");
			pstmt.close();
			rset.close();

			if (strNombre.length() > 60)
				strNombre = strNombre.substring(0, 59);

			//Relacionar cuenta con el centro de costo
			COMANDO = "INSERT INTO CONT_RELACION ";
			COMANDO += "(ID_EJERCICIO, ID_CTAMAYOR, ID_CCOSTO, ID_AUXILIAR, NOMBRE, NATURALEZA, STATUS, TIPO_CUENTA, ID_EJERCICIO2, ID_EJERCICIO3) ";
			COMANDO += "VALUES ";
			COMANDO += "(?, ?, ?, '0000000', ?, ?, 'A',?,?,?) ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, strIDEjercicio);
			pstmt.setString(2, strIDCtaMayor);
			pstmt.setString(3, strIDCCosto);
			pstmt.setString(4, strNombre);
			pstmt.setString(5, rctagetNaturaleza(strIDCtaMayor, strTipoCuenta));
			pstmt.setString(6, new CtaMayor().getTipoCuenta(strIDEjercicio, strIDCtaMayor));
			pstmt.setString(7, strIDEjercicio);
			pstmt.setString(8, strIDEjercicio);
			pstmt.execute();
			pstmt.close();
		}


		//Quitar relaci?n de un centro de costo y una cuenta de mayor
		public void rctaQuitarRelacionCC_CtaMayor
		(
			String strIDCtaMayor, String strIDCCosto, HttpSession session
		) throws SQLException, Exception
		{
			String strIDEjercicio = (String)session.getAttribute("id_ejercicio");
			Integer intNReg = null;

			PreparedStatement pstmt2 = null;

			//Cancelar la relaci?n de la cuenta, si existen movimientos con dicha cuenta
			//o eliminar f?sicamente la cuenta, si no existen movimientos con la cuenta
			String COMANDO = "SELECT COUNT(*) NREG ";
			COMANDO += "FROM CONT_MOVIMIENTO ";
			COMANDO += "WHERE ID_EJERCICIO = ? ";
			COMANDO += "AND ID_CTAMAYORM = ? ";
			COMANDO += "AND ID_CCOSTOM = ? ";
			PreparedStatement pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, strIDEjercicio);
			pstmt.setString(2, strIDCtaMayor);
			pstmt.setString(3, strIDCCosto);
			ResultSet rset = pstmt.executeQuery();

			if (rset.next())
			{
				intNReg = new Integer(rset.getInt("NReg"));
			}
			pstmt.close();
			rset.close();

			COMANDO = "SELECT ROWID ";
			COMANDO += "FROM CONT_RELACION ";
			COMANDO += "WHERE ID_EJERCICIO = ? ";
			COMANDO += "AND ID_CTAMAYOR = ? ";
			COMANDO += "AND ID_CCOSTO = ? ";
			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, strIDEjercicio);
			pstmt.setString(2, strIDCtaMayor);
			pstmt.setString(3, strIDCCosto);
			rset = pstmt.executeQuery();

			while (rset.next())
			{
				if (intNReg.compareTo(new Integer(0)) > 0)
				{
					COMANDO = "UPDATE CONT_RELACION ";
					COMANDO += "SET STATUS = 'I' ";
					COMANDO += "WHERE ROWID = ? ";
					pstmt2 = conn.prepareStatement(COMANDO);
					pstmt2.setString(1, rset.getString("RowID"));
					pstmt2.execute();
					pstmt2.close();
				}
				else
				{
					COMANDO = "DELETE ";
					COMANDO += "FROM CONT_RELACION ";
					COMANDO += "WHERE ROWID = ? ";
					pstmt2 = conn.prepareStatement(COMANDO);
					pstmt2.setString(1, rset.getString("RowID"));
					pstmt2.execute();
					pstmt2.close();
				}
			}
		}


		//Crear relaci?n de centro de costo con cuenta de mayor y auxiliar
		public void rctaRelacionarCC_CM_Auxiliar
		(
			String strIDCtaMayor, String strIDCCosto, String strIDAuxiliar,
			String strNombre, HttpSession session
		) throws SQLException, Exception
		{

			//Verificar si la cuenta de mayor debe llevar auxiliar
			if (!rctaLlevaAuxiliar(strIDCtaMayor, session).booleanValue())
			{
				throw new Error("La cuenta de mayor "+strIDCtaMayor+" no puede relacionar con ningun auxiliar");
			}

			String strIDEjercicio = (String)session.getAttribute("id_ejercicio");

			String strNombreCtaMayor = null;
			String strTipoCuenta = null;
			String strNombreTemp = null;

			Integer intNReg = null;

					//Verificar si existe la cuenta con status desactivado
			String COMANDO = "SELECT COUNT(*) NREG ";
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
							intNReg = new Integer(rset.getInt("NReg"));
					}
					rset.close();
					pstmt.close();

					if (intNReg.compareTo(new Integer(0)) > 0)
					{
							//La cuenta ya existe, solo prendemos el status
							COMANDO = "UPDATE CONT_RELACION ";
							COMANDO += "SET STATUS = 'A' ";
							COMANDO += "WHERE ID_EJERCICIO = ? ";
							COMANDO += "AND ID_CTAMAYOR = ? ";
							COMANDO += "AND ID_CCOSTO = ? ";
							COMANDO += "AND ID_AUXILIAR = ? ";
							pstmt = conn.prepareStatement(COMANDO);
							pstmt.setString(1, strIDEjercicio);
							pstmt.setString(2, strIDCtaMayor);
							pstmt.setString(3, strIDCCosto);
							pstmt.setString(4, strIDAuxiliar);
							pstmt.executeUpdate();
							pstmt.close();
					}
					else
					{
							//Verificar si existe la relacion del centro de costo, la cuenta de mayor
							//y el auxiliar '0000000', si no entonces crearla
							COMANDO = "SELECT COUNT(*) NREG ";
							COMANDO += "FROM CONT_RELACION ";
							COMANDO += "WHERE ID_EJERCICIO = ? ";
							COMANDO += "AND ID_CTAMAYOR = ? ";
							COMANDO += "AND ID_CCOSTO = ? ";
							COMANDO += "AND ID_AUXILIAR = '0000000' ";
							pstmt = conn.prepareStatement(COMANDO);
							pstmt.setString(1, strIDEjercicio);
							pstmt.setString(2, strIDCtaMayor);
							pstmt.setString(3, strIDCCosto);
							rset = pstmt.executeQuery();

							if (rset.next())
									intNReg = new Integer(rset.getInt("NReg"));
							else
									throw new Error("Cuenta de Mayor y Centro de Costo invalidos");

							pstmt.close();
							rset.close();

							//Si la relaci?n entre el centro de costo y la cuenta de mayor no existe
							if (intNReg.compareTo(new Integer(0)) == 0)
							{
									//Obtener nombre y tipo de cuenta
									COMANDO = "SELECT NOMBRE, TIPO_CUENTA ";
									COMANDO += "FROM CONT_CTAMAYOR ";
									COMANDO += "WHERE DETALLE = 'S' ";
									COMANDO += "AND ID_EJERCICIO = ? ";
									COMANDO += "AND ID_CTAMAYOR = ? ";
									pstmt = conn.prepareStatement(COMANDO);
									pstmt.setString(1, strIDEjercicio);
									pstmt.setString(2, strIDCtaMayor);
									rset = pstmt.executeQuery();

									if(rset.next())
									{
											strNombreCtaMayor = rset.getString("Nombre");
											strTipoCuenta = rset.getString("Tipo_Cuenta");
									}
									else
											throw new Error("Cuenta de mayor invalida!");

									pstmt.close();
									rset.close();

									//Relacionar Centro de costo y cuenta de mayor
									rctaRelacionarCC_CtaMayor(strIDCtaMayor, strIDCCosto,
											strNombreCtaMayor, strTipoCuenta, session);
							}

							//Relacionar centro de costo, cuenta de mayor y el auxiliar
							//Obtener nombre del auxiliar
							COMANDO = "SELECT NOMBRE ";
							COMANDO += "FROM CONT_AUXILIAR ";
							COMANDO += "WHERE ID_EJERCICIO = ? ";
							COMANDO += "AND ID_AUXILIAR = ? ";
							pstmt = conn.prepareStatement(COMANDO);
							pstmt.setString(1, strIDEjercicio);
							pstmt.setString(2, strIDAuxiliar);
							rset = pstmt.executeQuery();

							if (rset.next())
									strNombre = rset.getString("Nombre");
							else
									throw new Error("Auxiliar invalido");
							pstmt.close();
							rset.close();

							//Obtener nombre de la relacion, para validar longitud
							COMANDO = "SELECT NOMBRE ";
							COMANDO += "FROM CONT_RELACION ";
							COMANDO += "WHERE ID_EJERCICIO = ? ";
							COMANDO += "AND ID_CTAMAYOR = ? ";
							COMANDO += "AND ID_CCOSTO = ? ";
							COMANDO += "AND ID_AUXILIAR = '0000000' ";
							pstmt = conn.prepareStatement(COMANDO);
							pstmt.setString(1, strIDEjercicio);
							pstmt.setString(2, strIDCtaMayor);
							pstmt.setString(3, strIDCCosto);
							rset = pstmt.executeQuery();

							if (rset.next())
									strNombreTemp = rset.getString("Nombre");

							pstmt.close();
							rset.close();

							strNombreTemp += strNombre;

							if (strNombreTemp.length() > 60)
									strNombreTemp = strNombreTemp.substring(0,59);

							COMANDO = "UPDATE CONT_RELACION ";
							COMANDO += "SET ID_AUXILIAR = ?, ";
							COMANDO += "NOMBRE = ? ";
							COMANDO += "WHERE ID_EJERCICIO = ? ";
							COMANDO += "AND ID_CTAMAYOR = ? ";
							COMANDO += "AND ID_CCOSTO = ? ";
							COMANDO += "AND ID_AUXILIAR = '0000000' ";
							pstmt = conn.prepareStatement(COMANDO);
							pstmt.setString(1, strIDAuxiliar);
							pstmt.setString(2, strNombreTemp);
							pstmt.setString(3, strIDEjercicio);
							pstmt.setString(4, strIDCtaMayor);
							pstmt.setString(5, strIDCCosto);
							pstmt.execute();
							pstmt.close();
					}//Fin else
		}


		//Quitar relaci?n de un centro de costo, una cuenta de mayor y un auxiliar
		//No se quita la relacion centro de costo y cuenta de mayor
		public void rctaQuitarRelacionCC_CM_Auxiliar
		(
			String strIDCtaMayor, String strIDCCosto, String strIDAuxiliar,
			HttpSession session
		) throws SQLException, Exception
		{
			String strIDEjercicio = (String)session.getAttribute("id_ejercicio");

			//Asignar auxiliar 0000000 para indicar que hace falta auxiliar
			String COMANDO = "UPDATE CONT_RELACION ";
			COMANDO += "SET STATUS = 'I' ";
			COMANDO += "WHERE ID_EJERCICIO = ? ";
			COMANDO += "AND ID_CTAMAYOR = ? ";
			COMANDO += "AND ID_CCOSTO = ? ";
			COMANDO += "AND ID_AUXILIAR = ? ";
			PreparedStatement pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, strIDEjercicio);
			pstmt.setString(2, strIDCtaMayor);
			pstmt.setString(3, strIDCCosto);
			pstmt.setString(4, strIDAuxiliar);
			pstmt.execute();
			pstmt.close();
		}


		//Obtener el atributo Auxiliar de la cuenta de mayor
		public Boolean rctaLlevaAuxiliar
		(
			String strIDCtaMayor, HttpSession session
		) throws SQLException, Exception
		{
			String strIDEjercicio = (String)session.getAttribute("id_ejercicio");

			String strLlevaAuxiliar = null;

			String COMANDO = "SELECT AUXILIAR ";
			COMANDO += "FROM CONT_CTAMAYOR ";
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
			else
				throw new Error("Cuenta de mayor invalida");
			pstmt.close();
			rset.close();

			if (strLlevaAuxiliar.equals("S"))
				return new Boolean(true);
			else
				return new Boolean("false");
		}


		//Obtener listado de auxiliares
		public PreparedStatement rctagetAuxiliares
		(
			String strIDEjercicio, String strIDCtaMayor, String strIDCCosto
		) throws SQLException, Exception
		{
			String COMANDO = "SELECT A.ID_AUXILIAR, A.NOMBRE ";
			COMANDO += "FROM CONT_AUXILIAR A, CONT_RELACION R ";
			COMANDO += "WHERE A.ID_AUXILIAR = R.ID_AUXILIAR ";
			COMANDO += "AND R.ID_EJERCICIO = ? ";
			COMANDO += "AND R.ID_CTAMAYOR = ? ";
			COMANDO += "AND R.ID_CCOSTO = ? ";
			PreparedStatement pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, strIDEjercicio);
			pstmt.setString(2, strIDCtaMayor);
			pstmt.setString(3, strIDCCosto);

			return pstmt;
		}


		//Obtener el atributo Auxiliar de la cuenta de mayor
		public Boolean rctaExisteCuenta
		(
			String strIDCtaMayor, String strIDCCosto, String strIDAuxiliar,
			HttpSession session
		) throws SQLException, Exception
		{
			String strIDEjercicio = (String)session.getAttribute("id_ejercicio");

			PreparedStatement pstmt = null;

			boolean blnSw = false;

			String COMANDO = "SELECT COUNT(*) NREG ";
			COMANDO += "FROM MATEO.CONT_RELACION ";
			COMANDO += "WHERE STATUS = 'A' ";
					COMANDO += "AND ID_EJERCICIO = ? ";
			COMANDO += "AND ID_CTAMAYOR = ? ";
			COMANDO += "AND ID_CCOSTO = ? ";
			COMANDO += "AND ID_AUXILIAR = ? ";

			if (strIDAuxiliar == null || strIDAuxiliar.length() == 0 || strIDAuxiliar.equals("-"))
				strIDAuxiliar = "0000000";

			pstmt = conn.prepareStatement(COMANDO);
			pstmt.setString(1, strIDEjercicio);
			pstmt.setString(2, strIDCtaMayor);
			pstmt.setString(3, strIDCCosto);
			pstmt.setString(4, strIDAuxiliar);

			ResultSet rset = pstmt.executeQuery();

			if (rset.next())
			{
				if (rset.getInt("NReg") > 0)
					blnSw = true;
			}
			pstmt.close();
			rset.close();

			return new Boolean(blnSw);
		}


}
