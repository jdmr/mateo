<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@include file="../../idioma.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="depreciacionMensualPorCentroDeCosto.label" arguments="${fecha}" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="principal" />
        </jsp:include>

        <h1><s:message code="depreciacionMensualPorCentroDeCosto.label" arguments="${fecha}" /></h1>
        <hr/>
        <c:if test="${not empty message}">
            <div class="alert alert-block alert-success fade in" role="status">
                <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>
        <form action="<c:url value='/activoFijo/activo/depreciacionMensualPorCentroDeCosto'/>" method="post" class="form-search">
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
            <table id="lista" class="table table-striped table-hover">
                <thead>
                    <tr>

                        <th><s:message code="centroCosto.label" /></th>

                        <c:forEach items="${tiposDeActivo}" var="tipoDeActivo">
                            <th style="text-align:right;">${tipoDeActivo.nombre} | ${tipoDeActivo.cuenta}</th>
                        </c:forEach>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${centrosDeCosto}" var="centroDeCosto" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/activoFijo/activo/depreciacionMensualPorCentroDeCosto/${centroDeCosto.cuenta}/${fechaParam}' />">${centroDeCosto.nombre}</a></td>
                            <c:forEach items="${tiposDeActivo}" var="tipoDeActivo">
                                <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${centroDeCosto.totales[tipoDeActivo.cuenta]}" /></td>
                            </c:forEach>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <th>&nbsp;</th>
                        <c:forEach items="${tiposDeActivo}" var="tipoDeActivo">
                            <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoDeActivo.total}" /></th>
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
