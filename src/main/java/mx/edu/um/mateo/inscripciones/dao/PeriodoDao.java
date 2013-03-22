package mx.edu.um.mateo.inscripciones.dao;

import java.util.Map;
import mx.edu.um.mateo.inscripciones.model.Periodo;

public interface PeriodoDao {
    
    public Map<String, Object> lista(Map<String, Object> params);
    
    public Periodo obtiene(final Long id);
    
    public Periodo actualiza(Periodo periodo);
    
    public void graba(Periodo periodo);
    
    public String elimina(final Long id);
    
}
