/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo;

import java.math.BigDecimal;

/**
 *
 * @author nujev
 */
public class Constantes {

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
	public static final String STATUS_ACTIVO = "A";
	public static final String STATUS_INACTIVO = "I";
	public static final String DECIMAL_PATTERN = "###,###,###,##0.00";
	public static final String CURRENCY_PATTERN = "$###,###,##0.00";
	public static final String PERCENTAGE_PATTERN = "#.00%";
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
        public static final String PUESTO_KEY = "puestoForm";

        /**
         * The request scope attribute that holds the puesto list
         */
        public static final String PUESTO_LIST = "puestoList";
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

    /**
     * Valores para el los addAttribute para las clases
     */
    public static final String ADDATTRIBUTE_DEPENDIENTE = "dependiente";
    public static final String ADDATTRIBUTE_ESTUDIOSEMPLEADO = "estudioEmpleado";
    public static final String ADDATTRIBUTE_SECCION = "seccion";

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
    

    public static final String CATEGORIA_LIST = "categoriaList";
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
    public static final String CONCEPTO_LIST = "conceptoList";
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
    
    
    
    //Colportores Constantes
    //
   //    Session

    /**
     * Variable en session donde se guarda la Union Actual de Usuario
     */
    public static final String SESSION_UNION = "union";
    /**
     * Variable en session donde se guarda la Asociacion Actual de Usuario
     */
    public static final String SESSION_ASOCIACION = "asociacion";
    /**
     * Variable en session donde se guarda la Temporada Colportor activa del
     * colportor en caso que el colportor no tenga una temporada activa el valor
     * de esta sera null
     */
    public static final String SESSION_TEMPORADA_COLPORTOR = "temporadaColportor";
    public static final String ADMIN = "ADMIN";
    public static final String UNI = "UNI";
    public static final String ASO = "ASO";
    public static final String NOMBRE = "test";
    public static final String CLAVE = "000";
    public static final String DIRECCION = "testd";
    public static final String CORREO = "test@tes.tst";
    public static final String TELEFONO = "1234567890";
    public static final String TIPO_DOCUMENTO = "A";
    public static final String TIPO_COLPORTOR = "1";
    public static final String FOLIO = "test";
    public static final String FECHA = "dd/MM/yyyy";
    public static final BigDecimal IMPORTE = new BigDecimal("0.0");
    public static final String OBSERVACIONES = "test teste";
    public static final String CALLE = "test";
    public static final String COLONIA = "test";
    public static final String MUNICIPIO = "testmu";
    public static final String MATRICULA = "1070980";
    public static final String DEPOSITO_CAJA = "Deposito_Caja";
    public static final String DEPOSITO_BANCO = "Deposito_Banco";
    public static final String DIEZMO = "Diezmo";
    public static final String NOTAS_DE_COMPRA = "Notas_De_Compra";
    public static final String BOLETIN = "Boletín";
    public static final String INFORME = "Informe";
    public static final String TOTALBOLETIN = "Total_Boletin";
    public static final String TOTALDIEZMOS = "Total_Diezmos";
    public static final String TOTALDEPOSITOS = "Total_Depositos";
    public static final String ALCANZADO = "Alcanzado";
    public static final String OBJETIVO = "Objetivo";
    public static final String FIDELIDAD = "Fidelidad";
    public static final String TIEMPO_COMPLETO = "Tiempo_Completo";
    public static final String TIEMPO_PARCIAL = "Tiempo_Parcial";
    public static final String ESTUDIANTE = "Estudiante";
   
    /**
     * Valores para el los containsKey
     * 
     * ROLES *****
     
     
     */
    public static final String ROL_ADMINISTRADOR = "ROLE_ADMIN";
    public static final String ROLES = "roles";
    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ASO = "ROLE_ASO";
    public static final String ROLE_COL = "ROLE_COL";
    public static final String ROLE_TEST = "ROLE_TEST";
    
