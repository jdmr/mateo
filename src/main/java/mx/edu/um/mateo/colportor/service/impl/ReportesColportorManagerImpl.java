/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.service.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.colportor.dao.AsociadoDao;
import mx.edu.um.mateo.colportor.dao.ColportorDao;
import mx.edu.um.mateo.colportor.dao.DocumentoDao;
import mx.edu.um.mateo.colportor.dao.InformeMensualDetalleDao;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.model.Asociado;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Documento;
import mx.edu.um.mateo.colportor.model.InformeMensualDetalle;
import mx.edu.um.mateo.colportor.model.ReporteColportorVO;
import mx.edu.um.mateo.colportor.model.TemporadaColportor;
import mx.edu.um.mateo.colportor.service.ReportesColportorManager;
import mx.edu.um.mateo.general.service.BaseManager;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author osoto
 */
@Service
public class ReportesColportorManagerImpl extends BaseManager implements ReportesColportorManager{
    @Autowired
    private TemporadaColportorDao tmpClpDao;
    @Autowired
    private ColportorDao clpDao;
    @Autowired
    private DocumentoDao docDao;
    @Autowired
    private InformeMensualDetalleDao infMensualDetalleDao;
    @Autowired
    private AsociadoDao ascDao;

    @Override
    /**
     * @see mx.edu.um.mateo.colportor.service.ReportesColportorManagerImpl#censoColportores(java.util.Map <String,Object> params)  throws Exception
     */
    public Map <String, Object> censoColportores(Map <String, Object> params) throws Exception {
        log.debug("Entrando a 'censoColportores'");
        params = clpDao.obtieneColportores(params);        
        
        ReporteColportorVO vo = null;
        List <ReporteColportorVO> reporteClps = new ArrayList<>();
        
        {
        Documento doc = new Documento();
        doc.setFecha(new Date()); // por si en algun momento se requiere pasar la fecha desde el controller
        params.put(Constantes.DOCUMENTOCOLPORTOR, doc);
        params = docDao.numeroComprasPorMes(params);
        }
        
        Map <String, List <Object[]>> mDocs = (Map)params.get(Constantes.DOCUMENTOCOLPORTOR_LIST);
        List <Object []> lDocs = null;
        Iterator <Object []> it = null;
        Integer acumVentas = 0;
        
        List <Colportor> clps = (List)params.get(Constantes.COLPORTOR_LIST);
        for(Colportor c : clps){
            vo = new ReporteColportorVO();
            vo.setColportor(c);
            log.debug("Leyendo Colportor {}", c);
            
            //Determinar numero de meses que ha comprado
            if(mDocs.containsKey(c.getClave())){
                lDocs = (List)mDocs.get(c.getClave());
                vo.setCol1(lDocs.size()); //numero de meses que compro
                log.debug("Leyendo docs {}", lDocs.size());
            
            //Determinar numero de ventas en el anio
                acumVentas = 0;
                it = lDocs.iterator();
                while(it.hasNext()){
                    acumVentas += (Integer)it.next()[2];
                }
                vo.setCol2(acumVentas);
            }
            reporteClps.add(vo);
        }
        
        params.put(Constantes.CONTAINSKEY_CENSOCOLPORTORES, reporteClps);
        
        return params;
    }

    @Override
    /**
     * @see mx.edu.um.mateo.colportor.service.ReportesColportorManagerImpl#concentradoPorTemporadas(java.util.Map <String,Object> params)  throws Exception
     */
    public Map<String, Object> concentradoPorTemporadas(Map<String, Object> params) throws Exception {
        Map <Long, ReporteColportorVO> mVOS = new TreeMap<>();
        TemporadaColportor tmpClp = null;
        ReporteColportorVO vo = null;
        
        params = docDao.concentradoVentasPorColportor(params);
        List <Object[]> objs = (List<Object[]>) params.get(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS);
        for(Object[] obj : objs){
            log.debug("{}",obj[0]);
            log.debug("{}",obj[1]);
            log.debug("{}",obj[2]);
            
            tmpClp = tmpClpDao.obtiene((Long)obj[1]);
            
            if(mVOS.containsKey(tmpClp.getTemporada().getFechaInicio().getTime())){
                vo = mVOS.get(tmpClp.getTemporada().getFechaInicio().getTime());
            }
            else{
                vo = new ReporteColportorVO();
                vo.setTemporadaColportor(tmpClp);
                
            }
            
            switch((String)obj[2]){
                case "Boletin":
                {
                    vo.setAcumuladoBoletin((BigDecimal)obj[0]);
                    break;
                }
                case "Diezmo":
                {
                    vo.setAcumuladoDiezmo((BigDecimal)obj[0]);
                    break;
                }
            }
                        
            mVOS.put(vo.getTemporadaColportor().getTemporada().getFechaInicio().getTime(),vo);
        }
        params.remove(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS);
        params.put(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS, mVOS.values());
        
        log.debug("{}",mVOS.values());
        return params;
    }
    
