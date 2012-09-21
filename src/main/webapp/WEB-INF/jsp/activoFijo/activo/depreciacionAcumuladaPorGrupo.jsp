<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="depreciacionAcumuladaPorGrupo.label" arguments="${fecha}" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="principal" />
        </jsp:include>

        <h1><s:message code="depreciacionAcumuladaPorGrupo.label" arguments="${fecha}" /></h1>
        <hr/>
        <c:if test="${not empty message}">
            <div class="alert alert-block alert-success fade in" role="status">
                <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>
        <form action="<c:url value='/activoFijo/activo/depreciacionAcumuladaPorGrupo'/>" method="post" class="form-search">
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

                        <th><s:message code="tipoActivo.label" /></th>

                        <th style="text-align:right;"><s:message code="acumulada.label" /></th>
                        
                        <th style="text-align:right;"><s:message code="mensual.label" /></th>

                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${tiposDeActivo}" var="tipoActivo" varStatus="status">
                        <tr class="${status.index % 2 == 0 ? 'even' : 'odd'}">
                            <td><a href="<c:url value='/activoFijo/activo/depreciacionAcumuladaPorGrupo/${tipoActivo.cuenta}/${fechaParam}' />">${tipoActivo.cuenta} | ${tipoActivo.nombre}</a></td>
                            <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoActivo.ACUMULADA}" /></td>
                            <td style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${tipoActivo.MENSUAL}" /></td>
                        </tr>
                    </c:forEach>
                </tbody>
                <tfoot>
                    <tr>
                        <th>&nbsp;</th>
                        <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${totales.ACUMULADA}" /></th>
                        <th style="text-align:right;"><fmt:formatNumber type="currency" currencySymbol="$" value="${totales.MENSUAL}" /></th>
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
