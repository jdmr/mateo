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
import mx.edu.um.mateo.colportor.dao.ReciboColportorDao;
import mx.edu.um.mateo.colportor.model.PedidoColportor;
import mx.edu.um.mateo.colportor.model.ReciboColportor;
import mx.edu.um.mateo.general.model.Usuario;
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
@RequestMapping("/colportaje/ventas/recibos")
public class ReciboColportorController extends BaseController{

    @Autowired
    private ReciboColportorDao reciboColportorDao;
    @Autowired
    private PedidoColportorDao pedidoColportorDao;

    @SuppressWarnings("unchecked")
    @RequestMapping("lista")
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
        log.debug("Mostrando lista de recibo colportor");
        
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
            params = reciboColportorDao.lista(params);
            try {
                generaReporte(tipo, (List<ReciboColportor>) params.get("recibosColportor"),
                        response, "recibosColportor", Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = reciboColportorDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<ReciboColportor>) params.get("recibosColportor"),
                        request, "recibosColportor", Constantes.EMP, empresaId);
                modelo.addAttribute("message", "lista.enviado.message");
                modelo.addAttribute(
                        "messageAttrs",
                        new String[]{
                            messageSource.getMessage("reciboColportor.lista.label",
                                    null, request.getLocale()),
                            ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = reciboColportorDao.lista(params);
        modelo.addAttribute(Constantes.RECIBO_COLPORTOR_LIST, params.get(Constantes.RECIBO_COLPORTOR_LIST));

        this.pagina(params, modelo, Constantes.RECIBO_COLPORTOR_LIST, pagina);

        return Constantes.RECIBO_COLPORTOR_PATH_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando reciboColportor {}", id);
        ReciboColportor reciboColportor = reciboColportorDao.obtiene(id);

        modelo.addAttribute(Constantes.RECIBO_COLPORTOR, reciboColportor);

        return Constantes.RECIBO_COLPORTOR_PATH_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo reciboColportor");
        ReciboColportor reciboColportor = new ReciboColportor();
        modelo.addAttribute(Constantes.RECIBO_COLPORTOR, reciboColportor);

        return Constantes.RECIBO_COLPORTOR_PATH_NUEVO;
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request,
            HttpServletResponse response, @Valid ReciboColportor reciboColportor,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            
            return Constantes.RECIBO_COLPORTOR_PATH_NUEVO;
        }

        try {
            //Se supone que el colportor lo registra
            Usuario usuario = ambiente.obtieneUsuario();
            reciboColportor.setPedido(pedidoColportorDao.obtiene(((PedidoColportor)request.getSession().getAttribute(Constantes.PEDIDO_COLPORTOR)).getId()));
            reciboColportor.setStatus(Constantes.STATUS_ACTIVO);
            reciboColportor = reciboColportorDao.crea(reciboColportor);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al reciboColportor", e);
            
            return Constantes.RECIBO_COLPORTOR_PATH_NUEVO;
        }

        redirectAttributes.addFlashAttribute("message",
                "reciboColportor.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{reciboColportor.getNumRecibo().toString()});

        return "redirect:" + Constantes.RECIBO_COLPORTOR_PATH_VER + "/" + reciboColportor.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable Long id,
            Model modelo) {
        log.debug("Edita reciboColportor {}", id);
        ReciboColportor reciboColportor = reciboColportorDao.obtiene(id);
        modelo.addAttribute("reciboColportor", reciboColportor);
        
        return Constantes.RECIBO_COLPORTOR_PATH_EDITA;
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid ReciboColportor reciboColportor,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");
            
            return Constantes.RECIBO_COLPORTOR_PATH_EDITA;
        }

        try {
            reciboColportor.setPedido(pedidoColportorDao.obtiene(((PedidoColportor)request.getSession().getAttribute(Constantes.PEDIDO_COLPORTOR)).getId()));
            reciboColportor = reciboColportorDao.actualiza(reciboColportor);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo actualizar la reciboColportor", e);
            
            return Constantes.RECIBO_COLPORTOR_PATH_EDITA;
        }

        redirectAttributes.addFlashAttribute("message",
                "reciboColportor.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{reciboColportor.getNumRecibo().toString()});

        return "redirect:" + Constantes.RECIBO_COLPORTOR_PATH_VER + "/" + reciboColportor.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id,
            Model modelo, @ModelAttribute ReciboColportor reciboColportor,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina reciboColportor");
        try {
            String nombre = reciboColportorDao.elimina(id);

            redirectAttributes.addFlashAttribute("message",
                    "reciboColportor.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la reciboColportor " + id, e);
            bindingResult.addError(new ObjectError("reciboColportor",
                    new String[]{"reciboColportor.no.eliminado.message"},
                    null, null));
            return Constantes.RECIBO_COLPORTOR_PATH_VER;
        }

        return "redirect:" + Constantes.RECIBO_COLPORTOR_PATH_LISTA
                +"?pedidoId="+(Long)((PedidoColportor)request.getSession().getAttribute(Constantes.PEDIDO_COLPORTOR)).getId();
    }
}
