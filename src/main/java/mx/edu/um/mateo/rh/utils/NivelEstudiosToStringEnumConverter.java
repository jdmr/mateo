/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.utils;

import mx.edu.um.mateo.rh.model.NivelEstudios;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author zorch
 */
public class NivelEstudiosToStringEnumConverter implements Converter<NivelEstudios, String>{
    /**
     *
     * @param nivelEstudios
     * @return
     */
    @Override
    public String convert(NivelEstudios nivelEstudios){
        if (nivelEstudios==null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(NivelEstudios.class), 
				TypeDescriptor.valueOf(String.class), nivelEstudios, null);
        }
        return nivelEstudios.toString();
    }
    
}
