/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import javax.sql.DataSource;
import mx.edu.um.mateo.general.dao.BaseDao;
import mx.edu.um.mateo.inscripciones.model.Alumno;
import mx.edu.um.mateo.inscripciones.dao.AlumnoDao;
import org.springframework.transaction.annotation.Transactional;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.inscripciones.model.AlumnoAcademico;
import mx.edu.um.mateo.inscripciones.model.Modalidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 *
 * @author zorch
 */
@Transactional
@Repository
public class AlumnoDaoHibernate extends BaseDao implements AlumnoDao{
    
    @Autowired
    @Qualifier("dataSource2")
    private DataSource dataSource2;
    
    
    @Override
    @Transactional(readOnly=true)
    public Map<String, Object> lista(Map<String, Object> params) {
        
        //Trallendo de la base de datos.
        log.debug("Listado de Alumnos");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        java.util.List listaAlumn= new ArrayList();
        try {
            conn = dataSource2.getConnection();
            stmt = conn.prepareStatement("select ap.NOMBRE,ap.CODIGO_PERSONAL, ap.APELLIDO_PATERNO, ap.APELLIDO_MATERNO, aa.TIPO_ALUMNO, aa.MODALIDAD_ID, m.nombre_modalidad ,m.MODALIDAD_ID, m.ENLINEA " +
"from enoc.alum_personal ap, enoc.alum_academico aa, enoc.cat_modalidad m where ap.CODIGO_PERSONAL=aa.CODIGO_PERSONAL and aa.MODALIDAD_ID=m.MODALIDAD_ID " +
"and (ap.CODIGO_PERSONAL like ? or upper(ap.NOMBRE) like ? or upper(ap.APELLIDO_PATERNO) like ? or upper(ap.APELLIDO_MATERNO) like ?)");
            stmt.setString(1, ((String)params.get("filtro")).toUpperCase());
            stmt.setString(2, ((String)params.get("filtro")).toUpperCase());
            stmt.setString(3, ((String)params.get("filtro")).toUpperCase());
            stmt.setString(4, ((String)params.get("filtro")).toUpperCase());
            rs = stmt.executeQuery();
            while (rs.next()) {
                String nombre = rs.getString("NOMBRE");
                String apellido_materno = rs.getString("apellido_materno");
                String apellido_paterno = rs.getString("apellido_paterno");
                String codigo_personal = rs.getString("codigo_personal");
                Integer tipo_alumno = rs.getInt("tipo_alumno");
                Integer modalidad_id = rs.getInt("modalidad_id");
                String nombreModalidad= rs.getString("nombre_modalidad");
                String enLinea= rs.getString("enLinea");
                Modalidad modalidad= new Modalidad(modalidad_id , nombreModalidad, enLinea);
                AlumnoAcademico alumnoAcademico= new AlumnoAcademico(codigo_personal,modalidad,tipo_alumno);
                Alumno alumno = new Alumno(codigo_personal,nombre,apellido_paterno, apellido_materno, alumnoAcademico);
                listaAlumn.add(alumno);
                
            }}catch(Exception e){
                log.error("{}", e);
                
            }finally{
            try {
                stmt.close();
                rs.close();
                conn.close();
            } catch (SQLException ex) {
                log.error("{}", ex);
                stmt=null;
                conn=null;
                rs=null;
            }
            
        }
        
        //Haciendo el metodo lista despues de trabajo de traer de la base de datos
//        
//        log.debug("Buscando lista de Alumnos con params {}", params);
//        if (params == null) {
//            params = new HashMap<>();
//        }
//
//        if (!params.containsKey("max")) {
//            params.put("max", 10);
//        } else {
//            params.put("max", Math.min((Integer) params.get("max"), 100));
//        }
//
//        if (params.containsKey("pagina")) {
//            Long pagina = (Long) params.get("pagina");
//            Long offset = (pagina - 1) * (Integer) params.get("max");
//            params.put("offset", offset.intValue());
//        }
//
//        if (!params.containsKey("offset")) {
//            params.put("offset", 0);
//        }
//        Criteria criteria = currentSession().createCriteria(Alumno.class);
//        Criteria countCriteria = currentSession().createCriteria(Alumno.class);
//
//       if (params.containsKey("filtro")) {
//            String filtro = (String) params.get("filtro");
//            Disjunction propiedades = Restrictions.disjunction();
//            propiedades.add(Restrictions.ilike("apellido_paterno", filtro,
//                    MatchMode.ANYWHERE));
//            propiedades.add(Restrictions.ilike("nombre", filtro,
//                    MatchMode.ANYWHERE));
//            propiedades.add(Restrictions.ilike("codigo_personal", filtro,
//                    MatchMode.ANYWHERE));
//            propiedades.add(Restrictions.ilike("apellido_materno", filtro,
//                    MatchMode.ANYWHERE));
//            criteria.add(propiedades);
//            countCriteria.add(propiedades);
//        }
//
//        if (params.containsKey("order")) {
//            String campo = (String) params.get("order");
//            if (params.get("sort").equals("desc")) {
//                criteria.addOrder(Order.desc(campo));
//            } else {
//                criteria.addOrder(Order.asc(campo));
//            }
//        }
//
//        if (!params.containsKey("reporte")) {
//            criteria.setFirstResult((Integer) params.get("offset"));
//            criteria.setMaxResults((Integer) params.get("max"));
//        }
        params.put( Constantes.ALUMNO_LIST , listaAlumn);
//
//        countCriteria.setProjection(Projections.rowCount());
//        params.put("cantidad", (Long) countCriteria.list().get(0));

        return params;
    }

