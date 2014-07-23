/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.colportor.dao.ClienteColportorDao;
import mx.edu.um.mateo.colportor.model.ClienteColportor;
import mx.edu.um.mateo.colportor.model.ProyectoColportor;
import mx.edu.um.mateo.general.dao.EmpresaDao;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.LabelValueBean;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
import org.apache.commons.lang.StringUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author osoto
 */
@Controller
@RequestMapping("/colportaje/ventas/clientes")
public class ClienteColportorController extends BaseController {
    @Autowired
    private ClienteColportorDao clienteColportorDao;
    @Autowired
    private EmpresaDao empresaDao;
    
    @SuppressWarnings("unchecked")
    @PreAuthorize("hasRole('ROLE_ASOC','ROLE_CLP')")
	@RequestMapping
	public String lista(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String filtro,
			@RequestParam(required = false) Long pagina,
			@RequestParam(required = false) String tipo,
			@RequestParam(required = false) String correo,
			@RequestParam(required = false) String order,
			@RequestParam(required = false) String sort, Model modelo) {
		log.debug("***Mostrando lista de clienteColportores");
		Map<String, Object> params = new HashMap<>();
		Long empresaId = (Long) request.getSession().getAttribute("empresaId");
		params.put("empresa", empresaId);
                params.put("usuario", ambiente.obtieneUsuario().getId());
                
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
			params = clienteColportorDao.lista(params);
			try {
				generaReporte(tipo, (List<ClienteColportor>) params.get("clienteColportores"),
						response, "clienteColportores", Constantes.EMP, empresaId);
				return null;
			} catch (ReporteException e) {
				log.error("No se pudo generar el reporte", e);
			}
		}

