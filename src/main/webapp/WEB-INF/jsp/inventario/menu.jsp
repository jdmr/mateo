<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/inventario' />"><s:message code="inventario.label" /></a></li>
        <li<c:if test="${param.menu eq 'salida'}"> class="active"</c:if>><a href="<s:url value='/inventario/salida'/>" ><s:message code="salida.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'entrada'}"> class="active"</c:if>><a href="<s:url value='/inventario/entrada'/>" ><s:message code="entrada.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'cancelacion'}"> class="active"</c:if>><a href="<s:url value='/inventario/cancelacion'/>" ><s:message code="cancelacion.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'producto'}"> class="active"</c:if>><a href="<s:url value='/inventario/producto'/>" ><s:message code="producto.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'tipoProducto'}"> class="active"</c:if>><a href="<s:url value='/inventario/tipoProducto'/>" ><s:message code="tipoProducto.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'almacen'}"> class="active"</c:if>><a href="<s:url value='/inventario/almacen'/>" ><s:message code="almacen.lista.label" /></a></li>
    </ul>
</nav>
