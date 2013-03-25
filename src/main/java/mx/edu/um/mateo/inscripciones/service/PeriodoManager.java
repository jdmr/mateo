package mx.edu.um.mateo.inscripciones.service;

import java.util.Map;
import mx.edu.um.mateo.inscripciones.model.Periodo;

/*  @author semdariobarbaamaya */

public interface PeriodoManager {
     /**
     * Regresa todas los periodos
     */
    public Map<String, Object> lista(Map<String, Object> params);

    /**
     * Obtiene los periodos basandose en su id
     * @param id el id del periodo
     * @return regresa el periodo que obtuvo mediante el id
     */
    public Periodo obtiene(final Long id);

    /**
     * Graba la informacion del periodo
     * @param periodo el objeto que sera grabado
     */
    public void graba(Periodo periodo);

    /**
     * Elimina el periodo de la base de datos mediante su id
     * @param id el id del periodo
     */
    public String elimina(final Long id);
}
