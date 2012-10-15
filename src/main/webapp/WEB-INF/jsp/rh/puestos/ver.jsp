<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="puesto.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="puesto" />
        </jsp:include>

        <div id="ver-puesto" class="content scaffold-list" role="main">
            <h1><s:message code="puesto.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/puestos'/>"><i class="icon-list icon-white"></i> <s:message code='puesto.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/rh/puestos/nuevo'/>"><i class="icon-user icon-white"></i> <s:message code='puesto.nuevo.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/rh/puestos/elimina" />
            <form:form commandName="puesto" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="puesto.descripcion.label" /></h4>
                        <h3>${puesto.descripcion}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="puesto.categoria.label" /></h4>
                        <h3>${puesto.categoria}</h3>
                    </div>
                </div>

                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="puesto.seccion.label" /></h4>
                        <h3>${puesto.seccion}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="puesto.minimo.label" /></h4>
                        <h3>${puesto.minimo}</h3>
                    </div>
                </div>

                <c:if test="${not empty puesto.maximo}">
                    <div class="row-fluid" style="padding-bottom: 10px;">
                        <div class="span8">
                            <h4><s:message code="puesto.maximo.label" /></h4>
                            <h3>${puesto.maximo}</h3>
                        </div>
                    </div>
                </c:if>

                <c:if test="${not empty puesto.rangoAcademico}">
                    <div class="row-fluid" style="padding-bottom: 10px;">
                        <div class="span4">
                            <h4><s:message code="puesto.rangoAcademico.label" /></h4>
                            <h3>${puesto.rangoAcademico}</h3>
                        </div>
                    </div>
                </c:if>


                <p class="well">
                    <a href="<c:url value='/rh/puestos/edita/${puesto.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
