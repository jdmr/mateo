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
import mx.edu.um.mateo.rh.service.SeccionManager;

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
@RequestMapping("/rh/secciones")
public class SeccionController extends BaseController {

//	@Autowired
//	private SeccionManager mgr;
//
//	@SuppressWarnings("unchecked")
//	@RequestMapping
//	public String lista(HttpServletRequest request,
//			HttpServletResponse response,
//			@RequestParam(required = false) String filtro,
//			@RequestParam(required = false) Long pagina,
//			@RequestParam(required = false) String tipo,
//			@RequestParam(required = false) String correo,
//			@RequestParam(required = false) String order,
//			@RequestParam(required = false) String sort, Usuario usuario,
//			Errors errors, Model modelo) {
//		log.debug("Mostrando secciones {}");
//
//		Map<String, Object> params = new HashMap<>();
//		Puesto seccion = null;
//
//		Long organizacionId = (Long) request.getSession().getAttribute(
//				"organizacionId");
//		params.put("organizacion", organizacionId);
//		if (StringUtils.isNotBlank(filtro)) {
//			params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
//		}
//		if (StringUtils.isNotBlank(order)) {
//			params.put(Constantes.CONTAINSKEY_ORDER, order);
//			params.put(Constantes.CONTAINSKEY_SORT, sort);
//		}
//
//		if (StringUtils.isNotBlank(tipo)) {
//			params.put(Constantes.CONTAINSKEY_REPORTE, true);
//			params = mgr.getSecciones(seccion);
//			try {
//				generaReporte(tipo,
//						(List<Puesto>) params.get(Constants.SECCION_LIST),
//						response, Constants.SECCION_LIST,
//						mx.edu.um.mateo.general.utils.Constantes.ORG,
//						organizacionId);
//				return null;
//			} catch (ReporteException e) {
//				log.error("No se pudo generar el reporte", e);
//				params.remove(Constantes.CONTAINSKEY_REPORTE);
//				// errors.reject("error.generar.reporte");
//			}
//		}
//
//		if (StringUtils.isNotBlank(correo)) {
//			params.put(Constantes.CONTAINSKEY_REPORTE, true);
//			params = mgr.getSecciones(seccion);
//
//			params.remove(Constantes.CONTAINSKEY_REPORTE);
//			try {
//				enviaCorreo(correo,
//						(List<CuentaMayor>) params.get(Constants.SECCION_LIST),
//						request, Constants.SECCION_LIST,
//						mx.edu.um.mateo.general.utils.Constantes.ORG,
//						organizacionId);
//				modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE,
//						"lista.enviada.message");
//				modelo.addAttribute(
//						Constantes.CONTAINSKEY_MESSAGE_ATTRS,
//						new String[] {
//								messageSource.getMessage("mayores.lista.label",
//										null, request.getLocale()),
//								ambiente.obtieneUsuario().getUsername() });
//			} catch (ReporteException e) {
//				log.error("No se pudo enviar el reporte por correo", e);
//			}
//		}
//
//		params = mgr.getSecciones(seccion);
//
//		modelo.addAttribute(Constants.CATEGORIA_LIST, mgr.getSecciones(seccion));
//
//		this.pagina(params, modelo, Constants.SECCION_LIST, pagina);
//
//		return "/rh/seccion/lista";
//	}
//
//	@RequestMapping("/ver/{id}")
//	public String ver(@PathVariable Long id, Model modelo) {
//		log.debug("Mostrando seccion {}", id);
//
//		Puesto seccion = mgr.getSeccion(id.toString());
//
//		modelo.addAttribute(Constants.SECCION_LIST, seccion);
//
//		return "/rh/seccion/ver";
//	}
//
//	@RequestMapping("/nueva")
//	public String nueva(Model modelo) {
//		log.debug("Nueva seccion");
//
//		Puesto seccion = new Puesto();
//		modelo.addAttribute(Constants.SECCION_LIST, seccion);
//		return "/rh/seccion/nueva";
//	}
//
//	@Transactional
//	@RequestMapping(value = "/crea", method = RequestMethod.POST)
//	public String crea(HttpServletRequest request,
//			HttpServletResponse response, @Valid Puesto seccion,
//			BindingResult bindingResult, Errors errors, Model modelo,
//			RedirectAttributes redirectAttributes) {
//		for (String nombre : request.getParameterMap().keySet()) {
//			log.debug("Param: {} : {}", nombre,
//					request.getParameterMap().get(nombre));
//		}
//		if (bindingResult.hasErrors()) {
//			log.debug("Hubo algun error en la forma, regresando");
//			return "/rh/seccion/nueva";
//		}
//
//		try {
//			mgr.saveSeccion(seccion);
//		} catch (ConstraintViolationException e) {
//			log.error("No se pudo crear la seccion", e);
//			return "/rh/seccion/nueva";
//		}
//
//		redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
//				"seccion.creada.message");
//		redirectAttributes.addFlashAttribute(
//				Constantes.CONTAINSKEY_MESSAGE_ATTRS,
//				new String[] { seccion.getNombre() });
//
//		return "redirect:" + "/rh/seccion/ver" + "/" + seccion.getId();
//	}
//
//	@RequestMapping("/edita/{id}")
//	public String edita(@PathVariable Long id, Model modelo) {
//		log.debug("Editar cuenta de mayor {}", id);
//		Puesto seccion = mgr.getSeccion(id.toString());
//
//		modelo.addAttribute(Constants.SECCION_LIST, seccion);
//
//		return "/rh/seccion/edita";
//	}
//
//	@Transactional
//	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
//	public String actualiza(HttpServletRequest request, @Valid Puesto seccion,
//			BindingResult bindingResult, Errors errors, Model modelo,
//			RedirectAttributes redirectAttributes) {
//		if (bindingResult.hasErrors()) {
//			log.error("Hubo algun error en la forma, regresando");
//			return "/rh/seccion/edita";
//		}
//		try {
//			mgr.saveSeccion(seccion);
//		} catch (ConstraintViolationException e) {
//			log.error("No se pudo crear la seccion", e);
//			return "/rh/seccion/nueva";
//		}
//
//		redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
//				"seccion.actualizada.message");
//		redirectAttributes.addFlashAttribute(
//				Constantes.CONTAINSKEY_MESSAGE_ATTRS,
//				new String[] { seccion.getNombre() });
//
//		return "redirect:" + "/rh/seccion/ver" + "/" + seccion.getId();
//	}
//
//	@Transactional
//	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
//	public String elimina(HttpServletRequest request, @RequestParam Long id,
//			Model modelo, @ModelAttribute Puesto seccion,
//			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
//		log.debug("Elimina seccion");
//		try {
//			mgr.removeSeccion(id.toString());
//
//			redirectAttributes
//					.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
//							"seccion.eliminada.message");
//			redirectAttributes.addFlashAttribute(
//					Constantes.CONTAINSKEY_MESSAGE_ATTRS,
//					new String[] { seccion.getNombre() });
//		} catch (Exception e) {
//			log.error("No se pudo eliminar la seccion " + id, e);
//			bindingResult
//					.addError(new ObjectError("seccion",
//							new String[] { "seccion.no.eliminada.message" },
//							null, null));
//			return "/rh/seccion/ver";
//		}
//
//		return "redirect:" + "/rh/seccion/ver";
//	}
}
