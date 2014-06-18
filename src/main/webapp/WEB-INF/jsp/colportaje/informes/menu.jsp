<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/colportaje' />"><s:message code="colportaje.label" /></a></li>
        <li<c:if test="${param.menu eq 'informeMensual'}"> class="active"</c:if>><a href="<s:url value='/colportaje/informes/informeMensual'/>" ><s:message code="informeMensual.label" /></a></li>
        <sec:authorize access="hasRole('ROLE_ASOC')">
        <li<c:if test="${param.menu eq 'informeMensualAsociado'}"> class="active"</c:if>><a href="<s:url value='/colportaje/informes/informeMensualAsociado'/>" ><s:message code="informeMensualAsociado.label" /></a></li>
        <li<c:if test="${param.menu eq 'concentradoAsociados'}"> class="active"</c:if>><a href="<s:url value='/colportaje/informes/informeConcentradoMensualAsociados'/>" ><s:message code="concentradoAsociados.label" /></a></li>
        </sec:authorize>
    </ul>
</nav>
    
