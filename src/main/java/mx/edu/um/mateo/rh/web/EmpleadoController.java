/*
 * The MIT License
 *
 * Copyright 2012 jdmr.
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
package mx.edu.um.mateo.rh.web;

import mx.edu.um.mateo.general.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author AMDA
 */

@Controller
@RequestMapping("/rh/empleado")
public class EmpleadoController extends BaseController {
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

