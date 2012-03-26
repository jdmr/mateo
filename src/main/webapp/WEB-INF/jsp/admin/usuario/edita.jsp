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
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="usuario" />
        </jsp:include>

        <div id="edita-usuario" class="content scaffold-list" role="main">
            <h1><s:message code="usuario.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/admin/usuario'/>"><i class="icon-list icon-white"></i> <s:message code='usuario.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/admin/usuario/actualiza" />
            <form:form commandName="usuario" method="post" action="${actualizaUrl}">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <form:hidden path="id" />
                <form:hidden path="version" />

                <fieldset>
                    <s:bind path="usuario.username">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="username">
                                <s:message code="username.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="username" maxlength="128" required="true" />
                            <form:errors path="username" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="usuario.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="usuario.username">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="apellido">
                                <s:message code="apellido.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="apellido" maxlength="128" required="true" />
                            <form:errors path="apellido" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="usuario.roles">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="roles">
                                <s:message code="authorities.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <c:forEach items="${roles}" var="rol">
                                <form:checkbox path="roles" value="${rol.authority}" /> <s:message code="${rol.authority}" />&nbsp;
                            </c:forEach>
                            <form:errors path="authorities" cssClass="errors" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/admin/usuario/ver/${usuario.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#username').focus();
                });
            </script>                    
        </content>
    </body>
</html>
