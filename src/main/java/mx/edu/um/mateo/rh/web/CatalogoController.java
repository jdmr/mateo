/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author develop
 */
@Controller
@RequestMapping("/rh/catalogo")
public class CatalogoController {

    private static final Logger log = LoggerFactory
            .getLogger(CatalogoController.class);

    @RequestMapping
    public String index() {
        log.debug("Mostrando indice de catalogo controller");
        return "rh/catalogo/index";
    }
}
