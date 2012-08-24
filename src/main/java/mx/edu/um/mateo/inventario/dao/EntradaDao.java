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
import mx.edu.um.mateo.inventario.model.Entrada;
import mx.edu.um.mateo.inventario.model.LoteEntrada;
import mx.edu.um.mateo.inventario.utils.NoCuadraException;
import mx.edu.um.mateo.inventario.utils.NoEstaAbiertaException;
import mx.edu.um.mateo.inventario.utils.NoEstaCerradaException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarEnCeroException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarException;
import mx.edu.um.mateo.inventario.utils.ProductoNoSoportaFraccionException;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public interface EntradaDao {

	public Map<String, Object> lista(Map<String, Object> params);

	public List<Entrada> buscaEntradasPorFactura(Map<String, Object> params);

	public Entrada obtiene(Long id);

	public Entrada carga(Long id);

	public Entrada crea(Entrada entrada, Usuario usuario);

	public Entrada crea(Entrada entrada);

	public Entrada actualiza(Entrada entrada) throws NoEstaAbiertaException;

	public Entrada actualiza(Entrada otraEntrada, Usuario usuario)
			throws NoEstaAbiertaException;

	public String pendiente(Long entradaId, Usuario usuario)
			throws NoSePuedeCerrarException, NoCuadraException,
			NoSePuedeCerrarEnCeroException, NoEstaAbiertaException;

	public String cierra(Long entradaId, Usuario usuario)
			throws NoSePuedeCerrarException, NoCuadraException,
			NoSePuedeCerrarEnCeroException, NoEstaAbiertaException;

	public Entrada cierra(Entrada entrada, Usuario usuario)
			throws NoSePuedeCerrarException, NoCuadraException,
			NoSePuedeCerrarEnCeroException, NoEstaAbiertaException;

	public Entrada cierraPendiente(Entrada entrada, Usuario usuario)
			throws NoSePuedeCerrarException;

	public String elimina(Long id) throws NoEstaAbiertaException;

	public String elimina(Long id, Usuario usuario)
			throws NoEstaAbiertaException;

	public LoteEntrada creaLote(LoteEntrada lote)
			throws ProductoNoSoportaFraccionException, NoEstaAbiertaException;

	public Long eliminaLote(Long id) throws NoEstaAbiertaException;

	public Map<String, Object> preCancelacion(Long id, Usuario usuario)
			throws NoEstaCerradaException;

	public Cancelacion cancelar(Long id, Usuario usuario, String comentarios)
			throws NoEstaCerradaException;

}
