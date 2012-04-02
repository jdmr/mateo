/*
 * The MIT License
 *
 * Copyright 2012 Universidad de Montemorelos A. C.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package mx.edu.um.mateo.inventario.utils;

import mx.edu.um.mateo.inventario.model.Entrada;
import mx.edu.um.mateo.inventario.model.FacturaAlmacen;
import mx.edu.um.mateo.inventario.model.Salida;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public class NoSePuedeCancelarException extends Exception {

    private Entrada entrada;
    private Salida salida;
    private FacturaAlmacen factura;
    
    public NoSePuedeCancelarException(String message, Throwable cause, Entrada entrada) {
        super(message, cause);
        this.entrada = entrada;
    }

    public NoSePuedeCancelarException(String message, Throwable cause, Salida salida) {
        super(message, cause);
        this.salida = salida;
    }

    public NoSePuedeCancelarException(String message, Throwable cause, FacturaAlmacen factura) {
        super(message, cause);
        this.factura = factura;
    }

    public NoSePuedeCancelarException(String message, Entrada entrada) {
        super(message);
        this.entrada = entrada;
    }

    public NoSePuedeCancelarException(String message, Salida salida) {
        super(message);
        this.salida = salida;
    }

    public NoSePuedeCancelarException(String message, FacturaAlmacen factura) {
        super(message);
        this.factura = factura;
    }

    public NoSePuedeCancelarException(Entrada entrada) {
        super();
        this.entrada = entrada;
    }

    public NoSePuedeCancelarException(Salida salida) {
        super();
        this.salida = salida;
    }

    public NoSePuedeCancelarException(FacturaAlmacen factura) {
        super();
        this.factura = factura;
    }

    /**
     * @return the entrada
     */
    public Entrada getEntrada() {
        return entrada;
    }

    /**
     * @param entrada the entrada to set
     */
    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    /**
     * @return the salida
     */
    public Salida getSalida() {
        return salida;
    }

    /**
     * @param salida the salida to set
     */
    public void setSalida(Salida salida) {
        this.salida = salida;
    }

    /**
     * @return the factura
     */
    public FacturaAlmacen getFactura() {
        return factura;
    }

    /**
     * @param factura the factura to set
     */
    public void setFactura(FacturaAlmacen factura) {
        this.factura = factura;
    }

}
