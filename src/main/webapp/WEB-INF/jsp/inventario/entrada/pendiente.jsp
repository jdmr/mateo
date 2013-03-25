<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="entrada.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="entrada" />
        </jsp:include>

        <div id="pendiente-entrada" class="content scaffold-list" role="main">
            <h1><s:message code="entrada.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inventario/entrada'/>"><i class="icon-list icon-white"></i> <s:message code='entrada.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/inventario/entrada/pendiente/cerrar" />
            <form:form commandName="entrada" action="${actualizaUrl}" method="post">
                <form:hidden path="id" />
                <form:hidden path="version" />
                <form:hidden path="proveedor.id" id="proveedorId" />
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="entrada.factura">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="factura">
                                        <s:message code="factura.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="factura" required="true" cssClass="span6" />
                                    <form:errors path="factura" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="entrada.fechaFactura">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="fechaFactura">
                                        <s:message code="fechaFactura.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="fechaFactura" required="true" maxlength="64" cssClass="span6" />
                                    <form:errors path="fechaFactura" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="entrada.comentarios">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="comentarios">
                                        <s:message code="comentarios.label" />
                                    </label>
                                    <form:textarea path="comentarios" cssClass="span6" />
                                    <form:errors path="comentarios" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inventario/entrada/ver/${entrada.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#factura').focus();
                });
            </script>                    
        </content>
    </body>
</html>
