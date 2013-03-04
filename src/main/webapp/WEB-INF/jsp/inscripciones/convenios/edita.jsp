<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="afeConvenio.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="afeConvenio" />
        </jsp:include>

        <div id="edita-convenio" class="content scaffold-list" role="main">
            <h1><s:message code="afeConvenio.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/afeConvenio'/>"><i class="icon-list icon-white"></i> <s:message code='afeConvenio.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/inscripciones/afeConvenio/graba" />
            <form:form commandName="afeConvenio" method="post" action="${actualizaUrl}">
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
                    <s:bind path="afeConvenio.descripcion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="matricula">
                                <s:message code="matricula.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="matricula" maxlength="7" required="true" />
                            <form:errors path="matricula" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afeConvenio.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="2" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afeConvenio.diezma">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="diezma">
                                <s:message code="diezma.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="diezma" cssClass="span3" />
                            <form:errors path="diezma" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afeConvenio.numHoras">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="numHoras">
                                <s:message code="numHoras.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="numHoras" maxlength="128" required="true" />
                            <form:errors path="numHoras" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afeConvenio.importe">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="importe">
                                <s:message code="importe.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="importe" maxlength="128" required="true" />
                            <form:errors path="importe" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afeConvenio.tipoBeca">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="tipoBeca">
                                <s:message code="tiposBecas.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="tipoBeca" cssClass="span3" />
                            <form:errors path="tipoBeca" cssClass="alert alert-error" />
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
