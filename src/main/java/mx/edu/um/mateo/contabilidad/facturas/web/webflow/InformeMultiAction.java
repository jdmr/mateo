/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.web.webflow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 *
 * @author develop
 */
public class InformeMultiAction extends MultiAction {

    protected final transient Logger log = LoggerFactory.getLogger(getClass());
    
    public Event grabarInforme(RequestContext context) {
        log.debug("Entro a grabar informe");
        return success();
    }

}
