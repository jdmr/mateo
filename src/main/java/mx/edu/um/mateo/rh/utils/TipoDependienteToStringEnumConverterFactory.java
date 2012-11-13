/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.utils;

import mx.edu.um.mateo.rh.model.TipoDependiente;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 *
 * @author zorch
 */
public class TipoDependienteToStringEnumConverterFactory implements ConverterFactory<TipoDependiente, String>{
    
        @SuppressWarnings("unchecked")
	@Override
	public <T extends String>Converter<TipoDependiente, T> getConverter(Class<T> arg0) {		
		return (Converter<TipoDependiente, T>)new TipoDependienteToStringEnumConverter();
	}

}
