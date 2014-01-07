<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@include file="../../idioma.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="vacacionesEmpleado.ver.label" /></title>
    </head>
    <body>
        <br/>
        <br/>
        <br/>
        <br/>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="solicitudVacacionesEmpleado" />
        </jsp:include>

        <div id="ver-nacionalidad" class="content scaffold-list" role="main">
            <h1><s:message code="vacacionesEmpleado.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/solicitudVacacionesEmpleado'/>"><i class="icon-list icon-white"></i> <s:message code='vacacionesEmpleado.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/rh/solicitudVacacionesEmpleado/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='vacacionesEmpleado.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/rh/solicitudVacacionesEmpleado/elimina" />
            <form:form commandName="solicitudVacacionEmpleado" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="fechaInicio.label" /></div>
                    <div class="span11">${solicitudVacacionEmpleado.fechaInicio}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="fechaFinal.label" /></div>
                    <div class="span11">${solicitudVacacionEmpleado.fechaFinal}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="observaciones.label" /></div>
                    <div class="span11">${solicitudVacacionEmpleado.observaciones}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="telefono.label" /></div>
                    <div class="span11">${solicitudVacacionEmpleado.contactoTelefono}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="correo.label" /></div>
                    <div class="span11">${solicitudVacacionEmpleado.contactoCorreo}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="primaVacacional.label" /></div>
                    <div class="span11"><input type="checkbox" value="" disabled="true" <c:if test="${solicitudVacacionEmpleado.primaVacacional}">checked="checked"</c:if> />
                        </div>
                    </div>


                </div>
                <p class="well">
                    <a href="<c:url value='/rh/solicitudVacacionesEmpleado/edita/${solicitudVacacionEmpleado.id}' />" class="btn btn-primary"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                <form:hidden path="id" />
                <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
            </p>
        </form:form>
    </div>
</body>
</html>
