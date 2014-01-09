<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/rh' />"><s:message code="rh.label" /></a></li>
        <li<c:if test="${param.menu eq 'empleado'}"> class="active"</c:if>><a href="<s:url value='/rh/empleado'/>" ><s:message code="empleado.label" /></a></li>
        <li<c:if test="${param.menu eq 'dependiente'}"> class="active"</c:if>><a href="<s:url value='/rh/empleado/dependiente'/>" ><s:message code="dependiente.label" /></a></li>
        <li<c:if test="${param.menu eq 'perded'}"> class="active"</c:if>><a href="<s:url value='/rh/empleado/perded'/>" ><s:message code="perded.label" /></a></li>
        <li<c:if test="${param.menu eq 'empleadoPuesto'}"> class="active"</c:if>><a href="<s:url value='/rh/empleado/empleadoPuesto'/>" ><s:message code="empleadoPuesto.label" /></a></li>
        <li<c:if test="${param.menu eq 'empleadoEstudios'}"> class="active"</c:if>><a href="<s:url value='/rh/empleado/empleadoEstudios'/>" ><s:message code="empleadoEstudios.label" /></a></li>
        <li<c:if test="${param.menu eq 'claveEmpleado'}"> class="active"</c:if>><a href="<s:url value='/rh/empleado/claveEmpleado'/>" ><s:message code="claveEmpleado.label" /></a></li>
        <li<c:if test="${param.menu eq 'vacacionesEmpleado'}"> class="active"</c:if>><a href="<s:url value='/rh/empleado/vacacionesEmpleado'/>" ><s:message code="vacaciones.label" /></a></li>
        <li<c:if test="${param.menu eq 'solicitudVacacionesEmpleado'}"> class="active"</c:if>><a href="<s:url value='/rh/empleado/solicitudVacacionesEmpleado'/>" ><s:message code="solicitudVacaciones.label" /></a></li>
    </ul>
</nav>

