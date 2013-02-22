/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

/**
 *
 * @author osoto
 */
public class NivelEstudiosToStringConverterFactory implements ConverterFactory<NivelEstudios, String>{
 
	//@SuppressWarnings("unchecked")
	@Override
	public <T extends String> Converter<NivelEstudios, T> getConverter(Class<T> arg0) {		
		return (Converter<NivelEstudios, T>)new NivelEstudiosToStringConverter();
	}
 
}