package mx.edu.um.mateo.contabilidad.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.Constantes;
import mx.edu.um.mateo.contabilidad.dao.LibroDao;
import mx.edu.um.mateo.contabilidad.model.Libro;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(Constantes.PATH_CUENTA_LIBRO)
public class LibroController extends BaseController {

    @Autowired
    private LibroDao LibroDao;
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
        log.debug("Mostrando lista de  de libros");
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
            params = LibroDao.lista(params);
            try {
                generaReporte(tipo, (List<Libro>) params.get(Constantes.CONTAINSKEY_LIBROS), response, Constantes.CONTAINSKEY_LIBROS, mx.edu.um.mateo.general.utils.Constantes.ORG, organizacionId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
                params.remove(Constantes.CONTAINSKEY_REPORTE);
                //errors.reject("error.generar.reporte");
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put(Constantes.CONTAINSKEY_REPORTE, true);
            params = LibroDao.lista(params);

            params.remove(Constantes.CONTAINSKEY_REPORTE);
            try {
                enviaCorreo(correo, (List<Libro>) params.get(Constantes.CONTAINSKEY_LIBROS), request, Constantes.CONTAINSKEY_LIBROS, mx.edu.um.mateo.general.utils.Constantes.ORG, organizacionId);
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE, "lista.enviada.message");
                modelo.addAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{messageSource.getMessage("Libro.lista.label", null, request.getLocale()), ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = LibroDao.lista(params);
        modelo.addAttribute(Constantes.CONTAINSKEY_LIBROS, params.get(Constantes.CONTAINSKEY_LIBROS));

        this.pagina(params, modelo, Constantes.CONTAINSKEY_LIBROS, pagina);

        return Constantes.PATH_CUENTA_LIBRO_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando   libro {}", id);
        Libro libro = LibroDao.obtiene(id);

        modelo.addAttribute(Constantes.ADDATTRIBUTE_LIBRO, libro);

        return Constantes.PATH_CUENTA_LIBRO_VER;
    }

    @RequestMapping("/nueva")
    public String nueva(Model modelo) {
        log.debug("Nuevo   libro");
        Libro libro = new Libro();
        modelo.addAttribute(Constantes.ADDATTRIBUTE_LIBRO, libro);
        return Constantes.PATH_CUENTA_LIBRO_NUEVA;
    }

    @Transactional
    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request, HttpServletResponse response, @Valid Libro libro, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre, request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            return Constantes.PATH_CUENTA_LIBRO_NUEVA;
        }

        try {
            libro = LibroDao.crea(libro, ambiente.obtieneUsuario());
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear libro", e);
            return Constantes.PATH_CUENTA_LIBRO_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "libro.creado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{libro.getNombre()});

        return "redirect:" + Constantes.PATH_CUENTA_LIBRO_VER + "/" + libro.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(@PathVariable Long id, Model modelo) {
        log.debug("Editar  de libro {}", id);
        Libro libro = LibroDao.obtiene(id);
        modelo.addAttribute(Constantes.ADDATTRIBUTE_LIBRO, libro);
        return Constantes.PATH_CUENTA_LIBRO_EDITA;
    }

    @Transactional
    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid Libro libro, BindingResult bindingResult, Errors errors, Model modelo, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            return Constantes.PATH_CUENTA_LIBRO_EDITA;
        }
        try {
            //  libro = LibroDao.actualiza(libro, ambiente.obtieneUsuario());
            libro = LibroDao.actualiza(libro, ambiente.obtieneUsuario());
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear la  de libro", e);
            return Constantes.PATH_CUENTA_LIBRO_NUEVA;
        }

        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "libro.actualizado.message");
        redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{libro.getNombre()});

        return "redirect:" + Constantes.PATH_CUENTA_LIBRO_VER + "/" + libro.getId();
    }

    @Transactional
    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id, Model modelo, @ModelAttribute Libro libro, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina  de libro");
        try {
            String nombre = LibroDao.elimina(id);

            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE, "libro.eliminado.message");
            redirectAttributes.addFlashAttribute(Constantes.CONTAINSKEY_MESSAGE_ATTRS, new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la  de libro " + id, e);
            bindingResult.addError(new ObjectError(Constantes.ADDATTRIBUTE_LIBRO, new String[]{"libro.no.eliminado.message"}, null, null));
            return Constantes.PATH_CUENTA_LIBRO_VER;
        }

        return "redirect:" + Constantes.PATH_CUENTA_LIBRO;
    }
}