<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="alumnoPaquete.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="alumnoPaquete" />
        </jsp:include>

        <div id="edita-colegio" class="content scaffold-list" role="main">
            <h1><s:message code="alumnoPaquete.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/alumnoPaquete'/>"><i class="icon-list icon-white"></i> <s:message code='alumnoPaquete.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/inscripciones/alumnoPaquete/graba" />
            <form:form commandName="alumnoPaquete" method="post" action="${actualizaUrl}">
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
                    <s:bind path="alumnoPaquete.matricula">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="matricula">
                                <s:message code="matricula.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="matricula" maxlength="7" required="true" />
                            <form:errors path="matricula" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="alumnoPaquete.paquete">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="paquete">
                                <s:message code="paquete.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:select id="paquete" path="paquete.id" required="true" cssClass="span11" >
                                <form:options items="${paquetes}" itemValue="id" itemLabel="nombre"/>
                            </form:select>
                            <form:errors path="paquete" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                 <s:bind path="alumnoPaquete.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="2" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/alumnoPaquete/ver/${alumnoPaquete.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
