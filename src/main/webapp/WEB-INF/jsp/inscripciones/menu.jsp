<%-- 
    Document   : menu
    Created on : 18-feb-2013, 11:59:59
    Author     : semdariobarbaamaya
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/inscripciones' />"><s:message code="inscripciones.label" /></a></li>
        <li<c:if test="${param.menu eq 'periodo'}"> class="active"</c:if>><a href="<c:url value='/inscripciones/periodos' />"><s:message code="periodo.label" /></a></li>
        <li<c:if test="${param.menu eq 'institucion'}"> class="active"</c:if>><a href="<c:url value='/inscripciones/instituciones' />"><s:message code="institucion.label" /></a></li>
    </ul>
</nav>
