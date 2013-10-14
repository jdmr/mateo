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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
import mx.edu.um.mateo.general.utils.UtilControllerTests;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

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
    @Autowired
    protected UtilControllerTests utils;

    protected void pagina(Map<String, Object> params, Model modelo,
            String lista, Long pagina) {
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
        List<Long> paginas = paginacion(pagina, cantidad, max);
        List<?> listado = null;
        try {
            listado = (List<?>) params.get(lista);
        } catch (Exception e) {
            //Si la lista no existe...
            listado = new ArrayList<>();
        }
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (listado.size() - 1);
        String[] paginacion = new String[]{primero.toString(),
            ultimo.toString(), cantidad.toString()};
        modelo.addAttribute("paginacion", paginacion);
        modelo.addAttribute("paginas", paginas);
        // termina paginado
    }

    private List<Long> paginacion(Long pagina, Long cantidad, Integer max) {
        Long cantidadDePaginas = cantidad / max;
        if (cantidad % max > 0) {
            cantidadDePaginas++;
        }
        log.debug("Paginacion: {} {} {} {}", new Object[]{pagina, cantidad,
            max, cantidadDePaginas});
        Set<Long> paginas = new LinkedHashSet<>();
        long h = pagina - 1;
        long i = pagina;
        long j = pagina + 1;
        boolean esMiles = false;
        boolean esQuinientos = false;
        boolean esCientos = false;
        boolean esCincuentas = false;
        boolean esDecenas = false;
        boolean iniciado = false;
        if (h > 0 && h > 1000) {
            for (long y = 0; y < h; y += 1000) {
                if (y == 0) {
                    iniciado = true;
                    paginas.add(1l);
                } else {
                    paginas.add(y);
                }
            }
        } else if (h > 0 && h > 500) {
            for (long y = 0; y < h; y += 500) {
                if (y == 0) {
                    iniciado = true;
                    paginas.add(1l);
                } else {
                    paginas.add(y);
                }
            }
        } else if (h > 0 && h > 100) {
            for (long y = 0; y < h; y += 100) {
                if (y == 0) {
                    iniciado = true;
                    paginas.add(1l);
                } else {
                    paginas.add(y);
                }
            }
        } else if (h > 0 && h > 50) {
            for (long y = 0; y < h; y += 50) {
                if (y == 0) {
                    iniciado = true;
                    paginas.add(1l);
                } else {
                    paginas.add(y);
                }
            }
        } else if (h > 0 && h > 10) {
            for (long y = 0; y < h; y += 10) {
                if (y == 0) {
                    iniciado = true;
                    paginas.add(1l);
                } else {
                    paginas.add(y);
                }
            }
        }
        if (i > 1 && i < 4) {
            for (long x = 1; x < i; x++) {
                paginas.add(x);
            }
        } else if (h > 0) {
            if (!iniciado) {
                paginas.add(1L);
            }
            for (long x = h; x < i; x++) {
                paginas.add(x);
            }
        }
        do {
            paginas.add(i);
            if (i > j) {
                if (esMiles || (i + 1000) < cantidadDePaginas) {
                    esMiles = true;
                    i -= i % 1000;
                    i += 999;
                } else if (esQuinientos || (i + 500) < cantidadDePaginas) {
                    esQuinientos = true;
                    i -= i % 500;
                    i += 499;
                } else if (esCientos || (i + 100) < cantidadDePaginas) {
                    esCientos = true;
                    i -= i % 100;
                    i += 99;
                } else if (esCincuentas || (i + 50) < cantidadDePaginas) {
                    esCincuentas = true;
                    i -= i % 50;
                    i += 49;
                } else if (esDecenas || (i + 10) < cantidadDePaginas) {
                    esDecenas = true;
                    i -= i % 10;
                    i += 9;
                }
            }
        } while (i++ < cantidadDePaginas);
        if (cantidadDePaginas > 0) {
            paginas.add(cantidadDePaginas);
        }

        log.debug("Paginas {}: {}", pagina, paginas);
        return new ArrayList<>(paginas);
    }

    protected byte[] generaPdf(List<?> lista, String nombre, String tipo,
            Long id) throws JRException {
        log.debug("Generando PDF");
        Map<String, Object> params = new HashMap<>();
        JasperReport jasperReport = null;
        switch (tipo) {
            case Constantes.ADMIN:
                jasperReport = reporteDao.obtieneReporteAdministrativo(nombre);
                break;
            case Constantes.ORG:
                jasperReport = reporteDao.obtieneReportePorOrganizacion(nombre, id);
                break;
            case Constantes.EMP:
                jasperReport = reporteDao.obtieneReportePorEmpresa(nombre, id);
                break;
            case Constantes.ALM:
                jasperReport = reporteDao.obtieneReportePorAlmacen(nombre, id);
                break;
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                params, new JRBeanCollectionDataSource(lista));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    protected byte[] generaPdfSubreporte(List<?> lista, String nombre, String tipo,
            Long id) throws JRException {
        log.debug("Generando PDF");
        Map<String, Object> params = new HashMap<>();
        JasperReport jasperReport = null;
        JasperReport jasperReportSB = null;
        switch (tipo) {
            case Constantes.ADMIN:
                jasperReport = reporteDao.obtieneReporteAdministrativo(nombre);
                break;
            case Constantes.ORG:
                jasperReport = reporteDao.obtieneReportePorOrganizacion(nombre, id);
                break;
            case Constantes.EMP:
                jasperReport = reporteDao.obtieneReportePorEmpresa(nombre, id);
                jasperReportSB = reporteDao.obtieneReportePorEmpresa("contrareciboFacturasabajo", id);

                break;
            case Constantes.ALM:
                jasperReport = reporteDao.obtieneReportePorAlmacen(nombre, id);
                break;
        }

        log.debug("subreportarriba*****-*{}", jasperReportSB.toString());
//        params.put("subreportarriba", jasperReportSB);
        params.put("parameter1", jasperReportSB);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                params, new JRBeanCollectionDataSource(lista));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    protected byte[] generaCsv(List<?> lista, String nombre, String tipo,
            Long id) throws JRException {
        log.debug("Generando CSV");
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperReport jasperReport = null;
        switch (tipo) {
            case Constantes.ADMIN:
                jasperReport = reporteDao.obtieneReporteAdministrativo(nombre);
                break;
            case Constantes.ORG:
                jasperReport = reporteDao.obtieneReportePorOrganizacion(nombre, id);
                break;
            case Constantes.EMP:
                jasperReport = reporteDao.obtieneReportePorEmpresa(nombre, id);
                break;
            case Constantes.ALM:
                jasperReport = reporteDao.obtieneReportePorAlmacen(nombre, id);
                break;
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                params, new JRBeanCollectionDataSource(lista));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
                byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    protected byte[] generaXls(List<?> lista, String nombre, String tipo,
            Long id) throws JRException {
        log.debug("Generando XLS");
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperReport jasperReport = null;
        switch (tipo) {
            case Constantes.ADMIN:
                jasperReport = reporteDao.obtieneReporteAdministrativo(nombre);
                break;
            case Constantes.ORG:
                jasperReport = reporteDao.obtieneReportePorOrganizacion(nombre, id);
                break;
            case Constantes.EMP:
                jasperReport = reporteDao.obtieneReportePorEmpresa(nombre, id);
                break;
            case Constantes.ALM:
                jasperReport = reporteDao.obtieneReportePorAlmacen(nombre, id);
                break;
        }
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
                params, new JRBeanCollectionDataSource(lista));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
                byteArrayOutputStream);
        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,
                Boolean.FALSE);
        exporter.setParameter(
                JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                Boolean.TRUE);
        exporter.setParameter(
                JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,
                Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,
                Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS,
                Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,
                Boolean.FALSE);
        exporter.setParameter(
                JRXlsExporterParameter.PARAMETERS_OVERRIDE_REPORT_HINTS,
                Boolean.FALSE);
        exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,
                Boolean.TRUE);

        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    protected void generaReporte(String tipo, List<?> lista,
            HttpServletResponse response, String nombre, String tipoReporte,
            Long id) throws ReporteException {
        try {
            log.debug("Generando reporte {}", tipo);
            byte[] archivo = null;
            switch (tipo) {
                case "PDF":
                    archivo = generaPdfSubreporte(lista, nombre, tipoReporte, id);
                    response.setContentType("application/pdf");
                    response.addHeader("Content-Disposition",
                            "attachment; filename=" + nombre + ".pdf");
                    System.out.println("termina de generar pdf");
                    break;
                case "CSV":
                    archivo = generaCsv(lista, nombre, tipoReporte, id);
                    response.setContentType("text/csv");
                    response.addHeader("Content-Disposition",
                            "attachment; filename=" + nombre + ".csv");
                    break;
                case "XLS":
                    archivo = generaXls(lista, nombre, tipoReporte, id);
                    response.setContentType("application/vnd.ms-excel");
                    response.addHeader("Content-Disposition",
                            "attachment; filename=" + nombre + ".xls");
            }
            if (archivo != null) {
                response.setContentLength(archivo.length);
                try (BufferedOutputStream bos = new BufferedOutputStream(
                        response.getOutputStream())) {
                    bos.write(archivo);
                    bos.flush();
                }
            }
        } catch (JRException | IOException e) {
            throw new ReporteException("No se pudo generar el reporte", e);
        }

    }

    protected void enviaCorreo(String tipo, List<?> lista,
            HttpServletRequest request, String nombre, String tipoReporte,
            Long id) throws ReporteException {
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
            String titulo = messageSource.getMessage(nombre + ".reporte.label",
                    null, request.getLocale());
            helper.setSubject(messageSource.getMessage(
                    "envia.correo.titulo.message", new String[]{titulo},
                    request.getLocale()));
            helper.setText(messageSource.getMessage(
                    "envia.correo.contenido.message", new String[]{titulo},
                    request.getLocale()), true);
            helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(
                    archivo, tipoContenido));
            mailSender.send(message);
        } catch (JRException | MessagingException e) {
            throw new ReporteException("No se pudo generar el reporte", e);
        }
    }

    protected Map<String, Object> convierteParams(Map<String, String[]> mapa) {
        Map<String, Object> params = new HashMap<>();
        for (String key : mapa.keySet()) {
            String[] values = mapa.get(key);
            log.debug("Convirtiendo {} : {}", key, values);
            if (values.length == 1) {
                if (StringUtils.isNotBlank(values[0])) {
                    if (key.equals("pagina")) {
                        params.put(key, new Long(values[0]));
                    } else if (key.equals("cuentaId")) {
                        params.put(key, values[0]);
                    } else if (key.endsWith("Ids")) {
                        boolean tieneComas = false;
                        List<Long> ids = new ArrayList<>();
                        for (String x : values) {
                            String[] y = StringUtils.split(x, ", ");
                            for (String z : y) {
                                ids.add(new Long(z.trim()));
                                tieneComas = true;
                            }
                            if (!tieneComas) {
                                ids.add(new Long(x.trim()));
                            }
                        }
                        params.put(key, ids);
                    } else if (key.endsWith("Id")) {
                        params.put(key, new Long(values[0]));
                    } else {
                        params.put(key, values[0]);
                    }
                }
            } else if (values.length > 1) {
                if (key.endsWith("Ids")) {
                    boolean tieneComas = false;
                    List<Long> ids = new ArrayList<>();
                    for (String x : values) {
                        String[] y = StringUtils.split(x, ", ");
                        for (String z : y) {
                            ids.add(new Long(z.trim()));
                            tieneComas = true;
                        }
                        if (!tieneComas) {
                            ids.add(new Long(x.trim()));
                        }
                    }
                    params.put(key, ids);
                } else {
                    params.put(key, values);
                }
            }
        }
        return params;
    }

    public void despliegaBindingResultErrors(BindingResult bindingResult) {
        List<ObjectError> errores = bindingResult.getAllErrors();
        for (ObjectError err : errores) {
            log.error("{}", err);

        }
    }
}
