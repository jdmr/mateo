<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="cliente.ver.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/admin' />"><s:message code="admin.label" /></a></li>
                <li class="active"><a href="<s:url value='/admin/cliente'/>" ><s:message code="cliente.label" /></a></li>
                <li><a href="<s:url value='/admin/tipoCliente'/>" ><s:message code="tipoCliente.label" /></a></li>
                <li><a href="<s:url value='/admin/proveedor'/>" ><s:message code="proveedor.label" /></a></li>
                <li><a href="<s:url value='/admin/empresa'/>" ><s:message code="empresa.label" /></a></li>
                <li><a href="<s:url value='/admin/organizacion'/>" ><s:message code="organizacion.label" /></a></li>
                <li><a href="<s:url value='/admin/usuario'/>" ><s:message code="usuario.label" /></a></li>
            </ul>
        </nav>

        <div id="ver-cliente" class="content scaffold-list" role="main">
            <h1><s:message code="cliente.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/admin/cliente'/>"><i class="icon-list icon-white"></i> <s:message code='cliente.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/admin/cliente/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='cliente.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/admin/cliente/elimina" />
            <form:form commandName="cliente" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="nombre.label" /></h4>
                        <h3>${cliente.nombre}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="nombreCompleto.label" /></h4>
                        <h3>${cliente.nombreCompleto}</h3>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="rfc.label" /></h4>
                        <h3>${cliente.rfc}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="curp.label" /></h4>
                        <h3>${cliente.curp}</h3>
                    </div>
                </div>

                <c:if test="${not empty cliente.direccion}">
                    <div class="row-fluid" style="padding-bottom: 10px;">
                        <div class="span8">
                            <h4><s:message code="direccion.label" /></h4>
                            <h3>${cliente.direccion}</h3>
                        </div>
                    </div>
                </c:if>

                <c:if test="${not empty cliente.telefono || not empty cliente.fax}">
                    <div class="row-fluid" style="padding-bottom: 10px;">
                        <div class="span4">
                            <h4><s:message code="telefono.label" /></h4>
                            <h3>${cliente.telefono}</h3>
                        </div>
                        <div class="span4">
                            <h4><s:message code="fax.label" /></h4>
                            <h3>${cliente.fax}</h3>
                        </div>
                    </div>
                </c:if>

                <c:if test="${not empty cliente.contacto || not empty cliente.correo}">
                    <div class="row-fluid" style="padding-bottom: 10px;">
                        <div class="span4">
                            <h4><s:message code="contacto.label" /></h4>
                            <h3>${cliente.contacto}</h3>
                        </div>
                        <div class="span4">
                            <h4><s:message code="correo.label" /></h4>
                            <h3>${cliente.correo}</h3>
                        </div>
                    </div>
                </c:if>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="tipoCliente.label" /></h4>
                        <h3>${cliente.tipoCliente.nombre}</h3>
                    </div>
                </div>

                <p class="well">
                    <a href="<c:url value='/admin/cliente/edita/${cliente.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="crear"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