    /**
     * @see mx.edu.um.mateo.colportor.service.ReportesColportorManagerImpl#concentradoGralPorTemporadas(java.util.Map <String,Object> params)throwsException
     */
    public Map<String, Object> concentradoGralPorTemporadas(Map<String, Object> params) throws Exception {
        Map <Long, ReporteColportorVO> mVOS = new TreeMap<>();
        TemporadaColportor tmpClp = null;
        ReporteColportorVO vo = null;
        Colportor clp = null;
        
        BigDecimal totalBoletin = BigDecimal.ZERO;
        BigDecimal totalDiezmo = BigDecimal.ZERO;        
        
        params = docDao.concentradoGralVentasPorColportor(params);
        List <Object[]> objs = (List<Object[]>) params.get(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS);
        for(Object[] obj : objs){
            log.debug("{}",obj[0]);
            log.debug("{}",obj[1]);
            log.debug("{}",obj[2]);
            
            tmpClp = tmpClpDao.obtiene((Long)obj[1]);
            clp = tmpClp.getColportor();
            
            if(mVOS.containsKey(tmpClp.getFecha().getTime())){
                vo = mVOS.get(tmpClp.getFecha().getTime());
            }
            else{
                vo = new ReporteColportorVO();
                vo.setColportor(tmpClp.getColportor());
                vo.setTemporadaColportor(tmpClp);
                
            }
            
            switch((String)obj[2]){
                case "Boletin":
                {
                    vo.setAcumuladoBoletin((BigDecimal)obj[0]);
                    totalBoletin = totalBoletin.add((BigDecimal)obj[0]);
                    break;
                }
                case "Diezmo":
                {
                    vo.setAcumuladoDiezmo((BigDecimal)obj[0]);
                    totalDiezmo = totalDiezmo.add((BigDecimal)obj[0]);
                    break;
                }
            }
                        
            mVOS.put(vo.getTemporadaColportor().getFecha().getTime(),vo);
        }
        params.remove(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS);
        params.put(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS, mVOS.values());
        params.put(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_DIEZMO, totalDiezmo);
        params.put(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_BOLETIN, totalBoletin);
        
        log.debug("{}",mVOS.values());
        return params;
    }
    
    @Override
    /**
     * @see mx.edu.um.mateo.colportor.service.ReportesColportorManagerImpl#concentradoVentas(java.util.Map <String,Object> params)  throws Exception
     */
    public Map<String, Object> concentradoVentas(Map<String, Object> params) throws Exception {
        Map <String, ReporteColportorVO> mVOS = new TreeMap<>();
        TemporadaColportor tmpClp = null;
        Colportor clp = null;
        ReporteColportorVO vo = null;
        
        BigDecimal totalBoletin = BigDecimal.ZERO;
        BigDecimal totalDiezmo = BigDecimal.ZERO;
        
        params = docDao.concentradoVentas(params);
        List <Object[]> objs = (List<Object[]>) params.get(Constantes.CONTAINSKEY_CONCENTRADOVENTAS);
        for(Object[] obj : objs){
            log.debug("{}",obj[0]);
            log.debug("{}",obj[1]);
            log.debug("{}",obj[2]);
            log.debug("{}",obj[3]);
            
            tmpClp = tmpClpDao.obtiene((Long)obj[2]);
            clp = clpDao.obtiene((Long)obj[1]);
            
            if(mVOS.containsKey(clp.getApPaterno()+clp.getApMaterno()+tmpClp.getTemporada().getFechaInicio().getTime())){
                vo = mVOS.get(clp.getApPaterno()+clp.getApMaterno()+tmpClp.getTemporada().getFechaInicio().getTime());
            }
            else{
                vo = new ReporteColportorVO();
                vo.setColportor(clp);
                vo.setTemporadaColportor(tmpClp);
                
            }
            
            switch((String)obj[3]){
                case "Boletin":
                {
                    vo.setAcumuladoBoletin((BigDecimal)obj[0]);
                    totalBoletin = totalBoletin.add((BigDecimal)obj[0]);
                    break;
                }
                case "Diezmo":
                {
                    vo.setAcumuladoDiezmo((BigDecimal)obj[0]);
                    totalDiezmo = totalDiezmo.add((BigDecimal)obj[0]);
                    break;
                }
            }
                        
            mVOS.put(clp.getApPaterno()+clp.getApMaterno()+vo.getTemporadaColportor().getTemporada().getFechaInicio().getTime(),vo);
        }
        params.put(Constantes.CONTAINSKEY_CONCENTRADOVENTAS, mVOS.values());
        params.put(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_DIEZMO, totalDiezmo);
        params.put(Constantes.CONTAINSKEY_CONCENTRADOVENTAS_BOLETIN, totalBoletin);
        
        log.debug("{}",mVOS.values());
        return params;
    }
    
