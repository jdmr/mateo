/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web;

import mx.edu.um.mateo.inscripciones.web.*;
import mx.edu.um.mateo.general.web.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author semdariobarbaamaya
 *
 */
@Controller
@RequestMapping("/factura")
public class FacturasController extends BaseController {

    private static final Logger log = LoggerFactory
            .getLogger(FacturasController.class);

    @RequestMapping
    public String index() {
        log.debug("Mostrando indice de facturas");
        return "factura/index";
    }
}
