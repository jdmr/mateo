/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.general.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.um.mateo.general.dao.ReporteDao;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.utils.ReporteUtil;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.Model;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public abstract class BaseController {

    protected final transient Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    protected JavaMailSender mailSender;
    @Autowired
    protected ResourceBundleMessageSource messageSource;
    @Autowired
    protected Ambiente ambiente;
    @Autowired
    protected ReporteUtil reporteUtil;
    @Autowired
    protected ReporteDao reporteDao;

    protected void pagina(Map<String, Object> params, Model modelo, String lista, Long pagina) {
        if (pagina != null) {
            params.put("pagina", pagina);
            modelo.addAttribute("pagina", pagina);
        } else {
            pagina = 1L;
            modelo.addAttribute("pagina", pagina);
        }
        // inicia paginado
        Long cantidad = (Long) params.get("cantidad");
        Integer max = (Integer) params.get("max");
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
            if (i >= 10) {
                break;
            }
        } while (i++ <= cantidadDePaginas);
        List listado = (List) params.get(lista);
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (listado.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute("paginacion", paginacion);
        modelo.addAttribute("paginas", paginas);
        // termina paginado
    }

    protected byte[] generaPdf(List lista, String nombre, String tipo, Long id) throws JRException {
        log.debug("Generando PDF");
        Map<String, Object> params = new HashMap<>();
        JasperReport jasperReport = null;
        switch(tipo) {
            case Constantes.ADMIN : 
                jasperReport = reporteDao.obtieneReporteAdministrativo(nombre);
                break;
            case Constantes.ORG : 
                jasperReport = reporteDao.obtieneReportePorOrganizacion(nombre, id);
                break;
            case Constantes.EMP : 
                jasperReport = reporteDao.obtieneReportePorEmpresa(nombre, id);
                break;
            case Constantes.ALM : 
                jasperReport = reporteDao.obtieneReportePorAlmacen(nombre, id);
                break;
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(lista));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    protected byte[] generaCsv(List lista, String nombre, String tipo, Long id) throws JRException {
        log.debug("Generando CSV");
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperReport jasperReport = null;
        switch(tipo) {
            case Constantes.ADMIN : 
                jasperReport = reporteDao.obtieneReporteAdministrativo(nombre);
                break;
            case Constantes.ORG : 
                jasperReport = reporteDao.obtieneReportePorOrganizacion(nombre, id);
                break;
            case Constantes.EMP : 
                jasperReport = reporteDao.obtieneReportePorEmpresa(nombre, id);
                break;
            case Constantes.ALM : 
                jasperReport = reporteDao.obtieneReportePorAlmacen(nombre, id);
                break;
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(lista));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    protected byte[] generaXls(List lista, String nombre, String tipo, Long id) throws JRException {
        log.debug("Generando XLS");
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperReport jasperReport = null;
        switch(tipo) {
            case Constantes.ADMIN : 
                jasperReport = reporteDao.obtieneReporteAdministrativo(nombre);
                break;
            case Constantes.ORG : 
                jasperReport = reporteDao.obtieneReportePorOrganizacion(nombre, id);
                break;
            case Constantes.EMP : 
                jasperReport = reporteDao.obtieneReportePorEmpresa(nombre, id);
                break;
            case Constantes.ALM : 
                jasperReport = reporteDao.obtieneReportePorAlmacen(nombre, id);
                break;
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(lista));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    protected void generaReporte(String tipo, List lista, HttpServletResponse response, String nombre, String tipoReporte, Long id) throws ReporteException {
        try {
            log.debug("Generando reporte {}", tipo);
            byte[] archivo = null;
            switch (tipo) {
                case "PDF":
                    archivo = generaPdf(lista, nombre, tipoReporte, id);
                    response.setContentType("application/pdf");
                    response.addHeader("Content-Disposition", "attachment; filename=" + nombre + ".pdf");
                    System.out.println("termina de generar pdf");
                    break;
                case "CSV":
                    archivo = generaCsv(lista, nombre, tipoReporte, id);
                    response.setContentType("text/csv");
                    response.addHeader("Content-Disposition", "attachment; filename=" + nombre + ".csv");
                    break;
                case "XLS":
                    archivo = generaXls(lista, nombre, tipoReporte, id);
                    response.setContentType("application/vnd.ms-excel");
                    response.addHeader("Content-Disposition", "attachment; filename=" + nombre + ".xls");
            }
            if (archivo != null) {
                response.setContentLength(archivo.length);
                try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                    bos.write(archivo);
                    bos.flush();
                }
            }
        } catch (JRException | IOException e) {
            throw new ReporteException("No se pudo generar el reporte", e);
        }

    }

    protected void enviaCorreo(String tipo, List lista, HttpServletRequest request, String nombre, String tipoReporte, Long id) throws ReporteException {
        try {
            log.debug("Enviando correo {}", tipo);
            byte[] archivo = null;
            String tipoContenido = null;
            switch (tipo) {
                case "PDF":
                    archivo = generaPdf(lista, nombre, tipoReporte, id);
                    tipoContenido = "application/pdf";
                    break;
                case "CSV":
                    archivo = generaCsv(lista, nombre, tipoReporte, id);
                    tipoContenido = "text/csv";
                    break;
                case "XLS":
                    archivo = generaXls(lista, nombre, tipoReporte, id);
                    tipoContenido = "application/vnd.ms-excel";
            }

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(ambiente.obtieneUsuario().getCorreo());
            String titulo = messageSource.getMessage(nombre +".reporte.label", null, request.getLocale());
            helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
            helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
            helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
            mailSender.send(message);
        } catch (JRException | MessagingException e) {
            throw new ReporteException("No se pudo generar el reporte", e);
        }
    }
    
    protected Map<String, Object> convierteParams(Map<String, String[]> mapa) {
        Map<String, Object> params = new HashMap<>();
        for(String key : mapa.keySet()) {
            String[] values = mapa.get(key);
            log.debug("Convirtiendo {} : {}", key, values);
            if (values.length == 1) {
                if (StringUtils.isNotBlank(values[0])) {
                    if (key.equals("pagina")) {
                        params.put(key, new Long(values[0]));
                    } else {
                        params.put(key, values[0]);
                    }
                }
            } else if (values.length > 1) {
                params.put(key, values);
            }
        }
        return params;
    }
}
