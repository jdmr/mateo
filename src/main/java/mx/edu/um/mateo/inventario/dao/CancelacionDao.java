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
package mx.edu.um.mateo.inventario.dao;

import java.util.Date;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.inventario.model.Cancelacion;
import mx.edu.um.mateo.inventario.model.Folio;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class CancelacionDao {

    private static final Logger log = LoggerFactory.getLogger(CancelacionDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public CancelacionDao() {
        log.info("Nueva instance de CancelacionDao");
    }

    public Cancelacion crea(Cancelacion cancelacion) {
        return this.crea(cancelacion, null);
    }

    public Cancelacion crea(Cancelacion cancelacion, Usuario usuario) {
        Date fecha = new Date();
        cancelacion.setCreador((usuario != null) ? usuario.getUsername() : "sistema");
        cancelacion.setFechaCreacion(fecha);
        cancelacion.setFechaModificacion(fecha);
        currentSession().save(cancelacion);
        return cancelacion;
    }
    
    public String getFolio(Almacen almacen) {
        Query query = currentSession().createQuery("select f from Folio f where f.nombre = :nombre and f.almacen.id = :almacenId");
        query.setString("nombre", "CANCELACION");
        query.setLong("almacenId", almacen.getId());
        query.setLockOptions(LockOptions.UPGRADE);
        Folio folio = (Folio) query.uniqueResult();
        if (folio == null) {
            folio = new Folio("CANCELACION");
            folio.setAlmacen(almacen);
            currentSession().save(folio);
            return getFolio(almacen);
        }
        folio.setValor(folio.getValor() + 1);
        java.text.NumberFormat nf = java.text.DecimalFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumIntegerDigits(9);
        nf.setMaximumIntegerDigits(9);
        nf.setMaximumFractionDigits(0);
        StringBuilder sb = new StringBuilder();
        sb.append("C-");
        sb.append(almacen.getEmpresa().getOrganizacion().getCodigo());
        sb.append(almacen.getEmpresa().getCodigo());
        sb.append(almacen.getCodigo());
        sb.append(nf.format(folio.getValor()));
        return sb.toString();
    }

    
}
