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
package mx.edu.um.mateo.activos.dao;

import java.util.Date;
import java.util.Map;
import mx.edu.um.mateo.activos.model.Activo;
import mx.edu.um.mateo.activos.model.BajaActivo;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public interface ActivoDao {

    public Map<String, Object> lista(Map<String, Object> params);

    public Activo obtiene(Long id);

    public Activo crea(Activo activo, Usuario usuario);

    public Activo crea(Activo activo);

    public Activo actualiza(Activo activo);

    public Activo actualiza(Activo activo, Usuario usuario);
    
    public Activo carga(Long id);

    public String elimina(Long id);
    
    public String baja(BajaActivo bajaActivo, Usuario usuario);
    
    public void arreglaFechas();
    
    public void depreciar(Date fecha, Long empresaId);
}
