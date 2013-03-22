<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="periodo.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="periodo" />
        </jsp:include>

        <div id="nuevo-periodo" class="content scaffold-list" role="main">
            <h1><s:message code="periodo.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/periodos'/>"><i class="icon-list icon-white"></i> <s:message code='periodo.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/inscripciones/periodos/graba" />
            <form:form commandName="periodo" action="${actualizaUrl}" method="post">
                <form:hidden path="id" />
                <form:hidden path="version" />
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="periodo.descripcion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="descripcion">
                                <s:message code="periodo.descripcion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="descripcion" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="descripcion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                   <s:bind path="periodo.clave">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="clave">
                                <s:message code="periodo.clave.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clave" maxlength="10" required="true" cssClass="span3" />
                            <form:errors path="clave" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="periodo.fechaInicial">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fechaInicial">
                                <s:message code="periodo.fechaInicial.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaInicial" maxlength="10" required="true" cssClass="span3" />
                            <form:errors path="fechaInicial" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="periodo.fechaFinal">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fechaFinal">
                                <s:message code="periodo.fechaFinal.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaFinal" maxlength="10" required="true" cssClass="span3" />
                            <form:errors path="fechaFinal" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="periodo.incluye">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="incluye">
                                <s:message code="periodo.incluye.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="incluye" maxlength="50" required="true" cssClass="span3" />
                            <form:errors path="incluye" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="periodo.excluye">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="excluye">
                                <s:message code="periodo.excluye.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="excluye" maxlength="50" required="true" cssClass="span3" />
                            <form:errors path="excluye" cssClass="alert alert-error" />
                        </div>                
                    </s:bind> 
                        <s:bind path="periodo.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="status">
                                <s:message code="periodo.status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="2" required="true" cssClass="span3" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/periodos/ver/${periodo.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
