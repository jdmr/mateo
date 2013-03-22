<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="puesto.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" />

        <div id="nuevo-puesto" class="content scaffold-list" role="main">
            <h1><s:message code="puesto.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/puestos'/>"><i class="icon-list icon-white"></i> <s:message code='puesto.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/rh/puestos/graba" />
            <form:form commandName="puesto" action="${actualizaUrl}" method="post">
                <form:hidden path="id" />
                <form:hidden path="version" />
                <form:hidden path="status" />
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="descripcion">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="descripcion">
                                <s:message code="descripcion.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="descripcion" maxlength="150" required="true" cssClass="span4" />
                        </div>
                    </s:bind>
                    <s:bind path="categoria">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="categoria">
                                <s:message code="categoria.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="categoria" maxlength="5" required="true" cssClass="span4" min="3" />
                        </div>
                    </s:bind>


                    <s:bind path="seccion">
                        <s:message code="seccion.label" />
                        <span class="required-indicator">*</span>
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <div>
                                <label for="seccion">
                            </div>
                            <form:select id="seccionId" path="seccion.id" items="${secciones}" itemLabel="nombre" itemValue="id" />
                            <form:errors path="seccion" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="minimo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="minimo">
                                <s:message code="minimo.label" />
                            </label>
                            <form:input path="minimo" maxlength="5" cssClass="span4" min="2"/>
                        </div>
                    </s:bind>
                    <s:bind path="maximo">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="maximo">
                                <s:message code="maximo.label" />
                            </label>
                            <form:input path="maximo" maxlength="5" cssClass="span4" min="2" />
                        </div>
                    </s:bind>
                    <s:bind path="rangoAcademico">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="rangoAcademico">
                                <s:message code="rangoAcademico.label" />
                            </label>
                            <form:input path="rangoAcademico" maxlength="5" cssClass="span4" min="2" />
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/puestos/ver/${puesto.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
