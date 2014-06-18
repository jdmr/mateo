/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.sat.nomina;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author osoto
 */
@XmlRootElement(namespace = "de.vogella.xml.jaxb.model")
public class BookStore {
    @XmlElementWrapper(name = "booklist")
    @XmlElement(name = "book")
    
    private ArrayList <Book> bookList;
    private String name;
    private String location;

    public ArrayList<Book> getBooksList() {
        return bookList;
    }

    public void setBooksList(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
    
    
}
