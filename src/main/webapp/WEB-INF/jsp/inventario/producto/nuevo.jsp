<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="producto.nuevo.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/inventario' />"><s:message code="inventario.label" /></a></li>
                <li><a href="<s:url value='/inventario/entrada'/>" ><s:message code="entrada.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/salida'/>" ><s:message code="salida.lista.label" /></a></li>
                <li class="active"><a href="<s:url value='/inventario/producto'/>" ><s:message code="producto.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/tipoProducto'/>" ><s:message code="tipoProducto.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/almacen'/>" ><s:message code="almacen.lista.label" /></a></li>
            </ul>
        </nav>

        <div id="nuevo-producto" class="content scaffold-list" role="main">
            <h1><s:message code="producto.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inventario/producto'/>"><i class="icon-list icon-white"></i> <s:message code='producto.lista.label' /></a>
            </p>
            <form:form commandName="producto" action="crea" method="post">
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
                            <s:bind path="producto.codigo">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="codigo">
                                        <s:message code="codigo.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="codigo" maxlength="6" required="true" cssClass="span3" />
                                    <form:errors path="codigo" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="producto.sku">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="sku">
                                        <s:message code="sku.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="sku" maxlength="64" required="true" cssClass="span10" />
                                    <form:errors path="sku" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="producto.nombre">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="nombre">
                                        <s:message code="nombre.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="nombre" maxlength="128" required="true" cssClass="span10" />
                                    <form:errors path="nombre" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="producto.descripcion">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="descripcion">
                                        <s:message code="descripcion.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:textarea path="descripcion" maxlength="254" required="true" cssClass="span10" />
                                    <form:errors path="descripcion" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="producto.marca">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="marca">
                                        <s:message code="marca.label" />
                                    </label>
                                    <form:input path="marca" maxlength="64" cssClass="span10" />
                                    <form:errors path="marca" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="producto.modelo">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="modelo">
                                        <s:message code="modelo.label" />
                                    </label>
                                    <form:input path="modelo" maxlength="64" cssClass="span10" />
                                    <form:errors path="modelo" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="producto.unidadMedida">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="unidadMedida">
                                        <s:message code="unidadMedida.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="unidadMedida" maxlength="32" required="true" cssClass="span5" />
                                    <form:errors path="unidadMedida" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="producto.ubicacion">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="ubicacion">
                                        <s:message code="ubicacion.label" />
                                    </label>
                                    <form:input path="ubicacion" maxlength="128" cssClass="span10" />
                                    <form:errors path="ubicacion" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="producto.fraccion">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="fraccion">
                                        <s:message code="fraccion.label" />
                                    </label>
                                    <form:checkbox path="fraccion" cssClass="span3" />
                                    <form:errors path="fraccion" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="producto.tiempoEntrega">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="tiempoEntrega">
                                        <s:message code="tiempoEntrega.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="tiempoEntrega" maxlength="6" cssClass="span10" required="true" cssStyle="text-align:right;" type="number" step="1" min="1" max="30" />
                                    <form:errors path="tiempoEntrega" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                    </div>
                    <div class="row-fluid">
                        <div class="span4">
                            <s:bind path="producto.iva">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="iva">
                                        <s:message code="iva.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:input path="iva" maxlength="5" cssClass="span10" required="true" cssStyle="text-align:right;" type="number" step="0.01" min="0" max="1" />
                                    <form:errors path="iva" cssClass="alert alert-error" />
                                </div>
                            </s:bind>
                        </div>
                        <div class="span4">
                            <s:bind path="producto.tipoProducto">
                                <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                    <label for="tipoProducto">
                                        <s:message code="tipoProducto.label" />
                                        <span class="required-indicator">*</span>
                                    </label>
                                    <form:select id="tipoProductoId" path="tipoProducto.id" items="${tiposDeProducto}" itemLabel="nombre" itemValue="id" cssClass="span10" required="true" />
                                    <form:errors path="tipoProducto" cssClass="alert alert-error" />
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
                    $('input#codigo').focus();
                });
            </script>                    
        </content>
    </body>
</html>
