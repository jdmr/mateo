/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.service;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeProveedor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.AutorizacionCCPlInvalidoException;
import mx.edu.um.mateo.general.utils.FaltaArchivoPDFException;
import mx.edu.um.mateo.general.utils.FaltaArchivoXMLException;
import mx.edu.um.mateo.general.utils.FormaPagoNoSeleccionadaException;
import mx.edu.um.mateo.general.utils.MonedaNoSeleccionadaException;

/**
 *
 * @author develop
 */
public interface InformeProveedorManager {

    /**
     * Regresa una lista de Informes.
     *
     * @param params
     * @param InformeProveedor
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Lista de informes flistrados por id de empleado en session.
     *
     * @param params
     * @return
     */
    public Map<String, Object> listaEmpleado(Map<String, Object> params);

    public Map<String, Object> revisar(Map<String, Object> params);

    /**
     * Obtiene una InformeProveedor
     *
     * @param id
     * @return
     */
    public InformeProveedor obtiene(final Long id);

    /**
     * graba informacion sobre un proveedor
     *
     * @param informeProveedor
     * @param usuario
     * @throws mx.edu.um.mateo.general.utils.AutorizacionCCPlInvalidoException
     * @throws mx.edu.um.mateo.general.utils.MonedaNoSeleccionadaException
     * @throws mx.edu.um.mateo.general.utils.FormaPagoNoSeleccionadaException
     */
    public void graba(InformeProveedor informeProveedor, Usuario usuario) throws AutorizacionCCPlInvalidoException, MonedaNoSeleccionadaException, FormaPagoNoSeleccionadaException;

    public void actualiza(InformeProveedor informeProveedor, Usuario usuario);

    /**
     * Cambia el status de la proveedor a I
     *
     * @param id el id de InformeEmpleado
     * @return
     */
    public String elimina(final Long id);

    public void finaliza(InformeProveedor informeProveedor, Usuario usuario) throws FaltaArchivoPDFException,
            FaltaArchivoXMLException;

    public void autorizar(InformeProveedor informeProveedor, Usuario usuario);

    public void rechazar(InformeProveedor informeProveedor, Usuario usuario);

    public List<InformeProveedor> getInformes(Long empresaId);

    public void crea(InformeProveedor informe, Usuario usuario);
}
