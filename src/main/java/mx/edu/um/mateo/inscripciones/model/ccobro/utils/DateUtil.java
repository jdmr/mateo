package mx.edu.um.mateo.inscripciones.model.ccobro.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;


import mx.edu.um.mateo.inscripciones.model.ccobro.utils.ValueObject;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;


/**
 * Date Utility Class
 * This is used to convert Strings to Dates and Timestamps
 *
 * <p>
 * <a href="DateUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> 
 *   to correct time pattern. Minutes should be mm not MM
 * 	(MM is month). 
 * @version $Revision: 1.1 $ $Date: 2006/11/30 17:13:35 $
 * 
 * Modified by <a href="mailto:osoto@um.edu.mx">Omar Soto</a>
 *  to overrride default date pattern. (2008/09/11)
 */
public class DateUtil {
    //~ Static fields/initializers =============================================

    protected static org.slf4j.Logger log = LoggerFactory.getLogger(DateUtil.class);
    private static String defaultDatePattern = null;
    private static String timePattern = "HH:mm";

    //~ Methods ================================================================

    /**
     * Return default datePattern (MM/dd/yyyy)
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            defaultDatePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale)
                .getString("date.format");
        } catch (MissingResourceException mse) {
            defaultDatePattern = Constants.DATE_SHORT_HUMAN_PATTERN;
        }
        
        return defaultDatePattern;
    }
    
    public static String getDateTimePattern() {
        return DateUtil.getDatePattern() + " HH:mm:ss.S";
    }

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @see java.text.SimpleDateFormat
     * @throws ParseException
     */
    public static final Date convertStringToDate(String aMask, String strDate)
      throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);


        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * This method returns the current date time in the format:
     * MM/dd/yyyy HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     * 
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * 
     * @see java.text.SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     * 
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     * 
     * @param strDate the date to convert (in format MM/dd/yyyy)
     * @return a date object
     * 
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate)
      throws ParseException {
        Date aDate = null;

        try {
            

            aDate = convertStringToDate(getDatePattern(), strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate
                      + "' to a date, throwing exception");
            pe.printStackTrace();
            throw new ParseException(pe.getMessage(),
                                     pe.getErrorOffset());
                    
        }

        return aDate;
    }
    
    /**
     * Adds a day to a given date.
     * 
     * @param date  The given date of type Date
     * @return A variable of type Date
     */
    public static Date addOneDayToDate(Date date){
        Locale local = new Locale(Constants.LOCALE_LANGUAGE, Constants.LOCALE_COUNTRY, Constants.LOCALE_VARIANT);
        Calendar gcFecha = new GregorianCalendar(local);
        gcFecha.setTime(date);
        gcFecha.add(Calendar.DATE, 1);
        
        return gcFecha.getTime();
    }

    /**
     * @autor osoto
     * Returns the difference in milliseconds between date and Today
     * @param date
     * @return
     */
    public static Long getDiff(Date date) {
        return getDiff(date, new Date());
    }

    /**
     * @autor osoto
     * Returns the difference in milliseconds between start date and end date
     * @param startDate
     * @param endDate
     * @return
     */
    public static Long getDiff(Date startDate, Date endDate) {
        return endDate.getTime() - startDate.getTime();
    }

    /**
     * @autor osoto
     * Return the difference in years between date and Today
     * @param date
     * @return
     */
    public static Double getDiffInYears(Date date) {
        return getDiffInYears(date, new Date());
    }

    /**
     * @autor osoto
     * Return the difference in years between start date and end date
     * @param startDate
     * @param endDate
     * @return
     */
    public static Double getDiffInYears(Date startDate, Date endDate) {
        Long diff = getDiff(startDate, endDate);
        return diff / 31518720000.00;
    }

    /**
     * @author osoto
     * Returns the difference in days between date and Today
     * @param date
     * @return
     */
    public static Double getDiffInDays(Date date) {
        Long diff = getDiff(date);
        return (diff / 86400000.00)+1;
    }

    /**
     * @autor osoto
     * Returns the differenca in days between startDate and endDate
     * @param startDate
     * @param endDate
     * @return
     */
    public static Double getDiffInDays(Date startDate, Date endDate) {
        Long diff = getDiff(startDate, endDate);
        return (diff / 86400000.00)+1;
    }
    
    public static Integer getDiffInDaysWithoutWeekends(Date startDate, Date endDate) {
        log.debug("Fecha inicial {}, Fecha final {}", startDate, endDate);
        Locale local = new java.util.Locale (Constants.LOCALE_LANGUAGE, Constants.LOCALE_COUNTRY, Constants.LOCALE_VARIANT);
        Calendar gcFechaStart = new GregorianCalendar(local);
        gcFechaStart.setTime(startDate);
        Calendar gcFechaEnd = new GregorianCalendar(local);
        gcFechaEnd.setTime(endDate);
        
        Integer diffDias = 0;
        
        for(; gcFechaStart.getTimeInMillis() <= gcFechaEnd.getTimeInMillis();){
            if(gcFechaStart.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                    gcFechaStart.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
                log.debug("Dia en proceso {}",gcFechaStart.getTime());
                diffDias+=1;
            }
            gcFechaStart.add(Calendar.DATE, 1);
        }
        log.debug("Numero de dias {}", diffDias);
        return diffDias;
    }

    /**
     * Evalua periodos de tiempo, y determina rangos de tiempo continuos validando si los periodos se traslapan
     * @param periodos. Lista de periodos de tiempo conformados por ValueObject.
     * @return Lista de rangos unicos y continuos de tiempo
     */
    public static List <ValueObject> getRangosFechas(List <ValueObject> periodos) throws Exception{
        List rangos = new ArrayList();
        ValueObject vo = null;
        
        Date fechaInicialLast = null;
        Date fechaFinalLast = null;

        Date fechaInicial = null;
        Date fechaFinal = null;

        //Evaluar fechas iniciales de vo
        Iterator <ValueObject> it = periodos.iterator();
        while (it.hasNext()) {
            vo = it.next();

            fechaInicial = evaluaFechaInicial(vo, periodos);
            fechaFinal = evaluaFechaFinal(vo, periodos);

            //System.out.println("DateUtil - fechaInicial "+fechaInicial);
            //System.out.println("DateUtil - fechaFinal "+fechaFinal);

            if(fechaInicialLast == null ||
                    (fechaInicial.compareTo(fechaInicialLast) != 0 &&
                    fechaFinal.compareTo(fechaFinalLast) != 0))
            {
                fechaInicialLast = fechaInicial;
                fechaFinalLast = fechaFinal;

                vo = new ValueObject();
                vo.setValueOne(fechaInicial);
                vo.setValueTwo(fechaFinal);
                rangos.add(vo);
            }

            //Evaluar rango de fechas para saber si este se traslapa con algun periodo
            vo.setValueOne(evaluaFechaInicial(vo, periodos));
            vo.setValueTwo(evaluaFechaFinal(vo, periodos));
        }

        return rangos;
    }

    private static Date evaluaFechaInicial(ValueObject vo, List <ValueObject> periodos) {
        ValueObject tmp = null;
        Date voFechaInicial = null;
        Date fecha = null;

        Iterator <ValueObject> it2 = periodos.iterator();
        while(it2.hasNext()){
            tmp = it2.next();

            voFechaInicial = (Date)vo.getValueOne();

            if(tmp != vo &&
                    voFechaInicial.compareTo((Date)tmp.getValueOne()) >= 0){
                fecha = (Date)tmp.getValueOne();
                vo = tmp;
            }
        }
        return (fecha != null) ? fecha : voFechaInicial;
    }

        private static Date evaluaFechaFinal(ValueObject vo, List <ValueObject> periodos) {
        ValueObject tmp = null;
        Date voFechaFinal = null;
        Date fecha = null;

        Iterator <ValueObject> it2 = periodos.iterator();
        while(it2.hasNext()){
            tmp = it2.next();

            voFechaFinal = (Date)vo.getValueTwo();

            if(tmp != vo &&
                    voFechaFinal.compareTo((Date)tmp.getValueTwo()) <= 0){
                fecha = (Date)tmp.getValueTwo();
                vo = tmp;
            }
        }
        return (fecha != null) ? fecha : voFechaFinal;
    }
            
}