    @Override
    /**
     * @see mx.edu.um.mateo.colportor.service.ReportesColportorManagerImpl#planMensualOracion(java.util.Map <String,Object> params)  throws Exception
     */
    public Map<String, Object> planMensualOracion(Map<String, Object> params) throws Exception {
        Map <String, Colportor> mVOS = new TreeMap<>();
        
        Calendar cal = Calendar.getInstance(local);
        
        params = clpDao.obtieneColportores(params);
        List <Colportor> lista = (List)params.get(Constantes.COLPORTOR_LIST);
        
        gcFecha.setTime(new Date()); //Para comparar el mes actual
        
        for(Colportor clp : lista){
            try {
                cal.setTime(clp.getFechaDeNacimiento());
            } catch (NullPointerException e) {
                //Para los colportores que no tienen una fecha de nac. capturada
                cal.setTime(new Date()); 
                cal.set(Calendar.DATE, 1);
                clp.setFechaDeNacimiento(cal.getTime());
            }
            
            log.debug("Colportor {}, Mes Fecha Nac. {}, Mes Actual {}", new Object[]{clp.getNombre(), clp.getFechaDeNacimiento(), gcFecha.get(Calendar.MONTH) });
            if(gcFecha.get(Calendar.MONTH) == cal.get(Calendar.MONTH)){
                cal.set(Calendar.YEAR, gcFecha.get(Calendar.YEAR));
                mVOS.put(cal.getTimeInMillis()+"-"+clp.getNombre(), clp);
            }
            
        }
        for(Map.Entry<String, Colportor> entry : mVOS.entrySet()){
            log.debug("key {} - {} ", new Object[]{entry.getKey(), entry.getValue()});
        }
        
        params.put(Constantes.CONTAINSKEY_PLANMENSUALORACION, mVOS);        
        return params;
    }
    
    @Override
    /**
     * @see mx.edu.um.mateo.colportor.service.ReportesColportorManagerImpl#planDiarioOracion(java.util.Map <String,Object> params)  throws Exception
     */
    public Map<String, Object> planDiarioOracion(Map<String, Object> params) throws Exception {
        Map <String, Colportor> mVOS = new TreeMap<>();
        
        Calendar cal = Calendar.getInstance(local);
        
        params = clpDao.obtieneColportores(params);
        List <Colportor> lista = (List)params.get(Constantes.COLPORTOR_LIST);
        
        gcFecha.setTime(new Date()); //Para comparar el mes actual
        
        for(Colportor clp : lista){
            try {
                cal.setTime(clp.getFechaDeNacimiento());
            } catch (NullPointerException e) {
                //Para los colportores que no tienen una fecha de nac. capturada
                cal.setTime(new Date()); 
                cal.set(Calendar.DATE, 1);
                clp.setFechaDeNacimiento(cal.getTime());
            }
            
            log.debug("Colportor {}, Mes Fecha Nac. {}, Mes Actual {}", new Object[]{clp.getNombre(), clp.getFechaDeNacimiento(), gcFecha.get(Calendar.MONTH) });
            if(gcFecha.get(Calendar.DATE) == cal.get(Calendar.DATE)){
                cal.set(Calendar.YEAR, gcFecha.get(Calendar.YEAR));
                mVOS.put(cal.getTimeInMillis()+"-"+clp.getNombre(), clp);
            }
            
        }
        for(Map.Entry<String, Colportor> entry : mVOS.entrySet()){
            log.debug("key {} - {} ", new Object[]{entry.getKey(), entry.getValue()});
        }
        
        params.put(Constantes.CONTAINSKEY_PLANDIARIOORACION, mVOS);        
        return params;
    }
    
