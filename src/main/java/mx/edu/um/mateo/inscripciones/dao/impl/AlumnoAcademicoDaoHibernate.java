///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package mx.edu.um.mateo.inscripciones.dao.impl;
//
//import javax.sql.DataSource;
//import mx.edu.um.mateo.general.dao.BaseDao;
//import mx.edu.um.mateo.inscripciones.AlumnoAcademico;
//import mx.edu.um.mateo.inscripciones.dao.AlumnoAcademicoDao;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.transaction.annotation.Transactional;
//
///**
// *
// * @author zorch
// */
//public class AlumnoAcademicoDaoHibernate extends BaseDao implements AlumnoAcademicoDao{
//     
//    @Qualifier("dataSource2")
//    private DataSource dataSource2;
//    
//    @Override
//    @Transactional(readOnly = true)
//    public AlumnoAcademico obtiene(Long id) {
//        AlumnoAcademico  alumno = (AlumnoAcademico) currentSession().get(AlumnoAcademico.class, id);
//        return alumno;
//    }
//    
//}
