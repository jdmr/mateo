

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %><!DOCTYPE html>
<html>
    <head>
        <title><s:message code="temporadaColportor.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="temporadaColportor" />
        </jsp:include>

        <div id="ver-temporadaColportor" class="content scaffold-list" role="main">
            <h1><s:message code="temporadaColportor.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='../'/>"><i class="icon-list icon-white"></i> <s:message code='temporadaColportor.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='../nueva'/>"><i class="icon-user icon-white"></i> <s:message code='temporadaColportor.nueva.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/colportaje/temporadaColportor/elimina" />
            <form:form commandName="temporadaColportor" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <h4><s:message code="colportor.label" /></h4>
                    <h3>${temporadaColportor.colportor.clave}</h3>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <h4><s:message code="asociado.label" /></h4>
                    <h3>${temporadaColportor.asociado.nombre}</h3>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <h4><s:message code="temporada.label" /></h4>
                    <h3>${temporadaColportor.temporada.nombre}</h3>
                </div>                
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <h4><s:message code="colegio.label" /></h4>
                    <h3>${temporadaColportor.colegio.nombre}</h3>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <h4><s:message code="objetivo.label" /></h4>
                    <h3>${temporadaColportor.objetivo}</h3>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <h4><s:message code="observaciones.label" /></h4>
                    <h3>${temporadaColportor.observaciones}</h3>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <h4><s:message code="fecha.label" /></h4>
                    <h3>${temporadaColportor.fecha}</h3>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <h4><s:message code="status.label" /></h4>
                    <h3>${temporadaColportor.status}</h3>
                </div>
                <p class="well">
                    <a href="<c:url value='/colportaje/temporadaColportor/edita/${temporadaColportor.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
