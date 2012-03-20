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
package mx.edu.um.mateo.activos.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mx.edu.um.mateo.general.model.Departamento;
import mx.edu.um.mateo.general.model.Empresa;
import mx.edu.um.mateo.general.model.Imagen;
import mx.edu.um.mateo.general.model.Proveedor;

/**
 *
 * @author J. David Mendoza <jdmendoza@um.edu.mx>
 */
public class Activo implements Serializable {
    private Long id;
    private Integer version;
    private String folio;
    private Date fechaCreacion;
    private Date fechaModificacion;
    private String procedencia;
    private String factura;
    private String pedimento;
    private String moneda;
    private BigDecimal tipoCambio;
    private String condicion;
    private String poliza;
    private String codigo;
    private String descripcion;
    private String marca;
    private String modelo;
    private String serial;
    private BigDecimal moi = BigDecimal.ZERO;
    private BigDecimal valorRescate = BigDecimal.ONE;
    private BigDecimal inpc = BigDecimal.ZERO;
    private String ubicacion;
    private Boolean inactivo = false;
    private Date fechaInactivo;
    private TipoActivo tipoActivo;
    private Proveedor proveedor;
    private Departamento departamento;
    private Empresa empresa;
    private String responsable;
    private String motivo = "COMPRA";
    private Boolean garantia = false;
    private Integer mesesGarantia = 0;
    private Boolean seguro = false;
    private BigDecimal valorNeto = BigDecimal.ZERO;
    private List<Imagen> imagenes = new ArrayList<>();
    private List<ReubicacionActivo> reubicaciones = new ArrayList<>();
}
