<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="institucion.nueva.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="institucion" />
        </jsp:include>

        <div id="nueva-institucion" class="content scaffold-list" role="main">
            <h1><s:message code="institucion.nueva.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/inscripciones/instituciones/'/>"><i class="icon-list icon-white"></i> <s:message code='institucion.lista.label' /></a>
            </p>
            <form:form commandName="institucion" action="graba" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="institucion.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="institucion.nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="60" required="true" cssClass="span3" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                     <s:bind path="institucion.porcentaje">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="porcentaje">
                                <s:message code="institucion.porcentaje" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="porcentaje" required="true" cssClass="span3" />
                            <form:errors path="porcentaje" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                        <s:bind path="institucion.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="status">
                                <s:message code="institucion.status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="2" required="true" cssClass="span3" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>
                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/inscripciones/instituciones'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
