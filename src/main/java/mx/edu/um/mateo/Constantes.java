/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo;

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
    /**
     * Valores para el los containsKey para las clases
     */
    public static final String CONTAINSKEY_MAYORES = "mayores";
     public static final String CONTAINSKEY_EMPLEADOS = "empleados";
    /**
     * Valores para el los addAttribute para las clases
     */
    public static final String ADDATTRIBUTE_MAYOR = "mayor";
     public static final String ADDATTRIBUTE_EMPLEADO = "empleado";
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
    public static final String PATH_EMPLEADO_NUEVA = "/rh/empleado/nuevo"; 
    public static final String PATH_EMPLEADO_NUEVO = "/rh/empleado/nuevo";
    public static final String PATH_EMPLEADO_EDITA = "/rh/empleado/edita";
    public static final String PATH_EMPLEADO_CREA = "/rh/empleado/crea";
    public static final String PATH_EMPLEADO_ACTUALIZA = "/rh/empleado/actualiza";
    public static final String PATH_EMPLEADO_ELIMINA = "/rh/empleado/elimina";
}
