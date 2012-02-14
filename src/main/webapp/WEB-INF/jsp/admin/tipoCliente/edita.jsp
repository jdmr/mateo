<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="proveedor.nuevo.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/contabilidad' />"><s:message code="contabilidad.label" /></a></li>
                <li><a href="<c:url value='/inventario' />"><s:message code="inventario.label" /></a></li>
                <li class="active"><a href="<c:url value='/admin' />"><s:message code="admin.label" /></a></li>
            </ul>
        </nav>

        <header class="subhead" id="admin">
            <ul class="nav nav-pills">
                <li><a href="<s:url value='/admin/cliente'/>" ><s:message code="cliente.label" /></a></li>
                <li class="active"><a href="<s:url value='/admin/tipoCliente'/>" ><s:message code="tipoCliente.label" /></a></li>
                <li><a href="<s:url value='/admin/proveedor'/>" ><s:message code="proveedor.label" /></a></li>
                <li><a href="<s:url value='/admin/empresa'/>" ><s:message code="empresa.label" /></a></li>
                <li><a href="<s:url value='/admin/organizacion'/>" ><s:message code="organizacion.label" /></a></li>
                <li><a href="<s:url value='/admin/usuario'/>" ><s:message code="usuario.label" /></a></li>
            </ul>
        </header>

        <div id="nuevo-tipoCliente" class="content scaffold-list" role="main">
            <h1><s:message code="tipoCliente.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/admin/tipoCliente'/>"><i class="icon-list icon-white"></i> <s:message code='tipoCliente.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/admin/tipoCliente/actualiza" />
            <form:form commandName="tipoCliente" action="${actualizaUrl}" method="post">
                <form:hidden path="id" />
                <form:hidden path="version" />
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="tipoCliente.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="tipoCliente.descripcion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="descripcion">
                                <s:message code="descripcion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:textarea path="descripcion" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="descripcion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="tipoCliente.margenUtilidad">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="margenUtilidad">
                                <s:message code="margenUtilidad.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="margenUtilidad" maxlength="128" required="true" cssClass="span3" cssStyle="text-align:right;" />
                            <form:errors path="margenUtilidad" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <input type="submit" name="actualiza" class="btn btn-primary btn-large span2" value="<s:message code='actualizar.button'/>" id="crea" />
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
