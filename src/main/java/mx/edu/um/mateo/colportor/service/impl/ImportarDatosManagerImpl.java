/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import mx.edu.um.mateo.colportor.service.ImportarDatosManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.service.BaseManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.stereotype.Service;

/**
 * 
 * @author osoto
 */
@Service
public class ImportarDatosManagerImpl extends BaseManager implements ImportarDatosManager{
    public void importaInformeDeGema(File file, Usuario user) throws Exception{
        log.debug("importaInformeDeGema");
        try{
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(file));
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheetAt(0); 
            HSSFRow row;
            HSSFCell cell;

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
            
            Boolean sw = false;
            
            for (int r = 1; r < rows; r++) {
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
                                    }else{
                                        log.debug("String {}",cell.getStringCellValue());
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
                    
                }
            }
        } catch (IOException ioe) {
            log.error("Error al intentar abrir el archivo");
        } catch (Exception e){
            e.printStackTrace();
            log.error("Error al intentar generar los registros de ss");
        }
    }
}
