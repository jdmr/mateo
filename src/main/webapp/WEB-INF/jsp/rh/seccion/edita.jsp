<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="seccion.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="seccion" />
        </jsp:include>

       <div id="edita-seccion" class="content scaffold-list" role="main">
            <h1><s:message code="seccion.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/seccion'/>"><i class="icon-list icon-white"></i> <s:message code='seccion.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/rh/seccion/graba" />
            <form:form commandName="seccion" method="post" action="${actualizaUrl}">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>
                <form:hidden path="id" />
                <form:hidden path="version" />

                <fieldset>
                    <s:bind path="seccion.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="categoriaId">
                                <s:message code="categoriaId.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="categoriaId" maxlength="128" required="true" />
                            <form:errors path="categoriaId" cssClass="alert alert-error" />
                        </div>
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="rangoAcademico">
                                <s:message code="rangoAcademico.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="rangoAcademico" maxlength="128" required="true" />
                            <form:errors path="rangoAcademico" cssClass="alert alert-error" />
                        </div>
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="minimo">
                                <s:message code="minimo.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="minimo" maxlength="128" required="true" />
                            <form:errors path="minimo" cssClass="alert alert-error" />
                        </div>
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="maximo">
                                <s:message code="maximo.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="maximo" maxlength="128" required="true" />
                            <form:errors path="maximo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/seccion/ver/${seccion.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
        <content>
            <script>
                $(document).ready(function() {
                    $('input#nombre').focus();
                });
            </script>                    
        </content>
    </body>
</html>
