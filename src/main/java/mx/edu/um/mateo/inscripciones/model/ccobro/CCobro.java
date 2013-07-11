/*
 * Created on Jun 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package mx.edu.um.mateo.inscripciones.model.ccobro;

import mx.edu.um.mateo.inscripciones.model.ccobro.afe.Convenio;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Currency;
import mx.edu.um.mateo.inscripciones.model.ccobro.estudiantes.Aviso;
import mx.edu.um.mateo.inscripciones.model.ccobro.tFinanciera.TFinanciera;
import mx.edu.um.mateo.inscripciones.model.ccobro.dinscribir.Carga;
import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;
import mx.edu.um.mateo.inscripciones.model.ccobro.paquete.PaqueteAlumno;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import javax.sql.DataSource;
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.DateUtil;
//import mx.edu.um.mateo.inscripciones.model.ccobro.afe.BecaAdicionalVO;

import mx.edu.um.mateo.inscripciones.model.ccobro.afe.TipoOtraBecaEnum;
import mx.edu.um.mateo.inscripciones.model.ccobro.exception.UMException;
import mx.edu.um.mateo.inscripciones.model.ccobro.financiero.TipoOperacionCaja;
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.ValueObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class CCobro implements Constant {

    private Alumno alumno;
    private Materia materia;
    private Double semanasInternado;
    private Double factorInternado;
    private Double matricula;
    private Double ensenanza;
    private Double internado;
    private TFinanciera tFinanciera;
    private Double cobroClima;
    private Date fechaInicial;
    private Date fechaFinal;
    /*Maps Generales*/
    private Map mTParametros;
    private Map mDMaterias;
    private Map mProrrogas;
    private Map mBecas;
    private Map mAEmpleados;
    private Map mPaquetes;
    /*Bloques en los cuales el alumno inscribira materias*/
    private Map mBloques;
    //Maps que guardaran los movimientos de acuerdo a su categoria
    //Movimientos para calcular el costo del bloque
    private Map mMovimientosCB;
    //Movimientos para calcula la cuota de inscripcion
    private Map mMovimientosCI;
    //Movimientos varios (otros movimientos)
    private Map mMovimientosOT;
    private Map mComedor;
    /*Se utiliza un vector porque puede darse el caso de que un alumno tenga mas de un descuento activo*/
    private Vector vDAlumnos;
    /*Los pagares no tienen una llave unica, ademas que no requieren estarse consultado vez tras vez*/
    /*Asi que se implementan mediante un vector*/
    private Vector mPagares;
    /*Maps Calculo Cobro*/
    private Map mMaterias;
    private Map mMovimientos;
    private Connection conn;
    private Connection conn_noe;
    private Connection conn_enoc;
    private Map mCobrosClima;
    private BigDecimal importeBonificaciones; //Colportaje
    private BigDecimal importeBecaBasica; //Talleres
    private BigDecimal importeBecaAdicional; //Beca adicional
    private BigDecimal importeExcedente; //Beca excedente 
    private Boolean tipoAlumnoInstitucional;
    private Boolean alumnoPrecontratado;
    private Boolean online = false;
    private Boolean convenioEntregado = false;
//    private BecaAdicionalVO beca;
    private Double mPagare = 0.00;
    private DataSource ds;
    //Se utiliza para cargar los datos de reimpresion e inscripcion definitiva
    private Contrato contrato;
    //Usuario actual del sistema
    Long user;
    
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public CCobro(DataSource ds) throws Exception {
        this();
        log.info("Creando calculo de cobro con Datasource ");
        log.info("{}",ds);
        this.ds = ds;
    }

    /**
     *
     */
    public CCobro() throws Exception {
        mBloques = new TreeMap();
        mProrrogas = new TreeMap();

        this.cobroClima = new Double(0);
        this.factorInternado = new Double(1);

        this.semanasInternado = new Double(-1);
        this.fechaInicial = new Date();
        this.fechaFinal = new Date();

        /*Inicializar valores*/
        this.alumno = new Alumno();
        log.info("CCobro 1 ");
        log.info("{}",this.alumno);
        this.materia = new Materia();
        log.info("CCobro 2");

        /*Subir descuentos de materia a la memoria*/
        mDMaterias = new DMateria().getDMateria();
        log.info("CCobro 3");

        /*Subir descuentos de alumno a la memoria*/
        vDAlumnos = new DAlumno().getDAlumno();
        log.info("CCobro 4");

        /*Subir prorrogas a la memoria*/
        mProrrogas = new Prorroga().getProrrogas();
        log.info("CCobro 5");

        /*Subir becas a la memoria*/
        mBecas = new Beca().getBecas();
        log.info("CCobro 6");

        /*Subir autorizaciones de estudio para los empleados a la memoria*/
        mAEmpleados = new Empleado().getAutorizacionEmp();
        log.info("CCobro 7");

        /*Subir paquetes*/
        mPaquetes = new PaqueteAlumno().getPaquetes();

        /**
         * 04/06/2011 - El Sr. Randeles indico que ya no se utiliza el cobro de clima
         */
        //mCobrosClima = new CobroClima().getCobrosClima();
        mCobrosClima = new TreeMap();

        /*Inicializar maps de calculo de cobro*/
        mMaterias = new TreeMap();
        mMovimientos = new TreeMap();
        mPagares = new Vector();

        importeBonificaciones = new BigDecimal("0");
        importeBecaBasica = new BigDecimal("0");
        importeBecaAdicional = new BigDecimal("0");

        this.tipoAlumnoInstitucional = false;
        this.alumnoPrecontratado = false;
//        beca = null;
    }

//    public void calculaCobro(String matricula, String formaPago, Boolean inscrito, BecaAdicionalVO beca, DataSource ds) throws Exception {
//        if (beca != null) {
//            this.importeBonificaciones = beca.getBonificacionGema().add(beca.getBonificacionUM());
//            //Se descuenta el diezmo a la beca basica
//            this.importeBecaBasica = beca.getBecaBasica();
//            this.importeBecaAdicional = beca.getBecaAdicional();
//            this.importeExcedente = beca.getExcedente();
//
//            if (beca.hasContrato()) {
//                this.tipoAlumnoInstitucional = beca.getContrato().isIndustrial();
//                this.alumnoPrecontratado = beca.getContrato().estaPrecontratado();
//                formaPago = "P";
//            }
//            this.beca = beca;
//
//            log.info("Bonificaciones {} ",this.importeBonificaciones);
//            log.info("BecaBasica {}",this.getImporteBecaBasica());
//            log.info("BecaAdicional {}", this.getImporteBecaAdicional());
//            log.info("Excedente {}", this.getImporteExcedente());
//            log.info("tipoAlumnoInstitucional {}", this.tipoAlumnoInstitucional);
//            log.info("alumnoPrecontratado {}", this.alumnoPrecontratado);
//            log.info("formaPago {}", formaPago);
//            
//        }
//        
//        this.ds = ds;
//        this.calculaCobro(matricula, formaPago, inscrito);
//    }

//    public void calculaCobro(String matricula, String formaPago, Boolean inscrito, BecaAdicionalVO beca, DataSource ds, Long user) throws Exception {
//        if (beca != null) {
//            this.importeBonificaciones = beca.getBonificacionGema().add(beca.getBonificacionUM());
//            this.importeBecaBasica = beca.getBecaBasica();
//            this.importeBecaAdicional = beca.getBecaAdicional();
//            this.importeExcedente = beca.getExcedente();
//
//            if (beca.hasContrato()) {
//                this.tipoAlumnoInstitucional = beca.getContrato().isIndustrial();
//                this.alumnoPrecontratado = beca.getContrato().estaPrecontratado();
//                formaPago = "P";
//            }
//            this.beca = beca;
//
//        }


//        log.info("Bonificaciones {}", this.importeBonificaciones);
//        log.info("BecaBasica {}", this.getImporteBecaBasica());
//        log.info("BecaAdicional {}", this.getImporteBecaAdicional());
//        log.info("Excedente {}", this.getImporteExcedente());
//        log.info("tipoAlumnoInstitucional {}", this.tipoAlumnoInstitucional);
//        log.info("alumnoPrecontratado {}", this.alumnoPrecontratado);
//        log.info("formaPago {}", formaPago);
//
//
//        this.ds = ds;
//        this.user = user;
//
//        this.calculaCobro(matricula, formaPago, inscrito);
//    }

