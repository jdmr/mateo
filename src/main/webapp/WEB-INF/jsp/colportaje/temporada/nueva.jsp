<%-- 
    Document   : nuevo
    Created on : Mar 07, 2013, 10:37:52 AM
    Author     : osoto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="temporada.nueva.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="temporada" />
        </jsp:include>

        <div id="nuevo-temporada" class="content scaffold-list" role="main">
            <h1><s:message code="temporada.nueva.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/colportaje/temporada'/>"><i class="icon-list icon-white"></i> <s:message code='temporada.lista.label' /></a>
            </p>
            <form:form commandName="temporada" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="temporada.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="temporada.fechaInicio">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fechaInicio">
                                <s:message code="fechaInicio.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaInicio" maxlength="10" required="true" />
                            <form:errors path="fechaInicio" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="temporada.fechaFinal">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fechaFinal">
                                <s:message code="fechaFinal.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaFinal" maxlength="10" required="true" />
                            <form:errors path="fechaFinal" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/colportor/temporada'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    </body>
</html>
