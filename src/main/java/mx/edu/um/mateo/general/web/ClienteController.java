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

import mx.edu.um.mateo.general.dao.ClienteDao;
import mx.edu.um.mateo.general.dao.TipoClienteDao;
import mx.edu.um.mateo.general.model.Cliente;
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
@RequestMapping("/admin/cliente")
public class ClienteController extends BaseController {

	@Autowired
	private ClienteDao clienteDao;
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
			@RequestParam(required = false) String sort, Model modelo) {
		log.debug("Mostrando lista de clientes");
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
			params = clienteDao.lista(params);
			try {
				generaReporte(tipo, (List<Cliente>) params.get("clientes"),
						response, "clientes", Constantes.EMP, empresaId);
				return null;
			} catch (ReporteException e) {
				log.error("No se pudo generar el reporte", e);
			}
		}

		if (StringUtils.isNotBlank(correo)) {
			params.put("reporte", true);
			params = clienteDao.lista(params);

			params.remove("reporte");
			try {
				enviaCorreo(correo, (List<Cliente>) params.get("clientes"),
						request, "clientes", Constantes.EMP, empresaId);
				modelo.addAttribute("message", "lista.enviado.message");
				modelo.addAttribute(
						"messageAttrs",
						new String[] {
								messageSource.getMessage("cliente.lista.label",
										null, request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}
		params = clienteDao.lista(params);
		modelo.addAttribute("clientes", params.get("clientes"));

		this.pagina(params, modelo, "clientes", pagina);

		return "admin/cliente/lista";
	}

	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando cliente {}", id);
		Cliente cliente = clienteDao.obtiene(id);

		modelo.addAttribute("cliente", cliente);

		return "admin/cliente/ver";
	}

	@RequestMapping("/nuevo")
	public String nuevo(HttpServletRequest request, Model modelo) {
		log.debug("Nuevo cliente");
		Cliente cliente = new Cliente();
		modelo.addAttribute("cliente", cliente);

		Map<String, Object> params = new HashMap<>();
		params.put("empresa", request.getSession().getAttribute("empresaId"));
		params.put("reporte", true);
		params = tipoClienteDao.lista(params);
		modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));

		return "admin/cliente/nuevo";
	}

	@RequestMapping(value = "/crea", method = RequestMethod.POST)
	public String crea(HttpServletRequest request,
			HttpServletResponse response, @Valid Cliente cliente,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");

			Map<String, Object> params = new HashMap<>();
			params.put("empresa", request.getSession()
					.getAttribute("empresaId"));
			params.put("reporte", true);
			params = tipoClienteDao.lista(params);
			modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));

			return "admin/cliente/nuevo";
		}

		try {
			Usuario usuario = ambiente.obtieneUsuario();
			log.debug("TipoCliente: {}", cliente.getTipoCliente().getId());
			cliente = clienteDao.crea(cliente, usuario);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear al cliente", e);
			errors.rejectValue("nombre", "campo.duplicado.message",
					new String[] { "nombre" }, null);

			Map<String, Object> params = new HashMap<>();
			params.put("empresa", request.getSession()
					.getAttribute("empresaId"));
			params.put("reporte", true);
			params = tipoClienteDao.lista(params);
			modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));

			return "admin/cliente/nuevo";
		}

		redirectAttributes.addFlashAttribute("message",
				"cliente.creado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { cliente.getNombre() });

		return "redirect:/admin/cliente/ver/" + cliente.getId();
	}

	@RequestMapping("/edita/{id}")
	public String edita(HttpServletRequest request, @PathVariable Long id,
			Model modelo) {
		log.debug("Edita cliente {}", id);
		Cliente cliente = clienteDao.obtiene(id);
		modelo.addAttribute("cliente", cliente);

		Map<String, Object> params = new HashMap<>();
		params.put("empresa", request.getSession().getAttribute("empresaId"));
		params.put("reporte", true);
		params = tipoClienteDao.lista(params);
		modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));

		return "admin/cliente/edita";
	}

	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
	public String actualiza(HttpServletRequest request, @Valid Cliente cliente,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.error("Hubo algun error en la forma, regresando");

			Map<String, Object> params = new HashMap<>();
			params.put("empresa", request.getSession()
					.getAttribute("empresaId"));
			params.put("reporte", true);
			params = tipoClienteDao.lista(params);
			modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));

			return "admin/cliente/edita";
		}

		try {
			Usuario usuario = ambiente.obtieneUsuario();
			cliente = clienteDao.actualiza(cliente, usuario);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear la cliente", e);
			errors.rejectValue("nombre", "campo.duplicado.message",
					new String[] { "nombre" }, null);

			Map<String, Object> params = new HashMap<>();
			params.put("empresa", request.getSession()
					.getAttribute("empresaId"));
			params.put("reporte", true);
			params = tipoClienteDao.lista(params);
			modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));

			return "admin/cliente/nuevo";
		}

		redirectAttributes.addFlashAttribute("message",
				"cliente.actualizado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { cliente.getNombre() });

		return "redirect:/admin/cliente/ver/" + cliente.getId();
	}

	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute Cliente cliente,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina cliente");
		try {
			String nombre = clienteDao.elimina(id);

			redirectAttributes.addFlashAttribute("message",
					"cliente.eliminado.message");
			redirectAttributes.addFlashAttribute("messageAttrs",
					new String[] { nombre });
		} catch (Exception e) {
			log.error("No se pudo eliminar la cliente " + id, e);
			bindingResult
					.addError(new ObjectError("cliente",
							new String[] { "cliente.no.eliminado.message" },
							null, null));
			return "admin/cliente/ver";
		}

		return "redirect:/admin/cliente";
	}

}
