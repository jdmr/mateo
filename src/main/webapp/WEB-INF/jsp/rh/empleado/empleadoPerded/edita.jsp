<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="empleadoPerded.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="empleado" />
        </jsp:include>

        <div id="nuevo-perded" class="content scaffold-list" role="main">
            <h1><s:message code="empleadoPerded.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/empleado/empleadoPerded'/>"><i class="icon-list icon-white"></i> <s:message code='empleadoPerded.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/rh/empleado/empleadoPerded/graba" />
            <form:form commandName="perded" action="${actualizaUrl}" method="post">
                <form:hidden path="id" />
                <form:hidden path="status" />
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
                    <s:bind path="perded.clave">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="clave">
                                <s:message code="perded.clave.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:errors path="perDed.clave" cssClass="alert alert-error" />
                            <form:input path="perDed.clave" maxlength="10" required="true" cssClass="span3" />
                        </div>
                    </s:bind>
                    <s:bind path="perded.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:errors path="status" cssClass="alert alert-error" />
                            <form:input path="status" maxlength="1" required="true" cssClass="span3" />
                        </div>
                    </s:bind>
                   
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/empleado/empleadoPerded/ver/${perded.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
