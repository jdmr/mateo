package mx.edu.um.mateo.inscripciones.service;

import java.util.Map;
import mx.edu.um.mateo.inscripciones.model.Institucion;

/**
 * @author semdariobarbaamaya
 */

public interface InstitucionManager {
     /**
     * Regresa todas los institucion
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene la institucion basandose en su id
     * @param id el id de la institucion
     * @return regresa la institucion que obtuvo mediante el id
     */
    public Institucion obtiene(final Long id);

    /**
     * Graba la informacion de la institucion
     * @param institucion el objeto que sera grabado
     */
    public void graba(Institucion institucion);

    /**
     * Elimina la institucion de la base de datos mediante su id
     * @param id el id de la institucion
     */
    public String elimina(final Long id);
}
