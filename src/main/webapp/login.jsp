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
        <style type='text/css' media='screen'>
            #login {
                margin: 15px 0px;
                padding: 0px;
                text-align: center;
            }

            #login .inner {
                width: 340px;
                padding-bottom: 6px;
                margin: 60px auto;
                text-align: center;
                border: 1px solid #aab;
                background-color: #ffffff;
                -moz-box-shadow: 2px 2px 2px #eee;
                -webkit-box-shadow: 2px 2px 2px #eee;
                -khtml-box-shadow: 2px 2px 2px #eee;
                box-shadow: 2px 2px 2px #eee;
            }

            #login .inner .fheader {
                padding: 18px 26px 14px 26px;
                background-color: #f7f7ff;
                margin: 0px 0 14px 0;
                color: #2e3741;
                font-size: 18px;
                font-weight: bold;
            }

            #login .inner .cssform p {
                clear: left;
                margin: 0;
                padding: 4px 0 3px 0;
                padding-left: 105px;
                margin-bottom: 20px;
                height: 1%;
            }

            #login .inner .cssform input[type='text'] {
                width: 120px;
            }

            #login .inner .cssform label {
                font-weight: bold;
                float: left;
                text-align: right;
                margin-left: -105px;
                width: 110px;
                padding-top: 3px;
                padding-right: 10px;
            }

            #login #remember_me_holder {
                padding-left: 120px;
            }

            #login #submit {
                margin-left: 15px;
            }

            #login #remember_me_holder label {
                float: none;
                margin-left: 0;
                text-align: left;
                width: 200px
            }

            #login .inner .login_message {
                padding: 6px 25px 20px 25px;
                color: #c33;
            }

            #login .inner .text_ {
                width: 120px;
            }

            #login .inner .chk {
                height: 12px;
            }
        </style>

    </head>
    <body>
        <div id='login'>
            <div class='inner'>
                <div class='fheader'><s:message code="login.title" /></div>

                <c:if test="${not empty param.error}">
                <p style="color:red;padding: 0 10px 10px;">
                    <s:message code="login.invalido" />
                </p>
                <p style="color:red;padding: 0 10px 10px;">
                    <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
                </p>
                </c:if>

                <img src="<c:url value='/images/google_logo.jpg'/>" />
                <form action="j_spring_openid_security_check" id="googleOpenId" method="post" target="_top">
                    <input id="openid_identifier" name="openid_identifier" type="hidden" value="https://www.google.com/accounts/o8/id"/>
                    <input type="submit" value="<s:message code='google.sign.in' />"/>
                </form>
            </div>
        </div>
    </body>
</html>
