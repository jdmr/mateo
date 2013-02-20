/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author develop
 */
@Controller
@RequestMapping("/inscripciones")
public class InscripcionesController {
    private static final Logger log = LoggerFactory
			.getLogger(InscripcionesController.class);

	@RequestMapping
	public String index() {
		log.debug("Mostrando indice de rh");
		return "inscripciones/index";
	}
    
}
