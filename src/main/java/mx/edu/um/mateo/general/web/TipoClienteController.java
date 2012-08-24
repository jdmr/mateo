/*
 * The MIT License
 *
 * Copyright 2012 J. David Mendoza <jdmendoza@um.edu.mx>.
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import mx.edu.um.mateo.general.dao.TipoClienteDao;
import mx.edu.um.mateo.general.model.TipoCliente;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;

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
@RequestMapping("/admin/tipoCliente")
public class TipoClienteController extends BaseController {

	@Autowired
	private TipoClienteDao tipoClienteDao;

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
		log.debug("Mostrando lista de tipos de clientes");
		Map<String, Object> params = new HashMap<>();
		Long empresaId = (Long) request.getSession().getAttribute("empresaId");
		params.put("empresa", empresaId);
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
			params = tipoClienteDao.lista(params);
			try {
				generaReporte(tipo,
						(List<TipoCliente>) params.get("tiposDeCliente"),
						response, "tiposDeCliente", Constantes.EMP, empresaId);
				return null;
			} catch (ReporteException e) {
				log.error("No se pudo generar el reporte", e);
				params.remove("reporte");
				errors.reject("error.generar.reporte");
			}
		}

		if (StringUtils.isNotBlank(correo)) {
			params.put("reporte", true);
			params = tipoClienteDao.lista(params);

			params.remove("reporte");
			try {
				enviaCorreo(correo,
						(List<TipoCliente>) params.get("tiposDeCliente"),
						request, "tiposDeCliente", Constantes.EMP, empresaId);
				modelo.addAttribute("message", "lista.enviado.message");
				modelo.addAttribute(
						"messageAttrs",
						new String[] {
								messageSource.getMessage(
										"tipoCliente.lista.label", null,
										request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}
		params = tipoClienteDao.lista(params);
		modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));

		this.pagina(params, modelo, "tiposDeCliente", pagina);

		return "admin/tipoCliente/lista";
	}

	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando tipoCliente {}", id);
		TipoCliente tipoCliente = tipoClienteDao.obtiene(id);

		modelo.addAttribute("tipoCliente", tipoCliente);

		return "admin/tipoCliente/ver";
	}

	@RequestMapping("/nuevo")
	public String nuevo(Model modelo) {
		log.debug("Nuevo tipoCliente");
		TipoCliente tipoCliente = new TipoCliente();
		modelo.addAttribute("tipoCliente", tipoCliente);
		return "admin/tipoCliente/nuevo";
	}

	@RequestMapping(value = "/crea", method = RequestMethod.POST)
	public String crea(HttpServletRequest request,
			HttpServletResponse response, @Valid TipoCliente tipoCliente,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");
			return "admin/tipoCliente/nuevo";
		}

		try {
			Usuario usuario = ambiente.obtieneUsuario();
			tipoCliente = tipoClienteDao.crea(tipoCliente, usuario);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear al tipoCliente", e);
			errors.rejectValue("nombre", "campo.duplicado.message",
					new String[] { "nombre" }, null);
			return "admin/tipoCliente/nuevo";
		}

		redirectAttributes.addFlashAttribute("message",
				"tipoCliente.creado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { tipoCliente.getNombre() });

		return "redirect:/admin/tipoCliente/ver/" + tipoCliente.getId();
	}

	@RequestMapping("/edita/{id}")
	public String edita(@PathVariable Long id, Model modelo) {
		log.debug("Edita tipoCliente {}", id);
		TipoCliente tipoCliente = tipoClienteDao.obtiene(id);
		modelo.addAttribute("tipoCliente", tipoCliente);
		return "admin/tipoCliente/edita";
	}

	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
	public String actualiza(HttpServletRequest request,
			@Valid TipoCliente tipoCliente, BindingResult bindingResult,
			Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.error("Hubo algun error en la forma, regresando");
			return "admin/tipoCliente/edita";
		}

		try {
			Usuario usuario = ambiente.obtieneUsuario();
			tipoCliente = tipoClienteDao.actualiza(tipoCliente, usuario);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear la tipoCliente", e);
			errors.rejectValue("nombre", "campo.duplicado.message",
					new String[] { "nombre" }, null);
			return "admin/tipoCliente/nuevo";
		}

		redirectAttributes.addFlashAttribute("message",
				"tipoCliente.actualizado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { tipoCliente.getNombre() });

		return "redirect:/admin/tipoCliente/ver/" + tipoCliente.getId();
	}

	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute TipoCliente tipoCliente,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina tipoCliente");
		try {
			String nombre = tipoClienteDao.elimina(id);

			redirectAttributes.addFlashAttribute("message",
					"tipoCliente.eliminado.message");
			redirectAttributes.addFlashAttribute("messageAttrs",
					new String[] { nombre });
		} catch (Exception e) {
			log.error("No se pudo eliminar la tipoCliente " + id, e);
			bindingResult.addError(new ObjectError("tipoCliente",
					new String[] { "tipoCliente.no.eliminado.message" }, null,
					null));
			return "admin/tipoCliente/ver";
		}

		return "redirect:/admin/tipoCliente";
	}
}
