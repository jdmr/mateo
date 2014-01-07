/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.DiaFeriadoDao;
import mx.edu.um.mateo.rh.dao.SolicitudVacacionesEmpleadoDao;
import mx.edu.um.mateo.rh.dao.VacacionesEmpleadoDao;
import mx.edu.um.mateo.rh.model.SolicitudVacacionesEmpleado;
import mx.edu.um.mateo.rh.service.SolicitudVacacionesEmpleadoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author develop
 */
@Transactional
@Service
public class SolicitudVacacionesEmpleadoManagerImpl extends BaseDao implements SolicitudVacacionesEmpleadoManager {

    @Autowired
    private SolicitudVacacionesEmpleadoDao dao;
    @Autowired
    private DiaFeriadoDao feriadoDao;

    /**
     * Regresa una lista de vacaciones.
     *
     * @param params
     * @return
     */
    public Map<String, Object> lista(Map<String, Object> params) {
        return dao.lista(params);
    }

    /**
     * Obtiene una vacaciones
     *
     * @param id
     * @return
     */
    public SolicitudVacacionesEmpleado obtiene(final Long id) {
        return dao.obtiene(id);
    }

    /**
     * graba informacion sobre las vacaciones de algun empleado
     *
     * @param vacaciones the object to be saved
     * @param usuario
     */
    public void graba(SolicitudVacacionesEmpleado vacaciones, Usuario usuario) {
        Date fechaInicio = vacaciones.getFechaInicio();
        Date fechaFinal = vacaciones.getFechaFinal();

        GregorianCalendar cal = new GregorianCalendar();

        Calendar c = Calendar.getInstance();
        c.setTime(fechaInicio);
        int sabDom = 0;
        Date prueba;
        int contador = 1;
        do {

            c.add(Calendar.DATE, 1);
            prueba = c.getTime();
            cal.setTime(prueba);
            int numDia = cal.get(Calendar.DAY_OF_WEEK);
            String dia = diaSemana(numDia);
            if (numDia == 1 || numDia == 7 || feriadoDao.esFeriado(prueba)) {
                sabDom++;
                log.debug("feriado++");
            }
            log.debug("diaSemana{}", cal.get(Calendar.DAY_OF_WEEK));
            log.debug("fecha{}", prueba);
            log.debug("antes{}", contador);
            contador++;
            log.debug("despues{}", contador);
            c.setTime(prueba);
        } while (!fechaFinal.equals(prueba));
        dao.graba(vacaciones, usuario);
    }

    /**
     * Elimina las vacaciones
     *
     * @param id el id de vacaciones
     * @return
     */
    public String elimina(final Long id) {
        return dao.elimina(id);
    }

    public String diaSemana(int numDia) {

        switch (numDia) {
            case 1:
                return "domingo";
            case 2:
                return "lunes";
            case 3:
                return "martes";
            case 4:
                return "miercoles";
            case 5:
                return "jueves";
            case 6:
                return "viernes";
            case 7:
                return "sabado";
        }
        return "ningundia";
    }
}
