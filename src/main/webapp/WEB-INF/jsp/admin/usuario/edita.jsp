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
        <title><s:message code="usuario.edita.label" /></title>
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
            <h1><s:message code="usuario.edita.label" /></h1>
            <c:url var="actualizaUrl" value="/admin/usuario/actualiza" />
            <form:form commandName="usuario" method="post" action="${actualizaUrl}">
                <form:errors path="*" cssClass="errors" element="ul" />
                <form:hidden path="id" />
                <form:hidden path="version" />
                <fieldset class="form">

                    <div class="fieldcontain required">
                        <label for="username">
                            <s:message code="usuario.username.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <form:input path="username" maxlength="128" required="true" />
                        <form:errors path="username" cssClass="errors" />
                    </div>

                    <div class="fieldcontain  required">
                        <label for="nombre">
                            <s:message code="usuario.nombre.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <form:input path="nombre" maxlength="128" required="true" />
                        <form:errors path="nombre" cssClass="errors" />
                    </div>

                    <div class="fieldcontain  required">
                        <label for="apellido">
                            <s:message code="usuario.apellido.label" />
                            <span class="required-indicator">*</span>
                        </label>
                        <form:input path="apellido" maxlength="128" required="true" />
                        <form:errors path="apellido" cssClass="errors" />
                    </div>

                    <div class="fieldcontain  required">
                        <label for="roles">
                            <s:message code="usuario.authorities.label" />
                            <span class="required-indicator">*</span>
                        </label>

                        <c:forEach items="${roles}" var="rol">
                            <form:checkbox path="roles" value="${rol.authority}" /> <s:message code="${rol.authority}" />&nbsp;
                        </c:forEach>
                        <form:errors path="authorities" cssClass="errors" />

                    </div>


                </fieldset>
                <fieldset class="buttons">
                    <input type="submit" name="actualiza" value="<s:message code='actualizar.button' />" class="save" />
                </fieldset>
            </form:form>
        </div>
        <div id="spinner" class="spinner" style="display:none;"><s:message code="cargando.message" />&hellip;</div>

        <!-- JavaScript at the bottom for fast page loading -->

        <!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if offline -->
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="<c:url value='/js/jquery-1.7.1.min.js'/>"><\/script>')</script>

        <!-- end scripts -->        
        <script defer src="<c:url value='/js/application.js' />"></script>
        <script type="text/javascript">
            $(document).ready(function() {
                $('input#username').focus();
            });
        </script>                    
    </body>
</html>
