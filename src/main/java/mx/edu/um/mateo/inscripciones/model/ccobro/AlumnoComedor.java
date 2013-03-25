
package mx.edu.um.mateo.inscripciones.model.ccobro;

import java.util.Date;
import java.util.List;
import mx.edu.um.mateo.inscripciones.model.ccobro.comedor.ComidaType;

/**
 *
 * @author osoto
 * 
 * Esta clase fue creada de manera temporal par poder leer y afectar los registros del
 * alumno en el comedor que hibernate administra
 * 
 */
public class AlumnoComedor {
    private Alumno alumno;
    private List <ComidaType> comidas;
    private Date fechaInicial;
    private Date fechaFinal;
}
