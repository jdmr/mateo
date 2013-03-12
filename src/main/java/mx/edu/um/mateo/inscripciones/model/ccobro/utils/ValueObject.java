/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.edu.um.mateo.inscripciones.model.ccobro.utils;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * @author osoto
 */
public class ValueObject extends BaseObject{
    private Object valueOne;
    private Object valueTwo;

    /**
     * @return the valueOne
     */
    public Object getValueOne() {
        return valueOne;
    }

    /**
     * @param valueOne the valueOne to set
     */
    public void setValueOne(Object valueOne) {
        this.valueOne = valueOne;
    }

    /**
     * @return the valueTwo
     */
    public Object getValueTwo() {
        return valueTwo;
    }

    /**
     * @param valueTwo the valueTwo to set
     */
    public void setValueTwo(Object valueTwo) {
        this.valueTwo = valueTwo;
    }
    


    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("ValueOne", this.valueOne)
            .append("ValueTwo", this.valueTwo)
			.toString();
    }

    @Override
    public boolean equals(Object o) {
        return this.valueOne.equals(this.valueTwo);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(932218919, 575025674).toHashCode();
    }

}
