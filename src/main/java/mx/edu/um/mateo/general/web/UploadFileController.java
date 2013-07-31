/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.web;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import mx.edu.um.mateo.general.model.UploadFileForm;
import mx.edu.um.mateo.inscripciones.model.FileUploadForm;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

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
        
        //Subir archivos
       // List<MultipartFile> files = uploadFileForm.getFiles();
       // log.debug("files {}", files);
        log.debug("file {}", uploadFileForm.getFile().getOriginalFilename());
        String uploadDir = request.getSession().getServletContext().getRealPath("") + "/" + request.getRemoteUser() + "/" + uploadFileForm.getFile().getOriginalFilename();
        log.debug("upload dir {} ", uploadDir);
        File dirPath = new File(uploadDir);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }

        uploadFileForm.getFile().transferTo(new File(uploadDir));
        sw = true;
        log.debug("Archivo {} subido... ", uploadFileForm.getFile().getOriginalFilename());
/*
        if (null != files && files.size() > 0) {
            sw = false;
            for (MultipartFile multipartFile : files) {
                String fileName = multipartFile.getOriginalFilename();
                String uploadDir = request.getContextPath() + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename();
                log.debug("upload dir {} ", uploadDir);
                
                File dirPath = new File(uploadDir);
                if (!dirPath.exists()) {
                    dirPath.mkdirs();
                }
                
                multipartFile.transferTo(new File(uploadDir));
                sw = true;
                log.debug("Archivo {} subido... ", fileName);
            }
        }
        */
        return "redirect:" + "/uploadFile";
    }
}
