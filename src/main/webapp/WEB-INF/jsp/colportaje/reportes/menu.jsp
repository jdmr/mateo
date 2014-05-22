<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/colportaje' />"><s:message code="colportaje.label" /></a></li>
        <sec:authorize access="hasRole('ROLE_ASOC')">
        <li<c:if test="${param.menu eq 'censoColportores'}"> class="active"</c:if>><a href="<s:url value='/colportaje/reportes/censoColportores'/>" ><s:message code="censoColportores.label" /></a></li>
        <li<c:if test="${param.menu eq 'concentradoVentas'}"> class="active"</c:if>><a href="<s:url value='/colportaje/reportes/concentradoVentas'/>" ><s:message code="concentradoVentas.label" /></a></li>
        </sec:authorize>
        <li<c:if test="${param.menu eq 'concentradoGral'}"> class="active"</c:if>><a href="<s:url value='/colportaje/reportes/concentradoGeneralPorTemporadas'/>" ><s:message code="concentradoGralPorTemporadas.label" /></a></li>
        <li<c:if test="${param.menu eq 'concentradoInformes'}"> class="active"</c:if>><a href="<s:url value='/colportaje/reportes/concentradoInformesMensuales'/>" ><s:message code="concentradoInformesMensuales.label" /></a></li>
        <li<c:if test="${param.menu eq 'concentradoInformesAnuales'}"> class="active"</c:if>><a href="<s:url value='/colportaje/reportes/concentradoInformesAnuales'/>" ><s:message code="concentradoInformesAnuales.label" /></a></li>
        <li<c:if test="${param.menu eq 'planMensualOracion'}"> class="active"</c:if>><a href="<s:url value='/colportaje/reportes/planMensualOracion'/>" ><s:message code="planMensualOracion.label" /></a></li>
        <li<c:if test="${param.menu eq 'planDiarioOracion'}"> class="active"</c:if>><a href="<s:url value='/colportaje/reportes/planDiarioOracion'/>" ><s:message code="planDiarioOracion.label" /></a></li>
    </ul>
</nav>
    
