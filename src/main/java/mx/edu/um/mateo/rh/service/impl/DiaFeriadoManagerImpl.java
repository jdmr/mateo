/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Date;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.DiaFeriadoDao;
import mx.edu.um.mateo.rh.model.DiaFeriado;
import mx.edu.um.mateo.rh.service.DiaFeriadoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class DiaFeriadoManagerImpl implements DiaFeriadoManager {

    @Autowired
    private DiaFeriadoDao dao;

    /**
     * Regresa una lista de diaFeriadoes.
     *
     * @param params
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * Obtiene una diaFeriado
     *
     * @param id
     * @return
     */
    public DiaFeriado obtiene(final Long id) {
        return dao.obtiene(id);
    }

    /**
     * graba informacion sobre una diaFeriado
     *
     * @param diaFeriado the object to be saved
     * @param usuario
     */
    public void graba(DiaFeriado diaFeriado, Usuario usuario) {
        dao.graba(diaFeriado, usuario);
    }

    public Boolean esFeriado(Date fecha) {
        return dao.esFeriado(fecha);
    }

    /**
     * Cambia el status de la diaFeriado a I
     *
     * @param id el id de diaFeriado
     * @return regresa el nombre del dia eliminado
     */
    public String elimina(final Long id) {
        return dao.elimina(id);
    }

}
