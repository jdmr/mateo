/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.colportor.web;

import mx.edu.um.mateo.general.web.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import mx.edu.um.mateo.colportor.service.ImportarDatosManager;
import mx.edu.um.mateo.general.model.UploadFileForm;
import mx.edu.um.mateo.general.utils.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
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
            Enumeration docIds = request.getParameterNames();
            if (docIds != null) {
                String str = null;
                while(docIds.hasMoreElements()){
                    str = (String)docIds.nextElement();
                    if(str.startsWith("chk")){
                        log.debug(str);
                        archivo = (UploadFileForm)files.get(Integer.parseInt(str.split("-")[1]));
                    }
                }
            }

            if(archivo == null){
                log.error("archivo null");
                return "redirect:"+"/colportaje/importarDatos/listadoArchivos";
            }
            File file = new File(request.getSession().getServletContext().getRealPath("") + "/resources/" + request.getRemoteUser()+"/"+archivo.getName());
            mgr.importaInformeDeGema(file, ambiente.obtieneUsuario());
            
            return "redirect:"+"/colportaje/importarDatos/listadoArchivos";
    }
}
