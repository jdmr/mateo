/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.service.BaseManager;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ObjectRetrievalFailureException;
import mx.edu.um.mateo.rh.dao.EmpleadoDao;
import mx.edu.um.mateo.rh.model.Empleado;
import mx.edu.um.mateo.rh.service.EmpleadoManager;
import mx.edu.um.mateo.rh.service.TipoEmpleadoManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author AMDA
 */

@Component
@Transactional
public class EmpleadoManagerImpl extends BaseManager implements EmpleadoManager {
    
    @Autowired
    private EmpleadoDao dao;
    @Autowired
    private TipoEmpleadoManager teMgr;
    
    /**
     * @see mx.edu.um.mateo.mateo.rh.service.EmpleadoManager#lista(java.util.Map) 
     */
    public Map<String, Object> lista(Map<String, Object> params){
        return dao.lista(params);
    }
    /**
     * @see mx.edu.um.mateo.mateo.rh.service.EmpleadoManager#
     */
    public Empleado obtiene(Long id) throws ObjectRetrievalFailureException{
        return dao.obtiene(id);
    }
    
    /**
     * @see mx.edu.um.mateo.rh.service.EmpleadoManager#getEmpleados(mx.edu.um.rh.model.Empleado)
     */
    public List getEmpleados(final Empleado empleado) {
        return dao.searchEmpleado(empleado);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.EmpleadoManager#getEmpleado(String id)
     */
    public Empleado getEmpleado(final Empleado empleado) {
        return dao.getEmpleado(empleado);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.EmpleadoManager#saveEmpleado(mx.edu.um.mateo.rh.model.Empleado, mx.edu.um.mateo.general.model.Usuario) 
     */
    public void saveEmpleado(Empleado empleado, Usuario usuario) {
        log.debug("saveEmpleado ");
    	if(empleado.getId() == null){
    		Empleado emp = new Empleado();
                emp.setClave(teMgr.obtiene(empleado.getTipoEmpleado().getId()).getPrefijo());
    		empleado.setClave(this.getNuevaClave(emp));
                empleado.setStatus(Constantes.STATUS_ACTIVO);
    	}
    	//log.debug(empleado);
    	dao.saveEmpleado(empleado, usuario);
    }

    /**
     * @see mx.edu.um.mateo.rh.service.EmpleadoManager#removeEmpleado(mx.edu.um.mateo.rh.model.Empleado) 
     */
    public void removeEmpleado(final Empleado empleado) {
        //dao.removeEmpleado(empleado);
        
    }
    /**
     * @see mx.edu.um.mateo.rh.service.EmpleadoManager#elimina(java.lang.Long) 
     */
    public String elimina(Long id){
        return "";
    }
    
    public String getNuevaClave(final Empleado empleado){
        log.debug("getNuevaClave");
    	if(empleado.getClave()==null || empleado.getClave().length() == 3){
    		Integer clave = new Integer(0);
    		Integer tmp = new Integer(0);
    		
    		List claves = dao.searchEmpleado(empleado);
                
                if(claves.isEmpty()){
                    empleado.setClave("980");
                }
                
                if(empleado.getClave() != null){
                    log.debug("Clave del empleado {}", empleado.getClave());
                }
    		
    		Iterator i = claves.iterator();    		
    		while(i.hasNext()){
    			
    			clave = new Integer(((Empleado)i.next()).getClave().substring(3));                        
    			    		
                        log.debug("Clave {} - tmp{} = {}", new Object[]{clave, tmp, clave-tmp});
    			if(clave - tmp == 1){
    				tmp = clave;
    			}
    			else{
    				log.debug("Nueva clave {}", empleado.getClave()+"0000000".substring(empleado.getClave().length()+String.valueOf(tmp+1).length())+String.valueOf(tmp+1));
    				return empleado.getClave()+"0000000".substring(empleado.getClave().length()+String.valueOf(tmp+1).length())+String.valueOf(tmp+1);
    			}
    		}
            log.debug("Nueva clave {} ",empleado.getClave()+"0000000".substring(empleado.getClave().length()+String.valueOf(tmp+1).length())+String.valueOf(tmp+1));
            return empleado.getClave()+"0000000".substring(empleado.getClave().length()+String.valueOf(tmp+1).length())+String.valueOf(tmp+1);
    	}
    	return null;
    }

	public List searchEmpleado(Empleado empleado) {
		return dao.searchEmpleado(empleado);
	}
        public List searchEmpleadoByClaveOrApPaterno(Empleado empleado) {
                return dao.searchEmpleadoByClaveOrApPaterno (empleado);
        }
	public Object getEmpleadosBday(Empleado empleado){
		return dao.getEmpleadosBday(empleado);
	}
        
    public Empleado getEmpleadoByClave(final Empleado empleado){
    	return dao.getEmpleadoClave(empleado);
    }
         /**
      * @see mx.edu.um.mateo.rh.service.SolicitudSalidaManager#getDiasVacacionesActuales(SolicitudSalida solicitudSalida) throws Exception
     */
    public void saveDiasVacacionesActuales(Empleado empleado, User user) throws Exception {

        Integer dias = 0;
        Boolean sw = false;
        //EmpleadoVacaciones ev = null;

//        if (empleado.cumplioAniversarioLaboral(new Date())) {
//            gcFecha.setTime(new Date());
//            Integer year = gcFecha.get(Calendar.YEAR);
//            Iterator it = empleado.getVacacionesByProcess().iterator();
//
//            while (it.hasNext()) {
//                ev = (EmpleadoVacaciones) it.next();
//                gcFecha.setTime(ev.getFechaCaptura());
//                //log.debug(ev);
//                if (year == gcFecha.get(Calendar.YEAR) && ev.getNumeroDias() > 0) {
//                    sw = true;
//                }
//            }
//
//            //log.debug(sw);
//            if (!sw) {
//                if (empleado.getAntiguedadBase() < 6.0) {
//                    dias = 14;
//
//                } else if (empleado.getAntiguedadBase() >= 6.0 && empleado.getAntiguedadBase() < 10.0) {
//                    dias = 21;
//
//                } else {
//                    dias = 28;
//                }
//                //log.debug(dias);
//                EmpleadoVacaciones vacaciones = null;
//
//                //Generar registro de vacaciones
//                empleado.getVacaciones();
//
//                vacaciones = new EmpleadoVacaciones();
//                vacaciones.setDescripcion("Vacaciones por " + empleado.getAntiguedadBase() + " de antiguedad.");
//                vacaciones.setNumeroDias(dias);
//                vacaciones.setFechaCaptura(new Date());
//                vacaciones.setUserCaptura(user);
//                vacaciones.setStatus(Constants.EMPLEADOVACACIONES_STATUS_ALTA_BY_PROCESS);
//
//                empleado.addVacaciones(vacaciones);
//                dao.saveEmpleado(empleado);
//
//            }
//        }
    }


   /**
      * @see mx.edu.um.mateo.rh.service.EmpleadoManager#getDiasVacacionesNuevoEmpleado(mx.edu.um.rh.model.Empleado)
     */
//public Integer getDiasVacacionesNuevoEmpleado(Empleado empleado) throws Exception {
//
//    Integer dias = 0;
//    long fechaInicialMs, fechaFinalMs, diferencia;
//    double diff;
//    BigDecimal antiguedad=BigDecimal.valueOf(empleado.getAntiguedadBase()).add(empleado.getExperienciaFueraUm().divide(new BigDecimal(2)));
//    
//    //log.debug(antiguedad.doubleValue());
//    
//    // dias de vacaciones de acuerdo a antiguedad denominacional
//    if (antiguedad.doubleValue() < 6.0) {
//        dias = 14;
//    } else if (antiguedad.doubleValue() >= 6.0 && antiguedad.doubleValue() < 10.0) {
//        dias = 21;
//    } else {
//        dias = 28;
//    }
//     //log.debug(dias);
//
//     //establezco el ultimo dia del año
//     gcFecha.setTime(new Date());
//     gcFecha.set(Calendar.MONTH, Calendar.DECEMBER);
//     gcFecha.set(Calendar.DAY_OF_MONTH, gcFecha.getMaximum(Calendar.DAY_OF_MONTH));
//     //log.debug(gcFecha);
//
//     //obtengo la diferencia en dias de la fecha actual, al fin de año
//     fechaInicialMs= new Date().getTime();
//     fechaFinalMs=gcFecha.getTimeInMillis();
//     diferencia = fechaFinalMs - fechaInicialMs;
//     diff = Math.floor(diferencia / (1000 * 60 * 60 * 24));
//     //log.debug(diff);
//
//     //elimino el punto decimal del la cadena
//     String d =String.valueOf(diff);
//     d=d.substring(0, d.length()-2);
//     //log.debug(d);
//     //log.debug(Integer.parseInt(d)*dias/365);
//
//     return Integer.parseInt(d)*dias/365;
//}




    /**
     * @see mx.edu.um.mateo.rh.service.EmpleadoManager#getEmpleadosActivosByContabilidad(mx.edu.um.rh.model.EmpleadoPuesto)
     * @param puesto
     * @return
     * @throws java.lang.Exception
     */
//    public List getEmpleadosActivosByContabilidad(EmpleadoPuesto puesto) throws Exception {
//        List lista = new ArrayList();
//        List<Empleado> empleados = dao.searchEmpleadoByCCosto(puesto);
//        for (Empleado e : empleados) {
//            //Si el empleado esta activo
//            if (e.getStatus().equals(Constants.STATUS_ACTIVO)) {
//                lista.add(e);
//            }
//        }
//        return lista;
//    }

//     public List getEmpleadosActivosByContabilidadModalidadTipoEmpleado(EmpleadoPuesto puesto,Empleado empleado,String sChecked []) throws Exception {
//        List lista = new ArrayList();
//        List<Empleado> empleados = dao.searchEmpleadoByCCostoModalidadTipoEmpleado(puesto, empleado,sChecked);
//        for (Empleado e : empleados) {
//            //Si el empleado esta activo
//            if (e.getStatus().equals(Constants.STATUS_ACTIVO)) {
//                lista.add(e);
//            }
//        }
//        return lista;
//    }


//    public Map <String, Empleado> getEmpleadosActivosInMap(Ejercicio ejercicio, String... contabilidades) throws Exception {
//        //log.debug("Inmap >>>>>:"+contabilidades.length+":: "+contabilidades[0]);
//        Map <String,Empleado>empleados = new TreeMap();
//        List <Empleado> lista = null;
//      //  for (String c : contabilidades) {
//            //Obtener lista de empleados activos si esta no existe en el application
//            EmpleadoPuesto puesto = new EmpleadoPuesto();
//            CentroCosto ccosto = new CentroCosto();
//            ccosto.setEjercicio(ejercicio);
//            ccosto.setIdCCosto("0");
//            puesto.setCentroCosto(ccosto);
//
//            lista = this.getEmpleadosActivosByContabilidadModalidadTipoEmpleado(puesto, null, contabilidades);
//            //log.debug("inmap<<<<<"+lista.size());
//
//            for(Empleado e : lista){
//                empleados.put(e.getClave(),e);
//            }
//            
//        //}
//        return empleados;
//    }
//
//    public List verificarPuestoEstudios(Empleado empleado){
//        List verificacionList=new ArrayList();
//        if(empleado.getEstudios().size()==0)
//            verificacionList.add("empleadoEstudios");
//        
//        if(empleado.getPuestosActivos().size()==0)
//            verificacionList.add("empleadoPuestos");
//
//        //log.debug(verificacionList.size());
//        return verificacionList;
//    }
//
//    public Set getEmpleadoPerDeds(Empleado empleado){
//        return dao.getEmpleadoPerDeds(empleado);
//    }
    
}
