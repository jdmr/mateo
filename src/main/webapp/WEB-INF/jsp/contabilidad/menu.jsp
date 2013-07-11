<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/contabilidad' />"><s:message code="contabilidad.label" /></a></li>
        <li<c:if test="${param.menu eq 'ejercicio'}"> class="active"</c:if>><a href="<s:url value='/contabilidad/ejercicio'/>" ><s:message code="ejercicios.label" /></a></li>
        <li<c:if test="${param.menu eq 'auxiliar'}"> class="active"</c:if>><a href="<s:url value='/contabilidad/auxiliar'/>" ><s:message code="auxiliares.label" /></a></li>
        <li<c:if test="${param.menu eq 'resultado'}"> class="active"</c:if>><a href="<s:url value='/contabilidad/resultado'/>" ><s:message code="resultados.label" /></a></li>
        <li<c:if test="${param.menu eq 'libro'}"> class="active"</c:if>><a href="<s:url value='/contabilidad/libro'/>" ><s:message code="libros.label" /></a></li>
        <li<c:if test="${param.menu eq 'ordenPago'}"> class="active"</c:if>><a href="<s:url value='/contabilidad/registroOrdenPago'/>" ><s:message code="ordenPago.label" /></a></li>
    </ul>
</nav>
