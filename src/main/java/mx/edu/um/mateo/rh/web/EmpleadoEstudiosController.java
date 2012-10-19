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
import mx.edu.um.mateo.contabilidad.model.CuentaMayor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.rh.model.Dependiente;
import mx.edu.um.mateo.rh.model.EmpleadoEstudios;
import mx.edu.um.mateo.rh.service.DependienteManager;
import mx.edu.um.mateo.rh.service.EmpleadoEstudiosManager;
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
@RequestMapping("/rh/empleadoEstudios")

public class EmpleadoEstudiosController extends BaseController {
    
    @Autowired
	private EmpleadoEstudiosManager mgr;

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
		log.debug("Mostrando empleadoEstudios {}");

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
						(List<Dependiente>) params.get(Constantes.EMPLEADOESTUDIOS_LIST),
						response, Constantes.EMPLEADOESTUDIOS_LIST,
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
						(List<CuentaMayor>) params.get(Constantes.EMPLEADOESTUDIOS_LIST),
						request, Constantes.EMPLEADOESTUDIOS_LIST,
						mx.edu.um.mateo.general.utils.Constantes.ORG,
						organizacionId);
				modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE,
						"lista.enviada.message");
				modelo.addAttribute(
						Constantes.CONTAINSKEY_MESSAGE_ATTRS,
						new String[] {
								messageSource.getMessage("empleadoEstudios.lista.label",
										null, request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}

		params = mgr.lista(params);

		modelo.addAttribute(Constantes.EMPLEADOESTUDIOS_LIST, mgr.lista(params));

		this.pagina(params, modelo,Constantes.EMPLEADOESTUDIOS_LIST, pagina);

		return "/rh/empleadoEstudios/lista";
	}

	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando empleadoEstudios {}", id);

		EmpleadoEstudios empleadoEstudios = mgr.obtiene(id.toString());

		modelo.addAttribute(Constantes.EMPLEADOESTUDIOS_KEY, empleadoEstudios);

		return "/rh/empleadoEstudios/ver";
	}

	@RequestMapping("/nuevo")
	public String nuevo(Model modelo) {
		log.debug("Nuevo empleadoEstudios");

		EmpleadoEstudios empleadoEstudios = new EmpleadoEstudios();
		modelo.addAttribute(Constantes.EMPLEADOESTUDIOS_KEY, empleadoEstudios);
		return "/rh/empleadoEstudios/nuevo";
	}

	@Transactional
	@RequestMapping(value = "/crea", method = RequestMethod.POST)
	public String crea(HttpServletRequest request,
			HttpServletResponse response, @Valid EmpleadoEstudios empleadoEstudios,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");
			return "/rh/empleadoEstudios/nuevo";
		}

		try {
			mgr.graba(empleadoEstudios);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear la empleadoEstudios", e);
			return "/rh/empleadoEstudios/nuevo";
		}

		redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
				"empleadoEstudios.creado.message");
		redirectAttributes.addFlashAttribute(
				Constantes.CONTAINSKEY_MESSAGE_ATTRS,
				new String[] { empleadoEstudios.getNombreEstudios() });

		return "redirect:" + "/rh/empleadoEstudios/ver" + "/" + empleadoEstudios.getId();
	}

	@RequestMapping("/edita/{id}")
	public String edita(@PathVariable Long id, Model modelo) {
		log.debug("Editar cuenta de mayor {}", id);
		EmpleadoEstudios empleadoEstudios = mgr.obtiene(id.toString());

		modelo.addAttribute(Constantes.EMPLEADOESTUDIOS_LIST, empleadoEstudios);

		return "/rh/empleadoEstudios/edita";
	}

	@Transactional
	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
	public String actualiza(HttpServletRequest request, @Valid EmpleadoEstudios empleadoEstudios,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.error("Hubo algun error en la forma, regresando");
			return "/rh/empleadoEstudios/edita";
		}
		try {
			mgr.graba(empleadoEstudios);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear empleadoEstudios", e);
			return "/rh/empleadoEstudios/nuevo";
		}

		redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
				"empleadoEstudios.actualizado.message");
		redirectAttributes.addFlashAttribute(
				Constantes.CONTAINSKEY_MESSAGE_ATTRS,
				new String[] { empleadoEstudios.getNombreEstudios() });

		return "redirect:" + "/rh/empleadoEstudios/ver" + "/" + empleadoEstudios.getId();
	}

	@Transactional
	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute EmpleadoEstudios empleadoEstudios,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina empleadoEstudios");
		try {
			mgr.elimina(id);

			redirectAttributes
					.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE,
							"empleadoEstudios.eliminado.message");
			redirectAttributes.addFlashAttribute(
					Constantes.CONTAINSKEY_MESSAGE_ATTRS,
					new String[] { empleadoEstudios.getNombreEstudios() });
		} catch (Exception e) {
			log.error("No se pudo eliminar empleadoEstudios " + id, e);
			bindingResult
					.addError(new ObjectError("empleadoEstudios",
							new String[] { "empleadoEstudios.no.eliminado.message" },
							null, null));
			return "/rh/empleadoEstudios/ver";
		}

		return "redirect:" + "/rh/empleadoEstudios/ver";
	}
    
}
