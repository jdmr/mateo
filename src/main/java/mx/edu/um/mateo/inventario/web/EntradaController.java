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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.dao.ProveedorDao;
import mx.edu.um.mateo.general.model.Proveedor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.LabelValueBean;
import mx.edu.um.mateo.general.utils.ReporteUtil;
import mx.edu.um.mateo.inventario.dao.EntradaDao;
import mx.edu.um.mateo.inventario.model.Entrada;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/inventario/entrada")
public class EntradaController {

    private static final Logger log = LoggerFactory.getLogger(EntradaController.class);
    @Autowired
    private EntradaDao entradaDao;
    @Autowired
    private ProveedorDao proveedorDao;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private ResourceBundleMessageSource messageSource;
    @Autowired
    private Ambiente ambiente;
    @Autowired
    private ReporteUtil reporteUtil;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Usuario usuario,
            Errors errors,
            Model modelo) {
        log.debug("Mostrando lista de tipos de entradas");
        Map<String, Object> params = new HashMap<>();
        params.put("almacen", request.getSession().getAttribute("almacenId"));
        if (StringUtils.isNotBlank(filtro)) {
            params.put("filtro", filtro);
        }
        if (pagina != null) {
            params.put("pagina", pagina);
            modelo.addAttribute("pagina", pagina);
        } else {
            pagina = 1L;
            modelo.addAttribute("pagina", pagina);
        }
        if (StringUtils.isNotBlank(order)) {
            params.put("order", order);
            params.put("sort", sort);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = entradaDao.lista(params);
            try {
                generaReporte(tipo, (List<Entrada>) params.get("entradas"), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove("reporte");
                errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = entradaDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Entrada>) params.get("entradas"), request);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("entrada.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = entradaDao.lista(params);
        modelo.addAttribute("entradas", params.get("entradas"));

        // inicia paginado
        Long cantidad = (Long) params.get("cantidad");
        Integer max = (Integer) params.get("max");
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<Entrada> entradas = (List<Entrada>) params.get("entradas");
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (entradas.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute("paginacion", paginacion);
        modelo.addAttribute("paginas", paginas);
        // termina paginado

        return "inventario/entrada/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando entrada {}", id);
        Entrada entrada = entradaDao.obtiene(id);

        modelo.addAttribute("entrada", entrada);

        return "inventario/entrada/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo entrada");
        Entrada entrada = new Entrada();
        modelo.addAttribute("entrada", entrada);

        return "inventario/entrada/nuevo";
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Entrada entrada, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return "inventario/entrada/nuevo";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (request.getParameter("proveedor.id") == null) {
                log.warn("No se puede crear la entrada si no ha seleccionado un proveedor");
                errors.rejectValue("proveedor", "entrada.sin.proveedor.message");
                return "inventario/entrada/nuevo";
            }
            Proveedor proveedor = proveedorDao.obtiene(new Long(request.getParameter("proveedor.id")));
            entrada.setProveedor(proveedor);
            entrada = entradaDao.crea(entrada, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la entrada", e);
            errors.rejectValue("factura", "campo.duplicado.message", new String[]{"factura"}, null);

            return "inventario/entrada/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "entrada.creada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{entrada.getFolio()});

        return "redirect:/inventario/entrada/ver/" + entrada.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable Long id, Model modelo) {
        log.debug("Edita entrada {}", id);
        Entrada entrada = entradaDao.obtiene(id);
        modelo.addAttribute("entrada", entrada);

        return "inventario/entrada/edita";
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Entrada entrada, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "inventario/entrada/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (request.getParameter("proveedor.id") == null) {
                log.warn("No se puede crear la entrada si no ha seleccionado un proveedor");
                errors.rejectValue("proveedor", "entrada.sin.proveedor.message");
                return "inventario/entrada/nuevo";
            }
            Proveedor proveedor = proveedorDao.obtiene(new Long(request.getParameter("proveedor.id")));
            entrada.setProveedor(proveedor);
            entrada = entradaDao.actualiza(entrada, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la entrada", e);
            errors.rejectValue("factura", "campo.duplicado.message", new String[]{"factura"}, null);

            return "inventario/entrada/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "entrada.actualizada.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{entrada.getFolio()});

        return "redirect:/inventario/entrada/ver/" + entrada.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Entrada entrada, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina entrada");
        try {
            String nombre = entradaDao.elimina(id);

            redirectAttributes.addFlashAttribute("message", "entrada.eliminada.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la entrada " + id, e);
            bindingResult.addError(new ObjectError("entrada", new String[]{"entrada.no.eliminada.message"}, null, null));
            return "inventario/entrada/ver";
        }

        return "redirect:/inventario/entrada";
    }

    @RequestMapping(value = "/proveedores", params = "term", produces = "application/json")
    public @ResponseBody
    List<LabelValueBean> proveedores(HttpServletRequest request, @RequestParam("term") String filtro) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession().getAttribute("empresaId"));
        params.put("filtro", filtro);
        params = proveedorDao.lista(params);
        List<LabelValueBean> valores = new ArrayList<>();
        List<Proveedor> proveedores = (List<Proveedor>) params.get("proveedores");
        for (Proveedor proveedor : proveedores) {
            StringBuilder sb = new StringBuilder();
            sb.append(proveedor.getNombre());
            sb.append(" | ");
            sb.append(proveedor.getRfc());
            sb.append(" | ");
            sb.append(proveedor.getNombreCompleto());
            valores.add(new LabelValueBean(proveedor.getId(), sb.toString(), proveedor.getNombre()));
        }
        return valores;
    }

    private void generaReporte(String tipo, List<Entrada> entradas, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = reporteUtil.generaPdf(entradas, "/mx/edu/um/mateo/inventario/reportes/entradas.jrxml");
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=entradas.pdf");
                break;
            case "CSV":
                archivo = reporteUtil.generaCsv(entradas, "/mx/edu/um/mateo/inventario/reportes/entradas.jrxml");
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=entradas.csv");
                break;
            case "XLS":
                archivo = reporteUtil.generaXls(entradas, "/mx/edu/um/mateo/inventario/reportes/entradas.jrxml");
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=entradas.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<Entrada> entradas, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = reporteUtil.generaPdf(entradas, "/mx/edu/um/mateo/inventario/reportes/entradas.jrxml");
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = reporteUtil.generaCsv(entradas, "/mx/edu/um/mateo/inventario/reportes/entradas.jrxml");
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = reporteUtil.generaXls(entradas, "/mx/edu/um/mateo/inventario/reportes/entradas.jrxml");
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("entrada.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }
}
