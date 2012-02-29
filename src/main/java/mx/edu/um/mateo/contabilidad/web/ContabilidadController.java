/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author nujev
 */
@Controller
public class ContabilidadController {

    private static final Logger log = LoggerFactory.getLogger(ContabilidadController.class);

    @RequestMapping({"/contabilidad"})
    public String index() {
        log.debug("Mostrando menu de contabilidad");
        return "contabilidad/index";
    }
}
