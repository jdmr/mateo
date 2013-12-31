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
package mx.edu.um.mateo.contabilidad.dao;

import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.general.model.Usuario;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public interface CentroCostoDao {

    public CentroCosto crea(CentroCosto ccosto);
    
    public CentroCosto obtiene(String centroDeCostoId, Usuario usuario);

    public List<CentroCosto> busca(String filtro, Usuario usuario);

    public List<CentroCosto> buscaPorOrganizacion(String filtro, Usuario usuario);

    public List<CentroCosto> buscaPorEmpresa(String filtro, Usuario usuario);

    public List<CentroCosto> listaPorEmpresa(Usuario usuario);
    
    public List<CentroCosto> listaDepartamento (Usuario usuario);
}
