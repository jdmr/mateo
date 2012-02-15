/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.dao;

import java.util.Date;
import java.util.Set;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import mx.edu.um.mateo.rh.model.Empleado;
/**
 *
 * @author develop
 */
public class EmpleadoDao {
private static final long serialVersionUID = 1L;
private Long id;
private String clave;
private String nombre;
private String apPaterno;
private String apMaterno;
private String genero;
private Date fechaNacimiento;
private String direccion;
private String status;
private Integer version;
//private EmpleadoLaborales empleadoLaborales;
//private EmpleadoPersonales empleadoPersonales;
//private Nacionalidades nacionalidad;
private Set comunicaciones;
private Set puestos;
private Set dependientes;
private Set perDeds;
private Set regsPatronales;
private Set vacaciones;
private Set estudios;
    
@Repository
@Transactional
public class EmpresaDao {

    private static final Logger log = LoggerFactory.getLogger(mx.edu.um.mateo.rh.model.EmpleadoDao.class);
    @Autowired
    private SessionFactory sessionFactory;

    public EmpresaDao() {
        log.info("Nueva instancia de EmpleadoDao");
    }

    private Session currentSession() {
        return sessionFactory.getCurrentSession();
    }


}