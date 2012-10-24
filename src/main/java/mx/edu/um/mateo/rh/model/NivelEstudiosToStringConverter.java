/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.Converter;

/**
 *
 * @author osoto
 */
public class NivelEstudiosToStringConverter implements Converter<NivelEstudios, String>{
 
	@Override
	public String convert(NivelEstudios nivelEstudios) {
 
		if (nivelEstudios == null){
			throw new ConversionFailedException(TypeDescriptor.valueOf(NivelEstudios.class), 
				TypeDescriptor.valueOf(String.class), nivelEstudios, null);
		}
 
		return nivelEstudios.toString();
	}
 
}
