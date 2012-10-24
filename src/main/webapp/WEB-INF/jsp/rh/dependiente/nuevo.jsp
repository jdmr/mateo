<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="dependiente.nuevo.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="dependiente" />
        </jsp:include>

        <div id="nuevo-dependiente" class="content scaffold-list" role="main">
            <h1><s:message code="dependiente.nuevo.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/dependiente'/>"><i class="icon-list icon-white"></i> <s:message code='dependiente.lista.label' /></a>
            </p>
            <form:form commandName="dependiente" action="graba" method="post">
                <form:errors path="*">
                    <div class="alert alert-block alert-error fade in" role="status">
                        <a class="close" data-dismiss="alert">×</a>
                        <c:forEach items="${messages}" var="message">
                            <p>${message}</p>
                        </c:forEach>
                    </div>
                </form:errors>

                <fieldset>
                    <s:bind path="dependiente.nombre">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="nombre">
                                <s:message code="nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="nombre" maxlength="128" required="true" />
                            <form:errors path="nombre" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="dependiente.fechaNac">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="fechaNac">
                                <s:message code="fechaNacimiento.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fechaNac" maxlength="128" required="true" />
                            <form:errors path="fechaNac" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="dependiente.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                            <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="status" maxlength="2" required="true" />
                            <form:errors path="status" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                   <s:bind path="dependiente.tipoDependiente">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="tipoDependiente">
                                <s:message code="tipoDependiente.label" />
                                <span class="required-indicator">*</span>
                                <select name="tipoDependiente" >
                                    <c:forEach items= "${tiposDependiente}" var= "TD">
                                        <option value ="${TD.id} "><c:out value="${TD.descripcion}" /></option>
                                    </c:forEach>
                                </select>
                                
                        </div>
                    </s:bind>
                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="crearBtn" class="btn btn-primary btn-large" id="crear" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='crear.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/dependiente'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
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
