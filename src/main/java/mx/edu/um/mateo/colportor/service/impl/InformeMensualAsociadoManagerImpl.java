/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.service.impl;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import mx.edu.um.mateo.colportor.dao.InformeMensualAsociadoDao;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.InformeMensualAsociado;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;
import mx.edu.um.mateo.colportor.service.InformeMensualAsociadoManager;
import mx.edu.um.mateo.general.service.BaseManager;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author osoto
 */
@Service
public class InformeMensualAsociadoManagerImpl extends BaseManager implements InformeMensualAsociadoManager {
    @Autowired
    private InformeMensualAsociadoDao informeMensualAsociadoDao;

    /**
     * @see mx.edu.um.mateo.colportor.service.InformeMensualAsociadoManager#finalizaInforme(java.util.List, mx.edu.um.mateo.colportor.model.Asociado) 
     */
    @Override
    @Transactional
    public InformeMensualAsociado finalizaInforme(List<InformeMensualDetalle> detalles, Asociado asociado) throws Exception {
        InformeMensualAsociado informeMensualAsociado = null;
        Boolean flag = false;
        
        for(InformeMensualDetalle det : detalles){
            informeMensualAsociado = new InformeMensualAsociado();
            informeMensualAsociado.setStatus(Constantes.STATUS_ACTIVO);
            informeMensualAsociado.setAsociado(asociado);
            informeMensualAsociado.setColportor(det.getInformeMensual().getColportor());
            informeMensualAsociado.setHorasTrabajadas(det.getHrsTrabajadas().intValue());
            informeMensualAsociado.setLibrosVendidos(det.getLiteraturaVendida());
            informeMensualAsociado.setTotalPedidos(det.getTotalPedidos());
            informeMensualAsociado.setTotalVentas(det.getTotalVentas());
            informeMensualAsociado.setLiteraturaGratis(det.getLiteraturaGratis());
            informeMensualAsociado.setOracionesOfrecidas(det.getOracionesOfrecidas());
            informeMensualAsociado.setCasasVisitadas(det.getCasasVisitadas());
            informeMensualAsociado.setEstudiosBiblicos(det.getContactosEstudiosBiblicos());
            informeMensualAsociado.setBautizados(det.getBautizados());
            informeMensualAsociado.setDiezmos(det.getDiezmo());
            log.debug("Fecha Registro {}", det.getInformeMensual().getFecha());
            informeMensualAsociado.setFechaRegistro(det.getInformeMensual().getFecha());
            
            //Validar que no exista ya un registro de este asociado en este mes
            Map<String, Object> params = new HashMap<>();
        
            params.put("empresa", asociado.getEmpresa().getId());
            params.put("asociado", asociado.getId());
            
            Calendar cal = new GregorianCalendar(Locale.forLanguageTag("es"));
            cal.setTime(det.getInformeMensual().getFecha());

            Calendar calTmp1 = Calendar.getInstance();
            calTmp1.set(Calendar.YEAR, cal.get(Calendar.YEAR));
            calTmp1.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            calTmp1.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
            params.put("fechaInicial", calTmp1.getTime());

            Calendar calTmp2 = Calendar.getInstance();
            calTmp2.set(Calendar.YEAR, cal.get(Calendar.YEAR));
            calTmp2.set(Calendar.MONTH, cal.get(Calendar.MONTH));
            calTmp2.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
            params.put("fechaFinal", calTmp2.getTime());

            log.debug("cal1 {}, cal2 {}", calTmp1.getTime(), calTmp2.getTime());
            
            params = informeMensualAsociadoDao.lista(params);
            
            //Se prende la bandera la primera vez que entra
            if(!flag && ((Long)params.get(Constantes.CONTAINSKEY_CANTIDAD)) == 0 ){
                flag = true;
            }
            
            if(flag){
                informeMensualAsociado = informeMensualAsociadoDao.crea(informeMensualAsociado);
            }
        }
        return informeMensualAsociado;
    }
    
    
    
}