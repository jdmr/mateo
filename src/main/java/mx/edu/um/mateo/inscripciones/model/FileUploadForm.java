/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.inscripciones.model;
import java.util.List;
 
import org.springframework.web.multipart.MultipartFile;
/**
 *
 * @author develop
 */
public class FileUploadForm {
    private List<MultipartFile> files;

    public List<MultipartFile> getFiles() {
        return files;
    }

    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
    
}
