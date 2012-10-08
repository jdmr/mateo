<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="dependiente.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="dependiente" />
        </jsp:include>

        <div id="ver-dependiente" class="content scaffold-list" role="main">
            <h1><s:message code="dependiente.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/dependiente'/>"><i class="icon-list icon-white"></i> <s:message code='dependiente.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/rh/dependiente/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='dependiente.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/rh/dependiente/elimina" />
            <form:form commandName="dependiente" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="dependiente.nombre.label" /></div>
                    <div class="span11">${dependiente.nombre}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="dependiente.fechaNacimiento.label" /></div>
                    <div class="span11">${dependiente.apPaterno}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="dependiente.estudios.label" /></div>
                    <div class="span11">${dependiente.apMaterno}</div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="dependiente.grado.label" /></div>
                    <div class="span11">${dependiente.tipoDependiente}</div>
                </div>
                
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="dependiente.version.label" /></div>
                    <div class="span11">${dependiente.tipoDependiente}</div>
                </div>

                
                <p class="well">
                    <a href="<c:url value='/rh/dependiente/edita/${dependiente.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>

