<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@include file="../../idioma.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="producto.revisaEntradas.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="producto" />
        </jsp:include>

        <h1><s:message code="producto.revisaEntradas.label" /></h1>
        <hr/>

        <table id="lista" class="table table-striped table-hover">
            <thead>
                <tr>
                    <th><s:message code="folio.label"/></th>
                    <th><s:message code="sku.label"/></th>
                    <th><s:message code="cantidad.label"/></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${entradas}" var="entrada">
                    <tr>
                        <td><a href="<c:url value='/inventario/entrada/ver/${entrada.entradaId}' />">${entrada.folio}</a></td>
                        <td><a href="<c:url value='/inventario/producto/ver/${entrada.productoId}' />">${entrada.sku}</a></td>
                        <td>${entrada.cantidad}</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </body>
</html>
