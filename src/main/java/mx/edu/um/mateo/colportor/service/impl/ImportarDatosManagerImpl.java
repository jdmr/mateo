/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import mx.edu.um.mateo.colportor.dao.ColportorDao;
import mx.edu.um.mateo.colportor.dao.DocumentoDao;
import mx.edu.um.mateo.colportor.dao.TemporadaColportorDao;
import mx.edu.um.mateo.colportor.model.Colportor;
import mx.edu.um.mateo.colportor.model.Documento;
import mx.edu.um.mateo.colportor.service.ImportarDatosManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.service.BaseManager;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author osoto
 */
@Service
public class ImportarDatosManagerImpl extends BaseManager implements ImportarDatosManager{
    @Autowired
    private TemporadaColportorDao tmpClpDao;
    @Autowired
    private ColportorDao clpDao;
    @Autowired
    private DocumentoDao docDao;
    
    public void importaInformeDeGema(File file, Usuario user) throws NullPointerException, IOException, Exception{
        log.debug("importaInformeDeGema");
        try{
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = wb.getSheetAt(0); 
            XSSFRow row;
            XSSFCell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();
            //log.debug("Rows "+rows);

            int cols = 0; // No of columns
            int tmp = 0;

            //This trick ensures that we get the data properly even if it doesn't start from first few rows
            for (int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if (tmp > cols) {
                        cols = tmp;
                    }
                }
            }
            //log.debug("Cols "+cols);
            String clave = null;
            String nombre = null;
            BigDecimal boletin = null;
            BigDecimal boDcto = BigDecimal.ZERO;
            BigDecimal boNeto = BigDecimal.ZERO;
            BigDecimal premio = BigDecimal.ZERO;
            BigDecimal prDcto = BigDecimal.ZERO;
            BigDecimal prNeto = BigDecimal.ZERO;
            BigDecimal otros = BigDecimal.ZERO;
            BigDecimal otDcto = BigDecimal.ZERO;
            BigDecimal otNeto = BigDecimal.ZERO;
            BigDecimal total = BigDecimal.ZERO;
            BigDecimal ttDcto = BigDecimal.ZERO;
            BigDecimal ttNeto = BigDecimal.ZERO;
            
            Documento doc = null;
            Colportor clp = null;
            Boolean sw = false;
            
            //Obtener descripcion
            row = sheet.getRow(0);
            gcFecha.setTime(sdf.parse(row.getCell(0).getStringCellValue()));
            log.debug("Fecha {}", gcFecha.getTime());
            
            row = sheet.getRow(1);
            String descripcion = row.getCell(0).getStringCellValue();
            log.debug("Descripcion {}", descripcion);
            
            rowLoop:
            for (int r = 3; r < rows; r++) {
                row = sheet.getRow(r);
                if (row != null) {
                    sw =false; //Si no se leyo colportor alguno, entonces no se leen las demas celdas

                    for (int c = 0; c < cols; c++) {
                        cell = row.getCell(c);

                        if (cell != null) {
                            switch(c){
                                case 0:{
                                    if(cell.getCellType() == 0){
                                        log.debug("Numerico {}",cell.getNumericCellValue());
                                        clave = String.valueOf(cell.getNumericCellValue()).split("\\.")[0];
                                    }else{
                                        log.debug("String {}",cell.getStringCellValue());
                                        clave = cell.getStringCellValue();
                                    }
                                    log.debug("Clave clp {}", clave);
                                    try{
                                        Long.parseLong(clave);
                                    }catch(NumberFormatException e){
                                        //clave invalida
                                        break rowLoop;
                                    }
                                    break;
                                }
                                case 1:{
                                    log.debug("Nombre {}",cell.getStringCellValue());
                                    break;
                                }
                                case 2:{
                                    try {
                                        log.debug("boletin {}",cell.getNumericCellValue());
                                        boletin = new BigDecimal(String.valueOf(cell.getNumericCellValue()));                                        
                                    } catch (Exception e) {
                                        boletin = new BigDecimal("0");
                                    }
                                    break;
                                }
                                case 3:{
                                    try {
                                        log.debug("boDcto {}",cell.getNumericCellValue());
                                        boDcto = new BigDecimal(String.valueOf(cell.getNumericCellValue()));                                        
                                    } catch (Exception e) {
                                        boDcto = new BigDecimal("0");
                                    }
                                    break;
                                }
                                case 4:{
                                    try {
                                        log.debug("boNeto {}",cell.getNumericCellValue());
                                        boNeto = new BigDecimal(String.valueOf(cell.getNumericCellValue()));                                        
                                    } catch (Exception e) {
                                        boNeto = new BigDecimal("0");
                                    }
                                    break;
                                }
                                case 5:{
                                    try {
                                        log.debug("premio {}",cell.getNumericCellValue());
                                        premio = new BigDecimal(String.valueOf(cell.getNumericCellValue()));                                        
                                    } catch (Exception e) {
                                        premio = new BigDecimal("0");
                                    }
                                    break;
                                }
                                case 6:{
                                    try {
                                        log.debug("prDcto {}",cell.getNumericCellValue());
                                        prDcto = new BigDecimal(String.valueOf(cell.getNumericCellValue()));                                        
                                    } catch (Exception e) {
                                        prDcto = new BigDecimal("0");
                                    }
                                    break;
                                }
                                case 7:{
                                    try {
                                        log.debug("prNeto {}",cell.getNumericCellValue());
                                        prNeto = new BigDecimal(String.valueOf(cell.getNumericCellValue()));                                        
                                    } catch (Exception e) {
                                        prNeto = new BigDecimal("0");
                                    }
                                    break;
                                }
                                case 8:{
                                    try {
                                        log.debug("otros {}",cell.getNumericCellValue());
                                        otros = new BigDecimal(String.valueOf(cell.getNumericCellValue()));                                        
                                    } catch (Exception e) {
                                        otros = new BigDecimal("0");
                                    }
                                    break;
                                }
                                case 9:{
                                    try {
                                        log.debug("otDcto {}",cell.getNumericCellValue());
                                        otDcto = new BigDecimal(String.valueOf(cell.getNumericCellValue()));                                        
                                    } catch (Exception e) {
                                        otDcto = new BigDecimal("0");
                                    }
                                    break;
                                }
                                case 10:{
                                    try {
                                        log.debug("otNeto {}",cell.getNumericCellValue());
                                        otNeto = new BigDecimal(String.valueOf(cell.getNumericCellValue()));                                        
                                    } catch (Exception e) {
                                        otNeto = new BigDecimal("0");
                                    }
                                    break;
                                }
                            } //switch end
                            
                            
                        }
                    } //Finalizo de leer todas las celdas de una fila
                    
                    //Insertar boletin
                    doc = new Documento();
                    doc.setFecha(gcFecha.getTime());
                    doc.setFolio(clave);
                    doc.setImporte(boletin);
                    doc.setObservaciones(descripcion);
                    doc.setTipoDeDocumento("Boletin");

                    clp = clpDao.obtiene(clave);
                    
                    log.debug("Obtuvo colportor {}", clp);
                    doc.setTemporadaColportor(tmpClpDao.obtiene(clp));
                    log.debug("Obtuvo temporadaClp {}", doc.getTemporadaColportor());
                    docDao.crea(doc);
                }
            }
        } catch (IOException ioe) {
            log.error("Error al intentar abrir el archivo");
            throw ioe;
        } catch (Exception e){
            e.printStackTrace();
            log.error("Error al intentar generar los registros de colportores");
            throw e;
        }
        
    }
    public void importaDiezmos(File file, Usuario user) throws NullPointerException, IOException, Exception{
        log.debug("importaDiezmos");
        try{
            XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = wb.getSheetAt(0); 
            XSSFRow row;
            XSSFCell cell;

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();
            //log.debug("Rows "+rows);

            int cols = 0; // No of columns
            int tmp = 0;

            //This trick ensures that we get the data properly even if it doesn't start from first few rows
            for (int i = 0; i < 10 || i < rows; i++) {
                row = sheet.getRow(i);
                if (row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if (tmp > cols) {
                        cols = tmp;
                    }
                }
            }
            //log.debug("Cols "+cols);
            String fecha = null;
            String nombre = null;
            BigDecimal diezmo = null;
            
            Documento doc = null;
            Colportor clp = null;
            Boolean sw = false;
            
            Map <String, Object> params = new HashMap<>();
            params.put("empresa", user.getEmpresa().getId());
            NavigableMap <String, Colportor> mapa = clpDao.obtieneMapColportores(params);
            NavigableMap <String, Colportor> smapa = null;
            
            rowLoop:
            for (int r = 2; r < rows; r++) {
                row = sheet.getRow(r);
                if (row != null) {
                    sw =false; //Si no se leyo colportor alguno, entonces no se leen las demas celdas

                    for (int c = 0; c < cols; c++) {
                        cell = row.getCell(c);

                        if (cell != null) {
                            switch(c){
                                case 0:{
                                    log.debug("String {}",cell.getStringCellValue());
                                    sdf = new SimpleDateFormat ("dd-MMM-yy", local);
                                    try {
                                        gcFecha.setTime(sdf.parse(cell.getStringCellValue()));
                                    } catch (ParseException parseException) {
                                        continue rowLoop;
                                    }
                                                                        
                                    break;
                                }
                                case 3:{
                                    log.debug("Nombre {}",cell.getStringCellValue());
                                    nombre = cell.getStringCellValue();
                                    nombre = nombre.substring(15);
                                    log.debug("Nombre {}", nombre);
                                    String [] arr = nombre.split(",");
                                    for(String str : arr){
                                        log.debug("Nombre  {}",str);
                                        smapa = mapa.tailMap(str, true);
                                        if(smapa.size() == 1){
                                            clp = smapa.pollFirstEntry().getValue();
                                            log.debug("Colportor encontrado {}", clp);
                                            break;
                                        }
                                        if(mapa.size() == 0){
                                            clp = null;
                                            log.debug("No se encontro colportor ");
                                            break;
                                        }
                                    }
                                    log.debug("Nombre colortor {}",arr[0].split("\\s"));
                                    
                                    break;
                                }
                                case 4:{
                                    try {
                                        log.debug("diezmo {}",cell.getNumericCellValue());
                                        diezmo = new BigDecimal(String.valueOf(cell.getNumericCellValue()));                                        
                                    } catch (Exception e) {
                                        diezmo = new BigDecimal("0");
                                    }
                                    break;
                                }
                            } //switch end
                            
                            
                        }
                    } //Finalizo de leer todas las celdas de una fila
                    
                    //Insertar boletin
//                    doc = new Documento();
//                    doc.setFecha(gcFecha.getTime());
//                    doc.setFolio(clave);
//                    doc.setImporte(boletin);
//                    doc.setObservaciones(descripcion);
//                    doc.setTipoDeDocumento("Boletin");
//
//                    clp = clpDao.obtiene(clave);
//                    
//                    log.debug("Obtuvo colportor {}", clp);
//                    doc.setTemporadaColportor(tmpClpDao.obtiene(clp));
//                    log.debug("Obtuvo temporadaClp {}", doc.getTemporadaColportor());
//                    docDao.crea(doc);
                }
            }
        } catch (IOException ioe) {
            log.error("Error al intentar abrir el archivo");
            throw ioe;
        } catch (Exception e){
            e.printStackTrace();
            log.error("Error al intentar generar los registros de colportores");
            throw e;
        }
        
    }
}
