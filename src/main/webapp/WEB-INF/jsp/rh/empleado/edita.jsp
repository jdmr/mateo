<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="empleado.nuevo.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/rh' />"><s:message code="rh.label" /></a></li>
                <li class="active"><a href="<s:url value='/rh/empleado'/>" ><s:message code="rh.label" /></a></li>
            </ul>
        </nav>

       <div id="edita-mayor" class="content scaffold-list" role="main">
            <h1><s:message code="empleado.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/contabilidad/mayor'/>"><i class="icon-list icon-white"></i> <s:message code='empleado.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/rh/empleado/actualiza" />
            <form:form commandName="mayor" method="post" action="${actualizaUrl}">
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
                    <s:bind path="empleado.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleado.apPaterno">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="apPaterno">
                                <s:message code="apPaterno.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="apPaterno" maxlength="128" required="true" />
                            <form:errors path="apPaterno" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/empleado/ver/${mayor.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#nombre').focus();
                });
            </script>                    
        </content>
    </body>
</html>
