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
package mx.edu.um.mateo.inventario.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inventario.dao.TipoProductoDao;
import mx.edu.um.mateo.inventario.model.TipoProducto;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/inventario/tipoProducto")
public class TipoProductoController extends BaseController {

	@Autowired
	private TipoProductoDao tipoProductoDao;

	@SuppressWarnings("unchecked")
	@RequestMapping
	public String lista(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String filtro,
			@RequestParam(required = false) Long pagina,
			@RequestParam(required = false) String tipo,
			@RequestParam(required = false) String correo,
			@RequestParam(required = false) String order,
			@RequestParam(required = false) String sort, Usuario usuario,
			Errors errors, Model modelo) {
		log.debug("Mostrando lista de tipos de productos");
		Map<String, Object> params = new HashMap<>();
		Long almacenId = (Long) request.getSession().getAttribute("almacenId");
		params.put("almacen", almacenId);
		if (StringUtils.isNotBlank(filtro)) {
			params.put("filtro", filtro);
		}
		if (StringUtils.isNotBlank(order)) {
			params.put("order", order);
			params.put("sort", sort);
		}
		if (pagina != null) {
			params.put("pagina", pagina);
		}

		if (StringUtils.isNotBlank(tipo)) {
			params.put("reporte", true);
			params = tipoProductoDao.lista(params);
			try {
				generaReporte(tipo,
						(List<TipoProducto>) params.get("tiposDeProducto"),
						response, "tiposDeProducto", Constantes.ALM, almacenId);
				return null;
			} catch (ReporteException e) {
				log.error("No se pudo generar el reporte", e);
				params.remove("reporte");
				errors.reject("error.generar.reporte");
			}
		}

		if (StringUtils.isNotBlank(correo)) {
			params.put("reporte", true);
			params = tipoProductoDao.lista(params);

			params.remove("reporte");
			try {
				enviaCorreo(correo,
						(List<TipoProducto>) params.get("tiposDeProducto"),
						request, "tiposDeProducto", Constantes.ALM, almacenId);
				modelo.addAttribute("message", "lista.enviada.message");
				modelo.addAttribute(
						"messageAttrs",
						new String[] {
								messageSource.getMessage(
										"tipoProducto.lista.label", null,
										request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}
		params = tipoProductoDao.lista(params);
		modelo.addAttribute("tiposDeProducto", params.get("tiposDeProducto"));

		this.pagina(params, modelo, "tiposDeProducto", pagina);

		return "inventario/tipoProducto/lista";
	}

	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando tipoProducto {}", id);
		TipoProducto tipoProducto = tipoProductoDao.obtiene(id);

		modelo.addAttribute("tipoProducto", tipoProducto);

		return "inventario/tipoProducto/ver";
	}

	@RequestMapping("/nuevo")
	public String nuevo(Model modelo) {
		log.debug("Nuevo tipoProducto");
		TipoProducto tipoProducto = new TipoProducto();
		modelo.addAttribute("tipoProducto", tipoProducto);
		return "inventario/tipoProducto/nuevo";
	}

	@RequestMapping(value = "/crea", method = RequestMethod.POST)
	public String crea(HttpServletRequest request,
			HttpServletResponse response, @Valid TipoProducto tipoProducto,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");
			return "inventario/tipoProducto/nuevo";
		}

		try {
			Usuario usuario = ambiente.obtieneUsuario();
			tipoProducto = tipoProductoDao.crea(tipoProducto, usuario);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear al tipoProducto", e);
			errors.rejectValue("nombre", "campo.duplicado.message",
					new String[] { "nombre" }, null);
			return "inventario/tipoProducto/nuevo";
		}

		redirectAttributes.addFlashAttribute("message",
				"tipoProducto.creado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { tipoProducto.getNombre() });

		return "redirect:/inventario/tipoProducto/ver/" + tipoProducto.getId();
	}

	@RequestMapping("/edita/{id}")
	public String edita(@PathVariable Long id, Model modelo) {
		log.debug("Edita tipoProducto {}", id);
		TipoProducto tipoProducto = tipoProductoDao.obtiene(id);
		modelo.addAttribute("tipoProducto", tipoProducto);
		return "inventario/tipoProducto/edita";
	}

	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
	public String actualiza(HttpServletRequest request,
			@Valid TipoProducto tipoProducto, BindingResult bindingResult,
			Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.error("Hubo algun error en la forma, regresando");
			return "inventario/tipoProducto/edita";
		}

		try {
			Usuario usuario = ambiente.obtieneUsuario();
			tipoProducto = tipoProductoDao.actualiza(tipoProducto, usuario);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear la tipoProducto", e);
			errors.rejectValue("nombre", "campo.duplicado.message",
					new String[] { "nombre" }, null);
			return "inventario/tipoProducto/nuevo";
		}

		redirectAttributes.addFlashAttribute("message",
				"tipoProducto.actualizado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { tipoProducto.getNombre() });

		return "redirect:/inventario/tipoProducto/ver/" + tipoProducto.getId();
	}

	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute TipoProducto tipoProducto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina tipoProducto");
		try {
			String nombre = tipoProductoDao.elimina(id);

			redirectAttributes.addFlashAttribute("message",
					"tipoProducto.eliminado.message");
			redirectAttributes.addFlashAttribute("messageAttrs",
					new String[] { nombre });
		} catch (Exception e) {
			log.error("No se pudo eliminar la tipoProducto " + id, e);
			bindingResult.addError(new ObjectError("tipoProducto",
					new String[] { "tipoProducto.no.eliminado.message" }, null,
					null));
			return "inventario/tipoProducto/ver";
		}

		return "redirect:/inventario/tipoProducto";
	}
}
