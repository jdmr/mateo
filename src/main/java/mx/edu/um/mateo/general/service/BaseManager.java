/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import mx.edu.um.mateo.general.utils.Constantes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author osoto
 */
public class BaseManager {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected Locale local = new java.util.Locale (Constantes.LOCALE_LANGUAGE, Constantes.LOCALE_COUNTRY, Constantes.LOCALE_VARIANT);
    protected SimpleDateFormat sdf = new SimpleDateFormat (Constantes.DATE_SHORT_HUMAN_PATTERN, local);
    protected DecimalFormat df = (DecimalFormat)NumberFormat.getCurrencyInstance (local);
    protected Calendar gcFecha = new GregorianCalendar(local);
}
