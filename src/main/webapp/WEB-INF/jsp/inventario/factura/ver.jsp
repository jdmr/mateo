<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="facturaAlmacen.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="factura" />
        </jsp:include>

        <div id="ver-factura" class="content scaffold-list" role="main">
            <h1><s:message code="facturaAlmacen.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inventario/factura'/>"><i class="icon-list icon-white"></i> <s:message code='facturaAlmacen.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/inventario/factura/nueva'/>"><i class="icon-shopping-cart icon-white"></i> <s:message code='facturaAlmacen.nueva.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <div class="row-fluid" style="margin-bottom: 10px;">
                <div class="span4">
                    <h4><s:message code="folio.label" /></h4>
                    <h3>${factura.folio}</h3>
                </div>
                <div class="span4">
                    <h4><s:message code="estatus.label" /></h4>
                    <h2>${factura.estatus.nombre}</h2>
                </div>
            </div>
            <c:if test="${not empty factura.comentarios}">
                <div class="row-fluid" style="margin-bottom: 10px;">
                    <div class="span8">
                        <h4><s:message code="comentarios.label" /></h4>
                        <h3>${factura.comentarios}</h3>
                    </div>
                </div>
            </c:if>
            <div class="row-fluid" style="margin-bottom: 10px;">
                <div class="span4">
                    <h4><s:message code="iva.label" /></h4>
                    <h3>${factura.iva}</h3>
                </div>
                <div class="span4">
                    <h4><s:message code="total.label" /></h4>
                    <h3>${factura.total}</h3>
                </div>
            </div>
            <div class="row-fluid" style="margin-bottom: 10px;">
                <div class="span4">
                    <h4><s:message code="cliente.label" /></h4>
                    <h3>${factura.cliente.nombre}</h3>
                </div>
                <div class="span4">
                    <h4><s:message code="almacen.label" /></h4>
                    <h3>${factura.almacen.nombre}</h3>
                </div>
            </div>
            <div class="row-fluid">
                <div class="span12">
                    <h2><s:message code="salida.lista.label" /></h2>
                    <c:if test="${puedeEditar}">
                        <form action="<c:url value='/inventario/factura/salida/nueva' />" method="post" class="form-search">
                            <div class="well">
                                <input type="hidden" id="id" name="id" value="${factura.id}" />
                                <input type="hidden" id="salidaId" name="salidaId" value="" />
                                <input id="nuevaSalida" name="nuevaSalida" type="text" class="input-large search-query" value="" />
                                <button id="nuevaSalidaBtn" name="nuevaSalidaBtn" type="submit" class="btn btn-primary"><i class="icon-shopping-cart icon-white"></i> <s:message code="salida.nueva.label" /></button>
                            </div>
                        </form>
                    </c:if>
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th><s:message code="folio.label" /></th>
                                <th><s:message code="fecha.label" /></th>
                                <th style="text-align: right;"><s:message code="iva.label" /></th>
                                <th style="text-align: right;"><s:message code="subtotal.label" /></th>
                                <c:if test="${puedeEditar}">
                                    <th><s:message code="acciones.label" /></th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${factura.salidas}" var="salida" varStatus="status">
                                <tr>
                                    <td>${salida.folio}</td>
                                    <td>${salida.fechaCreacion}</td>
                                    <td style="text-align: right;">${salida.iva}</td>
                                    <td style="text-align: right;">${salida.subtotal}</td>
                                    <c:if test="${puedeEditar}">
                                        <td>
                                            <a href="<c:url value='/inventario/factura/salida/elimina/${factura.id}/${salida.id}' />" class="btn btn-mini btn-danger"><i class="icon-remove icon-white"></i></a>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot>
                            <tr>
                                <th></th>
                                <th></th>
                                <th style="text-align: right;"><s:message code="subtotal.label" /></th>
                                <th style="text-align: right;">${salidasSubtotal}</th>
                                <th></th>
                            </tr>
                            <tr>
                                <th></th>
                                <th></th>
                                <th style="text-align: right;"><s:message code="iva.label" /></th>
                                <th style="text-align: right;">${salidasIva}</th>
                                <th></th>
                            </tr>
                            <tr>
                                <th></th>
                                <th></th>
                                <th style="text-align: right;"><s:message code="total.label" /></th>
                                <th style="text-align: right;">${salidasTotal}</th>
                                <th></th>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>

            <div class="row-fluid">
                <div class="span12">
                    <h2><s:message code="entrada.lista.label" /></h2>
                    <c:if test="${puedeEditar}">
                        <form action="<c:url value='/inventario/factura/entrada/nueva' />" method="post" class="form-search">
                            <div class="well">
                                <input type="hidden" id="id" name="id" value="${factura.id}" />
                                <input type="hidden" id="entradaId" name="entradaId" value="" />
                                <input id="nuevaEntrada" name="nuevaEntrada" type="text" class="input-large search-query" value="" />
                                <button id="nuevaEntradaBtn" name="nuevaEntradaBtn" type="submit" class="btn btn-primary"><i class="icon-shopping-cart icon-white"></i> <s:message code="entrada.nueva.label" /></button>
                            </div>
                        </form>
                    </c:if>
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th><s:message code="folio.label" /></th>
                                <th><s:message code="fechaFactura.label" /></th>
                                <th style="text-align: right;"><s:message code="iva.label" /></th>
                                <th style="text-align: right;"><s:message code="subtotal.label" /></th>
                                <c:if test="${puedeEditar}">
                                    <th><s:message code="acciones.label" /></th>
                                </c:if>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${factura.entradas}" var="entrada" varStatus="status">
                                <tr>
                                    <td>${entrada.folio}</td>
                                    <td>${entrada.fechaFactura}</td>
                                    <td style="text-align: right;">${entrada.iva}</td>
                                    <td style="text-align: right;">${entrada.subtotal}</td>
                                    <c:if test="${puedeEditar}">
                                        <td>
                                            <a href="<c:url value='/inventario/factura/entrada/elimina/${factura.id}/${entrada.id}' />" class="btn btn-mini btn-danger"><i class="icon-remove icon-white"></i></a>
                                        </td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot>
                            <tr>
                                <th></th>
                                <th></th>
                                <th style="text-align: right;"><s:message code="subtotal.label" /></th>
                                <th style="text-align: right;">${entradasSubtotal}</th>
                                <th></th>
                            </tr>
                            <tr>
                                <th></th>
                                <th></th>
                                <th style="text-align: right;"><s:message code="iva.label" /></th>
                                <th style="text-align: right;">${entradasIva}</th>
                                <th></th>
                            </tr>
                            <tr>
                                <th></th>
                                <th></th>
                                <th style="text-align: right;"><s:message code="total.label" /></th>
                                <th style="text-align: right;">${entradasTotal}</th>
                                <th></th>
                            </tr>
                        </tfoot>
                    </table>
                </div>
            </div>

            <c:if test="${puedeEditar || puedeCerrar || puedePendiente || puedeEliminar || puedeEditarPendiente || puedeCancelar || puedeReporte}">
                <c:url var="eliminaUrl" value="/inventario/factura/elimina" />
                <form:form commandName="factura" action="${eliminaUrl}" cssClass="form-inline form-horizontal" >
                    <p class="well">
                        <c:if test="${puedeEditar}">
                            <a href="<c:url value='/inventario/factura/edita/${factura.id}' />" class="btn btn-large"><i class="icon-edit"></i> <s:message code="editar.button" /></a>
                        </c:if>
                        <c:if test="${puedeCerrar}">
                            <a href="<c:url value='/inventario/factura/cerrar/${factura.id}' />" class="btn btn-warning btn-large" onclick="return confirm('<s:message code="confirma.cerrar2.message" />');"><i class="icon-lock icon-white"></i> <s:message code="cerrar.button" /></a>
                        </c:if>
                        <c:if test="${puedeEliminar}">
                            <form:hidden path="id" />
                            <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina2.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                        </c:if>
                        <c:if test="${puedeReporte}">
                            <a href="<c:url value='/reporte/factura/almacen/${factura.id}' />" class="btn btn-primary btn-large" ><i class="icon-print icon-white"></i> <s:message code="reporte.button" /></a>
                        </c:if>
                        <c:if test="${puedeCancelar}">
                            <a href="<c:url value='/inventario/factura/cancela/${factura.id}' />" class="btn btn-warning btn-large" onclick="return confirm('<s:message code="confirma.cancela2.message" />');" ><i class="icon-exclamation-sign icon-white"></i> <s:message code="cancelar.button" /></a>
                        </c:if>
                    </p>
                </form:form>
            </c:if>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#nuevaSalida')
                        .autocomplete({
                            source: function(request, response) {
                                $.getJSON("<c:url value='/inventario/factura/buscaSalida' />", {term:request.term, facturaId: ${factura.id}}, response);
                            },
                            select: function(event, ui) {
                                $("input#salidaId").val(ui.item.id);
                                $("input#nuevaSalida").val(ui.item.nombre);
                                $("#nuevaSalidaBtn").focus();
                                return false;
                            }
                        });
                        
                    $('input#nuevaEntrada')
                        .autocomplete({
                            source: function(request, response) {
                                $.getJSON("<c:url value='/inventario/factura/buscaEntrada' />", {term:request.term, facturaId: ${factura.id}}, response);
                            },
                            select: function(event, ui) {
                                $("input#entradaId").val(ui.item.id);
                                $("input#nuevaEntrada").val(ui.item.nombre);
                                $("#nuevaEntradaBtn").focus();
                                return false;
                            }
                        });
                });
            </script>                    
        </content>
    </body>
</html>
