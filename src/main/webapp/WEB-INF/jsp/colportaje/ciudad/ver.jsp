<%-- 
    Document   : ver
    Created on : Mar 07, 2013, 6:52:45 AM
    Author     : osoto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="ciudad.ver.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="ciudad" />
        </jsp:include>

        <div id="ver-ciudad" class="content scaffold-list" role="main">
            <h1><s:message code="ciudad.ver.label" /></h1>

            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/ciudad'/>"><i class="icon-list icon-white"></i> <s:message code='ciudad.lista.label' /></a>
                <a class="btn btn-primary" href="<s:url value='/colportaje/ciudad/nueva'/>"><i class="icon-file icon-white"></i> <s:message code='ciudad.nueva.label' /></a>
            </p>
            <c:if test="${not empty message}">
                <div class="alert alert-block alert-success fade in" role="status">
                    <a class="close" data-dismiss="alert">×</a>
                    <s:message code="${message}" arguments="${messageAttrs}" />
                </div>
            </c:if>

            <c:url var="eliminaUrl" value="/colportaje/ciudad/elimina" />
            <form:form commandName="ciudad" action="${eliminaUrl}" >
                <form:errors path="*" cssClass="alert alert-error" element="ul" />
                <div class="row-fluid" style="padding-bottom: 10px;">
                    <div class="span4">
                        <h4><s:message code="nombre.label" /></h4>
                        <h3>${ciudad.nombre}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="pais.label" /></h4>
                        <h3>${ciudad.estado.nombre}</h3>
                    </div>
                    <div class="span4">
                        <h4><s:message code="pais.label" /></h4>
                        <h3>${ciudad.estado.pais.nombre}</h3>
                    </div>
                </div>

                <p class="well">
                    <a href="<c:url value='/colportaje/ciudad/edita/${ciudad.id}' />" class="btn btn-primary btn-large"><i class="icon-edit icon-white"></i> <s:message code="editar.button" /></a>
                    <form:hidden path="id" />
                    <button type="submit" name="eliminaBtn" class="btn btn-danger btn-large" id="eliminar"  onclick="return confirm('<s:message code="confirma.elimina.message" />');" ><i class="icon-trash icon-white"></i>&nbsp;<s:message code='eliminar.button'/></button>
                </p>
            </form:form>
        </div>
    </body>
</html>
