package mx.edu.um.mateo.inscripciones.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.general.model.Organizacion;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inscripciones.model.Periodo;
import mx.edu.um.mateo.inscripciones.service.PeriodoManager;
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

/**
 * @author semdariobarbaamaya
 */
@Controller
@RequestMapping("/inscripciones/periodos")
public class PeriodoController extends BaseController{
    
    @Autowired
    private PeriodoManager periodoManager; 
    
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
		log.debug("Mostrando lista de periodos");
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
			params = periodoManager.lista(params);
			try {
				generaReporte(tipo, (List<Periodo>) params.get(Constantes.CONTAINSKEY_PERIODOS),
						response, "periodo", Constantes.EMP, empresaId);
				return null;
			} catch (ReporteException e) {
				log.error("No se pudo generar el reporte", e);
			}
		}

		if (StringUtils.isNotBlank(correo)) {
			params.put("reporte", true);
			params = periodoManager.lista(params);

			params.remove("reporte");
			try {
				enviaCorreo(correo, (List<Periodo>) params.get(Constantes.CONTAINSKEY_PERIODOS),
						request, "periodo", Constantes.EMP, empresaId);
				modelo.addAttribute("message", "lista.enviado.message");
				modelo.addAttribute(
						"messageAttrs",
						new String[] {
								messageSource.getMessage("periodo.lista.label",
										null, request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}
		params = periodoManager.lista(params);
                log.debug("Periodos {}", ((List)params.get(Constantes.CONTAINSKEY_PERIODOS)).size());
		modelo.addAttribute("periodo", params.get(Constantes.CONTAINSKEY_PERIODOS));

		this.pagina(params, modelo, Constantes.CONTAINSKEY_PERIODOS, pagina);

		return Constantes.PATH_PERIODOS_LISTA;                
	}
    
    
        @RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando periodo {}", id);
		Periodo periodo = periodoManager.obtiene(id);

		modelo.addAttribute("periodo", periodo);

		return Constantes.PATH_PERIODOS_VER;
	}
    
        @RequestMapping("/nuevo")
	public String nuevo(HttpServletRequest request, Model modelo) {
		log.debug("Nuevo periodo");
		Periodo periodo = new Periodo();
		modelo.addAttribute("periodo", periodo);

		Map<String, Object> params = new HashMap<>();
		params.put("empresa", request.getSession().getAttribute("empresaId"));
		params.put("reporte", true);

		return Constantes.PATH_PERIODOS_NUEVO;
	}
        
        @RequestMapping(value = "/graba", method = RequestMethod.POST)
	public String graba(HttpServletRequest request,
			HttpServletResponse response, @Valid Periodo periodo,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String descripcion : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", descripcion,
					request.getParameterMap().get(descripcion));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");

			Map<String, Object> params = new HashMap<>();
			params.put("empresa", ambiente.obtieneUsuario().getEmpresa());
			params.put("reporte", true);
			return Constantes.PATH_PERIODOS_NUEVO;
		}

		try {
                        if(periodo.getId()!=null){
                        /*
                        Usuario usuario = ambiente.obtieneUsuario();
                        periodo.setEmpresa(usuario.getEmpresa());
                        periodoDao.graba(periodo);
                        */
                            Periodo tmp = periodoManager.obtiene(periodo.getId());
                            tmp.setClave(periodo.getClave());
                            tmp.setDescripcion(periodo.getDescripcion());
                            tmp.setExcluye(periodo.getExcluye());
                            tmp.setIncluye(periodo.getIncluye());
                            tmp.setStatus(periodo.getStatus());
                            tmp.setFechaFinal(periodo.getFechaFinal());
                            tmp.setFechaInicial(periodo.getFechaInicial());
                            log.debug("Hasta aqui ENTRA {}", tmp);
                            periodoManager.graba(tmp);
                            
                            redirectAttributes.addFlashAttribute("message",
				"periodo.actualizado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { periodo.getDescripcion() });
                        }else{
                            
                            Usuario usuario = ambiente.obtieneUsuario();
                            periodo.setOrganizacion(usuario.getEmpresa().getOrganizacion());
                            periodoManager.graba(periodo);
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
				"periodo.creado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { periodo.getDescripcion() }); 
                            
                            
                        }
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear el periodo", e);
			errors.rejectValue("descripcion", "campo.duplicado.message",
					new String[] { "descripcion" }, null);

			Map<String, Object> params = new HashMap<>();
			params.put("empresa", request.getSession()
					.getAttribute("empresaId"));
			params.put("reporte", true);
			return Constantes.PATH_PERIODOS_NUEVO;
		}

		

		return "redirect:"+ Constantes.PATH_PERIODOS;
	}
        
        @RequestMapping("/edita/{id}")
	public String edita(HttpServletRequest request, @PathVariable Long id,
			Model modelo) {
		log.debug("Edita periodo {}", id);
		Periodo periodo = periodoManager.obtiene(id);
		modelo.addAttribute("periodo", periodo);

		Map<String, Object> params = new HashMap<>();
		params.put("empresa", request.getSession().getAttribute("empresaId"));
		params.put("reporte", true);
                
		return Constantes.PATH_PERIODOS_EDITA;
	}
           
        @RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute Periodo periodo,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina periodo");
		try {
			String descripcion = periodoManager.elimina(id);

			redirectAttributes.addFlashAttribute("message",
					"periodo.eliminado.message");
			redirectAttributes.addFlashAttribute("messageAttrs",
					new String[] { descripcion });
		} catch (Exception e) {
			log.error("No se pudo eliminar el periodo " + id, e);
			bindingResult
					.addError(new ObjectError("periodo",
							new String[] { "periodo.no.eliminado.message" },
							null, null));
			return Constantes.PATH_PERIODOS_VER;
		}
                return "redirect:" + Constantes.PATH_PERIODOS;
        }
}
