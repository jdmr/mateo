<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/admin' />"><s:message code="admin.label" /></a></li>
        <li<c:if test="${param.menu eq 'cliente'}"> class="active"</c:if>><a href="<s:url value='/admin/cliente'/>" ><s:message code="cliente.label" /></a></li>
        <li<c:if test="${param.menu eq 'tipoCliente'}"> class="active"</c:if>><a href="<s:url value='/admin/tipoCliente'/>" ><s:message code="tipoCliente.label" /></a></li>
        <li<c:if test="${param.menu eq 'proveedor'}"> class="active"</c:if>><a href="<s:url value='/admin/proveedor'/>" ><s:message code="proveedor.label" /></a></li>
        <li<c:if test="${param.menu eq 'empresa'}"> class="active"</c:if>><a href="<s:url value='/admin/empresa'/>" ><s:message code="empresa.label" /></a></li>
        <li<c:if test="${param.menu eq 'organizacion'}"> class="active"</c:if>><a href="<s:url value='/admin/organizacion'/>" ><s:message code="organizacion.label" /></a></li>
        <li<c:if test="${param.menu eq 'usuario'}"> class="active"</c:if>><a href="<s:url value='/admin/usuario'/>" ><s:message code="usuario.label" /></a></li>
    </ul>
</nav>
