/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.general.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public class Constantes {


	public static final String ABIERTA = "ABIERTA";
	public static final String PENDIENTE = "PENDIENTE";
	public static final String CERRADA = "CERRADA";
	public static final String FACTURADA = "FACTURADA";
	public static final String CANCELADA = "CANCELADA";
	public static final String CREAR = "CREAR";
	public static final String ACTUALIZAR = "ACTUALIZAR";
	public static final String ELIMINAR = "ELIMINAR";
	public static final String CANCELAR = "CANCELAR";
	public static final String ADMIN = "ADMIN";
	public static final String ORG = "ORG";
	public static final String EMP = "EMP";
	public static final String ALM = "ALM";
	public static final String BAJA = "BAJA";
	public static final String IMAGEN = "IMAGEN";
	public static final String REUBICACION = "REUBICACION";
        public static final String DEVOLUCION = "DEVOLUCION";
        
        public static final String SYSTEM_CONSTANTS = "systemConstants";
	// Errores
	public static final String INVALIDSESSION = "Se detectaron datos invalidos.  Favor de salir del sistema y volver a ingresar su clave de usuario y password.";
	// Formato de fecha
	public static final String DATE_JAVA_FORMAT = "dd-MM-yyyy";
	public static final String DATE_USER_FORMAT = "(dd-mm-yyyy)";
	public static final String NOW = "now";
	// Constantes de la session
        
          /**
        * Variable en session donde se guarda la Union Actual de Usuario
        */
        public static final String SESSION_UNION = "union";
        /**
        * Variable en session donde se guarda la Asociacion Actual de Usuario
        */
        public static final String SESSION_ASOCIACION = "asociacion";
	public static final String SESSION_EJERCICIO = "id_ejercicio";
	public static final String SESSION_CCOSTO = "id_ccosto";
	public static final String SESSION_LIBRO = "id_libro";
	public static final String SESSION_LOGIN = "login";
	public static final String SESSION_USER_ID = "user_id";
	public static final String SESSION_TUSUARIO = "tusuario";
	public static final String SESSION_VALIDADOR = "validador";
	public static final String SESSION_FOLIO = "folio";
	// MathContext
	public static final MathContext mc = new MathContext(12,
			RoundingMode.HALF_EVEN);
	// Contabilidades
	/**
	 * Academico - 1.01
	 */
	public static final String CONTABILIDAD_ACA = "1.01";
	/**
	 * Clinica Dental - 1.02
	 */
	public static final String CONTABILIDAD_CD = "1.02";
	/**
	 * Covoprom - 2.01
	 */
	public static final String CONTABILIDAD_COV = "2.01";
	// Universidad por default
	public static final String UNIVERSIDAD_ALMA_MATER = "UNIVERSIDAD DE MONTEMORELOS, A.C.";
	//  Valores para Test Colportores
        public static final String CLAVE = "000";
        public static final String TELEFONO = "1234567890";
        public static final String MUNICIPIO = "testmu";
        public static final String CALLE = "test";
        public static final String COLONIA = "test"; 
        public static final String NOMBRE = "test";
        public static final String TIPO_DOCUMENTO = "A";
        public static final String FOLIO = "test";
        public static final BigDecimal IMPORTE = new BigDecimal("0.0");
        public static final String OBSERVACIONES = "test teste";
        // Valores de origen
	public static final String ORIGEN = "origen";
	public static final String ORIGEN_EMPLEADO = "empleado";
	public static final String ORIGEN_NOMINA = "nomina";
	public static final String ORIGEN_RH = "rh";
	public static final String ORIGEN_AFE = "afe";
	public static final String ORIGEN_DIR_CCOSTO = "dirCC";
	// Solo para test (valesGasolinaActionTest)
	public static final String ORIGEN_NOMINA_ADMIN = "nomina_admin";
	public static final String ORIGEN_CCP = "ccp";
	// Constantes para el coordinador
	public static final String ORIGEN_HOJA = "HOJA";
	public static final String ORIGEN_CONCENTRADO_ALUMNOS_AYUDA_ADICIONAL = "CONC";
	// IVA por default
	public static final BigDecimal IVA_16_PCT = new BigDecimal("16.00");
	// ACEGI SECURITY
	public static final String ACEGI_SECURITY_CONTEXT = "ACEGI_SECURITY_CONTEXT";
	// Constantes relacionadas con la contabilidad y cuentas
	/**
	 * Columna del debe. En naturaleza C, corresponde a los cargos En naturaleza
	 * D, corresponde a los creditos
	 */
	public static final String NATURALEZA_DEBE = "D";
	/**
	 * Columna del haber. En naturaleza D, corresponde a los cargos En
	 * naturaleza C, corresponde a los creditos
	 */
	public static final String NATURALEZA_HABER = "C";
	/**
	 * Valores para el constructor de Locale
	 */
	public static final String LOCALE_LANGUAGE = "es";
	public static final String LOCALE_COUNTRY = "MX";
	public static final String LOCALE_VARIANT = "Traditional_WIN";
	/**
	 * Formato (yyyy-MM-dd) de la fecha en el cual el mes se representa
	 * numericamente.
	 */
	public static final String DATE_SHORT_SYSTEM_PATTERN = "yyyy-MM-dd";
	/**
	 * Formato (dd/MM/yyyy) de la fecha en el cual el mes se representa
	 * numericamente
	 */
	public static final String DATE_SHORT_HUMAN_PATTERN = "dd/MM/yyyy";
	/**
	 * Formato (dd/MM/yyyy hh:mm) de la fecha en el cual el mes se representa
	 * numericamente incluyendo la hora:minutos:segundos am/pm
	 */
	public static final String DATE_SHORT_HHMM_HUMAN_PATTERN = "dd/MM/yyyy HH:mm";
	/**
	 * Se utiliza para informar al usuario el formato de fecha esperado
	 */
	public static final String DATE_SHORT_HUMAN_PATTERN_MSG = "dd/mm/yyyy";
	/**
	 * Formato (dd/MMM/yyyy) de la fecha en el cual el mes se representa en
	 * palabra
	 */
	public static final String DATE_LONG_HUMAN_PATTERN = "dd/MMM/yyyy";
	/**
	 * Formato (dd de MMMMM de yyyy) de la fecha en la cual el mes es completo
	 */
	public static final String DATE_XLONG_HUMAN_PATTERN = "dd 'de' MMMM 'de' yyyy";
	/**
	 * Formato USA
	 */
	public static final String DATE_SHORT_AMERICAN_PATTERN = "MM/dd/yyyy";
	/**
	 * Formato (HH:mm hrs.) de una fecha en la cual se muestra sola hora en
	 * formato de 24 horas Ej.(15:30 hrs)
	 */
	public static final String DATE_ONLY_24_HOUR_HUMAN_PATTERN = "HH:mm 'hrs.'";
	public static final String DATE_24_HOUR_HUMAN_PATTERN = "dd/MMM/yyyy HH:mm";
	public static final String STATUS_ACTIVO = "A";
	public static final String STATUS_INACTIVO = "I";
	public static final String STATUS_CANCELADO = "X";
	public static final String STATUS_CONFIRMADO = "C";
	public static final String STATUS_INSCRITO = "I";
	public static final String STATUS_ABIERTO = "Op";
	public static final String STATUS_CERRADO = "Cl";
	public static final String STATUS_RECHAZADO = "Rz";
	public static final String STATUS_ENVIADO = "OK";
	public static final String STATUS_AUTORIZADO = "At";
	public static final String STATUS_EMAIL_ENVIADO = "EE";
	public static final String STATUS_FINALIZADO = "Z";
	public static final String FILES_LIST = "filesList";
	/**
	 * Este status se utiliza para realizar <b>busquedas</b> donde el valor del
	 * status de los registros debe incluir 'Inscritos' y 'Confirmados' <br>
	 * <b>No se utiliza para asignar dicho valor a registro alguno</b>
	 */
	public static final String STATUS_CONFIRMADO_INSCRITO = "@";
	public static final String DECIMAL_PATTERN = "###,###,###,##0.00";
	public static final String CURRENCY_PATTERN = "$###,###,##0.00";
	public static final String PERCENTAGE_PATTERN = "#.00%";
	// Constantes de las impresiones por applets en caja y cheques
	public static final String FORMATO_CAJA_UM = "1.01";
	public static final String FORMATO_CAJA_COV = "2.01";
	public static final String FORMATO_CAJA_CD = "1.02";
	public static final String FORMATO_CAJA_HLC = "4.01";
	// Constantes de roles
	public static final String ROLE_NOMINA_PD = "NOMPD";
	public static final String ROLE_RH_ADMIN_NOMINA = "RHAdminNomina";
	public static final String ROLE_NOMINA_ADMIN = "NOMAdmin";
	public static final String ROLE_NOMINA_OPERSS = "NOMOperSS";
        public static final String ROL_ADMINISTRADOR = "ROLE_ADMIN";
        public static final String ROLE_TEST = "ROLE_TEST";
        public static final String ROLE_ASO = "ROLE_ASO";
        public static final String ROLE_COL = "ROLE_COL";
        // Constantes Open Id
        public static final String OPEN_ID = "openId";
        public static final String UNION_ID = "unionId";
        public static final String ASOCIACION_ID = "asociacionId";
	// Constantes globales
	public static final String GLOBAL_YES = "S";
	public static final String GLOBAL_NO = "N";
	/**
	 * Constantes de Calculo de Cobro
	 */
	public static final String CCOBRO = "ccobro";
	public static final String CCOBRO_INSCRITO = "S";
	public static final String CCOBRO_FORMA_PAGO_CONTADO = "C";
	public static final String CCOBRO_FORMA_PAGO_PAGARE = "P";
	public static final String CCOBRO_ONLINE_OBJ = "ccobroOnLine";
	public static final String CCOBRO_ONLINE_ID = "ccobroOnLineId";
	public static final String CCOBRO_ONLINE_SESSION = "sessionOnLine";
	public static final String CCOBRO_ONLINE_RANDOM = "randomOnLine";
	public static final String CCOBRO_ONLINE_CUOTAINSCRIPCION = "cuotaInscripcion";
	public static final String CCOBRO_ONLINE_CLERK = "inscripcionOnLine";
	public static final String CCOBRO_ONLINE_MSG = "ccobroOnLineMessage";
	public static final String CCOBRO_ONLINE_MATRICULA = "ccobroOnLineMatricula";
	public static final String CCOBRO_ONLINE_NOMBRE = "ccobroOnLineNombre";
	public static final String CCOBRO_ONLINE_RFC = "ccobroOnLineRFC";
	public static final String CCOBRO_ONLINE_DIRECCION = "ccobroOnLineDireccion";
	public static final String CCOBRO_ONLINE_FECHA_PAGO = "ccobroOnLineFechaPago";
	public static final String CCOBRO_ONLINE_FORMA_PAGO = "ccobroOnLineFormaPago";
	public static final String CCOBRO_ONLINE_CONCEPTO = "ccobroOnLineConcepto";
	public static final String CCOBRO_ONLINE_IMPORTE = "ccobroOnLineImporte";
	public static final String CCOBRO_ONLINE_PAGO_TOTAL = "ccobroOnLinePagoTotal";
	public static final String CCOBRO_ONLINE_SUBTOTAL = "ccobroOnLineSubTotal";
	public static final String CCOBRO_ONLINE_FOLIO_PAGO = "ccobroOnLineFolioPagol";
	public static final String CCOBRO_ONLINE_IMPORTE_LETRAS = "ccobroOnLineImporteLetras";
	public static final String CCOBRO_LIST = "ccobroList";
	public static final String COLPORTOR = "colportor";
	public static final String INVALID = "No Valido";
	/**
	 * Precio por hora que se considera para calcular el dizmo de los alumnos
	 * con beca institucional
	 */
	public static final BigDecimal PRECIOHORA_DIEZMO_BECA_INSTITUCIONAL = new BigDecimal(
			"22.00");
	/**
	 * Constante del cache de hibernate
	 * 
	 */
	public static Integer HIBERNATE_JDBC_BATCH_SIZE = 50;
	// Constantes varias del sistema
	/**
	 * The name of the ResourceBundle used in this application
	 */
	public static final String BUNDLE_KEY = "ApplicationResources";
	/**
	 * The encryption algorithm key to be used for passwords
	 */
	public static final String ENC_ALGORITHM = "algorithm";
	/**
	 * A flag to indicate if passwords should be encrypted
	 */
	public static final String ENCRYPT_PASSWORD = "encryptPassword";
	/**
	 * File separator from System properties
	 */
	public static final String FILE_SEP = System.getProperty("file.separator");
	/**
	 * User home from System properties
	 */
	public static final String USER_HOME = System.getProperty("user.home")
			+ FILE_SEP;
	/**
	 * The name of the configuration hashmap stored in application scope.
	 */
	public static final String CONFIG = "appConfig";
	/**
	 * Session scope attribute that holds the locale set by the user. By setting
	 * this key to the same one that Struts uses, we get synchronization in
	 * Struts w/o having to do extra work or have two session-level variables.
	 */
	public static final String PREFERRED_LOCALE_KEY = "org.apache.struts.action.LOCALE";
	/**
	 * The request scope attribute under which an editable user form is stored
	 */
	public static final String USER_KEY = "userForm";
	/**
	 * The request scope attribute that holds the user list
	 */
	public static final String USER_LIST = "userList";
	/**
	 * The request scope attribute for indicating a newly-registered user
	 */
	public static final String REGISTERED = "registered";
	/**
	 * The name of the Administrator role, as specified in web.xml
	 */
	public static final String ADMIN_ROLE = "admin";
	/**
	 * The name of the User role, as specified in web.xml
	 */
	public static final String USER_ROLE = "user";
	/**
	 * The name of the user's role list, a request-scoped attribute when
	 * adding/editing a user.
	 */
	public static final String USER_ROLES = "userRoles";
	/**
	 * The name of the available roles list, a request-scoped attribute when
	 * adding/editing a user.
	 */
	public static final String AVAILABLE_ROLES = "availableRoles";
	/**
	 * The name of the CSS Theme setting.
	 */
	public static final String CSS_THEME = "csstheme";
	// Comunicacion-START
	/**
	 * The request scope attribute that holds the comunicacion form.
	 */
	public static final String COMUNICACION_KEY = "comunicacionForm";
	/**
	 * The request scope attribute that holds the comunicacion list
	 */
	public static final String COMUNICACION_LIST = "comunicacionList";
	// Comunicacion-END
	// EmpleadoPuesto-START
	/**
	 * The request scope attribute that holds the comunicacion form.
	 */
	public static final String EMPLEADOPUESTO_KEY = "empleadoPuestoForm";
	/**
	 * The request scope attribute that holds the comunicacion list
	 */
	public static final String EMPLEADOPUESTO_LIST = "empleadoPuestoList";
	/**
	 * The request scope attribute that holds the empleadoPuesto value that has
	 * been edited
	 */
	public static final String EMPLEADOPUESTO_EDIT = "empleaoPuestoEdit";
	// EmpleadoPuesto-END
	// EmpleadoVacaciones-START
	public static final String EMPLEADOVACACIONES_KEY = "empleadoVacacionesForm";
	public static final String EMPLEADOVACACIONES_LIST = "empleadoVacacionesList";
	public static final String EMPLEADOVACACIONES_STATUS_ALTA_BY_USER = "U";
	public static final String EMPLEADOVACACIONES_STATUS_ALTA_BY_PROCESS = "P";
	// EmpleadoVacaciones-END
	// Grupo-START
	/**
	 * The request scope attribute that holds the grupo form.
	 */
	public static final String GRUPO_KEY = "grupoForm";
	/**
	 * The request scope attribute that holds the grupo list
	 */
	public static final String GRUPO_LIST = "grupoList";
	public static final String GRUPO_COMODIN = "X";
	// Grupo-END
	// Seccion-START
	/**
	 * The request scope attribute that holds the seccionCategoria form.
	 */
	public static final String SECCION_KEY = "seccionForm";
	/**
	 * The request scope attribute that holds the seccionCategoria list
	 */
	public static final String SECCION_LIST = "seccionList";
	// Seccion-END
	// TipoDependiente-START
	/**
	 * The request scope attribute that holds the tipoDependiente form.
	 */
	public static final String TIPODEPENDIENTE_KEY = "tipoDependiente";
	/**
	 * The request scope attribute that holds the tipoDependiente list
	 */
	public static final String TIPODEPENDIENTE_LIST = "tipoDependienteList";
	/**
	 * The request scope attribute that holds the alumnoInscripciones list
	 */
	public static final String ALUMNO_LIST = "alumnList";
	// TipoDependiente-END
	// Dependiente-START
	/**
	 * The request scope attribute that holds the dependiente form.
	 */
	public static final String EMPLEADODEPENDIENTE_KEY = "empleadoDependienteForm";
	/**
	 * The request scope attribute that holds the dependiente list
	 */
	public static final String EMPLEADODEPENDIENTE_LIST = "empleadoDependienteList";
	// Dependiente-END
	// EmpleadoComunicacion-START
	public static final String EMPLEADOCOMUNICACION_KEY = "empleadoComunicacionForm";
	public static final String EMPLEADOCOMUNICACION_LIST = "empleadoComunicacionList";
	// EmpleadoComunicacion-END
	// Detalle-START
	/**
	 * The request scope attribute that holds the detalle form.
	 */
	public static final String DETALLE_KEY = "detalleForm";
	/**
	 * The request scope attribute that holds the detalle list
	 */
	public static final String DETALLE_LIST = "detalleList";
	// Detalle-END
	// Docencia-START
	/**
	 * The request scope attribute that holds the docencia form.
	 */
	public static final String DOCENCIA_KEY = "docenciaForm";
	/**
	 * The request scope attribute that holds the docencia list
	 */
	public static final String DOCENCIA_LIST = "docenciaList";
	// Docencia-END
	// Universidad-START
	/**
	 * The request scope attribute that holds the universidad form.
	 */
	public static final String UNIVERSIDAD_KEY = "universidadForm";
	/**
	 * The request scope attribute that holds the universidad list
	 */
	public static final String UNIVERSIDAD_LIST = "universidadList";
	// Universidad-END
	// Ejercicio-START
	/**
	 * The request scope attribute that holds the ejercicio form.
	 */
	public static final String EJERCICIO_KEY = "ejercicioForm";
	/**
	 * The request scope attribute that holds the ejercicio list
	 */
	public static final String EJERCICIO_LIST = "ejercicioList";
	/**
	 * Status del ejercicio activo
	 */
	public static final String EJERCICIO_ACTIVO = "A";
	/**
	 * Status del ejercicio cerrado
	 */
	public static final String EJERCICIO_CERRADO = "I";
	// Ejercicio-END
	// Libro-START
	/**
	 * The request scope attribute that holds the libro form.
	 */
	public static final String LIBRO_KEY = "libroForm";
	/**
	 * The request scope attribute that holds the libro list
	 */
	public static final String LIBRO_LIST = "libroList";
	public static final String LIBRO_CAJA = "20";
	// Libro-END
	// CentroCosto-START
	/**
	 * The request scope attribute that holds the cCosto form.
	 */
	public static final String CCOSTO_KEY = "centroCostoForm";
	/**
	 * The request scope attribute that holds the cCosto list
	 */
	public static final String CCOSTO_LIST = "cCostoList";
	public static final String CCOSTO_DETALLE = "S";
	public static final String CCOSTO = "ccosto";
	public static final String CCOSTO_DIEZMO_OTROSGASTOS_OTROSPRODUCTOS_ACA = "1.01.4.07.02";
	public static final String CCOSTO_DIEZMO_OTROSGASTOS_OTROSPRODUCTOS_COV = "2.01.1.01.02";
	public static final String CCOSTO_RECHUMANOS = "1.01.4.01.05";
	// CentroCosto-END
	// CtaMayor-START
	/**
	 * The request scope attribute that holds the ctaMayor form.
	 */
	public static final String CTAMAYOR_KEY = "ctaMayorForm";
	/**
	 * The request scope attribute that holds the ctaMayor list
	 */
	public static final String CTAMAYOR_LIST = "ctaMayorList";
	/**
	 * Tipo de cuenta Balance
	 */
	public static final String CTAMAYOR_BALANCE = "B";
	/**
	 * Tipo de cuenta Resultados
	 */
	public static final String CTAMAYOR_RESULTADOS = "R";
	public static final String CTAMAYOR_NOMINA = "2.1.01.08";
	public static final String CTAMAYOR_NOMINA_JUBILADOS = "2.1.01.25";
	public static final String CTAMAYOR_PRESUPUESTO_BECAS = "1.3.12";
	public static final String CTAMAYOR_ESTUDIANTES_ACTIVOS = "1.1.04.01";
	public static final String CTAMAYOR_ESTUDIANTES_PASIVOS = "1.1.04.29";
	public static final String CTAMAYOR_ESTUDIANTES_INCOBRABLES = "1.1.04.30";
	public static final String CTAMAYOR_TALLERES_ESTUDIANTILES = "1.3.15";
	public static final String CTAMAYOR_DIEZMO_ASOCNORESTE = "2.1.01.10";
	public static final String CTAMAYOR_DIEZMO_OTROSGASTOS = "1.1.14";
	public static final String CTAMAYOR_BECA_INSTITUCIONAL = "1.3.13";
	public static final String CTAMAYOR_BECA_PROMOCIONAL = "1.3.16";
	public static final String CTAMAYOR_ACADEMICO_COVOPROM = "1.1.05.07";
	public static final String CTAMAYOR_COVOPROM_ACADEMICO = "2.1.01.09";
	public static final String CTAMAYOR_DESPENSA = "2.1.02";
	public static final String CTAMAYOR_PROVEEDORES = "2.1.01.04";
	public static final String CTAMAYOR_ACREEDORES_DIVERSOS = "2.1.01.05";
	public static final String CTAMAYOR_BANORTE_1511 = "1.1.02.01";
	public static final String CTAMAYOR_BANCO_JUBILADOS = "1.1.02.15";
	public static final String CTAMAYOR_CC_ENERGIA_ELECTRICA = "2.2.03";
	public static final String CTAMAYOR_CC_TELEFONIA = "2.5.02";
	public static final String CTAMAYOR_CC_IVA_DEDUCIBLE = "2.5.36";
	public static final String CTAMAYOR_CC_IVA_DEDUCIBLE_EE = "2.2.08";
	public static final String CTAMAYOR_CAJA_GENERAL = "1.1.01.01";
	/**
	 * El primer numero de la cuenta de mayor indica la naturaleza de la misma
	 */
	public static final String CTAMAYOR_DEUDORA = "1";
	/**
	 * Indica que la cuenta de mayor tiene auxiliares
	 */
	public static final String CTAMAYOR_AUXILIAR_SI = "S";
	// CtaMayor-END
	// Auxiliar-START
	/**
	 * The request scope attribute that holds the auxiliar form.
	 */
	public static final String AUXILIAR_KEY = "auxiliarForm";
	/**
	 * The request scope attribute that holds the auxiliar list
	 */
	public static final String AUXILIAR_LIST = "auxiliarList";
	/**
	 * Clave de auxiliar default Se utiliza cuando el usuario no proporciona una
	 * clave de auxiliar
	 */
	public static final String AUXILIAR_DEFAULT = "0000000";
	public static final String AUXILIAR_ACCORD = "8112107";
	public static final String AUXILIAR_COMPANIA_LUZ = "8000003";
	public static final String AUXILIAR_TELMEX = "8080001";
	/**
	 * Valor del atributo detalle
	 */
	public static final String AUXILIAR_DETALLE = "S";
	// Auxiliar-END
	// Cuenta-START
	/**
	 * The request scope attribute that holds the cuenta form.
	 */
	public static final String CUENTA_KEY = "cuentaForm";
	/**
	 * The request scope attribute that holds the cuenta list
	 */
	public static final String CUENTA_LIST = "cuentaList";
	/**
	 * Cuenta activa
	 */
	public static final String CUENTA_ACTIVA = "A";
	/**
	 * Cuenta inactiva No se pueden crear, modificar o borrar movimientos en
	 * esta cuenta Solo se puede consultar
	 */
	public static final String CUENTA_INACTIVA = "I";
	// Cuenta-END
	// Poliza-START
	/**
	 * The request scope attribute that holds the poliza form.
	 */
	public static final String POLIZA_KEY = "polizaForm";
	/**
	 * The request scope attribute that holds the poliza list
	 */
	public static final String POLIZA_LIST = "polizaList";
	/**
	 * Poliza activa
	 */
	public static final String POLIZA_ACTIVA = "A";
	/**
	 * Poliza cerrada. Si esta cerrada debe estar cuadrada.
	 */
	public static final String POLIZA_CERRADA = "D";
	/**
	 * Se inserta cada movimiento que se recibe tal cual
	 */
	public static final String POLIZA_COMPORTAMIENTO_NORMAL = "Normal";
	/**
	 * Acumula los importes de los movimientos con cuenta y naturaleza iguales
	 */
	public static final String POLIZA_COMPORTAMIENTO_BY_CUENTA = "Cuenta";
	/**
	 * Acumula los importes de los movimientos con cuenta, naturaleza y
	 * descripcion iguales
	 */
	public static final String POLIZA_COMPORTAMIENTO_BY_DESCRIPCION = "Descripcion";
	/**
	 * Acumula los importes de los movimientos con cuenta, naturaleza y
	 * descripcion, tomando en cuenta la referencia
	 */
	public static final String POLIZA_COMPORTAMIENTO_BY_CAJA = "Caja";
	// Poliza-END
	// Movimiento-START
	/**
	 * The request scope attribute that holds the movimiento form.
	 */
	public static final String MOVIMIENTO_KEY = "movimientoForm";
	/**
	 * The request scope attribute that holds the movimiento list
	 */
	public static final String MOVIMIENTO_LIST = "movimientoList";
	/**
	 * Descripcion de movimiento de saldo anterior
	 */
	public static final String SALDOANTERIOR = "Saldo Anterior";
	/**
	 * Movimiento activo
	 */
	public static final String MOVIMIENTO_ACTIVO = "A";
	/**
	 * Movimiento que cancela caja, y queda en espera que auditoria lo cancele
	 * definitivamente
	 */
	public static final String MOVIMIENTO_CANCELADO_POR_CAJA = "T";
	/**
	 * Movimiento cancelado definitivamente
	 */
	public static final String MOVIMIENTO_CANCELADO = "I";
	// Movimiento-END
	// Reportes Financieros - Start
	public static final String FONDOS_KEY = "fondosForm";
	/**
	 * Lista que contiene el estado de cuenta de fondos
	 */
	public static final String FONDOSEDOCTA_LIST = "fondosEdoCtaList";
	/**
	 * Map que contiene las listas de saldo incial, entradas y salidas de fondos
	 */
	public static final String FONDOSEDOCTA_MAP = "fondosEdoCtaMap";
	/**
	 * Lista que contiene el estado de cuenta de saldos de operaciones
	 */
	public static final String FONDOSEDOCTA_LIST_SALDOSOPERACIONES = "fondosEdoCtaSaldosOperaciones";
	/**
	 * Lista de saldo inicial
	 */
	public static final String FONDOSEDOCTA_LIST_SALDOINICIAL = "fondosEdoCtaListSaldoInicial";
	/**
	 * Lista de entradas
	 */
	public static final String FONDOSEDOCTA_LIST_ENTRADAS25 = "fondosEdoCtaListEntradas25";
	/**
	 * Lista de entradas
	 */
	public static final String FONDOSEDOCTA_LIST_ENTRADAS26 = "fondosEdoCtaListEntradas26";
	/**
	 * Lista de entradas
	 */
	public static final String FONDOSEDOCTA_LIST_ENTRADAS27 = "fondosEdoCtaListEntradas27";
	/**
	 * Lista de salidas
	 */
	public static final String FONDOSEDOCTA_LIST_SALIDAS05 = "fondosEdoCtaListSalidas05";
	/**
	 * Lista de salidas
	 */
	public static final String FONDOSEDOCTA_LIST_SALIDAS06 = "fondosEdoCtaListSalidas06";
	/**
	 * Lista de salidas
	 */
	public static final String FONDOSEDOCTA_LIST_SALIDAS07 = "fondosEdoCtaListSalidas07";
	/**
	 * Lista de salidas
	 */
	public static final String FONDOSEDOCTA_LIST_SALIDAS08 = "fondosEdoCtaListSalidas08";
	/**
	 * Lista de salidas
	 */
	public static final String FONDOSEDOCTA_LIST_SALIDAS25 = "fondosEdoCtaListSalidas25";
	/**
	 * Lista de salidas
	 */
	public static final String FONDOSEDOCTA_LIST_SALIDAS26 = "fondosEdoCtaListSalidas26";
	/**
	 * Cuenta de fondos que contiene los saldos iniciales
	 */
	public static final String FONDOS_SALDOINICIAL = "3.1.02.01";
	public static final String FONDOS_SALDOINICIAL_TIPO = "B";
	public static final String FONDOS_SALDOINICIAL_NOMBRE = "SALDO INICIAL";
	/**
	 * Cuenta de fondos que contiene las entradas
	 */
	public static final String FONDOS_ENTRADAS25 = "1.1.25";
	public static final String FONDOS_ENTRADAS25_TIPO = "R";
	public static final String FONDOS_ENTRADAS25_NOMBRE = "ENTRADAS";
	/**
	 * Cuenta de fondos que contiene las entradas
	 */
	public static final String FONDOS_ENTRADAS26 = "1.1.26";
	public static final String FONDOS_ENTRADAS26_TIPO = "R";
	public static final String FONDOS_ENTRADAS26_NOMBRE = "ENTRADAS";
	/**
	 * Cuenta de fondos que contiene las entradas
	 */
	public static final String FONDOS_ENTRADAS27 = "1.1.27";
	public static final String FONDOS_ENTRADAS27_TIPO = "R";
	public static final String FONDOS_ENTRADAS27_NOMBRE = "ENTRADAS";
	/**
	 * Cuenta de fondos que contiene las salidas
	 */
	public static final String FONDOS_SALIDAS05 = "3.1.05";
	public static final String FONDOS_SALIDAS05_TIPO = "R";
	public static final String FONDOS_SALIDAS05_NOMBRE = "SALIDAS";
	/**
	 * Cuenta de fondos que contiene las salidas
	 */
	public static final String FONDOS_SALIDAS06 = "3.1.06";
	public static final String FONDOS_SALIDAS06_TIPO = "R";
	public static final String FONDOS_SALIDAS06_NOMBRE = "SALIDAS";
	/**
	 * Cuenta de fondos que contiene las salidas
	 */
	public static final String FONDOS_SALIDAS07 = "3.1.07";
	public static final String FONDOS_SALIDAS07_TIPO = "R";
	public static final String FONDOS_SALIDAS07_NOMBRE = "SALIDAS";
	/**
	 * Cuenta de fondos que contiene las salidas
	 */
	public static final String FONDOS_SALIDAS08 = "3.1.08";
	public static final String FONDOS_SALIDAS08_TIPO = "R";
	public static final String FONDOS_SALIDAS08_NOMBRE = "SALIDAS";
	/**
	 * Cuenta de fondos que contiene las salidas
	 */
	public static final String FONDOS_SALIDAS25 = "3.1.25";
	public static final String FONDOS_SALIDAS25_TIPO = "R";
	public static final String FONDOS_SALIDAS25_NOMBRE = "SALIDAS";
	/**
	 * Cuenta de fondos que contiene las salidas
	 */
	public static final String FONDOS_SALIDAS26 = "3.1.26";
	public static final String FONDOS_SALIDAS26_TIPO = "R";
	public static final String FONDOS_SALIDAS26_NOMBRE = "SALIDAS";
	public static final String FONDO_ESTADO_CUENTA = "FN";
	public static final String FONDO_ESTADO_CUENTA_DETALLADO = "FD";
	public static final String FONDO_ESTADO_CUENTA_SALDOS_OPERACIONES = "SO";
	/**
	 * Reporte de Antiguedad de Saldos
	 */
	public static final String ANTIGUEDAD_SALDOS = "aSaldos";
	public static final String EDO_CUENTA_ANTIGUEDAD_SALDOS = "antiguedadSaldosReport";
	/**
	 * Reporte de Flujo de Efectivo
	 */
	public static final String FLUJO_EFECTIVO = "fEfectivo";
	public static final String RPT_FLUJO_EFECTIVO_ENTRADA = "flujoEfectivoEntradaReport";
	public static final String RPT_FLUJO_EFECTIVO_SALIDA = "flujoEfectivoSalidaReport";
	/**
	 * Reporte de Formas de Entrada a Caja
	 */
	public static final String FORMAS_ENTRADA_CAJA = "fEntradas";
	public static final String RPT_FORMAS_ENTRADA_CAJA = "formasEntradasCajaList";
	// Reportes Financieros - END
	// Concepto-START
	/**
	 * The request scope attribute that holds the concepto form.
	 */
	public static final String CONCEPTO_FORM = "conceptoForm";
	/**
	 * Id del concepto de vacaciones
	 */
	public static final Integer CONCEPTO_VACACIONES_ID = new Integer(18);
	/**
	 * Id del concepto de aguinaldo
	 */
	public static final Integer CONCEPTO_AGUINALDO_ID = new Integer(21);
	/**
	 * Id del concepto de diezmo
	 */
	public static final Integer CONCEPTO_DIEZMO_ID = new Integer(23);
	/**
	 * Id del concepto de ispt
	 */
	public static final Integer CONCEPTO_ISPT_ID = new Integer(24);
	/**
	 * Id del concepto de salario
	 */
	public static final Integer CONCEPTO_SALARIO_ID = new Integer(14);
	/**
	 * Id del concepto de diferencia del importe en dolares de los cheques
	 */
	public static final Integer CONCEPTO_DIF_DIVISAS_ID = new Integer(25);
	/**
	 * Id del concepto de los cheques
	 */
	public static final Integer CONCEPTO_CHEQUE_ID = new Integer(26);
	/**
	 * Id del concepto de caja
	 */
	public static final Integer CONCEPTO_CAJA_ID = new Integer(27);
	/**
	 * Id del concepto de iva de ingresos en caja
	 */
	public static final Integer CONCEPTO_IVA_INGRESOS_ID = new Integer(28);
	/**
	 * Id del concepto de creditos excentos
	 */
	public static final Integer CONCEPTO_CREDITOS_EXCENTOS_ID = new Integer(29);
	/**
	 * Id del concepto de creditos excentos
	 */
	public static final Integer CONCEPTO_MATRICULA_ID = new Integer(30);
	/**
	 * Id del concepto de creditos excentos
	 */
	public static final Integer CONCEPTO_ENSENANZA_ID = new Integer(31);
	/**
	 * Id del concepto de creditos excentos
	 */
	public static final Integer CONCEPTO_INTERNADO_ID = new Integer(32);
	/**
	 * Id del concepto de descuento pago contado
	 */
	public static final Integer CONCEPTO_DCTO_PAGO_CONTADO_ID = new Integer(33);
	/**
	 * Id del concepto de beca
	 */
	public static final Integer CONCEPTO_BECA_ID = new Integer(34);
	/**
    public static final String ABIERTA = "ABIERTA";
    public static final String PENDIENTE = "PENDIENTE";
    public static final String CERRADA = "CERRADA";
    public static final String FACTURADA = "FACTURADA";
    public static final String CANCELADA = "CANCELADA";
    public static final String CREAR = "CREAR";
    public static final String ACTUALIZAR = "ACTUALIZAR";
    public static final String ELIMINAR = "ELIMINAR";
    public static final String CANCELAR = "CANCELAR";
    public static final String ADMIN = "ADMIN";
    public static final String ORG = "ORG";
    public static final String EMP = "EMP";
    public static final String ALM = "ALM";
    public static final String BAJA = "BAJA";
    public static final String IMAGEN = "IMAGEN";
    public static final String REUBICACION = "REUBICACION";
    public static final String DEVOLUCION = "DEVOLUCION";
    public static final String SYSTEM_CONSTANTS = "systemConstants";
    // Errores
    public static final String INVALIDSESSION = "Se detectaron datos invalidos.  Favor de salir del sistema y volver a ingresar su clave de usuario y password.";
    // Formato de fecha
    public static final String DATE_JAVA_FORMAT = "dd-MM-yyyy";
    public static final String DATE_USER_FORMAT = "(dd-mm-yyyy)";
    public static final StriSng NOW = "now";
    S
    /**
     *
     */
    public static final Integer CONCEPTO_BECA_EDUCACIONAL_ID = new Integer(10);
    /**
     * Id del concepto de matricula extemporanea
     */
    public static final Integer CONCEPTO_MATRICULA_EXTEMPORANEA_ID = new Integer(
            35);
    /**
     * Id del concepto de descuento matricula extemporanea
     */
    public static final Integer CONCEPTO_DCTO_MEXT_ID = new Integer(36);
    /**
     * Id del concepto de manejo pagare
     */
    public static final Integer CONCEPTO_MANEJO_PAGARE_ID = new Integer(37);
    /**
     * Id del concepto de desuento obrero asd
     */
    public static final Integer CONCEPTO_DCTO_OBRERO_ASD_ID = new Integer(38);
    /**
     * Id del concepto de saldo inicial
     */
    public static final Integer CONCEPTO_SALDO_INICIAL_ID = new Integer(39);
    /**
     * Id del concepto de otro, se utiliza en casos donde no se ha definido el
     * concepto correcto
     */
    public static final Integer CONCEPTO_OTRO_ID = new Integer(20);
    public static final String CONCEPTO_TAGS_GLOBAL = "global";
    public static final String CONCEPTO_TAGS_CAJA = "caja";
    /**
     * The request scope attribute that holds the concepto list
     */
    public static final String CONCEPTO_LIST = "conceptoList";
    // Concepto-END
    // Plantilla-START
    /**
     * The request scope attribute that holds the plantilla form.
     */
    public static final String PLANTILLA_KEY = "plantillaForm";
    /**
     * The request scope attribute that holds the plantilla list
     */
    public static final String PLANTILLA_LIST = "plantillaList";
    public static final String PLANTILLA_ID = "plantillaId";
    public static final String PLANTILLA_ORIGEN = "plantilla";
    // Plantilla-END
    // Elemento-START
    /**
     * The request scope attribute that holds the elemento form.
     */
    public static final String ELEMENTO_KEY = "elementoForm";
    public static final String ELEMENTO_TMP = "elementoClass";
    /**
     * The request scope attribute that holds the elemento list
     */
    public static final String ELEMENTO_LIST = "elementoList";
    public static final String ELEMENTO_MAP = "elementoMap";
    public static final String ELEMENTOSPADRE_LIST = "elementosPadreList";
    public static final String ELEMENTO_ORIGEN = "elemento";
    // Elemento-END
    // Grupo-RH-START
    /**
     * The request scope attribute that holds the grupo form.
     */
    public static final String GRUPO_RH_KEY = "grupoRhForm";
    /**
     * The request scope attribute that holds the grupo list
     */
    public static final String GRUPO_RH_LIST = "grupoRhList";
    // Grupo-RH-END
    // TipoEmpleado-START
    /**
     * The request scope attribute that holds the tipoEmpleado form.
     */
    public static final String TIPOEMPLEADO_KEY = "tipoEmpleadoForm";
    /**
     * The request scope attribute that holds the tipoEmpleado list
     */
    public static final String TIPOEMPLEADO_LIST = "tipoEmpleadoList";
    // TipoEmpleado-END
    // Empleado-START
    /**
     * The request scope attribute that holds the empleado form.
     */
    public static final String EMPLEADO_KEY = "empleado";
    /**
     * The request scope attribute that holds the empleado list
     */
    public static final String EMPLEADO_LIST = "empleadoList";
    /**
     * Clave del empleado
     */
    public static final String EMPLEADO_CLAVE = "empleadoClave";
    /**
     * Guarda la clave del empleado cuando este ingresa a la pagina de consulta
     * de estado de cuenta de SS
     */
    public static final String EMPLEADO_CONSULTA = "empleadoConsulta";
    /**
     * Objecto Empleado
     */
    public static final String EMPLEADO = "empleado";
    // Empleado-END
    // EmpleadoLaborales-START
    /**
     * The request scope attribute that holds the empleadoLaborales form.
     */
    public static final String EMPLEADOLABORALES_KEY = "empleadoLaboralesForm";
    /**
     * The request scope attribute that holds the empleadoLaborales list
     */
    public static final String EMPLEADOLABORALES_LIST = "empleadoLaboralesList";
    // EmpleadoLaborales-END
    // EmpleadoPersonales-START
    /**
     * The request scope attribute that holds the empleadoPersonales form.
     */
    public static final String EMPLEADOPERSONALES_KEY = "empleadoPersonalesForm";
    /**
     * The request scope attribute that holds the empleadoPersonales list
     */
    public static final String EMPLEADOPERSONALES_LIST = "empleadoPersonalesList";
    // EmpleadoPersonales-END
    // Sobresueldo-START
    /**
     * The request scope attribute that holds the sobresueldo form.
     */
    public static final String SOBRESUELDO_KEY = "sobresueldoForm";
    /**
     * The request scope attribute that holds the sobresueldo list
     */
    public static final String SOBRESUELDO_LIST = "sobresueldoList";
    public static final String SOBRESUELDO_OBJ = "sobresueldo";
    /**
     * Sobresueldo Balanza Report
     */
    public static final String SOBRESUELDO_BALANZA_LIST = "sobresueldoBalanzaList";
    /**
     * Sobresueldo Conceptos Report
     */
    public static final String SOBRESUELDO_CONCEPTOS_MAP = "sobresueldoConceptosMap";
    public static final String SOBRESUELDO_CONCEPTOS_LIST = "sobresueldoConceptosList";
    /**
     * Reporte por conceptos
     */
    public static final Integer SOBRESUELDO_TOTAL_CARGOS = new Integer(97);
    public static final String SOBRESUELDO_TOTAL_CARGOS_D = "TOTAL CARGOS";
    public static final Integer SOBRESUELDO_TOTAL_CREDITOS = new Integer(98);
    public static final String SOBRESUELDO_TOTAL_CREDITOS_D = "TOTAL CREDITOS";
    public static final Integer SOBRESUELDO_SALDO_TOTAL = new Integer(99);
    public static final String SOBRESUELDO_SALDO_TOTAL_D = "SALDO TOTAL";
    /**
     * Status activo
     */
    public static final String SOBRESUELDO_STATUS_ACTIVO = "A";
    /**
     * Valor que se envia como parametro en el request para indicar que la
     * cancelacion sera automatica
     */
    public static final String SOBRESUELDO_PARAM_CANCELACION_AUTOMATICA = "CAuto";
    public static final String SOBRESUELDO_CANCELACION_AUTOMATICA = "T";
    public static final String SOBRESUELDO_PARAM_CANCELACION_AUTOMATICA_COPIA = "CAutoC";
    public static final String SOBRESUELDO_CANCELACION_AUTOMATICA_COPIA = "C";
    public static final String SOBRESUELDO_GENERA_CHEQUE = "S";
    public static final String SOBRESUELDO_NO_GENERA_CHEQUE = "N";
    public static final String SOBRESUELDO_OBSERVACIONES_TRASPASO_AUTOMATICO = "TRASPASO AUTOMATICO";
    // Sobresueldo-END
    // Pais-START
    /**
     * The request scope attribute that holds the pais form.
     */
    public static final String PAIS_KEY = "paisForm";
    /**
     * The request scope attribute that holds the pais list
     */
    public static final String PAIS_LIST = "paisList";
    // Pais-END
    // SolicitudSeguro-START
    /**
     * The request scope attribute that holds the solicitudSeguro form.
     */
    public static final String SOLICITUDSEGURO_KEY = "solicitudSeguroForm";
    /**
     * The request scope attribute that holds the solicitudSeguro list
     */
    public static final String SOLICITUDSEGURO_LIST = "solicitudSeguroList";
    // SolicitudSeguro-END
    // Estado-START
    /**
     * The request scope attribute that holds the estado form.
     */
    public static final String ESTADO_KEY = "estadoForm";
    /**
     * The request scope attribute that holds the estado list
     */
    public static final String ESTADO_LIST = "estadoList";
    // Estado-END
    // TipoTercero-START
    /**
     * The request scope attribute that holds the tipoTercero form.
     */
    public static final String TIPOTERCERO_KEY = "tipoTerceroForm";
    /**
     * The request scope attribute that holds the tipoTercero list
     */
    public static final String TIPOTERCERO_LIST = "tipoTerceroList";
    // TipoTercero-END
    // TipoOperacion-START
    /**
     * The request scope attribute that holds the tipoOperacion form.
     */
    public static final String TIPOOPERACION_KEY = "tipoOperacionForm";
    /**
     * The request scope attribute that holds the tipoOperacion list
     */
    public static final String TIPOOPERACION_LIST = "tipoOperacionList";
    public static final String TIPOOPERACION_SERVICIOS_PROFESIONALES = "03";
    public static final String TIPOOPERACION_ARR_INMUEBLES = "06";
    public static final String TIPOOPERACION_OTROS = "85";
    // TipoOperacion-ENDa
    // CodigoPostal-START
    /**
     * The request scope attribute that holds the codigoPostal form.
     */
    public static final String CODIGOPOSTAL_KEY = "codigoPostalForm";
    /**
     * The request scope attribute that holds the codigoPostal list
     */
    public static final String CODIGOPOSTAL_LIST = "codigoPostalList";
    // CodigoPostal-END
    // Colonia-START
    /**
     * The request scope attribute that holds the colonia form.
     */
    public static final String COLONIA_KEY = "coloniaForm";
    /**
     * The request scope attribute that holds the colonia list
     */
    public static final String COLONIA_LIST = "coloniaList";
    // Colonia-END
    // Municipio-START
    /**
     * The request scope attribute that holds the municipio form.
     */
    public static final String MUNICIPIO_KEY = "municipioForm";
    /**
     * The request scope attribute that holds the municipio list
     */
    public static final String MUNICIPIO_LIST = "municipioList";
    // Municipio-END
    // Ciudad-START
    /**
     * The request scope attribute that holds the ciudad form.
     */
    public static final String CIUDAD_KEY = "ciudadForm";
    /**
     * The request scope attribute that holds the ciudad list
     */
    public static final String CIUDAD_LIST = "ciudadList";
    // Ciudad-END
    // Puesto-START
    /**
     * The request scope attribute that holds the puesto form.
     */
    public static final String PUESTO_KEY = "puesto";
    /**
     * The request scope attribute that holds the puesto list
     */
    public static final String PUESTO_LIST = "puestoList";
    // Puesto-END
    // Vacaciones-START
    /**
     * The request scope attribute that holds the vacaciones form.
     */
    public static final String VACACIONES_KEY = "vacacionesForm";
    /**
     * The request scope attribute that holds the vacaciones list
     */
    public static final String VACACIONES_LIST = "vacacionesList";
    // Vacaciones-END
    // Direccion-START
    /**
     * The request scope attribute that holds the direccion form.
     */
    public static final String DIRECCION_KEY = "direccionForm";
    /**
     * The request scope attribute that holds the direccion list
     */
    public static final String DIRECCION_LIST = "direccionList";
    // Direccion-END
    // Proveedor-START
    /**
     * The request scope attribute that holds the proveedor form.
     */
    public static final String PROVEEDOR_KEY = "proveedorForm";
    /**
     * The request scope attribute that holds the proveedor list
     */
    public static final String PROVEEDOR_LIST = "proveedorList";
    public static final String TIPO_PERSONA_FISICA = "F";
    public static final String TIPO_PERSONA_MORAL = "M";
    // Proveedor-END
    // Factura-START
    /**
     * The request scope attribute that holds the factura form.
     */
    public static final String FACTURA_KEY = "facturaForm";
    /**
     * The request scope attribute that holds the factura list
     */
    public static final String FACTURA_LIST = "facturaList";
    /**
     * Se utiliza para la lista de importes gravados de la factura
     */
    public static final String GRAVADO_LIST = "gravadoList";
    // Factura-END
    // PorcentajeGravado-START
    /**
     * The request scope attribute that holds the porcentajeGravado form.
     */
    public static final String PORCENTAJEGRAVADO_KEY = "porcentajeGravadoForm";
    /**
     * The request scope attribute that holds the porcentajeGravado list
     */
    public static final String PORCENTAJEGRAVADO_LIST = "porcentajeGravadoList";
    // PorcentajeGravado-END
    // ActividadGravamen-START
    /**
     * The request scope attribute that holds the actividad form.
     */
    public static final String ACTIVIDADGRAVAMEN_KEY = "actividadGravamenForm";
    /**
     * The request scope attribute that holds the actividad list
     */
    public static final String ACTIVIDADGRAVAMEN_LIST = "actividadGravamenList";
    // ActividadGravamen-END
    // ModalidadGravamen-START
    /**
     * The request scope attribute that holds the modalidadGravamen form.
     */
    public static final String MODALIDADGRAVAMEN_KEY = "modalidadGravamenForm";
    /**
     * The request scope attribute that holds the modalidadGravamen list
     */
    public static final String MODALIDADGRAVAMEN_LIST = "modalidadGravamenList";
    // ModalidadGravamen-END
    // FacturaDetalle-START
    /**
     * The request scope attribute that holds the facturaDetalle form.
     */
    public static final String FACTURADETALLE_KEY = "facturaDetalleForm";
    /**
     * The request scope attribute that holds the facturaDetalle list
     */
    public static final String FACTURADETALLE_LIST = "facturaDetalleList";
    // FacturaDetalle-END
    // Asentamiento-START
    /**
     * The request scope attribute that holds the asentamiento form.
     */
    public static final String ASENTAMIENTO_KEY = "asentamientoForm";
    /**
     * The request scope attribute that holds the asentamiento list
     */
    public static final String ASENTAMIENTO_LIST = "asentamientoList";
    // Asentamiento-END
    // TipoAsentamiento-START
    /**
     * The request scope attribute that holds the tipoAsentamiento form.
     */
    public static final String TIPOASENTAMIENTO_KEY = "tipoAsentamientoForm";
    /**
     * The request scope attribute that holds the tipoAsentamiento list
     */
    public static final String TIPOASENTAMIENTO_LIST = "tipoAsentamientoList";
    // TipoAsentamiento-END
    public static final String MAP_SESSION = null;
    // ParamGral-START
    /**
     * The request scope attribute that holds the paramGral form.
     */
    public static final String PARAMGRAL_KEY = "paramGralForm";
    /**
     * The request scope attribute that holds the paramGral list
     */
    public static final String PARAMGRAL_LIST = "paramGralList";
    // ParamGral-END
    // DiasVacaciones-START
    /**
     * The request scope attribute that holds the diasVacaciones form.
     */
    public static final String DIASVACACIONES_KEY = "diasVacacionesForm";
    /**
     * The request scope attribute that holds the diasVacaciones list
     */
    public static final String DIASVACACIONES_LIST = "diasVacacionesList";
    // DiasVacaciones-END
    // Institucion-START
    /**
     * The request scope attribute that holds the institucion form.
     */
    public static final String INSTITUCION_KEY = "institucionForm";
    /**
     * The request scope attribute that holds the institucion list
     */
    public static final String INSTITUCION_LIST = "institucionList";
    // Institucion-END
    // InstitucionAlumno - START
    public static final String ALUMNOINSTITUCION_KEY = "alumnoInstitucionForm";
    public static final String ALUMNOINSTITUCION_SET = "alumnoInstitucionSet";
    public static final String ALUMNOINSTITUCION_LIST = "alumnoInstitucionList";
    public static final String ALUMNOINSTITUCION_ORIGEN = "origen";
    // InstitucionAlumno - END
    // Alumno - START
    public static final String ALUMNO_OBJ = "alumno";
    public static final String ESTADO_CIVIL_CASADO = "C";
    public static final String ESTADO_CIVIL_SOLTERO = "S";
    public static final String ESTADO_CIVIL_DIVORCIADO = "D";
    public static final String ESTADO_CIVIL_VIUDO = "V";
    public static final String ALUMNO_FACULTAD_PREPARATORIA = "107";
    // Alumno - END
    // ClasificacionFactura-START
    /**
     * The request scope attribute that holds the clasificacionFactura form.
     */
    public static final String CLASIFICACIONFACTURA_KEY = "clasificacionFacturaForm";
    /**
     * The request scope attribute that holds the clasificacionFactura list
     */
    public static final String CLASIFICACIONFACTURA_LIST = "clasificacionFacturaList";
    // ClasificacionFactura-END
    // DeclaracionInformativa-START
    public static final String DECLARACIONINFORMATIVA_KEY = "declaracionInformativaForm";
    public static final String DECLARACIONINFORMATIVA_LIST = "declaracionInformativaList";
    // DeclaracionInformativa-END
    // Cargas-START
    public static final String CARGA_KEY = "cargaForm";
    public static final String CARGA_LIST = "cargaList";
    // Cargas-END
    // Atributo-START
    /**
     * The request scope attribute that holds the atributo form.
     */
    public static final String ATRIBUTO_KEY = "atributoForm";
    /**
     * The request scope attribute that holds the atributo list
     */
    public static final String ATRIBUTO_LIST = "atributoList";
    // Atributo-END
    // PerDed-START
    /**
     * The request scope attribute that holds the perDed form.
     */
    public static final String PERDED_KEY = "perDedForm";
    /**
     * The request scope attribute that holds the perDed list
     */
    public static final String PERDED_LIST = "perDedList";
    public static final String PERDED_ATRIBUTOS = "atributos";
    public static final String AVAILABLE_ATRIBUTOS = "availableAtributos";
    // Quincenal
    public static final String PERDED_FRECUENCIA_PAGO_Q = "Q";
    // Mensual
    public static final String PERDED_FRECUENCIA_PAGO_M = "M";
    // Atributos
    public static final String PERDED_ATRIBUTO_DIEZMO = "D";
    public static final String PERDED_ATRIBUTO_ISPT = "I";
    public static final String PERDED_ATRIBUTO_DEPRECIACION = "S";
    public static final String PERDED_ATRIBUTO_BASENOMINA = "BN";
    public static final String PERDED_ATRIBUTO_SOBRESUELDO = "S";
    public static final String PERDED_ATRIBUTO_NOMINA = "N";
    // Claves palabras reservadas
    public static final String PERDED_AGUINALDO_CLAVE = "PD029";
    public static final String PERDED_AGUINALDO_NOMBRE = "Aguinaldo";
    public static final String PERDED_BONOS_PUNTUALIDAD_CLAVE = "PD025";
    public static final String PERDED_BONOS_PUNTUALIDAD_NOMBRE = "Bonos Puntualidad";
    public static final String PERDED_BONOS_ASISTENCIA_CLAVE = "PD026";
    public static final String PERDED_BONOS_ASISTENCIA_NOMBRE = "Bonos Asistencia";
    public static final String PERDED_DEPRECIACION_CLAVE = "PD005";
    public static final String PERDED_DEPRECIACION_NOMBRE = "Depreciacion";
    public static final String PERDED_ESCALAFON_CLAVE = "PD007";
    public static final String PERDED_ESCALAFON_NOMBRE = "Escalafon";
    public static final String PERDED_DESPENSA1_CLAVE = "PD006";
    public static final String PERDED_DESPENSA1_NOMBRE = "Bonos Despensa 1";
    public static final String PERDED_DESPENSA2_CLAVE = "PD004";
    public static final String PERDED_DESPENSA2_NOMBRE = "Bonos Despensa 2";
    public static final String PERDED_DONATIVO_CLAVE = "PD016";
    public static final String PERDED_DONATIVO_NOMBRE = "Donativo";
    public static final String PERDED_ISPT_CLAVE = "PD015";
    public static final String PERDED_ISPT_NOMBRE = "ISPT";
    public static final String PERDED_RENTA_CASA_CLAVE = "PD020";
    public static final String PERDED_RENTA_CASA_NOMBRE = "Renta Casa";
    public static final String PERDED_RENTA_PROPIETARIOS_CLAVE = "PD011";
    public static final String PERDED_RENTA_PROPIETARIOS_NOMBRE = "Renta Propietarios";
    public static final String PERDED_PERC_RENTA_PROPIETARIOS_CLAVE = "PD012";
    public static final String PERDED_PERC_RENTA_PROPIETARIOS_NOMBRE = "Perc. Renta Propietarios";
    public static final String PERDED_DED_RENTA_PROPIETARIOS_CLAVE = "PD019";
    public static final String PERDED_DED_RENTA_PROPIETARIOS_NOMBRE = "Ded. Renta Propietarios";
    public static final String PERDED_RENTA_SOLTEROS_CLAVE = "PD021";
    public static final String PERDED_RENTA_SOLTEROS_NOMBRE = "Renta Solteros";
    public static final String PERDED_SALARIO_CLAVE = "PD003";
    public static final String PERDED_SALARIO_NOMBRE = "Salario";
    public static final String PERDED_VACACIONES_CLAVE = "PD028";
    public static final String PERDED_VACACIONES_NOMBRE = "Vacaciones";
    public static final String PERDED_SDI_CLAVE = "PD024";
    public static final String PERDED_SDI_NOMBRE = "Salario Diario Integrado";
    public static final String PERDED_SMDF_CLAVE = "PD018";
    public static final String PERDED_SMDF_NOMBRE = "Salario Minimo DF";
    public static final String PERDED_FEUM_CLAVE = "PD017";
    public static final String PERDED_FEUM_NOMBRE = "FEUM";
    public static final String PERDED_FEUM_LP_CLAVE = "PD022";
    public static final String PERDED_FEUM_LP_NOMBRE = "FEUM L. PLAZO";
    public static final String PERDED_IMSS_CLAVE = "PD014";
    public static final String PERDED_IMSS_NOMBRE = "FEUM";
    public static final String PERDED_PCT_AMORT_INFO_CLAVE = "PD036";
    public static final String PERDED_PCT_AMORT_INFO_NOMBRE = "Pct. Amortizacion Infonavit";
    public static final String PERDED_AMORT_INFO_CLAVE = "PD039";
    public static final String PERDED_AMORT_INFO_NOMBRE = "Amortizacion Infonavit";
    public static final String PERDED_SMGZA_CLAVE = "PD037";
    public static final String PERDED_SMGZA_NOMBRE = "SMGZA";
    // public static final String PERDED_SEGURO_INFO_CLAVE = "PD038";
    // public static final String PERDED_SEGURO_INFO_NOMBRE =
    // "Seguro Infonavit";
    public static final String PERDED_DCTO_IMSS_CLAVE = "PD034";
    public static final String PERDED_DCTO_IMSS_NOMBRE = "Descuento IMSS";
    public static final String PERDED_DCTO_DIF_3SMDF_CLAVE = "PD035";
    public static final String PERDED_DCTO_DCTO_DIF_3SMDF_NOMBRE = "Dcto. Dif. 3 SMDF";
    public static final String PERDED_SOBRESUELDO_CLAVE = "PD010";
    public static final String PERDED_PCT_DEPRECIACION_CLAVE = "PD031";
    public static final String PERDED_PCT_DEPRECIACION_NOMBRE = "Pct. Depreciacion";
    public static final String PERDED_PCT_DESPENSA_CLAVE = "PD030";
    public static final String PERDED_PCT_DESPENSA_NOMBRE = "Pct. Pasajes";
    public static final String PERDED_PCT_NOMINA_CLAVE = "PD019";
    public static final String PERDED_PCT_NOMINA_NOMBRE = "Pct. Nomina";
    // PerDed-END
    // EmpleadoPerDed-START
    /**
     * Tipo importe - Porcentaje
     */
    public static final String TIPOIMPORTE_PCT = "%";
    /*
     * Tipo importe - Cantidad en efectivo.
     */
    public static final String TIPOIMPORTE_IMP = "$";
    public static final String EMPLEADOPERDED_KEY = "empleadoPerDedForm";
    public static final String EMPLEADOPERDED_LIST = "empleadoPerDedList";
    // EmpleadoPerDed-END
    // CalculoNomina-START
    public static final String CALCULONOMINA_QUINCENA = "Q";
    public static final String CALCULONOMINA_MENSUAL = "M";
    public static final String CALCULONOMINA_FORM = "calculoNominaForm";
    public static final String CALCULONOMINA_LIST = "calculoNominaList";
    // Estatus del proceso de confirmacion
    public static final String CALCULONOMINA_STATUS_NO_CONFIRMADO = "A";
    public static final String CALCULONOMINA_STATUS_CONFIRMADO_BY_OPER = "OK";
    public static final String CALCULONOMINA_STATUS_CONFIRMADO_BY_ADMIN = "R";
    public static final String CALCULONOMINA_STATUS_CONFIRMADO_BY_AUDITA = "AD";
    public static final String CALCULONOMINA_STATUS_POLIZA_GENERADA = "PG";
    // Opciones de confirmaciones
    public static final String CALCULONOMINA_CONFIRMA_OPER = "Oper";
    public static final String CALCULONOMINA_CONFIRMA_ADMIN = "Admin";
    public static final String CALCULONOMINA_CONFIRMA_AUDITA = "Audita";
    // Roles validos en el proceso
    public static final String CALCULONOMINA_ROLE_OPER = "NOMUM";
    public static final String CALCULONOMINA_ROLE_ADMIN = "NOMAdmin";
    public static final String CALCULONOMINA_ROLE_AUDITA = "AUDAdmin";
    public static final String CALCULONOMINA_GENERA_POLIZA = "generaPoliza";
    public static final String CALCULONOMINA_DESCRIPCION = "CALCULO DE NOMINA";
    // CalculoNomina-END
    // CalculoNominaDetalle-START
    public static final String CALCULONOMINADETALLE_LIST = "calculoNominaDetalleList";
    public static final String CALCULONOMINADETALLE_FORM = "calculoNominaDetalleForm";
    // CalculoNominaDetalle-END
    // ReportesFinancieros-START
    public static final String EDO_CTA_SALDOS_AUXILIARES = "saldosAuxiliares";
    // ReportesFinancieros-END
    // ReporteFEstudiantiles-START
    public static final String REPORTEFESTUDIANTILES_KEY = "reporteFestudiantilesForm";
    public static final String REPORTEFESTUDIANTILES_LIST = "reporteFestudiantilesList";
    public static final String ALUMNO_INSTITUCIONES = "alumnoInstituciones";
    public static final String CCOBRO_MOVS_MATRICULA = "01-";
    public static final String CCOBRO_MOVS_ENSENANZA = "02-";
    public static final String CCOBRO_MOVS_INTERNADO = "03-";
    public static final String CCOBRO_MOVS_MANEJO_PAGARE = "08-";
    public static final String CCOBRO_MOVS_DESCHO_MATRICULA = "04M";
    public static final String CCOBRO_MOVS_DESCHO_ENSENANZA = "04E";
    public static final String CCOBRO_MOVS_DESCHO_INTERNADO = "04I";
    // ReporteFEtudiantiles-END
    // TablaISPT-START
    /**
     * The request scope attribute that holds the tablaISPT form.
     */
    public static final String TABLAISPT_KEY = "tablaISPTForm";
    /**
     * The request scope attribute that holds the tablaISPT list
     */
    public static final String TABLAISPT_LIST = "tablaISPTList";
    /**
     * Tipo de tabla - Quincenal
     */
    public static final String TABLAISPT_TIPO_Q = "Q";
    /**
     * Tipo de tabla - Mensual
     */
    public static final String TABLAISPT_TIPO_M = "M";
    // TablaISPT-END
    // AlumnoComedor-START
    public static final String ALUMNOCOMEDOR_DESAYUNO = "desayuno";
    public static final String ALUMNOCOMEDOR_COMIDA = "comida";
    public static final String ALUMNOCOMEDOR_CENA = "cena";
    public static final String ALUMNOCOMEDOR_OBJ = "alumnoComedor";
    public static final String ALUMNOCOMEDOR_KEY = "alumnoComedorForm";
    public static final String ALUMNOCOMEDOR_LIST = "alumnoComedorList";
    // AlumnoComedor-END
    // EntradaComedor-START
    public static final String ENTRADACOMEDOR_KEY = "entradaComedorForm";
    public static final String ENTRADACOMEDOR_LIST = "entradaComedorList";
    public static final String ENTRADACOMEDOR_TIPO_COMIDA = "tipoComida";
    public static final String ENTRADACOMEDOR_TIPO_COMIDA_DESAYUNO = "desayuno";
    public static final String ENTRADACOMEDOR_TIPO_COMIDA_COMIDA = "comida";
    public static final String ENTRADACOMEDOR_TIPO_COMIDA_CENA = "cena";
    public static final String ENTRADACOMEDOR_FECHA = "fecha";
    public static final String ENTRADACOMEDOR_AUTORIZADO = "autorizado";
    public static final String ENTRADACOMEDOR_AUTORIZADO_SI = "S";
    public static final String ENTRADACOMEDOR_AUTORIZADO_NO = "N";
    public static final String ENTRADACOMEDOR_ORIGEN = "origen";
    public static final String ENTRADACOMEDOR_ORIGEN_GENERAL = "general";
    public static final String ENTRADACOMEDOR_ORIGEN_NORMAL = "normal";
    public static final String ENTRADACOMEDOR_ORIGEN_HECHO = "hecho";
    public static final String ENTRADACOMEDOR_ALUMNO = "alumno";
    public static final String ENTRADACOMEDOR_CONTADOR_ALUMNO = "contadorAlumno";
    // EntradaComedor-END
    // CobroClimaInternado-START
    /**
     * The request scope attribute that holds the cobroClimaInternado form.
     */
    public static final String COBROCLIMAINTERNADO_KEY = "cobroClimaInternadoForm";
    /**
     * The request scope attribute that holds the cobroClimaInternado list
     */
    public static final String COBROCLIMAINTERNADO_LIST = "cobroClimaInternadoList";
    // CobroClimaInternado-END
    // CobroOnLine-Start
    public static final String COBROONLINE_LIST = "cobroOnLineList";
    public static final String COBROONLINE_KEY = "cobroOnLineForm";
    public static final String COBROONLINE_LIBRO = "21";
    public static final String COBROONLINE_USER = "234";// "259";
    public static final String COBROONLINE_CONCEPTO = "41";
    public static final String COBROONLINE_POLIZA_DESCRIPCION = "INGRESOS VIA PAGOS ONLINE";
    public static final String COBROONLINE_MOVIMIENTO_DESCRIPCION = "DEPOSITO ONLINE";
    // CobroOnLine-END
    // TipoConvenio-START
    /**
     * The request scope attribute that holds the tipoConvenio form.
     */
    public static final String TIPOCONVENIO_KEY = "tipoConvenioForm";
    /**
     * The request scope attribute that holds the tipoConvenio object.
     */
    public static final String TIPOCONVENIO_ID = "tipoConvenioId";
    /**
     * The request scope attribute that holds the tipoConvenio list
     */
    public static final String TIPOCONVENIO_LIST = "tipoConvenioList";
    /**
     * Indica el orgien del llamado para el method=Edit: si este viene de
     * TipoConvenioFecha (fecha), o si viene de TipoConvenioList Este origen se
     * utiliza en global-forwards.xml
     */
    public static final String TIPOCONVENIO_ORIGEN_FECHA = "fecha";
    // TipoConvenio-END
    // TipoConvenioFecha-START
    /**
     * The request scope attribute that holds the tipoConvenioFecha form.
     */
    public static final String TIPOCONVENIOFECHA_KEY = "tipoConvenioFechaForm";
    /**
     * The request scope attribute that holds the tipoConvenioFecha list
     */
    public static final String TIPOCONVENIOFECHA_LIST = "tipoConvenioFechaList";
    // TipoConvenioFecha-END
    // Convenio-START
    /**
     * The request scope attribute that holds the convenio form.
     */
    public static final String CONVENIO_KEY = "convenioForm";
    /**
     * The request scope attribute that holds the convenio list
     */
    public static final String CONVENIO_LIST = "convenioList";
    public static final String CONVENIO_OBJ = "convenio";
    public static final String CONVENIO_STATUS_ACTIVO = "A";
    public static final String CONVENIO_STATUS_INACTIVO = "I";
    public static final String CONVENIO_STATUS_ELIMINADO = "X";
    // Convenio-END
    // AFEPuesto-START
    /**
     * The request scope attribute that holds the aFEPuesto form.
     */
    public static final String AFEPUESTO_KEY = "afePuestoForm";
    /**
     * The request scope attribute that holds the aFEPuesto list
     */
    public static final String AFEPUESTO_LIST = "afePuestoList";
    // AFEPuesto-END
    // ReportesAFE - START
    public static final String REPORTESAFE_KEY = "reportesAFEForm";
    public static final String REPORTESAFE_LIST = "reportesAFEList";
    public static final String REPORTESAFE_ORDERBY_CCOSTO = "CC";
    public static final String REPORTESAFE_ORDERBY_CATEGORIA = "Ctg";
    public static final String REPORTESAFE_FILTRO_TODAS_PLAZAS = "T";
    public static final String REPORTESAFE_FILTRO_PLAZAS_VACANTES = "V";
    public static final String REPORTESAFE_FILTRO_PLAZAS_CONTRATADAS = "C";
    public static final String REPORTESAFE_FILTRO_TODOS_INFORMES = "T";
    public static final String REPORTESAFE_FILTRO_INFORMES_NO_ENVIADOS = "Nein";
    public static final String REPORTESAFE_FILTRO_INFORMES_ENVIADOS = "OK";
    public static final String REPORTESAFE_FILTRO_INFORMES_AUTORIZADOS = "At";
    public static final String REPORTESAFE_FILTRO_INFORMES_CONTABILIZADOS = "Cl";
    public static final String REPORTESAFE_FILTRO_INFORMES_RECHAZADOS = "X";
    public static final String CONCENTRADO_AYUDAS_ADICIONALES_LIST = "concentradoAyudasAdicionalesList";
    public static final String CONCENTRADO_BECASADICIONALES_LIST = "concentradoBecasAdicionalesList";
    // ReportesAFE - END
    // WarningsComedor-START
    /**
     * Alumno ya ingreso al comedor en esa fecha y en esa comida
     *
     */
    public static final String WARNING_COMEDOR = "warningComedor";
    public static final String WARNING_COMEDOR_ALUMNO_NO_INSCRITO_ID = "alumnoNoInscrito";
    public static final String WARNING_COMEDOR_ALUMNO_NO_INSCRITO_NOMBRE = "alumnoNoInscrito";
    public static final String WARNING_COMEDOR_ALUMNO_NO_INSCRITO_DESCRIPCION = "entradaComedor.errors.studentNotRegistered";
    public static final String WARNING_COMEDOR_COMIDA_REPETIDA_ID = "comidaRepetida";
    public static final String WARNING_COMEDOR_COMIDA_REPETIDA_NOMBRE = "ComidaRepetida";
    public static final String WARNING_COMEDOR_COMIDA_REPETIDA_DESCRIPCION = "entradaComedor.warning.studentAlreadyRegistered";
    public static final String WARNING_COMEDOR_SIN_COMIDA_ID = "sinComida";
    public static final String WARNING_COMEDOR_SIN_COMIDA_NOMBRE = "sinComida";
    public static final String WARNING_COMEDOR_SIN_COMIDA_DESCRIPCION = "entradaComedor.warning.missingMeal";
    public static final String WARNING_COMEDOR_RANGO_FECHAS_ID = "rangoFechas";
    public static final String WARNING_COMEDOR_RANGO_FECHAS_NOMBRE = "rangoFechas";
    public static final String WARNING_COMEDOR_RANGO_FECHAS_DESCRIPCION = "entradaComedor.warning.outsideOfDate";
    public static final String WARNING_COMEDOR_COMIDADOBLE_COMIDA = "comidaDobleComida";
    public static final String WARNING_COMEDOR_COMIDADOBLE_CENA = "comidaDobleCena";
    public static final String WARNING_COMEDOR_COMIDAS_COMIDA = "comida";
    public static final String WARNING_COMEDOR_COMIDAS_CENA = "cena";
    public static final String WARNING_COMEDOR_COMIDADOBLE_COMIDA_DESC = "entradaComedor.warning.MissingLunchs";
    public static final String WARNING_COMEDOR_COMIDADOBLE_CENA_DESC = "entradaComedor.warning.MissingDinners";
    public static final String WARNING_COMEDOR_COMIDADOBLE = "comidaDoble";
    public static final String WARNING_COMEDOR_COMIDASDOBLE = "comidasDobles";
    public static final String WARNING_COMEDOR_COMIDADOBLE_DESC = "entradaComedor.warning.MissingMeals";
    public static final String WARNING_COMEDOR_COMIDA_NO_DESAYUNO_ID = "desayuno";
    public static final String WARNING_COMEDOR_COMIDAS_NO_DESAYUNO_NOMBRE = "desayunos";
    public static final String WARNING_COMEDOR_COMIDA_NO_DESAYUNO_DESCRIPCION = "entradaComedor.warning.MissingBreakfasts";
    public static final String WARNING_COMEDOR_COMIDA_NO_COMIDA_ID = "comida";
    public static final String WARNING_COMEDOR_COMIDAS_NO_COMIDA_NOMBRE = "comidas";
    public static final String WARNING_COMEDOR_COMIDA_NO_COMIDA_DESCRIPCION = "entradaComedor.warning.MissingLunchs";
    public static final String WARNING_COMEDOR_COMIDA_NO_CENA_ID = "cena";
    public static final String WARNING_COMEDOR_COMIDAS_NO_CENA_NOMBRE = "cenas";
    public static final String WARNING_COMEDOR_COMIDA_NO_CENA_DESCRIPCION = "entradaComedor.warning.MissingDinners";
    // WarningsComedor-END
    // ReportesComedor-START
    public static final String REPORTES_COMEDOR_ORIGEN_FEST = "fest";
    public static final String REPORTES_COMEDOR_ORIGEN_COMEDOR = "comedor";
    public static final String REPORTES_COMEDOR_FORM = "reporteComedorForm";
    public static final String REPORTES_COMEDOR_RPT_CENSO_COMIDAS = "censoComidasAlumnosList";
    public static final String REPORTECOMEDOR_CENFEST_LIST = "censoComidasAlumnoList";
    // ReportesComedor-END
    // DocumentoNomina-START
    /**
     * The request scope attribute that holds the documentoNomina form.
     */
    public static final String DOCUMENTONOMINA_KEY = "documentoNominaForm";
    /**
     * The request scope attribute that holds the documentoNomina list
     */
    public static final String DOCUMENTONOMINA_LIST = "documentoNominaList";
    public static final String DOCUMENTONOMINA_CONC_TENENCIA = "documentoNominaTenencia";
    public static final String DOCUMENTONOMINA_CONC_AGUA = "documentoNominaAgua";
    public static final String DOCUMENTONOMINA_CONC_GAS = "documentoNominaGas";
    public static final String DOCUMENTONOMINA_CONC_LUZ = "documentoNominaLuz";
    public static final String DOCUMENTONOMINA_CONC_TELEFONO = "documentoNominaTelefono";
    public static final String DOCUMENTONOMINA_PPTO_GASTOS_DISPONIBLE = "nominaPptoGastosDisponible";
    public static final String DOCUMENTONOMINA_PPTO_AGUA_DISPONIBLE = "nominaPptoAguaDisponible";
    /**
     * Status
     */
    public static final String DOCUMENTONOMINA_STATUS_ACTIVO = "A";
    public static final String DOCUMENTONOMINA_STATUS_ENVIADO = "OK";
    public static final String DOCUMENTONOMINA_STATUS_RECIBIDO = "R";
    public static final String DOCUMENTONOMINA_STATUS_AUTORIZADO = "I";
    public static final String DOCUMENTONOMINA_STATUS_RECHAZADO = "X";
    /**
     * DocumentoNomina's Origen parameter values
     */
    public static final String DOCUMENTONOMINA_ORIGEN_EMPLEADO = "empleado";
    public static final String DOCUMENTONOMINA_ORIGEN_NOMINA = "nomina";
    // DocumentoNomina-END
    // SolicitudSalida-START
    /**
     * The request scope attribute that holds the solicitudSalida form.
     */
    public static final String SOLICITUDSALIDA_KEY = "solicitudSalidaForm";
    /**
     * SolicitudSalida's POJO
     */
    public static final String SOLICITUDSALIDA = "solicitudSalida";
    /**
     * The request scope attribute that holds the solicitudSalida list
     */
    public static final String SOLICITUDSALIDA_LIST = "solicitudSalidaList";
    public static final String SOLICITUDSALIDA_LIST_FURLOUGH = "solicitudSalidaListFurlough";
    /**
     * SolicitudSalida's Origen parameter values
     */
    public static final String SOLICITUDSALIDA_ORIGEN_EMPLEADO = "empleado";
    public static final String SOLICITUDSALIDA_ORIGEN_EMPLEADO_FURLOUGH = "empleadoFurlough";
    public static final String SOLICITUDSALIDA_ORIGEN_RH = "rh";
    public static final String SOLICITUDSALIDA_ORIGEN_JEFECC = "cc";
    public static final String SOLICITUDSALIDA_ORIGEN_NOMINA = "nomina";
    /**
     * Status
     */
    public static final String SOLICITUDSALIDA_STATUS_FECHAAUTORIZA = "F";
    public static final String SOLICITUDSALIDA_STATUS_ACTIVO = "A";
    public static final String SOLICITUDSALIDA_STATUS_ENVIADO = "OK";
    public static final String SOLICITUDSALIDA_STATUS_RECIBIDO = "R";
    public static final String SOLICITUDSALIDA_STATUS_AUTORIZADO = "PR";
    public static final String SOLICITUDSALIDA_STATUS_RECHAZADO = "X";
    public static final String SOLICITUDSALIDA_STATUS_AUTORIZADO_CONTRALOR = "PC";
    public static final String SOLICITUDSALIDA_STATUS_ENVIADO_JEFE = "OJ";
    // Todos los estatus[para reportes]
    public static final String SOLICITUDSALIDA_STATUS_TODOS = "T";
    // indica que ya se pago la prima vacacional
    public static final String SOLICITUDSALIDA_STATUS_PRIMA_VACACIONAL = "PV";
    /**
     * Limite de kilometros para recibir dos dias adicionales
     */
    public static final Integer SOLICITUDSALIDA_KMS_LIMITE = 900;
    // Consultas
    /**
     * Indica que se quiere consultar TODOS las solicitudes recibidas
     */
    public static final String SOLICITUDSALIDA_CONSULTA_TODOS = "T";
    /**
     * Inidica que se quiere consultar las solicitudes (sin importar el status)
     * del empleado en session
     */
    public static final String SOLICITUDSALIDA_CONSULTA_EMPLEADO = "E";
    public static final String SOLICITUDSALIDA_DIR_RH = "C.P. Eliezer Castellanos Alvarez";
    // SolicitudSalida-END
    // PresupuestoAFE-START
    /**
     * The request scope attribute that holds the presupuestoAFE form.
     */
    public static final String PRESUPUESTOAFE_KEY = "presupuestoAFEForm";
    /**
     * The request scope attribute that holds the presupuestoAFE list
     */
    public static final String PRESUPUESTOAFE_LIST = "presupuestoAFEList";
    // PresupuestoAFE-END
    // UserCuenta - START
    public static final String USERCUENTA_LIST = "userCuentaList";
    public static final String USERCUENTA_KEY = "userCuentaForm";
    // UserCuenta - END
    // UserAutoriza - Start
    public static final String USERAUTORIZA_KEY = "userAutorizaForm";
    public static final String USERAUTORIZA_LIST = "userAutorizaList";
    // UserAutoriza - END
    // ValesGasolina- START
    public static final String VALESGASOLINA_LIST = "valesGasolinaList";
    public static final String VALESGASOLINA_KEY = "valesGasolinaForm";
    /**
     * constants cual es el estado de los vales de gasolina
     */
    public static final String VALESGASOLINA_STATUS_ACTIVO = "A";
    public static final String VALESGASOLINA_STATUS_ENVIADO = "OK";
    public static final String VALESGASOLINA_STATUS_RECIBIDO = "R";
    public static final String VALESGASOLINA_STATUS_AUTORIZADO = "I";
    public static final String VALESGASOLINA_STATUS_RECHAZADO = "X";
    public static final String VALESGASOLINA_STATUS_PAGADO = "P";
    // Consultas
    // ValesGasolina - END
    // ReportesRH - START
    public static final String REPORTESRH_LIST = "reportesRHList";
    // ReportesRH - END
    // Nacionalidades-START
    /**
     * The request scope attribute that holds the nacionalidades form.
     */
    public static final String NACIONALIDADES_KEY = "nacionalidadesForm";
    /**
     * The request scope attribute that holds the nacionalidades list
     */
    public static final String NACIONALIDADES_LIST = "nacionalidadesList";
    // Nacionalidades-END
    // Evento-START
    public static final String EVENTO = "evento";
    /**
     * The request scope attribute that holds the eventos form.
     */
    public static final String EVENTO_KEY = "eventoForm";
    /**
     * The request scope attribute that holds the eventos list
     */
    public static final String EVENTO_LIST = "eventoList";
    // Evento-END
    // AsistenciaEventos-START
    /**
     * The request scope attribute that holds the asistenciaEventos form.
     */
    public static final String ASISTENCIAEVENTOS_KEY = "asistenciaEventosForm";
    public static final String ASISTENCIAEVENTOS_TODOS_EMPLEADO_LIST = "asistenciaEventosTodosEmpleado";
    /**
     * The request scope attribute that holds the asistenciaEventos list
     */
    public static final String ASISTENCIAEVENTOS_LIST = "asistenciaEventosList";
    public static final String ASISTENCIAEVENTOS_ESTADISTICA = "asistenciaEventosEstadisticaList";
    public static final String ASISTENCIAEVENTOS_HORAS_LIST = "asistenciaEventosHorasList";
    public static final String ASISTENCIAEVENTOS_MINUTOS_LIST = "asistenciaEventosMinutosList";
    public static final String STATUS_ASISTENCIA_PRESENCIA = "P";
    public static final String STATUS_ASISTENCIA_AUSENCIA = "A";
    public static final String STATUS_ASISTENCIA_TARDANZA = "T";
    public static final String STATUS_ASISTENCIA_AUSENCIAJUSTIFICADA = "AJ";
    // AsistenciaEventos-END
    /**
     * Reportes RH Origen parameter values
     */
    public static final String REPORTE_RH_ORIGEN_EMPLEADO = "empleado";
    public static final String REPORTE_RH_ORIGEN_RH = "rh";
    // SolicitudBeca-START
    /**
     * The request scope attribute that holds the solicitudBeca form.
     */
    public static final String SOLICITUDBECA_KEY = "solicitudBecaForm";
    /**
     * The request scope attribute that holds the solicitudBeca list
     */
    public static final String SOLICITUDBECA_LIST = "solicitudBecaList";
    // SolicitudBeca-END
    // CentroCostoAFEPuesto - START
    public static final String CENTROCOSTO_AFEPUESTO_KEY = "centroCostoAFEPuestoForm";
    public static final String CENTROCOSTO_AFEPUESTO_LIST = "centroCostoAFEPuestoList";
    public static final String CENTROCOSTO_AFEPUESTO_STATUS_ACTIVO = "A";
    public static final String CCOSTO_CC_AFE_PUESTO = "ccostoCCAP";
    // CentroCostoAFEPuesto - END
    // OtraBecaAFEPuesto - START
    public static final String OTRABECA_AFEPUESTO_KEY = "otraBecaAFEPuestoForm";
    public static final String OTRABECA_AFEPUESTO_LIST = "otraBecaAFEPuestoList";
    public static final String OTRABECA_AFEPUESTO_STATUS_ACTIVO = "A";
    public static final String CCOSTO_OB_AFE_PUESTO = "ccostoOBAP";
    // OtraBecaAFEPuesto - END
    // ContratoAFEAlumno-START
    /**
     * The request scope attribute that holds the contratoAFEAlumno form.
     */
    public static final String CONTRATOAFEALUMNO_KEY = "contratoAFEAlumnoForm";
    /**
     * The request scope attribute that holds the contratoAFEAlumno list
     */
    public static final String CONTRATOAFEALUMNO_LIST = "contratoAFEAlumnoList";
    public static final String CONTRATOAFEALUMNO_HISTORICO_LIST = "contratoAFEAlumnoHistoricoList";
    public static final String CONTRATOAFEALUMNO_DESTINO_PLAZAS = "verPlazas";
    public static final String CONTRATOAFEALUMNO_DESTINO_CONTRATOS = "verContratos";
    public static final String PLAZA = "plaza";
    public static final String CCOSTO_CONTRATO_AFE_ALUMNO = "ccostoCAA";
    // ContratoAFEAlumno-END
    // AFECategoria-START
    /**
     * The request scope attribute that holds the aFECategoria form.
     */
    public static final String AFECATEGORIA_KEY = "categoriaAFEForm";
    /**
     * The request scope attribute that holds the aFECategoria list
     */
    public static final String AFECATEGORIA_LIST = "categoriaAFEList";
    // AFECategoria-END
    // SolicitudColportaje-START
    /**
     * The request scope attribute that holds the solicitudColportaje form.
     */
    public static final String SOLICITUDCOLPORTAJE_KEY = "solicitudColportajeForm";
    /**
     * The request scope attribute that holds the solicitudColportaje list.
     */
    public static final String SOLICITUDCOLPORTAJE_LIST = "solicitudColportajeList";
    // SolicitudColportaje-END
    // Colportor-START
    /**
     * The request scope attribute that holds the alumnoColportor form.
     */
    public static final String COLPORTOR_KEY = "colportorForm";
    /**
     * The request scope attribute that holds the alumnoColportor list.
     */
    public static final String COLPORTOR_LIST = "coportorList";
    // Tipos de documentos
    /**
     * (F)actura - Compras del alumno a GEMA
     */
    public static final String COLPORTOR_TIPODOC_FACTURA = "F";
    /**
     * (D)iezmo - Depositos de diezmo en el campo correspondiente
     */
    public static final String COLPORTOR_TIPODOC_DIEZMO = "D";
    /**
     * (UM) Depositos UM - Depositos a la cuenta del estudiante en la UM
     */
    public static final String COLPORTOR_TIPODOC_DEPOSITO = "UM";
    public static final String COLPORTOR_MATRICULA = "UM";
    // Colportor-END
    // ColportorDocumento-START
    /**
     * The request scope attribute that holds the colportorDocumento form.
     */
    public static final String COLPORTORDOCUMENTO_KEY = "colportorDocumentoForm";
    /**
     * The request scope attribute that holds the colportorDocumento list.
     */
    public static final String COLPORTORDOCUMENTO_LIST = "colportorDocumentoList";
    // ColportorDocumento-END
    // ProcesoAFE-START
    /**
     * The request scope attribute that holds the procesoAFE form.
     */
    public static final String PROCESOAFE_KEY = "procesoAFEForm";
    /**
     * The request scope attribute that holds the procesoAFE list.
     */
    public static final String PROCESOAFE_LIST = "procesoAFEList";
    // SolicitudColportaje-END
    // ProyectoInstitucional-START
    /**
     * The request scope attribute that holds the proyectoInstitucional form.
     */
    public static final String PROYECTOINSTITUCIONAL_KEY = "proyectoInstitucionalForm";
    /**
     * The request scope attribute that holds the proyectoInstitucional list
     */
    public static final String PROYECTOINSTITUCIONAL_LIST = "proyectoInstitucionalList";
    public static final String PROYECTOINSTITUCIONAL = "proyectoInstitucional";
    public static final String PROYECTOINSTITUCIONAL_LEYENDA_SELECCION = "Seleccione un proyecto";
    // ProyectoInstitucional-END
    // Calendario-START
    /**
     * The request scope attribute that holds the calendario form.
     */
    public static final String CALENDARIO_KEY = "calendarioForm";
    /**
     * The request scope attribute that holds the calendario list
     */
    public static final String CALENDARIO_LIST = "calendarioList";
    // Calendario-END
    // TipoColportorDocumento-START
    /**
     * The request scope attribute that holds the tipoColportorDocumento form.
     */
    public static final String TIPOCOLPORTORDOCUMENTO_KEY = "tipoColportorDocumentoForm";
    /**
     * The request scope attribute that holds the tipoColportorDocumento list
     */
    public static final String TIPOCOLPORTORDOCUMENTO_LIST = "tipoColportorDocumentoList";
    // TipoColportorDocumento-END
    // Gasolinera-START
    /**
     * The request scope attribute that holds the gasolinera form.
     */
    public static final String GASOLINERA_KEY = "gasolineraForm";
    /**
     * The request scope attribute that holds the gasolinera list
     */
    public static final String GASOLINERA_LIST = "gasolineraList";
    // Gasolinera-END
    // ClienteColportor-START
    /**
     * The request scope attribute that holds the clienteColportor form.
     */
    public static final String CLIENTECOLPORTOR_KEY = "clienteColportorForm";
    /**
     * The request scope attribute that holds the clienteColportor list
     */
    public static final String CLIENTECOLPORTOR_LIST = "clienteColportorList";
    // ClienteColportor-END
    // SolicitudPermiso-START
    public static final String SESSION_TEMPORADA_COLPORTOR = "temporadaColportor";
    /**
     * The request scope attribute that holds the solicitudPermiso form.
     */
    public static final String SOLICITUDPERMISO_KEY = "solicitudPermisoForm";
    /**
     * The request scope attribute that holds the solicitudPermiso list
     */
    public static final String SOLICITUDPERMISO_LIST = "solicitudPermisoList";
    public static final String SOLICITUDPERMISO = "solicitudPermiso";
    // Consultas
    /**
     * Indica que se quiere consultar TODOS las solicitudes recibidas
     */
    public static final String SOLICITUDPERMISO_CONSULTA_TODOS = "T";
    /**
     * Inidica que se quiere consultar las solicitudes (sin importar el status)
     * del empleado en session
     */
    public static final String SOLICITUDPERMISO_CONSULTA_EMPLEADO = "E";
    // SolicitudPermiso-END
    // Campo-START
    /**
     * The request scope attribute that holds the campo form.
     */
    public static final String CAMPO_KEY = "campoForm";
    /**
     * The request scope attribute that holds the campo list
     */
    public static final String CAMPO_LIST = "campoList";
    // Campo-END
    // Asociacion-START
    /**
     * The request scope attribute that holds the asociacion form.
     */
    public static final String ASOCIACION_KEY = "asociacionForm";
    /**
     * The request scope attribute that holds the asociacion list
     */
    public static final String ASOCIACION_LIST = "asociacionList";
    // Asociacion-END
    // ReportesNomina-START
    public static final String REPORTES_NOMINA_LIST = "reportesNominaList";
    // ReportesNomina-END
    // HojaAFEAlumno-START
    public static final String HOJAAFEALUMNO = "hojaAFEAlumno";
    public static final String HOJA_ALUMNO = "hojaAlumno";
    // HojaAFEAlumno-END
    // NivelEstudios-START
    /**
     * The request scope attribute that holds the nivelEstudios form.
     */
    public static final String NIVELESTUDIOS_KEY = "nivelEstudiosForm";
    /**
     * The request scope attribute that holds the nivelEstudios list
     */
    public static final String NIVELESTUDIOS_LIST = "nivelEstudiosList";
    // NivelEstudios-END
    // EmpleadoEstudios-START
    public static final String EMPLEADOESTUDIOS_KEY = "empleadoEstudios";
    public static final String EMPLEADOESTUDIOS_LIST = "empleadoEstudiosList";
    // EmpleadoEstudios-END
    // EmpleadoEventos-START
    public static final String EMPLEADO_ESTADISTICA_EVENTOS = "empleadoEventos";
    // EmpleadoEventos-END
    // BecaAFEAlumno-START
    /**
     * The request scope attribute that holds the becaAFEAlumno form.
     */
    public static final String BECAAFEALUMNO_KEY = "becaAFEAlumnoForm";
    /**
     * The request scope attribute that holds the becaAFEAlumno list
     */
    public static final String BECAAFEALUMNO_LIST = "becaAFEAlumnoList";
    public static final BigDecimal BECAAFEALUMNO_PCT_TOPE = new BigDecimal(
            "0.67");
    // BecaAFEAlumno-END
    // AFEPlaza-START
    public static final String AFEPLAZA_DISCR_CCOSTO = "CC";
    public static final String AFEPLAZA_DISCR_PROYECTO = "PI";
    // AFEPlaza-END
    // ProyectoAFEPuesto-START
    public static final String PROYECTO_AFEPUESTO_LIST = "proyectoAFEPuestoList";
    public static final String PROYECTO_AFEPUESTO_KEY = "proyectoAFEPuestoForm";
    // ProyectoAFEPuesto-END
    // Reportes Colportores
    public static final String DETALLADO_DOCUMENTOS_COLPORTORES_LIST = "detalladoDocumentosColportoresList";
    public static final String CONCENTRADO_FINANCIERO_COLPORTORES_LIST = "concentradoFinancieroColportoresList";
    public static final String CONCENTRADO_ASOCIADOS_LIST = "concentradoAsociadosList";
    // InformeAFEHoras-START
    /**
     * The request scope attribute that holds the informeAFEHoras form.
     */
    public static final String INFORMEAFEHORAS_KEY = "informeAFEHorasForm";
    public static final String INFORMEAFEHORAS = "informeAFEHoras";
    /**
     * The request scope attribute that holds the informeAFEHoras list
     */
    public static final String INFORMEAFEHORAS_LIST = "informeAFEHorasList";
    public static final String CENTROCOSTO_AFE_INFORMEHORAS = "ccostoIH";
    // InformeAFEHoras-END
    // Categoria-START
    /**
     * The request scope attribute that holds the categoria form.
     */
    public static final String CATEGORIA_KEY = "categoriaForm";
    /**
     * The request scope attribute that holds the categoria list
     */
    public static final String CATEGORIA_LIST = "categoriaList";
    // Categoria-END
    // AFEPeriodo-START
    /**
     * The request scope attribute that holds the aFEPeriodo form.
     */
    public static final String AFEPERIODO_KEY = "AFEPeriodoForm";
    /**
     * The request scope attribute that holds the aFEPeriodo list
     */
    public static final String AFEPERIODO_LIST = "AFEPeriodoList";
    // AFEPeriodo-END
    // AFEPeriodoInforme-START
    /**
     * The request scope attribute that holds the AFEPeriodoInforme form.
     */
    public static final String AFEPERIODOINFORME_KEY = "AFEPeriodoInformeForm";
    /**
     * The request scope attribute that holds the AFEPeriodoInforme list
     */
    public static final String AFEPERIODOINFORME_LIST = "AFEPeriodoInformeList";
    // AFEPeriodoInforme-END
    // TemporadaColportaje-START
    /**
     * The request scope attribute that holds the temporadaColportaje form.
     */
    public static final String TEMPORADACOLPORTAJE_KEY = "temporadaColportajeForm";
    /**
     * The request scope attribute that holds the temporadaColportaje list
     */
    public static final String TEMPORADACOLPORTAJE_LIST = "temporadaColportajeList";
    public static final String TEMPORADASCOLPORTAJE_ACTUALES = "TA";
    // TemporadaColportaje-END
    // TemporadaColportor-START
    /**
     * The request scope attribute that holds the temporadaColportor form.
     */
    public static final String TEMPORADACOLPORTOR_KEY = "temporadaColportorForm";
    /**
     * The request scope attribute that holds the temporadaColportor list
     */
    public static final String TEMPORADACOLPORTOR_LIST = "temporadaColportorList";
    // TemporadaColportor-END
    // ContratoFinanciero-START
    /**
     * The request scope attribute that holds the contratoFinanciero form.
     */
    public static final String CONTRATOFINANCIERO_KEY = "contratoFinancieroForm";
    /**
     * The request scope attribute that holds the contratoFinanciero list
     */
    public static final String CONTRATOFINANCIERO_LIST = "contratoFinancieroList";
    public static final String CONTRATOFINANCIERO_COLCHON = "contratoFinancieroColchon";
    // ContratoFinanciero-END
    // SolicitudEstudios-START
    /**
     * The request scope attribute that holds the solicitudEstudios form.
     */
    public static final String SOLICITUDESTUDIOS_KEY = "solicitudEstudiosForm";
    /**
     * The request scope attribute that holds the solicitudEstudios list
     */
    public static final String SOLICITUDESTUDIOS_LIST = "solicitudEstudiosList";
    // SolicitudEstudios-END
    /**
     * The request scope attribute that holds the asociadoColportor list.
     */
    public static final String ASOCIADOCOLPORTOR_LIST = "asociadoColportorList";
    /**
     * The request scope attribute that holds the asociadoColportor form.
     */
    public static final String ASOCIADOCOLPORTOR_KEY = "asociadoColportorForm";
    public static final String ASOCIADO_COLPORTOR = "asociadoColportor";
    // RHJefe-START
    /**
     * The request scope attribute that holds the rHJefe form.
     */
    public static final String RHJEFE_KEY = "RHJefeForm";
    /**
     * The request scope attribute that holds the rHJefe list
     */
    public static final String RHJEFE_LIST = "RHJefeList";
    public static final String RHJEFE_SUBORDINADOS_LIST = "RHJefeSubordinadosList";
    public static final String RHJEFE = "RHJefe";
    // RHJefe-END
    // HojaServicio-START
    /**
     * The request scope attribute that holds the hojaServicio form.
     */
    public static final String HOJASERVICIO_KEY = "hojaServicioForm";
    /**
     * The request scope attribute that holds the hojaServicio list
     */
    public static final String HOJASERVICIO_LIST = "hojaServicioList";
    // HojaServicio-END
    // AlumnoColportorDocumento-START
    /**
     * The request scope attribute that holds the alumnoColportorDocumento form.
     */
    public static final String ALUMNOCOLPORTORDOCUMENTO_KEY = "alumnoColportorDocumentoForm";
    /**
     * The request scope attribute that holds the alumnoColportorDocumento list
     */
    public static final String ALUMNOCOLPORTORDOCUMENTO_LIST = "alumnoColportorDocumentoList";
    // AlumnoColportorDocumento-END
    // ConfigDeposito - START
    public static final String CONFIGDEPOSITO_LIST = "configDepositoList";
    // ConfigDeposito - END
    // TipoOtraBeca - START
    public static final String TIPOOTRABECA_LIST = "tipoOtraBecaList";
    // TipoOtraBeca - END
    // TipoBecaPlaza - START
    public static final String TIPOBECAPLAZA_LIST = "tipoBecaPlazaList";
    public static final String TIPOBECAPLAZA_BECABASICA = "bb";
    public static final String TIPOBECAPLAZA_OTRABECA = "ob";
    public static final String TIPOBECAPLAZA_UNKNOWN = "all";
    // TipoBecaPlaza - END
    // Union-START
    /**
     * The request scope attribute that holds the union form.
     */
    public static final String UNION_KEY = "unionForm";
    /**
     * The request scope attribute that holds the union list
     */
    public static final String UNION_LIST = "unionList";
    // Union-END
    // CAJA-START
    public static final String XML_CAJA = "Caja";
    public static final String XML_ACREDITARA = "acreditarA";
    public static final String XML_RECIBIMOS_DE = "recibimosDe";
    public static final String XML_DOMICILIO = "domicilio";
    public static final String XML_RFC = "rfc";
    public static final String XML_FECHA = "fecha";
    public static final String XML_CANTIDAD_LETRA = "cantidadConLetra";
    public static final String XML_RECIBO_NO = "reciboNo";
    public static final String XML_FORMA_PAGO = "formaPago";
    public static final String XML_IMPORTE_SUBTOTAL = "importeSubTotal";
    public static final String XML_IMPORTE_TOTAL = "importeTotal";
    public static final String XML_IMPORTE_IVA = "importeIVA";
    public static final String XML_LEYENDA_IVA = "leyendaIVA";
    public static final String XML_NUMERO_RECIBO = "numRecibo";
    // Encabezados-START
    // ENCABEZADOS-END
    public static final String XML_CAJA_NODO = "Caja-Nodo";
    public static final String XML_CAJA_NODO_DESCRIPCION = "descripcion";
    public static final String XML_CAJA_NODO_CANTIDAD = "cantidad";
    public static final String UBICACION_JASPER = "/contabilidad/caja/reporteCajaUM.jasper";
    public static final String UBICACION_XML = "/contabilidad/caja/caja.xml";
    public static final String EXPRESION_DATASOURCE_JRXML = "/Caja/Caja-Nodo";
    // CHEQUE-END
    // Encabezados-START
    // ENCABEZADOS-END
    public static final String XML_CHEQUE = "Cheque";
    public static final String XML_CHEQUE_FECHA = "fecha";
    public static final String XML_CHEQUE_FOLIO = "folio";
    public static final String XML_CHEQUE_IMPORTE = "importe";
    public static final String XML_CHEQUE_IMPORTE_LETRAS = "importeLetras";
    public static final String XML_CHEQUE_PAGUESE_A = "pagueseA";
    public static final String XML_CHEQUE_DESCRIPCION = "descripcion";
    public static final String XML_CHEQUE_LEYENDA = "leyenda";
    public static final String XML_CHEQUE_FORMATO_CHEQUE = "formatoCheque";
    public static final String XML_CHEQUE_NODO_NOMBRE = "Nodo-Movimiento";
    public static final String XML_CHEQUE_NODO_MOV = "movimiento";
    public static final String XML_CHEQUE_NODO_IMPORTE_C = "importeMovC";
    public static final String XML_CHEQUE_NODO_IMPORTE_D = "importeMovD";
    public static final String XML_CHEQUE_NODO_NATURALEZA = "naturaleza";
    public static final String UBICACION_JASPER_CHEQUE_BANORTE = "/contabilidad/cheque/reporteChequeBanorte.jasper";
    public static final String UBICACION_JASPER_CHEQUE_BANORTE_DOLARES = "/contabilidad/cheque/reporteChequeBanorteUSA.jasper";
    public static final String UBICACION_JASPER_CHEQUE_BANCOMER = "/contabilidad/cheque/reporteChequeBancomer.jasper";
    public static final String UBICACION_JASPER_CHEQUE_BANAMEX = "/contabilidad/cheque/reporteChequeBanamex.jasper";
    public static final String UBICACION_JASPER_CHEQUE_BANAMEX_DOLARES = "/contabilidad/cheque/reporteChequeBanamexUSA.jasper";
    public static final String UBICACION_JASPER_CHEQUE_SCOTIA = "/contabilidad/cheque/reporteChequeBanamexUSA.jasper";
    public static final String UBICACION_JASPER_CHEQUE_COMERCE_BANK = "/contabilidad/cheque/reporteChequeComerceBank.jasper";
    public static final String UBICACION_JASPER_CHEQUE_BANORTE_COVOPROM = "/contabilidad/cheque/reporteChequeBanorteCOVOPROM.jasper";
    public static final String UBICACION_JASPER_CHEQUE_SCOTIA_COVOPROM = "/contabilidad/cheque/reporteChequeScotiaCOVOPROM.jasper";
    public static final String UBICACION_XML_CHEQUE = "/contabilidad/cheque/cheque.xml";
    public static final String EXPRESION_DATASOURCE_JRXML_CHEQUE = "/Cheque/Nodo-Movimiento";
    // CAJA-END
    // MarcaVehiculo-START
    /**
     * The request scope attribute that holds the marcaVehiculo form.
     */
    public static final String MARCAVEHICULO_KEY = "marcaVehiculoForm";
    /**
     * The request scope attribute that holds the marcaVehiculo list
     */
    public static final String MARCAVEHICULO_LIST = "marcaVehiculoList";
    // MarcaVehiculo-END
    // ModeloVehiculo-START
    /**
     * The request scope attribute that holds the modeloVehiculo form.
     */
    public static final String MODELOVEHICULO_KEY = "modeloVehiculoForm";
    /**
     * The request scope attribute that holds the modeloVehiculo list
     */
    public static final String MODELOVEHICULO_LIST = "modeloVehiculoList";
    // ModeloVehiculo-END
    // SeguroAuto-START
    /**
     * The request scope attribute that holds the seguroAuto form.
     */
    public static final String SEGUROAUTO_KEY = "seguroAutoForm";
    /**
     * The request scope attribute that holds the seguroAuto list
     */
    public static final String SEGUROAUTO_LIST = "seguroAutoList";
    // SeguroAuto-END
    // TipoSalida-START
    public static final String TIPOSALIDA_LIST = "tipoSalidaList";
    // TipoSalida-END
    // Colegio-START
    /**
     * The request scope attribute that holds the colegio form.
     */
    public static final String COLEGIO_KEY = "colegioForm";
    /**
     * The request scope attribute that holds the colegio list
     */
    public static final String COLEGIO_LIST = "colegioList";
    // Colegio-END
    // ProcesoPolizas-START
    public static final String PROCESOPOLIZASTIPO_FORM = "procesoPolizasTipoForm";
    public static final String PROCESOPOLIZASTIPO_LIST = "procesoPolizasTipoList";
    // ProcesoPolizas-END
    // OperacionCaja-START
    /**
     * The request scope attribute that holds the operacionCaja form.
     */
    public static final String OPERACIONCAJA_KEY = "operacionCajaForm";
    /**
     * The request scope attribute that holds the operacionCaja list
     */
    public static final String OPERACIONCAJA_LIST = "operacionCajaList";
    // OperacionCaja-END
    // DescuentoEmpleado- START
    public static final String DESCUENTOEMPLEADO_LIST = "descuentoEmpleadoList";
    public static final String DESCUENTOEMPLEADO_KEY = "descuentoEmpleadoForm";
    public static final String TIPODESCUENTO_LIST = "tipoDescuentoList";
    public static final String UNIDADTIEMPO_LIST = "unidadTiempoList";
    // Consultas
    // DescuentoEmpleado - END
    // NominaParamGral- START
    public static final String NOMINA_PARAMGRAL_LIST = "nominaParamGralList";
    public static final String NOMINA_PARAMGRAL_PPTO_GASTOS = "nominaPptoGastos";
    public static final String NOMINA_PARAMGRAL_PPTO_AGUA = "nominaPptoAgua";
    // NominaParamGral - END
    /**
     * Valores para el constructor de Locale
     */
    /**
     * Formato (yyyy-MM-dd) de la fecha en el cual el mes se representa
     * numericamente.
     */
    /**
     * Formato (dd/MM/yyyy) de la fecha en el cual el mes se representa
     * numericamente
     */
    /**
     * Formato (dd/MM/yyyy hh:mm) de la fecha en el cual el mes se representa
     * numericamente incluyendo la hora:minutos:segundos am/pm
     */
    /**
     * Se utiliza para informar al usuario el formato de fecha esperado
     */
    /**
     * Formato (dd/MMM/yyyy) de la fecha en el cual el mes se representa en
     * palabra
     */
    /**
     * Formato (dd de MMMMM de yyyy) de la fecha en la cual el mes es completo
     */
    /**
     * Valores para el los containsKey
     */
    public static final String CONTAINSKEY_MAX = "max";
    public static final String CONTAINSKEY_PAGINA = "pagina";
    public static final String CONTAINSKEY_PAGINAS = "paginas";
    public static final String CONTAINSKEY_PAGINACION = "paginacion";
    public static final String CONTAINSKEY_OFFSET = "offset";
    public static final String CONTAINSKEY_FILTRO = "filtro";
    public static final String CONTAINSKEY_ORDER = "order";
    public static final String CONTAINSKEY_SORT = "sort";
    public static final String CONTAINSKEY_DESC = "desc";
    public static final String CONTAINSKEY_REPORTE = "reporte";
    public static final String CONTAINSKEY_CANTIDAD = "cantidad";
    public static final String CONTAINSKEY_MESSAGE = "message";
    public static final String CONTAINSKEY_MESSAGE_ATTRS = "messageAttrs";
    public static final String CONTAINSKEY_ORGANIZACION = "organizacion";
    /**
     * Valores para el los containsKey para las clases
     */
    public static final String CONTAINSKEY_MAYORES = "mayores";
    public static final String CONTAINSKEY_EMPLEADOS = "empleados";
    public static final String CONTAINSKEY_RESULTADOS = "resultados";
    public static final String CONTAINSKEY_AUXILIARES = "auxiliares";
    public static final String CONTAINSKEY_ASOCIACIONES = "asociaciones";
    public static final String CONTAINSKEY_LIBROS = "libros";
    /**
     * Valores para el los addAttribute para las clases
     */
    // Valores para el los addAttribute para las clases
    public static final String ADDATTRIBUTE_MAYOR = "mayor";
    public static final String ADDATTRIBUTE_EMPLEADO = "empleado";
    public static final String ADDATTRIBUTE_RESULTADO = "resultado";
    public static final String ADDATTRIBUTE_AUXILIAR = "auxiliar";
    public static final String ADDATTRIBUTE_LIBRO = "libro";
    public static final String ADDATTRIBUTE_UNION = "union";
    public static final String ADDATTRIBUTE_NOMBRE = "nombre";
    public static final String ADDATTRIBUTE_ASOCIACION = "asociacion";
    public static final String ADDATTRIBUTE_ASOCIADO = "asociado";
    /**
     * Valores para el los path's para las clases
     */
    public static final String PATH_CUENTA_MAYOR = "/contabilidad/mayor";
    public static final String PATH_CUENTA_MAYOR_LISTA = "/contabilidad/mayor/lista";
    public static final String PATH_CUENTA_MAYOR_VER = "/contabilidad/mayor/ver";
    public static final String PATH_CUENTA_MAYOR_NUEVA = "/contabilidad/mayor/nueva";
    public static final String PATH_CUENTA_MAYOR_EDITA = "/contabilidad/mayor/edita";
    public static final String PATH_CUENTA_MAYOR_CREA = "/contabilidad/mayor/crea";
    public static final String PATH_CUENTA_MAYOR_ACTUALIZA = "/contabilidad/mayor/actualiza";
    public static final String PATH_CUENTA_MAYOR_ELIMINA = "/contabilidad/mayor/elimina";
    public static final String PATH_EMPLEADO = "/rh/empleado";
    public static final String PATH_EMPLEADO_LISTA = "/rh/empleado/lista";
    public static final String PATH_EMPLEADO_VER = "/rh/empleado/ver";
    public static final String PATH_EMPLEADO_NUEVO = "/rh/empleado/nuevo";
    public static final String PATH_EMPLEADO_EDITA = "/rh/empleado/edita";
    public static final String PATH_EMPLEADO_CREA = "/rh/empleado/crea";
    public static final String PATH_EMPLEADO_ACTUALIZA = "/rh/empleado/actualiza";
    public static final String PATH_EMPLEADO_ELIMINA = "/rh/empleado/elimina";
    public static final String PATH_CUENTA_RESULTADO = "/contabilidad/resultado";
    public static final String PATH_CUENTA_RESULTADO_LISTA = "/contabilidad/resultado/lista";
    public static final String PATH_CUENTA_RESULTADO_VER = "/contabilidad/resultado/ver";
    public static final String PATH_CUENTA_RESULTADO_NUEVA = "/contabilidad/resultado/nueva";
    public static final String PATH_CUENTA_RESULTADO_EDITA = "/contabilidad/resultado/edita";
    public static final String PATH_CUENTA_RESULTADO_CREA = "/contabilidad/resultado/crea";
    public static final String PATH_CUENTA_RESULTADO_ACTUALIZA = "/contabilidad/resultado/actualiza";
    public static final String PATH_CUENTA_RESULTADO_ELIMINA = "/contabilidad/resultado/elimina";
    public static final String PATH_CUENTA_AUXILIAR = "/contabilidad/auxiliar";
    public static final String PATH_CUENTA_AUXILIAR_LISTA = "/contabilidad/auxiliar/lista";
    public static final String PATH_CUENTA_AUXILIAR_VER = "/contabilidad/auxiliar/ver";
    public static final String PATH_CUENTA_AUXILIAR_NUEVA = "/contabilidad/auxiliar/nueva";
    public static final String PATH_CUENTA_AUXILIAR_EDITA = "/contabilidad/auxiliar/edita";
    public static final String PATH_CUENTA_AUXILIAR_CREA = "/contabilidad/auxiliar/crea";
    public static final String PATH_CUENTA_AUXILIAR_ACTUALIZA = "/contabilidad/auxiliar/actualiza";
    public static final String PATH_CUENTA_AUXILIAR_ELIMINA = "/contabilidad/auxiliar/elimina";
    public static final String PATH_CUENTA_LIBRO = "/contabilidad/libro";
    public static final String PATH_CUENTA_LIBRO_LISTA = "/contabilidad/libro/lista";
    public static final String PATH_CUENTA_LIBRO_VER = "/contabilidad/libro/ver";
    public static final String PATH_CUENTA_LIBRO_NUEVA = "/contabilidad/libro/nueva";
    public static final String PATH_CUENTA_LIBRO_EDITA = "/contabilidad/libro/edita";
    public static final String PATH_CUENTA_LIBRO_CREA = "/contabilidad/libro/crea";
    public static final String PATH_CUENTA_LIBRO_ACTUALIZA = "/contabilidad/libro/actualiza";
    public static final String PATH_CUENTA_LIBRO_ELIMINA = "/contabilidad/libro/elimina";
    //Puesto-START
    /**
     * The request scope attribute that holds the puesto form.
     */
    public static final String PUESTO_FORM = "puestoForm";
    /**
     * The request scope attribute that holds the puesto list
     */
    //Puesto-END
    /**
     * Valores para el los containsKey para las clases
     */
    public static final String CONTAINSKEY_DEPENDIENTES = "dependientes";
    public static final String TIPOS_DEPENDIENTE = "tiposDependiente";
    public static final String CONTAINSKEY_ESTUDIOSEMPLEADO = "estudiosEmpleado";
    public static final String CONTAINSKEY_COLEGIOS = "colegios";
    public static final String CONTAINSKEY_SECCIONES = "secciones";
    public static final String CONTAINSKEY_NACIONALIDADES = "nacionalidades";
    public static final String CONTAINSKEY_CATEGORIAS = "categorias";
    public static final String CONTAINSKEY_CONCEPTOS = "conceptos";
    public static final String CONTAINSKEY_PERDED = "perdeds";
    public static final String CONTAINSKEY_ASOCIADOS = "asociados";
    public static final String CONTAINSKEY_CIUDADES = "ciudades";
    public static final String CONTAINSKEY_COLPORTORES = "colportores";
    public static final String CONTAINSKEY_DOCUMENTOS = "documentos";
    public static final String CONTAINSKEY_ESTADOS = "estados";
    public static final String CONTAINSKEY_PAISES = "paises";
    public static final String CONTAINSKEY_TEMPORADACOLPORTORES = "temporadaColportores";
    public static final String CONTAINSKEY_TEMPORADAS = "temporadas";
    public static final String CONTAINSKEY_UNIONES = "uniones";
    public static final String CONTAINSKEY_USUARIOS = "usuarios";
    public static final String CONTAINSKEY_COLEGIOS_COLPORTOR = "colegios";
    public static final String CONTAINSKEY_TIPOSBECAS= "tiposBecas";
    public static final String CONTAINSKEY_AFECONVENIO= "afeConvenios";
    /**
     * Valores para el los addAttribute para las clases
     */
    public static final String ADDATTRIBUTE_DEPENDIENTE = "dependiente";
    public static final String ADDATTRIBUTE_ESTUDIOSEMPLEADO = "estudioEmpleado";
    public static final String ADDATTRIBUTE_SECCION = "seccion";
    public static final String ADDATTRIBUTE_STATUS = "status";
    public static final String ADDATTRIBUTE_COLEGIO = "colegio";
    public static final String ADDATTRIBUTE_CONCEPTO = "concepto";
    public static final String ADDATTRIBUTE_CATEGORIA = "categoria";
    public static final String ADDATTRIBUTE_NACIONALIDAD = "nacionalidad";
    public static final String ADDATTRIBUTE_PERDED = "perded";
    /*
     * Valores para el los path's para las clases
     */
    public static final String PATH_DEPENDIENTE = "/rh/dependiente";
    public static final String PATH_DEPENDIENTE_LISTA = "/rh/dependiente/lista";
    public static final String PATH_DEPENDIENTE_VER = "/rh/dependiente/ver";
    public static final String PATH_DEPENDIENTE_NUEVO = "/rh/dependiente/nuevo";
    public static final String PATH_DEPENDIENTE_EDITA = "/rh/dependiente/edita";
    public static final String PATH_DEPENDIENTE_CREA = "/rh/dependiente/crea";
    public static final String PATH_DEPENDIENTE_ACTUALIZA = "/rh/dependiente/actualiza";
    public static final String PATH_DEPENDIENTE_ELIMINA = "/rh/dependiente/elimina";
    public static final String PATH_ESTUDIOSEMPLEADO = "/rh/estudiosEmpleado";
    public static final String PATH_ESTUDIOSEMPLEADO_LISTA = "/rh/estudiosEmpleado/lista";
    public static final String PATH_ESTUDIOSEMPLEADO_VER = "/rh/estudiosEmpleado/ver";
    public static final String PATH_ESTUDIOSEMPLEADO_NUEVO = "/rh/estudiosEmpleado/nuevo";
    public static final String PATH_ESTUDIOSEMPLEADO_EDITA = "/rh/estudiosEmpleado/edita";
    public static final String PATH_ESTUDIOSEMPLEADO_CREA = "/rh/estudiosEmpleado/crea";
    public static final String PATH_ESTUDIOSEMPLEADO_ACTUALIZA = "/rh/estudiosEmpleado/actualiza";
    public static final String PATH_ESTUDIOSEMPLEADO_ELIMINA = "/rh/estudiosEmpleado/elimina";
    public static final String PATH_COLEGIO = "/rh/colegio";
    public static final String PATH_COLEGIO_LISTA = "/rh/colegio/lista";
    public static final String PATH_COLEGIO_VER = "/rh/colegio/ver";
    public static final String PATH_COLEGIO_NUEVO = "/rh/colegio/nuevo";
    public static final String PATH_COLEGIO_EDITA = "/rh/colegio/edita";
    public static final String PATH_COLEGIO_CREA = "/rh/colegio/crea";
    public static final String PATH_COLEGIO_ACTUALIZA = "/rh/colegio/actualiza";
    public static final String PATH_COLEGIO_ELIMINA = "/rh/colegio/elimina";
    public static final String PATH_SECCION = "/rh/seccion";
    public static final String PATH_SECCION_LISTA = "/rh/seccion/lista";
    public static final String PATH_SECCION_VER = "/rh/seccion/ver";
    public static final String PATH_SECCION_NUEVO = "/rh/seccion/nueva";
    public static final String PATH_SECCION_EDITA = "/rh/seccion/edita";
    public static final String PATH_SECCION_CREA = "/rh/seccion/crea";
    public static final String PATH_SECCION_ACTUALIZA = "/rh/seccion/actualiza";
    public static final String PATH_SECCION_ELIMINA = "/rh/colegio/elimina";
    public static final String PATH_CATEGORIA = "/rh/categoria";
    public static final String PATH_CATEGORIA_LISTA = "/rh/categoria/lista";
    public static final String PATH_CATEGORIA_VER = "/rh/categoria/ver";
    public static final String PATH_CATEGORIA_NUEVO = "/rh/categoria/nuevo";
    public static final String PATH_CATEGORIA_EDITA = "/rh/categoria/edita";
    public static final String PATH_CATEGORIA_CREA = "/rh/categoria/crea";
    public static final String PATH_CATEGORIA_ACTUALIZA = "/rh/categoria/actualiza";
    public static final String PATH_CATEGORIA_ELIMINA = "/rh/categoria/elimina";
    public static final String CATEGORIA_FORM = "categoriaForm";
    public static final String NACIONALIDAD_LIST = "nacionalidadList";
    public static final String PATH_NACIONALIDAD = "/rh/nacionalidad";
    public static final String PATH_NACIONALIDAD_LISTA = "/rh/nacionalidad/lista";
    public static final String PATH_NACIONALIDAD_VER = "/rh/nacionalidad/ver";
    public static final String PATH_NACIONALIDAD_NUEVO = "/rh/nacionalidad/nuevo";
    public static final String PATH_NACIONALIDAD_EDITA = "/rh/nacionalidad/edita";
    public static final String PATH_NACIONALIDAD_CREA = "/rh/nacionalidad/crea";
    public static final String PATH_NACIONALIDAD_ACTUALIZA = "/rh/nacionalidad/actualiza";
    public static final String PATH_NACIONALIDAD_ELIMINA = "/rh/nacionalidad/elimina";
    public static final String NACIONALIDAD_FORM = "nacionalidadForm";
    /**
     * Valores para los conceptos
     */
    public static final String CONCEPTO_KEY = "concepto";
    public static final String PATH_CONCEPTO = "/rh/concepto";
    public static final String PATH_CONCEPTO_LISTA = "rh/concepto/lista";
    public static final String PATH_CONCEPTO_VER = "/rh/concepto/ver";
    public static final String PATH_CONCEPTO_NUEVO = "/rh/concepto/nuevo";
    public static final String PATH_CONCEPTO_EDITA = "/rh/concepto/edita";
    public static final String PATH_CONCEPTO_CREA = "/rh/concepto/crea";
    public static final String PATH_CONCEPTO_ACTUALIZA = "/rh/concepto/actualiza";
    public static final String PATH_CONCEPTO_ELIMINA = "/rh/concepto/elimina";
    public static final String PATH_PERDED = "/rh/perded";
    public static final String PATH_PERDED_LISTA = "/rh/perded/lista";
    public static final String PATH_PERDED_VER = "/rh/perded/ver";
    public static final String PATH_PERDED_NUEVO = "/rh/perded/nuevo";
    public static final String PATH_PERDED_EDITA = "/rh/perded/edita";
    public static final String PATH_PERDED_GRABA = "/rh/perded/graba";
    /**
     * Valores para los Asociacion Colportor
     */
    public static final String PATH_ASOCIACION = "/admin/asociacion";
    public static final String PATH_ASOCIACION_LISTA = "/admin/asociacion/lista";
    public static final String PATH_ASOCIACION_VER = "/admin/asociacion/ver";
    public static final String PATH_ASOCIACION_NUEVA = "/admin/asociacion/nueva";
    public static final String PATH_ASOCIACION_EDITA = "/admin/asociacion/edita";
    public static final String PATH_ASOCIACION_CREA = "/admin/asociacion/crea";
    public static final String PATH_ASOCIACION_ACTUALIZA = "/admin/asociacion/actualiza";
    public static final String PATH_ASOCIACION_ELIMINA = "/admin/asociacion/elimina";
    /**
     * Variable en session donde se guarda la Temporada Colportor activa del
     * colportor en caso que el colportor no tenga una temporada activa el valor
     * de esta sera null
     */
    public static final String UNI = "UNI";
    public static final String ASO = "ASO";
    /**
     * Valores para las Uniones Colportor
     */
    public static final String PATH_UNION = "/admin/union";
    public static final String PATH_UNION_LISTA = "admin/union/lista";
    public static final String PATH_UNION_VER = "/admin/union/ver";
    public static final String PATH_UNION_NUEVA = "admin/union/nueva";
    public static final String PATH_UNION_EDITA = "admin/union/edita";
    public static final String PATH_UNION_CREA = "/admin/union/crea";
    public static final String PATH_UNION_ACTUALIZA = "/admin/union/actualiza";
    public static final String PATH_UNION_ELIMINA = "/admin/union/elimina";
    /**
     * Valores para los Asociados Colportor
     */
    public static final String PATH_ASOCIADO = "/asociado";
    public static final String PATH_ASOCIADO_LISTA = "/asociado/lista";
    public static final String PATH_ASOCIADO_VER = "/asociado/ver";
    public static final String PATH_ASOCIADO_NUEVO = "/asociado/nuevo";
    public static final String PATH_ASOCIADO_EDITA = "/asociado/edita";
    public static final String PATH_ASOCIADO_CREA = "/asociado/crea";
    public static final String PATH_ASOCIADO_ACTUALIZA = "/asociado/actualiza";
    public static final String PATH_ASOCIADO_ELIMINA = "/asociado/elimina";
    /**
     * Valores para el los containsKey
     *
     * ROLES *****
     *
     *
     */
    public static final String ROLES = "roles";
    /**
     * Valores para los Colportores Colportor
     */
    public static final String PATH_COLPORTOR = "/colportor";
    public static final String PATH_COLPORTOR_LISTA = "colportor/lista";
    public static final String PATH_COLPORTOR_VER = "/colportor/ver";
    public static final String PATH_COLPORTOR_NUEVO = "colportor/nuevo";
    public static final String PATH_COLPORTOR_EDITA = "colportor/edita";
    public static final String PATH_COLPORTOR_CREA = "/colportor/crea";
    public static final String PATH_COLPORTOR_ACTUALIZA = "/colportor/actualiza";
    public static final String PATH_COLPORTOR_ELIMINA = "/colportor/elimina";
    public static final String ADDATTRIBUTE_COLPORTOR = "colportor";
    /**
     * Valores para los Tipo Colportor Colportor
     */
    public static final String TIPO_COLPORTOR = "1";
    public static final String MATRICULA = "1070980";
    /**
     * Valores para los Temporada Colportor
     */
    public static final String PATH_TEMPORADA = "/temporada";
    public static final String PATH_TEMPORADA_LISTA = "/temporada/lista";
    public static final String PATH_TEMPORADA_VER = "/temporada/ver";
    public static final String PATH_TEMPORADA_NUEVA = "/temporada/nueva";
    public static final String PATH_TEMPORADA_EDITA = "/temporada/edita";
    public static final String PATH_TEMPORADA_CREA = "/temporada/crea";
    public static final String PATH_TEMPORADA_ACTUALIZA = "/temporada/actualiza";
    public static final String PATH_TEMPORADA_ELIMINA = "/temporada/elimina";
    public static final String ADDATTRIBUTE_TEMPORADA = "temporada";
    /**
     * Valores para la exportacion Colportor
     */
    public static final String TIPO_DOCUMENTO_PDF = "PDF";
    public static final String TIPO_DOCUMENTO_CSV = "CSV";
    public static final String TIPO_DOCUMENTO_XLS = "XLS";
    /**
     * Valores para Colegio Colportor
     */
    public static final String PATH_COLEGIO_COLPORTOR = "/colegio";
    public static final String PATH_COLEGIO_COLPORTOR_LISTA = "colegio/lista";
    public static final String PATH_COLEGIO_COLPORTOR_VER = "/colegio/ver";
    public static final String PATH_COLEGIO_COLPORTOR_NUEVO = "colegio/nuevo";
    public static final String PATH_COLEGIO_COLPORTOR_EDITA = "colegio/edita";
    public static final String PATH_COLEGIO_COLPORTOR_CREA = "/colegio/crea";
    public static final String PATH_COLEGIO_COLPORTOR_ACTUALIZA = "/colegio/actualiza";
    public static final String PATH_COLEGIO_COLPORTOR_ELIMINA = "/colegio/elimina";
    /**
     * Valores para Ciudad Colportor
     */
    public static final String PATH_CIUDAD = "/ciudad";
    public static final String PATH_CIUDAD_LISTA = "ciudad/lista";
    public static final String PATH_CIUDAD_VER = "/ciudad/ver";
    public static final String PATH_CIUDAD_NUEVA = "/ciudad/nueva";
    public static final String PATH_CIUDAD_EDITA = "/ciudad/edita";
    public static final String PATH_CIUDAD_CREA = "/ciudad/crea";
    public static final String PATH_CIUDAD_ACTUALIZA = "/ciudad/actualiza";
    public static final String PATH_CIUDAD_ELIMINA = "/ciudad/elimina";
    public static final String ADDATTRIBUTE_CIUDAD = "ciudad";
    /**
     * Valores para Estado Colportor
     */
    public static final String PATH_ESTADO = "/estado";
    public static final String PATH_ESTADO_LISTA = "estado/lista";
    public static final String PATH_ESTADO_VER = "/estado/ver";
    public static final String PATH_ESTADO_NUEVA = "/estado/nueva";
    public static final String PATH_ESTADO_EDITA = "/estado/edita";
    public static final String PATH_ESTADO_CREA = "/estado/crea";
    public static final String PATH_ESTADO_ACTUALIZA = "/estado/actualiza";
    public static final String PATH_ESTADO_ELIMINA = "/estado/elimina";
    public static final String ADDATTRIBUTE_ESTADO = "estado";
    /**
     * Valores para Pais Colportor
     */
    public static final String PATH_PAIS = "/pais";
    public static final String PATH_PAIS_LISTA = "pais/lista";
    public static final String PATH_PAIS_VER = "/pais/ver";
    public static final String PATH_PAIS_NUEVA = "/pais/nueva";
    public static final String PATH_PAIS_EDITA = "/pais/edita";
    public static final String PATH_PAIS_CREA = "/pais/crea";
    public static final String PATH_PAIS_ACTUALIZA = "/pais/actualiza";
    public static final String PATH_PAIS_ELIMINA = "/pais/elimina";
    public static final String ADDATTRIBUTE_PAIS = "pais";
    /**
     * Valores para Temporada Colportor Colportor
     */
    public static final String PATH_TEMPORADACOLPORTOR = "/temporadaColportor";
    public static final String PATH_TEMPORADACOLPORTOR_LISTA = "/temporadaColportor/lista";
    public static final String PATH_TEMPORADACOLPORTOR_VER = "/temporadaColportor/ver";
    public static final String PATH_TEMPORADACOLPORTOR_NUEVA = "/temporadaColportor/nueva";
    public static final String PATH_TEMPORADACOLPORTOR_EDITA = "/temporadaColportor/edita";
    public static final String PATH_TEMPORADACOLPORTOR_CREA = "/temporadaColportor/crea";
    public static final String PATH_TEMPORADACOLPORTOR_ACTUALIZA = "/temporadaColportor/actualiza";
    public static final String PATH_TEMPORADACOLPORTOR_ELIMINA = "/temporadaColportor/elimina";
    public static final String ADDATTRIBUTE_TEMPORADACOLPORTOR = "temporadaColportor";
    /**
     * Valores para Documentos
     */
    public static final String PATH_DOCUMENTO = "/documento";
    public static final String PATH_DOCUMENTO_LISTA = "/documento/lista";
    public static final String PATH_DOCUMENTO_VER = "/documento/ver";
    public static final String PATH_DOCUMENTO_NUEVO = "/documento/nuevo";
    public static final String PATH_DOCUMENTO_EDITA = "/documento/edita";
    public static final String PATH_DOCUMENTO_CREA = "/documento/crea";
    public static final String PATH_DOCUMENTO_ACTUALIZA = "/documento/actualiza";
    public static final String PATH_DOCUMENTO_ELIMINA = "/documento/elimina";
    public static final String ADDATTRIBUTE_DOCUMENTO = "documento";
    /**
     * Valores para Tipos de Documento
     *
     */
    public static final String BOLETIN = "Boletín";
    public static final String DIEZMO = "Diezmo";
    public static final String DEPOSITO_CAJA = "Deposito_Caja";
    public static final String DEPOSITO_BANCO = "Deposito_Banco";
    public static final String NOTAS_DE_COMPRA = "Notas_De_Compra";
    public static final String INFORME = "Informe";
    /**
     * Valores Totales para Tipos de Documento
     *
     */
    public static final String TOTALBOLETIN = "Total_Boletin";
    public static final String TOTALDIEZMOS = "Total_Diezmos";
    public static final String TOTALDEPOSITOS = "Total_Depositos";
    /**
     * Valores para Tabla de Porcentajes
     *
     */
    public static final String OBJETIVO = "Objetivo";
    public static final String ALCANZADO = "Alcanzado";
    public static final String FIDELIDAD = "Fidelidad";
    
    /**
     * Valores para Tabla de Porcentajes
     *
     */
    
    public static final String PATH_TIPOSBECAS = "/inscripciones/tiposBecas";
    public static final String PATH_TIPOSBECAS_LISTA = "/inscripciones/tiposBecas/lista";
    public static final String PATH_TIPOSBECAS_VER = "/inscripciones/tiposBecas/ver";
    public static final String PATH_TIPOSBECAS_NUEVO = "/inscripciones/tiposBecas/nuevo";
    public static final String PATH_TIPOSBECAS_EDITA = "/inscripciones/tiposBecas/edita";
    public static final String PATH_TIPOSBECAS_GRABA = "/inscripciones/tiposBecas/graba";
    public static final String PATH_TIPOSBECAS_ACTUALIZA = "/inscripciones/tiposBecas/actualiza";
    public static final String PATH_TIPOSBECAS_ELIMINA = "/inscripciones/tiposBecas/elimina";
    public static final String ADDATTRIBUTE_TIPOSBECAS = "tipoBeca";  
    
    
    public static final String PATH_AFECONVENIO = "/inscripciones/afeConvenio";
    public static final String PATH_AFECONVENIO_LISTA = "/inscripciones/afeConvenio/lista";
    public static final String PATH_AFECONVENIO_VER = "/inscripciones/afeConvenio/ver";
    public static final String PATH_AFECONVENIO_NUEVO = "/inscripciones/afeConvenio/nuevo";
    public static final String PATH_AFECONVENIO_EDITA = "/inscripciones/afeConvenio/edita";
    public static final String PATH_AFECONVENIO_GRABA = "/inscripciones/afeConvenio/graba";
    public static final String PATH_AFECONVENIO_ACTUALIZA = "/inscripciones/afeConvenio/actualiza";
    public static final String PATH_AFECONVENIO_ELIMINA = "/inscripciones/afeConvenio/elimina";
    public static final String ADDATTRIBUTE_AFECONVENIO = "afeConvenio";  
}
