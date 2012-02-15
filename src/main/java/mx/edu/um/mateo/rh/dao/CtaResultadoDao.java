/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import mx.edu.um.mateo.general.dao.RolDao;
import mx.edu.um.mateo.rh.model.CtaResultado;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author semdariobarbaamaya
 */
@Repository
@Transactional
public class CtaResultadoDao {

    
    private static final Logger log = LoggerFactory.getLogger(RolDao.class);
    @Autowired
    private SessionFactory sessionFactory;
    
      private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    public CtaResultado obtiene(Long id){
        CtaResultado ctaresultado = (CtaResultado) currentSession().get(CtaResultado.class, id);
        return ctaresultado;
    }
    
    public CtaResultado crea(CtaResultado ctaresultado){
        currentSession().save(ctaresultado);
        return ctaresultado;
    }
}
