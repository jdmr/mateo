/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.webflow.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleado;
import mx.edu.um.mateo.contabilidad.facturas.model.InformeEmpleadoDetalle;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeEmpleadoDetalleManager;
import mx.edu.um.mateo.contabilidad.facturas.service.InformeEmpleadoManager;
import mx.edu.um.mateo.general.model.Usuario;
import mx.edu.um.mateo.general.utils.Ambiente;
import mx.edu.um.mateo.general.utils.AutorizacionCCPlInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 *
 * @author develop
 */
@Component
public class InformeEmpleadoAction extends MultiAction {

    protected final transient Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private InformeEmpleadoManager instance;
    @Autowired
    protected Ambiente ambiente;
    @Autowired
    private InformeEmpleadoDetalleManager detalleManager;
    ///Archivos para subir

    public Event creaInforme(RequestContext context) {

        Usuario usuario = ambiente.obtieneUsuario();
        InformeEmpleado informeEmpleado = (InformeEmpleado) context.getFlowScope().get("informeEmpleado");
        log.debug("informeEmpleadoAction{}", informeEmpleado);
        instance.crea(informeEmpleado, usuario);
        context.getFlowScope().put("informeEmpleadoId", informeEmpleado.getId());
        return success();
    }

    public Event creaInformeDetalle(RequestContext context) throws AutorizacionCCPlInvalidoException {

        Usuario usuario = ambiente.obtieneUsuario();
        InformeEmpleadoDetalle informeEmpleadoDetalle = (InformeEmpleadoDetalle) context.getFlowScope().get("informeEmpleadoDetalle");
        log.debug("informeEmpleadoDetalleAction{}", informeEmpleadoDetalle);
        detalleManager.crea(informeEmpleadoDetalle, usuario);
        return success();
    }

    public Event processFile(RequestContext context) throws AutorizacionCCPlInvalidoException, IOException {
//        String name = file.getName();
//        log.debug("nombre archivo{}", name);
        List<MultipartFile> files = new ArrayList<>();
        List<String> fileNames = new ArrayList<String>();
        Usuario usuario = ambiente.obtieneUsuario();
        InformeEmpleadoDetalle informeEmpleadoDetalle = (InformeEmpleadoDetalle) context.getFlowScope().get("informeEmpleadoDetalle");
        files.add(informeEmpleadoDetalle.getFile());
        files.add(informeEmpleadoDetalle.getFile2());
        Long id = (Long) context.getFlowScope().get("informeEmpleadoId");
        InformeEmpleado informeEmpleado = instance.obtiene(id);
        informeEmpleadoDetalle.setInformeEmpleado(informeEmpleado);
        log.debug("informeEmpleadoDetalleAction{}", informeEmpleadoDetalle);
        detalleManager.crea(informeEmpleadoDetalle, usuario);
        Calendar calendar = GregorianCalendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DATE);
        String nombre = context.getExternalContext().getCurrentUser().getName();
        if (null != files && files.size() > 0) {
            for (MultipartFile multipartFile : files) {
                String fileName = multipartFile.getOriginalFilename();
                fileNames.add(fileName);
                String uploadDir = "/home/facturas/" + año + "/" + mes + "/" + dia + "/" + nombre + "/" + multipartFile.getOriginalFilename();
                File dirPath = new File(uploadDir);
                if (!dirPath.exists()) {
                    dirPath.mkdirs();
                }
                multipartFile.transferTo(new File("/home/facturas/" + año + "/" + mes + "/" + dia + "/" + nombre + "/" + multipartFile.getOriginalFilename()));
                if (multipartFile.getOriginalFilename().contains(".pdf")) {
                    informeEmpleadoDetalle.setPathPDF("/home/facturas/" + año + "/" + mes + "/" + dia + "/" + nombre + "/" + multipartFile.getOriginalFilename());
                    informeEmpleadoDetalle.setNombrePDF(multipartFile.getOriginalFilename());
                }
                if (multipartFile.getOriginalFilename().contains(".xml")) {
                    informeEmpleadoDetalle.setPathXMl("/home/facturas/" + año + "/" + mes + "/" + dia + "/" + nombre + "/" + multipartFile.getOriginalFilename());
                    informeEmpleadoDetalle.setNombreXMl(multipartFile.getOriginalFilename());
                }
            }
        }
        return success();

    }

//    public void setFile(MultipartFile file) {
//        this.file = file;
//    }
}
