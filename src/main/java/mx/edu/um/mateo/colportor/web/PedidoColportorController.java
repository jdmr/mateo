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
import mx.edu.um.mateo.colportor.dao.PedidoColportorDao;
import mx.edu.um.mateo.colportor.dao.PedidoColportorItemDao;
import mx.edu.um.mateo.colportor.model.FormaPago;
import mx.edu.um.mateo.colportor.model.PedidoColportor;
import mx.edu.um.mateo.colportor.model.PedidoColportorItem;
import mx.edu.um.mateo.colportor.model.PedidoColportorVO;
import mx.edu.um.mateo.general.dao.UsuarioDao;
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
@RequestMapping("/colportaje/ventas/pedidos")
public class PedidoColportorController extends BaseController{

    @Autowired
    private PedidoColportorDao pedidoColportorDao;
    @Autowired
    private UsuarioDao colportorDao;
    @Autowired
    private ClienteColportorDao clienteColportorDao;
    @Autowired
    private PedidoColportorItemDao pedidoColportorItemDao;

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
        log.debug("Mostrando lista de clienteColportores");
        
        //Se remueve el pedido de session
        request.getSession().removeAttribute(Constantes.PEDIDO_COLPORTOR);
        
        Map<String, Object> params = new HashMap<>();
        Long empresaId = (Long) request.getSession().getAttribute("empresaId");
        params.put("empresa", empresaId);
        Long colportorId = colportorDao.obtiene(ambiente.obtieneUsuario().getId()).getId();
        params.put("colportor", colportorId);

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
            params = pedidoColportorDao.lista(params);
            try {
                generaReporte(tipo, (List<PedidoColportor>) params.get("clienteColportores"),
                        response, "clienteColportores", Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = pedidoColportorDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<PedidoColportor>) params.get("clienteColportores"),
                        request, "clienteColportores", Constantes.EMP, empresaId);
                modelo.addAttribute("message", "lista.enviado.message");
                modelo.addAttribute(
                        "messageAttrs",
                        new String[]{
                            messageSource.getMessage("pedidoColportor.lista.label",
                                    null, request.getLocale()),
                            ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }
        params = pedidoColportorDao.lista(params);
        modelo.addAttribute(Constantes.PEDIDO_COLPORTOR_LIST, params.get(Constantes.PEDIDO_COLPORTOR_LIST));

        this.pagina(params, modelo, Constantes.PEDIDO_COLPORTOR_LIST, pagina);

        return Constantes.PEDIDO_COLPORTOR_PATH_LISTA;
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando pedidoColportor {}", id);
        PedidoColportor pedidoColportor = pedidoColportorDao.obtiene(id);

        modelo.addAttribute(Constantes.PEDIDO_COLPORTOR, pedidoColportor);

        return Constantes.PEDIDO_COLPORTOR_PATH_VER;
    }

    @RequestMapping("/nuevo")
    public String nuevo(HttpServletRequest request, Model modelo) {
        log.debug("Nuevo pedidoColportor");
        PedidoColportor pedidoColportor = new PedidoColportor();
        modelo.addAttribute(Constantes.PEDIDO_COLPORTOR, pedidoColportor);
        
        //Obtener la lista de clientes
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession().getAttribute("empresaId"));
        params.put("reporte", "reporte");
        modelo.addAttribute(Constantes.CLIENTE_COLPORTOR_LIST, clienteColportorDao.lista(params).get(Constantes.CLIENTE_COLPORTOR_LIST));
        
        modelo.addAttribute("formasPago", FormaPago.values());

        return Constantes.PEDIDO_COLPORTOR_PATH_NUEVO;
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(HttpServletRequest request,
            HttpServletResponse response, @Valid PedidoColportor pedidoColportor,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");
            
            //Obtener la lista de clientes
            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession().getAttribute("empresaId"));
            modelo.addAttribute(Constantes.CLIENTE_COLPORTOR_LIST, clienteColportorDao.lista(params).get(Constantes.CLIENTE_COLPORTOR_LIST));
            modelo.addAttribute("formasPago", FormaPago.values());
            
