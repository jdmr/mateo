<%-- 
    Document   : index
    Created on : 07/MAr/2013, 10:44:12
    Author     : osoto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
   <head>
        <title><s:message code="colportaje.label" /></title>
    </head>
     <body>
        <jsp:include page="menu.jsp" >
            <jsp:param name="menu" value="principal" />
        </jsp:include>

        <h1><s:message code="colportaje.label" /></h1>
        
         <c:if test="${not empty message}">
            <div class="alert alert-block alert-success fade in" role="status">
                <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>

    </body>
</html>
