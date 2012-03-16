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
package mx.edu.um.mateo.general.web;

import mx.edu.um.mateo.general.dao.ReporteDao;
import mx.edu.um.mateo.general.utils.Ambiente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/reporte")
public class ReporteController {

    private static final Logger log = LoggerFactory.getLogger(ReporteController.class);
    @Autowired
    private ReporteDao reporteDao;
    @Autowired
    private Ambiente ambiente;

    @RequestMapping("/compila/{tipo}/{reporte}")
    public String compila(@PathVariable String tipo, @PathVariable String reporte, RedirectAttributes redirectAttributes) {
        log.debug("Compilando reporte {}", reporte);
        reporteDao.compila(reporte, tipo, ambiente.obtieneUsuario());
        
        redirectAttributes.addFlashAttribute("message", "reporte.compilado.message");
        redirectAttributes.addFlashAttribute("messageAttrs",new String[]{reporte});
        
        return "redirect:/";
    }
}
