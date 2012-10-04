/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
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
package mx.edu.um.mateo.rh.dao.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.CategoriaDao;
import mx.edu.um.mateo.rh.model.Categoria;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CategoriaDaoHibernate extends BaseDao implements CategoriaDao {

    /**
     * @see mx.edu.um.mateo.rh.dao.CategoriaDao#lista(java.util.Map) 
     *
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de categorias con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }

        if (!params.containsKey("max")) {
            params.put("max", 10);
        } else {
            params.put("max", Math.min((Integer) params.get("max"), 100));
        }

        if (params.containsKey("pagina")) {
            Long pagina = (Long) params.get("pagina");
            Long offset = (pagina - 1) * (Integer) params.get("max");
            params.put("offset", offset.intValue());
        }

        if (!params.containsKey("offset")) {
            params.put("offset", 0);
        }
        Criteria criteria = currentSession().createCriteria(Categoria.class);
        Criteria countCriteria = currentSession().createCriteria(Categoria.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("status", filtro, MatchMode.ANYWHERE));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey("order")) {
            String campo = (String) params.get("order");
            if (params.get("sort").equals("desc")) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }
        }

        if (!params.containsKey("reporte")) {
            criteria.setFirstResult((Integer) params.get("offset"));
            criteria.setMaxResults((Integer) params.get("max"));
        }
        params.put(Constantes.CONTAINSKEY_CATEGORIAS, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.mateo.rh.service.CategoriaManager#obtiene(java.lang.String) 
     */
    @Override
    public Categoria obtiene(final Integer id) {
        Categoria categoria = (Categoria) currentSession().get(Categoria.class, id);
        if (categoria == null) {
            log.warn("uh oh, categoria with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Categoria.class, id);
        }

        return categoria;
    }

    /**
     *  @see mx.edu.um.mateo.rh.service.CategoriaManager#graba(mx.edu.um.mateo.rh.model.Categoria, mx.edu.um.mateo.general.model.Usuario) 
     */
    @Override
    public void graba(final Categoria categoria, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            categoria.setEmpresa(usuario.getEmpresa());
        }
        currentSession().saveOrUpdate(categoria);
        currentSession().merge(categoria);
        currentSession().flush();

//        if(categoria.getId() != null){
//        	Categoria nuevaCategoria = obtiene(categoria.getId());
//        	if(categoria.hashCode() == nuevaCategoria.hashCode()){
//        		System.out.println("No se modifico nada");
//        	}
//        	else{
//        		System.out.println("Hubo modificaciones");
//        		System.out.println(categoria);
//        		System.out.println(nuevaCategoria);
//        	}
//        }
    }

    /**
     *  @see mx.edu.um.mateo.rh.service.CategoriaManager#elimina(java.lang.String) 
     */
    @Override
    public String elimina(final Integer id) {
        log.debug("Eliminando la categoria {}", id);
        Categoria categoria = this.obtiene(id);
        String nombre = categoria.getNombre();
        currentSession().delete(categoria);

        currentSession().flush();

        return nombre;


    }
}
