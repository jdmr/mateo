<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="tiposBecas.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="tiposBecas" />
        </jsp:include>

        <div id="edita-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="colegio.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/tiposBecas'/>"><i class="icon-list icon-white"></i> <s:message code='tiposBecas.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/inscripciones/tiposBecas/graba" />
            <form:form commandName="tipoBeca" method="post" action="${actualizaUrl}">
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
                    <s:bind path="tipoBeca.descripcion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="descripcion">
                                <s:message code="descripcion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="descripcion" maxlength="128" required="true" />
                            <form:errors path="descripcion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="tipoBeca.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="2" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="tipoBeca.diezma">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="diezma">
                                <s:message code="diezma.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="diezma" cssClass="span3" />
                            <form:errors path="diezma" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="tipoBeca.numHoras">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="numHoras">
                                <s:message code="numHoras.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="numHoras" maxlength="128" required="true" />
                            <form:errors path="numHoras" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="tipoBeca.porcentaje">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="porcentaje">
                                <s:message code="porcentaje.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="porcentaje" maxlength="128" required="true" />
                            <form:errors path="porcentaje" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="tipoBeca.tope">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="tope">
                                <s:message code="tope.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="tope" maxlength="128" required="true" />
                            <form:errors path="tope" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="tipoBeca.perteneceAlumno">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="perteneceAlumno">
                                <s:message code="perteneceAlumno.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="perteneceAlumno" cssClass="span3" />
                            <form:errors path="perteneceAlumno" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="tipoBeca.soloPostgrado">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="soloPostgrado">
                                <s:message code="soloPostgrado.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="soloPostgrado" cssClass="span3" />
                            <form:errors path="soloPostgrado" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/tiposBecas/ver/${tiposBecas.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#descripcion').focus();
            });
        </script>                    
    </content>
</body>
</html>
