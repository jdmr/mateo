<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="paquete.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="paquete" />
        </jsp:include>

        <div id="nuevo-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="paquete.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/paquete'/>"><i class="icon-list icon-white"></i> <s:message code='paquete.lista.label' /></a>
            </p>
            <form:form commandName="paquete" action="graba" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                     <s:bind path="paquete.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="50" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="paquete.descripcion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="descripcion">
                                <s:message code="descripcion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="descripcion" maxlength="150" required="true" />
                            <form:errors path="descripcion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="paquete.matricula">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="matricula">
                                <s:message code="matricula.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="matricula" maxlength="8" required="true" />
                            <form:errors path="matricula" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="paquete.ensenanza">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="ensenanza">
                                <s:message code="ensenanza.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="ensenanza" maxlength="8" required="true" />
                            <form:errors path="ensenanza" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="paquete.internado">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="internado">
                                <s:message code="internado.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="internado" maxlength="8" required="true" />
                            <form:errors path="internado" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="paquete.acfe">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="acfe">
                                <s:message code="acfe.label" />
                                <span class="required-indicator">*</span>
                            </label>
                                <form:radiobutton path="acfe"  value="1"  cssClass="span3" />Si<br />
                                <form:radiobutton path="acfe"  value="0"  cssClass="span3" />No<br />
                            <form:errors path="acfe" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/paquete'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
