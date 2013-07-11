/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.utils;

import java.util.List;
import mx.edu.um.mateo.colportor.web.ColportorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

/**
 *
 * @author osoto
 */
@Component
public class UtilControllerTests {
    protected final transient Logger log = LoggerFactory.getLogger(getClass());
    
    public void despliegaBindingResultErrors(BindingResult bindingResult){
        List <ObjectError> errores = bindingResult.getAllErrors();
            for(ObjectError err : errores){
                log.error("{}",err);
                
            }
    }
    
}
