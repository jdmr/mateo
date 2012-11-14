<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="empleadoEstudios.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="empleadoEstudios" />
        </jsp:include>

        <div id="ver-dependiente" class="content scaffold-list" role="main">
            <h1><s:message code="empleadoEstudios.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/empleadoEstudios'/>"><i class="icon-list icon-white"></i> <s:message code='empleadoEstudios.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/rh/empleadoEstudios/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='empleadoEstudios.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/rh/empleadoEstudios/elimina" />
            <form:form commandName="empleadoEstudios" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="empleadoEstudios.nombreEstudios.label" /></div>
                    <div class="span11">${empleadoEstudios.nombreEstudios}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="empleadoEstudios.nivelEstudios.label" /></div>
                    <div class="span11">${empleadoEstudios.nivelEstudios}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="empleadoEstudios.titulado.label" /></div>
                    <div class="span11">${empleadoEstudios.titulado}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="empleadoEstudios.fechaTitulacion.label" /></div>
                    <div class="span11">${empleadoEstudios.fechaTitulacion}</div>
                </div>
                
               <%-- <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="empleadoEstudios.userCaptura.label" /></div>
                    <div class="span11">${empleadoEstudios.userCaptura}</div> 
                </div> --%>
                
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="empleadoEstudios.fechaCaptura.label" /></div>
                    <div class="span11">${empleadoEstudios.fechaCaptura}</div>
                </div>

                
                <p class="well">
                    <a href="<c:url value='/rh/empleadoEstudios/edita/${empleadoEstudios.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>

