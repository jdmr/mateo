<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
    <head>
        <title><s:message code="claveEmpleado.edita.label" /></title>
    </head>
    <body>
        <jsp:include page="../menu.jsp" >
            <jsp:param name="menu" value="claveEmpleado" />
        </jsp:include>

        <div id="nuevo-nacionalidad" class="content scaffold-list" role="main">
            <h1><s:message code="claveEmpleado.edita.label" /></h1>
            <p class="well">
                <a class="btn btn-primary" href="<s:url value='/rh/claveEmpleado'/>"><i class="icon-list icon-white"></i> <s:message code='claveEmpleado.lista.label' /></a>
            </p>
            <c:url var="actualizaUrl" value="/rh/claveEmpleado/graba" />
            <form:form commandName="claveEmpleado" action="${actualizaUrl}" method="post">
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
                    <s:bind path="claveEmpleado.clave">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="clave">
                                <s:message code="claveEmpleado.nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="clave" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="clave" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="claveEmpleado.fecha">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="fecha">
                                <s:message code="fecha.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="fecha" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="fecha" cssClass="alert alert-error" />
                        </div>
                    </s:bind>
                    <s:bind path="claveEmpleado.status">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="status">
                                <s:message code="status.label" />
                                <span class="required-indicator">*</span></label>
                            <select name="status">  
                                <option value="A" selected>Activo</option>  
                                <option value="I">Inactivo</option>  
                                <option value="J">Jubilado</option>  
                            </select> 
                        </div>
                    </s:bind>
                    <s:bind path="claveEmpleado.observaciones">
                        <div class="control-group <c:if test='${not empty status.errorMessages}'>error</c:if>">
                                <label for="observaciones">
                                <s:message code="observaciones.nombre.label" />
                                <span class="required-indicator">*</span>
                            </label>
                            <form:input path="observaciones" maxlength="128" required="true" cssClass="span3" />
                            <form:errors path="observaciones" cssClass="alert alert-error" />
                        </div>
                    </s:bind>

                </fieldset>

                <p class="well" style="margin-top: 10px;">
                    <button type="submit" name="actualizarBtn" class="btn btn-primary btn-large" id="actualizar" ><i class="icon-ok icon-white"></i>&nbsp;<s:message code='actualizar.button'/></button>
                    <a class="btn btn-large" href="<s:url value='/rh/claveEmpleado/ver/${claveEmpleado.id}'/>"><i class="icon-remove"></i> <s:message code='cancelar.button' /></a>
                </p>
            </form:form>
        </div>
    <content>
        <script>
            $(document).ready(function() {
                $('input#nombre').focus();
                $("input#fecha").datepicker($.datepicker.regional['es']);
                $("input#fecha").datepicker("option", "firstDay", 0);
            });
        </script>                    
    </content>
</body>
</html>
