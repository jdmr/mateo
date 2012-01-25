<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="inicio.label" /></title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><a href="<c:url value='/contabilidad' />" class="list"><s:message code="contabilidad.label" /></a></li>
                <li><a href="<c:url value='/inventario' />" class="list"><s:message code="inventario.label" /></a></li>
                <li><a href="<c:url value='/admin' />" class="list"><s:message code="admin.label" /></a></li>
                <li><a href="<c:url value='/usuario/perfil' />" class="edit"><s:message code="perfil.label" /></a></li>
            </ul>
        </div>

        <div class="content" style="padding:10px 25px;">
            <h1><s:message code="inicio.label" /></h1>
        </div>
    </body>
</html>
