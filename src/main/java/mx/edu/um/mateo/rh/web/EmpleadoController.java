/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.rh.web;

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
 * @author AMDA
 */

@Controller
@RequestMapping("/rh/empleado")
public class EmpleadoController {
//    private static final Logger log = LoggerFactory.getLogger(EmpleadoController.class);
//    @Autowired
//    private EmpleadoDao empleadoDao;
//    @Autowired
//    private TipoEmpleadoDao tipoEmpleadoDao;
//    @Autowired
//    private JavaMailSender mailSender;
//    @Autowired
//    private ResourceBundleMessageSource messageSource;
//    @Autowired
//    private Ambiente ambiente;
//    @Autowired
//    private ReporteUtil reporteUtil;
//    
//    @RequestMapping
//    public String lista(HttpServletRequest request, HttpServletResponse response,
//            @RequestParam(required = false) String filtro,
//            @RequestParam(required = false) Long pagina,
//            @RequestParam(required = false) String tipo,
//            @RequestParam(required = false) String correo,
//            @RequestParam(required = false) String order,
//            @RequestParam(required = false) String sort,
//            Model modelo) {
//        log.debug("Mostrando lista de empleados");
//        Map<String, Object> params = new HashMap<>();
//        params.put("empresa", request.getSession().getAttribute("empresaId"));
//        if (StringUtils.isNotBlank(filtro)) {
//            params.put("filtro", filtro);
//        }
//        if (pagina != null) {
//            params.put("pagina", pagina);
//            modelo.addAttribute("pagina", pagina);
//        } else {
//            pagina = 1L;
//            modelo.addAttribute("pagina", pagina);
//        }
//        if (StringUtils.isNotBlank(order)) {
//            params.put("order", order);
//            params.put("sort", sort);
//        }
//
//        if (StringUtils.isNotBlank(tipo)) {
//            params.put("reporte", true);
//            params = empleadoDao.lista(params);
//            try {
//                generaReporte(tipo, (List<Empleado>) params.get("empleados"), response);
//                return null;
//            } catch (JRException | IOException e) {
//                log.error("No se pudo generar el reporte", e);
//            }
//        }
//
//        if (StringUtils.isNotBlank(correo)) {
//            params.put("reporte", true);
//            params = empleadoDao.lista(params);
//
//            params.remove("reporte");
//            try {
//                enviaCorreo(correo, (List<Empleado>) params.get("empleados"), request);
//                modelo.addAttribute("message", "lista.enviado.message");
//                modelo.addAttribute("messageAttrs", new String[]{messageSource.getMessage("empleado.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
//            } catch (JRException | MessagingException e) {
//                log.error("No se pudo enviar el reporte por correo", e);
//            }
//        }
//        params = empleadoDao.lista(params);
//        modelo.addAttribute("empleados", params.get("empleados"));
//
//        // inicia paginado
//        Long cantidad = (Long) params.get("cantidad");
//        Integer max = (Integer) params.get("max");
//        Long cantidadDePaginas = cantidad / max;
//        List<Long> paginas = new ArrayList<>();
//        long i = 1;
//        do {
//            paginas.add(i);
//        } while (i++ < cantidadDePaginas);
//        List<Empleado> empleados = (List<Empleado>) params.get("empleados");
//        Long primero = ((pagina - 1) * max) + 1;
//        Long ultimo = primero + (empleados.size() - 1);
//        String[] paginacion = new String[]{primero.toString(), ultimo.toString(), cantidad.toString()};
//        modelo.addAttribute("paginacion", paginacion);
//        modelo.addAttribute("paginas", paginas);
//        // termina paginado
//
//        return "rh/empleado/lista";
//    }
//
//    @RequestMapping("/ver/{id}")
//    public String ver(@PathVariable Long id, Model modelo) {
//        log.debug("Mostrando empleado {}", id);
//        Empleado empleado = empleadoDao.obtiene(id);
//
//        modelo.addAttribute("empleado", empleado);
//
//        return "rh/empleado/ver";
//    }
//    
//    @RequestMapping("/nuevo")
//    public String nuevo(HttpServletRequest request, Model modelo) {
//        log.debug("Nuevo empleado");
//        Empleado empleado = new Empleado();
//        modelo.addAttribute("empleado", cliente);
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("empresa", request.getSession().getAttribute("empresaId"));
//        params.put("reporte", true);
//        params = tipoEmpleadoDao.lista(params);
//        modelo.addAttribute("tiposDeCliente", params.get("tiposDeEmpleado"));
//
//        return "rh/empleado/nuevo";
//    }
//    
//    @Transactional
//    @RequestMapping(value = "/crea", method = RequestMethod.POST)
//    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Empleado empleado, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
//        for (String nombre : request.getParameterMap().keySet()) {
//            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
//        }
//        if (bindingResult.hasErrors()) {
//            log.debug("Hubo algun error en la forma, regresando");
//
//            Map<String, Object> params = new HashMap<>();
//            params.put("empresa", request.getSession().getAttribute("empresaId"));
//            params.put("reporte", true);
//            params = tipoEmpleadoDao.lista(params);
//            modelo.addAttribute("tiposDeEmpleado", params.get("tiposDeEmpleado"));
//
//            return "rh/empleado/nuevo";
//        }
//
//        try {
//            Usuario usuario = ambiente.obtieneUsuario();
//            log.debug("TipoEmpleado: {}", empleado.getTipoEmpleado().getId());
//            empleado = empleadoDao.crea(empleado, usuario);
//        } catch (ConstraintViolationException e) {
//            log.error("No se pudo crear al empleado", e);
//            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
//
//            Map<String, Object> params = new HashMap<>();
//            params.put("empresa", request.getSession().getAttribute("empresaId"));
//            params.put("reporte", true);
//            params = tipoEmpleadoDao.lista(params);
//            modelo.addAttribute("tiposDeEmpleado", params.get("tiposDeEmpleado"));
//
//            return "rh/empleado/nuevo";
//        }
//
//        redirectAttributes.addFlashAttribute("message", "empleado.creado.message");
//        redirectAttributes.addFlashAttribute("messageAttrs", new String[]{empleado.getNombre()});
//
//        return "redirect:/rh/empleado/ver/" + empleado.getId();
//    }
//    
}

