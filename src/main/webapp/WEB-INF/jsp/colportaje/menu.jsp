<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/colportaje' />"><s:message code="colportaje.label" /></a></li>
        <li<c:if test="${param.menu eq 'uploadFile'}"> class="active"</c:if>><a href="<s:url value='/uploadFile'/>" ><s:message code="uploadFile.label" /></a></li>
        <li<c:if test="${param.menu eq 'pais'}"> class="active"</c:if>><a href="<s:url value='/colportaje/pais'/>" ><s:message code="pais.label" /></a></li>
        <li<c:if test="${param.menu eq 'estado'}"> class="active"</c:if>><a href="<s:url value='/colportaje/estado'/>" ><s:message code="estado.label" /></a></li>
        <li<c:if test="${param.menu eq 'ciudad'}"> class="active"</c:if>><a href="<s:url value='/colportaje/ciudad'/>" ><s:message code="ciudad.label" /></a></li>
        <li<c:if test="${param.menu eq 'union'}"> class="active"</c:if>><a href="<s:url value='/colportaje/union'/>" ><s:message code="union.label" /></a></li>
        <li<c:if test="${param.menu eq 'asociacion'}"> class="active"</c:if>><a href="<s:url value='/colportaje/asociacion'/>" ><s:message code="asociacion.label" /></a></li>
        <li<c:if test="${param.menu eq 'colegio'}"> class="active"</c:if>><a href="<s:url value='/colportaje/colegio'/>" ><s:message code="colegio.label" /></a></li>
        <li<c:if test="${param.menu eq 'temporada'}"> class="active"</c:if>><a href="<s:url value='/colportaje/temporada'/>" ><s:message code="temporada.label" /></a></li>
        <li<c:if test="${param.menu eq 'temporadaColportor'}"> class="active"</c:if>><a href="<s:url value='/colportaje/temporadaColportor'/>" ><s:message code="temporadaColportor.label" /></a></li>
    </ul>
</nav>
    
