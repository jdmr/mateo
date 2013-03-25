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
import mx.edu.um.mateo.inventario.model.Entrada;
import mx.edu.um.mateo.inventario.model.FacturaAlmacen;
import mx.edu.um.mateo.inventario.model.Salida;
import mx.edu.um.mateo.inventario.utils.NoEstaAbiertaException;
import mx.edu.um.mateo.inventario.utils.NoEstaCerradaException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCancelarException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarEnCeroException;
import mx.edu.um.mateo.inventario.utils.NoSePuedeCerrarException;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public interface FacturaAlmacenDao {

	public Map<String, Object> lista(Map<String, Object> params);

	public FacturaAlmacen obtiene(Long id);

	public FacturaAlmacen carga(Long id);

	public FacturaAlmacen crea(FacturaAlmacen factura, Usuario usuario);

	public FacturaAlmacen crea(FacturaAlmacen factura);

	public FacturaAlmacen actualiza(FacturaAlmacen factura)
			throws NoEstaAbiertaException;

	public FacturaAlmacen actualiza(FacturaAlmacen otraFactura, Usuario usuario)
			throws NoEstaAbiertaException;

	public String cierra(Long facturaId, Usuario usuario)
			throws NoSePuedeCerrarException, NoSePuedeCerrarEnCeroException,
			NoEstaAbiertaException;

	public FacturaAlmacen cierra(FacturaAlmacen factura, Usuario usuario)
			throws NoSePuedeCerrarException, NoSePuedeCerrarEnCeroException,
			NoEstaAbiertaException;

	public FacturaAlmacen cancelar(Long id, Usuario usuario)
			throws NoEstaCerradaException, NoSePuedeCancelarException;

	public String elimina(Long id) throws NoEstaAbiertaException;

	public String elimina(Long id, Usuario usuario)
			throws NoEstaAbiertaException;

	public FacturaAlmacen agregaSalida(Long facturaId, Long salidaId) throws NoEstaAbiertaException;

	public FacturaAlmacen agregaEntrada(Long facturaId, Long entradaId) throws NoEstaAbiertaException;

	public FacturaAlmacen eliminaSalida(Long facturaId, Long salidaId) throws NoEstaAbiertaException;

	public FacturaAlmacen eliminaEntrada(Long facturaId, Long entradaId) throws NoEstaAbiertaException;

	public List<Salida> salidas(Long facturaId);

	public List<Entrada> entradas(Long facturaId);
}
