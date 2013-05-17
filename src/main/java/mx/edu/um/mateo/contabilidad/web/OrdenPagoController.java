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
package mx.edu.um.mateo.contabilidad.web;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.contabilidad.model.OrdenPago;
import mx.edu.um.mateo.contabilidad.service.OrdenPagoManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
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
 * @author osoto
 */
@Controller
@RequestMapping("/contabilidad/ordenPago")
public class OrdenPagoController extends BaseController {

	@Autowired
	private OrdenPagoManager mgr;

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
		log.debug("Mostrando lista de ordenes de pago");
		Map<String, Object> params = this.convierteParams(request
				.getParameterMap());
		Long empresaId = (Long) request.getSession().getAttribute("empresaId");
		params.put("empresa", empresaId);

		if (StringUtils.isNotBlank(tipo)) {
			params.put("reporte", true);
			params = mgr.lista(params);
			try {
				generaReporte(tipo, (List<OrdenPago>) params.get(Constantes.ORDENPAGO_LIST),
						response, Constantes.ORDENPAGO_LIST, Constantes.ORG, empresaId);
				return null;
			} catch (ReporteException e) {
				log.error("No se pudo generar el reporte", e);
			}
		}

		if (StringUtils.isNotBlank(correo)) {
			params.put("reporte", true);
			params = mgr.lista(params);

			params.remove("reporte");
			try {
				enviaCorreo(correo, (List<OrdenPago>) params.get(Constantes.ORDENPAGO_LIST),
						request, Constantes.ORDENPAGO_LIST, Constantes.ORG, empresaId);
				modelo.addAttribute("message", "lista.enviada.message");
				modelo.addAttribute(
						"messageAttrs",
						new String[] {
								messageSource.getMessage(
										"ordenPago.lista.label", null,
										request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}
		params = mgr.lista(params);
		modelo.addAttribute(Constantes.ORDENPAGO_LIST, params.get(Constantes.ORDENPAGO_LIST));

		this.pagina(params, modelo, Constantes.ORDENPAGO_LIST, pagina);

		return Constantes.ORDENPAGO_PATH_LISTA;
	}

	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando ordenPago {}", id);
		OrdenPago ordenPago = mgr.obtiene(id);

		modelo.addAttribute("ordenPago", ordenPago);

		return Constantes.ORDENPAGO_PATH_VER;
	}

	@RequestMapping("/nueva")
	public String nueva(Model modelo) {
		log.debug("Nueva ordenPago");
		OrdenPago ordenPago = new OrdenPago();
		modelo.addAttribute("ordenPago", ordenPago);
		return Constantes.ORDENPAGO_PATH_NUEVO;
	}

	@RequestMapping(value = "/graba", method = RequestMethod.POST)
	public String graba(HttpServletRequest request,
			HttpServletResponse response, @Valid OrdenPago ordenPago,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");
			this.despliegaBindingResultErrors(bindingResult);
			return Constantes.ORDENPAGO_PATH_NUEVO;
		}

                Boolean isNew = true;
                if(ordenPago.getId() != null){
                    isNew = false;
                }
                
		try {
			Usuario usuario = ambiente.obtieneUsuario();
			mgr.graba(ordenPago, usuario);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear al ordenPago", e);
			errors.rejectValue("id", "campo.duplicado.message",
					new String[] { "id" }, null);
			errors.rejectValue("descripcion", "campo.duplicado.message",
					new String[] { "descripcion" }, null);
			return Constantes.ORDENPAGO_PATH_NUEVO;
		}

                redirectAttributes.addFlashAttribute("message",
				"ordenPago.graba.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { ordenPago.getDescripcion() });
                
                

		return "redirect:"+Constantes.ORDENPAGO_PATH;
	}
        
	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
	public String actualiza(HttpServletRequest request,
			HttpServletResponse response, @Valid OrdenPago ordenPago,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");
			this.despliegaBindingResultErrors(bindingResult);
			return Constantes.ORDENPAGO_PATH_NUEVO;
		}

                Boolean isNew = true;
                if(ordenPago.getId() != null){
                    isNew = false;
                }
                
		try {
			Usuario usuario = ambiente.obtieneUsuario();
			mgr.actualiza(ordenPago, usuario);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear al ordenPago", e);
			errors.rejectValue("id", "campo.duplicado.message",
					new String[] { "id" }, null);
			errors.rejectValue("descripcion", "campo.duplicado.message",
					new String[] { "descripcion" }, null);
			return Constantes.ORDENPAGO_PATH_NUEVO;
		}

                
                    redirectAttributes.addFlashAttribute("message",
				"ordenPago.actualiza.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { ordenPago.getDescripcion() });
                
		return "redirect:"+Constantes.ORDENPAGO_PATH;
	}

	@RequestMapping("/edita/{id}")
	public String edita(@PathVariable Long id, Model modelo) {
		log.debug("Edita ordenPago {}", id);
		OrdenPago ordenPago = mgr.obtiene(id);
		modelo.addAttribute("ordenPago", ordenPago);
		return Constantes.ORDENPAGO_PATH_EDITA;
	}

	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute OrdenPago ordenPago,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina ordenPago");
		try {
			String nombre = mgr.elimina(id);

			redirectAttributes.addFlashAttribute("message",
					"ordenPago.elimina.message");
			redirectAttributes.addFlashAttribute("messageAttrs",
					new String[] { nombre });
		} catch (Exception e) {
			log.error("No se pudo eliminar la orden de pago " + id, e);
			bindingResult.addError(new ObjectError("ordenPago",
					new String[] { "ordenPago.no.elimina.message" }, null,
					null));
			return Constantes.ORDENPAGO_PATH_VER + id;
		}

		return "redirect:"+Constantes.ORDENPAGO_PATH;
	}

}

