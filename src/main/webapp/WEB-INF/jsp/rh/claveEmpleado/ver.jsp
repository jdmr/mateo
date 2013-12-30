<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@include file="../../idioma.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="claveEmpleado.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="claveEmpleado" />
        </jsp:include>

        <div id="ver-nacionalidad" class="content scaffold-list" role="main">
            <h1><s:message code="claveEmpleado.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/claveEmpleado'/>"><i class="icon-list icon-white"></i> <s:message code='claveEmpleado.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/rh/claveEmpleado/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='claveEmpleado.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/rh/claveEmpleado/elimina" />
            <form:form commandName="claveEmpleado" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="clave.label" /></div>
                    <div class="span11">${claveEmpleado.clave}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="status.label" /></div>
                    <div class="span11">${claveEmpleado.status}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="fecha.label" /></div>
                    <div class="span11">${claveEmpleado.fecha}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="observaciones.label" /></div>
                    <div class="span11">${claveEmpleado.observaciones}</div>
                </div>


            </div>
            <p class="well">
                <a href="<c:url value='/rh/claveEmpleado/edita/${claveEmpleado.id}' />" class="btn btn-primary"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                <form:hidden path="id" />
                <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
            </p>
        </form:form>
    </div>
</body>
</html>
