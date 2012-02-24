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
        <title><s:message code="organizacion.ver.label" /></title>
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

        <div id="ver-organizacion" class="content scaffold-list" role="main">
            <h1><s:message code="organizacion.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/admin/organizacion'/>"><i class="icon-list icon-white"></i> <s:message code='organizacion.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/admin/organizacion/nueva'/>"><i class="icon-file icon-white"></i> <s:message code='organizacion.nueva.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/admin/organizacion/elimina" />
            <form:form commandName="organizacion" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span6">
                        <h4><s:message code="codigo.label" /></h4>
                        <h3>${organizacion.codigo}</h3>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span6">
                        <h4><s:message code="nombre.label" /></h4>
                        <h3>${organizacion.nombre}</h3>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span6">
                        <h4><s:message code="nombreCompleto.label" /></h4>
                        <h3>${organizacion.nombreCompleto}</h3>
                    </div>
                </div>

                <p class="well">
                    <a href="<c:url value='/admin/organizacion/edita/${organizacion.id}' />" class="btn btn-primary"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <input type="submit" name="elimina" value="<s:message code='eliminar.button'/>" class="btn btn-danger icon-remove" style="margin-bottom: 2px;" onclick="return confirm('<s:message code="confirma.elimina.message" />');" />
                </p>
            </form:form>
        </div>
    </body>
</html>
