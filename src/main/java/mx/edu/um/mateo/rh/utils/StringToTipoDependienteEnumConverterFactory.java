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
public class StringToTipoDependienteEnumConverterFactory implements ConverterFactory<String,TipoDependiente>{
        
        @SuppressWarnings("unchecked")
	@Override
	public <T extends TipoDependiente> Converter<String, T> getConverter(Class<T> arg0) {
		return (Converter<String, T>) new StringToTipoDependienteEnumConverter();
	}
}
