<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/activoFijo' />"><s:message code="activoFijo.label" /></a></li>
        <li<c:if test="${param.menu eq 'activo'}"> class="active"</c:if>><a href="<s:url value='/activoFijo/activo'/>" ><s:message code="activo.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'tipoActivo'}"> class="active"</c:if>><a href="<s:url value='/activoFijo/tipoActivo'/>" ><s:message code="tipoActivo.lista.label" /></a></li>
    </ul>
</nav>
