/*
 * The MIT License
 *
 * Copyright 2012 J. David Mendoza <jdmendoza@um.edu.mx>.
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
package mx.edu.um.mateo.general.utils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Component
public class ReporteUtil {

	private static final Logger log = LoggerFactory
			.getLogger(ReporteUtil.class);

	public byte[] generaPdf(List<?> lista, String reporte) throws JRException {
		log.debug("Generando PDF");
		Map<String, Object> params = new HashMap<>();
		JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream(
				reporte));
		JasperReport jasperReport = JasperCompileManager.compileReport(jd);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
				params, new JRBeanCollectionDataSource(lista));
		byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

		return archivo;
	}

	public byte[] generaCsv(List<?> lista, String reporte) throws JRException {
		log.debug("Generando CSV");
		Map<String, Object> params = new HashMap<>();
		JRCsvExporter exporter = new JRCsvExporter();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream(
				reporte));
		JasperReport jasperReport = JasperCompileManager.compileReport(jd);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,
				params, new JRBeanCollectionDataSource(lista));
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
				byteArrayOutputStream);
		exporter.exportReport();
		byte[] archivo = byteArrayOutputStream.toByteArray();

		return archivo;
	}

	public byte[] generaXls(List<?> lista, String reporte) throws JRException {
		log.debug("Generando XLS");
		Map<String, Object> params = new HashMap<>();
		JRXlsExporter exporter = new JRXlsExporter();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream(
				reporte));
		JasperReport jasperReport = JasperCompileManager.compileReport(jd);
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
		exporter.exportReport();
		byte[] archivo = byteArrayOutputStream.toByteArray();

		return archivo;
	}
}