            return Constantes.PEDIDO_COLPORTOR_PATH_NUEVO;
        }

        try {
            //Se supone que el colportor lo registra
            Usuario usuario = ambiente.obtieneUsuario();
            pedidoColportor.setColportor(colportorDao.obtiene(ambiente.obtieneUsuario().getId()));
            pedidoColportor.setCliente(clienteColportorDao.obtiene(pedidoColportor.getCliente().getId()));
            pedidoColportor.setStatus(Constantes.STATUS_ACTIVO);
            pedidoColportor = pedidoColportorDao.crea(pedidoColportor);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo crear al pedidoColportor", e);
            
            //Obtener la lista de clientes
            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession().getAttribute("empresaId"));
            modelo.addAttribute(Constantes.CLIENTE_COLPORTOR_LIST, clienteColportorDao.lista(params).get(Constantes.CLIENTE_COLPORTOR_LIST));
            
            modelo.addAttribute("formasPago", FormaPago.values());
            
            return Constantes.PEDIDO_COLPORTOR_PATH_NUEVO;
        }

        redirectAttributes.addFlashAttribute("message",
                "pedidoColportor.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{pedidoColportor.getNumPedido()});

        return "redirect:" + Constantes.PEDIDO_COLPORTOR_PATH_VER + "/" + pedidoColportor.getId();
    }

    @RequestMapping("/edita/{id}")
    public String edita(HttpServletRequest request, @PathVariable Long id,
            Model modelo) {
        log.debug("Edita pedidoColportor {}", id);
        PedidoColportor pedidoColportor = pedidoColportorDao.obtiene(id);
        modelo.addAttribute("pedidoColportor", pedidoColportor);
        
        //Obtener la lista de clientes
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession().getAttribute("empresaId"));
        modelo.addAttribute(Constantes.CLIENTE_COLPORTOR_LIST, clienteColportorDao.lista(params).get(Constantes.CLIENTE_COLPORTOR_LIST));

        modelo.addAttribute("formasPago", FormaPago.values());
        
        return Constantes.PEDIDO_COLPORTOR_PATH_EDITA;
    }

    @RequestMapping(value = "/actualiza", method = RequestMethod.POST)
    public String actualiza(HttpServletRequest request, @Valid PedidoColportor pedidoColportor,
            BindingResult bindingResult, Errors errors, Model modelo,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            log.error("Hubo algun error en la forma, regresando");

            //Obtener la lista de clientes
            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession().getAttribute("empresaId"));
            modelo.addAttribute(Constantes.CLIENTE_COLPORTOR_LIST, clienteColportorDao.lista(params));
            
            modelo.addAttribute("formasPago", FormaPago.values());
            
            return Constantes.PEDIDO_COLPORTOR_PATH_EDITA;
        }

        try {
            pedidoColportor.setColportor(colportorDao.obtiene(ambiente.obtieneUsuario().getId()));
            pedidoColportor.setCliente(clienteColportorDao.obtiene(pedidoColportor.getCliente().getId()));
            pedidoColportor = pedidoColportorDao.actualiza(pedidoColportor);
        } catch (ConstraintViolationException e) {
            log.error("No se pudo actualizar la pedidoColportor", e);
            
            //Obtener la lista de clientes
            Map<String, Object> params = new HashMap<>();
            params.put("empresa", request.getSession().getAttribute("empresaId"));
            modelo.addAttribute(Constantes.CLIENTE_COLPORTOR_LIST, clienteColportorDao.lista(params));
            
            modelo.addAttribute("formasPago", FormaPago.values());
            
            return Constantes.PEDIDO_COLPORTOR_PATH_EDITA;
        }

        redirectAttributes.addFlashAttribute("message",
                "pedidoColportor.actualizado.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{pedidoColportor.getNumPedido()});

        return "redirect:" + Constantes.PEDIDO_COLPORTOR_PATH_VER + "/" + pedidoColportor.getId();
    }

    @RequestMapping(value = "/elimina", method = RequestMethod.POST)
    public String elimina(HttpServletRequest request, @RequestParam Long id,
            Model modelo, @ModelAttribute PedidoColportor pedidoColportor,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Elimina pedidoColportor");
        try {
            String nombre = pedidoColportorDao.elimina(id);

            redirectAttributes.addFlashAttribute("message",
                    "pedidoColportor.eliminado.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo eliminar la pedidoColportor " + id, e);
            bindingResult.addError(new ObjectError("pedidoColportor",
                    new String[]{"pedidoColportor.no.eliminado.message"},
                    null, null));
            return Constantes.PEDIDO_COLPORTOR_PATH_VER;
        }

        return "redirect:" + Constantes.PEDIDO_COLPORTOR_PATH;
    }
    
    @RequestMapping("/finalizar/{id}")
    public String finalizar(HttpServletRequest request, @PathVariable Long id, Model modelo) {
        log.debug("Finalizando pedidoColportor {}", id);
        PedidoColportor pedidoColportor = pedidoColportorDao.obtiene(id);
        
        Map<String, Object> params = new HashMap<>();
        params.put("pedido", id);        
        params = pedidoColportorItemDao.lista(params);
        
        //Guardar los VO en params
        List <PedidoColportorVO> voList = new ArrayList<>();
        PedidoColportorVO vo = null;
        for(PedidoColportorItem pci : (List<PedidoColportorItem>)params.get(Constantes.PEDIDO_COLPORTOR_ITEM_LIST)){
            vo = new PedidoColportorVO();
            vo.setNumPedido(pedidoColportor.getNumPedido());
            vo.setFormaPago(pedidoColportor.getFormaPago());
            vo.setFechaPedido(pedidoColportor.getFechaPedido());
            vo.setFechaEntrega(pedidoColportor.getFechaEntrega());
            vo.setColportor(pedidoColportor.getColportor());
            vo.setItem(pci);
            voList.add(vo);
        }
        
        Usuario usuario = ambiente.obtieneUsuario();

        try {
            log.debug("enviaCorreo");
            enviaCorreo("PDF", voList,
                    request, "pedidoColportor", Constantes.EMP, usuario.getEmpresa().getId());
            modelo.addAttribute("message", "lista.enviado.message");
            modelo.addAttribute(
                    "messageAttrs",
                    new String[]{
                        messageSource.getMessage("pedidoColportor.lista.label",
                                null, request.getLocale()),
                        ambiente.obtieneUsuario().getUsername()});
        } catch (ReporteException e) {
            log.error("No se pudo enviar el reporte por correo", e);
        }

        return "redirect:" + Constantes.PEDIDO_COLPORTOR_PATH;
    }
}
