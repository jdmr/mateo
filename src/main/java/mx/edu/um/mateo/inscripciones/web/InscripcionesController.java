package mx.edu.um.mateo.inscripciones.web;

import mx.edu.um.mateo.general.web.AdminController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author semdariobarbaamaya
 */
@Controller
@RequestMapping("/inscripciones")
public class InscripcionesController {
    
    private static final Logger log = LoggerFactory
			.getLogger(AdminController.class);

	@RequestMapping
	public String index() {
		log.debug("Mostrando indice de inscripciones");
		return "inscripciones/index";
	}
}
