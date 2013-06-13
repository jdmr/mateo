<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="cobroCampo.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="cobroCampo" />
        </jsp:include>

        <div id="nuevo-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="cobroCampo.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/cobroCampo'/>"><i class="icon-list icon-white"></i> <s:message code='cobroCampo.lista.label' /></a>
            </p>
            <form:form commandName="cobroCampo" action="graba" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="cobroCampo.matricula">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="matricula">
                                <s:message code="matricula.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="matricula" maxlength="8" required="true" />
                            <form:errors path="matricula" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="cobroCampo.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="12" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="cobroCampo.importeMatricula">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="matricula">
                                <s:message code="importe.matricula.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="importeMatricula" maxlength="8" required="true" />
                            <form:errors path="importeMatricula" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="cobroCampo.importeEnsenanza">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="ensenanza">
                                <s:message code="importe.ensenanza.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="importeEnsenanza" maxlength="8" required="true" />
                            <form:errors path="importeEnsenanza" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="cobroCampo.importeInternado">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="internado">
                                <s:message code="importe.internado.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="importeInternado" maxlength="8" required="true" />
                            <form:errors path="importeInternado" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="cobroCampo.institucion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="institucion">
                                <s:message code="institucion.label" />
                                <span class="required-indicator">*</span>
                                <form:select id="institucionId" path="institucion.id" items="${instituciones}" itemLabel="nombre" itemValue="id" />
                                <form:errors path="institucion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/cobroCampo'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#matricula').focus();
            });
        </script>                    
    </content>
</body>
</html>
