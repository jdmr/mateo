/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.general.model;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 *
 * @author osoto
 */
public class UploadFileForm {
    private Integer id;
    private String name;
    private CommonsMultipartFile file;

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the file
     */
    public CommonsMultipartFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(CommonsMultipartFile file) {
        this.file = file;
        this.name = file.getOriginalFilename();
    }
    
}