//    public void calculaCobro(String matricula, String formaPago, Boolean inscrito, BecaAdicionalVO beca, DataSource ds, Long user, Boolean online) throws Exception {
//        if (beca != null) {
//            this.importeBonificaciones = beca.getBonificacionGema().add(beca.getBonificacionUM());
//            this.importeBecaBasica = beca.getBecaBasica();
//            this.importeBecaAdicional = beca.getBecaAdicional();
//            this.importeExcedente = beca.getExcedente();
//
//            if (beca.hasContrato()) {
//                this.tipoAlumnoInstitucional = beca.getContrato().isIndustrial();
//                this.alumnoPrecontratado = beca.getContrato().estaPrecontratado();
//                formaPago = "P";
//            }
//            this.beca = beca;
//        }
//
//
//        log.info("Bonificaciones {}", this.importeBonificaciones);
//        log.info("BecaBasica {}", this.getImporteBecaBasica());
//        log.info("BecaAdicional {}", this.getImporteBecaAdicional());
//        log.info("Excedente {}", this.getImporteExcedente());
//        log.info("tipoAlumnoInstitucional {}", this.tipoAlumnoInstitucional);
//        log.info("alumnoPrecontratado {}", this.alumnoPrecontratado);
//        log.info("formaPago {}", formaPago);
//
//
//        this.ds = ds;
//        this.user = user;
//        this.online = online;
//
//        this.calculaCobro(matricula, formaPago, inscrito);
//    }

    public void calculaCobro(String matricula, String formaPago,
            Boolean inscrito) throws Exception {

        if (this.alumnoPrecontratado) {
            throw new UMException("El alumno " + matricula + " no est\u00e1 contratado, sino precontratado. "
                    + "<br>Debe pasar al departamento donde labora para que actualicen su estatus de PRECONTRATADO a CONTRATADO");
        }

        log.info("calculaCobro / default ....");
        log.info("calculaCobro {}", matricula);
        this.alumno.setMatricula(matricula);
        log.info("CCobro 8");
        this.alumno.setFormaPago(formaPago);
        log.info("CCobro 9");
        this.alumno.getContabilidad();
        log.info("CCobro 10");
        this.alumno.setFactorInternado(this.factorInternado);
        log.info("CCobro 11");

        /*Obtener los datos del alumno*/
        alumno.getAlumno();
        log.info("CCobro 12");
        
//        //Si la bandera 'inscrito' esta prendida, validar candado de firma de convenio AFE
//        if (this.beca.hasContrato()) {
//            if (!Contrato.estaConvenioFirmado(matricula, alumno, ds.getConnection())) {
//                convenioEntregado = false;
//                if (inscrito) {
//                    throw new UMException("Convenio no ha sido firmado aun. <br> El alumno debe ir personalmente a Autofinanciamiento para firmar su convenio de servicio becario.");
//                }
//            } else {
//                convenioEntregado = true;
//            }
//            log.info("Convenio entregado {}", convenioEntregado);
//        }
//        else{
//            convenioEntregado = true; //Para que no mande la leyenda en el action
//        }

        if (this.tipoAlumnoInstitucional) {
            this.alumno.setTAlumno_id(Constant.ccfintTInstitucional);
            //20-08/12 El CP Alverto Maldonado solicito que todos los institucionales se les cobrara en pagares
            this.alumno.setFormaPago("P"); //Los alumnos institucionales solo se inscriben de contado
        }

        /*Obtener la carga y el bloque de estudio*/
        alumno.getCargaIDBloque();
        //log.info("CCobro 13 - Carga " + this.alumno.getCarga_id() + ", " + this.alumno.getBloque());
        mComedor = new AutorizaComida().getComidas(this.alumno.getCarga_id());
        log.info("CCobro 14");

        if (this.alumno.isInscrito().booleanValue()) {
            throw new UMException("El alumno " + matricula
                    + " ya esta inscrito en la carga " + alumno.getCarga_id());
        }

        /*Obtener datos persistentes del CCobro*/
        alumno.getDatosCCobro();
        log.info("Datos persistentes - Semanas Internado  {}, {}",alumno.getSemanasInternado(),this.semanasInternado);

        /*Subir tabla financiera a la memoria*/
        tFinanciera = new TFinanciera().getTFinanciera(alumno.getCarga_id());
        log.info("CCobro 15");

        /*Subir tabla de parametros a la memoria*/
        mTParametros = new TParametros().getTParametros(alumno.getCarga_id());
        log.info("CCobro 16");

        /*Subir materias del alumno a la memoria*/
        mMaterias = new Materia().getMaterias(alumno.getMatricula(),
                alumno.getCarga_id(), alumno.getBloque(), tFinanciera,
                mDMaterias, mAEmpleados, mComedor, mMovimientos, this.alumno, mBloques);
        log.info("CCobro 17");

        if (mMaterias.isEmpty()) {
            throw new UMException("El alumno " + matricula
                    + " no tiene materias asignadas en la carga "
                    + alumno.getCarga_id());
        }

        /*Acumular costo de materias para obtener total de ensenanza*/
        this.ensenanza = new Materia().getTotalEnsenanza(mMaterias);
        log.info("CCobro 18");

        /**
         * TODO Obtiene ensenanza del paquete
         * Si el alumno se encuentra en paquetes, entonces el costo de la ensenanza sera el del paquete
         * Siempre y cuando el valor de la ensenanza no sea -1
         * */
        if (mPaquetes.containsKey(matricula)) {
            PaqueteAlumno paquete = (PaqueteAlumno) mPaquetes.get(matricula);
            if (paquete.getPaquete().getEnsenanza().compareTo(new Double(0)) >= 0) {
                this.ensenanza = paquete.getPaquete().getEnsenanza();
            }
        }

        /*Insertar movimiento de ensenanza*/

        Movimiento movimiento = new Movimiento(matricula, alumno.getCarga_id(),
                alumno.getBloque(), ccfstrEnsenanzaID, ccfstrEnsenanza,
                this.ensenanza, "D", "S", "-", alumno.getId_ccosto());
        mMovimientos.put(matricula + alumno.getCarga_id() + alumno.getBloque()
                + ccfstrEnsenanzaID, movimiento);
        movimiento = null;
        log.info("CCobro 19");

        /*Obtener importe de matricula e internado*/
        if (!tFinanciera.getDetalles().containsKey(alumno.getCarrera_id()
                + alumno.getModalidad_id())) {
            throw new UMException(
                    "Los costos financieros no estan capturados para la carrera "
                    + alumno.getCarrera() + " en la modalidad "
                    + alumno.getModalidad());
        }

        this.matricula = tFinanciera.getMatricula(alumno.getCarrera_id(),
                alumno.getModalidad_id());
        this.internado = tFinanciera.getInternado(alumno.getCarrera_id(),
                alumno.getModalidad_id());
        log.info("CCobro 20");
        this.calculaMatricula(tFinanciera);
        log.info("CCobro 21");
        this.calculaInternado();
        log.info("CCobro 22");

        this.calculaBecaAdicional();


        /**
         * Evaluar tipo de alumno
         */
        //Alumno hijo de empleado
        alumno.becado(mBecas, mMovimientos, this.matricula, this.ensenanza,
                this.internado);

        log.info("CCobro 122");
        /*Evaluar cobros por clima en dormitorio*/
        //this.getCobrosClima();

        log.info("CCobro 23");
        alumno.hijoObrero(mTParametros, mMovimientos, this.matricula,
                this.ensenanza, this.internado, ds.getConnection());

        log.info("CCobro 24");
        alumno.Obrero(mMovimientos, this.ensenanza);
        log.info("CCobro 25");

        /*Determinar descuentos Mat. Ext.*/
        this.getDescuentos();
        log.info("CCobro 26");

        /*Determinar bonificaciones por colportaje*/
        this.getColportaje();
        log.info("CCobro 35");

        /*Evaluar si hay prorrogas de pago*/
        this.getProrrogas();
        log.info("CCobro 27");
        /*Determinar si es pago de contado o por pagares*/
        this.pagoContado();
        log.info("CCobro 28");
        this.getPagoMinimo();
        log.info("CCobro 29");
        this.getManejoPagare();
        log.info("CCobro 30");

        /*Evaluar si es un alumno institucional*/
        this.getInstitucional();
        log.info("CCobro 27.institucional");

        this.getPagares();
        log.info("CCobro 31");
        this.alumno.getSaldoAnterior(mMovimientos, mPagares); //Se pasan los pagares, ya que el saldo anterior se almacena como uno
        log.info("CCobro 32");
        this.getMatriculaExtemporanea();
        log.info("CCobro 33");
        this.grabaDatos(inscrito);
        log.info("CCobro 34");
    }

    private void calculaMatricula(TFinanciera tFinanciera)
            throws Exception {
        /*Validar si el alumno debe pagar matricula*/
        /*En el caso que sea empleado UM no paga matricula*/
        log.info("Costo de matricula de " + alumno.getMatricula() + "," + alumno.getCarrera_id() + "," + alumno.getModalidad_id() + "," + this.matricula);
        log.info("Valor boolean de isMatricula {}",alumno.isMatricula());
        log.info("Valor de Tipo Alumno {}",alumno.getTAlumno_id());
        if (alumno.isMatricula().booleanValue()) {
            /*Si el alumno es extranjero, anadir los gastos */
            if (alumno.getNacionalidad().equals("EXT")) {
                this.matricula = tFinanciera.getTLegales(
                        alumno.getCarrera_id(),
                        alumno.getModalidad_id().toString());
                log.info("Tramites legales " + this.matricula);
                this.matricula = new Double(this.matricula.doubleValue() + tFinanciera.getMatricula(alumno.getCarrera_id(),
                        alumno.getModalidad_id()).doubleValue());
                log.info("Matricula + TLegales " + this.matricula);
            }
            log.info("Costo de matricula de " + alumno.getMatricula() + "," + alumno.getCarrera_id() + "," + alumno.getModalidad_id() + "," + this.matricula);

            /**
             * TODO Obtiene matricula el paquete
             * Si el alumno se encuentra en paquetes, entonces el costo de la matricula sera el del paquete
             * Siempre y cuando el valor de la matricula sea mayor que -1
             * */
            if (mPaquetes.containsKey(alumno.getMatricula())) {
                PaqueteAlumno paquete = (PaqueteAlumno) mPaquetes.get(alumno.getMatricula());
                if (paquete.getPaquete().getMatricula().compareTo(new Double(0)) >= 0) {
                    this.matricula = paquete.getPaquete().getMatricula();
                }
            }

            /**
             * 09-Agosto-2010 El CP Joel Sebastian solicito que a los alumnos universitarios de primer ingreso se les cobre $234.00
             * 11-Agosto-2010 El CP Joel Sebastian solicito que se cancelara este cobro.
             */
//                        if(alumno.isUniversitario() && alumno.isPrimerIngreso()){
//                            this.matricula += 234.00;
//                        }
        } else{
            this.matricula = new Double(0);
        }
        
        if(alumno.getTAlumno_id().compareTo(ccfintTEmpleado) == 0) {
            /**
             * 04-Jun-2012 --El CP Raul Randeles, indico que no se debe cobrar la matricula a los empleados
             */
            this.matricula = new Double(0);
        }
        /*Generar movimiento de cobro de matricula*/
        if(this.matricula.compareTo(new Double(0)) > 0){
            Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque(),
                    ccfstrMatriculaID, ccfstrMatricula, this.matricula, "D",
                    "S", "-", alumno.getId_ccosto());
            mMovimientos.put(matricula + alumno.getCarga_id()
                    + alumno.getBloque() + ccfstrMatriculaID, movimiento);
            movimiento = null;
        }
        log.info("Costo de matricula de " + alumno.getMatricula() + "," + alumno.getCarrera_id() + "," + alumno.getModalidad_id() + "," + this.matricula);
    }

    /*Verificar el numero de comidas que el alumno tiene registradas*/
    /*para afectar el valor del internado a cobrar*/
    private void calculaInternado(Map mBloques) throws Exception {
        /*Si el alumno es externo, no cobrar internado*/
        if (alumno.getResidencia().equals("E")) {
            this.internado = new Double(0);
        } else {
            //Calcular total de internado
            //El 30% del valor del internado corresponde a vivienda
            //El 70% del valor del internado corresponde a comidas
            Double dblVivienda = Currency.getRound(new Double(
                    this.internado.doubleValue() * 0.30), new Integer(2));
            Double dblComidas = Currency.getRound(new Double(
                    this.internado.doubleValue() * 0.70), new Integer(2));

            log.info("Calcula Internado 1, Internado " + this.internado);
            Integer intNumComidas = new Integer(0);

            //Multiplicar el costo de la vivienda por el numero de bloques a inscribir
            if (mBloques.size() == 0) {
                throw new UMException("El alumno " + alumno.getMatricula() + " no tiene bloques registrados para inscribir, por lo que no se puede calcular el costo del internado");
            }

            log.info("Calcula Internado 7, numBloques " + mBloques.size());
            dblVivienda = Currency.getRound(new Double(dblVivienda.doubleValue() * mBloques.size()), new Integer(2));

            Map mTmp = new TreeMap();

            Iterator iMaterias = mMaterias.keySet().iterator();

            while (iMaterias.hasNext()) {
                Materia materia = (Materia) mMaterias.get(iMaterias.next());
                log.info("Calcula Internado 2");

                //Obtener el numero de comidas registradas para este alumno
                log.info("Calcula internado " + alumno.getMatricula()
                        + alumno.getCarga_id() + materia.getBloque_curso() + materia.getCurso_id());

                if (!mComedor.containsKey(alumno.getMatricula()
                        + alumno.getCarga_id() + materia.getBloque_curso())) {
                    throw new UMException(
                            "Error al obtener numero de comidas del alumno "
                            + alumno.getMatricula() + ",en la carga " + alumno.getCarga_id() + ", y bloque " + materia.getBloque_curso());
                }

                if (!mTmp.containsKey(materia.getBloque_curso())) {
                    mTmp.put(materia.getBloque_curso(), materia.getBloque_curso());

                    log.info("Calcula Internado 3");

                    AutorizaComida autorizaComida = (AutorizaComida) mComedor.get(alumno.getMatricula()
                            + alumno.getCarga_id() + materia.getBloque_curso());

                    log.info("Calcula Internado 4");

                    intNumComidas = new Integer(intNumComidas.intValue() + autorizaComida.getComidas().intValue());
                }
                log.info(alumno.getMatricula() + "@" + intNumComidas);
            }
            log.info("Calcula Internado 5");
            //Calcular valor del internado
            //Importe de comidas a pagar = (Internado)(70%)(Num.Comidas)(100/3)
            //Simplificando (Internado)(NumComidas)(0.233333)
            this.internado = Currency.getRound(new Double(dblVivienda.doubleValue()
                    + (this.internado.doubleValue() * Currency.getRound(
                    new Double(intNumComidas.doubleValue() * 0.23333),
                    new Integer(2)).doubleValue())), new Integer(2));

            log.info("Calcula Internado 6, Internado " + this.internado);
            //Si el numero el factor de internado es mayor que cero, multiplicar
            if (this.factorInternado.compareTo(new Double(0)) > 0) {
                this.internado = Currency.getRound(new Double(this.factorInternado.doubleValue()
                        * this.internado.doubleValue()), new Integer(2));
            }

            /**
             * TODO Obtiene el internado del paquete
             * Si el alumno se encuentra en paquetes, entonces el costo del internado sera el del paquete
             * Siempre y cuando el valor del internado sea mayor que -1
             */
            if (mPaquetes.containsKey(alumno.getMatricula())) {
                PaqueteAlumno paquete = (PaqueteAlumno) mPaquetes.get(alumno.getMatricula());
                if (paquete.getPaquete().getInternado().compareTo(new Double(0)) >= 0) {
                    this.internado = paquete.getPaquete().getInternado();
                }
            }

            //Insertar en la tabla, el importe del Internado
            log.info("Calcula Internado 8");
            Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque(),
                    ccfstrInternadoID, ccfstrInternado, this.internado, "D",
                    "S", "-", alumno.getId_ccosto());
            mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                    + alumno.getBloque() + ccfstrInternadoID, movimiento);
            movimiento = null;
        }
    }

    /**
     * Verificar el numero de comidas que el alumno tiene registradas
     * para afectar el valor del internado a cobrarv<br>
     * Este metodo utiliza el <b>numero de semanas de internado</b> para el calculo correspondiente
     */
    private void calculaInternado() throws Exception {
        /*Si el alumno es externo o institucional, no cobrar internado*/
        if (alumno.getResidencia().equals("E")) {
            this.internado = new Double(0);
        } else {
            //Calcular total de internado
            //El 30% del valor del internado corresponde a vivienda
            //El 70% del valor del internado corresponde a comidas
            //23-Nov-2010 - El CP Eliezer Castellanos solicito que se dividiera el internado en 34% dormitorio
            //y el 66% vivienda y ya no se tomara en cuenta el cobro de clima
            Double dblVivienda = Currency.getRound(new Double(
                    this.internado.doubleValue() * 0.34), new Integer(2));
            Double dblComidas = Currency.getRound(new Double(
                    this.internado.doubleValue() * 0.66), new Integer(2));
            
            log.info("Vivienda "+dblVivienda);
            log.info("Comidas "+dblComidas);
            log.info("Internado "+this.internado);

            Materia materia = null;
            ValueObject vo = null;
            List<ValueObject> periodos = new ArrayList<ValueObject>();
            Iterator iMaterias = mMaterias.keySet().iterator();

            Integer numBloques = 0;
            Integer lastBloque = 0;

            Integer items = 0;

            //Recorrer materias para obtener los periodos de tiempo que cada una va a durar...
            while (iMaterias.hasNext()) {
                materia = (Materia) mMaterias.get(iMaterias.next());

                if (lastBloque.compareTo(materia.getBloque_curso()) != 0) {
                    numBloques++;
                    lastBloque = materia.getBloque_curso();
                }
                log.info("CalculaInternado - bloques " + numBloques);

                //Se guardan las fechas en valueObject...
                vo = new ValueObject();
                vo.setValueOne(materia.getFechaInicio());
                vo.setValueTwo(materia.getFechaFinal());
                log.info("CalculaInternado - rangos " + materia.getFechaInicio() + " -> " + materia.getFechaFinal());
                //Se agregan a una lista...
                periodos.add(vo);
            }

            //Obtener el numero de comidas totales del alumno
            Integer numComidas = 0;
            List<AutorizaComida> comidas = null;

            
            if (mComedor.containsKey(alumno.getMatricula())) {
                comidas = (List) mComedor.get(alumno.getMatricula());
            } else {
                comidas = new ArrayList<AutorizaComida>();
                mComedor.put(alumno.getMatricula(), comidas);
            }
            
            log.info("Comidas "+comidas);

            for (AutorizaComida comida : comidas) {
                log.info("Comida "+comida.getComidas());
                numComidas += comida.getComidas();
            }
            
            //Para finalmente evaluar todos los periodos y obtener rangos continuos de tiempo de internado
            List<ValueObject> rangos = DateUtil.getRangosFechas(periodos);

            //Recuperar registros de comedor
            AutorizaComida comida = null;
            Double totalDias = 0.0;

            Integer cont = 0;

            for (ValueObject v : rangos) {
                log.info("CalculaInternado - rango[" + cont + "] " + v);
                try {
                    log.info("CalculaInternado - list " + comidas);
                    comida = comidas.get(cont);
                    log.info("Comida "+comida);
                } catch (IndexOutOfBoundsException e) {
                    comida = new AutorizaComida(alumno.getMatricula(),
                            alumno.getCarga_id(), alumno.getBloque(), (Date) v.getValueOne(), (Date) v.getValueTwo());
                    log.info("CalculaInternado -try:catch " + comida + ", "+ comida.getComidas());
                    //Nuevo rango de entrada al comedor, por lo tanto se incluyen las comidas
                    numComidas += comida.getComidas();
                    log.info("Num Comidas " + numComidas);
                    comidas.add(comida);
                }
                comida.setFechaInicio((Date) v.getValueOne());
                comida.setFechaFinal((Date) v.getValueTwo());

                cont++;

                //Obtener cantidad total en dias para el internado,
                //siempre y cuando la carga actual sea de verano
                totalDias += DateUtil.getDiffInDays((Date) v.getValueOne(), (Date) v.getValueTwo());
                log.info("CalculaInternado - Dias de internado " + DateUtil.getDiffInDays((Date) v.getValueOne(), (Date) v.getValueTwo()));
            }

            if (Carga.isVerano(alumno.getCarga_id())) {
                numComidas /= cont;
                items = totalDias.intValue();
                log.info("Carga isVerano, con totalDias " + items);
            } else {
                //Si es alumno de posgrado
                if(this.alumno.getNivelId().equals(3) || this.alumno.getNivelId().equals(4)){
                    //numComidas /= numBloques;
                    items = numBloques;
                    log.info("Carga NO isVerano, es de posgrado " + items);
                    
                }
                else{
                    //numComidas /= numBloques; //Solo es un bloque
                    items = 1;
                    log.info("Carga NO isVerano, es de licenciatura" + items);
                }
            }
            
            if(items.compareTo(new Integer(0)) <= 0){
                throw new UMException("Imposible calcular el internado... <br>Valor negativo de numero de dias/bloques. <br>"+items);
            }
            
            //--- No deberia ser dblComidas????---
            log.info("Formula (" + dblVivienda + "+(" + dblComidas + "/ 3) * " + numComidas + ")*" + items);
            log.info("Valor de Internado " + this.internado + ", Valor de dblComidas " + dblComidas + ", Valor de Vivienda " + dblVivienda);

            //Calcular valor del internado
            //Importe de comidas a pagar = (dblComidas)(Num.Comidas)/(3)

            this.internado = Currency.getRound(new Double((dblVivienda
                    + (Currency.getRound((dblComidas / 3) * numComidas.doubleValue(), 2))) * items), 2);

            log.info("Calcula Internado 6, Internado " + this.internado);
            //Si el numero el factor de internado es mayor que cero, multiplicar
            if (this.factorInternado.compareTo(new Double(0)) > 0) {
                this.internado = Currency.getRound(new Double(this.factorInternado.doubleValue()
                        * this.internado.doubleValue()), new Integer(2));
            }

            /**
             * TODO Obtiene el internado del paquete
             * Si el alumno se encuentra en paquetes, entonces el costo del internado sera el del paquete
             * Siempre y cuando el valor del internado sea mayor que -1
             */
            if (mPaquetes.containsKey(alumno.getMatricula())) {
                PaqueteAlumno paquete = (PaqueteAlumno) mPaquetes.get(alumno.getMatricula());
                if (paquete.getPaquete().getInternado().compareTo(new Double(0)) >= 0) {
                    this.internado = paquete.getPaquete().getInternado();
                }
            }

            //Se acumula el cobro del clima en internado
            this.internado += this.cobroClima;

            //Insertar en la tabla, el importe del Internado
            log.info("Calcula Internado 8");
            Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque(),
                    ccfstrInternadoID, ccfstrInternado, this.internado, "D",
                    "S", "-", alumno.getId_ccosto());
            mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                    + alumno.getBloque() + ccfstrInternadoID, movimiento);
            movimiento = null;

        }
    }

    public void calculaBecaAdicional() throws Exception {
        log.info("Calcula beca adicional " + this.getImporteBecaAdicional());
        if (this.getImporteBecaAdicional().compareTo(new BigDecimal(0)) > 0) {
            log.info("Calcula beca adicional  1");

            /**
             * 18/Ene/2011 El CP Raul Randeles solicito que la beca adicional no afecte a la cuota de inscripcion.
             *   Por lo tanto, a la constante de pctBecaAdicional se le asigno 0
             */
            BigDecimal pctBecaAdicional = this.getImporteBecaAdicional().multiply(new BigDecimal(ccfPctBecaAdicional));

            Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque(),
                    ccfstrBecaAdicionalID, ccfstrBecaAdicional,
                    pctBecaAdicional.doubleValue(), "C", "N", "T", alumno.getId_ccosto());
            mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                    + alumno.getBloque() + ccfstrBecaAdicionalID, movimiento);
            movimiento = null;
        }
    }

    public Double calculaSemanasInternado(Alumno alumno) throws Exception {
        Double semanas = 1.0;

        Locale local = new java.util.Locale("es", "MX", "Traditional_WIN");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", local);

        Map<String, Carga> mCargas = Carga.getCargas(sdf.format(new Date()));

        Carga carga = mCargas.get(alumno.getCarga_id());
        if (carga != null) {
            this.fechaInicial = sdf.parse(carga.getFInicio());
            this.fechaFinal = sdf.parse(carga.getFFinal());
            log.info("calculaSemanasInternado " + carga.getCargaId() + ", " + carga.getFInicio() + ", " + carga.getFFinal());
            semanas = CCobro.calculaSemanas(sdf.parse(carga.getFInicio()), sdf.parse(carga.getFFinal()));
            log.info("calculaSemanasInternado " + semanas);
        }

        return semanas;
    }

    public static Double calculaSemanas(Date fechaI, Date fechaF) throws Exception {
        Locale local = new java.util.Locale("es", "MX", "Traditional_WIN");

        Calendar fInicial = new GregorianCalendar(local);
        Calendar fFinal = new GregorianCalendar(local);

        fInicial.setTime(fechaI);
        fFinal.setTime(fechaF);

        Long diff = fFinal.getTimeInMillis() - fInicial.getTimeInMillis();
        //diff * (1/1000ms) * (1/60s) * (1/60m) * (1/24h) * (1/7d) => semanas
        Double op1 = new Double(diff.toString());
        Double op2 = new Double("604800000");

        Double semanas = op1 / op2;
        return semanas;
    }

    /*Obtener otros descuentos del alumno*/
    private void getDescuentos() throws Exception {
        Enumeration eDAlumnos = vDAlumnos.elements();

        while (eDAlumnos.hasMoreElements()) {
            DAlumno dAlumno = (DAlumno) eDAlumnos.nextElement();

            if (dAlumno.getMatricula().equals(alumno.getMatricula())) {
                /*Insertar en la tabla, descuentos*/
                Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                        alumno.getCarga_id(), alumno.getBloque(),
                        dAlumno.getTipo_descuento_id(),
                        dAlumno.getTipo_descuento(), dAlumno.getImporte(), "C",
                        dAlumno.getContabiliza().booleanValue() ? "S" : "N",
                        dAlumno.getAplica_en(), alumno.getId_ccosto());
                mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                        + alumno.getBloque() + ccfstrDesctoMExtID, movimiento);
                movimiento = null;
            }
        }
    }

    /*Determinar abonos por colportaje*/
    private void getColportaje() throws Exception {
//            if(mColportores.containsKey (alumno.getMatricula ()))
//            {
//                Colportor clp = (Colportor)mColportores.get (alumno.getMatricula ());
//
//                /*Validar la fecha del registro con el rango de fechas de la carga*/
//
//                Movimiento movimiento = new Movimiento (
//                        alumno.getMatricula (),
//                        alumno.getCarga_id (), alumno.getBloque (),
//                        ccfstrColportajeID,
//                        ccfstrColportaje, clp.getImporte (), "C",
//                        "N",
//                        "T",
//                        alumno.getId_ccosto ());
//
//                mMovimientos.put (alumno.getMatricula () + alumno.getCarga_id () +
//                        alumno.getBloque () + ccfstrColportajeID, movimiento);
//                movimiento = null;
//            }
        if (this.importeBonificaciones.compareTo(new BigDecimal("0")) > 0) {
            log.info("Obtiene bonificaciones de colportaje ");

            Movimiento movimiento = new Movimiento(
                    alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque(),
                    ccfstrColportajeID,
                    ccfstrColportaje, this.importeBonificaciones.doubleValue(), "C",
                    "N",
                    "T",
                    alumno.getId_ccosto());

            mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                    + alumno.getBloque() + ccfstrColportajeID, movimiento);
            movimiento = null;
        }
    }

    /**
     * 24/Enero/2010 - El CP Raul Randeles, solicito que al alumno institucional se le descuente el cobro de clima y el manejo de pagare.
     * 23/Agosto/2010 - El CP Arturo Sebastian, indico que los alumnos institucionales DEBEN pagar matricula, diezmo y manejo de pagare
     * @throws Exception
     */
    public void getInstitucional() throws Exception {
        if (this.alumno.getTAlumno_id().compareTo(Constant.ccfintTInstitucional) == 0) {
            //Insertar descuento
            log.info("Descuento institucional {}",this.ensenanza + ", " + this.internado + ", " + this.cobroClima + ", " + this.mPagare);
            Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque(),
                    ccfstrDesctoInstitucionalID, ccfstrDesctoInstitucional,
                    this.ensenanza + this.internado, "C", "S", "T", alumno.getId_ccosto());
            mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                    + alumno.getBloque() + ccfstrDesctoInstitucionalID + "T", movimiento);

