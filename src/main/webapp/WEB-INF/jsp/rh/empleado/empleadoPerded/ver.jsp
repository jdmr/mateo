<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="empleadoPerded.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="empleado" />
        </jsp:include>

        <div id="ver-empleadoPerded" class="content scaffold-list" role="main">
            <h1><s:message code="empleadoPerded.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/empleado/empleadoPerded'/>"><i class="icon-list icon-white"></i> <s:message code='empleadoPerded.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/rh/empleado/empleadoPerded/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='empleadoPerded.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/rh/empleado/empleadoPerded/elimina" />
            <form:form commandName="empleadoPerded" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="perDed.nombre.label" /></h4>
                        <h3>${empleadoPerded.perDed.nombre}</h3>
                    </div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="empleado.clave.label" /></h4>
                        <h3>${empleadoPerded.empleado.clave}</h3>
                    </div>
                </div>


                <p class="well">
                    <a href="<c:url value='/rh/empleado/empleadoPerded/edita/${empleadoPerded.id}' />" class="btn btn-primary"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