    @Override
    /**
     * @see mx.edu.um.mateo.colportor.service.ReportesColportorManagerImpl#informeMensualAsociado(java.util.Map <String,Object> params)  throws Exception
     */
    public Map<String, Object> informeMensualAsociado(Map<String, Object> params) throws Exception {
        Map <String, Colportor> mVOS = new TreeMap<>();
        
        Calendar cal = Calendar.getInstance(local);
        cal.set(Calendar.MONTH, ((Integer)params.get("mes")));
        cal.set(Calendar.YEAR, (Integer)params.get("year"));
        
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        System.out.println("Fecha Inicio "+cal.getTime());
        params.put("fechaInicio",cal.getTime());
        
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        System.out.println("Fecha Final "+cal.getTime());
        params.put("fechaFinal",cal.getTime());
                
        params = infMensualDetalleDao.listaInformes(params);
        List <InformeMensualDetalle> detalles = (List <InformeMensualDetalle>)params.get(Constantes.INFORMEMENSUAL_DETALLE_LIST);
        
        Map <String, Documento> mDiezmos = new TreeMap<>();
        params = docDao.obtieneTodosDiezmosAcumulados(params);
        
        log.debug("Primer diezmo {}", ((List<Documento>)params.get(Constantes.DOCUMENTOCOLPORTOR_LIST)).get(0));
        
        for(Object [] doc : (List<Object[]>)params.get(Constantes.DOCUMENTOCOLPORTOR_LIST)){
            log.debug("doc {}",doc);
            
            if(mDiezmos.containsKey(doc[0])){
                //acumulando
                mDiezmos.get(doc[0]).setImporte(mDiezmos.get(doc[0]).getImporte().add((BigDecimal)doc[2]));
            }
            else{                
                mDiezmos.put((String)doc[0], new Documento((BigDecimal)doc[2]));
            }
        }
        
        InformeMensualDetalle tmpDetalle = null;
        Map <String, InformeMensualDetalle> mDetalles = new TreeMap<>();
        
        InformeMensualDetalle totalDetalle = new InformeMensualDetalle();
        totalDetalle.setBautizados(0);
        totalDetalle.setCasasVisitadas(0);
        totalDetalle.setContactosEstudiosBiblicos(0);
        totalDetalle.setDiezmo(BigDecimal.ZERO);
        totalDetalle.setHrsTrabajadas(0.0);
        totalDetalle.setLiteraturaGratis(0);
        totalDetalle.setLiteraturaVendida(0);
        totalDetalle.setOracionesOfrecidas(0);
        totalDetalle.setTotalPedidos(BigDecimal.ZERO);
        totalDetalle.setTotalVentas(BigDecimal.ZERO);
        
        for(InformeMensualDetalle det : detalles){
            det.getInformeMensual().setColportor(clpDao.obtiene(det.getInformeMensual().getColportor().getId()));
            
            if(mDiezmos.containsKey(det.getInformeMensual().getColportor().getClave())){
                det.setDiezmo(mDiezmos.get(det.getInformeMensual().getColportor().getClave()).getImporte());
            }
            
            //Realizar aproximaciones de los valores de horas trabajadas
            BigDecimal rango = BigDecimal.ZERO;
            //este valor temporalmente es fijo, posteriormente debe cambiarse por la lectura de los rangos
            switch(gcFecha.get(Calendar.YEAR)){
                case 2014: {rango = new BigDecimal("5625.00"); break;}
                default: {break;}
            }
            
            if(det.getTotalVentas().compareTo(rango) > 0){
                det.setHrsTrabajadas(40.0*13.0/3.0);
            }
            else{
                //regla de tres para obtener hrs proporcionales
                BigDecimal tmp = new BigDecimal(40.0*13.0/3.0);
                MathContext mc = new MathContext(4, RoundingMode.HALF_EVEN);
                det.setHrsTrabajadas(((det.getTotalVentas().multiply(tmp, mc)).divide(rango, mc)).doubleValue());
            }
            
            det.setCasasVisitadas(new Double(det.getHrsTrabajadas()*2.0).intValue());
            det.setOracionesOfrecidas(new Double(det.getCasasVisitadas() * 0.1).intValue());
            det.setContactosEstudiosBiblicos(new Double(det.getOracionesOfrecidas() * 0.1).intValue());
            
            totalDetalle.setBautizados(totalDetalle.getBautizados()+det.getBautizados());
            totalDetalle.setCasasVisitadas(totalDetalle.getCasasVisitadas()+det.getCasasVisitadas());
            totalDetalle.setContactosEstudiosBiblicos(totalDetalle.getContactosEstudiosBiblicos()+det.getContactosEstudiosBiblicos());
            totalDetalle.setDiezmo(totalDetalle.getDiezmo().add(det.getDiezmo()));
            totalDetalle.setHrsTrabajadas(totalDetalle.getHrsTrabajadas()+det.getHrsTrabajadas());
            totalDetalle.setLiteraturaGratis(totalDetalle.getLiteraturaGratis()+det.getLiteraturaGratis());
            totalDetalle.setLiteraturaVendida(totalDetalle.getLiteraturaVendida()+det.getLiteraturaVendida());
            totalDetalle.setOracionesOfrecidas(totalDetalle.getOracionesOfrecidas()+det.getOracionesOfrecidas());
            totalDetalle.setTotalPedidos(totalDetalle.getTotalPedidos().add(det.getTotalPedidos()));
            totalDetalle.setTotalVentas(totalDetalle.getTotalVentas().add(det.getTotalVentas()));
            
            if(!mDetalles.containsKey(det.getInformeMensual().getColportor().getClave())){
                tmpDetalle = new InformeMensualDetalle();
                tmpDetalle.setInformeMensual(det.getInformeMensual());
                tmpDetalle.setBautizados(det.getBautizados());
                tmpDetalle.setCasasVisitadas(det.getCasasVisitadas());
                tmpDetalle.setContactosEstudiosBiblicos(det.getContactosEstudiosBiblicos());
                tmpDetalle.setDiezmo(det.getDiezmo());
                tmpDetalle.setHrsTrabajadas(det.getHrsTrabajadas());
                tmpDetalle.setLiteraturaGratis(det.getLiteraturaGratis());
                tmpDetalle.setLiteraturaVendida(det.getLiteraturaVendida());
                tmpDetalle.setOracionesOfrecidas(det.getOracionesOfrecidas());
                tmpDetalle.setTotalPedidos(det.getTotalPedidos());
                tmpDetalle.setTotalVentas(det.getTotalVentas());
                mDetalles.put(det.getInformeMensual().getColportor().getClave(), tmpDetalle);
            }
            else{
                tmpDetalle = mDetalles.get(det.getInformeMensual().getColportor().getClave());
                tmpDetalle.setBautizados(tmpDetalle.getBautizados()+det.getBautizados());
                tmpDetalle.setCasasVisitadas(tmpDetalle.getCasasVisitadas()+det.getCasasVisitadas());
                tmpDetalle.setContactosEstudiosBiblicos(tmpDetalle.getContactosEstudiosBiblicos()+det.getContactosEstudiosBiblicos());
                tmpDetalle.setDiezmo(tmpDetalle.getDiezmo().add(det.getDiezmo()));
                tmpDetalle.setHrsTrabajadas(tmpDetalle.getHrsTrabajadas()+det.getHrsTrabajadas());
                tmpDetalle.setLiteraturaGratis(tmpDetalle.getLiteraturaGratis()+det.getLiteraturaGratis());
                tmpDetalle.setLiteraturaVendida(tmpDetalle.getLiteraturaVendida()+det.getLiteraturaVendida());
                tmpDetalle.setOracionesOfrecidas(tmpDetalle.getOracionesOfrecidas()+det.getOracionesOfrecidas());
                tmpDetalle.setTotalPedidos(tmpDetalle.getTotalPedidos().add(det.getTotalPedidos()));
                tmpDetalle.setTotalVentas(tmpDetalle.getTotalVentas().add(det.getTotalVentas()));
            }
        }
        
        params.put(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO, new ArrayList(mDetalles.values()));        
        params.put(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO_TOTALES, totalDetalle);        
        return params;
    }
    
