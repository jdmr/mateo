/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mx.edu.um.mateo.colportor.dao.ProyectoColportorDao;
import mx.edu.um.mateo.colportor.model.PedidoColportor;
import mx.edu.um.mateo.colportor.model.ProyectoColportor;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author osoto
 */
@Repository
@Transactional
public class ProyectoColportorDaoHibernate extends BaseDao implements ProyectoColportorDao {

    @Override
    public Map<String, Object> lista(Map<String, Object> params) {
        log.debug("Buscando lista proyectos del colportor con params {}", params);
        if (params == null) {
            params = new HashMap<>();
        }
        log.debug("paginado {}", params.containsKey(Constantes.CONTAINSKEY_MAX));
        if (!params.containsKey(Constantes.CONTAINSKEY_MAX)) {
            params.put(Constantes.CONTAINSKEY_MAX, 10);
        } else {
            params.put(Constantes.CONTAINSKEY_MAX, Math.min((Integer) params.get(Constantes.CONTAINSKEY_MAX), 100));
        }

        if (params.containsKey(Constantes.CONTAINSKEY_PAGINA)) {
            Long pagina = (Long) params.get(Constantes.CONTAINSKEY_PAGINA);
            Long offset = (pagina - 1) * (Integer) params.get(Constantes.CONTAINSKEY_MAX);
            params.put(Constantes.CONTAINSKEY_OFFSET, offset.intValue());
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_OFFSET)) {
            params.put(Constantes.CONTAINSKEY_OFFSET, 0);
        }
        
        Criteria criteria = currentSession().createCriteria(ProyectoColportor.class);
        Criteria countCriteria = currentSession().createCriteria(ProyectoColportor.class);
        
        log.debug("empresa {}", params.get("empresa"));
        criteria.createAlias("usuario","user");
        criteria.createCriteria("empresa")
                .add(Restrictions.idEq(params.get("empresa")));

        countCriteria.createAlias("usuario","user");
        countCriteria.createCriteria("empresa")
                .add(Restrictions.idEq(params.get("empresa")));
        
        if(params.get("usuario")!=null){
            criteria.add(Restrictions.eq("user.id", (Long)params.get("usuario")));
            countCriteria.add(Restrictions.eq("user.id", (Long)params.get("usuario")));
        }
        else{
            //No hay id del asociado
            log.debug("No se proporciono id del usuario");
            params.put(Constantes.PROYECTO_COLPORTOR_LIST, new ArrayList());
            params.put(Constantes.CONTAINSKEY_CANTIDAD, 0L);
            return params;
        }

        if (params.containsKey(Constantes.CONTAINSKEY_FILTRO)) {
            String filtro = (String) params.get(Constantes.CONTAINSKEY_FILTRO);
            filtro = "%" + filtro + "%";            
            Disjunction propiedades = Restrictions.disjunction();
            propiedades.add(Restrictions.ilike("codigo", filtro));
            propiedades.add(Restrictions.ilike("nombre", filtro));
            criteria.add(propiedades);
            countCriteria.add(propiedades);
        }

        if (params.containsKey(Constantes.CONTAINSKEY_ORDER)) {
            String campo = (String) params.get(Constantes.CONTAINSKEY_ORDER);
            if (params.get(Constantes.CONTAINSKEY_SORT).equals(Constantes.CONTAINSKEY_DESC)) {
                criteria.addOrder(Order.desc(campo));
            } else {
                criteria.addOrder(Order.asc(campo));
            }
        }

        if (!params.containsKey(Constantes.CONTAINSKEY_REPORTE)) {
            criteria.setFirstResult((Integer) params.get(Constantes.CONTAINSKEY_OFFSET));
            criteria.setMaxResults((Integer) params.get(Constantes.CONTAINSKEY_MAX));
        }
        
