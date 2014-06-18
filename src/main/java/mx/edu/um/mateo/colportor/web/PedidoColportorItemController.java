/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.colportor.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import mx.edu.um.mateo.colportor.dao.PedidoColportorDao;
import mx.edu.um.mateo.colportor.dao.PedidoColportorItemDao;
import mx.edu.um.mateo.colportor.model.PedidoColportor;
import mx.edu.um.mateo.colportor.model.PedidoColportorItem;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.ReporteException;
import mx.edu.um.mateo.general.web.BaseController;
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
 *
 * @author osoto
 */
@Controller
@RequestMapping("/colportaje/ventas/items")
public class PedidoColportorItemController extends BaseController{
    @Autowired
    private PedidoColportorItemDao pedidoColportorItemDao;
    @Autowired
    private PedidoColportorDao pedidoColportorDao;
    
    @SuppressWarnings("unchecked")
	@RequestMapping({"", "/lista"})
	public String lista(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(required = false) String filtro,
			@RequestParam(required = false) Long pagina,
			@RequestParam(required = false) String tipo,
			@RequestParam(required = false) String correo,
			@RequestParam(required = false) String order,
			@RequestParam(required = false) String sort, 
			@RequestParam(required = true) Long pedidoId, 
                        Model modelo) {
		log.debug("Mostrando lista de pedidoColportorItemes");
                
                request.getSession().setAttribute(Constantes.PEDIDO_COLPORTOR, pedidoColportorDao.obtiene(pedidoId));
                
		Map<String, Object> params = new HashMap<>();
		Long empresaId = (Long) request.getSession().getAttribute("empresaId");
		params.put("empresa", empresaId);
		params.put("pedido", ((PedidoColportor)request.getSession().getAttribute(Constantes.PEDIDO_COLPORTOR)).getId());
                
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
			params = pedidoColportorItemDao.lista(params);
			try {
				generaReporte(tipo, (List<PedidoColportorItem>) params.get(Constantes.PEDIDO_COLPORTOR_ITEM_LIST),
						response, Constantes.PEDIDO_COLPORTOR_ITEM_LIST, Constantes.EMP, empresaId);
				return null;
			} catch (ReporteException e) {
				log.error("No se pudo generar el reporte", e);
			}
		}

		if (StringUtils.isNotBlank(correo)) {
			params.put("reporte", true);
			params = pedidoColportorItemDao.lista(params);

			params.remove("reporte");
			try {
				enviaCorreo(correo, (List<PedidoColportorItem>) params.get(Constantes.PEDIDO_COLPORTOR_ITEM_LIST),
						request, Constantes.PEDIDO_COLPORTOR_ITEM_LIST, Constantes.EMP, empresaId);
				modelo.addAttribute("message", "lista.enviado.message");
				modelo.addAttribute(
						"messageAttrs",
						new String[] {
								messageSource.getMessage("pedidoColportorItem.lista.label",
										null, request.getLocale()),
								ambiente.obtieneUsuario().getUsername() });
			} catch (ReporteException e) {
				log.error("No se pudo enviar el reporte por correo", e);
			}
		}
		params = pedidoColportorItemDao.lista(params);
		modelo.addAttribute(Constantes.PEDIDO_COLPORTOR_ITEM_LIST, params.get(Constantes.PEDIDO_COLPORTOR_ITEM_LIST));

		this.pagina(params, modelo, Constantes.PEDIDO_COLPORTOR_ITEM_LIST, pagina);
                
                

