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

import java.util.List;
import java.util.Map;

import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.model.Cancelacion;
import mx.edu.um.mateo.inventario.model.LoteSalida;
import mx.edu.um.mateo.inventario.model.Salida;
import mx.edu.um.mateo.inventario.utils.NoEstaAbiertaException;
import mx.edu.um.mateo.inventario.utils.NoEstaCerradaException;
import mx.edu.um.mateo.inventario.utils.NoHayExistenciasSuficientes;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarException;
import mx.edu.um.mateo.inventario.utils.ProductoNoSoportaFraccionException;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public interface SalidaDao {

	public Map<String, Object> lista(Map<String, Object> params);

	public List<Salida> buscaSalidasParaFactura(Map<String, Object> params);

	public Salida obtiene(Long id);

	public Salida carga(Long id);

	public Salida crea(Salida salida, Usuario usuario);

	public Salida crea(Salida salida);

	public Salida actualiza(Salida salida) throws NoEstaAbiertaException;

	public Salida actualiza(Salida otraSalida, Usuario usuario)
			throws NoEstaAbiertaException;

	public String cierra(Long salidaId, Usuario usuario)
			throws NoSePuedeCerrarException, NoHayExistenciasSuficientes,
			NoEstaAbiertaException;

	public Salida cierra(Salida salida, Usuario usuario)
			throws NoSePuedeCerrarException, NoHayExistenciasSuficientes,
			NoEstaAbiertaException;

	public String elimina(Long id) throws NoEstaAbiertaException;

	public LoteSalida creaLote(LoteSalida lote)
			throws ProductoNoSoportaFraccionException, NoEstaAbiertaException;

	public Long eliminaLote(Long id) throws NoEstaAbiertaException;

	public Map<String, Object> preCancelacion(Long id, Usuario usuario)
			throws NoEstaCerradaException;

	public Cancelacion cancelar(Long id, Usuario usuario, String comentarios)
			throws NoEstaCerradaException;
}
