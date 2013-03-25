package mx.edu.um.mateo.inscripciones.model.ccobro.comedor;

import mx.edu.um.mateo.inscripciones.model.ccobro.utils.Constants;

/**
 * @author mcdiel
 *
 */
public enum ComidaType {
	DESAYUNO(1, Constants.ENTRADACOMEDOR_TIPO_COMIDA_DESAYUNO),
	COMIDA(2, Constants.ENTRADACOMEDOR_TIPO_COMIDA_COMIDA),
	CENA(3, Constants.ENTRADACOMEDOR_TIPO_COMIDA_CENA),
	UNKNOWN(0, "Error");
	
	private Integer id;
	private String comida;
	
	private ComidaType(Integer id, String comida) {
		this.id = id;
		this.comida = comida;
	}
	
	/**
	 * @return the comida
	 */
	public String getComida() {
		return comida;
	}
	
	public Integer getId() {
		return id;
	}

	public static ComidaType valueOf(Integer valor) {
		switch(valor) {
		case 1 : return DESAYUNO;
		case 2 : return COMIDA;
		case 3 : return CENA;
		default : return UNKNOWN;
		}
	}
}
