/*
 * Created on Jun 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package mx.edu.um.mateo.inscripciones.model.ccobro;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface Constant
{
    //Funciones para el calculo de cobro
    
    //Tipos de movimiento de Calculo de Cobro
    //Movimientos que se toman en cuenta para el costo total del bloque
    //01 - Matricula
    //02 - Ense?anza
    //03 - Internado
    //04 - Descuento hijo de obrero
    //05 - Descuento de obrero ASD
    //06 - Descuento pago de contado
    //07 - Becas
    //08 - Manejo de pagare
    //09 - Comedor
    //10 - CobroClima
    
    //Movimientos que se toman para el total de la cuota de inscripci?n
    //20 - Saldo Anterior
    //21 - Descuentos Varios
    //22 = Colportaje
    //23 - Cuota Inscripcion
    //24 - Matricula Extemporanea
    //25 - Convenio trabajo
    //26 - Beca Adicional
    //27 - Alumno Institucional
    //28 - Diezmo de Beca Institucional
    
    //Movimientos que se toman en cuenta dependiendo del valor del atributo
    //aplica_en, y que no se deben desplegar en la secci?n indicada.
    //Si es T indica que afecta a la cuota de inscripci?n.
    //Si es distinto, indica que afecta al costo del bloque
    //50 - Pr?rrogas
    //51 - Ense?anza Excenta
    
    
    //Constantes de descripciones de tipo de movimiento de C?lculo de Cobro
    final String ccfstrMatricula		 = "MATRICULA";
    final String ccfstrEnsenanza		 = "ENSENANZA";
    final String ccfstrInternado		 = "INTERNADO";
    final String ccfstrDesctoHObrero	 = "DESCUENTO DE HIJO DE OBRERO";
    final String ccfstrDesctoObrero		 = "DESCUENTO DE OBRERO ASD";
    final String ccfstrDesctoContado	 = "DESCUENTO DE CONTADO";
    final String ccfstrBecas		 	 = "BECAS";
    final String ccfstrManejoPagare		 = "MANEJO DE PAGARE";
    final String ccfstrComedor      	 = "COMEDOR";
    final String ccfstrCobroClima      	 = "COBRO CLIMA DORMITORIO";
    final String ccfstrSaldoAnterior	 = "SALDO ANTERIOR";
    final String ccfstrDesctoMExt		 = "DESCUENTO DE MATRICULA EXTEMPORANEA";
    final String ccfstrColportaje		 = "COLPORTAJE";
    final String ccfstrCuotaInscripcion	 = "CUOTA INSCRIPCION";
    final String ccfstrMExtemporanea	 = "MATRICULA EXTEMPORANEA";
    final String ccfstrConvenioTrabajo   = "CONVENIO TRABAJO";
    final String ccfstrBecaAdicional     = "BECA ADICIONAL";
    final String ccfstrDesctoInstitucional	 = "DESCUENTO DE BECA INSTITUCIONAL";
    final String ccfstrProrrogas		 = "PRORROGAS DE PAGO";
    final String ccfstrEnsenanzaExcenta	 = "CREDITOS EXCENTOS";    
    final String ccfstrDiezmoBecaInstitucional	 = "DONATIVO BECA INSTITUCIONAL";
    
    //Constantes de descripciones de tipo de movimiento de C?lculo de Cobro
    final String ccfstrMatriculaID			 = "01";
    final String ccfstrEnsenanzaID			 = "02";
    final String ccfstrInternadoID			 = "03";
    final String ccfstrDesctoHObreroID		 = "04";
    final String ccfstrDesctoObreroID		 = "05";
    final String ccfstrDesctoContadoID		 = "06";
    final String ccfstrBecasID			     = "07";
    final String ccfstrManejoPagareID		 = "08";
    final String ccfstrComedorID    		 = "09";
    final String ccfstrCobroClimaID    		 = "10";
    final String ccfstrSaldoAnteriorID		 = "20";
    final String ccfstrDesctoMExtID 		 = "21";
    final String ccfstrColportajeID          = "22";
    final String ccfstrCuotaInscripcionID	 = "23";
    final String ccfstrMExtemporaneaID       = "24";
    final String ccfstrConvenioTrabajoID     = "25";
    final String ccfstrBecaAdicionalID       = "26";
    final String ccfstrDesctoInstitucionalID = "27";
    final String ccfstrDiezmoBecaInstitucionalID = "28";
    final String ccfstrProrrogasID			 = "50";
    final String ccfstrEnsenanzaExcentaID	 = "51";    
    
    /*Tipo de alumno*/
    final Integer ccfintTANormal = new Integer (1);
    final Integer ccfintTHObrero = new Integer (2);
    final Integer ccfintTBecado = new Integer (3);
    final Integer ccfintTColportor = new Integer (4);
    final Integer ccfintTEmpleado = new Integer (5);
    final Integer ccfintTObrero = new Integer (6);
    final Integer ccfintTInstitucional = new Integer (8);
    final Integer ccfintTHijoEmpleado = new Integer (9);
    
    /*Tipos de descuentos*/
    final Integer ccfintDMExt = new Integer (999);
    
    /*Cuentas de mayor de los estudiantes*/
    final String ccfCtaEstudiantes = "1.1.04.01";
    final String ccfCtaPasivos = "1.1.04.29";
    final String ccfCtaIncobrables = "1.1.04.30";
    
    /*Porcentaje de convenio que se acredita a la cuota de inscripcion*/
    final Double ccfPctConvenio = new Double (0.35);
    final Double ccfPctBecaAdicional = new Double(0); //new Double(0.20);

    /*Diezmo de beca institucional*/
    /*Se toma como base la beca tecnica*/
    final Double ccfdblDiezmoBecaInstitucional = 7360 * 0.1;

    /*Constantes de estatus de pagares*/
    final String ccfpstrPagare = "A";
    final String ccfpstrConvenio = "C";
    final String ccfpstrProrroga = "P";
    final String ccfpstrBecaAdicional = "BA";
    final String ccfpstrBecaBasica = "BB";
    final String ccfpstrSaldoAnterior = "FSA"; //Folio de Saldo Anterior
    final String ccfpstrCuotaInscripcion = "FCI"; //Folio de Cuota de Inscripcion
}
