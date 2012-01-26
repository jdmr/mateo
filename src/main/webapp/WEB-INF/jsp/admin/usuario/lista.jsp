<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="usuario.lista.label" /></title>
    </head>
    <body>
        <div class="nav" role="navigation">
            <ul>
                <li><a href="<s:url value='/admin'/>" class="home"><s:message code="admin.label" /></a></li>
                <li><a href="<s:url value='/admin/usuario/nuevo'/>" class="list"><s:message code="usuario.nuevo.label" /></a></li>
            </ul>
        </div>

        <div class="content" style="padding:10px 25px;">
            <h1><s:message code="usuario.lista.label" /></h1>
        </div>
    </body>
</html>
