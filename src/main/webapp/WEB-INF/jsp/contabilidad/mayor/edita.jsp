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
        <title><s:message code="cuentaMayor.edita.label" /></title>
    </head>
    <body>
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/contabilidad' />"><s:message code="contabilidad.label" /></a></li>
                <li class="active"><a href="<s:url value='/contabilidad/mayor'/>" ><s:message code="cuentaMayor.label" /></a></li>
            </ul>
        </nav>

        <div id="edita-mayor" class="content scaffold-list" role="main">
            <h1><s:message code="cuentaMayor.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/contabilidad/mayor'/>"><i class="icon-list icon-white"></i> <s:message code='cuentaMayor.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/contabilidad/mayor/actualiza" />
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
                    <s:bind path="mayor.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="cuentaMayor.nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="mayor.nombreFiscal">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombreFiscal">
                                <s:message code="cuentaMayor.nombreFiscal.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombreFiscal" maxlength="128" required="true" />
                            <form:errors path="nombreFiscal" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <input type="submit" name="actualiza" value="<s:message code='actualizar.button' />" class="btn btn-large btn-primary" />
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
