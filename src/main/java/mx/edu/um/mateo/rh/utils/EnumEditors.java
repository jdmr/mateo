/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.edu.um.mateo.rh.utils;
import java.beans.PropertyEditorSupport;

/**
 *
 * @author zorch
 */
public class EnumEditors extends PropertyEditorSupport {
 private Class clazz;
 
 public EnumEditors(Class clazz) {
  this.clazz = clazz;
 };
 
    @Override
 public String getAsText() {
  return (getValue() == null ? "" : ((Enum<?>) getValue()).name());
  
 }
 
    @Override
 public void setAsText(String text) throws IllegalArgumentException {
  setValue(Enum.valueOf(clazz, text));
 }
}