//            this.beca.setBecaInstitucional((new BigDecimal(String.valueOf(this.ensenanza + this.internado))).subtract(this.beca.getContrato().getImporteAPagar()));
//            log.info("Beca institucional {}",this.beca.getBecaInstitucional());
            
            //Registar la beca excedente del alumno
//            Contrato.registraBecaExcedenteInstitucional(alumno.getMatricula(), alumno.getCarga_id(), alumno.getBloque(), 
//                    this.beca.getBecaInstitucional(), user, ds.getConnection());
            

            /*****************************************************************************/
            //Crear movimiento de diezmo, no debe contabilizar            
//            movimiento = new Movimiento(alumno.getMatricula(),
//                    alumno.getCarga_id(), alumno.getBloque(),
//                    ccfstrDiezmoBecaInstitucionalID, ccfstrDiezmoBecaInstitucional,
//                    this.beca.getContrato().getDiezmo().doubleValue(), "D", "N", "T", alumno.getId_ccosto());
            mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                    + alumno.getBloque() + ccfstrDiezmoBecaInstitucionalID + "T", movimiento);
            
//            log.info("Diezmo beca Institucional "+ this.beca.getContrato().getDiezmo());

        }
    }

    //	Obtiene pr?rrogas de pago
    public void getProrrogas() throws Exception {
        if (mProrrogas.containsKey(alumno.getMatricula())) {
            Prorroga prorroga = (Prorroga) mProrrogas.get(alumno.getMatricula());

            /*Inserta movimiento*/
            Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque(),
                    ccfstrProrrogasID, ccfstrProrrogas, prorroga.getSaldo(),
                    "C", "N", "T", alumno.getId_ccosto());
            mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                    + alumno.getBloque() + ccfstrProrrogasID, movimiento);
            movimiento = null;

            /*Inserta prorroga en pagares*/
            Pagare pagare = new Pagare(alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque(),
                    prorroga.getFecha(), prorroga.getSaldo(), "P");
            mPagares.add(pagare);
        }
    }

    /**
     * Agosto 13, 2008.  El C.P. Raul Randeles solicito que por default a todo alumno interno se le cobrara el clima
     * a excepcion de aquellos que se registraran en cobrosClima.
     *
     * 04/06/2011 - El Sr. Randeles indico que ya no se utiliza el cobro de clima
     *
     */
