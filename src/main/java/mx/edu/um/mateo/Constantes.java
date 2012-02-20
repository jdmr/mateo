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
}
