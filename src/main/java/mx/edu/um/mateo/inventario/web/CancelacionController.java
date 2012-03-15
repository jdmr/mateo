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
package mx.edu.um.mateo.inventario.web;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inventario.dao.CancelacionDao;
import mx.edu.um.mateo.inventario.model.Cancelacion;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/inventario/cancelacion")
public class CancelacionController extends BaseController {

    @Autowired
    private CancelacionDao cancelacionDao;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Cancelacion cancelacion,
            Errors errors,
            Model modelo) {
        log.debug("Mostrando lista de tipos de salidas");
        Map<String, Object> params = new HashMap<>();
        params.put("almacen", request.getSession().getAttribute("almacenId"));
        if (StringUtils.isNotBlank(filtro)) {
            params.put("filtro", filtro);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put("order", order);
            params.put("sort", sort);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = cancelacionDao.lista(params);
            try {
                generaReporte(tipo, (List<Cancelacion>) params.get("cancelaciones"), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove("reporte");
                errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = cancelacionDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Cancelacion>) params.get("cancelaciones"), request);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("cancelacion.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = cancelacionDao.lista(params);
        modelo.addAttribute("cancelaciones", params.get("cancelaciones"));

        this.pagina(params, modelo, "cancelaciones", pagina);

        return "inventario/cancelacion/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando cancelacion {}", id);
        Cancelacion cancelacion = cancelacionDao.obtiene(id);
        modelo.addAttribute("cancelacion", cancelacion);
        return "inventario/cancelacion/ver";
    }

    private void generaReporte(String tipo, List<Cancelacion> cancelaciones, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = reporteUtil.generaPdf(cancelaciones, "/mx/edu/um/mateo/inventario/reportes/cancelaciones.jrxml");
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=cancelaciones.pdf");
                break;
            case "CSV":
                archivo = reporteUtil.generaCsv(cancelaciones, "/mx/edu/um/mateo/inventario/reportes/cancelaciones.jrxml");
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=cancelaciones.csv");
                break;
            case "XLS":
                archivo = reporteUtil.generaXls(cancelaciones, "/mx/edu/um/mateo/inventario/reportes/cancelaciones.jrxml");
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=cancelaciones.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<Cancelacion> cancelaciones, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = reporteUtil.generaPdf(cancelaciones, "/mx/edu/um/mateo/inventario/reportes/cancelaciones.jrxml");
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = reporteUtil.generaCsv(cancelaciones, "/mx/edu/um/mateo/inventario/reportes/cancelaciones.jrxml");
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = reporteUtil.generaXls(cancelaciones, "/mx/edu/um/mateo/inventario/reportes/cancelaciones.jrxml");
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("cancelacion.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }
}
