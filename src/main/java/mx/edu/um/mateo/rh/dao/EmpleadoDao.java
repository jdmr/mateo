
package mx.edu.um.mateo.rh.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.model.Empleado;

/**
 *
 * @author nujev
 */
public interface EmpleadoDao {

    /**
     * Regresa una lista de todos los empleados
     * @param params
     * @return 
     */
    public Map<String, Object> lista(Map<String, Object> params);
    /**
     * Regresa al empleado correspondiente al id
     * @param id
     * @return 
     */
    public Empleado obtiene(Long id);
    /**
     * Graba un empleado con su empresa, la cual obtiene del usuario
     * @param empleado
     * @param usuario
     * @return 
     */
    public Empleado graba(Empleado empleado, Usuario usuario);
    /**
     * Graba un empleado, SIN empresa. Este metodo se utiliza para pruebas DAO
     * @param empleado
     * @return 
     */
    public Empleado graba(Empleado empleado);
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
     *
     * @param empleado the object to be saved
     */
    public void saveEmpleado(Empleado empleado);

    /**
     * An employee cannot be deleted Removes a empleado from the database by id
     *
     * @param id the empleado's id
     */
    // public void removeEmpleado(final Empleado empleado);
    /**
     * Retrieves empleados by nombre, apellido or clave
     *
     * @param empleado
     * @return
     */
    public List<Empleado> searchEmpleado(final Empleado empleado);

    /**
     * Retrieves empleados by month of fechaNacimiento
     *
     * @param empleado
     * @return
     */
    public List<Empleado> getEmpleadosBday(final Empleado empleado);

    public Empleado getEmpleadoClave(Empleado empleado);

    public List<Empleado> searchEmpleadoByClaveOrApPaterno(Empleado empleado);
    /**
     * Filtra empleados por contabilidad o centro de costo
     *
     * @param ccosto
     * @return
     */
    //public List searchEmpleadoByCCosto(EmpleadoPuesto puesto);
    /**
     * Filtra empleados por contabilidad, modalidad y tipo de empleado
     *
     * @param puesto Puede contener la contabilidad deseada
     * @param emp Puede contener la modalidad y el tipo de empleado
     * @return
     */
    //public List searchEmpleadoByCCostoModalidadTipoEmpleado(EmpleadoPuesto puesto,Empleado emp,String sChecked []);
    /**
     * Regresa las percepciones y deducciones del empleado
     *
     * @param empleado
     * @return
     */
    //public Set getEmpleadoPerDeds(Empleado empleado);
}
