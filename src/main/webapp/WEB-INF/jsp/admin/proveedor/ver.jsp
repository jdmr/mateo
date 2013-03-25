<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="proveedor.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="proveedor" />
        </jsp:include>

        <div id="ver-proveedor" class="content scaffold-list" role="main">
            <h1><s:message code="proveedor.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/admin/proveedor'/>"><i class="icon-list icon-white"></i> <s:message code='proveedor.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/admin/proveedor/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='proveedor.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/admin/proveedor/elimina" />
            <form:form commandName="proveedor" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="nombre.label" /></h4>
                        <h3>${proveedor.nombre}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="nombreCompleto.label" /></h4>
                        <h3>${proveedor.nombreCompleto}</h3>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="rfc.label" /></h4>
                        <h3>${proveedor.rfc}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="curp.label" /></h4>
                        <h3>${proveedor.curp}</h3>
                    </div>
                </div>

                <c:if test="${not empty proveedor.direccion}">
                    <div class="row-fluid" style="padding-bottom: 10px;">
                        <div class="span8">
                            <h4><s:message code="direccion.label" /></h4>
                            <h3>${proveedor.direccion}</h3>
                        </div>
                    </div>
                </c:if>

                <c:if test="${not empty proveedor.telefono || not empty proveedor.fax}">
                    <div class="row-fluid" style="padding-bottom: 10px;">
                        <div class="span4">
                            <h4><s:message code="telefono.label" /></h4>
                            <h3>${proveedor.telefono}</h3>
                        </div>
                        <div class="span4">
                            <h4><s:message code="fax.label" /></h4>
                            <h3>${proveedor.fax}</h3>
                        </div>
                    </div>
                </c:if>

                <c:if test="${not empty proveedor.contacto || not empty proveedor.correo}">
                    <div class="row-fluid" style="padding-bottom: 10px;">
                        <div class="span4">
                            <h4><s:message code="contacto.label" /></h4>
                            <h3>${proveedor.contacto}</h3>
                        </div>
                        <div class="span4">
                            <h4><s:message code="correo.label" /></h4>
                            <h3>${proveedor.correo}</h3>
                        </div>
                    </div>
                </c:if>

                <p class="well">
                    <a href="<c:url value='/admin/proveedor/edita/${proveedor.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
