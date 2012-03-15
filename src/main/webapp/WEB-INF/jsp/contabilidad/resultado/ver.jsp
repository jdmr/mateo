<%-- 
    Document   : ver
    Created on : Mar 12, 2012, 9:17:41 AM
    Author     : develop
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="cuentaResultado.ver.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/contabilidad' />"><s:message code="contabilidad.label" /></a></li>
                <li class="active"><a href="<s:url value='/contabilidad/resultado'/>" ><s:message code="cuentaResultado.label" /></a></li>
            </ul>
        </nav>

        <div id="ver-resultado" class="content scaffold-list" role="main">
            <h1><s:message code="cuentaResultado.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/contabilidad/resultado'/>"><i class="icon-list icon-white"></i> <s:message code='cuentaResultado.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/contabilidad/resultado/nueva'/>"><i class="icon-user icon-white"></i> <s:message code='cuentaResultado.nueva.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/contabilidad/resultado/elimina" />
            <form:form commandName="resultado" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="nombre.label" /></div>
                    <div class="span11">${resultado.nombre}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="nombreFiscal.label" /></div>
                    <div class="span11">${resultado.nombreFiscal}</div>
                </div>

                <p class="well">
                    <a href="<c:url value='/contabilidad/resultado/edita/${resultado.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>