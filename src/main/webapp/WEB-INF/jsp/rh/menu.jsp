<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/rh' />"><s:message code="rh.label" /></a></li>
        <li<c:if test="${param.menu eq 'empleado'}"> class="active"</c:if>><a href="<s:url value='/rh/empleado'/>" ><s:message code="empleado.label" /></a></li>
        <li<c:if test="${param.menu eq 'puesto'}"> class="active"</c:if>><a href="<s:url value='/rh/puestos'/>" ><s:message code="puesto.label" /></a></li>
        <li<c:if test="${param.menu eq 'categoria'}"> class="active"</c:if>><a href="<s:url value='/rh/categoria'/>" ><s:message code="categoria.label" /></a></li>
        <li<c:if test="${param.menu eq 'nacionalidad'}"> class="active"</c:if>><a href="<s:url value='/rh/nacionalidad'/>" ><s:message code="nacionalidad.label" /></a></li>
        <li<c:if test="${param.menu eq 'concepto'}"> class="active"</c:if>><a href="<s:url value='/rh/concepto'/>" ><s:message code="concepto.label" /></a></li>
        <li<c:if test="${param.menu eq 'colegio'}"> class="active"</c:if>><a href="<s:url value='/rh/colegio'/>" ><s:message code="colegio.label" /></a></li>
        <li<c:if test="${param.menu eq 'dependiente'}"> class="active"</c:if>><a href="<s:url value='/rh/dependiente'/>" ><s:message code="dependiente.label" /></a></li>
        <li<c:if test="${param.menu eq 'perded'}"> class="active"</c:if>><a href="<s:url value='/rh/perded'/>" ><s:message code="perded.label" /></a></li>
        <li<c:if test="${param.menu eq 'seccion'}"> class="active"</c:if>><a href="<s:url value='/rh/seccion'/>" ><s:message code="seccion.label" /></a></li>
        <li<c:if test="${param.menu eq 'empleadoPuesto'}"> class="active"</c:if>><a href="<s:url value='/rh/empleadoPuesto'/>" ><s:message code="empleadoPuesto.label" /></a></li>
    </ul>
</nav>
    
