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
        <title><s:message code="organizacion.nueva.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/admin' />"><s:message code="admin.label" /></a></li>
                <li><a href="<s:url value='/admin/cliente'/>" ><s:message code="cliente.label" /></a></li>
                <li><a href="<s:url value='/admin/tipoCliente'/>" ><s:message code="tipoCliente.label" /></a></li>
                <li><a href="<s:url value='/admin/proveedor'/>" ><s:message code="proveedor.label" /></a></li>
                <li><a href="<s:url value='/admin/empresa'/>" ><s:message code="empresa.label" /></a></li>
                <li class="active"><a href="<s:url value='/admin/organizacion'/>" ><s:message code="organizacion.label" /></a></li>
                <li><a href="<s:url value='/admin/usuario'/>" ><s:message code="usuario.label" /></a></li>
            </ul>
        </nav>

        <div id="nueva-organizacion" class="content scaffold-list" role="main">
            <h1><s:message code="organizacion.nueva.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/admin/organizacion'/>"><i class="icon-list icon-white"></i> <s:message code='organizacion.lista.label' /></a>
            </p>
            <form:form commandName="organizacion" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="organizacion.codigo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="codigo">
                                <s:message code="codigo.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="codigo" maxlength="128" required="true" />
                            <form:errors path="codigo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="organizacion.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="organizacion.nombreCompleto">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombreCompleto">
                                <s:message code="nombreCompleto.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombreCompleto" maxlength="128" required="true" />
                            <form:errors path="nombreCompleto" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <input type="submit" name="_action_crea" class="btn btn-primary btn-large" value="<s:message code='crear.button'/>" id="crea" />
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#codigo').focus();
                });
            </script>                    
        </content>
    </body>
</html>
