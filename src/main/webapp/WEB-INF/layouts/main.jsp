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
<html>
    <head>
        <meta charset="utf-8">
        <title><s:message code="proyecto.nombre.label" /> - <sitemesh:write property='title'/></title>
        <meta name="description" content="" />
        <meta name="author" content="" />

        <!-- Le HTML5 shim, for IE6-8 support of HTML elements -->
        <!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->        
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        
        <!-- HTTP 1.1 -->
        <meta http-equiv="Cache-Control" content="no-store"/>
        <!-- HTTP 1.0 -->
        <meta http-equiv="Pragma" content="no-cache"/>
        <!-- Prevents caching at the Proxy Server -->
        <meta http-equiv="Expires" content="0"/>
        
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link rel="shortcut icon" href="<c:url value='/images/favicon.ico' />" type="image/x-icon" />
        <link rel="apple-touch-icon" href="<c:url value='/images/apple-touch-icon.png' />" />
        <link rel="apple-touch-icon" sizes="114x114" href="<c:url value='/images/apple-touch-icon-retina.png' />" />
        <link rel="stylesheet" href="<c:url value='/css/bootstrap.min.css' />" type="text/css" />
        <link rel="stylesheet" href="<c:url value='/css/bootstrap-responsive.min.css' />" type="text/css" />
        <%--link rel="stylesheet" href="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/themes/base/jquery-ui.css" type="text/css" media="all" /--%>
        <link rel="stylesheet" href="<c:url value='/css/custom-theme/jquery-ui-1.8.23.custom.css' />" type="text/css"/>
        <sitemesh:write property='head'/>
        <link rel="stylesheet" href="<c:url value='/css/app.css' />" type="text/css">
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <div class="navbar-inner">
                <div class="container-fluid">
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <a class="brand" href="<c:url value='/inicio' />"><s:message code="proyecto.nombre.label" /></a>
                    <div class="nav-collapse">
                        <sitemesh:write property="nav"/>
                        <p class="navbar-text pull-right">
                            <s:message code="mensaje.bienvenida" /> <a href="<c:url value='/perfil' />"><%= request.getUserPrincipal().getName()%></a> 
                            <a href="<c:url value='/salir' />"><i class="icon-off"></i></a></p>
                    </div><!--/.nav-collapse -->
                </div>
            </div>
        </nav>
        <header class="subnav-fixed" id="admin" role="subnavigation">
            <sitemesh:write property="header"/>
        </header>
        <div class="container-fluid">
            <sitemesh:write property='body'/>
        </div>

        <footer>
            <hr />
            <div class="row-fluid">
                <div class="span8"><p>&copy; <s:message code="proyecto.copyright.year.label" /> <s:message code="proyecto.empresa.label" /></p></div>
                <div class="span4">
                    <p class="pull-right" style="margin-right: 20px;"><a href="<c:url value='/perfil' />">${sessionScope.organizacionLabel} | ${sessionScope.empresaLabel} | ${sessionScope.almacenLabel} | ${sessionScope.ejercicioId}</a></p>
                </div>
            </div>
        </footer>

        <!-- JavaScript at the bottom for fast page loading -->

        <!-- Grab Google CDN's jQuery, with a protocol relative URL; fall back to local if offline -->
        <%--script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.1/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="<c:url value='/js/jquery-1.8.1.min.js'/>"><\/script>')</script--%>

        <!-- end scripts -->        
        <%--script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.8.23/jquery-ui.min.js"></script--%>
        <script src="<c:url value='/js/jquery-1.8.1.min.js' />"></script>
        <script src="<c:url value='/js/jquery-ui-1.8.23.custom.min.js' />"></script>
        <script src="<c:url value='/js/i18n/jquery.ui.datepicker-es.min.js' />"></script>
        <script src="<c:url value='/js/bootstrap.min.js' />"></script>
        <script src="<c:url value='/js/app.js' />"></script>
        <sitemesh:write property="content"/>
    </body>
</html>
