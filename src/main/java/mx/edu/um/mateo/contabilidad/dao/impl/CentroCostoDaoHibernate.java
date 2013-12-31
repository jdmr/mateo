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
import java.util.Map;
import mx.edu.um.mateo.contabilidad.dao.CentroCostoDao;
import mx.edu.um.mateo.contabilidad.model.CCostoPK;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Repository
@Transactional
public class CentroCostoDaoHibernate implements CentroCostoDao {

    
    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
    
    @Override
    @Transactional
    public CentroCosto crea(CentroCosto ccosto) {
        currentSession().save(ccosto);
        currentSession().flush();
        return ccosto;
        
    }

    @Override
    @Transactional(readOnly = true)
    public CentroCosto obtiene(String centroDeCostoId, Usuario usuario) {
        Ejercicio ejercicio = usuario.getEjercicio();
        CCostoPK pk = new CCostoPK(ejercicio, centroDeCostoId);
        return (CentroCosto) currentSession().get(CentroCosto.class, pk);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CentroCosto> busca(String filtro, Usuario usuario) {
        Ejercicio ejercicio = usuario.getEjercicio();
        Criteria criteria = currentSession().createCriteria(CentroCosto.class);
        criteria.add(Restrictions.eq("id.ejercicio", ejercicio));
        Disjunction or = Restrictions.disjunction();
        or.add(Restrictions.ilike("id.idCosto", filtro, MatchMode.ANYWHERE));
        or.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
        criteria.add(or);
        criteria.setMaxResults(10);
        return criteria.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CentroCosto> buscaPorOrganizacion(String filtro, Usuario usuario) {
        Ejercicio ejercicio = usuario.getEjercicio();
        Organizacion organizacion = usuario.getEmpresa().getOrganizacion();
        Criteria criteria = currentSession().createCriteria(CentroCosto.class);
        criteria.add(Restrictions.eq("id.ejercicio", ejercicio));
        Disjunction or = Restrictions.disjunction();
        Conjunction and = Restrictions.conjunction();
        String base = organizacion.getCentroCostoId();
        StringBuilder sb = new StringBuilder();
        sb.append(base);
        sb.append("%");
        sb.append(filtro);
        String cuenta = sb.toString();
        or.add(Restrictions.ilike("id.idCosto", cuenta, MatchMode.START));
        and.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
        and.add(Restrictions.ilike("id.idCosto", base, MatchMode.START));
        or.add(and);
        criteria.add(or);
        criteria.setMaxResults(10);
        return criteria.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CentroCosto> buscaPorEmpresa(String filtro, Usuario usuario) {
        Ejercicio ejercicio = usuario.getEjercicio();
        Empresa empresa = usuario.getEmpresa();
        Criteria criteria = currentSession().createCriteria(CentroCosto.class);
        criteria.add(Restrictions.eq("id.ejercicio", ejercicio));
        Disjunction or = Restrictions.disjunction();
        Conjunction and = Restrictions.conjunction();
        String base = empresa.getCentroCosto().getId().getIdCosto();
        StringBuilder sb = new StringBuilder();
        sb.append(base);
        sb.append("%");
        sb.append(filtro);
        String cuenta = sb.toString();
        or.add(Restrictions.ilike("id.idCosto", cuenta, MatchMode.START));
        and.add(Restrictions.ilike("nombre", filtro, MatchMode.ANYWHERE));
        and.add(Restrictions.ilike("id.idCosto", base, MatchMode.START));
        or.add(and);
        criteria.add(or);
        criteria.setMaxResults(10);
        return criteria.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CentroCosto> listaPorEmpresa(Usuario usuario) {
        Ejercicio ejercicio = usuario.getEjercicio();
        Empresa empresa = usuario.getEmpresa();
        Criteria criteria = currentSession().createCriteria(CentroCosto.class);
        criteria.add(Restrictions.eq("id.ejercicio", ejercicio));
        String base = empresa.getCentroCosto().getId().getIdCosto();
        StringBuilder sb = new StringBuilder();
        sb.append(base);
        sb.append("%");
        String cuenta = sb.toString();
        criteria.add(Restrictions.ilike("id.idCosto", cuenta, MatchMode.START));
        return criteria.list();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CentroCosto> listaDepartamento( Usuario usuario) {
        Ejercicio ejercicio = usuario.getEjercicio();        
        Organizacion organizacion = usuario.getEmpresa().getOrganizacion();
        Criteria criteria = currentSession().createCriteria(CentroCosto.class);
        criteria.add(Restrictions.eq("id.ejercicio", ejercicio));
        criteria.add(Restrictions.eq("detalle", "S"));
        Disjunction or = Restrictions.disjunction();
        Conjunction and = Restrictions.conjunction();
        String org = organizacion.getCentroCostoId();
        StringBuilder sb = new StringBuilder();
        sb.append(org);
        sb.append("%");
        String cuenta = sb.toString();
        or.add(Restrictions.ilike("id.idCosto", cuenta, MatchMode.START)); 
        criteria.addOrder(Order.asc("id.idCosto"));
        return criteria.list();
    }
}
