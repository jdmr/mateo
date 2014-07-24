/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web;

import mx.edu.um.mateo.contabilidad.facturas.dao.PasarProveedoresFacturasDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.CorreoMalFormadoException;
import mx.edu.um.mateo.general.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String index(RedirectAttributes redirectAttributes) {
        log.debug("Entrando a pase de Proveedores");
        Usuario usuario = ambiente.obtieneUsuario();
        log.debug("Enviando a dao");
        try {
            dao.pasar(usuario);
        } catch (CorreoMalFormadoException ex) {

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "error.actualizar.proveedorFactura");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{ex.getMessage()});
            return "factura/index";
        }
        log.debug("Pase exitoso");

        return "factura/index";
    }
}
