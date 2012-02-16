/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author AMDA
 */
@Controller
@RequestMapping("/rh")
public class RhController {

    private static final Logger log = LoggerFactory.getLogger(mx.edu.um.mateo.general.web.RhController.class);

    @RequestMapping
    public String index() {
        log.debug("Mostrando indice de rh");
        return "rh/index";
    }
}
