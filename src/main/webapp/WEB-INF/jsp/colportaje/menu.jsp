<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/colportaje' />"><s:message code="colportaje.label" /></a></li>
        <li<c:if test="${param.menu eq 'pais'}"> class="active"</c:if>><a href="<s:url value='/colportaje/pais'/>" ><s:message code="pais.label" /></a></li>
        <li<c:if test="${param.menu eq 'estado'}"> class="active"</c:if>><a href="<s:url value='/colportaje/estado'/>" ><s:message code="estado.label" /></a></li>
        <li<c:if test="${param.menu eq 'ciudad'}"> class="active"</c:if>><a href="<s:url value='/colportaje/ciudad'/>" ><s:message code="ciudad.label" /></a></li>
    </ul>
</nav>
    
