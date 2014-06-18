/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;

import java.io.File;
import javax.servlet.http.HttpServletRequest;
import mx.edu.um.mateo.general.model.UploadFileForm;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author osoto
 */
@Controller
@RequestMapping("/uploadFile")
public class UploadFileController extends BaseController {
    @Transactional
    @RequestMapping(value = {"","/archivo"}, method = RequestMethod.GET)
    public String fileToUpload(ModelMap model) throws Exception {
        UploadFileForm form = new UploadFileForm();
        model.addAttribute("uploadFileForm", form);
        return "/uploadFile/uploadFile";
    }
    @Transactional
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public String upload(HttpServletRequest request, @ModelAttribute("uploadFileForm") UploadFileForm uploadFileForm, 
        BindingResult bindingResult, Errors errors) throws Exception {
        
        despliegaBindingResultErrors(bindingResult);
        
        Boolean sw = false;
        
        //Subir archivo
        log.debug("file {}", uploadFileForm.getFile().getOriginalFilename());
        String uploadDir = request.getSession().getServletContext().getRealPath("") + "/resources/" + request.getRemoteUser() + "/" + uploadFileForm.getFile().getOriginalFilename();
        log.debug("upload dir {} ", uploadDir);
        File dirPath = new File(uploadDir);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        uploadFileForm.getFile().transferTo(new File(uploadDir));
        sw = true;
        log.debug("Archivo {} subido... ", uploadFileForm.getFile().getOriginalFilename());

        return "redirect:" + "/uploadFile";
    }
    
}
