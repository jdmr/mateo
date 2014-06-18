<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/colportaje' />"><s:message code="colportaje.label" /></a></li>
        <sec:authorize access="hasRole('ROLE_ASOC')">
                <li<c:if test="${param.menu eq 'proyectoColportor'}"> class="active"</c:if>><a href="<s:url value='/colportaje/catalogos/proyectoColportor'/>" ><s:message code="proyectoColportor.label" /></a></li>
        </sec:authorize>
    </ul>
</nav>
    
