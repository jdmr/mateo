/*
 * Created on Dec 23, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package mx.edu.um.mateo.inscripciones.model.ccobro.tFinanciera;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import mx.edu.um.mateo.inscripciones.model.ccobro.academico.Carga;
import mx.edu.um.mateo.inscripciones.model.ccobro.academico.Clasificacion;

/**
 * @author osoto
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TFinanciera {
	private TFinancieraEnc encabezado;
	private Map detalles;
	/**
	 * 
	 */
	public TFinanciera() {
		super();
				
		// TODO Auto-generated constructor stub
	}
	public TFinanciera(TFinancieraEnc encabezado, Map detalles){
		this.encabezado = encabezado;
		this.detalles = detalles;
	}
	public TFinanciera getTFinanciera(Carga carga) throws Exception {
		Map mCargas = new Carga().getCargas();
		
		//Verificar si existe la carga
		if(!mCargas.containsKey(carga.getCargaId())){
			throw new Error("La carga "+carga.getCargaId()+" no es valida!");
		}
		
		this.encabezado = new TFinancieraEnc().getEncabezado(carga);
		
		//Verificar si existe una tabla financiera de dicha carga
		if(this.encabezado == null){
			throw new Error("No existe ninguna tabla financiera para la carga "+carga.getCargaId());
		}
		
		this.detalles = new TFinancieraDet().getDetalles(this.encabezado);
		return this;
	}
	public void setTFinanciera(TFinancieraEnc encabezado, Map mDetalles) throws Exception{
		this.encabezado = encabezado;
		this.detalles = mDetalles;
	}
	public void grabaTFinanciera() throws Exception{
		encabezado.setEncabezado(this.encabezado);
		new TFinancieraDet().setDetalles(this.encabezado, this.detalles);				
	}
	public TFinancieraEnc getEncabezado() throws Exception{
		return this.encabezado;
	}
	public TFinancieraDet getDetalle(String key) throws Exception{
		
		if(!this.detalles.containsKey(key))
			return null;
		return (TFinancieraDet)this.detalles.get(key);		
	}
	public Map getDetalles() throws Exception{
		return this.detalles;
	}
	public Map getTFinanciera() throws Exception{
		Map mTFinanciera = new TreeMap();
		
		Map mEncabezados = new TFinancieraEnc().getEncabezados();
		Iterator iEncabezados = mEncabezados.keySet().iterator();
		
		while(iEncabezados.hasNext()){
			TFinancieraEnc encabezado = (TFinancieraEnc)mEncabezados.get(iEncabezados.next());
			
			Map detalles = new TFinancieraDet().getDetalles(encabezado);
			
			mTFinanciera.put(encabezado.getCarga().getCargaId(),new TFinanciera(encabezado, detalles));
		}
		
		return mTFinanciera;
	}
	public TFinanciera getTFinanciera(String cargaId) throws Exception{
				
		TFinancieraEnc encabezado = new TFinancieraEnc().getEncabezado(new Carga(cargaId));
		if(encabezado == null)
			throw new Error("No existe tabla financiera para la carga academcia "+cargaId);
		
		Map detalles = new TFinancieraDet().getDetalles(encabezado);		
		
		return new TFinanciera(encabezado, detalles);
	}
	public Double getCCredito(String carreraId, Integer modo, String clasificacion) throws Exception {
		Double cCredito = new Double(0);
		Double pctCCredito = new Double(0);
		Double total = new Double(0);
		String paso = "1";
		try{
			cCredito = ((Clasificacion)this.encabezado.getClasificacion().get(new Integer(clasificacion))).getCCredito();
			paso = "2";
			TFinancieraDet detalle = (TFinancieraDet)this.detalles.get(carreraId+modo);
			if(detalle == null)
				throw new Exception("La carrera "+carreraId+" en la modalidad "+modo+" no tiene valores en la tabla financiera");
			paso = "3";
			pctCCredito = detalle.getPCCredito();
			paso = "4";
			total = new Double(cCredito.doubleValue()*pctCCredito.doubleValue());		
		}catch(Exception e){
			throw new Error("Error al intentar obtener el costo del credito de la carrera "+carreraId+", en modalidad "+modo+
					", clasificacion "+clasificacion+" <br>CCredito "+cCredito+", pctCCredito "+pctCCredito+"<br>paso "+paso+"<br>"+e);
		}
		return total;
	}
	public Double getCMateria(String carreraId, Integer modo, String clasificacion) throws Exception {
		Double cMateria = new Double(0);
		Double pctCMateria = new Double(0);
		Double total = new Double(0);
		String paso = "1";
		try{
			cMateria = ((Clasificacion)this.encabezado.getClasificacion().get(new Integer(clasificacion))).getCMateria();
			paso = "2";
			TFinancieraDet detalle = (TFinancieraDet)this.detalles.get(carreraId+modo);
			if(detalle == null)
				throw new Exception("La carrera "+carreraId+" en la modalidad "+modo+" no tiene valores en la tabla financiera");
			paso = "3";
			pctCMateria = detalle.getPCMateria();
			paso = "4";
			total = new Double(cMateria.doubleValue()*pctCMateria.doubleValue());		
		}catch(Exception e){
			throw new Error("Error al intentar obtener el costo de la materia de la carrera "+carreraId+", en modalidad "+modo+
					", clasificacion "+clasificacion+" <br>Costo Materia "+cMateria+", pctCMateria "+pctCMateria+"<br>paso "+paso+"<br>"+e);
		}
		return total;
	}
	public Double getMatricula(String carreraId, Integer modo) throws Exception {
		Double matricula = (Double)this.encabezado.getMatricula();
		//System.out.println("TFinanciera 1");
		TFinancieraDet detalle = (TFinancieraDet)this.detalles.get(carreraId+modo);
		//System.out.println("TFinanciera 2 "+carreraId+modo);
		Double pctMatricula = detalle.getPMatricula();
		//System.out.println("TFinanciera 3");
		return new Double(matricula.doubleValue()*pctMatricula.doubleValue());
	}
	public Double getInternado(String carreraId, Integer modo) throws Exception {
		Double internado = (Double)this.encabezado.getInternado();
		//System.out.println("TFinanciera 4");
		TFinancieraDet detalle = (TFinancieraDet)this.detalles.get(carreraId+modo);
		//System.out.println("TFinanciera 5");
		Double pctInternado = detalle.getPInternado();
		//System.out.println("TFinanciera 6");
		return new Double(internado.doubleValue()*pctInternado.doubleValue());
	}
	public Double getTLegales(String carreraId, String modo) throws Exception {
		Double tLegales = (Double)this.encabezado.getTLegales();
		TFinancieraDet detalle = (TFinancieraDet)this.detalles.get(carreraId+modo);
		Double pctTLegales = detalle.getPTLegales();
		return new Double(tLegales.doubleValue()*pctTLegales.doubleValue());
	}
}
