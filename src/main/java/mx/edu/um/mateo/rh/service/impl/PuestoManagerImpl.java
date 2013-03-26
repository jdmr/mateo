/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Map;

import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.PuestoDao;
import mx.edu.um.mateo.rh.model.Puesto;
import mx.edu.um.mateo.rh.service.PuestoManager;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 
 * @author osoto
 */
@Service
@Transactional
public class PuestoManagerImpl implements PuestoManager {

    @Autowired
    private PuestoDao dao;
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    @Override
    public Puesto obtiene(Long id) {
        return dao.obtiene(id);
    }

    @Override
    public Puesto graba(Puesto puesto, Usuario usuario) {
        return dao.graba(puesto, usuario);
    }

    @Override
    public String elimina(Long id) {
        return dao.elimina(id);
    }

}
