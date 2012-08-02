<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="activo.dia.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="activo" />
        </jsp:include>

        <h1><s:message code="activo.dia.label" /></h1>
        <hr/>
        <c:if test="${not empty message}">
            <div class="alert alert-block alert-success fade in" role="status">
                <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>
        <form action="<c:url value='/activoFijo/activo/dia'/>" method="post" class="form-search">
        <fieldset>
            <div class="well">
                <label for="anio">
                    <s:message code="anio.label" />
                </label>
                <input type="text" name="anio" id="anio" value="${anio}" />
                <input type="submit" value="<s:message code='buscar.label' />" class="btn"/>
            </div>
        </fieldset>
        </form>
        <c:if test="${lista != null}">
            <table id="lista" class="table table-striped">
                <thead>
                    <tr>

                        <th><s:message code="cuenta.label" /></th>

                        <th><s:message code="nombre.label" /></th>

                        <th style="text-align:right;"><s:message code="costo.label" /></th>

                        <th style="text-align:right;"><s:message code="compras.label" /></th>

                        <th style="text-align:right;"><s:message code="bajas.label" /></th>

                        <th style="text-align:right;"><s:message code="costoFinal.label" /></th>

                        <th style="text-align:right;"><s:message code="depreciacionAcumulada.label" /></th>

                        <th style="text-align:right;"><s:message code="depreciacion.label" /></th>

                        <th style="text-align:right;"><s:message code="retiros.label" /></th>

                        <th style="text-align:right;"><s:message code="depreciacionFinal.label" /></th>

                        <th style="text-align:right;"><s:message code="valorNeto.label" /></th>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${lista}" var="tipoActivo" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td>${tipoActivo.cuenta}</td>
                            <td>${tipoActivo.nombre}</td>
                            <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoActivo.costo}" /></td>
                            <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoActivo.compras}" /></td>
                            <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoActivo.bajas}" /></td>
                            <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoActivo.costoFinal}" /></td>
                            <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoActivo.depreciacionAcumulada}" /></td>
                            <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoActivo.comprasAcumuladas}" /></td>
                            <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoActivo.bajasAcumuladas}" /></td>
                            <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoActivo.depreciacionFinal}" /></td>
                            <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoActivo.valorNeto}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <th colspan="2"><s:message code="total.label" /></th>
                        <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${totalCosto}" /></th>
                        <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${totalCompras}" /></th>
                        <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${totalBajas}" /></th>
                        <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${costoFinal}" /></th>
                        <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${totalDepreciacionAcumulada}" /></th>
                        <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${totalComprasAcumuladas}" /></th>
                        <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${totalBajasAcumuladas}" /></th>
                        <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${totalDepreciacionFinal}" /></th>
                        <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${valorNeto}" /></th>
                    </tr>
                </tfoot>
            </table>
        </c:if>
        <content>
            <script src="<c:url value='/js/lista.js' />"></script>
        </content>
    </body>
</html>
