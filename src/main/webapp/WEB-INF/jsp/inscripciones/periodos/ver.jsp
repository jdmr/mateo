<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@include file="../../idioma.jsp"%>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="periodo.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="periodo" />
        </jsp:include>

        <div id="ver-periodo" class="content scaffold-list" role="main">
            <h1><s:message code="periodo.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/periodos'/>"><i class="icon-list icon-white"></i> <s:message code='periodo.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/inscripciones/periodos/nuevo'/>"><i class="icon-file icon-white"></i> <s:message code='periodo.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/inscripciones/periodos/elimina" />
            <form:form commandName="periodo" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="periodo.descripcion.label" /></h4>
                        <h3>${periodo.descripcion}</h3>
                    </div>
                </div>

                <div class="span4">
                        <h4><s:message code="periodo.status.label" /></h4>
                        <h3>${periodo.status}</h3>
                    </div>
                    
                    
                     <div class="span4">
                        <h4><s:message code="periodo.clave.label" /></h4>
                        <h3>${periodo.clave}</h3>
                    </div>
                    
                </div>
                    
                <p class="well">
                    <a href="<c:url value='/inscripciones/periodos/edita/${periodo.id}' />" class="btn btn-primary"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
