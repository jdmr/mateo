<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="salida.ver.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/inventario' />"><s:message code="inventario.label" /></a></li>
                <li class="active"><a href="<s:url value='/inventario/salida'/>" ><s:message code="salida.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/entrada'/>" ><s:message code="entrada.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/producto'/>" ><s:message code="producto.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/tipoProducto'/>" ><s:message code="tipoProducto.lista.label" /></a></li>
                <li><a href="<s:url value='/inventario/almacen'/>" ><s:message code="almacen.lista.label" /></a></li>
            </ul>
        </nav>

        <div id="ver-salida" class="content scaffold-list" role="main">
            <h1><s:message code="salida.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inventario/salida'/>"><i class="icon-list icon-white"></i> <s:message code='salida.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/inventario/salida/nueva'/>"><i class="icon-shopping-cart icon-white"></i> <s:message code='salida.nueva.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/inventario/salida/elimina" />
            <form:form commandName="salida" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="margin-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="folio.label" /></h4>
                        <h3>${salida.folio}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="estatus.label" /></h4>
                        <h2>${salida.estatus.nombre}</h2>
                    </div>
                </div>
                <div class="row-fluid" style="margin-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="reporte.label" /></h4>
                        <h3>${salida.reporte}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="atendio.label" /></h4>
                        <h3>${salida.atendio}</h3>
                    </div>
                </div>
                <c:if test="${not empty salida.empleado || not empty salida.departamento}">
                    <div class="row-fluid" style="margin-bottom: 10px;">
                        <div class="span4">
                            <h4><s:message code="empleado.label" /></h4>
                            <h3>${salida.empleado}</h3>
                        </div>
                        <div class="span4">
                            <h4><s:message code="departamento.label" /></h4>
                            <h3>${salida.departamento}</h3>
                        </div>
                    </div>
                </c:if>
                <c:if test="${not empty salida.comentarios}">
                    <div class="row-fluid" style="margin-bottom: 10px;">
                        <div class="span8">
                            <h4><s:message code="comentarios.label" /></h4>
                            <h3>${salida.comentarios}</h3>
                        </div>
                    </div>
                </c:if>
                <div class="row-fluid" style="margin-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="iva.label" /></h4>
                        <h3>${salida.iva}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="total.label" /></h4>
                        <h3>${salida.total}</h3>
                    </div>
                </div>
                <div class="row-fluid" style="margin-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="cliente.label" /></h4>
                        <h3>${salida.cliente.nombre}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="almacen.label" /></h4>
                        <h3>${salida.almacen.nombre}</h3>
                    </div>
                </div>
                <div class="row-fluid">
                    <div class="span12">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th><s:message code="producto.label" /></th>
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
                                <c:forEach items="${salida.lotes}" var="lote" varStatus="status">
                                    <tr>
                                        <td>${lote.producto.nombre}</td>
                                        <td style="text-align: right;">${lote.cantidad}</td>
                                        <td style="text-align: right;">${lote.precioUnitario}</td>
                                        <td style="text-align: right;">${lote.iva}</td>
                                        <td style="text-align: right;">${lote.total}</td>
                                        <c:if test="${puedeEditar}">
                                            <td>
                                                <a href="<c:url value='/inventario/salida/lote/elimina/${lote.id}' />" class="btn btn-mini btn-danger"><i class="icon-remove icon-white"></i></a>
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
                                    <th style="text-align: right;"><s:message code="subtotal.label" /></th>
                                    <th style="text-align: right;"><span class="${estiloTotales}">${subtotal}</span></th>
                                    <c:if test="${puedeEditar}">
                                        <th>${salida.subtotal}</th>
                                    </c:if>
                                </tr>
                                <tr>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th style="text-align: right;"><s:message code="iva.label" /></th>
                                    <th style="text-align: right;"><span class="${estiloTotales}">${iva}</span></th>
                                    <c:if test="${puedeEditar}">
                                        <th>${salida.iva}</th>
                                    </c:if>
                                </tr>
                                <tr>
                                    <th></th>
                                    <th></th>
                                    <th></th>
                                    <th style="text-align: right;"><s:message code="total.label" /></th>
                                    <th style="text-align: right;"><span class="${estiloTotales}">${total}</span></th>
                                    <c:if test="${puedeEditar}">
                                        <th>${salida.total}</th>
                                    </c:if>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>

                <c:if test="${puedeEditar || puedeCerrar || puedePendiente || puedeEliminar || puedeEditarPendiente || puedeCancelar}">
                    <p class="well">
                        <c:if test="${puedeEditar}">
                            <a href="<c:url value='/inventario/salida/edita/${salida.id}' />" class="btn btn-large"><i class="icon-edit"></i> <s:message code="editar.button" /></a>
                            <a href="<c:url value='/inventario/salida/lote/${salida.id}' />" class="btn btn-primary btn-large"><i class="icon-shopping-cart icon-white"></i> <s:message code="lote.nuevo.button" /></a>
                        </c:if>
                        <c:if test="${puedeCerrar}">
                            <a href="<c:url value='/inventario/salida/cerrar/${salida.id}' />" class="btn btn-warning btn-large" onclick="return confirm('<s:message code="confirma.cerrar2.message" />');"><i class="icon-lock icon-white"></i> <s:message code="cerrar.button" /></a>
                        </c:if>
                        <c:if test="${puedeEliminar}">
                            <form:hidden path="id" />
                            <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina2.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                        </c:if>
                        <c:if test="${puedeCancelar}">
                            <a href="<c:url value='/inventario/salida/cancela/${salida.id}' />" class="btn btn-warning btn-large" ><i class="icon-exclamation-sign icon-white"></i> <s:message code="cancelar.button" /></a>
                        </c:if>
                    </p>
                </c:if>
            </form:form>
        </div>
    </body>
</html>
