/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.service;

import java.io.File;
import java.io.IOException;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author osoto
 */
public interface ImportarDatosManager {
    public void importaInformeDeGema(File file, Usuario user)  throws NullPointerException, IOException, Exception;
    public void importaDiezmos(File file, Usuario user) throws NullPointerException, IOException, Exception;
}
