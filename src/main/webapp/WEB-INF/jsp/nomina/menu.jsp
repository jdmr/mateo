<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/nomina' />"><s:message code="nomina.label" /></a></li>
        <li<c:if test="${param.menu eq 'catalogos'}"> class="active"</c:if>><a href="<s:url value='/nomina/catalogos'/>" ><s:message code="catalogo.label" /></a></li>
    </ul>
</nav>

