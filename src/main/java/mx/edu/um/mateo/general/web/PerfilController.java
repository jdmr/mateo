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

import java.util.List;

import javax.servlet.http.HttpSession;

import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.inventario.model.Almacen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/perfil")
public class PerfilController {

	private static final Logger log = LoggerFactory
			.getLogger(AdminController.class);
	@Autowired
	private Ambiente ambiente;
	@Autowired
	private UsuarioDao usuarioDao;

	@RequestMapping
	public String edita(Model modelo) {
		log.debug("Mostrando perfil");
		Usuario usuario = ambiente.obtieneUsuario();
		List<Almacen> almacenes = usuarioDao.obtieneAlmacenes();
		List<Ejercicio> ejercicios = usuarioDao.obtieneEjercicios(usuario
				.getEmpresa().getOrganizacion().getId());
		modelo.addAttribute("usuario", usuario);
		modelo.addAttribute("almacenes", almacenes);
		modelo.addAttribute("ejercicios", ejercicios);
		return "perfil/edita";
	}
	

	@Transactional
	@RequestMapping(value = "/guarda", method = RequestMethod.POST)
	public String guarda(HttpSession session,
			RedirectAttributes redirectAttributes,
			@RequestParam("almacen.id") Long almacenId,
			@RequestParam String password,
			@RequestParam("ejercicio.id.idEjercicio") String ejercicioId) {
		log.debug("Guardando perfil");
		Usuario usuario = ambiente.obtieneUsuario();
		usuario.setPassword(password);
		usuarioDao.asignaAlmacen(usuario, almacenId, ejercicioId);
		ambiente.actualizaSesion(session);

		redirectAttributes.addFlashAttribute("message",
				"perfil.actualizado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { usuario.getUsername() });

		return "redirect:/";
	}
        
        @PreAuthorize("hasRole('ROLE_ADMIN')")
        @RequestMapping("editaPasswd")
	public String editaPasswd(Model modelo) {
		log.debug("Mostrando perfil");
		Usuario usuario = ambiente.obtieneUsuario();
		List<Almacen> almacenes = usuarioDao.obtieneAlmacenes();
		List<Ejercicio> ejercicios = usuarioDao.obtieneEjercicios(usuario
				.getEmpresa().getOrganizacion().getId());
		modelo.addAttribute("usuario", usuario);
		modelo.addAttribute("almacenes", almacenes);
		modelo.addAttribute("ejercicios", ejercicios);
		return "perfil/editaPasswd";
	}
        
        @Transactional
        @PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/guardaPasswd", method = RequestMethod.POST)
	public String guardaPasswd(HttpSession session,
			RedirectAttributes redirectAttributes,
			@RequestParam("username") String username,
			@RequestParam("almacen.id") Long almacenId,
			@RequestParam String password,
			@RequestParam("ejercicio.id.idEjercicio") String ejercicioId) {
		log.debug("Modificando password de '{}'", username);
		Usuario usuario = usuarioDao.obtiene(username);
                log.debug("Se obtuvo el usuario {}", usuario);
		usuario.setPassword(password);
		usuarioDao.asignaAlmacen(usuario, almacenId, ejercicioId);
		ambiente.actualizaSesion(session);

		redirectAttributes.addFlashAttribute("message",
				"perfil.actualizado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { usuario.getUsername() });

		return "redirect:/";
	}
}
