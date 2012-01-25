<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="admin.label" /></title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><a href="<s:url value='/'/>" class="home"><s:message code="inicio.label" /></a></li>
                <li><a href="<s:url value='/admin/cliente'/>" class="list"><s:message code="cliente.label" /></a></li>
                <li><a href="<s:url value='/admin/tipoCliente'/>" class="list"><s:message code="tipoCliente.label" /></a></li>
                <li><a href="<s:url value='/admin/proveedor'/>" class="list"><s:message code="proveedor.label" /></a></li>
                <li><a href="<s:url value='/admin/empresa'/>" class="list"><s:message code="empresa.label" /></a></li>
                <li><a href="<s:url value='/admin/organizacion'/>" class="list"><s:message code="organizacion.label" /></a></li>
                <li><a href="<s:url value='/admin/usuario'/>" class="list"><s:message code="usuario.label" /></a></li>
            </ul>
        </div>

        <div class="content" style="padding:10px 25px;">
            <h1><s:message code="admin.label" /></h1>
        </div>
    </body>
</html>