    public static final String UNION_ID = "unionId";
    public static final String ASOCIACION_ID = "asociacionId";
    public static final String OPEN_ID = "openId";
    public static final String ALMACEN_ID = "almacenId";
    
    public static final String TIPO_DOCUMENTO_PDF = "PDF";
    public static final String TIPO_DOCUMENTO_CSV = "CSV";
    public static final String TIPO_DOCUMENTO_XLS = "XLS";
    
     /**
     * Valores para el los containsKey para las clases
     */
    public static final String CONTAINSKEY_UNIONES = "uniones";
    public static final String CONTAINSKEY_COLPORTORES = "colportores";
    public static final String CONTAINSKEY_DOCUMENTOS = "documentos";
    public static final String CONTAINSKEY_COLEGIOS_COLPORTOR = "colegios";
    public static final String CONTAINSKEY_CLIENTES = "clientes";
    public static final String CONTAINSKEY_ASOCIACIONES = "asociaciones";
    public static final String CONTAINSKEY_ASOCIADOS = "asociados";
    public static final String CONTAINSKEY_TEMPORADAS = "temporadas";
    public static final String CONTAINSKEY_TEMPORADACOLPORTORES = "temporadaColportores";
    public static final String CONTAINSKEY_ALMACENES = "almacenes";
    public static final String CONTAINSKEY_PAISES = "paises";
    public static final String CONTAINSKEY_ESTADOS = "estados";
    public static final String CONTAINSKEY_CIUDADES = "ciudades";
    public static final String CONTAINSKEY_USUARIOS = "usuarios";
    public static final String CONTAINSKEY_TEMPORADACOLPORTOR = "temporadaColportor";
    /**
     * Valores para el los addAttribute para las clases
     */
    public static final String ADDATTRIBUTE_USUARIO = "usuario";
    public static final String ADDATTRIBUTE_UNION = "union";
    public static final String ADDATTRIBUTE_COLPORTOR = "colportor";
    public static final String ADDATTRIBUTE_DOCUMENTO = "documento";
    public static final String ADDATTRIBUTE_COLEGIO_COLPORTOR = "colegio";
    public static final String ADDATTRIBUTE_CLIENTE = "cliente";
    public static final String ADDATTRIBUTE_ASOCIACION = "asociacion";
    public static final String ADDATTRIBUTE_ASOCIADO = "asociado";
    public static final String ADDATTRIBUTE_TEMPORADA = "temporada";
    public static final String ADDATTRIBUTE_TEMPORADACOLPORTOR = "temporadaColportor";
    public static final String ADDATTRIBUTE_ALMACEN = "almacen";
    public static final String ADDATTRIBUTE_PAIS = "pais";
    public static final String ADDATTRIBUTE_ESTADO = "estado";
    public static final String ADDATTRIBUTE_CIUDAD = "ciudad";
    public static final String ADDATTRIBUTE_NOMBRE = "nombre";
    public static final String ADDATTRIBUTE_CLAVE = "clave";
    public static final String ADDATTRIBUTE_STATUS = "status";

    
    /**
     * Valores para el los path's para las clases
     */
    public static final String PATH_UNION = "/admin/union";
    public static final String PATH_UNION_LISTA = "admin/union/lista";
    public static final String PATH_UNION_VER = "/admin/union/ver";
    public static final String PATH_UNION_NUEVA = "admin/union/nueva";
    public static final String PATH_UNION_EDITA = "admin/union/edita";
    public static final String PATH_UNION_CREA = "/admin/union/crea";
    public static final String PATH_UNION_ACTUALIZA = "/admin/union/actualiza";
    public static final String PATH_UNION_ELIMINA = "/admin/union/elimina";
    public static final String PATH_ASOCIACION = "/admin/asociacion";
    public static final String PATH_ASOCIACION_LISTA = "/admin/asociacion/lista";
    public static final String PATH_ASOCIACION_VER = "/admin/asociacion/ver";
    public static final String PATH_ASOCIACION_NUEVA = "/admin/asociacion/nueva";
    public static final String PATH_ASOCIACION_EDITA = "/admin/asociacion/edita";
    public static final String PATH_ASOCIACION_CREA = "/admin/asociacion/crea";
    public static final String PATH_ASOCIACION_ACTUALIZA = "/admin/asociacion/actualiza";
    public static final String PATH_ASOCIACION_ELIMINA = "/admin/asociacion/elimina";
    public static final String PATH_COLPORTOR = "/colportor";
    public static final String PATH_COLPORTOR_LISTA = "colportor/lista";
    public static final String PATH_COLPORTOR_VER = "/colportor/ver";
    public static final String PATH_COLPORTOR_NUEVO = "colportor/nuevo";
    public static final String PATH_COLPORTOR_EDITA = "colportor/edita";
    public static final String PATH_COLPORTOR_CREA = "/colportor/crea";
    public static final String PATH_COLPORTOR_ACTUALIZA = "/colportor/actualiza";
    public static final String PATH_COLPORTOR_ELIMINA = "/colportor/elimina";
    public static final String PATH_DOCUMENTO = "/documento";
    public static final String PATH_DOCUMENTO_LISTA = "/documento/lista";
    public static final String PATH_DOCUMENTO_VER = "/documento/ver";
    public static final String PATH_DOCUMENTO_NUEVO = "/documento/nuevo";
    public static final String PATH_DOCUMENTO_EDITA = "/documento/edita";
    public static final String PATH_DOCUMENTO_CREA = "/documento/crea";
    public static final String PATH_DOCUMENTO_ACTUALIZA = "/documento/actualiza";
    public static final String PATH_DOCUMENTO_ELIMINA = "/documento/elimina";
    public static final String PATH_COLEGIO_COLPORTOR = "/colegio";
    public static final String PATH_COLEGIO_LISTA_COLPORTOR = "colegio/lista";
    public static final String PATH_COLEGIO_VER_COLPORTOR = "/colegio/ver";
    public static final String PATH_COLEGIO_NUEVO_COLPORTOR = "colegio/nuevo";
    public static final String PATH_COLEGIO_EDITA_COLPORTOR = "colegio/edita";
    public static final String PATH_COLEGIO_CREA_COLPORTOR = "/colegio/crea";
    public static final String PATH_COLEGIO_ACTUALIZA_COLPORTOR = "/colegio/actualiza";
    public static final String PATH_COLEGIO_ELIMINA_COLPORTOR = "/colegio/elimina";
    public static final String PATH_CLIENTE = "/cliente";
    public static final String PATH_CLIENTE_LISTA = "cliente/lista";
    public static final String PATH_CLIENTE_VER = "/cliente/ver";
    public static final String PATH_CLIENTE_NUEVO = "cliente/nuevo";
    public static final String PATH_CLIENTE_EDITA = "cliente/edita";
    public static final String PATH_CLIENTE_CREA = "/cliente/crea";
    public static final String PATH_CLIENTE_ACTUALIZA = "/cliente/actualiza";
    public static final String PATH_CLIENTE_ELIMINA = "/cliente/elimina";
    public static final String PATH_ASOCIADO = "/asociado";
    public static final String PATH_ASOCIADO_LISTA = "/asociado/lista";
    public static final String PATH_ASOCIADO_VER = "/asociado/ver";
    public static final String PATH_ASOCIADO_NUEVO = "/asociado/nuevo";
    public static final String PATH_ASOCIADO_EDITA = "/asociado/edita";
    public static final String PATH_ASOCIADO_CREA = "/asociado/crea";
    public static final String PATH_ASOCIADO_ACTUALIZA = "/asociado/actualiza";
    public static final String PATH_ASOCIADO_ELIMINA = "/asociado/elimina";
    public static final String PATH_TEMPORADA = "/temporada";
    public static final String PATH_TEMPORADA_LISTA = "/temporada/lista";
    public static final String PATH_TEMPORADA_VER = "/temporada/ver";
    public static final String PATH_TEMPORADA_NUEVA = "/temporada/nueva";
    public static final String PATH_TEMPORADA_EDITA = "/temporada/edita";
    public static final String PATH_TEMPORADA_CREA = "/temporada/crea";
    public static final String PATH_TEMPORADA_ACTUALIZA = "/temporada/actualiza";
    public static final String PATH_TEMPORADA_ELIMINA = "/temporada/elimina";
    public static final String PATH_TEMPORADACOLPORTOR = "/temporadaColportor";
    public static final String PATH_TEMPORADACOLPORTOR_LISTA = "/temporadaColportor/lista";
    public static final String PATH_TEMPORADACOLPORTOR_VER = "/temporadaColportor/ver";
    public static final String PATH_TEMPORADACOLPORTOR_NUEVA = "/temporadaColportor/nueva";
    public static final String PATH_TEMPORADACOLPORTOR_EDITA = "/temporadaColportor/edita";
    public static final String PATH_TEMPORADACOLPORTOR_CREA = "/temporadaColportor/crea";
    public static final String PATH_TEMPORADACOLPORTOR_ACTUALIZA = "/temporadaColportor/actualiza";
    public static final String PATH_TEMPORADACOLPORTOR_ELIMINA = "/temporadaColportor/elimina";
    public static final String PATH_ALMACEN = "/almacen";
    public static final String PATH_ALMACEN_LISTA = "/almacen/lista";
    public static final String PATH_ALMACEN_VER = "/almacen/ver";
    public static final String PATH_ALMACEN_NUEVO = "/almacen/nuevo";
    public static final String PATH_ALMACEN_EDITA = "/almacen/edita";
    public static final String PATH_ALMACEN_CREA = "/almacen/crea";
    public static final String PATH_ALMACEN_ACTUALIZA = "/almacen/actualiza";
    public static final String PATH_ALMACEN_ELIMINA = "/almacen/elimina";
    public static final String PATH_PAIS = "/pais";
    public static final String PATH_PAIS_LISTA = "pais/lista";
    public static final String PATH_PAIS_VER = "/pais/ver";
    public static final String PATH_PAIS_NUEVA = "/pais/nueva";
    public static final String PATH_PAIS_EDITA = "/pais/edita";
    public static final String PATH_PAIS_CREA = "/pais/crea";
    public static final String PATH_PAIS_ACTUALIZA = "/pais/actualiza";
    public static final String PATH_PAIS_ELIMINA = "/pais/elimina";
    public static final String PATH_ESTADO = "/estado";
    public static final String PATH_ESTADO_LISTA = "estado/lista";
    public static final String PATH_ESTADO_VER = "/estado/ver";
    public static final String PATH_ESTADO_NUEVA = "/estado/nueva";
    public static final String PATH_ESTADO_EDITA = "/estado/edita";
    public static final String PATH_ESTADO_CREA = "/estado/crea";
    public static final String PATH_ESTADO_ACTUALIZA = "/estado/actualiza";
    public static final String PATH_ESTADO_ELIMINA = "/estado/elimina";
    public static final String PATH_CIUDAD = "/ciudad";
    public static final String PATH_CIUDAD_LISTA = "ciudad/lista";
    public static final String PATH_CIUDAD_VER = "/ciudad/ver";
    public static final String PATH_CIUDAD_NUEVA = "/ciudad/nueva";
    public static final String PATH_CIUDAD_EDITA = "/ciudad/edita";
    public static final String PATH_CIUDAD_CREA = "/ciudad/crea";
    public static final String PATH_CIUDAD_ACTUALIZA = "/ciudad/actualiza";
    public static final String PATH_CIUDAD_ELIMINA = "/ciudad/elimina";

}
