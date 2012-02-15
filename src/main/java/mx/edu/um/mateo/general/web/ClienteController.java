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
package mx.edu.um.mateo.general.web;

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
import mx.edu.um.mateo.general.dao.ClienteDao;
import mx.edu.um.mateo.general.dao.TipoClienteDao;
import mx.edu.um.mateo.general.model.Cliente;
import mx.edu.um.mateo.general.model.TipoCliente;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.ReporteUtil;
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
@RequestMapping("/admin/cliente")
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);
    @Autowired
    private ClienteDao clienteDao;
    @Autowired
    private TipoClienteDao tipoClienteDao;
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
            Model modelo) {
        log.debug("Mostrando lista de clientes");
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession().getAttribute("empresaId"));
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
            params = clienteDao.lista(params);
            try {
                generaReporte(tipo, (List<Cliente>) params.get("clientes"), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = clienteDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Cliente>) params.get("clientes"), request);
                modelo.addAttribute("message", "lista.enviado.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("cliente.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = clienteDao.lista(params);
        modelo.addAttribute("clientes", params.get("clientes"));

        // inicia paginado
        Long cantidad = (Long) params.get("cantidad");
        Integer max = (Integer) params.get("max");
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        for (long i = 1; i <= cantidadDePaginas + 1; i++) {
            paginas.add(i);
        }
        List<Cliente> clientes = (List<Cliente>) params.get("clientes");
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (clientes.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute("paginacion", paginacion);
        modelo.addAttribute("paginas", paginas);
        // termina paginado

        return "admin/cliente/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando cliente {}", id);
        Cliente cliente = clienteDao.obtiene(id);

        modelo.addAttribute("cliente", cliente);

        return "admin/cliente/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo cliente");
        Cliente cliente = new Cliente();
        modelo.addAttribute("cliente", cliente);
        
        Map<String, Object> params = new HashMap<>();
        params.put("empresa",request.getSession().getAttribute("empresaId"));
        params.put("reporte", true);
        params = tipoClienteDao.lista(params);
        modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));
        
        return "admin/cliente/nuevo";
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Cliente cliente, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");

            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession().getAttribute("empresaId"));
            params.put("reporte", true);
            params = tipoClienteDao.lista(params);
            modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));

            return "admin/cliente/nuevo";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            log.debug("TipoCliente: {}",cliente.getTipoCliente().getId());
            cliente = clienteDao.crea(cliente, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al cliente", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);

            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession().getAttribute("empresaId"));
            params.put("reporte", true);
            params = tipoClienteDao.lista(params);
            modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));

            return "admin/cliente/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "cliente.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{cliente.getNombre()});

        return "redirect:/admin/cliente/ver/" + cliente.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable Long id, Model modelo) {
        log.debug("Edita cliente {}", id);
        Cliente cliente = clienteDao.obtiene(id);
        modelo.addAttribute("cliente", cliente);
        
        Map<String, Object> params = new HashMap<>();
        params.put("empresa",request.getSession().getAttribute("empresaId"));
        params.put("reporte", true);
        params = tipoClienteDao.lista(params);
        modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));
        
        return "admin/cliente/edita";
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Cliente cliente, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");

            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession().getAttribute("empresaId"));
            params.put("reporte", true);
            params = tipoClienteDao.lista(params);
            modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));

            return "admin/cliente/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            cliente = clienteDao.actualiza(cliente, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la cliente", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);

            Map<String, Object> params = new HashMap<>();
            params.put("empresa",request.getSession().getAttribute("empresaId"));
            params.put("reporte", true);
            params = tipoClienteDao.lista(params);
            modelo.addAttribute("tiposDeCliente", params.get("tiposDeCliente"));

            return "admin/cliente/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "cliente.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{cliente.getNombre()});

        return "redirect:/admin/cliente/ver/" + cliente.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Cliente cliente, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina cliente");
        try {
            String nombre = clienteDao.elimina(id);

            redirectAttributes.addFlashAttribute("message", "cliente.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la cliente " + id, e);
            bindingResult.addError(new ObjectError("cliente", new String[]{"cliente.no.eliminado.message"}, null, null));
            return "admin/cliente/ver";
        }

        return "redirect:/admin/cliente";
    }

    private void generaReporte(String tipo, List<Cliente> clientes, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = reporteUtil.generaPdf(clientes, "/mx/edu/um/mateo/general/reportes/clientes.jrxml");
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=clientes.pdf");
                break;
            case "CSV":
                archivo = reporteUtil.generaCsv(clientes, "/mx/edu/um/mateo/general/reportes/clientes.jrxml");
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=clientes.csv");
                break;
            case "XLS":
                archivo = reporteUtil.generaXls(clientes, "/mx/edu/um/mateo/general/reportes/clientes.jrxml");
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=clientes.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<Cliente> clientes, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = reporteUtil.generaPdf(clientes, "/mx/edu/um/mateo/general/reportes/clientes.jrxml");
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = reporteUtil.generaCsv(clientes, "/mx/edu/um/mateo/general/reportes/clientes.jrxml");
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = reporteUtil.generaXls(clientes, "/mx/edu/um/mateo/general/reportes/clientes.jrxml");
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("cliente.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

}
