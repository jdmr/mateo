package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.inscripciones.model.Institucion;

/* @author semdariobarbaamaya */

public interface InstitucionDao {
    
    public Map<String, Object> lista(Map<String, Object> params);
    
    public Institucion obtiene(final Long id);
    
    public Institucion actualiza(Institucion institucion);
    
    public void graba(Institucion institucion);
    
    public String elimina(final Long id);
}
