/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

/**
 *
 * @author IrasemaBalderas
 */
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
import mx.edu.um.mateo.rh.model.Colegio;
import mx.edu.um.mateo.rh.service.ColegioManager;

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
@RequestMapping("/rh/colegios")
public class ColegioController extends BaseController {

	@Autowired
	private ColegioManager mgr;

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
		log.debug("Mostrando colegios {}");

		Map<String, Object> params = new HashMap<>();
		Colegio colegio = null;

		Long empresaId = (Long) request.getSession().getAttribute(
				"empresaId");
		params.put("empresa", empresaId);
		if (StringUtils.isNotBlank(filtro)) {
			params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
		}
		if (StringUtils.isNotBlank(order)) {
			params.put(Constantes.CONTAINSKEY_ORDER, order);
			params.put(Constantes.CONTAINSKEY_SORT, sort);
		}

		if (StringUtils.isNotBlank(tipo)) {
			params.put(Constantes.CONTAINSKEY_REPORTE, true);
			params = mgr.getColegios(colegio);
			try {
				generaReporte(tipo,
						(List<Colegio>) params.get(Constantes.CONTAINSKEY_COLEGIOS),
						response, Constantes.CONTAINSKEY_COLEGIOS,
						mx.edu.um.mateo.general.utils.Constantes.ORG,
						empresaId);
				return null;
			} catch (ReporteException e) {
				log.error("No se pudo generar el reporte", e);
				params.remove(Constantes.CONTAINSKEY_REPORTE);
				// errors.reject("error.generar.reporte");
			}
		}

		if (StringUtils.isNotBlank(correo)) {
			params.put(Constantes.CONTAINSKEY_REPORTE, true);
			params = mgr.getColegios(colegio);

			params.remove(Constantes.CONTAINSKEY_REPORTE);
			try {
				enviaCorreo(correo,
						(List<CuentaMayor>) params.get(Constantes.CONTAINSKEY_COLEGIOS),
						request, Constantes.CONTAINSKEY_COLEGIOS,
						mx.edu.um.mateo.general.utils.Constantes.ORG,
						empresaId);
				modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE,
						"lista.enviada.message");
				modelo.addAttribute(
						Constantes.CONTAINSKEY_MESSAGE_ATTRS,
						new String[] {
								messageSource.getMessage("mayores.lista.label",
										null, request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}

		params = mgr.getColegios(colegio);

		modelo.addAttribute(Constantes.ADDATTRIBUTE_COLEGIO, mgr.getColegios(colegio));

		this.pagina(params, modelo, Constantes.CONTAINSKEY_COLEGIOS, pagina);

		return "/rh/colegio/lista";
	}

	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando colegio {}", id);

		Colegio colegio = mgr.getColegio(id);

		modelo.addAttribute(Constantes.CONTAINSKEY_COLEGIOS, colegio);

		return "/rh/colegio/ver";
	}

	@RequestMapping("/nueva")
	public String nueva(Model modelo) {
		log.debug("Nueva colegio");

		Colegio colegio = new Colegio();
		modelo.addAttribute(Constantes.CONTAINSKEY_COLEGIOS, colegio);
		return "/rh/colegio/nueva";
	}

	@Transactional
	@RequestMapping(value = "/crea", method = RequestMethod.POST)
	public String crea(HttpServletRequest request,
			HttpServletResponse response, @Valid Colegio colegio,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");
			return "/rh/colegio/nueva";
		}

		try {
			mgr.saveColegio(colegio);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear la colegio", e);
			return "/rh/colegio/nueva";
		}

		redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
				"colegio.creada.message");
		redirectAttributes.addFlashAttribute(
				Constantes.CONTAINSKEY_MESSAGE_ATTRS,
				new String[] { colegio.getNombre() });

		return "redirect:" + "/rh/colegio/ver" + "/" + colegio.getId();
	}

	@RequestMapping("/edita/{id}")
	public String edita(@PathVariable Long id, Model modelo) {
		log.debug("Editar cuenta de mayor {}", id);
		Colegio colegio = mgr.getColegio(id);

		modelo.addAttribute(Constantes.CONTAINSKEY_COLEGIOS, colegio);

		return "/rh/colegio/edita";
	}

	@Transactional
	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
	public String actualiza(HttpServletRequest request, @Valid Colegio colegio,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.error("Hubo algun error en la forma, regresando");
			return "/rh/colegio/edita";
		}
		try {
			mgr.saveColegio(colegio);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear la colegio", e);
			return "/rh/colegio/nueva";
		}

		redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
				"colegio.actualizada.message");
		redirectAttributes.addFlashAttribute(
				Constantes.CONTAINSKEY_MESSAGE_ATTRS,
				new String[] { colegio.getNombre() });

		return "redirect:" + "/rh/colegio/ver" + "/" + colegio.getId();
	}

	@Transactional
	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute Colegio colegio,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina colegio");
		try {
			mgr.removeColegio(id);

			redirectAttributes
					.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
							"colegio.eliminada.message");
			redirectAttributes.addFlashAttribute(
					Constantes.CONTAINSKEY_MESSAGE_ATTRS,
					new String[] { colegio.getNombre() });
		} catch (Exception e) {
			log.error("No se pudo eliminar la colegio " + id, e);
			bindingResult
					.addError(new ObjectError("colegio",
							new String[] { "colegio.no.eliminada.message" },
							null, null));
			return "/rh/colegio/ver";
		}

		return "redirect:" + "/rh/colegio/ver";
	}
}

