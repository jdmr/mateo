package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.dao.PeriodoDao;
import mx.edu.um.mateo.inscripciones.model.Periodo;
import mx.edu.um.mateo.inscripciones.service.PeriodoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author semdariobarbaamaya
 */

@Transactional
@Service
public class PeriodoManagerImpl extends BaseDao implements PeriodoManager{
    
    @Autowired
    private PeriodoDao dao;
    
    /**
     *  @see mx.edu.um.mateo.inscripciones.service.PeriodoManager#lista(java.util.Map) 
     */
    @Override
     public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     *  @see mx.edu.um.mateo.inscripciones.service.PeriodoManager#obtiene(java.lang.String) 
     */
    @Override
    public Periodo obtiene(final Long id) {
        return dao.obtiene(new Long(id));
    }

    /**
     * @see mx.edu.um.mateo.inscripciones.service.PeriodoManager#graba(mx.edu.um.mateo.inscripciones.model.Periodo, mx.edu.um.mateo.general.model.Usuario) 
     */
    @Override
    public void graba(Periodo periodo) {
         log.debug("Hasta aqui ENTRA {}", periodo);
        if(periodo.getId()==null){
            periodo.setStatus(Constantes.STATUS_ACTIVO);
        }
        dao.graba(periodo);
    }

    /**
     *  @see mx.edu.um.mateo.inscripciones.service.PeriodoManager#elimina(java.lang.String) 
     */
    @Override
    public String elimina(final Long id) {
        return dao.elimina(id);
    }
    
}
