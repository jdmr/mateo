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
package mx.edu.um.mateo.general.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mx.edu.um.mateo.general.dao.OrganizacionDao;
import mx.edu.um.mateo.general.dao.ReporteDao;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Reporte;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.inventario.model.Almacen;
import mx.edu.um.mateo.inventario.model.Entrada;
import mx.edu.um.mateo.inventario.model.FacturaAlmacen;
import mx.edu.um.mateo.inventario.model.LoteEntrada;
import mx.edu.um.mateo.inventario.model.LoteSalida;
import mx.edu.um.mateo.inventario.model.Salida;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/reporte")
public class ReporteController {

	private static final Logger log = LoggerFactory
			.getLogger(ReporteController.class);
	@Autowired
	private ReporteDao reporteDao;
	@Autowired
	private Ambiente ambiente;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private OrganizacionDao organizacionDao;

	private Session currentSession() {
		return sessionFactory.getCurrentSession();
	}

        @PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/compila/{tipo}/{reporte}")
	public String compila(@PathVariable String tipo,
			@PathVariable String reporte, RedirectAttributes redirectAttributes) {
		log.debug("Compilando reporte {}", reporte);
		try {
			reporteDao.compila(reporte, tipo, ambiente.obtieneUsuario());
		} catch (Exception e) {
			log.error("Hubo un problema al compilar el reporte", e);
			redirectAttributes.addFlashAttribute("message",
					"reporte.no.compilado.message");
			redirectAttributes.addFlashAttribute("messageAttrs",
					new String[] { reporte });
			redirectAttributes.addFlashAttribute("messageStyle", "alert-error");

			return "redirect:/";
		}

		redirectAttributes.addFlashAttribute("message",
				"reporte.compilado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { reporte });

		return "redirect:/";
	}

        @PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping("/inicializa")
	public String inicializa(HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		log.debug("Inicializando reportes para organizacion "
				+ request.getSession().getAttribute("organizacionId"));
		reporteDao.inicializa();
		Long organizacionId = (Long) request.getSession().getAttribute(
				"organizacionId");
		Organizacion organizacion = organizacionDao.obtiene(organizacionId);
		reporteDao.inicializaOrganizacion(organizacion);
		for (Empresa empresa : organizacion.getEmpresas()) {
			reporteDao.inicializaEmpresa(empresa);
			for (Almacen almacen : empresa.getAlmacenes()) {
				reporteDao.inicializaAlmacen(almacen);
			}
		}

		redirectAttributes.addFlashAttribute("message",
				"reporte.inicializado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { organizacion.getNombre() });

		return "redirect:/";
	}

	@RequestMapping("/salida/{id}")
	public String salida(HttpServletRequest request,
			HttpServletResponse response, @PathVariable Long id) {
		try {
			log.debug("Generando reporte de salida {}", id);
			Salida salida = (Salida) currentSession().get(Salida.class, id);
			Long almacenId = (Long) request.getSession().getAttribute(
					"almacenId");
			Query query = currentSession()
					.createQuery(
							"select r from Almacen a inner join a.reportes r where a.id = :id and r.nombre = :nombre");
			query.setLong("id", almacenId);
			query.setString("nombre", "salida_lote");
			Reporte reporte = (Reporte) query.uniqueResult();
			JasperReport subreporte = reporte.getReporte();

			Map<String, Object> params = new HashMap<>();
			params.put("datasource",
					new JRBeanCollectionDataSource(salida.getLotes()));
			params.put("subreporte", subreporte);

			query.setLong("id", almacenId);
			query.setString("nombre", "salida");
			reporte = (Reporte) query.uniqueResult();

			List<Salida> salidas = new ArrayList<>();
			salidas.add(salida);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					reporte.getReporte(), params,
					new JRBeanCollectionDataSource(salidas));
			byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);
			String nombre = salida.getFolio();
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename="
					+ nombre + ".pdf");
			if (archivo != null) {
				response.setContentLength(archivo.length);
				try (BufferedOutputStream bos = new BufferedOutputStream(
						response.getOutputStream())) {
					bos.write(archivo);
					bos.flush();
				}
			}
		} catch (JRException | IOException e) {
			log.error("No se pudo crear el reporte de la salida " + id, e);
		}
		return "redirect:/inventario/salida/ver/" + id;
	}

	@RequestMapping("/factura/almacen/{id}")
	public String facturaAlmacen(HttpServletRequest request,
			HttpServletResponse response, @PathVariable Long id) {
		try {
			log.debug("Generando reporte de factura {}", id);
			FacturaAlmacen factura = (FacturaAlmacen) currentSession().get(
					FacturaAlmacen.class, id);
			Long almacenId = (Long) request.getSession().getAttribute(
					"almacenId");
			Query query = currentSession()
					.createQuery(
							"select r from Almacen a inner join a.reportes r where a.id = :id and r.nombre = :nombre");
			query.setLong("id", almacenId);
			query.setString("nombre", "facturaAlmacen_salida");
			Reporte reporte = (Reporte) query.uniqueResult();
			JasperReport subreporte1 = reporte.getReporte();

			Map<String, Object> params = new HashMap<>();
			params.put("ORGANIZACION", factura.getAlmacen().getEmpresa()
					.getOrganizacion().getNombreCompleto());
			params.put("EMPRESA", factura.getAlmacen().getEmpresa()
					.getNombreCompleto());
			params.put("ALMACEN", factura.getAlmacen().getNombre());
			List<LoteSalida> salidas = new ArrayList<>();
			for (Salida salida : factura.getSalidas()) {
				salidas.addAll(salida.getLotes());
			}
			params.put("datasource1", new JRBeanCollectionDataSource(salidas));
			params.put("subreporte1", subreporte1);

			query.setLong("id", almacenId);
			query.setString("nombre", "facturaAlmacen_entrada");
			reporte = (Reporte) query.uniqueResult();
			JasperReport subreporte2 = reporte.getReporte();
			List<LoteEntrada> entradas = new ArrayList<>();
			for (Entrada entrada : factura.getEntradas()) {
				entradas.addAll(entrada.getLotes());
			}
			params.put("datasource2", new JRBeanCollectionDataSource(entradas));
			params.put("subreporte2", subreporte2);

			query.setLong("id", almacenId);
			query.setString("nombre", "facturaAlmacen");
			reporte = (Reporte) query.uniqueResult();

			List<FacturaAlmacen> facturas = new ArrayList<>();
			facturas.add(factura);
			JasperPrint jasperPrint = JasperFillManager.fillReport(
					reporte.getReporte(), params,
					new JRBeanCollectionDataSource(facturas));
			byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);
			String nombre = factura.getFolio();
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename="
					+ nombre + ".pdf");
			if (archivo != null) {
				response.setContentLength(archivo.length);
				try (BufferedOutputStream bos = new BufferedOutputStream(
						response.getOutputStream())) {
					bos.write(archivo);
					bos.flush();
				}
			}
		} catch (JRException | IOException e) {
			log.error("No se pudo crear el reporte de la factura " + id, e);
		}
		return "redirect:/inventario/factura/ver/" + id;
	}
}
