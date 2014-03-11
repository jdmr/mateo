<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/colportaje' />"><s:message code="colportaje.label" /></a></li>
        <li<c:if test="${param.menu eq 'uploadFile'}"> class="active"</c:if>><a href="<s:url value='/uploadFile'/>" ><s:message code="uploadFile.label" /></a></li>
        <li<c:if test="${param.menu eq 'colegio'}"> class="active"</c:if>><a href="<s:url value='/colportaje/colegio'/>" ><s:message code="colegio.label" /></a></li>
        <li<c:if test="${param.menu eq 'temporada'}"> class="active"</c:if>><a href="<s:url value='/colportaje/temporada'/>" ><s:message code="temporada.label" /></a></li>
        <li<c:if test="${param.menu eq 'colportor'}"> class="active"</c:if>><a href="<s:url value='/colportaje/colportor'/>" ><s:message code="colportor.label" /></a></li>
        <li<c:if test="${param.menu eq 'temporadaColportor'}"> class="active"</c:if>><a href="<s:url value='/colportaje/temporadaColportor'/>" ><s:message code="temporadaColportor.label" /></a></li>
        <li<c:if test="${param.menu eq 'documento'}"> class="active"</c:if>><a href="<s:url value='/colportaje/documento'/>" ><s:message code="documento.label" /></a></li>
        <li<c:if test="${param.menu eq 'importarDatos'}"> class="active"</c:if>><a href="<s:url value='/colportaje/importarDatos'/>" ><s:message code="importarDatos.label" /></a></li>
        <li<c:if test="${param.menu eq 'reportes'}"> class="active"</c:if>><a href="<s:url value='/colportaje/reportes'/>" ><s:message code="reportes.label" /></a></li>
        <li<c:if test="${param.menu eq 'ventas'}"> class="active"</c:if>><a href="<s:url value='/colportaje/ventas'/>" ><s:message code="colportaje.ventas.label" /></a></li>
        <li<c:if test="${param.menu eq 'informeMensual'}"> class="active"</c:if>><a href="<s:url value='/colportaje/informeMensual'/>" ><s:message code="colportaje.informeMensual.label" /></a></li>
    </ul>
</nav>
    
