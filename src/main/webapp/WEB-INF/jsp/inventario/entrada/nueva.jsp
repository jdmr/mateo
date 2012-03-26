<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="entrada.nueva.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="entrada" />
        </jsp:include>

        <div id="nueva-entrada" class="content scaffold-list" role="main">
            <h1><s:message code="entrada.nueva.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inventario/entrada'/>"><i class="icon-list icon-white"></i> <s:message code='entrada.lista.label' /></a>
            </p>
            <form:form commandName="entrada" action="crea" method="post">
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
                            <s:bind path="entrada.proveedor">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="proveedorNombre">
                                        <s:message code="proveedor.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input id="proveedorNombre" path="proveedor.nombre" cssClass="span6" />
                                    <form:errors path="proveedor" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
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
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="entrada.devolucion">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="devolucion">
                                        <s:message code="devolucion.label" />
                                    </label>
                                    <form:checkbox path="devolucion" cssClass="span3" />
                                    <form:errors path="devolucion" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="entrada.iva">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="iva">
                                        <s:message code="iva.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="iva" cssClass="span6" required="true" cssStyle="text-align:right;" type="number" step="0.01" min="0" />
                                    <form:errors path="iva" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="entrada.total">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="total">
                                        <s:message code="total.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="total" cssClass="span6" required="true" cssStyle="text-align:right;" type="number" step="0.01" min="0" />
                                    <form:errors path="total" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="entrada.tipoCambio">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="tipoCambio">
                                        <s:message code="tipoCambio.label" />
                                    </label>
                                    <form:input path="tipoCambio" maxlength="5" cssClass="span6" cssStyle="text-align:right;" type="number" step="0.01" min="0" />
                                    <form:errors path="tipoCambio" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                </fieldset>
                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inventario/entrada'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#proveedorNombre')
                        .autocomplete({
                            source: "<c:url value='/inventario/entrada/proveedores' />",
                            select: function(event, ui) {
                                $("input#proveedorId").val(ui.item.id);
                                $("input#factura").focus();
                                return false;
                            }
                        })
                        .focus();
                    $("input#fechaFactura").datepicker($.datepicker.regional['es']);
                    $("input#fechaFactura").datepicker("option","firstDay",0);
                });
            </script>                    
        </content>
    </body>
</html>
