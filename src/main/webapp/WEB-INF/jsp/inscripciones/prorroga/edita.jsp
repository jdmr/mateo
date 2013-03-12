<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="prorroga.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="prorroga" />
        </jsp:include>

        <div id="edita-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="prorroga.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/prorroga'/>"><i class="icon-list icon-white"></i> <s:message code='prorroga.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/inscripciones/prorroga/graba" />
            <form:form commandName="prorroga" method="post" action="${actualizaUrl}">
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
                    <s:bind path="prorroga.matricula">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="matricula">
                                <s:message code="matricula.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="matricula" maxlength="8" required="true" />
                            <form:errors path="matricula" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="prorroga.descripcion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="descripcion">
                                <s:message code="descripcion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="descripcion" maxlength="150" required="true" />
                            <form:errors path="descripcion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="prorroga.observaciones">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="observaciones">
                                <s:message code="observaciones.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="observaciones" maxlength="200" required="true" />
                            <form:errors path="observaciones" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="prorroga.fecha">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fecha">
                                <s:message code="fecha.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fecha" maxlength="12" required="true" />
                            <form:errors path="fecha" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="prorroga.fechaCompromiso">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fechaCompromiso">
                                <s:message code="fechaCompromiso.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaCompromiso" maxlength="12" required="true" />
                            <form:errors path="fechaCompromiso" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="prorroga.saldo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="saldo">
                                <s:message code="saldo.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="saldo" maxlength="12" required="true" />
                            <form:errors path="saldo" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="prorroga.status">
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
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/prorroga/ver/${prorroga.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#matricula').focus();
                $("input#fechaCompromiso").datepicker($.datepicker.regional['es']);
                $("input#fechaCompromiso").datepicker("option","firstDay",0);
                $("input#fecha").datepicker($.datepicker.regional['es']);
                $("input#fecha").datepicker("option","firstDay",0);
            });
        </script>                    
    </content>
</body>
</html>
