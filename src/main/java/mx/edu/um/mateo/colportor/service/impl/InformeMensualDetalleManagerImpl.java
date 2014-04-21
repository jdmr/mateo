/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;
import mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao;
import mx.edu.um.mateo.colportor.model.InformeMensual;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;
import mx.edu.um.mateo.colportor.service.InformeMensualDetalleManager;
import mx.edu.um.mateo.general.service.BaseManager;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author osoto
 */
@Service
public class InformeMensualDetalleManagerImpl extends BaseManager implements InformeMensualDetalleManager {
    @Autowired
    private InformeMensualDetalleDao dao;
    
    /**
     * @see mx.edu.um.mateo.colportor.service.InformeMensualDetalleManager#lista(java.util.Map) 
     */
    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        params = dao.lista(params);
        
        //Varaibles para acumular totales
        Double totalHrsTrabajadas = 0.0;
        Integer totalLibrosVendidos = 0;
        BigDecimal totalPedidos = BigDecimal.ZERO;
        BigDecimal totalVentas = BigDecimal.ZERO;
        BigDecimal totalDiezmos = BigDecimal.ZERO;
        Integer totalLiteraturaGratis = 0;
        Integer totalOraciones = 0;
        Integer totalCasasVisitadas = 0;
        Integer totalEstudiosBiblicos = 0;
        Integer totalBautizados = 0;
        
        InformeMensualDetalle tmp = null;
        Calendar gcFecha = new GregorianCalendar(TimeZone.getTimeZone("America/Monterrey"), Locale.US);
        List<InformeMensualDetalle> infs = (List<InformeMensualDetalle>)params.get(Constantes.INFORMEMENSUAL_DETALLE_LIST);
        log.debug("Infs.size {}", infs.size());
        
        Map <Date, InformeMensualDetalle> mInformes = new TreeMap<>();
        for(InformeMensualDetalle imd : infs){
            mInformes.put(imd.getFecha(), imd);
        }
        
        InformeMensualDetalle totales = new InformeMensualDetalle();
        
        for(InformeMensualDetalle imd : infs){
            gcFecha.setTime(imd.getFecha());

            log.debug("Dia {}", gcFecha.get(Calendar.DAY_OF_WEEK));
            
            if(gcFecha.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                log.debug("Domingo ");
                tmp = new InformeMensualDetalle();                
                tmp.setInformeMensual(new InformeMensual("@"));//para identificar este tipo de registros
                gcFecha.add(Calendar.HOUR, -12);
                tmp.setFecha(gcFecha.getTime());
                tmp.setHrsTrabajadas(totalHrsTrabajadas);
                tmp.setLiteraturaVendida(totalLibrosVendidos);
                tmp.setTotalPedidos(totalPedidos);
                tmp.setTotalVentas(totalVentas);
                tmp.setDiezmo(totalDiezmos);
                tmp.setLiteraturaGratis(totalLiteraturaGratis);
                tmp.setOracionesOfrecidas(totalOraciones);
                tmp.setCasasVisitadas(totalCasasVisitadas);
                tmp.setContactosEstudiosBiblicos(totalEstudiosBiblicos);
                tmp.setBautizados(totalBautizados);
                mInformes.put(tmp.getFecha(), tmp);

                totalHrsTrabajadas = 0.0;
                totalLibrosVendidos = 0;
                totalPedidos = BigDecimal.ZERO;
                totalVentas = BigDecimal.ZERO;
                totalDiezmos = BigDecimal.ZERO;
                totalLiteraturaGratis = 0;
                totalOraciones = 0;
                totalCasasVisitadas = 0;
                totalEstudiosBiblicos = 0;
                totalBautizados = 0;
                
            }

            totalHrsTrabajadas += imd.getHrsTrabajadas();
            totalLibrosVendidos += imd.getLiteraturaVendida();
            totalPedidos = totalPedidos.add(imd.getTotalPedidos());
            totalVentas = totalVentas.add(imd.getTotalVentas());
            totalDiezmos = totalDiezmos.add(imd.getDiezmo());
            totalLiteraturaGratis += imd.getLiteraturaGratis();
            totalOraciones += imd.getOracionesOfrecidas();
            totalCasasVisitadas += imd.getCasasVisitadas();
            totalEstudiosBiblicos += imd.getContactosEstudiosBiblicos();
            totalBautizados += imd.getBautizados();
            
            totales.setHrsTrabajadas(totales.getHrsTrabajadas()+imd.getHrsTrabajadas());
            totales.setLiteraturaVendida(totales.getLiteraturaVendida()+imd.getLiteraturaVendida());
            totales.setTotalPedidos(totales.getTotalPedidos().add(imd.getTotalPedidos()));
            totales.setTotalVentas(totales.getTotalVentas().add(imd.getTotalVentas()));
            totales.setDiezmo(totales.getDiezmo().add(imd.getDiezmo()));
            totales.setLiteraturaGratis(totales.getLiteraturaGratis()+imd.getLiteraturaGratis());
            totales.setOracionesOfrecidas(totales.getOracionesOfrecidas()+imd.getOracionesOfrecidas());
            totales.setCasasVisitadas(totales.getCasasVisitadas()+imd.getCasasVisitadas());
            totales.setContactosEstudiosBiblicos(totales.getContactosEstudiosBiblicos()+imd.getContactosEstudiosBiblicos());
            totales.setBautizados(totales.getBautizados()+imd.getBautizados());
            

        }
        
        tmp = new InformeMensualDetalle();
        tmp.setInformeMensual(new InformeMensual("@"));//para identificar este tipo de registros
        gcFecha.add(Calendar.HOUR, -12);
        tmp.setFecha(gcFecha.getTime());
        tmp.setHrsTrabajadas(totalHrsTrabajadas);
        tmp.setLiteraturaVendida(totalLibrosVendidos);
        tmp.setTotalPedidos(totalPedidos);
        tmp.setTotalVentas(totalVentas);
        tmp.setDiezmo(totalDiezmos);
        tmp.setLiteraturaGratis(totalLiteraturaGratis);
        tmp.setOracionesOfrecidas(totalOraciones);
        tmp.setCasasVisitadas(totalCasasVisitadas);
        tmp.setContactosEstudiosBiblicos(totalEstudiosBiblicos);
        tmp.setBautizados(totalBautizados);
        mInformes.put(gcFecha.getTime(), tmp);    
        
        params.put(Constantes.INFORMEMENSUAL_DETALLE_LIST, new ArrayList(mInformes.values()));
        params.put("totales", totales);
        return params;
    }
    
    public InformeMensualDetalle obtiene(Long id){
        return dao.obtiene(id);
    }
    public InformeMensualDetalle crea(InformeMensualDetalle detalle){
        return crea(detalle);
    }
    
    public String elimina(Long id){
        return dao.elimina(id);
    }
    
}
