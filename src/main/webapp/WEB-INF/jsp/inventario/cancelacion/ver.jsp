<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="cancelacion.ver.label" arguments="${cancelacion.folio}" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="cancelacion" />
        </jsp:include>

        <h1><s:message code="cancelacion.ver.label" arguments="${cancelacion.folio}" /></h1>
        <hr/>

        <c:if test="${not empty message}">
            <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>
        
        <div class="row-fluid">
            <div class="span4">
                <h4><s:message code="comentarios.label" /></h4>
                <h3>${cancelacion.comentarios}</h3>
            </div>
            <div class="span4">
                <h4><s:message code="creador.label" /></h4>
                <h3>${cancelacion.creador}</h3>
            </div>
            <div class="span4">
                <h4><s:message code="fechaCreacion.label" /></h4>
                <h3>${cancelacion.fechaCreacion}</h3>
            </div>
        </div>
        <hr/>
        
        <c:if test="${not empty cancelacion.productos}">
            <h2><s:message code="producto.lista.label" /></h2>
            <table id="productos" class="table table-striped table-hover">
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
                    <c:forEach items="${cancelacion.productos}" var="producto">
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
                        
        <c:if test="${not empty cancelacion.entradas}">
            <h2><s:message code="entrada.lista.label" /></h2>
            <table id="productos" class="table table-striped table-hover">
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
                    <c:forEach items="${cancelacion.entradas}" var="entrada">
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
            
        <c:if test="${not empty cancelacion.salidas}">
            <h2><s:message code="salida.lista.label" /></h2>
            <table id="productos" class="table table-striped table-hover">
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
                    <c:forEach items="${cancelacion.salidas}" var="salida">
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
