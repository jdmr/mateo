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
package mx.edu.um.mateo.rh.service;

import java.util.List;

import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.rh.model.Empleado;
import mx.edu.um.mateo.rh.model.SolicitudVacaciones;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public interface SolicitudVacacionesManager {

	/**
	 * Retrieves all of the solicitudVacaciones
	 */
	public List<SolicitudVacaciones> getSolicitudVacaciones(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Retrieves all of the solicitudVacacionesNoFurlough
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesNoFurlough(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Retrieves all of the solicitudVacacionesFurlough
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesFurlough(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Retrieves all of the getSolicitudVacacionesByFechaRecepcion
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesByFechaRecepcion(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Gets solicitudVacaciones's information based on id.
	 * 
	 * @param id
	 *            the solicitudVacaciones's id
	 * @return solicitudVacaciones populated solicitudVacaciones object
	 */
	public SolicitudVacaciones getSolicitudVacaciones(final String id);

	/**
	 * Gets solicitudVacaciones's information based on Folio.
	 * 
	 * @param Folio
	 *            the solicitudVacaciones's Folio
	 * @return solicitudVacaciones populated solicitudVacaciones object
	 */
	public SolicitudVacaciones getSolicitudVacacionesByFolio(final String folio);

	/**
	 * 
	 * @param subordinados
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesToSubordinados(
			List<Empleado> subordinados);

	/**
	 * Saves a solicitudVacaciones's information
	 * 
	 * @param solicitudVacaciones
	 *            the object to be saved
	 */
	public void saveSolicitudVacaciones(SolicitudVacaciones solicitudVacaciones)
			throws Exception;

	/**
	 * Removes a solicitudVacaciones from the database by id
	 * 
	 * @param id
	 *            the solicitudVacaciones's id
	 */
	public void removeSolicitudVacaciones(final String id);

	/**
	 * Assign status 'SENDED' to solicitudVacaciones
	 * 
	 * @param solicitudVacaciones
	 */
	public void sendSolicitudVacaciones(SolicitudVacaciones solicitudVacaciones)
			throws Exception;

	/**
	 * Assign status 'RECEIVED' to solicitudVacaciones
	 * 
	 * @param solicitudVacaciones
	 */
	public void receiveSolicitudVacaciones(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Assign status 'AUTHORIZED' to solicitudVacaciones
	 * 
	 * @param solicitudVacaciones
	 */
	public void authorizeSolicitudVacaciones(
			SolicitudVacaciones solicitudVacaciones) throws Exception;

	/**
	 * Assign status 'AUTHORIZED' to solicitudVacaciones
	 * 
	 * @param solicitudVacaciones
	 */
	public void authorizeFechaSolicitudVacaciones(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Assign status 'REJECTED' to solicitudVacaciones
	 * 
	 * @param solicitudVacaciones
	 */
	public void rejectSolicitudVacaciones(
			SolicitudVacaciones solicitudVacaciones) throws Exception;

	/**
	 * Retrieves all the solicitudes of the employee
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesByEmpleado(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Retrieves all the solicitudes of the employee not furlough
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesByEmpleadoNoFurlough(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Retrieves all the solicitudes of the employee furlough
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesByEmpleadoFurlough(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Devuelve las solicitudes de salida enviadas al jefe , de los empleados
	 * que laboran en el centro de costo proporcionado
	 * 
	 * @param cCosto
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudesSalidaToCCostoEnviadoJefe(
			CentroCosto cCosto);

	/**
	 * Devuelve las solicitudes de salida de los empleados que laboran en el
	 * centro de costo proporcionado
	 * 
	 * @param cCosto
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudesSalidaToCCosto(
			CentroCosto cCosto);

	/**
	 * Retrieves all the solicitudes of the employee with status SENDED
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesByEmpleadoToRoleOper(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Retrieves all the solicitudes of the employee with status RECEIVED
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesByEmpleadoToRoleAdmin(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Retrieves all the solicitudes with status SENDED
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesToRoleOper(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Retrieves all the solicitudes with status RECEIVED
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesToRoleAdmin(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Retrieves all the solicitudes with status AUTHORIZED
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesToRoleNomina(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Returns TRUE if employee has already asked for 'Prima Vacacional' in the
	 * actual year
	 * 
	 * @param solicitudVacaciones
	 * @return boolean
	 */
	public Boolean isPrimaVacacionalSolicitada(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Returns TRUE if employee has already asked for 'Visita Padres' in the
	 * current year
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public Boolean isVisitaPadresSolicitada(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Returns the solicitud with 'Prima Vacacional' turned on in the current
	 * year
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public SolicitudVacaciones getSolicitudVacacionesConPrimaVacacional(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Returns the solicitud with 'Visita Padres' turned on in the current year
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public SolicitudVacaciones getSolicitudVacacionesConVisitaPadres(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Assign status 'Prima Vacional' to solicitudVacaciones
	 * 
	 * @param solicitudVacaciones
	 */
	public void payPrimaVacacional(SolicitudVacaciones solicitudVacaciones)
			throws Exception;

	public String getFolioAutorizacion();

	/**
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public List<SolicitudVacaciones> getSolicitudesSalidaProcesadas(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * 
	 * @param solicitudVacaciones
	 * @return SolicitudVacaciones
	 */
	public SolicitudVacaciones getSolicitudVacacionesFechaPrimaVacacional(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * 
	 * @param solicitudVacaciones
	 * @return
	 */
	public SolicitudVacaciones getSolicitudVacacionesFechaVisitaPadres(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * 
	 * @param solicitudVacaciones
	 */
	public void saveProcesoDiasFeriados(SolicitudVacaciones solicitudVacaciones);

	/**
	 * Regresa un listado de las solicitudes de salida autorizadas del empleado
	 * en el rango de fechas especificado
	 * 
	 * @param ssalidaInicial
	 *            , debe tener asignado al empleado y la fecha como fecha
	 *            inicial
	 * @param ssalidaFinal
	 *            , debe tener asignado al empleado y la fecha como fecha final
	 * @return lista de <SolicitudVacaciones>
	 */
	public List<SolicitudVacaciones> getSolicitudesSalidaByEmpleado(
			SolicitudVacaciones ssalidaInicial, SolicitudVacaciones ssalidaFinal)
			throws Exception;
}
