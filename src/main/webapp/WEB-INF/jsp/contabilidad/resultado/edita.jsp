<%-- 
    Document   : edita
    Created on : Mar 12, 2012, 9:09:08 AM
    Author     : develop
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="cuentaResultado.edita.label" /></title>
    </head>
    <body>
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/contabilidad' />"><s:message code="contabilidad.label" /></a></li>
                <li class="active"><a href="<s:url value='/contabilidad/resultado'/>" ><s:message code="cuentaResultado.label" /></a></li>
            </ul>
        </nav>

        <div id="edita-resultado" class="content scaffold-list" role="main">
            <h1><s:message code="cuentaResultado.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/contabilidad/resultado'/>"><i class="icon-list icon-white"></i> <s:message code='cuentaResultado.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/contabilidad/resultado/actualiza" />
            <form:form commandName="resultado" method="post" action="${actualizaUrl}">
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
                    <s:bind path="resultado.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="resultado.nombreFiscal">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombreFiscal">
                                <s:message code="nombreFiscal.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombreFiscal" maxlength="128" required="true" />
                            <form:errors path="nombreFiscal" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/admin/resultado/ver/${resultado.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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