<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@include file="../../idioma.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="institucion.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="institucion" />
        </jsp:include>

        <div id="ver-institucion" class="content scaffold-list" role="main">
            <h1><s:message code="institucion.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/instituciones'/>"><i class="icon-list icon-white"></i> <s:message code='institucion.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/inscripciones/instituciones/nueva'/>"><i class="icon-file icon-white"></i> <s:message code='institucion.nueva.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/inscripciones/instituciones/elimina" />
            <form:form commandName="institucion" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="institucion.nombre.label" /></h4>
                        <h3>${institucion.nombre}</h3>
                    </div>
                </div>

                    <div class="span4">
                        <h4><s:message code="institucion.porcentaje" /></h4>
                        <h3>${institucion.porcentaje}</h3>
                    </div>
                    
                <div class="span4">
                        <h4><s:message code="institucion.status.label" /></h4>
                        <h3>${institucion.status}</h3>
                    </div>
                    
                    
                </div>
                    
                <p class="well">
                    <a href="<c:url value='/inscripciones/instituciones/edita/${institucion.id}' />" class="btn btn-primary"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