         if(params.get("empresa") != null){
            log.debug("Se encontro una empresa");
            params.put(Constantes.PROYECTO_COLPORTOR_LIST, criteria.list());
            countCriteria.setProjection(Projections.rowCount());
            params.put(Constantes.CONTAINSKEY_CANTIDAD, (Long) countCriteria.list().get(0));
         }else{
             log.debug("No se encontro una empresa");
            params.put(Constantes.PROYECTO_COLPORTOR_LIST, new ArrayList());
            params.put(Constantes.CONTAINSKEY_CANTIDAD, 0L);
         }
       
         log.debug("Lista de proyectos {}", params.get(Constantes.PROYECTO_COLPORTOR_LIST));

        return params;
    }

    @Override
    public ProyectoColportor obtiene(Long id) {
        log.debug("Obtiene proyecto con id = {}", id);
        ProyectoColportor proyectoColportor = (ProyectoColportor) currentSession().get(ProyectoColportor.class, id);
        return proyectoColportor;
    }

    @Override
    public ProyectoColportor obtiene(Usuario user, Long id) {
        log.debug("Obtiene proyecto con id = {}", id);
        Criteria sql = getSession().createCriteria(ProyectoColportor.class);
        sql.createCriteria("usuario")
                .add(Restrictions.eq("id", user.getId()));
        sql.add(Restrictions.idEq(id));
        return (ProyectoColportor)sql.uniqueResult();
    }
    
    @Override
    @Transactional(readOnly = true)
    public ProyectoColportor obtiene(String codigo) {
        ProyectoColportor proyecto = (ProyectoColportor) currentSession()
                .createCriteria(ProyectoColportor.class)
                .add(Restrictions.eq("codigo", codigo))
                .uniqueResult();
        return proyecto;
    }

    @Override
    public ProyectoColportor crea(ProyectoColportor proyectoColportor) {
        log.debug("Creando proyecto : {}", proyectoColportor);
        
        //Generar nueva llave
        try{
            if(proyectoColportor.getId() == null){
                log.debug("Intentando generar la llave del proyecto");
                StringBuilder codigo = new StringBuilder("0");
                Calendar cal = GregorianCalendar.getInstance(local);
                sdf = new SimpleDateFormat ("YY", local); //Ultimos digitos del year
                codigo.append(sdf.format(cal.getTime()));
                log.debug("Se determino el anno {}", sdf.format(cal.getTime()));

                //Obtener todos los proyectos
                Map<String, Object> params = new HashMap<>();
                params.put("empresa", proyectoColportor.getEmpresa().getId());
                params.put("usuario", proyectoColportor.getUsuario().getId());
                params.put("reporte", "reporte");

                codigo.append("/");
                Integer proys = ((List<ProyectoColportor>)this.lista(params).get(Constantes.PROYECTO_COLPORTOR_LIST)).size();

                proys++;
                
                log.debug("#proyectos registrados {}", proys);

                StringBuilder str = new StringBuilder();
                //Se estandariza este numero a 5 posiciones
                for(int i = 5; i > proys.toString().length(); i--){
                    str.append("0");
                }
                str.append(proys);

                codigo.append(str.toString());
                log.debug("Se determino el #proyecto {}", str.toString());

                proyectoColportor.setCodigo(codigo.toString());
            }
        }catch(Exception e){
            log.error("Error al intentar generar la llave del proyecto");
        }
        
        try{
            currentSession().saveOrUpdate(proyectoColportor);
            currentSession().merge(proyectoColportor);
            currentSession().flush();
        }catch(Exception e){
            log.error("Error al intentar crear/actualizar el proyecto {}",e);
            currentSession().merge(proyectoColportor);
            
        }
        log.debug("Proyecto creado : {}", proyectoColportor);
        return proyectoColportor;
    }

    @Override
    public String elimina(Usuario user, Long id) {
        log.debug("Eliminando ProyectoColportor con id {}", id);
        ProyectoColportor proyectoColportor = obtiene(user, id);
        String nombre = proyectoColportor.getNombre();
        currentSession().delete(proyectoColportor);
        currentSession().flush();
        SimpleDateFormat sdf = new SimpleDateFormat ("MMM", local);
        return "Proyecto "+nombre;
    }
    
}
