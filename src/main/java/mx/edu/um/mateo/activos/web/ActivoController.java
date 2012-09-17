/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
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
package mx.edu.um.mateo.activos.web;

import java.io.IOException;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import mx.edu.um.mateo.activos.dao.ActivoDao;
import mx.edu.um.mateo.activos.dao.TipoActivoDao;
import mx.edu.um.mateo.activos.model.Activo;
import mx.edu.um.mateo.activos.model.BajaActivo;
import mx.edu.um.mateo.activos.model.ReubicacionActivo;
import mx.edu.um.mateo.activos.model.TipoActivo;
import mx.edu.um.mateo.activos.utils.ActivoNoCreadoException;
import mx.edu.um.mateo.contabilidad.dao.CentroCostoDao;
import mx.edu.um.mateo.contabilidad.model.CentroCosto;
import mx.edu.um.mateo.general.dao.ProveedorDao;
import mx.edu.um.mateo.general.model.Imagen;
import mx.edu.um.mateo.general.model.Proveedor;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Constantes;
import mx.edu.um.mateo.general.utils.LabelValueBean;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
@Controller
@RequestMapping("/activoFijo/activo")
public class ActivoController extends BaseController {

    @Autowired
    private ActivoDao activoDao;
    @Autowired
    private TipoActivoDao tipoActivoDao;
    @Autowired
    private CentroCostoDao centroCostoDao;
    @Autowired
    private ProveedorDao proveedorDao;

