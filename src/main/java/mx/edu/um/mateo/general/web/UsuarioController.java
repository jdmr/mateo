/*
 * The MIT License
 *
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
package mx.edu.um.mateo.general.web;

import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.utils.SpringSecurityUtils;
import mx.edu.um.mateo.general.utils.UltimoException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.keygen.KeyGenerators;
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
 * @author jdmr
 */
@Controller
@RequestMapping("/admin/usuario")
public class UsuarioController extends BaseController {

	@Autowired
	private UsuarioDao usuarioDao;
	@Autowired
	private SpringSecurityUtils springSecurityUtils;

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
		log.debug("Mostrando lista de usuarios");
		Map<String, Object> params = this.convierteParams(request
				.getParameterMap());
		Long empresaId = (Long) request.getSession().getAttribute("empresaId");
		params.put("empresa", empresaId);

		if (StringUtils.isNotBlank(tipo)) {
			params.put("reporte", true);
			params = usuarioDao.lista(params);
			try {
				generaReporte(tipo, (List<Usuario>) params.get("usuarios"),
						response, "usuarios", Constantes.EMP, empresaId);
				return null;
			} catch (ReporteException e) {
				log.error("No se pudo generar el reporte", e);
			}
		}

		if (StringUtils.isNotBlank(correo)) {
			params.put("reporte", true);
			params = usuarioDao.lista(params);

			params.remove("reporte");
			try {
				enviaCorreo(correo, (List<Usuario>) params.get("usuarios"),
						request, "usuarios", Constantes.EMP, empresaId);
				modelo.addAttribute("message", "lista.enviada.message");
				modelo.addAttribute(
						"messageAttrs",
						new String[] {
								messageSource.getMessage("usuario.lista.label",
										null, request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}
		params = usuarioDao.lista(params);
		modelo.addAttribute("usuarios", params.get("usuarios"));

		this.pagina(params, modelo, "usuarios", pagina);

		return "admin/usuario/lista";
	}

	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando usuario {}", id);
		Usuario usuario = usuarioDao.obtiene(id);
		List<Rol> roles = usuarioDao.roles();

		modelo.addAttribute("usuario", usuario);
		modelo.addAttribute("roles", roles);

		return "admin/usuario/ver";
	}

	@RequestMapping("/nuevo")
	public String nuevo(Model modelo) {
		log.debug("Nuevo usuario");
		List<Rol> roles = obtieneRoles();
		Usuario usuario = new Usuario();
		modelo.addAttribute("usuario", usuario);
		modelo.addAttribute("roles", roles);
		List<Ejercicio> ejercicios = usuarioDao.obtieneEjercicios(ambiente
				.obtieneUsuario().getEmpresa().getOrganizacion().getId());
		modelo.addAttribute("ejercicios", ejercicios);
		return "admin/usuario/nuevo";
	}

	@Transactional
	@RequestMapping(value = "/crea", method = RequestMethod.POST)
	public String crea(HttpServletRequest request,
			HttpServletResponse response, @Valid Usuario usuario,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");
			List<Rol> roles = obtieneRoles();
			modelo.addAttribute("roles", roles);
			return "admin/usuario/nuevo";
		}

		String password = null;
		try {
			log.debug("Evaluando roles {}", request.getParameterValues("roles"));
			String[] roles = request.getParameterValues("roles");
			if (roles == null || roles.length == 0) {
				log.debug("Asignando ROLE_USER por defecto");
				roles = new String[] { "ROLE_USER" };
			}
			Long almacenId = (Long) request.getSession().getAttribute(
					"almacenId");
			password = KeyGenerators.string().generateKey();
			usuario.setPassword(password);
			usuario = usuarioDao.crea(usuario, almacenId, roles);

			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(usuario.getCorreo());
			helper.setSubject(messageSource.getMessage(
					"envia.correo.password.titulo.message", new String[] {},
					request.getLocale()));
			helper.setText(messageSource.getMessage(
					"envia.correo.password.contenido.message", new String[] {
							usuario.getNombre(), usuario.getUsername(),
							password }, request.getLocale()), true);
			mailSender.send(message);

		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear al usuario", e);
			errors.rejectValue("username", "campo.duplicado.message",
					new String[] { "username" }, null);
			List<Rol> roles = obtieneRoles();
			modelo.addAttribute("roles", roles);
			return "admin/usuario/nuevo";
		} catch (MessagingException e) {
			log.error("No se pudo enviar la contrasena por correo", e);

			redirectAttributes.addFlashAttribute("message",
					"usuario.creado.sin.correo.message");
			redirectAttributes.addFlashAttribute("messageAttrs", new String[] {
					usuario.getUsername(), password });

			return "redirect:/admin/usuario/ver/" + usuario.getId();
		}

		redirectAttributes.addFlashAttribute("message",
				"usuario.creado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { usuario.getUsername() });

		return "redirect:/admin/usuario/ver/" + usuario.getId();
	}

	@RequestMapping("/edita/{id}")
	public String edita(@PathVariable Long id, Model modelo) {
		log.debug("Edita usuario {}", id);
		List<Rol> roles = obtieneRoles();
		Usuario usuario = usuarioDao.obtiene(id);
		modelo.addAttribute("usuario", usuario);
		modelo.addAttribute("roles", roles);
		return "admin/usuario/edita";
	}

	@Transactional
	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
	public String actualiza(HttpServletRequest request, @Valid Usuario usuario,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.error("Hubo algun error en la forma, regresando");
			List<Rol> roles = obtieneRoles();
			modelo.addAttribute("roles", roles);
			return "admin/usuario/edita";
		}

		try {
			String[] roles = request.getParameterValues("roles");
			if (roles == null || roles.length == 0) {
				roles = new String[] { "ROLE_USER" };
			}
			Long almacenId = (Long) request.getSession().getAttribute(
					"almacenId");
			usuario = usuarioDao.actualiza(usuario, almacenId, roles);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear al usuario", e);
			errors.rejectValue("username", "campo.duplicado.message",
					new String[] { "username" }, null);
			List<Rol> roles = obtieneRoles();
			modelo.addAttribute("roles", roles);
			return "admin/usuario/edita";
		}

		redirectAttributes.addFlashAttribute("message",
				"usuario.actualizado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { usuario.getUsername() });

		return "redirect:/admin/usuario/ver/" + usuario.getId();
	}

	@Transactional
	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(@RequestParam Long id, Model modelo,
			@ModelAttribute Usuario usuario, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		log.debug("Elimina usuario");
		try {
			String nombre = usuarioDao.elimina(id);
			redirectAttributes.addFlashAttribute("message",
					"usuario.eliminado.message");
			redirectAttributes.addFlashAttribute("messageAttrs",
					new String[] { nombre });
		} catch (UltimoException e) {
			log.error("No se pudo eliminar el usuario " + id, e);
			bindingResult.addError(new ObjectError("usuario",
					new String[] { "ultimo.usuario.no.eliminado.message" },
					null, null));
			List<Rol> roles = usuarioDao.roles();
			modelo.addAttribute("roles", roles);
			return "admin/usuario/ver";
		} catch (Exception e) {
			log.error("No se pudo eliminar el usuario " + id, e);
			bindingResult
					.addError(new ObjectError("usuario",
							new String[] { "usuario.no.eliminado.message" },
							null, null));
			List<Rol> roles = usuarioDao.roles();
			modelo.addAttribute("roles", roles);
			return "admin/usuario/ver";
		}

		return "redirect:/admin/usuario";
	}

	private List<Rol> obtieneRoles() {
		List<Rol> roles = usuarioDao.roles();
		if (springSecurityUtils.ifAnyGranted("ROLE_ADMIN")) {
			// no se hace nada
		} else if (springSecurityUtils.ifAnyGranted("ROLE_ORG")) {
			roles.remove(new Rol("ROLE_ADMIN"));
			roles.remove(new Rol("ROLE_ORG"));
		} else {
			roles.remove(new Rol("ROLE_ADMIN"));
			roles.remove(new Rol("ROLE_ORG"));
			roles.remove(new Rol("ROLE_EMP"));
		}

		return roles;
	}

}
