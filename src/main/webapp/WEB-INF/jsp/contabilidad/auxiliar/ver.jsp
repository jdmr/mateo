<%-- 
    Document   : ver
    Created on : Jan 27, 2012, 6:52:45 AM
    Author     : jdmr
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="cuentaAuxiliar.ver.label" /></title>
    </head>
    <body>
        <nav class="navbar navbar-fixed-top" role="navigation">
            <ul class="nav">
                <li><a href="<c:url value='/inicio' />"><s:message code="inicio.label" /></a></li>
                <li><a href="<c:url value='/contabilidad' />"><s:message code="contabilidad.label" /></a></li>
                <li class="active"><a href="<s:url value='/contabilidad/auxiliar'/>" ><s:message code="cuentaAuxiliar.label" /></a></li>
            </ul>
        </nav>

        <div id="ver-auxiliar" class="content scaffold-list" role="main">
            <h1><s:message code="cuentaAuxiliar.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/contabilidad/auxiliar'/>"><i class="icon-list icon-white"></i> <s:message code='cuentaAuxiliar.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/contabilidad/auxiliar/nueva'/>"><i class="icon-user icon-white"></i> <s:message code='cuentaAuxiliar.nueva.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/contabilidad/auxiliar/elimina" />
            <form:form commandName="auxiliar" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="nombre.label" /></div>
                    <div class="span11">${auxiliar.nombre}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="nombreFiscal.label" /></div>
                    <div class="span11">${auxiliar.nombreFiscal}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="clave.label" /></div>
                    <div class="span11">${auxiliar.clave}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="detalle.label" /></div>
                    <div class="span11"><form:checkbox path="detalle" disabled="true" /></div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="aviso.label" /></div>
                    <div class="span11"><form:checkbox path="aviso" disabled="true" /></div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="auxiliar.label" /></div>
                    <div class="span11"><form:checkbox path="auxiliar" disabled="true" /></div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="iva.label" /></div>
                    <div class="span11"><form:checkbox path="iva" disabled="true" /></div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="porcentajeIva.label" /></div>
                    <div class="span11">${auxiliar.porcentajeIva}</div>
                </div>
                
                <p class="well">
                    <a href="<c:url value='/contabilidad/auxiliar/edita/${auxiliar.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
