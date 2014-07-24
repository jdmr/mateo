/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.dao;

import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.CorreoMalFormadoException;

/**
 *
 * @author develop
 */
public interface PasarProveedoresFacturasDao {

    public void pasar(Usuario usuario)throws CorreoMalFormadoException;
}
