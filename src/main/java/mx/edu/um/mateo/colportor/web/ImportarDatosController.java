/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import mx.edu.um.mateo.general.web.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mx.edu.um.mateo.colportor.service.ImportarDatosManager;
import mx.edu.um.mateo.general.model.UploadFileForm;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author osoto
 */
@Controller
@RequestMapping("/colportaje/importarDatos")
public class ImportarDatosController extends BaseController {
    @Autowired
    private ImportarDatosManager mgr;
    
    @RequestMapping(value = {"","/listadoArchivos"})
    public String verArchivos(HttpServletRequest request, ModelMap model) throws Exception {
        
        List lista = new ArrayList();
        File dir = new File(request.getSession().getServletContext().getRealPath("") + "/resources/" + request.getRemoteUser());
        String [] children = dir.list();

        if(children == null){
            ;
        }
        else{
            children = dir.list();
            //log.debug(dir.list());
            UploadFileForm ev = null;
            for(int i = 0;i < children.length;i++){
                ev = new UploadFileForm();
                ev.setId(i);
                ev.setName(children[i]);
                lista.add(ev);
            }

        }
        request.getSession().setAttribute(Constantes.FILES_LIST, lista);
        model.addAttribute(Constantes.FILES_LIST, lista);
        
        return "/colportaje/importarDatos/listadoArchivos";
    }
    @RequestMapping(value = "/cargandoArchivo")
    public String cargarArchivo(HttpServletRequest request, ModelMap model) throws Exception{
        UploadFileForm archivo = null;
            List docs = new ArrayList();
            List files = (List)request.getSession().getAttribute(Constantes.FILES_LIST);
            String[] docIds = request.getParameterValues("archivo");
            if (docIds != null) {
                for (Integer i = 0; i < docIds.length; i++) {
                    //log.debug(i+", "+docIds[i]);
                }
                archivo = (UploadFileForm)files.get(Integer.parseInt(docIds[0]));
            }

            if(archivo == null){
                return "redirect"+"/importarDatos/listadoArchivos";
            }
            new File(request.getSession().getServletContext().getRealPath("") + "/resources/" + request.getRemoteUser()+"/"+archivo.getName());
            return "redirect"+"/importarDatos/listadoArchivos";
    }
}
