<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="usuario.lista.label" /></title>
    </head>
    <body>
        <a href="#list-usuario" class="skip" tabindex="-1"><s:message code="brincar.al.contenido" />&hellip;</a>
        <div class="nav" role="navigation">
            <ul>
                <li><a href="<s:url value='/admin'/>" class="admin"><s:message code="admin.label" /></a></li>
                <li><a href="<s:url value='/admin/usuario/nuevo'/>" class="create"><s:message code="usuario.nuevo.label" /></a></li>
            </ul>
        </div>

        <div id="list-usuario" class="content scaffold-list" role="main">
            <h1><s:message code="usuario.lista.label" /></h1>

            <c:if test="${not empty message}">
                <div class="message" role="status"><s:message code="${message}" arguments="${messageAttrs}" /></div>
            </c:if>
                
            <table id="usuarios">
                <thead>
                    <tr>
                        <th><s:message code="usuario.username.label" /></th>
                        <th><s:message code="usuario.nombre.label" /></th>
                        <th><s:message code="usuario.apellido.label" /></th>
                        <th><s:message code="empresa.label" /></th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${usuarios}" var="usuario" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/admin/usuario/ver/${usuario.id}' />">${usuario.username}</a></td>
                            <td>${usuario.nombre}</td>
                            <td>${usuario.apellido}</td>
                            <td>${usuario.empresa.nombre}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="pagination">

            </div>
        </div>
        <div id="spinner" class="spinner" style="display:none;"><s:message code="cargando.message" />&hellip;</div>

        <script src="<c:url value='/js/application.js' />" type="text/javascript" ></script>
        <script type="text/javascript">
            highlightTableRows('usuarios')
        </script>                    
    </body>
</html>
