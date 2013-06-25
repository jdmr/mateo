<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="informeProveedorDetalle.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informeProveedorDetalle" />
        </jsp:include>

        <div id="edita-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="informeProveedorDetalle.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/factura/informeProveedorDetalle'/>"><i class="icon-list icon-white"></i> <s:message code='informeProveedorDetalle.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/factura/informeProveedorDetalle/actualiza" />
            <form:form commandName="informeProveedorDetalle" method="post" action="${actualizaUrl}">
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
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="proveedor.label" /></div>
                    <div class="span11">${proveedor.nombre}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="rfc.label" /></div>
                    <div class="span11">${proveedor.rfc}</div>
                </div>
                <fieldset>
                    <s:bind path="informeProveedorDetalle.folioFactura">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="folioFactura">
                                <s:message code="folio.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="folioFactura" maxlength="150" required="true" />
                            <form:errors path="folioFactura" cssClass="alert alert-error" />
                        </div>
                    </s:bind>



                    <s:bind path="informeProveedorDetalle.IVA" >
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="IVA" >
                                <s:message code="iva.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="IVA"   maxlength="150" required="true"  />
                            <form:errors path="IVA" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeProveedorDetalle.subtotal">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="subtotal">
                                <s:message code="subtotal.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="subtotal"  maxlength="150" required="true"  />
                            <form:errors path="subtotal" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeProveedorDetalle.total">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="total">
                                <s:message code="total.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="total"    maxlength="150" required="true"   />
                            <form:errors path="total" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeProveedorDetalle.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="150" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informeProveedorDetalle.fechaFactura">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fechaFactura">
                                <s:message code="fechaFactura.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaFactura" maxlength="12" required="true" />
                            <form:errors path="fechaFactura" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>
                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/informeProveedorDetalle/ver/${informeProveedorDetalle.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#nombre').focus();
                $("input#fechaFactura").datepicker($.datepicker.regional['es']);
                $("input#fechaFactura").datepicker("option", "firstDay", 0);
            });
        </script>                    
    </content>
</body>
</html>
