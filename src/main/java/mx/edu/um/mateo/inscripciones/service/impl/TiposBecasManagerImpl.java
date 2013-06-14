/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.service.impl;

import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inscripciones.dao.TiposBecasDao;
import mx.edu.um.mateo.inscripciones.model.TiposBecas;
import mx.edu.um.mateo.inscripciones.service.TiposBecasManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class TiposBecasManagerImpl implements TiposBecasManager{
    @Autowired
     private TiposBecasDao dao;    
    
      @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

  
    @Override
    public TiposBecas obtiene(final String id) {
        return dao.obtiene(new Integer(id));
    }

  
    @Override
    public void graba(TiposBecas tipoBeca, Usuario usuario) {
        dao.graba(tipoBeca, usuario);
    }

   
    @Override
    public void elimina(final String id) {
        dao.elimina(new Integer(id));
    }
    
}
