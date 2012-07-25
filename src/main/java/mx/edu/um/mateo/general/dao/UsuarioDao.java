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
package mx.edu.um.mateo.general.dao;

import java.util.*;
import mx.edu.um.mateo.contabilidad.model.Ejercicio;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.UltimoException;
import mx.edu.um.mateo.inventario.model.Almacen;

/**
 *
 * @author jdmr
 */
public interface UsuarioDao {

    public Map<String, Object> lista(Map<String, Object> params);

    public Usuario obtiene(Long id);

    public Usuario obtiene(String username);

    public Usuario obtienePorOpenId(String openId);

    public Usuario obtienePorCorreo(String correo);

    public Usuario crea(Usuario usuario, Long almacenId, String[] nombreDeRoles);

    public Usuario actualiza(Usuario usuario, Long almacenId, String[] nombreDeRoles);

    public void actualiza(Usuario usuario);

    public String elimina(Long id) throws UltimoException;

    public List<Rol> roles();

    public List<Almacen> obtieneAlmacenes();

    public void asignaAlmacen(Usuario usuario, Long almacenId, String ejercicioId);

    public List<Ejercicio> obtieneEjercicios(Long organizacionId);
}
