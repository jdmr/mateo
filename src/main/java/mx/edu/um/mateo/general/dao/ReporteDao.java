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
package mx.edu.um.mateo.general.dao;

import java.util.List;
import mx.edu.um.mateo.colportor.model.Asociacion;
import mx.edu.um.mateo.colportor.model.Union;

import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Reporte;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.inventario.model.Almacen;
import net.sf.jasperreports.engine.JasperReport;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public interface ReporteDao {

        public JasperReport obtieneReporteAdministrativo(String nombre);

	public JasperReport obtieneReportePorOrganizacion(String nombre,
			Long organizacionId);

	public JasperReport obtieneReportePorEmpresa(String nombre, Long empresaId);

	public JasperReport obtieneReportePorAlmacen(String nombre, Long almacenId);

	public void inicializa();
        
        
       

	public List<Reporte> inicializaReportes(List<String> nombres);
        
	public void inicializaOrganizacion(Organizacion organizacion);

	public void inicializaEmpresa(Empresa empresa);

	public void inicializaAlmacen(Almacen almacen);

	public void compila(String nombre, String tipo, Usuario usuario) throws Exception;
}
