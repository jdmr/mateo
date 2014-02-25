<%-- 
    Document   : ver
    Created on : Mar 07, 2013, 6:52:45 AM
    Author     : osoto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="pedidoColportor.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="pedidoColportor" />
        </jsp:include>

        <div id="ver-pedidoColportor" class="content scaffold-list" role="main">
            <h1><s:message code="pedidoColportor.ver.label" /></h1>
            
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/ventas/pedidos'/>">
                    <i class="icon-list icon-white"></i> <s:message code='pedidoColportor.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/colportaje/ventas/pedidos/nuevo'/>">
                    <i class="icon-file icon-white"></i> <s:message code='pedidoColportor.nuevo.label' /></a>
                    <a class="btn btn-primary" href="<s:url value='/colportaje/ventas/pedidos/finalizar/${pedidoColportor.id}'/>">
                    <i class="icon-file icon-white"></i> <s:message code='pedidoColportor.finalizar.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/colportaje/ventas/pedidos/elimina" />
            <form:form commandName="pedidoColportor" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="numPedido.label" /></h4>
                        <h3>${pedidoColportor.numPedido}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="cliente.label" /></h4>
                        <h3>${pedidoColportor.cliente.nombreCompleto}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="lugar.label" /></h4>
                        <h3>${pedidoColportor.lugar}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="fechaPedido.label" /></h4>
                        <h3><fmt:formatDate pattern="dd/MMM/yyyy" value="${pedidoColportor.fechaPedido}" /></h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="horaPedido.label" /></h4>
                        <h3>${pedidoColportor.horaPedido}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="fechaEntrega.label" /></h4>
                        <h3><fmt:formatDate pattern="dd/MMM/yyyy" value="${pedidoColportor.fechaEntrega}" /></h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="horaEntrega.label" /></h4>
                        <h3>${pedidoColportor.horaEntrega}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="formaPago.label" /></h4>
                        <h3>${pedidoColportor.formaPagoValue}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="razonSocial.label" /></h4>
                        <h3>${pedidoColportor.razonSocial}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="observaciones.label" /></h4>
                        <h3>${pedidoColportor.observaciones}</h3>
                    </div>
                </div>

                <p class="well">
                    <a href="<c:url value='/colportaje/ventas/pedidos/edita/${pedidoColportor.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                    <a href="<c:url value='/colportaje/ventas/items/lista?pedidoId=${pedidoColportor.id}' />" class="btn btn-primary btn-large"><i class="icon-list icon-white"></i> <s:message code="items.button" /></a>
                    <a href="<c:url value='/colportaje/ventas/recibos/lista?pedidoId=${pedidoColportor.id}' />" class="btn btn-primary btn-large"><i class="icon-list icon-white"></i> <s:message code="recibos.button" /></a>
                </p>
            </form:form>
        </div>
    </body>
</html>
