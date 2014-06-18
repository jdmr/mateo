/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.rh.dao.TraspasoDatosEmpleadoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author develop
 */
@Controller
@RequestMapping(Constantes.PATH_TRASPASDODATOSEMPLEADO)
public class TraspasoDatosEmpleadoController extends BaseController {

    @Autowired
    private TraspasoDatosEmpleadoDao dao;

    @RequestMapping
    public String index() {
        log.debug("Entrando a pase de empleados");
        Usuario usuario = ambiente.obtieneUsuario();
        log.debug("Enviando a dao");
        dao.traspaso(usuario);
        log.debug("Pase exitoso");

        return "rh/empleado";
    }

}
