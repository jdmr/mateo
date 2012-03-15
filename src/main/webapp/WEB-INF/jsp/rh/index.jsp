<%-- 
    Document   : index
    Created on : 13-feb-2012, 10:44:12
    Author     : AMDA
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
   <head>
        <title><s:message code="rh.label" /></title>
    </head>
     <body>
        <jsp:include page="menu.jsp" >
            <jsp:param name="menu" value="principal" />
        </jsp:include>
        <h1><s:message code="rh.label" /></h1>

    </body>
</html>
