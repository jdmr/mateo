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
import mx.edu.um.mateo.general.dao.ProveedorDao;
import mx.edu.um.mateo.general.model.Proveedor;
import mx.edu.um.mateo.general.model.Usuario;
import net.sf.jasperreports.engine.JRException;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin/proveedor")
public class ProveedorController extends BaseController {

    @Autowired
    private ProveedorDao proveedorDao;

    @RequestMapping
    public String lista(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo) {
        log.debug("Mostrando lista de proveedores");
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
            params = proveedorDao.lista(params);
            try {
                generaReporte(tipo, (List<Proveedor>) params.get("proveedores"), response);
                return null;
            } catch (JRException | IOException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = proveedorDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Proveedor>) params.get("proveedores"), request);
                modelo.addAttribute("message", "lista.enviado.message");
                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("proveedor.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (JRException | MessagingException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = proveedorDao.lista(params);
        modelo.addAttribute("proveedores", params.get("proveedores"));

        // inicia paginado
        Long cantidad = (Long) params.get("cantidad");
        Integer max = (Integer) params.get("max");
        Long cantidadDePaginas = cantidad / max;
        List<Long> paginas = new ArrayList<>();
        long i = 1;
        do {
            paginas.add(i);
        } while (i++ < cantidadDePaginas);
        List<Proveedor> proveedores = (List<Proveedor>) params.get("proveedores");
        Long primero = ((pagina - 1) * max) + 1;
        Long ultimo = primero + (proveedores.size() - 1);
        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
        modelo.addAttribute("paginacion", paginacion);
        modelo.addAttribute("paginas", paginas);
        // termina paginado

        return "admin/proveedor/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando proveedor {}", id);
        Proveedor proveedor = proveedorDao.obtiene(id);

        modelo.addAttribute("proveedor", proveedor);

        return "admin/proveedor/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(Model modelo) {
        log.debug("Nuevo proveedor");
        Proveedor proveedor = new Proveedor();
        modelo.addAttribute("proveedor", proveedor);
        return "admin/proveedor/nuevo";
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Proveedor proveedor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return "admin/proveedor/nuevo";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            proveedor = proveedorDao.crea(proveedor, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al proveedor", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "admin/proveedor/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "proveedor.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{proveedor.getNombre()});

        return "redirect:/admin/proveedor/ver/" + proveedor.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita proveedor {}", id);
        Proveedor proveedor = proveedorDao.obtiene(id);
        modelo.addAttribute("proveedor", proveedor);
        return "admin/proveedor/edita";
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Proveedor proveedor, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return "admin/proveedor/edita";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            proveedor = proveedorDao.actualiza(proveedor, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la proveedor", e);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return "admin/proveedor/nuevo";
        }

        redirectAttributes.addFlashAttribute("message", "proveedor.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{proveedor.getNombre()});

        return "redirect:/admin/proveedor/ver/" + proveedor.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Proveedor proveedor, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina proveedor");
        try {
            String nombre = proveedorDao.elimina(id);

            redirectAttributes.addFlashAttribute("message", "proveedor.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs", new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la proveedor " + id, e);
            bindingResult.addError(new ObjectError("proveedor", new String[]{"proveedor.no.eliminado.message"}, null, null));
            return "admin/proveedor/ver";
        }

        return "redirect:/admin/proveedor";
    }

    private void generaReporte(String tipo, List<Proveedor> proveedores, HttpServletResponse response) throws JRException, IOException {
        log.debug("Generando reporte {}", tipo);
        byte[] archivo = null;
        switch (tipo) {
            case "PDF":
                archivo = reporteUtil.generaPdf(proveedores, "/mx/edu/um/mateo/general/reportes/proveedores.jrxml");
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=proveedores.pdf");
                break;
            case "CSV":
                archivo = reporteUtil.generaCsv(proveedores, "/mx/edu/um/mateo/general/reportes/proveedores.jrxml");
                response.setContentType("text/csv");
                response.addHeader("Content-Disposition", "attachment; filename=proveedores.csv");
                break;
            case "XLS":
                archivo = reporteUtil.generaXls(proveedores, "/mx/edu/um/mateo/general/reportes/proveedores.jrxml");
                response.setContentType("application/vnd.ms-excel");
                response.addHeader("Content-Disposition", "attachment; filename=proveedores.xls");
        }
        if (archivo != null) {
            response.setContentLength(archivo.length);
            try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
                bos.write(archivo);
                bos.flush();
            }
        }

    }

    private void enviaCorreo(String tipo, List<Proveedor> proveedores, HttpServletRequest request) throws JRException, MessagingException {
        log.debug("Enviando correo {}", tipo);
        byte[] archivo = null;
        String tipoContenido = null;
        switch (tipo) {
            case "PDF":
                archivo = reporteUtil.generaPdf(proveedores, "/mx/edu/um/mateo/general/reportes/proveedores.jrxml");
                tipoContenido = "application/pdf";
                break;
            case "CSV":
                archivo = reporteUtil.generaCsv(proveedores, "/mx/edu/um/mateo/general/reportes/proveedores.jrxml");
                tipoContenido = "text/csv";
                break;
            case "XLS":
                archivo = reporteUtil.generaXls(proveedores, "/mx/edu/um/mateo/general/reportes/proveedores.jrxml");
                tipoContenido = "application/vnd.ms-excel";
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(ambiente.obtieneUsuario().getUsername());
        String titulo = messageSource.getMessage("proveedor.lista.label", null, request.getLocale());
        helper.setSubject(messageSource.getMessage("envia.correo.titulo.message", new String[]{titulo}, request.getLocale()));
        helper.setText(messageSource.getMessage("envia.correo.contenido.message", new String[]{titulo}, request.getLocale()), true);
        helper.addAttachment(titulo + "." + tipo, new ByteArrayDataSource(archivo, tipoContenido));
        mailSender.send(message);
    }

}
