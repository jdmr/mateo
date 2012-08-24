/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
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
package mx.edu.um.mateo.inventario.tasks;

import java.util.Date;

import mx.edu.um.mateo.inventario.dao.ProductoDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Component
public class ProductoTask {

	private static final Logger log = LoggerFactory
			.getLogger(ProductoTask.class);
	@Autowired
	private ProductoDao productoDao;

	public ProductoTask() {
		log.info("Se ha creado una nueva instancia del cron de productos");
	}

	@Scheduled(cron = "0 0 0 * * *")
	public void buscaHistorial() {
		Date fecha = new Date();
		log.debug("Buscando historial {}", fecha);
		// productoDao.guardaHistorial(fecha);
		log.debug("TERMINO DE BUSCAR EL HISTORIAL {}", new Date());
	}

}