    @Override
    /**
     * @see mx.edu.um.mateo.colportor.service.ReportesColportorManagerImpl#informeConcentradoAsociadosAsociacion(java.util.Map <String,Object> params)  throws Exception
     */
    public Map<String, Object> informeConcentradoAsociadosAsociacion(Map<String, Object> params) throws Exception {
        Map <String, Colportor> mVOS = new TreeMap<>();
        
        Calendar cal = Calendar.getInstance(local);
        cal.set(Calendar.MONTH, ((Integer)params.get("mes")));
        cal.set(Calendar.YEAR, (Integer)params.get("year"));
        
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
        System.out.println("Fecha Inicio "+cal.getTime());
        params.put("fechaInicio",cal.getTime());
        
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
        System.out.println("Fecha Final "+cal.getTime());
        params.put("fechaFinal",cal.getTime());
                
        params = infMensualDetalleDao.listaInformesConcentradoAsociadosAsociacion(params);
        List <InformeMensualDetalle> detalles = (List <InformeMensualDetalle>)params.get(Constantes.INFORMEMENSUAL_DETALLE_LIST);
        
        Map <String, Documento> mDiezmos = new TreeMap<>();
        params = docDao.obtieneTodosDiezmosAcumulados(params);
        
        for(Object [] doc : (List<Object[]>)params.get(Constantes.DOCUMENTOCOLPORTOR_LIST)){
            if(mDiezmos.containsKey(doc[0])){
                //acumulando
                mDiezmos.get(doc[0]).setImporte(mDiezmos.get(doc[0]).getImporte().add((BigDecimal)doc[2]));
            }
            else{                
                mDiezmos.put((String)doc[0], new Documento((BigDecimal)doc[2]));
            }
        }
        
        Asociado asociado = null;
        InformeMensualDetalle tmpDetalle = null;
        Map <String, InformeMensualDetalle> mDetalles = new TreeMap<>();
        
        InformeMensualDetalle totalDetalle = new InformeMensualDetalle();
        totalDetalle.setBautizados(0);
        totalDetalle.setCasasVisitadas(0);
        totalDetalle.setContactosEstudiosBiblicos(0);
        totalDetalle.setDiezmo(BigDecimal.ZERO);
        totalDetalle.setHrsTrabajadas(0.0);
        totalDetalle.setLiteraturaGratis(0);
        totalDetalle.setLiteraturaVendida(0);
        totalDetalle.setOracionesOfrecidas(0);
        totalDetalle.setTotalPedidos(BigDecimal.ZERO);
        totalDetalle.setTotalVentas(BigDecimal.ZERO);
        
        MathContext mc = new MathContext(4, RoundingMode.HALF_EVEN);
        
        for(InformeMensualDetalle det : detalles){
            asociado = ascDao.obtiene(det.getInformeMensual().getColportor().getId());
            
            if(mDiezmos.containsKey(asociado.getClave())){
                det.setDiezmo(mDiezmos.get(asociado.getClave()).getImporte());
            }
            
            //Realizar aproximaciones de los valores de horas trabajadas
            BigDecimal rango = BigDecimal.ZERO;
            //este valor temporalmente es fijo, posteriormente debe cambiarse por la lectura de los rangos
            switch(gcFecha.get(Calendar.YEAR)){
                case 2014: {rango = new BigDecimal("5625.00"); break;}
                default: {break;}
            }
            
            //Las ventas se dividen por 2, para evitar que las horas se dupliquen
            if(det.getTotalVentas().divide(new BigDecimal("2"),mc).compareTo(rango) > 0){
                det.setHrsTrabajadas(40.0*13.0/3.0);
            }
            else{
                //regla de tres para obtener hrs proporcionales
                BigDecimal tmp = new BigDecimal(40.0*13.0/3.0);
                det.setHrsTrabajadas(((det.getTotalVentas().multiply(tmp, mc)).divide(rango, mc)).doubleValue());
            }
            
            det.setCasasVisitadas(new Double(det.getHrsTrabajadas()*2.0).intValue());
            det.setOracionesOfrecidas(new Double(det.getCasasVisitadas() * 0.1).intValue());
            det.setContactosEstudiosBiblicos(new Double(det.getOracionesOfrecidas() * 0.1).intValue());
            
            totalDetalle.setBautizados(totalDetalle.getBautizados()+det.getBautizados());
            totalDetalle.setCasasVisitadas(totalDetalle.getCasasVisitadas()+det.getCasasVisitadas());
            totalDetalle.setContactosEstudiosBiblicos(totalDetalle.getContactosEstudiosBiblicos()+det.getContactosEstudiosBiblicos());
            totalDetalle.setDiezmo(totalDetalle.getDiezmo().add(det.getDiezmo()));
            totalDetalle.setHrsTrabajadas(totalDetalle.getHrsTrabajadas()+det.getHrsTrabajadas());
            totalDetalle.setLiteraturaGratis(totalDetalle.getLiteraturaGratis()+det.getLiteraturaGratis());
            totalDetalle.setLiteraturaVendida(totalDetalle.getLiteraturaVendida()+det.getLiteraturaVendida());
            totalDetalle.setOracionesOfrecidas(totalDetalle.getOracionesOfrecidas()+det.getOracionesOfrecidas());
            totalDetalle.setTotalPedidos(totalDetalle.getTotalPedidos().add(det.getTotalPedidos()));
            totalDetalle.setTotalVentas(totalDetalle.getTotalVentas().add(det.getTotalVentas()));
            
            if(!mDetalles.containsKey(asociado.getClave())){
                tmpDetalle = new InformeMensualDetalle();
                tmpDetalle.setInformeMensual(det.getInformeMensual());
                tmpDetalle.setBautizados(det.getBautizados());
                tmpDetalle.setCasasVisitadas(det.getCasasVisitadas());
                tmpDetalle.setContactosEstudiosBiblicos(det.getContactosEstudiosBiblicos());
                tmpDetalle.setDiezmo(det.getDiezmo());
                tmpDetalle.setHrsTrabajadas(det.getHrsTrabajadas());
                tmpDetalle.setLiteraturaGratis(det.getLiteraturaGratis());
                tmpDetalle.setLiteraturaVendida(det.getLiteraturaVendida());
                tmpDetalle.setOracionesOfrecidas(det.getOracionesOfrecidas());
                tmpDetalle.setTotalPedidos(det.getTotalPedidos());
                tmpDetalle.setTotalVentas(det.getTotalVentas());
                mDetalles.put(asociado.getClave(), tmpDetalle);
            }
            else{
                tmpDetalle = mDetalles.get(asociado.getClave());
                tmpDetalle.setBautizados(tmpDetalle.getBautizados()+det.getBautizados());
                tmpDetalle.setCasasVisitadas(tmpDetalle.getCasasVisitadas()+det.getCasasVisitadas());
                tmpDetalle.setContactosEstudiosBiblicos(tmpDetalle.getContactosEstudiosBiblicos()+det.getContactosEstudiosBiblicos());
                tmpDetalle.setDiezmo(tmpDetalle.getDiezmo().add(det.getDiezmo()));
                tmpDetalle.setHrsTrabajadas(tmpDetalle.getHrsTrabajadas()+det.getHrsTrabajadas());
                tmpDetalle.setLiteraturaGratis(tmpDetalle.getLiteraturaGratis()+det.getLiteraturaGratis());
                tmpDetalle.setLiteraturaVendida(tmpDetalle.getLiteraturaVendida()+det.getLiteraturaVendida());
                tmpDetalle.setOracionesOfrecidas(tmpDetalle.getOracionesOfrecidas()+det.getOracionesOfrecidas());
                tmpDetalle.setTotalPedidos(tmpDetalle.getTotalPedidos().add(det.getTotalPedidos()));
                tmpDetalle.setTotalVentas(tmpDetalle.getTotalVentas().add(det.getTotalVentas()));
            }
        }
        
        params.put(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO, new ArrayList(mDetalles.values()));        
        params.put(Constantes.CONTAINSKEY_INFORMEMENSUALASOCIADO_TOTALES, totalDetalle);        
        return params;
    }
}
