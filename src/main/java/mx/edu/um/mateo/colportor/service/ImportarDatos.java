/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.service;

import java.io.File;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author osoto
 */
public interface ImportarDatos {
    public void importaInformeDeGema(File file, Usuario user)  throws Exception;
}
