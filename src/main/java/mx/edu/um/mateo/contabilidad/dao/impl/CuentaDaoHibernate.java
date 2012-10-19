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
package mx.edu.um.mateo.contabilidad.dao.impl;

import java.util.List;
import mx.edu.um.mateo.contabilidad.dao.CuentaDao;
import mx.edu.um.mateo.contabilidad.model.Cuenta;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.rh.model.Seccion;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class CuentaDaoHibernate extends BaseDao implements CuentaDao {

    /**
     * @see mx.edu.um.rh.dao.SeccionDao#getSeccions(mx.edu.um.rh.model.Seccion)
     */
//    @SuppressWarnings("unchecked")
    
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    @Override
    public List<Cuenta> departamentos(Long organizacionId, String ejercicioId) {
        log.debug("departamentos");
        Query query = currentSession()
                .createQuery(
                "select c from CuentaMayor c where c.id.ejercicio.id.organizacion.id = :organizacionId and c.id.ejercicio.id.idEjercicio = :ejercicioId order by c.id.idCtaMayor");
        query.setLong("organizacionId", organizacionId);
        query.setString("ejercicioId", ejercicioId);
        return query.list();
    }

    

        public List<Cuenta> tiposDeActivo(Long organizacionId, String ejercicioId) {
        log.debug("tipos de activo");
        Query query = currentSession()
                .createQuery(
                "select c from CuentaMayor c where c.id.ejercicio.id.organizacion.id = :organizacionId and c.id.ejercicio.id.idEjercicio = :ejercicioId order by c.id.idCtaMayor");
        query.setLong("organizacionId", organizacionId);
        query.setString("ejercicioId", ejercicioId);
        return query.list();
    }
}
