/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.web;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.dao.CuentaResultadoDao;
import mx.edu.um.mateo.contabilidad.model.CuentaResultado;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
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
 * @author develop
 */
@Controller
@RequestMapping(Constantes.PATH_CUENTA_RESULTADO)
public class CuentaResultadoController extends BaseController {

    @Autowired
    private CuentaResultadoDao cuentaResultadoDao;
    @Autowired
    private Ambiente ambiente;

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
        log.debug("Mostrando lista de cuentaResultados");
        Map<String, Object> params = new HashMap<>();
        Long organizacionId = (Long) request.getSession().getAttribute("organizacionId");
        params.put("organizacion", organizacionId);
        if (StringUtils.isNotBlank(filtro)) {
            params.put(Constantes.CONTAINSKEY_FILTRO, filtro);
        }

        if (StringUtils.isNotBlank(order)) {
            params.put(Constantes.CONTAINSKEY_ORDER, order);
            params.put(Constantes.CONTAINSKEY_SORT, sort);
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = cuentaResultadoDao.lista(params);
            try {
                generaReporte(tipo, (List<CuentaResultado>) params.get(Constantes.CONTAINSKEY_RESULTADOS), response, Constantes.CONTAINSKEY_RESULTADOS, mx.edu.um.mateo.general.utils.Constantes.ORG, organizacionId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }
        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = cuentaResultadoDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<CuentaResultado>) params.get(Constantes.CONTAINSKEY_RESULTADOS), request, Constantes.CONTAINSKEY_RESULTADOS, mx.edu.um.mateo.general.utils.Constantes.ORG, organizacionId);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("cuentaResultado.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = cuentaResultadoDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_RESULTADOS, params.get(Constantes.CONTAINSKEY_RESULTADOS));

        // inicia paginado
        this.pagina(params, modelo, Constantes.CONTAINSKEY_RESULTADOS, pagina);
        // termina paginado

        return Constantes.PATH_CUENTA_RESULTADO_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando cuentaResultado {}", id);
        CuentaResultado cuentaResultado = cuentaResultadoDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_RESULTADO, cuentaResultado);

        return Constantes.PATH_CUENTA_RESULTADO_VER;
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nueva cuentaResultado");
        CuentaResultado cuentaResultado = new CuentaResultado();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_RESULTADO, cuentaResultado);
        return Constantes.PATH_CUENTA_RESULTADO_NUEVA;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid CuentaResultado cuentaResultado, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            for (ObjectError error : bindingResult.getAllErrors()) {
                log.debug("Error: {}", error);
            }
            return Constantes.PATH_CUENTA_RESULTADO_NUEVA;
        }

        try {
            //Usuario usuario = ambiente.obtieneUsuario();
            cuentaResultado = cuentaResultadoDao.crea(cuentaResultado);

            //ambiente.actualizaSesion(request, usuario);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al cuentaResultado", e);
            errors.rejectValue("codigo", "campo.duplicado.message", new String[]{"codigo"}, null);
            errors.rejectValue("nombre", "campo.duplicado.message", new String[]{"nombre"}, null);
            return Constantes.PATH_CUENTA_RESULTADO_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaResultado.creada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{cuentaResultado.getNombre()});

        return "redirect:" + Constantes.PATH_CUENTA_RESULTADO_VER + "/" + cuentaResultado.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Edita cuentaResultado {}", id);
        CuentaResultado cuentaResultado = cuentaResultadoDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_RESULTADO, cuentaResultado);
        return Constantes.PATH_CUENTA_RESULTADO_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid CuentaResultado cuentaResultado, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_CUENTA_RESULTADO_EDITA;
        }

        try {
            // cuentaResultado = cuentaResultadoDao.actualiza(cuentaResultado, ambiente.obtieneUsuario());
            cuentaResultado = cuentaResultadoDao.actualiza(cuentaResultado);

        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la cuentaResultado", e);
            return Constantes.PATH_CUENTA_RESULTADO_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaResultado.actualizada.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{cuentaResultado.getNombre()});

        return "redirect:" + Constantes.PATH_CUENTA_RESULTADO_VER + "/" + cuentaResultado.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute CuentaResultado cuentaResultado, BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        log.debug("Elimina cuenta de resultado");
        try {
            String nombre = cuentaResultadoDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "cuentaResultado.eliminada.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la cuenta de resultado" + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_RESULTADO, new String[]{"cuentaResultado.no.eliminada.message"}, null, null));
            return Constantes.PATH_CUENTA_RESULTADO_VER;
        }

        return "redirect:" + Constantes.PATH_CUENTA_RESULTADO;
    }
   
}
