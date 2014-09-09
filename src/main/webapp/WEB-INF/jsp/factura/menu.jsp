<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<nav class="navbar navbar-fixed-top" role="navigation">
    <ul class="nav">
        <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
        <li<c:if test="${param.menu eq 'principal'}"> class="active"</c:if>><a href="<c:url value='/factura' />"><s:message code="factura.label" /></a></li>
            <sec:authorize access="hasRole('ROLE_ADMIN')">
            <li<c:if test="${param.menu eq 'informe'}"> class="active"</c:if>><a href="<s:url value='/factura/informe'/>" ><s:message code="informe.lista.label" /></a></li>
            <li<c:if test="${param.menu eq 'revisa'}"> class="active"</c:if>><a href="<s:url value='/factura/informe/encabezados'/>" ><s:message code="revisarEmpleado.label" /></a></li>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_PRV_COMPRAS')">
            <li<c:if test="${param.menu eq 'revisaProveedor'}"> class="active"</c:if>><a href="<s:url value='/factura/informeProveedorDetalle/revisarFacturasCompras'/>" ><s:message code="revisarProveedor.label" /></a></li>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_PRV_VALIDA')">
            <li<c:if test="${param.menu eq 'revisaProveedor'}"> class="active"</c:if>><a href="<s:url value='/factura/informeProveedorDetalle/revisar'/>" ><s:message code="revisarProveedor.label" /></a></li>
            <li<c:if test="${param.menu eq 'proveedorFacturas'}"> class="active"</c:if>><a href="<s:url value='/factura/proveedorFacturas'/>" ><s:message code="proveedor.label" /></a></li>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_PRV_GENERA')">
            <li<c:if test="${param.menu eq 'asignarFecha'}"> class="active"</c:if>><a href="<s:url value='/factura/informeProveedorDetalle/listaRevisados'/>" ><s:message code="fechaCompromiso.label" /></a></li>
            <li<c:if test="${param.menu eq 'informeProveedorDetalle'}"> class="active"</c:if>><a href="<s:url value='/factura/informeProveedorDetalle/contrarecibos'/>" ><s:message code="contrarecibo.label" /></a></li>
            </sec:authorize>
            <sec:authorize access="hasRole('ROLE_PRV_USER')">
            <li<c:if test="${param.menu eq 'informeProveedor'}"> class="active"</c:if>><a href="<s:url value='/factura/informeProveedor'/>" ><s:message code="informeProveedor.lista.label" /></a></li>
            </sec:authorize>
    </ul>
</nav>