		if (StringUtils.isNotBlank(correo)) {
			params.put("reporte", true);
			params = clienteColportorDao.lista(params);

			params.remove("reporte");
			try {
				enviaCorreo(correo, (List<ClienteColportor>) params.get("clienteColportores"),
						request, "clienteColportores", Constantes.EMP, empresaId);
				modelo.addAttribute("message", "lista.enviado.message");
				modelo.addAttribute(
						"messageAttrs",
						new String[] {
								messageSource.getMessage("clienteColportor.lista.label",
										null, request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}
		params = clienteColportorDao.lista(params);
		modelo.addAttribute(Constantes.CLIENTE_COLPORTOR_LIST, params.get(Constantes.CLIENTE_COLPORTOR_LIST));

		this.pagina(params, modelo, Constantes.CLIENTE_COLPORTOR_LIST, pagina);

		return Constantes.CLIENTE_COLPORTOR_PATH_LISTA;
	}
        
        @PreAuthorize("hasRole('ROLE_ASOC', 'ROLE_CLP')")
    @RequestMapping(value="/get_cliente_list", method = RequestMethod.GET, headers="Accept=*/*", produces = "application/json")    
    public @ResponseBody 
    List <LabelValueBean> getClienteList(@RequestParam("term") String filtro, HttpServletResponse response){
        log.debug("Buscando clientes por {}", filtro);
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", ambiente.obtieneUsuario().getEmpresa().getId());
        params.put("usuario", ambiente.obtieneUsuario().getId());
        params.put("filtro", filtro);
        params.put("reportes","reportes");
        
        params = clienteColportorDao.lista(params);
        
        List <LabelValueBean> rValues = new ArrayList<>();
        List <ClienteColportor> clientes = (List <ClienteColportor>) params.get(Constantes.CLIENTE_COLPORTOR_LIST);
        for(ClienteColportor cl : clientes){
            log.debug("ClienteColportor {}", cl.getNombreCompleto());
            StringBuilder sb = new StringBuilder();
            sb.append(cl.getId());
            sb.append(" | ");
            sb.append(cl.getNombreCompleto());   
            //Por alguna razon, el jQuery toma el valor del attr value por default.
            //Asi que en el constructor invertimos los valores: como value va el string, y como nombre la clave
            rValues.add(new LabelValueBean(cl.getId(), sb.toString(), cl.getNombreCompleto()));
        }        
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        return rValues;        
    }

        @PreAuthorize("hasRole('ROLE_ASOC','ROLE_CLP')")
	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando clienteColportor {}", id);
		ClienteColportor clienteColportor = clienteColportorDao.obtiene(id);

		modelo.addAttribute(Constantes.CLIENTE_COLPORTOR, clienteColportor);

		return Constantes.CLIENTE_COLPORTOR_PATH_VER;
	}

        @PreAuthorize("hasRole('ROLE_ASOC','ROLE_CLP')")
	@RequestMapping("/nuevo")
	public String nuevo(HttpServletRequest request, Model modelo) {
		log.debug("Nuevo clienteColportor");
		ClienteColportor clienteColportor = new ClienteColportor();
		modelo.addAttribute(Constantes.CLIENTE_COLPORTOR, clienteColportor);

		return Constantes.CLIENTE_COLPORTOR_PATH_NUEVO;
	}

        @PreAuthorize("hasRole('ROLE_ASOC','ROLE_CLP')")
	@RequestMapping(value = "/crea", method = RequestMethod.POST)
	public String crea(HttpServletRequest request,
			HttpServletResponse response, @Valid ClienteColportor clienteColportor,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");
			return Constantes.CLIENTE_COLPORTOR_PATH_NUEVO;
		}

		try {
                        //Se supone que el colportor lo registra
			Usuario usuario = ambiente.obtieneUsuario();
                        clienteColportor.setUsuario(usuario);
                        clienteColportor.setEmpresa(usuario.getEmpresa());
                        clienteColportor.setStatus(Constantes.STATUS_ACTIVO);
			clienteColportor = clienteColportorDao.crea(clienteColportor);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear al clienteColportor", e);
			return Constantes.CLIENTE_COLPORTOR_PATH_NUEVO;
		}

		redirectAttributes.addFlashAttribute("message",
				"clienteColportor.creado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { clienteColportor.getNombre() });

		return "redirect:" + Constantes.CLIENTE_COLPORTOR_PATH_VER + "/" + clienteColportor.getId();
	}

        @PreAuthorize("hasRole('ROLE_ASOC','ROLE_CLP')")
	@RequestMapping("/edita/{id}")
	public String edita(HttpServletRequest request, @PathVariable Long id,
			Model modelo) {
		log.debug("Edita clienteColportor {}", id);
		ClienteColportor clienteColportor = clienteColportorDao.obtiene(id);
		modelo.addAttribute("clienteColportor", clienteColportor);

		return Constantes.CLIENTE_COLPORTOR_PATH_EDITA;
	}

        @PreAuthorize("hasRole('ROLE_ASOC','ROLE_CLP')")
	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
	public String actualiza(HttpServletRequest request, @Valid ClienteColportor clienteColportor,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.error("Hubo algun error en la forma, regresando");
			
			return Constantes.CLIENTE_COLPORTOR_PATH_EDITA;
		}

		try {
			Usuario usuario = ambiente.obtieneUsuario();
                        clienteColportor.setUsuario(usuario);
                        clienteColportor.setEmpresa(empresaDao.obtiene(clienteColportor.getEmpresa().getId()));
			clienteColportor = clienteColportorDao.actualiza(clienteColportor);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo actualizar la clienteColportor", e);
			return Constantes.CLIENTE_COLPORTOR_PATH_EDITA;
		}

		redirectAttributes.addFlashAttribute("message",
				"clienteColportor.actualizado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { clienteColportor.getNombre() });

		return "redirect:" + Constantes.CLIENTE_COLPORTOR_PATH_VER + "/" + clienteColportor.getId();
	}

        @PreAuthorize("hasRole('ROLE_ASOC','ROLE_CLP')")
	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute ClienteColportor clienteColportor,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina clienteColportor");
		try {
			String nombre = clienteColportorDao.elimina(id);

			redirectAttributes.addFlashAttribute("message",
					"clienteColportor.eliminado.message");
			redirectAttributes.addFlashAttribute("messageAttrs",
					new String[] { nombre });
		} catch (Exception e) {
			log.error("No se pudo eliminar la clienteColportor " + id, e);
			bindingResult.addError(new ObjectError("clienteColportor",
                            new String[] { "clienteColportor.no.eliminado.message" },
                                null, null));
			return Constantes.CLIENTE_COLPORTOR_PATH_VER;
		}

		return "redirect:"+Constantes.CLIENTE_COLPORTOR_PATH;
	}
}
