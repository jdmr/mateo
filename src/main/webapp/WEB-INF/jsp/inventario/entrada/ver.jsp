<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@include file="../../idioma.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="entrada.ver.label" /></title>
        <link rel="stylesheet" href="<c:url value='/css/jquery.lightbox.min.css' />" type="text/css">
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="entrada" />
        </jsp:include>

        <div id="ver-entrada" class="content scaffold-list" role="main">
            <h1><s:message code="entrada.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inventario/entrada'/>"><i class="icon-list icon-white"></i> <s:message code='entrada.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/inventario/entrada/nueva'/>"><i class="icon-shopping-cart icon-white"></i> <s:message code='entrada.nueva.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/inventario/entrada/elimina" />
            <form:form commandName="entrada" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="folio.label" /></h4>
                        <h3>${entrada.folio}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="estatus.label" /></h4>
                        <h2>${entrada.estatus.nombre}</h2>
                    </div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="factura.label" /></h4>
                        <h3>${entrada.factura}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="fechaFactura.label" /></h4>
                        <h3>${entrada.fechaFactura}</h3>
                    </div>
                </div>
                <c:if test="${not empty entrada.comentarios}">
                    <div class="row-fluid" style="margin-bottom: 10px;">
                        <div class="span8">
                            <h4><s:message code="comentarios.label" /></h4>
                            <h3>${entrada.comentarios}</h3>
                        </div>
                    </div>
                </c:if>
                <c:if test="${entrada.devolucion || entrada.tipoCambio != null}">
                    <div class="row-fluid" style="padding-bottom: 10px;">
                        <div class="span4">
                            <h4><s:message code="devolucion.label" /></h4>
                            <h3><form:checkbox path="devolucion" disabled="true" /></h3>
                        </div>
                        <div class="span4">
                            <h4><s:message code="tipoCambio.label" /></h4>
                            <h3>${entrada.tipoCambio}</h3>
                        </div>
                    </div>
                </c:if>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="iva.label" /></h4>
                        <h3>${entrada.iva}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="total.label" /></h4>
                        <h3>${entrada.total}</h3>
                    </div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="proveedor.label" /></h4>
                        <h3>${entrada.proveedor.nombre}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="almacen.label" /></h4>
                        <h3>${entrada.almacen.nombre}</h3>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span12">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th><s:message code="sku.label" /></th>
                                    <th><s:message code="producto.label" /></th>
                                    <th><s:message code="descripcion.label" /></th>
                                    <th style="text-align: right;"><s:message code="existencia.label" /></th>
                                    <th style="text-align: center;"><s:message code="imagen.label" /></th>
                                    <th style="text-align: right;"><s:message code="cantidad.label" /></th>
                                    <th style="text-align: right;"><s:message code="precioUnitario.label" /></th>
                                    <th style="text-align: right;"><s:message code="iva.label" /></th>
                                    <th style="text-align: right;"><s:message code="total.label" /></th>
                                    <c:if test="${puedeEditar}">
                                        <th><s:message code="acciones.label" /></th>
                                    </c:if>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${entrada.lotes}" var="lote" varStatus="status">
                                    <tr>
                                        <td style="vertical-align: middle;">${lote.producto.sku}</td>
                                        <td style="vertical-align: middle;">${lote.producto.nombre}</td>
                                        <td style="vertical-align: middle;">${lote.producto.descripcion}</td>
                                        <td style="text-align: right; vertical-align: middle;">
                                            <c:choose>
                                                <c:when test="${!lote.producto.fraccion}">
                                                    <fmt:formatNumber value="${lote.producto.existencia}" minFractionDigits="0" maxFractionDigits="0" groupingUsed="true" />
                                                </c:when>
                                                <c:otherwise>
                                                    ${lote.producto.existencia} 
                                                </c:otherwise>
                                            </c:choose>
                                            &nbsp;${lote.producto.unidadMedida}
                                        </td>
                                        <c:url var="imagenUrl" value='/imagen/producto/${lote.producto.id}' />
                                        <td style="text-align: center; vertical-align: middle;"><a class="lightbox" href="${imagenUrl}"><img src="${imagenUrl}" style="height: 50px;" /></a></td>
                                        <td style="text-align: right; vertical-align: middle; font-size: 1.2em; font-weight: bold;">
                                            <c:choose>
                                                <c:when test="${!lote.producto.fraccion}">
                                                    <fmt:formatNumber value="${lote.cantidad}" minFractionDigits="0" maxFractionDigits="0" groupingUsed="true" />
                                                </c:when>
                                                <c:otherwise>
                                                    ${lote.cantidad} 
                                                </c:otherwise>
                                            </c:choose>
                                            &nbsp;${lote.producto.unidadMedida}
                                        </td>
                                        <td style="text-align: right; vertical-align: middle; font-size: 1.2em;"><fmt:formatNumber value="${lote.precioUnitario}" type="currency" currencySymbol="$" /></td>
                                        <td style="text-align: right; vertical-align: middle; font-size: 1.2em;"><fmt:formatNumber value="${lote.iva}" type="currency" currencySymbol="$" /></td>
                                        <td style="text-align: right; vertical-align: middle; font-size: 1.2em; font-weight: bold;"><fmt:formatNumber value="${lote.total}" type="currency" currencySymbol="$" /></td>
                                        <c:if test="${puedeEditar}">
                                            <td style="vertical-align: middle;">
                                                <a href="<c:url value='/inventario/entrada/lote/elimina/${lote.id}' />" class="btn btn-mini btn-danger"><i class="icon-remove icon-white"></i></a>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot>
                                <tr>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th style="text-align: right;"><s:message code="subtotal.label" /></th>
                                    <th style="text-align: right;"><span class="${estiloTotales}"><fmt:formatNumber value="${subtotal}" type="currency" currencySymbol="$" /></span></th>
                                    <c:if test="${puedeEditar}">
                                        <th><fmt:formatNumber value="${entrada.subtotal}" type="currency" currencySymbol="$" /></th>
                                    </c:if>
                                </tr>
                                <tr>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th style="text-align: right;"><s:message code="iva.label" /></th>
                                    <th style="text-align: right;"><span class="${estiloTotales}"><fmt:formatNumber value="${iva}" type="currency" currencySymbol="$" /></span></th>
                                    <c:if test="${puedeEditar}">
                                        <th><fmt:formatNumber value="${entrada.iva}" type="currency" currencySymbol="$" /></th>
                                    </c:if>
                                </tr>
                                <tr>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th style="text-align: right;"><s:message code="total.label" /></th>
                                    <th style="text-align: right;"><span class="${estiloTotales}" style="font-size: 1.5em; font-weight: bold;"><fmt:formatNumber value="${total}" type="currency" currencySymbol="$" /></span></th>
                                    <c:if test="${puedeEditar}">
                                        <th><fmt:formatNumber value="${entrada.total}" type="currency" currencySymbol="$" /></th>
                                    </c:if>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>

                <c:if test="${puedeEditar || puedeCerrar || puedePendiente || puedeEliminar || puedeEditarPendiente || puedeCancelar}">
                    <p class="well">
                        <c:if test="${puedeEditar}">
                            <a href="<c:url value='/inventario/entrada/edita/${entrada.id}' />" class="btn btn-large"><i class="icon-edit"></i> <s:message code="editar.button" /></a>
                            <a href="<c:url value='/inventario/entrada/lote/${entrada.id}' />" class="btn btn-primary btn-large"><i class="icon-shopping-cart icon-white"></i> <s:message code="lote.nuevo.button" /></a>
                        </c:if>
                        <c:if test="${puedeCerrar}">
                            <a href="<c:url value='/inventario/entrada/cerrar/${entrada.id}' />" class="btn btn-warning btn-large"><i class="icon-lock icon-white"></i> <s:message code="cerrar.button" /></a>
                        </c:if>
                        <c:if test="${puedePendiente}">
                            <a href="<c:url value='/inventario/entrada/pendiente/${entrada.id}' />" class="btn btn-warning btn-large"><i class="icon-asterisk icon-white"></i> <s:message code="pendiente.button" /></a>
                        </c:if>
                        <c:if test="${puedeEditarPendiente}">
                            <a href="<c:url value='/inventario/entrada/pendiente/edita/${entrada.id}' />" class="btn btn-warning btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                        </c:if>
                        <c:if test="${puedeEliminar}">
                            <form:hidden path="id" />
                            <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                        </c:if>
                        <c:if test="${puedeCancelar}">
                            <a href="<c:url value='/inventario/entrada/cancela/${entrada.id}' />" class="btn btn-warning btn-large" ><i class="icon-exclamation-sign icon-white"></i> <s:message code="cancelar.button" /></a>
                        </c:if>
                    </p>
                </c:if>
            </form:form>
        </div>
        <content>
            <script src="<c:url value='/js/jquery.lightbox.min.js' />"></script>
            <script>
                $(document).ready(function() {
                    $("a.lightbox").lightBox({
                        imageLoading:'<c:url value="/images/lightbox-ico-loading.gif" />',
			imageBtnPrev:'<c:url value="/images/lightbox-btn-prev.gif" />',
			imageBtnNext:'<c:url value="/images/lightbox-btn-next.gif" />',
			imageBtnClose:'<c:url value="/images/lightbox-btn-close.gif" />',
			imageBlank:'<c:url value="/images/lightbox-blank.gif" />'
                    });
                });
            </script>
        </content>
    </body>
</html>
