///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package mx.edu.um.mateo.inscripciones.dao.impl;
//
//import java.util.HashMap;
//import java.util.Map;
//import mx.edu.um.mateo.inscripciones.dao.AlumnoDao;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.junit.Test;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
///**
// *
// * @author zorch
// */
//public class AlumnoAcademicoDaoHibernateTest {
//     private static final Logger log = LoggerFactory.getLogger(AlumnoDaoHibernateTest.class);
//    @Autowired
//    private AlumnoDao instance;
//    @Autowired
//    private SessionFactory sessionFactory;
//
//    private Session currentSession() {
//        return sessionFactory.getCurrentSession();
//    }
//    
//    @Test
//     log.debug("Obtiene un AlumnoAcademico");
//        String matricula= "1080506";
//        Map<String, Object> params = new HashMap<>();
//        params.put("filtro", matricula);
//        Map<String, Object> result = instance.lista(params);
//        assertEquals("1080506", result.get(matricula));
//}
