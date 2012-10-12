/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.model.Empleado;
import org.springframework.security.core.userdetails.User;


/**
 *
 * @author osoto@um.edu.mxs
 */
public interface EmpleadoManager {
    /**
     * Regresa la lista de empleados dependiendo los parametros de filtro
     * @param params
     * @return 
     */
    public Map<String, Object> lista(Map<String, Object> params);
    /**
     * Regresa al empleado correspondiente al id
     * @param id
     * @return 
     */
    public Empleado obtiene(Long id) throws ObjectRetrievalFailureException;
    /**
     * Retrieves all of the empleados
     */
    public List getEmpleados (Empleado empleado);
    
    /**
     * Gets empleado's information based on id.
     * @param id the empleado's id
     * @return empleado populated empleado object
     */
    public Empleado getEmpleado (final Empleado empleado);
    
    /**
     * Saves a empleado's information
     * @param empleado the object to be saved
     */
    public void saveEmpleado (Empleado empleado);
    
    /**
     * El empleado nunca se borra....
     * @param id the empleado's id
     */
    public void removeEmpleado (final Empleado empleado);
    /**
     * El empleado nunca se borra...
     * @param id 
     * @return
     */
    public String elimina (final Long id);
    
    /**
     * Gets empleado's information based on id.
     * @param id the empleado's id
     * @return empleado populated empleado object
     */
    public List searchEmpleado (Empleado empleado);
    /**
     * regresa una lista de empleado por fecha y clave
     * @param id the empleado's id
     * @return empleado populated empleado object
     */
    
    public Object getEmpleadosBday (Empleado empleado);
    
    public Empleado getEmpleadoByClave(final Empleado empleado);
    
    public List searchEmpleadoByClaveOrApPaterno(Empleado empleado);
     /**
     * @deprecated 
     * graba dias  vacaciones disponibles de acuerdo a la antiguedad de empleado
     * @param solicitudSalida
     * @return
     */
    public void saveDiasVacacionesActuales(Empleado empleado, User user)throws Exception;

    /**
     * Regresa una lista de empleados activos filtrandolos por contabilidad
     */
    //public List getEmpleadosActivosByContabilidad(EmpleadoPuesto puesto) throws Exception;

    /**
     * Regresa en un map los empleados activos en las contabilidades indicadas.
     * La llave del map es la clave del empleado
     * El map acumula la lista de empleados de las distintas contabilidades indicadas.
     * @param ejercicio
     * @param contabilidades
     * @return
     * @throws java.lang.Exception
     */
    //public Map getEmpleadosActivosInMap(Ejercicio ejercicio, String... contabilidades) throws Exception;
    /*
     * Regresa una lista de empleados activos, filtrandolos por contabilidad, modalidad y tipo de empleado.
     * @param puesto Puede contener la contabilidad deseada
     * @param emp Puede contener la modalidad y el tipo de empleado
     * @return
     */
    //public List getEmpleadosActivosByContabilidadModalidadTipoEmpleado(EmpleadoPuesto puesto,Empleado empleado, String  sChecked []) throws Exception ;

    /**
     * Regresa el path de a donde se redireccionara dado que el empleado en cuestion no tiene asignado los estudios o un puesto
     * @param empleado
     * @return List
    
     */
    //public List verificarPuestoEstudios(Empleado empleado);
    /**
     * Regresela  los dias de vacaciones que le corresponden a un empleado nuevo, tomado en cuenta
     * su antiguedad denominacional , su experiencia fuera de la UM y la parte proporcional del año que trabajara
     * @param empleado
     * @return
     * @throws Exception
     */
    //public Integer getDiasVacacionesNuevoEmpleado(Empleado empleado) throws Exception ;

    /**
     * Regresa la lista de percepciones y deducciones
     * @param empleado
     * @return
     */
    //public Set getEmpleadoPerDeds(Empleado empleado);
    
}
