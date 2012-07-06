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
package mx.edu.um.mateo.general.web;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.um.mateo.general.model.Imagen;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/imagen")
public class ImagenController {

    private static final Logger log = LoggerFactory.getLogger(ImagenController.class);
    @Autowired
    private SessionFactory sessionFactory;

    @RequestMapping("/mostrar/{id}")
    public String mostrar(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) {
        log.debug("Mostrando imagen {}",id);
        Imagen imagen = (Imagen)currentSession().get(Imagen.class, id);
        if (imagen != null) {
            response.setContentType(imagen.getTipoContenido());
            response.setContentLength(imagen.getTamano().intValue());
            try {
                response.getOutputStream().write(imagen.getArchivo());
            } catch (IOException e) {
                log.error("No se pudo escribir el archivo",e);
                throw new RuntimeException("No se pudo escribir el archivo en el outputstream");
            }
        } else {
            try {
                response.sendRedirect(request.getContextPath()+"/images/sin-foto.jpg");
            } catch(IOException e) {
                log.error("No se pudo obtener la imagen", e);
            }
        }
        return null;
    }
    
    @RequestMapping("/producto/{id}")
    public String producto(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) {
        log.debug("Buscando imagen del producto {}", id);
        Query query = currentSession().createQuery("select imagen from Producto p left outer join p.imagenes as imagen where p.id = :productoId");
        query.setLong("productoId", id);
        Imagen imagen = (Imagen) query.uniqueResult();
        if (imagen != null) {
            response.setContentType(imagen.getTipoContenido());
            response.setContentLength(imagen.getTamano().intValue());
            try {
                response.getOutputStream().write(imagen.getArchivo());
            } catch (IOException e) {
                log.error("No se pudo escribir el archivo",e);
                throw new RuntimeException("No se pudo escribir el archivo en el outputstream");
            }
        } else {
            try {
                response.sendRedirect(request.getContextPath()+"/images/sin-foto.jpg");
            } catch(IOException e) {
                log.error("No se pudo obtener la imagen", e);
            }
        }
        return null;
    }
    
    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }
}
