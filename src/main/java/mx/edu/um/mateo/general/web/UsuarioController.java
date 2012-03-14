/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
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
import javax.validation.Valid;
import mx.edu.um.mateo.general.dao.UsuarioDao;
import mx.edu.um.mateo.general.model.Rol;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.SpringSecurityUtils;
import mx.edu.um.mateo.general.utils.UltimoException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.keygen.KeyGenerators;
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
 * @author jdmr
 */
@Controller
@RequestMapping("/admin/usuario")
public class UsuarioController extends BaseController {

    @Autowired
    private UsuarioDao usuarioDao;
    @Autowired
    private SpringSecurityUtils springSecurityUtils;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de usuarios");
        Map<String, Object> params = new HashMap<>();
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

        params.put("empresa", request.getSession().getAttribute("empresaId"));

        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = usuarioDao.lista(params);
            try {
                generaReporte(tipo, (List<Usuario>) params.get("usuarios"), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = usuarioDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Usuario>) params.get("usuarios"), request);
                modelo.addAttribute("message", "lista.enviada.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("usuario.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = usuarioDao.lista(params);
        modelo.addAttribute("usuarios", params.get("usuarios"));

        // inicia paginado
        Long cantidad = (Long) params.get("cantidad");
        Integer max = (Integer) params.get("max");
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<Usuario> usuarios = (List<Usuario>) params.get("usuarios");
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (usuarios.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute("paginacion", paginacion);
        modelo.addAttribute("paginas", paginas);
        // termina paginado

        return "admin/usuario/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando usuario {}", id);
        Usuario usuario = usuarioDao.obtiene(id);
        List<Rol> roles = usuarioDao.roles();

        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("roles", roles);

        return "admin/usuario/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo usuario");
        List<Rol> roles = obtieneRoles();
        Usuario usuario = new Usuario();
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("roles", roles);
        return "admin/usuario/nuevo";
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Usuario usuario, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            List<Rol> roles = obtieneRoles();
            modelo.addAttribute("roles", roles);
            return "admin/usuario/nuevo";
        }

        String password = null;
        try {
            log.debug("Evaluando roles {}", request.getParameterValues("roles"));
            String[] roles = request.getParameterValues("roles");
            if (roles == null || roles.length == 0) {
                log.debug("Asignando ROLE_USER por defecto");
                roles = new String[]{"ROLE_USER"};
            }
            Long almacenId = (Long) request.getSession().getAttribute("almacenId");
            password = KeyGenerators.string().generateKey();
            usuario.setPassword(password);
            usuario = usuarioDao.crea(usuario, almacenId, roles);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(usuario.getUsername());
            helper.setSubject(messageSource.getMessage("envia.correo.password.titulo.message", new String[]{}, request.getLocale()));
            helper.setText(messageSource.getMessage("envia.correo.password.contenido.message", new String[]{usuario.getNombre(), usuario.getUsername(), password}, request.getLocale()), true);
            mailSender.send(message);

        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al usuario", e);
            errors.rejectValue("username", "campo.duplicado.message", new String[]{"username"}, null);
            List<Rol> roles = obtieneRoles();
            modelo.addAttribute("roles", roles);
            return "admin/usuario/nuevo";
        } catch (MessagingException e) {
            log.error("No se pudo enviar la contrasena por correo",e);
            
            redirectAttributes.addFlashAttribute("message", "usuario.creado.sin.correo.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{usuario.getUsername(), password});

            return "redirect:/admin/usuario/ver/" + usuario.getId();
        }

        redirectAttributes.addFlashAttribute("message", "usuario.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{usuario.getUsername()});

        return "redirect:/admin/usuario/ver/" + usuario.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita usuario {}", id);
        List<Rol> roles = obtieneRoles();
        Usuario usuario = usuarioDao.obtiene(id);
        modelo.addAttribute("usuario", usuario);
        modelo.addAttribute("roles", roles);
        return "admin/usuario/edita";
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Usuario usuario, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            List<Rol> roles = obtieneRoles();
            modelo.addAttribute("roles", roles);
            return "admin/usuario/edita";
        }

        try {
            String[] roles = request.getParameterValues("roles");
            if (roles == null || roles.length == 0) {
                roles = new String[]{"ROLE_USER"};
            }
            Long almacenId = (Long) request.getSession().getAttribute("almacenId");
            usuario = usuarioDao.actualiza(usuario, almacenId, roles);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al usuario", e);
            errors.rejectValue("username", "campo.duplicado.message", new String[]{"username"}, null);
            List<Rol> roles = obtieneRoles();
            modelo.addAttribute("roles", roles);
            return "admin/usuario/edita";
        }

        redirectAttributes.addFlashAttribute("message", "usuario.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{usuario.getUsername()});

        return "redirect:/admin/usuario/ver/" + usuario.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(@RequestParam Long id, Model modelo, @ModelAttribute Usuario usuario, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina usuario");
        try {
            String nombre = usuarioDao.elimina(id);
            redirectAttributes.addFlashAttribute("message", "usuario.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (UltimoException e) {
            log.error("No se pudo eliminar el usuario " + id, e);
            bindingResult.addError(new ObjectError("usuario", new String[]{"ultimo.usuario.no.eliminado.message"}, null, null));
            List<Rol> roles = usuarioDao.roles();
            modelo.addAttribute("roles", roles);
            return "admin/usuario/ver";
        } catch (Exception e) {
            log.error("No se pudo eliminar el usuario " + id, e);
            bindingResult.addError(new ObjectError("usuario", new String[]{"usuario.no.eliminado.message"}, null, null));
            List<Rol> roles = usuarioDao.roles();
            modelo.addAttribute("roles", roles);
            return "admin/usuario/ver";
        }

        return "redirect:/admin/usuario";
    }

    private List<Rol> obtieneRoles() {
        List<Rol> roles = usuarioDao.roles();
        if (springSecurityUtils.ifAnyGranted("ROLE_ADMIN")) {
            // no se hace nada
        } else if (springSecurityUtils.ifAnyGranted("ROLE_ORG")) {
            roles.remove(new Rol("ROLE_ADMIN"));
            roles.remove(new Rol("ROLE_ORG"));
        } else {
            roles.remove(new Rol("ROLE_ADMIN"));
            roles.remove(new Rol("ROLE_ORG"));
            roles.remove(new Rol("ROLE_EMP"));
        }

        return roles;
    }

    private void generaReporte(String tipo, List<Usuario> usuarios, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(usuarios);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=usuarios.pdf");
                break;
            case "CSV":
                archivo = generaCsv(usuarios);
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=usuarios.csv");
                break;
            case "XLS":
                archivo = generaXls(usuarios);
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=usuarios.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<Usuario> usuarios, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = generaPdf(usuarios);
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = generaCsv(usuarios);
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = generaXls(usuarios);
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("usuario.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

    private byte[] generaPdf(List usuarios) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/usuarios.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(usuarios));
        byte[] archivo = JasperExportManager.exportReportToPdf(jasperPrint);

        return archivo;
    }

    private byte[] generaCsv(List usuarios) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRCsvExporter exporter = new JRCsvExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/usuarios.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(usuarios));
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        exporter.exportReport();
        byte[] archivo = byteArrayOutputStream.toByteArray();

        return archivo;
    }

    private byte[] generaXls(List usuarios) throws JRException {
        Map<String, Object> params = new HashMap<>();
        JRXlsExporter exporter = new JRXlsExporter();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperDesign jd = JRXmlLoader.load(this.getClass().getResourceAsStream("/mx/edu/um/mateo/general/reportes/usuarios.jrxml"));
        JasperReport jasperReport = JasperCompileManager.compileReport(jd);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(usuarios));
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
}
