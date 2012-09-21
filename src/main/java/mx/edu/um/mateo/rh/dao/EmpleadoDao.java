/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.List;
import java.util.Set;
import mx.edu.um.mateo.rh.model.Empleado;

/**
 *
 * @author nujev
 */
public interface EmpleadoDao {

    /**
     * Gets empleado's information based on parameter values
     * 
     * @param empleado
     * @return empleado populated empleado object
     */
    public Empleado getEmpleado(final Empleado empleado);
    
    public Empleado getEmpleado(String clave);

    /**
     * Saves a empleado's information
     * @param empleado the object to be saved
     */    
    public void saveEmpleado(Empleado empleado);

    /**
     * An employee cannot be deleted
     * Removes a empleado from the database by id
     * @param id the empleado's id
     */
    //public void removeEmpleado(final Empleado empleado);
    /**
     * Retrieves empleados by nombre, apellido or clave
     * @param empleado
     * @return
     */
    public List searchEmpleado(final Empleado empleado);
    /**
     * Retrieves empleados by month of fechaNacimiento
     * @param empleado
     * @return
     */
    public List getEmpleadosBday(final Empleado empleado);
    
    public Empleado getEmpleadoClave(Empleado empleado);
    
    public List searchEmpleadoByClaveOrApPaterno(Empleado empleado);

}
