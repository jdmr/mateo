<%-- 
    Document   : nuevo
    Created on : Mar 07, 2013, 10:37:52 AM
    Author     : osoto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="pedidoColportorItem.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="pedidoColportorItem" />
        </jsp:include>

        <div id="edita-pedidoColportorItem" class="content scaffold-list" role="main">
            <h1><s:message code="pedidoColportorItem.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<c:url value='/colportaje/ventas/items/lista?pedidoId=${pedidoColportor.id}' />"><i class="icon-list icon-white"></i>
                    <s:message code='pedidoColportorItem.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/colportaje/ventas/items/actualiza" />
            <form:form commandName="pedidoColportorItem" method="post" action="${actualizaUrl}">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>
                <form:hidden path="id" />
                <form:hidden path="version" />
                <form:hidden path="pedido.id" />
                <form:hidden path="status" />

                <fieldset>
                    <s:bind path="pedidoColportorItem.item">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="item">
                                <s:message code="item.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="item" maxlength="100" required="true" />
                            <form:errors path="item" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="pedidoColportorItem.cantidad">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="cantidad">
                                <s:message code="cantidad.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="cantidad" maxlength="10" required="true" />
                            <form:errors path="cantidad" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="pedidoColportorItem.precioUnitario">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="precioUnitario">
                                <s:message code="precioUnitario.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="precioUnitario" maxlength="20" required="true" />
                            <form:errors path="precioUnitario" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="pedidoColportorItem.observaciones">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="observaciones">
                                <s:message code="observaciones.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="observaciones" maxlength="200" required="true" />
                            <form:errors path="observaciones" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/colportaje/ventas/items/ver/${pedidoColportorItem.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    </body>
</html>
