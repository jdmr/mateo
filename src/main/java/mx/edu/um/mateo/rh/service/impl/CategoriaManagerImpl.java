/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.HashMap;
import java.util.Map;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.rh.dao.CategoriaDao;
import mx.edu.um.mateo.rh.model.Categoria;
import mx.edu.um.mateo.rh.service.CategoriaManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author osoto
 */
@Service
@Transactional
public class CategoriaManagerImpl implements CategoriaManager {

    private CategoriaDao dao;

    /**
     * @see
     * mx.edu.um.rh.service.CategoriaManager#getCategorias(mx.edu.um.rh.model.Categoria)
     */
    @Override
    public Map<String, Object> getCategorias(final Categoria categoria) {
        Map<String, Object> params = new HashMap<>();
        params.put(Constantes.CATEGORIA_LIST, dao.getCategoria(categoria.getId()));
        return params;
    }

    /**
     * @see mx.edu.um.rh.service.CategoriaManager#getCategoria(String id)
     */
    @Override
    public Categoria getCategoria(final String id) {
        return dao.getCategoria(new Integer(id));
    }

    /**
     * @see mx.edu.um.rh.service.CategoriaManager#saveCategoria(Categoria categoria)
     */
    @Override
    public void saveCategoria(Categoria categoria) {
        dao.saveCategoria(categoria);
    }

    /**
     * @see mx.edu.um.rh.service.CategoriaManager#removeCategoria(String id)
     */
    @Override
    public void removeCategoria(final String id) {
        dao.removeCategoria(new Integer(id));
    }
}