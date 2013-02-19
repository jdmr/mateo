/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

/**
 *
 * @author zorch
 */
public class AFEconvenio implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne(optional=false)
    private Alumno alumno;
    @ManyToOne(optional=false)
    //private TipoBeca tipoBeca;
    @Column(name="codigo_personal", length=7)
    protected String matricula;
    @Column(nullable=false, length=40, insertable=false, updatable=false)
    protected String nombre;
    @Column(name="apellido_paterno", length=40, nullable=false, insertable=false, updatable=false)
    protected String apPaterno;
    @Column(name="apellido_materno", length=40, nullable=false, insertable=false, updatable=false)
    protected String apMaterno;
    
}
