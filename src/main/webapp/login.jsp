<%-- 
    Document   : login
    Created on : Jan 24, 2012, 6:59:00 PM
    Author     : jdmr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s"    uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c"    uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="login.title" /></title>
    </head>
    <body>
        <h2><s:message code="login.title" /></h2>
        <c:if test="${not empty param.error}">
            <p style="color:red;padding: 0 10px 10px;">
                <s:message code="login.invalido" />
            </p>
            <p style="color:red;padding: 0 10px 10px;">
                <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
            </p>
        </c:if>
        <img src="<c:url value='/images/google_logo2.jpg'/>" />
        <form action="j_spring_openid_security_check" id="googleOpenId" method="post" target="_top">
            <input id="openid_identifier" name="openid_identifier" type="hidden" value="https://www.google.com/accounts/o8/id"/>
            <input type="submit" value="<s:message code='google.sign.in' />" class="btn btn-large btn-primary" style="width:170px;"/>
        </form>

    </body>
</html>
