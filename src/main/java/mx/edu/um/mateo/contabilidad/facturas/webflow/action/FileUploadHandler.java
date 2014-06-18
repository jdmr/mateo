/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.contabilidad.facturas.webflow.action;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author develop
 */
@Component
public class FileUploadHandler implements Serializable {

    private final Logger log = LoggerFactory.getLogger(FileUploadHandler.class);

    private transient MultipartFile file;
    private transient MultipartFile file2;

    public void processFile(String username) throws IOException {
        String name = file.getName();
        log.debug("nombre archivo{}", name);
         List<MultipartFile> files = new ArrayList<>();
         files.add(file);
         files.add(file);

        List<String> fileNames = new ArrayList<String>();

        if (null != files && files.size() > 0) {
            for (MultipartFile multipartFile : files) {
                String fileName = multipartFile.getOriginalFilename();
                fileNames.add(fileName);
                String uploadDir = "/home/facturas/" + username + "/" + multipartFile.getOriginalFilename();
                File dirPath = new File(uploadDir);
                if (!dirPath.exists()) {
                    dirPath.mkdirs();
                }
                multipartFile.transferTo(new File("/home/facturas/" + username + "/" + multipartFile.getOriginalFilename()));
                if (multipartFile.getOriginalFilename().contains(".pdf")) {
//                    detalle.setPathPDF("/home/facturas/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename());
//                    detalle.setNombrePDF(multipartFile.getOriginalFilename());
                }
                if (multipartFile.getOriginalFilename().contains(".xml")) {
//                    detalle.setPathXMl("/home/facturas/" + request.getRemoteUser() + "/" + multipartFile.getOriginalFilename());
//                    detalle.setNombreXMl(multipartFile.getOriginalFilename());
                }
            }
        }
        
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
