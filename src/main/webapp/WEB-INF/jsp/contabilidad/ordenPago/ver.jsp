

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="ordenPago.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="ordenPago" />
        </jsp:include>

        <div id="ver-ordenPago" class="content scaffold-list" role="main">
            <h1><s:message code="ordenPago.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/contabilidad/ordenPago'/>"><i class="icon-list icon-white"></i> <s:message code='ordenPago.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/contabilidad/ordenPago/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='ordenPago.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/contabilidad/ordenPago/elimina" />
            <form:form commandName="ordenPago" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="descripcion.label" /></div>
                    <div class="span11">${ordenPago.descripcion}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="cheque.label" /></div>
                    <div class="span11"><form:checkbox path="cheque" disabled="true" /></div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="fechaPago.label" /></div>
                    <div class="span11">${ordenPago.fechaPago}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="userCaptura.label" /></div>
                    <div class="span11">${ordenPago.userCaptura.username}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="fechaCaptura.label" /></div>
                    <div class="span11">${ordenPago.fechaCaptura}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="empresa.label" /></div>
                    <div class="span11">${ordenPago.empresa.nombre}</div>
                </div>
                

                <p class="well">
                    <a href="<c:url value='/contabilidad/ordenPago/edita/${ordenPago.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
