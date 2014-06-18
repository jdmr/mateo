<%-- 
    Document   : ver
    Created on : Mar 07, 2013, 6:52:45 AM
    Author     : osoto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="informeMensual.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeMensual" />
        </jsp:include>

        <div id="ver-informeMensual" class="content scaffold-list" role="main">
            <h1><s:message code="informeMensual.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/informes/informeMensual'/>"><i class="icon-list icon-white"></i> <s:message code='informeMensual.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/colportaje/informes/informeMensual/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='informeMensual.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/colportaje/informes/informeMensual/elimina" />
            <form:form commandName="informeMensual" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="colportor.label" /></h4>
                        <h3>${informeMensual.colportor.nombreCompleto}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="fecha.label" /></h4>
                        <h3><fmt:formatDate pattern="MMMM" value="${informeMensual.fecha}" /></h3>
                    </div>
                </div>

                <p class="well">
                    <a href="<c:url value='/colportaje/informes/informeMensual/edita/${informeMensual.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                    <a href="<c:url value='/colportaje/informes/informeMensualDetalle/lista?id=${informeMensual.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="informeMensualDetalles.button" /></a>
                    <a href="<c:url value='/colportaje/informes/informeMensualDetalle/listaSemanal?id=${informeMensual.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="informeMensualDetallesSemanal.button" /></a>
                </p>
            </form:form>
        </div>
    </body>
</html>
