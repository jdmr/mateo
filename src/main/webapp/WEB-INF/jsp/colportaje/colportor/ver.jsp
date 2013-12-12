<%-- 
    Document   : ver
    Created on : Mar 07, 2013, 6:52:45 AM
    Author     : osoto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="colportor.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="colportor" />
        </jsp:include>

        <div id="ver-colportor" class="content scaffold-list" role="main">
            <h1><s:message code="colportor.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/colportor'/>"><i class="icon-list icon-white"></i> <s:message code='colportor.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/colportaje/colportor/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='colportor.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/colportaje/colportor/elimina" />
            <form:form commandName="colportor" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="nombre.label" /></h4>
                        <h3>${colportor.nombre}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="apPaterno.label" /></h4>
                        <h3>${colportor.apPaterno}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="apMaterno.label" /></h4>
                        <h3>${colportor.apMaterno}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="clave.label" /></h4>
                        <h3>${colportor.clave}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="telefono.label" /></h4>
                        <h3>${colportor.telefono}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="correo.label" /></h4>
                        <h3>${colportor.telefono}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="calle.label" /></h4>
                        <h3>${colportor.calle}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="colonia.label" /></h4>
                        <h3>${colportor.colonia}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="municipio.label" /></h4>
                        <h3>${colportor.municipio}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="fechaDeNacimiento.label" /></h4>
                        <h3><fmt:formatDate pattern="dd/MMM/yyyy" value="${colportor.fechaDeNacimiento}" /></h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="matricula.label" /></h4>
                        <h3>${colportor.matricula}</h3>
                    </div>
                </div>

                <p class="well">
                    <a href="<c:url value='/colportaje/colportor/edita/${colportor.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
