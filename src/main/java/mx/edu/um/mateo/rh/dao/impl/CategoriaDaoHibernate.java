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

import java.util.List;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.rh.dao.CategoriaDao;
import mx.edu.um.mateo.rh.model.Categoria;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author zorch
 */
@Transactional
@Repository
public class CategoriaDaoHibernate extends BaseDao implements CategoriaDao {

    /**
     * @see mx.edu.um.rh.dao.CategoriaDao#getCategorias(mx.edu.um.rh.model.Categoria)
     */
    @Override
    public List getCategorias(final Categoria categoria) {
        return currentSession().createCriteria(Categoria.class).list();
        
    }

    /**
     * @see mx.edu.um.rh.dao.CategoriaDao#getCategoria(Integer id)
     */
    @Override
    public Categoria getCategoria(final Integer id) {
        Categoria categoria = (Categoria) currentSession().get(Categoria.class, id);
        if (categoria == null) {
            log.warn("uh oh, Categoria with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Categoria.class, id);
        }

        return categoria;
    }

    /**
     * @see mx.edu.um.rh.dao.CategoriaDao#save Categoria(Categoria categoria)
     */    
    @Override
    public void saveCategoria(final Categoria categoria) {
        if (categoria.getId() == null) {
            getSession().save(categoria);
        } else {
            log.debug("{}", categoria);
            getSession().merge(categoria);
        }

    }

    /**
     * @see mx.edu.um.rh.dao.CategoriaDao#removeCategoria(Integer id)
     */
    @Override
    public void removeCategoria(final Integer id) {
        currentSession().delete(getCategoria(id));
    }
    
}
