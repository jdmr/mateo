/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.colportor.dao.ColportorDao;
import mx.edu.um.mateo.colportor.dao.DocumentoDao;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Documento;
import mx.edu.um.mateo.colportor.model.ReporteColportorVO;
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
    
        
}
