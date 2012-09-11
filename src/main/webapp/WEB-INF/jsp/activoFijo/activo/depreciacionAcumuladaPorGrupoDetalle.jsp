<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="depreciacionAcumuladaPorGrupoDetalle.label" arguments="${tipoActivoId},${fecha}" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="principal" />
        </jsp:include>

        <h1><s:message code="depreciacionAcumuladaPorGrupoDetalle.label" arguments="${tipoActivo.nombre},${fecha}" /></h1>
        <hr/>
        <table id="lista" class="table table-striped table-hover">
            <thead>
                <tr>
                    <th><s:message code="folio.label" /></th>
                    <th><s:message code="codigo.label" /></th>
                    <th><s:message code="descripcion.label" /></th>
                    <th><s:message code="serial.label" /></th>                        
                    <th><s:message code="tipoActivo.label" /></th>
                    <th><s:message code="fechaCompra.label" /></th>
                    <th><s:message code="moi.label" /></th>
                    <th><s:message code="ubicacion.label" /></th>
                    <th><s:message code="depreciacionAnual.label" /></th>
                    <th><s:message code="depreciacionMensual.label" /></th>
                    <th><s:message code="depreciacionAcumulada.label" /></th>
                    <th><s:message code="valorNeto.label" /></th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${activos}" var="activo" varStatus="status">
                    <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                        <td><a href="<c:url value='/activoFijo/activo/ver/${activo.id}' />">${activo.folio}</a></td>
                        <td>${activo.codigo}</td>
                        <td>${activo.descripcion}</td>
                        <td>${activo.serial}</td>
                        <td>${activo.tipoActivoNombre}</td>
                        <td><fmt:formatDate pattern="dd/MMM/yyyy" value="${activo.fechaCompra}" /></td>
                        <td style="text-align:right;"><fmt:formatNumber value="${activo.moi}" type="currency" currencySymbol="$" /></td>
                        <td>${activo.ubicacion}</td>
                        <td style="text-align:right;"><fmt:formatNumber value="${activo.depreciacionAnual}" type="currency" currencySymbol="$" /></td>
                        <td style="text-align:right;"><fmt:formatNumber value="${activo.depreciacionMensual}" type="currency" currencySymbol="$" /></td>
                        <td style="text-align:right;"><fmt:formatNumber value="${activo.depreciacionAcumulada}" type="currency" currencySymbol="$" /></td>
                        <td style="text-align:right;"><fmt:formatNumber value="${activo.valorNeto}" type="currency" currencySymbol="$" /></td>
                    </tr>
                </c:forEach>
            </tbody>
            <tfoot>
                <tr>
                    <th colspan="8">&nbsp;</th>
                    <th style="text-align:right;"><fmt:formatNumber value="${totales.ANUAL}" type="currency" currencySymbol="$" /></th>
                    <th style="text-align:right;"><fmt:formatNumber value="${totales.MENSUAL}" type="currency" currencySymbol="$" /></th>
                    <th style="text-align:right;"><fmt:formatNumber value="${totales.ACUMULADA}" type="currency" currencySymbol="$" /></th>
                    <th style="text-align:right;"><fmt:formatNumber value="${totales.NETO}" type="currency" currencySymbol="$" /></th>
                </tr>
            </tfoot>
        </table>
        <content>
            <script src="<c:url value='/js/lista.js' />"></script>
        </content>
    </body>
</html>
