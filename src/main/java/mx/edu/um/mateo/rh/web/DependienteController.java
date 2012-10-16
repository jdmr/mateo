/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
 * Copyright 2012 jdmr.
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
package mx.edu.um.mateo.rh.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.rh.model.Dependiente;
import mx.edu.um.mateo.rh.service.DependienteManager;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author AMDA
 */
@Controller
@RequestMapping("/rh/dependiente")

public class DependienteController extends BaseController {

	@Autowired
	private DependienteManager mgr;

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
		log.debug("Mostrando dependientes {}");

		Map<String, Object> params = new HashMap<>();
		Dependiente dependiente = null;

		Long organizacionId = (Long) request.getSession().getAttribute(
				"organizacionId");
		params.put("organizacion", organizacionId);
		if (StringUtils.isNotBlank(filtro)) {
			params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
		}
		if (StringUtils.isNotBlank(order)) {
			params.put(Constantes.CONTAINSKEY_ORDER, order);
			params.put(Constantes.CONTAINSKEY_SORT, sort);
		}

		if (StringUtils.isNotBlank(tipo)) {
			params.put(Constantes.CONTAINSKEY_REPORTE, true);
			params = mgr.lista(params);
			try {
				generaReporte(tipo,
						(List<Dependiente>) params.get(Constantes.DEPENDIENTE_LIST),
						response, Constantes.DEPENDIENTE_LIST,
						mx.edu.um.mateo.general.utils.Constantes.ORG,
						organizacionId);
				return null;
			} catch (ReporteException e) {
				log.error("No se pudo generar el reporte", e);
				params.remove(Constantes.CONTAINSKEY_REPORTE);
				// errors.reject("error.generar.reporte");
			}
		}

		if (StringUtils.isNotBlank(correo)) {
			params.put(Constantes.CONTAINSKEY_REPORTE, true);
			params = mgr.lista(params);

			params.remove(Constantes.CONTAINSKEY_REPORTE);
			try {
				enviaCorreo(correo,
						(List<CuentaMayor>) params.get(Constantes.DEPENDIENTE_LIST),
						request, Constantes.DEPENDIENTE_LIST,
						mx.edu.um.mateo.general.utils.Constantes.ORG,
						organizacionId);
				modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE,
						"lista.enviada.message");
				modelo.addAttribute(
						Constantes.CONTAINSKEY_MESSAGE_ATTRS,
						new String[] {
								messageSource.getMessage("dependiente.lista.label",
										null, request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}

		params = mgr.lista(params);

		modelo.addAttribute(Constantes.DEPENDIENTE_LIST, mgr.lista(params));

		this.pagina(params, modelo,Constantes.DEPENDIENTE_LIST, pagina);

		return "/rh/dependiente/lista";
	}

	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando dependiente {}", id);

		Dependiente dependiente = mgr.obtiene(id.toString());

		modelo.addAttribute(Constantes.DEPENDIENTE_KEY, dependiente);

		return "/rh/dependiente/ver";
	}

	@RequestMapping("/nuevo")
	public String nuevo(Model modelo) {
		log.debug("Nuevo dependiente");

		Dependiente dependiente = new Dependiente();
		modelo.addAttribute(Constantes.DEPENDIENTE_KEY, dependiente);
		return "/rh/dependiente/nuevo";
	}

	@Transactional
	@RequestMapping(value = "/crea", method = RequestMethod.POST)
	public String crea(HttpServletRequest request,
			HttpServletResponse response, @Valid Dependiente dependiente,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");
			return "/rh/dependiente/nuevo";
		}

		try {
			mgr.graba(dependiente);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear la dependiente", e);
			return "/rh/dependiente/nuevo";
		}

		redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
				"dependiente.creado.message");
		redirectAttributes.addFlashAttribute(
				Constantes.CONTAINSKEY_MESSAGE_ATTRS,
				new String[] { dependiente.getNombre() });

		return "redirect:" + "/rh/dependiente/ver" + "/" + dependiente.getId();
	}

	@RequestMapping("/edita/{id}")
	public String edita(@PathVariable Long id, Model modelo) {
		log.debug("Editar cuenta de mayor {}", id);
		Dependiente dependiente = mgr.obtiene(id.toString());

		modelo.addAttribute(Constantes.DEPENDIENTE_LIST, dependiente);

		return "/rh/dependiente/edita";
	}

	@Transactional
	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
	public String actualiza(HttpServletRequest request, @Valid Dependiente dependiente,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.error("Hubo algun error en la forma, regresando");
			return "/rh/dependiente/edita";
		}
		try {
			mgr.graba(dependiente);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear la dependiente", e);
			return "/rh/dependiente/nuevo";
		}

		redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
				"dependiente.actualizado.message");
		redirectAttributes.addFlashAttribute(
				Constantes.CONTAINSKEY_MESSAGE_ATTRS,
				new String[] { dependiente.getNombre() });

		return "redirect:" + "/rh/dependiente/ver" + "/" + dependiente.getId();
	}

	@Transactional
	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute Dependiente dependiente,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina dependiente");
		try {
			mgr.elimina(id.toString());

			redirectAttributes
					.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
							"dependiente.eliminado.message");
			redirectAttributes.addFlashAttribute(
					Constantes.CONTAINSKEY_MESSAGE_ATTRS,
					new String[] { dependiente.getNombre() });
		} catch (Exception e) {
			log.error("No se pudo eliminar la dependiente " + id, e);
			bindingResult
					.addError(new ObjectError("dependiente",
							new String[] { "dependiente.no.eliminado.message" },
							null, null));
			return "/rh/dependiente/ver";
		}

		return "redirect:" + "/rh/dependiente/ver";
	}
}

