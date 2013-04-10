/*
 * Created on Jun 24, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package mx.edu.um.mateo.inscripciones.model.ccobro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.Vector;

import mx.edu.um.mateo.inscripciones.model.ccobro.tFinanciera.TFinanciera;
import mx.edu.um.mateo.inscripciones.model.ccobro.dinscribir.Carga;
import mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Constants;


/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Materia implements Constant {
    private String matricula;
    private String carga_id;
    private Integer bloque;
    private String curso_carga_id;
    private String curso_id;
    private String facultad_id;
    private String carrera_id;
    private String nombre_curso;
    private Double creditos;
    private Integer ciclo;
    private String grupo;
    private Integer modalidad_id;
    private String curso_plan_id;
    private Double costo_credito;
    private Double costo_curso;
    private String carga_id_curso;
    private Integer bloque_curso;
    private Date fechaInicio;
    private Date fechaFinal;
    private Connection conn;
    private Connection conn_noe;
    private Connection conn_enoc;
    
    /**
     * @param matricula
     * @param carga_id
     * @param bloque
     */
    public Materia(String matricula, String carga_id, Integer bloque) {
        super();
        this.matricula = matricula;
        this.carga_id = carga_id;
        this.bloque = bloque;
    }
    
    /**
     *
     *
     *
     */
    public Materia() {
        super();
    }
    
    /**
     * @param matricula
     * @param carga_id
     * @param bloque
     * @param curso_carga_id
     * @param curso_id
     * @param facultad_id
     * @param carrera_id
     * @param nombre_curso
     * @param creditos
     * @param ciclo
     * @param grupo
     * @param modalidad_id
     * @param curso_plan_id
     * @param carga_id_curso
     * @param bloque_curso
     */
    public Materia(String matricula, String carga_id, Integer bloque,
            String curso_carga_id, String curso_id, String facultad_id,
            String carrera_id, String nombre_curso, Double creditos, Integer ciclo,
            String grupo, Integer modalidad_id, String curso_plan_id,
            String carga_id_curso, Integer bloque_curso, Date fechaInicio, Date fechaFinal) {
        this.matricula = matricula;
        this.carga_id = carga_id;
        this.bloque = bloque;
        this.curso_carga_id = curso_carga_id;
        this.curso_id = curso_id;
        this.modalidad_id = modalidad_id;
        this.facultad_id = facultad_id;
        this.carrera_id = carrera_id;
        this.nombre_curso = nombre_curso;
        this.creditos = creditos;
        this.ciclo = ciclo;
        this.grupo = grupo;
        this.curso_plan_id = curso_plan_id;
        this.carga_id_curso = carga_id_curso;
        this.bloque_curso = bloque_curso;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
    }
    
    /**
     * @param matricula
     * @param carga_id
     * @param bloque
     * @param curso_carga_id
     * @param curso_id
     * @param facultad_id
     * @param carrera_id
     * @param nombre_curso
     * @param creditos
     * @param ciclo
     * @param grupo
     * @param modalidad_id
     * @param curso_plan_id
     * @param costo_credito
     * @param costo_curso
     * @param carga_id_curso
     * @param bloque_curso
     */
    public Materia(String matricula, String carga_id, Integer bloque,
            String curso_carga_id, String curso_id, String facultad_id,
            String carrera_id, String nombre_curso, Double creditos, Integer ciclo,
            String grupo, Integer modalidad_id, String curso_plan_id,
            Double costo_credito, Double costo_curso, String carga_id_curso,
            Integer bloque_curso) {
        this.matricula = matricula;
        this.carga_id = carga_id;
        this.bloque = bloque;
        this.curso_carga_id = curso_carga_id;
        this.curso_id = curso_id;
        this.facultad_id = facultad_id;
        this.carrera_id = carrera_id;
        this.nombre_curso = nombre_curso;
        this.creditos = creditos;
        this.ciclo = ciclo;
        this.grupo = grupo;
        this.modalidad_id = modalidad_id;
        this.curso_plan_id = curso_plan_id;
        this.costo_credito = costo_credito;
        this.costo_curso = costo_curso;
        this.carga_id_curso = carga_id_curso;
        this.bloque_curso = bloque_curso;
    }
    
    /**
     * @return Returns the bloque.
     */
    public Integer getBloque() {
        return bloque;
    }
    
    /**
     * @param bloque The bloque to set.
     */
    public void setBloque(Integer bloque) {
        this.bloque = bloque;
    }
    
    /**
     * @return Returns the bloque_curso.
     */
    public Integer getBloque_curso() {
        return bloque_curso;
    }
    
    /**
     * @param bloque_curso The bloque_curso to set.
     */
    public void setBloque_curso(Integer bloque_curso) {
        this.bloque_curso = bloque_curso;
    }
    
    /**
     * @return Returns the carga_id.
     */
    public String getCarga_id() {
        return carga_id;
    }
    
    /**
     * @param carga_id The carga_id to set.
     */
    public void setCarga_id(String carga_id) {
        this.carga_id = carga_id;
    }
    
    /**
     * @return Returns the carrera_id.
     */
    public String getCarrera_id() {
        return carrera_id;
    }
    
    /**
     * @param carrera_id The carrera_id to set.
     */
    public void setCarrera_id(String carrera_id) {
        this.carrera_id = carrera_id;
    }
    
    /**
     * @return Returns the ciclo.
     */
    public Integer getCiclo() {
        return ciclo;
    }
    
    /**
     * @param ciclo The ciclo to set.
     */
    public void setCiclo(Integer ciclo) {
        this.ciclo = ciclo;
    }
    
    /**
     * @return Returns the costo_credito.
     */
    public Double getCosto_credito() {
        return costo_credito;
    }
    
    /**
     * @param costo_credito The costo_credito to set.
     */
    public void setCosto_credito(Double costo_credito) {
        this.costo_credito = costo_credito;
    }
    
    /**
     * @return Returns the costo_curso.
     */
    public Double getCosto_curso() {
        return costo_curso;
    }
    
    /**
     * @param costo_curso The costo_curso to set.
     */
    public void setCosto_curso(Double costo_curso) {
        this.costo_curso = costo_curso;
    }
    
    /**
     * @return Returns the creditos.
     */
    public Double getCreditos() {
        return creditos;
    }
    
    /**
     * @param creditos The creditos to set.
     */
    public void setCreditos(Double creditos) {
        this.creditos = creditos;
    }
    
    /**
     * @return Returns the curso_carga_id.
     */
    public String getCurso_carga_id() {
        return curso_carga_id;
    }
    
    /**
     * @param curso_carga_id The curso_carga_id to set.
     */
    public void setCurso_carga_id(String curso_carga_id) {
        this.curso_carga_id = curso_carga_id;
    }
    
    /**
     * @return Returns the curso_id.
     */
    public String getCurso_id() {
        return curso_id;
    }
    
    /**
     * @param curso_id The curso_id to set.
     */
    public void setCurso_id(String curso_id) {
        this.curso_id = curso_id;
    }
    
    /**
     * @return Returns the curso_plan_id.
     */
    public String getCurso_plan_id() {
        return curso_plan_id;
    }
    
    /**
     * @param curso_plan_id The curso_plan_id to set.
     */
    public void setCurso_plan_id(String curso_plan_id) {
        this.curso_plan_id = curso_plan_id;
    }
    
    /**
     * @return Returns the facultad_id.
     */
    public String getFacultad_id() {
        return facultad_id;
    }
    
    /**
     * @param facultad_id The facultad_id to set.
     */
    public void setFacultad_id(String facultad_id) {
        this.facultad_id = facultad_id;
    }
    
    /**
     * @return Returns the grupo.
     */
    public String getGrupo() {
        return grupo;
    }
    
    /**
     * @param grupo The grupo to set.
     */
    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }
    
    /**
     * @return Returns the matricula.
     */
    public String getMatricula() {
        return matricula;
    }
    
    /**
     * @param matricula The matricula to set.
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    
    /**
     * @return Returns the nombre_curso.
     */
    public String getNombre_curso() {
        return nombre_curso;
    }
    
    /**
     * @param nombre_curso The nombre_curso to set.
     */
    public void setNombre_curso(String nombre_curso) {
        this.nombre_curso = nombre_curso;
    }

    public Date getFechaInicio() {
        return this.fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFinal() {
        return this.fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Map getMaterias(String matricula, String carga_id, Integer bloque,
            TFinanciera tFinanciera, Map mDMaterias, Map mAEmpleados, Map mComedor,
            Map mMovimientos, Alumno alumno, Map mBloques) throws Exception {
        Map mMaterias = new TreeMap();
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Vector vMaterias = new Vector();
        String cargaId = "";
        Boolean sw = new Boolean(false);
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            Double creditosExcentos = new Double(0);
            Double ensenanzaExcenta = new Double(0);
            //System.out.println("Materias 1");
            
            if (mAEmpleados.containsKey(matricula)) {
                Empleado empleado = (Empleado) mAEmpleados.get(matricula);
                creditosExcentos = new Double(empleado.getCreditos_autorizados()
                .doubleValue());
                
                if (creditosExcentos.compareTo(new Double(0)) == 0) {
                    throw new Exception("El alumno " + matricula +
                            " es Empleado UM y no tiene registrada ayuda de estudios");
                }
            }
            
            //System.out.println("Materias 1.9 "+creditosExcentos);
            
            Integer nivel = new Integer(0);
            String COMANDO = "SELECT NIVEL_ID " +
                    "FROM ENOC.CAT_CARRERA " +
                    "WHERE CARRERA_ID = ? ";
            
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, alumno.getCarrera_id());
            rset = pstmt.executeQuery();
            
            if(rset.next()){
                nivel = new Integer(rset.getInt("Nivel_ID"));
            }
            
            pstmt.close();
            rset.close();
            
            /**
             * TODO Obtener los componentes que el alumno ha cursado
             * No se leen las materias por inscribir (M) ni las dadas de baja (3)
             * */
            Map mComponentes = new TreeMap();
            COMANDO = "SELECT MC.CURSO_ID " +
                    "FROM enoc.KRDX_CURSO_ACT KCA, enoc.MAPA_CURSO MC " +
                    "WHERE KCA.CODIGO_PERSONAL = ? " +
                    "AND (KCA.TIPOCAL_ID != 'M' AND KCA.TIPOCAL_ID != '3') " +
                    "AND MC.TIPOCURSO_ID = 3 " +
                    "AND MC.CURSO_ID = KCA.CURSO_ID ";
            
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            rset = pstmt.executeQuery();
            
            while(rset.next()){
                mComponentes.put(rset.getString("Curso_ID"),null);
            }
            
            pstmt.close();
            rset.close();

            /**
             * TODO Obtener los remediales que el alumno ha cursado
             * No se leen las materias por inscribir (M) ni las dadas de baja (3)
             * */
            Map mRemediales = new TreeMap();
            COMANDO = "SELECT MC.CURSO_ID " +
                    "FROM enoc.KRDX_CURSO_ACT KCA, enoc.MAPA_CURSO MC " +
                    "WHERE KCA.CODIGO_PERSONAL = ? " +
                    "AND (KCA.TIPOCAL_ID != 'M' AND KCA.TIPOCAL_ID != '3') " +
                    "AND MC.TIPOCURSO_ID = 4 " +
                    "AND MC.CURSO_ID = KCA.CURSO_ID ";

            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            rset = pstmt.executeQuery();

            while(rset.next()){
                mRemediales.put(rset.getString("Curso_ID"),null);
            }

            pstmt.close();
            rset.close();
            
            //System.out.println("Materias 2");
            
            /*Seria bueno poder ordenar esta busqueda por costo de credito, ya que asi en el caso de ayuda */
            /*por creditos se beneficia al alumno*/
            COMANDO = "SELECT F.FACULTAD_ID, C.CARRERA_ID, MC.CURSO_ID, MC.NOMBRE_CURSO, ";
            COMANDO += "MC.CICLO, MC.CREDITOS, COALESCE(CG.GRUPO, 'U') AS GRUPO, CG.CURSO_CARGA_ID, ";
            COMANDO += "P.PLAN_ID, COALESCE(CG.BLOQUE_ID, 0) BLOQUE, CG.CARGA_ID, CG.MODALIDAD_ID, ";
            COMANDO += "CG.F_INICIO, CG.F_FINAL ";
            COMANDO += "FROM enoc.CARGA_GRUPO_CURSO CGC, enoc.CARGA_GRUPO CG, enoc.MAPA_CURSO MC, enoc.KRDX_CURSO_ACT KCA, ";
            COMANDO += "enoc.MAPA_PLAN P, enoc.CAT_CARRERA C, enoc.CAT_FACULTAD F ";
            COMANDO += "WHERE CGC.CURSO_CARGA_ID = CG.CURSO_CARGA_ID ";
            COMANDO += "AND KCA.CURSO_CARGA_ID = CGC.CURSO_CARGA_ID ";
            COMANDO += "AND MC.CURSO_ID = CGC.CURSO_ID ";
            COMANDO += "AND KCA.CURSO_ID = MC.CURSO_ID ";
            COMANDO += "AND P.PLAN_ID = MC.PLAN_ID ";
            COMANDO += "AND F.FACULTAD_ID = C.FACULTAD_ID ";
            COMANDO += "AND C.CARRERA_ID = P.CARRERA_ID ";
            COMANDO += "AND KCA.TIPOCAL_ID = 'M' ";
            COMANDO += "AND KCA.CODIGO_PERSONAL = ? ";
            COMANDO += "AND CG.CARGA_ID = ? ";
            COMANDO += "ORDER BY MC.CREDITOS DESC ";
            
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setString(2, carga_id);
            rset = pstmt.executeQuery();
            //System.out.println("Materias 3");

            Locale local = new java.util.Locale (Constants.LOCALE_LANGUAGE, Constants.LOCALE_COUNTRY, Constants.LOCALE_VARIANT);
            SimpleDateFormat sdf = new SimpleDateFormat (Constants.DATE_SHORT_HUMAN_PATTERN, local);
            
            /*Leer cursos a inscribir*/
            while (rset.next()) {
                if (!sw.booleanValue()) {
                    cargaId = rset.getString("carga_id");
                    sw = new Boolean(true);
                }

                //Obtener el numero de dias que durara la materia
                Calendar fechaI = new GregorianCalendar();
                fechaI.setTimeInMillis(rset.getDate("F_Inicio").getTime());

                Calendar fechaF = new GregorianCalendar();
                fechaF.setTimeInMillis(rset.getDate("F_Final").getTime());
                
                //System.out.println("Materias 4");
                
                if (cargaId.equals(rset.getString("carga_id"))) {
                    vMaterias.add(rset.getString("Curso_Carga_ID") + "@" +
                            rset.getString("Curso_ID") + "@" +
                            rset.getString("Facultad_ID") + "@" +
                            rset.getString("Carrera_ID") + "@" +
                            rset.getString("Nombre_Curso") + "@" +
                            new Double(rset.getDouble("Creditos")) + "@" +
                            new Integer(rset.getString("Ciclo")) + "@" +
                            rset.getString("Grupo") + "@" +
                            new Integer(rset.getString("Modalidad_ID")) + "@" +
                            rset.getString("Plan_ID") + "@" +
                            rset.getString("Carga_ID") + "@" +
                            new Integer(rset.getString("Bloque")) + "@" +
                            sdf.format(fechaI.getTime()) + "@" +
                            sdf.format(fechaF.getTime()));
                    
                    //Guardar bloques en el map de bloques
                    mBloques.put(cargaId+rset.getString("Bloque"),null);
                    
                    
                } else {
                    throw new Exception("El alumno " + matricula +
                            " tiene asignadas materias de mas de un periodo academico " +
                            cargaId + " y " + rset.getString("carga_Id") +
                            "<br>Es posible que se haya cambiado de carrera sin ser cancelada su carga academica correspondiente " +
                            "<br>Si tal es el caso, favor de comunicarse a la ext. 120 ");
                }
            }
            
            pstmt.close();
            rset.close();
            
            
            Enumeration eMaterias = vMaterias.elements();
            //System.out.println("Materias 5");
            
            while (eMaterias.hasMoreElements()) {
                /*Validar si la materia se puede inscribir*/
                String mCarga_id = carga_id;
                Integer mBloque = bloque;
                String eMateria = (String) eMaterias.nextElement();
                //System.out.println("Materias 5.1 ");
                
                
                StringTokenizer strTkn = new StringTokenizer(eMateria, "@");
                
                String curso_carga_id = strTkn.nextToken();
                String curso_id = strTkn.nextToken();
                String facultad_id = strTkn.nextToken();
                String carrera_id = strTkn.nextToken();
                String nombre_curso = strTkn.nextToken();
                Double creditos = new Double(strTkn.nextToken());
                Integer ciclo = new Integer(strTkn.nextToken());
                String grupo = strTkn.nextToken();
                Integer modo = new Integer(strTkn.nextToken());
                String curso_plan_id = strTkn.nextToken();
                String mCarga_id2 = strTkn.nextToken();
                Integer mBloque2 = new Integer(strTkn.nextToken());
                Date fechaInicio = sdf.parse(strTkn.nextToken());
                Date fechaFinal = sdf.parse(strTkn.nextToken());
                //System.out.println("Materias 5.2 ");
                
                if (!this.isMateriaValida(mCarga_id, mBloque).booleanValue()) {
                    throw new Exception("La materia " + nombre_curso +
                            " no es valida, ya que el bloque " + mCarga_id + "-" +
                            mBloque + " de la misma esta caducado ");
                }
                
                //System.out.println("Materias 6");
                
                /*
                 * Si la carga no es de verano, el bloque es mayor que 1 y el plan de estudios no es posgrado
                 * se genera un error.
                 * 
                 * 13/12/2012 - Se elimino la validacion del bloque, ya que en el calculo del internado valida el cobro para los 
                 * universitarios durante el semestre regular.
                 */
                
                
//                if (!Carga.isVerano(mCarga_id) && mBloque.compareTo(new Integer(1)) > 0 &&
//                        (nivel.compareTo(new Integer(3)) != 0 && nivel.compareTo(new Integer(4)) != 0)){
//                    throw new Exception("<p>El alumno "+alumno.getMatricula()+" tiene asignada la materia "+nombre_curso+
//                            ".<br>Esta materia esta registrada en el bloque "+mBloque+" el cual para nivel medio y universitario es invalido" +
//                            ".<br>Favor de comunicarse a la extension 120.");
//                }
                
                Materia materia = new Materia(matricula, carga_id, bloque,
                        curso_carga_id, curso_id, facultad_id, carrera_id,
                        nombre_curso, creditos, ciclo, grupo, modo,
                        curso_plan_id, mCarga_id2, mBloque2,fechaInicio,fechaFinal);
                //System.out.println("Materias 7");
                
                /*Validar que el bloque de cada materia sea mayor o igual al bloque del alumno*/
                //System.out.println("Blques "+mBloque+","+alumno.getBloque());
                if(materia.getBloque_curso().compareTo(alumno.getBloque()) < 0)
                    throw new Exception("No es posible calcular costo de la materia "+materia.getNombre_curso()+" del bloque "+materia.getBloque_curso()+" en el bloque "+alumno.getBloque());
                
                /**
                 * TODO Componentes
                 * La Profa. Chio solicito el 17 de Nov de 2005 se validara si el alumno esta inscribiendo
                 * un componente que previamente habia inscrito y reprobado.  De ser asi se le cobrara al alumno.
                 * El numero de creditos a cobrar seran 2, pero la materia no debe tener credito alguno
                 */
                /*Asignar numero de creditos a los componentes*/
                if(mComponentes.containsKey(materia.getCurso_id()))
                    materia.setCreditos(new Double(2));

                /**
                 * TODO Remediales
                 * La Profa. Chio solicito el 5 de Agosto de 2010 que se validara si el alumno esta inscribiendo
                 * un remedial que previamente habia inscrito y reprobado.  De ser asi se le cobrara al alumno.
                 * El numero de creditos a cobrar seran 1, pero la materia no debe tener credito alguno
                 */
                /*Asignar numero de creditos a los componentes*/
                if(mRemediales.containsKey(materia.getCurso_id()))
                    materia.setCreditos(new Double(1));
                
                /**
                 * 19-Agosto-2008 El C.P. Raul Randeles, el Prof. Israel Escobedo y la Dra. Ruth estuvieron de acuerdo en que a la preparatoria
                 * se le haga el calculo de cobro tomando en cuenta la modalidad del alumno y no la de la materia, ya que los alumnos
                 * de la diurna y la nocturna llevan los mismos cursos pero se les cobra diferente
                 */
                
                if(alumno.getFacultad_id().equals("107")) {
                    modo = alumno.getModalidad_id();
                    //System.out.println("Alumno de la preparatoria "+alumno.getMatricula()+" modalidad "+modo);
                }
                
                /*Obtener costo por credito y costo del curso*/
                //System.out.println("Materias 8, carrera_id "+carrera_id+", modo "+modo);
                if (!tFinanciera.getDetalles().containsKey(carrera_id + modo)) {
                    throw new Exception("El costo por credito de la carga " +
                            carga_id + ", la carrera " + carrera_id +
                            ", en la modalidad " + modo +
                            " no esta capturado en la tabla financiera");
                }
                
                //System.out.println("Materias 9 "+alumno.getReligion());
                if (!tFinanciera.getEncabezado().getClasificacion().containsKey(new Integer(
                        alumno.getReligion()))) {
                    throw new Exception("El costo por credito de la carga " +
                            carga_id + ", la carrera " + carrera_id +
                            ", en la modalidad " + modo +
                            " y en la clasificacion financiera " +
                            alumno.getReligion() +
                            " no esta capturado en la tabla financiera");
                }
                
                
                /**
                 * TODO Costo del credito
                 * --18-Agosto-2005, El Prof. Collins solicito que el costo por credito se tomara en base a la carrera del alumno
                 * y no de la materia como estaba hasta hoy. (carrera_id)
                 * --18-Nov-2005, La Prepa esta uniendo los grupos de la diurna con la nocturna, por lo que el cobro
                 * de la materia debe ser en base a la carrera del alumno
                 * --21-Agosto-2006, El Prof. Collins solicito que los alumnos puedan tener un plan primario y un plan secundario
                 * tomandose el costo por credito del plan correspondiente.  Esto es porque hay alumnos que estan estudiando carreras
                 * simultaneas
                 */
                /*Verificar modalidad de extensiones UM*/
                if(modo.compareTo(new Integer(7)) == 0){
                    materia.setCosto_curso(new Double(tFinanciera.getCMateria(
                            alumno.getCarrera_id(), modo, alumno.getReligion()).doubleValue()));
                    
                    if(materia.getCosto_curso().compareTo(new Double(0)) == 0){
                        //Buscar costo de credito
                        materia.setCosto_credito(new Double(
                                tFinanciera.getCCredito(alumno.getCarrera_id(),modo, alumno.getReligion()).doubleValue()));
//                        materia.setCosto_curso(new Double(
//                                materia.getCreditos().doubleValue() * tFinanciera.getCCredito(
//                                alumno.getCarrera_id(), modo, alumno.getReligion()).doubleValue()));
                        materia.setCosto_curso(new Double(materia.getCreditos().doubleValue() * materia.getCosto_credito().doubleValue()));
                    } else{
                        materia.setCosto_credito(new Double(0));
                    }
                } else{
                    try{
                        materia.setCosto_credito(tFinanciera.getCCredito(alumno.getCarrera_id(),
                                modo, alumno.getReligion()));
                        
                        //System.out.println("Materias 9.3");
                        
                        materia.setCosto_curso(new Double(
                                materia.getCreditos().doubleValue() * materia.getCosto_credito().doubleValue()));
                    }catch(Exception e){
                        //System.out.println("Materias 9.4");
                        materia.setCosto_credito(tFinanciera.getCMateria(alumno.getCarrera_id(),
                                modo, alumno.getReligion()));
                        if(materia.getCosto_credito() == null){
                            throw new Exception("Error al intentar obtener el costo del credito o de la materia <br>"+e);
                        }
                        materia.setCosto_curso(materia.getCosto_credito());
                    }
                }
                /*Verificar si la clave de cursocarga_id se encuentra en el map de descuentos de materias*/
                Double descuento = new Double(0);
                //System.out.println("Materias 10");
                
                if (mDMaterias.containsKey(materia.getCurso_id())) {
                    DMateria dMateria = (DMateria) mDMaterias.get(materia.getCurso_id());
                    
                    /*Obtener descuento*/
                    descuento = new Double(dMateria.getImporte().doubleValue() / 100);
                }
                
                //System.out.println("Materias 11 ");
                
                /*Modificar valor del costo del curso*/
                materia.setCosto_curso(new Double(
                        materia.getCosto_curso().doubleValue() * (1 -
                        descuento.doubleValue())));
                //System.out.println("Materias 12 ");
                
                /*Si el alumno es interno*/
                //System.out.println("getMaterias 1 " + matricula + "@" +
                //        carga_id + "@" + materia.getMatricula() +
                //       materia.getCarga_id_curso() + materia.getBloque_curso()+"@"+alumno.getResidencia().length());
                
//                if (alumno.getResidencia().equals("I")) {
//                    //System.out.println(alumno.getResidencia());
//                    /*Registrar carga y bloque para el comedor*/
//                    /*Es necesario validar si el map ya contiene una valor para el alumno*/
//                    /*De ser asi, no se debe modificar dicho valor*/
//                    /*Los valores de la carga y el bloque a guardar en el map del comedor, deben ser lo de las materias*/
//                    if (!mComedor.containsKey(materia.getMatricula() +
//                            materia.getCarga_id_curso() +
//                            materia.getBloque_curso())) {
//                        //System.out.println("getMaterias 2 " + matricula + "@" +
//                                carga_id + "@" + materia.getMatricula() +
//                                materia.getCarga_id_curso() +
//                                materia.getBloque_curso());
//
//                        AutorizaComida autorizaComida = new AutorizaComida(materia.getMatricula(),
//                                materia.getCarga_id_curso(),
//                                materia.getBloque_curso(), fechaInicio, fechaFinal);
//
//                        mComedor.put(materia.getMatricula() +
//                                materia.getCarga_id_curso() +
//                                materia.getBloque_curso(), autorizaComida);
//
//                        //System.out.println("getMaterias 3 " + matricula + "@" +
//                                carga_id + "@" + materia.getMatricula() +
//                                materia.getCarga_id_curso() +
//                                materia.getBloque_curso());
//                    }
//                }
                
                /*Quitar numero de creditos a los componentes*/
                if(mComponentes.containsKey(materia.getCurso_id()))
                    materia.setCreditos(new Double(0));
                
                mMaterias.put(matricula + "@" + carga_id + "@" + bloque + "@" +
                        curso_carga_id, materia);
                
                if(creditosExcentos.compareTo(new Double(0)) > 0){
                    //System.out.println("calculando ensenanza excenta " +ensenanzaExcenta+", "+creditosExcentos+", "+materia.getCreditos());
                    /*Calcula la ensenanza excenta*/
                    /*Si los creditos excentos son mayores o igual que los creditos de la materia*/
                    /*se acumula el total de la ense?anza excenta y se disminuye el valor de los creditos excentos*/
                    /*Si los creditos excentos con menores que los creditos de la materia solo se descuenta*/
                    /*el valor equivalente multiplicando el valor de los creditos excentos por el costo del credito de la materia*/
                    if (creditosExcentos.compareTo(materia.getCreditos()) >= 0) {
                        ensenanzaExcenta = new Double(ensenanzaExcenta.doubleValue() +
                                materia.getCosto_curso().doubleValue());
                        creditosExcentos = new Double(creditosExcentos.doubleValue() -
                                materia.getCreditos().doubleValue());
                    } else if (creditosExcentos.compareTo(new Double(0)) > 0) {
                        ensenanzaExcenta = new Double(ensenanzaExcenta.doubleValue() +
                                (materia.getCosto_credito().doubleValue() * creditosExcentos.doubleValue()));
                        creditosExcentos = new Double(0);
                    }
                    //System.out.println("calculando ensenanza excenta " +ensenanzaExcenta+", "+creditosExcentos);
                }
            }
            
            //System.out.println("Materias 13 "+ensenanzaExcenta);
            
            mComponentes = null;
            
            /*Guardar movimiento de la ensenanza excenta*/
            if (ensenanzaExcenta.compareTo(new Double(0)) > 0) {
                Movimiento movimiento = new Movimiento(matricula, carga_id,
                        bloque, ccfstrEnsenanzaExcentaID,
                        ccfstrEnsenanzaExcenta, ensenanzaExcenta, "C", "N",
                        "E", alumno.getId_ccosto());
                mMovimientos.put(matricula + carga_id + bloque +
                        ccfstrEnsenanzaExcentaID, movimiento);
            }
        } catch (Exception e) {
            throw new Exception("Error al obtener las materias " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if ((conn_enoc != null) && !conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
        
        return mMaterias;
    }
    
    /**
     * @return Returns the carga_id_curso.
     */
    public String getCarga_id_curso() {
        return carga_id_curso;
    }
    
    /**
     * @param carga_id_curso The carga_id_curso to set.
     */
    public void setCarga_id_curso(String carga_id_curso) {
        this.carga_id_curso = carga_id_curso;
    }
    
    /**
     * @return Returns the modalidad_id.
     */
    public Integer getModalidad_id() {
        return modalidad_id;
    }
    
    /**
     * @param modalidad_id The modalidad_id to set.
     */
    public void setModalidad_id(Integer modalidad_id) {
        this.modalidad_id = modalidad_id;
    }
    
    /*Verificar si la materia se puede inscribir*/
    private Boolean isMateriaValida(String carga_id, Integer bloque)
    throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        java.sql.Date dtFAltaBaja = null;
        java.sql.Date dtFecha = null;
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            /*Obtener fecha del sistema, y fecha limite de inscripcion de bloque*/
            String COMANDO = "SELECT TO_DATE(F_CIERRE, 'dd-mm-yy') FALTABAJA, ";
            COMANDO += "TO_DATE(SYSDATE, 'dd-mm-yy') FECHA ";
            COMANDO += "FROM enoc.CARGA_BLOQUE ";
            COMANDO += "WHERE CARGA_ID = ? ";
            COMANDO += "AND BLOQUE_ID = ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, carga_id);
            pstmt.setInt(2, bloque.intValue());
            rset = pstmt.executeQuery();
            
            if (rset.next()) {
                dtFAltaBaja = rset.getDate("FAltaBaja");
                dtFecha = rset.getDate("Fecha");
            }
            
            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new Exception("Error al validar las materias " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
        
        if (dtFecha.compareTo(dtFAltaBaja) <= 0) {
            return new Boolean(true);
        }
        
        return new Boolean(false);
    }
    
    /*Obtener valor de la ensenanza, validando descuentos de materia*/
    public Double getTotalEnsenanza(Map mMaterias) throws Exception {
        Double ensenanza = new Double(0);
        Double descuento = new Double(0);
        
        String key = "";
        
        Materia materia = null;
        DMateria dMateria = null;
        
        /*Recorrer mapa de las materias*/
        Iterator iMaterias = mMaterias.keySet().iterator();
        
        while (iMaterias.hasNext()) {
            key = (String) iMaterias.next();
            
            /*Obtener la materia*/
            materia = (Materia) mMaterias.get(key);
            
            /*Acumular ensenanza*/
            ensenanza = new Double(ensenanza.doubleValue() +
                    materia.getCosto_curso().doubleValue());
        }
        
        return ensenanza;
    }
    
    public static void limpiaTabla(Connection conn, String matricula,
            String carga_id, Integer bloque) throws Exception {
        PreparedStatement pstmt = null;
        
        try {
            String COMANDO = "DELETE " + "FROM MATEO.FES_CC_MATERIA " +
                    "WHERE MATRICULA = ? " + "AND CARGA_ID = ? " +
                    "AND BLOQUE = ? ";
            
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setString(2, carga_id);
            pstmt.setInt(3, bloque.intValue());
            pstmt.execute();
            pstmt.close();
        } catch (Exception e) {
            throw new Exception("Error al inicializar las materias del alumno " +
                    matricula + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }
    }
    
    /*Grabar movimientos del alumno en la base de datos*/
    public static void grabaTabla(Connection conn, 
            Map mMaterias, Alumno alumno, Boolean inscrito)
            throws Exception {
        PreparedStatement pstmt = null;
        
        try {
            Iterator iMaterias = mMaterias.keySet().iterator();
            
            while (iMaterias.hasNext()) {
                Materia materia = (Materia) mMaterias.get((String) iMaterias.next());
                
                if (materia.getMatricula().equals(alumno.getMatricula ())) {
                    /*Insertar materia*/
                    String COMANDO = "INSERT INTO MATEO.FES_CC_MATERIA ";
                    COMANDO += "(ID, MATRICULA, CARGA_ID, BLOQUE, CURSO_ID, CREDITOS, CICLO, ";
                    COMANDO += "GRUPO, CURSO_CARGA_ID, CURSO_PLAN_ID, COSTO_CREDITO, ";
                    COMANDO += "NOMBRE_CURSO, FACULTAD_ID, CARRERA_ID, COSTO_CURSO, ";
                    COMANDO += "BLOQUE_CURSO, CARGAID_CURSO, MODALIDAD_ID, CCOBRO_ID, VERSION) ";
                    COMANDO += "VALUES ";
                    COMANDO += "((SELECT MAX(ID)+1 FROM MATEO.FES_CC_MATERIA),?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0) ";
                    
                    pstmt = conn.prepareStatement(COMANDO);
                    pstmt.setString(1, materia.getMatricula());
                    pstmt.setString(2, materia.getCarga_id());
                    pstmt.setInt(3, materia.getBloque().intValue());
                    pstmt.setString(4, materia.getCurso_id());
                    pstmt.setDouble(5, materia.getCreditos().doubleValue());
                    pstmt.setString(6, materia.getCiclo().toString());
                    pstmt.setString(7, materia.getGrupo());
                    pstmt.setString(8, materia.getCurso_carga_id());
                    pstmt.setString(9, materia.getCurso_plan_id());
                    pstmt.setDouble(10, materia.getCosto_credito().doubleValue());
                    pstmt.setString(11, materia.getNombre_curso());
                    pstmt.setString(12, materia.getFacultad_id());
                    pstmt.setString(13, materia.getCarrera_id());
                    pstmt.setDouble(14, materia.getCosto_curso().doubleValue());
                    pstmt.setInt(15, materia.getBloque_curso().intValue());
                    pstmt.setString(16, materia.getCarga_id_curso());
                    pstmt.setInt(17, materia.getModalidad_id().intValue());
                    pstmt.setInt (18, alumno.getId ().intValue ());                    
                    pstmt.execute();
                    pstmt.close();
                    //System.out.println("Inserto en ccobro la materia"+materia.getNombre_curso()+", "+materia.getCurso_carga_id()+" del alumno "+materia.getMatricula());
                    
                    //System.out.println("Bandera de inscrito "+inscrito);
                    if (inscrito.booleanValue()) {
                        /*Modificar el status de las materias de la carga de materias*/
                        COMANDO = "UPDATE ENOC.KRDX_CURSO_ACT ";
                        COMANDO += "SET TIPOCAL_ID = 'I' ";
                        COMANDO += "WHERE TIPOCAL_ID = 'M' ";
                        COMANDO += "AND CODIGO_PERSONAL = ? ";
                        COMANDO += "AND CURSO_CARGA_ID = ? ";
                        pstmt = conn.prepareStatement(COMANDO);
                        pstmt.setString(1, materia.getMatricula());
                        pstmt.setString(2, materia.getCurso_carga_id());
                        pstmt.execute();
                        pstmt.close();
                        //System.out.println("Inserto en krdx_curso_act la materia"+materia.getNombre_curso()+", "+materia.getCurso_carga_id()+" del alumno "+materia.getMatricula());
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(
                    "Error al insertar las materias del calculo de cobro del alumno " +
                    alumno.getMatricula () + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }
    }
    
    public Map getMateriasCC(String matricula, String carga_id, Integer bloque)
    throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Map mMaterias = new TreeMap();
        
        try {
            if ((conn == null) || conn.isClosed()) {
                conn = new Conexion().getConexionMateo(new Boolean(false));
            }
            
            String COMANDO =
                    "SELECT CURSO_CARGA_ID, CURSO_ID, FACULTAD_ID, CARRERA_ID, NOMBRE_CURSO, CREDITOS, " +
                    "CICLO, GRUPO, CURSO_PLAN_ID, COSTO_CREDITO, COSTO_CURSO, CARGAID_CURSO, BLOQUE_CURSO, " +
                    "COALESCE(MODALIDAD_ID,0) MODALIDAD_ID " + "FROM mateo.FES_CC_MATERIA " +
                    "WHERE MATRICULA = ? " + "AND CARGA_ID = ? " +
                    "AND BLOQUE = ? ";
            
            pstmt = conn.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setString(2, carga_id);
            pstmt.setInt(3, bloque.intValue());
            rset = pstmt.executeQuery();
            
            while (rset.next()) {
                Materia materia = new Materia(matricula, carga_id, bloque,
                        rset.getString("Curso_Carga_ID"),
                        rset.getString("Curso_ID"),
                        rset.getString("Facultad_ID"),
                        rset.getString("Carrera_ID"),
                        rset.getString("Nombre_Curso"),
                        new Double(rset.getDouble("Creditos")),
                        new Integer(rset.getString("Ciclo")),
                        rset.getString("Grupo"),
                        new Integer(rset.getString("Modalidad_ID")),
                        rset.getString("Curso_Plan_ID"),
                        new Double(rset.getDouble("Costo_Credito")),
                        new Double(rset.getDouble("Costo_Curso")),
                        rset.getString("CargaID_Curso"),
                        new Integer(rset.getInt("Bloque_Curso")));
                
                mMaterias.put(matricula + carga_id + bloque +
                        rset.getString("Curso_Carga_ID"), materia);
            }
            
            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new Exception("Error al obtener las materias del alumno " +
                    matricula + " en la carga " + carga_id + " y el bloque " +
                    bloque + "<br>" + e);
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
        
        return mMaterias;
    }
    
    public Map getMateriasInscritas(String matricula, String carga_id,
            Integer bloque) throws Exception {
        Map mMaterias = new TreeMap();
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed()) {
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            }
            
            /*Seria bueno poder ordenar esta busqueda por costo de credito, ya que asi en el caso de ayuda */
            /*por creditos se beneficia al alumno*/
            String COMANDO = "SELECT F.FACULTAD_ID, C.CARRERA_ID, MC.CURSO_ID, MC.NOMBRE_CURSO, ";
            COMANDO += "MC.CICLO, MC.CREDITOS, COALESCE(CG.GRUPO, 'U') AS GRUPO, CG.CURSO_CARGA_ID, ";
            COMANDO += "P.PLAN_ID, COALESCE(CG.BLOQUE_ID, 0) BLOQUE, CG.CARGA_ID, CG.MODALIDAD_ID, ";
            COMANDO += "CG.F_INICIO, CG.F_FINAL ";
            COMANDO += "FROM enoc.CARGA_GRUPO_CURSO CGC, enoc.CARGA_GRUPO CG, enoc.MAPA_CURSO MC, enoc.KRDX_CURSO_ACT KCA, ";
            COMANDO += "enoc.MAPA_PLAN P, enoc.CAT_CARRERA C, enoc.CAT_FACULTAD F ";
            COMANDO += "WHERE CGC.CURSO_CARGA_ID = CG.CURSO_CARGA_ID ";
            COMANDO += "AND KCA.CURSO_CARGA_ID = CGC.CURSO_CARGA_ID ";
            COMANDO += "AND MC.CURSO_ID = CGC.CURSO_ID ";
            COMANDO += "AND KCA.CURSO_ID = MC.CURSO_ID ";
            COMANDO += "AND P.PLAN_ID = MC.PLAN_ID ";
            COMANDO += "AND F.FACULTAD_ID = C.FACULTAD_ID ";
            COMANDO += "AND C.CARRERA_ID = P.CARRERA_ID ";
            COMANDO += "AND KCA.TIPOCAL_ID = 'I' ";
            COMANDO += "AND KCA.CODIGO_PERSONAL = ? ";
            COMANDO += "AND CG.CARGA_ID = ? ";
            COMANDO += "AND CG.BLOQUE_ID = ? ";
            
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, matricula);
            pstmt.setString(2, carga_id);
            pstmt.setInt(3, bloque.intValue());
            
            rset = pstmt.executeQuery();
            
            /*Leer cursos a inscribir*/
            while (rset.next()) {
                Calendar fechaI = new GregorianCalendar();
                fechaI.setTimeInMillis(rset.getDate("F_Inicio").getTime());

                Calendar fechaF = new GregorianCalendar();
                fechaF.setTimeInMillis(rset.getDate("F_Final").getTime());

                Materia materia = new Materia(matricula, carga_id, bloque,
                        rset.getString("curso_carga_id"),
                        rset.getString("curso_id"),
                        rset.getString("facultad_id"),
                        rset.getString("carrera_id"),
                        rset.getString("nombre_curso"),
                        new Double(rset.getDouble("creditos")),
                        new Integer(rset.getInt("ciclo")),
                        rset.getString("grupo"),
                        new Integer(rset.getInt("modalidad_id")),
                        rset.getString("plan_id"), rset.getString("Carga_id"),
                        new Integer(rset.getInt("Bloque")),
                        fechaI.getTime(),
                        fechaF.getTime());
                
                mMaterias.put(matricula + carga_id + bloque +
                        rset.getString("curso_carga_id"), materia);
            }
        } catch (Exception e) {
            throw new Exception(
                    "Error al obtener las materias inscritas del alumno " +
                    matricula + "<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
        
        return mMaterias;
    }
    
    public void activarMaterias(String matricula, Map mMaterias,
            Connection conn_enoc) throws Exception {
        PreparedStatement pstmt = null;
        String COMANDO = null;
        
        try {
            Iterator iMaterias = mMaterias.keySet().iterator();
            
            while (iMaterias.hasNext()) {
                Materia materia = (Materia) mMaterias.get(iMaterias.next());
                COMANDO = "UPDATE enoc.KRDX_CURSO_ACT " + "SET TIPOCAL_ID = 'M' " +
                        "WHERE CODIGO_PERSONAL = ? " + "AND CURSO_CARGA_ID = ? ";
                pstmt = conn_enoc.prepareStatement(COMANDO);
                pstmt.setString(1, matricula);
                pstmt.setString(2, materia.getCurso_carga_id());
                pstmt.executeUpdate();
                pstmt.close();
            }
        } catch (Exception e) {
            throw new Exception(
                    "Error al intentar activar las materias del alumno " +
                    matricula + "<br>" + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
        }
    }
    
    public Map tieneCargaMaterias(Carga carga, Map mConCargas) throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        
        try {
            if ((conn_enoc == null) || conn_enoc.isClosed())
                conn_enoc = new Conexion().getConexionEnoc(new Boolean(false));
            
            String COMANDO = "SELECT DISTINCT CODIGO_PERSONAL ";
            COMANDO += "FROM enoc.KRDX_CURSO_ACT ";
            COMANDO += "WHERE CURSO_CARGA_ID LIKE ? ";
            pstmt = conn_enoc.prepareStatement(COMANDO);
            pstmt.setString(1, carga.getCargaId()+"%");
            rset = pstmt.executeQuery();
            
            while (rset.next()) {
                mConCargas.put(rset.getString("Codigo_Personal"), null);
            }
            
            pstmt.close();
            rset.close();
        } catch (Exception e) {
            throw new Exception(
                    "Error al obtener los alumnos que tienen cargas academicas en la carga " +
                    carga.getCargaId() + " " + e);
        } finally {
            if (pstmt != null) {
                pstmt.close();
                pstmt = null;
            }
            
            if (rset != null) {
                rset.close();
                rset = null;
            }
            
            if (!conn_enoc.isClosed()) {
                conn_enoc.close();
                conn_enoc = null;
            }
        }
        
        return mConCargas;
    }
}
