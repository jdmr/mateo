/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE. 
 */
package mx.edu.um.mateo.colportor.dao;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.colportor.model.TipoColportor;
import mx.edu.um.mateo.colportor.utils.FaltaAsociacionException;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author 
 */
@Repository
@Transactional
public class TipoColportorDao extends BaseDao {

    @Autowired
    private SessionFactory sessionFactory;

    public TipoColportorDao() {
        log.info("Nueva instancia de TipoColportorDao");
    }    

    public Map<String, Object> lista(Map<String, Object> params){
        log.debug("Buscando lista de tipoColportores con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_MAX)) {
            params.put(Constantes.CONTAINSKEY_MAX, 10);
        } else {
            params.put(Constantes.CONTAINSKEY_MAX, Math.min((Integer) params.get(Constantes.CONTAINSKEY_MAX), 100));
        }

        if (params.containsKey(Constantes.CONTAINSKEY_PAGINA)) {
            Long pagina = (Long) params.get(Constantes.CONTAINSKEY_PAGINA);
            Long offset = (pagina - 1) * (Integer) params.get(Constantes.CONTAINSKEY_MAX);
            params.put(Constantes.CONTAINSKEY_OFFSET, offset.intValue());
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_OFFSET)) {
            params.put(Constantes.CONTAINSKEY_OFFSET, 0);
        }
        Criteria criteria = currentSession().createCriteria(TipoColportor.class);
        Criteria countCriteria = currentSession().createCriteria(TipoColportor.class);       

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("tipoColportor", filtro));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey(Constantes.CONTAINSKEY_ORDER)) {
            String campo = (String) params.get(Constantes.CONTAINSKEY_ORDER);
            if (params.get(Constantes.CONTAINSKEY_SORT).equals(Constantes.CONTAINSKEY_DESC)) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_REPORTE)) {
            criteria.setFirstResult((Integer) params.get(Constantes.CONTAINSKEY_OFFSET));
            criteria.setMaxResults((Integer) params.get(Constantes.CONTAINSKEY_MAX));
        }
        params.put(Constantes.CONTAINSKEY_TIPO_COLPORTOR, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public TipoColportor obtiene(Long id) {
        TipoColportor tipoColportor = (TipoColportor) currentSession().get(TipoColportor.class, id);
        return tipoColportor;
    }

    public TipoColportor crea(TipoColportor tipoColportor) {
        return this.crea(tipoColportor, null);
    }

    public TipoColportor crea(TipoColportor tipoColportor, Usuario usuario) {
        Session session = currentSession();        
        session.save(tipoColportor);        
        session.flush();
        return tipoColportor;
    }

    public TipoColportor actualiza(TipoColportor tipoColportor) {
        return this.actualiza(tipoColportor, null);
    }

    public TipoColportor actualiza(TipoColportor tipoColportor, Usuario usuario) {
        Session session = currentSession();
        
        try {
            // Actualiza la tipoColportor
            log.debug("Actualizando tipoColportor");
            session.update(tipoColportor);
            session.flush();

        } catch (NonUniqueObjectException e) {
            try {
                session.merge(tipoColportor);
            } catch (Exception ex) {
                log.error("No se pudo actualizar la tipoColportor", ex);
                throw new RuntimeException("No se pudo actualizar la tipoColportor", ex);
            }
        }
        
        session.flush();
        return tipoColportor;
    }

    public String elimina(Long id) {
        log.debug("Eliminando tipo de colportor {}", id);
        TipoColportor tipoColportor = obtiene(id);
        String nombre = tipoColportor.getTipoColportor();
        currentSession().delete(tipoColportor);
        return nombre;
    }
}
