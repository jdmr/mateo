<%-- 
    Document   : edita
    Created on : Feb 13, 2012, 3:15:52 PM
    Author     : J. David Mendoza <jdmendoza@um.edu.mx>
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="perfil.edita.label" /></title>
        <link rel="stylesheet" href="<c:url value='/css/chosen.css' />" type="text/css">
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/contabilidad' />"><s:message code="contabilidad.label" /></a></li>
                <li><a href="<c:url value='/inventario' />"><s:message code="inventario.label" /></a></li>
                <li><a href="<c:url value='/admin' />"><s:message code="admin.label" /></a></li>
            </ul>
        </nav>

        <div id="edita-usuario" class="content scaffold-list" role="main">
            <h1><s:message code="perfil.edita.label" /></h1>
            <c:url var="actualizaUrl" value="/perfil/guardaPasswd" />
            <form:form commandName="usuario" method="post" action="${actualizaUrl}">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="usuario.almacen">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="almacen">
                                <s:message code="perfil.almacen.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:select path="almacen.id" id="almacenId" items="${almacenes}" itemLabel="nombreCompleto" itemValue="id"/>
                            <form:errors path="almacen" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="usuario.ejercicio">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="ejercicio">
                                <s:message code="ejercicio.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:select path="ejercicio.id.idEjercicio" id="ejercicioId" items="${ejercicios}" itemLabel="nombreCompleto" itemValue="id.idEjercicio"/>
                            <form:errors path="ejercicio" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="usuario.username">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="username">
                                <s:message code="username.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="username" required="true" />
                            <form:errors path="username" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="usuario.password">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="password">
                                <s:message code="password.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:password path="password" required="true" showPassword="true" />
                            <form:errors path="password" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script src="<c:url value='/js/chosen.jquery.min.js' />"></script>
            <script>
                $(document).ready(function() {
                    $("select#ejercicioId").chosen();
                    $("select#almacenId").chosen();
                    $('#almacenId').focus();
                });
            </script>                    
        </content>
    </body>
</html>
