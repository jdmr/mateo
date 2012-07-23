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

import java.util.ArrayList;
import java.util.List;
import mx.edu.um.mateo.Constants;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.rh.dao.SolicitudVacacionesDao;
import mx.edu.um.mateo.rh.model.SolicitudVacaciones;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */

public class SolicitudVacacionesDaoHibernate extends BaseDao implements SolicitudVacacionesDao {

    /**
     * @see
     * mx.edu.um.rh.dao.SolicitudVacacionesDao#getSolicitudVacaciones(mx.edu.um.rh.model.SolicitudVacaciones)
     */
    @Override
    public List getSolicitudVacaciones(final SolicitudVacaciones solicitudVacaciones) {
        Criteria sql = getSession().createCriteria(SolicitudVacaciones.class);

        if (solicitudVacaciones.getEmpleado() != null && solicitudVacaciones.getEmpleado().getId() != null) {
            log.debug("entro a filtrar por empleado");
            sql.add(Restrictions.eq("empleado", solicitudVacaciones.getEmpleado()));
        }

        if (solicitudVacaciones.getStatus() != null && !"".equals(solicitudVacaciones.getStatus())) {
            log.debug("entro a filtrar por status");
            //Like para poder buscar los registros con cualquier status
            sql.add(Restrictions.like("status", solicitudVacaciones.getStatus() + "%"));
        }

        if (solicitudVacaciones.getFolio() != null && !"".equals(solicitudVacaciones.getFolio())) {
            log.debug("entro a filtrar por folio");
            //Like para poder buscar los registros con cualquier folio
            sql.add(Restrictions.eq("folio", solicitudVacaciones.getFolio()));
        }
        if (solicitudVacaciones.getPrimaVacacional() != null) {
            log.debug("entro a filtrar por prima vacacional");
            sql.add(Restrictions.eq("primaVacacional", solicitudVacaciones.getPrimaVacacional()));
        }

        sql.addOrder(Order.desc("fechaInicial"));
        return sql.list();
    }

    /**
     * @see
     * mx.edu.um.rh.dao.SolicitudVacacionesDao#getSolicitudVacaciones(mx.edu.um.rh.model.SolicitudVacaciones)
     */
    @Override
    public List getSolicitudVacacionesByFechaInicial(final SolicitudVacaciones solicitudVacaciones) {
        Criteria sql = getSession().createCriteria(SolicitudVacaciones.class);
        ArrayList list = new ArrayList();
        list.add(Constants.SOLICITUDSALIDA_STATUS_ACTIVO);
        list.add(Constants.SOLICITUDSALIDA_STATUS_ENVIADO);

        //Not se usa para agregar a la lista culquier cosa que no sea el par�metro especificado.
        //In se usa para agregar a la lista culquier cosa que se encuentre en el list que se pasa como par�metro
        sql.add(Restrictions.not(Restrictions.in("status", list)));

        if (solicitudVacaciones.getFechaInicial() != null) {
            sql.add(Restrictions.ge("fechaInicial", solicitudVacaciones.getFechaInicial()));
        }

        if (solicitudVacaciones.getFechaFinal() != null) {
            sql.add(Restrictions.le("fechaFinal", solicitudVacaciones.getFechaFinal()));
        }

        if (!solicitudVacaciones.getStatus().equals(Constants.SOLICITUDSALIDA_STATUS_TODOS)) {
            sql.add(Restrictions.like("status", solicitudVacaciones.getStatus()));
        }

        sql.addOrder(Order.desc("fechaInicial"));
        return sql.list();
    }

