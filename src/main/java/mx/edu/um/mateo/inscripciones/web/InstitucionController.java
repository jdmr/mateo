package mx.edu.um.mateo.inscripciones.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inscripciones.dao.InstitucionDao;
import mx.edu.um.mateo.inscripciones.model.Institucion;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/** @author semdariobarbaamaya */

@Controller
@RequestMapping("/inscripciones/instituciones")
public class InstitucionController extends BaseController{
    
    @Autowired
    private InstitucionDao institucionDao;
    
    @SuppressWarnings("unchecked")
    @RequestMapping
    public String lista(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String filtro,
			@RequestParam(required = false) Long pagina,
			@RequestParam(required = false) String tipo,
			@RequestParam(required = false) String correo,
			@RequestParam(required = false) String order,
			@RequestParam(required = false) String sort, Model modelo) {
		log.debug("Mostrando lista de instituciones");
		Map<String, Object> params = new HashMap<>();
		Long empresaId = (Long) request.getSession().getAttribute("empresaId");
		params.put("empresa", empresaId);
		if (StringUtils.isNotBlank(filtro)) {
			params.put("filtro", filtro);
		}
		if (StringUtils.isNotBlank(order)) {
			params.put("order", order);
			params.put("sort", sort);
		}
		if (pagina != null) {
			params.put("pagina", pagina);
		}

		if (StringUtils.isNotBlank(tipo)) {
			params.put("reporte", true);
			params = institucionDao.lista(params);
			try {
				generaReporte(tipo, (List<Institucion>) params.get(Constantes.CONTAINSKEY_INSTITUCION),
						response, "institucion", Constantes.EMP, empresaId);
				return null;
			} catch (ReporteException e) {
				log.error("No se pudo generar el reporte", e);
			}
		}

		if (StringUtils.isNotBlank(correo)) {
			params.put("reporte", true);
			params = institucionDao.lista(params);

			params.remove("reporte");
			try {
				enviaCorreo(correo, (List<Institucion>) params.get(Constantes.CONTAINSKEY_INSTITUCION),
						request, "institucion", Constantes.EMP, empresaId);
				modelo.addAttribute("message", "lista.enviado.message");
				modelo.addAttribute(
						"messageAttrs",
						new String[] {
								messageSource.getMessage("institucion.lista.label",
										null, request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}
		params = institucionDao.lista(params);
                log.debug("Instituciones {}", ((List)params.get(Constantes.CONTAINSKEY_INSTITUCION)).size());
		modelo.addAttribute(Constantes.CONTAINSKEY_INSTITUCION, params.get(Constantes.CONTAINSKEY_INSTITUCION));

		this.pagina(params, modelo, Constantes.CONTAINSKEY_INSTITUCION, pagina);

		return Constantes.PATH_INSTITUCION_LISTA;     
	}
    
        @RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando institucion {}", id);
		Institucion institucion = institucionDao.obtiene(id);

		modelo.addAttribute("institucion", institucion);

		return Constantes.PATH_INSTITUCION_VER;
	}
    
        @RequestMapping("/nueva")
	public String nuevo(HttpServletRequest request, Model modelo) {
		log.debug("Nueva institucion");
		Institucion institucion = new Institucion();
		modelo.addAttribute("institucion", institucion);

		Map<String, Object> params = new HashMap<>();
		params.put("empresa", request.getSession().getAttribute("empresaId"));
		params.put("reporte", true);

		return Constantes.PATH_INSTITUCION_NUEVO;
	}
        
        @RequestMapping(value = "/graba", method = RequestMethod.POST)
	public String graba(HttpServletRequest request,
			HttpServletResponse response, @Valid Institucion institucion,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");

			Map<String, Object> params = new HashMap<>();
			//params.put("organizacion", request.getSession()
					//.getAttribute("organizacionId"));
                        params.put("empresa", ambiente.obtieneUsuario().getEmpresa());
			params.put("reporte", true);
			return Constantes.PATH_INSTITUCION_NUEVO;
		}

		try {
                        if(institucion.getId()!=null){
                        /*
                        Usuario usuario = ambiente.obtieneUsuario();
                        periodo.setEmpresa(usuario.getEmpresa());
                        periodoDao.graba(periodo);
                        */
                            Institucion tmp = institucionDao.obtiene(institucion.getId());
                            tmp.setNombre(institucion.getNombre());
                            tmp.setPorcentaje(institucion.getPorcentaje());
                            tmp.setStatus(institucion.getStatus());
                            
                            institucionDao.graba(tmp);
                            
                            redirectAttributes.addFlashAttribute("message",
				"institucion.actualizada.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { institucion.getNombre() });
                        }else{
                            
                            Usuario usuario = ambiente.obtieneUsuario();
                            institucion.setOrganizacion(usuario.getEmpresa().getOrganizacion());
                            institucionDao.graba(institucion);
                            /*
                            Periodo tmp = periodoDao.obtiene(periodo.getId());
                            tmp.setClave(periodo.getClave());
                            tmp.setDescripcion(periodo.getDescripcion());
                            tmp.setExcluye(periodo.getExcluye());
                            tmp.setIncluye(periodo.getIncluye());
                            tmp.setStatus(periodo.getStatus());
                            tmp.setFechaFinal(periodo.getFechaFinal());
                            tmp.setFechaInicial(periodo.getFechaInicial());
                            */
                           redirectAttributes.addFlashAttribute("message",
				"institucion.creada.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { institucion.getNombre() }); 
                            
                            
                        }
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear la institucion", e);
			errors.rejectValue("nombre", "campo.duplicado.message",
					new String[] { "nombre" }, null);

			Map<String, Object> params = new HashMap<>();
			params.put("organizacion", request.getSession()
					.getAttribute("organizacionId"));
			params.put("reporte", true);
			return Constantes.PATH_INSTITUCION_NUEVO;
		}

		log.error("Hasta Aqui llega {}", institucion);

		return "redirect:"+ Constantes.PATH_INSTITUCION + "/";
	}
        
        @RequestMapping("/edita/{id}")
	public String edita(HttpServletRequest request, @PathVariable Long id,
			Model modelo) {
		log.debug("Edita institucion {}", id);
		Institucion institucion = institucionDao.obtiene(id);
		modelo.addAttribute("institucion", institucion);

		Map<String, Object> params = new HashMap<>();
		params.put("organizacion", request.getSession().getAttribute("organizacionId"));
		params.put("reporte", true);
                
		return Constantes.PATH_INSTITUCION_EDITA;
	}
        
        @RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute Institucion institucion,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina institucion");
		try {
			String nombre = institucionDao.elimina(id);

			redirectAttributes.addFlashAttribute("message",
					"institucion.eliminada.message");
			redirectAttributes.addFlashAttribute("messageAttrs",
					new String[] { nombre });
		} catch (Exception e) {
			log.error("No se pudo eliminar la institucion " + id, e);
			bindingResult
					.addError(new ObjectError("institucion",
							new String[] { "institucion.no.eliminada.message" },
							null, null));
			return Constantes.PATH_INSTITUCION_VER;
		}
                return "redirect:" + Constantes.PATH_INSTITUCION;
        }
    
}
