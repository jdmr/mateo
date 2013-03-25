<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="empleadoEstudios.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="empleadoEstudios" />
        </jsp:include>

        <div id="nuevo-dependiente" class="content scaffold-list" role="main">
            <h1><s:message code="empleadoEstudios.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/empleadoEstudios'/>"><i class="icon-list icon-white"></i> <s:message code='empleadoEstudios.lista.label' /></a>
            </p>
            <form:form commandName="empleadoEstudios" action="crea" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="empleadoEstudios.nombreEstudios">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombreEstudios">
                                <s:message code="empleadoEstudios.nombreEstudios.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombreEstudios" maxlength="75" required="true" />
                            <form:errors path="nombreEstudios" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleadoEstudios.nivelEstudios">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="nivelEstudios">
                                <s:message code="nivelEstudios.label" />
                                <span class="required-indicator">*</span>
                                </label>
                                <form:select id="nivelEstudios" path="nivelEstudios" required="true" cssClass="span3" >
                                    <form:options items= "${nivelEstudios}" />
                                </form:select>
                                <form:errors path="nivelEstudios" cssClass="alert alert-error" />
                                     
                        </div>
                    </s:bind>
                    <s:bind path="empleadoEstudios.titulado">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="titulado">
                                <s:message code="empleadoEstudios.titulado.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:checkbox path="titulado" cssClass="span3" />
                            <form:errors path="titulado" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="empleadoEstudios.fechaTitulacion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fechaTitulacion">
                                <s:message code="empleadoEstudios.fechaTitulacion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaTitulacion" maxlength="128" required="true" />
                            <form:errors path="fechaTitulacion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <%--  <s:bind path="empleadoEstudios.userCaptura">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="userCaptura">
                                <s:message code="empleadoEstudios.userCaptura.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="userCaptura" maxlength="128" required="true" />
                            <form:errors path="userCaptura" cssClass="alert alert-error" />
                        </div>
                      </s:bind> --%>
                    <s:bind path="empleadoEstudios.fechaCaptura">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fechaCaptura">
                                <s:message code="empleadoEstudios.fechaCaptura.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaCaptura" maxlength="128" required="true" />
                            <form:errors path="fechaCaptura" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/empleadoEstudios'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
