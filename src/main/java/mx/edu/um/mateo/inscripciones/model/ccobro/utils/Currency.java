/*
 * Created on Jul 23, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.utils;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Currency{
	/**
	 * 
	 */
	public Currency() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public static String parseCurrency
	(
		String strNumero, String strMoneda
	) throws Exception
	{
	String retVar = "";
	String num = "";
	String centavos = "";
	String tango = "";
	String  pesos = "";
	int intTamanoNumero = 0;
	int largo = 0;
	int veces = 0;
	int reslarg = 0;

	if (strNumero =="")
		return "";

	String unidad[] = new String[10];
	String decena[] = new String[10];
	String centena[] = new String[10];
	String millar[] =  new String[10];
	int bandera = 0;
	String decmillt = "";
	String ciecentt = "";
	String largic = "";
	String millt = "";
	String respnu = "";
	String veintes = "";
	String textt = "";
	String centt = "";
	String decet = "";
	String unidt = "";


	unidad[0] = "cero";
	unidad[1] = "un";
	unidad[2] = "dos";
	unidad[3] = "tres";
	unidad[4] = "cuatro";
	unidad[5] = "cinco";
	unidad[6] = "seis";
	unidad[7] = "siete";
	unidad[8] = "ocho";
	unidad[9] = "nueve";

	decena[1] = "diez";
	decena[2] = "veinte";
	decena[3] = "treinta";
	decena[4] = "cuarenta";
	decena[5] = "cincuenta";
	decena[6] = "sesenta";
	decena[7] = "setenta";
	decena[8] = "ochenta";
	decena[9] = "noventa";

	centena[1] = "ciento";
	centena[2] = "doscientos";
	centena[3] = "trescientos";
	centena[4] = "cuatrocientos";
	centena[5] = "quinientos";
	centena[6] = "seiscientos";
	centena[7] = "setecientos";
	centena[8] = "ochocientos";
	centena[9] = "novecientos";

	millar[1] = "mil";
	millar[2] = "dos mil";
	millar[3] = "tres mil";
	millar[4] = "cuatro mil";
	millar[5] = "cinco mil";
	millar[6] = "seis mil";
	millar[7] = "siete mil";
	millar[8] = "ocho mil";
	millar[9] = "nueve mil";

	java.util.StringTokenizer strtCantidad = new java.util.StringTokenizer(strNumero, ".");
	if(strtCantidad.countTokens() == 2){
		num = strtCantidad.nextToken();
		centavos =	strtCantidad.nextToken();
		intTamanoNumero = num.length();
		if(centavos.length() > 2)
			centavos = centavos.substring(0,2);
                if(centavos.length() < 2)
                        centavos += "0";
	}
	else if(strtCantidad.countTokens() == 1){
		num = strNumero;
		intTamanoNumero = num.length();
	}
	else{
		return "";
	}

	largo = num.length();

	veces = 1;

	if (largo > 6 ) {
			respnu = num;
			reslarg = largo;
			num = num.substring(0,largo - 6);
			largo = largo - 6;
			veces = 2;
	}


	for ( int a=1; a <= veces; a++ ) {
		if (a == 2 ) {
				num = respnu.substring(respnu.length() - 6 , respnu.length()); /**********verificar*******/
				largo = 6;
		}

		if ((Double.parseDouble(num) % 10) != 0 || strNumero.equals("0")){
			unidt = unidad[Integer.parseInt(num.substring(largo - 1, largo))];
		}
		else {
			unidt="";
		}

		if (largo > 1 ) {
				if (Integer.parseInt(num.substring(largo - 2, largo - 1)) > 0 ) {
					if (Integer.parseInt(num.substring(largo-1, largo)) > 0 ) {
						decet = decena[Integer.parseInt(num.substring(largo - 2, largo - 1))] + " y ";
					} else {
						decet = decena[Integer.parseInt(num.substring(largo - 2, largo - 1))];
					}
				} else {
					decet = "";
				}
		}
		if (largo > 2 ) {
				if (Integer.parseInt(num.substring(largo - 3, largo - 2)) > 1 ) {
				centt = centena[Integer.parseInt(num.substring( largo - 3, largo - 2))] + " ";
				} else {
					if (Integer.parseInt(num.substring(largo - 3, largo - 2)) == 0 )
                                        {
						centt = "";
                                        }
					else if (Integer.parseInt(num.substring( largo - 3, largo -2 )) == 1 && (Integer.parseInt(num.substring(largo - 2,num.length()))) > 0 )
                                        {
						centt = centena[Integer.parseInt(num.substring(largo - 3, largo - 2))] + " ";
                                        }
                                        else if (Integer.parseInt(num.substring( largo - 3, largo - 2)) == 1 && (Integer.parseInt(num.substring(largo - 2,num.length()))) == 0 )
                                        {
						centt = "cien";
                                        }
				}
		}
		if (largo > 3 ) {
				if (Integer.parseInt(num.substring( largo - 4, largo - 3)) > 1 ) {
				millt = unidad[Integer.parseInt(num.substring( largo - 4, largo - 3))] + " mil ";
				} else {
				if (Integer.parseInt(num.substring( largo - 4, largo - 3)) == 1 )
					millt = "un mil ";
				if (Integer.parseInt(num.substring( largo - 4, largo - 3)) == 0 )
					millt = " mil ";

				}
		}

		if (largo > 4 ) {

				if (Integer.parseInt(num.substring( largo - 5, largo - 4)) > 0 ) {
				if (Integer.parseInt(num.substring( largo - 4, largo - 3)) > 0 ) {
				decmillt = decena[Integer.parseInt(num.substring( largo - 5, largo - 4))] + " y ";
				} else {
				decmillt = decena[Integer.parseInt(num.substring( largo - 5, largo - 4))];
				}

				} else {
				decmillt = "";
				}
		}
		if (largo > 5 ) {
				if (Integer.parseInt(num.substring( largo - 6, largo - 5)) > 1 ) {
				ciecentt = centena[Integer.parseInt(num.substring( largo - 6, largo - 5))] + " ";
				} else {
				if (Integer.parseInt(num.substring( largo - 6, largo - 5)) == 0 )
					ciecentt = "";
				if (Integer.parseInt(num.substring( largo - 6, largo - 5)) == 1 && (Integer.parseInt(num.substring( largo - 5, largo - 3))) > 0 )
					ciecentt = centena[Integer.parseInt(num.substring( largo - 6, largo - 5))] + " ";
				if (Integer.parseInt(num.substring( largo - 6, largo - 5)) == 1 && (Integer.parseInt(num.substring( largo - 5, largo - 3))) == 0 )
					ciecentt = "cien ";
				}
		}


		if (decet.equals("diez y ")) {
		switch ( Integer.parseInt(num.substring( largo - 1, largo)) ){
		case 1:
			veintes = "once";
			break;
		case 2:
			veintes = "doce";
			break;
		case 3:
			veintes = "trece";
			break;
		case 4:
			veintes = "catorce";
			break;
		case 5:
			veintes = "quince";
			break;
		case 6:
			veintes = "dieciseis";
			break;
		case 7:
			veintes = "diecisiete";
			break;
		case 8:
			veintes = "dieciocho";
			break;
		case 9:
			veintes = "diecinueve";
			break;
		case 0:
			veintes = "diez";
			break;
		}
		decet = veintes;
		unidt = "";
		}		

		if (decet.equals("veinte y ")) {
		switch ( Integer.parseInt(num.substring( largo - 1, largo)) ){
		case 1:
			veintes = "veintiun";
			break;
		case 2:
			veintes = "veintidos";
			break;
		case 3:
			veintes = "veintitres";
			break;
		case 4:
			veintes = "veinticuatro";
			break;
		case 5:
			veintes = "veinticinco";
			break;
		case 6:
			veintes = "veintiseis";
			break;
		case 7:
			veintes = "veintisiete";
			break;
		case 8:
			veintes = "veintiocho";
			break;
		case 9:
			veintes = "veintinueve";
			break;
		case 0:
			veintes = "veinte";
			break;
		}
		decet = veintes;
		unidt = "";
		}		
	
		
		if (decmillt.equals("diez y ") ) {		
		switch (Integer.parseInt(num.substring( largo - 4, largo - 3))){
		case 1:			
			veintes = "once mil ";
			break;
		case 2:			
			veintes = "doce mil ";
			break;
		case 3:
			veintes = "trece mil ";
			break;
		case 4:
			veintes = "catorce mil ";
			break;
		case 5:
			veintes = "quince mil ";
			break;
		case 6:
			veintes = "dieciseis mil ";
			break;
		case 7:
			veintes = "diecisiete mil ";
			break;
		case 8:
			veintes = "dieciocho mil ";
			break;
		case 9:
			veintes = "diecinueve mil ";
			break;
		case 0:
			veintes = "diez mil ";
			break;
		}
		decmillt = veintes;
		millt = "";
		}
		
		if (decmillt.equals("veinte y ")) {
		switch ( Integer.parseInt(num.substring( largo - 4, largo - 3)) ){
		case 1:
			veintes = "veintiun mil ";
			break;
		case 2:
			veintes = "veintidos mil ";
			break;
		case 3:
			veintes = "veintitres mil ";
			break;
		case 4:
			veintes = "veinticuatro mil ";
			break;
		case 5:
			veintes = "veinticinco mil ";
			break;
		case 6:
			veintes = "veintiseis mil ";
			break;
		case 7:
			veintes = "veintisiete mil ";
			break;
		case 8:
			veintes = "veintiocho mil ";
			break;
		case 9:
			veintes = "veintinueve mil ";
			break;
		case 0:
			veintes = "veinte ";
			break;
		}
		decmillt = veintes;
		millt = "";
		}
		
		if (a == 1 && veces == 2 ) {
				textt = ciecentt + decmillt + millt + centt + decet + unidt;

				if (textt.substring(0, 2).equals("un")) {
				textt = ciecentt + decmillt + millt + centt + decet + unidt + " millon ";
				} else {
				textt = ciecentt + decmillt + millt + centt + decet + unidt + " millones ";
				}
		}

		if (veces > 1 ) {
			int inicio = 0 ;
			int fin = 0;
			inicio = num.length() - 6;
			
			if(inicio < 0)
				inicio = 0;			
			fin=num.length();		
			tango = num.substring(inicio,fin);			
			if (Integer.parseInt(tango.substring( 0, 1)) == 0 && Integer.parseInt(tango.substring( 1, 2)) == 0 && Integer.parseInt(tango.substring( 2, 3)) == 0 ){
				millt = "";
			}
		}
		
		if (a == 1 && veces == 1 ){		
		
			textt = ciecentt + decmillt + millt + centt + decet + unidt;
			
			}

		if (a == 2 && veces == 2 ){
		
			textt = textt + ciecentt + decmillt + millt + centt + decet + unidt;
			
			}
	
	}
	
	
	if (Double.parseDouble(strNumero) >= 1 && Double.parseDouble(strNumero) < 2){
			pesos= " peso ";
		} else {
			pesos = " pesos ";
	}
	
	if(respnu == ""){
	respnu = "0";
	}

	if(centavos.equals("0"))
		centavos = "00";
	if (Double.parseDouble(respnu) >= 1000000  && (Double.parseDouble(respnu) % 1000000)==0){
		if(strMoneda.equals("P"))
			retVar = "( " + textt + " de pesos " + centavos + "/100 M.N. )";
		else
			retVar = "( " + textt + " de dolares " + centavos + "/100 U.S. dls )";
	} else {
		if(strMoneda.equals("P"))
			retVar = "( " + textt + " pesos " + centavos + "/100 M.N. )";
		else
			retVar = "( " + textt + " dolares " + centavos + "/100 U.S. dls )";

	}

	return retVar;
	}
	
	 public static Double getRound
     (
             Double dblNum, Integer intDecimales
     ) throws Exception
     {
             if (intDecimales.compareTo(new Integer(0)) > 0)
             {
                     if (new  Integer((dblNum.toString()).length() -(dblNum.toString()).lastIndexOf(".")).compareTo(new Integer(intDecimales.intValue() + 1)) > 0)
                     {
							Double dblRedondeo = new Double(java.lang.Math.pow(10.0, new Double(intDecimales.toString()).doubleValue()));
							dblNum = new Double(java.lang.Math.round(dblNum.doubleValue() * dblRedondeo.doubleValue()) / dblRedondeo.doubleValue());							
                     }
             }

             return dblNum;
     }
}
