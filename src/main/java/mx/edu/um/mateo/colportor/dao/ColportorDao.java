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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.utils.FaltaAsociacionException;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.SpringSecurityUtils;
import org.hibernate.*;
import org.hibernate.criterion.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author wilbert
 */
@Repository
@Transactional
public class ColportorDao {

    private static final Logger log = LoggerFactory.getLogger(ColportorDao.class);
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    private RolDao rolDao;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SpringSecurityUtils springSecurityUtils;

    public ColportorDao() {
        log.info("Nueva instancia de ColportorDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
     public Map<String, Object> lista(Map<String, Object> params) throws FaltaAsociacionException{
        log.debug("Buscando lista de colportores con params {}", params);
        log.debug("params"+params);
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

        if (!params.containsKey(Constantes.ADDATTRIBUTE_ASOCIACION)) {
            params.put(Constantes.CONTAINSKEY_COLPORTORES, new ArrayList());
            params.put(Constantes.CONTAINSKEY_CANTIDAD, 0L);

               throw new FaltaAsociacionException("Asociacion No Encontrada");
        }
        
        Criteria criteria = currentSession().createCriteria(Colportor.class);
        Criteria countCriteria = currentSession().createCriteria(Colportor.class);

        if (params.containsKey(Constantes.ADDATTRIBUTE_ASOCIACION)) {
            log.debug("valor de asociacion"+params.get(Constantes.ADDATTRIBUTE_ASOCIACION));
            criteria.createCriteria(Constantes.ADDATTRIBUTE_ASOCIACION).add(Restrictions.eq("id",((Asociacion)params.get(Constantes.ADDATTRIBUTE_ASOCIACION)).getId()));
            countCriteria.createCriteria(Constantes.ADDATTRIBUTE_ASOCIACION).add(Restrictions.eq("id",((Asociacion)params.get(Constantes.ADDATTRIBUTE_ASOCIACION)).getId()));
        }

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("username", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apPaterno", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apMaterno", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("clave", filtro, MatchMode.ANYWHERE));
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
        params.put(Constantes.CONTAINSKEY_COLPORTORES, criteria.list());
        log.debug("colportores***"+((List) params.get(Constantes.CONTAINSKEY_COLPORTORES)).size());
               
        countCriteria.setProjection(Projections.rowCount());
        params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));

        return params;
    }

    public Colportor obtiene(Long id) {
        log.debug("Obtiene colportor con id = {}", id);
        Colportor colportor = (Colportor) currentSession().get(Colportor.class, id);
        return colportor;
    }
    
    public Colportor obtiene(String clave) {
        log.debug("Obtiene colportor con clave = {}", clave);
        Criteria criteria = currentSession().createCriteria(Colportor.class);
        criteria.add(Restrictions.eq("clave", clave));
        Colportor colportor = (Colportor) criteria.uniqueResult();
        return colportor;
    }
    
    public Colportor crea(Colportor colportor, String[] nombreDeRoles, Usuario usuario) {
        log.debug("Creando colportor : {}", colportor);
        colportor.setPassword(passwordEncoder.encodePassword(colportor.getPassword(), colportor.getUsername()));
        log.debug("password"+colportor.getPassword());
        colportor.addRol(rolDao.obtiene("ROLE_COL"));
        colportor.setStatus(Constantes.STATUS_ACTIVO);
        if (usuario != null) {
            colportor.setEmpresa(usuario.getEmpresa());
        }
        currentSession().save(colportor);
        currentSession().flush();
        return colportor;
    }
        public Colportor actualiza(Colportor colportor, String[] nombreDeRoles) {
        Colportor nuevoColportor= (Colportor) currentSession().get(Usuario.class, colportor.getId());
        nuevoColportor.setVersion(colportor.getVersion());
        nuevoColportor.setUsername(colportor.getUsername());
        nuevoColportor.setNombre(colportor.getNombre());
        nuevoColportor.setApPaterno(colportor.getApPaterno());
        nuevoColportor.setApMaterno(colportor.getApMaterno());       
        log.debug("password"+nuevoColportor.getPassword());


        try {
            currentSession().update(nuevoColportor);
            currentSession().flush();
        } catch (NonUniqueObjectException e) {
            log.warn("Ya hay un objeto previamente cargado, intentando hacer merge", e);
            currentSession().merge(nuevoColportor);
            currentSession().flush();
        }
        return nuevoColportor;
    }
    public Colportor actualiza(Colportor colportor) {
        log.debug("Actualizando colportor {}", colportor);

        //trae el objeto de la DB 
        Colportor nuevo = (Colportor) currentSession().get(Colportor.class, colportor.getId());
        //actualiza el objeto
        BeanUtils.copyProperties(colportor, nuevo);
        //lo guarda en la BD

        currentSession().update(nuevo);
        currentSession().flush();
        return nuevo;
    }

   public String elimina(Long id) {
        log.debug("Eliminando colportor {}", id);

        Colportor colportor = obtiene(id);
        colportor.setStatus(Constantes.STATUS_INACTIVO);
        actualiza(colportor);
        String clave = colportor.getClave();
        return clave;
    }
}