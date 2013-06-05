<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="informe.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="informe" />
        </jsp:include>

        <div id="edita-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="informe.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/factura/informe'/>"><i class="icon-list icon-white"></i> <s:message code='informe.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/factura/informe/actualiza" />
            <form:form commandName="informe" method="post" action="${actualizaUrl}">
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
                    <s:bind path="informe.numNomina">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="numNomina">
                                <s:message code="numeroNomina.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="numNomina" maxlength="50" required="true" />
                            <form:errors path="numNomina" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informe.nombreEmpleado">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="nombreEmpleado">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombreEmpleado" maxlength="150" required="true" />
                            <form:errors path="nombreEmpleado" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informe.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="150" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informe.informe">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="informe">
                                <s:message code="informe.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="informe" cssClass="span3" />
                            <form:errors path="informe" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informe.pesos">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="persos">
                                <s:message code="pesos.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="pesos" cssClass="span3" />
                            <form:errors path="pesos" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informe.dolares">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="dolares">
                                <s:message code="dolares.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="dolares" cssClass="span3" />
                            <form:errors path="dolares" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informe.reembolso">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="reembolso">
                                <s:message code="reembolso.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="reembolso" cssClass="span3" />
                            <form:errors path="reembolso" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="informe.fechaInforme">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fechaInforme">
                                <s:message code="fechaInforme.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaInforme" maxlength="12" required="true" />
                            <form:errors path="fechaInforme" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/paquete/ver/${paquete.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#nombre').focus();
                $("input#fechaInforme").datepicker($.datepicker.regional['es']);
                $("input#fechaInforme").datepicker("option", "firstDay", 0);
            });
        </script>                    
    </content>
</body>
</html>
