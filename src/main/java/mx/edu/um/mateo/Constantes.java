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
    public static final String CONTAINSKEY_ORGANIZACION = "organizacion";
    /**
     * Valores para el los containsKey para las clases
     */
    public static final String CONTAINSKEY_MAYORES = "mayores";
    public static final String CONTAINSKEY_EMPLEADOS = "empleados";
    public static final String CONTAINSKEY_RESULTADOS = "resultados";
    public static final String CONTAINSKEY_AUXILIARES = "auxiliares";
    public static final String CONTAINSKEY_LIBROS = "libros";
    public static final String CONTAINSKEY_COLEGIOS = "colegios";
    public static final String CONTAINSKEY_NACIONALIDADES = "nacionalidades";
    public static final String CONTAINSKEY_CATEGORIAS = "categorias";
    public static final String CONTAINSKEY_CONCEPTOS = "conceptos";

    /**
     * Valores para el los addAttribute para las clases
     */
    public static final String ADDATTRIBUTE_MAYOR = "mayor";
    public static final String ADDATTRIBUTE_EMPLEADO = "empleado";
    public static final String ADDATTRIBUTE_RESULTADO = "resultado";
    public static final String ADDATTRIBUTE_AUXILIAR = "auxiliar";
    public static final String ADDATTRIBUTE_LIBRO = "libro";
    public static final String ADDATTRIBUTE_COLEGIO = "colegio";
    public static final String ADDATTRIBUTE_CATEGORIA = "categoria";
    public static final String ADDATTRIBUTE_NACIONALIDAD = "nacionalidad";
    public static final String ADDATTRIBUTE_DEPENDIENTE = "dependiente";
    public static final String ADDATTRIBUTE_ESTUDIOSEMPLEADO = "estudioEmpleado";
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
    


    public static final String CONTAINSKEY_DEPENDIENTES = "dependientes";
    public static final String CONTAINSKEY_ESTUDIOSEMPLEADO = "estudiosEmpleado";

    /**
     * Valores para los conceptos
     */
    public static final String CONCEPTO_LIST = "conceptoList";
    public static final String CONCEPTO_KEY = "concepto";
 

}
