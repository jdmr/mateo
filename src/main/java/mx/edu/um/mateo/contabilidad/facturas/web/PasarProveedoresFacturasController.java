/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web;

import mx.edu.um.mateo.contabilidad.facturas.dao.PasarProveedoresFacturasDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author develop
 */
@Controller
@RequestMapping("/factura/pasarProveedor")
public class PasarProveedoresFacturasController extends BaseController {

    @Autowired
    private PasarProveedoresFacturasDao dao;

    @RequestMapping
    public String index() {
        log.debug("Entrando a pase de Proveedores");
        Usuario usuario = ambiente.obtieneUsuario();
        log.debug("Enviando a dao");
        dao.pasar(usuario);
        log.debug("Pase exitoso");

        return "factura/index";
    }
}
