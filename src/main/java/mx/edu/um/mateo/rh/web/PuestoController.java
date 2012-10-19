/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.Constants;
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.rh.model.Puesto;
import mx.edu.um.mateo.rh.service.PuestoManager;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
 * @author osoto
 */

@Controller
@RequestMapping("/rh/puestos")
public class PuestoController extends BaseController {

	@Autowired
	private PuestoManager mgr;

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
		log.debug("Mostrando puestos {}");

		Map<String, Object> params = new HashMap<>();
		Puesto puesto = null;

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
						(List<Puesto>) params.get(Constants.PUESTO_LIST),
						response, Constants.PUESTO_LIST,
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
						(List<CuentaMayor>) params.get(Constants.PUESTO_LIST),
						request, Constants.PUESTO_LIST,
						mx.edu.um.mateo.general.utils.Constantes.ORG,
						organizacionId);
				modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE,
						"lista.enviada.message");
				modelo.addAttribute(
						Constantes.CONTAINSKEY_MESSAGE_ATTRS,
						new String[] {
								messageSource.getMessage("puesto.lista.label",
										null, request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}

		params = mgr.lista(params);

		modelo.addAttribute(Constants.PUESTO_LIST, params.get(Constants.PUESTO_LIST));

		this.pagina(params, modelo, Constants.PUESTO_LIST, pagina);

		return "/rh/puestos/lista";
	}

	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando puesto {}", id);

		Puesto puesto = mgr.obtiene(id);

		modelo.addAttribute(Constants.PUESTO_KEY, puesto);

		return "/rh/puestos/ver";
	}

	@RequestMapping("/nuevo")
	public String nueva(Model modelo) {
		log.debug("Nuevo puesto");

		Puesto puesto = new Puesto();
		modelo.addAttribute(Constants.PUESTO_KEY, puesto);
		return "/rh/puestos/nuevo";
	}

	@Transactional
	@RequestMapping(value = "/crea", method = RequestMethod.POST)
	public String crea(HttpServletRequest request,
			HttpServletResponse response, @Valid Puesto puesto,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");
			return "/rh/puestos/nuevo";
		}

		try {
			mgr.graba(puesto, null);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear el puesto", e);
			return "/rh/puestos/nuevo";
		}

		redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
				"puesto.creado.message");
		redirectAttributes.addFlashAttribute(
				Constantes.CONTAINSKEY_MESSAGE_ATTRS,
				new String[] { puesto.getDescripcion() });

		return "redirect:" + "/rh/puestos/ver" + "/" + puesto.getId();
	}

	@RequestMapping("/edita/{id}")
	public String edita(@PathVariable Long id, Model modelo) {
		log.debug("Editar el puesto {}", id);
		Puesto puesto = mgr.obtiene(id);

		modelo.addAttribute(Constants.PUESTO_KEY, puesto);

		return "/rh/puestos/edita";
	}

	@Transactional
	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
	public String actualiza(HttpServletRequest request, @Valid Puesto puesto,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.error("Hubo algun error en la forma, regresando");
			return "/rh/puestos/edita";
		}
		try {
			mgr.graba(puesto, null);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear el puesto", e);
			return "/rh/puestos/nuevo";
		}

		redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
				"puesto.actualizado.message");
		redirectAttributes.addFlashAttribute(
				Constantes.CONTAINSKEY_MESSAGE_ATTRS,
				new String[] { puesto.getDescripcion() });

		return "redirect:" + "/rh/puestos/ver" + "/" + puesto.getId();
	}

	@Transactional
	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute Puesto puesto,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina puesto");
		try {
			mgr.elimina(id);

			redirectAttributes
					.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
							"puesto.eliminado.message");
			redirectAttributes.addFlashAttribute(
					Constantes.CONTAINSKEY_MESSAGE_ATTRS,
					new String[] { puesto.getDescripcion() });
		} catch (Exception e) {
			log.error("No se pudo eliminar el puesto " + id, e);
			bindingResult
					.addError(new ObjectError("puesto",
							new String[] { "puesto.no.eliminado.message" },
							null, null));
			return "/rh/puestos/ver";
		}

		return "redirect:" + "/rh/puestos";
	}
}
