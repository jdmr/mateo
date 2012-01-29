<%-- 
    Document   : ver
    Created on : Jan 27, 2012, 6:52:45 AM
    Author     : jdmr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="usuario.ver.label" /></title>
    </head>
    <body>
        <a href="#ver-usuario" class="skip" tabindex="-1"><s:message code="brincar.al.contenido" />&hellip;</a>
        <div class="nav" role="navigation">
            <ul>
                <li><a href="<s:url value='/admin'/>" class="admin"><s:message code="admin.label" /></a></li>
                <li><a href="<s:url value='/admin/usuario'/>" class="list"><s:message code="usuario.lista.label" /></a></li>
                <li><a href="<s:url value='/admin/usuario/nuevo'/>" class="create"><s:message code="usuario.nuevo.label" /></a></li>
            </ul>
        </div>

        <div id="ver-usuario" class="content scaffold-list" role="main">
            <h1><s:message code="usuario.ver.label" /></h1>
            
            <c:if test="${not empty message}">
                <div class="message" role="status"><s:message code="${message}" arguments="${messageAttrs}" /></div>
            </c:if>
            
            <c:url var="eliminaUrl" value="/admin/usuario/elimina" />
            <form:form commandName="usuario" action="${eliminaUrl}">
                <form:errors path="*" cssClass="errors" element="ul" />
                <ol class="property-list usuario">
                    <li class="fieldcontain">
                        <span id="username-label" class="property-label"><s:message code="usuario.username.label" /></span>

                        <span class="property-value" aria-labelledby="username-label">${usuario.username}</span>

                    </li>

                    <li class="fieldcontain">
                        <span id="nombre-label" class="property-label"><s:message code="usuario.nombre.label" /></span>

                        <span class="property-value" aria-labelledby="nombre-label">${usuario.nombre}</span>

                    </li>

                    <li class="fieldcontain">
                        <span id="apellido-label" class="property-label"><s:message code="usuario.apellido.label" /></span>

                        <span class="property-value" aria-labelledby="apellido-label">${usuario.apellido}</span>

                    </li>

                    <li class="fieldcontain">
                        <span id="correo-label" class="property-label"><s:message code="usuario.correo.label" /></span>

                        <span class="property-value" aria-labelledby="correo-label">${usuario.correo}</span>

                    </li>

                    <li class="fieldcontain">
                        <span id="empresa-label" class="property-label"><s:message code="empresa.label" /></span>

                        <span class="property-value" aria-labelledby="empresa-label"><a href="/mateo/empresa/show/1">${usuario.empresa.nombre}</a></span>

                    </li>

                    <li class="fieldcontain">
                        <span id="roles-label" class="property-label"><s:message code="rol.list.label" /></span>

                        <span class="property-value" aria-labelledby="roles-label">

                            <c:forEach items="${roles}" var="rol">
                                <input name="authorities" disabled="disabled" type="checkbox" value="${rol.authority}" <c:if test="${not empty seleccionados[rol.authority]}" >checked="checked"</c:if> /> <s:message code="${rol.authority}" />&nbsp;
                            </c:forEach>

                        </span>
                    </li>


                </ol>
                <fieldset class="buttons">
                    <form:hidden path="id" />
                    <a href="<c:url value='/admin/usuario/edita/${usuario.id}' />" class="edit"><s:message code="editar.button" /></a>
                    <input type="submit" name="elimina" value="<s:message code='eliminar.button'/>" class="delete" onclick="return confirm('<s:message code="confirma.elimina.message" />');" />
                </fieldset>
            </form:form>
        </div>
        <div id="spinner" class="spinner" style="display:none;"><s:message code="cargando.message" />&hellip;</div>

        <script src="<c:url value='/js/application.js' />" type="text/javascript" ></script>
        <script type="text/javascript">
            highlightTableRows('usuarios')
        </script>                    
    </body>
</html>
