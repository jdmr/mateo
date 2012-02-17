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
        <h3 data-toggle="collapse" data-target="#demo"><s:message code="login.usuario.password.message" /></h3>
        <div id="demo" class="collapse out">
            <form action='<c:url value="/entrar" />' method='POST' id='loginForm' class='cssform' autocomplete='off'>
                <fieldset>
                    <div class="control-group">
                        <label for='username'><s:message code="login.username" /></label>
                        <input type='text' class='text_' name='j_username' id='username' value="<c:if test="${not empty param.error}"><c:out value="${SPRING_SECURITY_LAST_USERNAME}"/></c:if>"/>
                    </div>
                    <div class="control-group">
                        <label for='password'><s:message code="login.password" /></label>
                        <input type='password' class='text_' name='j_password' id='password'/>
                    </div>
                    <div class="control-group">
                        <label for='remember_me' style="float:none;"><s:message code="login.remember.me" /></label>
                        <input type='checkbox' class='chk' name='_spring_security_remember_me' id='remember_me' />
                    </div>
                </fieldset>
                <p>
                    <input type='submit' id="submit" value='<s:message code="login.entrar" />' class="btn btn-large btn-primary" style="width:170px;"/>
                </p>
            </form>
        </div>
    </body>
</html>
