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
import java.util.List;
import java.util.Locale;

import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.rh.dao.EmpleadoDao;
import mx.edu.um.mateo.rh.model.Empleado;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */

public class EmpleadoDaoHibernate extends BaseDao implements EmpleadoDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> getEmpleadosBday(Empleado empleado) {
		List<Empleado> lista = new ArrayList<>();

		if (empleado != null) {
			if (empleado.getClave() != null
					&& empleado.getFechaNacimiento() != null) {
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
	 * @see mx.edu.um.rh.dao.EmpleadoDao#getEmpleado(Empleado empleado)
	 */
	@Override
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
	 * @see mx.edu.um.rh.dao.EmpleadoDao#saveEmpleado(Empleado empleado)
	 */
	@Override
	public void saveEmpleado(final Empleado empleado) {

		getSession().saveOrUpdate(empleado);

	}

	/**
	 * An employee cannot be deleted
	 * 
	 * @see mx.edu.um.rh.dao.EmpleadoDao#removeEmpleado(Empleado empleado)
	 */
	// public void removeEmpleado (final Empleado empleado)
	// {
	// getHibernateTemplate ().delete (getEmpleado (empleado));
	// }

	@Override
	@SuppressWarnings("unchecked")
	public List<Empleado> searchEmpleado(Empleado empleado) {
		Criteria criteria;
		List<Empleado> empleados;
		criteria = getSession().createCriteria(Empleado.class);

		if (empleado.getClave() != null && !"".equals(empleado.getClave())) {
			criteria.add(Restrictions.ilike("clave", empleado.getClave() + "%"));
		}
		if (empleado.getApPaterno() != null
				&& !"".equals(empleado.getApPaterno())) {
			criteria.add(Restrictions.ilike("apPaterno",
					empleado.getApPaterno() + "%"));
		}
		if (empleado.getApMaterno() != null
				&& !"".equals(empleado.getApMaterno())) {
			criteria.add(Restrictions.ilike("apMaterno",
					empleado.getApMaterno() + "%"));
		}
		if (empleado.getNombre() != null && !"".equals(empleado.getNombre())) {
			criteria.add(Restrictions.ilike("nombre", empleado.getNombre()
					+ "%"));
		}
		// if (empleado.getRegsPatronales().size() > 0) {
		// criteria = null;
		// Integer cont = new Integer(1);
		// Boolean flag = new Boolean(false);
		// String query = "select e "
		// + "from mx.edu.um.rh.model.Empleado e join e.puestos p ";
		//
		// Iterator i = empleado.getRegsPatronales().iterator();
		// while (i.hasNext()) {
		// if (!flag) {
		// query += "where ";
		// flag = !flag;
		// } else {
		// query += "and ";
		// }
		//
		// query += " p.centroCosto.key.idCCosto like :conta" + cont.toString();
		// i.next();
		// cont++;
		// }
		//
		// if (empleado.getId() != null) {
		// query += " and e.id = :empId ";
		// }
		//
		// query += " and e.status = :empStatus ";
		// query += " order by e.clave ";
		//
		// Query sql = getSession().createQuery(query);
		//
		// String conta = "";
		// cont = new Integer(1);
		// RegistroPatronal reg = null;
		//
		// i = empleado.getRegsPatronales().iterator();
		// while (i.hasNext()) {
		// conta = "conta" + cont.toString();
		// reg = (RegistroPatronal) i.next();
		// sql.setString(conta, reg.getContabilidad().getIdCCosto() + "%");
		// cont++;
		// }
		// if (empleado.getId() != null) {
		// sql.setLong("empId", empleado.getId());
		// }
		// sql.setString("empStatus", Constants.STATUS_ACTIVO);
		// return sql.list();
		// }

		criteria.addOrder(Order.asc("clave"));
		empleados = criteria.list();

		return empleados;

	}

	@Override
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
	 * @see org.appfuse.dao.EmpleadoDAO#getEmpleado(final String clave)
	 */
	@Override
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

	@Override
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
}
