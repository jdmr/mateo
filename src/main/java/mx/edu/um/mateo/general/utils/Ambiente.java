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
package mx.edu.um.mateo.general.utils;

import javax.servlet.http.HttpSession;
import mx.edu.um.mateo.general.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Component
public class Ambiente {

    private static final Logger log = LoggerFactory.getLogger(Ambiente.class);

    public void actualizaSesion(HttpSession session) {
        Usuario usuario = obtieneUsuario();
        this.actualizaSesion(session, usuario);
    }

    public void actualizaSesion(HttpSession session, Usuario usuario) {
        log.debug("Actualizando sesion");
        if (usuario != null) {
            if (usuario.getEjercicio() != null) {
                session.setAttribute("ejercicioLabel", usuario.getEjercicio().getId().getIdEjercicio());
            }
            session.setAttribute("organizacionLabel", usuario.getEmpresa().getOrganizacion().getNombre());
            session.setAttribute("empresaLabel", usuario.getEmpresa().getNombre());
            session.setAttribute("almacenLabel", usuario.getAlmacen().getNombre());
            session.setAttribute("organizacionId", usuario.getEmpresa().getOrganizacion().getId());
            session.setAttribute("empresaId", usuario.getEmpresa().getId());
            session.setAttribute("almacenId", usuario.getAlmacen().getId());
            session.setAttribute("ejercicioId", usuario.getEjercicio().getId().getIdEjercicio());
        }
    }

    public Usuario obtieneUsuario() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuario;
    }
}
