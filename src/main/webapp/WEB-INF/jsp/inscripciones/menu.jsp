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
        <li<c:if test="${param.menu eq 'tiposBecas'}"> class="active"</c:if>><a href="<s:url value='/inscripciones/tiposBecas'/>" ><s:message code="tiposBecas.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'afeConvenio'}"> class="active"</c:if>><a href="<s:url value='/inscripciones/afeConvenio'/>" ><s:message code="afeConvenio.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'descuento'}"> class="active"</c:if>><a href="<s:url value='/inscripciones/descuento'/>" ><s:message code="descuento.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'paquete'}"> class="active"</c:if>><a href="<s:url value='/inscripciones/paquete'/>" ><s:message code="paquete.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'prorroga'}"> class="active"</c:if>><a href="<s:url value='/inscripciones/prorroga'/>" ><s:message code="prorroga.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'periodo'}"> class="active"</c:if>><a href="<c:url value='/inscripciones/periodos' />"><s:message code="periodo.label" /></a></li>
        <li<c:if test="${param.menu eq 'institucion'}"> class="active"</c:if>><a href="<c:url value='/inscripciones/instituciones' />"><s:message code="institucion.label" /></a></li>
        <li<c:if test="${param.menu eq 'tiposBecas'}"> class="active"</c:if>><a href="<s:url value='/inscripciones/tiposBecas'/>" ><s:message code="tiposBecas.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'paquete'}"> class="active"</c:if>><a href="<s:url value='/inscripciones/paquete'/>" ><s:message code="paquete.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'prorroga'}"> class="active"</c:if>><a href="<s:url value='/inscripciones/prorroga'/>" ><s:message code="prorroga.lista.label" /></a></li>
        <li<c:if test="${param.menu eq 'alumnoPaquete'}"> class="active"</c:if>><a href="<s:url value='/inscripciones/alumnoPaquete'/>" ><s:message code="alumnoPaquete.lista.label" /></a></li>
    </ul>
</nav>
