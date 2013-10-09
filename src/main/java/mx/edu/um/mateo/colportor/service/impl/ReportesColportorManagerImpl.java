/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mx.edu.um.mateo.colportor.dao.ColportorDao;
import mx.edu.um.mateo.colportor.dao.DocumentoDao;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Documento;
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
     * @see mx.edu.um.mateo.colportor.service.ReportesColportorManagerImpl#concentradoGralPorTemporadas(java.util.Map <String,Object> params)  throws Exception
     */
    public Map<String, Object> concentradoGralPorTemporadas(Map<String, Object> params) throws Exception {
        Map <Long, ReporteColportorVO> mVOS = new TreeMap<>();
        TemporadaColportor tmpClp = null;
        ReporteColportorVO vo = null;
        Colportor clp = null;
        
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
                    break;
                }
                case "Diezmo":
                {
                    vo.setAcumuladoDiezmo((BigDecimal)obj[0]);
                    break;
                }
            }
                        
            mVOS.put(vo.getTemporadaColportor().getFecha().getTime(),vo);
        }
        params.remove(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS);
        params.put(Constantes.CONTAINSKEY_CONCENTRADOPORTEMPORADAS, mVOS.values());
        
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
                    break;
                }
                case "Diezmo":
                {
                    vo.setAcumuladoDiezmo((BigDecimal)obj[0]);
                    break;
                }
            }
                        
            mVOS.put(clp.getApPaterno()+clp.getApMaterno()+vo.getTemporadaColportor().getTemporada().getFechaInicio().getTime(),vo);
        }
        params.put(Constantes.CONTAINSKEY_CONCENTRADOVENTAS, mVOS.values());
        
        log.debug("{}",mVOS.values());
        return params;
    }
}
