/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
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
package mx.edu.um.mateo.colportor.web;

import javax.servlet.http.HttpServletRequest;
import mx.edu.um.mateo.colportor.dao.TraspasoDao;
import mx.edu.um.mateo.general.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author gibrandemetrioo
 */
@Controller
@RequestMapping("/colportaje/traspaso")
public class TraspasoController extends BaseController {

    @Autowired
    private TraspasoDao dao;
    
    public TraspasoController() {
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = {"/traspasoClp"}, method = RequestMethod.GET)
    public String traspasoClp(HttpServletRequest request) {
        log.debug("Traspaso Colportores");
        try {
            dao.traspasaColportores();
        } catch (Exception e) {
            log.error("Error al intentar traspasar los colportores ", e);
            return null;
        }
        return null;
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = {"/traspasoTmpClp"}, method = RequestMethod.GET)
    public String traspasoTmpClp(HttpServletRequest request) {
        log.debug("Traspaso Temporadas Colportores");
        try {
            dao.traspasaTemporadasColportor();
        } catch (Exception e) {
            log.error("Error al intentar traspasar las temporadas de colportores ", e);
            return null;
        }
        return null;
    }
    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = {"/traspasoDocClp"}, method = RequestMethod.GET)
    public String traspasoDocClp(HttpServletRequest request) {
        log.debug("Traspaso Documentos Colportores");
        try {
            dao.traspasaDocumentos();
        } catch (Exception e) {
            log.error("Error al intentar traspasar los documentos de colportores ", e);
            return null;
        }
        return null;
    }
}
