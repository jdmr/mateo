<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@include file="../../idioma.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="concentrado.depreciacionPorCentroDeCosto.label" arguments="${fecha}" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="principal" />
        </jsp:include>

        <h1><s:message code="concentrado.depreciacionPorCentroDeCosto.label" arguments="${fecha}" /></h1>
        <hr/>
        <c:if test="${not empty message}">
            <div class="alert alert-block alert-success fade in" role="status">
                <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>
        <form action="<c:url value='/activoFijo/activo/concentrado/depreciacionPorCentroDeCosto'/>" method="post" class="form-search">
            <input type="hidden" name="hojaCalculo" id="hojaCalculo" value="0" />
            <fieldset>
                <div class="well">
                    <label for="fecha">
                        <s:message code="fecha.label" />
                    </label>
                    <input type="text" name="fecha" id="fecha" value="${fecha}" />
                    <button type="submit" class="btn" name="submitBtn" id="submitBtn"><s:message code='buscar.label' /></button>
                    <button type="submit" class="btn btn-success" name="hojaCalculoBtn" id="hojaCalculoBtn" ><s:message code='excel.label' /></button>
                </div>
            </fieldset>
        </form>
        <c:if test="${tiposDeActivo != null}">
            <table id="lista" class="table table-striped table-bordered table-hover">
                <thead>
                    <tr>

                        <th><s:message code="cuenta.label" /></th>
                        <th><s:message code="centroCosto.label" /></th>

                        <c:forEach items="${tiposDeActivo}" var="tipoDeActivo">
                            <th colspan="6">${tipoDeActivo.nombre} | ${tipoDeActivo.cuenta}</th>
                        </c:forEach>

                    </tr>
                    <tr>

                        <th></th>
                        <th></th>

                        <c:forEach items="${tiposDeActivo}" var="tipoDeActivo">
                            <th style="text-align:right;"><s:message code="costo.label" /></th>
                            <th style="text-align:right;"><s:message code="depreciacionAnio.label" /></th>
                            <th style="text-align:right;"><s:message code="depreciacionAnual.label" /></th>
                            <th style="text-align:right;"><s:message code="depreciacionMensual.label" /></th>
                            <th style="text-align:right;"><s:message code="depreciacionAcumulada.label" /></th>
                            <th style="text-align:right;"><s:message code="valorNeto.label" /></th>
                        </c:forEach>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${centrosDeCosto}" var="centroDeCosto">
                        <tr>
                            <td>${centroDeCosto.cuenta}</td>
                            <td><a href="<c:url value='/activoFijo/activo/depreciacionAcumuladaPorCentroDeCosto/${centroDeCosto.cuenta}/${fechaParam}' />">${centroDeCosto.nombre}</a></td>
                            <c:forEach items="${tiposDeActivo}" var="tipoDeActivo">
                                <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${centroDeCosto.totales[tipoDeActivo.cuenta]['costo']}" /></td>
                                <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="0" /></td>
                                <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${centroDeCosto.totales[tipoDeActivo.cuenta]['anual']}" /></td>
                                <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${centroDeCosto.totales[tipoDeActivo.cuenta]['mensual']}" /></td>
                                <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${centroDeCosto.totales[tipoDeActivo.cuenta]['acumulada']}" /></td>
                                <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${centroDeCosto.totales[tipoDeActivo.cuenta]['valorNeto']}" /></td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <th colspan="2"><s:message code="total.label" /></th>
                        <c:forEach items="${tiposDeActivo}" var="tipoDeActivo">
                            <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoDeActivo.costo}" /></th>
                            <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="0" /></th>
                            <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoDeActivo.anual}" /></th>
                            <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoDeActivo.mensual}" /></th>
                            <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoDeActivo.acumulada}" /></th>
                            <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoDeActivo.valorNeto}" /></th>
                        </c:forEach>
                    </tr>
                    <tr>

                        <th colspan="2">&nbsp;</th>

                        <c:forEach items="${tiposDeActivo}" var="tipoDeActivo">
                            <th style="text-align:right;"><s:message code="costo.label" /></th>
                            <th style="text-align:right;"><s:message code="depreciacionAnio.label" /></th>
                            <th style="text-align:right;"><s:message code="depreciacionAnual.label" /></th>
                            <th style="text-align:right;"><s:message code="depreciacionMensual.label" /></th>
                            <th style="text-align:right;"><s:message code="depreciacionAcumulada.label" /></th>
                            <th style="text-align:right;"><s:message code="valorNeto.label" /></th>
                        </c:forEach>

                    </tr>
                    <tr>

                        <th colspan="2"><s:message code="centroCosto.label" /></th>

                        <c:forEach items="${tiposDeActivo}" var="tipoDeActivo">
                            <th colspan="6">${tipoDeActivo.nombre} | ${tipoDeActivo.cuenta}</th>
                        </c:forEach>

                    </tr>
                </tfoot>
            </table>
        </c:if>
        <content>
            <script src="<c:url value='/js/lista.js' />"></script>
            <script>
                $(document).ready(function() {
                    $("input#fecha").datepicker();
                    $("button#hojaCalculoBtn").click(function() {
                        $("input#hojaCalculo").val("1");
                        return true;
                    });
                    $("button#submitBtn").click(function() {
                        $("input#hojaCalculo").val("0");
                        return true;
                    });
                });
            </script>
        </content>
    </body>
</html>
