<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="afePlaza.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="afePlaza" />
        </jsp:include>

        <div id="nuevo-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="afePlaza.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/afePlaza'/>"><i class="icon-list icon-white"></i> <s:message code='afePlaza.lista.label' /></a>
            </p>
            <form:form commandName="afePlaza" action="graba" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>

                    <s:bind path="afePlaza.tipoPlaza">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="tipoPlaza">
                                <s:message code="tipoPlaza.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="tipoPlaza" maxlength="85" required="true" />
                            <form:errors path="tipoPlaza" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afePlaza.clave">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="clave">
                                <s:message code="afePlaza.clave.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clave" maxlength="50" required="true" />
                            <form:errors path="clave" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afePlaza.observaciones">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="observaciones">
                                <s:message code="observaciones.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="observaciones" maxlength="85" required="true" />
                            <form:errors path="observaciones" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afePlaza.primerIngreso">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="primerIngreso">
                                <s:message code="primerIngreso.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="primerIngreso" cssClass="span3" />
                            <form:errors path="primerIngreso" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afePlaza.industrial">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="industrial">
                                <s:message code="industrial.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="industrial" cssClass="span3" />
                            <form:errors path="industrial" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afePlaza.turno">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="turno">
                                <s:message code="turno.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="turno" maxlength="85" required="true" />
                            <form:errors path="turno" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afePlaza.dias">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="dias">
                                <s:message code="dias.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="dias" maxlength="85" required="true" />
                            <form:errors path="dias" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afePlaza.requisitos">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="requisitos">
                                <s:message code="requisitos.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="requisitos" maxlength="85" required="true" />
                            <form:errors path="requisitos" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afePlaza.email">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="email">
                                <s:message code="email.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="email" maxlength="85" required="true" />
                            <form:errors path="email" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="afePlaza.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="12" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>


                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/afePlaza'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#tipoPlaza').focus();
            });
        </script>                    
    </content>
</body>
</html>
