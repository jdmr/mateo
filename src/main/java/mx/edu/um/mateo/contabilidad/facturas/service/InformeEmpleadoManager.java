/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;

/**
 *
 * @author develop
 */
public interface InformeEmpleadoManager {

    /**
     * Regresa una lista de Informes.
     *
     * @param InformeEmpleado
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene una InformeEmpleado
     *
     * @param id
     * @return
     */
    public InformeEmpleado obtiene(final Long id);

    /**
     * graba informacion sobre una nacionalidad
     *
     * @param InformeEmpleado the object to be saved
     */
    public void graba(InformeEmpleado informe, Usuario usuario);

    public void actualiza(InformeEmpleado informe, Usuario usuario);

    /**
     * Cambia el status de la nacionalidad a I
     *
     * @param id el id de InformeEmpleado
     */
    public String elimina(final Long id);

    public void finaliza(InformeEmpleado informe, Usuario usuario);

    public void autorizar(InformeEmpleado informe, Usuario usuario);

    public void rechazar(InformeEmpleado informe, Usuario usuario);

    public List<InformeEmpleado> getInformes(Long empresaId);
    
    public void crea(InformeEmpleado informe, Usuario usuario);
}