//    public void getCobrosClima() throws Exception {
//        if (this.alumno.getResidencia().equals("I") && !mCobrosClima.containsKey(alumno.getMatricula())) {
//            //CobroClima cClima = (CobroClima) mCobrosClima.get(alumno.getMatricula());
//
//            TParametros tParametros = (TParametros) mTParametros.get("CCL" + alumno.getCarga_id() + "I");
//            log.info(alumno.getMatricula() + "," + alumno.getCarga_id() + "," + tParametros.getValor());
//            this.cobroClima = new Double(tParametros.getValor());
//
//            /*Inserta movimiento*/
////                Movimiento movimiento = new Movimiento(alumno.getMatricula(),
////                                alumno.getCarga_id(), alumno.getBloque(),
////                                ccfstrCobroClimaID, ccfstrCobroClima, this.cobroClima,
////                                "D", "S", "-", alumno.getId_ccosto());
//
////                mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id() +
////					alumno.getBloque() + ccfstrCobroClimaID, movimiento);
////		movimiento = null;
//        }
//    }

    /*Calcular pago de contado*/
    public void pagoContado() throws Exception {
        //Si la forma de pago es de contado
        if (alumno.getFormaPago().equals("C")) {
            Double dblPagoMinimo = new Double(0);

            //Si el tipo de alumno es normal
            /**
             * Agosto 13, 2008.  El C.P. Raul Randeles solicito que a los hijos de obrero que pagaran de contado su parte
             * correspondiente de ensenanza e internado, se le aplicara el descuento de pago de contado
             */
            if (alumno.getTAlumno_id().compareTo(ccfintTANormal) == 0
                    || alumno.getTAlumno_id().compareTo(ccfintTHObrero) == 0) {
                Double dblDctoMatricula = new Double(0);
                Double dblDctoEnsenanza = new Double(0);
                Double dblDctoInternado = new Double(0);

                Double dctoHOEnsenanza = new Double(1);
                Double dctoHOInternado = new Double(1);

                //Si alumno es hijo de obrero, obtener porcentajes de ayuda
                if (alumno.getTAlumno_id().compareTo(ccfintTHObrero) == 0) {
                    dctoHOEnsenanza = new Double(1);
                    dctoHOInternado = new Double(1);
                    TParametros tParametros = (TParametros) mTParametros.get("HO" + this.alumno.getCarga_id() + "E");
                    try{
                    dctoHOEnsenanza = new Double(1.0 - new Double(tParametros.getValor()).doubleValue());
                    }catch(Exception e){
                        dctoHOEnsenanza = new Double(0.0);
                    }
                    tParametros = (TParametros) mTParametros.get("HO" + this.alumno.getCarga_id() + "I");
                    
                    try{
                    dctoHOInternado = new Double(1.0 - new Double(tParametros.getValor()).doubleValue());
                    }catch(Exception e){
                        dctoHOInternado = new Double(0.0);
                    }

                    //log.info("PagoContado - Dcto Contado HO - Ensenanza " + dctoHOEnsenanza + ", Internado " + dctoHOInternado);
                }

                //Obtener descuento de contado nacional
                if (alumno.getNacionalidad().equals("MEX")) {
                    /*Obtener descuento de contado en matricula para alumnos nacionales*/
                    if (mTParametros.containsKey("PCN" + alumno.getCarga_id()
                            + "M")) {
                        TParametros tParametros = (TParametros) mTParametros.get(
                                "PCN" + alumno.getCarga_id() + "M");

                        dblDctoMatricula = new Double(this.matricula.doubleValue() * new Double(
                                tParametros.getValor()).doubleValue());
                    }

                    /*Obtener descuento de contado en ensenanza para alumnos nacionales*/
                    if (mTParametros.containsKey("PCN" + alumno.getCarga_id()
                            + "E")) {
                        TParametros tParametros = (TParametros) mTParametros.get(
                                "PCN" + alumno.getCarga_id() + "E");
                        //log.info("Dcto. HO Ensenanza " + this.ensenanza + "*" + dctoHOEnsenanza + "*" + tParametros.getValor());
                        dblDctoEnsenanza = new Double(this.ensenanza.doubleValue() * dctoHOEnsenanza * new Double(
                                tParametros.getValor()).doubleValue());
                    }

                    /*Obtener descuento de contado en internado para alumnos nacionales*/
                    if (mTParametros.containsKey("PCN" + alumno.getCarga_id()
                            + "I")) {
                        TParametros tParametros = (TParametros) mTParametros.get(
                                "PCN" + alumno.getCarga_id() + "I");
                        //log.info("Dcto. HO Ensenanza " + this.internado + "*" + dctoHOInternado + "*" + tParametros.getValor());
                        dblDctoInternado = new Double(this.internado.doubleValue() * dctoHOInternado * new Double(
                                tParametros.getValor()).doubleValue());
                    }
                } else if (alumno.getNacionalidad().equals("EXT")) {
                    /*Obtener descuento de contado en matricula para alumnos no nacionales*/
                    if (mTParametros.containsKey("PCE" + alumno.getCarga_id()
                            + "M")) {
                        TParametros tParametros = (TParametros) mTParametros.get(
                                "PCE" + alumno.getCarga_id() + "M");

                        dblDctoMatricula = new Double(this.matricula.doubleValue() * new Double(
                                tParametros.getValor()).doubleValue());
                    }

                    /*Obtener descuento de contado en ensenanza para alumnos no nacionales*/
                    if (mTParametros.containsKey("PCE" + alumno.getCarga_id()
                            + "E")) {
                        TParametros tParametros = (TParametros) mTParametros.get(
                                "PCE" + alumno.getCarga_id() + "E");

                        dblDctoEnsenanza = new Double(this.ensenanza.doubleValue() * dctoHOEnsenanza * new Double(
                                tParametros.getValor()).doubleValue());
                    }

                    /*Obtener descuento de contado en internado para alumnos no nacionales*/
                    if (mTParametros.containsKey("PCE" + alumno.getCarga_id()
                            + "I")) {
                        TParametros tParametros = (TParametros) mTParametros.get(
                                "PCE" + alumno.getCarga_id() + "I");

                        dblDctoInternado = new Double(this.internado.doubleValue() * dctoHOInternado * new Double(
                                tParametros.getValor()).doubleValue());
                    }
                }

                /*Insertar en la tabla el descuento de contado sobre la matricula*/
                Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                        alumno.getCarga_id(), alumno.getBloque(),
                        ccfstrDesctoContadoID, ccfstrDesctoContado,
                        dblDctoMatricula, "C", "S", "M", alumno.getId_ccosto());
                mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                        + alumno.getBloque() + ccfstrDesctoContadoID + "M", movimiento);
                movimiento = null;

                /*Insertar en la tabla el descuento de contado sobre la ensenanza*/
                movimiento = new Movimiento(alumno.getMatricula(),
                        alumno.getCarga_id(), alumno.getBloque(),
                        ccfstrDesctoContadoID, ccfstrDesctoContado,
                        dblDctoEnsenanza, "C", "S", "E", alumno.getId_ccosto());
                mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                        + alumno.getBloque() + ccfstrDesctoContadoID + "E", movimiento);
                movimiento = null;

                /*Insertar en la tabla el descuento de contado sobre la internado*/
                movimiento = new Movimiento(alumno.getMatricula(),
                        alumno.getCarga_id(), alumno.getBloque(),
                        ccfstrDesctoContadoID, ccfstrDesctoContado,
                        dblDctoInternado, "C", "S", "I", alumno.getId_ccosto());
                mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                        + alumno.getBloque() + ccfstrDesctoContadoID + "I", movimiento);
                movimiento = null;
            }

            //Calcular pago minimo, que es en realidad el costo del bloque
            dblPagoMinimo = new Movimiento().getCostoBloque(mMovimientos);

            if (dblPagoMinimo.compareTo(new Double(0)) < 0) {
                dblPagoMinimo = new Double(dblPagoMinimo.doubleValue() * (-1));
            }

            /*Insertar en la tabla el pago minimo*/
            Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque(),
                    ccfstrCuotaInscripcionID, ccfstrCuotaInscripcion,
                    dblPagoMinimo, "D", "N", "T", alumno.getId_ccosto());
            mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                    + alumno.getBloque() + ccfstrDesctoContadoID, movimiento);
            movimiento = null;

            //Registro del folio del pago minimo de contado
            Pagare pagare = new Pagare(alumno.getMatricula(), alumno.getCarga_id(), alumno.getBloque(), "", dblPagoMinimo, ccfpstrCuotaInscripcion);

            mPagares.add(pagare);
        }
    }

    private void getPagoMinimo() throws Exception {
        if (alumno.getFormaPago().equals("P")) {
            //Calcular el pago m?nimo
            Double dblTotalPagoMinimo = new Double(0);

            Double dblPMMatricula = new Double(0);
            Double dblPMEnsenanza = new Double(0);
            Double dblPMInternado = new Double(0);

            /*Obtener porcentaje minimo a pagar por concepto de matricula*/
            if (mTParametros.containsKey("PMI" + alumno.getCarga_id() + "M")) {
                TParametros tParametros = (TParametros) mTParametros.get("PMI"
                        + alumno.getCarga_id() + "M");

                dblPMMatricula = new Double(tParametros.getValor());
            }

            /*Obtener porcentaje minimo a pagar por concepto de ensenanza*/
            if (mTParametros.containsKey("PMI" + alumno.getCarga_id() + "E")) {
                TParametros tParametros = (TParametros) mTParametros.get("PMI"
                        + alumno.getCarga_id() + "E");

                dblPMEnsenanza = new Double(tParametros.getValor());
            }

            /*Obtener porcentaje minimo a pagar por concepto de internado*/
            if (mTParametros.containsKey("PMI" + alumno.getCarga_id() + "I")) {
                TParametros tParametros = (TParametros) mTParametros.get("PMI"
                        + alumno.getCarga_id() + "I");

                dblPMInternado = new Double(tParametros.getValor());
            }

            /*Obtener total de descuentos y becas al concepto de matricula*/
            Map mTipoMov = new TreeMap();

            /*Indicar los descuentos que queremos obtener*/
            mTipoMov.put(ccfstrDesctoContadoID, ccfstrDesctoContadoID);
            mTipoMov.put(ccfstrDesctoHObreroID, ccfstrDesctoHObreroID);
            mTipoMov.put(ccfstrDesctoObreroID, ccfstrDesctoObreroID);
            mTipoMov.put(ccfstrBecasID, ccfstrBecasID);
            mTipoMov.put(ccfstrEnsenanzaExcentaID, ccfstrEnsenanzaExcentaID);
            mTipoMov.put(ccfstrDesctoInstitucionalID, ccfstrDesctoInstitucionalID);

            Double dMatricula = new Movimiento().getImporte(mMovimientos,
                    mTipoMov, "M", "C");

            Double dEnsenanza = new Movimiento().getImporte(mMovimientos,
                    mTipoMov, "E", "C");

            Double dInternado = new Movimiento().getImporte(mMovimientos,
                    mTipoMov, "I", "C");

            /*Obtener Pago Minimo*/
            dblTotalPagoMinimo = new Double((dblPMMatricula.doubleValue() * (this.matricula.doubleValue()
                    - dMatricula.doubleValue()))
                    + (dblPMEnsenanza.doubleValue() * (this.ensenanza.doubleValue()
                    - dEnsenanza.doubleValue()))
                    + (dblPMInternado.doubleValue() * ((this.internado.doubleValue())
                    - dInternado.doubleValue())));
            //log.info("Pago minimo " + dblPMMatricula + "*(" + this.matricula + "-" + dMatricula + ")+"
            //        + dblPMEnsenanza + "*(" + this.ensenanza + "-" + dEnsenanza + ")+"
            //        + dblPMInternado + "*(" + this.internado + "-" + dInternado + ")+"
            //        + "*" + dblPMEnsenanza);
            /*Insertar en la tabla el pago minimo*/
            Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque(),
                    ccfstrCuotaInscripcionID, ccfstrCuotaInscripcion,
                    dblTotalPagoMinimo, "D", "N", "T", alumno.getId_ccosto());
            mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                    + alumno.getBloque() + ccfstrCuotaInscripcionID, movimiento);
            movimiento = null;

            //Registro del folio del pago minimo de contado
            Pagare pagare = new Pagare(alumno.getMatricula(), alumno.getCarga_id(), alumno.getBloque(), "", dblTotalPagoMinimo, ccfpstrCuotaInscripcion);

            mPagares.add(pagare);
        }
    }

    /*Obtener saldo por manejo de pagare*/
    public void getManejoPagare() throws Exception {
        if (alumno.getFormaPago().equals("P")) {
            //Double mPagare = new Double(0);

            /*Si el alumno es nacional*/
            if (alumno.getNacionalidad().equals("MEX")) {
                /*Obtener manejo de pagare a estudiantes nacionales*/
                if (mTParametros.containsKey("MPN" + alumno.getCarga_id()
                        + "Z")) {
                    TParametros tParametros = (TParametros) mTParametros.get(
                            "MPN" + alumno.getCarga_id() + "Z");

                    mPagare = new Double(tParametros.getValor());
                    tParametros = null;
                }
            } else if (alumno.getNacionalidad().equals("EXT")) {
                /*Obtener manejo de pagare a estudiantes extranjeros*/
                if (mTParametros.containsKey("MPE" + alumno.getCarga_id()
                        + "Z")) {
                    TParametros tParametros = (TParametros) mTParametros.get(
                            "MPE" + alumno.getCarga_id() + "Z");

                    mPagare = new Double(tParametros.getValor());
                    tParametros = null;
                }
            }

            /*Insertar en la tabla el manejo de pagare*/
            Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque(),
                    ccfstrManejoPagareID, ccfstrManejoPagare, mPagare, "D",
                    "S", "-", alumno.getId_ccosto());
            mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                    + alumno.getBloque() + ccfstrManejoPagareID, movimiento);
            movimiento = null;
        }
    }

    /*Obtener pagares y calcular su importe*/
    private void getPagares() throws Exception {
        if (alumno.getFormaPago().equals("P")) {
            Integer nPagares = new Integer(0);

            /*Obtener numero de pagares*/
            if (mTParametros.containsKey("NP" + alumno.getCarga_id() + "Z")) {
                TParametros tParametros = (TParametros) mTParametros.get("NP"
                        + alumno.getCarga_id() + "Z");

                nPagares = new Integer(tParametros.getValor());
                tParametros = null;
            }

            /*Porcentajes de pago minimo*/
            Double pMMatricula = new Double(0);
            Double pMEnsenanza = new Double(0);
            Double pMInternado = new Double(0);

            /*Obtener porcentaje minimo a pagar por concepto de matricula*/
            if (mTParametros.containsKey("PMI" + alumno.getCarga_id() + "M")) {
                TParametros tParametros = (TParametros) mTParametros.get("PMI"
                        + alumno.getCarga_id() + "M");

                pMMatricula = new Double(tParametros.getValor());
            }

            /*Obtener porcentaje minimo a pagar por concepto de ensenanza*/
            if (mTParametros.containsKey("PMI" + alumno.getCarga_id() + "E")) {
                TParametros tParametros = (TParametros) mTParametros.get("PMI"
                        + alumno.getCarga_id() + "E");

                pMEnsenanza = new Double(tParametros.getValor());
            }

            /*Obtener porcentaje minimo a pagar por concepto de internado*/
            if (mTParametros.containsKey("PMI" + alumno.getCarga_id() + "I")) {
                TParametros tParametros = (TParametros) mTParametros.get("PMI"
                        + alumno.getCarga_id() + "I");

                pMInternado = new Double(tParametros.getValor());
            }

            /*Obtener total de descuentos y becas al concepto de matricula*/
            Map mTipoMov = new TreeMap();

            /*Indicar los descuentos que queremos obtener*/
            mTipoMov.put(ccfstrDesctoContadoID, ccfstrDesctoContadoID);
            mTipoMov.put(ccfstrDesctoHObreroID, ccfstrDesctoHObreroID);
            mTipoMov.put(ccfstrDesctoObreroID, ccfstrDesctoObreroID);
            mTipoMov.put(ccfstrBecasID, ccfstrBecasID);
            mTipoMov.put(ccfstrEnsenanzaExcentaID, ccfstrEnsenanzaExcentaID);


            Double dMatricula = new Movimiento().getImporte(mMovimientos,
                    mTipoMov, "M", "C");
            Double dEnsenanza = new Movimiento().getImporte(mMovimientos,
                    mTipoMov, "E", "C");
            Double dInternado = new Movimiento().getImporte(mMovimientos,
                    mTipoMov, "I", "C");

            if (this.alumno.getTAlumno_id().compareTo(Constant.ccfintTInstitucional) == 0) {
                dEnsenanza = this.ensenanza;
                dInternado = this.internado;
            }

            //Calcular importe de cada concepto
            Double dblMatricula = Currency.getRound(new Double(
                    ((this.matricula.doubleValue()
                    - dMatricula.doubleValue()) * (1
                    - pMMatricula.doubleValue())) / nPagares.doubleValue()),
                    new Integer(4));
            Double dblEnsenanza = Currency.getRound(new Double(
                    ((this.ensenanza.doubleValue()
                    - dEnsenanza.doubleValue()) * (1
                    - pMEnsenanza.doubleValue())) / nPagares.doubleValue()),
                    new Integer(4));
            Double dblInternado = Currency.getRound(new Double(
                    (((this.internado.doubleValue())
                    - dInternado.doubleValue()) * (1
                    - pMInternado.doubleValue())) / nPagares.doubleValue()),
                    new Integer(4));
            //Obtener manejo de pagare entre los pagares
            mTipoMov = new TreeMap();
            mTipoMov.put(ccfstrManejoPagareID, ccfstrManejoPagareID);

            Double mPagare = new Double(0);

            if (alumno.getNacionalidad().equals("MEX")) {
                if (mTParametros.containsKey("MPN" + alumno.getCarga_id()
                        + "Z")) {
                    TParametros tParametros = (TParametros) mTParametros.get(
                            "MPN" + alumno.getCarga_id() + "Z");

                    mPagare = new Double(tParametros.getValor());
                }
            } else {
                if (mTParametros.containsKey("MPE" + alumno.getCarga_id()
                        + "Z")) {
                    TParametros tParametros = (TParametros) mTParametros.get(
                            "MPE" + alumno.getCarga_id() + "Z");

                    mPagare = new Double(tParametros.getValor());
                }
            }

            mPagare = Currency.getRound(new Double(
                    mPagare.doubleValue() / nPagares.doubleValue()),
                    new Integer(4));

            //Calcular importe de cada pagare			
            Double iPagare = Currency.getRound(new Double(dblMatricula.doubleValue()
                    + dblEnsenanza.doubleValue()
                    + dblInternado.doubleValue() + mPagare.doubleValue()),
                    new Integer(2));
            //log.info("Pagare " + dblMatricula + "+" + dblEnsenanza + "+" + dblInternado + "+" + mPagare);
            /*Obtener pagares*/

            for (Integer temp = new Integer(1); temp.compareTo(nPagares) <= 0;
                    temp = new Integer(temp.intValue() + 1)) {
                if (mTParametros.containsKey("PGR" + temp
                        + alumno.getCarga_id() + "Z")) {
                    TParametros tParametros = (TParametros) mTParametros.get(
                            "PGR" + temp + alumno.getCarga_id() + "Z");

                    Locale local = new Locale("es", "MX", "Traditional_WIN");
                    SimpleDateFormat sdFormat = new SimpleDateFormat("dd-MMM-yyyy",
                            local);
                    SimpleDateFormat sdFormat2 = new SimpleDateFormat("dd-MM-yyyy",
                            local);

                    /*Validar la fecha que devuelve*/
                    try {
                        sdFormat2.parse(tParametros.getValor());
                    } catch (Exception e) {
                        throw new UMException("La fecha " + tParametros.getValor()
                                + " del pagare " + temp + " es invalida.");
                    }

                    /*Inserta pagare*/
                    Pagare pagare = new Pagare(alumno.getMatricula(),
                            alumno.getCarga_id(), alumno.getBloque(),
                            tParametros.getValor(), iPagare, ccfpstrPagare);
                    mPagares.add(pagare);

                    /**
                     * TODO 27-Enero-2006 Prof. Collins y CP Raul Randeles solicitaron que se validara si
                     * el alumno modifico su carga despues de haber solicitado un convenio; de ser asi
                     * se borrara el convenio actual, para realizar un nuevo convenio.
                     * Para detectar dicho cambio, se comparara el total de ensenanza grabado en el convenio
                     * contra el actual del ccobro.
                     * Inserta importe totalConvenios
                     * */
//					if (mConvenios.containsKey(alumno.getMatricula() + alumno.getCarga_id() + alumno.getBloque())) {
//                                            if (totalConvenio.compareTo(new Double(0)) > 0) {
//                                                if (!isConvenio.booleanValue()) {
//                                                    /**
//                                                     * TODO 16/Ago/2006 El C.P. Raul Randeles y C.P. Salatiel, solicitaron que el 35%
//                                                     * del convenio se descuente de la cuota de inscripcion, y el resto se divida en
//                                                     * pagares
//                                                     */
//                                                    pagare = new Pagare(alumno.getMatricula(),
//                                                            alumno.getCarga_id(), alumno.getBloque(),
//                                                            tParametros.getValor(),
//                                                            new Double((totalConvenio.doubleValue() * (1 - ccfPctConvenio.doubleValue())) / nPagares.doubleValue()), "C");
//                                                    isConvenio = new Boolean(true);
//                                                    mPagares.add(pagare);
//                                                }
//                                            }
//                       }
                    try {
                        /**
                         * TODO
                         * 04/Ago/2009 El C.P. Raul Randeles y el C.P. Joel Sebastian, solicitaron que el 20%
                         * de la beca adicional se descuente de la cuota de inscripcion, y el resto se divida en
                         * pagares.
                         *
                         * 18/Ene/2011 El CP Raul Randeles solicito que la beca adicional no afecte a la cuota de inscripcion.
                         *   Por lo tanto, a la constante de pctBecaAdicional se le asigno 0
                         * 
                         */
                        pagare = new Pagare(alumno.getMatricula(),
                                alumno.getCarga_id(), alumno.getBloque(),
                                tParametros.getValor(),
                                new Double((this.getImporteBecaAdicional().doubleValue() * (1 - ccfPctBecaAdicional.doubleValue())) / nPagares.doubleValue()), ccfpstrBecaAdicional);

                        mPagares.add(pagare);

                        /** ***
                         * 26-Enero-2011 - El CP Joel Sebastian solicito que las otras becas no descuenten diezmo
                         * 
                         * Ademas, acordaron que el total de la beca basica sea descontada de los pagares.
                         * Para todas las otras becas no se descuenta el diezmo
                         */
                        BigDecimal importeBeca = BigDecimal.ZERO;
                        try{
                            log.info("CCobro - ImporteBecaBasica {}",this.getImporteBecaBasica());
                            importeBeca = this.getImporteBecaBasica();
                            log.info("CCobro - ImporteExcedente {}",this.getImporteExcedente());
                            importeBeca = importeBeca.add(this.getImporteExcedente());
                            log.info("CCobro - ImporteBecaTotal {}",importeBeca);
                            
//                        if (!(beca.getContrato().getPlaza().isTipoOtraBeca(TipoOtraBecaEnum.PLAN_PROMOCIONAL_EDUCACION)
//                                || beca.getContrato().getPlaza().isTipoOtraBeca(TipoOtraBecaEnum.BECAS_MUNICIPALES)
//                                || beca.getContrato().getPlaza().isTipoOtraBeca(TipoOtraBecaEnum.BECAS_ESPECIALES_VRF)
//                                || beca.getContrato().getPlaza().isTipoOtraBeca(TipoOtraBecaEnum.BECAS_EDUCATIVAS_HOSPITAL)
//                                || beca.getContrato().getPlaza().isTipoOtraBeca(TipoOtraBecaEnum.BECA_RECTORIA)
//                                || beca.getContrato().getPlaza().isTipoOtraBeca(TipoOtraBecaEnum.COMEDOR)
//                                || beca.getContrato().getPlaza().isTipoOtraBeca(TipoOtraBecaEnum.BECAS_ESPECIALES)
//                                || beca.getContrato().getPlaza().isTipoOtraBeca(TipoOtraBecaEnum.PLANES_ESPECIALES_UNIONES))) {
//                            //importeBeca = this.getImporteBecaBasica().subtract(this.getImporteBecaBasica().movePointLeft(1));
//                            importeBeca = this.beca.getBecaBasicaNeto().add(this.getImporteExcedente());
//                            log.info("CCobro - ImporteBecaNeto {}",importeBeca);
//                        }
                        }catch(Exception e){
                            System.out.print("Error al evaluar si descontar diezmo o no ");
                            e.printStackTrace();
                            //
                        }
                        //log.info(importeBeca+", "+importeBeca.doubleValue() / nPagares.doubleValue());
                        pagare = new Pagare(alumno.getMatricula(),
                                alumno.getCarga_id(), alumno.getBloque(),
                                tParametros.getValor(),
                                new Double(importeBeca.doubleValue() / nPagares.doubleValue()), ccfpstrBecaBasica);

                        mPagares.add(pagare);
                    }catch(Exception ex){
                        ex.printStackTrace();
                        //El alumno no tiene beca...
                    }
                }
            }
        }
    }

    /*Calcula el cargo por matricula extemporanea*/
    public void getMatriculaExtemporanea() throws Exception {
        //Calcular matricula extemporanea
        //MEB - Es lo que se cobra el primer dia
        //MED - Es lo que se cobra por cada dia que pasa
        //DIA - Si dia es (uno) indica que es dia habil
        //      Si dia es (cero) indica que es dia inhabil
        //NDIAS - Numero de dias que hay de diferencia entre la fecha de hoy
        //        y la fecha de inicio de cobro de matricula extemporanea
        //FME	- Fecha de inicio de cobro de matricula extemporanea
        Double dblTMExtemporanea = new Double(0);

        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }

            if ((conn_noe == null) || conn_noe.isClosed()) {
                conn_noe = new Conexion().getConexionNoe(new Boolean(false));
            }

            String COMANDO = " SELECT TO_NUMBER(P2.VALOR, '99999') AS MEB, ";
            COMANDO += " TO_NUMBER(P1.VALOR, '99999') AS MED, ";
            COMANDO += " TO_DATE(SYSDATE, 'DD/MM/YY') - TO_DATE(P3.VALOR, 'DD/MM/YY') AS NDIAS ";
            COMANDO += " FROM noe.FES_PARAMGRALDET P1, noe.FES_PARAMGRALDET P2, noe.FES_PARAMGRALDET P3  ";
            COMANDO += " WHERE P1.CLAVE = 'SMED' ";
            COMANDO += " AND P1.CONCEPTO = 'Z' ";
            COMANDO = COMANDO + " AND P1.CARGA_ID = ? ";
            COMANDO += " AND P2.CLAVE = 'SMEB' ";
            COMANDO += " AND P2.CONCEPTO = 'Z' ";
            COMANDO = COMANDO + " AND P2.CARGA_ID = ? ";
            COMANDO += " AND P3.CLAVE = 'FME' ";
            COMANDO += " AND P3.CONCEPTO = 'Z' ";
            COMANDO = COMANDO + " AND P3.CARGA_ID = ? ";
            pstmt = conn_noe.prepareStatement(COMANDO);
            pstmt.setString(1, alumno.getCarga_id());
            pstmt.setString(2, alumno.getCarga_id());
            pstmt.setString(3, alumno.getCarga_id());
            rset = pstmt.executeQuery();

            if (rset.next()) {
                int intConta = 0; //Lo utilizamos para controlar el ciclo de dias
                int intNDias = rset.getInt("NDias");

                Double dblMEBase = new Double(rset.getDouble("MEB"));
                Double dblMEDia = new Double(rset.getDouble("MED"));

                //Si la cantidad de dias es mayor que cero indica que ya se debe comenzar a cobrar
                //matricula extemporanea
                if (intNDias >= 0) {
                    dblTMExtemporanea = dblMEBase;

                    //Sumamos un dia, que seria el primer dia en que se cobra matricual extemporanea
                    intConta = 1;

                    PreparedStatement pstmt2 = null;
                    ResultSet rset2 = null;

                    while (intConta <= intNDias) {
                        //Si el dia actual comienza con S, indica que es Sunday o Saturday, dias inhabiles
                        COMANDO = " SELECT CASE SUBSTR(TO_CHAR(SYSDATE-?, 'DY'),0,1) WHEN 'S' THEN 0 ELSE 1 END AS DIA ";
                        COMANDO += " FROM noe.FES_PARAMGRALDET ";
                        COMANDO += " WHERE CLAVE = 'FME' ";
                        COMANDO += " AND CONCEPTO = 'Z' ";
                        COMANDO = COMANDO + " AND CARGA_ID = ? ";
                        pstmt2 = conn_noe.prepareStatement(COMANDO);
                        pstmt2.setInt(1, intConta);
                        pstmt2.setString(2, alumno.getCarga_id());
                        rset2 = pstmt2.executeQuery();

                        if (rset2.next()) {
                            if (rset2.getInt("Dia") == 1) {
                                dblTMExtemporanea = new Double(dblTMExtemporanea.doubleValue()
                                        + dblMEDia.doubleValue());
                            }
                        }

                        intConta++;
                        pstmt2.close();
                        rset2.close();
                    }
                }
            }

            pstmt.close();
            rset.close();

            if (dblTMExtemporanea.compareTo(new Double(0)) != 0) {
                /*Insertar movimiento de matricula extemporanea*/
                Movimiento movimiento = new Movimiento(alumno.getMatricula(),
                        alumno.getCarga_id(), alumno.getBloque(),
                        ccfstrMExtemporaneaID, ccfstrMExtemporanea,
                        dblTMExtemporanea, "D", "S", "T", alumno.getId_ccosto());
                mMovimientos.put(alumno.getMatricula() + alumno.getCarga_id()
                        + alumno.getBloque() + ccfstrMExtemporaneaID, movimiento);
                movimiento = null;
            }
        } catch (Exception e) {
            throw new UMException(
                    "Error al obtener el importe de matricula extemporanea del alumno "
                    + alumno.getMatricula() + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }

            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }

            if (!conn_noe.isClosed()) {
                conn_noe.close();
                conn_noe = null;
            }
        }
    }

    /*Si la ejecucion del programa ha llegado hasta este punto, indica que no ha habido errores*/
    /*por lo que se procede a guardar los datos en la base de datos de las colecciones */
    /*movimientos, pagares, materias, comedor y de la clase alumno*/
    /*Para ello tenemos que prender una conexion a la base de datos con transaccion*/
    private void grabaDatos(Boolean inscrito) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {

            log.info("Datasource definido ");
            log.info("{}",ds);
            conn = ds.getConnection();            

            if ((conn == null) || conn.isClosed()) {
                throw new UMException("La conexion esta invalida");
            }
            conn.setAutoCommit(false);

            /*Limpiar datos de tablas de calculo de cobro*/
            /*Es necesario limpiar primero las tablas de movimientos, pagares y materias*/
            /*Y finalizar con la de ccobro*/
            Movimiento.limpiaTabla(conn, alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque());
            Pagare.limpiaTabla(conn, alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque());
            Materia.limpiaTabla(conn, alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque());
            Alumno.limpiaTabla(conn, alumno.getMatricula(),
                    alumno.getCarga_id(), alumno.getBloque());
            //No se puede limpiar la tabla de comedor ya que se borrarian las modificaciones de las comidas que
            //FEST haga al alumno
            AutorizaComida.limpiaTabla(conn, alumno.getMatricula(), alumno.getCarga_id(), alumno.getBloque());

            //Borra las operaciones de caja registradas de este alumno
            OperacionCajaCC.limpiaTabla(ds, alumno.getMatricula(), TipoOperacionCaja.CALCULO_COBRO);
            
            //Borrar informacion AFE
            Contrato.limpiaTabla(alumno.getMatricula(), alumno.getCarga_id(), alumno.getBloque(), conn);

            //alumno.borraStatus(conn_enoc);

            /*Grabar datos de calculo de cobro*/
            log.info("Graba Tabla Alumno");
            alumno.grabaTabla(conn, inscrito, online);
            log.info("Graba Tabla Movimiento");
            Movimiento.grabaTabla(conn, mMovimientos, alumno);

            /*Al insertar las materias del calculo de cobro, tambien se modificara el status de las materias */
            /*en el calculo de cobro*/
            log.info("Graba Tabla Materia");
            Materia.grabaTabla(conn, mMaterias, alumno, inscrito);
            log.info("Graba Tabla Pagare");
            Pagare.grabaTabla(conn, mPagares, alumno);

            log.info("Graba Tabla AutorizacionComida");
            AutorizaComida.grabaTabla(conn, (List) mComedor.get(alumno.getMatricula()), alumno.getMatricula(), alumno.getCarga_id());

            //Insertar operacion de caja
            OperacionCajaCC.grabaTabla(ds, alumno.getMatricula(), TipoOperacionCaja.CALCULO_COBRO,
                    Movimiento.getCuotaInscripcion(conn, alumno.getMatricula(), alumno.getCarga_id(), alumno.getBloque()), alumno.getId(), user);
            
            
            //Insertar contrato
            log.info("Evalua si el alumno tiene beca");
//            if (beca != null && beca.hasContrato()) {
//                //Evaluar si la beca adicional esta en base a porcentaje, entonces calcular importe
//                if(beca.getContrato().getPorcentaje()){
//                    beca.getContrato().setTotalBecaAdic((beca.getContrato().getImporte().movePointLeft(2)).multiply(new BigDecimal(this.ensenanza.toString())));
//                }
//                else{
//                    beca.getContrato().setTotalBecaAdic(beca.getContrato().getImporte());
//                }
//                log.info("Grabando contrato");
//                Contrato.grabaTabla(this.alumno.getMatricula(), this.alumno.getCarga_id(), this.alumno.getBloque(), getBeca(), conn);
//            }

            /*Si es el calculo de cobro definitivo, validar los avisos de alumnos pasivos/incobrables*/
            if (inscrito.booleanValue()) {
                log.info("Aviso");
                new Aviso().mavi_SetActivaAlumno(alumno.getMatricula(),
                        "CCOBRO", conn);

                /*Desactivar prorrogas, descuentos, becas, autorizaciones y paquetes de empleados*/
                log.info("Prorrogas");
                new Prorroga().desactivarProrrogas(alumno.getMatricula(),
                        conn);
                log.info("Descuentos");
                new DAlumno().desactivarDescuentos(alumno.getMatricula(),
                        conn);

                log.info("Autorizacion");
                new Empleado().desactivaAutorizacion(alumno.getMatricula(),
                        conn);
                new PaqueteAlumno().desactivaPaquete(alumno.getMatricula(),
                        conn);

                log.info("Hijo de empleado");
                new Beca().desactivaBeca(alumno.getMatricula(), conn);


//                log.info("Beca Basica - Contrato");
//                if (beca != null && beca.hasContrato()) {
//                    Convenio.desactivaContrato(alumno.getMatricula(), alumno.getCarga_id(), alumno.getBloque(), conn);
//                    log.info("Beca Adicional");
//                    Beca.desactivaBecaAdicional(alumno.getMatricula(), alumno.getCarga_id(), alumno.getBloque(), conn);
//                    if (beca.isColportor()) {
//                        log.info("Documentos de Colportor");
//                        Colportor.desactivaDocumentosColportor(alumno.getMatricula(), alumno.getCarga_id(), alumno.getBloque(), conn);
//                    }
//                }

                AutorizaComida.setInscrito(conn, (List) mComedor.get(alumno.getMatricula()),
                        alumno.getMatricula(), alumno.getCarga_id(), this.fechaInicial, this.fechaFinal);

                /**
                * 04/06/2011 - El Sr. Randeles indico que ya no se utiliza el cobro de clima
                */
//                if (mCobrosClima.containsKey(this.alumno.getMatricula())) {
//                    CobroClima cClima = new CobroClima();
//                    cClima.changeStatus(conn, this.alumno.getMatricula());
//                }

                //modificar status de hijo de obrero
                alumno.modificaHOStatus(conn);
                InstitucionHObrero.savePorcentajeInstituciones(this.alumno.getMatricula(), conn);

                

                /*Insertar registro de alumno inscrito*/
                log.info("Status");
                alumno.modificaStatus(conn);
            }

            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            e.printStackTrace();
            throw new UMException(
                    "Error al grabar los datos del calculo de cobro del alumno "
                    + alumno.getMatricula() + " " + e);
        } finally {
            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }


        }
    }

    public void getDatosGenerales(String matricula) throws Exception {
        //Obtener carga y bloque
        Alumno alumno = new Alumno();
        alumno.setMatricula(matricula);
        alumno.getCargaIDBloque();

        log.info("Se obtuvo la carga y bloque del alumno");
        String carga_id = alumno.getCarga_id();
        Integer bloque = alumno.getBloque();
        //log.info(carga_id + ", " + bloque);

        alumno = null;

        //Obtener datos del alumno
        log.info("{}",this);
        log.info("{}",this.alumno);

        if (this.alumno == null) {
            this.alumno = new Alumno();
        }

        this.alumno.getAlumnoCC(matricula, carga_id, bloque);
        log.info("Obtuvo el alumno del CC");

        Alumno tmp = new Alumno();
        tmp.setMatricula(this.alumno.getMatricula());
        tmp.getAlumno();
        this.alumno.setHOStatus(tmp.getHOStatus());
        //log.info(tmp.getMatricula() + ", " + tmp.getHOStatus());
        tmp = null;


    }

    public void getMovimientos(String matricula, String carga_id, Integer bloque)
            throws Exception {
        //Obtener movimientos
        Movimiento movimiento = new Movimiento();

        //Movimientos del costo del bloque
        this.mMovimientosCB = movimiento.getMovimientosCC(matricula, carga_id,
                bloque, "CB");

        //Movimientos de la cuota de inscripcion
        this.mMovimientosCI = movimiento.getMovimientosCC(matricula, carga_id,
                bloque, "CI");

        //Otros movimientos
        this.mMovimientosOT = movimiento.getMovimientosCC(matricula, carga_id,
                bloque, "OT");

        //Todos los movimientos    	
        this.mMovimientos = movimiento.getMovimientosCC(matricula, carga_id,
                bloque, "TODOS");
    }

    public void getMaterias(String matricula, String carga_id, Integer bloque)
            throws Exception {
        //Obtener materias
        Materia materia = new Materia();
        this.mMaterias = materia.getMateriasCC(matricula, carga_id, bloque);
    }

    public void getPagares(String matricula, String carga_id, Integer bloque)
            throws Exception {
        //Obtener pagares
        Pagare pagare = new Pagare();
        this.mPagares = pagare.getPagaresCC(matricula, carga_id, bloque);

    }

    public void getBecaBasicaAdicional(String matricula, String carga_id, Integer bloque) throws Exception {
        this.contrato = Contrato.getContratoInscrito(matricula, carga_id, bloque, alumno, this.ds.getConnection());
    }

    public void getDatosGenerales(String matricula, String carga_id, Integer bloque) throws Exception {
        //Obtener datos del alumno
        this.alumno.getAlumnoCC(matricula, carga_id, bloque);

        Alumno tmp = new Alumno();
        tmp.setMatricula(this.alumno.getMatricula());
        tmp.getAlumno();
        this.alumno.setHOStatus(tmp.getHOStatus());
        //log.info(tmp.getMatricula() + ", " + tmp.getHOStatus());

        //Obtener movimientos
        Movimiento movimiento = new Movimiento();

        //Movimientos del costo del bloque
        this.mMovimientosCB = movimiento.getMovimientosCC(matricula, carga_id,
                bloque, "CB");

        //Movimientos de la cuota de inscripcion
        this.mMovimientosCI = movimiento.getMovimientosCC(matricula, carga_id,
                bloque, "CI");

        //Otros movimientos
        this.mMovimientosOT = movimiento.getMovimientosCC(matricula, carga_id,
                bloque, "OT");

        //Todos los movimientos    	
        this.mMovimientos = movimiento.getMovimientosCC(matricula, carga_id,
                bloque, "TODOS");

        //Obtener materias
        Materia materia = new Materia();
        this.mMaterias = materia.getMateriasCC(matricula, carga_id, bloque);

        //Obtener pagares
        Pagare pagare = new Pagare();
        this.mPagares = pagare.getPagaresCC(matricula, carga_id, bloque);
    }

    /**
     * @return Returns the mMaterias.
     */
    public Map getMMaterias() {
        return mMaterias;
    }

    /**
     * @return Returns the mMovimientosCB.
     */
    public Map getMMovimientosCB() {
        return mMovimientosCB;
    }

    /**
     * @return Returns the mMovimientosCI.
     */
    public Map getMMovimientosCI() {
        return mMovimientosCI;
    }

    /**
     * @return Returns the mMovimientosOT.
     */
    public Map getMMovimientosOT() {
        return mMovimientosOT;
    }

    /**
     * @return Returns the mPagares.
     */
    public Vector getMPagares() {
        return mPagares;
    }

    /**
     * @return Returns the alumno.
     */
    public Alumno getAlumno() {
        return alumno;
    }

    /**
     * @return Returns the mMovimientos.
     */
    public Map getMMovimientos() {
        return mMovimientos;
    }

    public Map getCCobrosNoInscritos(Carga carga, Map mConCCobros) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }

            String COMANDO = "SELECT MATRICULA ";
            COMANDO += "FROM mateo.FES_CCOBRO ";
            COMANDO += "WHERE CARGA_ID = ? ";

            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, carga.getCargaId());
            rset = pstmt.executeQuery();

            while (rset.next()) {
                mConCCobros.put(rset.getString("Matricula"), null);
            }

            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new UMException(
                    "Error al obtener los alumnos no inscritos en la carga "
                    + carga.getCargaId() + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }

            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }

        return mConCCobros;
    }

    public Map getCCobrosInscritos(Carga carga, Map mInscritos) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }

            String COMANDO = "SELECT MATRICULA ";
            COMANDO += "FROM mateo.FES_CCOBRO ";
            COMANDO += "WHERE CARGA_ID = ? ";
            COMANDO += "AND INSCRITO = 'S' ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, carga.getCargaId());
            rset = pstmt.executeQuery();

            while (rset.next()) {
                mInscritos.put(rset.getString("Matricula"), null);
            }

            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new UMException(
                    "Error al obtener los alumnos inscritos en la carga "
                    + carga.getCargaId() + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }

            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }

        return mInscritos;
    }

    public List getCCobrosAlumnoInscrito(String matricula) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        List inscripciones = new ArrayList();

        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }

            String COMANDO = "SELECT MATRICULA, CARGA_ID, BLOQUE, NOMBRE, TO_CHAR(FECHA,'DD/MM/YYYY') FECHA, FACULTAD, CARRERA, NOMBRE_PLAN ";
            COMANDO += "FROM mateo.FES_CCOBRO ";
            COMANDO += "WHERE MATRICULA = ? ";
            COMANDO += "AND INSCRITO = 'S' ";
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                Alumno alumno = new Alumno();
                alumno.setMatricula(rset.getString("Matricula"));
                alumno.setCarga_id(rset.getString("Carga_id"));
                alumno.setBloque(new Integer(rset.getInt("Bloque")));
                alumno.setNombre(rset.getString("Nombre"));
                alumno.setFecha(rset.getString("Fecha"));
                alumno.setFacultad(rset.getString("Facultad"));
                alumno.setCarrera(rset.getString("Carrera"));
                alumno.setNombre_plan(rset.getString("Nombre_Plan"));
                inscripciones.add(alumno);
            }

            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new UMException(
                    "Error al obtener las inscripciones del alumno "
                    + matricula + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }

            if (rset != null) {
                rset.close();
                rset = null;
            }

            if (!conn.isClosed()) {
                conn.close();
                conn = null;
            }
        }

        return inscripciones;
    }

    /**
     * @return the contrato
     */
    public Contrato getContrato() {
        return contrato;
    }

    /**
     * @return the importeBecaBasica
     */
    public BigDecimal getImporteBecaBasica() {
        return importeBecaBasica;
    }

    /**
     * @return the importeBecaAdicional
     */
    public BigDecimal getImporteBecaAdicional() {
        return importeBecaAdicional;
    }
    /**
     * @return the importeExcedente
     */
    public BigDecimal getImporteExcedente() {
        return importeExcedente;
    }

    /**
     * @return the beca
     */
//    public BecaAdicionalVO getBeca() {
//        return beca;
//    }

    /**
     * Regresa el importe de cuota de inscripcion
     * @return
     * @throws Exception 
     */
    public BigDecimal getCuotaInscripcion() throws Exception {
        this.getMovimientos(this.alumno.getMatricula(), this.alumno.getCarga_id(), this.alumno.getBloque());
        return new BigDecimal(new Movimiento().getImporte(this.getMMovimientos(), "T", new Boolean(false)).toString());
    }
    
    public Boolean getConvenioEntregado(){
        return this.convenioEntregado;
    }
    
    public Boolean getTipoAlumnoInstitucional() {
        return this.tipoAlumnoInstitucional;
    }
}
