<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="contabilidad.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li class="active"><a href="<c:url value='/contabilidad' />"><s:message code="contabilidad.label" /></a></li>
                <li><a href="<s:url value='/contabilidad/mayor'/>" ><s:message code="cuentaMayor.label" /></a></li>
                <li><a href="<s:url value='/contabilidad/resultado'/>" ><s:message code="cuentaResultado.label" /></a></li>
            </ul>
        </nav>
        <jsp:include page="menu.jsp" >
            <jsp:param name="menu" value="principal" />
        </jsp:include>
        <h1><s:message code="contabilidad.label" /></h1>
        <c:if test="${not empty message}">
            <div class="alert alert-block alert-success fade in" role="status">
                <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>

    </body>as
</html>
