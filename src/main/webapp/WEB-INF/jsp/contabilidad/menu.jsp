<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/contabilidad' />"><s:message code="contabilidad.label" /></a></li>
        <li<c:if test="${param.menu eq 'mayor'}"> class="active"</c:if>><a href="<s:url value='/contabilidad/mayor'/>" ><s:message code="mayores.label" /></a></li>
        <li<c:if test="${param.menu eq 'auxiliar'}"> class="active"</c:if>><a href="<s:url value='/contabilidad/auxiliar'/>" ><s:message code="auxiliares.label" /></a></li>
    </ul>
</nav>
