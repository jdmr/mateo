<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="lote.nuevo.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/inventario' />"><s:message code="inventario.label" /></a></li>
                <li><a href="<s:url value='/inventario/salida'/>" ><s:message code="salida.lista.label" /></a></li>
                <li class="active"><a href="<s:url value='/inventario/entrada'/>" ><s:message code="entrada.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/producto'/>" ><s:message code="producto.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/tipoProducto'/>" ><s:message code="tipoProducto.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/almacen'/>" ><s:message code="almacen.lista.label" /></a></li>
            </ul>
        </nav>

        <div id="nuevo-entrada" class="content scaffold-list" role="main">
            <h1><s:message code="lote.nuevo.label" /></h1>
            <c:url var="creaUrl" value="/inventario/entrada/lote/crea" />
            <form:form commandName="lote" action="${creaUrl}" method="post">
                <form:hidden path="entrada.id" id="entradaId" />
                <form:hidden path="producto.id" id="productoId" />
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
                            <s:bind path="lote.producto">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="productoNombre">
                                        <s:message code="producto.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input id="productoNombre" path="producto.nombre" cssClass="span12" />
                                    <form:errors path="producto" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="lote.cantidad">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="cantidad">
                                        <s:message code="cantidad.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="cantidad" cssClass="span6" required="true" cssStyle="text-align:right;" type="number" step="0.001" min="0" />
                                    <form:errors path="cantidad" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span12">
                            <s:bind path="lote.precioUnitario">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="precioUnitario">
                                        <s:message code="precioUnitario.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="precioUnitario" cssClass="span6" required="true" cssStyle="text-align:right;" type="number" step="0.01" min="0" />
                                    <form:errors path="precioUnitario" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                </fieldset>
                <p class="well" style="margin-top: 10px;">
                    <input type="submit" name="_action_crea" class="btn btn-primary btn-large span2" value="<s:message code='crear.button'/>" id="crea" />
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#productoNombre')
                        .autocomplete({
                            source: "<c:url value='/inventario/entrada/productos' />",
                            select: function(event, ui) {
                                $("input#productoId").val(ui.item.id);
                                $("input#cantidad").focus();
                                return false;
                            }
                        })
                        .focus();
                });
            </script>                    
        </content>
    </body>
</html>
