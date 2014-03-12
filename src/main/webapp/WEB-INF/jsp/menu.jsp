<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
            <sec:authorize access="!hasRole('ROLE_PRV')">
            <li><a href="<c:url value='/admin' />"><s:message code="admin.label" /></a></li>  
            <li><a href="<c:url value='/inventario' />"><s:message code="inventario.label" /></a></li>
            <li><a href="<c:url value='/activoFijo' />"><s:message code="activoFijo.label" /></a></li>
            <li><a href="<c:url value='/rh' />"><s:message code="rh.label" /></a></li>
            <li><a href="<c:url value='/contabilidad' />"><s:message code="contabilidad.label" /></a></li>
            <li><a href="<c:url value='/inscripciones' />"><s:message code="inscripciones.label" /></a></li>
            <li><a href="<c:url value='/colportaje' />"><s:message code="colportaje.label" /></a></li>              
            <li><a href="<c:url value='/nomina' />"><s:message code="nomina.label" /></a></li>              
            </sec:authorize>
            <sec:authorize access="hasAnyRole('ROLE_PRV','ROLE_ADMIN','ROLE_EMP')">
            <li><a href="<c:url value='/factura' />"><s:message code="factura.label" /></a></li>              
            </sec:authorize>
    </ul>
</nav>
