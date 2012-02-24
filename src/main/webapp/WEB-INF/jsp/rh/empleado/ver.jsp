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
        <title><s:message code="usuario.ver.label" /></title>
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
                <li><a href="<s:url value='/admin/tipoCliente'/>" ><s:message code="tipoCliente.label" /></a></li>
                <li><a href="<s:url value='/admin/proveedor'/>" ><s:message code="proveedor.label" /></a></li>
                <li><a href="<s:url value='/admin/empresa'/>" ><s:message code="empresa.label" /></a></li>
                <li><a href="<s:url value='/admin/organizacion'/>" ><s:message code="organizacion.label" /></a></li>
                <li class="active"><a href="<s:url value='/admin/usuario'/>" ><s:message code="usuario.label" /></a></li>
            </ul>
        </header>

        <div id="ver-usuario" class="content scaffold-list" role="main">
            <h1><s:message code="usuario.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/admin/usuario'/>"><i class="icon-list icon-white"></i> <s:message code='usuario.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/admin/usuario/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='usuario.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/admin/usuario/elimina" />
            <form:form commandName="usuario" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span8">
                        <h4><s:message code="usuario.username.label" /></h4>
                        <h3>${usuario.username}</h3>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span8">
                        <h4><s:message code="usuario.nombre.label" /></h4>
                        <h3>${usuario.nombre}</h3>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span8">
                        <h4><s:message code="usuario.apellido.label" /></h4>
                        <h3>${usuario.apellido}</h3>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span8">
                        <h4><s:message code="empresa.label" /></h4>
                        <h3>${usuario.empresa.nombre}</h3>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span8">
                        <h4><s:message code="rol.list.label" /></h4>
                        <h3>
                            <c:forEach items="${roles}" var="rol">
                                <form:checkbox path="roles" value="${rol.authority}" disabled="true" /> <s:message code="${rol.authority}" />&nbsp;
                            </c:forEach>
                        </h3>
                    </div>
                </div>
                <p class="well">
                    <a href="<c:url value='/admin/usuario/edita/${usuario.id}' />" class="btn btn-primary"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <input type="submit" name="elimina" value="<s:message code='eliminar.button'/>" class="btn btn-danger icon-remove" style="margin-bottom: 2px;" onclick="return confirm('<s:message code="confirma.elimina.message" />');" />
                </p>
            </form:form>
        </div>
    </body>
</html>
