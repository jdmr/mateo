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
 * The above copyright   notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS  PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
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
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Union;
import mx.edu.um.mateo.general.dao.ReporteDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author jdmr
 */
@Repository
@Transactional
public class UnionDao {

    private static final Logger log = LoggerFactory.getLogger(UnionDao.class);
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private ReporteDao reporteDao;
    @Autowired
    private AsociacionDao asociacionDao;

    public UnionDao() {
        log.info("Nueva instancia de UnionDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de uniones con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Union.class);
        Criteria countCriteria = currentSession().createCriteria(Union.class);

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.CONTAINSKEY_UNIONES, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public Union obtiene(Long id) {
        Union union = (Union) currentSession().get(Union.class, id);
        return union;
    }

    public Union crea(Union union) {
        return this.crea(union, null);
    }

    public Union crea(Union union, Usuario usuario) {
        Session session = currentSession();
        session.save(union);
        Asociacion asociacion = new Asociacion("Asociacion del Noreste", Constantes.STATUS_ACTIVO, union);
        if (usuario != null) {
            usuario.setAsociacion(asociacion);
        }
        asociacionDao.crea(asociacion, usuario);
        reporteDao.inicializaUnion(union);
        session.refresh(union);
        session.flush();
        return union;
    }

    public Union actualiza(Union union) {
        return this.actualiza(union, null);
    }

    public Union actualiza(Union union, Usuario usuario) {
        Session session = currentSession();
        log.debug("NombreCompleto: {}", union.getNombre());
        session.update(union);
        session.merge(union);
        session.flush();
        if (usuario != null) {
            session.refresh(union);
            actualizaUsuario:
            for (Asociacion asociacion : union.getAsociaciones()) {
                usuario.setAsociacion(asociacion);
                session.update(usuario);
                break actualizaUsuario;
            }
        }

        session.flush();
        return union;
    }

    public String elimina(Long id) {
        log.debug("Eliminando union {}", id);
        Union union = obtiene(id);
        String nombre = union.getNombre();
        union.setStatus(Constantes.STATUS_INACTIVO);
        actualiza(union);
        return nombre;
    }
}
