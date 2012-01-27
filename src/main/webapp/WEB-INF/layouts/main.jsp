<%-- 
    Document   : main
    Created on : Jan 24, 2012, 1:43:27 PM
    Author     : jdmr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s"   uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!doctype html>
<!--[if lt IE 7 ]> <html lang="en" class="no-js ie6"> <![endif]-->
<!--[if IE 7 ]>    <html lang="en" class="no-js ie7"> <![endif]-->
<!--[if IE 8 ]>    <html lang="en" class="no-js ie8"> <![endif]-->
<!--[if IE 9 ]>    <html lang="en" class="no-js ie9"> <![endif]-->
<!--[if (gt IE 9)|!(IE)]><!--> <html lang="en" class="no-js"><!--<![endif]-->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title>MATEO - <sitemesh:write property='title'/></title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="shortcut icon" href="<c:url value='/images/favicon.ico' />" type="image/x-icon">
        <link rel="apple-touch-icon" href="<c:url value='/images/apple-touch-icon.png' />">
        <link rel="apple-touch-icon" sizes="114x114" href="<c:url value='/images/apple-touch-icon-retina.png' />">
        <link rel="stylesheet" href="<c:url value='/css/main.css' />" type="text/css">
        <link rel="stylesheet" href="<c:url value='/css/mobile.css' />" type="text/css">
        <sitemesh:write property='head'/>
    </head>
    <body>
        <div id="grailsLogo" role="banner">
            <div id="logo">
                <a href="<c:url value='/' />" style="text-decoration:none;font-size:3em;letter-spacing:2px;">MATEO</a>
            </div>
            <div class="encabezado">
                <sec:authorize access="isAuthenticated()">
                    <p><a href="<c:url value='/perfil'/>"><s:message code="mensaje.bienvenida" arguments="<%= request.getUserPrincipal().getName() %>" /></a></p>
                    <p><a href="<c:url value='/perfil'/>">${sessionScope.organizacion} | ${sessionScope.empresa} | ${sessionScope.almacen}</a></p>
                    <p><a href="<c:url value='/salir'/>"><s:message code="salir.label" /></a></p>
                </sec:authorize>
            </div>
        </div>
        <div id="page-body" role="main">
            <sitemesh:write property='body'/>
        </div>
        <div class="footer" role="contentinfo">
            <p>&copy; 2012 Universidad de Montemorelos A.C.</p>
        </div>
        <div id="spinner" class="spinner" style="display:none;"><g:message code="spinner.alt" default="Loading&hellip;"/></div>
    </body>
</html>
