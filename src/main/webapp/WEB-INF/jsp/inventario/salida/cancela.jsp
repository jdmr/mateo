<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="salida.cancela.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/inventario' />"><s:message code="inventario.label" /></a></li>
                <li class="active"><a href="<s:url value='/inventario/salida'/>" ><s:message code="salida.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/entrada'/>" ><s:message code="entrada.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/producto'/>" ><s:message code="producto.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/tipoProducto'/>" ><s:message code="tipoProducto.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/almacen'/>" ><s:message code="almacen.lista.label" /></a></li>
            </ul>
        </nav>

        <h1><s:message code="salida.cancela.label" /></h1>
        <hr/>

        <c:if test="${productos != null}">
            <h2><s:message code="producto.lista.label" /></h2>
            <table id="productos" class="table table-striped">
                <thead>
                    <tr>
                        <th><s:message code="sku.label" /></th>
                        <th><s:message code="nombre.label" /></th>
                        <th><s:message code="descripcion.label" /></th>
                        <th><s:message code="precioUnitario.label" /></th>
                        <th><s:message code="ultimoPrecio.label" /></th>
                        <th><s:message code="existencia.label" /></th>
                        <th><s:message code="tipoProducto.label" /></th>
                        <th><s:message code="almacen.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${productos}" var="producto">
                        <tr>
                            <td>${producto.sku}</td>
                            <td>${producto.nombre}</td>
                            <td>${producto.descripcion}</td>
                            <td>${producto.precioUnitario}</td>
                            <td>${producto.ultimoPrecio}</td>
                            <td><fmt:formatNumber type="number" value="${producto.existencia}" maxFractionDigits="3" groupingUsed="true" /> ${producto.unidadMedida}</td>
                            <td>${producto.tipoProducto.nombre}</td>
                            <td>${producto.almacen.nombre}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
            
        <c:if test="${productosCancelados != null}">
            <h2><s:message code="producto.con.historia.label" /></h2>
            <table id="productos" class="table table-striped">
                <thead>
                    <tr>
                        <th><s:message code="sku.label" /></th>
                        <th><s:message code="nombre.label" /></th>
                        <th><s:message code="descripcion.label" /></th>
                        <th><s:message code="precioUnitario.label" /></th>
                        <th><s:message code="ultimoPrecio.label" /></th>
                        <th><s:message code="existencia.label" /></th>
                        <th><s:message code="tipoProducto.label" /></th>
                        <th><s:message code="almacen.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${productosCancelados}" var="producto">
                        <tr>
                            <td>${producto.sku}</td>
                            <td>${producto.nombre}</td>
                            <td>${producto.descripcion}</td>
                            <td>${producto.precioUnitario}</td>
                            <td>${producto.ultimoPrecio}</td>
                            <td><fmt:formatNumber type="number" value="${producto.existencia}" maxFractionDigits="3" groupingUsed="true" /> ${producto.unidadMedida}</td>
                            <td>${producto.tipoProducto.nombre}</td>
                            <td>${producto.almacen.nombre}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
            
        <c:if test="${productosSinHistoria != null}">
            <h2><s:message code="producto.sin.historia.label" /></h2>
            <table id="productos" class="table table-striped">
                <thead>
                    <tr>
                        <th><s:message code="sku.label" /></th>
                        <th><s:message code="nombre.label" /></th>
                        <th><s:message code="descripcion.label" /></th>
                        <th><s:message code="precioUnitario.label" /></th>
                        <th><s:message code="ultimoPrecio.label" /></th>
                        <th><s:message code="existencia.label" /></th>
                        <th><s:message code="tipoProducto.label" /></th>
                        <th><s:message code="almacen.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${productosSinHistoria}" var="producto">
                        <tr>
                            <td>${producto.sku}</td>
                            <td>${producto.nombre}</td>
                            <td>${producto.descripcion}</td>
                            <td>${producto.precioUnitario}</td>
                            <td>${producto.ultimoPrecio}</td>
                            <td><fmt:formatNumber type="number" value="${producto.existencia}" maxFractionDigits="3" groupingUsed="true" /> ${producto.unidadMedida}</td>
                            <td>${producto.tipoProducto.nombre}</td>
                            <td>${producto.almacen.nombre}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
            
        <c:if test="${entradas != null}">
            <h2><s:message code="entrada.lista.label" /></h2>
            <table id="productos" class="table table-striped">
                <thead>
                    <tr>
                        <th><s:message code="folio.label" /></th>
                        <th><s:message code="factura.label" /></th>
                        <th><s:message code="fechaFactura.label" /></th>
                        <th><s:message code="iva.label" /></th>
                        <th><s:message code="total.label" /></th>
                        <th><s:message code="proveedor.label" /></th>
                        <th><s:message code="almacen.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${entradas}" var="entrada">
                        <tr>
                            <td>${entrada.folio}</td>
                            <td>${entrada.factura}</td>
                            <td>${entrada.fechaFactura}</td>
                            <td>${entrada.iva}</td>
                            <td>${entrada.total}</td>
                            <td>${entrada.proveedor.nombre}</td>
                            <td>${entrada.almacen.nombre}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
            
        <c:if test="${salidas != null}">
            <h2><s:message code="salida.lista.label" /></h2>
            <table id="productos" class="table table-striped">
                <thead>
                    <tr>
                        <th><s:message code="folio.label" /></th>
                        <th><s:message code="reporte.label" /></th>
                        <th><s:message code="empleado.label" /></th>
                        <th><s:message code="atendio.label" /></th>
                        <th><s:message code="iva.label" /></th>
                        <th><s:message code="total.label" /></th>
                        <th><s:message code="cliente.label" /></th>
                        <th><s:message code="almacen.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${salidas}" var="salida">
                        <tr>
                            <td>${salida.folio}</td>
                            <td>${salida.reporte}</td>
                            <td>${salida.empleado}</td>
                            <td>${salida.atendio}</td>
                            <td>${salida.iva}</td>
                            <td>${salida.total}</td>
                            <td>${salida.cliente.nombre}</td>
                            <td>${salida.almacen.nombre}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:if>
    </body>
</html>
