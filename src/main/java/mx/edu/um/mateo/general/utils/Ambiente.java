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

import javax.servlet.http.HttpServletRequest;
import mx.edu.um.mateo.general.model.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Component
public class Ambiente {

    public void actualizaSesion(HttpServletRequest request) {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.actualizaSesion(request, usuario);
    }

    public void actualizaSesion(HttpServletRequest request, Usuario usuario) {
        if (usuario != null) {
            request.getSession().setAttribute("organizacionLabel", usuario.getEmpresa().getOrganizacion().getNombre());
            request.getSession().setAttribute("empresaLabel", usuario.getEmpresa().getNombre());
            request.getSession().setAttribute("almacenLabel", usuario.getAlmacen().getNombre());
            request.getSession().setAttribute("organizacionId", usuario.getEmpresa().getOrganizacion().getId());
            request.getSession().setAttribute("empresaId", usuario.getEmpresa().getId());
            request.getSession().setAttribute("almacenId", usuario.getAlmacen().getId());
        }
    }

    public Usuario obtieneUsuario() {
        Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return usuario;
    }
}
