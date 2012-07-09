<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="activo.nuevo.label" /></title>
        <link rel="stylesheet" href="<c:url value='/css/chosen.css' />" type="text/css">
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="activo" />
        </jsp:include>

        <div id="nuevo-producto" class="content scaffold-list" role="main">
            <h1><s:message code="activo.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/activoFijo/activo'/>"><i class="icon-list icon-white"></i> <s:message code='activo.lista.label' /></a>
            </p>
            <form:form commandName="activo" action="crea" method="post" enctype="multipart/form-data">
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
                        <div class="span4">
                            <s:bind path="activo.codigo">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="codigo">
                                        <s:message code="codigo.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="codigo" maxlength="64" required="true" cssClass="span2" />
                                    <form:errors path="codigo" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.descripcion">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="descripcion">
                                        <s:message code="descripcion.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:textarea path="descripcion" required="true" cssClass="span4" />
                                    <form:errors path="descripcion" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.marca">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="marca">
                                        <s:message code="marca.label" />
                                    </label>
                                    <form:input path="marca" maxlength="64" cssClass="span4" />
                                    <form:errors path="marca" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.modelo">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="modelo">
                                        <s:message code="modelo.label" />
                                    </label>
                                    <form:input path="modelo" maxlength="64" cssClass="span4" />
                                    <form:errors path="modelo" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.serial">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="serial">
                                        <s:message code="serial.label" />
                                    </label>
                                    <form:input path="serial" maxlength="64" cssClass="span4" />
                                    <form:errors path="serial" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.condicion">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="condicion">
                                        <s:message code="condicion.label" />
                                    </label>
                                    <form:input path="condicion" maxlength="64" cssClass="span4" />
                                    <form:errors path="condicion" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.factura">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="factura">
                                        <s:message code="factura.label" />
                                    </label>
                                    <form:input path="factura" maxlength="32" cssClass="span2" />
                                    <form:errors path="factura" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.poliza">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="poliza">
                                        <s:message code="poliza.label" />
                                    </label>
                                    <form:input path="poliza" maxlength="64" cssClass="span4" />
                                    <form:errors path="poliza" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.pedimento">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="pedimento">
                                        <s:message code="pedimento.label" />
                                    </label>
                                    <form:input path="pedimento" maxlength="64" cssClass="span2" />
                                    <form:errors path="pedimento" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.procedencia">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="procedencia">
                                        <s:message code="procedencia.label" />
                                    </label>
                                    <form:input path="procedencia" maxlength="64" cssClass="span4" />
                                    <form:errors path="procedencia" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.moneda">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="moneda">
                                        <s:message code="moneda.label" />
                                    </label>
                                    <form:input path="moneda" maxlength="64" cssClass="span2" />
                                    <form:errors path="moneda" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.tipoCambio">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="tipoCambio">
                                        <s:message code="tipoCambio.label" />
                                    </label>
                                    <form:input path="tipoCambio" cssClass="span4" cssStyle="text-align:right;" type="number" step="0.01" min="0" />
                                    <form:errors path="tipoCambio" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.fechaCompra">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="fechaCompra">
                                        <s:message code="fechaCompra.label" />
                                    </label>
                                    <form:input path="fechaCompra" maxlength="64" cssClass="span2" />
                                    <form:errors path="fechaCompra" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.ubicacion">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="ubicacion">
                                        <s:message code="ubicacion.label" />
                                    </label>
                                    <form:input path="ubicacion" maxlength="64" cssClass="span4" />
                                    <form:errors path="ubicacion" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.responsable">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="responsable">
                                        <s:message code="responsable.label" />
                                    </label>
                                    <form:input path="responsable" maxlength="64" cssClass="span4" />
                                    <form:errors path="responsable" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.motivo">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="motivo">
                                        <s:message code="motivo.label" />
                                    </label>
                                    <form:select id="motivo" path="motivo" required="true" cssClass="span4" >
                                        <form:options items="${motivos}" />
                                    </form:select>
                                    <form:errors path="motivo" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.moi">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="moi">
                                        <s:message code="moi.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="moi" cssClass="span4" required="true" cssStyle="text-align:right;" type="number" step="0.01" min="0" />
                                    <form:errors path="moi" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.valorRescate">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="valorRescate">
                                        <s:message code="valorRescate.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="valorRescate" cssClass="span4" required="true" cssStyle="text-align:right;" type="number" step="0.01" min="0" />
                                    <form:errors path="valorRescate" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.inpc">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="inpc">
                                        <s:message code="inpc.label" />
                                    </label>
                                    <form:input path="inpc" cssClass="span4" required="true" cssStyle="text-align:right;" type="number" step="0.01" min="0" />
                                    <form:errors path="inpc" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.seguro">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="seguro">
                                        <s:message code="seguro.label" />
                                        <form:checkbox path="seguro" maxlength="64" />
                                    </label>
                                    <form:errors path="seguro" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.garantia">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="garantia">
                                        <s:message code="garantia.label" />
                                        <form:checkbox path="garantia" maxlength="64" />
                                    </label>
                                    <form:errors path="garantia" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.mesesGarantia">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="mesesGarantia">
                                        <s:message code="mesesGarantia.label" />
                                    </label>
                                    <form:input path="mesesGarantia" cssClass="span4" cssStyle="text-align:right;" type="number" step="1" min="0" max="600" />
                                    <form:errors path="mesesGarantia" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.proveedor">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="proveedorNombre">
                                        <s:message code="proveedor.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:hidden id="proveedorId" path="proveedor.id" />
                                    <form:input id="proveedorNombre" path="proveedor.nombre" cssClass="span4" />
                                    <form:errors path="proveedor" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="activo.tipoActivo">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="tipoActivo">
                                        <s:message code="tipoActivo.label" />
                                    </label>
                                    <form:select id="tipoActivoId" path="tipoActivo.id" required="true" cssClass="span4" >
                                        <form:options items="${tiposDeActivo}" itemValue="id" itemLabel="nombre" />
                                    </form:select>
                                    <form:errors path="tipoActivo" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="activo.cuenta">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="cuentaId">
                                        <s:message code="cuenta.label" />
                                    </label>
                                    <form:select id="cuentaId" path="cuenta.id" required="true" cssClass="span4" >
                                        <form:options items="${cuentas}" itemValue="id" itemLabel="nombreCompleto" />
                                    </form:select>
                                    <form:errors path="cuenta" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <div class="control-group">
                                <label for="imagen">
                                    <s:message code="imagen.label" />
                                </label>
                                <input name="imagen" type="file" />
                            </div>
                        </div>
                    </div>
                </fieldset>
                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inventario/producto'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script src="<c:url value='/js/chosen.jquery.min.js' />"></script>
            <script>
                $(document).ready(function() {
                    $('input#proveedorNombre')
                        .autocomplete({
                            source: "<c:url value='/inventario/entrada/proveedores' />",
                            select: function(event, ui) {
                                $("input#proveedorId").val(ui.item.id);
                                return false;
                            }
                        });
                    $("select#motivo").chosen();
                    $("select#tipoActivoId").chosen();
                    $("select#cuentaId").chosen();
                    $("input#fechaCompra").datepicker($.datepicker.regional['es']);
                    $("input#fechaCompra").datepicker("option","firstDay",0);
                    $('input#codigo').focus();
                });
            </script>                    
        </content>
    </body>
</html>
