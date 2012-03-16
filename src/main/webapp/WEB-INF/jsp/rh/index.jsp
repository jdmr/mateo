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
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>              
                <li class="active"><a href="<c:url value='/rh' />"><s:message code="rh.label" /></a></li>
                <li><a href="<s:url value='/rh/empleado'/>" ><s:message code="empleado.label" /></a></li>             
            </ul>
        </nav>
        <h1><s:message code="rh.label" /></h1>
        
         <c:if test="${not empty message}">
            <div class="alert alert-block alert-success fade in" role="status">
                <a class="close" data-dismiss="alert">×</a>
                <s:message code="${message}" arguments="${messageAttrs}" />
            </div>
        </c:if>

    </body>
</html>