    /**
     * @see
     * mx.edu.um.rh.dao.SolicitudVacacionesDao#getSolicitudVacaciones(Integer
     * id)
     */
    @Override
    public SolicitudVacaciones getSolicitudVacaciones(final Integer id) {
        SolicitudVacaciones solicitudVacaciones = (SolicitudVacaciones) getSession().get(SolicitudVacaciones.class, id);
        if (solicitudVacaciones == null) {
            log.warn("uh oh, solicitudVacaciones with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(SolicitudVacaciones.class, id);
        }

        return solicitudVacaciones;
    }

    /**
     * @see
     * mx.edu.um.rh.dao.SolicitudVacacionesDao#saveSolicitudVacaciones(SolicitudVacaciones
     * solicitudVacaciones)
     */
    @Override
    public void saveSolicitudVacaciones(final SolicitudVacaciones solicitudVacaciones) {
        log.debug("saveSolicitudVacaciones");

        if (solicitudVacaciones.getId() == null) {
            getSession().save(solicitudVacaciones);
        } else {
            log.debug("{}", solicitudVacaciones);
            getSession().merge(solicitudVacaciones);
        }
    }

    /**
     * @see
     * mx.edu.um.rh.dao.SolicitudVacacionesDao#removeSolicitudVacaciones(Integer
     * id)
     */
    @Override
    public void removeSolicitudVacaciones(final Integer id) {
        getSession().delete(getSolicitudVacaciones(id));
    }

    /**
     * @see
     * mx.edu.um.rh.dao.SolicitudVacacionesDao#searchSolicitudVacaciones(SolicitudVacaciones
     * solicitudVacacionesInicial, SolicitudVacaciones solicitudVacacionesFinal)
     */
    @Override
    public List searchSolicitudVacaciones(SolicitudVacaciones solicitudVacacionesInicial, SolicitudVacaciones solicitudVacacionesFinal) {
        List solicitudes;

        Criteria sql = getSession().createCriteria(SolicitudVacaciones.class);


        log.debug("entro a searchSolicitudVacaciones");
        if (solicitudVacacionesInicial != null) {
            if (solicitudVacacionesInicial.getEmpleado() != null && solicitudVacacionesInicial.getEmpleado().getId() != null) {
                sql.add(Restrictions.eq("empleado", solicitudVacacionesInicial.getEmpleado()));
                log.debug("si trajo un empleado la ss");
            }
            if (solicitudVacacionesInicial.getFechaInicial() != null) {
                sql.add(Restrictions.ge("fechaInicial", solicitudVacacionesInicial.getFechaInicial()));
                log.debug("si trajo fechainicial la ss");
            }
            if (solicitudVacacionesInicial.getStatus() != null && !"".equals(solicitudVacacionesInicial.getStatus())) {
                sql.add(Restrictions.eq("status", solicitudVacacionesInicial.getStatus()));
                log.debug("si trajo status la ss");
            }
        }

        if (solicitudVacacionesFinal != null) {
            if (solicitudVacacionesFinal.getFechaInicial() != null) {
                sql.add(Restrictions.le("fechaInicial", solicitudVacacionesFinal.getFechaInicial()));
                log.debug("si trajo fechainicial final la ss");
            }
        }

        sql.addOrder(Order.asc("fechaInicial"));
        log.debug("+>" + sql);
        solicitudes = sql.list();

        return solicitudes;
    }

    /**
     * @see
     * mx.edu.um.rh.dao.SolicitudVacacionesDao#getSolicitudesSalida(mx.edu.um.rh.model.SolicitudVacaciones,
     * mx.edu.um.rh.model.SolicitudVacaciones)
     */
    @Override
    public List getSolicitudesSalida(SolicitudVacaciones ssalidaInicial, SolicitudVacaciones ssalidaFinal) throws Exception {
        Criteria sql = getSession().createCriteria(SolicitudVacaciones.class);
        sql.add(Restrictions.eq("empleado", ssalidaInicial.getEmpleado()));
        sql.add(Restrictions.or(Restrictions.between("fechaInicial", ssalidaInicial.getFechaInicial(), ssalidaFinal.getFechaInicial()),
                Restrictions.between("fechaFinal", ssalidaInicial.getFechaInicial(), ssalidaFinal.getFechaInicial())));
        sql.add(Restrictions.or(Restrictions.eq("status", Constants.SOLICITUDSALIDA_STATUS_AUTORIZADO),
                Restrictions.eq("status", Constants.SOLICITUDSALIDA_STATUS_PRIMA_VACACIONAL)));
        sql.addOrder(Order.asc("fechaInicial"));
        return sql.list();
    }
}
