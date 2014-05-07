<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="inicio.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="principal" />
        </jsp:include>
        <h1><s:message code="inicio.label" /></h1>
        <c:if test="${not empty message}">
            <div class="alert alert-block <c:choose><c:when test='${not empty messageStyle}'>${messageStyle}</c:when><c:otherwise>alert-success</c:otherwise></c:choose> fade in" role="status">
                <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>
        <div class="alert alert-block alert-info fade in" role="status">
            <a class="close" data-dismiss="alert">×</a>
            <h1 class="alert-heading"><a href="http://escuelasabaticauniversitaria.org" alt="Escuela Sab&aacute;tica" target="_blank">
                    Estudio diario de la Escuela Sab&aacute;tica</a>
            </h1>
            <p></p>
        </div>
    </body>
</html>
