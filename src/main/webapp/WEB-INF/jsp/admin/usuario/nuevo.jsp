<%-- 
    Document   : nuevo
    Created on : Jan 27, 2012, 10:37:52 AM
    Author     : jdmr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="usuario.nuevo.label" /></title>
    </head>
    <body>
        <a href="#nuevo-usuario" class="skip" tabindex="-1"><s:message code="brincar.al.contenido" />&hellip;</a>
        <div class="nav" role="navigation">
            <ul>
                <li><a href="<s:url value='/admin'/>" class="admin"><s:message code="admin.label" /></a></li>
                <li><a href="<s:url value='/admin/usuario'/>" class="list"><s:message code="usuario.lista.label" /></a></li>
            </ul>
        </div>

        <div id="nuevo-usuario" class="content scaffold-list" role="main">
            <h1><s:message code="usuario.nuevo.label" /></h1>
            <form:form commandName="usuario" action="crea" method="post">
                <fieldset class="form">

                    <div class="fieldcontain  required">
                        <label for="username">
                            <s:message code="usuario.username.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <form:input path="username" maxlength="128" />
                        <form:errors path="username" />
                    </div>

                    <div class="fieldcontain  required">
                        <label for="password">
                            <s:message code="usuario.password.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <form:password path="password" />
                        <form:errors path="password" />
                    </div>

                    <div class="fieldcontain  required">
                        <label for="nombre">
                            <s:message code="usuario.nombre.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <form:input path="nombre" maxlength="128" />
                        <form:errors path="nombre" />
                    </div>

                    <div class="fieldcontain  required">
                        <label for="apellido">
                            <s:message code="usuario.apellido.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <form:input path="apellido" maxlength="128" />
                        <form:errors path="apellido" />
                    </div>

                    <div class="fieldcontain  required">
                        <label for="correo">
                            <s:message code="usuario.correo.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <form:input path="correo" maxlength="128" />
                        <form:errors path="correo" />
                    </div>


                    <div class="fieldcontain  required">
                        <label for="roles">
                            Roles
                            <span class="required-indicator">*</span>
                        </label>

                        <c:forEach items="${roles}" var="rol">
                            <input name="roles" type="checkbox" value="${rol.authority}" <c:if test="${not empty seleccionados[rol.authority]}" >checked="checked"</c:if> /> <s:message code="${rol.authority}" />&nbsp;
                        </c:forEach>

                    </div>


                </fieldset>
                <fieldset class="buttons">
                    <input type="submit" name="create" class="save" value="<s:message code='crear.button'/>" id="create" />
                </fieldset>
            </form:form>
        </div>
        <div id="spinner" class="spinner" style="display:none;"><s:message code="cargando.message" />&hellip;</div>

        <script src="<c:url value='/js/application.js' />" type="text/javascript" ></script>
        <script type="text/javascript">
        </script>                    
    </body>
</html>
