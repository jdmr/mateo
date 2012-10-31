/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.utils;

import mx.edu.um.mateo.rh.model.TipoDependiente;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;


/**
 *
 * @author zorch
 */
public class TipoDependienteToStringEnumConverter implements Converter<TipoDependiente, String>{
    /**
     *
     * @param tipoDependiente
     * @return
     */
    @Override
    public String convert(TipoDependiente tipoDependiente){
        if (tipoDependiente==null){
            throw new ConversionFailedException(TypeDescriptor.valueOf(TipoDependiente.class), 
				TypeDescriptor.valueOf(String.class), tipoDependiente, null);
        }
        return tipoDependiente.toString();
    }
}
