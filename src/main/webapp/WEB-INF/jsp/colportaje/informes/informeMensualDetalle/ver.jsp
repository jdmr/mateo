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
        <title><s:message code="informeMensualDetalle.ver.label" /> de <fmt:formatDate pattern="MMMM/yyyy" value="${informeMensual.fecha}" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeMensualDetalle" />
        </jsp:include>

        <div id="ver-informeMensualDetalle" class="content scaffold-list" role="main">
            <h1><s:message code="informeMensualDetalle.ver.label" /> de <fmt:formatDate pattern="MMMM/yyyy" value="${informeMensual.fecha}" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/informes/informeMensualDetalle'/>"><i class="icon-list icon-white"></i> <s:message code='informeMensualDetalle.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/colportaje/informes/informeMensualDetalle/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='informeMensualDetalle.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/colportaje/informes/informeMensualDetalle/elimina" />
            <form:form commandName="informeMensualDetalle" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="fecha.label" /></h4>
                        <h3><fmt:formatDate pattern="dd/MMM" value="${informeMensualDetalle.fecha}"/></h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="hrsTrabajadas.label" /></h4>
                        <h3>${informeMensualDetalle.hrsTrabajadas}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="literaturaVendida.label" /></h4>
                        <h3>${informeMensualDetalle.literaturaVendida}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="totalPedidos.label" /></h4>
                        <h3>${informeMensualDetalle.totalPedidos}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="totalVentas.label" /></h4>
                        <h3>${informeMensualDetalle.totalVentas}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="literaturaGratis.label" /></h4>
                        <h3>${informeMensualDetalle.literaturaGratis}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="oracionesOfrecidas.label" /></h4>
                        <h3>${informeMensualDetalle.oracionesOfrecidas}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="casasVisitadas.label" /></h4>
                        <h3>${informeMensualDetalle.casasVisitadas}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="contactosEstudiosBiblicos.label" /></h4>
                        <h3>${informeMensualDetalle.contactosEstudiosBiblicos}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="bautizados.label" /></h4>
                        <h3>${informeMensualDetalle.bautizados}</h3>
                    </div>
                </div>

                <p class="well">
                    <a href="<c:url value='/colportaje/informes/informeMensualDetalle/edita/${informeMensualDetalle.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