   @Override
    @Transactional(readOnly = true)
    public Alumno obtiene(String matricula) {
       log.debug("Obteniendo un alumno");
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Alumno alumno= new Alumno();
        try {
            conn = dataSource2.getConnection();
            stmt = conn.prepareStatement("select ap.NOMBRE,ap.CODIGO_PERSONAL, ap.APELLIDO_PATERNO, ap.APELLIDO_MATERNO, aa.TIPO_ALUMNO, aa.MODALIDAD_ID, m.nombre_modalidad ,m.MODALIDAD_ID, m.ENLINEA \n" +
"from enoc.alum_personal ap, enoc.alum_academico aa, enoc.cat_modalidad m where ap.CODIGO_PERSONAL=aa.CODIGO_PERSONAL and aa.MODALIDAD_ID=m.MODALIDAD_ID \n" +
"and (ap.CODIGO_PERSONAL =  ? )");
            stmt.setString(1, matricula);
            log.debug("Provando conexion");
            rs = stmt.executeQuery();
            log.debug("Ejecutando Query");
            if(rs.next()) {
                log.debug("Entrando al Metodo");
                String nombre = rs.getString("NOMBRE");
                String apellido_materno = rs.getString("apellido_materno");
                String apellido_paterno = rs.getString("apellido_paterno");
                String codigo_personal = rs.getString("codigo_personal");
                Integer tipo_alumno = rs.getInt("tipo_alumno");
                Integer modalidad_id = rs.getInt("modalidad_id");
                String nombreModalidad= rs.getString("nombre_modalidad");
                String enLinea= rs.getString("enLinea");
                Modalidad modalidad= new Modalidad(modalidad_id , nombreModalidad, enLinea);
                AlumnoAcademico alumnoAcademico= new AlumnoAcademico(codigo_personal,modalidad,tipo_alumno);
                alumno = new Alumno(codigo_personal,nombre,apellido_paterno, apellido_materno, alumnoAcademico);
                
                
            }}catch(Exception e){
                
                log.error("{}",e);
            }finally{
            try {
                stmt.close();
                rs.close();
                conn.close();
            } catch (SQLException ex) {
                log.error("{}", ex);
                stmt=null;
                conn=null;
                rs=null;
            }
            
        }
        return alumno;
    }
  
   
}
