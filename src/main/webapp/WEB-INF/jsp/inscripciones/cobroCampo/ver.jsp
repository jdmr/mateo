<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="cobroCampo.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="cobroCampo" />
        </jsp:include>

        <div id="ver-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="cobroCampo.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/cobroCampo'/>"><i class="icon-list icon-white"></i> <s:message code='cobroCampo.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/inscripciones/cobroCampo/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='cobroCampo.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/inscripciones/cobroCampo/elimina" />
            <form:form commandName="cobroCampo" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="matricula.label" /></div>
                    <div class="span11">${cobroCampo.matricula}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="status.label" /></div>
                    <div class="span11">${cobroCampo.status}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="importe.matricula.label" /></div>
                    <div class="span11">${cobroCampo.importeMatricula}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="importe.ensenanza.label" /></div>
                    <div class="span11">${cobroCampo.importeEnsenanza}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="importe.internado.label" /></div>
                    <div class="span11">${cobroCampo.importeInternado}</div>
                </div>
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span1"><s:message code="institucion.label" /></div>
                    <div class="span11">${cobroCampo.institucion.nombre}</div>
                </div>





                <p class="well">
                    <a href="<c:url value='/inscripciones/cobroCampo/edita/${cobroCampo.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>

