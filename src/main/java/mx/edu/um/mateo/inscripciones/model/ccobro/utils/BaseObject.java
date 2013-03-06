package mx.edu.um.mateo.inscripciones.model.ccobro.utils;

import java.io.Serializable;
import java.math.MathContext;
import java.math.RoundingMode;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Base class for Model objects.  Child objects should implement toString(), 
 * equals() and hashCode();
 *
 * <p>
 * <a href="BaseObject.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public abstract class BaseObject implements Serializable {
    protected Locale local = new java.util.Locale (Constants.LOCALE_LANGUAGE, Constants.LOCALE_COUNTRY, Constants.LOCALE_VARIANT);
    protected SimpleDateFormat sdf = new SimpleDateFormat (Constants.DATE_SHORT_HUMAN_PATTERN, local);
    protected DecimalFormat df = (DecimalFormat)NumberFormat.getCurrencyInstance (local);
    protected Calendar gcFecha = new GregorianCalendar(local);
    protected MathContext mc = new MathContext(6, RoundingMode.HALF_EVEN);

    //protected final Log log = LogFactory.getLog(getClass());
    protected final Logger log = LoggerFactory.getLogger(getClass());
    public abstract String toString();
    public abstract boolean equals(Object o);
    public abstract int hashCode();
}
