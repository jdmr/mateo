/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import mx.edu.um.mateo.general.model.Empresa;

/**
 *
 * @author osoto
 */
@Entity
@Table(name="tipo_empleado", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"prefijo"})}))
public class TipoEmpleado {
    private static final long serialVersionUID = 6001011125338853446L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Integer version;
    @ManyToOne
    private Empresa empresa;
    @Column(nullable=false,unique=true)
    private String prefijo;
    @Column(nullable=false)
    private String descripcion;
    
}
