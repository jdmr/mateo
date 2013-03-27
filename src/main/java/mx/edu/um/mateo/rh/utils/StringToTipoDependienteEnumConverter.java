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
public class StringToTipoDependienteEnumConverter implements Converter<String,TipoDependiente>{
      @Override
    public TipoDependiente convert(String tipoDependienteAsString) {

        if (tipoDependienteAsString == null) {
            throw new ConversionFailedException(TypeDescriptor.valueOf(String.class),
                    TypeDescriptor.valueOf(String.class),tipoDependienteAsString, null);
        }

        return TipoDependiente.valueOf(tipoDependienteAsString);
    }
}
