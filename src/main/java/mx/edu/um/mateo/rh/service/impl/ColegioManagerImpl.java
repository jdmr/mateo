/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

/**
 *
 * @author IrasemaBalderas
 */


import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.rh.dao.ColegioDao;
import mx.edu.um.mateo.rh.model.Colegio;
import mx.edu.um.mateo.rh.service.ColegioManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ColegioManagerImpl  implements ColegioManager {
    private ColegioDao dao;

    /**
     * Set the Dao for communication with the data layer.
     * @param dao
     */
    public void setColegioDao(ColegioDao dao) {
        this.dao = dao;
    }

    /**
     * @see mx.edu.um.rh.service.ColegioManager#getColegios(mx.edu.um.rh.model.Colegio)
     */
	@Override
	public Map<String, Object> getColegios(final Colegio colegio) {
		Map<String, Object> params = new HashMap<>();
		params.put(Constantes.CONTAINSKEY_COLEGIOS, dao.getColegio(colegio.getId()));
		return params;
	}
    /**
     * @see mx.edu.um.rh.service.ColegioManager#getColegio(String id)
     */
    @Override
    public Colegio getColegio(final Long id) {
        return dao.getColegio(id);
    }

    /**
     * @see mx.edu.um.rh.service.ColegioManager#saveColegio(Colegio colegio)
     */
    @Override
    public void saveColegio(Colegio colegio) {
        dao.saveColegio(colegio);
    }

    /**
     * @see mx.edu.um.rh.service.ColegioManager#removeColegio(String id)
     */
    @Override
    public void removeColegio(final Long id) {
        dao.removeColegio(id);
    }
}
