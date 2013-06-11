/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import mx.edu.um.mateo.general.web.BaseController;
import mx.edu.um.mateo.inscripciones.model.FileUploadForm;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;    // Apache commons IO
import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author develop
 */
@Controller
@RequestMapping("/inscripciones/uploadFiles")
public class FileUploadController extends BaseController {
//     private String saveDirectory = "D:/Test/Upload/";

    @RequestMapping(value = "/show", method = RequestMethod.GET)
    public String displayForm() {
        return "/inscripciones/uploadFiles/file_upload_form";
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(HttpServletRequest request,
            @ModelAttribute("uploadForm") FileUploadForm uploadForm,
            Model map) throws IOException {

        List<MultipartFile> files = uploadForm.getFiles();

        List<String> fileNames = new ArrayList<String>();

        if (null != files && files.size() > 0) {
            for (MultipartFile multipartFile : files) {

                String fileName = multipartFile.getOriginalFilename();
                fileNames.add(fileName);
                //Handle file content - multipartFile.getInputStream()
                String uploadDir = "/home/develop/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename();
                File dirPath = new File(uploadDir);

                if (!dirPath.exists()) {
                    dirPath.mkdirs();
                }
                multipartFile.transferTo(new File("/home/develop/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename()));
                log.debug(fileName);
                log.debug("/home/develop/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename());
                if (multipartFile.getOriginalFilename().contains(".pdf")) {
                    map.addAttribute("pathFilePdf", "/home/develop/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename());
                } else {
                    map.addAttribute("pathFileXml", "/home/develop/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename());
                }

            }
        }
        map.addAttribute("files", fileNames);
        return "/inscripciones/uploadFiles/file_upload_success";
    }

    @RequestMapping(value = "/descargarPdf", method = RequestMethod.GET)
    public ModelAndView handleRequestPDF(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        try {
            // Suponemos que es un zip lo que se quiere descargar el usuario.
            // Aqui se hace a piñón fijo, pero podría obtenerse el fichero
            // pedido por el usuario a partir de algún parámetro del request
            // o de la URL con la que nos han llamado.
            String nombreFichero = "el_camino_a_cristo.pdf";
            String unPath = "/home/develop/sam/";

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreFichero + "\"");

            InputStream is = new FileInputStream(unPath + nombreFichero);

            IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();

        } catch (IOException ex) {
            // Sacar log de error.
            throw ex;
        }
        return null;
    }

    @RequestMapping(value = "/descargarXML", method = RequestMethod.GET)
    public ModelAndView handleRequestXML(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        try {
            // Suponemos que es un zip lo que se quiere descargar el usuario.
            // Aqui se hace a piñón fijo, pero podría obtenerse el fichero
            // pedido por el usuario a partir de algún parámetro del request
            // o de la URL con la que nos han llamado.
            String nombreFichero = "build.xml";
            String unPath = "/home/develop/sam/";

            response.setContentType("application/xml");
            response.setHeader("Content-Disposition", "attachment; filename=\""
                    + nombreFichero + "\"");

            InputStream is = new FileInputStream(unPath + nombreFichero);

            IOUtils.copy(is, response.getOutputStream());

            response.flushBuffer();

        } catch (IOException ex) {
            // Sacar log de error.
            throw ex;
        }
        return null;
    }
}
