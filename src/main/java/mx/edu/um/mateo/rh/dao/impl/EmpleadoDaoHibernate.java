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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.persistence.Entity;
import mx.edu.um.mateo.Constants;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.rh.dao.EmpleadoDao;
import mx.edu.um.mateo.rh.model.Empleado;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Omar Soto <osoto@um.edu.mx>
 */
@Entity
@Transactional
public class EmpleadoDaoHibernate extends BaseDao implements EmpleadoDao {

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#lista(java.util.Map)
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista de empleados con params {}", params);
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
        Criteria criteria = currentSession().createCriteria(Empleado.class);
        Criteria countCriteria = currentSession().createCriteria(Empleado.class);

        if (params.containsKey("empresa")) {
            criteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
            countCriteria.createCriteria("empresa").add(
                    Restrictions.idEq(params.get("empresa")));
        }

        if (params.containsKey("filtro")) {
            String filtro = (String) params.get("filtro");
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("clave", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("nombre", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apMaterno", filtro,
                    MatchMode.ANYWHERE));
            propiedades.add(Restrictions.ilike("apPaterno", filtro,
                    MatchMode.ANYWHERE));
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
        params.put(Constants.EMPLEADO_LIST, criteria.list());

        countCriteria.setProjection(Projections.rowCount());
        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#obtiene(java.lang.Long)
     */
    @Override
    public Empleado obtiene(Long id) {
        Empleado empleado = (Empleado) currentSession().get(Empleado.class, id);
        return empleado;
    }

    @Override
    public Empleado graba(Empleado empleado, Usuario usuario) {
        Session session = currentSession();
        if (usuario != null) {
            empleado.setEmpresa(usuario.getEmpresa());
        }
        session.save(empleado);
        session.flush();
        return empleado;
    }

    @Override
    public Empleado graba(Empleado empleado) {
        return this.graba(empleado, null);
    }

    /**
     * @see
     * mx.edu.um.mateo.rh.dao.EmpleadoDao#getEmpleadosBday(mx.edu.um.mateo.rh.model.Empleado)
     */
    @Override
    @SuppressWarnings("unchecked")
    public List getEmpleadosBday(Empleado empleado) {
        List lista = new ArrayList();

        if (empleado != null) {
            if (empleado.getClave() != null && empleado.getFechaNacimiento() != null) {

                Criteria sql = getSession().createCriteria(Empleado.class);
                sql.add(Restrictions.like("clave", empleado.getClave() + "%"));

                Locale locale = new Locale("es", "MX", "Traditional_WIN");

                Calendar gc = new GregorianCalendar(locale);
                Calendar tmp = new GregorianCalendar(locale);

                tmp.setTime(new Date());

                gc.setTime(empleado.getFechaNacimiento());
                gc.set(Calendar.YEAR, tmp.get(Calendar.YEAR) - 100);

                tmp = null;

                sql.add(this.getQueryByMonth(gc));

                sql.addOrder(Order.asc("clave"));

                lista = sql.list();
            }

        }

        return lista;
    }

    private Criterion getQueryByMonth(Calendar gc) {
        Criterion cr = null;

        gc.add(Calendar.YEAR, 1);

        gc.set(Calendar.DAY_OF_MONTH, 1);
        Date fechaI = gc.getTime();

        gc.set(Calendar.DAY_OF_MONTH, gc.getMaximum(Calendar.DAY_OF_MONTH));
        Date fechaF = gc.getTime();

        cr = Restrictions.between("fechaNacimiento", fechaI, fechaF);

        Calendar tmp = (Calendar) gc.clone();
        tmp.clear();
        tmp.setTime(new Date());
        tmp.add(Calendar.YEAR, -17);

        if (gc.compareTo(tmp) <= 0) {
            return Restrictions.or(cr, getQueryByMonth(gc));
        } else {
            return cr;
        }
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#getEmpleado(Empleado empleado)
     */
    @Override
    @Transactional(readOnly = true)
    public Empleado getEmpleado(final Empleado empleado) {
        Empleado emp = new Empleado();

        if (empleado != null) {
            Criteria sql = getSession().createCriteria(Empleado.class);

            // Buscar por id
            if (empleado.getId() != null) {
                sql.add(Restrictions.idEq(empleado.getId()));
            } // Buscar por clave
            else if (empleado.getClave() != null
                    && !"".equals(empleado.getClave())) {
                sql.add(Restrictions.eq("clave", empleado.getClave()));
            }
            emp = (Empleado) sql.uniqueResult();

        }
        if (emp == null) {
            log.warn("uh oh, empleado with id '" + empleado.getId()
                    + "' not found...");
            throw new ObjectRetrievalFailureException(Empleado.class,
                    empleado.getId());
        }
        return emp;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#saveEmpleado(Empleado empleado)
     */
    @Override
    public void saveEmpleado(final Empleado empleado) {
        currentSession().saveOrUpdate(empleado);
    }

    /**
     * An employee cannot be deleted
     *
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#removeEmpleado(Empleado empleado)
     */
//    public void removeEmpleado (final Empleado empleado)
//    {
//        getHibernateTemplate ().delete (getEmpleado (empleado));
//    }
    /**
     * @see
     * mx.edu.um.mateo.rh.dao.EmpleadoDao#searchEmpleado(mx.edu.um.mateo.rh.model.Empleado)
     */
    @Override
    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public List<Empleado> searchEmpleado(Empleado empleado) {
        Criteria criteria = null;
        List<Empleado> empleados = null;

        criteria = getSession().createCriteria(Empleado.class);

        if (empleado.getClave() != null && !"".equals(empleado.getClave())) {
            criteria.add(Restrictions.ilike("clave", empleado.getClave() + "%"));
        }

        if (empleado.getApPaterno() != null && !"".equals(empleado.getApPaterno())) {
            criteria.add(Restrictions.ilike("apPaterno", empleado.getApPaterno() + "%"));
        }
        if (empleado.getApMaterno() != null && !"".equals(empleado.getApMaterno())) {
            criteria.add(Restrictions.ilike("apMaterno", empleado.getApMaterno() + "%"));
        }
        if (empleado.getNombre() != null && !"".equals(empleado.getNombre())) {
            criteria.add(Restrictions.ilike("nombre", empleado.getNombre() + "%"));
        }
//        if(empleado.getRegsPatronales ().size () > 0)
//        {
//            criteria = null;
//            Integer cont = new Integer (1);
//            Boolean flag = new Boolean (false);
//            String query = "select e " +
//                    "from mx.edu.um.rh.model.Empleado e join e.puestos p ";
//            
//            Iterator i = empleado.getRegsPatronales ().iterator ();
//            while(i.hasNext ())
//            {
//                if(!flag){
//                query += "where ";
//                flag = !flag;
//                }
//                else{
//                    query += "and ";
//                }
//                
//                query += " p.centroCosto.key.idCCosto like :conta"+cont.toString ();
//                i.next ();
//                cont ++;
//            }
//            
//            if(empleado.getId () != null){
//                query += " and e.id = :empId ";
//            }
//            
//            query += " and e.status = :empStatus ";
//            query+= " order by e.clave ";
//            
//            Query sql = getSession ().createQuery (query);
//                        
//            String conta = "";
//            cont = new Integer(1);
//            RegistroPatronal reg = null;
//            
//            i = empleado.getRegsPatronales ().iterator ();
//            while(i.hasNext ())
//            {
//                conta = "conta"+cont.toString ();
//                reg = (RegistroPatronal)i.next ();                
//                sql.setString (conta, reg.getContabilidad().getIdCCosto()+"%");
//                cont++;
//            }
//            if(empleado.getId () != null){
//                sql.setLong ("empId", empleado.getId ());
//            }
//            sql.setString ("empStatus", Constants.STATUS_ACTIVO);
//            return sql.list ();
//        }

        criteria.addOrder(Order.asc("clave"));
        empleados = criteria.list();

        return empleados;

    }

    /**
     * @see
     * mx.edu.um.mateo.rh.dao.EmpleadoDao#searchEmpleadoByClaveOrApPaterno(mx.edu.um.mateo.rh.model.Empleado)
     */
    @Override
    @Transactional(readOnly = true)
    public List<Empleado> searchEmpleadoByClaveOrApPaterno(Empleado empleado) {
        if (empleado.getClave() != null
                && !"".equals(empleado.getClave())
                && empleado
                .getClave()
                .substring(0,
                (empleado.getClave().length() > 2) ? 2 : 1)
                .equals("98".substring(0,
                (empleado.getClave().length() > 2) ? 2 : 1))) {
            empleado.setApPaterno("");
        } else if (empleado.getApPaterno() != null
                && !"".equals(empleado.getApPaterno())) {
            empleado.setClave("");
        }

        return this.searchEmpleado(empleado);

    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#getEmpleadoClave(mx.edu.um.mateo.rh.model.Empleado)
     */
    @Override
    @Transactional(readOnly = true)
    public Empleado getEmpleadoClave(Empleado empleado) {
        Empleado emp = (Empleado) getSession()
                .createCriteria(Empleado.class)
                .add(org.hibernate.criterion.Restrictions.eq("clave",
                empleado.getClave())).uniqueResult();

        if (emp == null) {
            log.warn("uh oh, empleado with clave '" + empleado.getClave()
                    + "' not found...");
            throw new ObjectRetrievalFailureException(Empleado.class,
                    empleado.getClave());
        }

        return emp;
    }

    /**
     * @see mx.edu.um.mateo.rh.dao.EmpleadoDao#getEmpleado(java.lang.String)
     */
    @Override
    @Transactional(readOnly = true)
    public Empleado getEmpleado(final String clave) {
        Empleado emp = (Empleado) getSession().createCriteria(Empleado.class)
                .add(org.hibernate.criterion.Restrictions.eq("clave", clave))
                .uniqueResult();

        if (emp == null) {
            log.warn("uh oh, empleado with clave '" + clave + "' not found...");
            throw new ObjectRetrievalFailureException(Empleado.class, clave);
        }

        return emp;
    }
//    public List searchEmpleadoByCCosto(EmpleadoPuesto puesto) {
//        List lista = new ArrayList();
//        
//        if(puesto != null){
//            if(puesto.getCentroCosto() != null){
//                Query sql = getSession().createQuery("select e from mx.edu.um.rh.model.Empleado e inner join e.puestos p where p.centroCosto.key.ejercicio.idEjercicio = ? and p.centroCosto.key.idCCosto like ?");
//                sql.setString(0, puesto.getCentroCosto().getEjercicio().getIdEjercicio());
//                sql.setString(1, puesto.getCentroCosto().getIdCCosto()+"%");
//                lista = sql.list();
//            }
//        }
//
//        return lista;
//    }
//       public List searchEmpleadoByCCostoModalidadTipoEmpleado(EmpleadoPuesto puesto, Empleado emp,String sChecked []) {
//        //log.debug("searchEmpleadoByCCostoModalidadTipoEmpleado");
//        
//        List lista = new ArrayList();
//
//        String sQuey = "select e from mx.edu.um.rh.model.Empleado e ";
//        Boolean bPuesto = false, bModalidad = false;
//
//        if ((puesto != null && puesto.getCentroCosto() != null && puesto.getCentroCosto().getIdCCosto() != null)
//                && !puesto.getCentroCosto().getIdCCosto().isEmpty()) {
//            //log.debug("entro puesto o empleado");
//
//            //log.debug("entro cc " + puesto.getCentroCosto().getIdCCosto());
//            bPuesto = true;
//            sQuey += "inner join e.puestos p where p.centroCosto.key.ejercicio.idEjercicio = :idEjercicio and ";
//
//            if (puesto.getCentroCosto().getIdCCosto().equals("0")) {
//                sQuey = sQuey + " p.centroCosto.key.idCCosto like :todos ";
//            } else {
//                sQuey = sQuey + " p.centroCosto.key.idCCosto like :idCCosto";
//            }
//
//
//        }
//
//
//
//            //log.debug("hasta aqui llego0");
//            if (emp != null
//                && (emp.getModalidad() != null && !emp.getModalidad().trim().equals("0")
//                    && !emp.getModalidad().isEmpty())) {
//                //log.debug("Entro modalidad " + emp.getEmpleadoLaborales().getModalidad());
//            if (!bPuesto) {
//                sQuey += " where ";
//                bPuesto = true;
//            } else {
//                sQuey += " and ";
//            }
//                sQuey = sQuey + " e.modalidad= :modalidad";
//            }
//
//
//            List  tipoEmpList=new ArrayList();
//            if (sChecked != null &&   sChecked.length>0) {
//                //log.debug("Entro tipo empleado " + sChecked[0] );
//            if (!bPuesto) {
//                sQuey += " where ";
//            } else {
//                sQuey += " and ";
//            }
//
//              
//                for(int i=0;i<sChecked.length;i++)
//                    tipoEmpList.add( Long.parseLong(sChecked[i]));
//
//                //log.debug(tipoEmpList);
//                
//                sQuey = sQuey + " e.tipoEmpleado.id in (:tipoEmpleadoIds)";
//
//            }
//            
//        //log.debug(sQuey);
//
//        Query sql = getSession().createQuery(sQuey);
//
//
//        if ((puesto != null && puesto.getCentroCosto() != null && puesto.getCentroCosto().getIdCCosto() != null)
//                && !puesto.getCentroCosto().getIdCCosto().isEmpty()) {
//            sql.setString("idEjercicio", puesto.getCentroCosto().getEjercicio().getIdEjercicio());
//
//            if (puesto.getCentroCosto().getIdCCosto().equals("0")) {
//                sql.setString("todos", "%");
//            } else {
//                sql.setString("idCCosto", puesto.getCentroCosto().getIdCCosto() + "%");
//            }
//
//              //log.debug("puesto");
//        }
//
//        if (emp != null
//                && (emp.getModalidad() != null && !emp.getModalidad().trim().equals("0")
//                    && !emp.getModalidad().isEmpty())) {
//                sql.setString("modalidad", emp.getModalidad());
//            //log.debug("modalidad");
//            }
//
//            if (sChecked != null && sChecked.length>0) {
//                sql.setParameterList("tipoEmpleadoIds",tipoEmpList);
//               //log.debug("tipoEmpleadoIds");
//            }
//        
//
//
//        lista = sql.list();
//        //log.debug("*>"+lista.size());
//        return lista;
//    }
//
//    public Set getEmpleadoPerDeds(Empleado empleado) {
//        //log.debug("getEmpleadoPerDeds");
//        Query sql = getSession().createQuery("select new mx.edu.um.rh.model.EmpleadoPerDed(pd.perDed, pd.importe, pd.tipoImporte, pd.atributos, pd.otorgado) from mx.edu.um.rh.model.Empleado e join e.perDeds pd where e.id = :id order by pd.perDed.nombre");
//        sql.setLong("id", empleado.getId());
//        Set rSet = new HashSet();
//        rSet.addAll((Collection)sql.list());
//        return rSet;
//    }
}