    @SuppressWarnings("unchecked")
    @RequestMapping
    public String lista(HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) String filtro,
            @RequestParam(required = false) Long pagina,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String correo,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) String sort,
            Model modelo)
            throws ParseException {
        log.debug("Mostrando lista de activos");
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Map<String, Object> params = this.convierteParams(request
                .getParameterMap());
        Long empresaId = (Long) request.getSession().getAttribute("empresaId");
        params.put("empresa", empresaId);

        if (params.containsKey("fechaIniciado")) {
            log.debug("FechaIniciado: {}", params.get("fechaIniciado"));
            params.put("fechaIniciado",
                    sdf.parse((String) params.get("fechaIniciado")));
        }

        if (params.containsKey("fechaTerminado")) {
            params.put("fechaTerminado",
                    sdf.parse((String) params.get("fechaTerminado")));
        }

        if (StringUtils.isNotBlank(tipo)) {
            params.put("reporte", true);
            params = activoDao.lista(params);
            try {
                generaReporte(tipo, (List<Activo>) params.get("activos"),
                        response, "activos", Constantes.EMP, empresaId);
                return null;
            } catch (ReporteException e) {
                log.error("No se pudo generar el reporte", e);
            }
        }

        if (StringUtils.isNotBlank(correo)) {
            params.put("reporte", true);
            params = activoDao.lista(params);

            params.remove("reporte");
            try {
                enviaCorreo(correo, (List<Activo>) params.get("activos"),
                        request, "activos", Constantes.EMP, empresaId);
                modelo.addAttribute("message", "lista.enviado.message");
                modelo.addAttribute(
                        "messageAttrs",
                        new String[]{
                            messageSource.getMessage("activo.lista.label",
                            null, request.getLocale()),
                            ambiente.obtieneUsuario().getUsername()});
            } catch (ReporteException e) {
                log.error("No se pudo enviar el reporte por correo", e);
            }
        }

        params = activoDao.lista(params);
        modelo.addAttribute("activos", params.get("activos"));
        modelo.addAttribute("resumen", params.get("resumen"));

        this.pagina(params, modelo, "activos", pagina);
        
        List<TipoActivo> tiposDeActivo = tipoActivoDao.lista(ambiente.obtieneUsuario());
        if (params.containsKey("tipoActivoIds")) {
            List<Long> ids = (List<Long>) params.get("tipoActivoIds");
            List<TipoActivo> seleccionados = new ArrayList<>();
            for(TipoActivo tipoActivo : tiposDeActivo) {
                if (ids.contains(tipoActivo.getId())) {
                    seleccionados.add(tipoActivo);
                }
            }
            tiposDeActivo.removeAll(seleccionados);
            modelo.addAttribute("seleccionados", seleccionados);
        }
        modelo.addAttribute("disponibles", tiposDeActivo);

        return "activoFijo/activo/lista";
    }

    @RequestMapping("/ver/{id}")
    public String ver(@PathVariable Long id, Model modelo) {
        log.debug("Mostrando activo {}", id);
        Activo activo = activoDao.obtiene(id);
        modelo.addAttribute("activo", activo);
        if (activo.getImagenes() != null & activo.getImagenes().size() > 0) {
            modelo.addAttribute("tieneImagenes", Boolean.TRUE);
        }
        if (!activo.getInactivo()) {
            modelo.addAttribute("puedeDarDeBaja", Boolean.TRUE);
        }

        return "activoFijo/activo/ver";
    }

    @RequestMapping("/nuevo")
    public String nuevo(HttpSession session, Model modelo) {
        log.debug("Nuevo activo");
        Long empresaId = (Long) session.getAttribute("empresaId");

        Activo activo = new Activo();
        modelo.addAttribute("activo", activo);

        List<String> motivos = new ArrayList<>();
        motivos.add("COMPRA");
        motivos.add("DONACION");
        modelo.addAttribute("motivos", motivos);

        Map<String, Object> params = new HashMap<>();
        params.put("empresa", empresaId);
        params.put("reporte", true);
        params = tipoActivoDao.lista(params);
        modelo.addAttribute("tiposDeActivo", params.get("tiposDeActivo"));

        List<CentroCosto> centrosDeCosto = activoDao.centrosDeCosto(ambiente
                .obtieneUsuario());
        modelo.addAttribute("centrosDeCosto", centrosDeCosto);

        return "activoFijo/activo/nuevo";
    }

    @RequestMapping(value = "/crea", method = RequestMethod.POST)
    public String crea(
            HttpServletRequest request,
            HttpServletResponse response,
            @Valid Activo activo,
            BindingResult bindingResult,
            Errors errors,
            Model modelo,
            RedirectAttributes redirectAttributes,
            @RequestParam(value = "imagen", required = false) MultipartFile archivo) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }
        if (bindingResult.hasErrors()) {
            log.debug("Hubo algun error en la forma, regresando");

            Long empresaId = (Long) request.getSession().getAttribute(
                    "empresaId");

            List<String> motivos = new ArrayList<>();
            motivos.add("COMPRA");
            motivos.add("DONACION");
            modelo.addAttribute("motivos", motivos);

            Map<String, Object> params = new HashMap<>();
            params.put("empresa", empresaId);
            params.put("reporte", true);
            params = tipoActivoDao.lista(params);
            modelo.addAttribute("tiposDeActivo", params.get("tiposDeActivo"));

            List<CentroCosto> centrosDeCosto = activoDao
                    .centrosDeCosto(ambiente.obtieneUsuario());
            modelo.addAttribute("centrosDeCosto", centrosDeCosto);

            return "activoFijo/activo/nuevo";
        }

        try {
            Usuario usuario = ambiente.obtieneUsuario();
            if (archivo != null && !archivo.isEmpty()) {
                Imagen imagen = new Imagen(archivo.getOriginalFilename(),
                        archivo.getContentType(), archivo.getSize(),
                        archivo.getBytes());
                activo.getImagenes().add(imagen);
            }
            log.debug("TipoActivo: {}", activo.getTipoActivo().getId());
            activo = activoDao.crea(activo, usuario);
        } catch (ConstraintViolationException | IOException e) {
            log.error("No se pudo crear al activo", e);
            errors.rejectValue("codigo", "campo.duplicado.message",
                    new String[]{"codigo"}, null);

            Long empresaId = (Long) request.getSession().getAttribute(
                    "empresaId");

            List<String> motivos = new ArrayList<>();
            motivos.add("COMPRA");
            motivos.add("DONACION");
            modelo.addAttribute("motivos", motivos);

            Map<String, Object> params = new HashMap<>();
            params.put("empresa", empresaId);
            params.put("reporte", true);
            params = tipoActivoDao.lista(params);
            modelo.addAttribute("tiposDeActivo", params.get("tiposDeActivo"));

            List<CentroCosto> centrosDeCosto = activoDao
                    .centrosDeCosto(ambiente.obtieneUsuario());
            modelo.addAttribute("centrosDeCosto", centrosDeCosto);

            return "activoFijo/activo/nuevo";
        }

        redirectAttributes
                .addFlashAttribute("message", "activo.creado.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{activo.getFolio()});

        return "redirect:/activoFijo/activo/ver/" + activo.getId();
    }

    @RequestMapping(value = "/preparaBaja", method = RequestMethod.POST)
    public String preparaBaja(HttpServletRequest request,
            @RequestParam Long id, Model modelo) {
        log.debug("Preparando para de baja al activo {}", id);
        Activo activo = activoDao.obtiene(id);
        BajaActivo bajaActivo = new BajaActivo(activo, new Date());
        modelo.addAttribute("bajaActivo", bajaActivo);
        List<String> motivos = new ArrayList<>();
        motivos.add("OBSOLETO");
        motivos.add("PERDIDA");
        motivos.add("DONACION");
        motivos.add("VENTA");
        modelo.addAttribute("motivos", motivos);

        return "activoFijo/activo/baja";
    }

    @RequestMapping(value = "/baja", method = RequestMethod.POST)
    public String baja(Model modelo, @ModelAttribute BajaActivo bajaActivo,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.debug("Dando de baja al activo {}", bajaActivo.getActivo().getId());
        try {
            Usuario usuario = ambiente.obtieneUsuario();
            String nombre = activoDao.baja(bajaActivo, usuario);

            redirectAttributes.addFlashAttribute("message",
                    "activo.baja.message");
            redirectAttributes.addFlashAttribute("messageAttrs",
                    new String[]{nombre});
        } catch (Exception e) {
            log.error("No se pudo dar de baja al activo", e);
            bindingResult.addError(new ObjectError("activo",
                    new String[]{"activo.no.baja.message"}, null, null));
            return "activoFijo/activo/ver";
        }

        return "redirect:/activoFijo/activo";
    }

    @RequestMapping("/arreglaFechas")
    public String arreglaFechas(HttpServletResponse response,
            RedirectAttributes redirectAttributes) {
        log.debug("Arreglando fechas");
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition",
                    "attachment; filename='arreglarFechas.xlsx'");
            activoDao.arreglaFechas(response.getOutputStream());
        } catch (IOException e) {
            log.error(
                    "Hubo un problema al intentar arreglar las fechas de los activos",
                    e);
        }
        redirectAttributes
                .addFlashAttribute("message", "activo.arregla.fechas");
        redirectAttributes.addFlashAttribute("messageStyle", "alert-success");
        return "redirect:/activoFijo/activo";
    }

    @RequestMapping(value = "/depreciar", method = RequestMethod.GET)
    public String preparaParaDepreciar() {
        return "activoFijo/activo/depreciar";
    }

    @RequestMapping(value = "/depreciar", method = RequestMethod.POST)
    public String depreciar(HttpSession session, @RequestParam String fecha,
            RedirectAttributes redirectAttributes) {
        log.debug("Depreciando activos");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy");
        Long empresaId = (Long) session.getAttribute("empresaId");
        Date fechaDepreciacion = new Date();
        try {
            fechaDepreciacion = sdf2.parse(fecha);
        } catch (ParseException e) {
            log.error("No se pudo convertir la fecha", e);
        }
        activoDao.depreciar(fechaDepreciacion, empresaId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        redirectAttributes.addFlashAttribute("message",
                "activo.depreciar.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{sdf.format(fechaDepreciacion)});
        redirectAttributes.addFlashAttribute("messageStyle", "alert-success");
        return "redirect:/activoFijo/activo";
    }

    @RequestMapping("/depreciar/{anio}/{mes}/{dia}")
    public String depreciarPorFecha(HttpSession session,
            @PathVariable Integer anio, @PathVariable Integer mes,
            @PathVariable Integer dia, RedirectAttributes redirectAttributes) {
        Long empresaId = (Long) session.getAttribute("empresaId");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, anio);
        cal.set(Calendar.MONTH, mes - 1);
        cal.set(Calendar.DAY_OF_MONTH, dia);
        Date fecha = cal.getTime();

        log.debug("Depreciando activos para la fecha {}", fecha);
        activoDao.depreciar(fecha, empresaId);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
        redirectAttributes.addFlashAttribute("message",
                "activo.depreciar.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{sdf.format(fecha)});
        redirectAttributes.addFlashAttribute("messageStyle", "alert-success");
        return "redirect:/activoFijo/activo";
    }

    @RequestMapping("/sube/{id}")
    public String sube(@PathVariable Long id, Model modelo) {
        Activo activo = activoDao.obtiene(id);
        modelo.addAttribute("activo", activo);
        return "activoFijo/activo/sube";
    }

    @RequestMapping("/subeImagen")
    public String subeImagen(
            @RequestParam Long activoId,
            @RequestParam(value = "imagen", required = false) MultipartFile archivo,
            RedirectAttributes redirectAttributes) {
        log.debug("Subiendo imagen para activo {}", activoId);
        try {
            if (archivo != null && !archivo.isEmpty()) {
                Usuario usuario = ambiente.obtieneUsuario();
                Activo activo = activoDao.obtiene(activoId);
                Imagen imagen = new Imagen(archivo.getOriginalFilename(),
                        archivo.getContentType(), archivo.getSize(),
                        archivo.getBytes());
                activo.getImagenes().add(imagen);
                activoDao.subeImagen(activo, usuario);
            }
            redirectAttributes.addFlashAttribute("message",
                    "activo.sube.imagen.message");
            redirectAttributes.addFlashAttribute("messageStyle",
                    "alert-success");
        } catch (IOException e) {
            log.error(
                    "Hubo un problema al intentar subir la imagen del activo",
                    e);
        }
        return "redirect:/activoFijo/activo/ver/" + activoId;
    }

    @RequestMapping(value = "/reubica/{id}", method = RequestMethod.GET)
    public String preparaReubicacion(@PathVariable Long id, Model modelo) {
        Activo activo = activoDao.obtiene(id);
        ReubicacionActivo reubicacion = new ReubicacionActivo(activo,
                new Date());
        modelo.addAttribute("reubicacion", reubicacion);
        List<CentroCosto> centrosDeCosto = activoDao.centrosDeCosto(ambiente
                .obtieneUsuario());
        modelo.addAttribute("centrosDeCosto", centrosDeCosto);

        return "activoFijo/activo/reubica";
    }

    @RequestMapping(value = "/reubica", method = RequestMethod.POST)
    public String reubica(Model modelo,
            @ModelAttribute ReubicacionActivo reubicacion,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "activoFijo/activo/reubica/"
                    + reubicacion.getActivo().getId();
        }

        Usuario usuario = ambiente.obtieneUsuario();
        String nombre = activoDao.reubica(reubicacion, usuario);
        redirectAttributes.addFlashAttribute("message",
                "activo.reubica.message");
        redirectAttributes.addFlashAttribute("messageAttrs",
                new String[]{nombre});

        return "redirect:/activoFijo/activo/ver/"
                + reubicacion.getActivo().getId();
    }

    @RequestMapping(value = "/subeActivos", method = RequestMethod.GET)
    public String preparaParaSubir() {
        return "activoFijo/activo/subeActivos";
    }

    @RequestMapping(value = "/subeActivos", method = RequestMethod.POST)
    public String sube(HttpServletResponse response,
            RedirectAttributes redirectAttributes, MultipartFile archivo,
            @RequestParam Integer codigo) throws IOException,
            ActivoNoCreadoException {
        redirectAttributes.addFlashAttribute("message",
                "activo.sube.archivo.message");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader(
                "Content-disposition",
                "attachment; filename='errores-"
                + archivo.getOriginalFilename() + "'");
        OutputStream out = response.getOutputStream();
        activoDao.sube(archivo.getBytes(), ambiente.obtieneUsuario(),
                response.getOutputStream(), codigo);
        out.flush();

        return "redirect:/activoFijo/activo";
    }

    @RequestMapping("/depreciacionAcumuladaPorCentroDeCosto")
    public String depreciacionAcumuladaPorCentroDeCosto(
            HttpServletRequest request,
            HttpServletResponse response,
            Model modelo,
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) Byte hojaCalculo) throws ParseException, IOException {
        log.debug("Depreciacion Acumulada por Centro de Costo {}", fecha);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        if (fecha != null) {
            Date date = sdf.parse(fecha);
            Map<String, Object> params = new HashMap<>();
            params.put("usuario", ambiente.obtieneUsuario());
            params.put("fecha", date);
            params = activoDao.depreciacionAcumuladaPorCentroDeCosto(params);
            if (hojaCalculo == 1) {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader(
                        "Content-disposition",
                        "attachment; filename='depreciacionAcumuladaPorCentroDeCosto-" + sdf2.format(date) + ".xlsx'");
                params.put("out", response.getOutputStream());
                activoDao.hojaCalculoDepreciacion(params);
                return null;
            }
            modelo.addAllAttributes(params);
            modelo.addAttribute("fecha", fecha);
            modelo.addAttribute("fechaParam", sdf2.format(date));
        } else {
            modelo.addAttribute("fecha", sdf.format(new Date()));
            modelo.addAttribute("fechaParam", sdf2.format(new Date()));

        }
        return "activoFijo/activo/depreciacionAcumuladaPorCentroDeCosto";
    }

    @RequestMapping("/depreciacionAcumuladaPorCentroDeCosto/{centroCostoId}/{fecha}")
    public String depreciacionAcumuladaPorCentroDeCostoDetalle(
            @PathVariable String centroCostoId, @PathVariable String fecha,
            Model modelo) throws ParseException {
        log.debug(
                "Detalle de Depreciacion Acumulada por Centro de Costo {} y fecha {}",
                centroCostoId, fecha);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (fecha != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("usuario", ambiente.obtieneUsuario());
            params.put("centroCostoId", centroCostoId);
            params.put("fecha", sdf.parse(fecha));
            params = activoDao
                    .depreciacionAcumuladaPorCentroDeCostoDetalle(params);
            modelo.addAllAttributes(params);
            modelo.addAttribute("fecha", fecha);
        } else {
            modelo.addAttribute("fecha", sdf.format(new Date()));
        }
        return "activoFijo/activo/depreciacionAcumuladaPorCentroDeCostoDetalle";
    }

    @RequestMapping("/depreciacionMensualPorCentroDeCosto")
    public String depreciacionMensualPorCentroDeCosto(
            HttpServletRequest request,
            HttpServletResponse response,
            Model modelo,
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) Byte hojaCalculo) throws ParseException, IOException {
        log.debug("Depreciacion Mensual por Centro de Costo {}", fecha);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        if (fecha != null) {
            Date date = sdf.parse(fecha);
            Map<String, Object> params = new HashMap<>();
            params.put("usuario", ambiente.obtieneUsuario());
            params.put("fecha", date);
            params = activoDao.depreciacionMensualPorCentroDeCosto(params);
            if (hojaCalculo == 1) {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader(
                        "Content-disposition",
                        "attachment; filename='depreciacionMensualPorCentroDeCosto-" + sdf2.format(date) + ".xlsx'");
                params.put("out", response.getOutputStream());
                activoDao.hojaCalculoDepreciacion(params);
                return null;
            }
            modelo.addAllAttributes(params);
            modelo.addAttribute("fecha", fecha);
            modelo.addAttribute("fechaParam", sdf2.format(date));
        } else {
            modelo.addAttribute("fecha", sdf.format(new Date()));
            modelo.addAttribute("fechaParam", sdf2.format(new Date()));

        }
        return "activoFijo/activo/depreciacionMensualPorCentroDeCosto";
    }

    @RequestMapping("/depreciacionMensualPorCentroDeCosto/{centroCostoId}/{fecha}")
    public String depreciacionMensualPorCentroDeCostoDetalle(
            @PathVariable String centroCostoId, @PathVariable String fecha,
            Model modelo) throws ParseException {
        log.debug(
                "Detalle de Depreciacion Mensual por Centro de Costo {} y fecha {}",
                centroCostoId, fecha);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (fecha != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("usuario", ambiente.obtieneUsuario());
            params.put("centroCostoId", centroCostoId);
            params.put("fecha", sdf.parse(fecha));
            params = activoDao
                    .depreciacionMensualPorCentroDeCostoDetalle(params);
            modelo.addAllAttributes(params);
            modelo.addAttribute("fecha", fecha);
        } else {
            modelo.addAttribute("fecha", sdf.format(new Date()));
        }
        return "activoFijo/activo/depreciacionMensualPorCentroDeCostoDetalle";
    }

    @RequestMapping("/depreciacionAcumuladaPorGrupo")
    public String depreciacionAcumuladaPorGrupo(
            HttpServletRequest request,
            HttpServletResponse response,
            Model modelo,
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) Byte hojaCalculo) throws ParseException, IOException {
        log.debug("Depreciacion Acumulada por Tipo de Activo {}", fecha);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        if (fecha != null) {
            Date date = sdf.parse(fecha);
            Map<String, Object> params = new HashMap<>();
            params.put("usuario", ambiente.obtieneUsuario());
            params.put("fecha", date);
            params = activoDao.depreciacionAcumuladaPorTipoActivo(params);
            if (hojaCalculo == 1) {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader(
                        "Content-disposition",
                        "attachment; filename='depreciacionAcumuladaYMensualPorGrupo-" + sdf2.format(date) + ".xlsx'");
                params.put("out", response.getOutputStream());
                activoDao.hojaCalculoDepreciacion(params);
                return null;
            }
            modelo.addAllAttributes(params);
            modelo.addAttribute("fecha", fecha);
            modelo.addAttribute("fechaParam", sdf2.format(date));
        } else {
            modelo.addAttribute("fecha", sdf.format(new Date()));
            modelo.addAttribute("fechaParam", sdf2.format(new Date()));

        }
        return "activoFijo/activo/depreciacionAcumuladaPorGrupo";
    }

    @RequestMapping("/depreciacionAcumuladaPorGrupo/{tipoActivoId}/{fecha}")
    public String depreciacionAcumuladaPorGrupoDetalle(
            @PathVariable String tipoActivoId, @PathVariable String fecha,
            Model modelo) throws ParseException {
        log.debug(
                "Detalle de Depreciacion Acumulada por TipoDeActivo {} y fecha {}",
                tipoActivoId, fecha);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        if (fecha != null) {
            Map<String, Object> params = new HashMap<>();
            params.put("usuario", ambiente.obtieneUsuario());
            params.put("tipoActivoId", tipoActivoId);
            params.put("fecha", sdf.parse(fecha));
            params = activoDao
                    .depreciacionAcumuladaPorTipoActivoDetalle(params);
            modelo.addAllAttributes(params);
            modelo.addAttribute("fecha", fecha);
        } else {
            modelo.addAttribute("fecha", sdf.format(new Date()));
        }
        return "activoFijo/activo/depreciacionAcumuladaPorGrupoDetalle";
    }

    @RequestMapping("/dia")
    public String dia(Model modelo,
            @RequestParam(required = false) Integer anio) throws ParseException {
        log.debug("Reporte DIA para el anio {}", anio);
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        if (anio != null) {
            Map<String, Object> params = activoDao.reporteDIA(anio,
                    ambiente.obtieneUsuario());
            modelo.addAllAttributes(params);
            modelo.addAttribute("anio", anio);
            modelo.addAttribute("year", nf.format(anio));
        } else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.YEAR, -1);
            int year = cal.get(Calendar.YEAR);
            modelo.addAttribute("anio", year);
            modelo.addAttribute("year", nf.format(year));
        }
        return "activoFijo/activo/dia";
    }

    @RequestMapping("/concentrado/depreciacionPorCentroDeCosto")
    public String concentradoDepreciacionPorCentroDeCosto(
            HttpServletRequest request,
            HttpServletResponse response,
            Model modelo,
            @RequestParam(required = false) String fecha,
            @RequestParam(required = false) Byte hojaCalculo) throws ParseException, IOException {
        log.debug("Concentrado de Depreciacion por Centro de Costo {}", fecha);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        if (fecha != null) {
            Date date = sdf.parse(fecha);
            Map<String, Object> params = new HashMap<>();
            params.put("usuario", ambiente.obtieneUsuario());
            params.put("fecha", date);
            params = activoDao.concentradoDepreciacionPorCentroDeCosto(params);
            if (hojaCalculo == 1) {
                response.setContentType("application/vnd.ms-excel");
                response.setHeader(
                        "Content-disposition",
                        "attachment; filename='concentradoDepreciacionPorCentroDeCosto-" + sdf2.format(date) + ".xlsx'");
                params.put("out", response.getOutputStream());
//                activoDao.hojaCalculoDepreciacion(params);
                return null;
            }
            modelo.addAllAttributes(params);
            modelo.addAttribute("fecha", fecha);
            modelo.addAttribute("fechaParam", sdf2.format(date));
        } else {
            modelo.addAttribute("fecha", sdf.format(new Date()));
            modelo.addAttribute("fechaParam", sdf2.format(new Date()));

        }
        return "activoFijo/activo/concentradoDepreciacionPorCentroDeCosto";
    }

    @RequestMapping(value = "/centrosDeCosto", params = "term", produces = "application/json")
    public @ResponseBody
    List<Map<String, String>> centrosDeCosto(HttpServletRequest request,
            @RequestParam("term") String filtro) {
        log.debug("Buscando Centros de Costo por {}", filtro);
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }

        List<CentroCosto> centrosDeCosto = centroCostoDao.buscaCentrosDeCostoPorEmpresa(filtro, ambiente.obtieneUsuario());
        List<Map<String, String>> resultados = new ArrayList<>();
        for (CentroCosto centroCosto : centrosDeCosto) {
            Map<String, String> map = new HashMap<>();
            map.put("id", centroCosto.getId().getIdCosto());
            map.put("value", centroCosto.getNombreCompleto());
            resultados.add(map);
        }

        return resultados;
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/proveedores", params = "term", produces = "application/json")
    public @ResponseBody
    List<LabelValueBean> proveedores(HttpServletRequest request,
            @RequestParam("term") String filtro) {
        for (String nombre : request.getParameterMap().keySet()) {
            log.debug("Param: {} : {}", nombre,
                    request.getParameterMap().get(nombre));
        }
        Map<String, Object> params = new HashMap<>();
        params.put("empresa", request.getSession().getAttribute("empresaId"));
        params.put("filtro", filtro);
        params = proveedorDao.lista(params);
        List<LabelValueBean> valores = new ArrayList<>();
        List<Proveedor> proveedores = (List<Proveedor>) params
                .get("proveedores");
        for (Proveedor proveedor : proveedores) {
            StringBuilder sb = new StringBuilder();
            sb.append(proveedor.getNombre());
            sb.append(" | ");
            sb.append(proveedor.getRfc());
            sb.append(" | ");
            sb.append(proveedor.getNombreCompleto());
            valores.add(new LabelValueBean(proveedor.getId(), sb.toString(),
                    proveedor.getNombre()));
        }
        return valores;
    }
}