		return Constantes.PEDIDO_COLPORTOR_ITEM_PATH_LISTA;
	}

	@RequestMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Model modelo) {
		log.debug("Mostrando pedidoColportorItem {}", id);
		PedidoColportorItem pedidoColportorItem = pedidoColportorItemDao.obtiene(id);

		modelo.addAttribute(Constantes.PEDIDO_COLPORTOR_ITEM, pedidoColportorItem);

		return Constantes.PEDIDO_COLPORTOR_ITEM_PATH_VER;
	}

	@RequestMapping("/nuevo")
	public String nuevo(HttpServletRequest request, Model modelo) {
		log.debug("Nuevo pedidoColportorItem ");
		PedidoColportorItem pedidoColportorItem = new PedidoColportorItem();
		modelo.addAttribute(Constantes.PEDIDO_COLPORTOR_ITEM, pedidoColportorItem);

		return Constantes.PEDIDO_COLPORTOR_ITEM_PATH_NUEVO;
	}

	@RequestMapping(value = "/crea", method = RequestMethod.POST)
	public String crea(HttpServletRequest request,
			HttpServletResponse response, @Valid PedidoColportorItem pedidoColportorItem,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		for (String nombre : request.getParameterMap().keySet()) {
			log.debug("Param: {} : {}", nombre,
					request.getParameterMap().get(nombre));
		}
		if (bindingResult.hasErrors()) {
			log.debug("Hubo algun error en la forma, regresando");
			return Constantes.PEDIDO_COLPORTOR_ITEM_PATH_NUEVO;
		}

		try {
                        //Se supone que el colportor lo registra
			pedidoColportorItem.setPedido(pedidoColportorDao
                                .obtiene(((PedidoColportor)request.getSession().getAttribute(Constantes.PEDIDO_COLPORTOR)).getId()));
                        pedidoColportorItem.setStatus(Constantes.STATUS_ACTIVO);
			pedidoColportorItem = pedidoColportorItemDao.crea(pedidoColportorItem);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo crear al pedidoColportorItem", e);
			return Constantes.PEDIDO_COLPORTOR_ITEM_PATH_NUEVO;
		}

		redirectAttributes.addFlashAttribute("message",
				"pedidoColportorItem.creado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { pedidoColportorItem.getItem()});

		return "redirect:" + Constantes.PEDIDO_COLPORTOR_ITEM_PATH_VER + "/" + pedidoColportorItem.getId();
	}

	@RequestMapping("/edita/{id}")
	public String edita(HttpServletRequest request, @PathVariable Long id,
			Model modelo) {
		log.debug("Edita pedidoColportorItem {}", id);
		PedidoColportorItem pedidoColportorItem = pedidoColportorItemDao.obtiene(id);
		modelo.addAttribute("pedidoColportorItem", pedidoColportorItem);

		return Constantes.PEDIDO_COLPORTOR_ITEM_PATH_EDITA;
	}

	@RequestMapping(value = "/actualiza", method = RequestMethod.POST)
	public String actualiza(HttpServletRequest request, @Valid PedidoColportorItem pedidoColportorItem,
			BindingResult bindingResult, Errors errors, Model modelo,
			RedirectAttributes redirectAttributes) {
		if (bindingResult.hasErrors()) {
			log.error("Hubo algun error en la forma, regresando");
			
			return Constantes.PEDIDO_COLPORTOR_ITEM_PATH_EDITA;
		}

		try {
			pedidoColportorItem.setPedido(pedidoColportorDao
                                .obtiene(((PedidoColportor)request.getSession().getAttribute(Constantes.PEDIDO_COLPORTOR)).getId()));
			pedidoColportorItem = pedidoColportorItemDao.actualiza(pedidoColportorItem);
		} catch (ConstraintViolationException e) {
			log.error("No se pudo actualizar la pedidoColportorItem", e);
			return Constantes.PEDIDO_COLPORTOR_ITEM_PATH_EDITA;
		}

		redirectAttributes.addFlashAttribute("message",
				"pedidoColportorItem.actualizado.message");
		redirectAttributes.addFlashAttribute("messageAttrs",
				new String[] { pedidoColportorItem.getItem()});

		return "redirect:" + Constantes.PEDIDO_COLPORTOR_ITEM_PATH_VER + "/" + pedidoColportorItem.getId();
	}

	@RequestMapping(value = "/elimina", method = RequestMethod.POST)
	public String elimina(HttpServletRequest request, @RequestParam Long id,
			Model modelo, @ModelAttribute PedidoColportorItem pedidoColportorItem,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		log.debug("Elimina pedidoColportorItem");
		try {
			String nombre = pedidoColportorItemDao.elimina(id);

			redirectAttributes.addFlashAttribute("message",
					"pedidoColportorItem.eliminado.message");
			redirectAttributes.addFlashAttribute("messageAttrs",
					new String[] { nombre });
		} catch (Exception e) {
			log.error("No se pudo eliminar la pedidoColportorItem " + id, e);
			bindingResult.addError(new ObjectError("pedidoColportorItem",
                            new String[] { "pedidoColportorItem.no.eliminado.message" },
                                null, null));
			return Constantes.PEDIDO_COLPORTOR_ITEM_PATH_VER;
		}

		return "redirect:"+Constantes.PEDIDO_COLPORTOR_ITEM_PATH_LISTA+"?pedidoId="
                        +((PedidoColportor)request.getSession().getAttribute(Constantes.PEDIDO_COLPORTOR)).getId();
	}
}
