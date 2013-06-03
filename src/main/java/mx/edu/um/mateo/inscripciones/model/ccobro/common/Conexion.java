package mx.edu.um.mateo.inscripciones.model.ccobro.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Autor: Luis P?rez
 * Organizaci?n: Sistemas
 * Creado: Martes, 22 de Julio de 2003 10:32:43 a.m.
 * Descripci?n: 
 */


public class Conexion{
    
    private static final Logger log = LoggerFactory.getLogger(mx.edu.um.mateo.inscripciones.model.ccobro.common.Conexion.class);
    private DataSource ds = null;
	public Connection conn;
	private Boolean transaccion;
	
	public Conexion(){
	}
	
	public Connection getConexionMateo(Boolean transaccion) throws Exception
	{
		
		this.transaccion=transaccion;
		try {
			log.debug("Entrandoo a Conexion arriba");
			Context initContext = new InitialContext();
			DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/conn_mateo");
			conn = ds.getConnection();
			
			conn.setAutoCommit(!transaccion.booleanValue());
		} catch (NamingException e) {
			//System.out.println("No se encontro el Datasource "+e);
            /**
             * TODO Quitar esta linea
             */
                    log.debug("{}", e);
                    log.debug("Entrandoo a Conexion abajo");
                    log.debug("{}", conn);
            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.16.254.20:1521:ora1", "mateo", "jgrjwjiewm");
            log.debug("{}", conn);
		}catch(Exception e2)
		{
			//System.out.println("Error al intentar conectarse a la BD "+e2);
		}	
		
		return conn;
	}
	
	public Connection getConexionAron(Boolean transaccion) throws Exception
	{
		
		this.transaccion=transaccion;
		try {
                        log.debug("Entrandoo a Conexion arriba");
			Context initContext = new InitialContext();
			//System.out.println("Tratando de conectarse.");
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			//System.out.println("Tratando de conectarse..");
			DataSource ds = (DataSource)envContext.lookup("jdbc/conn_aron");
			//System.out.println("Tratando de conectarse...");
			conn = ds.getConnection();
			//System.out.println("Obteniendo la conexion...");
			conn.setAutoCommit(!transaccion.booleanValue());
			//System.out.println("Asignando autocommit...");
		} catch (NamingException e) {
                    log.debug("{}", e);
                    log.debug("Entrandoo a Conexion abajo");
                    log.debug("{}", conn);
            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.16.254.20:1521:ora1", "mateo", "jgrjwjiewm");
            log.debug("{}", conn);
			//System.out.println("No se encontro el Datasource "+e);
		}catch(Exception e2)
		{
			//System.out.println("Error al intentar conectarse a la BD "+e2);
		}	
		
		return conn;
	}
	
	public Connection getConexionNoe(Boolean transaccion) throws Exception
	{
		log.debug("Entrandoo a Conexion arriba");
		this.transaccion=transaccion;
		try {
			
			Context initContext = new InitialContext();
			
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			
			DataSource ds = (DataSource)envContext.lookup("jdbc/conn_noe");
			
			conn = ds.getConnection();
			
			conn.setAutoCommit(!transaccion.booleanValue());
		} catch (NamingException e) {
                   log.debug("{}", e);
                    log.debug("Entrandoo a Conexion abajo");
                    log.debug("{}", conn);
            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.16.254.20:1521:ora1", "mateo", "jgrjwjiewm");
            log.debug("{}", conn);
			//System.out.println("No se encontro el Datasource "+e);
		}catch(Exception e2)
		{
			//System.out.println("Error al intentar conectarse a la BD "+e2);
		}
		
		return conn;
	}
	
	public Connection getConexionEnoc(Boolean transaccion) throws Exception
	{
		log.debug("Entrandoo a Conexion arriba");
		this.transaccion=transaccion;
		try {
			
			Context initContext = new InitialContext();
			
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			
			DataSource ds = (DataSource)envContext.lookup("jdbc/conn_enoc");
			
			conn = ds.getConnection();
			
			conn.setAutoCommit(!transaccion.booleanValue());
		} catch (NamingException e) {
                    log.debug("{}", e);
                    log.debug("Entrandoo a Conexion abajo");
                    log.debug("{}", conn);
            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.16.254.20:1521:ora1", "mateo", "jgrjwjiewm");
            log.debug("{}", conn);
			//System.out.println("No se encontro el Datasource "+e);
		}catch(Exception e2)
		{
			//System.out.println("Error al intentar conectarse a la BD "+e2);
		}
		
		return conn;
	}
	
	public Connection getConexionPedro(Boolean transaccion) throws Exception
	{
		log.debug("Entrandoo a Conexion arriba");
		this.transaccion=transaccion;
		try {
			
			Context initContext = new InitialContext();
			
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			
			DataSource ds = (DataSource)envContext.lookup("jdbc/conn_pedro");
			
			conn = ds.getConnection();
			
			conn.setAutoCommit(!transaccion.booleanValue());
		} catch (NamingException e) {
                    log.debug("{}", e);
                    log.debug("Entrandoo a Conexion abajo");
                    log.debug("{}", conn);
            conn = DriverManager.getConnection("jdbc:oracle:thin:@172.16.254.20:1521:ora1", "mateo", "jgrjwjiewm");
            log.debug("{}", conn);
            
			//System.out.println("No se encontro el Datasource "+e);
		}catch(Exception e2)
		{
			//System.out.println("Error al intentar conectarse a la BD "+e2);
		}
		
		return conn;
	}
	
	public void closeConexion()
	{
		try{
			if(transaccion.booleanValue())
				conn.commit();
			conn.close();
		}catch(SQLException e){
			//System.out.println("Error al cerrar la conexion");
		}
	}
	
	public void closeConexion(Boolean commit){
		try{
			if(transaccion.booleanValue())
				if(commit.booleanValue())
					conn.commit();
				else
					conn.rollback();
			
			conn.close();
		}catch(SQLException e){
			//System.out.println("Error al cerrar la conexion");
		}
	}
	
}
