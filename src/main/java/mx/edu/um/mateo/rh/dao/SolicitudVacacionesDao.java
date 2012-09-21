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
package mx.edu.um.mateo.rh.dao;

import java.util.List;

import mx.edu.um.mateo.rh.model.SolicitudVacaciones;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public interface SolicitudVacacionesDao {

	/**
	 * Retrieves all of the solicitudVacaciones
	 */
	public List<SolicitudVacaciones> getSolicitudVacaciones(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Retrieves all of the sSolicitudVacacionesByFechaRecepcion
	 */
	public List<SolicitudVacaciones> getSolicitudVacacionesByFechaInicial(
			SolicitudVacaciones solicitudVacaciones);

	/**
	 * Gets solicitudVacaciones's information based on primary key. An
	 * ObjectRetrievalFailureException Runtime Exception is thrown if nothing is
	 * found.
	 * 
	 * @param id
	 *            the solicitudVacaciones's id
	 * @return solicitudVacaciones populated solicitudVacaciones object
	 */
	public SolicitudVacaciones getSolicitudVacaciones(final Integer id);

	/**
	 * Saves a solicitudVacaciones's information
	 * 
	 * @param solicitudVacaciones
	 *            the object to be saved
	 */
	public void saveSolicitudVacaciones(SolicitudVacaciones solicitudVacaciones);

	/**
	 * Removes a solicitudVacaciones from the database by id
	 * 
	 * @param id
	 *            the solicitudVacaciones's id
	 */
	public void removeSolicitudVacaciones(final Integer id);

	/**
	 * Search solicitudesby by different atributes like date range, employee
	 * 
	 * @param solicitudVacacionesInicial
	 * @param solicitudVacacionesFinal
	 * @return
	 */
	public List<SolicitudVacaciones> searchSolicitudVacaciones(
			SolicitudVacaciones solicitudVacacionesInicial,
			SolicitudVacaciones solicitudVacacionesFinal);

	/**
	 * Regresa una lista de solicitudes de vacaciones de un empleado dado que
	 * esten autorizadas y cuya fecha inicial o fecha final este entre un rago
	 * de fechas determinado
	 * 
	 * @param ssalidaInicial
	 *            , debe tener asignado el empleado y la fecha inicial en el
	 *            atributo 'fecha inicial'
	 * @param ssalidaFinal
	 *            , debe tener asignado el empleado y la fecha inicial en el
	 *            atributo 'fecha inicial'
	 * @return
	 * @throws Exception
	 */
	public List<SolicitudVacaciones> getSolicitudesSalida(
			SolicitudVacaciones ssalidaInicial, SolicitudVacaciones ssalidaFinal)
			throws Exception;
}
